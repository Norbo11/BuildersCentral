package com.github.norbo11.topbuilders.controllers.tabs;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.ValidationInfo;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.QuoteSettingsScene;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.JobGroup;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.models.QuoteSetting;
import com.github.norbo11.topbuilders.models.StockedMaterial;
import com.github.norbo11.topbuilders.models.enums.QuoteSettingType;
import com.github.norbo11.topbuilders.util.GoogleMaps;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.Settings;
import com.github.norbo11.topbuilders.util.Validation;
import com.github.norbo11.topbuilders.util.factories.DoubleMoneyConverter;
import com.github.norbo11.topbuilders.util.factories.HeadingTreeTableRow;
import com.github.norbo11.topbuilders.util.factories.MaterialsCell;
import com.github.norbo11.topbuilders.util.factories.StringStringConverter;
import com.github.norbo11.topbuilders.util.factories.TextAreaTreeCell;
import com.github.norbo11.topbuilders.util.factories.TextFieldTreeCell;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;
import com.github.norbo11.topbuilders.util.helpers.StringUtil;

public class QuotesTab extends AbstractController {

    public final static String FXML_FILENAME = "tabs/QuotesTab.fxml";
    
    @FXML private GridPane jobsGrid;
    @FXML private ComboBox<Project> projectPicker;
    @FXML private ComboBox<JobGroup> jobGroupCombo;
    @FXML private Button deleteProjectButton, newGroupButton, settingsButton, jobGroupButton;
    @FXML private TextField newGroupField;
    
    @FXML private TextArea projectDescription, projectNote;
    @FXML private TextField firstName, lastName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode;
    
    @FXML private ValidationInfo validation;
    @FXML private TreeTableView<Job> table;
    @FXML private TreeTableColumn<Job, Job> materialsCol, deleteJobCol; 
    @FXML private TreeTableColumn<Job, String> titleCol, descriptionCol;
    @FXML private TreeTableColumn<Job, Number> materialsCostCol, labourCostCol, totalCostCol;
    
    /* Factories, cells and rows */
    
    //Class which removes the expansion arrow of tree items
    private class JobTableRow extends HeadingTreeTableRow<Job> {        
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);
            
