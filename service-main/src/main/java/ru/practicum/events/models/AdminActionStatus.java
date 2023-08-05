package ru.practicum.events.models;

import ru.practicum.exceptions.IllegalStatusException;

import java.util.Arrays;

public enum AdminActionStatus {
    PUBLISH_EVENT, REJECT_EVENT;

    public static AdminActionStatus validAdminActionStatus(String status) {
        return Arrays.stream(values()).filter(actionStatus -> actionStatus.name().equalsIgnoreCase(status)).findFirst()
                .orElseThrow(() -> new IllegalStatusException("not found status: " + status));
    }
}
