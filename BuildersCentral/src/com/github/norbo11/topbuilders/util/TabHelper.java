package com.github.norbo11.topbuilders.util;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.controllers.AbstractTab;

public class TabHelper {
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
		return new AbstractTab(tabPane, FXMLHelper.loadFxml(fxmlFilename));
	}
	
    public static void switchTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    public static void setTabPane(TabPane tabPane) {
        TabHelper.tabPane = tabPane;
    }
    
    public static void closeCurrentTab() {
    	AbstractTab tab = (AbstractTab) tabPane.getSelectionModel().getSelectedItem();
    	tab.close();
    }

	public static void refreshAllTabs() {
		//Iterate through all tabs and update each one by loading it again
		for (Tab tab : tabPane.getTabs()) {
	    	AbstractTab abstractTab = (AbstractTab) tab;	
	    	abstractTab = loadTab(abstractTab.getFxmlFilename());
		}
	}
}
