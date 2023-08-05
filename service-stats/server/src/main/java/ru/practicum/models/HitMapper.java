package ru.practicum.models;

import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constant.DATE_TIME_FORMATTER;

public class HitMapper {
    public static Hit mapToHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DATE_TIME_FORMATTER));
        return hit;
    }

    public static StatResponseDto mapToResponseDto(StatsOut stats) {
        return StatResponseDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits()).build();
    }
}