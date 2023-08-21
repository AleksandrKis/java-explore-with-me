package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

import static ru.practicum.utils.Const.DATE_TIME_FORMATTER;

@Getter
@ToString
@Builder(toBuilder = true)
public class ViewsStatsRequest {
    @Singular("uri")
    private Set<String> uris;

    @Builder.Default
    private String start = LocalDateTime.now().withHour(0).withMinute(0).format(DATE_TIME_FORMATTER);

    @Builder.Default
    private String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);

    private boolean unique;

    private String application;

    public boolean hasUriCondition() {
        return uris != null && !uris.isEmpty();
    }
}