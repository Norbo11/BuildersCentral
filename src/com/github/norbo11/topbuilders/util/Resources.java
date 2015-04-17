package com.github.norbo11.topbuilders.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;

import org.apache.poi.util.IOUtils;

import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.EmployeeSettingType;

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
        StringBuilder output = new StringBuilder(string);
        String lastChars = "";
        int paramCount = 0;
        
        //Iterate through every character in the resource, storing the last X characters read in the lastChars string (X being the length of the parameter marker)
        for (int charPos = 0; charPos < output.length(); charPos++) {
            char character = output.charAt(charPos);
            lastChars += character;
            
            if (lastChars.length() > marker.length()) lastChars = lastChars.substring(1); //This removes the first character and ensures lastChars represents the last X chars
            
            if (lastChars.equals(marker)) {
                //Start is inclusive, end is exclusive, and such an arrangement replaces the parameter marker with the required parameter
               
            	String param = "";
            	try {
            		//This will throw an exception if the number of params is different from the number of markers
            		param = params[paramCount].toString();
                } catch (ArrayIndexOutOfBoundsException e) {
                	Log.error("ResourceUtil#getResourceWithParameters: expected at least " + paramCount + " parameters, received " + params.length);
                }
                
            	output.replace(charPos - (marker.length() - 1), charPos + 1, param);
            	
            	//Skip on past the replaced string
                charPos += param.length();
                paramCount++;
            }
        }
        
        return output.toString();
    }

    public static void setCurrentBundle(Employee employee) {
        currentBundle = ResourceBundle.getBundle("lang.lang", employee.loadSettings().getLocale(EmployeeSettingType.LOCALE), ClassLoader.getSystemClassLoader());
    }
    
    public static void setResources(FXMLLoader loader) {
        if (currentBundle != null) {
            loader.setResources(currentBundle);
        }
    }

    public static byte[] getAsBytes(String string) {
        FileInputStream stream = null;
        byte[] bytes = null;
        
        try {
            stream = new FileInputStream(new File(string));
            bytes = IOUtils.toByteArray(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return bytes;
    }
}
