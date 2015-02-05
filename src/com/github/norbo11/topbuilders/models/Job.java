package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.util.Database;

public class Job extends AbstractModel {
    public static final String DB_TABLE_NAME = "jobs";
    
    private IntegerProperty jobGroupId = new SimpleIntegerProperty(0);
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private DoubleProperty labourPrice = new SimpleDoubleProperty(0);
    private DoubleProperty materialPrice = new SimpleDoubleProperty(0);
    
    public Job() {
    	super();
    }
    
    public Job(String title) {
    	super();
    	
    	setTitle(title);
    	setDummy(true);
    }
    
	/* Getters and setters */
    
    public int getJobGroupId() {
		return jobGroupId.get();
	}

	public void setJobGroupId(int jobGroupId) {
		this.jobGroupId.set(jobGroupId);
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public double getLabourPrice() {
		return labourPrice.get();
	}

	public void setLabourPrice(double labourPrice) {
		this.labourPrice.set(labourPrice);
	}

	public double getMaterialPrice() {
		return materialPrice.get();
	}

	public void setMaterialPrice(double materialPrice) {
		this.materialPrice.set(materialPrice);
	}    

	/* Instance methods */	

	public Vector<RequiredMaterial> getRequiredMaterials() {
		return RequiredMaterial.loadRequiredMaterialsForJob(this);
	}
	 
	/* Overrides */
    
	@Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (jobGroupId,title,description,labourPrice,materialPrice) "
        + "VALUES (?,?,?,?,?)"
        , getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice());
    }
    
    @Override
    public void save() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "jobGroupId=?,title=?,description=?,labourPrice=?,materialPrice=? "
        + "WHERE id = ?", getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice(), getId());
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "jobGroupId")) setJobGroupId(result.getInt("jobGroupId"));
        if (containsColumn(columns, "title")) setTitle(result.getString("title"));
        if (containsColumn(columns, "description")) setDescription(result.getString("description"));
        if (containsColumn(columns, "labourPrice")) setLabourPrice(result.getDouble("labourPrice"));
        if (containsColumn(columns, "materialPrice")) setMaterialPrice(result.getDouble("materialPrice"));

    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}

	/* Static methods */
	
	public static Vector<Job> loadJobsForJobGroup(JobGroup jobGroup) {
		return loadList(loadAllModelsWhere(DB_TABLE_NAME, "jobGroupId", jobGroup.getId()));
	}
	
	/* Standard static methods */
	
	public static Vector<Job> loadList(ResultSet result) {
		return loadList(result, Job.class);
	}
}
