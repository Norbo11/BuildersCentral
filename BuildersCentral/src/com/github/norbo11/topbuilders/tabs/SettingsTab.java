package com.github.norbo11.topbuilders.tabs;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import com.github.norbo11.topbuilders.Constants;

public class SettingsTab implements Initializable {
    public final static String FXML_FILENAME = "SettingsTab.fxml";
    
    @FXML ComboBox<String> languagesCombo;
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		File folder = new File(Constants.LANGUAGES_DIRECTORY);
		for (File file : folder.listFiles()) {
			languagesCombo.getItems().add(file.getName().substring(0, file.getName().length() - 4));
		}
	}
}
