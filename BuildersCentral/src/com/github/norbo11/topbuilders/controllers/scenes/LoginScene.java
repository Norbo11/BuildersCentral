package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class LoginScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/LoginScene.fxml";
    
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Text statusText;
    @FXML Button submitButton;
    
    @FXML
    public void initialize() {
        //TODO This is still broken
        /*Log.info(submitButton);
        Log.info(submitButton.getScene());
        Log.info(submitButton.getScene().getAccelerators());
        Log.info(new KeyCodeCombination(KeyCode.ENTER));
        submitButton.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> submitButton.fire());*/
    }
    
    @FXML
    public void submitLogin(ActionEvent event) {
        try {
            Employee.login(usernameField.getText(), passwordField.getText());            
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
        Employee.loginTestAccount();
    }

	public static String getAbsoluteFxmlFilename() {
		return "/com/github/norbo11/topbuilders/scenes/fxml/" + FXML_FILENAME;
	}
}