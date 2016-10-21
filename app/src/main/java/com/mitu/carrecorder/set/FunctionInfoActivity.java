package com.mitu.carrecorder.set;


import android.os.Bundle;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

public class FunctionInfoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_info);
	}
	@Override
	public void initView() {
		setTitle(getString(R.string.functionInfo));
		initTitleBack();
		
	}

}
