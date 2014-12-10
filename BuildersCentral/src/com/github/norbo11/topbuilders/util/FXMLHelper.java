package com.github.norbo11.topbuilders.util;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.models.User;
import com.github.norbo11.topbuilders.scenes.LoginScene;

public class FXMLHelper {
    public static Parent loadFxml(String filename) {
        Parent root = null;
        try {
        	FXMLLoader loader = new FXMLLoader();
        	
        	if (!filename.equals(LoginScene.getAbsoluteFxmlFilename())) {
        		Locale locale = User.getCurrentUser().getSettings().getLocale();
        		loader.setResources(ResourceBundle.getBundle("lang.lang", locale, ClassLoader.getSystemClassLoader()));
        	}
            root = loader.load(Main.getApp().getClass().getResource(filename).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
}
