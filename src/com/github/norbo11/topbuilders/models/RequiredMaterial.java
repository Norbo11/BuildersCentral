package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import com.github.norbo11.topbuilders.util.Database;

public class RequiredMaterial extends AbstractModel {
    public static final String DB_TABLE_NAME = "requiredMaterials";
    
    private IntegerProperty stockedMaterialId = new SimpleIntegerProperty(0);
    private IntegerProperty jobId = new SimpleIntegerProperty(0);
    private DoubleProperty quantityRequired = new SimpleDoubleProperty(0);

    
	/* Getters and setters */
    
	public int getStockedMaterialId() {
		return stockedMaterialId.get();
	}

	public void setStockedMaterialId(int stockedMaterialId) {
		this.stockedMaterialId.set(stockedMaterialId);
	}

	public int getJobId() {
		return jobId.get();
	}

	public void setJobId(int jobId) {
		this.jobId.set(jobId);
	}

	public double getQuantityRequired() {
		return quantityRequired.get();
	}

	public void setQuantityRequired(double quantityRequired) {
		this.quantityRequired.set(quantityRequired);
	}
    
	/* Instance methods */	

	public StockedMaterial getStockedMaterial() {
		return StockedMaterial.loadStockedMaterialForRequiredMaterial(this);
	}
	
	public static Vector<RequiredMaterial> loadRequiredMaterialsForJob(Job job) {
		return loadList(loadAllModelsWhere(DB_TABLE_NAME, "jobId", job.getId()));
	}

	/* Overrides */
	
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (stockedMaterialId,jobId,quantityRequired) "
        + "VALUES (?,?,?)"
        , getStockedMaterialId(), getJobId(), getQuantityRequired());
    }
    
    @Override
    public void save() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "stockedMaterialId=?,jobId=?,quantityRequired=? "
        + "WHERE id = ?", getStockedMaterialId(), getJobId(), getId());
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "stockedMaterialId")) setStockedMaterialId(result.getInt("stockedMaterialId"));
        if (containsColumn(columns, "jobId")) setJobId(result.getInt("jobId"));
        if (containsColumn(columns, "quantityRequired")) setQuantityRequired(result.getDouble("quantityRequired"));
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return "REQUIRED MATERIAL TOSTRING";
	}

	/* Static methods */
	
	/* Standard static methods */
	
	public static Vector<RequiredMaterial> loadList(ResultSet result) {
		return loadList(result, RequiredMaterial.class);
	}
}
