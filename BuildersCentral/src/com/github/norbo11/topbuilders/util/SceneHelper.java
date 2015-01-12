package com.github.norbo11.topbuilders.util;

import javafx.scene.Node;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.controllers.AbstractScene;

public class SceneHelper {
	
	//Change the contents of a given window (stage) by loading the FXML file provided
	public static AbstractScene changeScene(Stage stage, boolean fullscreen, String filename) {
		AbstractScene scene = new AbstractScene(filename, stage);
        stage.setScene(scene);
        stage.setFullScreen(fullscreen);
        stage.show();
        return scene;
	}
	
	//Change the contents of the main window
	public static void changeMainScene(String filename, boolean fullscreen) {
        changeScene(Main.getMainStage(), fullscreen, filename);
	}

    public static void closeScene(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
