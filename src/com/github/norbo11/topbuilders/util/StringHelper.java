package com.github.norbo11.topbuilders.util;

public class StringHelper {
    public static String join(String[] strings, String seperator) {
        String joined = "";
        
        if (strings.length > 0) {
            for (String string : strings) {
                joined += seperator + string;
            }
            joined = joined.substring(seperator.length()); //Cut off the initial seperator
        }
        
        return joined; 
    }
}
