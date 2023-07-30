package ru.practicum.mainservice.event.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationDto {

    Float lat;

    Float lon;
}
