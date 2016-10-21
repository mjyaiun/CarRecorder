package com.mitu.carrecorder.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.httputils.callback.StringCallback;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.bean.UserBean;
import com.mitu.carrecorder.net.RequestMethodName;
import com.mitu.carrecorder.net.Response;
import com.mitu.carrecorder.utils.ImageUtils;
import com.mitu.carrecorder.utils.PhotoUtils;
import com.mitu.carrecorder.utils.SystemUtils;

import java.io.File;

import okhttp3.Call;

public class PersonalInfoActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout rlnick, rlpass;
    private TextView tv, tvmima;
    private SimpleDraweeView ivtoux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ivtoux.setImageURI(Uri.parse(sp.getUserImage()));


    }

    private UserBean userBean;

    // 修改头像接口
    private void doModifyPhoto() {

        //联网请求修改
        if(bitmapHeader == null){
            showToast(getString(R.string.youNotChoosePhoto));
            return;
        }
        // TODO Auto-generated method stub
        initProgressDialog(getString(R.string.onUpload));

        OkHttpUtils.post()
                .url(SystemUtils.getFullAddressUrl(RequestMethodName.LOGIN))
                .addParams("flag", "4")
                .addParams("userid", String.valueOf(sp.getId()))
                .addParams("imagelinks", ImageUtils.getBase64Str(bitmapHeader))
                .addParams("imagetype", "png")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissDialog();

                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.i("OkHttpUtils","modifyPhoto" + s);
                        dismissDialog();
                        userBean = Response.parseLoginResult(s);
                        if (userBean.result) {
                            sp.saveUserImage(userBean.user.getImage());
                            ivtoux.setImageURI(Uri.parse(userBean.user.getImage()));
                            showToast(R.string.modifySuccess);
                        }else{
                            showToast(userBean.msg);
                        }
                    }
                });

    }


    @Override
    public void initView() {
        setTitle(getString(R.string.grxx));
        initTitleBack();
        //
        rlnick = (RelativeLayout) findViewById(R.id.rl_my_setphon);
        tv = (TextView) findViewById(R.id.tvnick);
        if (sp.getIsLogin()){
            tv.setText(sp.getUserName());
        }else {
            tv.setText(R.string.tourist);
        }
        //tv.setOnClickListener(this);

        //rlnick.setOnClickListener(this);
        //
        rlpass = (RelativeLayout) findViewById(R.id.rl_my_setmima);

        tvmima = (TextView) findViewById(R.id.tvmima);
        if (sp.getIsLogin()){//没有登陆的用户不能设置密码
            rlpass.setOnClickListener(this);
        }
        //
        ivtoux = (SimpleDraweeView) findViewById(R.id.iv_photo);
        ivtoux.setImageURI(Uri.parse(sp.getUserImage()));
        ivtoux.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_setmima:
                startActivity(new Intent(this, ModifyPwdActivity.class));

                break;

            case R.id.rl_my_setphon:
                //startActivity(new Intent(this, NickNameActivity.class));
                changeActivity(NickNameActivity.class);
                break;
            case R.id.iv_photo:
                //startActivity(new Intent(this, PhotoActivity.class));
                showPhotoPopup();
                break;
            case R.id.buttonTakePhoto:
                PhotoUtils.takePhoto(this);//拍照
                break;
            case R.id.buttonPhotoAlbum:
                PhotoUtils.requestGalleryPhoto(this);//从手机相册选择
                break;
            default:
                break;

        }

    }


    /**修改头像弹出界面*/
    protected PopupWindow popupTakePhoto;
    /**拍照按钮*/
    protected TextView buttonTakePhoto;
    /**相册按钮*/
    protected TextView buttonAlbum;

    /**
     * 初始化拍照弹出界面
     */
    protected void showPhotoPopup(){
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.popup_take_photo, null);
        popupTakePhoto = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTakePhoto.setBackgroundDrawable(new BitmapDrawable());
        //点击空白处时，隐藏掉pop窗口
        popupTakePhoto.setFocusable(true);
        popupTakePhoto.setOutsideTouchable(true);
        //
        popupTakePhoto.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        //初始化按钮事件
        buttonTakePhoto = (TextView)v.findViewById(R.id.buttonTakePhoto);
        buttonAlbum = (TextView)v.findViewById(R.id.buttonPhotoAlbum);
        TextView buttonCancel = (TextView)v.findViewById(R.id.buttonCancel);
        buttonTakePhoto.setOnClickListener(this);
        buttonAlbum.setOnClickListener(this);
        buttonCancel.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(popupTakePhoto != null){
                    popupTakePhoto.dismiss();
                    popupTakePhoto = null;
                }
            }
        });
        //点击关闭
        v.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if(popupTakePhoto != null){
                    popupTakePhoto.dismiss();
                    popupTakePhoto = null;
                }
            }
        });
        popupTakePhoto.setAnimationStyle(R.style.popupWindowStyle);
        popupTakePhoto.showAtLocation(findViewById(R.id.id_toolbar), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);
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


    /**头像图片*/
    private Bitmap bitmapHeader;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case PhotoUtils.TAKE_PHOTO://拍照
                    try{
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;//缩放图片为原来的1/4
                        bitmapHeader = BitmapFactory.decodeFile(PhotoUtils.TAKE_PHOTO_FILE_PATH, options);
                        //myHeadPhoto.setImageBitmap(ImageUtils.toRoundCorner(ImageUtils.scaleBitmap(bitmapHeader, BEApplication.width / 4)));
                        if(popupTakePhoto != null){
                            popupTakePhoto.dismiss();
                            popupTakePhoto = null;
                        }
                        //
                        doModifyPhoto();//
                    }catch(Exception e){

                    }
                    break;
                case PhotoUtils.GET_IMAGE_LIST://获得图片列表
                    if(data != null){
                        try{
                            Bitmap bitmap = ImageUtils.getBitmapFromUri(this.getContentResolver(), data);
                            bitmapHeader = bitmap;
                            //myHeadPhoto.setImageBitmap(ImageUtils.toRoundCorner(ImageUtils.scaleBitmap(bitmapHeader, BEApplication.width / 4)));
                            if(popupTakePhoto != null){
                                popupTakePhoto.dismiss();
                                popupTakePhoto = null;
                            }
                            doModifyPhoto();//
                        }catch(Exception e){

                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
