package com.github.norbo11.topbuilders.util;

import com.github.norbo11.topbuilders.util.helpers.SceneUtil;


public class Log {

    public static void error(String string) {
        SceneUtil.showInfoDialog("Error!", string);
    }
    
    public static void error(String string, Exception e) {
    	e.printStackTrace();
        
        error(string);
    }

    public static void info(Object object) {
        System.out.println(object + "");
    }
    
	public static void info(String string) {
		System.out.println(string);
	}

}
