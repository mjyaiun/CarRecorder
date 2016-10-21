package com.mitu.carrecorder.test;

/**
 * 说明：
 * 2016/7/6 0006
 */
public class TestData {

    public static String getOBDCarState(){
        String response = "f00f555555550001fff0000c610501010201040005000701";
        return response;
    }

    public static String getModifyResult(){
        String response = "f00f555555550001fff0000c6e0501010201040005000701";
        return response;
    }

    public static String getHealthCheckResult(){
        String response = "f00f555555550001fff1000e5804000110010111020112030113";
        return response;
    }

}
