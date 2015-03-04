package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Assignment;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.JobGroup;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.Resources;

public class MyAssignmentsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/MyAssignmentsTab.fxml";
    
    @FXML private TreeTableView<Assignment> table;
    @FXML private TreeTableColumn<Assignment, Assignment> completedCol;
    @FXML private TreeTableColumn<Assignment, String> materialsCol, coWorkersCol;
    
    private Employee employee;
    
    /* FXML methods */
    
    private class CompletedCell extends TreeTableCell<Assignment, Assignment> {
        @Override
        protected void updateItem(Assignment item, boolean empty) {
            if (item != null & !empty) {
                if (item.getProjectDummy() != null) {
                    setGraphic(null);
                    setText(item.getProjectDummy().toString());
                } else if (item.getJobGroupDummy() != null) {
                    setGraphic(null);
                    setText(item.getJobGroupDummy().toString());
                } else {
                    CheckBox checkBox = new CheckBox(Resources.getResource("assignments.completed"));
                    checkBox.setSelected(item.isCompleted());
                    checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
                        item.setCompleted(checkBox.isSelected());
                        item.save();
                    });
                    setGraphic(checkBox);
                    setText("");
                }
            } else {
                setText("");
                setGraphic(null);
            }
        }
    }
    
    @FXML
	public void initialize() {			
        employee = Employee.getCurrentEmployee();
        
        completedCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Assignment>(param.getValue().getValue()));
        completedCol.setCellFactory(param -> new CompletedCell());
        
        updateTable();
	}
    
    
    public void addProject(Project project) {
        //If project is already added, do nothing
        for (TreeItem<Assignment> item : table.getRoot().getChildren()) {
            if (item.getValue().getProjectDummy() == project) {
                return;
            }
        }
        
        //Otherwise, add the project as a dummy assignment
        Assignment dummyProject = new Assignment();
        dummyProject.setProjectDummy(project);
        
        TreeItem<Assignment> projectRoot = new TreeItem<Assignment>(dummyProject);
        projectRoot.setExpanded(true);
        table.getRoot().getChildren().add(projectRoot);
    }
    
    public void addJobGroup(JobGroup jobGroup) {
        //If the job group is already added, do nothing
        for (TreeItem<Assignment> project : table.getRoot().getChildren()) {
            for (TreeItem<Assignment> item : project.getChildren()) {
                if (item.getValue().getJobGroupDummy() == jobGroup) {
                    return;
                }
            }
        }
       
        //Otherwise, find the tree item corresponding to the project of this job group and add the job group
        for (TreeItem<Assignment> item : table.getRoot().getChildren()) {
            if (item.getValue().getProjectDummy() == jobGroup.getProject()) {
                Assignment dummyJobGroup = new Assignment();
                dummyJobGroup.setJobGroupDummy(jobGroup);
                
                TreeItem<Assignment> jobGroupRoot = new TreeItem<Assignment>(dummyJobGroup);
                jobGroupRoot.setExpanded(true);
                item.getChildren().add(jobGroupRoot);
                return;
            }
        }
    }
    
    public void addAssignment(Assignment assignment) {
        //Go through all items and find the correct job group tree item for this assignment - then add the assignment and return
        for (TreeItem<Assignment> project : table.getRoot().getChildren()) {
            for (TreeItem<Assignment> jobGroup : project.getChildren()) {
                if (jobGroup.getValue().getJobGroupDummy() == assignment.getJob().getJobGroup()) {
                    jobGroup.getChildren().add(new TreeItem<Assignment>(assignment));
                    return;
                }
            }
        }
    }
    
    public void updateTable() {
        for (Assignment assignment : employee.loadAssignments()) {
            addProject(assignment.getProject());
            addJobGroup(assignment.getJob().getJobGroup());
            addAssignment(assignment);
        }
    }
}
