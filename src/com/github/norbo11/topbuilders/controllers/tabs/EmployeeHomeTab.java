package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class EmployeeHomeTab extends ManagerHomeTab {
    public final static String FXML_FILENAME = "tabs/EmployeeHomeTab.fxml";
    
    @FXML private VBox notificationsList;
    
    @Override
    public VBox getNotificationsList() {
        return notificationsList;
    }
}
