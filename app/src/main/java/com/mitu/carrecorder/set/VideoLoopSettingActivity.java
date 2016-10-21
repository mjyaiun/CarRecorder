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

public class VideoLoopSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{

	@Bind(R.id.loop_set_lv)
	ListView loopSetListview;

	private String[] itemStrs;
	private ArrayList<BEMenuItem> arrayList;
	private CheckSettingListviewAdapter adapter;

	public static final  int RESULT_OK = 113;

	private int loopType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_loop_setting);
		//
		ButterKnife.bind(this);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.loopVideo));
		initTitleBack();
		itemStrs = getResources().getStringArray(R.array.loopArray);
		loopType = getIntent().getIntExtra("type",0);
		arrayList = new ArrayList<>();
		for (int i = 0; i < itemStrs.length; i++) {
			BEMenuItem item = new BEMenuItem(itemStrs[i]);
			if (i == loopType){
				item.isCheck = true;
			}else {
				item.isCheck = false;
			}
			arrayList.add(item);
		}
		//
		adapter = new CheckSettingListviewAdapter(this,arrayList);
		loopSetListview.setAdapter(adapter);
		loopSetListview.setOnItemClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == loopSetListview){
			for (int i = 0; i < arrayList.size(); i++) {
				if (i == position){
					//DeviceSettingsCache.setVideoLoop(position);
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
