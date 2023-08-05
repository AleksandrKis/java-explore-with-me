package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.models.SortEvent;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchPublicDto {
    String text;
    Set<Long> categories;
    Boolean paid;
    TimeDto timeDto;
    Boolean onlyAvailable;
    SortEvent sort;
    Integer from;
    Integer size;
}
