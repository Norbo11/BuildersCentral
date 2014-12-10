package com.github.norbo11.topbuilders.tabs;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.models.User;
import com.github.norbo11.topbuilders.util.AbstractController;

public class SettingsTab extends AbstractController {
    public final static String FXML_FILENAME = "SettingsTab.fxml";
    
    @FXML ComboBox<Locale> languagesCombo;
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		
		File folder = new File(Constants.LANGUAGES_DIRECTORY);
		for (File file : folder.listFiles()) {
			//Remove .properties from the file name
			languagesCombo.getItems().add(Locale.forLanguageTag(file.getName().substring(0, file.getName().length() - 10)));
		}
	}
	
	@FXML public void chooseLanguage(ActionEvent event) {
		//Language.load(languagesCombo.getSelectionModel().getSelectedItem() + ".xml");
		User.getCurrentUser().getSettings().setLocale(languagesCombo.getSelectionModel().getSelectedItem());
	}
}
