package ru.practicum.exceptions;

public class PublicationCanceledException extends RuntimeException {
    public PublicationCanceledException(String message) {
        super(message);
    }
}
