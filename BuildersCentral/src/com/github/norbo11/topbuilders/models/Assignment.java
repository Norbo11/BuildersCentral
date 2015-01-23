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

import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Log;

public class Assignment extends AbstractModel {
    public static final String DB_TABLE_NAME = "assignments";
	
	private IntegerProperty employeeId = new SimpleIntegerProperty(0);
	private IntegerProperty jobId = new SimpleIntegerProperty(0);
	private DoubleProperty hourlyWage = new SimpleDoubleProperty(0);
	private ObjectProperty<LocalDateTime> startDate = new SimpleObjectProperty<LocalDateTime>();
	private ObjectProperty<LocalDateTime> endDate = new SimpleObjectProperty<LocalDateTime>();
	private BooleanProperty isCompleted = new SimpleBooleanProperty(false);
	
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
	
	public void setEmployeeId(int employeeId) {
		this.employeeId.set(employeeId);
	}

	/* Overrides */


	@Override
	public void loadFromResult(ResultSet result) throws SQLException {
	    setId(result.getInt("id"));
	    setEmployeeId(result.getInt("employeeId"));
        setJobId(result.getInt("jobId"));
        setHourlyWage(result.getDouble("hourlyWage"));
        setStartDate(DateTimeUtil.getDateTimeFromTimestamp(result.getString("startDate")));
        setEndDate(DateTimeUtil.getDateTimeFromTimestamp(result.getString("endDate")));
        setIsCompleted(result.getBoolean("isCompleted"));
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	/* Static methods */
	
	public static Vector<Assignment> loadAll() {
		return loadList(loadAllModels(DB_TABLE_NAME));
	}
	
	public static <T> Vector<Assignment> loadAllWhere(String field, T id) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, field, id));
    }
	
	public static Vector<Assignment> loadList(ResultSet result) {
		Vector<Assignment> assignments = new Vector<Assignment>();
        
        try {
			while (result.next()) {
				Assignment assignment = new Assignment();
				assignment.loadFromResult(result);
				assignments.add(assignment);
			}
		} catch (SQLException e) {
			Log.error(e);
		}
        return assignments;
	}
	
}
