package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.util.Database;

public class JobGroup extends AbstractModel {
    public static final String DB_TABLE_NAME = "jobGroups";
    private static ObservableList<JobGroup> jobGroups = FXCollections.observableArrayList();
    
    private IntegerProperty projectId = new SimpleIntegerProperty(0);
    private StringProperty groupName = new SimpleStringProperty("");
    private ObservableList<Job> jobs = null;
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
    
    /* Forgeign key methods */

	//Getting & settings
	
	public Project getProject() {
	    return project == null ? loadProject() : project;
	}
	
	public void setProject(Project project) {
	    this.project = project;
	}

    public ObservableList<Job> getJobs() {
        return jobs == null ? loadJobs() : jobs;
    }

    public void setJobs(ObservableList<Job> jobs) {
        this.jobs = jobs;
    }
    
    //Loading
    
    public ObservableList<Job> loadJobs() {
        jobs = Job.loadJobsForJobGroup(this);
        return jobs;
    }
    
    public Project loadProject() {
        project = Project.loadProjectForJobGroup(this);
        return project;
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
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getGroupName();
	}

	/* Static methods */
	
	public static ObservableList<JobGroup> loadJobGroupsForProject(Project project) {
		return loadList(project, loadAllModelsWhere(DB_TABLE_NAME, "projectId", project.getId()));
	}
	
	/* Standard static methods */
	
	public static ObservableList<JobGroup> loadList(Project project, ResultSet result) {
		ObservableList<JobGroup> groups = loadList(result, JobGroup.class);
		for (JobGroup group : groups) {
		    group.setProject(project);
		}
		return groups;
	}

	public static JobGroup loadJobGroupForJob(Job job) {
		return loadOne(loadAllModelsWhere(DB_TABLE_NAME, "id", job.getJobGroupId()), JobGroup.class);
	}
	
	public static ObservableList<JobGroup> getModels() {
	    return jobGroups;
	}
}
