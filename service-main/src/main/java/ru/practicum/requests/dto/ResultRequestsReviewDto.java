package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultRequestsReviewDto {
    List<ParticipantRequestDto> confirmedRequests;
    List<ParticipantRequestDto> rejectedRequests;
}