package ru.practicum.mainservice.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mainservice.admin.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.error.exception.BadRequestException;
import ru.practicum.mainservice.error.exception.ConflictException;
import ru.practicum.mainservice.error.exception.InternalServerErrorException;
import ru.practicum.mainservice.error.exception.NotFoundException;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.QEvent;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.repository.LocationRepository;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.statsclient.StatisticsClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.event.mapper.EventMapper.*;
import static ru.practicum.mainservice.utils.Constants.*;
import static ru.practicum.mainservice.utils.Utility.getPageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;

    LocationRepository locationRepository;

    RequestService requestService;

    CategoryService categoryService;

    StatisticsClient statisticsClient;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> searchEventsByUser(String text, List<Integer> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable, String sort, int from, int size) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Start date is after end date.");
            }
        }
        BooleanBuilder builder = new BooleanBuilder();
        if (text != null) {
            builder.and(QEvent.event.annotation.likeIgnoreCase("%" + text.toLowerCase() + "%"))
                    .or(QEvent.event.title.likeIgnoreCase("%" + text.toLowerCase() + "%"));
        }
        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            builder.and(QEvent.event.paid.eq(paid));
        }
        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }

        BooleanExpression expression = builder.getValue() == null ?
                QEvent.event.isNotNull() : Expressions.asBoolean(builder.getValue());
        Page<Event> eventsPage = eventRepository.findAll(expression, getPageRequest(from, size));
        List<EventFullDto> eventFullDto = getEventFullDto(eventsPage.toList());

        if (onlyAvailable) {
            eventFullDto = eventFullDto.stream()
                    .filter(i -> i.getConfirmedRequests() <= i.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        if (sort != null && sort.equals("EVENT_DATE")) {
            eventFullDto.sort(Comparator.comparing(EventFullDto::getEventDate));
        } else {
            eventFullDto.sort(Comparator.comparing(EventFullDto::getViews));
        }

        if (sort != null && sort.equals("EVENT_DATE")) {
            eventFullDto.sort(Comparator.comparing(EventFullDto::getEventDate));
        } else {
            eventFullDto.sort(Comparator.comparing(EventFullDto::getViews));
        }

        return eventFullDto.stream().map(EventMapper::mapEventFullDtoToEventShortDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private List<EventFullDto> getEventFullDto(List<Event> events) {
        Map<Integer, Integer> viewsStatistics = getViewsStatistics(events.stream()
                .map(Event::getId)
                .collect(Collectors.toList()));
        return events.stream()
                .map(event -> mapEventToEventFullDto(
                        event,
                        requestService.getCountConfirmedRequest(event.getId()),
                        viewsStatistics.getOrDefault(event.getId(), 0)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Map<Integer, Integer> getViewsStatistics(List<Integer> eventIds) {
        List<String> uris = eventIds.stream()
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());
        log.info("GET /stats request: uris={}", uris);
        ResponseEntity<Object> response = statisticsClient.getStatistic(
                START_DATE.format(DATE_TIME_FORMATTER), END_DATE.format(DATE_TIME_FORMATTER), uris, true);
        Gson gson = new Gson();
        ViewStatsDto[] viewStatsDtos;
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = gson.toJson(response.getBody());
        try {
            viewStatsDtos = objectMapper.readValue(responseJson, ViewStatsDto[].class);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Internal Server Error: unable to read statistics server data");
        }
        Map<Integer, Integer> viewsMap = new HashMap<>();
        for (ViewStatsDto viewStatsDto : viewStatsDtos) {
            String[] lines = viewStatsDto.getUri().split("/");
            int a = Integer.parseInt(lines[2]);
            viewsMap.put(a, viewStatsDto.getHits().intValue());
        }
        return viewsMap;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto findEventFullDtoById(int eventId) {
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        return getEventFullDto(List.of(event)).get(0);
    }

    @Transactional
    @Override
    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId)));
    }

    @Transactional
    @Override
    public EventFullDto patchEventByUser(int eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = getEventById(eventId);
        patchEventFields(event, updateEventUserRequest);
        event = eventRepository.save(event);
        return getEventFullDto(List.of(event)).get(0);
    }

    private void patchEventFields(Event event, UpdateEventUserRequest updateEventUserRequest) {
        String annotation = updateEventUserRequest.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        Integer category = updateEventUserRequest.getCategory();
        if (category != null) {
            event.setCategory(categoryService.findCategoryById(category));
        }
        String description = updateEventUserRequest.getDescription();
        if (description != null) {
            event.setDescription(description);
        }
        LocalDateTime eventDate = updateEventUserRequest.getEventDate();
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date is in the past");
            }
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        LocationDto location = updateEventUserRequest.getLocation();
        if (location != null) {
            event.setLocation(locationRepository.save(mapLocationDtoToLocation(location)));
        }
        Boolean paid = updateEventUserRequest.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }
        Integer participantLimit = updateEventUserRequest.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        Boolean requestModeration = updateEventUserRequest.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        StateAction stateAction = updateEventUserRequest.getStateAction();
        if (stateAction != null) {
            event.setState(stateAction == StateAction.CANCEL_REVIEW ? State.CANCELED : State.PENDING);
        }
        String title = updateEventUserRequest.getTitle();
        if (title != null) {
            event.setTitle(title);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression;

        if (users != null) builder.and(QEvent.event.initiator.id.in(users));
        if (states != null) {
            for (String state : states) {
                try {
                    State.valueOf(state);
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException(String.format("Incorrect parameter state: %s", state));
                }
            }
            builder.and(QEvent.event.state.in(states.stream().map(State::valueOf).collect(Collectors.toList())));
        }
        if (categories != null) {
            builder.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            builder.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            builder.and(QEvent.event.eventDate.before(rangeEnd));
        }
        if (builder.getValue() == null) {
            expression = QEvent.event.isNotNull();
        } else {
            expression = Expressions.asBoolean(builder.getValue());
        }
        Page<Event> events = eventRepository.findAll(expression, getPageRequest(from, size));
        return getEventFullDto(events.toList());
    }

    @Transactional
    @Override
    public EventFullDto patchEventByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEventById(eventId);
        patchEventFields(event, updateEventAdminRequest);
        event = eventRepository.save(event);
        return getEventFullDto(List.of(event)).get(0);
    }

    private void patchEventFields(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (event.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("");
        }
        event.setPublishedOn(LocalDateTime.now());
        String annotation = updateEventAdminRequest.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        Integer category = updateEventAdminRequest.getCategory();
        if (category != null) {
            event.setCategory(categoryService.findCategoryById(category));
        }
        String description = updateEventAdminRequest.getDescription();
        if (description != null) {
            event.setDescription(description);
        }
        LocalDateTime eventDate = updateEventAdminRequest.getEventDate();
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date is in the past");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        LocationDto location = updateEventAdminRequest.getLocation();
        if (location != null) {
            event.setLocation(locationRepository.save(mapLocationDtoToLocation(location)));
        }
        Boolean paid = updateEventAdminRequest.getPaid();
        if (paid != null) {
            event.setPaid(paid);
        }
        Integer participantLimit = updateEventAdminRequest.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        Boolean requestModeration = updateEventAdminRequest.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        ru.practicum.mainservice.admin.dto.StateAction stateAction = updateEventAdminRequest.getStateAction();
        if (stateAction != null) {
            if (stateAction.equals(ru.practicum.mainservice.admin.dto.StateAction.PUBLISH_EVENT)) {
                if (!State.PENDING.equals(event.getState())) {
                    throw new ConflictException("Event must have state: PENDING");
                }
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (stateAction.equals(ru.practicum.mainservice.admin.dto.StateAction.REJECT_EVENT)) {
                if (State.PUBLISHED.equals(event.getState())) {
                    throw new ConflictException("Event is already has State: PUBLISHED");
                }
                event.setState(State.CANCELED);
            }
        }
        String title = updateEventAdminRequest.getTitle();
        if (title != null) {
            event.setTitle(title);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventByUserId(int userId, int from, int size) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, getPageRequest(from, size));
        return getEventFullDto(events)
                .stream()
                .map(EventMapper::mapEventFullDtoToEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto createEventByUser(User user, NewEventDto newEventDto) {
        Category category = categoryService.findCategoryById(newEventDto.getCategory());
        Location location = createLocation(newEventDto.getLocation());
        Event event = eventRepository.save(mapToEvent(category, user, location, newEventDto));
        return EventMapper.mapEventToEventFullDto(event, 0, 0);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByUser(int userId, int eventId) {
        return getEventFullDto(List.of(eventRepository.findByIdAndInitiatorId(eventId, userId))).get(0);
    }

    @Transactional
    private Location createLocation(LocationDto locationDto) {
        return locationRepository.save(mapLocationDtoToLocation(locationDto));
    }
}
