package com.mitu.carrecorder.phvedio;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.DeviceListAdapter;
import com.mitu.carrecorder.db.CacheHelper;
import com.mitu.carrecorder.entiy.Device;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.set.RecorderSettingActivity;
import com.mitu.carrecorder.utils.MyWifiManager;
import com.mitu.carrecorder.view.CustomDialog;

import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.Call;

public class DeviceListActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @Bind(R.id.device_lv)
    ListView deviceListview;

    @Bind(R.id.no_device_layout)
    RelativeLayout noDataLayout;
    @Bind(R.id.refresh_btn)
    Button refreshBtn;

    private DeviceListAdapter adapter;
    private ArrayList<Device> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.deviceList));
        initTitleBack();
        //
        noDataLayout.setVisibility(View.GONE);
//        for (int i = 0; i < devices.length; i++) {
//            Device device = new Device();
//            device.deviceName = devices[i];
//            device.isConnect = false;
//            arrayList.add(device);
//        }

        arrayList = CacheHelper.getDBHelper(this).getDeviceList();
        String mac = MyWifiManager.getMacAddress(this);
        for (int i = 0; i < arrayList.size(); i++) {
            if (mac.equals(arrayList.get(i).device_mac)){
                arrayList.get(i).isConnect = true;
            }else
                arrayList.get(i).isConnect = false;
        }

        //检查当前网络是否在缓存中，不存在则添加并刷新列表
        doCheckHasCurrent();

        adapter = new DeviceListAdapter(this,arrayList);
        deviceListview.setAdapter(adapter);
        deviceListview.setOnItemClickListener(this);
        adapter.setOnItemImageClickListener(new DeviceListAdapter.OnItemImageClickListener() {
            @Override
            public void onButtonClick(Context context, int position, int index) {
                if (index == 0){//设置
                    changeActivity(RecorderSettingActivity.class);
                }else if (index == 1){//下载
                    changeActivity(PhotoVideoActivity.class);
                }else if (index == 2){//删除
                    showMyDialog(position);
                }
            }
        });

        //
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认当前连接的WIFI 是设备WIFI
                doCheckHasCurrent();
            }
        });

    }

    private void doCheckHasCurrent() {
        String mac = MyWifiManager.getMacAddress(this);
        boolean isHas = CacheHelper.getDBHelper(this).isHasCurrent(mac);
        if (!isHas){
            String name = MyWifiManager.getWifiName(this).replace("\"","");
            CacheHelper.getDBHelper(this).addDevice(name,mac);
            refreshDeviceList();
        }
    }


    private void refreshDeviceList() {
        arrayList.clear();
        arrayList.addAll(CacheHelper.getDBHelper(this).getDeviceList());
        if (arrayList.size() > 0){
            deviceListview.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            String mac = MyWifiManager.getMacAddress(this);
            for (int i = 0; i < arrayList.size(); i++) {
                if (mac.equals(arrayList.get(i).device_mac)){
                    arrayList.get(i).isConnect = true;
                }else
                    arrayList.get(i).isConnect = false;
            }

            if (adapter == null){
                adapter = new DeviceListAdapter(this,arrayList);
                deviceListview.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }else{
            deviceListview.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

    }


    private void showMyDialog(final int position) {
        // TODO Auto-generated method stub
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(getString(R.string.deviceSureToDelete));
        builder.setTitle(getString(R.string.prompt));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                showToast("删除设备");
                int id = arrayList.get(position).device_id;
                CacheHelper.getDBHelper(DeviceListActivity.this).deleteDevice(id);
                refreshDeviceList();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //mWifiManager.setWifiEnabled(false);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }



    /**
     * 开启网络请求
     */
    String state = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == deviceListview){

            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    XmlUtils x = new XmlUtils();
                    state = x.getState("3001", "1");//将wifi mode 切换成movie模式
                    final String state2 = x.getState("2015","1"); //开始播放实时视频
                    runOnUiThread(new Runnable() {
                        public void run() {
//						addFileList();
                            if (state.equals("0") && state2.equals("0")){
                                //startActivity(new Intent(this, VideoActivity.class));
                                String url = "rtsp://192.168.1.254/xxxx.mov";
                                changeActivity(VideoPlayActivity.class,"url",url);
                            }
                           // Toast.makeText(DeviceListActivity.this, "返回状态值：" + state + "值2:" + state2, Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }).start();*/

           doChangeWifiMode();

        }
    }


    private void doChangeWifiMode(){

        OkHttpUtils.get()//将WIFI切换到MOVIE 模式
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3001")
                .addParams("par","1")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(DeviceListActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
                            doStartPlay();
                            //String url = BEConstants.RTSP_ADDRSS_MOVIE_LIVE;
                            //changeActivity(VideoPlayActivity.class,"url",url);
                        }
                    }
                });
    }


    private void doStartPlay() {
        OkHttpUtils.get()//
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2015")
                .addParams("par","1")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(DeviceListActivity.this, "2015返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
                            String url = BEConstants.RTSP_ADDRSS_MOVIE_LIVE;
                            changeActivity(VideoPlayActivity.class,"url",url);
                        }else {
                            Toast.makeText(DeviceListActivity.this, "错误码是：" + s , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
