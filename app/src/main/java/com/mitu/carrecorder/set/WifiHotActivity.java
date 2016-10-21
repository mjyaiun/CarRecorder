package com.mitu.carrecorder.set;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.WifiAdapter;
import com.mitu.carrecorder.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 扫描wifi热点
 * 
 * @author Administrator
 * 
 */
public class WifiHotActivity extends BaseActivity implements
		OnItemClickListener {

	private static final int RESULT_CODE_WIFI = 0x123;
	private ListView lvWifi;
	private WifiAdapter adapter;
	private List<ScanResult> listResult;
	private ScanResult mScanResult;
	// 定义WifiManager对象
	private WifiManager mWifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_hot);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!mWifiManager.isWifiEnabled()) {
			showDialog();
		} else {
			getAllWifiHot();
		}

	}

	private void getAllWifiHot() {
		// TODO Auto-generated method stub
		mWifiManager.startScan();
		// listResult = mWifiManager.getScanResults();
		List<ScanResult> list = mWifiManager.getScanResults();
		Log.i("com.mh.fjkj.menu", list.toString());
		if (list != null) {
			listResult.clear();
			listResult.addAll(list);
			adapter.notifyDataSetChanged();

		} else {
			showMyDialog(this, getString(R.string.notUsableWifi));
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.chooseWIFI));
		initTitleBack();
		lvWifi = (ListView) findViewById(R.id.lv_wifi);
		lvWifi.setOnItemClickListener(this);
		listResult = new ArrayList<ScanResult>();
		adapter = new WifiAdapter(listResult, this);
		lvWifi.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("ssid", listResult.get(position).SSID);
		intent.putExtra("capability", listResult.get(position).capabilities);
		Log.i("com.mh.fjkj.menu", "result=" + listResult.get(position).SSID);
		Log.i("com.mh.fjkj.menu", "cap="
				+ listResult.get(position).capabilities);

		WifiHotActivity.this.setResult(RESULT_CODE_WIFI, intent);
		finish();
	}

	/**
	 * 显示提示框，搜索wifi信息
	 */
	private void showDialog() {
		// TODO Auto-generated method stub
		CustomDialog.Builder builder = new CustomDialog.Builder(
				WifiHotActivity.this);
		builder.setMessage("您的wifi功能暂未打开，确定要打开吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mWifiManager.setWifiEnabled(true);
				getAllWifiHot();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mWifiManager.setWifiEnabled(false);

						dialog.dismiss();
					}
				});
		builder.create().show();
	}

}
