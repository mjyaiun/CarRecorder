package com.mitu.carrecorder.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;


public class MessageActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout rlgrncar, rlxtmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.message));
        initTitleBack();
        rlgrncar = (RelativeLayout) findViewById(R.id.rl_my_gcmessage);
        rlxtmessage = (RelativeLayout) findViewById(R.id.rl_my_xtmessage);
        rlgrncar.setOnClickListener(this);
        rlxtmessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_gcmessage:
                startActivity(new Intent(this, GencarActivity.class));
                break;
            case R.id.rl_my_xtmessage:
                startActivity(new Intent(this, SystemMessageActivity.class));
                break;
            default:
                break;
        }

    }
}
