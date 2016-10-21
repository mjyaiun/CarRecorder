package com.mitu.carrecorder.my;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.aboutus.AboutActivity;
import com.mitu.carrecorder.activity.MainActivity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.obd.ObdCarStateActivity;
import com.mitu.carrecorder.phvedio.LocalPhotoVideoActivity;
import com.mitu.carrecorder.set.SettingActivity;
import com.mitu.carrecorder.trackingtu.MileageActivity;
import com.mitu.carrecorder.traffic.TrafficIllegalQueryActivity;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.view.CustomDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 个人设置
 * 
 * @author Administrator
 * 
 */
public class MineActivity extends AppCompatActivity implements OnClickListener {

	private SimpleDraweeView ivPhoto;
	private ImageView  ivBack;
	private LinearLayout pLayout;
	private RelativeLayout rlmyset, rlmyus, rlphvideo;
	private TextView message,nickName;
	private RadioButton rbweizhang, rbcarstate, rbcartrak, rbmessage;
	private SpHelper sp;
	private Button Logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		sp = new SpHelper(MineActivity.this);
		ButterKnife.bind(this);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//
		ivPhoto.setImageURI(Uri.parse(sp.getUserImage()));
		if (!sp.getIsLogin()){
			nickName.setText(getString(R.string.tourist));
		}else {
			nickName.setText(sp.getUserName());
		}

		//}
	}

	public void CancelSoftKeyBord(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}


	@Bind(R.id.tv_pserson_dis)
	TextView totalMilTv;
	//
	public void initView() {
		sp = new SpHelper(this);
		rbcarstate = (RadioButton) findViewById(R.id.rb_mycarstate);
		rbcartrak = (RadioButton) findViewById(R.id.rb_mycartrak);
		rbweizhang = (RadioButton) findViewById(R.id.rb_myweizhang);
		rbmessage = (RadioButton) findViewById(R.id.rb_mymessage);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivPhoto = (SimpleDraweeView) findViewById(R.id.iv_photo);
		ivPhoto.setImageURI(Uri.parse(sp.getUserImage()));
		if (sp.getTotalMileage().equals("")){
			totalMilTv.setText(0+"km");

		}else {
			totalMilTv.setText(sp.getTotalMileage()+ "km");
		}
		//
		ivBack.setOnClickListener(this);
		rlmyus = (RelativeLayout) findViewById(R.id.rl_my_us);
		pLayout = (LinearLayout) findViewById(R.id.ll_person);
		message = (TextView) findViewById(R.id.title_manage_tv);
		nickName = (TextView) findViewById(R.id.tv_pserson_name);
		if (!sp.getIsLogin()){
			nickName.setText(getString(R.string.tourist));
		}else {
			nickName.setText(sp.getUserName());
		}
		rlphvideo = (RelativeLayout) findViewById(R.id.rl_my_phv);
		Logout = (Button) findViewById(R.id.rl_my_logout);
		rlmyset = (RelativeLayout) findViewById(R.id.rl_my_set);
		rbmessage.setOnClickListener(this);
		Logout.setOnClickListener(this);
		rbcarstate.setOnClickListener(this);
		rlmyset.setOnClickListener(this);
		rlphvideo.setOnClickListener(this);
		rbcartrak.setOnClickListener(this);
		rbweizhang.setOnClickListener(this);
		rlmyus.setOnClickListener(this);
		message.setOnClickListener(this);
		pLayout.setOnClickListener(this);
	}



	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rb_mymessage:
			//消息
			startActivity(new Intent(this, MessageActivity.class));
			break;
		case R.id.rl_my_logout:
			//退出登陆
			showLoginOutDialog();
			break;
		case R.id.rl_my_set:
			//设置
			doTestConnect(CHANGE_TO_SETTING);
//			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.rl_my_phv:
			//显示本地下载的图片和视频
			startActivity(new Intent(this, LocalPhotoVideoActivity.class));
			break;
		case R.id.rl_my_us:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.rb_mycartrak:
			startActivity(new Intent(this, MileageActivity.class));
			break;
		case R.id.rb_myweizhang:
			startActivity(new Intent(this, TrafficIllegalQueryActivity.class));
			break;
		case R.id.rb_mycarstate:
			startActivity(new Intent(this, ObdCarStateActivity.class));
			break;
		case R.id.title_manage_tv:
			startActivity(new Intent(this, PersonalInfoActivity.class));
			break;
		default:
			break;
		}
	}

	private boolean isWifiResponse = false;
	/**
	 * 网络请求测试是否连接设备WIFI
	 * @param value
	 */
	private void doTestConnect(int value) {
		startTimeOutThread(value);
		OkHttpUtils.get()
				.url(BEConstants.ADDRESS_IP_DEVICE)
				.addParams("custom","1")
				.addParams("cmd","3016")
				.build()
				.execute(new CommandCallBack() {
					@Override
					public void onError(Call call, Exception e, int id) {

					}

					@Override
					public void onResponse(String response, int id) {
						if (response.equals("0")){
							isWifiResponse = true;
						}
					}
				});

	}


	private static final int CHANGE_TO_SETTING = 1;


	/**
	 * 启动联网超时处理线程
	 * @param value
	 */
	protected void startTimeOutThread(final int value){
		initProgressDialog(getString(R.string.onLoading));
		new Thread(){
			public void run(){
				Looper.prepare();
				try {
					for (int i = 0; i < 5; i++) {// 5秒的联网超时
						Thread.sleep(1000);
						if (isWifiResponse) {
							dismissDialog();
							if (value == CHANGE_TO_SETTING){
								startActivity(new Intent(MineActivity.this,SettingActivity.class));
							}
							return;
						}
					}
				} catch (Exception e) {
				}

				dismissDialog();
//                Toast.makeText(MainActivity.this,"请连接设备WIFI",Toast.LENGTH_SHORT).show();
				showMyDialog(MineActivity.this,getString(R.string.notConnectedToDeviceWifi));
				Looper.loop();
				Looper.myLooper().quit();
			}
		}.start();
	}


	public void showMyDialog(Context context, String message){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new
						Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**联网对话框*/
	public Dialog pdD;
	/**
	 * 初始化联网进度条对象
	 */
	public void initProgressDialog(String info){
		try{
			if(pdD == null){
				pdD = new Dialog(this,R.style.customDialog);
				LayoutInflater inflater = LayoutInflater.from(this);
				View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
				TextView content = (TextView)v.findViewById(R.id.textViewContent);
				content.setText(info);
				pdD.setContentView(v);
				pdD.show();
				pdD.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {

					}
				});
			}else{
				pdD.show();
			}
		}catch(Exception e){}
	}

	/**
	 * dismiss联网进度条
	 */
	public void dismissDialog(){
		if(pdD != null){
			pdD.dismiss();
			pdD = null;
		}
	}

	private void showLoginOutDialog(){
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle(getString(R.string.prompt));
		builder.setMessage(getString(R.string.sureToLoginOut));
		builder.setPositiveButton(getString(R.string.confirm),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						sp.saveIsLogin(false);
						startActivity(new Intent(MineActivity.this,MainActivity.class));
						finish();

					}
				});

		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}

				});

		builder.create().show();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
