package ru.practicum.request.service;

import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

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
