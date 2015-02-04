package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.TabHelper;

public class QuotesTab extends AbstractController {

    public final static String FXML_FILENAME = "tabs/QuotesTab.fxml";
    
    
    @FXML private ComboBox<Project> projectPicker;
    @FXML private TextField clientFirstName;
    @FXML private GridPane jobsGrid;
    private int lastRow;
    private boolean isNew;
    
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
    public void updateFields() {
    	
    }

    @FXML
	public void initialize() {
		update();
		addJobRow();
	}

    @FXML
    public void viewMap(ActionEvent e) {
    	
    }
    
    @FXML
    public void export(ActionEvent e) {
    	
    }
    
    @FXML
    public void saveProject(ActionEvent e) {
    	Project project = getCurrentProject();
    	        
        if (isNew) project.add();
        else project.save();
        
        TabHelper.updateAllTabs();
    }
    
    /* Instance methods */
    
    public Project getCurrentProject() {
		return projectPicker.getSelectionModel().getSelectedItem();
    }
    
    @Override
    public void update() {
    	Vector<Project> projectList = Project.getAllProjects();
        
        Project newProject = new Project("New Project");
        projectList.add(newProject);
        
        projectPicker.getItems().clear();
        projectPicker.getItems().addAll(projectList);
    }
}
