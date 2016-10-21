package com.mitu.carrecorder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.SpHelper;

public class BootActivity extends Activity {
	private boolean first=true;  //判断是否首次进入
	private Button btn;
	private Animation animation; //动画效果
	private SpHelper sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot);
//		sp=new SpHelper(BootActivity.this);
		into();
	}

	private void into() {
		
			btn=(Button) findViewById(R.id.btn_start);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
//					sp.setFirst(false);
					startActivity(new Intent(BootActivity.this, LoginActivity.class));
					BootActivity.this.finish();
					
				}
	
	});
}
}