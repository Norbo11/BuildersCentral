package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.models.enums.SettingType;
import com.github.norbo11.topbuilders.util.Log;

//This doesn't extend AbstractModel, as it is an object which holds all the settings of an employee.
//Settings in the database are stored one per row, so this class does not represent the model of any particular setting, and therefore has no ID.
public class EmployeeSettings extends AbstractModel {
    public static final String DB_TABLE_NAME = "employeeSettings";
    
	private StringProperty localeId = new SimpleStringProperty((String) SettingType.LOCALE.getDefaultValue());
	private BooleanProperty fullscreen = new SimpleBooleanProperty((boolean) SettingType.FULLSCREEN.getDefaultValue());

	/* Getters and setters */
	
    public boolean isFullscreen() {
        return fullscreen.get();
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen.set(fullscreen);
    }

    public String getLocaleId() {
		return localeId.get();
	}

	public void setLocaleId(String localeId) {
		this.localeId.set(localeId);
	}
	
	/* Instance methods */

	public Locale getLocale() {
	    return Locale.forLanguageTag(getLocaleId());
	}
	
    @Override
    public int add() {
        return 0;
        // TODO Auto-generated method stub
        
    }
	
    @Override
    public void save() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {
        while (result.next()) {
            int settingTypeId = result.getInt("settingTypeId");
            String value = result.getString("value");
            
            switch (SettingType.getSettingType(settingTypeId)) {
            case FULLSCREEN: setFullscreen(Boolean.valueOf(value)); break;
            case LOCALE: setLocaleId(value); break;
            }
        }
    }

    @Override
    public String getDbTableName() {
        return DB_TABLE_NAME;
    }
    
    /* Static methods */
    
    public static EmployeeSettings loadSettingsForEmployee(Employee employee) {
        ResultSet result = loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId());
       
        EmployeeSettings settings = new EmployeeSettings();
        try {
            settings.loadFromResult(result);
        } catch (SQLException e) {
            Log.error(e);
        }

        return settings;
    }
}
