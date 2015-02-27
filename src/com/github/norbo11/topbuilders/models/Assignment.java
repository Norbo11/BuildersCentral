package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class Assignment extends AbstractModel {
    public static final String DB_TABLE_NAME = "assignments";
	private static ArrayList<Assignment> assignments;
    
	private IntegerProperty employeeId = new SimpleIntegerProperty(0);
	private IntegerProperty jobId = new SimpleIntegerProperty(0);
	private DoubleProperty hourlyWage = new SimpleDoubleProperty(0);
	private LongProperty startTimestamp = new SimpleLongProperty();
	private LongProperty endTimestamp = new SimpleLongProperty();
	private BooleanProperty isCompleted = new SimpleBooleanProperty(false);
	private Employee employee = null;
	private Job job = null;
	
    /* Getters and setters */
    
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

	public void setIsCompleted(boolean isCompleted) {
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
        return job == null ? loadJob() : job;
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
	
	/* Overrides */

	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
	    if (containsColumn(columns, "id")) setId(result.getInt("id"));
	    if (containsColumn(columns, "employeeId")) setEmployeeId(result.getInt("employeeId"));
	    if (containsColumn(columns, "jobId")) setJobId(result.getInt("jobId"));
	    if (containsColumn(columns, "hourlyWage")) setHourlyWage(result.getDouble("hourlyWage"));
	    if (containsColumn(columns, "startTimestamp")) setStartTimestamp(result.getLong("startTimestamp"));
	    if (containsColumn(columns, "endTimestamp")) setEndTimestamp(result.getLong("endTimestamp"));
        if (containsColumn(columns, "isCompleted")) setIsCompleted(result.getBoolean("isCompleted"));
	}
    
    @Override
    public int add() {
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
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}

	/* Static methods */
	
	public static ArrayList<Assignment> loadAssignments() {
        assignments = loadList(loadAllModels(DB_TABLE_NAME));
        return assignments;
    }

	public static ArrayList<Assignment> loadList(ResultSet result) {
		return loadList(result, Assignment.class);
	}

    public static ArrayList<Assignment> loadAssignmentsForJob(Job job) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "jobId", job.getId()));
    }
    
    public static ArrayList<Assignment> loadAssignmentsForEmployee(Employee employee) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId()));
    }
    
    public static ArrayList<Assignment> getModels() {
	    return assignments;
	}
}
