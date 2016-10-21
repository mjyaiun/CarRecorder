package com.mitu.carrecorder.set;


import android.os.Bundle;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

import butterknife.Bind;

public class AccountInfoActivity extends BaseActivity {


	@Bind(R.id.total_memory_tv)
	TextView totalMemoryTv;
	@Bind(R.id.surplus_memory_tv)
	TextView surplusMemoryTv;
	@Bind(R.id.number_tv)
	TextView numberTv;
	@Bind(R.id.model_number_tv)
	TextView modelNumberTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info);
	}

	@Override
	public void initView() {
		setTitle(getString(R.string.accountInfo));
		initTitleBack();
		initContent();
	}

	private void initContent() {
		totalMemoryTv.setText("0");
		surplusMemoryTv.setText("0");
		numberTv.setText("0");
		modelNumberTv.setText("0");
	}


}
