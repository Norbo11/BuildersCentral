package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;


public class DisplayMessageScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/DisplayMessageScene.fxml";
    
    @FXML private Text title, date, from;
    @FXML private WebView content;
    private Message message;
    
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

    @FXML
    public void reply(ActionEvent event) {
        
    }
    
    @FXML
    public void forward(ActionEvent event) {
        
    }
    
    @FXML
    public void delete(ActionEvent event) {
        
    }
    
    @FXML
    public void close(ActionEvent event) {
        SceneUtil.closeScene((Node) event.getSource());
    }

    public void update() {
        title.setText(message.getTitle());
        date.setText(DateTimeUtil.formatDateAndTime(message.getDate()));
        from.setText(message.getSender().getFullName());
        WebEngine webEngine = content.getEngine();
        webEngine.loadContent(message.getContent());
    }
}
