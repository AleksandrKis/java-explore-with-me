package ru.practicum.events.models;

import ru.practicum.exceptions.IllegalStatusException;

import java.util.Arrays;

public enum UserActionStatus {
    SEND_TO_REVIEW, CANCEL_REVIEW;

    public static UserActionStatus validOwnerActionStatus(String status) {
        return Arrays.stream(values()).filter(actionStatus -> actionStatus.name().equalsIgnoreCase(status)).findFirst()
                .orElseThrow(() -> new IllegalStatusException("not found status: " + status));
    }
}