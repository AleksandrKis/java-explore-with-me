package ru.practicum.events.service;

import ru.practicum.events.dto.*;
import ru.practicum.events.models.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    List<ShortEventDto> getAllEventsByOwner(long userId, int from, int size);

    EventFullDto getEventByOwner(long userId, long eventId);

    EventFullDto getPublishedEventById(long eventId, HttpServletRequest request);

    EventFullDto updateEventByAdmin(long eventId, EventUpdateDto adminUpdateDto);

    EventFullDto updateEventByOwner(long ownerId, long eventId, EventUpdateDto userUpdateDto);

    List<ShortEventDto> searchEventsByPublic(SearchPublicDto publicDto, HttpServletRequest request);

    List<EventFullDto> searchEventsByAdmin(SearchAdminDto adminDto);

    Event checkValidEvent(long eventId);
}