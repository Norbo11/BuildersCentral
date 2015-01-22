package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.github.norbo11.topbuilders.models.enums.SettingType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;

//This doesn't extend AbstractModel, as it is an object which holds all the settings of an employee.
//Settings in the database are stored one per row, so this class does not represent the model of any particular setting, and therefore has no ID.
public class EmployeeSettings {
    public static final String DB_TABLE_NAME = "employeeSettings";
    
	public EmployeeSettings(boolean fullscreen, Locale locale) {
		this.fullscreen = new SimpleBooleanProperty(fullscreen);
		this.locale = new SimpleObjectProperty<Locale>(locale);
	}
	
	private ObjectProperty<Locale> locale;
	private BooleanProperty fullscreen;

    public boolean isFullscreen() {
        return fullscreen.get();
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen.set(fullscreen);
    }

    public Locale getLocale() {
		return locale.get();
	}

	public void setLocale(Locale locale) {
		this.locale.set(locale);
	}

    public static EmployeeSettings getSettingsFromEmployeeId(int employeeId) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE employeeId = ?", employeeId);
        
        try {
        	boolean fullscreen = (boolean) SettingType.FULLSCREEN.getDefaultValue();
            Locale locale = Locale.forLanguageTag((String) SettingType.LOCALE.getDefaultValue());
        	
            while (result.next()) {
                int settingTypeId = result.getInt("settingTypeId");
                String value = result.getString("value");
                
                switch (SettingType.getSettingType(settingTypeId)) {
                case FULLSCREEN: fullscreen = Boolean.valueOf(value); break;
                case LOCALE: locale = Locale.forLanguageTag(value); break;
                }
            }
            return new EmployeeSettings(fullscreen, locale);

        } catch (SQLException e) {
            Log.error(e);
        }

		return null;
    }
}
