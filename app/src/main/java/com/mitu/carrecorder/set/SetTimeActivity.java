package com.mitu.carrecorder.set;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.ArriveTimeWheelViewAdapter;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.view.wheelwidget.views.OnWheelChangedListener;
import com.mitu.carrecorder.view.wheelwidget.views.OnWheelScrollListener;
import com.mitu.carrecorder.view.wheelwidget.views.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;


public class SetTimeActivity extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener,OnWheelChangedListener {

    private RelativeLayout rlstyle, rldate, rltime, rlautoset;
    private CheckBox tb, tbzd;
    private TextView tvtime, tvdate, tvtimeN, tvdateN;
    private Boolean flag = true;
    //private SpHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
    }




    @Override
    public void initView() {
        setTitle(getString(R.string.timeSetting));
        initTitleBack();
        rlautoset = (RelativeLayout) findViewById(R.id.rl_my_autoset);
        rldate = (RelativeLayout) findViewById(R.id.rl_my_date);
        rlstyle = (RelativeLayout) findViewById(R.id.rl_my_style);
        tvtime = (TextView) findViewById(R.id.tv_my_settime);
        tvtimeN = (TextView) findViewById(R.id.tv_my_time);
        tvtimeN.setText(sp.getRuntime());
        tvdate = (TextView) findViewById(R.id.tv_my_setdate);
        tvdateN = (TextView) findViewById(R.id.tv_my_date);
        tvdateN.setText(sp.getdate());
        rltime = (RelativeLayout) findViewById(R.id.rl_my_time);
        rlautoset.setOnClickListener(this);
        rldate.setOnClickListener(this);
        rlstyle.setOnClickListener(this);
        rltime.setOnClickListener(this);
        tb = (CheckBox) findViewById(R.id.switch_24_hour_cb);
        tb.setOnCheckedChangeListener(this);
        tbzd = (CheckBox) findViewById(R.id.auto_setting_cb);
        tbzd.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_date:
//                startActivity(new Intent(this, DateActivity.class));
                showDatePopup();
                break;
            case R.id.rl_my_time:
                showTimePopUp();
                /*if (flag) {
                    startActivity(new Intent(this, NotifyActivity.class));
                } else {
                    Log.i("..1....", "运行" + flag);
                    startActivity(new Intent(this, TimeActivity.class));
                }*/

                break;
            default:
                break;
        }
    }




    /**修改头像弹出界面*/
    protected PopupWindow popupDate;
    private WheelView yearWheelView,monthWheelView,dayWheelView;
    private TextView cancelTextview,comfirmTextview;
    private List<String> labelsDay;
    private String currentYear, currentMonth, currentDay;

    private ArriveTimeWheelViewAdapter yearAdapter;
    private ArriveTimeWheelViewAdapter monthAdapter;
    private ArriveTimeWheelViewAdapter dayAdapter;

    private ArrayList<String> yearList = new ArrayList<>();
    private ArrayList<String> monthList = new ArrayList<>();
    private ArrayList<String> dayList = new ArrayList<>();


    //显示文字的字体大小
    private int maxsize = 24;
    private int minsize = 14;

    protected void showDatePopup(){
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.popup_date_choose, null);
        popupDate = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupDate.setBackgroundDrawable(new BitmapDrawable());
        //点击空白处时，隐藏掉pop窗口
        popupDate.setFocusable(true);
        popupDate.setOutsideTouchable(true);
        //
        popupDate.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        //初始化按钮事件
        yearWheelView = (WheelView) v.findViewById(R.id.year);
        monthWheelView = (WheelView) v.findViewById(R.id.month);
        dayWheelView = (WheelView)v.findViewById(R.id.day);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
