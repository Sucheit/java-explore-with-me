package ru.practicum.mainservice.compilation.service;

import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationDtoById(int compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int comId);

    CompilationDto patchCompilation(int comId, UpdateCompilationRequest compilation);
}
