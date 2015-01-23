package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty firstName = new SimpleStringProperty("");
    private StringProperty lastName = new SimpleStringProperty("");
    
    private StringProperty firstLineAddress = new SimpleStringProperty("");
    private StringProperty secondLineAddress = new SimpleStringProperty("");
    private StringProperty city = new SimpleStringProperty("");
    private StringProperty postcode = new SimpleStringProperty("");

    private DoubleProperty defaultWage = new SimpleDoubleProperty(0);
    private ObjectProperty<UserType> userType = new SimpleObjectProperty<UserType>();
    private ObjectProperty<EmployeeSettings> settings = new SimpleObjectProperty<EmployeeSettings>();
	
    /* Properties */
    
    public StringProperty usernameProperty() {
        return username;
    }
    
    public StringProperty passwordProperty() {
        return password;
    }
    
    public StringProperty emailProperty() {
        return email;
    }
    
    public StringProperty firstNameProperty() {
        return firstName;
    }
    
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    public StringProperty firstLineAddressProperty() {
        return firstLineAddress;
    }
    
    public StringProperty secondLineAddressProperty() {
        return secondLineAddress;
    }
    
    public StringProperty cityProperty() {
        return city;
    }
    
    public StringProperty postcodeProperty() {
        return postcode;
    }
    
    public DoubleProperty defaultWageProperty() {
        return defaultWage;
    }
    
    public ObjectProperty<UserType> userTypeProperty() {
        return userType;
    }
    
	/* Getters and setters */
    
    public static void setCurrentEmployee(Employee currentEmployee) {
        Employee.currentEmployee = currentEmployee;
    }
    
    public static Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return password.get();
    }

    public String getEmail() {
        return email.get();
    }
    
    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getFirstLineAddress() {
        return firstLineAddress.get();
    }

    public String getSecondLineAddress() {
        return secondLineAddress.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getPostcode() {
        return postcode.get();
    }
    
    public double getDefaultWage() {
        return defaultWage.get();
    }
    
    public UserType getUserType() {
        return userType.get();
    }

    public EmployeeSettings getSettings() {
        return settings.get();
    }

    public void setFirstLineAddress(String firstLineAddress) {
        this.firstLineAddress.set(firstLineAddress);
    }

    public void setSecondLineAddress(String secondLineAddress) {
        this.secondLineAddress.set(secondLineAddress);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setPostcode(String postcode) {
        this.postcode.set(postcode);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
    
    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setUserType(UserType userType) {
        this.userType.set(userType);
    }

    public void setDefaultWage(double defaultWage) {
        this.defaultWage.set(defaultWage);
    }
    
    public void setSettings(EmployeeSettings settings) {
        this.settings.set(settings);
    }
    
    /* Instance methods */
    
    public String getFullName() {
        return getFirstName() + " " + (getLastName() != null ? getLastName() : "");
    }
    
    public String getAddress() {
        String address = getFirstLineAddress();
        if (!getSecondLineAddress().equals("")) address += "\n" + getSecondLineAddress();
        if (!getCity().equals("")) address += "\n" + getCity();
        if (!getPostcode().equals("")) address += "\n" + getPostcode();
        return address;
    }
    
	public void save() {
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET username=?,password=?,email=?,firstName=?,lastName=?,firstLineAddress=?" +
	    "secondLineAddress=?,city=?,postcode=?,defaultWage=,userType=? "
	    + "WHERE id = ?", getId(), getPassword(), getEmail(), getFirstName(), getLastName(), getFirstLineAddress(), getSecondLineAddress(), getCity(), getPostcode(), getDefaultWage(), getUserType());
	}
	
	/* Overrides */
	
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public void loadFromResult(ResultSet result) throws SQLException {	    
	    int id = result.getInt("id");
	    setId(id);
        setUsername(result.getString("username"));
        setPassword(result.getString("password"));
        setFirstName(result.getString("firstName"));
        setLastName(result.getString("lastName"));
        setEmail(result.getString("email"));
        setFirstLineAddress(result.getString("firstLineAddress"));
        setSecondLineAddress(result.getString("secondLineAddress"));
        setCity(result.getString("city"));
        setPostcode(result.getString("postcode"));
        setUserType(UserType.getUserType(result.getInt("userType")));
        setSettings(EmployeeSettings.getSettingsFromEmployeeId(id));
	}
	
	public  Vector<Notification> getNotifications() {
		return Notification.loadNotificationsForEmployee(this);
    }
	
	/* Static methods */
	
    public static Employee login(String inputUser, String inputPassword) throws UsernameException, PasswordException {
        try {
            ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME  + " WHERE username = ?", inputUser);

            if (result.next())
            {
                Employee employee = new Employee();
                employee.loadFromResult(result);
                
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
		return loadList(loadAllModels(DB_TABLE_NAME));
	}

	public static Vector<Employee> loadList(ResultSet result) {
		Vector<Employee> employees = new Vector<Employee>();
        
        try {
			while (result.next()) {
				Employee employee = new Employee();
				employee.loadFromResult(result);
				employees.add(employee);
			}
		} catch (SQLException e) {
			Log.error(e);
		}
        return employees;
	}
	
	//TODO this is not ideal
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
