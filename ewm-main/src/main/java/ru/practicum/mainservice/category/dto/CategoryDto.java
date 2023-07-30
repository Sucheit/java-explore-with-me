package ru.practicum.mainservice.category.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryDto {

    Integer id;

    String name;
}
