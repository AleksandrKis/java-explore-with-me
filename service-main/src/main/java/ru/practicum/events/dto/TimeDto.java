package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.utility.TimeUtil.convertString;
import static ru.practicum.utility.TimeUtil.convertTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeDto {
    String rangeStart;
    String rangeEnd;
    LocalDateTime start;
    LocalDateTime end;

    public TimeDto(String rangeStart, String rangeEnd) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;

        if (rangeStart != null) {
            this.start = convertString(rangeStart);
        } else {
            this.start = convertTime(LocalDateTime.now().minusDays(7));
        }

        if (rangeEnd != null) {
            this.end = convertString(rangeEnd);
        } else {
            this.end = convertTime(LocalDateTime.now().plusDays(7));
        }
    }
}