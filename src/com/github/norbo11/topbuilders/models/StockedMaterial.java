package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.models.enums.QuantityType;
import com.github.norbo11.topbuilders.util.Database;

public class StockedMaterial extends AbstractModel {

public static final String DB_TABLE_NAME = "stockedMaterials";
	
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
    
	public Double getQuantityRequired() {
	    //TODO implement properly
		return 5d;
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
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {      
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "name")) setName(result.getString("name"));
        if (containsColumn(columns, "quantityInStock")) setQuantityInStock(result.getDouble("quantityInStock"));
        if (containsColumn(columns, "quantityTypeId")) setQuantityTypeId(result.getInt("quantityTypeId"));
    }


	@Override
	public String getDbTableName() {
	    return DB_TABLE_NAME;
	}
	
	/* Static methods */

	public static StockedMaterial loadStockedMaterialForRequiredMaterial(RequiredMaterial requiredMaterial) {
		return loadList(loadAllModelsWhere(DB_TABLE_NAME, "id", requiredMaterial.getId())).get(0);
	}
	
	/* Standard static methods */
	
	public static Vector<StockedMaterial> getAllStockedMaterials() {
		return loadList(loadAllModels(DB_TABLE_NAME));
	}
	
	public static Vector<StockedMaterial> loadList(ResultSet result) {
		return loadList(result, StockedMaterial.class);
	}
}
