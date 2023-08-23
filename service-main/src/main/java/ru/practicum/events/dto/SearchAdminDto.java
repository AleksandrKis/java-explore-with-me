package ru.practicum.events.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.models.EventStatus;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchAdminDto {
    Set<Long> users;
    Set<EventStatus> states;
    Set<Long> categories;
    TimeDto timeDto;
    Integer from;
    Integer size;
}