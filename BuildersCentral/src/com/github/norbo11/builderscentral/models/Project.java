package com.github.norbo11.builderscentral.models;

import java.util.HashMap;

import com.github.norbo11.builderscentral.util.Database;

public class Project {
    
    public Project(int id, boolean isQuoteRequested, HashMap<String, String> projectDetails) {
        super();
        this.id = id;
        this.isQuoteRequested = isQuoteRequested;
        clientName = projectDetails.get("clientName");
        firstLineAddress = projectDetails.get("firstLineAddress");
        secondLineAddress = projectDetails.get("secondLineAddress");
        city = projectDetails.get("city");
        postcode = projectDetails.get("postcode");
        contactNumber = projectDetails.get("contactNumber");
        email = projectDetails.get("email");
        projectDescription = projectDetails.get("projectDescription");
    }

    public Project() {
    }

    public static final String DB_TABLE_NAME = "projects";
    private int id;
    protected boolean isQuoteRequested;
    protected String clientName;
    protected String firstLineAddress;
    protected String secondLineAddress;
    protected String city;
    protected String postcode;
    protected String contactNumber;
    protected String email;
    protected String projectDescription;
    
    public boolean isQuoteRequested() {
        return isQuoteRequested;
    }

    public String getClientName() {
        return clientName;
    }

    public String getFirstLineAddress() {
        return firstLineAddress;
    }

    public String getSecondLineAddress() {
        return secondLineAddress;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setQuoteRequested(boolean isQuoteRequested) {
        this.isQuoteRequested = isQuoteRequested;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setFirstLineAddress(String firstLineAddress) {
        this.firstLineAddress = firstLineAddress;
    }

    public void setSecondLineAddress(String secondLineAddress) {
        this.secondLineAddress = secondLineAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @Override
    public String toString() {
        return clientName + " - " + firstLineAddress;
    }

    public void save() {
        Database.executeUpdate("UPDATE " + Project.DB_TABLE_NAME + " SET "
        + "isQuoteRequested = ?"
        + ",clientName = ?"
        + ",firstLineAddress = ?"
        + ",secondLineAddress = ?"
        + ",city = ?"
        + ",postcode = ?"
        + ",contactNumber = ?"
        + ",email = ?"
        + ",projectDescription = ?"
        + " WHERE id = ?"
        , isQuoteRequested, clientName, firstLineAddress, secondLineAddress, city, postcode, contactNumber, email, projectDescription, id);
    }
}
