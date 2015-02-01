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
  
    public static String getResource(String key, Object... params) {
        return replaceStringWithParameters(currentBundle.getString(key), PARAMETER_MARKER, params);
    }
    
    public static String replaceStringWithParameters(String string, String marker, Object... params) {
        StringBuilder resource = new StringBuilder(string);
        String lastChars = "";
        int paramCount = 0;
        
        //Iterate through every character in the resource, storing the last X characters read in the lastChars string (X being the length of the parameter marker)
        for (int charPos = 0; charPos < resource.length(); charPos++) {
            char character = resource.charAt(charPos);
            lastChars += character;
            
            if (lastChars.length() > marker.length()) lastChars = lastChars.substring(1); //This removes the first character and ensures lastChars represents the last X chars
            if (lastChars.equals(marker)) {
                //Start is inclusive, end is exclusive, and such an arrangement replaces the parameter marker with the required parameter
                resource.replace(charPos - (marker.length() - 1), charPos + 1, params[paramCount].toString());
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
