package com.mitu.carrecorder.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.mitu.carrecorder.BEConstants;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


public class SystemUtils {

	/**
	 * 分享设置
	 * @param context 上下文
	 * @param content 内容
	 */
	public static void share(Context context, String content) {
		Intent intentItem = new Intent(Intent.ACTION_SEND); // 分享发送的数据类型
		intentItem.setType("text/plain"); // 分享发送的数据类型
		// intentItem.putExtra(Intent.EXTRA_SUBJECT, "subject"); //分享的主题
		intentItem.putExtra(Intent.EXTRA_TEXT, content); // 分享的内容
		intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 这个也许是分享列表的背景吧
		context.startActivity(Intent.createChooser(intentItem, "分享"));// 目标应用选择对话框的标题
	}
	
	/**
	 * 获得程序版本名称
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
		String versionName = "";
		try{
			String pkName = context.getPackageName();
			versionName = context.getPackageManager().getPackageInfo(
						pkName, 0).versionName;
		}catch(Exception e){
//			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
	 * 获得IMEI
	 * @param context
	 * @return IMEI
	 */
	public static String getIMEI(Context context) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String IMEI = telephonyManager.getDeviceId();
			if (IMEI != null) {
				return IMEI;
			}
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 拨打电话
	 * @param phone
	 */
	public static void dial(Context context,String phone){
		try{
			Intent intent=new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + phone));
			context.startActivity(intent);
		}catch(Exception e){}
	}
	
	/**
	 *
	 * @param context
	 * @param phone
	 * @param content
	 */
	public static void sendSMS(Context context,String phone,String content) {
		try{
			Uri smsToUri = Uri.parse("smsto:" + phone);
			Intent intent = new Intent(Intent.ACTION_SENDTO,
					smsToUri);
			intent.putExtra("sms_body", content); // ����������
			context.startActivity(intent);
		}catch(Exception e){}
	}

	/**
	 *
	 * @param context
	 * @param url
	 */
	public static void openUrl(Context context,String url){
		if(!url.startsWith("http")){//���httpͷ
			url = "http://" + url;
		}
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}

	/**
	 *当前是否有网络连接
	 * @param context
	 * @return true-false-
	 */
	public static boolean isNetConnected(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(gprs != null && gprs.isConnected()){
        	return true;
        }
        if(wifi != null && wifi.isConnected()){
        	return true;
        }
        return false;
	}

	//获取设备Id
	public static String getDeviceId(Context context) {
		// TODO Auto-generated method stub
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
		return DEVICE_ID;
	}

	/**
	 *设置列表框高度，解决滚屏问题
	 * @param listView
	 * @param addHeight
	 */
	public static void initListViewHeight(ListView listView,int addHeight) {
		Adapter adapter = listView.getAdapter();
		if (adapter == null || adapter.getCount() == 0) {
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = 0;
			listView.setLayoutParams(params);
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		params.height += 20 + addHeight;
		listView.setLayoutParams(params);
	}

	/**
	 * 格式化日期，格式为：yyyy-MM-dd
	 * @param c
	 * @return
	 */
	public static String formatCalendar(Calendar c){
		if(c == null){
			return "";
		}
		String dateStr = c.get(Calendar.YEAR) + "-";
		int month = c.get(Calendar.MONTH);
		if(month + 1 < 10){
			dateStr += '0';
		}
		dateStr += (month + 1) + "-";
		int date = c.get(Calendar.DATE);
		if(date < 10){
			dateStr += '0';
		}
		dateStr += date;
		return dateStr;
	}

	/**
	 * 是否有SD卡
	 * @return true-,false-
	 */
	public static boolean isHasSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文件SD卡缓存路径
	 * @return
     */
	public static String getLocalFilePath(){
		return Environment.getExternalStorageDirectory()+
				File.separator + "carrecorder" + File.separator + "download" + File.separator;
	}
	
	/**
	 * 文件是否存在
	 * @param folder SD下的目录
	 * @param fileName 文件名
	 * @return true-存在，false-不存在
	 */
	public static boolean isExistFile(String folder,String fileName){
		if(!isHasSDCard()){
			 return false;
		 }
		 if(fileName == null || fileName.length() == 0){
			 return false;
		 }else{
			 try{
				 String filePath = Environment.getExternalStorageDirectory() + folder + fileName;
				 File f = new File(filePath);
				 if(f.exists()){
					 return true;
				 }
			 }catch(Exception e){
			 }
		 }
		 return false;
	}
	
	/**
	  * 从url中获得文件名
	  * @param url 文件路径
	  * @return 文件名
	  */
	 public static String getFileName(String url){
		 if(url != null && url.length() > 0){
			 int index = url.lastIndexOf('/');
			 if(index > 0){
				 return url.substring(index + 1);
			 }
		 }
		 return "";
	 }
	 
	 /**
	  * 获得全地址
	  * @param url 当前url
	  * @return 完整地址url
	  */
	 public static String getFullAddressUrl(String url){
		 if(url == null || url.length() == 0){
			 return "";
		 }else{
			 if(url.startsWith("http")){
				 return url;
			 }else{
				 return BEConstants.ADDRESS_HEADER + url;
			 }
		 }
		 //return "";
	 }



	 /**
	 * 导入raw目录下的资源文件
	 * @param context 上下文对象
	 * @param rawId 资源id
	 * @return 资源id
	 */
	public static String loadRaw(Context context, int rawId) {
		InputStream is = null;
		String str = "";
		try {
			is = context.getResources().openRawResource(rawId);
			int len = is.available();
			byte[] data = new byte[len];
			is.read(data);
			str = new String(data, "UTF-8");
			return str;
		} catch (Exception e) {
//				 e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
		return str;
	}
	
	/**
	 * 评价软件功能方法
	 * @param context 上下文对象
	 */
	public static void evaluateSoft(Context context){
		try{
			Uri uri = Uri.parse("market://details?id=" + context.getPackageName());  
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			context.startActivity(intent);
		}catch(Exception e){}
	}


	/**
	 * 获取系统时间
	 * @return
	 */
	public static Date getSystemTime(){
		Date curDate =   new Date(System.currentTimeMillis());//获取当前时间
		return curDate;

	}


	/**
	 * dp转成px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/*private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}*/


}