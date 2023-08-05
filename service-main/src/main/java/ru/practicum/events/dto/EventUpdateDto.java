package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.locations.dto.LocationDto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {
    @Size(min = 3, max = 120)
    String title;
    @Size(min = 20, max = 2000)
    String annotation; // short annotation for this event
    @PositiveOrZero
    Long category; // identification number of category event
    @Size(min = 20, max = 7000)
    String description; // Full description for this event
    @Pattern(regexp = "[0-9:\\-\\s]+")
    String eventDate; // Date and time for this event in format("yyyy-MM-dd HH:mm:ss")
    LocationDto location; // Location(latitude, longitude) for this event
    Boolean paid; // this event is for free (paid = false) or not (paid = true)
    Long participantLimit; // what is the limit on the number of participant? if 0 = unlimited
    Boolean requestModeration; // moderation this event: need = true or not need = false
    String stateAction; // new event status from admin (PUBLISH_EVENT or REJECT_EVENT)
}