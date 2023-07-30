package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import static ru.practicum.utils.Constants.DATE_TIME_FORMATTER;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto mapRequestToDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DATE_TIME_FORMATTER))
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .build();
    }
}
