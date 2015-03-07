package com.github.norbo11.topbuilders.util.helpers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.EmployeeSettingType;
import com.github.norbo11.topbuilders.util.Resources;

public class SceneUtil {

    private static AbstractScene changeScene(Stage stage, AbstractScene scene) {
        boolean fullscreen = false;
        
        if (Employee.getCurrentEmployee() != null) {
            fullscreen = Employee.getCurrentEmployee().getSettings().getBoolean(EmployeeSettingType.FULLSCREEN);
        }
        
        if (!scene.isNeverFullScreen()) stage.setFullScreen(fullscreen);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
    
    public static AbstractScene changeScene(Stage stage, Parent root) {
        return changeScene(stage, new AbstractScene(root));
    }
    
    public static AbstractScene changeScene(Stage stage, String filename, boolean neverFullScreen) {
        AbstractScene scene = new AbstractScene(FXMLUtil.loadFxml(filename));
        scene.setNeverFullScreen(true);
        return changeScene(stage, scene);
    }
        
    public static AbstractScene changeScene(Stage stage, String filename) {
        return changeScene(stage, new AbstractScene(FXMLUtil.loadFxml(filename)));
	}
	
	//Change the contents of the main window
	public static void changeMainScene(String filename) {
        changeScene(Main.getMainStage(), filename);
	}

    public static void closeScene(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    
    public static void showInfoDialog(String title, String info) {
        showInfoDialog(title, info, () -> {});
    }
    
    public static void showInfoDialog(String title, String info, Runnable runnable) {
        Button button = new Button("OK");
        
        button.setOnAction(e -> { 
            runnable.run();
            SceneUtil.closeScene((Node) e.getSource());
        });
        
        showDialog(title, info, button);
    }
    
    public static void showConfirmationDialog(String title, String info, Runnable confirmRunnable) {
        showConfirmationDialog(title, info, confirmRunnable, () -> { return; });
    }

    
    public static void showConfirmationDialog(String title, String info, Runnable confirmRunnable, Runnable discardRunnable) {
        Button ok = new Button(Resources.getResource("general.ok"));
        ok.setOnAction(e -> { 
            confirmRunnable.run();
            SceneUtil.closeScene((Node) e.getSource());
        });
        
        Button cancel = new Button(Resources.getResource("general.cancel"));
        cancel.setOnAction(e -> { 
            discardRunnable.run();
            SceneUtil.closeScene((Node) e.getSource());
        });
        
        showDialog(title, info, ok, cancel);
    }
    
    private static void showDialog(String title, String info, Button... buttons) {
        VBox vbox = createDialogVBox(info, buttons);
        Stage stage = StageUtil.createDialogStage(title);
        AbstractScene scene = new AbstractScene(vbox);
        scene.setNeverFullScreen(true);
        changeScene(stage, scene);
    }
    
    private static VBox createDialogVBox(String info, Button... buttons) {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15, 75, 15, 75));
        
        Label label = new Label(info);
        label.setMinHeight(80);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        
        for (Button button : buttons) {
            button.setPrefWidth(190);
            button.setPrefHeight(40);
            hbox.getChildren().add(button);
        }
        
        vbox.getChildren().addAll(label, hbox);
        return vbox;
    }
}
