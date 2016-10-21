package com.mitu.carrecorder.phvedio;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.CaptureResult;
import com.mitu.carrecorder.net.CaptureCallBack;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.net.CommandValueCallBack;
import com.mitu.carrecorder.set.RecorderSettingActivity;
import com.mitu.carrecorder.set.VideoResolutionSettingActivity;
import com.mitu.carrecorder.utils.DeviceSettingsCache;
import com.mitu.carrecorder.view.PlayerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class VideoPlayActivity extends AppCompatActivity implements
        View.OnClickListener,PlayerView.OnChangeListener,
        SeekBar.OnSeekBarChangeListener,Callback {

    //
    @Bind(R.id.topradio)
    RelativeLayout topLayout;
    @Bind(R.id.leftradio)
    RadioGroup leftLayout;
    @Bind(R.id.rightradio)
    LinearLayout rightLayout;
    @Bind(R.id.bottomradio)
    RelativeLayout bottomLayout;
    @Bind(R.id.pv_video)
    PlayerView mPlayerView;
    //top
    @Bind(R.id.iv_back)
    ImageView backImg;
//    @Bind(R.id.tv_date)
//    TextView dateTv;
    @Bind(R.id.record_layout)
    LinearLayout recordLayout;
    @Bind(R.id.record_time_tv)
    TextView recordTimeTv;

    @Bind(R.id.on_record_layout)
    LinearLayout onRecordLayout;
    @Bind(R.id.time_tv)
    TextView recordTimeNormalTv;
    //

//    @Bind(R.id.tv_time)
//    TextView timeTv;

    //left
    @Bind(R.id.wenjian_rb)
    RadioButton fileBtn;
    @Bind(R.id.fen_rb)
    RadioButton resolutionBtn;
    @Bind(R.id.set_rb)
    RadioButton setBtn;
    //bottom
    @Bind(R.id.stop_play_img)
    ImageView stopImg;
    @Bind(R.id.time_start_tv)
    TextView timeStartTv;
    @Bind(R.id.speed_img)
    ImageView seepdImg;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    //

    //right
    @Bind(R.id.iv_luyin)
    LinearLayout videoVoiceRb;//录音
    @Bind(R.id.rb_luxiang)
    LinearLayout videoRb;//录像
    @Bind(R.id.iv_camera)
    LinearLayout takephotoRb;
    //
    @Bind(R.id.rl_loading)
    LinearLayout rlLoading;

    //
    @Bind(R.id.rl_capture)
    LinearLayout captureWaitLayout;
    @Bind(R.id.netProgress_tv)
    TextView netInfoTextview;

    @Bind(R.id.tv_buffer)
    TextView tvBuffer;


    private static final int SHOW_PROGRESS = 0;
    private static final int ON_LOADED = 1;
    private static final int HIDE_OVERLAY = 2;

    //
    private static final int GET_RECORD_TIME = 3;
//    private static final int GET_RECORD_TIME_STOP = 4;

    private String mUrl;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_play);
        //
        ButterKnife.bind(this);
        //
        initUI();
        //
        //doStartPlay();
    }

    private void initUI() {
        initButton();
        initContent();
    }

    private void initContent() {

        mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            Toast.makeText(this, "error:no url in intent!", Toast.LENGTH_SHORT).show();
            return;
        }


