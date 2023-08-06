package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

public interface UserService {

    User getUserById(int userId);

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    void deleteUser(int userId);

    List<EventShortDto> getEventsByUserId(int userId, int from, int size);

    EventFullDto createEventByUser(int userId, NewEventDto newEventDto);

    EventFullDto getEventByUser(int userId, int eventId);

    EventFullDto patchEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRequestStatusUpdateResult patchEventByUser(int userId, int eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId);

    List<ParticipationRequestDto> getParticipationRequests(int userId);

    ParticipationRequestDto createParticipationRequest(int userId, int eventId);

    ParticipationRequestDto cancelParticipationRequest(int userId, int requestId);

    List<EventShortDto> getSubscriptionsEvents(int userId, int from, int size);

    List<UserDto> createSubscription(int userId, int subscribedToId, int from, int size);

    List<UserDto> getSubscriptions(int userId, int from, int size);

    void deleteSubscription(int userId, int subscribedToId);
}
