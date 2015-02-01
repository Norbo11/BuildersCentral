package com.github.norbo11.topbuilders.controllers.custom;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.github.norbo11.topbuilders.models.AbstractModel;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.FXMLHelper;
import com.github.norbo11.topbuilders.util.Resources;

public class NotificationItem extends TitledPane {
    public static final String FXML_FILENAME = "NotificationItem.fxml";
    
    @FXML private ImageView image;
    @FXML private Label title;
    @FXML private Label content;
    @FXML private Label timestamp;

    private Notification notification; 
    private AbstractModel associatedModel;
    
    public NotificationItem(Notification notification) {
        this.notification = notification;
        this.associatedModel = notification.getAssociatedModel();

        FXMLHelper.loadFxml(FXML_FILENAME, this, this);
    }

    @FXML
    public void initialize() {  
        
    	if (associatedModel != null) {
	        switch (notification.getType()) {
	            case ASSIGNMENT_CLOSE_TO_END:
	                break;
	            case EMPLOYEE_ASSIGNMENT_COMPLETE:
	                break;
	            case NEW_ASSIGNMENT:
	                break;
	            case NEW_MESSAGE:
	                Message message = (Message) associatedModel;
	                
	                setText(Resources.getResource("notifications.new_message"));
	                content.setText(Resources.getResource("messages.sender") + ": " + message.getSender().getFullName() + "\nTitle: " + message.getTitle());
	                break;
	            case NEW_QUOTE_REQUEST:
	                break;
	        }
	        
	        timestamp.setText(DateTimeUtil.formatDateAndTime(notification.getDate()));
    	}
    }
    
    @FXML
    public void view(MouseEvent event) {
    	if (associatedModel != null) {
	        switch (notification.getType()) {
	            case ASSIGNMENT_CLOSE_TO_END:
	                break;
	            case EMPLOYEE_ASSIGNMENT_COMPLETE:
	                break;
	            case NEW_ASSIGNMENT:
	                break;
	            case NEW_MESSAGE:
	                Message message = (Message) associatedModel;
	                Message.displayMessage(message);
	                break;
	            case NEW_QUOTE_REQUEST:
	                break;
	        }
    	}
    }
    
}
