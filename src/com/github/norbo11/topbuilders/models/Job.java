package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.models.enums.QuoteSettingType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Settings;

public class Job extends AbstractModel {
    public static final String DB_TABLE_NAME = "jobs";
    private static ObservableList<Job> jobs = FXCollections.observableArrayList();
    
    private IntegerProperty jobGroupId = new SimpleIntegerProperty(0);
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private DoubleProperty labourPrice = new SimpleDoubleProperty(0);
    private DoubleProperty materialPrice = new SimpleDoubleProperty(0);
    
    private ObservableList<RequiredMaterial> requiredMaterials = null;
    private ObservableList<Assignment> assignments = null;
    private JobGroup jobGroup, jobGroupDummy = null;
    
    /* Properties */

    public IntegerProperty jobGroupIdProperty() {
        return jobGroupId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public DoubleProperty labourPriceProperty() {
        return labourPrice;
    }

    public DoubleProperty materialPriceProperty() {
        return materialPrice;
    }
    
	/* Getters and setters */
    
    public JobGroup getJobGroupDummy() {
        return jobGroupDummy;
    }

    public void setJobGroupDummy(JobGroup jobGroupDummy) {
        this.jobGroupDummy = jobGroupDummy;
    }
    
    public int getJobGroupId() {
		return jobGroupId.get();
	}

	public void setJobGroupId(int jobGroupId) {
		this.jobGroupId.set(jobGroupId);
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDescription() {
		return description.get();
	}

	public void setDescription(String description) {
		this.description.set(description);
	}

	public double getLabourPrice() {
		return labourPrice.get();
	}

	public void setLabourPrice(double labourPrice) {
		this.labourPrice.set(labourPrice);
	}

	public double getMaterialPrice() {
		return materialPrice.get();
	}

	public void setMaterialPrice(double materialPrice) {
		this.materialPrice.set(materialPrice);
	}    
    
    /* Forgeign key methods */
	
	//Getters & setters
    
    public ObservableList<RequiredMaterial> getRequiredMaterials() {
        return requiredMaterials == null ? loadRequiredMaterials() : requiredMaterials;
    }

    public void setRequiredMaterials(ObservableList<RequiredMaterial> requiredMaterials) {
        this.requiredMaterials = requiredMaterials;
    }
    
    public ObservableList<Assignment> getAssignments() {
        return assignments == null ? loadAssignments() : assignments;
    }

    public void setAssignments(ObservableList<Assignment> assignments) {
        this.assignments = assignments;
    }
    
    public JobGroup getJobGroup() {
        return jobGroup == null ? loadJobGroup() : jobGroup;
    }

    public void setJobGroup(JobGroup jobGroup) {
        this.jobGroup = jobGroup;
    }
    
    //Loaders
    
    public ObservableList<RequiredMaterial> loadRequiredMaterials() {
        requiredMaterials = RequiredMaterial.loadRequiredMaterialsForJob(this);
        return requiredMaterials;
    }
    
    public ObservableList<Assignment> loadAssignments() {
        assignments = Assignment.loadAssignmentsForJob(this);
        return assignments;
    }
    
    public JobGroup loadJobGroup() {
    	jobGroup = JobGroup.loadJobGroupForJob(this);
    	return jobGroup;
    }
    
	
	/* Instance methods */	

    public String getRequiredMaterialsString(boolean commaSeperated) {
        String materials = "";
        
        if (!isDummy()) {
            //Go through all materials of the job corresponding to this assignment, and add them to the materials list
            for (RequiredMaterial material : getRequiredMaterials()) {
                materials += material;
                if (commaSeperated) materials += ", ";
                else materials += "\n";
            }           
        }
            
        return materials.length() > 0 ? materials.substring(0, materials.length() - 2) : materials; //Get rid of 2 characters
    }

    public double getTotalCost() {
        //If both material and labour prices are enabled, add them. Otherwise, use the labour price field to decide the total price.
        
        Settings<QuoteSetting> settings = getJobGroup().getProject().getSettings();
        return settings.getBoolean(QuoteSettingType.SPLIT_PRICE) ? getLabourPrice() + getMaterialPrice() : getLabourPrice();
    }
    
	/* Overrides */
	
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (jobGroupId,title,description,labourPrice,materialPrice) "
        + "VALUES (?,?,?,?,?)"
        , getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice());
    }
    
    @Override
    public void update() {   
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "jobGroupId=?,title=?,description=?,labourPrice=?,materialPrice=? "
        + "WHERE id = ?", getJobGroupId(), getTitle(), getDescription(), getLabourPrice(), getMaterialPrice(), getId());
    }
    
    @Override
    public void save() {
        super.save();
        
        for (RequiredMaterial requiredMaterial : requiredMaterials) {
            requiredMaterial.save();
        }
    }
    
    @Override
    public void delete() {
        for (RequiredMaterial requiredMaterial : requiredMaterials) {
            requiredMaterial.delete();
        }
        
        for (Assignment assignment : getAssignments()) {
            assignment.delete();
        }
        
        super.delete();
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "jobGroupId")) setJobGroupId(result.getInt("jobGroupId"));
        if (containsColumn(columns, "title")) setTitle(result.getString("title"));
        if (containsColumn(columns, "description")) setDescription(result.getString("description"));
        if (containsColumn(columns, "labourPrice")) setLabourPrice(result.getDouble("labourPrice"));
        if (containsColumn(columns, "materialPrice")) setMaterialPrice(result.getDouble("materialPrice"));
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
		return getTitle();
	}
	
	@Override
    public boolean isDummy() {
        return jobGroupDummy != null;
    }

	/* Static methods */
	
	public static ObservableList<Job> loadJobsForJobGroup(JobGroup jobGroup) {		
		return loadList(jobGroup, loadAllModelsWhere(DB_TABLE_NAME, "jobGroupId", jobGroup.getId()));
	}
	
	public static Job loadJobForRequiredMaterial(RequiredMaterial requiredMaterial) {
        return loadOne(loadAllModelsWhere(DB_TABLE_NAME, "id", requiredMaterial.getJobId()), Job.class);
    }
	
	/* Standard static methods */
	
	public static ObservableList<Job> loadList(JobGroup jobGroup, ResultSet result) {
		ObservableList<Job> jobs = loadList(result, Job.class);
		for (Job job : jobs) {
		    job.setJobGroup(jobGroup);
		}
		return jobs;
	}

	public static Job loadJobForAssignment(Assignment assignment) {
		return loadOne(loadAllModelsWhere(DB_TABLE_NAME, "id", assignment.getJobId()), Job.class);
	}
	
	public static ObservableList<Job> getModels() {
	    return jobs;
	}
}
