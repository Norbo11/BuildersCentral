package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Vector;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Log;

public class Assignment extends AbstractModel {
    public static final String DB_TABLE_NAME = "assignments";
	
	private IntegerProperty employeeId;
	private IntegerProperty jobId;
	private DoubleProperty hourlyWage;
	private ObjectProperty<LocalDateTime> startDate;
	private ObjectProperty<LocalDateTime> endDate;
	private BooleanProperty isCompleted;
	
    public Assignment(int id, int employeeId, int jobId, double hourlyWage, LocalDateTime startDate, LocalDateTime endDate, boolean isCompleted) {
        super(id);
        
        this.employeeId = new SimpleIntegerProperty(employeeId);
        this.jobId = new SimpleIntegerProperty(jobId);
        this.hourlyWage = new SimpleDoubleProperty(hourlyWage);
        this.startDate = new SimpleObjectProperty<LocalDateTime>(startDate);
        this.endDate = new SimpleObjectProperty<LocalDateTime>(endDate);
        this.isCompleted = new SimpleBooleanProperty(isCompleted);
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

	public LocalDateTime getStartDate() {
		return startDate.get();
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate.set(startDate);
	}

	public LocalDateTime getEndDate() {
		return endDate.get();
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate.set(endDate);
	}

	public boolean getIsCompleted() {
		return isCompleted.get();
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted.set(isCompleted);
	}

	public IntegerProperty getEmployeeId() {
		return employeeId;
	}
	
	private static Assignment getAssignmentFromResult(ResultSet result) throws SQLException {
	    int id = result.getInt("id");
	    int employeeId = result.getInt("employeeId");
        int jobId = result.getInt("jobId");
        double hourlyWage = result.getDouble("hourlyWage");
        LocalDateTime startDate = DateTimeUtil.getDateTimeFromTimestamp(result.getString("startDate"));
        LocalDateTime endDate =  DateTimeUtil.getDateTimeFromTimestamp(result.getString("endDate"));
        boolean isCompleted = result.getBoolean("isCompleted");
        
        return new Assignment(id, employeeId, jobId, hourlyWage, startDate, endDate, isCompleted);
	}
	
	
	public static Vector<Assignment> getAssignmentsFromEmployeeId(int employeeId) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE employeeId = ?", employeeId);
        Vector<Assignment> assignments = new Vector<Assignment>();
        
        try {
            while (result.next()) {
                assignments.add(getAssignmentFromResult(result));
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

	public static Assignment getAssignmentFromId(int id) {
		ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                return getAssignmentFromResult(result);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
	}
}
