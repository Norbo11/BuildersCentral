package com.github.norbo11.topbuilders.util;

import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

import com.github.norbo11.topbuilders.models.Employee;

public class Resources {
    public static final String PARAMETER_MARKER = "(?)";
    private static ResourceBundle currentBundle;

    public static String getResource(String key) {
    	return currentBundle.getString(key);
    }
    
    public static String getResource(String key, String... params) {
    	return getResourceWithParameters(key, params);
    }
    
    private static String getResourceWithParameters(String key, String... params) {
        StringBuilder resource = new StringBuilder(currentBundle.getString(key));
        String lastThree = "";
        int paramCount = 0;
        
        //Iterate through every character in the resource, storing the last three characters read in the lastThree string
        for (int charPos = 0; charPos < resource.length(); charPos++) {
            char character = resource.charAt(charPos);
            lastThree += character;
            
            if (lastThree.length() > 3) lastThree = lastThree.substring(1); //This removes the first character and ensures lastThree represents the last 3 chars
            if (lastThree.equals(PARAMETER_MARKER)) {
                //Start is inclusive, end is exclusive, and such an arrangement replaces the parameter marker with the required parameted
                resource.replace(charPos - (PARAMETER_MARKER.length() - 1), charPos + 1, params[paramCount]);
                paramCount++;
            }
        }
        
        //Debug info
        if (paramCount != params.length) Log.error("ResourceUtil#getResourceWithParameters: expected " + paramCount + " parameters, received " + params.length);
        return resource.toString();
    }

    public static void setCurrentBundle(Employee employee) {
        currentBundle = ResourceBundle.getBundle("lang.lang", employee.getSettings().getLocale(), ClassLoader.getSystemClassLoader());
    }
    
    public static void setResources(FXMLLoader loader) {
        if (currentBundle != null) {
            loader.setResources(currentBundle);
        }
    }
}
