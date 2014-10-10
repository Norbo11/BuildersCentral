package com.github.norbo11.builderscentral.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.github.norbo11.builderscentral.Constants;
import com.github.norbo11.builderscentral.Main;
import com.github.norbo11.builderscentral.models.User;
import com.github.norbo11.builderscentral.models.exceptions.PasswordException;
import com.github.norbo11.builderscentral.models.exceptions.UsernameException;

class TestAccountHandler implements EventHandler<ActionEvent> {
    
    Scene scene;
    
    public TestAccountHandler(Scene scene) {
        this.scene = scene;
    }
    
    @Override
    public void handle(ActionEvent event) {
        ((TextField) scene.lookup("#usernameField")).setText("test");
        ((TextField) scene.lookup("#passwordField")).setText("abc123");
        ((Button) scene.lookup("#submitButton")).fire();
    }
}

class SubmitLoginHandler implements EventHandler<ActionEvent> {
    
    Scene scene;
    TextField usernameField;
    TextField passwordField;
    Text statusText;
    
    public SubmitLoginHandler(Scene scene) {
        this.scene = scene;
    }
    
    @Override
    public void handle(ActionEvent event) {
        usernameField = (TextField) scene.lookup("#usernameField");
        passwordField = (TextField) scene.lookup("#passwordField");
        statusText = (Text) scene.lookup("#statusText");
        
        try {
            User user = User.get(usernameField.getText(), passwordField.getText());
            User.setCurrentUser(user);
            Main.changeMainScene(new MainScene());
        } catch (UsernameException | PasswordException e) {
            statusText.setText(e.getMessage());
        }
    }
}


public class LoginScene extends StyledScene {
    
    public LoginScene()
    {
        super(new GridPane(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, "login.css");
        
        GridPane gridPane = (GridPane) getRoot();
        gridPane.setPrefSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25));
        gridPane.setAlignment(Pos.CENTER);
        
        Text title = new Text(Constants.APPLICATION_NAME);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrappingWidth(300);
        title.setFont(new Font(25));
        gridPane.add(title, 0, 0, 2, 1);
        
        gridPane.add(new Label("User Name:"), 0, 1);
        TextField usernameField = new TextField();
        usernameField.setId("usernameField");
        gridPane.add(usernameField, 1, 1);
        
        gridPane.add(new Label("Password:"), 0, 2);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        gridPane.add(passwordField, 1, 2);

        Button submitButton = new Button("Sign In");
        submitButton.setPrefSize(110, 25);
        submitButton.setOnAction(new SubmitLoginHandler(this));
        submitButton.setId("submitButton");
        
        Button registerButton = new Button("Register");
        registerButton.setPrefSize(110, 25);
        registerButton.setOnAction(e -> Main.changeMainScene(new RegisterScene()));
        
        Button testAccountButton = new Button("Test account");
        testAccountButton.setPrefSize(110, 25);
        testAccountButton.setOnAction(new TestAccountHandler(this));
        
        Text statusText = new Text();
        statusText.setId("statusText");
        statusText.setTextAlignment(TextAlignment.CENTER);
        
        HBox hbox = new HBox(20);        
        hbox.getChildren().addAll(submitButton, registerButton, testAccountButton);
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(hbox, statusText);
        gridPane.add(vbox, 1, 4);
    }
}
