package com.github.norbo11.topbuilders.models;

import com.github.norbo11.topbuilders.util.Database;


public class NewProject extends Project {
    public NewProject() {
        super();
    }

    @Override
    public String toString() {
        return "New project";
    }
    
    @Override
    public void save() {        
        Database.executeUpdate("INSERT INTO " + Project.DB_TABLE_NAME + " "
        + "(isQuoteRequested,clientName,firstLineAddress,secondLineAddress,city,postcode,contactNumber,email,projectDescription) "
        + "VALUES (?,?,?,?,?,?,?,?,?)"
        , isQuoteRequested, clientName, firstLineAddress, secondLineAddress, city, postcode, contactNumber, email, projectDescription);
    }
}
