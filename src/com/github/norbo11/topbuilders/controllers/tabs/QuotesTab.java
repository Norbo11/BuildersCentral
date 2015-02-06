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
    
    @FXML private TextArea projectDescription, projectNote;
    @FXML private TextField firstName, lastName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode;
    
    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    @FXML private TreeTableView<Job> table;
    @FXML private TreeTableColumn<Job, Job> materialsCol, deleteJobCol; 
    @FXML private TreeTableColumn<Job, String> titleCol, descriptionCol;
    @FXML private TreeTableColumn<Job, Double> materialsCostCol, labourCostCol, totalCostCol;
            
    /* Factories */
   
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
            
            setDisclosureNode(null);
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
                
                RequiredMaterial newMaterial = new RequiredMaterial();
                newMaterial.setNewModel(true);
                newMaterial.jobIdProperty().bind(job.idProperty());
                job.getChildren().add(newMaterial);
                
                addMaterialRow(newMaterial); 
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

        private void addMaterialRow(RequiredMaterial requiredMaterial) {
            Button deleteButton = new Button("X");
            
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
                    
                    this.job = job;
                    
                    for (RequiredMaterial requiredMaterial : job.getChildren()) {
                    	addMaterialRow(requiredMaterial);
                    }
                    
                    addButton();
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
                    if (job.isDummy()) {
                        JobGroup jobGroup = job.getJobGroupDummy();
                        String title = Resources.getResource("general.confirm");
                        String info = Resources.getResource("quotes.confirmJobGroupDelete", jobGroup.getGroupName());
                        SceneUtil.showConfirmationDialog(title, info, () -> { 
                            jobGroup.delete();
                        });
                    } else {
                        job.delete();
                    }
                    
                    updateJobGroups();
                });
                setGraphic(button); //setGraphic allows me to set an actual node instead of text for these cell contents
            }
        }
    }
    
    /* FXML Methods */
    
    @FXML
	public void initialize() {
		update();
		
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

	}
    
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
        Project project = getCurrentProject();
        
        //Make a new job group and add it to the project
        JobGroup newGroup = new JobGroup();
        newGroup.setNewModel(true);
        newGroup.projectIdProperty().bind(project.idProperty());
        newGroup.setGroupName(newGroupField.getText());
        
        project.getChildren().add(newGroup);
        updateJobGroups();
    }
    
    @FXML
    public void addJob(ActionEvent e) {   
    	JobGroup selectedGroup = jobGroupCombo.getSelectionModel().getSelectedItem();
    	
    	//Make a new job and add it to the job group
    	Job newJob = new Job();
        newJob.setNewModel(true);
        newJob.jobGroupIdProperty().bind(selectedGroup.idProperty());
        newJob.setTitle(Resources.getResource("jobs.title"));
        newJob.setDescription(Resources.getResource("jobs.description"));
        
        selectedGroup.getChildren().add(newJob);
        updateJobGroups();
    }
    
    //Called whenever the the user picks a project using the ComboBox
    @FXML
    public void updateFields(ActionEvent e) {
    	Project project = getCurrentProject();
    	
    	if (project != null) {
        	projectDescription.textProperty().bindBidirectional(project.projectDescriptionProperty());
        	projectNote.textProperty().bindBidirectional(project.projectNoteProperty());
        	firstName.textProperty().bindBidirectional(project.clientFirstNameProperty());
        	lastName.textProperty().bindBidirectional(project.clientLastNameProperty());
        	email.textProperty().bindBidirectional(project.emailProperty());
        	contactNumber.textProperty().bindBidirectional(project.contactNumberProperty());
        	firstLineAddress.textProperty().bindBidirectional(project.firstLineAddressProperty());
        	secondLineAddress.textProperty().bindBidirectional(project.secondLineAddressProperty());
        	city.textProperty().bindBidirectional(project.cityProperty());
        	postcode.textProperty().bindBidirectional(project.postcodeProperty());
        	        	
        	updateJobGroups();
        	jobGroupCombo.getSelectionModel().select(0);
        	
        	if (project.isNewModel()) deleteProjectButton.setVisible(false);
        	else deleteProjectButton.setVisible(true);
    	}
    }
    
    @FXML
    public void saveProject(ActionEvent e) {
    	Project project = getCurrentProject();
    	        
    	if (validate()) {    		
            project.save();
    	}
    }
    
    @FXML
    public void deleteProject(ActionEvent e) {
        getCurrentProject().delete();
        update();
    }
    
    /* Instance methods */
    
    public void updateJobGroups() {
        Project project = getCurrentProject();
        table.getRoot().getChildren().clear();
        
        for (JobGroup group : project.getChildren()) {
            Job jobDummy = new Job();
            jobDummy.setDummy(true);
            jobDummy.setJobGroupDummy(group);
            jobDummy.setTitle(group.getGroupName());
            
            JobTreeItem groupRoot = new JobTreeItem(jobDummy);
            groupRoot.setExpanded(true);
            
            for (Job job : group.getChildren()) {
                groupRoot.getChildren().add(new JobTreeItem(job));
            }
            
            table.getRoot().getChildren().add(groupRoot);
        }
        
        JobGroup selectedGroup = jobGroupCombo.getValue();
        jobGroupCombo.getItems().clear();
        jobGroupCombo.getItems().addAll(project.getChildren());
        jobGroupCombo.getSelectionModel().select(selectedGroup);
    }
    
    public boolean validate() {
        /* Email */
        if (email.getText().length() > 0 && !Validation.checkEmailFormat(email.getText())) addErrorFromResource("validation.invalidEmail");

        return displayErrors();
    }
    
    public Project getCurrentProject() {
		return projectPicker.getSelectionModel().getSelectedItem();
    }
    
    /* Override methods */
    
    public void update() {
        Project.loadProjects();
        Vector<Project> projects = Project.getProjects();
        projectPicker.getItems().clear();
        
        Project newProject = new Project();
        newProject.setNewModel(true);
        newProject.setFirstLineAddress(Resources.getResource("quotes.newProject"));
        projects.add(newProject);
        
        projectPicker.getItems().addAll(projects);
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
