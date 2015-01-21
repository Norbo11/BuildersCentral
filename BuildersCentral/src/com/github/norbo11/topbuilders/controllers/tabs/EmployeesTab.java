package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;

public class EmployeesTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/EmployeesTab.fxml";
    
    public class EmployeeFullNameValueFactory implements Callback<TreeTableColumn<Employee, String>, TreeTableCell<Employee, String>> {

		@Override
		public TreeTableCell<Employee, String> call(
				TreeTableColumn<Employee, String> arg0) {
			// TODO Auto-generated method stub
			return null;
		}


    }
    
    @FXML private ResourceBundle resources;
    @FXML private TreeTableView<Employee> table;
    @FXML private TreeItem<Employee> superusers, managers, employees;
    
    @FXML
	public void initialize() {	
    	for (Employee employee : Employee.getAllEmployees()) {
    		TreeItem<Employee> item = new TreeItem<Employee>(employee);
    		
    		table.getColumns().get(0).setCellFactory(new EmployeeFullNameValueFactory());
    		    		
    		switch (employee.getUserType()) {
    		case SUPERUSER: superusers.getChildren().add(item); break;
    		case MANAGER: managers.getChildren().add(item); break;
    		case EMPLOYEE: employees.getChildren().add(item); break;
    		}
    	}
	}
}