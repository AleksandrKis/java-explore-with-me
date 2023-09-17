package ru.practicum.ratings.dto;

import ru.practicum.events.models.Event;
import ru.practicum.ratings.models.Rate;

// the resulting mapping for rating dto
public class RateMapperDto {
    /**
     * Generates a RateDto by checking for the existence of an estimate in the rating event field,
     * associated with the Rate entity and counting all TRUE,
     * sequentially first and recording their number in the Like variable.
     * Similarly, with the disLike variable.
     */
    public static RateDto mapToDto(Event event) {
        return RateDto.builder()
                .likes(event.getRating() != null && !event.getRating().isEmpty() ? event.getRating()
                        .stream().filter(rate -> rate.getRate().equals(true)).count() : 0L)
                // If there is no rating for the current event yet, the algorithm returns zero.
                .disLikes(event.getRating() != null && !event.getRating().isEmpty() ? event.getRating()
                        .stream().filter(rate -> rate.getRate().equals(false)).count() : 0L).build();
    }

    /**
     * Analyzing the request, generates a confirmation for the user who gave the rating.
     */
    public static ResponseDto mapToResponsesDto(Rate rate) {
        if (rate.getRate()) {
            return ResponseDto.builder().response("your Like record for Event ID:" + rate.getEvent().getId())
                    .message(rate.getMessage()).build();
        }
        return ResponseDto.builder().response("your DisLike record for Event ID:" + rate.getEvent().getId())
                .message(rate.getMessage()).build();
    }

    public static RatingInitiatorsDto mapToRateInitiatorsDto(Event event) {
        return RatingInitiatorsDto.builder().name(event.getInitiator().getName()).rate(mapToDto(event)).build();
    }

    /**
     * Mapping from entity Rate to RateMessageDto response: event ID-Rate Type-rater name-message
     */
    public static RateMessageDto mapToRateMessageDto(Rate rate) {
        return RateMessageDto.builder().eventId(rate.getEvent().getId()).rate(rate.getRate())
                .author(rate.getRater().getName()).message(rate.getMessage()).build();
    }

    public static RecommendDto mapToRecommendDto(Event event) {
        return RecommendDto.builder().eventId(event.getId()).annotation(event.getAnnotation()).build();
    }
}
