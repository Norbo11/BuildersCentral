package com.github.norbo11.topbuilders.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.github.norbo11.topbuilders.models.User;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class LoginScene {
    public static final String FXML_FILENAME = "LoginScene.fxml";
    
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Text statusText;
    @FXML Button submitButton;
    
    @FXML
    public void submitLogin(ActionEvent event) {
        try {
            User.login(usernameField.getText(), passwordField.getText());
            SceneHelper.changeMainScene(MainScene.FXML_FILENAME);
        } catch (UsernameException | PasswordException e) {
            statusText.setText(e.getMessage());
        }
    }
    
    @FXML
    public void register(ActionEvent event) {
    	SceneHelper.changeMainScene(RegisterScene.FXML_FILENAME);
    }
    
    @FXML
    public void testAccount(ActionEvent event) {
        User.loginTestAccount();
    }
}