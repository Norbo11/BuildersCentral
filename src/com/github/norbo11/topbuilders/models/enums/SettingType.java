package com.github.norbo11.topbuilders.models.enums;

public enum SettingType {
    FULLSCREEN(0, false), LOCALE(1, "en");
    
    private int settingId;
    private Object defaultValue;
    
    SettingType(int settingId, Object defaultValue) {
        this.settingId = settingId;
        this.defaultValue = defaultValue;
    }
    
    public int getSettingId() {
        return settingId;
    }
    
    public Object getDefaultValue() {
		return defaultValue;
	}

	public static SettingType getSettingType(int id) {
        return values()[id];
    }
}
