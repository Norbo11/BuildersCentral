package com.github.norbo11.builderscentral.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_FILEPATH = "TopBuilders.db";
    private static Connection connection = null;
    
    public static ResultSet executeUpdate(String query) {
        try
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            return statement.executeQuery(query);
        } catch (SQLException e)
        {
            Log.error(e);
        }
        return null;
    }
    
    public static ResultSet executeQuery(String query) {
        try
        {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e)
        {
            Log.error(e);
        }
        return null;
    }
    
    public static void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch(SQLException e)
        {
            Log.error(e);
        }
    }
    
    public static void connect() {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILEPATH);
        } catch (ClassNotFoundException | SQLException e)
        {
            Log.error(e);
        }
    }
    
    public static void createTables() {
        executeQuery("CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY, username TEXT, password TEXT)");
        
    }
}
