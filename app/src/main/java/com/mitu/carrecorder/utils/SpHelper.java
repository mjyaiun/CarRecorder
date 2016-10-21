package com.mitu.carrecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 共享文件
 * @author Administrator
 *
 */
public class SpHelper {
	
	Context mContext;
	private SharedPreferences mPreferences;
	private String  ID="id";  //id
	private String USERNAME="phone";//手机号
	private String PASSWORD="password";//密码
	private String DEVICEDNAME="devicename";//设备名字
	private String LEVELNAME="levelname";//等级名字
	private String IMAGEURL = "imageurl";//头像网络地址
	private String IMAGELOC="imageloc";//头像本地地址
	private String NICKNAME = "nickname";//昵称
	private String TOTALMILEAGE="totalmileage";//总里程
	private String TOTALTIME="totaltime";//总时间
	private String SPPEED="speed";//平均速度
	private String ISDEL="isdel";//用户状态
	private String ISREMEMBER="isRemember";
	private String INFOTR = "info";
	private String CHANELID="channelid";
	private  String DEVICIID="deviceid";
	private String BLUETOOTH="bluetooth";
	private String DATE="date";
	private String FIRST_USE = "first";
	private String FLAG = "flag";
	private String AUTO="flag";
	private String PHTONUM="photonum";
	private String VIDEONUM="videonum";
	private String ZNC = "znc";
	private String SNC = "snc";
	private String YYC="ync";
	private String RUNTIME="runtime";//运动提醒时间
	private String PHOTOSIZE="photosize";
	private String VIDEOSIZE="vdeosize";
	private String PSAVE="psave";
	private String VSAVE="vsave";

	//

	private String IS_LOGIN = "isLogin";
	
	private static final String WIFINAME = "wifiName";
	
	public SpHelper(Context context) {
		mContext = context;
		mPreferences = mContext.getSharedPreferences("fjkj",
				Context.MODE_PRIVATE);
	}

	public void saveIsLogin(boolean isLogin){
		mPreferences.edit().putBoolean(IS_LOGIN, isLogin).commit();
	}

	public boolean getIsLogin(){
		return mPreferences.getBoolean(IS_LOGIN, false);
	}
	
	public String getPhotosize() {
		return mPreferences.getString(PHOTOSIZE, "");
	}

	public void setPhotosize(String photosize) {
		mPreferences.edit().putString(PHOTOSIZE, photosize).commit();
	}
	public String getVideosize() {
		return mPreferences.getString(VIDEOSIZE, "");
	}

	public void setVideosive(String videosize) {
		mPreferences.edit().putString(VIDEOSIZE, videosize).commit();
	}
	public String getPsave() {
		return mPreferences.getString(PSAVE, "");
	}

	public void setPsave(String psave) {
		mPreferences.edit().putString(PSAVE, psave).commit();
	}
	public String getVsave() {
		return mPreferences.getString(VSAVE, "");
	}

	public void setVsave(String vsave) {
		mPreferences.edit().putString(VSAVE, vsave).commit();
	}
	public String getPHTONUM() {
		return mPreferences.getString(PHTONUM, "");
	}

	public void setPHTONUM(String photonum) {
		mPreferences.edit().putString(PHTONUM, photonum).commit();
	}

	public String getVIDEONUM() {
		return mPreferences.getString(VIDEONUM, "");
	}

	public void setVIDEONUM(String videonum) {
		mPreferences.edit().putString(VIDEONUM, videonum).commit();
	}

	public String getZNC() {
		return mPreferences.getString(ZNC, "");
	}

	public void setZNC(String znc) {
		mPreferences.edit().putString(ZNC, znc).commit();
	}

	public String getSNC() {
		return mPreferences.getString(SNC, "");
	}

	public void setSNC(String snc) {
		mPreferences.edit().putString(SNC, snc).commit();
	}

	public String getYYC() {
		return mPreferences.getString(YYC, "");
	}

	public void setYYC(String yyc) {
		mPreferences.edit().putString(YYC, yyc).commit();
	}

	public String getBluetooth(){
		return mPreferences.getString(BLUETOOTH, "");
	}
	
	public void saveBluetooth(String bluetooth){
		mPreferences.edit().putString(BLUETOOTH, bluetooth).commit();
	}
	public String getdate(){
		return mPreferences.getString(DATE, "");
	}
	
	public void savedate(String date){
		mPreferences.edit().putString(DATE, date).commit();
	}
	public String getDeviceid(){
		return mPreferences.getString(DEVICIID, "");
	}
	
	public void saveDeviceid(String deviceid){
		mPreferences.edit().putString(DEVICIID, deviceid).commit();
	}
	
	
	
	public String getChannelid(){
		return mPreferences.getString(CHANELID, "");
	}
	
	public void saveChannelid(String channelid){
		mPreferences.edit().putString(CHANELID, channelid).commit();
	}
	
	public String getImageLoc(){
		return mPreferences.getString(IMAGELOC, "");
	}
	
	public void saveImageLoc(String imageLoc){
		mPreferences.edit().putString(IMAGELOC, imageLoc).commit();
	}
	
	
	public boolean getIsRemeber(){
		return mPreferences.getBoolean(ISREMEMBER, false);
	}
	
