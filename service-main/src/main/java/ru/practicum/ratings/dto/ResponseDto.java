package ru.practicum.ratings.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// for response for user how rate some event.
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    final String response;
    final String message;
}