package ru.practicum.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsOut {
    String app;
    String uri;
    Long hits;

    public StatsOut(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}