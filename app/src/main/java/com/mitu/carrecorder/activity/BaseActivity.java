package com.mitu.carrecorder.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.view.CustomDialog;

import butterknife.ButterKnife;

/**
 *基础类
 * @author Administrator
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {

	private View contentView;
	private LinearLayout ll_content;


	/**资源对象*/
	public Resources res;

	//@Bind(R.id.toolbar_title)
	TextView titleTextview;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		//setContentView(R.layout.action_bar);
		setContentView(R.layout.app_title);
		//
		ll_content = (LinearLayout) findViewById(R.id.contentll);

		res = this.getResources();
	}


	//设置标题
	public void setTitle(String title) {
		titleTextview = (TextView) findViewById(R.id.toolbar_title);
		titleTextview.setVisibility(View.VISIBLE);
		titleTextview.setText(title);
	}


	/**标题右侧*/
	TextView rightTextview;
	/**
	 * 设置标题栏右侧文字
	 * @param text
	 */
	public void setRightTitleText(String text){
		rightTextview = (TextView)findViewById(R.id.toobar_right_text);
		rightTextview.setVisibility(View.VISIBLE);
		if(rightTextview != null){
			rightTextview.setText(text);
			rightTextview.setOnClickListener(this);
		}
	}

	ImageView share;
	public void showToolBarShare(){
		share = (ImageView)findViewById(R.id.toolbar_share_img);
		share.setVisibility(View.VISIBLE);
		share.setOnClickListener(this);
	}

	/*//
	public void setRightTitleText(int text,int id){
		setRightTitleTextId(id);
		setRightTitleText(res.getString(text));
	}

	private int rightTextviewId;

	public void setRightTitleTextId(int id){
		rightTextviewId = id;
	}


	public int getRightTitleTextId(){
		return rightTextviewId;
	}*/

	Toolbar toolbar;
	/**
	 * 显示标题栏返回按钮，并注册事件
	 */
	protected void initTitleBack(){
		toolbar = (Toolbar) findViewById(R.id.id_toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		toolbar.setNavigationIcon(R.drawable.back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				return;
			}
		});
	}

	/*//完成按钮显示
	public void completeVisibility(){
		btnComplete.setVisibility(View.VISIBLE);
	}
	public void showButton(String text){
		btnComplete.setVisibility(View.VISIBLE);
		btnComplete.setText(text);
	}*/

	/**
	 * 切换Activity
	 * @param c 需要切换到的Activity
	 */
	public void changeActivity(Class<?> c){
		Intent intent = new Intent(this,c);
		this.startActivity(intent);
	}

	public void changeActivity(Class<?> c ,Bundle bundle){
		Intent intent = new Intent(this,c);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}


	public void changeActivityForResult(Class<?> c ,Bundle bundle,int requestCode){
		Intent intent = new Intent(this,c);
		intent.putExtras(bundle);
		this.startActivityForResult(intent,requestCode);
	}

	/***
	 * 带返回结果的跳转
	 * @param c
	 * @param requestCode
	 */
	public void changeActivityForResult(Class<?> c,int requestCode){
		Intent intent = new Intent(this,c);
		this.startActivityForResult(intent,requestCode);
	}



	/**
	 * 切换Activity
	 * @param c 需要切换到的Activity
	 */
	public void changeActivity(Class<?> c,String name,int type){
		Intent intent = new Intent(this,c);
		intent.putExtra(name,type);
		this.startActivity(intent);
	}

	public void changeActivity(Class<?> c,String name,String value){
		Intent intent = new Intent(this,c);
		intent.putExtra(name,value);
		this.startActivity(intent);
	}

	/**
	 * 提示对话框
	 *//*
	public void showPromoteDialog(Context context,String message) {
		// TODO Auto-generated method stub
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(getString(R.string.deviceSureToDelete));
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//showToast("删除设备");
				dialog.dismiss();
			}
		});

		*//*builder.setNegativeButton(getString(R.string.cancel),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//mWifiManager.setWifiEnabled(false);
						dialog.dismiss();
					}
				});*//*
		builder.create().show();
	}*/




	/**联网对话框*/
	public Dialog pd;
	/**
	 * 初始化联网进度条对象
	 */
	public void initProgressDialog(String info){
		try{
			if(pd == null){
				pd = new Dialog(this,R.style.customDialog);
				LayoutInflater inflater = LayoutInflater.from(this);
				View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
				TextView content = (TextView)v.findViewById(R.id.textViewContent);
				content.setText(info);
				pd.setContentView(v);
				pd.show();
				pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
//						isResponseData = true;
					}
				});
			}else{
				pd.show();
			}
		}catch(Exception e){}
	}

	/**
	 * dismiss联网进度条
	 */
	public void dismissDialog(){
		if(pd != null){
			pd.dismiss();
			pd = null;
		}
	}

	/*public void showProgressDialog(Context context,String message){
		pd = new DefineProgressDialog(context,message);
		pd.show();
	}



    //关闭dialog
	public void cancelDialog(){
		pd.cancel();
	}*/
	

		
	/*//分享图标的可见性
	public void shareVisibility(){	
		ivShare.setVisibility(View.VISIBLE);
	}
*/

	/**
	 * 显示自定义提示对话框
	 * @param context
	 * @param message
     */
	public void showMyDialog(final Context context, String message){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(message);  
        builder.setTitle(getString(R.string.prompt));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); 
            }  
        });  
        builder.create().show(); 
	}

	public void showMyDialog(final Context context, String message,String positiveString){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	public void showMyDialog(final Context context, String message,String positiveString,String navigationString){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.setNegativeButton(navigationString, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void showMyDialog(final Context context, String message, String positiveString, String navigationString, final OnMyDialogPositiveButtonClickListener listener1){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				listener1.onMyPositiveButtonClick(context);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(navigationString, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public void showMyDialog(final Context context, String message, final OnMyDialogPositiveButtonClickListener listener){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(getString(R.string.prompt));
		builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				listener.onMyPositiveButtonClick(context);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	//private OnMyDialogPositiveButtonClickListener listener;

	@Override
	public void onClick(View v) {

	}

	public static interface OnMyDialogPositiveButtonClickListener{
		void onMyPositiveButtonClick(Context context);
	}


	/*public void setOnMyPositiveButtonClickListener(OnMyDialogPositiveButtonClickListener listener){
		this.listener = listener;
	}*/


	//取消软键盘

	public void CancelSoftKeyBord(View view){
		InputMethodManager imm = (InputMethodManager)  
		getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  
	}


	/*//判断手机号
	public  boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11)
				&& isMobileNO(phoneNums)) {
			return true;
		}
		return false;
	}
	
	//判断是否是手机号
		private  boolean isMobileNO(String phoneNums) {

			String telRegex = "[1][3587]\\d{9}";
			if (TextUtils.isEmpty(phoneNums))
				return false;
			else
				return phoneNums.matches(telRegex);
		}
	*//**
	 * 判断手机号码长度
	 * @param phoneNums
	 * @param length
	 * @return
	 *//*
	private  boolean isMatchLength(String phoneNums, int length) {
		
		if (TextUtils.isEmpty(phoneNums)) {
			return false;
		} else {
			return phoneNums.length() == length ? true : false;
		}
	}*/




	/**
	 * 获得屏幕宽度
	 */
	private void getDimention(){
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;

		BEApplication.width = width;
		BEApplication.height = height;
		BEApplication.dx = metric.density;
	}

	/**
	 * 显示toast提示
	 * @param info 信息内容
	 */
	public void showToast(String info){
		Toast.makeText(this, info, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示toast提示
	 * @param infoId 信息内容
	 */
	public void showToast(int infoId){
		Toast.makeText(this, infoId, Toast.LENGTH_LONG).show();
	}
	
	public SpHelper sp;
	
	/**
	 * 初始化view
	 */
	public abstract void initView();
		
	@Override
	public void setContentView(int layoutResID) {
		sp = new SpHelper(this);
		getDimention();

		//
		if (layoutResID == R.layout.app_title) {
			super.setContentView(layoutResID);
		} else {
			contentView = getLayoutInflater().inflate(layoutResID, null);
			contentView.setLayoutParams(ll_content.getLayoutParams());
			ll_content.addView(contentView);

			ButterKnife.bind(this);
//
			initView();
		}
	}
}
