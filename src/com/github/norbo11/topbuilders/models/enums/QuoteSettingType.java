package com.github.norbo11.topbuilders.models.enums;

public enum QuoteSettingType implements AbstractSettingType {
    MATERIALS_ENABLED(0, true), 
    MATERIALS_PRICE_ENABLED(1, true),
    LABOUR_PRICE_ENABLED(2, true),
    JOB_DESCRIPTIONS_ENABLED(3, true),
    GROUPS_ENABLED(4, true);
    
    private int settingTypeId;
    private Object defaultValue;
    
    QuoteSettingType(int settingId, Object defaultValue) {
        this.settingTypeId = settingId;
        this.defaultValue = defaultValue;
    }
    
    public int getSettingTypeId() {
        return settingTypeId;
    }
    
    public Object getDefaultValue() {
        return defaultValue;
    }

    public static QuoteSettingType getSettingType(int id) {
        return values()[id];
    }
}
