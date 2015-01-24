package com.github.norbo11.topbuilders.controllers.scenes;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.tabs.EmployeesTab;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.HashUtil;
import com.github.norbo11.topbuilders.util.ResourceUtil;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class ModifyEmployeeScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/ModifyEmployeeScene.fxml";
    
    private Employee employee;
    private boolean isNew;
    
    @FXML private ResourceBundle resources;
    @FXML private TextField username, email, firstName, lastName, firstLineAddress, secondLineAddress, city, postcode;
    @FXML private Label activationCode;
    @FXML private DoubleTextField defaultWage;
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
        SceneHelper.closeScene((Node) event.getSource());
    }
    
    @FXML
    public void saveEmployee(ActionEvent event) {
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
            
            String info = ResourceUtil.getResourceWithParameters(resources, "employees.activationCodeInfo", code);
            SceneHelper.showInfoDialog(resources.getString("general.info"), info, () -> SceneHelper.closeScene((Node) event.getSource()));
        }
        employee.setActivationCode(activationCode.getText());
        
        if (isNew) employee.add();
        else employee.save();
        
        EmployeesTab.updateAllTabs();
        SceneHelper.closeScene((Node) event.getSource());
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
        if (code.equals("") && !isNew) code = resources.getString("employees.activationCodeActivated");
        activationCode.setText(code);
    }    
}
