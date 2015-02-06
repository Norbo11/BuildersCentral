package com.github.norbo11.topbuilders.controllers.tabs;

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
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.TabUtil;

public class ManagerHomeTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/ManagerHomeTab.fxml";
    
    @FXML private VBox notificationsList;
    
    @FXML 
    public void buttonAction(ActionEvent event) {
        switch (((Button) event.getSource()).getId()) {
        case "myAssignments": break;
        case "messages": TabUtil.createAndSwitchTab(Resources.getResource("home.messages"), MessagesTab.FXML_FILENAME); break;
        case "settings": TabUtil.createAndSwitchTab(Resources.getResource("home.settings"), SettingsTab.FXML_FILENAME); break;
        case "logout": SceneUtil.changeMainScene(LoginScene.FXML_FILENAME); break;
        case "employees": TabUtil.createAndSwitchTab(Resources.getResource("home.employees"), EmployeesTab.FXML_FILENAME); break;
        case "quotes": TabUtil.createAndSwitchTab(Resources.getResource("home.quotes"), QuotesTab.FXML_FILENAME); break;
        case "requests": break;
        case "materials": TabUtil.createAndSwitchTab(Resources.getResource("home.materials"), MaterialsTab.FXML_FILENAME); break;
        case "manageAssignments": break;
        }
    }
    
   @FXML
   public void initialize() {    
       update();
   }
   
   public void update() {
       ObservableList<Node> notifications = getNotificationsList().getChildren();
       notifications.clear();
        
       for (Notification notification : Employee.getCurrentEmployee().getNotifications()) {
           notifications.add(new NotificationItem(notification));
       }
         
        if (notifications.size() > 0) {
            notifications.get(notifications.size() - 1).getStyleClass().add("notification-item-last");
            getNotificationsList().getStyleClass().remove("empty");
       } else {
            Label label = new Label(Resources.getResource("notifications.empty"));
            getNotificationsList().getStyleClass().add("empty");
            notifications.add(label);
        }
    }
    
    public VBox getNotificationsList() {
        return notificationsList;
    }
}
