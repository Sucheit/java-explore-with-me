package ru.practicum.mainservice.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.request.model.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    Integer id;

    String created;

    Integer event;

    Integer requester;

    Status status;
}
