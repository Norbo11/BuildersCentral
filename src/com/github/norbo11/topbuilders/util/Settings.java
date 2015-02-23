package com.github.norbo11.topbuilders.util;

import java.util.ArrayList;
import java.util.Locale;

import com.github.norbo11.topbuilders.models.AbstractModel;
import com.github.norbo11.topbuilders.models.AbstractSetting;
import com.github.norbo11.topbuilders.models.enums.AbstractSettingType;
import com.github.norbo11.topbuilders.models.enums.EmployeeSettingType;

public class Settings<T extends AbstractSetting> {
    private ArrayList<T> settings;
    private Class<T> clazz;
    private AbstractModel parentModel;
    
    public Settings(AbstractModel parentModel, ArrayList<T> settings, Class<T> clazz) {
        this.settings = settings;
        this.clazz = clazz;
        this.parentModel = parentModel;
    }

    public Settings(AbstractModel parentModel, Class<T> clazz) {
        this(parentModel, new ArrayList<T>(), clazz);
    }

    public T getSetting(AbstractSettingType type) {
        for (T setting : settings) {
            if (setting.getSettingTypeId() == type.getSettingTypeId()) return setting;
        }
        
        //If none was found, create one
        try {
            T setting = clazz.newInstance();
            setting.setNewModel(true);
            setting.setModelId(parentModel.getId());
            setting.setValue(type.getDefaultValue() + "");
            setting.setSettingTypeId(type.getSettingTypeId());
            settings.add(setting);
            return setting;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(AbstractSettingType setting, Object newValue) {
        getSetting(setting).setValue(newValue.toString());
    }

    public void save() {
        for (T setting : settings) {
            setting.save();
        }
    }

    public void delete() {
        for (T setting : settings) {
            setting.delete();
        }
    }
    
    /* Settin getters */
    
    public String getString(AbstractSettingType type) {
        return getSetting(type).getValue();
    }
    
    public boolean getBoolean(AbstractSettingType type) {
        return Boolean.valueOf(getString(type));
    }

    public Locale getLocale(EmployeeSettingType type) {
        return Locale.forLanguageTag(getString(type));
    } 
}
