package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.norbo11.topbuilders.controllers.scenes.MainScene;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.TabHelper;

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

    private StringProperty activationCode = new SimpleStringProperty("");
    private DoubleProperty defaultWage = new SimpleDoubleProperty(0);
    private IntegerProperty userTypeId = new SimpleIntegerProperty(0);
    
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
    
    public StringProperty activationCodeProperty() {
        return activationCode;
    }
    
    public IntegerProperty userTypeIdProperty() {
        return userTypeId;
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
    
    public int getUserTypeId() {
        return userTypeId.get();
    }
    
    public String getActivationCode() {
        return activationCode.get();
    }
    
    public double getDefaultWage() {
        return defaultWage.get();
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

    public void setUserTypeId(int userType) {
        this.userTypeId.set(userType);
    }
    
    public void setActivationCode(String activationCode) {
        this.activationCode.set(activationCode);
    }

    public void setDefaultWage(double defaultWage) {
        this.defaultWage.set(defaultWage);
    }
    
    /* Instance methods */
    
    public  Vector<Notification> getNotifications() {
        return Notification.loadNotificationsForEmployee(this);
    }
    
    public  Vector<Message> getMessages() {
        return Message.loadMessagesForEmployee(this);
    }
    
    public EmployeeSettings getSettings() {
        return EmployeeSettings.loadSettingsForEmployee(this);
    }
    
    public UserType getUserType() {
        return UserType.getUserType(getUserTypeId());
    }
    
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
	
	/* Overrides */
	
    @Override
    public void delete() {
        super.delete();
        TabHelper.updateAllTabs();
    }
    
    @Override
    public void add() {
        Database.executeUpdate("INSERT INTO " + DB_TABLE_NAME
        + " (username,password,email,firstName,lastName,firstLineAddress,secondLineAddress,city,postcode,defaultWage,userTypeId,activationCode) "
        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
        , getUsername(), getPassword(), getEmail(), getFirstName(), getLastName(), getFirstLineAddress(), getSecondLineAddress(), getCity(), getPostcode(), getDefaultWage(), getUserTypeId(), getActivationCode());
    }
    
    @Override
    public void save() {                
        Database.executeUpdate("UPDATE " + DB_TABLE_NAME + " SET "
        + "username=?,password=?,email=?,firstName=?,lastName=?,firstLineAddress=?,secondLineAddress=?,city=?,postcode=?,defaultWage=?,userTypeId=?,activationCode=? "
        + "WHERE id = ?", getUsername(), getPassword(), getEmail(), getFirstName(), getLastName(), getFirstLineAddress(), getSecondLineAddress(), getCity(), getPostcode(), getDefaultWage(), getUserTypeId(), getActivationCode(), getId());
    }
    
    @Override
    public void loadFromResult(ResultSet result, String... columns) throws SQLException {   
        if (containsColumn(columns, "id")) setId(result.getInt("id"));
        if (containsColumn(columns, "username")) setUsername(result.getString("username"));
        if (containsColumn(columns, "password")) setPassword(result.getString("password"));
        if (containsColumn(columns, "firstName")) setFirstName(result.getString("firstName"));
        if (containsColumn(columns, "lastName")) setLastName(result.getString("lastName"));
        if (containsColumn(columns, "email")) setEmail(result.getString("email"));
        if (containsColumn(columns, "firstLineAddress")) setFirstLineAddress(result.getString("firstLineAddress"));
        if (containsColumn(columns, "secondLineAddress")) setSecondLineAddress(result.getString("secondLineAddress"));
        if (containsColumn(columns, "city")) setCity(result.getString("city"));
        if (containsColumn(columns, "postcode")) setPostcode(result.getString("postcode"));
        if (containsColumn(columns, "defaultWage")) setDefaultWage(result.getDouble("defaultWage"));
        if (containsColumn(columns, "activationCode")) setActivationCode(result.getString("activationCode"));
        if (containsColumn(columns, "userTypeId")) setUserTypeId(result.getInt("userTypeId"));
    }
    
	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}
	
	@Override
	public String toString() {
	    return getFullName();
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
                    SceneHelper.setFullscreen(employee.getSettings().isFullscreen());
                    Resources.setCurrentBundle(employee);
                    return employee; 
                } else throw new PasswordException();
            } else throw new UsernameException();
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

	public static void loginTestAccount() {
		try {
		    login("test", "abc123");
	        SceneHelper.changeMainScene(MainScene.FXML_FILENAME);
		} catch (UsernameException | PasswordException e) {
			e.printStackTrace();
		}
	}
	
	public static Vector<Employee> getAllEmployees() {
		return loadList(loadAllModels(DB_TABLE_NAME));
	}

	public static boolean checkUsernameExists(String username) {
		try {
			return loadAllModelsWhere(DB_TABLE_NAME, "username", username).next();
		} catch (SQLException e) {
			Log.error(e);
			return true;
		}
	}
	
	public static boolean checkEmailExists(String email) {
		try {
			return loadAllModelsWhere(DB_TABLE_NAME, "email", email).next();
		} catch (SQLException e) {
			Log.error(e);
			return true;
		}
	}
	
	public static Vector<Employee> loadList(ResultSet result) {
		return loadList(result, Employee.class);
	}
}
