package ru.practicum.statsserver.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.error.BadRequestException;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.statsserver.mapper.StatisticsMapper.mapEndpointDtoToEntity;

@Primary
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsServiceImpl implements StatisticsService {

    StatisticsRepository statisticsRepository;

    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = mapEndpointDtoToEntity(endpointHitDto);
        statisticsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Start must be before end.");
        }
        if (uris == null || uris.isEmpty()) {
            return unique ?
                    statisticsRepository.findViewStatsDtoWithoutUrisAndUniqueIp(start, end) :
                    statisticsRepository.findViewStatsDtoWithoutUris(start, end);
        } else {
            return unique ?
                    statisticsRepository.findViewStatsDtoWithUrisAndUniqueIp(start, end, uris) :
                    statisticsRepository.findViewStatsDtoWithUris(start, end, uris);
        }
    }
}
