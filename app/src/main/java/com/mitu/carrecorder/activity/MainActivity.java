package com.mitu.carrecorder.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.aboutus.AboutActivity;
import com.mitu.carrecorder.aboutus.FeedbackActivity;
import com.mitu.carrecorder.aboutus.ServiceTermActivity;
import com.mitu.carrecorder.adapter.AdDotGridViewAdapter;
import com.mitu.carrecorder.adapter.AdViewPagerAdapter;
import com.mitu.carrecorder.adapter.MainMenuListviewAdapter;
import com.mitu.carrecorder.bean.BannerListBean;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.entiy.Banner;
import com.mitu.carrecorder.my.MineActivity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.obd.ConnectOBDActivity;
import com.mitu.carrecorder.set.SettingActivity;
import com.mitu.carrecorder.tracking.TrackingActivity;
import com.mitu.carrecorder.trackingtu.MileageActivity;
import com.mitu.carrecorder.traffic.TrafficIllegalQueryActivity;
import com.mitu.carrecorder.utils.AsyncImageLoader;
import com.mitu.carrecorder.utils.MyWifiManager;
import com.mitu.carrecorder.utils.SpHelper;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.view.CustomDialog;
import com.mitu.carrecorder.view.DragLayout;
import com.mitu.carrecorder.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends Activity implements OnClickListener ,AdapterView.OnItemClickListener,Runnable{

    /**
     * 左边侧滑菜单
     */
    private DragLayout mDragLayout;
    private ImageButton menuSettingBtn;// 菜单呼出按钮
    private TextView tvNickname, tvMileage;
    private SimpleDraweeView ivMyphoto;
    private LinearLayout findLayout, trackLayout, photoVideoLayout, Obd;
    private SpHelper sp;
    private long exitTime;


    @Bind(R.id.menu_listview)
    MyListView menuListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sp = new SpHelper(MainActivity.this);
        initView();

        //轮播线程
        new Thread(this).start();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //
        initLeftMenu();
        //}
    }




    private void initView() {
        getAdBannerList();
        //
        initLeftMenu();
        //
        initMainContent();
    }


    /**
     * 初始化主页面布局
     */
    private void initMainContent() {
        initViewPages();

        photoVideoLayout = (LinearLayout) findViewById(R.id.ll_main_photo);
        trackLayout = (LinearLayout) findViewById(R.id.ll_main_tracking);
        Obd = (LinearLayout) findViewById(R.id.ll_main_obd);
        findLayout = (LinearLayout) findViewById(R.id.ll_main_find);


        Obd.setOnClickListener(this);
        findLayout.setOnClickListener(this);
        trackLayout.setOnClickListener(this);

        photoVideoLayout.setOnClickListener(this);
        mDragLayout = (DragLayout) findViewById(R.id.dl);
        mDragLayout.setDragListener(new DragLayout.DragListener() {// 动作监听
            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onDrag(float percent) {

            }
        });
        // 添加监听，可点击呼出菜单
        menuSettingBtn = (ImageButton) findViewById(R.id.menu_imgbtn);
        menuSettingBtn.setOnClickListener(this);

    }

    /**viewPager对象*/
    private ViewPager advPager = null;

    /**默认图片对象*/
    private Bitmap bitmapDefault;
    /**布局界面数组*/
    private View[] adView;

    ArrayList<String> urlList = null;



    /**
     * 初始化广告ViewPager
     */
    private void initViewPages(){
        LayoutInflater inflater = this.getLayoutInflater();
        if(advPager == null){
            advPager = (ViewPager) findViewById(R.id.viewPagerAd);
        }

        // 图片列表
        List<View> advPics = new ArrayList<View>();
        advPager.removeAllViews();//删除已有


        if(urlList == null || urlList.size() == 0){//未获取
            adView = new View[1];
            for(int i = 0;i < adView.length;i++){
                adView[i] = inflater.inflate(R.layout.item_ad, null);
                advPics.add(adView[i]);
            }
            advPager.setAdapter(new AdViewPagerAdapter(advPics));
            initDotAndText(adView.length);
        }else{//已获取信息
            bitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.default_ad);
            adView = new View[urlList.size()];
            for(int i = 0;i < 5 && i < urlList.size();i++){
                adView[i] = inflater.inflate(R.layout.item_ad, null);
                final ImageView img = (ImageView)adView[i].findViewById(R.id.imageViewAd);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                String url = urlList.get(i);
                Bitmap b = BEApplication.loader.loadBitmap(url, new AsyncImageLoader.ImageCallback(){
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        img.setImageBitmap(bitmap);
                    }
                });
                if(b != null){
                    img.setImageBitmap(b);
                }else{
                    img.setImageBitmap(bitmapDefault);
                }
                adView[i].setOnClickListener(this);
                advPics.add(adView[i]);
            }
            advPager.setAdapter(new AdViewPagerAdapter(advPics));
            initDotAndText(urlList.size());
        }
        advPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int position) {
                dotAdapter.setSelectIndex(position);
                dotAdapter.notifyDataSetChanged();
            }
        });

        /*advPager.*/

        //设置轮播广告高度
        ViewGroup.LayoutParams params = advPager.getLayoutParams();
        params.height = BEApplication.width * 4 / 12;
        advPager.setLayoutParams(params);
    }

    /**圆点*/
    private GridView gridViewDot;
    //点适配器
    private AdDotGridViewAdapter dotAdapter;

    //初始化ViewPager

    private void initDotAndText(int num){
        getDimention();
        gridViewDot = (GridView)findViewById(R.id.gridViewDot);
        //初始化点
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)gridViewDot.getLayoutParams();
        params2.width = (int)(num * 22 * BEApplication.dx);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridViewDot.setLayoutParams(params2);
        gridViewDot.setNumColumns(num);
        dotAdapter = new AdDotGridViewAdapter(this,num);
        gridViewDot.setAdapter(dotAdapter);
    }


    private BannerListBean bannerListBean;
    private void getAdBannerList(){
        OkHttpUtils
                .get()
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.AD_BANNER))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        bannerListBean = Response.parseADBannerList(s);
                        if (bannerListBean.result){
                            int size = bannerListBean.arrayList.size();
                            urlList = new ArrayList<>();
                            for (int j = 0; j < size; j++) {
                                Banner banner = bannerListBean.arrayList.get(j);
                                //if(banner.showInBusiness){
                                urlList.add(banner.getImgUrl());
                                //}
                            }
                            refresh();
                        }
                    }
                });
    }

    private void refresh() {
        initViewPages();
    }


    /**
     * 获得屏幕宽度
     */
    private void getDimention(){
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;

        BEApplication.width = width;
        BEApplication.height = height;
        BEApplication.dx = metric.density;
    }


    private String[] menuArray;
    private int[] iconIds = {R.drawable.ic_menu_lilu,R.drawable.weizhangchaxun,R.drawable.ic_menu_shezi,
            R.drawable.ic_menu_guanyu};
    private ArrayList<BEMenuItem> arrayList;
    private MainMenuListviewAdapter adapter;


    /**
     * 左侧滑菜单
     */
    private void initLeftMenu(){
        initMenuListview();
        tvNickname = (TextView) findViewById(R.id.menu_name);
        if (!sp.getUserName().equals("") && sp.getIsLogin()) {
            tvNickname.setText(sp.getUserName());
        } else {
            tvNickname.setText(R.string.tourist);
        }
        tvMileage = (TextView) findViewById(R.id.tv_menu_totalMileage);

        if (sp.getTotalMileage().equals("")) {
            tvMileage.setText(0 + "km");
        } else {
            tvMileage.setText(sp.getTotalMileage() + "km");
        }

        ivMyphoto = (SimpleDraweeView) findViewById(R.id.iv_menu_avatar);
        ivMyphoto.setOnClickListener(this);
//			Log.i("userImage", sp.getUserImage());
        ivMyphoto.setImageURI(Uri.parse(sp.getUserImage()));
    }

    private void initMenuListview() {
        menuArray = getResources().getStringArray(R.array.mainMenuArray);
        arrayList = new ArrayList<>();
        for (int i = 0; i < menuArray.length; i++) {
            BEMenuItem item = new BEMenuItem(iconIds[i],menuArray[i]);
            arrayList.add(item);
        }
        adapter = new MainMenuListviewAdapter(this,arrayList);
        menuListview.setAdapter(adapter);
        menuListview.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == menuListview){
            switch (position){
                case 0:
                    if (MyWifiManager.isWiFiOpen(this)){//wifi 连接的话要判断连接的网络是否是设备

                        doTestConnect2(CHANGE_TO_ORFBIT);

                    }else if (SystemUtils.isNetConnected(this)){

                    }else {

                    }

                    break;
                case 1:
                    //违章查询
                    if(sp.getIsLogin())
                    {
                        startActivity(new Intent(MainActivity.this, TrafficIllegalQueryActivity.class));
                    }else
                    {
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }

                    break;
                case 2:
//                    if (MyWifiManager.)
                    //设置
                    doTestConnect(CHANGE_TO_SETTING);
//                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    break;
                case 3:
                    //关于
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case 4:
                    //反馈
                    startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
                    break;
                case 5:
                    //服务条款
                    startActivity(new Intent(MainActivity.this, ServiceTermActivity.class));
                    break;
                default:
                    break;

            }
        }
    }

    private boolean isRun = true;

    @Override
    public void run() {
        try{
            while(isRun){
                Thread.sleep(4000);
                handler.sendEmptyMessage(30);
            }
        }catch(Exception e){
//			e.printStackTrace();
        }

    }

    private static final int AD_PLAY = 30;
    private  Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case AD_PLAY://切换滚动图片
                    moveToNext();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    /**
     * 显示下一个内容
     */
    private void moveToNext(){
        if(dotAdapter != null){
            int count = dotAdapter.getCount();
            int index = dotAdapter.selectIndex;
            int next = (index + 1) % count;
            advPager.setCurrentItem(next);
            dotAdapter.setSelectIndex(next);
            dotAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onClick(View view) {
        //
        if(adView != null && adView.length > 0){
            for(int i = 0;i < adView.length;i++){
                if(adView[i] == view){
                    String url = bannerListBean.arrayList.get(i).getCnUrl();
                    openBrower(url);
                    //showPopupImage(urlList[i]);
                    break;
                }
            }
        }
        //
        switch (view.getId()) {
            case R.id.menu_imgbtn://顶部菜单按钮
                mDragLayout.open();
                break;
            case R.id.iv_menu_avatar://头像
//				showPicDialog(view );
                if (sp.getIsLogin()){
                    startActivity(new Intent(MainActivity.this, MineActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }

                break;
            case R.id.ll_main_find://轨迹

                if (MyWifiManager.isWiFiOpen(this)){
                    doTestConnect2(CHANGE_TO_ORFBIT);
                }else if (!SystemUtils.isNetConnected(this) &&  !MyWifiManager.isWiFiOpen(this)){
                    showMyDialog(this,getString(R.string.openInternet));
                }

                //我的轨迹
//                if(sp.getIsLogin())
//                {
//                    startActivity(new Intent(MainActivity.this, MileageActivity.class));
//                }else
//                {
//                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
//                }

                break;
            case R.id.ll_main_tracking://追踪
                if(sp.getIsLogin())
                {
                    startActivity(new Intent(MainActivity.this,TrackingActivity.class));
                }else
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
//                startActivity(new Intent(MainActivity.this, TrackingActivity.class));
                break;
            case R.id.ll_main_obd:
                //obd
                if (MyWifiManager.isWiFiOpen(this)){
                    doTestConnect(CHANGE_TO_OBD_CONNECTED);
                /*Toast.makeText(MainActivity.this,MyWifiManager.getWifiName(MainActivity.this),
                        Toast.LENGTH_SHORT).show();*/
//                    Toast.makeText(MainActivity.this,MyWifiManager.getWifiName(MainActivity.this) +" ==="+ MyWifiManager.getMacAddress(this),
//                            Toast.LENGTH_SHORT).show();
                }else{
                    showMyDialog(this,getString(R.string.wifiNotOpen));
                }


                break;
            case R.id.ll_main_photo://记录仪

                if (MyWifiManager.isWiFiOpen(this)){
                    doTestConnect(CHANGE_TO_DEVICELIST);
                /*Toast.makeText(MainActivity.this,MyWifiManager.getWifiName(MainActivity.this),
                        Toast.LENGTH_SHORT).show();*/
//                    Toast.makeText(MainActivity.this,MyWifiManager.getWifiName(MainActivity.this) +" ==="+ MyWifiManager.getMacAddress(this),
//                            Toast.LENGTH_SHORT).show();
                }else{
                    showMyDialog(this,getString(R.string.wifiNotOpen));
                }

                break;
            default:
                break;
        }
    }




    private boolean isWifiResponse = false;
    /**
     * 网络请求测试是否连接设备WIFI
     * @param value
     */
    private void doTestConnect(int value) {
        isWifiResponse = false;
        startTimeOutThread(value);
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3016")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("0")){
                            isWifiResponse = true;
                        }
                    }
                });

    }


    /**
     * 网络请求测试是否连接设备WIFI
     * @param value
     */
    private void doTestConnect2(int value) {
        isWifiResponse = false;
        startTimeOutThread2(value);
        OkHttpUtils.post()///调用反馈接口，用于测试网络连接是否是internet,否则
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.FEED_BACK))
                .addParams("content", "")
                .addParams("username", "")
                .addParams("nickname", "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissDialog();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (s.length() != 0){
                            isWifiResponse = true;
                        }

                    }
                });

    }

    private static final int CHANGE_TO_DEVICELIST = 0;
    private static final int CHANGE_TO_SETTING = 1;
    private static final int CHANGE_TO_OBD_CONNECTED = 2;
    private static final int CHANGE_TO_ORFBIT = 3;


    /**
     * 启动联网超时处理线程
     * @param value
     */
    protected void startTimeOutThread(final int value){
        initProgressDialog(getString(R.string.onLoading));
        new Thread(){
            public void run(){
                Looper.prepare();
                try {
                    for (int i = 0; i < 5; i++) {// 5秒的联网超时
                        Thread.sleep(1000);
                        if (isWifiResponse) {
                            dismissDialog();
                            if (value == CHANGE_TO_DEVICELIST){
                                startActivity(new Intent(MainActivity.this,BlindActivity.class));
                            }else if (value == CHANGE_TO_SETTING){
                                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                            }else if (value == CHANGE_TO_OBD_CONNECTED){
                                startActivity(new Intent(MainActivity.this,ConnectOBDActivity.class));
                            }

                            return;
                        }
                    }
                } catch (Exception e) {
                }

                dismissDialog();

//                Toast.makeText(MainActivity.this,"请连接设备WIFI",Toast.LENGTH_SHORT).show();
                showMyDialog(MainActivity.this,getString(R.string.notConnectedToDeviceWifi));
                Looper.loop();
                Looper.myLooper().quit();
            }
        }.start();
    }
    /**
     * 启动联网超时处理线程
     * @param value
     */
    protected void startTimeOutThread2(final int value){
        initProgressDialog(getString(R.string.onLoading));
        new Thread(){
            public void run(){
                Looper.prepare();
                try {
                    for (int i = 0; i < 5; i++) {// 5秒的联网超时
                        Thread.sleep(1000);
                        if (isWifiResponse) {
                            dismissDialog();
                            if (value == CHANGE_TO_ORFBIT){
                                //我的轨迹
                                if(sp.getIsLogin())
                                {
                                    startActivity(new Intent(MainActivity.this, MileageActivity.class));
                                }else
                                {
                                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                }
                            }
                            return;
                        }
                    }
                } catch (Exception e) {
                }

                dismissDialog();
//                Toast.makeText(MainActivity.this,"请连接设备WIFI",Toast.LENGTH_SHORT).show();
                showMyDialog(MainActivity.this,getString(R.string.needDisconnectDeviceWifi));
                Looper.loop();
                Looper.myLooper().quit();
            }
        }.start();
    }



    public void showMyDialog(Context context, String message){
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(getString(R.string.prompt));
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new
                        Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**联网对话框*/
    public Dialog pdD;
    /**
     * 初始化联网进度条对象
     */
    public void initProgressDialog(String info){
        try{
            if(pdD == null){
                pdD = new Dialog(this,R.style.customDialog);
                LayoutInflater inflater = LayoutInflater.from(this);
                View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
                TextView content = (TextView)v.findViewById(R.id.textViewContent);
                content.setText(info);
                pdD.setContentView(v);
                pdD.show();
                pdD.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {

                    }
                });
            }else{
                pdD.show();
            }
        }catch(Exception e){}
    }

    /**
     * dismiss联网进度条
     */
    public void dismissDialog(){
        if(pdD != null){
            pdD.dismiss();
            pdD = null;
        }
    }

    protected void openBrower(String url) {

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()
            {
                Toast.makeText(getApplicationContext(), getString(R.string.zayc),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // IbeegeeAppliction.getInstence().clearActivity();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /*public DefineProgressDialog pd;
    public void showProgressDialog( String message){
        pd = new DefineProgressDialog(MainActivity.this
                ,message);
        pd.show();
    }



    //关闭dialog
    public void cancelDialog(){
        pd.cancel();
    }*/
}
