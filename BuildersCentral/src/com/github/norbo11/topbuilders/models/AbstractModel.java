package com.github.norbo11.topbuilders.models;


public abstract class AbstractModel {
    private int id;
    
    public AbstractModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
        
    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractModel) {
            AbstractModel e = (AbstractModel) o;
            return id == e.getId();
        }
        
        return false;
    }
}
