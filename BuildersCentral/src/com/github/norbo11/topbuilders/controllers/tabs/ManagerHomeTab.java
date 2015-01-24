package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.NotificationItem;
import com.github.norbo11.topbuilders.controllers.scenes.LoginScene;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

public class ManagerHomeTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/ManagerHomeTab.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private Button quotesAndInvoicesButton;   
    @FXML private VBox notificationsList;
    
    @FXML 
    public void buttonAction(ActionEvent event) {
        switch (((Button) event.getSource()).getId()) {
        case "myAssignments": break;
        case "messages": TabHelper.createAndSwitchTab(resources.getString("home.messages"), MessagesTab.FXML_FILENAME); break;
        case "settings": TabHelper.createAndSwitchTab(resources.getString("home.settings"), SettingsTab.FXML_FILENAME); break;
        case "logout": SceneHelper.changeMainScene(LoginScene.FXML_FILENAME);
        case "employees": TabHelper.createAndSwitchTab(resources.getString("home.employees"), EmployeesTab.FXML_FILENAME);
        case "quotes": break;
        case "requests": break;
        case "materials": break;
        case "manageAssignments": break;
        }
    }
    
   @FXML
   public void initialize() {    
        ObservableList<Node> notifications = notificationsList.getChildren();
       
        for (Notification notification : Employee.getCurrentEmployee().getNotifications()) {
            notifications.add(new NotificationItem(notification));
        }
        
        //TODO For some reason this line adds a slight visual glitch around the notifcation heading edges, investigate later
        if (notifications.size() > 0)
            notifications.get(notifications.size() - 1).getStyleClass().add("notification-item-last");
        else {
            Label label = new Label(resources.getString("notifications.empty"));
            notifications.add(label);
        }
    }
}
