package com.mitu.carrecorder.set;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.SettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.entiy.Command;
import com.mitu.carrecorder.net.QueryStatusCallBack;
import com.mitu.carrecorder.utils.DeviceSettingsCache;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 记录仪设置
 */
public class RecorderSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    private String[] types;
    private ArrayList<BEMenuItem> itemArrayList ;
    private SettingListviewAdapter adapter;

    @Bind(R.id.recorder_set_lv)
    ListView recorderSettingListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_setting);
        //
        ButterKnife.bind(this);
        //
        queryCurrentState();
    }



    @Override
    public void initView() {
        setTitle(getString(R.string.recorderSetting));
        initTitleBack();
        types = getResources().getStringArray(R.array.recorderSettingArray);
        itemArrayList = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            BEMenuItem item = new BEMenuItem(types[i]);
            itemArrayList.add(item);
        }

        adapter = new SettingListviewAdapter(this,itemArrayList);
        recorderSettingListview.setAdapter(adapter);
        recorderSettingListview.setOnItemClickListener(this);

    }



    private ArrayList<Command> currentStatus = new ArrayList<>();

    private void queryCurrentState() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3014")
                .build()
                .execute(new QueryStatusCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(ArrayList<Command> commands, int i) {
                        if (commands.size() != 0){
//                            Toast.makeText(getApplicationContext(), "3014返回的状态值是：" + commands.size() , Toast.LENGTH_SHORT).show();
                            currentStatus.clear();
                            currentStatus.addAll(commands);
                            doLocalCache(commands);
                        }
                    }
                });
    }

    private void doLocalCache(ArrayList<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i).getNum();
            String state = commands.get(i).getState();
            DeviceSettingsCache.setCommandState(command,Integer.parseInt(state));
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == recorderSettingListview){
            switch (position){
                case 0://功能信息
                    changeActivity(FunctionInfoActivity.class);
                    break;
                case 1://wifi热点
                    changeActivity(WifiActivity.class);
                    break;
                case 2://时间设定
                    changeActivity(SetTimeActivity.class);
                    break;
                case 3://账户信息
                    changeActivity(AccountInfoActivity.class);
                    break;
                case 4://照片视频设置
                    changeActivity(PhotoVideoSettingActivity.class);
                    break;
                case 5:
                    changeActivity(StorageManageActivity.class);
                    break;
                case 6://高级设置
//                    Bundle bundleAd = new Bundle();
//                    bundleAd.putInt("movieEV",DeviceSettingsCache.getCommandState("2005"));
//                    bundleAd.putInt("HDR",DeviceSettingsCache.getCommandState("2004"));
//                    bundleAd.putInt("tvStyle",DeviceSettingsCache.getCommandState("3009"));
//                    bundleAd.putInt("GSensor",DeviceSettingsCache.getCommandState("2011"));
                    changeActivity(AdvanceSettingActivity.class);
                    break;
            }
        }
    }

    private int getMovieStamp() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2008")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getGSensor() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("3009")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getTVStyle() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("3009")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }


    private int getHDR() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2004")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getMovieEV() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2005")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getMovieVideo() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2007")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getMotionDection() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2006")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getMovieLoop() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2003")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }

    private int getMovieResolution() {
        int state = -1;
        for (int i = 0; i < currentStatus.size(); i++) {
            String command = currentStatus.get(i).getNum();
            if (command.equals("2002")){
                state = Integer.parseInt(currentStatus.get(i).getState());
                break;
            }
        }
        return state;
    }


}
