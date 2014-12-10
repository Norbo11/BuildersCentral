package com.github.norbo11.topbuilders.models;

import java.util.Locale;

public class UserSettings {
	
	public UserSettings(Locale locale) {
		this.locale = locale;
	}
	
	private Locale locale;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public static UserSettings load(User user) {
		return new UserSettings(Locale.ENGLISH);
	}
}
