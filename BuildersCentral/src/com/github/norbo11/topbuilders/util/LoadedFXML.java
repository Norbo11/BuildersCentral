package com.github.norbo11.topbuilders.util;

import javafx.scene.Parent;

public class LoadedFXML {
    public LoadedFXML(Parent root, Object controller) {
        this.root = root;
        this.controller = controller;
    }
    
    private Parent root;
    private Object controller;
    
    public Parent getRoot() {
        return root;
    }
    
    public Object getController() {
        return controller;
    }
}