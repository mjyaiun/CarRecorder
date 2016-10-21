package com.mitu.carrecorder.obd;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;


public class ConnectOBDActivity extends BaseActivity implements OnClickListener {
    private ImageView ivobd;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_obd);

    }

    @Override
    public void initView() {
        setTitle(getString(R.string.connectObdSystem));
        initTitleBack();
        ivobd = (ImageView) findViewById(R.id.obd_iv);
        tv = (TextView) findViewById(R.id.obd_tv);
        ivobd.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeActivity(OBDSystemSettingActivity.class);
            }
        }).start();
//        btnComplete.setVisibility(View.VISIBLE);
//        btnComplete.setText("功能检测");
//        btnComplete.setTextSize(14);
//        btnComplete.setOnClickListener(this);
       // tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
               // startActivity(new Intent(this, ObdcarSettingActivity.class));
                break;

            default:
                break;
        }

    }

}
