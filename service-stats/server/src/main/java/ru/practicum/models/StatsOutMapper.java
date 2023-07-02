package ru.practicum.models;

import ru.practicum.StatResponseDto;

public class StatsOutMapper {
    public static StatResponseDto mapToDto(StatsOut stats) {
        return StatResponseDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits()).build();
    }
}