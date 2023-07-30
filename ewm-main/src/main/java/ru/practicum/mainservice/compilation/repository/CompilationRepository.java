package ru.practicum.mainservice.compilation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findByPinned(Boolean pinned, PageRequest pageRequest);
}
