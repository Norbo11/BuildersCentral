package com.github.norbo11.builderscentral.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_FILEPATH = "TopBuilders.db";
    private static Connection connection = null;
    
    public static void executeUpdate(String query) {
        try
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(10);
            statement.executeUpdate(query);
        } catch (SQLException e)
        {
            Log.error(e);
        }
    }
    
    public static ResultSet executeQuery(String query, Object[] objects) {
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
                statement.setObject(i, objects[i - 1]);
            }
            return statement.executeQuery();
        } catch (SQLException e)
        {
            Log.error(e);
        }
        return null;
    }
    
    public static ResultSet executeQuery(String query, Object object) {
        return executeQuery(query, new Object[] { object });
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
        executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY, username TEXT, password TEXT)");
        
    }
}
