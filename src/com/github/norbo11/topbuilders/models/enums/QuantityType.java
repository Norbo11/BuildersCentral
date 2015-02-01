package com.github.norbo11.topbuilders.models.enums;

public enum QuantityType {
    MILLIMETERS(1, "mm"),
    MILLIMETERS_SQUARED(2, "mm²"),
    MILLIMETERS_CUBED(3, "mm³"),
    
    CENTIMETERS(4, "cm"),
    CENTIMETERS_SQUARED(5, "cm²"),
    CENTIMETERS_CUBED(6, "cm³"),
    
    METERS(7, "m"),
	METERS_SQUARED(8, "m²"),
	METERS_CUBED(9, "m³"),
	
	GRAMS(10, "g"),
	KILOGRAMS(11, "kg"),
	
	MILLILITRES(12, "ml"),
	LITRES(13, "l");
	
	private String name = "UNDEFINED";
    private int id;
    
    QuantityType(int settingId, String name) {
        this.id = settingId;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }

	public static QuantityType getQuantityType(int id) {
        for (QuantityType quantityType : values()) {
            if (quantityType.getId() == id) return quantityType;
        }
        return null;
    }
	
	@Override
	public String toString() {
	    return name;
	}
}
