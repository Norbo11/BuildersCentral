package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private ArrayList<RequiredMaterial> requiredMaterials = new ArrayList<RequiredMaterial>();
    private JobGroup jobGroupDummy;
    private ArrayList<Assignment> assignments;
    
    /* Properties */

    public IntegerProperty jobGroupIdProperty() {
        return jobGroupId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public DoubleProperty labourPriceProperty() {
        return labourPrice;
    }

    public DoubleProperty materialPriceProperty() {
        return materialPrice;
    }
    
	/* Getters and setters */
    
    public JobGroup getJobGroupDummy() {
        return jobGroupDummy;
    }

    public void setJobGroupDummy(JobGroup jobGroupDummy) {
        this.jobGroupDummy = jobGroupDummy;
    }
    
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
	
	public ArrayList<Assignment> getAssignments() {
	    return assignments;
	}

	/* Instance methods */	

	/* Overrides */
	
	@Override
    public ArrayList<RequiredMaterial> getChildren() {
        return requiredMaterials;
    }
	
    public void setChildren(ArrayList<RequiredMaterial> requiredMaterials) {
        this.requiredMaterials = requiredMaterials;
    }
	
	@Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (jobGroupId,title,description,labourPrice,materialPrice) "
        + "VALUES (?,?,?,?,?)"
        , getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice());
    }
    
    @Override
    public void update() {   
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "jobGroupId=?,title=?,description=?,labourPrice=?,materialPrice=? "
        + "WHERE id = ?", getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice(), getId());
    }
    
    @Override
    public void loadFromResult(AbstractModel parent, ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "jobGroupId")) setJobGroupId(result.getInt("jobGroupId"));
        if (containsColumn(columns, "title")) setTitle(result.getString("title"));
        if (containsColumn(columns, "description")) setDescription(result.getString("description"));
        if (containsColumn(columns, "labourPrice")) setLabourPrice(result.getDouble("labourPrice"));
        if (containsColumn(columns, "materialPrice")) setMaterialPrice(result.getDouble("materialPrice"));
        
        setParent(parent);
        setChildren(RequiredMaterial.loadRequiredMaterialsForJob(this));
        loadAssignments();
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
	
	public ArrayList<Assignment> loadAssignments() {
	    assignments = Assignment.loadAssignmentsForJob(this);
	    return assignments;
	}

	/* Static methods */
	
	public static ArrayList<Job> loadJobsForJobGroup(JobGroup jobGroup) {		
		return loadList(jobGroup, loadAllModelsWhere(DB_TABLE_NAME, "jobGroupId", jobGroup.getId()));
	}
	
	/* Standard static methods */
	
	public static ArrayList<Job> loadList(AbstractModel parent, ResultSet result) {
		return loadList(parent, result, Job.class);
	}
}
