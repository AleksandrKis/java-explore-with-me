package ru.practicum.requests.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantRequestDto {
    Long id; // request id
    Long event; // event id
    Long requester; // user requester id
    String status; // request status (enum: PENDING, CONFIRMED, REJECTED, CANCELED)
    String created; // string LocalDataTime request created( format "yyyy-MM-dd HH:mm:ss")
}
