package ru.practicum.exceptions;

public class StatRequestException extends RuntimeException {
    public StatRequestException(String message) {
        super(message);
    }
}
