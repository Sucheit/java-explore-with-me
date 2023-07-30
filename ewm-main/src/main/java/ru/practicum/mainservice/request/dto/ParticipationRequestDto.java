package ru.practicum.mainservice.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.mainservice.request.model.Status;

@Value
@Builder
public class ParticipationRequestDto {

    Integer id;

    String created;

    Integer event;

    Integer requester;

    Status status;
}
