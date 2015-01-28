package com.github.norbo11.topbuilders.util;

import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.InfoDialog;

public class Log {

    public static void error(Exception e)
    {
        e.printStackTrace();
    }

    public static void error(String string) {
        System.out.println(string);
        
        Stage stage = StageHelper.createDialogStage("Error!");
        AbstractScene scene = SceneHelper.changeScene(stage, InfoDialog.FXML_FILENAME);
        InfoDialog dialog = (InfoDialog) scene.getController();
        dialog.setText(string);
    }
    
    public static void error(String string, Exception e) {
        for (StackTraceElement element : e.getStackTrace()) {
            string += element.toString();
        }
        
        error(string);
        
    }

    public static void info(Object object) {
        System.out.println(object + "");
    }
    
	public static void info(String string) {
		System.out.println(string);
	}

}
