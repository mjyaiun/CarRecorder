package com.mitu.carrecorder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validates {

	/*
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 
	 * 　　 联通：130、131、132、152、155、156、185、186
	 * 
	 * 　　 电信：133、153、180、189、（1349卫通）
	 * 
	 *   170
	 */

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9]))\\d{8}$");
		m = p.matcher(str);
		b = m.matches();
		return b;

		/*
		 * String value="手机号"; //第二种 String regExp =
		 * "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$"; Pattern p =
		 * Pattern.compile(regExp); Matcher m = p.matcher(value); return
		 * m.find();//boolean
		 */

	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}


	/**
	 * 验证密码
	 * @param pasdNums
	 * @return
     */
	public  static boolean checkPwd(String pasdNums) {
		// TODO Auto-generated method stub
		if(Pattern.matches("^[0-9a-zA-Z]{6,16}", pasdNums)){
			return true;
		}
		return false;
	}



	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 * 
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^（[a-z0-9A-Z]+[-|\\.]?）+[a-z0-9A-Z]@（[a-z0-9A-Z]+（-[a-z0-9A-Z]+）？\\.）+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,长度为4-20位,中文按二位计数
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean validateYHNC(String userName) {
		String validateStr = "^[\\w\\-－＿[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
		boolean rs = false;
		rs = matcher(validateStr, userName);
		if (rs) {
			int strLenth = getStrLength(userName);
			if (strLenth < 4 || strLenth > 20) {
				rs = false;
			}
		}
		return rs;
	}

	/**
	 * 获取字符串的长度，对双字符（包括汉字）按两位计数
	 * 
	 * @param value
	 * @return
	 */
	public static int getStrLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	private static boolean matcher(String reg, String string) {
		boolean tem = false;
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		tem = matcher.matches();
		return tem;
	}

	/**
	 * 隐藏身份证的生日部分
	 * 
	 * @return
	 */
	public static String getCardId(String cardId) {
		String str1 = cardId.substring(3, 16);
		String str = cardId.replace(str1, "*************");
		return str;
	}

	/**
	 * 隐藏手机号中间四位
	 * @param phone
	 * @return
     */
	public static String getPhoneID(String phone){
		String string = phone.substring(3,7);
		phone = phone.replace(string,"****");
		return phone;
	}


	/***
	 * 匹配手机号是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			//System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}


	/**
	 * 匹配手机号是否合法   有问题：123456789能通过验证
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneVaild(String phone){
		String value = phone;
		//正则匹配包含目前全新的号段
		String regExp = "^[1]([3|4|7|8][0-9]{1}|55|59|58|)[0-9]{8}$";
		//进行正则验证
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		//返回值boolean类型
		//System.out.println(m.find()+phone);
		return m.find();
	}

	/**
	 * 判断是否包含特殊字符
	 * @param string
	 * @return
	 */
	public static boolean isContainsOtherChar(String string){
		//String str = "/";
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(string);
        /*if(m.find())
            ToastUtils.showShort(this,"密码不允许输入特殊字符！");*/
		//System.out.println(m.find());
		return m.find();

	}


}
