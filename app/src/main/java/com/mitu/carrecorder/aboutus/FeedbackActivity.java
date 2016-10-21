package com.mitu.carrecorder.aboutus;

import android.content.Context;
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
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.activity.LoginActivity;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 留言板
 *
 * @author Administrator
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener {
    private Button btnSubmit;
    private EditText etContent;
    private RelativeLayout canLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    public void initView() {
        // TODO Auto-generated method stub
        setTitle(getString(R.string.feedback));
        initTitleBack();
        btnSubmit = (Button) findViewById(R.id.btn_feedback_submit);
        btnSubmit.setOnClickListener(this);
        etContent = (EditText) findViewById(R.id.et_feedback);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            /*case R.id.rl_feedback:
                CancelSoftKeyBord(view);
                break;*/
            case R.id.btn_feedback_submit:
                CancelSoftKeyBord(view);
                doSubmit();
                break;
            /*case R.id.iv_back:
                CancelSoftKeyBord(view);
                finish();
                break;
*/
            default:
                break;
        }

    }

	/*// 取消软键盘

	public void CancelSoftKeyBord(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}*/


//    private boolean isLogin = false;
    private void doSubmit() {
        // TODO Auto-generated method stub
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            showMyDialog(this, getString(R.string.backContentNotAllowEmpty));
        } else if(!sp.getIsLogin()){
            showMyDialog(this, getString(R.string.notLogin), getString(R.string.login), getString(R.string.cancel), new OnMyDialogPositiveButtonClickListener() {
                @Override
                public void onMyPositiveButtonClick(Context context) {
                    changeActivity(LoginActivity.class);
                    //finish();
                }
            });
        }else {
            //pd.show();
            initProgressDialog(getString(R.string.onSubmit));
            OkHttpUtils.post()
                    .url(SystemUtils.getFullAddressUrl(RequestMethodName.FEED_BACK))
                    .addParams("content", etContent.getText().toString())
                    .addParams("username", sp.getUserName())
//                    .addParams("nickname", sp.getNickName())
                    .addParams("nickname", "123")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            dismissDialog();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            dismissDialog();
                            if (s.length() != 0){
                                try {
                                    JSONObject object = new JSONObject(s);
                                    if (object != null){
                                        int resultCode = object.getInt("resultscode");
                                        if (resultCode == 1){
                                            showToast(R.string.feedbackSuccess);
                                        }else if (resultCode == 0){
                                            showToast(R.string.feedbackFailed);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("feedBack", s);

                        }
                    });
			/*new Thread() {
				public void run() {
					RequestParams params = new RequestParams();
					params.addBodyParameter("content", etContent.getText()
							.toString());
					params.addBodyParameter("username", sp.getUserName());
					params.addBodyParameter("nickname", sp.getNickName());
					HttpUtils utils = new HttpUtils();
					utils.configSoTimeout(30000);
					utils.send(HttpRequest.HttpMethod.POST,
							NetField.FEEDBACK_IP, params,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Toast.makeText(FeedbackActivity.this,
											getString(R.string.qjcwl),
											Toast.LENGTH_SHORT).show();
									pd.cancel();

									showMydialog(getString(R.string.scsb));
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									if (JsonUtils.getResultCode(arg0.result)
											.equals("1")) {
										pd.cancel();
										etContent.setText("");										
										showMydialog(getString(R.string.gxntjcg));
									}
								}

							});
				}
			}.start();*/
        }
    }


}
