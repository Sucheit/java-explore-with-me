package ru.practicum.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.event.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_TIME_PATTERN;

@Value
@Builder
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    StateAction stateAction;

    String title;
}
