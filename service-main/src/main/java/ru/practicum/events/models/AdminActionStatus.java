package ru.practicum.events.models;

import ru.practicum.exceptions.IllegalStatusException;

public enum AdminActionStatus {
    PUBLISH_EVENT, REJECT_EVENT;

    public static AdminActionStatus validAdminActionStatus(String status) {
        try {
            return AdminActionStatus.valueOf(status);
        } catch (IllegalStatusException e) {
            throw new IllegalStatusException("not found status: " + status);
        }
    }
}