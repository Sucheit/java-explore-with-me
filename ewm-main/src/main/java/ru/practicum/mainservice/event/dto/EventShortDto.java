package ru.practicum.mainservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.user.dto.UserShortDto;

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

    String eventDate;

    UserShortDto initiator;

    Boolean paid;

    String title;

    Integer views;
}
