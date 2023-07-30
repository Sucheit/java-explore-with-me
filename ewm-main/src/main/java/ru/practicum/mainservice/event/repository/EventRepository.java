package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByInitiatorId(Integer userId, PageRequest pageRequest);

    Event findByIdAndInitiatorId(Integer userId, Integer eventId);

    Optional<Event> findByIdAndState(Integer eventId, State state);

    List<Event> findByIdIn(List<Integer> ids);
}
