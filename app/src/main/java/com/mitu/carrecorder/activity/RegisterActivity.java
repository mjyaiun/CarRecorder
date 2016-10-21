package com.mitu.carrecorder.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.Validates;

/**
 * 注册
 * @author Administrator
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	private TextView tvProtocal;
	private Button btnNext,btyzm;
	private EditText etPhone,etyzm;
	private CheckBox chbAgree;
	private LinearLayout llregist;
	private String smsCode;
	private int time=60;
	private boolean codeFlag=true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}


	//
	public void initView() {
        setTitle(getString(R.string.register));
		initTitleBack();
        llregist=(LinearLayout) findViewById(R.id.rl_register);
        etyzm=(EditText) findViewById(R.id.et_register_sms);
		tvProtocal=(TextView) findViewById(R.id.tv_register_protocal);
		tvProtocal.setOnClickListener(this);
		btyzm=(Button) findViewById(R.id.tv_register_sms);
		btnNext=(Button) findViewById(R.id.btn_register_next);
		btnNext.setOnClickListener(this);
		btyzm.setOnClickListener(this);
		llregist.setOnClickListener(this);
		etPhone=(EditText) findViewById(R.id.et_register_phone);
		chbAgree=(CheckBox) findViewById(R.id.chb_register_agree);
	}

	
	@Override
	public void onClick(View view) {
		 String phoneString = etPhone.getText().toString();
	     String etCoString = etyzm.getText().toString();
		switch (view.getId()) {
			case R.id.tv_register_protocal:
				changeActivity(RegistrationProtocolActivity.class);
				break;
			case R.id.rl_register://取消软键盘
				CancelSoftKeyBord(view);
				break;
			case R.id.btn_register_next:
				if(TextUtils.isEmpty(phoneString.trim())){
					showMyDialog(this, getString(R.string.inputPhoneNumber));
					return;
				}
				if (TextUtils.isEmpty(etCoString)){
					showMyDialog(this,getString(R.string.inputCheckCode));
					return;
				}
				if(!Validates.isMobile(phoneString)){
					showMyDialog(this, getString(R.string.phoneInputError));
					return;
				}
				if(!chbAgree.isChecked()){
					showMyDialog(this,getString(R.string.agreeProtcol));
				}else {
					changeActivity(SetPwdForRegisteActivity.class,"phone",phoneString);
				}
//			    	else if(!etCoString.equals(smsCode)){
//						showMyDialog(this, "验证码错误，请重新输入");
//			    }}else if(chbAgree.isChecked()){
//				doJudgePhone(phoneString);
//			}else {
//				showMyDialog(this, "请您同意注册协议");
//			}

				break;
			case R.id.tv_register_sms:
				doGetCheckCode();
				break;
			default:
				break;
		}
	}


	/**
	 * 请求验证码
	 */
	private void doGetCheckCode(){
		/*OkHttpUtils.post()
				.url()
				.addParams()
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int i) {

					}

					@Override
					public void onResponse(String s, int i) {

					}
				});*/
	}


}
