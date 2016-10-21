package com.mitu.carrecorder.set;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.CheckSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.utils.DeviceSettingsCache;

import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.Call;

public class GsSensorSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @Bind(R.id.carstop_lv)
    ListView listView;
    private String[] itemStrs;
    private ArrayList<BEMenuItem> arrayList;
    private CheckSettingListviewAdapter adapter;

    private int gSensor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_stop_monitor_setting);
    }

    @Override
    public void initView() {

        setTitle(getString(R.string.Gsensor));
        initTitleBack();
        //
        gSensor = DeviceSettingsCache.getCommandState("2011");

        //
        itemStrs = getResources().getStringArray(R.array.carStopMonitorArray);
        arrayList = new ArrayList<>();
        for (int i = 0; i < itemStrs.length; i++) {
            BEMenuItem item = new BEMenuItem(itemStrs[i]);
            if (i == gSensor){
                item.isCheck = true;
            }else {
                item.isCheck = false;
            }
            arrayList.add(item);
        }
        //
        adapter = new CheckSettingListviewAdapter(this,arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == listView){
            doModifyStatus(position);
        }
    }

    private void doModifyStatus(final int posi) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2011")
                .addParams("par",String.valueOf(posi))
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.i("OkHttpUtils","tvStyle:"+s);
                        DeviceSettingsCache.setCommandState("2011",posi);
                        gSensor = posi;
                        refreshData();
                    }
                });
    }

    private void refreshData() {
        for (int i = 0; i < itemStrs.length; i++) {
            if (i == gSensor){
                arrayList.get(i).isCheck = true;
            }else {
                arrayList.get(i).isCheck = false;
            }

        }

        adapter.notifyDataSetChanged();
    }

}
