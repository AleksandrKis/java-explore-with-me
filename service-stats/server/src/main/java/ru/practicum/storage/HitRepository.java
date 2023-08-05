package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Hit;
import ru.practicum.models.StatsOut;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT COUNT (DISTINCT h.ip) FROM Hit h WHERE h.uri = ?3 AND h.timestamp BETWEEN ?1 AND ?2")
    Long countUniqueIp(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select new ru.practicum.models.StatsOut(h.app, h.uri, count (h.ip))" +
            " from Hit h where h.timestamp between ?1 and ?2 and (h.uri in ?3 or ?3 = null)" +
            " group by h.app, h.uri order by count(h.ip) desc ")
    List<StatsOut> getAllByTimePeriod(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.models.StatsOut(h.app, h.uri, count (distinct h.ip))" +
            " from Hit h where h.timestamp between ?1 and ?2 and (h.uri in ?3 or ?3 = null)" +
            " group by h.app, h.uri order by count(distinct h.ip) desc ")
    List<StatsOut> getUniqueByTimePeriod(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.models.StatsOut(h.app, h.uri, count (distinct h.ip))" +
            " from Hit h where h.uri in ?3" +
            " group by h.app, h.uri order by count(distinct h.ip) desc")
    List<StatsOut> getUniqueTestPeriod(List<String> uris);
}