package com.github.norbo11.topbuilders.util;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.controllers.AbstractTab;

public class TabHelper {
    private static TabPane tabPane;

	public static AbstractTab createAndSwitchTab(String tabName, String fxmlFilename) {
        AbstractTab tab = new AbstractTab(tabPane, fxmlFilename);
        tab.setText(tabName);
        
        tabPane.getTabs().add(tab);
        switchTab(tab);
        return tab;
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
		for (Tab tab : tabPane.getTabs()) {
	    	AbstractTab abstractTab = (AbstractTab) tab;	    	
	    	abstractTab.loadFromFxml();
		}
	}
}
