package com.mitu.carrecorder;

/**
 * 项目常数接口
 * @author
 */
public interface BEConstants {
	/**项目文件夹*/
	String FOLDER = "/carrecorder/";

	//String ADDRESS_HEADER = "http://115.159.161.251/";
	/**正式服务器*/
	String ADDRESS_HEADER = "http://123.56.192.49:8081/drawable/inter/";
	String ADDRESS_HEADER_REAL = "";

	String ADDRESS_IP_DEVICE = "http://192.168.1.254";
	String RTSP_ADDRSS_MOVIE_LIVE = "rtsp://192.168.1.254/xxxx.mov";

	String FILE_ADDRESS = "http://192.168.1.254/CARDV/";

	//******************存储名称******************
	/**用户名存储名称*/
	String SAVE_NAME_USERNAME = "userName";
	/**密码存储名称*/
	String SAVE_NAME_PASSWORD = "password";
	/**是否自动登录*/
	String SAVE_NAME_AUTOLOGIN = "autoLogin";

	/**本地保存的MD5值*/
	String MD5STRING = "md5String";



	/**新版本是否引导*/
	String NEW_VERSION_GUIDE = "newVersionGuide";

	/**收货地址操作类型*/
	String ADDRESS_OP_TYPE = "addressOP";
	/**新增收货地址*/
	int ADDRESS_ADD = 1;
	/**修改收货地址*/
	int ADDRESS_MODIFY = 2;


	/**首页主图的宽度*/
	//int MAIN_IMAGE_WIDTH = 100;
	/**首页主图的高度*/
	int MAIN_IMAGE_HEIGHT = 225;


	int IMAGE_WIDTH_60 = 60;
	int IMAGE_HEIGHT_60 = 60;

	int IMAGE_WIDTH_180 = 140;
	int IMAGE_HEIGHT_140 = 120;

	String NO_CHANGE = "nochange";

	/**订单状态*/
	//-2-取消 -1-支付失败 0-未支付 1-支付成功 2-已发货 3-已收货
	int ORDER_CANCEL = -2;
	int ORDER_PAY_FAILED = -1;
	int ORDER_NOT_PAY = 0;
	int ORDER_ALREADY_PAY = 1;
	int ORDER_DELIVER = 2;
	int ORDER_TAKE_DELIVER = 3;

	//配送状态 0-未配送 1-配送中 2-配送完成
	//int SEND_


	String ORDER_FINISH_STRING = "已送达";
	String ORDER_ALREADY_PAY_STRING = "已支付";
	String ORDER_NOT_PAY_STRING = "未支付";



	//md5信息表中MD5类型
	int MD5_FOR_BUSINESSLIST = 0;
	int MD5_FOR_DELIVER_FEE = 1;


	//
	String DELIVER_FEE = "deliver_fee";

	//******************微信支付常量***************//
	/**appId*/
	String WECHAT_APPID = "wx94feb48beaac3ddc";
	/**apiKey*/
	String WECHAT_APIKEY = "LEEANLIANOP098765HJKLMNTGBI98342";



}
