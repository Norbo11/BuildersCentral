package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class ModifyEmployeeScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/ModifyEmployeeScene.fxml";
    
    private Employee employee;
    @FXML private TextField username, email, firstName, lastName, defaultWage, firstLineAddress, secondLineAddress, city, postcode;
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
        
    }
    
    @FXML
    public void deleteEmployee(ActionEvent event) {
        
    }

    public void bind() {
        username.textProperty().bindBidirectional(employee.usernameProperty());
        email.textProperty().bindBidirectional(employee.emailProperty());
        firstName.textProperty().bindBidirectional(employee.firstNameProperty());
        lastName.textProperty().bindBidirectional(employee.lastNameProperty());
        email.textProperty().bindBidirectional(employee.emailProperty());
        defaultWage.textProperty().bindBidirectional(employee.defaultWageProperty(), new NumberStringConverter());
        firstLineAddress.textProperty().bindBidirectional(employee.firstLineAddressProperty());
        secondLineAddress.textProperty().bindBidirectional(employee.secondLineAddressProperty());
        city.textProperty().bindBidirectional(employee.cityProperty());
        postcode.textProperty().bindBidirectional(employee.postcodeProperty());
        employee.userTypeProperty().bind(userType.getSelectionModel().selectedItemProperty());
    }    
}
