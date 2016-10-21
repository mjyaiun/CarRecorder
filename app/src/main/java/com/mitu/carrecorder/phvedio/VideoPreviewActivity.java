package com.mitu.carrecorder.phvedio;

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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.view.PlayerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class VideoPreviewActivity extends AppCompatActivity implements View.OnClickListener,PlayerView.OnChangeListener,
        SeekBar.OnSeekBarChangeListener,Callback {

    //
    @Bind(R.id.topradio)
    RelativeLayout topLayout;

    @Bind(R.id.bottomradio)
    RelativeLayout bottomLayout;
    @Bind(R.id.pv_preview_video)
    PlayerView mPlayerView;
    //top
    @Bind(R.id.iv_back)
    ImageView backImg;
    @Bind(R.id.tv_date)
    TextView dateTv;


    //bottom
    @Bind(R.id.stop_play_img)
    ImageView stopImg;
    @Bind(R.id.time_start_tv)
    TextView timeStartTv;
    @Bind(R.id.speed_forward_img)
    ImageView speedForwardImg;
    @Bind(R.id.speed_back_img)
    ImageView speedBackImg;

    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    //

    //
    @Bind(R.id.rl_loading)
    LinearLayout rlLoading;

//    //
//    @Bind(R.id.rl_capture)
//    LinearLayout captureWaitLayout;
//    @Bind(R.id.netProgress_tv)
//    TextView netInfoTextview;

    @Bind(R.id.tv_buffer)
    TextView tvBuffer;


    private static final int SHOW_PROGRESS = 0;
    private static final int ON_LOADED = 1;
    private static final int HIDE_OVERLAY = 2;

    //
    private static final int GET_RECORD_TIME = 3;
//    private static final int GET_RECORD_TIME_STOP = 4;

    private String mUrl;
    private String fileName;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_preview);
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
        fileName = getIntent().getStringExtra("fileName");

        if (TextUtils.isEmpty(mUrl)) {
            Toast.makeText(this, "error:no url in intent!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(fileName)){
            dateTv.setText(fileName);
        }

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


    private void initButton() {
        backImg.setOnClickListener(this);
        stopImg.setOnClickListener(this);
        speedForwardImg.setOnClickListener(this);
        speedBackImg.setOnClickListener(this);
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
        mPlayerView.pause();
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
        Log.i("videoPlay","onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_back:
                finish();
                break;
            case R.id.stop_play_img:
                if (mPlayerView.isPlaying()) {
                    mPlayerView.pause();
                    stopImg.setBackgroundResource(R.drawable.ic_video_preview_play);
                } else {
                    mPlayerView.play();
                    stopImg.setBackgroundResource(R.drawable.ic_video_preview_pause);
                }
                break;
            case R.id.speed_back_img:

                break;
            case R.id.speed_forward_img:
                break;
            default:
                break;
        }
    }


    /***
     * 切换设备模式
     * @param mode
     */
    private void doChangeMode(final int mode) {

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3001")
                .addParams("par","0")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
//                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){

                        }else if (s.equals("-11")){
//                            hideOnNetProgress();
                            Toast.makeText(VideoPreviewActivity.this, getString(R.string.noFreeSpace) , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

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
//    private void showOnNetProgress(String info){
//        captureWaitLayout.setVisibility(View.VISIBLE);
//        netInfoTextview.setText(info);
//    }

    /**
     * 隐藏网络请求进度
     */
//    private void hideOnNetProgress(){
//        captureWaitLayout.setVisibility(View.GONE);
//    }

    private void showOverlay() {
        topLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);


        //
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        mHandler.removeMessages(HIDE_OVERLAY);
        mHandler.sendEmptyMessageDelayed(HIDE_OVERLAY, 5 * 1000);


    }

    private void hideOverlay() {
        topLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
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
        finish();
    }


    private int setOverlayProgress() {
        if (mPlayerView == null) {
            return 0;
        }
        int time = (int) mPlayerView.getTime();
        int length = (int) mPlayerView.getLength();
        boolean isSeekable = mPlayerView.canSeekable() && length > 0;
        stopImg.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
        speedForwardImg.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
        speedBackImg.setVisibility(isSeekable ? View.VISIBLE : View.GONE);
        seekBar.setMax(length);
        seekBar.setProgress(time);
        if (time >= 0) {
            timeStartTv.setText(millisToString(time, false));
        }
        if (length >= 0) {
            endTimeTv.setText(millisToString(length, false));
        }

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

           // case GET_RECORD_TIME_STOP:

//                break;
            default:
                break;
        }
        return false;
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
