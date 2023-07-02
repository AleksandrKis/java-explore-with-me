package ru.practicum;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StatClient {
    private final RestTemplate rest;
    private static final String STAT_SERVER_URL = "http://localhost:9090";

    public StatClient() {
        this.rest = new RestTemplate();
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        rest.exchange(STAT_SERVER_URL + "/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(String start, String end, String uri, boolean unique) {
        Map<String, Object> parameters;
        String path;
        if (uri != null) {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uri", uri,
                    "unique", unique
            );
            path = STAT_SERVER_URL + "/stats/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique
            );
            path = STAT_SERVER_URL + "/stats/?start={start}&end={end}unique={unique}";
        }
        ResponseEntity<Object> responseEntity = rest.getForEntity(path, Object.class, parameters);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity;
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(responseEntity.getStatusCode());
        if (responseEntity.hasBody()) {
            return bodyBuilder.body(responseEntity.getBody());
        }
        return bodyBuilder.build();
    }
}
