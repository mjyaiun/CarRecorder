package com.mitu.carrecorder.set;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.CheckSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.utils.DeviceSettingsCache;

import java.util.ArrayList;

import butterknife.Bind;

public class LanguageSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {
	//private RelativeLayout rlenglishi, rlchinese;

	@Bind(R.id.language_setting_lv)
	ListView langageListview;

	private int currentType;
	private String[] types;
	private ArrayList<BEMenuItem> arrayList;
	private CheckSettingListviewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.language));
		initTitleBack();
		types = getResources().getStringArray(R.array.languageArray);
		currentType = DeviceSettingsCache.getAppLanguageType();
		arrayList = new ArrayList<>();
		initData();
		adapter = new CheckSettingListviewAdapter(this,arrayList);
		langageListview.setAdapter(adapter);
		langageListview.setOnItemClickListener(this);
		/*adapter.setOnItemToggleButtonClickListener(new LanguageSettingListviewAdapter.OnItemToggleButtonClickListener() {
			@Override
			public void onButtonClick(Context context, int position) {
				DeviceSettingsCache.setAppLanguage(position);
				initData();
				adapter.notifyDataSetChanged();
			}
		});*/
//		rlchinese = (RelativeLayout) findViewById(R.id.rl_setting_chinese);
//		rlenglishi = (RelativeLayout) findViewById(R.id.rl_my_english);
//		rlchinese.setOnClickListener(this);
//		rlenglishi.setOnClickListener(this);
	}

	private void initData(){
		arrayList.clear();
		currentType = DeviceSettingsCache.getAppLanguageType();
		for (int i = 0; i < types.length; i++) {
			BEMenuItem item = new BEMenuItem();
			item.name = types[i];
			if (currentType == i){
				item.isCheck = true;
			}else{
				item.isCheck = false;
			}
			arrayList.add(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == langageListview){

			for (int i = 0; i < arrayList.size(); i++) {
				if (i == position){
					arrayList.get(i).isCheck = true;
					DeviceSettingsCache.setAppLanguage(position);
				}else{
					arrayList.get(i).isCheck = false;
				}
			}
			adapter.notifyDataSetChanged();
		}
	}
}
