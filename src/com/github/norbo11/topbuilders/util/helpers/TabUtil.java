package com.github.norbo11.topbuilders.util.helpers;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.controllers.AbstractTab;
import com.github.norbo11.topbuilders.controllers.tabs.EmployeeHomeTab;
import com.github.norbo11.topbuilders.controllers.tabs.ManagerHomeTab;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.Resources;

public class TabUtil {
    private static TabPane tabPane;

	public static AbstractTab createAndSwitchTab(String tabName, String fxmlFilename) {
		//Load the tab, set its name, add it to the tab pane and switch to it
        AbstractTab tab = loadTab(fxmlFilename);
        
        tab.setText(tabName);
        tabPane.getTabs().add(tab);
        
        switchTab(tab);
        return tab;
    }
    
	public static AbstractTab loadTab(String fxmlFilename) {
		return new AbstractTab(tabPane, FXMLUtil.loadFxml(fxmlFilename));
	}
	
    public static void switchTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    public static void setTabPane(TabPane tabPane) {
        TabUtil.tabPane = tabPane;
    }
    
    public static void closeCurrentTab() {
    	AbstractTab tab = (AbstractTab) tabPane.getSelectionModel().getSelectedItem();
    	tab.close();
    }

	public static void createHomeTab() {
		//Get type of scene (manager or normal) depending on the type of the user
		String scene = Employee.getCurrentEmployee().getUserType().isAtLeast(UserType.MANAGER) ? ManagerHomeTab.FXML_FILENAME : EmployeeHomeTab.FXML_FILENAME;

        AbstractTab tab = TabUtil.createAndSwitchTab(Resources.getResource("home"), scene);
		tab.setClosable(false);
		
		tab.selectedProperty().addListener((obs, oldValue, newValue) -> {
			//If home tab is selected, update it
			if (newValue) ((ManagerHomeTab) tab.getController()).updateAll();
		});
	}
}
