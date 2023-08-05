package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ErrorHandler {
    @ExceptionHandler({UnknownStateException.class, WrongTimePeriodException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownState(RuntimeException exception) {
        log.error("ОШИБКА");
        return new ErrorResponse(exception.getMessage());
    }
}