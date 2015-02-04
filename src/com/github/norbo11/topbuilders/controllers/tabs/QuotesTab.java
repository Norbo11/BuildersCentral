package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.scenes.AbstractValidationScene;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.TabHelper;
import com.github.norbo11.topbuilders.util.Validation;

public class QuotesTab extends AbstractValidationScene {

    public final static String FXML_FILENAME = "tabs/QuotesTab.fxml";
    
    @FXML private GridPane jobsGrid;
    @FXML private ComboBox<Project> projectPicker;
    
    @FXML private TextArea projectDescription, projectNote;
    @FXML private TextField firstName, lastName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode;
    
    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    @FXML private TreeTableView<Project> table;
        
    @FXML
	public void initialize() {
		update();
		addJobRow(null);
	}
    
    @FXML
    public void addJobRow(ActionEvent e) {     
    	
    }
    
    //Called whenever the the user picks a project using the ComboBox
    @FXML
    public void updateFields(ActionEvent e) {
    	Project project = getCurrentProject();
    	
    	if (project != null) {
        	projectDescription.setText(project.getProjectDescription());
        	projectNote.setText(project.getProjectNote());
        	firstName.setText(project.getClientFirstName());
        	lastName.setText(project.getClientLastName());
        	email.setText(project.getEmail());
        	contactNumber.setText(project.getContactNumber());
        	firstLineAddress.setText(project.getFirstLineAddress());
        	secondLineAddress.setText(project.getSecondLineAddress());
        	city.setText(project.getCity());
        	postcode.setText(project.getPostcode());
    	}
    }

    @FXML
    public void settings(ActionEvent e) {
    	
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
    	        
    	if (validate()) {
    	    //project.setCompleted(completed);
    	    project.setClientFirstName(firstName.getText());
    	    project.setClientLastName(lastName.getText());
    	    project.setFirstLineAddress(firstLineAddress.getText());
    	    project.setSecondLineAddress(secondLineAddress.getText());
    	    project.setCity(city.getText());
    	    project.setPostcode(postcode.getText());
    	    project.setContactNumber(contactNumber.getText());
    	    project.setEmail(email.getText());
    	    project.setProjectDescription(projectDescription.getText());
    	    project.setProjectNote(projectNote.getText());
    	    
            if (project.isDummy()) {
            	project.setDummy(false);
            	project.add();
            } else project.save();
            
            Project current = getCurrentProject();
            TabHelper.updateAllTabs();
            projectPicker.getSelectionModel().select(current);
    	}
    }
    
    /* Instance methods */
    
    public boolean validate() {
        /* Email */
        if (email.getText().length() > 0 && !Validation.checkEmailFormat(email.getText())) addErrorFromResource("validation.invalidEmail");

        return displayErrors();
    }
    
    public Project getCurrentProject() {
		return projectPicker.getSelectionModel().getSelectedItem();
    }
    
    /* Override methods */
    
    @Override
    public void update() {
        Vector<Project> projectList = Project.getAllProjects();
        Project newProject = new Project(Resources.getResource("quotes.newProject"));
        newProject.setDummy(true);
        projectList.add(newProject);
        
        projectPicker.getItems().clear();
        projectPicker.getItems().addAll(projectList);
        projectPicker.getSelectionModel().select(newProject);
    }

	@Override
	public VBox getErrorsList() {
		return errorsList;
	}

	@Override
	public Label getErrorsLabel() {
		return errorsLabel;
	}
}
