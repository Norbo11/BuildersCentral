package com.github.norbo11.topbuilders.controllers.scenes;

import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.tabs.EmployeesTab;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.HashUtil;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.Validation;

public class ModifyEmployeeScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/ModifyEmployeeScene.fxml";
    
    private Employee employee;
    private boolean isNew;
    
    @FXML private ResourceBundle resources;
    @FXML private TextField username, email, firstName, lastName, firstLineAddress, secondLineAddress, city, postcode;
    @FXML private Label activationCode, errorsLabel;
    @FXML private DoubleTextField defaultWage;
    @FXML private ComboBox<UserType> userType;
    @FXML private VBox errors;
    
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
        SceneHelper.closeScene((Node) event.getSource());
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
	            
	            String info = Resources.getResource(resources, "activationCodeInfo", code);
	            SceneHelper.showInfoDialog(Resources.getResource(resources, "general.info"), info, () -> SceneHelper.closeScene((Node) event.getSource()));
	        }
	        employee.setActivationCode(activationCode.getText());
	        
	        if (isNew) employee.add();
	        else employee.save();
	        
	        EmployeesTab.updateAllTabs();
	        SceneHelper.closeScene((Node) event.getSource());
    	}
    }

	@FXML
    public void deleteEmployee(ActionEvent event) {
        employee.delete();
    }

    public void updateFields() {
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
        if (code.equals("") && !isNew) code = Resources.getResource(resources, "employees.activationCodeActivated");
        activationCode.setText(code);
    }    
    
    private boolean validate() {
    	Vector<Label> errors = new Vector<Label>();
    	boolean userChanged = !employee.getUsername().equals(username.getText());
    	boolean emailChanged = !employee.getEmail().equals(email.getText());
    	
    	/* Username */
    	if (username.getText().length() > 0) {
    		if (userChanged && Employee.checkUsernameExists(username.getText())) {
        		errors.add(new Label(Resources.getResource(resources, "validation.usernameExists")));
        	}
    	} else errors.add(new Label(Resources.getResource(resources, "validation.usernameRequired")));
    	
    	
    	/* Email */
    	if (email.getText().length() > 0) {
    		
    		if (Validation.checkEmailFormat(email.getText())) {
    			if (emailChanged && Employee.checkEmailExists(email.getText())) {
            		errors.add(new Label(Resources.getResource(resources, "validation.emailExists")));
            	}
        	} else errors.add(new Label(Resources.getResource(resources, "validation.invalidEmail")));
    	} else errors.add(new Label(Resources.getResource(resources, "validation.emailRequired")));
    	
    	if (errors.size() > 0) {
    		displayErrors(errors);
    		
    		return false;
    	} else return true;
	}

	private void displayErrors(Vector<Label> errors) {
		errorsLabel.setVisible(true);
		ObservableList<Node> errorList = this.errors.getChildren();
		errorList.clear();
		errorList.addAll(errors);
	}
}
