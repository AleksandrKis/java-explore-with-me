package ru.practicum.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;

    public ErrorResponse(final String error) {
        this.error = error;
    }
}