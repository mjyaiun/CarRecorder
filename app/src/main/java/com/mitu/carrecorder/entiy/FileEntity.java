package com.mitu.carrecorder.entiy;

import java.io.Serializable;

/**
 * File list entity
 * @author Administrator
 *
 */
public class FileEntity implements Serializable{

	public String Name;
	public String FPath;
	public String Size;
	public String TimeCode;
	public String Time;
	public String Attr;

	//
	public boolean isHasThumbNail = false;

	public String dateString ;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getFPath() {
		return FPath;
	}
	public void setFPath(String fPath) {
		FPath = fPath;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getTimeCode() {
		return TimeCode;
	}
	public void setTimeCode(String timeCode) {
		TimeCode = timeCode;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getAttr() {
		return Attr;
	}
	public void setAttr(String attr) {
		Attr = attr;
	}
	
	
}
