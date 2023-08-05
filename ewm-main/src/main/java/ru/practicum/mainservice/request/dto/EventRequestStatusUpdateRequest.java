package ru.practicum.mainservice.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.request.model.Status;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {

    @NotNull
    List<Integer> requestIds;

    @NotNull
    Status status;
}
