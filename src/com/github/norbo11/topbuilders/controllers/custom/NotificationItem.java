package com.github.norbo11.topbuilders.controllers.custom;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.github.norbo11.topbuilders.controllers.AbstractTab;
import com.github.norbo11.topbuilders.controllers.tabs.ManageAssignmentsTab;
import com.github.norbo11.topbuilders.controllers.tabs.MyAssignmentsTab;
import com.github.norbo11.topbuilders.controllers.tabs.QuoteRequestsTab;
import com.github.norbo11.topbuilders.models.AbstractModel;
import com.github.norbo11.topbuilders.models.Assignment;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.FXMLUtil;
import com.github.norbo11.topbuilders.util.helpers.TabUtil;

public class NotificationItem extends TitledPane {
    public static final String FXML_FILENAME = "NotificationItem.fxml";
    
    @FXML private ImageView image;
    @FXML private Label title;
    @FXML private Label content;
    @FXML private Label timestamp;
    @FXML private CheckBox seenCheckbox;

    private Notification notification; 
    private AbstractModel associatedModel;
    
    public NotificationItem(Notification notification) {
        this.notification = notification;
        this.associatedModel = notification.getAssociatedModel();

        FXMLUtil.loadFxml(FXML_FILENAME, this, this);
    }
    
    @FXML
    public void initialize() {  
        
    	if (associatedModel != null) {
	        switch (notification.getType()) {
	            case ASSIGNMENT_CLOSE_TO_END:
	            	Assignment assignment1 = (Assignment) associatedModel;
	            	
	            	long daysLeft = assignment1.calculateDaysLeft();
	            	if (daysLeft >= 0) {
		            	setText(Resources.getResource("notifications.assignmentCloseToEnd", daysLeft));
	            	} else {
	            		setText(Resources.getResource("notifications.assignmentCloseToEnd.late", -daysLeft));
	            	}
	            	
	            	content.setText(
	            			Resources.getResource("quotes.project") + ": " + assignment1.getProject().getFirstLineAddress() + "\n" + 
        					Resources.getResource("jobs.title") + ": " + assignment1.getJob().getTitle()
        			);	                
	            	break;
	            case EMPLOYEE_ASSIGNMENT_COMPLETE:
	            	Assignment assignment2 = (Assignment) associatedModel;
	            	
	            	setText(Resources.getResource("notifications.assignmentCompleted", assignment2.getEmployee()));
	            	content.setText(
	            			Resources.getResource("quotes.project") + ": " + assignment2.getProject().getFirstLineAddress() + "\n" + 
        					Resources.getResource("jobs.title") + ": " + assignment2.getJob().getTitle()
        			);
	                break;
	            case NEW_ASSIGNMENT:
	            	Assignment assignment3 = (Assignment) associatedModel;
	            	
	            	setText(Resources.getResource("notifications.newAssignment"));
	            	content.setText(
	            			Resources.getResource("quotes.project") + ": " + assignment3.getProject().getFirstLineAddress() + "\n" + 
        					Resources.getResource("jobs.title") + ": " + assignment3.getJob().getTitle()
        			);
	                break;
	            case NEW_MESSAGE:
	                Message message = (Message) associatedModel;
	                
	                setText(Resources.getResource("notifications.newMessage"));
	                content.setText(
	                		Resources.getResource("messages.sender") + ": " + message.getSender().getFullName() + "\n" + 
            				Resources.getResource("messages.title") + ": " + message.getTitle()
            		);
	                break;
	            case NEW_QUOTE_REQUEST:
	            	Project project = (Project) associatedModel;
	            	
	            	setText(Resources.getResource("notifications.newQuoteReqeust"));
	                content.setText(
	                		Resources.getResource("quotes.clientFullName") + ": " + project.getClientFullName() + "\n" +
	                		Resources.getResource("general.address") + ": " + project.getFirstLineAddress()
	                );
	                break;
	        }
	        
	        /* Set image */
	        switch (notification.getType()) {
	            case ASSIGNMENT_CLOSE_TO_END: case NEW_ASSIGNMENT: case EMPLOYEE_ASSIGNMENT_COMPLETE:
	                image.setImage(new Image("img/page.png"));
	                break;
	            case NEW_MESSAGE:
	                image.setImage(new Image("img/envelope.png"));
	                break;
	            case NEW_QUOTE_REQUEST:
	                image.setImage(new Image("img/house.png"));
	                break;
	        }
	        
	        //Mark the seen checkbox and collapse the titled pane if appropriate
	        boolean seen = notification.isSeen();
	        seenCheckbox.setSelected(seen);
	        setExpanded(!seen);
	        
	        if (seen) getStyleClass().add("seen");
	        
	        //Set seen checkbox behaviour
	        seenCheckbox.setOnAction(e -> {
	        	boolean newSeen = seenCheckbox.isSelected();
	        	notification.setSeen(newSeen);
	        	notification.save();
	        	
	        	setExpanded(!newSeen);
	        	if (newSeen) getStyleClass().add("seen");
	        	else getStyleClass().remove("seen");
	        });
	        
	        timestamp.setText(DateTimeUtil.formatDate(notification.getDate()) + "\n" + DateTimeUtil.formatTime(notification.getDate()));
    	}
    }
    
    @FXML
    public void view(MouseEvent event) {
    	if (associatedModel != null) {
	        switch (notification.getType()) {
	            case ASSIGNMENT_CLOSE_TO_END: case NEW_ASSIGNMENT:
	            	//Simply open the My Assignments tab
	            	TabUtil.createAndSwitchTab(Resources.getResource("home.myAssignments"), MyAssignmentsTab.FXML_FILENAME);
	            	break;
	                
	            case NEW_MESSAGE:
	            	//Display the message in a new window
	                Message.displayMessage((Message) associatedModel);
	                break;
	                
	            /* Manager specific */
	                
	            case EMPLOYEE_ASSIGNMENT_COMPLETE:
	            	//Simply open the Manage Assignments tab
	            	TabUtil.createAndSwitchTab(Resources.getResource("home.manageAssignments"), ManageAssignmentsTab.FXML_FILENAME);
	                break;
	                
	            case NEW_QUOTE_REQUEST:
	            	//Open the Quote Requests tab and select the associated quote
	            	AbstractTab tab = TabUtil.createAndSwitchTab(Resources.getResource("home.requests"), QuoteRequestsTab.FXML_FILENAME);
	                QuoteRequestsTab controller = (QuoteRequestsTab) tab.getController();
	                controller.select((Project) associatedModel);
	            	break;
	        }
	        
	        //Mark as read, for all types of notification
	        notification.setSeen(true);
	        notification.save();
    	}
    }
    
}
