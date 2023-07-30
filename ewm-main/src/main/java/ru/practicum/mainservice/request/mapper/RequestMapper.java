package ru.practicum.mainservice.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_FORMATTER;

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
