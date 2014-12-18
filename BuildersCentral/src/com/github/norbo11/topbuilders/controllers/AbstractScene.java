package com.github.norbo11.topbuilders.controllers;

import java.net.URL;

import javafx.scene.Parent;
import javafx.scene.Scene;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.util.FXMLHelper;

public class AbstractScene extends Scene {
    
    public AbstractScene(double windowWidth, double windowHeight, String fxmlFilename) {
    	super(FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename));
    	this.fxmlFilename = fxmlFilename;
    	loadFromFxml();
    }

	private String fxmlFilename;
    private String stylesheetPath;
        
    public String getStylesheetPath() {
        return stylesheetPath;
    }
    
    public void setStylesheetPath(String stylesheetPath) {
        this.stylesheetPath = stylesheetPath;
    }
    
    public void applyStylesheet() {
        //String styleSheet = loadStylesheet(stylesheetPath);
        //if (styleSheet != null) getStylesheets().add(styleSheet);
    }

    public static String loadStylesheet(String filename) {
        URL url = Main.getApp().getClass().getResource("/com/github/norbo11/topbuilders/css/" + filename);
        return url != null ? url.toExternalForm() : null;
    }
    
    public void loadFromFxml() {
        //Create the tab object, fill it with the loaded FXML
        Parent root = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename);
	    setRoot(root);
	}
}
