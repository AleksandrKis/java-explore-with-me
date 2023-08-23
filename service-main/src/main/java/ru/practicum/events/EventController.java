package ru.practicum.events;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.*;
import ru.practicum.events.models.EventStatus;
import ru.practicum.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

import static ru.practicum.events.models.SortEvent.validSort;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventController {
    static final String PRIVATE = "/users/{userId}/events";
    static final String ADMIN = "/admin/events";
    static final String PUBLIC = "/events";
    static final String ANSWER = "EventController: received request for ";
    final EventService service;

    @PostMapping(PRIVATE)
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info(ANSWER + "add new event, from user by ID:{}", userId);
        return service.createEvent(userId, newEventDto);
    }

    @GetMapping(PRIVATE)
    @ResponseStatus(HttpStatus.OK)
    public List<ShortEventDto> getAllByOwner(@PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                             @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get all events by owner, from user by ID:{}", userId);
        return service.getAllEventsByOwner(userId, from, size);
    }

    @GetMapping(PRIVATE + "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info(ANSWER + "get event by ID:{}, from user by ID:{}", eventId, userId);
        return service.getEventByOwner(userId, eventId);
    }

    @PatchMapping(PRIVATE + "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByOwner(@PathVariable Long userId, @PathVariable Long eventId,
                                      @Valid @RequestBody EventUpdateDto updateDto) {
        log.info(ANSWER + "update event by ID:{}, from owner by ID:{}", eventId, userId);
        return service.updateEventByOwner(userId, eventId, updateDto);
    }

    @PatchMapping(ADMIN + "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateByAdmin(@PathVariable Long eventId, @Valid @RequestBody EventUpdateDto updateDto) {
        log.info(ANSWER + "update event by ID:{}, from admin", eventId);
        return service.updateEventByAdmin(eventId, updateDto);
    }

    @GetMapping(ADMIN)
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchByAdmin(@RequestParam(required = false) Set<Long> users,
                                            @RequestParam(required = false) Set<EventStatus> states,
                                            @RequestParam(required = false) Set<Long> categories,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                            @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "search event by admin: users-{}, states-{}, categories-{}", users, states, categories);
        return service.searchEventsByAdmin(SearchAdminDto.builder().users(users).states(states).categories(categories)
                .timeDto(new TimeDto(rangeStart, rangeEnd)).from(from).size(size).build());
    }

    @GetMapping(PUBLIC)
    @ResponseStatus(HttpStatus.OK)
    public List<ShortEventDto> searchByPublic(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) Set<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort,
                                              @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                              @Positive @RequestParam(required = false, defaultValue = "10") int size,
                                              HttpServletRequest request) {
        log.info(ANSWER + "search public event by param: text-{} in categories-{}", text, categories);
        return service.searchEventsByPublic(SearchPublicDto.builder().text(text).categories(categories)
                .paid(paid).timeDto(new TimeDto(rangeStart, rangeEnd))
                .onlyAvailable(onlyAvailable).sort(validSort(sort)).from(from).size(size).build(), request);
    }

    @GetMapping(PUBLIC + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublicById(@PathVariable Long id, HttpServletRequest request) {
        log.info(ANSWER + "get public event by ID:{}", id);
        return service.getPublishedEventById(id, request);
    }
}