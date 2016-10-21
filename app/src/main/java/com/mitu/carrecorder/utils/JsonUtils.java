package com.mitu.carrecorder.utils;

import com.mitu.carrecorder.entiy.Mileage;
import com.mitu.carrecorder.entiy.MileageRecord;
import com.mitu.carrecorder.entiy.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * json解析工具
 * @author Administrator
 *
 */
public class JsonUtils {

	//获取返回码
	public static String getResultCode(String jsonString){
		String  resultCode=null;
		try {
			JSONObject object = new JSONObject(jsonString);
			resultCode=object.getString("resultscode");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return resultCode;		
	}
	
	//获取返回的错误信息
	public static String getMessage(String jsonString){
		String message=null;
		try {
			JSONObject object = new JSONObject(jsonString);
			message=object.getString("errorMessage");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return message;
	}
	
	//返回用户信息
	public static User getUser(String jsonString){
		User user=new User();
		try {
			JSONObject object=new JSONObject(jsonString);
			JSONObject userObject=object.getJSONObject("vipuser");
			user.setDate(userObject.getString("date"));//注册日期
			user.setDevicename(userObject.getString("devicename"));//设备名字
			user.setId(userObject.getInt("id"));
			user.setImage(userObject.getString("image"));
			user.setIsdel(userObject.getInt("isdel"));
			user.setLevelname(userObject.getString("levelname"));
			user.setNickname(userObject.getString("nickname"));
			user.setPasswd(userObject.getString("passwd"));
			user.setSpeed(userObject.getString("speed"));
			user.setTotalmileage(userObject.getString("totalmileage"));
			user.setTotaltime(userObject.getString("totaltime"));
			user.setUsername(userObject.getString("username"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}
    //获取所有里程
	public static List<Mileage> getAllMileage(String jsonString) {
		// TODO Auto-generated method stub
		List<Mileage> mileageList=new ArrayList<Mileage>();
		try{
			JSONObject object=new JSONObject(jsonString);
			JSONArray array=object.getJSONArray("journeylist");
			for (int i = 0; i < array.length(); i++) {
				JSONObject mObject=array.getJSONObject(i);
				Mileage mileage=new Mileage();
				mileage.setDate(mObject.getString("date"));
				mileage.setEnd(mObject.getString("end"));
				mileage.setId(mObject.getInt("id"));
				mileage.setMileage(mObject.getString("mileage"));
				mileage.setNickname(mObject.getString("nickname"));
				mileage.setSpeed(mObject.getString("speed"));
				mileage.setStart(mObject.getString("start"));
				mileage.setTime(mObject.getString("time"));
				mileage.setUid(mObject.getInt("uid"));
				mileage.setUsername(mObject.getString("username"));
				mileageList.add(mileage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mileageList;
	}

	//获取单条行车所有的记录
	public static List<MileageRecord> getMileageRecord(String result) {
		// TODO Auto-generated method stub
		List<MileageRecord> mrList=new ArrayList<MileageRecord>();
		try{
			JSONObject object=new JSONObject(result);
			JSONArray array=object.getJSONArray("list");
			for (int i = 0; i < array.length(); i++) {
				JSONObject mObject=array.getJSONObject(i);
				MileageRecord mileage=new MileageRecord();
				mileage.setDevicename(mObject.getString("devicename"));
				mileage.setHour(mObject.getString("hour"));
				mileage.setId(mObject.getInt("id"));
				mileage.setLat(mObject.getString("lat"));
				mileage.setLng(mObject.getString("lng"));
				mileage.setMileage(mObject.getString("mileage"));
				mileage.setStatus(mObject.getInt("status"));
				mileage.setTime(mObject.getString("time"));
				mileage.setUid(mObject.getInt("uid"));
				mileage.setUsername(mObject.getString("username"));
				mrList.add(mileage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mrList;
	}
	
	
}
