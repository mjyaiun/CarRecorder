package com.mitu.carrecorder.entiy;

import java.io.Serializable;

/**]
 * ��Ƭ
 * @author Administrator
 *
 */
public class Photo implements Serializable{

	private int id;
	private String path;
	private boolean flag;
	private String time;
	private  int type;//照片0或视频1
	private boolean status;
	private String name;
	
	public Photo() {
		super();
	}
	public Photo(int id, String path, boolean flag) {
		super();
		this.id = id;
		this.path = path;
		this.flag = flag;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "Photo [id=" + id + ", path=" + path + ", flag=" + flag
				+ ", time=" + time + ", type=" + type + ", status=" + status
				+ ", name=" + name + "]";
	}
	
	
}
