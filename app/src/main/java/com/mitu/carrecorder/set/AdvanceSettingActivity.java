package com.mitu.carrecorder.set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.utils.DeviceSettingsCache;

import okhttp3.Call;

public class AdvanceSettingActivity extends BaseActivity implements OnClickListener ,CompoundButton.OnCheckedChangeListener{
    private RelativeLayout rlpugLayout, rlWD, rlTV, rlGp, rlstopcar, rlGese;
    private CheckBox hdrCheckbox;

    private int movieEV;
    private int HDR;
    private int tvStyle;
    private int gsensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_setting);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.seniorSetting));
        initTitleBack();

        //
        initContent();
        //
        rlGese = (RelativeLayout) findViewById(R.id.rl_setting_gsen);
        rlGp = (RelativeLayout) findViewById(R.id.rl_my_gp);
        rlpugLayout = (RelativeLayout) findViewById(R.id.rl_my_puguang);
        rlstopcar = (RelativeLayout) findViewById(R.id.rl_my_stopcar);
        rlTV = (RelativeLayout) findViewById(R.id.rl_setting_tv);
        rlWD = (RelativeLayout) findViewById(R.id.rl_setting_wdr);
        hdrCheckbox = (CheckBox) findViewById(R.id.hdr_switch_cb);
        //
        rlGese.setOnClickListener(this);
        rlGp.setOnClickListener(this);
        rlpugLayout.setOnClickListener(this);
        rlstopcar.setOnClickListener(this);
        rlTV.setOnClickListener(this);
        rlWD.setOnClickListener(this);
        hdrCheckbox.setOnCheckedChangeListener(this);
    }


    private void initContent(){
//        Bundle bundle = getIntent().getExtras();
//
//        Bundle bundleAd = new Bundle();
//        bundleAd.putInt("movieEV", DeviceSettingsCache.getCommandState("2005"));
//        bundleAd.putInt("HDR",DeviceSettingsCache.getCommandState("2004"));
//        bundleAd.putInt("tvStyle",DeviceSettingsCache.getCommandState("3009"));
//        bundleAd.putInt("GSensor",DeviceSettingsCache.getCommandState("2011"));

        movieEV = DeviceSettingsCache.getCommandState("2005");
        HDR = DeviceSettingsCache.getCommandState("2004");
        tvStyle = DeviceSettingsCache.getCommandState("3009");
        gsensor = DeviceSettingsCache.getCommandState("2011");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_stopcar:
                startActivity(new Intent(this, CarStopMonitorSettingActivity.class));
                break;
            case R.id.rl_my_gp:
                startActivity(new Intent(this, LightSourceFrequenceSettingActivity.class));
//                changeActivity(LightSourceFrequenceSettingActivity.class,"type",);

                break;
            case R.id.rl_setting_tv:
//                startActivity(new Intent(this, TVStyleActivity.class));
                changeActivity(TVStyleActivity.class);

                break;
            case R.id.rl_my_puguang:
//                startActivity(new Intent(this, ExposureCompensationSettingActivity.class));
                changeActivity(ExposureCompensationSettingActivity.class,"type",movieEV);

                break;
            case R.id.rl_setting_gsen:
//                startActivity(new Intent(this, GsSensorSettingActivity.class));
                changeActivity(GsSensorSettingActivity.class,"type",gsensor);
                break;
            default:
                break;
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int parm = 0;
        if (buttonView == hdrCheckbox){
            if (isChecked){
                parm = 1;
            }else {
                parm = 0;
            }
            doModifyStatus(parm);
        }
    }

    private void doModifyStatus(final int parm) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2004")
                .addParams("par",String.valueOf(parm))
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.i("OkHttpUtils","tvStyle:"+s);
                        DeviceSettingsCache.setCommandState("2004",parm);
                        HDR = parm;
                    }
                });
    }


}
