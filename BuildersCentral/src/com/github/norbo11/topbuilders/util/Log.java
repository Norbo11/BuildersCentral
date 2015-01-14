package com.github.norbo11.topbuilders.util;

public class Log {

    public static void error(Exception e)
    {
        e.printStackTrace();
    }

    public static void error(String string) {
        System.out.println(string);
    }
    
    public static void error(String string, Exception e) {
        error(string);
        e.printStackTrace();
    }

    public static void info(Object object) {
        System.out.println(object + "");
    }
    
	public static void info(String string) {
		System.out.println(string);
	}

}
