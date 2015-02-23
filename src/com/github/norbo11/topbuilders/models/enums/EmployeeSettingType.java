package com.github.norbo11.topbuilders.models.enums;

public enum EmployeeSettingType implements AbstractSettingType {
    FULLSCREEN(0, false), LOCALE(1, "en");
    
    private int settingTypeId;
    private Object defaultValue;
    
    EmployeeSettingType(int settingTypeId, Object defaultValue) {
        this.settingTypeId = settingTypeId;
        this.defaultValue = defaultValue;
    }

    @Override
    public int getSettingTypeId() {
        return settingTypeId;
    }
    
    @Override
    public Object getDefaultValue() {
		return defaultValue;
	}

	public static EmployeeSettingType getSettingType(int id) {
        return values()[id];
    }
}
