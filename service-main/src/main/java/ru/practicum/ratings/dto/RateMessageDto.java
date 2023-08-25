package ru.practicum.ratings.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateMessageDto {
    final Long eventId;
    final Boolean rate;
    final String author;
    final String message;
}