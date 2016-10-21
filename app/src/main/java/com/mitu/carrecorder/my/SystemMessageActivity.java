package com.mitu.carrecorder.my;


import android.os.Bundle;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

public class SystemMessageActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xtmessage);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.systemMessage));
		initTitleBack();
		
	}
}
