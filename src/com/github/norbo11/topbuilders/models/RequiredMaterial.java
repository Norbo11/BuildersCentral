package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.util.Database;

public class RequiredMaterial extends AbstractModel {
    public static final String DB_TABLE_NAME = "requiredMaterials";
    private static ObservableList<RequiredMaterial> requiredMaterials = FXCollections.observableArrayList();
    
    private IntegerProperty stockedMaterialId = new SimpleIntegerProperty(0);
    private IntegerProperty jobId = new SimpleIntegerProperty(0);
    private DoubleProperty quantityRequired = new SimpleDoubleProperty(0);
    private StockedMaterial stockedMaterial;
    private Job job;
    
    /* Properties */
    
    public IntegerProperty stockedMaterialIdProperty() {
        return stockedMaterialId;
    }

    public IntegerProperty jobIdProperty() {
        return jobId;
    }

    public DoubleProperty quantityRequiredProperty() {
        return quantityRequired;
    }
    
	/* Getters and setters */
    
	public StockedMaterial getStockedMaterial() {
		return stockedMaterial;
	}

	public void setStockedMaterial(StockedMaterial stockedMaterial) {
		this.stockedMaterial = stockedMaterial;
	}
    
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
	
	/* Foreign model methods */
	
	public Job getJob() {
        return job == null ? loadJob() : job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
    
    public StockedMaterial loadStockedMaterial() {
        stockedMaterial = StockedMaterial.loadStockedMaterialForRequiredMaterial(this);
        return stockedMaterial;
    }
    
    public Job loadJob() {
        job = Job.loadJobForRequiredMaterial(this);
        return job;
    }
    
	/* Instance methods */	

	/* Overrides */
	
	@Override
	public void save() {
	    if (stockedMaterial != null)
	        setStockedMaterialId(stockedMaterial.getId());
	    
	    super.save();
	}
	
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (stockedMaterialId,jobId,quantityRequired) "
        + "VALUES (?,?,?)"
        , getStockedMaterialId(), getJobId(), getQuantityRequired());
    }
    
    @Override
    public void update() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "stockedMaterialId=?,jobId=?,quantityRequired=? "
        + "WHERE id = ?", getStockedMaterialId(), getJobId(), getQuantityRequired(), getId());
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
	
	public static ObservableList<RequiredMaterial> loadList(Job job, ResultSet result) {
        ObservableList<RequiredMaterial> requiredMaterials = loadList(result, RequiredMaterial.class);
        for (RequiredMaterial requiredMaterial : requiredMaterials) {
            requiredMaterial.setJob(job);
        }
        return requiredMaterials;
    }
	
	public static ObservableList<RequiredMaterial> loadRequiredMaterials() {
        requiredMaterials = loadList(null, loadAllModels(DB_TABLE_NAME));
        return requiredMaterials;
    }
    
    public static ObservableList<RequiredMaterial> loadRequiredMaterialsForJob(Job job) {
        return loadList(job, loadAllModelsWhere(DB_TABLE_NAME, "jobId", job.getId()));
    }

    public static ObservableList<RequiredMaterial> getModels() {
        return requiredMaterials;
    }
}
