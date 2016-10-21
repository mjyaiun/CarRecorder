package com.mitu.carrecorder.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.phvedio.DeviceListActivity;
import com.mitu.carrecorder.utils.MyWifiManager;

public class BlindActivity extends BaseActivity{



    private TextView nameTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.blindRecorder));
        initTitleBack();
        nameTextview = (TextView)findViewById(R.id.device_wifi_name_tv);
        nameTextview.setText(MyWifiManager.getWifiName(this));

      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeActivity(DeviceListActivity.class);
                finish();
            }
        }).start();*/

        handler.sendEmptyMessageAtTime(0,800);
    }


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0)
            {
                changeActivity(DeviceListActivity.class);
                finish();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null)
        {
            handler.removeMessages(0);
        }
    }
}
