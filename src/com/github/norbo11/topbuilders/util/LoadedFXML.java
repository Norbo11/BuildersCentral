package com.github.norbo11.topbuilders.util;

import javafx.scene.Parent;

public class LoadedFXML {
    public LoadedFXML(Parent root, Object controller, String fxmlFilename) {
        this.root = root;
        this.controller = controller;
        this.fxmlFilename = fxmlFilename;
    }
    
    
    private Parent root;
    private Object controller;
    private String fxmlFilename;
    
    public String getFxmlFilename() {
		return fxmlFilename;
	}

	public Parent getRoot() {
        return root;
    }
    
    public Object getController() {
        return controller;
    }
}