	public void saveIsRemeber(boolean isRemember){
		mPreferences.edit().putBoolean(ISREMEMBER, isRemember).commit();
	}
	
	public int getIsdel(){
		return mPreferences.getInt(ISDEL, 0);
	}
	
	public void saveIsdel(int isdel){
		mPreferences.edit().putInt(ISDEL, isdel);
	}
	public String getSpeed(){
		return mPreferences.getString(SPPEED, "");
	}
	
	public void saveSpeed(String speed){
		mPreferences.edit().putString(SPPEED, speed).commit();
	}
	
	public String getTotalTime(){
		return mPreferences.getString(TOTALTIME, "");
	}
	
	public void saveTotalTime(String totalTime){
		mPreferences.edit().putString(TOTALTIME, totalTime).commit();
	}
	
	public String getTotalMileage(){
		return mPreferences.getString(TOTALMILEAGE, "");
	}
	
	public void saveTotalMileage(String totalMileage){
		mPreferences.edit().putString(TOTALMILEAGE, totalMileage).commit();
	}
	
	public String getLevelName(){
		return mPreferences.getString(LEVELNAME, "");
	}
	
	public void saveLevelName(String levelName){
		mPreferences.edit().putString(LEVELNAME, levelName).commit();
	}
	
	public String getDeviceName(){
		return mPreferences.getString(DEVICEDNAME, "");
	}
	
	public void saveDeviceName(String deciceName){
		mPreferences.edit().putString(DEVICEDNAME, deciceName).commit();
	}
	
	
	public String getPassword(){
		return mPreferences.getString(PASSWORD, "");
	}
	
	public void savePassword(String password){
		mPreferences.edit().putString(PASSWORD, password).commit();
	}
	
	public String getUserName(){
		return mPreferences.getString(USERNAME, "");
	}
	
	public void saveUserName(String username){
		mPreferences.edit().putString(USERNAME, username).commit();
	}
	
	public int getId(){
		return mPreferences.getInt(ID, 0);
	}
	
	public void saveId(int id){
		mPreferences.edit().putInt(ID, id).commit();
	}
	
	/**
	 * 获取wifiName
	 * @return
	 */
	public  String getWIfiName()
	{
		return mPreferences.getString(WIFINAME, "");
	}
	/**
	 * 设置WiFi名称
	 * @param wifiName
	 */
	public  void setWifiName(String wifiName)
	{
		mPreferences.edit().putString(WIFINAME, wifiName).commit();
	}
	
	
	public void saveShareFlag(String flag) {
		mPreferences.edit().putString(FLAG, flag).commit();
	}

	public String getShareFlag() {
		return mPreferences.getString(FLAG, "");
	}
	public void saveUserImage(String image) {
		mPreferences.edit().putString(IMAGEURL, image).commit();
	}

	public String getUserImage() {
		return mPreferences.getString(IMAGEURL, "");
	}
	
	public void saveNickName(String name) {
		mPreferences.edit().putString(NICKNAME, name).commit();
	}

	public String getNickName() {
		return mPreferences.getString(NICKNAME, "");
	}
	public void saveInfo(String info) {
		mPreferences.edit().putString(INFOTR, info).commit();
	}

	public String getInfo(){
		return mPreferences.getString(INFOTR, "");
	}
	public boolean isFirst() {
		return mPreferences.getBoolean(FIRST_USE, true);
	}

	public void setFirst(boolean flag) {
		mPreferences.edit().putBoolean(FIRST_USE, flag).commit();
	}
    public boolean isAutologin(){
	return mPreferences.getBoolean(AUTO, false);}
	public void saveAutologin(boolean flag) {
		mPreferences.edit().putBoolean(AUTO, flag).commit();
		
	}

	public String getRuntime() {
		return mPreferences.getString(RUNTIME, "");
	}

	public void saveRuntime(String runtime) {
		mPreferences.edit().putString(RUNTIME, runtime).commit();
	}

	@Override
	public String toString() {
		return "SpHelper [mContext=" + mContext + ", mPreferences="
				+ mPreferences + ", ID=" + ID + ", USERNAME=" + USERNAME
				+ ", PASSWORD=" + PASSWORD + ", DEVICEDNAME=" + DEVICEDNAME
				+ ", LEVELNAME=" + LEVELNAME + ", IMAGEURL=" + IMAGEURL
				+ ", IMAGELOC=" + IMAGELOC + ", NICKNAME=" + NICKNAME
				+ ", TOTALMILEAGE=" + TOTALMILEAGE + ", TOTALTIME=" + TOTALTIME
				+ ", SPPEED=" + SPPEED + ", ISDEL=" + ISDEL + ", ISREMEMBER="
				+ ISREMEMBER + ", INFOTR=" + INFOTR + ", CHANELID=" + CHANELID
				+ ", DEVICIID=" + DEVICIID + ", BLUETOOTH=" + BLUETOOTH
				+ ", DATE=" + DATE + ", FIRST_USE=" + FIRST_USE + ", FLAG="
				+ FLAG + ", AUTO=" + AUTO + ", PHTONUM=" + PHTONUM
				+ ", VIDEONUM=" + VIDEONUM + ", ZNC=" + ZNC + ", SNC=" + SNC
				+ ", YYC=" + YYC + ", RUNTIME=" + RUNTIME + "]";
	}

}
