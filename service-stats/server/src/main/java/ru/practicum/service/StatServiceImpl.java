package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;
import ru.practicum.models.HitMapper;
import ru.practicum.models.StatsOut;
import ru.practicum.storage.HitRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.models.HitMapper.mapToHit;
import static ru.practicum.utils.DateUtil.convertString;
import static ru.practicum.utils.DateUtil.validTime;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final HitRepository repoHit;

    @Override
    public void addHit(HitDto hitDto) {
        repoHit.save(mapToHit(hitDto));
    }

    @Override
    public List<StatResponseDto> getStats(String start, String end, List<String> uris, Boolean uniques) {
        validTime(start, end);
        List<StatsOut> resultStat;
        if (uniques) {
            resultStat = repoHit.getUniqueByTimePeriod(convertString(start), convertString(end), uris);
        } else {
            resultStat = repoHit.getAllByTimePeriod(convertString(start), convertString(end), uris);
        }
        return resultStat.isEmpty() ? Collections.emptyList() : resultStat
                .stream()
                .map(HitMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getCount(String start, String end, String uri) {
        return repoHit.countUniqueIp(convertString(start), convertString(end), uri);
    }
}
