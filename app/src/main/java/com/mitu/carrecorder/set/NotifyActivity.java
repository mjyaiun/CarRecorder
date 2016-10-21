package com.mitu.carrecorder.set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.view.CycleWheelView;

import java.util.ArrayList;
import java.util.List;


/**
 * 每日运动提醒时间
 * @author Administrator
 *
 */
public class NotifyActivity extends Activity implements OnClickListener {

	private TextView tvCancel,tvSubmit;
	private CycleWheelView hourWheel,fenhaoWheel,miniteWheel;
	private int currentHour,currentMin;
	private Intent  intent;
	private SpHelper sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify);
		sp=new SpHelper(this);
		tvCancel=(TextView) findViewById(R.id.tv_height_cancel);
		tvSubmit=(TextView) findViewById(R.id.tv_height_submit);
		tvCancel.setOnClickListener(this);
		tvSubmit.setOnClickListener(this);
		
		hourWheel = (CycleWheelView) findViewById(R.id.hour);
		fenhaoWheel = (CycleWheelView) findViewById(R.id.fenhao);
		miniteWheel= (CycleWheelView) findViewById(R.id.minite);
        
		
        List<String> labelsHour = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
        	if(i<10){
        		labelsHour.add("0" + i);
        	}else {
        		labelsHour.add("" + i);
			}
           
        }
        hourWheel.setLabels(labelsHour);
        try {
        	hourWheel.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        hourWheel.setCycleEnable(true);
        String hour;
        String minute;
        if(sp.getRuntime().equals("")){
        	hour="06";
        	minute="30";
        }else {
        	hour=sp.getRuntime().substring(0, 2);
        	minute=sp.getRuntime().substring(3, 5);
		}
//        int tHour=0;
//        if(hour.startsWith("0")){
//        	tHour=Integer.parseInt(hour.substring(1));
//        }else {
//			tHour=Integer.parseInt(hour);
//		}
        hourWheel.setSelection(6);
        hourWheel.setAlphaGradual(0.6f);
        hourWheel.setDivider(Color.GRAY, 1);
        hourWheel.setSolid(Color.WHITE,Color.WHITE);
        hourWheel.setLabelColor(Color.GRAY);
        hourWheel.setLabelSelectColor(getResources().getColor(R.color.color_text_gray));
        hourWheel.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
            	currentHour=position;
            }
        });
        
        List<String> labelsFenhao = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
        	labelsFenhao.add(":");  
        }
        fenhaoWheel.setLabels(labelsFenhao);
        try {
        	fenhaoWheel.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        fenhaoWheel.setCycleEnable(true);
        fenhaoWheel.setSelection(12);
        fenhaoWheel.setAlphaGradual(0.6f);
        fenhaoWheel.setDivider(Color.GRAY, 1);
        fenhaoWheel.setSolid(Color.WHITE,Color.WHITE);
        fenhaoWheel.setLabelColor(Color.GRAY);
        fenhaoWheel.setLabelSelectColor(getResources().getColor(R.color.color_text_gray));
        fenhaoWheel.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
             currentHour=position;
            }
        });
        
        
        List<String> labelsFenMinite = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
        	if(i<10){
        		labelsFenMinite.add("0" + i);
        	}else {
        		labelsFenMinite.add("" + i);
			}
           
        }
        miniteWheel.setLabels(labelsFenMinite);
        try {
        	miniteWheel.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        miniteWheel.setCycleEnable(true);
        
        
       
//        int tMinu=0;
//        if(minute.startsWith("0")){
//        	tMinu=Integer.parseInt(minute.substring(1));
//        }else {
//			tMinu=Integer.parseInt(minute);
//		}
        miniteWheel.setSelection(6);
        miniteWheel.setAlphaGradual(0.6f);
        miniteWheel.setDivider(Color.GRAY, 1);
        miniteWheel.setSolid(Color.WHITE,Color.WHITE);
        miniteWheel.setLabelColor(Color.GRAY);
        miniteWheel.setLabelSelectColor(getResources().getColor(R.color.color_text_gray));
        miniteWheel.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
             currentMin=position;
            }
        });
        
        
	}
	
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_height_cancel:
			finish();
			break;
		case R.id.tv_height_submit:
			doN();
//			doSetNofityTime();
		finish();
			break;

		default:
			break;
		}
	}


	private void doN() {
		 new Thread(){
				public void run() {
					final String hour;
					final String minute;
					if(currentHour<10){
						hour="0"+currentHour;
					}else {
						hour=currentHour+"";
					}
					
					if(currentMin<10){
						minute="0"+currentMin;
					}else {
						minute=currentMin+"";
					}
					sp.saveRuntime(hour+":"+minute);
					 Intent intent =new Intent("com.mh.carcorder.set");
					  sendBroadcast(intent); 
				}			}.start();
	}

//   //设置运动提醒时间
//	private void doSetNofityTime() {
//		// TODO Auto-generated method stub
//		Log.i("com.mh.aoyi", "运动提醒时间+"+currentHour+":"+currentMin);
//		 new Thread(){
//				public void run() {
//					RequestParams params=new RequestParams();
//					params.addBodyParameter("flag", "6");
//					final String hour;
//					final String minute;
//					if(currentHour<10){
//						hour="0"+currentHour;
//					}else {
//						hour=currentHour+"";
//					}
//					
//					if(currentMin<10){
//						minute="0"+currentMin;
//					}else {
//						minute=currentMin+"";
//					}
//					params.addBodyParameter("runtime",hour+":"+minute);					
//					params.addBodyParameter("id", sp.getId()+"");
//					HttpUtils utils = new HttpUtils();
//	     			utils.configSoTimeout(60000);
//	     			utils.send(HttpRequest.HttpMethod.POST, NetField.VIPUSER_IP, params, 
//	     					new RequestCallBack<String>() {
//
//								@Override
//								public void onFailure(HttpException arg0,
//										String arg1) {
//									// TODO Auto-generated method stub
//								
//									
//								}
//
//								@Override
//								public void onSuccess(ResponseInfo<String> arg0) {
//									// TODO Auto-generated method stub
//									 
//									  if(JsonUtils.getResultCode(arg0.result).equals("1")){
//										  
//										  sp.saveRuntime(hour+":"+minute);
//										  intent.putExtra("time",hour+":"+minute);
//										  sendBroadcast(intent); 
//										   finish();
//									  }else {
//										 
//									}
//									 
//								}
//	     				
//	     			});
//	     			}
//
//				
//			}.start();
//	}
}
