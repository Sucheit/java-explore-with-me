package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationDtoById(int compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int comId);

    CompilationDto patchCompilation(int comId, UpdateCompilationRequest compilation);
}
