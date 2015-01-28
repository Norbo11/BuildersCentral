package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.StringHelper;


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
    
    //Sets all of the properties for this model as obtained by the ResultSet - will only load the specified columns
    public abstract void loadFromResult(ResultSet result, String... columns) throws SQLException;
    
    public abstract String getDbTableName();
        
    /* Instance methods */
    
    //This is called by loadFromResult - if no columns were specified, then an empty array will be passed here, for which we check and return true (because that
    //means all columns were requested
    public boolean containsColumn(String[] columns, String needle) {
        if (columns.length == 0) return true;
        
        for (String column : columns) {
            if (column.equals(needle)) return true;
        }
        
        return false;
    }

    //Load specified columns
    public void loadFromId(int id, String... columns) {
        String columnString = columns.length > 0 ? StringHelper.join(columns, ",") : "*";
        ResultSet result = Database.executeQuery("SELECT " + columnString + " FROM " + getDbTableName() + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                loadFromResult(result, columns);
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
	
    protected static ResultSet loadAllModels(final String DB_TABLE_NAME) {
    	return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
    }
    
    protected static <T> ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String field, T param, String sortField, boolean desc) {
        return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ? ORDER BY " + sortField + " " + (desc ? "DESC" : "ASC"), param);
    }
    
    protected static <T> ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String field, T param) {
        return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ?", param);
    }
    
	protected static <T extends AbstractModel> Vector<T> loadList(ResultSet result, Class<T> clazz) {
		Vector<T> models = new Vector<T>();
        		
        try {
			while (result.next()) {
				T model = clazz.newInstance();
				model.loadFromResult(result);
				models.add(model);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			Log.error(e);
		}
        return models;
	}
}
