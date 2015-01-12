package com.github.norbo11.topbuilders.models.enums;

public enum UserType {
    MANAGER("Manager"), SUPERUSER("Superuser"), EMPLOYEE("Normal");
    
    public String typeName = "";
    
    UserType(String typeName) {
        this.typeName = typeName;
    }
    
    public String toString() {
        return typeName;
    }

    public static UserType getUserType(int type) {
        switch (type) {
            case 0: return SUPERUSER;
            case 1: return MANAGER;
            case 2: return EMPLOYEE;
        }
        return null;
    }
}
