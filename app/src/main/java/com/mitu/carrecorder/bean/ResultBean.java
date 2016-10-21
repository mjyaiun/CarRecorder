package com.mitu.carrecorder.bean;

/**
 * 响应反馈类的父类
 * @author
 */
public class ResultBean {
	/**处理结果*/
	public boolean result;
	/**错误提示*/
	public String msg = "数据错误";

	//影响行数
	public int back;

	public ResultBean(){}

}
