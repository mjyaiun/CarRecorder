package com.mitu.carrecorder.set;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.utils.MyWifiManager;

public class WifiActivity extends BaseActivity implements OnClickListener {

    private LinearLayout llLayout;
    private RelativeLayout wifiLayout;
    private RelativeLayout wifiPwdLayout;
    private TextView etWifiPsw;
    private TextView etwifi;
    private android.net.wifi.WifiManager mWifiManager;

    private boolean isWifiFlag = false;
    //private ToggleButton tb;

    /*@Bind(R.id.wifi_switch_rb)
    CheckBox wifiSwitchRb;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        mWifiManager = (android.net.wifi.WifiManager) getSystemService(Context.WIFI_SERVICE);


    }




    @Override
    public void initView() {
        setTitle(getString(R.string.wifi));
        initTitleBack();
        //
//        setRightTitleText(getString(R.string.complate));

        //
        llLayout = (LinearLayout) findViewById(R.id.ll_my_wifi);
        llLayout.setOnClickListener(this);

        wifiPwdLayout = (RelativeLayout)findViewById(R.id.rl_wifi_pwd);
        wifiPwdLayout.setOnClickListener(this);

        etwifi = (TextView) findViewById(R.id.et_wifi);
        String name = MyWifiManager.getWifiName(this).replace("\"","");
        etwifi.setText(name);
        etWifiPsw = (TextView) findViewById(R.id.et_wifimm);
        wifiLayout = (RelativeLayout) findViewById(R.id.rl_setting_wifi);
        wifiLayout.setOnClickListener(this);
//        wifiSwitchRb.setOnCheckedChangeListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_my_wifi:
                CancelSoftKeyBord(view);
                break;
            case R.id.rl_setting_wifi:
                changeActivity(ModifyWifiNameActivity.class);
//                startActivityForResult(new Intent(this, WifiHotActivity.class), REQUEST_CODE_WIFI);
                break;
            case R.id.rl_wifi_pwd:
                changeActivity(ModifyWifiPwdActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//        if (requestCode == REQUEST_CODE_WIFI && resultCode == REQUEST_CODE_WIFI) {
//            capability = data.getStringExtra("capability");
//            ssid = data.getStringExtra("ssid");
//            etwifi.setText(ssid);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



}

