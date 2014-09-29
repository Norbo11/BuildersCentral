package com.github.norbo11.builderscentral.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.github.norbo11.builderscentral.Constants;
import com.github.norbo11.builderscentral.Main;

public class RegisterScene extends StyledScene {
	
    public RegisterScene() {
    	super(new VBox(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, "register.css");
        
    	VBox box = (VBox) getRoot();
    	
        box.setPrefSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        box.setPadding(new Insets(25));
        box.setSpacing(20);
        box.setFillWidth(false);
        box.setAlignment(Pos.CENTER);
        
        Label label = new Label("Please enter the 12-character long code received by your employer:");
        
        TextField field = new TextField();
        field.setPrefWidth(340);
        
        Button backButton = new Button("Back");
        Button submitButton = new Button("Activate account!");
        submitButton.setPrefWidth(150);
        backButton.setPrefWidth(150);
        backButton.setOnAction(e -> Main.changeMainScene(new LoginScene()));
        
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(backButton, submitButton);
        box.getChildren().addAll(label, field, hbox);
    }
}
