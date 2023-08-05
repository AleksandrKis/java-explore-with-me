package ru.practicum.utils;

import ru.practicum.exceptions.WrongTimePeriodException;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constant.DATE_TIME_FORMATTER;

public class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime convertString(String time) {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }

    public static void validTime(String start, String end) {
        if (convertString(start).isAfter(convertString(end))) {
            throw new WrongTimePeriodException("Wrong Time period");
        }
    }
}