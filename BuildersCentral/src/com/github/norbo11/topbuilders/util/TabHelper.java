package com.github.norbo11.topbuilders.util;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabHelper {
    private static TabPane tabPane;
	
    private static Tab loadFromFxml(String filename) {
        //Create the tab object, fill it with the loaded FXML
        Parent root = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/tabs/fxml/" + filename);
        Tab tab = new Tab();
	    tab.setContent(root);
	    return tab;
	}

	public static Tab createAndSwitchTab(String tabName, String fxmlFilename) {
        Tab tab = loadFromFxml(fxmlFilename);
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
	    	ObservableList<Tab> tabList = tabPane.getTabs();
	    	
	    	tabList.set(tabList.indexOf(tab), loadFromFxml(abstractTab.getFxmlFilename()));
		}
	}
}
