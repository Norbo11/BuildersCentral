package com.github.norbo11.topbuilders.models;

import java.util.Locale;

public class EmployeeSettings {
    public static final String DB_TABLE_NAME = "employeeSettings";
    
	public EmployeeSettings(Locale locale) {
		this.locale = locale;
	}
	
	private Locale locale;
	private boolean fullscreen;

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
		return new EmployeeSettings(Locale.ENGLISH);
	}
}
