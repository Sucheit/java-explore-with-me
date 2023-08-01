package ru.practicum.statsserver.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.statsserver.model.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.statsserver.utils.Constants.DATE_TIME_FORMATTER;

@UtilityClass
public class StatisticsMapper {

    public static EndpointHit mapEndpointDtoToEntity(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .created(LocalDateTime.parse(endpointHitDto.getTimestamp(), DATE_TIME_FORMATTER))
                .build();
    }
}
