
package com.mitu.carrecorder.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.view.TrackingDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import okhttp3.Call;


public class LoginActivity extends AppCompatActivity implements Callback, OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private Button btnLogin, btnForget, btnRegister;
    private ImageView ivQQ, ivWeixin, ivSina;
    private EditText etPhone, etPassword;
    private CheckBox remberBox;
    private SpHelper sp;
    private RelativeLayout llLogin;
    private ImageView back;

    //
    private UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        sp = new SpHelper(this);
        etPhone.setText(sp.getUserName());
        etPhone.setSelection(sp.getUserName().length());
        if (sp.getIsRemeber()) {
            etPassword.setText(sp.getPassword());
            remberBox.setChecked(true);
        }

        mShareAPI = UMShareAPI.get(this);

    }

    public void showMyDialog(Context context, String message) {
        TrackingDialog.Builder builder = new TrackingDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(getString(R.string.prompt));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    public void initView() {
        // TODO Auto-generated method stub
        btnForget = (Button) findViewById(R.id.btn_login_forget);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        ivQQ = (ImageView) findViewById(R.id.iv_login_qq);
        ivWeixin = (ImageView) findViewById(R.id.iv_login_weixin);
        ivSina = (ImageView) findViewById(R.id.iv_login_weibo);
        etPhone = (EditText) findViewById(R.id.et_login_phone);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        remberBox = (CheckBox) findViewById(R.id.chb_login_remember);
        llLogin = (RelativeLayout) findViewById(R.id.ll_login);
        //
        back = (ImageView)findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        //
        llLogin.setOnClickListener(this);
        btnForget.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        ivWeixin.setOnClickListener(this);
        ivSina.setOnClickListener(this);
        remberBox.setOnCheckedChangeListener(this);
    }


    @Override
    public boolean handleMessage(Message arg0) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_forget:
                Intent intent = new Intent(this, ForgetPwdActivity.class);
                startActivity(intent);
                //changeActivity(ForgetPwdActivity.class);
                break;
            case R.id.btn_login:
                doLogin();//登录方法
                break;
            case R.id.btn_register:
                Intent reg = new Intent(this, RegisterActivity.class);
                startActivity(reg);
                //changeActivity(RegisterActivity.class);
                break;

            case R.id.ll_login://取消软键盘
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.iv_login_qq:
                platform = SHARE_MEDIA.QQ;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);

                break;
            case R.id.iv_login_weixin:
                platform = SHARE_MEDIA.WEIXIN;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.iv_login_weibo:
                platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private SHARE_MEDIA platform = SHARE_MEDIA.QQ;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText( getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private UserBean userBean;

    //登录
    private void doLogin() {
        // TODO Auto-generated method stub
        final String phoneString = etPhone.getText().toString();
        final String passwordString = etPassword.getText().toString();
//			Toast.makeText(this, "2", 0).show();
        if (TextUtils.isEmpty(phoneString.trim())) {
            showMyDialog(this, getString(R.string.inputPhoneNumber));
        } else if (TextUtils.isEmpty(passwordString.trim())) {
            showMyDialog(this, getString(R.string.inputPwd));
        } else if (!SystemUtils.isNetConnected(LoginActivity.this)) {
            showMyDialog(this, getString(R.string.checkNetWorkAndTry));
        } else {
//            pd.show();
            initProgressDialog(getString(R.string.onLogining));

//            sp.saveUserName(phoneString);
//            sp.savePassword(passwordString);
//            sp.saveIsLogin(true);
//            finish();
//            Intent main = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(main);

            OkHttpUtils.post()
                    .url(SystemUtils.getFullAddressUrl(RequestMethodName.LOGIN))
                    .addParams("username", phoneString)
                    .addParams("passwd", passwordString)
                    .addParams("flag", "1")
                    //6.0会有权限限制,需要动态申请权限，暂且先不获取设备id
//                    .addParams("deviceid", SystemUtils.getDeviceId(this))
                    .addParams("deviceid", "")

                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            //showToast(userBean.msg);
                            dismissDialog();
                            Toast.makeText(LoginActivity.this, userBean.msg, Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            Log.i("OkHttpUtils", "login" + s);
                            dismissDialog();
                            userBean = Response.parseLoginResult(s);
                            if (userBean.result) {
                                // BEApplication.currentUser = userBean.user;
                                sp.saveIsLogin(true);
                                sp.savePassword(passwordString);
                                sp.saveId(userBean.user.getId());
                                sp.saveNickName(userBean.user.getNickname());
                                sp.saveTotalMileage(userBean.user.getTotalmileage());
                                sp.saveUserImage(userBean.user.getImage());
                                sp.saveUserName(userBean.user.getUsername());
                                sp.saveSpeed(userBean.user.getSpeed());
                                sp.saveDeviceName(userBean.user.getDevicename());
                                //changeActivity(MainActivity.class);
                                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(main);

                            } else {
                                //showToast(userBean.msg);
                                Toast.makeText(LoginActivity.this, userBean.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    /**
     * 联网对话框
     */
    public Dialog pdD;

    /**
     * 初始化联网进度条对象
     */
    public void initProgressDialog(String info) {
        try {
            if (pdD == null) {
                pdD = new Dialog(this, R.style.customDialog);
                LayoutInflater inflater = LayoutInflater.from(this);
                View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
                TextView content = (TextView) v.findViewById(R.id.textViewContent);
                content.setText(info);
                pdD.setContentView(v);
                pdD.show();
                pdD.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {

                    }
                });
            } else {
                pdD.show();
            }
        } catch (Exception e) {
        }
    }

    /**
     * dismiss联网进度条
     */
    public void dismissDialog() {
        if (pdD != null) {
            pdD.dismiss();
            pdD = null;
        }
    }


//		//更新设备接口
//		private void UpdateDevice(final int id) {
//			// TODO Auto-generated method stub
//			 new Thread(){
//	  			public void run() {		
//	  		   RequestParams params = new RequestParams();
//	  			params.addBodyParameter("deviceid", sp.getDeviceid());
//	  			params.addBodyParameter("channelid", sp.getChannelid());
//	  			params.addBodyParameter("osType", "1");
//	  			params.addBodyParameter("flag", "7");
//	  			params.addBodyParameter("uid", id+"");
//	  			Log.i("com.mh.fjkj", "deviceid22="+sp.getDeviceid());
//	  			Log.i("com.mh.fjkj", "channelid22="+sp.getChannelid());
//	  			Log.i("com.mh.fjkj", "uid22="+id);
////	  			params.addBodyParameter("deviceid", getDeviceId());
////	  			params.addBodyParameter("channelid", sp.getChannelid());
////	  			params.addBodyParameter("osType", "1");
//	  			HttpUtils utils = new HttpUtils();
//	  			utils.configSoTimeout(30000);
//	  			utils.send(HttpRequest.HttpMethod.POST, NetField.VIPUSER_IP, params, 
//	  					new RequestCallBack<String>() {
//
//								@Override
//								public void onFailure(HttpException arg0,
//										String arg1) {
//									// TODO Auto-generated method stub
//									Toast.makeText(LoginActivity.this, "连接超时，请检查网络设置或稍后再试！", Toast.LENGTH_SHORT).show();
//								}
//
//								@Override
//								public void onSuccess(ResponseInfo<String> arg0) {
//									// TODO Auto-generated method stub
//									    String errorString=JsonUtils.getMessage(arg0.result);
//										String resultCode=JsonUtils.getResultCode(arg0.result);
//										Log.i("com.mh.fjkj", "arg0="+arg0.result);
//										if(resultCode.equals("1")){
//											
////											Toast.makeText(LoginActivity.this, "绑定设备成功！", Toast.LENGTH_SHORT).show();
//											
//										}else {
//											Toast.makeText(LoginActivity.this, errorString, Toast.LENGTH_SHORT).show();
//										}
//								}
//
//							
//	  				
//							});
//	  			}
//	     	 }.start();
//		}


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == remberBox) {
            sp.saveIsRemeber(isChecked);
        }
    }

    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()���ۺ�ʱ���ã��϶�����2000
            {
//                Toast.makeText(getApplicationContext(), getString(R.string.zayc),
//                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}


