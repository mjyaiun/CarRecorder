package com.mitu.carrecorder.entiy;
/**
 * 用户类
 * @author Administrator
 *
 */
public class User {

	private int id;
	private String username;//手机号
	private String passwd; //密码
	private String nickname;//昵称  最大长度32
	private String image;//头像 地址
	private int osType;//1=andriod 2=ios
	private String devicename;//设备名  最大长度32
	private String date;//注册时间
	private String totalmileage;//总里程
	private String totaltime;//总时间
	private String speed;//平均速度
	private int isdel;//用户状态1=禁用 0=可用
	private String levelname;//等级
	private String deviceid;//设备id 用于百度云推送
	private String channelid;//通道id 用于百度云推送
	private String qquid,weibouid,weixinuid;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(int id, String username, String passwd, String nickname,
			String image, int osType, String devicename, String date,
			String totalmileage, String totaltime, String speed, int isdel,
			String levelname, String deviceid, String channelid, String qquid,
			String weibouid, String weixinuid) {
		super();
		this.id = id;
		this.username = username;
		this.passwd = passwd;
		this.nickname = nickname;
		this.image = image;
		this.osType = osType;
		this.devicename = devicename;
		this.date = date;
		this.totalmileage = totalmileage;
		this.totaltime = totaltime;
		this.speed = speed;
		this.isdel = isdel;
		this.levelname = levelname;
		this.deviceid = deviceid;
		this.channelid = channelid;
		this.qquid = qquid;
		this.weibouid = weibouid;
		this.weixinuid = weixinuid;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getOsType() {
		return osType;
	}
	public void setOsType(int osType) {
		this.osType = osType;
	}
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTotalmileage() {
		return totalmileage;
	}
	public void setTotalmileage(String totalmileage) {
		this.totalmileage = totalmileage;
	}
	public String getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}
	public String getLevelname() {
		return levelname;
	}
	public void setLevelname(String levelname) {
		this.levelname = levelname;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getQquid() {
		return qquid;
	}
	public void setQquid(String qquid) {
		this.qquid = qquid;
	}
	public String getWeibouid() {
		return weibouid;
	}
	public void setWeibouid(String weibouid) {
		this.weibouid = weibouid;
	}
	public String getWeixinuid() {
		return weixinuid;
	}
	public void setWeixinuid(String weixinuid) {
		this.weixinuid = weixinuid;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", passwd="
				+ passwd + ", nickname=" + nickname + ", image=" + image
				+ ", osType=" + osType + ", devicename=" + devicename
				+ ", date=" + date + ", totalmileage=" + totalmileage
				+ ", totaltime=" + totaltime + ", speed=" + speed + ", isdel="
				+ isdel + ", levelname=" + levelname + ", deviceid=" + deviceid
				+ ", channelid=" + channelid + ", qquid=" + qquid
				+ ", weibouid=" + weibouid + ", weixinuid=" + weixinuid + "]";
	}
	
	
}
