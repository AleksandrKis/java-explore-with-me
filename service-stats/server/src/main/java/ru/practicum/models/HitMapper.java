package ru.practicum.models;

import ru.practicum.HitDto;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constant.DATE_TIME_FORMATTER;

public class HitMapper {
    public static Hit mapToHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
//        hit.setTimestamp(hitDto.getTimestamp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DATE_TIME_FORMATTER));
        return hit;
    }
}