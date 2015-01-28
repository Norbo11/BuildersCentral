package com.github.norbo11.topbuilders.models.enums;

public enum NotificationType {
    NEW_MESSAGE, NEW_ASSIGNMENT, ASSIGNMENT_CLOSE_TO_END, NEW_QUOTE_REQUEST, EMPLOYEE_ASSIGNMENT_COMPLETE;

    public static NotificationType getNotificationType(int typeId) {
        return values()[typeId];
    }
}
