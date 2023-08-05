package ru.practicum.mainservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.user.dto.UserShortDto;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {

    Integer id;

    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    String eventDate;

    UserShortDto initiator;

    Boolean paid;

    String title;

    Integer views;
}
