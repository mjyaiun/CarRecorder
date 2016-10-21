package com.mitu.carrecorder.entiy;

/**
 * 菜单项数据
 */
public class BEMenuItem {
	/**图标资源*/
	public int iconId;
	public String iconUrl;
	/**菜单名称*/
	public String name = "";
	public int checkId;

	public String moreInfo = "";

	public int type ;
	public boolean isCheck;

	public BEMenuItem(){}
	public BEMenuItem(String name){
		this.name = name;
	}

	public BEMenuItem(int iconId, String name){
		this.iconId = iconId;
		this.name = name;
	}

	public BEMenuItem(String name, String info){
		this.name = name;
		this.moreInfo = info;
	}

	public BEMenuItem(int iconId, String name, String info){
		this.iconId = iconId;
		this.name = name;
		this.moreInfo = info;
	}
}
