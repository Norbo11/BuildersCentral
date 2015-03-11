package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.beans.binding.Bindings;
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
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;

public class NewMessageScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/NewMessageScene.fxml";
    
    @FXML private ComboBox<Employee> toCombo;
    @FXML private TextField titleField;
    @FXML private HTMLEditor contentEditor;
    
    @FXML
    public void sendMessage(ActionEvent event) {
        int fromId = Employee.getCurrentEmployee().getId();
        Employee to = toCombo.getSelectionModel().getSelectedItem();
        String title = titleField.getText();
        String content = contentEditor.getHtmlText().replace("contenteditable=\"true\"", "");
        long timestamp = DateTimeUtil.getCurrentTimestamp();
                
        Message message = new Message();
        message.setRecipientEmployee(to);
        message.setNewModel(true);
        message.setSenderId(fromId);
        message.setRecipientId(to.getId());
        message.setTitle(title);
        message.setContent(content);
        message.setTimestamp(timestamp);
        message.save();        
        
        Notification notification = new Notification();
        notification.setNewModel(true);
        notification.setEmployeeId(to.getId());
        notification.setTypeId(NotificationType.NEW_MESSAGE.getId());
        notification.setAssociatedId(message.getId());
        notification.setTimestamp(timestamp);
        notification.save();
        
        discard(event);
    }
    
    @FXML
    public void discard(ActionEvent event) {
        SceneUtil.closeScene((Node) event.getSource());
    }
    
    @FXML
    public void initialize() {
    	Bindings.bindContent(toCombo.getItems(), Employee.loadEmployees());

        toCombo.getSelectionModel().selectFirst();
    }
}
