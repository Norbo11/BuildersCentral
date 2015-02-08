package com.github.norbo11.topbuilders.controllers.tabs;
import java.util.Vector;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
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
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.Validation;
import com.github.norbo11.topbuilders.util.factories.HeadingTreeTableRow;
import com.github.norbo11.topbuilders.util.factories.TextAreaTreeTableCell;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StringUtil;

public class QuotesTab extends AbstractValidationScene {

    public final static String FXML_FILENAME = "tabs/QuotesTab.fxml";
    
    @FXML private GridPane jobsGrid;
    @FXML private ComboBox<Project> projectPicker;
    @FXML private ComboBox<JobGroup> jobGroupCombo;
    @FXML private Button deleteProjectButton;
    @FXML private TextField newGroupField;
    @FXML private Button newGroupButton;
    
    @FXML private TextArea projectDescription, projectNote;
    @FXML private TextField firstName, lastName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode;
    
    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    @FXML private TreeTableView<Job> table;
    @FXML private TreeTableColumn<Job, Job> materialsCol, deleteJobCol; 
    @FXML private TreeTableColumn<Job, String> titleCol, descriptionCol;
    @FXML private TreeTableColumn<Job, Double> materialsCostCol, labourCostCol, totalCostCol;
    private Vector<Project> projects;        
    
    /* Factories, cells and rows */
   
    //Class which cancels the expanding events of tree items
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
    
