package com.github.norbo11.topbuilders.models;

import java.util.ArrayList;

import com.github.norbo11.topbuilders.util.Settings;

public class EmployeeSetting extends AbstractSetting {
    public static final String DB_TABLE_NAME = "employeeSettings";	
    private static ArrayList<EmployeeSetting> employeeSettings = new ArrayList<EmployeeSetting>();
    
    public EmployeeSetting() {
        super("employeeId");
    }
    
    @Override
    public String getDbTableName() {
        return DB_TABLE_NAME;
    }
    
    /* Static methods */
    
    public static Settings<EmployeeSetting> loadSettingsForEmployee(Employee employee) {
        ArrayList<EmployeeSetting> list = loadList(loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId()), EmployeeSetting.class);
        
        return new Settings<EmployeeSetting>(employee, list, EmployeeSetting.class);
    }
    
    public static ArrayList<EmployeeSetting> getModels() {
	    return employeeSettings;
	}
}
