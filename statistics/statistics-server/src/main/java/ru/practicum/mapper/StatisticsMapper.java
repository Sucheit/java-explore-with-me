package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.FORMATTER;

public class StatisticsMapper {

    public static EndpointHit mapEndpointDtoToEntity(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .created(LocalDateTime.parse(endpointHitDto.getTimestamp(), FORMATTER))
                .build();
    }
}
