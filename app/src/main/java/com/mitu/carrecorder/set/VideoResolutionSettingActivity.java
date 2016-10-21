package com.mitu.carrecorder.set;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.CheckSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoResolutionSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	@Bind(R.id.video_resolution_lv)
	ListView listView;
	private String[] itemStrs;
	private ArrayList<BEMenuItem> arrayList;
	private CheckSettingListviewAdapter adapter;

	public static final  int RESULT_OK = 111;

	private int videoRe  = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_resolution_setting);
		//
		ButterKnife.bind(this);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.videoResolution));
		initTitleBack();
		//
		itemStrs = getResources().getStringArray(R.array.videoResoluationArray);
		videoRe = getIntent().getIntExtra("type",0);
		arrayList = new ArrayList<>();
		for (int i = 0; i < itemStrs.length; i++) {
			BEMenuItem item = new BEMenuItem(itemStrs[i]);
			if (i == videoRe){
				item.isCheck = true;
			}else {
				item.isCheck = false;
			}
			arrayList.add(item);
		}
		//
		adapter = new CheckSettingListviewAdapter(this,arrayList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == listView){
			for (int i = 0; i < arrayList.size(); i++) {
				if (i == position){
					//DeviceSettingsCache.setVideoResolution(position);
					arrayList.get(i).isCheck = true;
				}else {
					arrayList.get(i).isCheck = false;
				}
			}
			adapter.notifyDataSetChanged();
			Intent intent = new Intent();
			intent.putExtra("type",position);
			setResult(RESULT_OK, intent);
			finish();
		}

	}
}
