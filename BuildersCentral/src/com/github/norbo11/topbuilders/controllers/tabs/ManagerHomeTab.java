package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.util.TabHelper;

public class ManagerHomeTab extends EmployeeHomeTab {
    public final static String FXML_FILENAME = "tabs/ManagerHomeTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private Button quotesAndInvoicesButton;   
    @FXML private VBox notificationsList;
    
    @Override
    @FXML
    public void buttonAction(ActionEvent event) {
        super.buttonAction(event);
        
        switch (((Button) event.getSource()).getId()) {
		case "employees": TabHelper.createAndSwitchTab(resources.getString("home.employees"), EmployeesTab.FXML_FILENAME);
		case "quotes": break;
		case "requests": break;
		case "materials": break;
		case "manageAssignments": break;
		}
    }
}
