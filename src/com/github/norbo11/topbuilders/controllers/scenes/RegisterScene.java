package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.custom.ValidationInfo;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.util.helpers.HashUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;

public class RegisterScene {
	public static final String FXML_FILENAME = "scenes/RegisterScene.fxml";
        
    @FXML TextField code, newPass, confirmPass;
    @FXML VBox activateForm, passwordForm, parent;
    @FXML ValidationInfo validation;
    
    private Employee employee;
    
    @FXML
    public void next(ActionEvent event) {
        String code = this.code.getText();
        
        if (!code.equals("")) {
            employee = Employee.checkActivationCode(code);
            
            if (employee != null) {
                parent.getChildren().remove(activateForm);
                passwordForm.setVisible(true);
                validation.validate();
                return;
            }
        }  
        
        validation.addError("Invalid activation code! Please ensure that you have typed the 12-character code EXACTLY as it was given to you, or contact your manager.");
        validation.displayErrors(false);
    }

    @FXML
    public void activate(ActionEvent event) {        
        if (newPass.getText().length() > 0) {
            if (newPass.getText().equals(confirmPass.getText())) {
                employee.setPassword(HashUtil.generateMD5Hash(newPass.getText()));
                employee.removeActivationCode();
                employee.save();
                employee.login();
                return;
            } else validation.addError("Passwords must match");
        } else validation.addError("You must input a password");
        
        validation.validate();
    }
    
    @FXML
    public void back(ActionEvent event) {
        SceneUtil.changeMainScene(LoginScene.FXML_FILENAME);
    }
}
