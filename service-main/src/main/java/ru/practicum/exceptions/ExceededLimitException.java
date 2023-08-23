package ru.practicum.exceptions;

public class ExceededLimitException extends RuntimeException {
    public ExceededLimitException(String message) {
        super(message);
    }
}
