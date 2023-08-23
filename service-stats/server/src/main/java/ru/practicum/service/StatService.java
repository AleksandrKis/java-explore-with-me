package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;

import java.util.List;

public interface StatService {
    void addHit(HitDto hitDto);

    List<StatResponseDto> getStats(String start, String end, List<String> uris, Boolean uniques);

    Long getCount(String start, String end, String uri);
}