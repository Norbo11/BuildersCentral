package com.github.norbo11.topbuilders.models.enums;

public enum NotificationType {
    NEW_MESSAGE(1), NEW_ASSIGNMENT(2), ASSIGNMENT_CLOSE_TO_END(3), EMPLOYEE_ASSIGNMENT_COMPLETE(4), NEW_QUOTE_REQUEST(5);

    private int id = 0;
    
    NotificationType(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public static NotificationType getNotificationType(int id) {
        for (NotificationType type : values()) {
            if (type.getId() == id) return type;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return null;
    }
}
