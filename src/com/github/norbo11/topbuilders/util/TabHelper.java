package com.github.norbo11.topbuilders.util;

import java.util.Vector;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.AbstractTab;

public class TabHelper {
    private static TabPane tabPane;
    private static Vector<AbstractController> tabs = new Vector<AbstractController>();

	public static AbstractTab createAndSwitchTab(String tabName, String fxmlFilename) {
		//Load the tab, set its name, add it to the tab pane and switch to it
        AbstractTab tab = loadTab(fxmlFilename);
        AbstractController controller = tab.getController();
        
        tab.setText(tabName);
        tabPane.getTabs().add(tab);
        tabs.add(controller);
        
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

    public static void updateAllTabs() {
        for (AbstractController tab : tabs) {
            tab.update();
        }
    }
}
