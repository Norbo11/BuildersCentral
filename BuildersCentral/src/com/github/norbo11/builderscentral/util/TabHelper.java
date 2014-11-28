package com.github.norbo11.builderscentral.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabHelper {
	/*public static void newTab(TabPane pane, Tab tab) {
        pane.getTabs().add(tab);
        switchTab(pane, tab);
    }*/
	
	public static void newTab(TabPane pane, String tabName) {
        Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("EmployeeHomeTab.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        setContent(root);
        
        pane.getTabs().add(tab);
        switchTab(pane, tab);
	}
    
    public static void switchTab(TabPane pane, Tab tab) {
        pane.getSelectionModel().select(tab);
    }
}
