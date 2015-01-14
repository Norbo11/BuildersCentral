package com.github.norbo11.topbuilders.controllers;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.EmployeeSettings;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

public class SettingsTab extends AbstractController {
    public final static String FXML_FILENAME = "SettingsTab.fxml";
    
    @FXML ComboBox<Locale> languagesCombo;
    @FXML CheckBox fullscreenCheckbox;
    
    //Custom ListCell class which used to override the default display method of cells in the languages combo box
    private class LocaleCell extends ListCell<Locale> {
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else setText(item.getDisplayLanguage());
        }
    }
    
    @FXML
	public void initialize() {		
		//Use the custom ListCell class declared earlier to change default display behaviour
		languagesCombo.setCellFactory(p -> new LocaleCell()); //This changes the cells as displayed upon clicking the combo box (using a Lambda expression)
		languagesCombo.setButtonCell(new LocaleCell()); //This changes the cell seen after selecting an item
		
		File folder = null;
		try {
			folder = new File(ClassLoader.getSystemResource(Constants.LANGUAGES_DIRECTORY).toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		//Extract languages from the languages folder
		for (File file : folder.listFiles()) {
		    String filename = file.getName();
			String langCode = filename.substring(5, file.getName().length() - 11); //Extract language code XX from lang_XX.properties file name
			Locale locale = Locale.forLanguageTag(langCode);
			if (locale != null) 
			{ 
			    languagesCombo.getItems().add(locale);
			} else Log.error("Invalid language file in /lang/ directory: " + filename);
		}
		
		//Adjust components to reflect current user settings
		EmployeeSettings settings = Employee.getCurrentEmployee().getSettings();
        languagesCombo.getSelectionModel().select(settings.getLocale());
        fullscreenCheckbox.setSelected(settings.isFullscreen());
	}
	
	@FXML public void saveSettings(ActionEvent event) {
	    EmployeeSettings settings = Employee.getCurrentEmployee().getSettings();
		settings.setLocale(languagesCombo.getSelectionModel().getSelectedItem());
		settings.setFullscreen(fullscreenCheckbox.isSelected());
		
		TabHelper.closeCurrentTab();
		TabHelper.refreshAllTabs();
	    SceneHelper.changeMainScene(MainScene.FXML_FILENAME, fullscreenCheckbox.isSelected());

	}
	
	@FXML public void cancel(ActionEvent event) {
		TabHelper.closeCurrentTab();
	}
}
