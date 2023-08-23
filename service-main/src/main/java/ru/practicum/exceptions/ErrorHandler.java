package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum")
public class ErrorHandler {
    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, EventNotFoundException.class,
            RequestNotFoundException.class, CompilationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(RuntimeException exception) {
        log.error(exception.getLocalizedMessage());
        return ErrorResponse.builder()
                .error(exception.getMessage())
                .errorTime(LocalDateTime.now()).build();
    }

    @ExceptionHandler({StatRequestException.class, TwoHoursBeforeException.class,
            IllegalStatusException.class, TimePeriodException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValid(RuntimeException exception) {
        log.error(exception.getLocalizedMessage());
        return ErrorResponse.builder()
                .error(exception.getMessage())
                .errorTime(LocalDateTime.now()).build();
    }

    @ExceptionHandler({PublicationAlreadyException.class, PublicationCanceledException.class,
            RequestAlreadyException.class, ExceededLimitException.class, NoRestrictionsException.class,
            ConstraintViolationException.class, HaveNotPermissionException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlready(RuntimeException exception) {
        log.error(exception.getLocalizedMessage());
        return ErrorResponse.builder()
                .error(exception.getMessage())
                .errorTime(LocalDateTime.now()).build();
    }
}
