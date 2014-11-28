package com.github.norbo11.builderscentral.scenes;

import java.net.URL;

import javafx.scene.Parent;

import com.github.norbo11.builderscentral.Main;

public abstract class AbstractScene {
    
    public AbstractScene(Parent root, double windowWidth, double windowHeight, String stylesheetPath) {
        //super(root, windowWidth, windowHeight);
        
        /*try {
            root = FXMLLoader.load(Main.app.getClass().getResource("/com/github/norbo11/builderscentral/fxml/" + getFxmlFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setRoot(root);
        applyStylesheet();*/
    }

    private String stylesheetPath = "";
        
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
        URL url = Main.app.getClass().getResource("/com/github/norbo11/builderscentral/css/" + filename);
        return url != null ? url.toExternalForm() : null;
    }
}
