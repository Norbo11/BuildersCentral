package com.github.norbo11.topbuilders;
	
import javafx.application.Application;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.scenes.LoginScene;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class Main extends Application {
   private static Stage mainStage = null;
   private static Application app = null;
    
	@Override
	public void start(Stage stage) {
        Database.connect();
        Database.createTables();
        
        Main.app = this;
        Main.mainStage = stage;
        stage.setResizable(false);
        stage.setTitle(Constants.APPLICATION_NAME);
        SceneHelper.changeMainScene(LoginScene.FXML_FILENAME);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getMainStage() {
		return mainStage;
	}

	public static Application getApp() {
		return app;
	}
}
