package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;


public abstract class AbstractModel {
    private IntegerProperty id;
    private boolean dummy;
    
    public AbstractModel() {
        this(0);
    }
    
    public AbstractModel(int id) {
        this.id = new SimpleIntegerProperty(id);
    }
    
    /* Getters and setters */

    public int getId() {
        return id.get();
    }
    
    public void setId(int id) {
        this.id.set(id);
    }
        
    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }
    
    /* Abstract methods */
    
    public abstract void add();
    
    public abstract void save();
    
    public abstract void loadFromResult(ResultSet result) throws SQLException;
    
    public abstract String getDbTableName();
	
    /* Instance methods */

    public void loadFromId(int id) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + getDbTableName() + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                loadFromResult(result);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    public void delete() {
        Database.executeUpdate("DELETE FROM " + getDbTableName() + " WHERE id = ?", getId());
    }
    
    /* Overrides */

	@Override
    public boolean equals(Object o) {
        if (o instanceof AbstractModel) {
            AbstractModel e = (AbstractModel) o;
            return getId() == e.getId();
        }
        
        return false;
    }
	
    public static ResultSet loadAllModels(final String DB_TABLE_NAME) {
    	return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
    }
    
    public static <T> ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String field, T param, String sortField, boolean desc) {
        return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ? ORDER BY " + sortField + " " + (desc ? "DESC" : "ASC"), param);
    }
    
	public static <T> ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String field, T param) {
        return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ?", param);
    }
}
