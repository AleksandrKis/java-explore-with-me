package ru.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StatsOut {
    String app;
    String uri;
    Long hits;
}
