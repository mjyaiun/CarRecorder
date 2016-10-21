package com.mitu.carrecorder.set;


import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.CheckSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

import butterknife.Bind;

public class CarStopMonitorSettingActivity extends BaseActivity {

	@Bind(R.id.carstop_lv)
	ListView listView;
	private String[] itemStrs;
	private ArrayList<BEMenuItem> arrayList;
	private CheckSettingListviewAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_stop_monitor_setting);
	}

	@Override
	public void initView() {
		
		setTitle(getString(R.string.carStopMonitor));
		initTitleBack();
		//
		itemStrs = getResources().getStringArray(R.array.carStopMonitorArray);
		arrayList = new ArrayList<>();
		for (int i = 0; i < itemStrs.length; i++) {
			BEMenuItem item = new BEMenuItem(itemStrs[i]);
			arrayList.add(item);
		}
		//
		adapter = new CheckSettingListviewAdapter(this,arrayList);
		listView.setAdapter(adapter);
		adapter.setOnItemToggleButtonClickListener(new CheckSettingListviewAdapter.OnItemToggleButtonClickListener() {
			@Override
			public void onButtonClick(Context context, int position) {

			}
		});
		listView.setSelection(0);
		
	}

}
