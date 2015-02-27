package com.github.norbo11.topbuilders.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.util.Settings;

public class EmployeeSetting extends AbstractSetting {
    public static final String DB_TABLE_NAME = "employeeSettings";	
    private static ObservableList<EmployeeSetting> employeeSettings = FXCollections.observableArrayList();
    
    public EmployeeSetting() {
        super("employeeId");
    }
    
    @Override
    public String getDbTableName() {
        return DB_TABLE_NAME;
    }
    
    /* Static methods */
    
    public static Settings<EmployeeSetting> loadSettingsForEmployee(Employee employee) {
    	ObservableList<EmployeeSetting> list = loadList(loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId()), EmployeeSetting.class);
        
        return new Settings<EmployeeSetting>(employee, list, EmployeeSetting.class);
    }
    
    public static ObservableList<EmployeeSetting> getModels() {
	    return employeeSettings;
	}
}
