package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StatClient {
    private final RestTemplate rest;
    private final String statServerUrl;

    public StatClient(@Value("${stat.server.url}") String statServerUrl) {
        this.rest = new RestTemplate();
        this.statServerUrl = statServerUrl;
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        rest.exchange(statServerUrl + "/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<Object> getStats(ViewsStatsRequest request) {
        Map<String, Object> parameters;
        String path;
        if (request.hasUriCondition()) {
            parameters = Map.of(
                    "start", request.getStart(),
                    "end", request.getEnd(),
                    "uri", request.getUris(),
                    "unique", request.isUnique()
            );
            path = statServerUrl + "/stats/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", request.getStart(),
                    "end", request.getEnd(),
                    "unique", request.isUnique()
            );
            path = statServerUrl + "/stats/?start={start}&end={end}unique={unique}";
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
