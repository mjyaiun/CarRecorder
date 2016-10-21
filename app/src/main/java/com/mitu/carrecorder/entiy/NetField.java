package com.mitu.carrecorder.entiy;

/**
 * 接口地址
 *
 * @author Administrator
 */
public class NetField {
    //公网接口地址
    public static final String IP = "http://123.56.192.49:8081/drawable/inter/";
    //用户登录
    public static final String VIPUSER_IP = IP + "/VipUserInter?";
    //第三方登录
    public static final String THIRD_LOGIN_IP = IP + "PlatFormInter?";
    //所有里程
    public static final String MILLEAGE_IP = IP + "JourneyInter?";
    //单条行车记录
    public static final String EACH_MILLEAGE_IP = IP + "RecordInter?";
    //意见反馈
    public static final String FEEDBACK_IP = IP + "OpinionInfoInter?";
    //跟踪
    public static final String TRACKING_IP = IP + "FollowInter?";
    //关于我们
    public static final String ABOUTUS_IP = IP + "AboutUsInter";
    //服务我们
    public static final String SEVERUS_IP = IP + "AboutServiceUsInter";
    /**
     * 接口请求方法定义
     *
     * @author Michael Zhang (zhangyi_0820@qq.com) <br>
     *         2015年5月5日上午11:49:55
     */
    /**
     * 请求成功
     */
    public static final int SUCC = 0;

    /**
     * 请求失败
     */
    public static final int FAIL = 11;

    /**
     * 默认SecretKey
     */
    public static final String DEFAULT_SECRETKEY = "  ";

    // =======================start 接口站点==========================
    /** 测试服务器 */
    /**
     * 正式服务器
     */
    public static final String SITE_OFFICAL = "http://192.168.1.254/";
    /**
     * 当前使用
     */
    //改成正式环境
//		public static final String SITE = SITE_TEST;

    public static final String SITE = SITE_OFFICAL;
    // =======================end 接口站点==========================

    // =======================start 接口方法==========================
    // public static final String me="@me";
    public static final String me = "";

    /**
     * 1.2登陆
     */
    public static final String LOGIN = SITE + "loginvalidV3/" + me;
    /**
     * 1.11注册
     */
    public static final String REGISTER = SITE + "autoRegByUsernamev1/" + me;
    /**
     * 1.3电站设备
     */
    public static final String PLANTDEVICES = SITE + "PlantDevicesV3/" + me;
    /**
     * 1.4设备和测点信息
     */
//		public static final String DEVICEINFO = SITE + "DeviceinfoV3/" + me;

    public static final String DEVICEINFO = SITE + "DeviceinfoV3/";
    /**
     * 1.5设备测点日图表数据
     */
    public static final String MONITORDAYCHARTV = SITE + "monitordaychartv1/"
            + me;
    /**
     * 1.6设备各种类型发电量月天发电量数据
     */
    public static final String DEVICEMONTHDAYCHARTBYTYPE = SITE
            + "devicemonthdaychartbytype/" + me;
    /**
     * 1.7设备各种类型发电量月天发电量数据
     */
    public static final String DEVICEYEARMONTHCHARTBYTYPE = SITE
            + "deviceyearmonthchartbytype/" + me;
    /**
     * 1.8设备各种类型电量总体图表数据
     */
    public static final String DEVICEYEARCHARTBYTYPE = SITE
            + "deviceyearchartbytype/" + me;
    /**
     * 1.9修改密码
     */
    public static final String RESETPASSWORD = SITE + "resetpassword/" + me;
    /**
     * 1.10找回密码
     */
    public static final String FINDPASSWORD = SITE + "findpassword/" + me;

    /**
     * 1.11获取历史故障
     **/
    public static final String GETERRLIST = SITE + "errorlistv2/" + me;
    /**
     * 1.12接口更新
     **/
    public static final String UPDATEVERSION =
            "http://120.55.196.127/sungws/AppService";
    // =======================end 接口方法==========================
}
