package ru.practicum.requests.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.models.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.ExceededLimitException;
import ru.practicum.exceptions.HaveNotPermissionException;
import ru.practicum.exceptions.RequestAlreadyException;
import ru.practicum.exceptions.RequestNotFoundException;
import ru.practicum.requests.dto.ParticipantRequestDto;
import ru.practicum.requests.dto.ParticipantRequestMapper;
import ru.practicum.requests.dto.RequestReviewDto;
import ru.practicum.requests.dto.ResultRequestsReviewDto;
import ru.practicum.requests.models.ParticipantRequest;
import ru.practicum.requests.models.RequestStatus;
import ru.practicum.requests.storage.RequestRepository;
import ru.practicum.users.models.User;
import ru.practicum.users.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.requests.dto.ParticipantRequestMapper.mapToDto;
import static ru.practicum.requests.models.RequestStatus.*;
import static ru.practicum.requests.service.RequestServiceUtility.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {
    final UserService userService;
    final EventService eventService;
    final RequestRepository requestRepo;

    @Transactional
    @Override
    public ParticipantRequestDto createRequest(long userId, long eventId) {
        Event event = eventService.checkValidEvent(eventId);
        User user = userService.checkUserExist(userId);
        checkRequestExist(event, user);
        checkEventUserValid(event, user);
        ParticipantRequest newRequest = new ParticipantRequest();
        newRequest.setRequester(user);
        newRequest.setEvent(event);
        setStatus(event, newRequest);
        newRequest.setCreated(LocalDateTime.now());
        return mapToDto(requestRepo.save(newRequest));
    }

    @Override
    public List<ParticipantRequestDto> getAllByOwnerRequests(long userId) {
        return requestRepo.findAllByRequester(userService.checkUserExist(userId))
                .stream()
                .map(ParticipantRequestMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipantRequestDto canselRequest(long userId, long requestId) {
        User user = userService.checkUserExist(userId);
        ParticipantRequest request = checkValidRequest(requestId);
        checkValidRequestOwner(user, request);
        request.setStatus(RequestStatus.CANCELED);
        return mapToDto(requestRepo.save(request));
    }

    @Override
    public List<ParticipantRequestDto> getAllByOwnerEvent(long userId, long eventId) {
        Event event = eventService.checkValidEvent(eventId);
        User user = userService.checkUserExist(userId);
        checkValidEventOwner(event, user);
        return requestRepo.findAllByEvent(event)
                .stream()
                .map(ParticipantRequestMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResultRequestsReviewDto changeStatusRequests(long userId, long eventId, RequestReviewDto reviewDto) {
        Event event = eventService.checkValidEvent(eventId);
        User user = userService.checkUserExist(userId);
        checkValidEventOwner(event, user);
        checkEventForRestrict(event);
        RequestStatus status = validOwnerActionStatus(reviewDto.getStatus());
        List<ParticipantRequest> toBeUpdate = getPendingRequests(reviewDto);
        checkLimitAndConfirmed(event, toBeUpdate, status);
        requestRepo.saveAllAndFlush(toBeUpdate);
        List<ParticipantRequest> res = requestRepo.findAllByEventAndStatus(event, Arrays.asList(CONFIRMED, REJECTED));
        return ParticipantRequestMapper.mapToResult(collectResult(res, CONFIRMED), collectResult(res, REJECTED));
    }

    @Override
    public ParticipantRequest checkValidRequest(long requestId) {
        return requestRepo.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("ParticipantRequest not found that ID:" + requestId));
    }

    private List<ParticipantRequest> getPendingRequests(RequestReviewDto reviewDto) {
        return reviewDto.getRequestIds().stream()
                .map(this::checkValidRequest)
                .filter(pr -> pr.getStatus().equals(PENDING)).collect(Collectors.toList());
    }

    private void checkLimitAndConfirmed(Event event, List<ParticipantRequest> toBeUpdate, RequestStatus status) {
        if (!toBeUpdate.isEmpty() && status.equals(CONFIRMED)) {
            setOrExceptStatus(event, toBeUpdate, status);
        } else if (toBeUpdate.isEmpty()) {
            throw new HaveNotPermissionException("events with status PENDING not found");
        } else if (status.equals(REJECTED)) {
            toBeUpdate.forEach(pr -> pr.setStatus(REJECTED));
        }
    }

    private void setOrExceptStatus(Event event, List<ParticipantRequest> toBeUpdate, RequestStatus status) {
        Long conf = requestRepo.countByEventAndStatus(event, status);
        if (event.getParticipantLimit() - conf > 0) {
            toBeUpdate.forEach(pr -> {
                if (toBeUpdate.indexOf(pr) < event.getParticipantLimit() - conf) {
                    pr.setStatus(CONFIRMED);
                } else {
                    pr.setStatus(RequestStatus.REJECTED);
                }
            });
        } else {
            throw new ExceededLimitException("Participant Limit for that event is Exceeded.");
        }
    }

    private void checkRequestExist(Event event, User user) {
        if (requestRepo.existsByEventAndRequester(event, user)) {
            throw new RequestAlreadyException("you already have request on this event");
        }
    }

    private List<ParticipantRequest> collectResult(List<ParticipantRequest> res, RequestStatus status) {
        return res.isEmpty() ? Collections.emptyList() : res.stream().filter(pr -> pr.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
