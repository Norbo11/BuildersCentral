package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.models.enums.QuantityType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Resources;

public class StockedMaterial extends AbstractModel {

	public static final String DB_TABLE_NAME = "stockedMaterials";
	private static ArrayList<StockedMaterial> stockedMaterials = new ArrayList<StockedMaterial>();

	private StringProperty name = new SimpleStringProperty("");
	private DoubleProperty quantityInStock = new SimpleDoubleProperty(0);
	private IntegerProperty quantityTypeId = new SimpleIntegerProperty(0);
	
	public StockedMaterial() {
	    super();
	}
	
	public StockedMaterial(String name, double quantityInStock, QuantityType quantityType) {
	    setName(name);
	    setQuantityInStock(quantityInStock);
	    setQuantityTypeId(quantityType.getId());
	    setId(add());
	}
	
	/* Getters and setters */
	
    public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public double getQuantityInStock() {
		return quantityInStock.get();
	}

	public void setQuantityInStock(double quantityInStock) {
		this.quantityInStock.set(quantityInStock);
	}

	public int getQuantityTypeId() {
		return quantityTypeId.get();
	}

	public void setQuantityTypeId(int quantityTypeId) {
		this.quantityTypeId.set(quantityTypeId);
	}
	
	/* Property getters */
	
	public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty quantityInStockProperty() {
        return quantityInStock;
    }

    public IntegerProperty quantityTypeIdProperty() {
        return quantityTypeId;
    }
	
	/* Instance methods */

    public QuantityType getQuantityType() {
        return QuantityType.getQuantityType(getQuantityTypeId());
    }
    
	public Double getQuantityRequiredInProjects() {
	    double required = 0;
	    
		for (RequiredMaterial material : RequiredMaterial.getRequiredMaterials()) {
		    if (material.getStockedMaterialId() == getId()) required += material.getQuantityRequired();
		}
		
		return required;
	}
	
	public String getQuantityString() {
        return getQuantityInStock() + "" + getQuantityType();
    }
	
    /* Override methods */

    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (name,quantityInStock,quantityTypeId) "
        + "VALUES (?,?,?)"
        , getName(), getQuantityInStock(), getQuantityTypeId());
    }
    
    @Override
    public void update() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "name=?,quantityInStock=?,quantityTypeId=? "
        + "WHERE id = ?", getName(), getQuantityInStock(), getQuantityTypeId(), getId());
    }
    
    @Override
    public void loadFromResult(AbstractModel parent, ResultSet result, String... columns) throws SQLException {      
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "name")) setName(result.getString("name"));
        if (containsColumn(columns, "quantityInStock")) setQuantityInStock(result.getDouble("quantityInStock"));
        if (containsColumn(columns, "quantityTypeId")) setQuantityTypeId(result.getInt("quantityTypeId"));
    }

	@Override
	public String getDbTableName() {
	    return DB_TABLE_NAME;
	}

	@Override
	public String toString() {
		return getName() + " - " + getQuantityString() + " " + Resources.getResource("materials.inStock").toLowerCase();
	}
	
	/* Static methods */

	public static StockedMaterial loadStockedMaterialForRequiredMaterial(RequiredMaterial requiredMaterial) {
	    return loadOne(null, loadAllModelsWhere(DB_TABLE_NAME, "id", requiredMaterial.getStockedMaterialId()), StockedMaterial.class);
	}
	
	/* Standard static methods */
	
	public static ArrayList<StockedMaterial> getStockedMaterials() {
		return stockedMaterials;
	}
	
	public static void loadStockedMaterials() {
		stockedMaterials = loadList(loadAllModels(DB_TABLE_NAME));
	}
	
	public static ArrayList<StockedMaterial> loadList(ResultSet result) {
		return loadList(null, result, StockedMaterial.class);
	}

    public static StockedMaterial getStockedMaterialByName(String name) {
        for (StockedMaterial material : stockedMaterials) {
            if (material.getName().equalsIgnoreCase(name)) return material;
        }
        
        return null;
    }
}
