package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.locations.dto.LocationDto;

import javax.validation.constraints.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotNull @NotBlank @Size(min = 3, max = 120, message = "your title mast have more then 12 letters and less than 2000")
    String title;
    @NotNull @NotBlank @Size(min = 20, max = 2000, message = "your annotation mast have more then 20 letters and less than 2000")
    String annotation;
    @Positive
    Long category;
    @NotNull @NotBlank @Size(min = 20, max = 7000, message = "your description mast have more then 21 letters and less than 7000")
    String description;
    @NotNull
    @Pattern(regexp = "[0-9:\\-\\s]+")
    String eventDate;
    @NotNull
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
}