package com.github.norbo11.topbuilders.models.enums;

public enum UserType {
    NORMAL("Normal"), MANAGER("Manager"), SUPERUSER("Superuser"); 
    
    public String typeName = "";
    
    UserType(String typeName) {
        this.typeName = typeName;
    }
    
    public String toString() {
        return typeName;
    }

    public static UserType getUserType(String type) {
        switch (type) {
            case "normal": return NORMAL;
            case "manager": return MANAGER;
            case "superuser": return SUPERUSER;
        }
        return null;
    }
}
