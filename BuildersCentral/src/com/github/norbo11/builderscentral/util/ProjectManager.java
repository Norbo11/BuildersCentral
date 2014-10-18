package com.github.norbo11.builderscentral.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.github.norbo11.builderscentral.models.Project;

public class ProjectManager {
    public static ObservableList<Project> fetchProjects() {    
        ObservableList<Project> projects = FXCollections.observableArrayList();
        ResultSet rs = Database.executeQuery("SELECT * FROM " + Project.DB_TABLE_NAME);
        
        try {
            while (rs.next()) {
                HashMap<String, String> projectDetails = new HashMap<String, String>();
                
                for (int col = 3; col <= rs.getMetaData().getColumnCount(); col++) {
                    projectDetails.put(rs.getMetaData().getColumnName(col), rs.getString(col));
                }
                
                Project project = new Project(rs.getInt("id"), rs.getBoolean("isQuoteRequested"), projectDetails);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

}
