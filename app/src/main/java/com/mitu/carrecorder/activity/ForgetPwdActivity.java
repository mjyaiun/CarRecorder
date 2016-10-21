package com.mitu.carrecorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.Validates;

/**
 * 忘记密码
 * 
 * @author Administrator
 * 
 */
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {

	private Button btnNext;
	private EditText etPhone, etCode;
	private LinearLayout llpass;
	private String smsCode;
	private TextView tvSms;
	private int time = 60;
	private boolean codeFlag = true;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0x111) {
				tvSms.setText(getString(R.string.sysj) + time + ")");
				if (time == 0) {
					codeFlag = false;
					tvSms.setText(getString(R.string.sendAgain));
					tvSms.setClickable(true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_psw);

	}

	public void initView() {
		setTitle(getString(R.string.zhmm));
		initTitleBack();
		llpass = (LinearLayout) findViewById(R.id.rl_register);
		etCode = (EditText) findViewById(R.id.et_forget_sms);
		tvSms = (TextView) findViewById(R.id.tv_forget_sms);
		btnNext = (Button) findViewById(R.id.btn_register_next);
		btnNext.setOnClickListener(this);
		llpass.setOnClickListener(this);
		etPhone = (EditText) findViewById(R.id.et_register_phone);
		tvSms.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.rl_register:// 取消软键盘
			CancelSoftKeyBord(view);
			break;
		case R.id.btn_register_next:
			String phoneString = etPhone.getText().toString();
			if (TextUtils.isEmpty(phoneString)) {
				showMyDialog(this,getString(R.string.inputPhoneNumber));
			} else if (!Validates.isMobile(phoneString)) {
				showMyDialog(this,getString(R.string.phoneInputError));
			}
			// else if(TextUtils.isEmpty(etCoString)){
//			 showMyDialog(this,getString(R.string.qsryzm));
			// }else if(!etCoString.equals(smsCode)){
			// showDialog(this,getString(R.string.yzmcw));

			else {
				Intent intent = new Intent(ForgetPwdActivity.this,
						ResetPwdActivity.class);
				intent.putExtra("phone", phoneString);
				startActivity(intent);
//doGetSMScode(phoneString);
//				
//				//开启线程计时
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						while (codeFlag) {
//							time--;
//							handler.sendEmptyMessage(0x111);
//							try {
//								Thread.sleep(1000);
//							} catch (Exception e) {
//								// TODO: handle exception
//							}
//
//						}
//					}
//				}).start();
//			}
			}
			break;
		default:
			break;
		}

	}
//	//获取短信验证码
//			private void doGetSMScode(final String phoneString) {
//				// TODO Auto-generated method stub
//				 showDialog(ForgetPswActivity.this, "正在发送...");
//				 new Thread(){
//						public void run() {
//							RequestParams params=new RequestParams();
//							params.addBodyParameter("flag", "8");
//							params.addBodyParameter("phone",phoneString);					
//							HttpUtils utils = new HttpUtils();
//			     			utils.configSoTimeout(60000);
//			     			utils.send(HttpRequest.HttpMethod.POST, NetField.VIPUSER_IP, params, 
//			     					new RequestCallBack<String>() {
//
//										@Override
//										public void onFailure(HttpException arg0,
//												String arg1) {
//											// TODO Auto-g enerated method stub
//											cancelDialog();
//											tvSms.setClickable(true);
//											showToast(ForgetPswActivity.this,getString(R.string.fssb));
//											
//										}
//
//										@Override
//										public void onSuccess(ResponseInfo<String> arg0) {
//											// TODO Auto-generated method stub
//											      cancelDialog();
//											      tvSms.setClickable(false);
//												  showToast(ForgetPswActivity.this,getString(R.string.yzmfscg));
//												  try {
//														JSONObject object = new JSONObject(arg0.result);
//														smsCode=object.getString("code");
//													} catch (JSONException e) {
//														e.printStackTrace();
//													}	
//											     }
//											 
//									
//			     				
//			     			});
//			     			}
//
//						
//					}.start();
//			}

}
