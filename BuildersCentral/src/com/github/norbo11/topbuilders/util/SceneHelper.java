package com.github.norbo11.topbuilders.util;

import com.github.norbo11.topbuilders.Main;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHelper {
	
	//Change the contents of a given window (stage) by loading the FXML file provided
	public static void changeScene(Stage stage, String filename) {
        Parent root = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/scenes/fxml/" + filename);
        stage.setScene(new Scene(root));
        stage.show();
	}
	
	//Change the contents of the main window
	public static void changeMainScene(String filename) {
        changeScene(Main.getMainStage(), filename);
	}
}
