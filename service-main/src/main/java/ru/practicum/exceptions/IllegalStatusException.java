package ru.practicum.exceptions;

public class IllegalStatusException extends RuntimeException {
    public IllegalStatusException(String message) {
        super(message);
    }
}