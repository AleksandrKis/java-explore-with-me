package ru.practicum.requests.models;

import ru.practicum.exceptions.IllegalStatusException;

import java.util.Arrays;

public enum RequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    public static RequestStatus validOwnerActionStatus(String status) {
        return Arrays.stream(values()).filter(requestStatus -> requestStatus.name().equalsIgnoreCase(status)).findFirst()
                .orElseThrow(() -> new IllegalStatusException("not found status: " + status));
    }
}