package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestReviewDto {
    List<Long> requestIds; // list ID requests for participant the event of user
    String status; // status for requestIds
}