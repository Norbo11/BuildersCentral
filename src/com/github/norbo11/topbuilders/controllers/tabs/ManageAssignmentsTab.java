package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Assignment;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.ModelFinder;
import com.github.norbo11.topbuilders.util.helpers.GuiUtil;

public class ManageAssignmentsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/ManageAssignmentsTab.fxml";
    
    @FXML private ListView<Project> projectList;
    @FXML private ListView<Assignment> assignmentList;
    @FXML private ListView<Employee> employeeSearchSearchList, employeeAddSearchList;
    @FXML private TreeView<Job> jobList;
    @FXML private TableView<Assignment> assignmentSearchTable;
    @FXML private TextField startDate, endDate, hourlyWage, employeeSearchNameField, employeeAddNameField;
    @FXML private Label employeeAddLabel;
    private ModelFinder<Employee> employeeSearchFinder, employeeAddFinder;
    
    /* FXML methods */
    
    private static class AssignmentCellFactory implements Callback<ListView<Assignment>, ListCell<Assignment>> {
        @Override
        public ListCell<Assignment> call(ListView<Assignment> param) {
            return new ListCell<Assignment>() {
                @Override
                protected void updateItem(Assignment item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    setText("");
                    if (item != null && !empty) {
                        HBox box = new HBox(5);
                        
                        Button button = new Button("X");
                        button.setOnAction(e -> { 
                            //TODO This isnt being called
                            System.out.println("here");
                            item.delete();
                        });
                        
                        Label label = new Label(item.getEmployee().getFullName());
                        box.getChildren().addAll(button, label);
                        
                        setGraphic(box);
                    } else {
                        setGraphic(null);
                    }
                }
            };
        }
    }
    
    @FXML
	public void initialize() {		
        /* Load necessary resources */
        Assignment.loadAssignments();
        Employee.loadEmployees();
        
        /* Populate project list */
        projectList.getItems().setAll(Project.loadProjects()); 
        projectList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, clickedProject) -> {
            if (clickedProject != null) {
                clickedProject.populateTreeTable(jobList);
            }
        });
        
        /* Define job list behaviour */
        jobList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, clickedJob) -> {
            if (clickedJob != null && !clickedJob.getValue().isDummy()) {
                selectJob(clickedJob.getValue());
            }
        });       
        
        /* Define assignment list cell factory */
        assignmentList.setCellFactory(new AssignmentCellFactory());
        
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
            GuiUtil.hideNodeManaged(employeeSearchSearchList);
        });
        
        hideEmployeeAddArea();
    }    
    
    /* Instance methods */
    
    public void hideEmployeeAddArea() {
        employeeAddNameField.setVisible(false);
        employeeAddLabel.setVisible(false);
    }
    
    public void showEmployeeAddArea() {
        employeeAddNameField.setVisible(true);
        employeeAddLabel.setVisible(true);
    }
    
    public void selectJob(Job job) {
        assignmentList.getItems().setAll(job.getAssignments());
        showEmployeeAddArea();
    }

    public Job getSelectedJob() {
        return jobList.getSelectionModel().getSelectedItem().getValue();
    }
}
