package com.mitu.carrecorder.set;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.utils.MyWifiManager;

import butterknife.Bind;
import okhttp3.Call;

public class ModifyWifiNameActivity extends BaseActivity implements View.OnClickListener{


//    @Bind(R.id.save_btn)
//    Button saveBtn;
    @Bind(R.id.wifi_name_etv)
    EditText nameEtv;
//    @Bind(R.id.wifi_name_tv)
//    TextView nameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_wifi_name);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.modifyWifiName));
        initTitleBack();
        setRightTitleText(getString(R.string.complate));
        //
//        saveBtn.setOnClickListener(this);

        String name = MyWifiManager.getWifiName(this).replace("\"","");
        nameEtv.setText(name);
        nameEtv.setSelection(name.length());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.toobar_right_text:
                final String name = nameEtv.getText().toString().trim();
                if (name .length() == 0){
                    showMyDialog(this,getString(R.string.nameInputEmpty));
                    return;
                }
                showMyDialog(this, getString(R.string.saveToReConnectedDevice), getString(R.string.save),
                        getString(R.string.cancel), new OnMyDialogPositiveButtonClickListener() {
                            @Override
                            public void onMyPositiveButtonClick(Context context) {
                                doModifyWifiName(name);
                            }
                        });

                break;
        }

    }

    private void doModifyWifiName(String str) {

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3003")
                .addParams("str",str)
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
                        //no response
                    }

                });

    }


}
