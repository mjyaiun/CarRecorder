package com.mitu.carrecorder.entiy;
/**
 * 单条行车记录
 * @author Administrator
 *
 */
public class MileageRecord {

	 private int id;
	 private String time;
	 private int status;//0:启动  1：运行中 2：停止
	 private String devicename;
	 private String lat;//纬度
	 private String lng;//经度
	 private String username;
	 private String start;
	 private String end;
	 private String mileage;
	 private String hour;
    private int uid;
    
    
    
	public MileageRecord() {
		super();
	}

	public MileageRecord(int id, String time, int status, String devicename,
			String lat, String lng, String username, String start, String end,
			String mileage, String hour, int uid) {
		super();
		this.id = id;
		this.time = time;
		this.status = status;
		this.devicename = devicename;
		this.lat = lat;
		this.lng = lng;
		this.username = username;
		this.start = start;
		this.end = end;
		this.mileage = mileage;
		this.hour = hour;
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "MileageRecord [id=" + id + ", time=" + time + ", status="
				+ status + ", devicename=" + devicename + ", lat=" + lat
				+ ", lng=" + lng + ", username=" + username + ", start="
				+ start + ", end=" + end + ", mileage=" + mileage + ", hour="
				+ hour + ", uid=" + uid + "]";
	}
    
	
    

}
