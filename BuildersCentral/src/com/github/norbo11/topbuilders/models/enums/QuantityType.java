package com.github.norbo11.topbuilders.models.enums;

public enum QuantityType {
	METER(0);
	
    private int settingId;
    
    QuantityType(int settingId) {
        this.settingId = settingId;
    }
    
    public int getSettingId() {
        return settingId;
    }

	public static QuantityType getSettingType(int id) {
        return values()[id];
    }
}
