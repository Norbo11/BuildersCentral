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
    private static Vector<Project> projects = new Vector<Project>();
    
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
	
	/* Instance methods */
	
	public String getClientFullName() {
		return getClientFirstName() + " " + getClientLastName();
	}
	
	/* Override methods */
	
	@Override
    public Vector<JobGroup> getChildren() {
        return jobGroups;
    }
    
    public void setChildren(Vector<JobGroup> jobGroups) {
        this.jobGroups = jobGroups;
    }
	
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
    public void loadFromResult(AbstractModel parent, ResultSet result, String... columns) throws SQLException {  
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
        
        setChildren(JobGroup.loadJobGroupsForProject(this));
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
	
	public static Vector<Project> loadList(ResultSet result) {
		return loadList(null, result, Project.class);
	}

    public static void loadProjects() {
        projects = loadList(loadAllModels(DB_TABLE_NAME));
    }
    
    public static Vector<Project> getProjects() {
        return projects;
    }
}
