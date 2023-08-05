package ru.practicum.mainservice.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Integer id;

    Boolean pinned;

    String title;

    List<EventShortDto> events;
}
