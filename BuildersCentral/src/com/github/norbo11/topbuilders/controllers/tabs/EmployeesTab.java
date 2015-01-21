package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;

public class EmployeesTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/EmployeesTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private TreeTableView<Employee> table;
    @FXML private TreeItem<Employee> superusers, managers, employees;
    
    @FXML
	public void initialize() {	
    	for (Employee employee : Employee.getAllEmployees()) {
    		TreeItem<Employee> item = new TreeItem<Employee>(employee);
    		switch (employee.getUserType()) {
    		case SUPERUSER: superusers.getChildren().add(item); break;
    		case MANAGER: managers.getChildren().add(item); break;
    		case EMPLOYEE: superusers.getChildren().add(item); break;
    		}
    	}
	}
}