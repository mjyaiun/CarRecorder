package com.mitu.carrecorder.tracking;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.utils.SpHelper;

/**
 * 处理跟车申请
 * 
 * @author Administrator
 * 
 */
public class DisposeTrackActivity extends BaseActivity implements OnClickListener {

	private ImageView ivClose;
	private TextView tvDisposeName, tvfromphone, tvmess;
	private Button btnOk, btnNo;
	private SpHelper sp;
	private String status, fromphoner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispose_track);
		// fromuser=getIntent().getStringExtra("from");
		initView();
		// description=getIntent().getStringExtra("description");
		// int length=description.length();
		// fromphone=description.substring(length-15, length-4);
		// if(length>15){
		// nickname=description.substring(0, length-15);
		// }else {
		// nickname="";
		// }
		// tvDisposeName.setText(nickname+"  "+fromphone);
	}


	@Override
	public void initView() {
		//ivClose = (ImageView) findViewById(R.id.iv_dispose_close);
		tvmess = (TextView) findViewById(R.id.tv_register);
		tvmess.setVisibility(View.VISIBLE);
		tvfromphone = (TextView) findViewById(R.id.tv_dispose_phone);
		fromphoner = tvfromphone.getText().toString().trim();
		tvDisposeName = (TextView) findViewById(R.id.tv_dispose_name);
		btnOk = (Button) findViewById(R.id.btn_dispose_ok);
		btnNo = (Button) findViewById(R.id.btn_disope_no);
		ivClose.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnNo.setOnClickListener(this);
		sp = new SpHelper(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		/*case R.id.iv_dispose_close:
			finish();
			break;*/
		case R.id.btn_dispose_ok:
			status = "1";
			doDispose(status);
			//
			// if(nickname.equals("")){
			// nickname=fromphone;
			// }
			//
			// TrackingDialog.Builder builder = new
			// TrackingDialog.Builder(DisposeTrackActivity.this);
			// builder.setMessage(nickname+"向你发起跟车申请!");
			// builder.setTitle("提示");
			// builder.setPositiveButton("去看看", new
			// DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			//
			// // Intent sendIntent=new Intent();
			// // sendIntent.putExtra("socketId", socketId);
			// // sendIntent.setAction("com.mh.fjkj.home.action.agree");
			// // sendBroadcast(sendIntent);
			// startActivity(new Intent(DisposeTrackActivity.this,
			// DisGpsActivity.class));
			// dialog.dismiss();
			// }
			// });
			// builder.create().show();
			break;
		case R.id.btn_disope_no:
			status = "0";
			doDispose(status);
			finish();
			break;
		default:
			break;
		}
	}

	// 同意或拒绝接口
	private void doDispose(final String status) {
		// TODO Auto-generated method stub
		/*new Thread() {
			public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("flag", "1");
				params.addBodyParameter("fromusername", fromphoner);
				params.addBodyParameter("tousername", sp.getUserName());
				params.addBodyParameter("status", status);
				HttpUtils utils = new HttpUtils();
				utils.configSoTimeout(30000);
				utils.send(HttpRequest.HttpMethod.POST, NetField.TRACKING_IP,
						params, new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(DisposeTrackActivity.this,
										"连接超时，请检查网络设置或稍后再试！",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								// TODO Auto-generated method stub
								String errorString = JsonUtils
										.getMessage(arg0.result);
								String resultCode = JsonUtils
										.getResultCode(arg0.result);
								Log.i("com.mh.fjkj", "arg0=" + arg0.result);
								if (resultCode.equals("1")) {
									if (status.equals("1")) {
										startActivity(new Intent(
												DisposeTrackActivity.this,
												TrackingActivity.class));
										tvmess.setVisibility(View.GONE);
									} else {
										Toast.makeText(
												DisposeTrackActivity.this,
												"您拒绝了对方的申请！",
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(DisposeTrackActivity.this,
											errorString, Toast.LENGTH_SHORT)
											.show();
								}
							}

						});
			}
		}.start();*/
	}
}
