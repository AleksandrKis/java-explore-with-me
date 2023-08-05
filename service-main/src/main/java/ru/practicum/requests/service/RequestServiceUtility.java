package ru.practicum.requests.service;

import ru.practicum.events.models.Event;
import ru.practicum.events.models.EventStatus;
import ru.practicum.exceptions.ExceededLimitException;
import ru.practicum.exceptions.HaveNotPermissionException;
import ru.practicum.exceptions.NoRestrictionsException;
import ru.practicum.requests.models.ParticipantRequest;
import ru.practicum.requests.models.RequestStatus;
import ru.practicum.users.models.User;

import static ru.practicum.events.dto.EventMapperDto.requestConfirmedCount;
import static ru.practicum.requests.models.RequestStatus.CONFIRMED;

public class RequestServiceUtility {

    static void checkEventForRestrict(Event event) {
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            throw new NoRestrictionsException("your event not need any restriction");
        }
    }

    static void setStatus(Event event, ParticipantRequest newRequest) {
        if ((event.getParticipantLimit() == 0) || (!event.getRequestModeration())) {
            newRequest.setStatus(CONFIRMED);
        } else {
            newRequest.setStatus(RequestStatus.PENDING);
        }
    }

    static void checkEventUserValid(Event event, User user) {
        if (event.getInitiator().equals(user)) { // создатель события не может быть его реквестером. Ожидается 409
            throw new HaveNotPermissionException("you can't create request, that's your event.");
        } else if (!event.getState().equals(EventStatus.PUBLISHED)) { // создать реквест можно только на опубликованные события. Ожидается 409
            throw new HaveNotPermissionException("that's event mast be PUBLISHED");
        } else if ((event.getParticipantLimit() != 0) // проверка превышения лимита одобренных реквестов.Ожидается 409
                && (requestConfirmedCount(event.getRequests()) >= event.getParticipantLimit())) {
            throw new ExceededLimitException("Participant Limit for that event is Exceeded.");
        }
    }

    static void checkValidRequestOwner(User user, ParticipantRequest request) {
        if (!request.getRequester().equals(user)) {
            throw new HaveNotPermissionException("you can't change.That's not your request!");
        }
    }

    static void checkValidEventOwner(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new HaveNotPermissionException("you can't get all requests.That's not your event!");
        }
    }
}
