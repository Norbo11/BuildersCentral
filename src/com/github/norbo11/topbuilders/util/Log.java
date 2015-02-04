package com.github.norbo11.topbuilders.util;


public class Log {

    public static void error(Exception e)
    {
        error("", e);
    }

    public static void error(String string) {
        SceneHelper.showInfoDialog("Error!", string);
    }
    
    public static void error(String string, Exception e) {
    	e.printStackTrace();
    	
        for (StackTraceElement element : e.getStackTrace()) {
            string += element.toString() + "\n";
        }
        
        error(string);
    }

    public static void info(Object object) {
        System.out.println(object + "");
    }
    
	public static void info(String string) {
		System.out.println(string);
	}

}
