package ru.practicum.mainservice.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.error.exception.BadRequestException;
import ru.practicum.mainservice.error.exception.ConflictException;
import ru.practicum.mainservice.error.exception.NotFoundException;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.service.RequestService;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.user.mapper.UserMapper.mapNewUserRequestToUser;
import static ru.practicum.mainservice.user.mapper.UserMapper.mapUserToUserDto;
import static ru.practicum.mainservice.utils.Utility.getPageRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    EventService eventService;

    RequestService requestService;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return mapUserToUserDto(userRepository.save(mapNewUserRequestToUser(newUserRequest)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return ids == null ?
                userRepository.findAll(getPageRequest(from, size))
                        .map(UserMapper::mapUserToUserDto)
                        .getContent() :
                userRepository.findByIdIn(ids, getPageRequest(from, size))
                        .stream()
                        .map(UserMapper::mapUserToUserDto)
                        .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        userRepository.delete(getUserById(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventsByUserId(int userId, int from, int size) {
        getUserById(userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @Transactional
    @Override
    public EventFullDto createEventByUser(int userId, NewEventDto newEventDto) {
        return eventService.createEventByUser(getUserById(userId), newEventDto);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByUser(int userId, int eventId) {
        return eventService.getEventByUser(userId, eventId);
    }

    @Transactional
    @Override
    public EventFullDto patchEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        getUserById(userId);
        Event event = eventService.getEventById(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event id=%s is already published.", eventId));
        }
        return eventService.patchEventByUser(eventId, updateEventUserRequest);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchEventByUser(
            int userId, int eventId, EventRequestStatusUpdateRequest request) {
        getUserById(userId);
        int participantLimit = eventService.getEventById(eventId).getParticipantLimit();
        return requestService.patchEventRequestStatus(eventId, participantLimit, request);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getParticipationRequests(int userId, int eventId) {
        getUserById(userId);
        return requestService.getEventParticipationRequests(eventService.getEventById(eventId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getParticipationRequests(int userId) {
        getUserById(userId);
        return requestService.getUserParticipationRequests(userId);
    }

    @Transactional
    @Override
    public ParticipationRequestDto createParticipationRequest(int userId, int eventId) {
        User user = getUserById(userId);
        return requestService.createParticipationRequest(user, eventService.getEventById(eventId));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) {
        getUserById(userId);
        return requestService.cancelParticipationRequest(requestId);
    }

    @Override
    public List<EventShortDto> getSubscriptionsEvents(int userId, int from, int size) {
        User user = getUserById(userId);
        List<User> subscribedTo = user.getSubscriptions();
        List<Integer> subscribedToIds = subscribedTo.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return eventService.getSubscribedToEvents(subscribedToIds, from, size);
    }

    @Override
    public List<UserDto> createSubscription(int userId, int subscribedToId, int from, int size) {
        User user = getUserById(userId);
        User subscribedTo = getUserById(subscribedToId);
        List<User> subscribedToList = user.getSubscriptions();
        if (subscribedToList.stream()
                .anyMatch(u -> u.getId() == subscribedToId)) {
            throw new BadRequestException(String.format("User id=%s is already subscribed to user id=%s.", userId, subscribedToId));
        }
        subscribedToList.add(subscribedTo);
        user.setSubscriptions(subscribedToList);
        userRepository.save(user);
        return getSubscriptions(userId, from, size);
    }

    @Override
    public List<UserDto> getSubscriptions(int userId, int from, int size) {
        User user = getUserById(userId);
        List<Integer> subscribedToIds = user.getSubscriptions().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return getUsers(subscribedToIds, from, size);
    }

    @Override
    public void deleteSubscription(int userId, int subscribedToId) {
        User user = getUserById(userId);
        User subscribedTo = getUserById(subscribedToId);
        List<User> subscribedToList = user.getSubscriptions();
        if (!subscribedToList.remove(subscribedTo)) {
            throw new BadRequestException(String.format("User id=%s is not subscribed to user id=%s.", userId, subscribedToId));
        }
        user.setSubscriptions(subscribedToList);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%s was not found", userId)));
    }
}
