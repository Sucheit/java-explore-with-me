package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

@Value
@Builder
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;

    @NotNull
    Integer category;

    @NotBlank
    @Size(min = 20, max = 7000)
    String description;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime eventDate;

    @NotNull
    @Valid
    LocationDto location;

    Boolean paid = false;

    Integer participantLimit = 0;

    Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    String title;
}
