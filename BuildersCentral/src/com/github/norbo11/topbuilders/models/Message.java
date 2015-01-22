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
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class Message extends AbstractModel {
    public static final String DB_TABLE_NAME = "messages";

    private IntegerProperty senderId;
    private IntegerProperty recipientId;
    private StringProperty title;
    private StringProperty content;
    private ObjectProperty<LocalDateTime> date;
    
    public Message(int id, int senderId, int recipientId, String title, String content, LocalDateTime date) {
        super(id);
        
        this.senderId = new SimpleIntegerProperty(senderId);
        this.recipientId = new SimpleIntegerProperty(recipientId);
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.date = new SimpleObjectProperty<LocalDateTime>(date);
    }
    
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

    public static Vector<Message> getMessagesByRecipient(Employee recipient) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + Message.DB_TABLE_NAME + " WHERE recipientId = ? ORDER BY timestamp DESC", recipient.getId());
        Vector<Message> messages = new Vector<Message>();
        
        try {
            while (result.next()) {
                messages.add(getMessageFromResult(result));
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return messages;
    }

    public static void deleteMessage(Message item) {
        Database.executeUpdate("DELETE FROM " + DB_TABLE_NAME + " WHERE id = ?", item.getId());
    }

    private static Message getMessageFromResult(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        int senderId = result.getInt("senderId");
        int recipientId = result.getInt("recipientId");
        String title = result.getString("title");
        String content = result.getString("content");
        LocalDateTime timestamp = DateTimeUtil.getDateTimeFromTimestamp(result.getString("timestamp"));
        
        return new Message(id, senderId, recipientId, title, content, timestamp);
    }
    
    public static Message getMessageFromId(int id) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                return getMessageFromResult(result);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

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
        Stage stage = new Stage();
        stage.setTitle(message.getTitle());
        AbstractScene scene = SceneHelper.changeScene(stage, Employee.getCurrentEmployee().getSettings().isFullscreen(), DisplayMessageScene.FXML_FILENAME);
        
        //Display details
        DisplayMessageScene controller = (DisplayMessageScene) scene.getController();
        controller.setMessage(message);
        controller.update();
    }
}
