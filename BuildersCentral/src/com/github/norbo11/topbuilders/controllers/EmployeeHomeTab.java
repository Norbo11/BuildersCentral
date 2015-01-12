package com.github.norbo11.topbuilders.controllers;

import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

public class EmployeeHomeTab extends AbstractController {
    public final static String FXML_FILENAME = "EmployeeHomeTab.fxml";
    
    @FXML private ResourceBundle resources;
	@FXML private Button quotesAndInvoicesButton;	
	@FXML private VBox notificationsList;
	
	@FXML 
	private void buttonAction(ActionEvent event) {
		switch (((Button) event.getSource()).getId()) {
		case "myAssignments": break;
		case "messages": TabHelper.createAndSwitchTab(resources.getString("home.messages"), MessagesTab.FXML_FILENAME); break;
		case "settings": TabHelper.createAndSwitchTab(resources.getString("home.settings"), SettingsTab.FXML_FILENAME); break;
		case "logout": SceneHelper.changeMainScene(LoginScene.FXML_FILENAME, false);
		}
	}
	
   @FXML
   public void initialize() {    
        ObservableList<Node> notifications = notificationsList.getChildren();
       
        for (Notification notification : Notification.getNotificationsFromEmployee(Employee.getCurrentEmployee())) {
            System.out.println("NOTIFICATION!");
            NotificationItem node = new NotificationItem(notification);
            notifications.add(node);
        }
        
        //TODO For some reason this line adds a slight visual glitch around the notifcation heading edges, investigate later
        notifications.get(notifications.size() - 1).getStyleClass().add("notification-item-last");
    }
}
