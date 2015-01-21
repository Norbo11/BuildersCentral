package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.NewProject;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.ProjectManager;

public class QuotesTab extends AbstractController {

    public final static String FXML_FILENAME = "tabs/QuotesTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private ComboBox<Project> projectPicker;
    @FXML private TextField clientNameField;
    @FXML private GridPane jobsGrid;
    private int lastRow;
    
    @FXML
    public void addJobRow() {        
        TextField[] fields = new TextField[3];
        
        for (int i = 0; i < fields.length; i++)
        {
            fields[i] = new TextField();
            fields[i].setPrefWidth(200);
        }
     
        jobsGrid.addRow(lastRow++, fields);
    }
    
    @FXML
    private void updateFields() {
        if (getCurrentProject() != null) clientNameField.setText(getCurrentProject().getClientName());
    }

    @FXML
    private void updateProject() {
    	Project project = getCurrentProject();
    	
    	project.setClientName(clientNameField.getText());
    	project.save();
        populatePicker(project);
    }

    private void populatePicker(Project viewProject) {
        ObservableList<Project> projectList = ProjectManager.fetchProjects();
        NewProject newProject = new NewProject();
        projectList.add(newProject);
        projectPicker.getItems().clear();
        projectPicker.getItems().addAll(projectList);
        projectPicker.getSelectionModel().select(viewProject != null ? viewProject : newProject);
    }

    private Project getCurrentProject() {
    	return projectPicker.getSelectionModel().getSelectedItem();
    }
    	
    @FXML
	public void initialize() {
		populatePicker(null);
		addJobRow();
	}
}
