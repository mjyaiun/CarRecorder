package com.mitu.carrecorder.traffic;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;


public class TrafficIllegalQueryActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout rlchaxun;
    private LinearLayout llshuruxinxi, llsoft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_illegal_query);

    }


    @Override
    public void initView() {
        setTitle(getString(R.string.trafficIllegalQuery));
        initTitleBack();
//        setRightTitleText(getString(R.string.query));
        rlchaxun = (RelativeLayout) findViewById(R.id.rl_my_set);
        llshuruxinxi = (LinearLayout) findViewById(R.id.ll_wz);
        rlchaxun.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        /*case R.id.ll_head:
			CancelSoftKeyBord(view);
			break;
		case R.id.iv_back:
			CancelSoftKeyBord(view);
			finish();*/
//			break;

            case R.id.toobar_right_text:
                changeActivity(MinuteActivity.class);
                break;

            case R.id.rl_my_set:
                llshuruxinxi.setVisibility(view.VISIBLE);
                setRightTitleText(getString(R.string.query));

                break;
            default:
                break;

        }

    }


}
