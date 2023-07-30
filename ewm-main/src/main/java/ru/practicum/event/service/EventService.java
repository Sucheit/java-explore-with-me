package ru.practicum.event.service;

import ru.practicum.admin.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> searchEventsByUser(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from,
                                           int size);

    EventFullDto findEventFullDtoById(int eventId);

    List<EventFullDto> getEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto patchEventByAdmin(int eventId, UpdateEventAdminRequest event);

    List<EventShortDto> getEventByUserId(int userId, int from, int size);

    EventFullDto createEventByUser(User user, NewEventDto newEventDto);

    EventFullDto getEventByUser(int userId, int eventId);

    Event getEventById(int eventId);

    EventFullDto patchEventByUser(int eventId, UpdateEventUserRequest updateEventUserRequest);
}
