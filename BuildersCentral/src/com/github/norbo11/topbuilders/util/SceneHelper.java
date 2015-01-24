package com.github.norbo11.topbuilders.util;

import javafx.scene.Node;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.InfoDialog;
import com.github.norbo11.topbuilders.models.Employee;

public class SceneHelper {
	
    private static boolean fullscreen = false;
    
	public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void setFullscreen(boolean value) {
        fullscreen = value;
    }

    public static AbstractScene changeScene(Stage stage, String filename) {
		AbstractScene scene = new AbstractScene(FXMLHelper.loadFxml(filename));
        stage.setScene(scene);
        
        Employee employee = Employee.getCurrentEmployee();
        if (employee != null) stage.setFullScreen(fullscreen);
        stage.show();
        return scene;
	}
	
	//Change the contents of the main window
	public static void changeMainScene(String filename) {
        changeScene(Main.getMainStage(), filename);
	}

    public static void closeScene(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    
    public static void showInfoDialog(String title, String info, Runnable runnable) {
        Stage stage = StageHelper.createDialogStage(title);
        AbstractScene scene = changeScene(stage, InfoDialog.FXML_FILENAME);
        stage.sizeToScene();
        
        InfoDialog dialog = (InfoDialog) scene.getController();
        dialog.setText(info);
        dialog.setRunnable(runnable);
    }
}
