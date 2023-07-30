package ru.practicum.user.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

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
}
