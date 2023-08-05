package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipantRequestDto;
import ru.practicum.requests.dto.RequestReviewDto;
import ru.practicum.requests.dto.ResultRequestsReviewDto;
import ru.practicum.requests.models.ParticipantRequest;

import java.util.List;

public interface RequestService {
    ParticipantRequestDto createRequest(long userId, long eventId);

    List<ParticipantRequestDto> getAllByOwnerRequests(long userId);

    ParticipantRequestDto canselRequest(long userId, long requestId);

    List<ParticipantRequestDto> getAllByOwnerEvent(long userId, long eventId);

    ResultRequestsReviewDto changeStatusRequests(long userId, long eventId, RequestReviewDto reviewDto);

    ParticipantRequest checkValidRequest(long requestId);
}
