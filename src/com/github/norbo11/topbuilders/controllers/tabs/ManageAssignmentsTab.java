package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.models.Assignment;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.ModelFinder;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.GuiUtil;

public class ManageAssignmentsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/ManageAssignmentsTab.fxml";
    
    @FXML private ListView<Project> projectList;
    @FXML private ListView<Assignment> assignmentList;
    @FXML private ListView<Employee> employeeSearchSearchList, employeeAddSearchList;
    @FXML private TreeView<Job> jobList;
    @FXML private TableView<Assignment> assignmentSearchTable;
    @FXML private TextField employeeSearchNameField, employeeAddNameField;
    @FXML private DatePicker startDate, endDate;
    @FXML private DoubleTextField hourlyWage;
    @FXML private Label employeeAddLabel;
    @FXML private TitledPane assignmentDetailsPane;
    private ModelFinder<Employee> employeeSearchFinder, employeeAddFinder;
    
    /* FXML methods */
    
    private static class AssignmentCell extends ListCell<Assignment> {
    	private HBox box;
    	private Label label;
    	
    	public AssignmentCell() {
    		Button button = new Button("X");
            button.setOnAction(e -> { 
                getItem().delete();
                getListView().getItems().remove(getItem());
            });
            
            label = new Label();
            
            box = new HBox(5);
            box.getChildren().addAll(button, label);
    	}
    	
        @Override
        protected void updateItem(Assignment item, boolean empty) {
            super.updateItem(item, empty);
            
            setText("");
            if (item != null && !empty) {
            	label.setText(getItem().getEmployee().getFullName());
                setGraphic(box);
            } else {
                setGraphic(null);
            }
        }
    }
    
    @FXML
	public void initialize() {		
        /* Populate project list */
        projectList.getItems().setAll(Project.getProjects()); 
        
        /* Define project list behaviour */
        projectList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, clickedProject) -> {
            if (clickedProject != null) {
                selectProject(clickedProject);
            }
            
            updateAssignmentDetails();
        });
        
        /* Define job list behaviour */
        jobList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, clickedJob) -> {
            if (clickedJob != null && !clickedJob.getValue().isDummy()) {
                showEmployeeAddArea();
            } else {
                hideEmployeeAddArea();
            }
            
            updateAssignmentsBasedOnJob();
            updateAssignmentDetails();
        });       
        
        /* Define assignment list cell factory */
        assignmentList.setCellFactory((param) -> new AssignmentCell());
        
        /* Define assignment list behaviour */
        assignmentList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, clickedAssignment) -> {
        	updateAssignmentDetails();
        });
        
        /* Search functions */
        this.employeeSearchFinder = new ModelFinder<Employee>(employeeSearchSearchList, Employee.getEmployees(), (entry, input) -> {
            return entry.getFirstName().toUpperCase().startsWith(input.toUpperCase()) || entry.getLastName().toUpperCase().startsWith(input.toUpperCase()); //Standard search by name
        });
        
        this.employeeAddFinder = new ModelFinder<Employee>(employeeAddSearchList, Employee.getEmployees(), (entry, input) -> {
          //Iterate through the current assignments and only filter based on whether an employee is not already assigned to this job (as well as their name compared to the input)
            boolean alreadyAssigned = false;
            for (Assignment assignment : assignmentList.getItems()) {
                if (assignment.getEmployeeId() == entry.getId()) alreadyAssigned = true;
            }
            
            return (entry.getFirstName().toUpperCase().startsWith(input.toUpperCase()) || entry.getLastName().toUpperCase().startsWith(input.toUpperCase())) && !alreadyAssigned;
        });
	
        /* Hook up search text fields to respective model finders */
        employeeSearchNameField.textProperty().addListener((obs, oldVal, newVal) -> employeeSearchFinder.search(oldVal, newVal));
        employeeAddNameField.textProperty().addListener((obs, oldVal, newVal) -> employeeAddFinder.search(oldVal, newVal));
        
        /* Define behaviour upon clicking a found employee (add an assignment) */
        employeeAddSearchList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, clickedEmployee) -> {           
            if (clickedEmployee != null) {
	        	//Create new assignment, add it to database
	            Assignment assignment = new Assignment();
	            assignment.setNewModel(true);
	            assignment.setEmployee(clickedEmployee);
	            assignment.setEmployeeId(clickedEmployee.getId());
	            assignment.setJobId(getSelectedJob().getId());
	            assignment.setHourlyWage(clickedEmployee.getDefaultWage());
	            assignment.save();
	            
	            //Add to the assignment list and hide the search list
	            assignmentList.getItems().add(assignment);
	            GuiUtil.hideNodeManaged(employeeAddSearchList);
	            
	            //Select the recently added item so that assignment details may be edited
	            assignmentList.getSelectionModel().select(assignment);
            }
        });
        
        /* Define behaviour upon clicking a found employee (display all found assignments) */
        employeeSearchSearchList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, clickedEmployee) -> {                
            if (clickedEmployee != null) {
                assignmentSearchTable.getItems().setAll(clickedEmployee.getAssignments());
            }
        });
        
        hideEmployeeAddArea();
        disableAssignmentDetailsArea();
    }    
    
    @FXML
    public void saveDetails() {
    	Assignment assignment = getSelectedAssignment();
    	
    	if (assignment != null) {
    		assignment.setStartTimestamp(DateTimeUtil.getTimestampFromDate(startDate.getValue()));
    		assignment.setEndTimestamp(DateTimeUtil.getTimestampFromDate(endDate.getValue()));
    		assignment.setHourlyWage(hourlyWage.getDouble());
    		
    		assignment.save();
    		getSelectedJob().getAssignments().add(assignment);
    	}
    }
    
    /* Instance methods */
    
    public void disableAssignmentDetailsArea() {
        assignmentDetailsPane.setDisable(true);
    }
    
    public void enableAssignmentDetailsArea() {
        assignmentDetailsPane.setDisable(false);
    }
    
    public void hideEmployeeAddArea() {
        employeeAddNameField.setVisible(false);
        employeeAddLabel.setVisible(false);
    }
    
    public void showEmployeeAddArea() {
        employeeAddNameField.setVisible(true);
        employeeAddLabel.setVisible(true);
    }
    
    public void selectProject(Project project) {
        project.populateTreeTable(jobList);
    }
    
    public void updateAssignmentsBasedOnJob() {
        Job job = getSelectedJob();
        
        if (job != null && !job.isDummy()) {
            assignmentList.getItems().setAll(job.getAssignments());
        } else assignmentList.getItems().clear();
    }
    
    public void updateAssignmentDetails() {
        Assignment assignment = getSelectedAssignment();
        
        if (assignment != null) {
    		startDate.setValue(DateTimeUtil.getDateFromTimestamp(assignment.getStartTimestamp()));
    		endDate.setValue(DateTimeUtil.getDateFromTimestamp(assignment.getEndTimestamp()));
    		hourlyWage.setDouble(assignment.getHourlyWage());
    		
    		assignmentDetailsPane.setText(Resources.getResource("manageAssignments.details", getSelectedJob().getTitle(), assignment.getEmployee().getFullName()));
    		enableAssignmentDetailsArea();
        } else disableAssignmentDetailsArea();
    }

    public Job getSelectedJob() {
        TreeItem<Job> treeItem = jobList.getSelectionModel().getSelectedItem();
        return treeItem == null ? null : treeItem.getValue();
    }
    
    public Assignment getSelectedAssignment() {
        return assignmentList.getSelectionModel().getSelectedItem();
    }
}
