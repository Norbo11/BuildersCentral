package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;

public class EmployeesTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/EmployeesTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML ComboBox<Locale> languagesCombo;
    @FXML CheckBox fullscreenCheckbox;
    
    @FXML
	public void initialize() {		

	}
}