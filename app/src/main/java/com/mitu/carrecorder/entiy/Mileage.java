package com.mitu.carrecorder.entiy;

import java.io.Serializable;
/**
 * 我的里程
 * @author Administrator
 *
 */
public class Mileage implements Serializable{

    private int id;
	 private int uid;
	 private String start;//起点
	 private String end;//终点
	 private String mileage;//里程
	 private String time;//耗时
	 private String date;//日期
	 private String speed;//速度
	 private String nickname;
	 private String username;
	 private String ln;
    private  String lg;
	
	public Mileage() {
		super();
	}
	
	public Mileage(int id, int uid, String start, String end, String mileage,
			String time, String date, String speed, String nickname,
			String username,String ln,String lg) {
		super();
		this.id = id;
		this.uid = uid;
		this.start = start;
		this.end = end;
		this.mileage = mileage;
		this.time = time;
		this.date = date;
		this.speed = speed;
		this.nickname = nickname;
		this.username = username;
		this.ln= ln;
		this.lg = lg;
	}

	public String getLn() {
		return ln;
	}

	public void setLn(String ln) {
		this.ln = ln;
	}

	public String getLg() {
		return lg;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Mileage [id=" + id + ", uid=" + uid + ", start=" + start
				+ ", end=" + end + ", mileage=" + mileage + ", time=" + time
				+ ", date=" + date + ", speed=" + speed + ", nickname="
				+ nickname + ", username=" + username + ", ln=" + ln + ", lg="
				+ lg + "]";
	}

	
}
