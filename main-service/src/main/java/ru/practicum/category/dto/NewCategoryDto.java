package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class NewCategoryDto {

    @NotBlank
    @Size(min = 1, max = 50)
    String name;
}
