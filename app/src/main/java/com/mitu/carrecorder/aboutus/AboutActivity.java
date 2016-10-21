package com.mitu.carrecorder.aboutus;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

import butterknife.Bind;

public class AboutActivity extends BaseActivity {


    @Bind(R.id.version_tv)
    TextView versionTv;
    @Bind(R.id.rl_my_aboutus)
    RelativeLayout aboutUsLayout;
    @Bind(R.id.rl_my_userback)
    RelativeLayout feedBackLayout;
    @Bind(R.id.rl_my_item)
    RelativeLayout serviceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.about));
        initTitleBack();
        //
        versionTv.setText("1.0.1");
        //
        aboutUsLayout.setOnClickListener(this);
        feedBackLayout.setOnClickListener(this);
        serviceLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_my_aboutus:
                //关于我们
                changeActivity(AboutUsActivity.class);
                break;
            case R.id.rl_my_userback:
                //用户反馈
                changeActivity(FeedbackActivity.class);
                break;
            case R.id.rl_my_item:
                //服务条款
                changeActivity(ServiceTermActivity.class);
                break;

        }
    }
}
