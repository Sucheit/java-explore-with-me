package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.Status;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Integer countByEventIdAndStatus(Integer eventId, Status status);

    List<Request> findByEventId(Integer eventId);

    List<Request> findByIdInAndEventId(List<Integer> requestIds, Integer eventId);

    List<Request> findByEventIdAndStatus(Integer eventId, Status status);

    List<Request> findByRequesterId(Integer userId);

    Optional<Request> findByEventIdAndRequesterId(Integer eventId, Integer userId);
}
