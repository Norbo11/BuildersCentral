package com.github.norbo11.topbuilders.controllers;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.github.norbo11.topbuilders.controllers.tabs.MessagesTab;
import com.github.norbo11.topbuilders.models.AbstractModel;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.util.FXMLHelper;
import com.github.norbo11.topbuilders.util.DateTimeUtil;

public class NotificationItem extends TitledPane {
    public static final String FXML_FILENAME = "NotificationItem.fxml";
    
    @FXML private ResourceBundle resources;  
    @FXML private ImageView image;
    @FXML private Label title;
    @FXML private Label content;
    @FXML private Label timestamp;

    private Notification notification; 
    private AbstractModel associatedModel;
    
    public NotificationItem(Notification notification) {
        this.notification = notification;
        this.associatedModel = notification.getAssociatedModel();

        FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + FXML_FILENAME, this, this);
    }

    @FXML
    public void initialize() {              
        switch (notification.getType()) {
            case ASSIGNMENT_CLOSE_TO_END:
                break;
            case EMPLOYEE_ASSIGNMENT_COMPLETE:
                break;
            case NEW_ASSIGNMENT:
                break;
            case NEW_MESSAGE:
                Message message = (Message) associatedModel;
                setText(resources.getString("home.notifications.new_message"));
                content.setText(resources.getString("messages.sender") + ": " + message.getSender() + "\nTitle: " + message.getTitle());
                break;
            case NEW_QUOTE_REQUEST:
                break;
        }
        
        timestamp.setText(DateTimeUtil.formatDate(notification.getDate()) + "\n" + DateTimeUtil.formatTime(notification.getDate()));
    }
    
    @FXML
    public void view(MouseEvent event) {
        switch (notification.getType()) {
            case ASSIGNMENT_CLOSE_TO_END:
                break;
            case EMPLOYEE_ASSIGNMENT_COMPLETE:
                break;
            case NEW_ASSIGNMENT:
                break;
            case NEW_MESSAGE:
                Message message = (Message) associatedModel;
                MessagesTab.displayMessage(message);
                break;
            case NEW_QUOTE_REQUEST:
                break;
        }
        
        timestamp.setText(DateTimeUtil.formatDateAndTime(notification.getDate()));
    }
    
}