    //Class which removes the expansion arrow of tree items
    private class JobTableRow extends HeadingTreeTableRow<Job> {        
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            
            setDisclosureNode(null); //Responsible for the "arrow" node
        }
    }
    
    //Class which handles the displaying of materials in the material column
    private class MaterialsCell extends TreeTableCell<Job, Job> {
    	GridPane grid;
    	int lastRow = 0;
    	Button newMaterialButton;
    	Job job;
    	//boolean toggled = false;
    	    	
    	public MaterialsCell() {
    	    super();
    	    
    	    /* Initialize components which never change */
    	    newMaterialButton = new Button(Resources.getResource("quotes.newMaterial"));
            newMaterialButton.setMaxWidth(Double.MAX_VALUE);
            newMaterialButton.setOnAction(e -> {
                removeButton(); //Remove the New Material button temporarily, so that a new material row can be added in its place
                
                /* Make a new required material, add it to the job*/
                RequiredMaterial newMaterial = new RequiredMaterial();
                newMaterial.setNewModel(true);
                newMaterial.jobIdProperty().bind(job.idProperty());
                newMaterial.setParent(job);
                job.getChildren().add(newMaterial);
                
                TextField field = addMaterialRow(newMaterial); 
                field.requestFocus();
                addButton(); //Re-add the New Material button
             });
            
            grid = new GridPane();
            grid.setVgap(5);
            grid.setHgap(5);
            grid.setMaxWidth(Double.MAX_VALUE);
            
            /* Below code ensures that the New Material button is always as wide as the whole cell */
            ColumnConstraints constraint = new ColumnConstraints();
            constraint.setFillWidth(true); 
            constraint.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().addAll(new ColumnConstraints(), constraint);
            
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

        private TextField addMaterialRow(RequiredMaterial requiredMaterial) {
            Button deleteButton = new Button("X");
            deleteButton.setOnAction(e -> {
                requiredMaterial.delete();
                requiredMaterial.deleteFromParent();
                updateJobGroups();
            });
            
    		TextField nameField = new TextField();
    		nameField.setMaxHeight(Double.MAX_VALUE);
    		
            DoubleTextField quantityField = new DoubleTextField(requiredMaterial.getQuantityRequired());
            quantityField.setMinWidth(50);
            quantityField.setPrefWidth(50);
            
            Label typeLabel = new Label();
            typeLabel.setMinWidth(30);
            typeLabel.setPrefWidth(30);
            
            StockedMaterial stockedMaterial = requiredMaterial.getStockedMaterial();
            
            if (stockedMaterial != null) {
                nameField.setText(stockedMaterial.getName());
                typeLabel.setText(stockedMaterial.getQuantityType().toString());
            }
            
            grid.addRow(lastRow, deleteButton, nameField, quantityField, typeLabel);
            lastRow++;
            return nameField;
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
                    
                    /* Initialize */
                    lastRow = 0;
                    grid.getChildren().clear();
                    this.job = job;
                    
                    /* Add material row entries for all required materials for this job */
                    for (RequiredMaterial requiredMaterial : job.getChildren()) {
                    	addMaterialRow(requiredMaterial);
                    }
                    
                    addButton(); //Comment this, and uncomment all other lines, to add show-on-hover functionality to the New Material button
                    setGraphic(grid);
                }
            }
        }
    }
    
    //Class which handles the delete cells of tree items
    private class DeleteModelButtonTreeCellFactory extends TreeTableCell<Job, Job> {
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            if (empty) {
                setText("");
                setGraphic(null);
            } else {
                Button button = new Button("X");
                button.setOnAction(e -> { 
                    //If this is a job group sell
                    if (job.isDummy()) {
                        JobGroup jobGroup = job.getJobGroupDummy();
                        String title = Resources.getResource("general.confirm");
                        String info = Resources.getResource("quotes.confirmJobGroupDelete", jobGroup.getGroupName());
                        SceneUtil.showConfirmationDialog(title, info, () -> { 
                            jobGroup.delete();
                            jobGroup.deleteFromParent();
                            updateJobGroups();
                        });
                    } else {
                        job.delete();
                        job.deleteFromParent();
                        updateJobGroups();
                    }                    
                });
                setGraphic(button); //setGraphic allows me to set an actual node instead of text for these cell contents
            }
        }
    }
    
    /* Initialization */
    
    @FXML
	public void initialize() {        
        deleteJobCol.setCellFactory(param -> new DeleteModelButtonTreeCellFactory());
        deleteJobCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	
        titleCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        titleCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
        
        descriptionCol.setCellFactory(param -> new TextAreaTreeTableCell<Job>());
        descriptionCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
        
    	materialsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	materialsCol.setCellFactory(column -> new MaterialsCell());
    	
    	materialsCostCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new DoubleStringConverter()));
    	labourCostCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(new DoubleStringConverter()));
    	
        table.setRowFactory(row -> new JobTableRow());
        
        //Called whenever the the user picks a project using the ComboBox
        projectPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                projectDescription.textProperty().unbindBidirectional(oldValue.projectDescriptionProperty());
                projectNote.textProperty().unbindBidirectional(oldValue.projectNoteProperty());
                firstName.textProperty().unbindBidirectional(oldValue.clientFirstNameProperty());
                lastName.textProperty().unbindBidirectional(oldValue.clientLastNameProperty());
                email.textProperty().unbindBidirectional(oldValue.emailProperty());
                contactNumber.textProperty().unbindBidirectional(oldValue.contactNumberProperty());
                firstLineAddress.textProperty().unbindBidirectional(oldValue.firstLineAddressProperty());
                secondLineAddress.textProperty().unbindBidirectional(oldValue.secondLineAddressProperty());
                city.textProperty().unbindBidirectional(oldValue.cityProperty());
                postcode.textProperty().unbindBidirectional(oldValue.postcodeProperty());
            }
            
            if (newValue != null) {
                projectDescription.textProperty().bindBidirectional(newValue.projectDescriptionProperty());
                projectNote.textProperty().bindBidirectional(newValue.projectNoteProperty());
                firstName.textProperty().bindBidirectional(newValue.clientFirstNameProperty());
                lastName.textProperty().bindBidirectional(newValue.clientLastNameProperty());
                email.textProperty().bindBidirectional(newValue.emailProperty());
                contactNumber.textProperty().bindBidirectional(newValue.contactNumberProperty());
                firstLineAddress.textProperty().bindBidirectional(newValue.firstLineAddressProperty());
                secondLineAddress.textProperty().bindBidirectional(newValue.secondLineAddressProperty());
                city.textProperty().bindBidirectional(newValue.cityProperty());
                postcode.textProperty().bindBidirectional(newValue.postcodeProperty());            
                
                updateJobGroups();
                jobGroupCombo.getSelectionModel().selectFirst();
                
                if (newValue.isNewModel()) deleteProjectButton.setVisible(false);
                else deleteProjectButton.setVisible(true);
            }
        });
        
        newGroupField.setOnAction(e -> newGroupButton.fire());
        
        projects = Project.loadAllProjects();
        addNewProjectOption();
        updateAll();
        projectPicker.getSelectionModel().selectLast();
	}
    
    /* Action methods */ 
    
    @FXML
    public void settings(ActionEvent e) {
        
    }

    @FXML
    public void viewMap(ActionEvent e) {
        GoogleMaps.openMap(StringUtil.formatAddress(firstLineAddress.getText(), secondLineAddress.getText(), city.getText(), postcode.getText()));
    }
    
    @FXML
    public void export(ActionEvent e) {
        
    }
    
    @FXML
    public void addGroup(ActionEvent e) {   
        Project project = getSelectedProject();
        
        //Make a new job group and add it to the project
        JobGroup newGroup = new JobGroup();
        newGroup.setNewModel(true);
        newGroup.projectIdProperty().bind(project.idProperty());
        newGroup.setParent(project);
        newGroup.setGroupName(newGroupField.getText());
        
        project.getChildren().add(newGroup);
        updateJobGroups();
    }
    
    @FXML
    public void addJob(ActionEvent e) {   
    	JobGroup selectedGroup = jobGroupCombo.getSelectionModel().getSelectedItem();
    	
    	if (selectedGroup != null) {
        	//Make a new job and add it to the job group
        	Job newJob = new Job();
            newJob.setNewModel(true);
            newJob.jobGroupIdProperty().bind(selectedGroup.idProperty());
            newJob.setParent(selectedGroup);
            newJob.setTitle(Resources.getResource("jobs.title"));
            newJob.setDescription(Resources.getResource("jobs.description"));
            
            selectedGroup.getChildren().add(newJob);
            updateJobGroups();
    	}
    }
    
    @FXML
    public void saveProject(ActionEvent e) {    	        
    	if (validate()) {   
    	    Project project = getSelectedProject();
    	    
    	    //If this project is new, ensure that another New Project option is added to the project picker
    	    if (project.isNewModel()) {
    	        addNewProjectOption();
    	    }
    	    
            project.save();
            updateAll();
    	}
    }
    
    @FXML
    public void deleteProject(ActionEvent e) {
        Project project = getSelectedProject();
        
        /* Create a confirmation dialog which will delete the project and remove it from the list */
        String title = Resources.getResource("general.confirm");
        String info = Resources.getResource("quotes.confirmProjectDelete", project.getFirstLineAddress());
        SceneUtil.showConfirmationDialog(title, info, () -> { 
            project.delete();
            projects.remove(project);
            updateAll();
        });
    }
    
    /* Non-FXML methods */
    
    public void updateAll() {        
        updateProjectPicker();
        updateJobGroups();
    }
    
    public void updateJobGroups() {
        Project project = getSelectedProject();
        
        if (project != null) {
            table.getRoot().getChildren().clear();
            
            //Go through each job group in the project
            for (JobGroup group : project.getChildren()) {
                
                /* Create a dummy Job object, which will be used in a TreeItem to represent this group. This is necessary
                 * as JavaFX does not support more than one data type in a TreeTableView */
                
                Job jobDummy = new Job();
                jobDummy.setDummy(true);
                jobDummy.setJobGroupDummy(group);
                jobDummy.setTitle(group.getGroupName());
                
                //Create the actual TreeItem
                JobTreeItem groupRoot = new JobTreeItem(jobDummy);
                groupRoot.setExpanded(true);
                
                //Go through each job inside the ACTUAL job group object and add them to the above tree item
                for (Job job : group.getChildren()) {
                    groupRoot.getChildren().add(new JobTreeItem(job));
                }
                
                table.getRoot().getChildren().add(groupRoot);
            }
            
            /* Update the job group combo box, retaining the previously selected item */
            SelectionModel<JobGroup> selectionModel = jobGroupCombo.getSelectionModel();
            JobGroup previousSelection = selectionModel.getSelectedItem();
            jobGroupCombo.getItems().clear();
            jobGroupCombo.getItems().addAll(project.getChildren());
            if (jobGroupCombo.getItems().contains(previousSelection)) selectionModel.select(previousSelection);
        }
    }
    
    //Update the project combo box, retaining the previously selected item
    public void updateProjectPicker() {
        int index = projectPicker.getSelectionModel().getSelectedIndex();
        projectPicker.getItems().clear();
        projectPicker.getItems().addAll(projects);
        projectPicker.getSelectionModel().select(index);
    }
    
    //Add a New Project option to the project picker
    private void addNewProjectOption() {
        Project newProject = new Project();
        newProject.setNewModel(true);
        newProject.setFirstLineAddress(Resources.getResource("quotes.newProject"));
        projects.add(newProject);
    }
    
    //Convenience method
    public Project getSelectedProject() {
        return projectPicker.getSelectionModel().getSelectedItem();
    }
    
    /* Overriden validation methods */
    
    public boolean validate() {
        /* Email */
        if (email.getText().length() > 0 && !Validation.checkEmailFormat(email.getText())) addErrorFromResource("validation.invalidEmail");

        return displayErrors();
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
