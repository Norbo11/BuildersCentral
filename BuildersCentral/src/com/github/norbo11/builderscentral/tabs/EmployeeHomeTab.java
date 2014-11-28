package com.github.norbo11.builderscentral.tabs;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import com.github.norbo11.builderscentral.Main;
import com.github.norbo11.builderscentral.scenes.LoginScene;
import com.github.norbo11.builderscentral.util.TabHelper;

public class EmployeeHomeTab implements Initializable {
    public final static String FXML_FILENAME = "EmployeeHomeTab.fxml";
    
	@FXML
	private Button quotesAndInvoicesButton;
	
	@FXML 
	private void openNewTab(ActionEvent event) {		
		if (event.getSource() == quotesAndInvoicesButton) TabHelper.createAndSwitchTab(QuotesTab.FXML_FILENAME);
	}
	
	@FXML 
	private void logOut(ActionEvent event) {
		Main.changeMainScene(LoginScene.FXML_FILENAME);
	}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
	
}
