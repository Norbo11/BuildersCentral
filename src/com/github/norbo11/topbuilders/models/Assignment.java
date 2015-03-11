package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.models.enums.NotificationType;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;

public class Assignment extends AbstractModel {
    public static final String DB_TABLE_NAME = "assignments";
	private static ObservableList<Assignment> assignments = FXCollections.observableArrayList();
    
	private IntegerProperty employeeId = new SimpleIntegerProperty(0);
	private IntegerProperty jobId = new SimpleIntegerProperty(0);
	private DoubleProperty hourlyWage = new SimpleDoubleProperty(0);
	private LongProperty startTimestamp = new SimpleLongProperty();
	private LongProperty endTimestamp = new SimpleLongProperty();
	private BooleanProperty isCompleted = new SimpleBooleanProperty(false);
	private Employee employee = null;
	private Job job = null;
	private Project projectDummy = null;
	private JobGroup jobGroupDummy = null;
	
    /* Getters and setters */
	
    public Project getProjectDummy() {
        return projectDummy;
    }

    public JobGroup getJobGroupDummy() {
        return jobGroupDummy;
    }

    public void setProjectDummy(Project projectDummy) {
        this.projectDummy = projectDummy;
    }

    public void setJobGroupDummy(JobGroup jobGroupDummy) {
        this.jobGroupDummy = jobGroupDummy;
    }
    
    public int getJobId() {
		return jobId.get();
	}

    public void setJobId(int jobId) {
		this.jobId.set(jobId);
	}

	public double getHourlyWage() {
		return hourlyWage.get();
	}

	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage.set(hourlyWage);
	}

	public long getStartTimestamp() {
		return startTimestamp.get();
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp.set(startTimestamp);
	}

	public long getEndTimestamp() {
		return endTimestamp.get();
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp.set(endTimestamp);
	}

	public boolean isCompleted() {
		return isCompleted.get();
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted.set(isCompleted);
	}

	public int getEmployeeId() {
		return employeeId.get();
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId.set(employeeId);
	}

	/* Foreign key methods */
	
	public Employee getEmployee() {
        return employee == null ? loadEmployee() : employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
	
    public Employee loadEmployee() {
        employee = Employee.loadEmployeeForAssignment(this);
        return employee;
    }
    
	public Job getJob() {
	    if (!isDummy()) {
	        return job == null ? loadJob() : job;
	    } else return null;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public Job loadJob() {
    	job = Job.loadJobForAssignment(this);
    	return job;
    }
    
    public Project getProject() {
    	return getJob().getJobGroup().getProject();
    }
    
    /* Instance methods */
    
	public LocalDate getStartDate() {
		return DateTimeUtil.getDateFromTimestamp(getStartTimestamp());
	}
	
	public LocalDate getEndDate() {
		return DateTimeUtil.getDateFromTimestamp(getEndTimestamp());
	}
	        
	public String getCoWorkers() {
	    String coWorkers = "";
	    
	    if (!isDummy()) {
    	    //Go through all assignments of the job corresponding to this assignment, and add the ones that aren't this assignment (therefore adding all co-workers)
    	    for (Assignment assignment : getJob().getAssignments()) {
    	        if (assignment != this) {
    	            coWorkers += assignment.getEmployee() + "\n";
    	        }
    	    }    	    
	    }
	    
	    return coWorkers.trim(); //Get rid of last newline character
	}
	
	public String getMaterials() {
	    return isDummy() ? "" : getJob().getRequiredMaterialsString(false);
	}
	
	public String getDescription() {
	    return isDummy() ? "" : getJob().getDescription();
	}
	
	/* Overrides */

	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
	    if (containsColumn(columns, "id")) setId(result.getInt("id"));
	    if (containsColumn(columns, "employeeId")) setEmployeeId(result.getInt("employeeId"));
	    if (containsColumn(columns, "jobId")) setJobId(result.getInt("jobId"));
	    if (containsColumn(columns, "hourlyWage")) setHourlyWage(result.getDouble("hourlyWage"));
	    if (containsColumn(columns, "startTimestamp")) setStartTimestamp(result.getLong("startTimestamp"));
	    if (containsColumn(columns, "endTimestamp")) setEndTimestamp(result.getLong("endTimestamp"));
        if (containsColumn(columns, "isCompleted")) setCompleted(result.getBoolean("isCompleted"));
	}
    
    @Override
    public int add() {
        getJob().getAssignments().add(this);
        getEmployee().getAssignments().add(this);
        
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (employeeId,jobId,hourlyWage,startTimestamp,endTimestamp,isCompleted) "
        + "VALUES (?,?,?,?,?,?)"
        , getEmployeeId(), getJobId(), getHourlyWage(), getStartTimestamp(), getEndTimestamp(), isCompleted());
    }
    
    @Override
    public void update() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "employeeId=?,jobId=?,hourlyWage=?,startTimestamp=?,endTimestamp=?,isCompleted=? "
        + "WHERE id = ?", getEmployeeId(), getJobId(), getHourlyWage(), getStartTimestamp(), getEndTimestamp(), isCompleted(), getId());
    }

    @Override
    public void delete() {
        getJob().getAssignments().remove(this);
        getEmployee().getAssignments().remove(this);
        
        super.delete();
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
	    return getJob() + " for " + getEmployee();
	}
	
	@Override
	public boolean isDummy() {
	    return jobGroupDummy != null || projectDummy != null;
	}

	/* Static methods */
	
	public static ObservableList<Assignment> loadAssignments() {
        assignments = loadList(loadAllModels(DB_TABLE_NAME));
        return assignments;
    }

	public static ObservableList<Assignment> loadList(ResultSet result) {
		return loadList(result, Assignment.class);
	}

    public static ObservableList<Assignment> loadAssignmentsForJob(Job job) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "jobId", job.getId()));
    }
    
    public static ObservableList<Assignment> loadAssignmentsForEmployee(Employee employee) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId()));
    }
    
    public static ObservableList<Assignment> getModels() {
	    return assignments;
	}

	public void updateNewAssignmentNotification() {
		if (isCompleted()) {
			
			//Create an Assignment Completed notification for all managers
			for (Employee manager : Employee.getModels()) {
				if (manager.getUserType().isAtLeast(UserType.MANAGER)) {
		            Notification notification = new Notification();
		            notification.setNewModel(true);
		            notification.setEmployeeId(manager.getId());
		            notification.setTypeId(NotificationType.EMPLOYEE_ASSIGNMENT_COMPLETE.getId());
		            notification.setAssociatedId(getId());
		            notification.setTimestamp(DateTimeUtil.getCurrentTimestamp());
		            notification.save();
				}
			}
		} else {
			//Delete all notifications associated with this assignment
			Notification.deleteCorrespondingNotifications(this);
		}
	}

	public long calculateDaysLeft() {
		return Period.between(LocalDate.now(), getEndDate()).getDays();
	}

	public void updateAssignmentCloseToEndNotification() {
		Notification existingNotification = Notification.loadAssignmentCloseToEndNotificationForAssignment(this);
		
		//If there is no existing notification and the days left to the assignment are less than 7, create a new notification
		
		if (getEndDate() != null && calculateDaysLeft() < 7 && existingNotification == null) {
			Notification notification = new Notification();
            notification.setNewModel(true);
            notification.setEmployeeId(getEmployeeId());
            notification.setTypeId(NotificationType.ASSIGNMENT_CLOSE_TO_END.getId());
            notification.setAssociatedId(getId());
            notification.setTimestamp(DateTimeUtil.getCurrentTimestamp());
            notification.save();
		}
	}
}
