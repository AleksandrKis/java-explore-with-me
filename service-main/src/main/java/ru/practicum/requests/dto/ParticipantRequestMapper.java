package ru.practicum.requests.dto;

import ru.practicum.requests.models.ParticipantRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utility.Constant.DATE_TIME_FORMATTER;

public class ParticipantRequestMapper {
    public static ParticipantRequestDto mapToDto(ParticipantRequest request) {
        return ParticipantRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .created(request.getCreated().format(DATE_TIME_FORMATTER)).build();
    }

    public static ResultRequestsReviewDto mapToResult(List<ParticipantRequest> listConfirmed,
                                                      List<ParticipantRequest> listRejected) {
        return ResultRequestsReviewDto.builder()
                .confirmedRequests(listConfirmed.stream()
                        .map(ParticipantRequestMapper::mapToDto).collect(Collectors.toList()))
                .rejectedRequests(listRejected.stream()
                        .map(ParticipantRequestMapper::mapToDto).collect(Collectors.toList())).build();
    }
}
