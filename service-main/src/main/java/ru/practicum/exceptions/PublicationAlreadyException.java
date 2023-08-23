package ru.practicum.exceptions;

public class PublicationAlreadyException extends RuntimeException {
    public PublicationAlreadyException(String message) {
        super(message);
    }
}
