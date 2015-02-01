package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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
	
	private IntegerProperty employeeId = new SimpleIntegerProperty(0);
	private IntegerProperty jobId = new SimpleIntegerProperty(0);
	private DoubleProperty hourlyWage = new SimpleDoubleProperty(0);
	private LongProperty startTimestamp = new SimpleLongProperty();
	private LongProperty endTimestamp = new SimpleLongProperty();
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
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
	    if (containsColumn(columns, "id")) setId(result.getInt("id"));
	    if (containsColumn(columns, "employeeId")) setEmployeeId(result.getInt("employeeId"));
	    if (containsColumn(columns, "jobId")) setJobId(result.getInt("jobId"));
	    if (containsColumn(columns, "hourlyWage")) setHourlyWage(result.getDouble("hourlyWage"));
	    if (containsColumn(columns, "startDate")) setStartTimestamp(result.getLong("startDate"));
	    if (containsColumn(columns, "endDate")) setEndTimestamp(result.getLong("endDate"));
        if (containsColumn(columns, "isCompleted")) setIsCompleted(result.getBoolean("isCompleted"));
	}
	
    @Override
    public int add() {
        return 0;
        // TODO Auto-generated method stub
        
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
		return loadList(result, Assignment.class);
	}
}
