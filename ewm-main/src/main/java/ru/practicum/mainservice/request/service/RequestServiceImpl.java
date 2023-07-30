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
        return requestRepository.countByEventAndStatus(eventId, Status.CONFIRMED);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult patchEventRequestStatus(
            int eventId, int participantLimit, EventRequestStatusUpdateRequest updateRequest) {
        List<Request> requests = requestRepository
                .findByIdInAndEventId(updateRequest.getRequestIds(), eventId);
        List<Request> requestsByEvent = requestRepository.findByEventId(eventId);
        AtomicInteger countConfirm = new AtomicInteger();
        requestsByEvent.forEach(request -> {
            if (request.getStatus() == Status.CONFIRMED) {
                countConfirm.set(countConfirm.get() + 1);
            }
        });
        for (Request request : requests) {
            if (!request.getStatus().equals(Status.PENDING))
                throw new ConflictException(String.format("Request id=%s status is not PENDING.", request.getId()));
            if (participantLimit > countConfirm.get()) {
                request.setStatus(updateRequest.getStatus());
                countConfirm.set(request.getStatus() == Status.CONFIRMED ? countConfirm.get() + 1 : countConfirm.get());
                requestRepository.save(request);
            } else {
                List<Request> pendingRequests = requestRepository.findByEventIdAndStatus(eventId, Status.PENDING);
                pendingRequests.forEach(pendingRequest -> {
                    pendingRequest.setStatus(Status.REJECTED);
                    requestRepository.save(pendingRequest);
                });
                throw new ConflictException("The participant limit has been reached");
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
        Request request = requestRepository.findByEventIdAndRequesterId(event.getId(), user.getId())
                .orElseThrow(() -> new ConflictException("Request already created."));
        if (request.getRequester().getId().equals(user.getId())) {
            throw new ConflictException("Event initiator cannot create participation request.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event is not published.");
        }
        int limit = event.getParticipantLimit();
        if (event.getRequestModeration()
                && limit != 0
                && limit <= requestRepository.findByEventIdAndStatus(event.getId(), Status.CONFIRMED).size()) {
            throw new ConflictException("Confirmed participation limit reached.");
        }
        Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
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
