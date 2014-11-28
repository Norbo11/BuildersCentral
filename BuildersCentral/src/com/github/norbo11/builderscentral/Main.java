package com.github.norbo11.builderscentral;
	
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.github.norbo11.builderscentral.scenes.LoginScene;
import com.github.norbo11.builderscentral.util.Database;
import com.github.norbo11.builderscentral.util.FXMLHelper;

public class Main extends Application {
   private static Stage stage = null;
   public static Application app = null;
    
	@Override
	public void start(Stage stage) {
        Database.connect();
        Database.createTables();
        
        Main.app = this;
        Main.stage = stage;
        stage.setResizable(false);
        stage.setTitle(Constants.APPLICATION_NAME);
        changeMainScene(LoginScene.FXML_FILENAME);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
    public static void changeMainScene(String filename) {
        Parent root = FXMLHelper.loadFxml(filename);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
