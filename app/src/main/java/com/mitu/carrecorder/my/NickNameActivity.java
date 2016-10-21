package com.mitu.carrecorder.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.utils.Validates;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 修改昵称
 *
 * @author Administrator
 */
public class NickNameActivity extends BaseActivity implements OnClickListener {

    private EditText etUserName;
    private LinearLayout softLayout;

    @Bind(R.id.save_modify_btn)
    Button modifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        setTitle(getString(R.string.modifyNickName));
        initTitleBack();

//        showButton(getString(R.string.complate));
//        btnComplete.setOnClickListener(this);
        softLayout = (LinearLayout) findViewById(R.id.ll_header);
        softLayout.setOnClickListener(this);
        etUserName = (EditText) findViewById(R.id.et_username);
        etUserName.setText(sp.getNickName());
        //
        modifyBtn.setOnClickListener(this);
        //etUserNameSet();
    }

    private void etUserNameSet() {
        // TODO Auto-generated method stub
        etUserName.setFocusableInTouchMode(true);
        etUserName.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) etUserName.getContext().getSystemService(NickNameActivity.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etUserName, 0);
            }
        }, 998);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.ll_header:
                CancelSoftKeyBord(view);
                break;
            case R.id.save_modify_btn:
                String nickName = etUserName.getText().toString().trim();

                if (TextUtils.isEmpty(nickName)) {
                    showMyDialog(this,getString(R.string.nickNameNotAllowEmpty));
                    return;
                }
                if (!Validates.validateYHNC(nickName)){
                    showMyDialog(this,getString(R.string.inputStanderNickName));
                    return;
                }
                if (!sp.getNickName().equals(nickName)) {
                    doModifyNickName(nickName);
                    //finish();
                }
                break;

            default:
                break;
        }
    }



    private UserBean userBean;

    private void doModifyNickName(final String nickName) {
        initProgressDialog(getString(R.string.onSaving));
        OkHttpUtils.post()
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.LOGIN))
                .addParams("flag", "3")
                .addParams("nickname",nickName)
                .addParams("userid", sp.getId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.i("OkHttpUtils",""+s);
                        dismissDialog();
                        userBean = Response.parseLoginResult(s);
                        if (userBean.result) {
                            sp.saveNickName(userBean.user.getNickname());
                            showToast(getString(R.string.modifySuccess));
                        }else{
                            showToast(userBean.msg);
                        }
                    }
                });
         /*new Thread(){
				public void run() {
					RequestParams params=new RequestParams();
					params.addBodyParameter("flag", "3");
					params.addBodyParameter("nickname",nickName);					
					params.addBodyParameter("userid", sp.getId()+"");
					HttpUtils utils = new HttpUtils();
	     			utils.configSoTimeout(60000);
	     			utils.send(HttpRequest.HttpMethod.POST, NetField.VIPUSER_IP, params,
	     					new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
								Toast.makeText(NickNameActivity.this, getString(R.string.qjcwl), Toast.LENGTH_SHORT).show();	
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									cancelDialog();
									 String errorString= JsonUtils.getMessage(arg0.result);
										String resultCode=JsonUtils.getResultCode(arg0.result);
										Log.d("com.mh.fjkj.menu", "fanhui1="+arg0.result);
										if(resultCode.equals("1")){
										  Toast.makeText(NickNameActivity.this, getString(R.string.gxnxgcg), Toast.LENGTH_SHORT).show();
											
											Intent intent=getIntent();
											intent.putExtra("nickname",nickName);sp.saveNickName(nickName);
											NickNameActivity.this.setResult(REQUEST_CODE_NICKNAME, intent);
									
											Intent receiverIntent=new Intent();
											receiverIntent.putExtra("nickname", etUserName.getText().toString());
											receiverIntent.setAction("com.mh.fjkj.menu.action.nickname");
											sendBroadcast(receiverIntent);
										  Log.i("com.mh.carcorder.myNick", "intent");
										  Log.i("com.mh.carcorder.myNick", "nickName");
										  finish();
									  }else {
										  Toast.makeText(NickNameActivity.this, errorString, Toast.LENGTH_SHORT).show();
									}
									 
								}
	     				
	     			});
	     			}

				
			}.start();*/
    }


}
