package ru.practicum.mainservice.request.service;

import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

public interface RequestService {

    int getCountConfirmedRequest(int eventId);

    EventRequestStatusUpdateResult patchEventRequestStatus(int eventId, int limit, EventRequestStatusUpdateRequest request);

    ParticipationRequestDto createParticipationRequest(User user, Event event);

    ParticipationRequestDto cancelParticipationRequest(int requestId);

    Request getRequestById(Integer requestId);

    List<ParticipationRequestDto> getEventParticipationRequests(Event event);

    List<ParticipationRequestDto> getUserParticipationRequests(int userId);
}
