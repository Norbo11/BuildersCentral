package com.github.norbo11.builderscentral.tabs;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;

import com.github.norbo11.builderscentral.Main;
import com.github.norbo11.builderscentral.scenes.LoginScene;
import com.github.norbo11.builderscentral.util.TabHelper;

public class EmployeeHomeTab extends Tab {
	public EmployeeHomeTab() {
		setText("Home");
        setClosable(false);
    }
	
	@FXML 
	private void openNewTab(ActionEvent event) {
		Button source = (Button) event.getSource();
		
		switch (source.getId()) {
			case "quotesAndInvoicesButton": TabHelper.newTab(getTabPane(), new QuotesTab());
		}
	}
	
	@FXML 
	private void logOut(ActionEvent event) {
		Main.changeMainScene(new LoginScene());
	}
}
