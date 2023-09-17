package ru.practicum.ratings.service;

import ru.practicum.ratings.dto.*;

import java.util.List;

public interface RatingService {
    ResponseDto hitRateToEvent(long raterId, long eventId, RateInputDto rateInputDto);

    List<RatingInitiatorsDto> getInitiatorRating();

    List<RateMessageDto> getEventRateMessage(long eventId, Boolean rate, int from, int size);

    List<RateMessageDto> getRateMessageByUser(long userId, Boolean rate, int from, int size);

    List<RecommendDto> getRecommendedEvents(long userId);
}
