package ru.practicum.ratings.service;

import ru.practicum.ratings.dto.RateInputDto;
import ru.practicum.ratings.dto.RateMessageDto;
import ru.practicum.ratings.dto.RatingInitiatorsDto;
import ru.practicum.ratings.dto.ResponseDto;

import java.util.List;

public interface RatingService {
    ResponseDto hitRateToEvent(long raterId, long eventId, RateInputDto rateInputDto);

    List<RatingInitiatorsDto> getInitiatorRating();

    List<RateMessageDto> getEventRateMessage(long eventId, Boolean rate, int from, int size);

    List<RateMessageDto> getRateMessageByUser(long userId, Boolean rate, int from, int size);
}
