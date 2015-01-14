package com.github.norbo11.topbuilders.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public abstract class AbstractModel {
    private IntegerProperty id;
    
    public AbstractModel(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public int getId() {
        return id.get();
    }
        
    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractModel) {
            AbstractModel e = (AbstractModel) o;
            return getId() == e.getId();
        }
        
        return false;
    }
}
