package com.mitu.carrecorder.entiy;

import java.io.Serializable;
/**
 * 轮播图
 * @author Administrator
 *
 */
public class PictureInfo implements Serializable{
	    private int id;
	    private String localImage;
		private String image;// 图片地址
		private String title;// 标题
		private String type;// 类型 1=内部 2=外部 3=图文
		private String path;
	//跳转路径  1=首页 2=开始检测 3=扫描设备 4=健康统计 5=客户管理
   
		
		
	public PictureInfo() {
		super();
	}
	
	public PictureInfo(int id, String localImage, String image, String title,
			String type, String path) {
		super();
		this.id = id;
		this.localImage = localImage;
		this.image = image;
		this.title = title;
		this.type = type;
		this.path = path;
	}
	
	
	
	public String getLocalImage() {
		return localImage;
	}

	public void setLocalImage(String localImage) {
		this.localImage = localImage;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "PictureInfo [id=" + id + ", localImage=" + localImage
				+ ", image=" + image + ", title=" + title + ", type=" + type
				+ ", path=" + path + "]";
	}
	
	
	

		
		
		
		
}
