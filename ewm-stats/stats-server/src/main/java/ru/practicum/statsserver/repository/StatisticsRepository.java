package ru.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.statsserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.created BETWEEN :start AND :end " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh) DESC")
    List<ViewStatsDto> findViewStatsDtoWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.created BETWEEN :start AND :end " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStatsDto> findViewStatsDtoWithoutUrisAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.created BETWEEN :start AND :end AND eh.uri IN (:uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(eh) DESC")
    List<ViewStatsDto> findViewStatsDtoWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
            "FROM EndpointHit eh " +
            "WHERE eh.created BETWEEN :start AND :end AND eh.uri IN (:uris) " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY COUNT(DISTINCT eh.ip) DESC")
    List<ViewStatsDto> findViewStatsDtoWithUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
