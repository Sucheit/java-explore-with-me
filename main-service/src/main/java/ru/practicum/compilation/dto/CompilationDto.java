package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Value
@Builder
public class CompilationDto {

    Integer id;

    Boolean pinned;

    String title;

    List<EventShortDto> events;
}
