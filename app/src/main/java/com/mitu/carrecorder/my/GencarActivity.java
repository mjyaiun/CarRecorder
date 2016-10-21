package com.mitu.carrecorder.my;


import android.os.Bundle;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

public class GencarActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gencar);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.followMessage));
		initTitleBack();
		
	}
}