//        dateTv.setText(DateUtil.getCurDateStr("yyyy-MM-dd HH:mm:ss"));
        //
        mHandler = new Handler(this);

        //第二步：设置参数，毫秒为单位
        mPlayerView.setNetWorkCache(20000);
        //第三步:初始化播放器

        mPlayerView.initPlayer(mUrl);

        //第四步:设置事件监听，监听缓冲进度等
        mPlayerView.setOnChangeListener(this);
        //第五步：开始播放
        mPlayerView.start();
        showLoading();
        hideOverlay();
    }


    private int audioStete;
    private int isMovieRecord = 0;

    private void initButton() {
        fileBtn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
        resolutionBtn.setOnClickListener(this);
        videoVoiceRb.setOnClickListener(this);
        videoRb.setOnClickListener(this);
        takephotoRb.setOnClickListener(this);
        backImg.setOnClickListener(this);
        //
        audioStete = DeviceSettingsCache.getCommandState("2007");
        if (audioStete == 0){
            videoVoiceRb.setBackgroundResource(R.drawable.shipinbogangbg2);
        }else {
            videoVoiceRb.setBackgroundResource(R.drawable.shipingonneng_jianbg);
        }

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (topLayout.getVisibility() != View.VISIBLE) {
                showOverlay();
            } else {
                hideOverlay();
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mPlayerView.changeSurfaceSize();
        super.onConfigurationChanged(newConfig);
    }

    private boolean isStop = false;

    @Override
    public void onPause() {
        super.onPause();
        Log.i("videoPlay","onPause");
        hideOverlay();
//        stopPlay();
        isStop = true;

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.i("videoPlay","onResume");

        if (isStop){
            mPlayerView.initPlayer(mUrl);

            //第四步:设置事件监听，监听缓冲进度等
            mPlayerView.setOnChangeListener(this);
            //第五步：开始播放
            mPlayerView.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isMovieRecord == 1){
            doStopMovieRecord();
        }else {
            doStopPlayLiveVideo();
        }

        Log.i("videoPlay","onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE ){
            if (resultCode == VideoResolutionSettingActivity.RESULT_OK){
                Bundle bundle = data.getExtras();
                int type = bundle.getInt("type");
                doModifyStatus(2002,type);
            }
        }
    }

    private void doModifyStatus(final int cmd, final int parm) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd",String.valueOf(cmd))
                .addParams("par",String.valueOf(parm))
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Toast.makeText(VideoPlayActivity.this,"选择了："+ cmd  + "    " + parm + "  "+  s,Toast.LENGTH_SHORT).show();
                        switch (cmd){
                            case 2002:
                                DeviceSettingsCache.setCommandState("2002",parm);
                                break;

                        }
                    }
                });

    }

    private void doStopPlayLiveVideo() {
        OkHttpUtils.get()//
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2015")
                .addParams("par","0")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(DeviceListActivity.this, "2015返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
//                            String url = BEConstants.RTSP_ADDRSS_MOVIE_LIVE;
//                            changeActivity(VideoPlayActivity.class,"url",url);
                        }else {
//                            showToast(R.string.playFailed);
                        }
                    }
                });
    }


    private void doStopMovieRecord() {
        OkHttpUtils.get()//
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2001")
                .addParams("par","0")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(DeviceListActivity.this, "2015返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
//                            String url = BEConstants.RTSP_ADDRSS_MOVIE_LIVE;
//                            changeActivity(VideoPlayActivity.class,"url",url);
                        }else {
//                            showToast(R.string.playFailed);
                        }
                    }
                });
    }

    private static final int REQUEST_CODE = 88;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wenjian_rb:
