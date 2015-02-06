package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.Vector;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;

import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractValidationScene;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.JobGroup;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.models.StockedMaterial;
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
    @FXML private ComboBox<JobGroup> jobGroupCombo;
    
    @FXML private TextArea projectDescription, projectNote;
    @FXML private TextField firstName, lastName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode;
    
    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    @FXML private TreeTableView<Job> table;
    @FXML private TreeTableColumn<Job, Job> materialsCol; 
    @FXML private TreeTableColumn<Job, String> titleCol, descriptionCol;
    @FXML private TreeTableColumn<Job, Double> materialsCostCol, labourCostCol, totalCostCol;
        
    /* Factories */
    
    private class JobTreeItem extends TreeItem<Job> {
        private EventHandler<TreeModificationEvent<Job>> cancelExpandHandler = new EventHandler<TreeModificationEvent<Job>>() {
            @Override
            public void handle(TreeModificationEvent<Job> e) {
                e.consume();
            }
        };

        public JobTreeItem(Job job) {
            super(job);
            
            this.setGraphic(null);
            this.addEventHandler(branchExpandedEvent(), cancelExpandHandler);
            this.addEventHandler(branchCollapsedEvent(), cancelExpandHandler);
        }
    }
    
    private class MaterialsCell extends TreeTableCell<Job, Job> {
    	GridPane grid;
    	int lastRow = 0;
    	Button newMaterialButton;
    	//boolean toggled = false;
    	    	
    	public MaterialsCell() {
    	    super();
    	    
    	    grid = new GridPane();
            grid.setVgap(5);
            grid.setHgap(5);
            grid.setMaxWidth(Double.MAX_VALUE);
            
            //Set the second column to always grow
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setFillWidth(true);
            constraint.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().addAll(new ColumnConstraints(), constraint);
    	    
    	    newMaterialButton = new Button(Resources.getResource("quotes.newMaterial"));
            newMaterialButton.setMaxWidth(Double.MAX_VALUE);
            newMaterialButton.setOnAction(e -> {
                removeButton(); 
                addMaterialRow("", 0, ""); 
                addButton();
             });
            
            GridPane.setColumnSpan(newMaterialButton, 4);
            GridPane.setHgrow(newMaterialButton, Priority.ALWAYS);
    	}
    	
        /*private void toggleButton() {
            if (!toggled) {
                 addButton();
             } else {
                 removeButton();
             }
        }*/
    	
    	private void addButton() {
    	    grid.addRow(lastRow, newMaterialButton);
            lastRow++;
            //toggled = true;
        }

        private void removeButton() {
    	    grid.getChildren().remove(newMaterialButton);
            lastRow--;
            //toggled = false;
        }

        private void addMaterialRow(String materialName, double quantityRequired, String materialType) {
            Button deleteButton = new Button("X");
            
    		TextField nameField = new TextField(materialName);
    		nameField.setMaxHeight(Double.MAX_VALUE);
    		
            DoubleTextField quantityField = new DoubleTextField(quantityRequired);
            quantityField.setMinWidth(50);
            quantityField.setPrefWidth(50);
            
            Label typeLabel = new Label(materialType);
            typeLabel.setMinWidth(30);
            typeLabel.setPrefWidth(30);
            
            grid.addRow(lastRow, deleteButton, nameField, quantityField, typeLabel);
            lastRow++;
    	}
    	
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            if (empty) {
                setText("");
                setGraphic(null);
            }
            else {            
                if (!job.isDummy()) {
                    /*setOnMouseExited(e -> toggleButton());
                    setOnMouseEntered(e -> toggleButton());*/
                    lastRow = 0;
                    grid.getChildren().clear();
                    
                    Vector<RequiredMaterial> materials = job.getRequiredMaterials();
                    
                    for (RequiredMaterial requiredMaterial : materials) {
                        StockedMaterial stockedMaterial = requiredMaterial.getStockedMaterial();
                    	addMaterialRow(stockedMaterial.getName(), requiredMaterial.getQuantityRequired(), stockedMaterial.getQuantityType().toString());
                    }
                    
                    addButton();
                    setGraphic(grid);
                }
            }
        }
    }
    
    private class JobTableRow extends HeadingTreeTableRow<Job> {        
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            
            setDisclosureNode(null);
        }
    }
    
    /* FXML Methods */
    
    @FXML
	public void initialize() {
    	Project.loadProjects();
		update();
    	
        titleCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        descriptionCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        
    	materialsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	materialsCol.setCellFactory(column -> new MaterialsCell());
    	table.setRowFactory(row -> new JobTableRow());
    	
    	materialsCostCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new DoubleStringConverter()));
    	labourCostCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new DoubleStringConverter()));
	}
    
    @FXML
    public void addJobRow(ActionEvent e) {   
    	JobGroup selectedGroup = jobGroupCombo.getSelectionModel().getSelectedItem();
    	
    	//Iterate through all job groups (which will all be dummy Job models)
    	for (TreeItem<Job> item : table.getRoot().getChildren()) {
    	    Job job = item.getValue();
    	    
    	    //As soon as you find the dummy model with the name of the selected group (stored in the title field), add a new job to its children and return
    	    if (job.getTitle().equals(selectedGroup.getGroupName())) {
    	    	
    	    	Job newJob = new Job();
    	    	newJob.setTitle(Resources.getResource("jobs.title"));
    	    	newJob.setDescription(Resources.getResource("jobs.description"));
    	    	newJob.setNewModel(true);
    	    	newJob.setJobGroupId(selectedGroup.getId());
    	    	
    	    	item.getChildren().add(new JobTreeItem(newJob));
    	        return;
    	    }
    	}
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
        	    JobTreeItem groupRoot = new JobTreeItem(new Job(group.getGroupName()));
        		groupRoot.setExpanded(true);
        		
        		for (Job job : group.getJobs()) {
        			groupRoot.getChildren().add(new JobTreeItem(job));
        		}
        		
        		table.getRoot().getChildren().add(groupRoot);
        	}
        	
        	jobGroupCombo.getItems().clear();
        	jobGroupCombo.getItems().addAll(project.getJobGroups());
        	jobGroupCombo.getSelectionModel().select(0);
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
    	    
    	    ///Go through job groups
    	    for (TreeItem<Job> groupItem : table.getRoot().getChildren()) {
    	        Job jobGroup = groupItem.getValue();
    	        Vector<Job> jobs = new Vector<Job>();
    	        
    	        //Go through jobs in this group
    	        for (TreeItem<Job> jobItem : groupItem.getChildren()) {
    	            jobs.add(jobItem.getValue());
    	        }

	            project.updateJobGroup(jobGroup.getTitle(), jobs);  	        
    	    }
    	    
            if (project.isNewModel()) {
            	project.setNewModel(false);
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
        Vector<Project> projectList = Project.getProjects();
        Project newProject = new Project(Resources.getResource("quotes.newProject"));
        newProject.setNewModel(true);
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
