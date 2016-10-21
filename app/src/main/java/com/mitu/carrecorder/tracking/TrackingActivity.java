package com.mitu.carrecorder.tracking;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.view.TrackingDialog;

import java.util.regex.Pattern;

/**
 * 跟车
 *
 * @author Administrator
 */
public class TrackingActivity extends BaseActivity implements OnClickListener {

    private TextView tvWaiting;
    private Button btnTracking;
    private EditText etPhone;
    private SpHelper sp;
    private LinearLayout canLayout, ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

    }

    @Override
    public void initView() {
        initTitleBack();
        setTitle(getString(R.string.applyFollowCar));
        sp = new SpHelper(this);
        ll = (LinearLayout) findViewById(R.id.ll);
        btnTracking = (Button) findViewById(R.id.btn_tracking);
        btnTracking.setOnClickListener(this);
        etPhone = (EditText) findViewById(R.id.et_tracking_phone);

        canLayout = (LinearLayout) findViewById(R.id.rl_tracking);
        canLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_tracking:
                String phoneString = etPhone.getText().toString();
                if (phoneString.trim().equals("")) {
                    showMyDialog(this, "请输入手机号码");
                } else if (!judgePhoneNums(phoneString)) {
                    showMyDialog(this, "手机号码有误，请重新输入");
                }
                if (phoneString.equals(sp.getUserName())) {
                    showMyDialog(this, "手机号码不能是自己的注册手机号码！");
                } else {
                /*new Thread() {
					public void run() {
						RequestParams params = new RequestParams();
						params.addBodyParameter("flag", "2");
						params.addBodyParameter("fromusername",
								sp.getUserName());
						params.addBodyParameter("tousername", etPhone.getText()
								.toString());
						HttpUtils utils = new HttpUtils();
						utils.configSoTimeout(30000);
						utils.send(HttpRequest.HttpMethod.POST,
								NetField.TRACKING_IP, params,
								new RequestCallBack<String>() {

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated method stub
										pd.cancel();
										Toast.makeText(TrackingActivity.this,
												"连接超时，请检查网络设置或稍后再试！",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onSuccess(
											ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										pd.cancel();
										String errorString = JsonUtils
												.getMessage(arg0.result);
										String resultCode = JsonUtils
												.getResultCode(arg0.result);
										Log.i("com.mh.fjkj", "arg0="
												+ arg0.result);
										if (resultCode.equals("1")) {
											ll.setVisibility(View.VISIBLE);
										} else {
											Toast.makeText(
													TrackingActivity.this,
													errorString,
													Toast.LENGTH_SHORT).show();
										}
									}

								});
					}
				}.start();*/
                }
                break;
            case R.id.rl_tracking:
                CancelSoftKeyBord(view);
                break;
            default:
                break;
        }
    }

    // 判断手机号
    public boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }

    // 判断是否是手机号
    private boolean isMobileNO(String phoneNums) {

        String telRegex = "[1][3587]\\d{9}";
        if (TextUtils.isEmpty(phoneNums))
            return false;
        else
            return phoneNums.matches(telRegex);
    }

    /**
     * 判断手机号码长度
     *
     * @param phoneNums
     * @param length
     * @return
     */
    private boolean isMatchLength(String phoneNums, int length) {

        if (TextUtils.isEmpty(phoneNums)) {
            return false;
        } else {
            return phoneNums.length() == length ? true : false;
        }
    }

    protected boolean judgePasswordNums(String pasdNums) {
        // TODO Auto-generated method stub
        if (Pattern.matches("^[0-9a-zA-Z]{6,16}", pasdNums)) {
            return true;
        }
        return false;
    }


    public void showMyDialog(Context context, String message) {
        TrackingDialog.Builder builder = new TrackingDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void CancelSoftKeyBord(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // class SocketHandler extends Handler {
    //
    // @Override
    // public void handleMessage(Message msg) {
    // // TODO Auto-generated method stub
    // super.handleMessage(msg);
    // switch (msg.what) {
    // case 0:
    // try {
    // Log.i("com.jimstin.socketclient", "socketHandler---->>");
    // JSONObject json = new JSONObject((String)msg.obj);
    // //Toast.makeText(MainActivity.this, "�յ�����Ϣ��"+msg,
    // Toast.LENGTH_SHORT).show();
    // if(json.getString("msg").equals("tracking")){
    // Intent intent=new Intent(TrackingActivity.this,
    // DisposeTrackActivity.class);
    // intent.putExtra("from", json.getString("from"));
    // startActivity(intent);
    // }
    //
    // if(json.getString("msg").equals("refuse")){
    // Toast.makeText(TrackingActivity.this, "对方拒绝了您的申请！",
    // Toast.LENGTH_SHORT).show();
    // }
    //
    // if(json.getString("msg").equals("agree")){
    // TrackingDialog.Builder builder = new
    // TrackingDialog.Builder(TrackingActivity.this);
    // builder.setMessage("对方同意了您的跟车申请!");
    // builder.setTitle("提示");
    // builder.setPositiveButton("去看看", new DialogInterface.OnClickListener() {
    // public void onClick(DialogInterface dialog, int which) {
    // tvWaiting.setVisibility(View.GONE);
    // Intent intent=new Intent(TrackingActivity.this, DisGpsActivity.class);
    // startActivity(intent);
    // dialog.dismiss();
    // }
    // });
    // builder.create().show();
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // break;
    //
    // default:
    // break;
    // }
    // }
    // }
    //
    //

}
