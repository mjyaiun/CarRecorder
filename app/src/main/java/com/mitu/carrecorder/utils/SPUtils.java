package com.mitu.carrecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences类型的存储工具类
 * @author
 */
public class SPUtils {
	/**SharedPreferences名称*/
	private final static String NAME = "ud_win";
	public static final String OBD_FRAME_COUNTER = "frame_counter";

	/**
	 * 存储数据
	 * @param context 上下文
	 * @param name 存储名称
	 * @param value 存储内容
	 */
	public static void saveString(Context context,String name,String value){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		sp.edit().putString(name, value).commit();
	}

	/**
	 * 读取名称为name的存储数值
	 * @param context 上下文对象
	 * @param name 存储名称
	 * @return 存储内容，如果没有则返回""
	 */
	public static String readString(Context context,String name){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return sp.getString(name, "");
	}



	/**
	 * 存储数据
	 * @param context 上下文
	 * @param name 存储名称
	 * @param value 存储内容
	 */
	public static void saveInt(Context context,String name,int value){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(name, value).commit();
	}

	/**
	 * 读取名称为name的存储数值
	 * @param context 上下文对象
	 * @param name 存储名称
	 * @return 存储内容，如果没有则返回""
	 */
	public static int readInt(Context context,String name){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return sp.getInt(name, 1);
	}




	public static void saveBoolean(Context con,String name,boolean value){
		SharedPreferences sp = con.getSharedPreferences(NAME,Context.MODE_PRIVATE);
		sp.edit().putBoolean(name,value).commit();
	}

	public static boolean readBoolean(Context context,String name){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(name,false);
	}



	/**
	 * 清除存储的所有数据
	 * @param context 上下文对象
	 */
	public static void clearAll(Context context){
		SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	/**
	 * 判断activity是否引导过
	 *
	 * @param context
	 * @return  是否已经引导过 true引导过了 false未引导
	 */
	public static boolean activityIsGuided(Context context,String name,String className){
		if(context==null || className==null||"".equalsIgnoreCase(className))return false;
		String[] classNames = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
				.getString(name, "").split("\\|");//取得所有类名 如 com.my.MainActivity
		for (String string : classNames) {
			if(className.equalsIgnoreCase(string)){
				return true;
			}
		}
		return false;
	}

	/**设置该activity被引导过了。 将类名已  |a|b|c这种形式保存为value，因为偏好中只能保存键值对
	 * @param context
	 * @param className
	 */
	public static void setIsGuided(Context context,String name,String className){
		if(context==null || className==null||"".equalsIgnoreCase(className))return;
		String classNames = context.getSharedPreferences(NAME, Context.MODE_WORLD_READABLE)
				.getString(name, "");
		StringBuilder sb = new StringBuilder(classNames).append("|").append(className);//添加值
		context.getSharedPreferences(NAME, Context.MODE_WORLD_READABLE)//保存修改后的值
				.edit()
				.putString(name, sb.toString())
				.commit();
	}




}