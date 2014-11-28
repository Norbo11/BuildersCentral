package com.github.norbo11.builderscentral.util;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabHelper {
	/*public static void newTab(TabPane pane, Tab tab) {
        pane.getTabs().add(tab);
        switchTab(pane, tab);
    }*/
	
    private static TabPane tabPane;
    
	public static Tab createAndSwitchTab(String filename) {
	    //Load the tab from FXML
        Parent root = FXMLHelper.loadFxml(filename);
        
        //Create the tab object, fill it with the loaded FXML and add it to the list of tabs
	    Tab tab = new Tab();
	    tab.setContent(root);
	    tabPane.getTabs().add(tab);
	    
        switchTab(tab);
        return tab;
	}
	
    public static Tab createAndSwitchTab(String tabName, String fxmlFilename) {
        Tab tab = createAndSwitchTab(fxmlFilename);
        tab.setText(tabName);
        return tab;
    }
    
    public static void switchTab(Tab tab) {
        tabPane.getSelectionModel().select(tab);
    }

    public static void setTabPane(TabPane tabPane) {
        TabHelper.tabPane = tabPane;
    }
}
