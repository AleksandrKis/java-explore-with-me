package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.ShortUserDto;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortEventDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    String eventDate;
    ShortUserDto initiator;
    Boolean paid;
    String title;
    Long views;
}
