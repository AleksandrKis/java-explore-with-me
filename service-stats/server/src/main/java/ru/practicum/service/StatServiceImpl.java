package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;
import ru.practicum.storage.HitRepository;

import java.util.List;

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
        if (uniques) {
            return repoHit.getUniqueByTimePeriod(convertString(start), convertString(end), uris);
        } else {
            return repoHit.getAllByTimePeriod(convertString(start), convertString(end), uris);
        }
    }
}