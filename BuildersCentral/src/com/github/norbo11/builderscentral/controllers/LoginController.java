package com.github.norbo11.builderscentral.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.github.norbo11.builderscentral.Main;
import com.github.norbo11.builderscentral.models.User;
import com.github.norbo11.builderscentral.models.exceptions.PasswordException;
import com.github.norbo11.builderscentral.models.exceptions.UsernameException;

public class LoginController {
    
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Text statusText;
    
    @FXML private void submitLogin(ActionEvent event) {
        try {
            User user = User.get(usernameField.getText());

            if (user.checkPassword(passwordField.getText()))
            {
                User.setCurrentUser(user);
                MainController controller = Main.<MainController>changeScene("main");
                controller.welcomeText.setText("Welcome, " + user.getUsername());
            }
        } catch (UsernameException | PasswordException e) {
            statusText.setText(e.getMessage());
        }
    }
}
