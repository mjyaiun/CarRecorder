package com.mitu.carrecorder.activity;
/**
 * 忘记密码重置密码
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.Validates;

public class ResetPwdActivity extends BaseActivity implements OnClickListener {

	private EditText etNewPsw,etConfirmPsw;
	private Button btnComplete;
	//private SpHelper sp;
	private RelativeLayout resetLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_psw);
	}


	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.resetPwd));
		initTitleBack();
		etNewPsw=(EditText) findViewById(R.id.et_newPassword);
		etConfirmPsw=(EditText) findViewById(R.id.et_password_confirm);
		btnComplete=(Button) findViewById(R.id.btn_reset_complet);
		btnComplete.setText(getString(R.string.complate));
		btnComplete.setOnClickListener(this);
		resetLayout=(RelativeLayout) findViewById(R.id.rl_resetpss);
		resetLayout.setOnClickListener(this);
		//sp=new SpHelper(this);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_reset_complet:
			String newPasw=etNewPsw.getText().toString();
			String confirmPasw=etConfirmPsw.getText().toString();
			if(TextUtils.isEmpty(newPasw.trim())){
			    showMyDialog(this,getString(R.string.pwdNotAllowEmpty))	;
			}else if(!Validates.checkPwd(newPasw)){
				showMyDialog(this,getString(R.string.pwdNotStandard));
			}else if(!newPasw.equals(confirmPasw)){
				showMyDialog(this,getString(R.string.twiceNotSame));
			}else {
				doPasssord(newPasw);//修改密码
			}
			break;
		case R.id.rl_resetpss:
			CancelSoftKeyBord(view);
		default:
			break;
		}
	}

	private void doPasssord(final String newPasw) {
		// TODO Auto-generated method stub
		initProgressDialog(getString(R.string.onSaving));
		/*new Thread(){
			public void run() {
				RequestParams params=new  RequestParams();
				params.addBodyParameter("flag", "5");
				params.addBodyParameter("username", getIntent().getStringExtra("phone"));
				params.addBodyParameter("newpasswd", newPasw);
				HttpUtils utils = new HttpUtils();
     			utils.configSoTimeout(30000);
     			utils.send(HttpRequest.HttpMethod.POST, NetField.VIPUSER_IP, params, 
     					new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								cancelDialog();
								Toast.makeText(com.mh.carcorder.ResetPwdActivity.this, getString(R.string.qjcwl), Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								// TODO Auto-generated method stub
								cancelDialog();
								  showToast(com.mh.carcorder.ResetPwdActivity.this,getString(R.string.gxnxgcg));
								
									if(JsonUtils.getResultCode(arg0.result).equals("1")){
										  sp.savePassword(newPasw);		
										  startActivity(new Intent(com.mh.carcorder.ResetPwdActivity.this, LoginActivity.class));
									}else {
										showToast(com.mh.carcorder.ResetPwdActivity.this,JsonUtils.getMessage(arg0.result));
									}
							}

							
     				
     			});
			};
		}.start();*/
	}
	
}
