package ru.practicum.events.dto;

import ru.practicum.events.models.Event;
import ru.practicum.events.models.EventStatus;
import ru.practicum.ratings.dto.RateMapperDto;
import ru.practicum.requests.models.ParticipantRequest;
import ru.practicum.requests.models.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.categories.dto.MapperCategoryDto.mapToCategoryDto;
import static ru.practicum.locations.dto.MapperLocationDto.mapToDto;
import static ru.practicum.users.dto.MapperUserDto.mapToShortDto;
import static ru.practicum.utility.Constant.DATE_TIME_FORMATTER;

public class EventMapperDto {
    public static Event mapToEvent(NewEventDto newEvent) {
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .description(newEvent.getDescription())
                .eventDate(LocalDateTime.parse(newEvent.getEventDate(), DATE_TIME_FORMATTER))
                .state(EventStatus.PENDING)
                .paid(newEvent.getPaid() != null ? newEvent.getPaid() : false)
                .participantLimit(newEvent.getParticipantLimit() != null ? newEvent.getParticipantLimit() : 0L)
                .title(newEvent.getTitle())
                .requestModeration(newEvent.getRequestModeration() != null ? newEvent.getRequestModeration() : true).build();
    }

    public static EventFullDto mapToFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapToCategoryDto(event.getCategory()))
                .confirmedRequests(requestConfirmedCount(event.getRequests()))
                .createdOn(event.getCreatedOn().format(DATE_TIME_FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(mapToShortDto(event.getInitiator()))
                .location(mapToDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? "" : event.getPublishedOn().format(DATE_TIME_FORMATTER))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .rating(RateMapperDto.mapToDto(event)).build();
    }

    public static ShortEventDto mapToShortEventDto(Event event) {
        return ShortEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapToCategoryDto(event.getCategory()))
                .confirmedRequests(requestConfirmedCount(event.getRequests()))
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(mapToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .rating(RateMapperDto.mapToDto(event)).build();
    }

    public static Long requestConfirmedCount(List<ParticipantRequest> list) {
        return (list == null || list.isEmpty()) ? 0 : list.stream().filter(request -> request.getStatus()
                .equals(RequestStatus.CONFIRMED)).count();
    }
}
