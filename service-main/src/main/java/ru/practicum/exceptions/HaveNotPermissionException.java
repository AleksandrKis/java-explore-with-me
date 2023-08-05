package ru.practicum.exceptions;

public class HaveNotPermissionException extends RuntimeException {
    public HaveNotPermissionException(String message) {
        super(message);
    }
}