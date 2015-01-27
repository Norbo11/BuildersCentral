package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Vector;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.DisplayMessageScene;
import com.github.norbo11.topbuilders.controllers.tabs.MessagesTab;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.StageHelper;

public class Message extends AbstractModel {
    public static final String DB_TABLE_NAME = "messages";

    private IntegerProperty senderId = new SimpleIntegerProperty(0);
    private IntegerProperty recipientId = new SimpleIntegerProperty(0);
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty content = new SimpleStringProperty("");
    private ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<LocalDateTime>();
  
    /* Getters and setters */
    
    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }
    
    public String getSenderName() {
        return Employee.getNameFromId(senderId.get());
    }

    public int getRecipient() {
        return recipientId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getContent() {
        return content.get();
    }

    public LocalDateTime getDate() {
        return date.get();
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

	public void setDate(LocalDateTime date) {
		this.date.set(date);
	}

	/* Instance methods */
	
    @Override
    public void delete() {
        super.delete();
        MessagesTab.updateAllTabs();
    }
	
    @Override
    public void add() {
        // TODO Auto-generated method stub
        
    }
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromResult(ResultSet result) throws SQLException {
        setId(result.getInt("id"));
        setSenderId(result.getInt("senderId"));
        setRecipientId(result.getInt("recipientId"));
        setTitle(result.getString("title"));
        setContent(result.getString("content"));
        setDate(DateTimeUtil.getDateTimeFromTimestamp(result.getString("timestamp")));
	}

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	/* Static methods */

    public static int addMessage(int fromId, int toId, String title, String content, long timestamp) {
        try {
            ResultSet result = Database.executeUpdate("INSERT INTO " + Message.DB_TABLE_NAME + " (senderId, recipientId, title, content, timestamp) VALUES (?,?,?,?,?)", 
            fromId, toId, title, content, timestamp);
            return result.getInt(1);
        } catch (SQLException e) {
            Log.error(e);
        }
        return 0;
    }

    public static void displayMessage(Message message) {
        //Create new window
        Stage stage = StageHelper.createDialogStage(message.getTitle());
        AbstractScene scene = SceneHelper.changeScene(stage, DisplayMessageScene.FXML_FILENAME);
        
        //Display details
        DisplayMessageScene controller = (DisplayMessageScene) scene.getController();
        controller.setMessage(message);
        controller.update();
    }
    
    public static Vector<Message> loadList(ResultSet result) {
        Vector<Message> messages = new Vector<Message>();
        try {
            while (result.next()) {
                Message message = new Message();
                message.loadFromResult(result);
                messages.add(message);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        return messages;
    }

    public static Vector<Message> loadMessagesForEmployee(Employee employee) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "recipientId", employee.getId(), "timestamp", true));
    }
}
