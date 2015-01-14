package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.Vector;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.github.norbo11.topbuilders.models.enums.NotificationType;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;

public class Notification extends AbstractModel {
    public static final String DB_TABLE_NAME = "notifications";
    
    private ObjectProperty<Employee> employee;
    private ObjectProperty<NotificationType> type;
    private ObjectProperty<AbstractModel> associatedModel;
    private ObjectProperty<LocalDateTime> date;
    private BooleanProperty seen;

    public Notification(int id, Employee employee, NotificationType type, AbstractModel associatedModel, LocalDateTime date, boolean seen) {
        super(id);
        
        this.employee = new SimpleObjectProperty<Employee>(employee);
        this.type = new SimpleObjectProperty<NotificationType>(type);
        this.associatedModel = new SimpleObjectProperty<AbstractModel>(associatedModel);
        this.date = new SimpleObjectProperty<LocalDateTime>(date);
        this.seen = new SimpleBooleanProperty(seen);
    }
    
    public Employee getEmployee() {
        return employee.get();
    }

    public NotificationType getType() {
        return type.get();
    }

    public AbstractModel getAssociatedModel() {
        return associatedModel.get();
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public boolean isSeen() {
        return seen.get();
    }
    
    public static Vector<Notification> getNotificationsFromEmployee(Employee employee) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE employeeId = ?", employee.getId());
        Vector<Notification> notifications = new Vector<Notification>();
        
        try {
            while (result.next()) {
                notifications.add(getNotificationFromResult(result));
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return notifications;
    }
    
    public static Notification getNotificationFromResult(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        int associatedId = result.getInt("associatedId");
        Employee employee = Employee.getEmployeeFromId(result.getInt("employeeId"));
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(result.getString("timestamp"))), TimeZone.getDefault().toZoneId());
        NotificationType type = NotificationType.getNotificationType(result.getInt("type"));
        boolean read = result.getBoolean("seen");
        
        AbstractModel associatedModel = null;
        switch (type) {
            case ASSIGNMENT_CLOSE_TO_END: case EMPLOYEE_ASSIGNMENT_COMPLETE: case NEW_ASSIGNMENT:
                associatedModel = Assignment.getAssignmentFromId(associatedId);
                break;
            case NEW_MESSAGE:
                associatedModel = Message.getMessageFromId(associatedId);
                break;
            case NEW_QUOTE_REQUEST:
                associatedModel = QuoteRequest.getQuoteRequestFromId(associatedId);
                break;
        }
        
        return new Notification(id, employee, type, associatedModel, date, read);
    }
    
    public static void addNotification(int employeeId, int type, int associatedId, long timestamp, boolean read) {
        Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME + " (employeeId, type, associatedId, timestamp, seen) VALUES (?,?,?,?,?)", 
        employeeId, type, associatedId, timestamp, read);
    }
}
