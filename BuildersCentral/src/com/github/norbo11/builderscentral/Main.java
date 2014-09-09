package com.github.norbo11.builderscentral;
	
import javafx.application.Application;
import javafx.stage.Stage;

import com.github.norbo11.builderscentral.scenes.LoginScene;
import com.github.norbo11.builderscentral.scenes.StyledScene;
import com.github.norbo11.builderscentral.util.Database;

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
        changeMainScene(LoginScene.getScene());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
    public static void changeMainScene(StyledScene scene) {
        stage.setScene(scene);
        stage.show();
    }
}
