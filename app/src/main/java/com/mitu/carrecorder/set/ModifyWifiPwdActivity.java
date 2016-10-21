package com.mitu.carrecorder.set;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.net.CommandCallBack;

import butterknife.Bind;
import okhttp3.Call;

public class ModifyWifiPwdActivity extends BaseActivity {


    @Bind(R.id.pwd_etv)
    EditText pwdEtv;
    @Bind(R.id.pwd_again_etv)
    EditText pwdAgainEtv;
    @Bind(R.id.save_pwd_btn)
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_wifi_pwd);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.modifyWifiPwd));
        initTitleBack();
        //
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == saveBtn){
            final String pwd = pwdEtv.getText().toString().trim();
            String pwdAgain = pwdAgainEtv.getText().toString().trim();

            if(pwd.length() == 0 || pwdAgain.length() == 0){
                showMyDialog(this,getString(R.string.pwdNotAllowEmpty));
                return;
            }

            if (!pwd.equals(pwdAgain)){
                showMyDialog(this,getString(R.string.twiceNotSame));
                return;
            }

            showMyDialog(this, getString(R.string.saveToReConnectedDevice), getString(R.string.save),
                    getString(R.string.cancel), new OnMyDialogPositiveButtonClickListener() {
                        @Override
                        public void onMyPositiveButtonClick(Context context) {
                            doModifyPwd(pwd);
                        }
                    });



        }
    }

    private void doModifyPwd(String pwdStr) {

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3004")
                .addParams("str",pwdStr)
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (s.equals("0")){
//                            showToast(getString(R.string.modifySuccess));
                            doReConnectWifi();
                            finish();
                        }
                    }
                });



    }

    private void doReConnectWifi() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3018")
                .addParams("par","1")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        //net no response
                    }

                });

    }


}
