package com.github.norbo11.topbuilders.controllers.tabs;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.Main;
import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.custom.ValidationInfo;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.EmployeeSetting;
import com.github.norbo11.topbuilders.models.enums.EmployeeSettingType;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.Settings;
import com.github.norbo11.topbuilders.util.Validation;
import com.github.norbo11.topbuilders.util.helpers.EmailUtil;
import com.github.norbo11.topbuilders.util.helpers.HashUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;
import com.github.norbo11.topbuilders.util.helpers.StringUtil;
import com.github.norbo11.topbuilders.util.helpers.TabUtil;

public class SettingsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/SettingsTab.fxml";
    
    @FXML private ComboBox<Locale> languagesCombo;
    @FXML private CheckBox fullscreenCheckbox;
    @FXML private ValidationInfo passwordValidation, emailValidation;
    @FXML private PasswordField currentPassword, newPassword1, newPassword2;
    @FXML private TextField newEmail1, newEmail2;
    @FXML private DoubleTextField assignmentCloseToEndDays;
    @FXML private Label currentEmail;
    
    private Settings<EmployeeSetting> settings;
    
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
		settings = Employee.getCurrentEmployee().loadSettings();
		
        languagesCombo.getSelectionModel().select(settings.getLocale(EmployeeSettingType.LOCALE));
        fullscreenCheckbox.setSelected(settings.getBoolean(EmployeeSettingType.FULLSCREEN));
        assignmentCloseToEndDays.setDouble(settings.getDouble(EmployeeSettingType.ASSIGNMENT_CLOSE_TO_END_DAYS));
        
        //Set current e-mail
        currentEmail.setText(Employee.getCurrentEmployee().getEmail());
	}
	
	@FXML
	public void saveSettings(ActionEvent event) {
		settings.set(EmployeeSettingType.LOCALE, languagesCombo.getSelectionModel().getSelectedItem().getLanguage());
		settings.set(EmployeeSettingType.FULLSCREEN, fullscreenCheckbox.isSelected());
		settings.set(EmployeeSettingType.ASSIGNMENT_CLOSE_TO_END_DAYS, assignmentCloseToEndDays.getDouble());
		settings.save();
		
		TabUtil.closeCurrentTab();
		Main.getMainStage().setFullScreen(fullscreenCheckbox.isSelected());
	}
	
	@FXML
	public void changePassword() {
	    /* Check current password */
        if (!Employee.getCurrentEmployee().checkPassword(currentPassword.getText())) {
            passwordValidation.addErrorFromResource("settings.password.invalid");
        }
        
        /* Check new password format */
        if (!Validation.checkPasswordFormat(newPassword1.getText())) {
            passwordValidation.addErrorFromResource("validation.invalidPasswordFormat");
        }
        
        /* Check new password match */
        if (!newPassword1.getText().equals(newPassword2.getText())) {
            passwordValidation.addErrorFromResource("validation.passwordsMustMatch");
        }
        
        //If no errors occurred
        if (passwordValidation.validate()) {
            Employee employee = Employee.getCurrentEmployee();
            employee.setPassword(HashUtil.generateMD5Hash(newPassword1.getText()));
            employee.save();
            
            SceneUtil.showInfoDialog(Resources.getResource("settings.password.new"), Resources.getResource("settings.password.changed"));
        }
	}
	
	@FXML
	public void changeEmail() {
		/* Check new email format */
		if (!Validation.checkEmailFormat(newEmail1.getText())) {
        	emailValidation.addErrorFromResource("validation.invalidEmailFormat");
        }
		
		/* Check new email match */
		if (!newEmail1.getText().equals(newEmail2.getText())) {
			emailValidation.addErrorFromResource("validation.emailMustMatch");
		}
		
		/* Check if email exists */
		if (Employee.checkEmailExists(newEmail1.getText())) {
            emailValidation.addErrorFromResource("validation.emailExists");
		}
		
		//If no errors occurred
		if (emailValidation.validate()) {
		    //Generate a random verification code
		    String generatedCode = StringUtil.generateRandomString(10);
		   
		    //Obtain required string resources (which are used more than once and hence stored in a variable)
            String stageTitle = Resources.getResource("settings.confirmEmail.title");
            String stageInfo = Resources.getResource("settings.confirmEmail.info");
		    
		    //Send this code to the user's e-mail address
		    Employee employee = Employee.getCurrentEmployee();
		    EmailUtil.sendEmail(newEmail1.getText(), stageTitle, Resources.getResource("settings.confirmEmail.emailText", generatedCode));
		    
		    //Create a stage with an info label, text field and a confirm button
			Stage stage = StageUtil.createDialogStage(stageTitle);
			
			VBox vbox = new VBox(5);
			vbox.setAlignment(Pos.CENTER);
			vbox.setPadding(new Insets(30));
			
			Label label = new Label(stageInfo);
			TextField confirmField = new TextField();
			Button confirmButton = new Button(Resources.getResource("settings.confirmEmail.confirm"));
			
			confirmButton.setOnAction(e -> {
			    if (confirmField.getText().equals(generatedCode)) {
		            //Upon clicking the confirm button, if the inputted code equals the generated code, change the email, close the dialog and open a confirmation dialog
			        employee.setEmail(newEmail1.getText());
			        employee.save();
			        
			        currentEmail.setText(newEmail1.getText());
			        newEmail1.clear();
			        newEmail2.clear();
			        
    			    SceneUtil.closeScene(confirmButton);
    			    SceneUtil.showInfoDialog(stageTitle, Resources.getResource("settings.confirmEmail.confirmed"));
			    } else {
			        //Otherwise, change the label to an error message and do nothing else, allowing the user to try again
			        label.setText(Resources.getResource("settings.confirmEmail.invalidCode"));
			    }
			});
			
			vbox.getChildren().addAll(label, confirmField, confirmButton);
	        SceneUtil.changeScene(stage, vbox);
		}
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		TabUtil.closeCurrentTab();
	}
}
