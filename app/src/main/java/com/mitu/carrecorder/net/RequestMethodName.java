package com.mitu.carrecorder.net;

/**
 * 请求方法名常数接口
 * @author
 */
public interface RequestMethodName {


	/**默认登陆*/
	//String DEFAULT_LOGIN = "user!defaultLogin.action";
	/**登陆*/
	String LOGIN = "VipUserInter";
	String ABOUTUS_IP = "AboutUsInter";
	String FEED_BACK = "OpinionInfoInter";
	String AD_BANNER = "advInter";
	/**检查登录*//*
	String CHECK_LOGIN = "user!checkLogin.action";
	*//**退出登陆*//*
	String LOGIN_OUT = "user!logout.action";
	*//**验证吗登陆*//*
	String LOGIN_WITH_VALIDATE = "user!loginWithValidate.action";

	*//**获取验证码*//*
	String GET_CHECK_CODE = "user!sendValidate.action";
	*//**忘记密码获取验证码*//*
	String FORGET_PWD_GET_CHECK_CODE = "user!sendValidateForLogin.action";
	*//**重置密码获取验证吗*//*
	String RESET_PWD_GET_CHECKCODE = "user!sendValidateForReset.action";
	*//**修改密码*//*
	String RESET_PWD = "user!resetPwd.action";

	*//**注册*//*
	String REG = "user!reg.action";
	*//**首页轮播广告*//*
	String AD_BANNER = "business!getAdBanner.action";
	*//**获取餐饮商家信息*//*
	String GET_CATERING_INFO_LIST = "catering!getCateringInfos.action";

	*//**获取当前用户的所有消息通知*//*
	String  GET_MESSAGE_INFOS = "user!notifications.action";

	*//**获取商家的所有菜品信息*//*
	String GET_DISHES_INFOS = "catering!getDishesInfos.action";

	*//**获取商家评论*//*
	String GET_BUSINESS_EVALUATE = "business!getCommentByBusinessId.action";

	*//**当前用户的所有订单*//*
	String GET_ORDER_LIST = "order!getOrders.action";
	*//**根据订单号获取订单详情*//*
	String GET_ORDER_CONTENT_BY_NUMBER = "order!getOrderByNumber.action";
	*//***//*
	String GET_ORDER_DISH = "order!getOrderDishesByOrder.action";
	*//**获取配送费信息*//*
	String GET_DELIVER_FEE = "catering!getDeliverFees.action";
	*//**获取用户默认收货地址*//*
	String GET_DEFAULT_PICK_ADDRESS = "user!getDefaultAddress.action";

	*//**商家分类一级分类*//*
	String GET_BUSINESS_TYPE_PARENT = "business!getBusinessTypeParent.action";
	*//**商家分类二级分类*//*
	String GET_BUSINESS_DISPLAY_CHILD = "business!getBusinessDisplayTypes.action";
	*//**提交新订单*//*
	String SUBMIT_NEW_ORDER = "order!newOrder.action";
	*//**检查是否首单*//*
	String CHECK_FIRST_ORDER = "order!checkFirstOrder.action";
	*//**获取城市列表*//*
	String GET_CITY_LIST = "business!getCitys.action";
	*//**取消订单*//*
	String ORDER_CANCEL = "order!cancelOrder.action";
	*//**提交抽到的奖金结果*//*
	String LOTTERY_RESULT_MONEY = "user!awardBalance.action";
	*//**众筹*//*
	String CROWED_INFO = "invest!index.action";
	*//**添加推荐人*//*
	String ADD_RECOMMEND_USER = "invest!addRecommendUser.action";
	*//**投资记录*//*
	String INVEST_RECORD = "invest!getInvestRecords.action";
	*//***提现记录*//*
	String TAKE_CASH_RECORD = "invest!getWithdrawRecords.action";
	*//**历史收益*//*
	String HISTORY_PROFIT_RECORD = "invest!getProfitRecords.action";

	*//**支付宝支付*//*
	String ALPAY_PAYMENT = "payment/pay!cateringNative.action";
	*//**微信支付*//*
	String WXPAY_PAYMENT = "payment/pay!cateringWxNative.action";
	*//**郎币支付*//*
	String LANGBI_PAY = "order!cateringChargePayAjax.action";
	*//**货到付款*//*
	String ARRIVE_PAY = "order!cateringArriveAjax.action";
	*//**新增投资获取订单号*//*
	String NEW_INVEST = "invest!newOrder.action";
	*//**投资支付宝支付参数*//*
	String INVEST_ALIPAY = "payment/pay!investNative.action";
	*//**微信投资参数*//*
	String INVEST_WX = "payment/pay!investWxNative.action";
	*//**翼支付*//*
	String INVEST_YI = "payment/pay!invest.action";
	*//**提现*//*
	String TAKE_CASH = "invest!withdraw.action";
	*//**获取地址列表*//*
	String ADDRESS_LIST = "user!getAddressList.action";
	*//**修改或新增地址*//*
	String MODIFY_ADDRESS = "user!saveAddress.action";
	*//**默认地址*//*
	String DEFAULT_ADDRESS = "user!setDefaultAddress.action";

	*//**修改头像*//*
	String USER_HEAD = "user!uploadProfile.action";

	*//**新建充值订单*//*
	String NEW_CHARGE_ORDER = "charge!newCharge.action";




	*//**========================================**//*
	*//**奖品接口列表*//*
	String LOTTERY_LIST = "userinterface/redpacketlist";
	*//**抽中接口*//*
	String LOTTERY_RESULT = "userinterface/addwinning";
	*//**已获奖接口*//*
	String WINNING_LIST = "userinterface/winninglist";






	//*//**找回密码*//*
	//String RETRIEVE_PASSWORD = "FindPassword";
	//*//**修改密码*//*
	//String MODIFY_PASSWORD = "ModifyPassword";



	*//**软件升级*//*
	//String UPDATE = "UpdateAppVersion";
	*//**意见反馈*//*
	String FEEDBACK = "feedback.action";


	*//**忘记密码*//*
	String FORGET_PASSWORD = "setPassword.action";*/



}
