package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.AbstractTab;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.GoogleMaps;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.StringUtil;
import com.github.norbo11.topbuilders.util.helpers.TabUtil;
public class QuoteRequestsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/QuoteRequestsTab.fxml";
    
    @FXML private Label clientName, email, contactNumber, firstLineAddress, secondLineAddress, city, postcode, projectDescription;
    private int currentSelection = 0;
    
    /* FXML methods */
    
    @FXML
	public void initialize() {			
        select(getInitialProject());        
	}
    
    @FXML
    public void respond() {
    	AbstractTab tab = TabUtil.createAndSwitchTab(Resources.getResource("home.quotes"), QuotesTab.FXML_FILENAME);
    	QuotesTab controller = (QuotesTab) tab.getController();
    	
    	controller.selectProject(null, getSelectedProject());
    }
    
    @FXML
    public void viewMap() {
        GoogleMaps.openMap(StringUtil.formatAddress(firstLineAddress.getText(), secondLineAddress.getText(), city.getText(), postcode.getText()));
    }
    
    @FXML
    public void goRight() {
        select(getNextProject());
    }
    
    @FXML
    public void goLeft() {
        select(getPreviousProject());
    }
    
    /* Instance methods */
    
    public Project getNextProject() {
        ObservableList<Project> projects = Project.getModels();
        
        int previousSelection = currentSelection;
        
        Project current = null;
        do {
            currentSelection++;
            
            if (currentSelection >= projects.size()) currentSelection = 0;
            if (currentSelection == previousSelection) break;
            
            current = projects.get(currentSelection);
        } while (!current.isQuoteRequested());
        
        return current;
    }
    
    public Project getPreviousProject() {
    	ObservableList<Project> projects = Project.getModels();
        
        int previousSelection = currentSelection;
        
        Project current = null;
        do {
            currentSelection--;
            
            if (currentSelection < 0) currentSelection = projects.size() - 1;
            if (currentSelection == previousSelection) break;
            
            current = projects.get(currentSelection);
        } while (!current.isQuoteRequested());
        
        return current;
    }
    
    public Project getInitialProject() {
    	ObservableList<Project> projects = Project.getModels();
        
        for (Project project : projects) {
            if (project.isQuoteRequested()) return project;
        }
        
        return null;
    }
    
    public void select(Project project) {
        if (project != null) {
            clientName.setText(project.getClientFullName());
            email.setText(project.getEmail());
            contactNumber.setText(project.getContactNumber());
            firstLineAddress.setText(project.getFirstLineAddress());
            secondLineAddress.setText(project.getSecondLineAddress());
            city.setText(project.getCity());
            postcode.setText(project.getPostcode());
            projectDescription.setText(project.getProjectDescription());
        }
    }
    
    public Project getSelectedProject() {
    	return Project.getModels().get(currentSelection);
    }
}
