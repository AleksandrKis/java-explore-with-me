package ru.practicum.ratings;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ratings.dto.RateInputDto;
import ru.practicum.ratings.dto.RateMessageDto;
import ru.practicum.ratings.dto.RatingInitiatorsDto;
import ru.practicum.ratings.dto.ResponseDto;
import ru.practicum.ratings.service.RatingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateController {
    final RatingService service;
    static final String ANSWER = "RateController: received request for ";

    @PostMapping("/rate/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto giveRateToEvent(@PathVariable long userId,
                                      @PathVariable long eventId,
                                      @Valid @RequestBody RateInputDto rateInputDto) {
        log.info(ANSWER + "Hit rate for event by ID:{}, from user by ID:{}, rate by {}", eventId, userId, rateInputDto);

        return service.hitRateToEvent(userId, eventId, rateInputDto);
    }

    @GetMapping("/rate/initiators")
    @ResponseStatus(HttpStatus.OK)
    public List<RatingInitiatorsDto> getRatingInitiators() {
        log.info(ANSWER + "get Initiators rating.");
        return service.getInitiatorRating();
    }

    @GetMapping("/rate/event/message/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RateMessageDto> getEventRateMessage(@PathVariable long eventId,
                                                    @RequestParam(required = false, defaultValue = "true") Boolean rate,
                                                    @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                                    @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get all messageRating by eventID:{}, rate:{}.", eventId, rate);
        return service.getEventRateMessage(eventId, rate, from, size);
    }

    @GetMapping("/rate/user/message/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<RateMessageDto> getAllRateMessageByUser(@PathVariable long userId,
                                                        @RequestParam(required = false, defaultValue = "true") Boolean rate,
                                                        @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                                        @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get all messageRating by userID:{}, rate:{}.", userId, rate);
        return service.getRateMessageByUser(userId, rate, from, size);
    }
}