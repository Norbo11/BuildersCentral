package com.github.norbo11.topbuilders.controllers.tabs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.DisplayMessageScene;
import com.github.norbo11.topbuilders.controllers.scenes.NewMessageScene;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.Message;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.factories.DeleteModelButtonCellFactory;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;

public class MessagesTab implements AbstractController {
    public final static String FXML_FILENAME = "tabs/MessagesTab.fxml";    
    
    @FXML public TableView<Message> table;
    @FXML public TableColumn<Message, LocalDateTime> dateCol, timeCol;
    @FXML public TableColumn<Message, String> senderCol, titleCol;
    @FXML public TableColumn<Message, Message> xCol;
    
    private class DateCell extends TableCell<Message, LocalDateTime> {
        @Override
        protected void updateItem(LocalDateTime item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) setText("");
            else setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }
    
    private class TimeCell extends TableCell<Message, LocalDateTime> {
        @Override
        protected void updateItem(LocalDateTime item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) setText("");
            else setText(item.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }
    
    /* FXML methods */

    @FXML
	public void initialize() {		
    	Bindings.bindContent(table.getItems(), Employee.getCurrentEmployee().loadMessages());
    	
		//Set custom date/time cell display classes
		dateCol.setCellFactory(column -> new DateCell());
		timeCol.setCellFactory(column -> new TimeCell());

		DeleteModelButtonCellFactory.assignCellFactory(xCol, () -> { updateAll(); });
		
	    table.setRowFactory(value -> {
	        TableRow<Message> row = new TableRow<Message>();
	        row.setOnMouseClicked(e -> {
	            if (e.getClickCount() == 2 && !row.isEmpty()) {
	                readMessage(null);
	            }
	        });
	        return row;
	    });
	}

    @FXML
	public void newMessage(ActionEvent event) {
	    newMessage();
	}
    
    @FXML
    public void readMessage(ActionEvent event) {
        displayMessage(table.getSelectionModel().getSelectedItem(), this);
    }
    
    public void updateAll() {
    	table.getItems().clear();
    	table.getItems().addAll(Employee.getCurrentEmployee().loadMessages());
    }
    
    /* Static methods */
    
    public static void displayMessage(Message message, MessagesTab tab) {
        //Create new window
        Stage stage = StageUtil.createDialogStage(message.getTitle());
        AbstractScene scene = SceneUtil.changeScene(stage, DisplayMessageScene.FXML_FILENAME, true);
        
        //Display details
        DisplayMessageScene controller = (DisplayMessageScene) scene.getController();
        controller.setMessage(message);
        controller.setMessagesTab(tab);
        controller.updateAll();
    }
    
    public static NewMessageScene newMessage() {
    	/* Create the new message dialog */
    	Stage stage = StageUtil.createDialogStage(Resources.getResource("messages.new"));
	    AbstractScene scene = SceneUtil.changeScene(stage, NewMessageScene.FXML_FILENAME);
	    
	    /* Return the new message scene controller */
	    NewMessageScene controller = (NewMessageScene) scene.getController();
	    return controller;
    }
}
