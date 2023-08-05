package ru.practicum.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FOR = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Pageable getPage(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}