package com.github.norbo11.builderscentral.scenes;

import java.net.URL;

import javafx.scene.Parent;
import javafx.scene.Scene;

import com.github.norbo11.builderscentral.Main;

public class StyledScene extends Scene {
    
    public StyledScene(Parent root, String stylesheetPath) {
        super(root);
        this.stylesheetPath = stylesheetPath;
        applyStylesheet();
    }

    public StyledScene(Parent root, double windowWidth, double windowHeight, String stylesheetPath) {
        super(root, windowWidth, windowHeight);
        this.stylesheetPath = stylesheetPath;
        applyStylesheet();
    }

    private String stylesheetPath = "";
        
    public String getStylesheetPath() {
        return stylesheetPath;
    }
    
    public void setStylesheetPath(String stylesheetPath) {
        this.stylesheetPath = stylesheetPath;
    }
    
    public void applyStylesheet() {
        String styleSheet = loadStylesheet(stylesheetPath);
        if (styleSheet != null) getStylesheets().add(styleSheet);
    }

    public static String loadStylesheet(String filename) {
        URL url = Main.app.getClass().getResource("/com/github/norbo11/builderscentral/css/" + filename);
        return url != null ? url.toExternalForm() : null;
    }
}
