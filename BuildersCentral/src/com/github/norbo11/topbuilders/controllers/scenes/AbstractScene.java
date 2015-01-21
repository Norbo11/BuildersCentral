package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.scene.Scene;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.util.LoadedFXML;

public class AbstractScene extends Scene {
    
    public AbstractScene(LoadedFXML fxml) {
    	super(fxml.getRoot());
    	
    	controller = (AbstractController) fxml.getController();
    	
    	loadStylesheet();
    }
   
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
}
