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

public class TakePhotoResolutionSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{


	@Bind(R.id.photo_resolution_tv)
	ListView valueListview;

	private CheckSettingListviewAdapter adapter;
	private ArrayList<BEMenuItem> arrayList;
	private String[] items;


	public static final  int RESULT_OK = 111;
	private int photoReType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo_resolution);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.takePhoteResolution));
		initTitleBack();
		items = getResources().getStringArray(R.array.takephotoResolutionArray);

		photoReType = getIntent().getIntExtra("type",0);
		arrayList = new ArrayList<>();
		for (int i = 0; i < items.length; i++) {
			BEMenuItem item = new BEMenuItem(items[i]);
			if (i == photoReType){
				item.isCheck = true;
			}else {
				item.isCheck = false;
			}
			arrayList.add(item);
		}
		adapter = new CheckSettingListviewAdapter(this,arrayList);
		valueListview.setAdapter(adapter);
		valueListview.setOnItemClickListener(this);

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == valueListview){

			for (int i = 0; i < arrayList.size(); i++) {
				if (i == position){
					/*DeviceSettingsCache.setTakePhotoResolution(position);*/
					arrayList.get(i).isCheck = true;
				}else {
					arrayList.get(i).isCheck = false;
				}
			}
			adapter.notifyDataSetChanged();
			Intent intent = new Intent();
			intent.putExtra("type", position);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
