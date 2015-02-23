package com.github.norbo11.topbuilders.util.helpers;

import java.util.ArrayList;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.AbstractTab;
import com.github.norbo11.topbuilders.controllers.tabs.ManagerHomeTab;

public class TabUtil {
    private static TabPane tabPane;
    private static ArrayList<AbstractController> tabs = new ArrayList<AbstractController>();
    private static ManagerHomeTab mainTab;

	public static AbstractTab createAndSwitchTab(String tabName, String fxmlFilename) {
		//Load the tab, set its name, add it to the tab pane and switch to it
        AbstractTab tab = loadTab(fxmlFilename);
        AbstractController controller = tab.getController();
        if (controller instanceof ManagerHomeTab) mainTab = (ManagerHomeTab) controller;
        
        tab.setText(tabName);
        tabPane.getTabs().add(tab);
        tabs.add(controller);
        
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
    
    public static void removeTab(AbstractController tab) {
        tabs.remove(tab);
    }

    public static void updateMainTab() {
        mainTab.updateAll();
    }
}
