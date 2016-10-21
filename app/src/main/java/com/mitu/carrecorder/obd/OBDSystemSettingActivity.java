package com.mitu.carrecorder.obd;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.SettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

import butterknife.Bind;

/***
 * obd系统检测页面
 */
public class OBDSystemSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    @Bind(R.id.obd_setting_lv)
    ListView settingListview;
    private ArrayList<BEMenuItem> itemArrayList;
    private SettingListviewAdapter adapter;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obdsystem_setting);

    }

    @Override
    public void initView() {
        setTitle(getString(R.string.obdSystemCheck));
        initTitleBack();
        items = getResources().getStringArray(R.array.OBDSettingArray);
        itemArrayList = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            BEMenuItem item = new BEMenuItem(items[i]);
            itemArrayList.add(item);
        }

        adapter = new SettingListviewAdapter(this,itemArrayList);
        settingListview.setAdapter(adapter);
        settingListview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == settingListview){
            switch (position){
                case 0://车辆功能设置
                    changeActivity(ObdcarSettingActivity.class);
                    break;
                case 1://车辆健康检测
                    changeActivity(ObdCarHealthCheckActivity.class);
                    break;
                case 2://胎压检测
                    changeActivity(ObdCarStateActivity.class);
                    break;
                default:
                    break;
            }
        }
    }
}
