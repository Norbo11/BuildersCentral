package com.github.norbo11.topbuilders.models.enums;

import javafx.scene.control.ComboBox;

import com.github.norbo11.topbuilders.models.Employee;

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

    //Adds valid UserType choices to the ComboBox in the arugment, ensuring that only superusers can add managers. Nobody can add superusers (except directly through DB)
    public static void populateCombo(ComboBox<UserType> combo) {
        UserType loggedInUserType = Employee.getCurrentEmployee().getUserType();
        
        if (loggedInUserType.isAtLeast(UserType.SUPERUSER)) combo.getItems().add(UserType.MANAGER);
        combo.getItems().add(UserType.EMPLOYEE);
        combo.getSelectionModel().select(0);
    }
}
