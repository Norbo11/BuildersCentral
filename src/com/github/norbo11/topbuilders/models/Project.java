package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Resources;


public class Project extends AbstractModel {
    public static final String DB_TABLE_NAME = "projects";    
    private static Vector<Project> projects;
    
    private BooleanProperty quoteRequested = new SimpleBooleanProperty(false);
    private BooleanProperty completed = new SimpleBooleanProperty(false);
    private StringProperty clientFirstName = new SimpleStringProperty("");
    private StringProperty clientLastName = new SimpleStringProperty("");
    
    private StringProperty firstLineAddress = new SimpleStringProperty("");
    private StringProperty secondLineAddress = new SimpleStringProperty("");
    private StringProperty city = new SimpleStringProperty("");
    private StringProperty postcode = new SimpleStringProperty("");

    private StringProperty contactNumber = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty projectDescription = new SimpleStringProperty("");
    private StringProperty projectNote = new SimpleStringProperty("");
    private Vector<JobGroup> jobGroups = new Vector<JobGroup>();
    
    
    public Project() {
    	super();
    }
    
    public Project(String firstLineAddress) {
    	super();
    	
    	setFirstLineAddress(firstLineAddress);
	}

    /* Getters and setters */
    
	public Vector<JobGroup> getJobGroups() {
        return jobGroups;
    }
	
	public void setJobGroups(Vector<JobGroup> jobGroups) {
        this.jobGroups = jobGroups;
    }

	public void setQuoteRequested(boolean quoteRequested) {
		this.quoteRequested.set(quoteRequested);
	}

	public boolean isQuoteRequested() {
		return quoteRequested.get();
	}

	public boolean isCompleted() {
		return completed.get();
	}

	public void setCompleted(boolean completed) {
		this.completed.set(completed);
	}

	public String getClientFirstName() {
		return clientFirstName.get();
	}

	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName.set(clientFirstName);
	}

	public String getClientLastName() {
		return clientLastName.get();
	}

	public void setClientLastName(String clientLastName) {
		this.clientLastName.set(clientLastName);
	}

	public String getFirstLineAddress() {
		return firstLineAddress.get();
	}

	public void setFirstLineAddress(String firstLineAddress) {
		this.firstLineAddress.set(firstLineAddress);
	}

	public String getSecondLineAddress() {
		return secondLineAddress.get();
	}

	public void setSecondLineAddress(String secondLineAddress) {
		this.secondLineAddress.set(secondLineAddress);
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public String getPostcode() {
		return postcode.get();
	}

	public void setPostcode(String postcode) {
		this.postcode.set(postcode);
	}

	public String getContactNumber() {
		return contactNumber.get();
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber.set(contactNumber);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getProjectDescription() {
		return projectDescription.get();
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription.set(projectDescription);
	}

	public String getProjectNote() {
		return projectNote.get();
	}

	public void setProjectNote(String projectNote) {
		this.projectNote.set(projectNote);
	}
	
	/* Instance methods */
	
	public String getClientFullName() {
		return getClientFirstName() + " " + getClientLastName();
	}
	
	/* Override methods */
	
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (quoteRequested,completed,clientFirstName,clientLastName,firstLineAddress,secondLineAddress,city,postcode,contactNumber,email,projectDescription,projectNote) "
        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
        , isQuoteRequested(), isCompleted(), getClientFirstName(), getClientLastName(), getFirstLineAddress(), getSecondLineAddress(), getCity(), getPostcode(), getContactNumber(), getEmail(), getProjectDescription(), getProjectNote());
    }
    
    @Override
    public void update() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "quoteRequested=?,completed=?,clientFirstName=?,clientLastName=?,firstLineAddress=?,secondLineAddress=?,city=?,postcode=?,contactNumber=?,email=?,projectDescription=?,projectNote=? "
        + "WHERE id = ?", isQuoteRequested(), isCompleted(), getClientFirstName(), getClientLastName(), getFirstLineAddress(), getSecondLineAddress(), getCity(), getPostcode(), getContactNumber(), getEmail(), getProjectDescription(), getProjectNote(), getId());
        
        for (JobGroup jobGroup : jobGroups) {
        	jobGroup.save();
        }
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {  
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "quoteRequested")) setQuoteRequested(result.getBoolean("quoteRequested"));
        if (containsColumn(columns, "completed")) setCompleted(result.getBoolean("completed"));
        if (containsColumn(columns, "clientFirstName")) setClientFirstName(result.getString("clientFirstName"));
        if (containsColumn(columns, "clientLastName")) setClientLastName(result.getString("clientLastName"));
        if (containsColumn(columns, "firstLineAddress")) setFirstLineAddress(result.getString("firstLineAddress"));
        if (containsColumn(columns, "secondLineAddress")) setSecondLineAddress(result.getString("secondLineAddress"));
        if (containsColumn(columns, "city")) setCity(result.getString("city"));
        if (containsColumn(columns, "postcode")) setPostcode(result.getString("postcode"));
        if (containsColumn(columns, "contactNumber")) setContactNumber(result.getString("contactNumber"));
        if (containsColumn(columns, "email")) setEmail(result.getString("email"));
        if (containsColumn(columns, "projectDescription")) setProjectDescription(result.getString("projectDescription"));
        if (containsColumn(columns, "projectNote")) setProjectNote(result.getString("projectNote"));
        
        setJobGroups(JobGroup.loadJobGroupsForProject(this));
    }

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getFirstLineAddress().equals("") ? Resources.getResource("quotes.unnamedProject") : getFirstLineAddress();
	}
	
	/* Static methods */
    
	/* Standard static methods */
	
	public static void setProjects(Vector<Project> projects) {
		Project.projects = projects;
	}
	
	public static Vector<Project> getProjects() {
		return projects;
	}
	
	public static void loadProjects() {
		setProjects(loadList(loadAllModels(DB_TABLE_NAME)));
	}
	
	public static Vector<Project> loadList(ResultSet result) {
		return loadList(result, Project.class);
	}

    public void updateJobGroup(String title, Vector<Job> jobs) {
        Vector<JobGroup> jobGroups = getJobGroups();
        JobGroup groupToUpdate = null;
        
        for (JobGroup group : jobGroups) {
            if (group.getGroupName().equals(title)) {
            	groupToUpdate = group;
                break;
            }
        }
        
        //If no matching job group was found, create one
        if (groupToUpdate == null) {
        	groupToUpdate = new JobGroup(title);
        	groupToUpdate.add();
        }
        
        groupToUpdate.setJobs(jobs);
        groupToUpdate.save();
    }
}
