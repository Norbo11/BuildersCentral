package com.github.norbo11.topbuilders.controllers;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.models.User;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.TabHelper;

public class SettingsTab extends AbstractController {
    public final static String FXML_FILENAME = "SettingsTab.fxml";
    
    @FXML ComboBox<String> languagesCombo;
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		File folder = null;
		try {
			folder = new File(ClassLoader.getSystemResource(Constants.LANGUAGES_DIRECTORY).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Log.error(folder.toString());
		for (File file : folder.listFiles()) {
			//Extract language code XX from lang_XX.properties file name
			String langCode = file.getName().substring(5, file.getName().length() - 11);
			languagesCombo.getItems().add(langCode);
			
			//Set the combo to the user's language code
			languagesCombo.getSelectionModel().select(User.getCurrentUser().getSettings().getLocale().getCountry());
		}
	}
	
	@FXML public void saveSettings(ActionEvent event) {
		//Save language
		String langCode = languagesCombo.getSelectionModel().getSelectedItem();
		Locale locale = Locale.forLanguageTag(langCode);
		User.getCurrentUser().getSettings().setLocale(locale);
		
		TabHelper.closeCurrentTab();
		TabHelper.refreshAllTabs();
	}
	
	@FXML public void cancel(ActionEvent event) {
		TabHelper.closeCurrentTab();
	}
}
