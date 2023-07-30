package ru.practicum.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationController {

    CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(defaultValue = "false") boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /compilations request: pinned={}, from={}, size={}", pinned, from, size);
        List<CompilationDto> compilationDtoList = compilationService.getCompilations(pinned, from, size);
        log.info("GET /compilations completed: {}", compilationDtoList);
        return compilationDtoList;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationsById(
            @PathVariable int compId
    ) {
        log.info("GET /compilations/{compId} request: Id={}", compId);
        CompilationDto compilationDto = compilationService.getCompilationDtoById(compId);
        log.info("GET /compilation/{compId} completed: {}", compilationDto);
        return compilationDto;
    }
}
