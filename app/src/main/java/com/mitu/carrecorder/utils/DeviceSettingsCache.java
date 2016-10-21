package com.mitu.carrecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mitu.carrecorder.BEApplication;

/**设置
 * 说明：
 * 2016/6/15 0015
 */
public class DeviceSettingsCache {

    private static final String SETTING_INFOS = "app_device_setting_infos";
    private static final String LANGUAGE = "language";
    private static final String VIDEO_MOTION_DETECTION = "video_motion_detection";
    private static final String VIDEO_VOICE = "video_voice";
    private static final String VIDEO_WATER_MARK = "video_water_mark";


    //
    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;

    /***
     * 设置语言
     * @param languageType
     */
    public static void setAppLanguage(int languageType){
        SharedPreferences settings = BEApplication.getAppContext().getSharedPreferences(
                SETTING_INFOS, Context.MODE_PRIVATE);
        settings.edit()
                .putInt(LANGUAGE,languageType).commit();

    }

    public static int getAppLanguageType(){
        SharedPreferences settings = BEApplication.getAppContext().getSharedPreferences(
                SETTING_INFOS, Context.MODE_PRIVATE);
        return settings.getInt(LANGUAGE,CHINESE);
    }


    /**
     *
     * @param command
     * @param state
     */
    public static void setCommandState(String command,int state){
        SharedPreferences settings = BEApplication.getAppContext().getSharedPreferences(
                SETTING_INFOS, Context.MODE_PRIVATE);
        settings.edit()
                .putInt(command,state).commit();
    }


    /**
     *
     * @param command
     * @return
     */
    public static int getCommandState(String command){
        SharedPreferences settings = BEApplication.getAppContext().getSharedPreferences(
                SETTING_INFOS, Context.MODE_PRIVATE);
        return settings.getInt(command,0);
    }






}
