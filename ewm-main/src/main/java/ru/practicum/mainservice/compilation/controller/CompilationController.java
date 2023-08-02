package ru.practicum.mainservice.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
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
