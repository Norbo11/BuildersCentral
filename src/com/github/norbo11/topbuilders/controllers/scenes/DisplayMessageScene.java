package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.tabs.MessagesTab;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;


public class DisplayMessageScene implements AbstractController {
    public static final String FXML_FILENAME = "scenes/DisplayMessageScene.fxml";
    
    @FXML private Text title, date, from;
    @FXML private WebView content;
    private Message message;
    private MessagesTab messagesTab;
    
    /* FXML Methods */
    
    @FXML
    public void reply(ActionEvent event) {
    	/* Create a new message dialog and set the recipient and title based on the message being displayed */
        NewMessageScene controller = MessagesTab.newMessage();
        controller.setRecipientByFullName(from.getText());
        controller.getTitleField().setText("Re: " + title.getText());
    }

    @FXML
    public void delete(ActionEvent event) {
    	/* Delete the message, delete the messages tab if any, close the dialog */
    	
        message.delete();
        
        if (messagesTab != null) {
        	messagesTab.updateAll();
        }
        
        close(event);
    }
    
    @FXML
    public void close(ActionEvent event) {
        SceneUtil.closeScene((Node) event.getSource());
    }
    
    /* Instance methods */
    
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Text getTitle() {
        return title;
    }

    public Text getDate() {
        return date;
    }

    public Text getFrom() {
        return from;
    }

    public WebView getContent() {
        return content;
    }

	public void setMessagesTab(MessagesTab messagesTab) {
		this.messagesTab = messagesTab;
	}

	public void updateAll() {
    	/* Update labels */
        title.setText(message.getTitle());
        date.setText(DateTimeUtil.formatDateAndTime(message.getDate()));
        from.setText(message.getSender().getFullName());
        
        /* Load HTML content */
        WebEngine webEngine = content.getEngine();
        webEngine.loadContent(message.getContent());
    }
}
