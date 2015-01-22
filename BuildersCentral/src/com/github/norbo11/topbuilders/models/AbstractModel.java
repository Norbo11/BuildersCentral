package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;


public class AbstractModel {
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
    
    public void save() {
    	
    }
    
    public void loadFromResult(ResultSet result) throws SQLException {
    	
    }
    
    public String getDbTableName() {
		return null;
    }
	
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
	
    public static <T extends AbstractModel> Vector<AbstractModel> loadAllModels(final String DB_TABLE_NAME) {
        Vector<T> models = new Vector<T>();

        try {
            ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
            
            while (result.next())
            {
            	AbstractModel model = new AbstractModel();
            	model.loadFromResult(result);
            	models.add(model);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return models;
    }
    
	public static Vector<AbstractModel> loadAllModelsWhereId(final String DB_TABLE_NAME, String field, int id) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ?", id);
        Vector<AbstractModel> models = new Vector<AbstractModel>();
        
        try {
            while (result.next()) {
            	AbstractModel model = new AbstractModel();
            	model.loadFromResult(result);
            	models.add(model);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return models;
    }
}
