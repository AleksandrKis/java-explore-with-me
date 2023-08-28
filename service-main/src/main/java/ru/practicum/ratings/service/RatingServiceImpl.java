package ru.practicum.ratings.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.events.models.Event;
import ru.practicum.events.models.EventStatus;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.EventNotFoundException;
import ru.practicum.exceptions.HaveNotPermissionException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.ratings.dto.*;
import ru.practicum.ratings.models.Rate;
import ru.practicum.ratings.storage.RateRepository;
import ru.practicum.requests.models.RequestStatus;
import ru.practicum.users.models.User;
import ru.practicum.users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.utility.Constant.getPage;

/**
 * The class implements the rating functionality.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingServiceImpl implements RatingService {
    final UserService userService;
    final EventService eventService;
    final RateRepository rateRepo;
    static final Long timeLimit = 1L;

    /**
     * the feature of this method:
     * -Any registered user can give a positive or negative rating to any event in which he participated.
     * -User can also optionally leave a message with a comment to your rating.
     * -Editing your rating and/or message is possible no more than once a day.
     *
     * @param raterId      id of the user rating the event.
     * @param eventId      id of the event to be rated.
     * @param rateInputDto rating data and a message to it.
     * @return ResponseDto a message confirming the rating and a message to it.
     */
    @Override
    public ResponseDto hitRateToEvent(long raterId, long eventId, RateInputDto rateInputDto) {
        User user = userService.checkUserExist(raterId);// checking that a user id is registered in the system.
        Event event = checkValidForRating(eventId, user);
        checkTimeForRate(event);//checking the time of rating, it is necessary after the EventDate of the event.
        Optional<Rate> rating = rateRepo.findFirstByEventAndRater(event, user);//checking the existence rating-
        if (rating.isPresent()) { // -from the current user for this event. If rating exist:
            checkChangeTime(rating.get().getTime().plusDays(timeLimit), // Checking that the rating or message changes
                    "you can change your mind only once a day"); // no more than once a day
            return getChangeResponseDto(rateInputDto, rating.get(), user, event);
        } else {
            return getNewResponseDto(rateInputDto, event, user);
        }
    }

    /**
     * The feature of this method:
     * getting a list of the best events initiators.
     *
     * @return Rating list of the best initiators of events in the ratio of positive ratings to negative.
     */
    @Override
    public List<RatingInitiatorsDto> getInitiatorRating() {
        //Select from the database all events that have more positive ratings than negative ratings and Order by desc
        return rateRepo.findAllByEventRating()
                .stream()
                .map(eventService::checkValidEvent)
                .map(RateMapperDto::mapToRateInitiatorsDto)
                .collect(Collectors.toList());
    }

    /**
     * The feature of this method:
     * getting a list of messages by rating type for Event.
     *
     * @param eventId id of event for getting all messages by rating type.
     * @param rate    the type of rating by getting messages on the event.
     * @param from    Pagination setting.
     * @param size    Pagination setting.
     * @return List all messages by type rating for current Event.
     */
    @Override
    public List<RateMessageDto> getEventRateMessage(long eventId, Boolean rate, int from, int size) {
        Event event = eventService.checkValidEvent(eventId);
        return rateRepo.findAllByEventAndRate(event, rate, getPage(from, size))
                .stream()
                .filter(rate1 -> !rate1.getMessage().isEmpty())
                .map(RateMapperDto::mapToRateMessageDto)
                .collect(Collectors.toList());
    }

    /**
     * The feature of this method:
     * getting a list of messages by rating type from current User.
     *
     * @param userId id of user from getting all messages by rating type.
     * @param rate   the type of rating by getting messages from the User.
     * @param from   Pagination setting.
     * @param size   Pagination setting.
     * @return List all messages by type rating from current User.
     */
    @Override
    public List<RateMessageDto> getRateMessageByUser(long userId, Boolean rate, int from, int size) {
        User user = userService.checkUserExist(userId);
        return rateRepo.findAllByRaterAndRate(user, rate, getPage(from, size))
                .stream()
                .filter(rate1 -> !rate1.getMessage().isEmpty())
                .map(RateMapperDto::mapToRateMessageDto)
                .collect(Collectors.toList());
    }

    private Event checkValidForRating(long eventId, User user) {
        Event event = eventService.checkValidEvent(eventId);// checking that event id is registered in the system.
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            checkValidUserParticipant(user, event);// checking that user participated event id is registered in the system.
        } else {
            throw new EventNotFoundException("event by ID:" + eventId + " mast be PUBLISHED");
        }
        return event;
    }

    private static void checkValidUserParticipant(User user, Event event) {
        if (!event.getInitiator().equals(user)) {
            event.getRequests()
                    .stream()
                    .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                    .filter(rc -> rc.getRequester().equals(user))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException("need CONFIRMED ParticipantRequest but that's request not found."));
        } else {
            throw new HaveNotPermissionException("That's event is your.Initiator can't rate your-self events");
        }
    }

    private ResponseDto getResponseDto(Rate.RateBuilder event, RateInputDto rateInputDto, LocalDateTime time) {
        return RateMapperDto.mapToResponsesDto(rateRepo.save(event
                .rate(rateInputDto.getRate())
                .message(rateInputDto.getMessage() != null ? rateInputDto.getMessage() : "")
                .time(time).build()));
    }

    private ResponseDto getNewResponseDto(RateInputDto rateInputDto, Event event, User user) {
        return getResponseDto(Rate.builder()
                .event(event)
                .rater(user), rateInputDto, LocalDateTime.now().plusDays(timeLimit));
    }

    private ResponseDto getChangeResponseDto(RateInputDto rateInputDto, Rate rating, User user, Event event) {
        return getResponseDto(Rate.builder()
                .id(rating.getId())
                .rater(user)
                .event(event), rateInputDto, LocalDateTime.now());
    }

    private static void checkChangeTime(LocalDateTime rateTime, String message) {
        if (!LocalDateTime.now().plusDays(timeLimit).isAfter(rateTime)) {
            throw new HaveNotPermissionException(message);
        }
    }

    private static void checkTimeForRate(Event event) {
        checkChangeTime(event.getEventDate(), "your rate mast be later then EventDate");
    }
}
