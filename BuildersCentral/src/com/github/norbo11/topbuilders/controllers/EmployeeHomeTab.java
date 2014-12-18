package com.github.norbo11.topbuilders.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

public class EmployeeHomeTab implements Initializable {
    public final static String FXML_FILENAME = "EmployeeHomeTab.fxml";
    
	@FXML
	private Button quotesAndInvoicesButton;
	
	@FXML 
	private void openNewTab(ActionEvent event) {
		switch (((Button) event.getSource()).getText()) {
		case "Quotes and invoices": TabHelper.createAndSwitchTab("Quotes and invioces", QuotesTab.FXML_FILENAME); break;
		case "Settings": TabHelper.createAndSwitchTab("Settings", SettingsTab.FXML_FILENAME); break;
		}
	}
	
	@FXML 
	private void logOut(ActionEvent event) {
		SceneHelper.changeMainScene(LoginScene.FXML_FILENAME);
	}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
	
}
