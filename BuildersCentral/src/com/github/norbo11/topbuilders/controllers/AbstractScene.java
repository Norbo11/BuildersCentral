package com.github.norbo11.topbuilders.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.util.FXMLHelper;
import com.github.norbo11.topbuilders.util.LoadedFXML;

public class AbstractScene extends Scene {
    
    public AbstractScene(String fxmlFilename, Stage stage) {
    	super(FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename).getRoot());
    	
    	this.fxmlFilename = fxmlFilename;
    	
    	loadFromFxml();
    	loadStylesheet();
    }
   
	private String fxmlFilename;
    private String stylesheetPath;
    private AbstractController controller;
        
    public AbstractController getController() {
        return controller;
    }

    public String getStylesheetPath() {
        return stylesheetPath;
    }
    
    public void setStylesheetPath(String stylesheetPath) {
        this.stylesheetPath = stylesheetPath;
    }

    public void loadStylesheet() {
        getStylesheets().add("/com/github/norbo11/topbuilders/css/global.css");
    }
    
    public void loadFromFxml() {
        LoadedFXML fxml = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename);
        controller = (AbstractController) fxml.getController();
	    setRoot(fxml.getRoot());
	}
}
