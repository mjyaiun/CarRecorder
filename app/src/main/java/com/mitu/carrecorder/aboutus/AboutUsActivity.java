package com.mitu.carrecorder.aboutus;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.utils.SystemUtils;

import okhttp3.Call;

/**
 * 关于我们
 *
 * @author Administrator
 */
public class AboutUsActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

    }

    @Override
    public void initView() {
        // TODO 自动生成的方法存根
        setTitle(getString(R.string.aboutUs));
        initTitleBack();
        tv = (TextView) findViewById(R.id.tv_about);
        getMessage();
    }

    private void getMessage() {
        initProgressDialog(getString(R.string.onLoading));
        OkHttpUtils.get()
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.ABOUTUS_IP))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissDialog();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dismissDialog();
                        Log.i("OkHttpUtils"," aboutUs"+s);
                    }
                });
         /*new Thread(){
				public void run() {
					RequestParams params=new RequestParams();
					HttpUtils utils = new HttpUtils();
	     			utils.configSoTimeout(60000);
	     			utils.send(HttpRequest.HttpMethod.POST, NetField.ABOUTUS_IP, params,
	     					new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
								Toast.makeText(AboutUsActivity.this, getString(R.string.qjcwl), Toast.LENGTH_SHORT).show();	
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									cancelDialog();
									 String errorString= JsonUtils.getMessage(arg0.result);
										String resultCode=JsonUtils.getResultCode(arg0.result);
										Log.d("com.mh.fjkj.menu", "fanhui1="+arg0.result);
										if(resultCode.equals("1")){
//										tv.setText(text);
										
									  }else {
										  Toast.makeText(AboutUsActivity.this, errorString, Toast.LENGTH_SHORT).show();
									}
									 
								}
	     				
	     			});
	     			}

				
			}.start();*/

    }


}
