package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Vector;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.github.norbo11.topbuilders.models.enums.NotificationType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Log;

public class Notification extends AbstractModel {
    public static final String DB_TABLE_NAME = "notifications";
    
    private IntegerProperty employeeId = new SimpleIntegerProperty(0);
    private IntegerProperty associatedModelId = new SimpleIntegerProperty();
    private ObjectProperty<NotificationType> type = new SimpleObjectProperty<NotificationType>();
    private ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<LocalDateTime>();
    private BooleanProperty seen = new SimpleBooleanProperty(false);
    
    /* Getters and setters */
    
    public Integer getEmployee() {
        return employeeId.get();
    }

    public NotificationType getType() {
        return type.get();
    }

    public int getAssociatedModelId() {
        return associatedModelId.get();
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public boolean isSeen() {
        return seen.get();
    }

	public IntegerProperty getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId.set(employeeId);
	}

	public BooleanProperty getSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen.set(seen);
	}

	public void setType(NotificationType type) {
		this.type.set(type);
	}

	public void setAssociatedModelId(int associatedModelId) {
		this.associatedModelId.set(associatedModelId);
	}

	public void setDate(LocalDateTime date) {
		this.date.set(date);
	}
	
    /* Instance methods */

    @Override
    public void add() {
        // TODO Auto-generated method stub
        
    }
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "associatedId")) setAssociatedModelId(result.getInt("associatedId"));
        if (containsColumn(columns, "employeeId")) setEmployeeId(result.getInt("employeeId"));
        if (containsColumn(columns, "timestamp")) setDate(DateTimeUtil.getDateTimeFromTimestamp(result.getString("timestamp")));
        if (containsColumn(columns, "type")) setType(NotificationType.getNotificationType(result.getInt("type")));
        if (containsColumn(columns, "seen")) setSeen(result.getBoolean("seen"));
	}

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
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
        	associatedModel = new QuoteRequest();
            break;
		}
        if (associatedModel != null) associatedModel.loadFromId(getAssociatedModelId());
        else Log.error("Detected invalid notification type for notification #" + getId());
        return associatedModel;
	}
	
	/* Static methods */
	
	public static Vector<Notification> loadList(ResultSet result) {
		Vector<Notification> notifications = new Vector<Notification>();

		try {
			while (result.next()) {
				Notification notification = new Notification();
				notification.loadFromResult(result);
				notifications.add(notification);
			}
		} catch (SQLException e) {
			Log.error(e);
		}
        return notifications;
	}
    
    public static void addNotification(int employeeId, int type, int associatedId, long timestamp, boolean read) {
        Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME + " (employeeId, type, associatedId, timestamp, seen) VALUES (?,?,?,?,?)", 
        employeeId, type, associatedId, timestamp, read);
    }

	public static Vector<Notification> loadNotificationsForEmployee(Employee employee) {
        return loadList(loadAllModelsWhere(DB_TABLE_NAME, "employeeId", employee.getId()));
	}
}
