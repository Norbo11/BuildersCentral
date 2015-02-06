package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.util.Database;

public class JobGroup extends AbstractModel {
    public static final String DB_TABLE_NAME = "jobGroups";
    
    private IntegerProperty projectId = new SimpleIntegerProperty(0);
    private StringProperty groupName = new SimpleStringProperty("");
    private Vector<Job> jobs = new Vector<Job>();
    
	/* Getters and setters */
    
    public JobGroup() {
        super();
    }
    
    public JobGroup(String groupName) {
        super();
        
        setGroupName(groupName);
    }

    public int getProjectId() {
		return projectId.get();
	}

	public void setProjectId(int projectId) {
		this.projectId.set(projectId);
	}

	public String getGroupName() {
		return groupName.get();
	}

	public void setGroupName(String groupName) {
		this.groupName.set(groupName);
	}
    
	public Vector<Job> getJobs() {
	    return jobs;
	}
	
	public void setJobs(Vector<Job> jobs) {
	    this.jobs = jobs;
	}
	
	/* Instance methods */	

	/* Overrides */
    
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (projectId,groupName) "
        + "VALUES (?,?)"
        , getProjectId(), getGroupName());
    }
    
    @Override
    public void update() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "projectId=?,groupName=? "
        + "WHERE id = ?", getProjectId(), getGroupName(), getId());
        
        for (Job job : getJobs()) {
        	job.save();
		}	
	}
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "projectId")) setProjectId(result.getInt("projectId"));
        if (containsColumn(columns, "groupName")) setGroupName(result.getString("groupName"));
        
        setJobs(Job.loadJobsForJobGroup(this));
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getGroupName();
	}

	/* Static methods */
	
	public static Vector<JobGroup> loadJobGroupsForProject(Project project) {
		return loadList(loadAllModelsWhere(DB_TABLE_NAME, "projectId", project.getId()));
	}
	
	/* Standard static methods */
	
	public static Vector<JobGroup> loadList(ResultSet result) {
		return loadList(result, JobGroup.class);
	}
}
