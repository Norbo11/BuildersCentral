package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.DisplayMessageScene;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;

public class Message extends AbstractModel {
    public static final String DB_TABLE_NAME = "messages";
    private static ObservableList<Message> messages = FXCollections.observableArrayList();
    
    private IntegerProperty senderId = new SimpleIntegerProperty(0);
    private IntegerProperty recipientId = new SimpleIntegerProperty(0);
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty content = new SimpleStringProperty("");
    private LongProperty timestamp = new SimpleLongProperty(0);
    private Employee recipientEmployee;
  
    /* Getters and setters */
    
    public String getTitle() {
        return title.get();
    }

    public String getContent() {
        return content.get();
    }

    public long getTimestamp() {
        return timestamp.get();
    }
    
    public int getSenderId() {
		return senderId.get();
	}

	public void setSenderId(int senderId) {
		this.senderId.set(senderId);
	}

	public int getRecipientId() {
		return recipientId.get();
	}

	public void setRecipientId(int recipientId) {
		this.recipientId.set(recipientId);
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public void setContent(String content) {
		this.content.set(content);
	}

	public void setTimestamp(long timestamp) {
		this.timestamp.set(timestamp);
	}

	/* Instance methods */

	public Employee getSender() {
        Employee employee = new Employee();
        employee.loadFromId(getSenderId(), "firstName", "lastName");
        return employee;
    }
    
    public Employee getRecipient() {
        Employee employee = new Employee();
        employee.loadFromId(getRecipientId(), "firstName", "lastName");
        return employee;
    }
	
	public LocalDateTime getDate() {
        return DateTimeUtil.getDateTimeFromTimestamp(getTimestamp());
	}
	
	/* Foreign key methods */
	
	public Employee getRecipientEmployee() {
		return recipientEmployee;
	}
	
	public void setRecipientEmployee(Employee employee) {
		this.recipientEmployee = employee;
	}
	
	/* Override methods */

	@Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + Message.DB_TABLE_NAME + " (senderId, recipientId, title, content, timestamp) VALUES (?,?,?,?,?)", 
        getSenderId(), getRecipientId(), getTitle(), getContent(), getTimestamp());
    }
	
	@Override
	public void update() {
	    Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "senderId=?,recipientId=?,title=?,content=?,timestamp=? "
        + "WHERE id = ?", getSenderId(), getRecipientId(), getTitle(), getContent(), getTimestamp(), getId());        
	}
	
	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "senderId")) setSenderId(result.getInt("senderId"));
        if (containsColumn(columns, "recipientId")) setRecipientId(result.getInt("recipientId"));
        if (containsColumn(columns, "title")) setTitle(result.getString("title"));
        if (containsColumn(columns, "content")) setContent(result.getString("content"));
        if (containsColumn(columns, "timestamp")) setTimestamp(result.getInt("timestamp"));
	}

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	/* Static methods */

    public static void displayMessage(Message message) {
        //Create new window
        Stage stage = StageUtil.createDialogStage(message.getTitle());
        AbstractScene scene = SceneUtil.changeScene(stage, DisplayMessageScene.FXML_FILENAME, true);
        
        //Display details
        DisplayMessageScene controller = (DisplayMessageScene) scene.getController();
        controller.setMessage(message);
        controller.updateAll();
    }
    
    public static ObservableList<Message> loadMessagesForEmployee(Employee employee) {
        return loadList(loadAllModelsWhereOrdered(DB_TABLE_NAME, "recipientId", employee.getId(), "timestamp", true),  Message.class);
    }
    
    public static ObservableList<Message> getModels() {
	    return messages;
	}
    
}
