package com.mitu.carrecorder.set;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.SettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * 设置
 * @author Administrator
 * 
 */
public class SettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{

	/*private RelativeLayout gnLayout, timeLayout, wifiLayout, zhLayout,
			yuLayout, ccLayout, gjLayout;*/

	@Bind(R.id.normal_setting_lv)
	ListView settingListview;

	private String[] settingItems ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//
	}


	private ArrayList<BEMenuItem> items;
	private SettingListviewAdapter adapter;

	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.setting));
		initTitleBack();
		settingItems = this.getResources().getStringArray(R.array.settingItemArray);
		items = new ArrayList<>();
		for (int i = 0; i < settingItems.length; i++) {
			BEMenuItem item = new BEMenuItem(settingItems[i]);
			items.add(item);
		}
		adapter = new SettingListviewAdapter(this,items);
		settingListview.setAdapter(adapter);
		settingListview.setOnItemClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == settingListview){
			switch (position){
				case 0:
					changeActivity(RecorderSettingActivity.class);
					break;
				case 1:
					changeActivity(LanguageSettingActivity.class);
					break;
				case 2:
					break;
			}
		}
	}

	//
}
