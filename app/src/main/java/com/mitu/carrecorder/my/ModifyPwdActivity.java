package com.mitu.carrecorder.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.utils.Validates;

import okhttp3.Call;

public class ModifyPwdActivity extends BaseActivity implements OnClickListener {

    private EditText etNewPsw, etConfirmPsw, oldPwd;
    private Button btnComplete;
    //private SpHelper sp;
    private RelativeLayout resetLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.modifyPwd));
        initTitleBack();
        etNewPsw = (EditText) findViewById(R.id.et_new_password);
        oldPwd = (EditText) findViewById(R.id.et_oldPassword);
        etConfirmPsw = (EditText) findViewById(R.id.et_password_confirm);
        //
        btnComplete = (Button) findViewById(R.id.btn_reset_complet);
        btnComplete.setOnClickListener(this);
        resetLayout = (RelativeLayout) findViewById(R.id.rl_resetpss);
        resetLayout.setOnClickListener(this);
        //sp = new SpHelper(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_reset_complet:

                String newPasw = etNewPsw.getText().toString();
                String confirmPasw = etConfirmPsw.getText().toString();
                String old = oldPwd.getText().toString();

                //原密码不能为空
                if (TextUtils.isEmpty(old.trim())) {
                    showMyDialog(this, getString(R.string.inputOldPwd));
                    return;
                }
                //新密码不能为空
                if (TextUtils.isEmpty(confirmPasw.trim()) || TextUtils.isEmpty(newPasw)) {
                    showMyDialog(this, getString(R.string.newPwdNotAllowEmpty));
                    return;
                }
                //验证新密码
                if (!Validates.checkPwd(confirmPasw)) {
                    showMyDialog(this, getString(R.string.pwdNotStandard));
                    return;
                }

                //验证旧密码
                String oldPwd = sp.getPassword();
                if (oldPwd != null && oldPwd.length() != 0){
                    if (!old.trim() .equals(oldPwd)){
                        showMyDialog(this, getString(R.string.oldPwdNotCorrect));
                        return;
                    }
                }

                //验证两次输入的新密码是否一致
                if (!confirmPasw.equals(newPasw.trim())) {
                    showMyDialog(this, getString(R.string.twiceNotSame));
                    return;
                }

                //通过以上验证则进行修改
                doModifyPassword(confirmPasw);//修改密码

                break;
            case R.id.rl_resetpss:
                CancelSoftKeyBord(view);
            default:
                break;
        }
    }

    private UserBean userBean;
    //修改密码接口
    private void doModifyPassword(final String newPswString) {
        // TODO Auto-generated method stub
        initProgressDialog(getString(R.string.onSaving));

        OkHttpUtils.post()
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.LOGIN))
                .addParams("flag", "5")
                .addParams("username", sp.getUserName())
                .addParams("newpasswd", newPswString)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissDialog();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dismissDialog();
                        //
                        userBean = Response.parseLoginResult(s);
                        Log.i("","modifypWD" + s);
                        if (userBean.result) {
                            sp.saveNickName(userBean.user.getNickname());
                            showToast(getString(R.string.modifySuccess));
                        }else {
                            showToast(getString(R.string.modifyFailed));
                        }

                    }
                });

    }


}
