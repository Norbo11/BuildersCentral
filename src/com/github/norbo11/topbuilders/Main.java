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
        Database.connect();
        
        stage.setWidth(Constants.WINDOW_WIDTH);
        stage.setHeight(Constants.WINDOW_HEIGHT);
        
        Main.app = this;
        Main.mainStage = stage;
        stage.setOnCloseRequest(e -> Database.disconnect());
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Constants.APPLICATION_NAME);
        stage.setMaximized(true);
        
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
