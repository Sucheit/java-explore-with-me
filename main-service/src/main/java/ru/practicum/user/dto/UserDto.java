package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {


    String email;

    Integer id;

    String name;
}
