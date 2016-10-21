package com.mitu.carrecorder.set;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guotiny.httputils.OkHttpUtils;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.InfoSettingListviewAdapter;
import com.mitu.carrecorder.adapter.SwitchSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.utils.DeviceSettingsCache;

import java.util.ArrayList;

import butterknife.Bind;
import okhttp3.Call;

public class PhotoVideoSettingActivity extends BaseActivity  implements AdapterView.OnItemClickListener{
	// private RelativeLayout rlpaizhao,rlluying,rlxunhuan;

	@Bind(R.id.photo_video_setting_lv_1)
	ListView listViewTop;

	@Bind(R.id.photo_video_setting_lv_2)
	ListView listViewBottom;

	private ArrayList<BEMenuItem> topArray;
	private ArrayList<BEMenuItem> buttomArray;

	private InfoSettingListviewAdapter adapterTop;
	private SwitchSettingListviewAdapter adapterBottom;

	private String[] array1;
	private String[] array2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_video_setting);
		//

	}

	/***拍照分辨率*/
	private int photoRe ;
	/***录影分辨率*/
	private int movieRe;
	private int movieLoop;
	private int motionDection;
	private int movieVideo;
	private int movieWaterStamp;


	private void getStatus() {
		photoRe = DeviceSettingsCache.getCommandState("1002");
		movieRe = DeviceSettingsCache.getCommandState("2002");
		movieLoop = DeviceSettingsCache.getCommandState("2003");
		//
		motionDection = DeviceSettingsCache.getCommandState("2006");
		movieVideo = DeviceSettingsCache.getCommandState("2007");
		movieWaterStamp = DeviceSettingsCache.getCommandState("2008");//0 : off  1: on
	}


	@Override
	protected void onResume() {
		super.onResume();
		//
		refreshData();

	}

	private String[] arrayPhoto ;
	private String[] arrayVideo ;
	private String[] loopArray ;

	@Override
	public void initView() {
		getStatus();
		//
	    setTitle(getString(R.string.photoVideoSetting));
		initTitleBack();
		//
		array1 = getResources().getStringArray(R.array.photoVideoSettingArray1);
		array2 = getResources().getStringArray(R.array.photoVideoSettingArray2);
		//
		arrayPhoto = getResources().getStringArray(R.array.takephotoResolutionArray);
		arrayVideo = getResources().getStringArray(R.array.videoResoluationArray);
		loopArray = getResources().getStringArray(R.array.loopArray);
		//

		topArray = new ArrayList<>();
		buttomArray = new ArrayList<>();

		//
		for (int i = 0; i < array1.length; i++) {
			BEMenuItem item = new BEMenuItem(array1[i]);
			if (i == 0){
				int type = photoRe;
				item.moreInfo = arrayPhoto[type];
			}else if (i == 1){
				int videoType = movieRe;
				item.moreInfo = arrayVideo[videoType];
			}else if (i == 2){
				int loopType = movieLoop;
				item.moreInfo = loopArray[loopType];
			}
			topArray.add(item);
		}

		adapterTop = new InfoSettingListviewAdapter(this,topArray);
		listViewTop.setAdapter(adapterTop);
		listViewTop.setOnItemClickListener(this);
		//
		for (int i = 0; i < array2.length; i++) {
			BEMenuItem item = new BEMenuItem(array2[i]);
			if (i == 0){
				if (motionDection == 1){
					item.isCheck = true;
				}else if(motionDection == 0){
					item.isCheck = false;
				}

			}else if (i == 1){
				if (movieVideo == 1){
					item.isCheck = true;
				}else{
					item.isCheck = false;
				}

			}else if (i == 2){
				if (movieWaterStamp == 1){
					item.isCheck = true;
				}else{
					item.isCheck = false;
				}

			}
			buttomArray.add(item);

		}


		adapterBottom = new SwitchSettingListviewAdapter(this,buttomArray);
		listViewBottom.setAdapter(adapterBottom);
		adapterBottom.setOnItemCheckChangeListener(new SwitchSettingListviewAdapter.OnItemCheckChangeListener() {
			@Override
			public void onButtonClick(Context context, int position, boolean ischeck) {
				Log.i("TAG+click","position:" + position + buttomArray.get(position).isCheck + "   "+ischeck);

				int status;
				switch (position){
					case 0:
						if (ischeck) {
							status = 1;
						} else {status = 0;}
						doModifyStatus(2006, status);
						break;

					case 1:
						if (ischeck) {
							status = 1;
						} else {status = 0;}
						doModifyStatus(2007, status);
						break;

					case 2:
						if (ischeck) {
							status = 1;
						} else {status = 0;}
						doModifyStatus(2008, status);
						break;
				}

/*
				if (position == 0){
                    DeviceSettingsCache.setVideoMotionDetection(ischeck);
                }else if (position == 1){
                    DeviceSettingsCache.setVideoVoice(ischeck);
                }else if (position == 2){
                    DeviceSettingsCache.setVideoWaterMark(ischeck);
                }

				buttomArray.get(position).isCheck = ischeck;

				for (int i = 0; i < buttomArray.size(); i++) {
					Log.i("TAG","position:" + buttomArray.get(i).isCheck + "  "+ i);
				}
				adapterBottom.notifyDataSetChanged();*/
			}
		});
		//

	}


	/**
	 * 刷新数据
	 */
	private void refreshData(){
		for (int i = 0; i < topArray.size(); i++) {
			if (i == 0){
				int type = photoRe;
				topArray.get(0).moreInfo = arrayPhoto[type];
			}else if (i == 1){
				int videoType = movieRe;
				topArray.get(1).moreInfo = arrayVideo[videoType];
			}else if (i == 2){
				int loopType = movieLoop;
				topArray.get(2).moreInfo = loopArray[loopType];
			}
		}
		adapterTop.notifyDataSetChanged();

		//
		for (int i = 0; i < buttomArray.size(); i++) {
			if (i == 0){

				if (motionDection == 0){
					buttomArray.get(0).isCheck = false;
				}else {
					buttomArray.get(0).isCheck = true;
				}

			}else if (i == 1){
				if (movieVideo == 0){
					buttomArray.get(1).isCheck = false;
				}else {
					buttomArray.get(1).isCheck = true;
				}
			}else if (i == 2){
				if (movieWaterStamp == 0){
					buttomArray.get(2).isCheck = false;
				}else {
					buttomArray.get(2).isCheck = true;
				}
			}
		}
	}

	private static final int REQ_PHOTO_RE = 11;
	private static final int REQ_MOVIE_RE = 12;
	private static final int REQ_MOVIE_LOOP = 13;
	//



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == listViewTop){
			switch (position){
				case 0:
					Bundle bundle = new Bundle();
					bundle.putInt("type",photoRe);
					changeActivityForResult(TakePhotoResolutionSettingActivity.class,bundle,REQ_PHOTO_RE);
					break;
				case 1:
					Bundle movie = new Bundle();
					movie.putInt("type",movieRe);
					changeActivityForResult(VideoResolutionSettingActivity.class,movie,REQ_MOVIE_RE);
					break;
				case 2:
					Bundle movieLoopBundle = new Bundle();
					movieLoopBundle.putInt("type",movieLoop);
					changeActivityForResult(VideoLoopSettingActivity.class,movieLoopBundle,REQ_MOVIE_LOOP);
					break;

			}
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case REQ_PHOTO_RE:
				if (resultCode == TakePhotoResolutionSettingActivity.RESULT_OK){
					Bundle bundle = data.getExtras();
					int type = bundle.getInt("type");
					doModifyStatus(1002,type);
				}
				break;
			case REQ_MOVIE_RE:
				if (resultCode == VideoResolutionSettingActivity.RESULT_OK){
					Bundle bundle = data.getExtras();
					int type = bundle.getInt("type");
					doModifyStatus(2002,type);
				}

				break;
			case REQ_MOVIE_LOOP:
				if (resultCode == VideoLoopSettingActivity.RESULT_OK){
					Bundle bundle = data.getExtras();
					int type = bundle.getInt("type");
					doModifyStatus(2003,type);
				}
				break;

		}
	}

	private void doModifyStatus(final int cmd, final int parm) {
		OkHttpUtils.get()
				.url(BEConstants.ADDRESS_IP_DEVICE)
				.addParams("custom","1")
				.addParams("cmd",String.valueOf(cmd))
				.addParams("par",String.valueOf(parm))
				.build()
				.execute(new CommandCallBack() {
					@Override
					public void onError(Call call, Exception e, int i) {

					}

					@Override
					public void onResponse(String s, int i) {
						//showToast("选择了："+ cmd  + "    " + parm + "  "+  s);
						switch (cmd){
							case 1002:
								DeviceSettingsCache.setCommandState("1002",parm);
								photoRe = parm;
								refreshData();
								break;
							case 2002:
								DeviceSettingsCache.setCommandState("2002",parm);
								movieRe = parm;
								refreshData();
								break;
							case 2003:
								DeviceSettingsCache.setCommandState("2003",parm);
								movieLoop = parm;
								refreshData();
								break;
							case 2006:
								DeviceSettingsCache.setCommandState("2006",parm);
								motionDection = parm;
								refreshData();
								break;
							case 2007:
								DeviceSettingsCache.setCommandState("2007",parm);
								movieVideo = parm;
								refreshData();
								break;
							case 2008:
								DeviceSettingsCache.setCommandState("2008",parm);
								movieWaterStamp = parm;
								refreshData();
								break;
						}
					}
				});

	}
}
