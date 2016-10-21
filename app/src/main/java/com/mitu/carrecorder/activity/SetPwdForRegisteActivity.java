package com.mitu.carrecorder.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.utils.Validates;

import okhttp3.Call;


/**
 * 设置密码
 * @author Administrator
 *
 */
public class SetPwdForRegisteActivity extends BaseActivity implements OnClickListener {

	private EditText etNewPsw,etConfirmPsw;
	private Button btnComplete;
	//private SpHelper sp;
	private RelativeLayout setLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pwd_for_registe);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.setPwd));
		initTitleBack();
		etNewPsw=(EditText) findViewById(R.id.et_newPassword);
		etConfirmPsw=(EditText) findViewById(R.id.et_password_confirm);
		btnComplete=(Button) findViewById(R.id.btn_reset_complet);
		btnComplete.setOnClickListener(this);
		setLayout=(RelativeLayout) findViewById(R.id.rl_resetpss);
		setLayout.setOnClickListener(this);
		//sp = new SpHelper(this);
	}

	@Override
	public void onClick(View view) {
		String newPasw = etNewPsw.getText().toString();
		String confirmPasw = etConfirmPsw.getText().toString();
		switch (view.getId()) {
		case R.id.btn_reset_complet:
			
			if(TextUtils.isEmpty(newPasw.trim())){
				showMyDialog(this, getString(R.string.pwdNotAllowEmpty));
			}else if(!newPasw.equals(confirmPasw)){
				showMyDialog(this, getString(R.string.twiceNotSame));
			}else if(!Validates.checkPwd(newPasw)){
				showMyDialog(this, getString(R.string.pwdNotStandard));
			}else {
				doRegister(newPasw);//注册
			}
			break;
		case R.id.rl_resetpss:
			CancelSoftKeyBord(view);
			break;

		default:
			break;
		}
	}


	private UserBean userBean;
	//
	private void doRegister(final String newPasw) {
		initProgressDialog(getString(R.string.onDoingRegiste));
		OkHttpUtils.post()
				.url(SystemUtils.getFullAddressUrl(RequestMethodName.LOGIN))
				.addParams("flag","2")
				.addParams("username",getIntent().getStringExtra("phone"))
				.addParams("passwd",newPasw)
				.addParams("channelid",SystemUtils.getDeviceId(this))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int i) {
						dismissDialog();
						showToast(R.string.checkNetWorkAndTry);
					}

					@Override
					public void onResponse(String s, int i) {
						dismissDialog();
						Log.i("userRegiste",s);
						userBean = Response.parseUserRegisteResult(s);
						if (userBean.result){
							showToast(getString(R.string.registeSuccess));
							//sp.set
						}else{
							showToast(userBean.msg);
						}
					}
				});
	}

	

}
