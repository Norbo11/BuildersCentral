package com.github.norbo11.topbuilders.controllers;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ManagerHomeTab extends EmployeeHomeTab {
    public final static String FXML_FILENAME = "ManagerHomeTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private Button quotesAndInvoicesButton;   
    @FXML private VBox notificationsList;
    
    @Override
    @FXML
    public void buttonAction(ActionEvent event) {
        super.buttonAction(event);
    }
}
