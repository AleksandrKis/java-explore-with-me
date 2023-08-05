package ru.practicum.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String error;
    LocalDateTime errorTime;
}
