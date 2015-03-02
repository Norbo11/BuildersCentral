package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.custom.ValidationInfo;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.Validation;
import com.github.norbo11.topbuilders.util.helpers.HashUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;

public class ModifyEmployeeScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/ModifyEmployeeScene.fxml";
    
    private Employee employee;
    private boolean isNew;
    
    @FXML private ValidationInfo validation;
    
    @FXML private TextField username, email, firstName, lastName, firstLineAddress, secondLineAddress, city, postcode;
    @FXML private DoubleTextField defaultWage;
    @FXML private Label activationCode;
    @FXML private ComboBox<UserType> userType;
    
    public void setEmployee(Employee employee, boolean isNew) {
        this.employee = employee; 
        this.isNew = isNew;
    }
    
    @FXML
    public void initialize() {
        UserType.populateCombo(userType);  
    }
    
    @FXML
    public void close(ActionEvent event) {
        SceneUtil.closeScene((Node) event.getSource());
    }
    
    @FXML
    public void saveEmployee(ActionEvent event) {
    	if (validate()) {
	        employee.setUsername(username.getText());
	        employee.setEmail(email.getText());
	        employee.setFirstName(firstName.getText());
	        employee.setLastName(lastName.getText());
	        employee.setDefaultWage(defaultWage.getDouble());
	        employee.setFirstLineAddress(firstLineAddress.getText());
	        employee.setSecondLineAddress(secondLineAddress.getText());
	        employee.setCity(city.getText());
	        employee.setPostcode(postcode.getText());     
	        employee.setUserTypeId(userType.getSelectionModel().getSelectedItem().getRank());
	        
	        if (isNew) {
	            String code = HashUtil.generateMD5Hash(username.getText());
	            activationCode.setText(code);
	            
	            String info = Resources.getResource("employees.activationCodeInfo", code);
	            SceneUtil.showInfoDialog(Resources.getResource("general.info"), info, () -> SceneUtil.closeScene((Node) event.getSource()));
	        }
	        employee.setActivationCode(activationCode.getText());
	        
	        if (isNew) employee.add();
	        else employee.save();
	        
	        updateAll();
	        SceneUtil.closeScene((Node) event.getSource());
    	}
    }

    public void updateAll() {
        username.setText(employee.getUsername());
        email.setText(employee.getEmail());
        firstName.setText(employee.getFirstName());
        lastName.setText(employee.getLastName());
        firstLineAddress.setText(employee.getFirstLineAddress());
        secondLineAddress.setText(employee.getSecondLineAddress());
        city.setText(employee.getCity());
        postcode.setText(employee.getPostcode());
        defaultWage.setText(employee.getDefaultWage() + "");
        userType.getSelectionModel().select(employee.getUserType());
        
        String code = employee.getActivationCode();
        if (code.equals("") && !isNew) code = Resources.getResource("employees.activationCodeActivated");
        activationCode.setText(code);
    }    
    

    public boolean validate() {
    	boolean userChanged = !employee.getUsername().equals(username.getText());
    	boolean emailChanged = !employee.getEmail().equals(email.getText());
    	
    	/* Username */
    	if (username.getText().length() > 0) {
    		if (userChanged && Employee.checkUsernameExists(username.getText())) {
    		    validation.addErrorFromResource("validation.usernameExists");
        	}
    	} else validation.addErrorFromResource("validation.usernameRequired");
    	
    	
    	/* Email */
    	if (email.getText().length() > 0) {
    		if (Validation.checkEmailFormat(email.getText())) {
    			if (emailChanged && Employee.checkEmailExists(email.getText())) {
    				validation.addErrorFromResource("validation.emailExists");
            	}
        	} else validation.addErrorFromResource("validation.invalidEmail");
    	} else validation.addErrorFromResource("validation.emailRequired");
    	
    	/* Name */
    	if (firstName.getText().length() == 0 || lastName.getText().length() == 0) {
    		validation.addErrorFromResource("validation.namesRequired");
    	}
    	
		return validation.displayErrors();
	}
}
