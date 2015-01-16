package com.github.norbo11.topbuilders.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_HOSTNAME = "norbz.co.uk";
    private static final String DB_PORT = "443"; //3306 is default
    private static final String DB_NAME = "top_builders";
    private static final String DB_USER = "top_builders";
    private static final String DB_PASSWORD = "computing";
    private static Connection connection = null;
    
    private static ResultSet execute(String query, boolean update, Object... objects) {
        try
        {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            if (statement.getParameterMetaData().getParameterCount() == objects.length) {
                //Replace every '?' in the query with the corresponding object from the objects array
                for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
                    statement.setObject(i, objects[i - 1]);
                }
                
                System.out.println("Executing SQL: " + query);
                if (update) {
                    statement.executeUpdate();
                    ResultSet result = statement.getGeneratedKeys();
                    result.next();
                    return result;
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
    public static ResultSet executeUpdate(String query, Object... objects) {
        return execute(query, true, objects);
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
    	//TODO Create connection with SQLite database for use during the development stage - later explain (in design document)
    	//that in production an online-hosted MySQL database would be used
        try {
        	//Uncomment to enable MySQL
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + DB_HOSTNAME + ":" + DB_PORT 	+ "/" + DB_NAME, DB_USER, DB_PASSWORD);

        	//Class.forName("org.sqlite.JDBC");
        	//connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME + ".db");
        } catch (Exception e) {
            Log.error(e);
        }
    }
    
    public static void createTables() {
        //TODO Insert code here once table structure is created -  generate with PHPMyAdmin
    }
}
