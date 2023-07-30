package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.dto.UserShortDto;

@Value
@Builder
public class EventFullDto {

    Integer id;

    String annotation;

    CategoryDto category;

    String description;

    String eventDate;

    LocationDto location;

    UserShortDto initiator;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;

    String createdOn;

    String publishedOn;

    State state;

    Integer views;

    Integer confirmedRequests;
}
