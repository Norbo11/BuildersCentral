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
    
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static ResultSet execute(String query, boolean update, Object... objects) {
        try
        {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            String debug = Resources.replaceStringWithParameters(query, "?", objects);
            
            if (statement.getParameterMetaData().getParameterCount() == objects.length) {
                //Replace every '?' in the query with the corresponding object from the objects array
                for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
                    statement.setObject(i, objects[i - 1]);
                }
                
                System.out.println("Executing SQL: " + debug);
                
                //If this is an update statement, get the ID of the inserted row (if one was inserted) and return it
                //If this isn't an update, just execture the statement, returning the requested data
                if (update) {
                    statement.executeUpdate();
                    ResultSet result = statement.getGeneratedKeys();
                    return result;
                } else {
                    return statement.executeQuery();
                }
            } else Log.error("Query error: inconsistent objects array. SQL: " + debug);
        } catch (SQLException e)
        {
            Log.error("Database exception on SELECT", e);
        }
        return null;
    }
    
    //Executes a query, returning the requested result (used for SELECT, etc)
    public static ResultSet executeQuery(String query, Object... objects) {
        return execute(query, false, objects);
    }
    
    //Executes a query without returning any data (used for UPDATE, INSERT, CREATE, etc). When used to INSERT, will return ID of the row inserted
    public static int executeUpdate(String query, Object... objects) {
        try {
            ResultSet result = execute(query, true, objects);
            if (result.next()) {
                int insertedId = result.getInt(1);
                
                Log.info("ID of inserted row: #" + insertedId);
                return insertedId;
            }
        } catch (SQLException e) {
            Log.error("Database exception on (INSERT/UPDATE)", e);
        }
        return 0;
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            } else Log.error("Database close: not connected");
            
        } catch (SQLException e)
        {
            Log.error("Database close: exception", e);
        }
    }
    
    public static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + DB_HOSTNAME + ":" + DB_PORT 	+ "/" + DB_NAME + "?autoReconnect=true", DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            Log.error("Database connect: exception", e);
        }
    }
}
