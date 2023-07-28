package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserShortDto {

    Integer id;

    String name;
}
