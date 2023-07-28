package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.request.model.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class EventRequestStatusUpdateRequest {

    @NotNull
    List<Integer> requestIds;

    @NotBlank
    Status status;
}
