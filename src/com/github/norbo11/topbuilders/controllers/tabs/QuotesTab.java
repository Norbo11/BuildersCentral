package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.Vector;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractValidationScene;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.JobGroup;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.util.GoogleMaps;
import com.github.norbo11.topbuilders.util.HeadingTreeTableRow;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.StringHelper;
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
    @FXML private TreeTableView<Job> table;
    @FXML private TreeTableColumn<Job, Job> materialsCol; 
    @FXML private TreeTableColumn<Job, Double> materialsCostCol, labourCostCol, totalCostCol;
        
    /* Factories */
    
    private class MaterialsCell extends TreeTableCell<Job, Job> {
    	
    	GridPane grid;
    	
    	private void addMaterialRow() {
    		grid.addRow(lastRow++, children);
    	}
    	
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            if (empty) {
                setText("");
                setGraphic(null);
            }
            else {                
                grid = new GridPane();
                grid.setVgap(5);
                grid.setHgap(5);
                
                Vector<RequiredMaterial> materials = job.getRequiredMaterials();
                for (int i = 0; i < materials.size(); i++) {
                	RequiredMaterial required = materials.get(i);
                	
                	TextField nameField = new TextField(required.getStockedMaterial().getName());
                	DoubleTextField quantityField = new DoubleTextField(required.getQuantityRequired());
                	
                	grid.addRow(i, nameField, quantityField);
                }
                
                setGraphic(grid);
            }
        }
    }
    
    /* FXML Methods */
    
    @FXML
	public void initialize() {
    	materialsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	materialsCol.setCellFactory(column -> new MaterialsCell());
    	table.setRowFactory(row -> new HeadingTreeTableRow<Job>());
    	
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
        	
        	table.getRoot().getChildren().clear();
        	for (JobGroup group : project.getJobGroups()) {
        		TreeItem<Job> groupRoot = new TreeItem<Job>(new Job(group.getGroupName()));
        		groupRoot.setExpanded(true);
        		
        		for (Job job : group.getJobs()) {
        			groupRoot.getChildren().add(new TreeItem<Job>(job));
        		}
        		
        		table.getRoot().getChildren().add(groupRoot);
        	}
    	}
    }

    @FXML
    public void settings(ActionEvent e) {
    	
    }

    @FXML
    public void viewMap(ActionEvent e) {
    	GoogleMaps.openMap(StringHelper.formatAddress(firstLineAddress.getText(), secondLineAddress.getText(), city.getText(), postcode.getText()));
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
