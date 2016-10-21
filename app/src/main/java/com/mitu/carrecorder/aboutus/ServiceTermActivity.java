package com.mitu.carrecorder.aboutus;

import android.os.Bundle;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

/**
 * 服务条款
 * @author Administrator
 *
 */
public class ServiceTermActivity extends BaseActivity {
     private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_term);
	}

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		setTitle(getString(R.string.fwtk));
		initTitleBack();
		tv=(TextView) findViewById(R.id.tv_item);
	}

}
