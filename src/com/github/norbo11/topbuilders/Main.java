package com.github.norbo11.topbuilders;
	
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.scenes.LoginScene;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;

public class Main extends Application {
   private static Stage mainStage = null;
   private static Application app = null;
    
	@Override
	public void start(Stage stage) {
		//Connect to database
        Database.connect();
        
        //Set dimensions of main window
        stage.setWidth(Constants.WINDOW_WIDTH);
        stage.setHeight(Constants.WINDOW_HEIGHT);
        
        //Store an instance of the application and the main stage static variables
        Main.app = this;
        Main.mainStage = stage;
        
        //Disconnect from the database when the main window is closed
        stage.setOnCloseRequest(e -> Database.disconnect());
        
        //Disable any fullscreen messages, as well as disable the ability to exit fullscreen with escape key
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        
        //Set window name and maximise
        stage.setTitle(Constants.APPLICATION_NAME);
        stage.setMaximized(true);
        
        //Show the login screen, and log in to test accuont if debug mode is true
        SceneUtil.changeMainScene(LoginScene.FXML_FILENAME);
        if (Constants.DEBUG_MODE) Employee.loginTestAccount();
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
