package ru.practicum.events.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.models.Category;
import ru.practicum.categories.service.CategoryService;
import ru.practicum.events.dto.*;
import ru.practicum.events.models.Event;
import ru.practicum.events.models.EventStatus;
import ru.practicum.events.storage.EventRepository;
import ru.practicum.exceptions.*;
import ru.practicum.locations.storage.LocationRepo;
import ru.practicum.users.models.User;
import ru.practicum.users.service.UserService;
import ru.practicum.utility.StatSender;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.events.dto.EventMapperDto.*;
import static ru.practicum.events.models.AdminActionStatus.validAdminActionStatus;
import static ru.practicum.events.models.UserActionStatus.validOwnerActionStatus;
import static ru.practicum.locations.dto.MapperLocationDto.mapToLocation;
import static ru.practicum.utility.Constant.getPage;
import static ru.practicum.utility.TimeUtil.convertString;
import static ru.practicum.utility.TimeUtil.validTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {
    final UserService userService;
    final CategoryService categoryService;
    final EventRepository eventRepo;
    final LocationRepo locationRepo;
    final StatSender statSender;

    @Transactional
    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
        checkHoursBefore(newEventDto.getEventDate(), 2L);
        Event newEvent = mapToEvent(newEventDto);
        newEvent.setCategory(checkValidCat(newEventDto.getCategory()));
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(checkValidUser(userId));
        newEvent.setLocation(locationRepo.save(mapToLocation(newEventDto.getLocation())));
        newEvent.setState(EventStatus.PENDING);
        newEvent.setViews(0L);
        return mapToFullDto(eventRepo.save(newEvent));
    }

    @Override
    public List<ShortEventDto> getAllEventsByOwner(long userId, int from, int size) {
        return eventRepo.findAllByInitiator(checkValidUser(userId), getPage(from, size))
                .orElseThrow(() -> new UserNotFoundException("Not found Initiator with id: " + userId))
                .stream().map(EventMapperDto::mapToShortEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByOwner(long userId, long eventId) {
        Event event = checkValidEvent(eventId);
        checkValidUser(userId);
        checkValidOwner(userId, event);
        return mapToFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto getPublishedEventById(long eventId, HttpServletRequest request) {
        Event event = checkValidEvent(eventId);
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            checkValidUniqueIp(request, Collections.singletonList(event));
            return mapToFullDto(event);
        } else {
            throw new EventNotFoundException("event by ID:" + eventId + " mast be PUBLISHED");
        }
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(long eventId, EventUpdateDto adminUpdateDto) {
        Event forUpdate = checkValidEvent(eventId);
        validAdminUpdateStatus(forUpdate);
        checkValidEventDate(adminUpdateDto, 1L);
        updateEvent(forUpdate, adminUpdateDto);
        updateAdminActionStatus(forUpdate, adminUpdateDto);
        return mapToFullDto(eventRepo.save(forUpdate));
    }

    @Transactional
    @Override
    public EventFullDto updateEventByOwner(long ownerId, long eventId, EventUpdateDto userUpdateDto) {
        Event forUpdate = checkValidEvent(eventId);
        checkValidOwner(ownerId, forUpdate);
        validOwnerUpdateStatus(forUpdate);
        checkValidEventDate(userUpdateDto, 2L);
        updateEvent(forUpdate, userUpdateDto);
        updateOwnerActionStatus(forUpdate, userUpdateDto);
        return mapToFullDto(eventRepo.save(forUpdate));
    }

    @Transactional
    @Override
    public List<ShortEventDto> searchEventsByPublic(SearchPublicDto publicDto, HttpServletRequest request) {
        validTime(publicDto.getTimeDto().getRangeStart(), publicDto.getTimeDto().getRangeEnd());
        List<Event> result = eventRepo.findEventsByPublicRate(
                publicDto.getText(),
                publicDto.getCategories(),
                publicDto.getPaid(),
                publicDto.getTimeDto().getStart(),
                publicDto.getTimeDto().getEnd(),
                publicDto.getSort(),
                publicDto.getLikesSort(),
                getPage(publicDto.getFrom(), publicDto.getSize())
        );
        checkValidUniqueIp(request, result);
        if (publicDto.getOnlyAvailable()) {
            return result.stream().filter(e -> requestConfirmedCount(e.getRequests()) < e.getParticipantLimit())
                    .map(EventMapperDto::mapToShortEventDto).collect(Collectors.toList());
        }
        return result.stream().map(EventMapperDto::mapToShortEventDto).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(SearchAdminDto adminDto) {
        Page<Event> result = eventRepo.findEventsByAdmin(
                adminDto.getUsers(),
                adminDto.getStates(),
                adminDto.getCategories(),
                adminDto.getTimeDto().getStart(),
                adminDto.getTimeDto().getEnd(),
                getPage(adminDto.getFrom(), adminDto.getSize()));
        return result.stream()
                .map(EventMapperDto::mapToFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void checkValidUniqueIp(HttpServletRequest request, List<Event> events) {
        Long countHit = statSender.getStat(request);
        statSender.send(request);
        Long checkUniqHit = statSender.getStat(request);
        if (countHit < checkUniqHit) {
            events.forEach(event -> event.setViews(event.getViews() + 1L));
            eventRepo.saveAllAndFlush(events);
        }
    }

    private void checkValidEventDate(EventUpdateDto updateDto, long hours) {
        if (updateDto.getEventDate() != null) {
            checkHoursBefore(updateDto.getEventDate(), hours);
        }
    }

    private void checkHoursBefore(String checkTime, long hours) {
        if (convertString(checkTime).isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new TwoHoursBeforeException("eventDate must be an hours:" + hours + " later than local time.");
        }
    }

    private void checkValidOwner(long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new HaveNotPermissionException("that's event not your.");
        }
    }

    private User checkValidUser(long userId) {
        return userService.checkUserExist(userId);
    }

    private Category checkValidCat(long categoryId) {
        return categoryService.checkExist(categoryId);
    }

    public Event checkValidEvent(long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Not found event by Id: " + eventId));
    }

    private void updateEvent(Event forUpdate, EventUpdateDto updateDto) {
        descriptionUpdate(forUpdate, updateDto);
        annotationUpdate(forUpdate, updateDto);
        limitUpdate(forUpdate, updateDto);
        titleUpdate(forUpdate, updateDto);
        dateUpdate(forUpdate, updateDto);
        locationUpdate(forUpdate, updateDto);
        paidUpdate(forUpdate, updateDto);
        categoryUpdate(forUpdate, updateDto);
    }

    private void updateAdminActionStatus(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getStateAction() != null) {
            switch (validAdminActionStatus(updateDto.getStateAction())) {
                case PUBLISH_EVENT:
                    forUpdate.setState(EventStatus.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    forUpdate.setState(EventStatus.CANCELED);
                    break;
            }
        }
    }

    private void updateOwnerActionStatus(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getStateAction() != null) {
            switch (validOwnerActionStatus(updateDto.getStateAction())) {
                case SEND_TO_REVIEW:
                    forUpdate.setState(EventStatus.PENDING);
                    break;
                case CANCEL_REVIEW:
                    forUpdate.setState(EventStatus.CANCELED);
            }
        }
    }

    private void validAdminUpdateStatus(Event forUpdate) {
        if (forUpdate.getState().equals(EventStatus.PUBLISHED)) {
            throw new PublicationAlreadyException("that's event have status PUBLISHED");
        } else if (forUpdate.getState().equals(EventStatus.CANCELED)) {
            throw new PublicationCanceledException("that's event have status CANCELED");
        }
    }

    private void validOwnerUpdateStatus(Event forUpdate) {
        if (forUpdate.getState().equals(EventStatus.PUBLISHED)) {
            throw new PublicationAlreadyException("that's event have status PUBLISHED");
        }
    }

    private void categoryUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getCategory() != null) {
            if (!Objects.equals(forUpdate.getCategory().getId(), updateDto.getCategory())) {
                forUpdate.setDescription(updateDto.getDescription());
            }
        }
    }

    private void paidUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getPaid() != null) {
            forUpdate.setPaid(updateDto.getPaid());
        }
    }

    private void locationUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getLocation() != null) {
            if (!forUpdate.getLocation().getLat().equals(updateDto.getLocation().getLat()) ||
                    !forUpdate.getLocation().getLon().equals(updateDto.getLocation().getLon())) {
                forUpdate.setDescription(updateDto.getDescription());
            }
        }
    }

    private void dateUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getEventDate() != null) {
            forUpdate.setEventDate(convertString(updateDto.getEventDate()));
        }
    }

    private void descriptionUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getDescription() != null) {
            forUpdate.setDescription(updateDto.getDescription());
        }
    }

    private void annotationUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getAnnotation() != null) {
            forUpdate.setAnnotation(updateDto.getAnnotation());
        }
    }

    private void limitUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getParticipantLimit() != null) {
            forUpdate.setParticipantLimit(updateDto.getParticipantLimit());
        }
    }

    private void titleUpdate(Event forUpdate, EventUpdateDto updateDto) {
        if (updateDto.getTitle() != null) {
            forUpdate.setTitle(updateDto.getTitle());
        }
    }
}
