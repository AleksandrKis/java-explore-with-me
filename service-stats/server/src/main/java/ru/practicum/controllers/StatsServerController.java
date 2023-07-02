package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.StatResponseDto;
import ru.practicum.service.StatService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsServerController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody HitDto hitDto) {
        log.info("StatServer record stat: {}", hitDto);
        service.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatResponseDto> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("StatServer give out list stats.");
        return service.getStats(start, end, uris, unique);
    }
}
