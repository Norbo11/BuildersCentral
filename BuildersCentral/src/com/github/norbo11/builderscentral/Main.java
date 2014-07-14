package com.github.norbo11.builderscentral;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.github.norbo11.builderscentral.util.Database;
import com.github.norbo11.builderscentral.util.Log;


public class Main extends Application {
   private static Stage stage = null;
   private static Application app = null;
    
	@Override
	public void start(Stage stage) {
        Database.connect();
        Database.createTables();
        
        Main.app = this;
        Main.stage = stage;
        stage.setTitle(Constants.APPLICATION_NAME);
        changeScene("login");
	}
	
	public static FXMLLoader loadFxml(String filename) {
	    try {
	        FXMLLoader loader = new FXMLLoader(app.getClass().getResource("/com/github/norbo11/builderscentral/fxml/" + filename + ".fxml"));
            loader.load();
            return loader;
        } catch (IOException e) {
            Log.error(e);
        }
	    return null;
	}
	
	public static String loadStylesheet(String filename) {
        return app.getClass().getResource("/com/github/norbo11/builderscentral/css/" + filename + ".css").toExternalForm();
    }
	
	public static <T> T changeScene(String sceneName)
	{
	    FXMLLoader loader = loadFxml(sceneName);
        Parent root = loader.getRoot();   
        if (root != null) {
            Scene newScene = new Scene(root);
            String styleSheet = loadStylesheet(sceneName);
            if (styleSheet != null) newScene.getStylesheets().add(styleSheet);
            
            stage.setScene(newScene);
            stage.show();
            return loader.getController();
        }
        return null;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
