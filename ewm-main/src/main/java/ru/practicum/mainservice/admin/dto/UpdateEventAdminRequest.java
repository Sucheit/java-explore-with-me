package ru.practicum.mainservice.admin.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.event.dto.LocationDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000)
    String description;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    StateAction stateAction;

    String title;
}
