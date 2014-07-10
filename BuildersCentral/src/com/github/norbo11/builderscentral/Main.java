package com.github.norbo11.builderscentral;
	
import java.io.IOException;

import com.github.norbo11.builderscentral.util.Database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            
            Scene scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
            
            Database.connect();
            Database.createTables();
            
            stage.setTitle(Constants.APPLICATION_NAME);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
