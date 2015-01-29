package com.github.norbo11.topbuilders.models.enums;

public enum QuantityType {
	METER(0);
	
    private int id;
    
    QuantityType(int settingId) {
        this.id = settingId;
    }
    
    public int getId() {
        return id;
    }

	public static QuantityType getQuantityType(int id) {
        return values()[id];
    }
}