//        c.set(Calendar.YEAR, 2016);
//        c.set(Calendar.MONTH, 2);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        //final List<String> labelsYear = new ArrayList<String>();
        for (int i = year-30; i < year + 30; i++) {
            yearList.add("" + i);
        }

        yearWheelView.addChangingListener(this);
        monthWheelView.addChangingListener(this);
        dayWheelView.addChangingListener(this);


        yearAdapter = new ArriveTimeWheelViewAdapter(this, yearList, getYearItem(String.valueOf(year)), maxsize, minsize);
        yearWheelView.setVisibleItems(5);
        yearWheelView.setCyclic(true);
        yearWheelView.setViewAdapter(yearAdapter);
        yearWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, yearAdapter);
                Log.i(TAG,"onScrollingFinished" +"yearWheelView" + "  "+ currentText);
            }
        });


        for (int i = 1; i < 13; i++) {
            if (i < 10){
                monthList.add("0"+i);
            }else{
                monthList.add(""+i);
            }

        }
        monthAdapter = new ArriveTimeWheelViewAdapter(SetTimeActivity.this, monthList, month+1, maxsize, minsize);
        monthWheelView.setVisibleItems(5);
        monthWheelView.setViewAdapter(monthAdapter);
        monthWheelView.setCyclic(true);
        monthWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, monthAdapter);
                Log.i(TAG,"onScrollingFinished" +"monthWheelView" + "  "+ currentText);

                //

            }
        });



        //int maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayList .clear();
        for (int i = 1; i <= maxDays; i++) {
            if (i < 10){
                dayList.add("0"+i);
            }else{
                dayList.add(String.valueOf(i));
            }
        }

        dayAdapter = new ArriveTimeWheelViewAdapter(this, dayList, day, maxsize, minsize);
        dayWheelView.setVisibleItems(5);
        dayWheelView.setCyclic(true);
        dayWheelView.setViewAdapter(dayAdapter);
        dayWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
                Log.i(TAG,"onScrollingFinished" +"dayWheelView" + "  "+ currentText);
            }
        });



        //
        cancelTextview = (TextView)v.findViewById(R.id.tv_height_cancel);
        cancelTextview.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(popupDate != null){
                    popupDate.dismiss();
                    popupDate = null;
                }
            }
        });

        comfirmTextview = (TextView)v.findViewById(R.id.tv_height_submit);
        comfirmTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentYear == null )
                    currentYear = (String)yearAdapter.getItemText(yearWheelView.getCurrentItem());
                if (currentMonth == null)
                    currentMonth = (String)monthAdapter.getItemText(monthWheelView.getCurrentItem());
                if (currentDay == null)
                    currentDay = (String)dayAdapter.getItemText(dayWheelView.getCurrentItem());

                String modifyDateString = currentYear+ "-"+ currentMonth +"-"+currentDay;
                doModifyDate(modifyDateString);
            }
        });
        //点击关闭
        v.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(popupDate != null){
                    popupDate.dismiss();
                    popupDate = null;
                }
            }
        });
        popupDate.setAnimationStyle(R.style.popupWindowStyle);
        popupDate.showAtLocation(findViewById(R.id.rl_my_autoset), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);

