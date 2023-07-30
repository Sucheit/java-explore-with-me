package ru.practicum.mainservice.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.LocationDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.mainservice.category.mapper.CategoryMapper.mapCategoryToDto;
import static ru.practicum.mainservice.user.mapper.UserMapper.mapUserToUserShortDto;
import static ru.practicum.mainservice.utils.Constants.DATE_TIME_FORMATTER;

@UtilityClass
public class EventMapper {

    public static EventFullDto mapEventToEventFullDto(Event event, Integer countConfirmedRequest, Integer views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapCategoryToDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .location(mapLocationToLocationDto(event.getLocation()))
                .initiator(mapUserToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .createdOn(event.getCreatedOn().format(DATE_TIME_FORMATTER))
                .publishedOn(event.getPublishedOn() == null ? "" : event.getPublishedOn().format(DATE_TIME_FORMATTER))
                .state(event.getState())
                .views(views)
                .confirmedRequests(countConfirmedRequest)
                .build();
    }

    public static EventShortDto mapEventFullDtoToEventShortDto(EventFullDto event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static EventShortDto mapEventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapCategoryToDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(mapUserToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event mapToEvent(Category category, User user, Location location, NewEventDto newEventDto) {
        return Event.builder()
                .initiator(user)
                .category(category)
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(location)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .state(State.PENDING)
                .build();
    }

    public static LocationDto mapLocationToLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLat())
                .build();
    }

    public static Location mapLocationDtoToLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLat())
                .build();
    }
}