//                stopPlay();
                if (isMovieRecord == 0){
                    mPlayerView.stop();
                    Intent fileIntent = new Intent(this,PhotoVideoActivity.class);
                    startActivity(fileIntent);
                }else{
                    Toast.makeText(this,getString(R.string.pleaseStopMovieRecord),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.set_rb:
//                stopPlay();
                if (isMovieRecord == 0){
                    mPlayerView.stop();
                    Intent setIntent = new Intent(this, RecorderSettingActivity.class);
                    startActivity(setIntent);
                }else{
                    Toast.makeText(this,getString(R.string.pleaseStopMovieRecord),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fen_rb:
//                stopPlay();
                if (isMovieRecord == 0){
                    mPlayerView.stop();
                    Intent resolutionIntent = new Intent(this, VideoResolutionSettingActivity.class);
                    startActivityForResult(resolutionIntent,REQUEST_CODE);
                }else {
                    Toast.makeText(this,getString(R.string.pleaseStopMovieRecord),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_luyin://录音按钮
                if (isMovieRecord == 0){
                    if (audioStete == 0){
                        doChangeAudioMode(1);
                    }else if (audioStete == 1){
                        doChangeAudioMode(0);
                    }
                }else {
                    Toast.makeText(this,getString(R.string.pleaseStopMovieRecord),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rb_luxiang:
                if (isMovieRecord == 0){
                    doChangeMovieRecordState(1);//开始录像
                    mHandler.sendEmptyMessageDelayed(GET_RECORD_TIME,2000);
                }else if (isMovieRecord == 1){
                    doChangeMovieRecordState(0);//停止录像

                }

                break;
            case R.id.iv_camera:
                if (isMovieRecord == 0){
                    doChangeMode(PHOTO);
                }else {
                    Toast.makeText(this,getString(R.string.pleaseStopMovieRecord),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    private void doChangeMovieRecordState(final int mode) {
        if (mode == 0){
            showOnNetProgress(getString(R.string.onRequestMovieRecordStop));
        }else {
            showOnNetProgress(getString(R.string.onRequestMovieRecord));
        }

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2001")
                .addParams("par",mode+"")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        hideOnNetProgress();

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        hideOnNetProgress();
//                        Toast.makeText(VideoPlayActivity.this, "2001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){

                            isMovieRecord = mode;
                            refreshModeData();
                            if (isMovieRecord == 1){
                               // mHandler.sendEmptyMessage(GET_RECORD_TIME);
                            }else {
                                mHandler.removeMessages(GET_RECORD_TIME);
                            }

                        }else {
                            Toast.makeText(VideoPlayActivity.this, getString(R.string.opFailed)+" errorCode:"+ s, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 切换音频状态
     * @param mode
     */
    private void doChangeAudioMode(final int mode) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2007")
                .addParams("par",mode+"")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
                            audioStete = mode;
                            DeviceSettingsCache.setCommandState("2007",mode);
                            refreshModeData();
                        }
                    }
                });
    }


    private int recordTime = 0;
    /**
     * 刷新界面
     */
    private void refreshModeData() {

        if (audioStete == 0){
            videoVoiceRb.setBackgroundResource(R.drawable.shipinbogangbg2);
        }else {
            videoVoiceRb.setBackgroundResource(R.drawable.shipingonneng_jianbg);
        }

        if (isMovieRecord == 0){
            videoRb.setBackgroundResource(R.drawable.shipinbogangbg2);
        }else {
            videoRb.setBackgroundResource(R.drawable.shipingonneng_jianbg);
        }




    }


    private String recordTimeFormat(int second){
        String timeStr = "";
        if (second < 10){
            timeStr = "00:00:0"+second;
        }else if (second >= 10 && second < 60){
            timeStr = "00:00:"+second;
        }else if (second > 60){
            int minite = second /60;
            int sec = second % 60;
            if (minite  < 10){
                if (sec < 10)
                    timeStr = "00:0"+minite+":0"+ sec;
                else
                    timeStr = "00:0"+minite+":"+ sec;
            }

        }
        return timeStr;
    }


    private static final int PHOTO = 0;
    private static final int MOVIE = 1;


    /***
     * 切换设备模式
     * @param mode
     */
    private void doChangeMode(final int mode) {
        if (mode == PHOTO){
            showOnNetProgress(getString(R.string.onCapture));//显示拍照等待对话框
        }

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3001")
                .addParams("par","0")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
                            if (mode == PHOTO){
                                doGetFreePicNum();
                            }else if(mode == MOVIE){
                                // do nothing
                            }
                        }else if (s.equals("-11")){
                            hideOnNetProgress();
                            Toast.makeText(VideoPlayActivity.this, getString(R.string.noFreeSpace) , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private int freeNum = 0;


    /**
     * 请求当前可以拍照的数量
     */
    private void doGetFreePicNum() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","1003")
                .build()
                .execute(new CommandValueCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "1003返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        freeNum = Integer.parseInt(s);
                        if (freeNum > 0){
                            doCapture();
                        }else {
                            hideOnNetProgress();
                            Toast.makeText(VideoPlayActivity.this, getString(R.string.noFreeSpace) , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 执行拍照
     */
    private void doCapture() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","1001")
                .build()
                .execute(new CaptureCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(CaptureResult captureResult, int i) {
                        hideOnNetProgress();
                        if (captureResult != null){
//                            Toast.makeText(VideoPlayActivity.this,"1001返回的状态值是：" + captureResult.getPhotoName(),Toast.LENGTH_SHORT).show();
                            if (captureResult.getPhotoName().length() > 0){
                                Toast.makeText(VideoPlayActivity.this,getString(R.string.captureSuccess),Toast.LENGTH_SHORT).show();
                                doChangeMode(MOVIE);
                            }else {
                                hideOnNetProgress();
                                Toast.makeText(VideoPlayActivity.this,getString(R.string.takePhotoFailed),Toast.LENGTH_SHORT).show();
                            }
                        }
//
                    }
                });
    }


//    private void stopPlay(){
//        OkHttpUtils.get()//
//                .url(BEConstants.ADDRESS_IP_DEVICE)
//                .addParams("custom","1")
//                .addParams("cmd","2015")
//                .addParams("par","0")
//                .build()
//                .execute(new CommandCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "2015返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
//                        if (s.equals("0")){
////                            String url = BEConstants.RTSP_ADDRSS_MOVIE_LIVE;
////                            changeActivity(VideoPlayActivity.class,"url",url);
//                        }else {
////                            showToast(R.string.playFailed);
//                        }
//                    }
//                });
//    }




    /**
     * 显示视频加载进度
     */
    private void showLoading() {
        rlLoading.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏视频加载进度
     */
    private void hideLoading() {
        rlLoading.setVisibility(View.GONE);
    }


    /**
     * 显示网络请求进度
     * @param info
     */
    private void showOnNetProgress(String info){
        captureWaitLayout.setVisibility(View.VISIBLE);
        netInfoTextview.setText(info);
    }

    /**
     * 隐藏网络请求进度
     */
    private void hideOnNetProgress(){
        captureWaitLayout.setVisibility(View.GONE);
    }

    private void showOverlay() {
        topLayout.setVisibility(View.VISIBLE);
        leftLayout.setVisibility(View.VISIBLE);
        rightLayout.setVisibility(View.VISIBLE);
//        bottomLayout.setVisibility(View.VISIBLE);
        //
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        mHandler.removeMessages(HIDE_OVERLAY);
        mHandler.sendEmptyMessageDelayed(HIDE_OVERLAY, 5 * 1000);
        //
//        if (isMovieRecord == 1){
//            dateTv.setVisibility(View.GONE);
//            recordLayout.setVisibility(View.VISIBLE);
//        }else{
//            dateTv.setVisibility(View.VISIBLE);
//            dateTv.setText(DateUtil.getCurDateStr());
//            recordLayout.setVisibility(View.GONE);
//        }
//
//        //
        onRecordLayout.setVisibility(View.GONE);

    }

    private void hideOverlay() {
        topLayout.setVisibility(View.GONE);
        leftLayout.setVisibility(View.GONE);
        rightLayout.setVisibility(View.GONE);
//        bottomLayout.setVisibility(View.GONE);
        onRecordLayout.setVisibility(View.VISIBLE);//
        mHandler.removeMessages(SHOW_PROGRESS);
    }

    //PlayerView.OnChangeListener
    @Override
    public void onBufferChanged(float buffer) {
        if (buffer >= 100) {
            hideLoading();
        } else {
            showLoading();
        }
        tvBuffer.setText("正在缓冲中..." + (int) buffer + "%");
    }


    @Override
    public void onLoadComplet() {
        mHandler.sendEmptyMessage(ON_LOADED);
    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(), "Player Error Occur！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEnd() {

    }


    private int setOverlayProgress() {
        if (mPlayerView == null) {
            return 0;
        }
        int time = (int) mPlayerView.getTime();
        int length = (int) mPlayerView.getLength();
        boolean isSeekable = mPlayerView.canSeekable() && length > 0;
        stopImg.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
        seepdImg.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
        seekBar.setMax(length);
        seekBar.setProgress(time);
        if (time >= 0) {
            timeStartTv.setText(millisToString(time, false));
        }
        if (length >= 0) {
            timeStartTv.setText(millisToString(length, false));
        }

        String text = recordTimeFormat(recordTime);
        recordTimeTv.setText(text);

        return time;
    }


    //seekbar

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && mPlayerView.canSeekable()) {
            mPlayerView.setTime(progress);
            setOverlayProgress();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS:
                setOverlayProgress();
                mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 20);
                break;
            case ON_LOADED:
                showOverlay();
                hideLoading();
                break;
            case HIDE_OVERLAY:
                hideOverlay();
                break;
            case GET_RECORD_TIME:
                getMovieRecordTime();
                mHandler.sendEmptyMessageDelayed(GET_RECORD_TIME,1000);
                break;
           // case GET_RECORD_TIME_STOP:

//                break;
            default:
                break;
        }
        return false;
    }

    /**获取录影时间
     *
     */
    private void getMovieRecordTime() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","2016")
                .build()
                .execute(new CommandValueCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        //Toast.makeText(VideoPlayActivity.this, "2016返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){
                            recordTime = 0;
                        }else {
                            recordTime = Integer.parseInt(s);
                            if (onRecordLayout.getVisibility() == View.VISIBLE){
                                recordTimeNormalTv.setText(recordTimeFormat(recordTime));
                            }
                        }
                    }
                });
    }


    //
    private String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);
        int mini_sec = (int) millis % 1000;
        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");

        DecimalFormat format2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format2.applyPattern("000");
        if (text) {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        } else {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec) + ":" + format2.format(mini_sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec) + ":" + format2.format(mini_sec);
        }
        return time;
    }
}
