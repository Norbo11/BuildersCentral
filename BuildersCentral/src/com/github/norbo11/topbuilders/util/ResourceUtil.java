package com.github.norbo11.topbuilders.util;

import java.util.ResourceBundle;

public class ResourceUtil {
    public static final String PARAMETER_MARKER = "(?)";
    
    public static String getResourceWithParameters(ResourceBundle bundle, String key, String... params) {
        StringBuilder resource = new StringBuilder(bundle.getString(key));
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
}
