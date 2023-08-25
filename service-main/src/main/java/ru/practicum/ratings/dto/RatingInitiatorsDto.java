package ru.practicum.ratings.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

//for response to request for a rating, initiators of events.
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingInitiatorsDto {
    String name; // initiator of event name.
    RateDto rate; // summary rate info for current event.
}
