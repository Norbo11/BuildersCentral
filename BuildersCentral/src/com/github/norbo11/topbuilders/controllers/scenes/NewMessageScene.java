package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.models.Notification;
import com.github.norbo11.topbuilders.models.enums.NotificationType;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

public class NewMessageScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/NewMessageScene.fxml";
    
    @FXML private ComboBox<Employee> toCombo;
    @FXML private TextField titleField;
    @FXML private HTMLEditor contentEditor;
    
    @FXML
    public void sendMessage(ActionEvent event) {
        int fromId = Employee.getCurrentEmployee().getId();
        int toId = toCombo.getSelectionModel().getSelectedItem().getId();
        String title = titleField.getText();
        String content = contentEditor.getHtmlText().replace("contenteditable=\"true\"", "");
        long timestamp = DateTimeUtil.getCurrentTimestamp();
                
        int messageId = Message.addMessage(fromId, toId, title, content, timestamp);
        Notification.addNotification(toId, NotificationType.NEW_MESSAGE.ordinal(), messageId, timestamp, false);
        discard(event);
        TabHelper.refreshAllTabs();
    }
    
    @FXML
    public void discard(ActionEvent event) {
        SceneHelper.closeScene((Node) event.getSource());
    }
    
    @FXML
    public void initialize() {
    	for (Employee employee : Employee.loadAll()) {
    		toCombo.getItems().add(employee);
    	}
        
        toCombo.getSelectionModel().select(0);
    }
}
