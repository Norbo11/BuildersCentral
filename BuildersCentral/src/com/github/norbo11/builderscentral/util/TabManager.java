package com.github.norbo11.builderscentral.util;

import com.github.norbo11.builderscentral.scenes.MainScene;

import javafx.scene.control.Tab;

public class TabManager {
    
    public static void newTab(Tab tab) {
        MainScene.getTabPane().getTabs().add(tab);
        switchTab(tab);
    }
    
    public static void switchTab(Tab tab) {
        MainScene.getTabPane().getSelectionModel().select(tab);
    }
    
}
