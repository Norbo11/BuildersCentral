package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Assignment;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.Project;

public class ManageAssignmentsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/ManageAssignmentsTab.fxml";
    
    @FXML private ListView<Project> projectList;
    @FXML private ListView<Employee> employeeList;
    @FXML private TreeView<Job> jobList;
    @FXML private TextField startDate, endDate, hourlyWage, employeeName;
    @FXML private TableView<Assignment> assignmentTable;
    
    /* FXML methods */
    
    @FXML
	public void initialize() {			

	}

}
