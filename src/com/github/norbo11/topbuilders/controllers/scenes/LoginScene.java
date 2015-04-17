package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.util.helpers.EmailUtil;
import com.github.norbo11.topbuilders.util.helpers.HashUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;
import com.github.norbo11.topbuilders.util.helpers.StringUtil;


public class LoginScene implements AbstractController {
    public static final String FXML_FILENAME = "scenes/LoginScene.fxml";
    
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Text statusText;
    @FXML private Button submitButton;

    @FXML
    public void initialize() {
    	/* Set enter key behaviour for username and password fields */
        usernameField.setOnAction(e -> submitButton.fire());
        passwordField.setOnAction(e -> submitButton.fire());
    }
    
    @FXML
    public void submitLogin(ActionEvent event) {
        try {
            Employee.login(usernameField.getText(), passwordField.getText());            
        } catch (UsernameException | PasswordException e) {
            statusText.setText(e.getMessage());
        }
    }
    
    @FXML
    public void register(ActionEvent event) {
    	SceneUtil.changeMainScene(RegisterScene.FXML_FILENAME);
    }
    
    @FXML
    public void testAccount(ActionEvent event) {
        Employee.loginTestAccount();
    }
    
    @FXML
    public void forgotPassword() {
    	/* Create a forgot password dialog */
    	Stage stage = StageUtil.createDialogStage("Forgotten your password?");
    	
    	VBox vbox = new VBox(10);
    	vbox.setAlignment(Pos.CENTER);
    	vbox.setPadding(new Insets(30));
    	
    	Label label = new Label("Please enter your username below and we will reset your password, which will be sent by e-mail.");
    	
    	HBox hbox = new HBox(5);
    	hbox.setAlignment(Pos.CENTER);
    	TextField textField = new TextField();
    	hbox.getChildren().addAll(new Label("Username:"), textField);
    	
    	Button button = new Button("Reset my password");
    	
    	/* Set submit button behaviour */
    	button.setOnAction(e -> {
    		//Get the employee object by the user name entered
    		Employee employee = Employee.loadEmployeeByUsername(textField.getText());
    		
    		//If an employee was found
    		if (employee != null) {
    			//Generate a random 7-character password
    			String newPassword = StringUtil.generateRandomString(7);
    			
    			EmailUtil.sendEmail(employee.getEmail(), "Top Builders password reset", ""
    					+ "<p>You have received this message because you attempted to reset your password for the Top Builders project managment system.</p>"
    					+ "<p>Your new password is: " + newPassword + "</p>"
    					+ "<p>We strongly advise that you change this password into a more desirable one after you log in.</p>"
    					+ "<p>Regards,<br />Top Builders team.</p>"
				);	
    			
    			employee.setPassword(HashUtil.generateMD5Hash(newPassword));
                employee.save();
    			
                SceneUtil.closeScene(button);
                SceneUtil.showInfoDialog("Password reset!", "Your password has been reset and an e-mail has been sent to " + employee.getEmail());
    		} else {
    			label.setText("Username not found. Try again.");
    		}
    	});
    	
    	/* Add all components and change the scene */
    	vbox.getChildren().addAll(label, hbox, button);    	
    	SceneUtil.changeScene(stage, vbox);
    }
}