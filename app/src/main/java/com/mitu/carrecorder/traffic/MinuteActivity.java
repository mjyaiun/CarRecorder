package com.mitu.carrecorder.traffic;


import android.os.Bundle;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

public class MinuteActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_minute);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.trafficIllegalQuery));
		initTitleBack();
		
	}

}
