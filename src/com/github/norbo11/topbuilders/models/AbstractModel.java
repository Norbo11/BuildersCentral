package com.github.norbo11.topbuilders.models;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;


public abstract class AbstractModel {
    private IntegerProperty id;
    private boolean newModel;
    
    public AbstractModel() {
        this(0);
    }
    
    public AbstractModel(int id) {
        this.id = new SimpleIntegerProperty(id);
    }
    
    /* Properties */
    
    public IntegerProperty idProperty() {
        return id;
    }
    
    /* Getters and setters */
    
    public int getId() {
        return id.get();
    }
    
    public boolean isNewModel() {
		return newModel;
	}

	public void setNewModel(boolean newModel) {
		this.newModel = newModel;
	}

	public void setId(int id) {
        this.id.set(id);
    }
        
    /* Abstract methods */
    
    public abstract int add();
    
    public abstract void update();
    
    //Sets all of the properties for this model as obtained by the ResultSet - will only load the specified columns
    public abstract void loadFromResult(ResultSet result, String... columns) throws SQLException;
    
    public abstract String getDbTableName();
    
    public boolean isDummy() {
        return false;
    }
            
    /* Instance methods */
    
    public void save() {
        //Add or update the model accordingly
        if (newModel) {
            setId(add());
            newModel = false;
        } else {
            update();
        }
    }

    public void delete() {
        //Delete model if it isn't new
        if (!newModel) {
            Database.executeUpdate("DELETE FROM " + getDbTableName() + " WHERE id = ?", getId());
            Notification.deleteCorrespondingNotifications(this);
        }
    }
    
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
    public void loadFromId(int id) {
        ResultSet result = loadAllModelsWhere(getDbTableName(), "id", id);
        
        try {
            if (result.next()) {
                loadFromResult(result);
            }
        } catch (SQLException e) {
			Log.error("Error loading model from ID " + this);
        }
    }
    
    /* Static methods */
	
    protected static ResultSet loadAllModels(final String DB_TABLE_NAME) {
    	return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
    }
    
    protected static ResultSet loadAllModelsWhereOrdered(final String DB_TABLE_NAME, String field, Object param, String sortField, boolean desc) {
        return Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE " + field + " = ? ORDER BY " + sortField + " " + (desc ? "DESC" : "ASC"), param);
    }
    
    protected static ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String field, Object param) {
        return loadAllModelsWhere(DB_TABLE_NAME, new String[] { field }, new Object[] { param });
    }
    
    protected static ResultSet loadAllModelsWhere(final String DB_TABLE_NAME, String[] field, Object[] param) {
        if (field.length != param.length) throw new IllegalArgumentException();
        
        int noOfParams = field.length;
        String query = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ";
        
        for (int i = 0; i < noOfParams; i++) {
            query += field[i] + "=?";
            if (i != noOfParams - 1) query += " AND "; //Add this at the end of each WHERE clause, except the last one
        }
        
        return Database.executeQuery(query, param);
    }
    
	@SuppressWarnings("unchecked")
    //Returns a list of model instances based on the given result of a query. The class corresponding to the models must be supplied to make use of reflection
    protected static <T extends AbstractModel> ObservableList<T> loadList(ResultSet result, Class<T> clazz) {
        //Create a list to store loaded models which we will eventually return
    	ObservableList<T> models = FXCollections.observableArrayList();
        		
        try {
            //Go through all loaded rows
    		while (result.next()) {
    		    //Create a variable to hold the model object which we will eventually return, and initialise it to a new instance of the class
    			T model = clazz.newInstance();
    			
    			//Create a variable to store the currently existing models
    			ObservableList<T> modelList = null;
    			
    			try {
    			    //Invoke the static method getModels() inside the desired class
    			    modelList = (ObservableList<T>) clazz.getMethod("getModels").invoke(null); 
    			} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
    				//Print an error if one occurred
    			    Log.error("Error loading list of models " + clazz, e);
    			}
    			
    			//Try to find an existing model witin the model list
    			for (AbstractModel existingModel : modelList) {
    			    //If a model ID matches the ID of the current row in the result set, then set the model variable which we are returning to the existing model object
    				if (existingModel.getId() == result.getInt("id")) {
    					model = (T) existingModel;
    				}
    			}
    			
    			//If no existing model was found (i.e. if the model variable still has an ID of 0, which would be the case as we created a new instance of the class)
    			if (model.getId() == 0) {
    			    //Load the row data into the new model instance
    				model.loadFromResult(result);
    				
    				//Add the model to the list of existing models
    				modelList.add(model);
    			}
    
    			//Add the model to the list of models which we are returning
    			models.add(model);
    		}
    	} catch (SQLException | InstantiationException | IllegalAccessException e) {
    	    //Print an error if one occurred
            Log.error("Error loading list of models " + clazz, e);
    	}
        
        //Return the models list consisting of existing instances of the required models or new instances based on the logic above
        return models;
    }
	
	protected static <T extends AbstractModel> T loadOne(ResultSet result, Class<T> clazz) {
		ObservableList<T> models = loadList(result, clazz);
        
        if (models.size() != 0) return models.get(0);
        else return null;
    }
}
