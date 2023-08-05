package ru.practicum.mainservice.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto mapToCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(eventShortDtoList)
                .pinned(compilation.getPinned())
                .build();
    }

    public static Compilation mapToCompilation(UpdateCompilationRequest updateCompilationRequest, List<Event> events) {
        return Compilation.builder()
                .pinned(updateCompilationRequest.getPinned())
                .title(updateCompilationRequest.getTitle())
                .events(events)
                .build();
    }

    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .events(events)
                .build();
    }


}
