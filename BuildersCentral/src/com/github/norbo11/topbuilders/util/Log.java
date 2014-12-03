package com.github.norbo11.topbuilders.util;

public class Log {

    public static void error(Exception e)
    {
        e.printStackTrace();
    }

    public static void error(String string) {
        System.out.println(string);
    }

}
