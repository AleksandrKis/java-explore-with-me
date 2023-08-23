package ru.practicum.exceptions;

public class RequestAlreadyException extends RuntimeException {
    public RequestAlreadyException(String message) {
        super(message);
    }
}
