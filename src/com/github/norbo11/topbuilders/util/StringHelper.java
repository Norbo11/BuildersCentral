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

	public static String formatAddress(String firstLineAddress, String secondLineAddress, String city, String postcode) {
		String address = firstLineAddress;
        if (!secondLineAddress.equals("")) address += "\n" + secondLineAddress;
        if (!city.equals("")) address += "\n" + city;
        if (!postcode.equals("")) address += "\n" + postcode;
        return address;
	}
}
