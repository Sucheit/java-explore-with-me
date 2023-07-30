package ru.practicum.mainservice.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByIdIn(List<Integer> ids, PageRequest pageRequest);
}
