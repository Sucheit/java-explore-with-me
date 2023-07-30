package ru.practicum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
