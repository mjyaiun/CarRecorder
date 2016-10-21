package com.mitu.carrecorder.phvedio;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mitu.carrecorder.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 照片视频
 *
 * @author Administrator
 */
public class PhotoVideoActivity extends FragmentActivity implements OnClickListener, OnCheckedChangeListener {

    private ImageView ivBack, toolbarEdit, toolbarDownload;
    private FrameLayout flPhotoVideo;
    private RadioGroup rgMenu;
    private RadioButton rbtnPhoto, rbtnVideo;
    private FragmentPagerAdapter fragmentAdapter;
    private int currentPosition;
    private Button btnDelete, btnShare;

    private TextView tvTitle, tvDelete, tvShare, tvselect, toolbarCancel;

    @Bind(R.id.tv_tan_xiazai)
    TextView downloadTv;
    @Bind(R.id.ll_bottom_edit)
    LinearLayout editorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_video);
        //
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        // TODO Auto-generated method stub
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        toolbarCancel = (TextView) findViewById(R.id.tv_photo_cancel);
        toolbarCancel.setOnClickListener(this);
        toolbarDownload = (ImageView) findViewById(R.id.iv_photo_download);
        toolbarDownload.setOnClickListener(this);
        //
        tvTitle = (TextView) findViewById(R.id.tv_title);
        toolbarEdit = (ImageView) findViewById(R.id.iv_photo_bianji);
        toolbarEdit.setOnClickListener(this);
        flPhotoVideo = (FrameLayout) findViewById(R.id.fl_photo_container);
        rgMenu = (RadioGroup) findViewById(R.id.rg_photo);
        rgMenu.setOnCheckedChangeListener(this);
        FragmentManager fManager = getSupportFragmentManager();
        fragmentAdapter = new innerFragmentPagerAdapter(fManager);
        currentPosition = R.id.rbtn_photo;
        editorLayout = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        rbtnPhoto = (RadioButton) findViewById(R.id.rbtn_photo);
        rbtnVideo = (RadioButton) findViewById(R.id.rbtn_video);
        //
        tvDelete = (TextView) findViewById(R.id.tv_tan_delete);
        tvShare = (TextView) findViewById(R.id.tv_tan_share);
        tvDelete.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        //
        downloadTv.setOnClickListener(this);
    }

    public class innerFragmentPagerAdapter extends FragmentPagerAdapter {

        public innerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FragmentPhoto();
                    break;
                case 1:
                    fragment = new FragmentVideo();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 2;
        }

    }

    private int fragmentIndex = 0;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub

        switch (checkedId) {
            case R.id.rbtn_photo:
                tvTitle.setText(getString(R.string.myPhoto));
                rbtnPhoto.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbtnVideo.setBackgroundColor(getResources().getColor(R.color.color_gray));
                fragmentIndex = 0;
                break;
            case R.id.rbtn_video:
                tvTitle.setText(getString(R.string.myVideo));
                rbtnPhoto.setBackgroundColor(getResources().getColor(R.color.color_gray));
                rbtnVideo.setBackgroundColor(getResources().getColor(R.color.color_white));
                fragmentIndex = 1;
                break;
        }
        Object fragment = fragmentAdapter.instantiateItem(flPhotoVideo, fragmentIndex);
        fragmentAdapter.setPrimaryItem(flPhotoVideo, fragmentIndex, fragment);
        fragmentAdapter.finishUpdate(flPhotoVideo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        rgMenu.check(currentPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPosition = rgMenu.getCheckedRadioButtonId();
    }



    public static final int CANCEL_TOOLBAR = 22;
    public static final int DOWNLOAD_TOOLBAR = 11;
    public static final int EDIT_TOOLBAR = 12;

    public static final int DOWNLOAD_BUTTON = 21;
    public static final int DELETE_BUTTON = 23;
    //
    public static final int MODE_NORMAL = 99;
    public static final int MODE_DELETE = 98;
    public static final int MODE_DOWNLOAD = 97;
    public int pageMode = MODE_NORMAL;

    public int getPageMode() {
        return pageMode;
    }

    //点击加号监听器
    private OnMyBtnClickListener mOnMyBtnClickListener ;

    public interface OnMyBtnClickListener
    {
        void onButtonClick(Context context, int type);
    }


    public void setOnMyBtnClickListener(OnMyBtnClickListener listener)
    {
        mOnMyBtnClickListener = listener;
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_photo_download:
                tvTitle.setText(getString(R.string.download));
                //
                pageMode = MODE_DOWNLOAD;
                mOnMyBtnClickListener.onButtonClick(this,DOWNLOAD_TOOLBAR);
                toolbarCancel.setVisibility(View.VISIBLE);
                toolbarDownload.setVisibility(View.GONE);
                toolbarEdit.setVisibility(View.GONE);
                ivBack.setVisibility(View.GONE);
                downloadTv.setVisibility(View.VISIBLE);
                editorLayout.setVisibility(View.GONE);
                break;
            case R.id.iv_photo_bianji:
                pageMode = MODE_DELETE;
                mOnMyBtnClickListener.onButtonClick(this,EDIT_TOOLBAR);
                String format = getString(R.string.alreadyChoose);
                String result = String.format(format,0);
                tvTitle.setText(result);
                toolbarCancel.setVisibility(View.VISIBLE);
                toolbarDownload.setVisibility(View.GONE);
                toolbarEdit.setVisibility(View.GONE);
                ivBack.setVisibility(View.GONE);
                downloadTv.setVisibility(View.GONE);
                editorLayout.setVisibility(View.VISIBLE);
                break;
//			break;
            case R.id.tv_photo_cancel:
                pageMode = MODE_NORMAL;
                mOnMyBtnClickListener.onButtonClick(this,CANCEL_TOOLBAR);
                if (fragmentIndex == 0){
                    tvTitle.setText(getString(R.string.myPhoto));
                }else if (fragmentIndex == 1){
                    tvTitle.setText(getString(R.string.myVideo));
                }
                toolbarCancel.setVisibility(view.GONE);
                ivBack.setVisibility(view.VISIBLE);
                toolbarEdit.setVisibility(view.VISIBLE);
                toolbarDownload.setVisibility(view.VISIBLE);
                editorLayout.setVisibility(View.GONE);
                downloadTv.setVisibility(View.GONE);

                break;
            case R.id.tv_tan_delete:
                mOnMyBtnClickListener.onButtonClick(this,DELETE_BUTTON);

                break;
            case R.id.tv_tan_xiazai:
                mOnMyBtnClickListener.onButtonClick(this,DOWNLOAD_BUTTON);

                break;
            case R.id.tv_tan_share:
                //
                ShowUMengShareWindow();
                break;
            default:
                break;
        }
    }


    /****=====================友盟分享===========================*******/

    private void ShowUMengShareWindow(){
        new ShareAction(this).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_custom")){
                            Toast.makeText(PhotoVideoActivity.this,"自定义按钮",Toast.LENGTH_LONG).show();
                        }else {
                            new ShareAction(PhotoVideoActivity.this).withText("来自友盟自定义分享面板")
                                    .setPlatform(share_media)
                                    .setCallback(umShareListener)
                                    .share();
                        }
                    }
                }).open();

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(PhotoVideoActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PhotoVideoActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PhotoVideoActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PhotoVideoActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };




}

