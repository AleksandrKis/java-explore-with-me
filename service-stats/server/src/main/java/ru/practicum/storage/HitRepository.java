package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatResponseDto;
import ru.practicum.models.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.StatResponseDto(h.app, h.uri, count (h.ip))" +
            " from Hit h where h.timestamp between ?1 and ?2 and (h.uri in ?3 or ?3 = null)" +
            " group by h.app, h.uri order by count(h.ip) desc ")
    List<StatResponseDto> getAllByTimePeriod(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.StatResponseDto(h.app, h.uri, count (distinct h.ip))" +
            " from Hit h where h.timestamp between ?1 and ?2 and (h.uri in ?3 or ?3 = null)" +
            " group by h.app, h.uri order by count(distinct h.ip) desc ")
    List<StatResponseDto> getUniqueByTimePeriod(LocalDateTime start, LocalDateTime end, List<String> uris);
}