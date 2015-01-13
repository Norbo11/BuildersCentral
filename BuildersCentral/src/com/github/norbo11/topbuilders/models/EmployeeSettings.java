package com.github.norbo11.topbuilders.models;

import java.util.Locale;

import com.github.norbo11.topbuilders.models.enums.UserType;

public class EmployeeSettings {
    public static final String DB_TABLE_NAME = "employeeSettings";
    
	public EmployeeSettings(Locale locale, boolean fullscreen, UserType userType) {
		this.locale = locale;
		this.fullscreen = fullscreen;
		this.userType = userType;
	}
	
	private Locale locale;
	private boolean fullscreen;
	private UserType userType;

	public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public static EmployeeSettings load(Employee user) {
	    //TODO Temporary until loading is implemented
		return new EmployeeSettings(Locale.ENGLISH, false, UserType.MANAGER);
	}
}
