package com.mitu.carrecorder;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.entiy.User;
import com.mitu.carrecorder.utils.AsyncImageLoader;
import com.umeng.socialize.PlatformConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class BEApplication extends Application {


	private static BEApplication sInstance;

	public static User currentUser;


	/**图片loader*/
	public static AsyncImageLoader loader = new AsyncImageLoader();

	/**屏幕宽度*/
	public static int width = 480;
	/**屏幕高度*/
	public static int height = 800;
	/**密度比例*/
	public static double dx = 1.0;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//FrontiaApplication.initFrontiaApplication(getApplicationContext());//百度云推送初始化

		SDKInitializer.initialize(this);

		//OkHttpUtils.getInstance().
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.build();
		OkHttpUtils.initClient(okHttpClient,this);
		//
		Fresco.initialize(this);
		super.onCreate();
		sInstance = this;

		//
		PlatformConfig.setQQZone("1105558016", "7IoAqxguYpzbQnbt");
		PlatformConfig.setSinaWeibo("713817874","6157eae778b54bbca50ff55aecfec01c");
	    
	}


	public static Context getAppContext() {
		return sInstance;
	}

}
