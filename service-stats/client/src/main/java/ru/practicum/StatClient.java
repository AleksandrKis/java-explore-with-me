package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static ru.practicum.utils.Const.DATE_TIME_FORMATTER;

@Service
public class StatClient {
    private final RestTemplate rest;
    private final String statServerUrl;

    public StatClient(@Value("${stats.server.url}") String statServerUrl) {
        this.rest = new RestTemplate();
        this.statServerUrl = statServerUrl;
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        rest.exchange(statServerUrl + "/hit", HttpMethod.POST, requestEntity, HitDto.class);
    }

    public ResponseEntity<StatResponseDto[]> getStats(ViewsStatsRequest request) {
        Map<String, Object> parameters;
        String path;
        if (request.hasUriCondition()) {
            parameters = Map.of(
                    "start", request.getStart().format(DATE_TIME_FORMATTER),
                    "end", request.getEnd().format(DATE_TIME_FORMATTER),
                    "uris", request.getUris().toArray(),
                    "unique", request.isUnique()
            );
            path = statServerUrl + "/stats/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", request.getStart().format(DATE_TIME_FORMATTER),
                    "end", request.getEnd().format(DATE_TIME_FORMATTER),
                    "unique", request.isUnique()
            );
            path = statServerUrl + "/stats/?start={start}&end={end}unique={unique}";
        }
        ResponseEntity<StatResponseDto[]> responseEntity = rest.getForEntity(path, StatResponseDto[].class, parameters);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(responseEntity.getStatusCode());
        if (responseEntity.hasBody()) {
            return bodyBuilder.body(responseEntity.getBody());
        }
        return bodyBuilder.build();
    }

    public Long getCount(String start, String end, String uri) {
        Map<String, Object> parameters;
        String path;
        parameters = Map.of(
                "start", start,
                "end", end,
                "uri", uri
        );
        path = statServerUrl + "/count/?start={start}&end={end}&uri={uri}";
        ResponseEntity<Long> countHit = rest.getForEntity(path, Long.class, parameters);
        if (countHit.getStatusCode().is2xxSuccessful()) {
            return countHit.getBody();
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(countHit.getStatusCode());
        countHit.hasBody();
        return bodyBuilder.body(-1L).getBody();
    }
}
