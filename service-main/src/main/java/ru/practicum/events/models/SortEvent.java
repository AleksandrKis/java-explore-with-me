package ru.practicum.events.models;

import ru.practicum.exceptions.IllegalStatusException;

import java.util.Arrays;

public enum SortEvent {
    EVENT_DATE, VIEWS;

    public static SortEvent validSort(String sort) {
        return Arrays.stream(values()).filter(sortEvent -> sortEvent.name().equalsIgnoreCase(sort)).findFirst()
                .orElseThrow(() -> new IllegalStatusException("not found sort: " + sort));
    }
}