//        yearWheelView.setCurrentItem(30);
//        monthWheelView.setCurrentItem(month);

    }

    /**
     * 返回省会索引
     */
    public int getYearItem(String year) {
        int size = yearList.size();
        int yearIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (year.equals(yearList.get(i))) {
                noprovince = false;
                return yearIndex;
            } else {
                yearIndex++;
            }
        }
        if (noprovince) {
            year = "2016";
            return 30;
        }
        return yearIndex;
    }

    private void doModifyDate(String dateStr) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3005")
                .addParams("str",dateStr)
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.i(TAG,s);
                        if (s.equals("0")){
                            showToast(getString(R.string.modifySuccess));
                            if (popupDate != null){
                                popupDate.dismiss();
                                popupDate = null;
                            }
                        }
                    }
                });
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }



    /**修改头像弹出界面*/
    protected PopupWindow popupTime;
    private WheelView hourWheelView,miniteWheelView,secondWheelView;
    private TextView timeCancelTextview;

    private TextView timeComfirmTextview;

    private String currentHour ,currentMinite,currentSecond;

    private ArriveTimeWheelViewAdapter hourAdapter;
    private ArriveTimeWheelViewAdapter secondAdapter;
    private ArriveTimeWheelViewAdapter miniteAdapter;

    private ArrayList<String> hourList = new ArrayList<>();
    private ArrayList<String> miniteList = new ArrayList<>();
    private ArrayList<String> secondList = new ArrayList<>();

    private void showTimePopUp() {
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.popup_time_choose, null);
        popupTime = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTime.setBackgroundDrawable(new BitmapDrawable());
        //点击空白处时，隐藏掉pop窗口
        popupTime.setFocusable(true);
        popupTime.setOutsideTouchable(true);
        //
        popupTime.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        hourWheelView = (WheelView)v.findViewById(R.id.hour);
        secondWheelView = (WheelView)v.findViewById(R.id.second);
        miniteWheelView = (WheelView)v.findViewById(R.id.minite);
        for (int i = 1; i < 25; i++) {
            if (i < 10){
                hourList.add("0"+ i);
            }else{
                hourList.add(""+i);
            }
        }
        hourAdapter = new ArriveTimeWheelViewAdapter(this, hourList, 0, maxsize, minsize);
        hourWheelView.setVisibleItems(5);
        hourWheelView.setCyclic(true);
        hourWheelView.setViewAdapter(hourAdapter);
        hourWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, hourAdapter);
                Log.i(TAG,"onScrollingFinished" +"yearWheelView" + "  "+ currentText);
            }
        });

        for (int i = 0; i < 60; i++) {
            if (i < 10){
                secondList.add("0"+i);
            }else {
                secondList.add(""+ i );
            }
        }

        secondAdapter = new ArriveTimeWheelViewAdapter(this, secondList, 0, maxsize, minsize);
        secondWheelView.setVisibleItems(5);
        secondWheelView.setViewAdapter(secondAdapter);
        secondWheelView.setCyclic(true);
        //
        for (int i = 0; i < 60; i++) {
            if (i < 10){
                miniteList.add("0"+ i);
            }else{
                miniteList.add(""+i);
            }
        }
        miniteAdapter = new ArriveTimeWheelViewAdapter(this, miniteList, 0, maxsize, minsize);
        miniteWheelView.setVisibleItems(5);
        miniteWheelView.setCyclic(true);
        miniteWheelView.setViewAdapter(miniteAdapter);
        miniteWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) miniteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, miniteAdapter);
                Log.i(TAG,"onScrollingFinished" +"yearWheelView" + "  "+ currentText);
            }
        });

        hourWheelView.addChangingListener(this);
        miniteWheelView.addChangingListener(this);
        secondWheelView.addChangingListener(this);

        timeCancelTextview = (TextView)v.findViewById(R.id.tv_cancel);
        timeCancelTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupTime != null){
                    popupTime.dismiss();
                    popupTime = null;
                }
            }
        });

        timeComfirmTextview = (TextView)v.findViewById(R.id.tv_submit);
        timeComfirmTextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentHour == null)
                    currentHour = (String)hourAdapter.getItemText(hourWheelView.getCurrentItem());
                if (currentMinite == null)
                    currentMinite = (String)miniteAdapter.getItemText(miniteWheelView.getCurrentItem());
                if (currentSecond == null){
                    currentSecond = (String)secondAdapter.getItemText(secondWheelView.getCurrentItem());
                }
                String modifyStr = currentHour + ""+ currentMinite+ ":" + currentSecond;
                doModifyTime(modifyStr);
            }
        });


        popupTime.setAnimationStyle(R.style.popupWindowStyle);
        popupTime.showAtLocation(findViewById(R.id.rl_my_autoset), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);

    }


    /***
     * 更改时间
     * @param modifyStr
     */
    private void doModifyTime(String modifyStr) {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3006")
                .addParams("str",modifyStr)
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (s.equals("0")){
                            showToast(getString(R.string.modifySuccess));
                            if (popupTime != null){
                                popupTime.dismiss();
                                popupTime = null;
                            }
                        }
                    }
                });
    }


    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        switch (button.getId()) {
            case R.id.auto_setting_cb:
                if (isChecked) {
                    Log.i("..1....", "自动" + flag);
                    rldate.setClickable(false);
                    rltime.setClickable(false);
                    tvdate.setTextColor(Color.parseColor("#eeeeeeee"));
                    tvtime.setTextColor(Color.parseColor("#eeeeeeee"));
                } else {
                    rldate.setClickable(true);
                    rltime.setClickable(true);
                    tvdate.setTextColor(Color.BLACK);
                    tvtime.setTextColor(Color.BLACK);
                }
                break;
            case R.id.switch_24_hour_cb:
                if (isChecked) {
                    Log.i("..1....", "0" + flag);
                    flag = true;
                } else {
                    Log.i("..1....", "点击时值" + flag);
                    flag = false;

                }
                break;

            default:
                break;
        }


    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private String TAG = "settime_activity";

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == yearWheelView){
            Log.i(TAG,"onChanged" +"  yearWheelView"+ oldValue +"  ;" +newValue);
            String currentText = (String) yearAdapter.getItemText(yearWheelView.getCurrentItem());
//            String currentText = yearList.get(newValue);
            currentYear = currentText;
            setTextviewSize(currentText, yearAdapter);

        }else if (wheel == monthWheelView){

            String currentText = (String) monthAdapter.getItemText(monthWheelView.getCurrentItem());
            currentMonth = currentText;
            setTextviewSize(currentText, monthAdapter);
            Log.i(TAG,"onChanged" +"  monthWheelView"+ "  currentMonth:" + currentMonth);


            Calendar calendar = Calendar.getInstance();
            if (currentYear == null){
                currentYear = (String)yearAdapter.getItemText(yearWheelView.getCurrentItem());
            }
            calendar.set(Calendar.YEAR, Integer.parseInt(currentYear));
            calendar.set(Calendar.MONTH, Integer.parseInt(currentMonth) - 1);

            int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.i(TAG,"MAX_DAY:"+" "+ maxDays);
            dayList.clear();
            for (int i = 1; i <= maxDays; i++) {
                if (i < 10){
                    dayList.add("0"+i);
                }else{
                    dayList.add(String.valueOf(i));
                }
            }

            dayAdapter = new ArriveTimeWheelViewAdapter(SetTimeActivity.this, dayList, 0, maxsize, minsize);
            dayWheelView.setViewAdapter(dayAdapter);
            dayWheelView.setCurrentItem(0);


        }else if (wheel == dayWheelView){
            Log.i(TAG,"onChanged" +"dayWheelView"+ oldValue +"  ;" +newValue);
            String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
            currentDay = currentText;
            setTextviewSize(currentText, dayAdapter);
        }else if (wheel == hourWheelView){
            String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
            currentHour = currentText;
            setTextviewSize(currentText, hourAdapter);
        }else if (wheel == miniteWheelView){
            String currentText = (String) miniteAdapter.getItemText(wheel.getCurrentItem());
            currentMinite = currentText;
            //strArea = mAreaDatasMap.get(strCity)[newValue];
            setTextviewSize(currentText, miniteAdapter);
        }else if (wheel == secondWheelView){
            String currentText = (String) secondAdapter.getItemText(wheel.getCurrentItem());
            currentSecond = currentText;
            //strArea = mAreaDatasMap.get(strCity)[newValue];
            setTextviewSize(currentText, secondAdapter);
        }


    }


    //
    public void setTextviewSize(String curriteItemText, ArriveTimeWheelViewAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }
}