            setDisclosureNode(null); //Responsible for the "arrow" node
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
                            jobGroup.getProject().getJobGroups().remove(jobGroup);
                            updateJobGroups();
                        });
                    } else {
                        job.delete();
                        job.getJobGroup().getJobs().remove(job);
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
        Project.loadProjects();
        StockedMaterial.loadStockedMaterials();
        
    	/* Cell factories */
        table.setRowFactory(row -> new JobTableRow());
    	
        deleteJobCol.setCellFactory(param -> new DeleteModelButtonTreeCellFactory());
        titleCol.setCellFactory(param -> new TextFieldTreeCell<Job, String>(new StringStringConverter()));
        descriptionCol.setCellFactory(param -> new TextAreaTreeCell<Job>());
    	materialsCol.setCellFactory(column -> new MaterialsCell(this));
    	materialsCostCol.setCellFactory(param -> new TextFieldTreeCell<Job, Number>(new DoubleMoneyConverter()));
    	labourCostCol.setCellFactory(param -> new TextFieldTreeCell<Job, Number>(new DoubleMoneyConverter()));
    	totalCostCol.setCellFactory(param -> new TextFieldTreeCell<Job, Number>(new DoubleMoneyConverter()));
    	
        /* Cell value factories */
        deleteJobCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	materialsCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<Job>(param.getValue().getValue()));
    	totalCostCol.setCellValueFactory(param -> { 
    		Job job = param.getValue().getValue();
    		return job.labourPriceProperty().add(job.materialPriceProperty());
    	});

        /* Edit handlers */
        titleCol.setOnEditCommit(e -> e.getRowValue().getValue().setTitle(e.getNewValue()));
        descriptionCol.setOnEditCommit(e -> e.getRowValue().getValue().setDescription(e.getNewValue()));
        materialsCostCol.setOnEditCommit(e -> e.getRowValue().getValue().setMaterialPrice(e.getNewValue().doubleValue()));
        labourCostCol.setOnEditCommit(e -> e.getRowValue().getValue().setLabourPrice(e.getNewValue().doubleValue()));
        
        /* Layout properties */
        titleCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
        descriptionCol.minWidthProperty().bind(table.widthProperty().multiply(0.15));
        
        //Called whenever the the user picks a project using the ComboBox
        projectPicker.valueProperty().addListener((observable, oldValue, newValue) -> selectProject(oldValue, newValue));
        
        newGroupField.setOnAction(e -> newGroupButton.fire());
        
        addNewProjectOption();
        updateAll();
        
        projectPicker.getSelectionModel().selectLast();
	}

    /* Action methods */ 
    
    @FXML
    public void settings(ActionEvent e) {
        Stage stage = StageUtil.createDialogStage(Resources.getResource("quotes.settings"));
        AbstractScene scene = SceneUtil.changeScene(stage, QuoteSettingsScene.FXML_FILENAME);
        
        //Display details
        QuoteSettingsScene controller = (QuoteSettingsScene) scene.getController();
        controller.setSettings(getSelectedProject().getSettings());
        controller.setQuotesTab(this);
        controller.updateAll();
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
        newGroup.setProjectId(project.getId());
        newGroup.setGroupName(newGroupField.getText());
        
        project.getJobGroups().add(newGroup);
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
            newJob.setTitle(Resources.getResource("jobs.title"));
            newJob.setDescription(Resources.getResource("jobs.description"));
            
            selectedGroup.getJobs().add(newJob);
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
            Project.getModels().remove(project);
            updateAll();
        });
    }
    
    /* Non-FXML methods */
    
    public void updateAll() {  
        updateProjectPicker();
        updateJobGroups();
        updateButtons();
        updateColumns();
        updateAddGroupControl();
        updateAddJobControl();
    }
    
    public void updateButtons() {
        Project project = getSelectedProject();
        
        if (project != null) {
            if (getSelectedProject().isNewModel()) {
                settingsButton.setDisable(true);
                deleteProjectButton.setDisable(true);
            } else {
                settingsButton.setDisable(false);
                deleteProjectButton.setDisable(false);
            }
        }
    }

    public void updateJobGroups() {
        if (getSelectedProject() != null) {
            getSelectedProject().populateTreeTable(table);
            
            updateAddJobControl(); //This used to be inside that methods ^ at the end of the first condition - if it breaks, remember that
        }
    }
    
    public void updateAddJobControl() {
        Project project = getSelectedProject();
        
        if (project != null) {
            if (project.getSettings().getBoolean(QuoteSettingType.GROUPS_ENABLED)) {
                /* Update the job group combo box, retaining the previously selected item */
                SelectionModel<JobGroup> selectionModel = jobGroupCombo.getSelectionModel();
                JobGroup previousSelection = selectionModel.getSelectedItem();
                jobGroupCombo.getItems().clear();
                jobGroupCombo.getItems().addAll(project.getJobGroups());
                if (jobGroupCombo.getItems().contains(previousSelection)) selectionModel.select(previousSelection);
                
                jobGroupCombo.setDisable(false);
                jobGroupButton.setDisable(false);
            } else {
                jobGroupCombo.setDisable(true);
                jobGroupButton.setDisable(true);
            }
        }
    }

    public void updateAddGroupControl() {
        Project project = getSelectedProject();
        
        if (project != null) {
            if (project.getSettings().getBoolean(QuoteSettingType.GROUPS_ENABLED)) {
                newGroupField.setDisable(false);
                newGroupButton.setDisable(false);
            } else {
                newGroupField.setDisable(true);
                newGroupButton.setDisable(true);
            }
        }
    }
    
    //Update the project combo box, retaining the previously selected item
    public void updateProjectPicker() {
        int index = projectPicker.getSelectionModel().getSelectedIndex();
        projectPicker.getItems().clear();
        projectPicker.getItems().addAll(Project.getModels());
        projectPicker.getSelectionModel().select(index);
    }
    
    public void updateColumns() {
        Project project = getSelectedProject();
        
        if (project != null) {
            Settings<QuoteSetting> settings = project.getSettings();
            
            descriptionCol.setVisible(settings.getBoolean(QuoteSettingType.JOB_DESCRIPTIONS_ENABLED));
            materialsCol.setVisible(settings.getBoolean(QuoteSettingType.MATERIALS_ENABLED));
            materialsCostCol.setVisible(settings.getBoolean(QuoteSettingType.MATERIALS_PRICE_ENABLED));
            labourCostCol.setVisible(settings.getBoolean(QuoteSettingType.LABOUR_PRICE_ENABLED));
        }
    }
    
    //Add a New Project option to the project picker
    public void addNewProjectOption() {
        Project newProject = new Project();
        newProject.setNewModel(true);
        newProject.setFirstLineAddress(Resources.getResource("quotes.newProject"));
        Project.getModels().add(newProject);
    }
    
    public void selectProject(Project oldProject, Project newProject) {
        if (oldProject != null) {
            projectDescription.textProperty().unbindBidirectional(oldProject.projectDescriptionProperty());
            projectNote.textProperty().unbindBidirectional(oldProject.projectNoteProperty());
            firstName.textProperty().unbindBidirectional(oldProject.clientFirstNameProperty());
            lastName.textProperty().unbindBidirectional(oldProject.clientLastNameProperty());
            email.textProperty().unbindBidirectional(oldProject.emailProperty());
            contactNumber.textProperty().unbindBidirectional(oldProject.contactNumberProperty());
            firstLineAddress.textProperty().unbindBidirectional(oldProject.firstLineAddressProperty());
            secondLineAddress.textProperty().unbindBidirectional(oldProject.secondLineAddressProperty());
            city.textProperty().unbindBidirectional(oldProject.cityProperty());
            postcode.textProperty().unbindBidirectional(oldProject.postcodeProperty());
        }
        
        if (newProject != null) {            
            projectDescription.textProperty().bindBidirectional(newProject.projectDescriptionProperty());
            projectNote.textProperty().bindBidirectional(newProject.projectNoteProperty());
            firstName.textProperty().bindBidirectional(newProject.clientFirstNameProperty());
            lastName.textProperty().bindBidirectional(newProject.clientLastNameProperty());
            email.textProperty().bindBidirectional(newProject.emailProperty());
            contactNumber.textProperty().bindBidirectional(newProject.contactNumberProperty());
            firstLineAddress.textProperty().bindBidirectional(newProject.firstLineAddressProperty());
            secondLineAddress.textProperty().bindBidirectional(newProject.secondLineAddressProperty());
            city.textProperty().bindBidirectional(newProject.cityProperty());
            postcode.textProperty().bindBidirectional(newProject.postcodeProperty());            
            
            jobGroupCombo.getSelectionModel().selectFirst();
            updateAll();
        }
    }
    
    //Convenience method
    public Project getSelectedProject() {
        return projectPicker.getSelectionModel().getSelectedItem();
    }
    
    /* Overriden validation methods */
    
    public boolean validate() {
        /* Email */
        if (email.getText().length() > 0 && !Validation.checkEmailFormat(email.getText())) {
        	validation.addErrorFromResource("validation.invalidEmail");
        }

        return validation.displayErrors();
    }
}
