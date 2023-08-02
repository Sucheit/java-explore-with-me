package ru.practicum.mainservice.admin.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.admin.service.AdminService;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminCompilationsController {

    AdminService adminService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(
            @RequestBody @Valid NewCompilationDto newCompilationDto
    ) {
        log.info("POST /admin/compilations request: {}", newCompilationDto);
        CompilationDto compilationDto = adminService.createCompilation(newCompilationDto);
        log.info("POST /admin/compilations completed: {}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(
            @PathVariable int compId
    ) {
        log.info("DELETE /admin/compilations/{compId} request: comId={}", compId);
        adminService.deleteCompilation(compId);
        log.info("DELETE /admin/compilations/{compId} completed: comId={}", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(
            @PathVariable int compId,
            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest
    ) {
        log.info("PATCH /admin/compilations/{compId} request: compId={}, {}", compId, updateCompilationRequest);
        CompilationDto compilationDto = adminService.patchCompilation(compId, updateCompilationRequest);
        log.info("PATCH /admin/compilations/{compId} completed: {}", compilationDto);
        return compilationDto;
    }
}