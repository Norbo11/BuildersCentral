package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.util.Database;

public abstract class AbstractSetting extends AbstractModel {

    private IntegerProperty modelId = new SimpleIntegerProperty(0);
    private IntegerProperty settingTypeId = new SimpleIntegerProperty(0);
    private StringProperty value = new SimpleStringProperty("");
    private String modelField;
    
    public AbstractSetting(String modelField) {
        this.modelField = modelField;
    }
    
    /* Getters and Setters */
    
    public int getModelId() {
        return modelId.get();
    }

    public int getSettingTypeId() {
        return settingTypeId.get();
    }

    public String getValue() {
        return value.get();
    }
    
    public void setModelId(int modelId) {
        this.modelId.set(modelId);
    }

    public void setSettingTypeId(int settingTypeId) {
        this.settingTypeId.set(settingTypeId);
    }

    public void setValue(String value) {
        this.value.set(value);
    }
    
    /* Properties */

    public IntegerProperty modelIdProperty() {
        return modelId;
    }

    public IntegerProperty settingTypeIdProperty() {
        return settingTypeId;
    }

    public StringProperty valueProperty() {
        return value;
    }
    
    /* Instance methods */
    
    
    
    /* Override methods */
    
    @Override
    public int add() {        
        return Database.executeUpdate("INSERT INTO " + getDbTableName() + " (" + modelField + ", settingTypeId, value) VALUES (?,?,?)", 
        getModelId(), getSettingTypeId(), getValue());
    }
    
    @Override
    public void update() {
        Database.executeUpdate("UPDATE " + getDbTableName() + " SET "
        + modelField + "=?,settingTypeId=?,value=? "
        + "WHERE id = ?", getModelId(), getSettingTypeId(), getValue(), getId()); 
    }

    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, modelField)) setModelId(result.getInt(modelField));
        if (containsColumn(columns, "settingTypeId")) setSettingTypeId(result.getInt("settingTypeId"));
        if (containsColumn(columns, "value")) setValue(result.getString("value"));
    }
}
