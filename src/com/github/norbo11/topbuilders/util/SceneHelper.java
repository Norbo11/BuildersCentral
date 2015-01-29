package com.github.norbo11.topbuilders.util;

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

public class SceneHelper {
	
    private static boolean fullscreen = false;
    
	public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void setFullscreen(boolean value) {
        fullscreen = value;
    }

    private static AbstractScene changeScene(Stage stage, AbstractScene scene) {
        stage.setFullScreen(fullscreen);
        stage.setScene(scene);
        stage.show();
        return scene;
    }
    
    public static AbstractScene changeScene(Stage stage, Parent root) {
        return changeScene(stage, new AbstractScene(root));
    }
    
    public static AbstractScene changeScene(Stage stage, String filename) {
        return changeScene(stage, new AbstractScene(FXMLHelper.loadFxml(filename)));
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
        Button button = new Button(Resources.getResource("general.ok"));
        
        button.setOnAction(e -> { 
            runnable.run();
            SceneHelper.closeScene((Node) e.getSource());
        });
        
        showDialog(title, info, button);
    }
    
    public static void showConfirmationDialog(String title, String info, Runnable runnable) {
        Button ok = new Button(Resources.getResource("general.ok"));
        ok.setOnAction(e -> { 
            runnable.run();
            SceneHelper.closeScene((Node) e.getSource());
        });
        
        Button cancel = new Button(Resources.getResource("general.cancel"));
        cancel.setOnAction(e -> { 
            SceneHelper.closeScene((Node) e.getSource());
        });
        
        showDialog(title, info, ok, cancel);
    }
    
    private static void showDialog(String title, String info, Button... buttons) {
        VBox vbox = createDialogVBox(info, buttons);
        Stage stage = StageHelper.createDialogStage(title);
        changeScene(stage, vbox);
    }
    
    private static VBox createDialogVBox(String info, Button... buttons) {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15, 75, 15, 75));
        
        Label label = new Label(info);
        label.setMinHeight(80);
        label.setTextAlignment(TextAlignment.CENTER);
        
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
