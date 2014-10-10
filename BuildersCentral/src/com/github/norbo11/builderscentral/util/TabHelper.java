package com.github.norbo11.builderscentral.util;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabHelper {
	public static void newTab(TabPane pane, Tab tab) {
        pane.getTabs().add(tab);
        switchTab(pane, tab);
    }
    
    public static void switchTab(TabPane pane, Tab tab) {
        pane.getSelectionModel().select(tab);
    }
}
