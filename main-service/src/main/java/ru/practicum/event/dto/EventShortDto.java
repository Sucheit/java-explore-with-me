package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

@Value
@Builder
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
