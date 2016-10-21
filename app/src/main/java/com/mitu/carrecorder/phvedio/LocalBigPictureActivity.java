package com.mitu.carrecorder.phvedio;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.LocalHorizontalScrollViewAdapter;
import com.mitu.carrecorder.utils.SystemUtils;
import com.mitu.carrecorder.view.LocalMyHorizontalScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 大图预览
 *
 * @author Administrator
 */
public class LocalBigPictureActivity extends BaseActivity implements OnClickListener {


    @Bind(R.id.id_local_horizontalScrollView)
    LocalMyHorizontalScrollView mHorizontalScrollView;
    @Bind(R.id.iv_camera)
    SimpleDraweeView bigImageView;
    @Bind(R.id.toolbar_share_img)
    ImageView shareImge;

    private LocalHorizontalScrollViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_big_picture);

    }


    public void initView() {
        // TODO Auto-generated method stub
        setTitle("预览");
        initTitleBack();
        showToolBarShare();
        //
        shareImge.setOnClickListener(this);
//        //
//        ViewGroup.LayoutParams params = bigImageView.getLayoutParams();
//        params.height = BEApplication.height * 4/5;
//        bigImageView.setLayoutParams(params);
//
//        ViewGroup.LayoutParams params1 = mHorizontalScrollView.getLayoutParams();
//        params.height = BEApplication.height /5;
//        mHorizontalScrollView.setLayoutParams(params1);
        //
        getIntentData();
        initHorizontalScrollView();

        //shareVisibility();
//		ivShare.setOnClickListener(this);

    }

    /**
     * 照片数据
     */
    private ArrayList<String> photoList;
    private String dateStr;
    private int position;

    private void getIntentData() {
        if (photoList == null){
            photoList = new ArrayList<>();
        }else
            photoList.clear();

        Bundle bundle = getIntent().getExtras();
        dateStr = bundle.getString("dateStr");
        position = bundle.getInt("gv_posi",0);
        photoList = (ArrayList<String>) bundle.getSerializable("photoList");
        if (photoList.size() == 0)
            return;

    }

    private void initHorizontalScrollView() {
        mAdapter = new LocalHorizontalScrollViewAdapter(this, photoList);
        //添加滚动回调
        mHorizontalScrollView
                .setCurrentImageChangeListener(new LocalMyHorizontalScrollView.CurrentImageChangeListener()
                {
                    @Override
                    public void onCurrentImgChanged(int position, View viewIndicator)
                    {

                        String content = photoList.get(position);
                        String url = SystemUtils.getLocalFilePath()+ content;
                        bigImageView.setImageURI(Uri.parse("file://"+url));
                        viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));

                        setTitle((position+1)+"/"+photoList.size());

                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new LocalMyHorizontalScrollView.OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                String content = photoList.get(position);
                String url = SystemUtils.getLocalFilePath()+ content;
                bigImageView.setImageURI(Uri.parse("file://"+url));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
                //
                setTitle((position+1)+"/"+photoList.size());
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter);  ////////
    }






    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.toolbar_share_img:
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
                            Toast.makeText(LocalBigPictureActivity.this,"自定义按钮",Toast.LENGTH_LONG).show();
                        }else {
                            new ShareAction(LocalBigPictureActivity.this).withText("来自友盟自定义分享面板")
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
                Toast.makeText(LocalBigPictureActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LocalBigPictureActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(LocalBigPictureActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(LocalBigPictureActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
