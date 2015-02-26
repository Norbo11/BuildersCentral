package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.util.Database;

public class JobGroup extends AbstractModel {
    public static final String DB_TABLE_NAME = "jobGroups";
    
    private IntegerProperty projectId = new SimpleIntegerProperty(0);
    private StringProperty groupName = new SimpleStringProperty("");
    private ArrayList<Job> jobs = null;
    private Project project;

    /* Properties */

    public IntegerProperty projectIdProperty() {
        return projectId;
    }

    public StringProperty groupNameProperty() {
        return groupName;
    }
    
	/* Getters and setters */

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
	
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    /* Forgeign key methods */
    
    public ArrayList<Job> getJobs() {
        return jobs  == null ? loadJobs() : jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
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
	}
    
    @Override
    public void save() {
        super.save();
        
        for (Job job : jobs) {
            job.save();
        }
    }
    
    @Override
    public void delete() {
        for (Job job : jobs) {
            job.delete();
        }
        
        super.delete();
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "projectId")) setProjectId(result.getInt("projectId"));
        if (containsColumn(columns, "groupName")) setGroupName(result.getString("groupName"));
    }
    
    public ArrayList<Job> loadJobs() {
        jobs = Job.loadJobsForJobGroup(this);
        return jobs;
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
	
	public static ArrayList<JobGroup> loadJobGroupsForProject(Project project) {
		return loadList(project, loadAllModelsWhere(DB_TABLE_NAME, "projectId", project.getId()));
	}
	
	/* Standard static methods */
	
	public static ArrayList<JobGroup> loadList(Project project, ResultSet result) {
		ArrayList<JobGroup> groups = loadList(result, JobGroup.class);
		for (JobGroup group : groups) {
		    group.setProject(project);
		}
		return groups;
	}
}
