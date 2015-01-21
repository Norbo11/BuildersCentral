package com.github.norbo11.topbuilders.util;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.models.Employee;

public class FXMLHelper {
    public static LoadedFXML loadFxml(String filename) {
        return loadFxml(filename, null, null);
    }
    
    public static LoadedFXML loadFxml(String filename, Object root, Object controller) {
    	String absoluteFilename = "/com/github/norbo11/topbuilders/fxml/" + filename;
    	Log.info("Loading FXML: " + filename);
        Parent loadedRoot = null;
        Object loadedController = null;
        
        try {
        	FXMLLoader loader = new FXMLLoader(Main.getApp().getClass().getResource(absoluteFilename));
        	if (root != null) loader.setRoot(root);
        	if (controller != null) loader.setController(controller);
        	
    		setResources(loader);
        	
        	loadedRoot = loader.load();
        	loadedController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LoadedFXML(loadedRoot, loadedController, filename);
    }
    
    public static void setResources(FXMLLoader loader) {
    	Employee user = Employee.getCurrentEmployee();
		
		//If the user is logged in
		if (user != null) {
    		Locale locale = Employee.getCurrentEmployee().getSettings().getLocale();
    		loader.setResources(ResourceBundle.getBundle("lang.lang", locale, ClassLoader.getSystemClassLoader()));
		}
    }
}
