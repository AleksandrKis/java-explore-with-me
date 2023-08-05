package ru.practicum.utility;

import ru.practicum.exceptions.TimePeriodException;

import java.time.LocalDateTime;

import static ru.practicum.utility.Constant.DATE_TIME_FOR;
import static ru.practicum.utility.Constant.DATE_TIME_FORMATTER;

public class TimeUtil {

    public static LocalDateTime convertTime(LocalDateTime unFormat) {
        return LocalDateTime.parse(unFormat.format(DATE_TIME_FOR));
    }

    public static LocalDateTime convertString(String time) {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }

    public static void validTime(String start, String end) {
        if (start != null && end != null) {
            if (convertString(start).isAfter(convertString(end))) {
                throw new TimePeriodException("Wrong Time period");
            }
        }
    }
}