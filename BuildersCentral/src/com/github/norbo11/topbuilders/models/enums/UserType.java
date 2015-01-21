package com.github.norbo11.topbuilders.models.enums;

import java.lang.Comparable;

public enum UserType implements Comparable<UserType> {
    SUPERUSER(0), MANAGER(1), EMPLOYEE(2);
    
    private int rank;
    
    UserType(int rank) {
        this.rank = rank;
    }
    
    public int getRank() {
        return rank;
    }

    public static UserType getUserType(int rank) {
        return values()[rank];
    }

    public boolean isAtLeast(UserType type) {
        return rank <= type.getRank();
    }
}
