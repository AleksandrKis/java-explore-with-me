package ru.practicum.utility;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatClient;
import ru.practicum.StatResponseDto;
import ru.practicum.ViewsStatsRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.practicum.utility.Constant.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatSender {
    final StatClient statClient;
    @Value("${name.service.main}")
    String serviceName;

    public void send(HttpServletRequest request) {
        statClient.addHit(HitDto.builder()
                .app(serviceName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER)).build());
    }

    public Long getStat(HttpServletRequest request) {
        ResponseEntity<StatResponseDto[]> response = statClient.getStats(
                ViewsStatsRequest.builder().uris(List.of(request.getRequestURI())).unique(true).build());
        Long hit = 0L;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Optional<StatResponseDto> resDto = Arrays.stream(response.getBody()).findFirst();
            if (resDto.isPresent()) {
                hit = resDto.get().getHits();
            }
        }
        return hit;
    }

    public Long getCountHit(HttpServletRequest request) {
        String start = LocalDateTime.now().minusDays(100).format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().plusDays(100).format(DATE_TIME_FORMATTER);
        return statClient.getCount(start, end, request.getRequestURI());
    }
}

