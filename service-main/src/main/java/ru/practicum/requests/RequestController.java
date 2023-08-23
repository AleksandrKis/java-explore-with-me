package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipantRequestDto;
import ru.practicum.requests.dto.RequestReviewDto;
import ru.practicum.requests.dto.ResultRequestsReviewDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestController {
    private static final String CANCEL_REQUEST = "/requests/{requestId}/cancel";
    private static final String EVENT_REQUEST = "/events/{eventId}/requests";
    private static final String REQUEST = "/requests";
    private static final String ANSWER = "RequestController: received request for ";
    private final RequestService service;

    @PostMapping(REQUEST)
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantRequestDto create(@PathVariable long userId, @RequestParam long eventId) {
        log.info(ANSWER + "create eventRequest form user by ID:" + userId);
        return service.createRequest(userId, eventId);
    }

    @GetMapping(REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getAllByOwner(@PathVariable long userId) {
        log.info(ANSWER + "get all eventRequests by current user ID:" + userId);
        return service.getAllByOwnerRequests(userId);
    }

    @PatchMapping(CANCEL_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public ParticipantRequestDto cancelRequest(@PathVariable long requestId, @PathVariable long userId) {
        log.info(ANSWER + "cancel eventRequest ID:{} by user ID:{}", requestId, userId);
        return service.canselRequest(userId, requestId);
    }

    @GetMapping(EVENT_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipantRequestDto> getALLByEventOwner(@PathVariable long eventId, @PathVariable long userId) {
        log.info(ANSWER + "get all eventRequests for event ID:{} by owner ID:{}", eventId, userId);
        return service.getAllByOwnerEvent(userId, eventId);
    }

    @PatchMapping(EVENT_REQUEST)
    @ResponseStatus(HttpStatus.OK)
    public ResultRequestsReviewDto updateRequestsStatus(@PathVariable long userId, @PathVariable long eventId,
                                                        @RequestBody RequestReviewDto reviewDto) {
        log.info(ANSWER + "update status for eventRequests by owner event ID:{}", eventId);
        return service.changeStatusRequests(userId, eventId, reviewDto);
    }
}
