package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.locations.dto.LocationDto;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUserUpdateDto {
    String title;
    String annotation; // short annotation for this event
    Long categoryId; // identification number of category event
    String description; // Full description for this event
    String eventDate; // Date and time for this event in format("yyyy-MM-dd HH:mm:ss")
    LocationDto location; // Location(latitude, longitude) for this event
    Boolean paid; // this event is for free (paid = false) or not (paid = true)
    Long participantLimit; // what is the limit on the number of participant? if 0 = unlimited
    Boolean requestModeration; // moderation this event: need = true or not need = false
    String status; // new event status from user (SEND_TO_REVIEW or CANCEL_REVIEW)
}
