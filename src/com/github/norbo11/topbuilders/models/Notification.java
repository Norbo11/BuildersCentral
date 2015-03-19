package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.topbuilders.models.enums.NotificationType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.helpers.DateTimeUtil;

public class Notification extends AbstractModel {
    public static final String DB_TABLE_NAME = "notifications";
    private static ObservableList<Notification> notifications = FXCollections.observableArrayList();
    
    private IntegerProperty employeeId = new SimpleIntegerProperty(0);
    private IntegerProperty typeId = new SimpleIntegerProperty(0);
    private IntegerProperty associatedId = new SimpleIntegerProperty(0);
    private LongProperty timestamp = new SimpleLongProperty(0);
    private BooleanProperty seen = new SimpleBooleanProperty(false);
    
    /* Getters and setters */
    
    public int getEmployeeId() {
        return employeeId.get();
    }

    public int getTypeId() {
        return typeId.get();
    }

    public int getAssociatedId() {
        return associatedId.get();
    }

    public long getTimestamp() {
        return timestamp.get();
    }

    public boolean isSeen() {
        return seen.get();
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId.set(employeeId);
    }

    public void setTypeId(int typeId) {
        this.typeId.set(typeId);
    }

    public void setAssociatedId(int associatedId) {
        this.associatedId.set(associatedId);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp.set(timestamp);
    }

    public void setSeen(boolean seen) {
        this.seen.set(seen);
    }
	
    /* Instance methods */
    
    public LocalDateTime getDate() {
        return DateTimeUtil.getDateTimeFromTimestamp(getTimestamp());
    }
    
    public NotificationType getType() {
        return NotificationType.getNotificationType(getTypeId());
    }

    public AbstractModel getAssociatedModel() {
        AbstractModel associatedModel = null;
                
        switch (getType()) {
        case ASSIGNMENT_CLOSE_TO_END: case EMPLOYEE_ASSIGNMENT_COMPLETE: case NEW_ASSIGNMENT:
            associatedModel = new Assignment();
            break;
        case NEW_MESSAGE:
            associatedModel = new Message();
            break;
        case NEW_QUOTE_REQUEST:
            associatedModel = new Project();
            break;
        }
        
        if (associatedModel != null) associatedModel.loadFromId(getAssociatedId());
        else Log.error("Detected invalid notification type for notification #" + getId());
        
        return associatedModel;
    }
    
    /* Override methods */
    
    @Override
    public int add() {
        return Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME + " (employeeId, typeId, associatedId, timestamp, seen) VALUES (?,?,?,?,?)", 
        getEmployeeId(), getTypeId(), getAssociatedId(), getTimestamp(), isSeen());
    }

    @Override
	public void update() {
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET employeeId=?, typeId=?, associatedId=?, timestamp=?, seen=? WHERE id=?", 
        getEmployeeId(), getTypeId(), getAssociatedId(), getTimestamp(), isSeen(), getId());
	}

	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "employeeId")) setEmployeeId(result.getInt("employeeId"));
        if (containsColumn(columns, "typeId")) setTypeId(result.getInt("typeId"));
        if (containsColumn(columns, "associatedId")) setAssociatedId(result.getInt("associatedId"));
        if (containsColumn(columns, "timestamp")) setTimestamp(result.getInt("timestamp"));
        if (containsColumn(columns, "seen")) setSeen(result.getBoolean("seen"));
	}
	
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	/* Static methods */

	//Override
	public static ObservableList<Notification> loadList(ResultSet result) {
        return loadList(result, Notification.class);
    }

	public static ObservableList<Notification> loadNotificationsForEmployee(Employee employee) {
        return loadList(loadAllModelsWhereOrdered(DB_TABLE_NAME, "employeeId", employee.getId(), "timestamp", true));
	}
	
	public static ObservableList<Notification> getModels() {
	    return notifications;
	}
	
    public static void deleteCorrespondingNotifications(AbstractModel model) {
        if (model instanceof Message) {
            Database.executeUpdate("DELETE FROM " + DB_TABLE_NAME + " WHERE typeId=? AND associatedId=?", NotificationType.NEW_MESSAGE.getId(), model.getId());
        }
        
        if (model instanceof Assignment) {
            Database.executeUpdate("DELETE FROM " + DB_TABLE_NAME + " WHERE (typeId=? OR typeId=? OR typeId=?) AND associatedId=?", 
            NotificationType.ASSIGNMENT_CLOSE_TO_END.getId(), NotificationType.EMPLOYEE_ASSIGNMENT_COMPLETE.getId(), NotificationType.NEW_ASSIGNMENT.getId(), model.getId());
        }
        
        if (model instanceof Project) {
            Database.executeUpdate("DELETE FROM " + DB_TABLE_NAME + " WHERE typeId=? AND associatedId=?", NotificationType.NEW_QUOTE_REQUEST.getId(), model.getId());
        }
    }

	public static Notification loadAssignmentCloseToEndNotificationForAssignment(Assignment assignment) {
		return loadOne(loadAllModelsWhere(DB_TABLE_NAME, new String[] { "typeId", "associatedId" }, new Object[] { NotificationType.ASSIGNMENT_CLOSE_TO_END.getId(), assignment.getId() }), Notification.class);
	}
    
}
