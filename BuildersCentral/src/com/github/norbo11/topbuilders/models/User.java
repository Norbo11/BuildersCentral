package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.models.exceptions.PasswordException;
import com.github.norbo11.topbuilders.models.exceptions.UsernameException;
import com.github.norbo11.topbuilders.scenes.MainScene;
import com.github.norbo11.topbuilders.util.Database;
import com.github.norbo11.topbuilders.util.Log;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class User {
    public static final String DB_TABLE_NAME = "users";
    public static User currentUser = null;
    private String username;
    private String password;
    private UserType type;
    
    public User(String username, String password, UserType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "[USER] " + username + " - " + password + " - " + type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }
    
    public void save() {
        
    }
    
    public static User login(String inputUser, String inputPassword) throws UsernameException, PasswordException {
        try {
            ResultSet result = Database.executeQuery("SELECT * FROM " + DB_TABLE_NAME  + " WHERE username = ? COLLATE NOCASE", inputUser);

            if (result.next())
            {
                String username = result.getString("username");
                String password = result.getString("password");
                
                if (password.equalsIgnoreCase(inputPassword)) {
                    String type = result.getString("type");
                    User user = new User(username, password, UserType.getUserType(type));
                    User.setCurrentUser(user);
                    return user; 
                } else throw new PasswordException();
            } else throw new UsernameException();
        } catch (SQLException e) {
            Log.error(e);
        }
        
        return null;
    }

    public static void setCurrentUser(User user) {
        User.currentUser = user;
    }
    
    public static User getCurrentUser() {
        return User.currentUser;
    }

	public static void loginTestAccount() {
		try {
			login("test", "abc123");
		} catch (UsernameException | PasswordException e) {
			e.printStackTrace();
		}
        SceneHelper.changeMainScene(MainScene.FXML_FILENAME);
	}
}
