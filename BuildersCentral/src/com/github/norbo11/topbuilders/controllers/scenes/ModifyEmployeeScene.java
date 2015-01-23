package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class ModifyEmployeeScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/ModifyEmployeeScene.fxml";
    
    private Employee employee;
    @FXML private TextField username, email, firstName, lastName, firstLineAddress, secondLineAddress, city, postcode;
    @FXML private DoubleTextField defaultWage;
    @FXML private ComboBox<UserType> userType;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        employee.setEmail(email.getText());
        employee.setDefaultWage(defaultWage.getDouble());
        employee.setFirstLineAddress(firstLineAddress.getText());
        employee.setSecondLineAddress(secondLineAddress.getText());
        employee.setCity(city.getText());
        employee.setPostcode(postcode.getText());
        employee.setUserType(userType.getSelectionModel().getSelectedItem());
        employee.save();
    }
    
    @FXML
    public void deleteEmployee(ActionEvent event) {
        employee.delete();
    }

    public void bind() {
    	//Bind every property except the user type as it uses a ComboBox which has a read-only selection property	

    }    
}
