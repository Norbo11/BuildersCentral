package com.github.norbo11.topbuilders.util;

import javafx.stage.Stage;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.controllers.AbstractScene;

public class SceneHelper {
	
	//Change the contents of a given window (stage) by loading the FXML file provided
	public static AbstractScene changeScene(Stage stage, double width, double height, String filename) {
		AbstractScene scene = new AbstractScene(width, height, filename);
        stage.setScene(scene);
        stage.show();
        return scene;
	}
	
	//Change the contents of the main window
	public static void changeMainScene(String filename) {
        changeScene(Main.getMainStage(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, filename);
	}
}
