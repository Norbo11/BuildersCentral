package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;

import com.github.norbo11.topbuilders.models.enums.QuoteSettingType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.Settings;


public class Project extends AbstractModel {
    public static final String DB_TABLE_NAME = "projects";        
    private static ArrayList<Project> projects = null;
    
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
    
    private Settings<QuoteSetting> settings = new Settings<QuoteSetting>(this, QuoteSetting.class);
    private ArrayList<JobGroup> jobGroups = null;
    
    /* Properties */

    public BooleanProperty quoteRequestedProperty() {
        return quoteRequested;
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public StringProperty clientFirstNameProperty() {
        return clientFirstName;
    }

    public StringProperty clientLastNameProperty() {
        return clientLastName;
    }

    public StringProperty firstLineAddressProperty() {
        return firstLineAddress;
    }

    public StringProperty secondLineAddressProperty() {
        return secondLineAddress;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty postcodeProperty() {
        return postcode;
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty projectDescriptionProperty() {
        return projectDescription;
    }

    public StringProperty projectNoteProperty() {
        return projectNote;
    }

    /* Getters and setters */
    
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
	
	public Settings<QuoteSetting> getSettings() {
	    return settings;
	}
	
	/* Forgeign key methods */
	
	public ArrayList<JobGroup> getJobGroups() {
        return jobGroups == null ? loadJobGroups() : jobGroups;
    }

    public void setJobGroups(ArrayList<JobGroup> jobGroups) {
        this.jobGroups = jobGroups;
    }
    
    public ArrayList<JobGroup> loadJobGroups() {
        jobGroups = JobGroup.loadJobGroupsForProject(this);
        return jobGroups;
    }

    public Settings<QuoteSetting> loadSettings() {
        settings = QuoteSetting.loadQuoteSettingsForProject(this);
        return settings;
    }
    
	/* Instance methods */
	
    public String getClientFullName() {
		return getClientFirstName() + " " + getClientLastName();
	}
	
    public ArrayList<Job> getAllJobs() {
        ArrayList<Job> jobs = new ArrayList<Job>();
        
        for (JobGroup group : jobGroups) {
            jobs.addAll(group.loadJobs());
        }
        
        return jobs;
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
    }
    
    @Override
    public void save() {
        super.save();
        
        for (JobGroup group : jobGroups) {
           group.save(); 
        }
    }
    
    @Override
    public void delete() {
        for (JobGroup group : jobGroups) {
            group.delete(); 
        }
        
        settings.delete();
        super.delete();
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
	
	public static ArrayList<Project> loadList(ResultSet result) {
		return loadList(result, Project.class);
	}

    public static ArrayList<Project> loadProjects() {
        projects = loadList(loadAllModels(DB_TABLE_NAME));
        return projects;
    }
    
    public static ArrayList<Project> getProjects() {
        return projects == null ? loadProjects() : projects;
    }

    public void populateTreeTable(TreeView<Job> table) {
        table.getRoot().getChildren().clear();
        
        if (getSettings().getBoolean(QuoteSettingType.GROUPS_ENABLED)) {
            //Go through each job group in the project
            for (JobGroup group : getJobGroups()) {
                
                /* Create a dummy Job object, which will be used in a TreeItem to represent this group. This is necessary
                 * as JavaFX does not support more than one data type in a TreeTableView */
                
                Job jobDummy = new Job();
                jobDummy.setDummy(true);
                jobDummy.setJobGroupDummy(group);
                jobDummy.setTitle(group.getGroupName());
                
                //Create the actual TreeItem
                TreeItem<Job> groupRoot = new TreeItem<Job>(jobDummy);
                groupRoot.setExpanded(true);
                
                //Go through each job inside the ACTUAL job group object and add them to the above tree item
                for (Job job : group.getJobs()) {
                    groupRoot.getChildren().add(new TreeItem<Job>(job));
                }
                
                table.getRoot().getChildren().add(groupRoot);
            }
        } else {
            for (Job job : getAllJobs()) {
                table.getRoot().getChildren().add(new TreeItem<Job>(job));
            }
        }
    }
    
    public void populateTreeTable(TreeTableView<Job> table) {
        table.getRoot().getChildren().clear();
        
        if (getSettings().getBoolean(QuoteSettingType.GROUPS_ENABLED)) {
            //Go through each job group in the project
            for (JobGroup group : getJobGroups()) {
                
                /* Create a dummy Job object, which will be used in a TreeItem to represent this group. This is necessary
                 * as JavaFX does not support more than one data type in a TreeTableView */
                
                Job jobDummy = new Job();
                jobDummy.setDummy(true);
                jobDummy.setJobGroupDummy(group);
                jobDummy.setTitle(group.getGroupName());
                
                //Create the actual TreeItem
                TreeItem<Job> groupRoot = new TreeItem<Job>(jobDummy);
                groupRoot.setExpanded(true);
                
                //Go through each job inside the ACTUAL job group object and add them to the above tree item
                for (Job job : group.getJobs()) {
                    groupRoot.getChildren().add(new TreeItem<Job>(job));
                }
                
                table.getRoot().getChildren().add(groupRoot);
            }
        } else {
            for (Job job : getAllJobs()) {
                table.getRoot().getChildren().add(new TreeItem<Job>(job));
            }
        }
    }
}
