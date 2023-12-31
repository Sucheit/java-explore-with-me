package ru.practicum.mainservice.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.error.exception.ConflictException;
import ru.practicum.mainservice.error.exception.NotFoundException;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.request.mapper.RequestMapper.mapRequestToDto;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;

    @Transactional(readOnly = true)
    @Override
    public int getCountConfirmedRequest(int eventId) {
        return requestRepository.countByEventIdAndStatus(eventId, Status.CONFIRMED);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchEventRequestStatus(
            int eventId, int participantLimit, EventRequestStatusUpdateRequest updateRequest) {
        AtomicInteger countConfirmed = new AtomicInteger(
                requestRepository.findByEventIdAndStatus(eventId, Status.CONFIRMED).size());
        if (countConfirmed.intValue() == participantLimit) {
            throw new ConflictException("Participation limit has been reached");
        }
        List<Request> requests = requestRepository
                .findByIdInAndEventId(updateRequest.getRequestIds(), eventId);
        for (Request request : requests) {
            if (!request.getStatus().equals(Status.PENDING))
                throw new ConflictException(String.format("Request id=%s status is not PENDING.", request.getId()));
            if (participantLimit > countConfirmed.get() || participantLimit == 0) {
                request.setStatus(updateRequest.getStatus());
                countConfirmed.set(request.getStatus() == Status.CONFIRMED ? countConfirmed.get() + 1 : countConfirmed.get());
                requestRepository.save(request);
            } else {
                List<Request> pendingRequests = requestRepository.findByEventIdAndStatus(eventId, Status.PENDING);
                pendingRequests.forEach(pendingRequest -> {
                    pendingRequest.setStatus(Status.REJECTED);
                    requestRepository.save(pendingRequest);
                });
            }
        }

        List<Request> confirmed = requestRepository.findByEventIdAndStatus(eventId, Status.CONFIRMED);
        List<Request> rejected = requestRepository.findByEventIdAndStatus(eventId, Status.REJECTED);

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed.stream()
                        .map(RequestMapper::mapRequestToDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(rejected.stream()
                        .map(RequestMapper::mapRequestToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public ParticipationRequestDto createParticipationRequest(User user, Event event) {
        if (requestRepository.findByEventIdAndRequesterId(event.getId(), user.getId()).isPresent()) {
            throw new ConflictException(String.format("User id=%s already sent request to participated in event id%s", user.getId(), event.getId()));
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Event initiator cannot create participation request.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event is not published.");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0 && limit <= requestRepository.findByEventIdAndStatus(event.getId(), Status.CONFIRMED).size()) {
            throw new ConflictException("Confirmed participation limit has been reached.");
        }
        Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status((!event.getRequestModeration() || limit == 0) ? Status.CONFIRMED : Status.PENDING)
                .build();
        return mapRequestToDto(requestRepository.save(newRequest));
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(int requestId) {
        Request canceldRequest = getRequestById(requestId);
        canceldRequest.setStatus(Status.CANCELED);
        return mapRequestToDto(requestRepository.save(canceldRequest));
    }

    @Override
    public Request getRequestById(Integer requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%s was not found", requestId)));
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipationRequests(Event event) {
        return requestRepository.findByEventId(event.getId()).stream()
                .map(RequestMapper::mapRequestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipationRequests(int userId) {
        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::mapRequestToDto)
                .collect(Collectors.toList());
    }
}
