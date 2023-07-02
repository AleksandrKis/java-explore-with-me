package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;
import ru.practicum.storage.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.models.HitMapper.mapToHit;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final HitRepository repoHit;

    @Override
    public void addHit(HitDto hitDto) {
        repoHit.save(mapToHit(hitDto));
    }

    @Override
    public List<StatResponseDto> getStats(String start, String end, List<String> uris, Boolean uniques) {
        validTime(start, end);
        if (uniques) {
            return repoHit.getUniqueByTimePeriod(convertString(start), convertString(end), uris);
        } else {
            return repoHit.getAllByTimePeriod(convertString(start), convertString(end), uris);
        }
    }

    private LocalDateTime convertString(String time) {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }

    private void validTime(String start, String end) {
        if (convertString(start).isAfter(convertString(end))) {
            throw new RuntimeException("Wrong Time period");
        }
    }
}
