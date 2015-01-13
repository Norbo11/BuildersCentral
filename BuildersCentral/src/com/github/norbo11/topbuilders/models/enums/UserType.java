package com.github.norbo11.topbuilders.models.enums;

import java.lang.Comparable;

public enum UserType implements Comparable<UserType> {
    SUPERUSER("Superuser", 0), MANAGER("Manager", 1), EMPLOYEE("Normal", 2);
    
    private String typeName;
    private int rank;
    
    UserType(String typeName, int rank) {
        this.typeName = typeName;
        this.rank = rank;
    }
    
    public String getTypeName() {
        return typeName;
    }

    public int getRank() {
        return rank;
    }

    public String toString() {
        return typeName;
    }

    public static UserType getUserType(int rank) {
        for (UserType type : values()) {
            if (type.getRank() == rank) return type;
        }
        return null;
    }

    public boolean isAtLeast(UserType type) {
        return rank <= type.getRank();
    }
}
