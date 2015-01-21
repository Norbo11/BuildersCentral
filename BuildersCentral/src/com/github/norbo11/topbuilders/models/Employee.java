package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.controllers.scenes.MainScene;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class Employee extends AbstractModel {
    public static final String DB_TABLE_NAME = "employees";
    
    private static Employee currentEmployee;
    
    private StringProperty username;
    private StringProperty password;
    private StringProperty firstName;
    private StringProperty lastName;
    private ObjectProperty<UserType> userType;
    private ObjectProperty<EmployeeSettings> settings;
	
	public Employee(int id, String username, String password, String firstName, String lastName, UserType userType) {
	    super(id);
	    
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.userType = new SimpleObjectProperty<UserType>(userType);
        this.settings = new SimpleObjectProperty<EmployeeSettings>(EmployeeSettings.getSettingsFromEmployeeId(getId()));
    }

    public EmployeeSettings getSettings() {
        return settings.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public UserType getUserType() {
        return userType.get();
    }
    
    public static void setCurrentEmployee(Employee user) {
        Employee.currentEmployee = user;
    }
    
    public static Employee getCurrentEmployee() {
        return Employee.currentEmployee;
    }
    
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public static Employee login(String inputUser, String inputPassword) throws UsernameException, PasswordException {
        try {
            ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME  + " WHERE username = ?", inputUser);

            if (result.next())
            {
                Employee employee = getEmployeeFromResult(result);
                
                if (employee.getPassword().equals(inputPassword)) {
                    Employee.setCurrentEmployee(employee);
                    return employee; 
                } else throw new PasswordException();
            } else throw new UsernameException();
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

	public static void loginTestAccount() {
	    Employee user = null;
		try {
		    user = login("test", "abc123");
	        SceneHelper.changeMainScene(MainScene.FXML_FILENAME, user.getSettings().isFullscreen());
		} catch (UsernameException | PasswordException e) {
			e.printStackTrace();
		}
	}
	
	public static Vector<Employee> getAllEmployees() {
        Vector<Employee> employees = new Vector<Employee>();

        try {
            ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME);
            
            while (result.next())
            {
                employees.add(getEmployeeFromResult(result));
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return employees;
	}
	
	private static Employee getEmployeeFromResult(ResultSet result) throws SQLException {
	    int id = result.getInt("id");
        String username = result.getString("username");
        String password = result.getString("password");
        String firstName = result.getString("firstName");
        String lastName = result.getString("lastName");
        UserType userType = UserType.getUserType(result.getInt("userType"));
        return new Employee(id, username, password, firstName, lastName, userType);
	}

    public static Employee getEmployeeFromId(int id) {
        ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME + " WHERE id = ?", id);
        
        try {
            if (result.next()) {
                return getEmployeeFromResult(result);
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

    public static String getNameFromId(int id) {
        ResultSet result = Database.executeQuery("SELECT firstname, lastname FROM " + DB_TABLE_NAME + " WHERE id = ?", id);
        try {
            if (result.next()) return result.getString("firstName") + " " + result.getString("lastName");
        } catch (SQLException e) {
            Log.error(e);
        }
        return null;
    }
}
