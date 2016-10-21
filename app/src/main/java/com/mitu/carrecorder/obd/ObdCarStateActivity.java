package com.mitu.carrecorder.obd;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;


public class ObdCarStateActivity extends BaseActivity implements OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd_car_state);

        //ButterKnife.bind(this);

    }


   /* @Bind(R.id.id_toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView titleTextview;*/


    public void initView() {
        setTitle(getString(R.string.carState));
        initTitleBack();


    }

    @Override
    public void onClick(View v) {

    }
}
