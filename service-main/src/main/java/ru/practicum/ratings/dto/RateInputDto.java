package ru.practicum.ratings.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// dto for input rating and message from user to event;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateInputDto {
    @NotNull
    final Boolean rate; // user type of rating. true - like, false - disLike.
    @Size(max = 2000)
    final String message; // Optional message from user rater with rating.
}