package com.github.norbo11.topbuilders.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final String DB_FILEPATH = "TopBuilders.db";
    private static Connection connection = null;
    
    public static ResultSet execute(String query, boolean update, Object... objects) {
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            
            if (statement.getParameterMetaData().getParameterCount() == objects.length) {
                //Replace every '?' in the query with the corresponding object from the objects array
                for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
                    statement.setObject(i, objects[i - 1]);
                }
                
                System.out.println("Executing SQL: " + query);
                if (update) {
                    statement.executeUpdate();
                    return null;
                } else {
                    return statement.executeQuery();
                }
            } else Log.error("Query error: inconsistent objects array. SQL: " + query);
        } catch (SQLException e)
        {
            Log.error(e);
        }
        return null;
    }
    
    //Executes a query, returning the requested result (used for SELECT, etc)
    public static ResultSet executeQuery(String query, Object... objects) {
        return execute(query, false, objects);
    }
    
    //Executes a query, without returning anything (used for UPDATE, INSERT, CREATE, etc)
    public static void executeUpdate(String query, Object... objects) {
        execute(query, true, objects);
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
        executeUpdate("CREATE TABLE IF NOT EXISTS projects ("
        + "id INTEGER PRIMARY KEY, isQuoteRequested BOOLEAN, clientName TEXT, firstLineAddress TEXT, secondLineAddress TEXT, city TEXT, "
        + "postcode TEXT, contactNumber TEXT, email TEXT, projectDescription TEXT)");
        
        executeUpdate("CREATE TABLE IF NOT EXISTS jobs ("
        + "id INTEGER PRIMARY KEY, projectId INTEGER, jobDescription TEXT, jobCost FLOAT)");
        
        executeUpdate("CREATE TABLE IF NOT EXISTS assignments ("
        + "id INTEGER PRIMARY KEY, employeeId INTEGER, jobId INTEGER, hourlyWage FLOAT, startDate TEXT, endDate TEXT, isCompleted BOOLEAN)");
        
        executeUpdate("CREATE TABLE IF NOT EXISTS employees ("
        + "id INTEGER PRIMARY KEY, username TEXT, password TEXT, email TEXT, fullName TEXT, userType TEXT)");
        
        executeUpdate("CREATE TABLE IF NOT EXISTS job_materials ("
        + "id INTEGER PRIMARY KEY, materialId INTEGER, jobId INTEGER, quantityRequired FLOAT)");
        
        executeUpdate("CREATE TABLE IF NOT EXISTS job_materials ("
        + "id INTEGER PRIMARY KEY, quantity FLOAT, quantityType TEXT)");
    }
}
