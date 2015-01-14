package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.models.enums.QuantityType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;

public class StockedMaterial extends AbstractModel {

public static final String DB_TABLE_NAME = "stockedMaterials";
	
	private StringProperty name;
	private DoubleProperty quantityInStock;
	private ObjectProperty<QuantityType> quantityType;
	
	public StockedMaterial(int id, String name, double quantityInStock, QuantityType quantityType) {
		super(id);
		
		this.name = new SimpleStringProperty(name);
		this.quantityInStock = new SimpleDoubleProperty(quantityInStock);
		this.quantityType = new SimpleObjectProperty<QuantityType>(quantityType);
	}

   public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public double getQuantityInStock() {
		return quantityInStock.get();
	}

	public void setQuantityInStock(double hourlyWage) {
		this.quantityInStock.set(hourlyWage);
	}

	public QuantityType getQuantityType() {
		return quantityType.get();
	}

	public void setQuantityType(QuantityType startDate) {
		this.quantityType.set(startDate);
	}
	

	private static StockedMaterial getStockedMaterialFromResult(ResultSet result) throws SQLException {
	    int id = result.getInt("id");
        String name = result.getString("name");
        double quantityInStock = result.getDouble("quantityInStock");
        QuantityType quantityType = QuantityType.getSettingType(result.getInt("quantityType"));
        return new StockedMaterial(id, name, quantityInStock, quantityType);
	}

    public static StockedMaterial getStockedMaterialFromId(int id) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                return getStockedMaterialFromResult(result);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }
}
