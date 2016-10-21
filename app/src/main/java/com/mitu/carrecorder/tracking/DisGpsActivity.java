package com.mitu.carrecorder.tracking;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

public class DisGpsActivity extends BaseActivity {
	  //地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dis_gps);
	}
	@Override
	public void initView() {
		setTitle("导航");
		 //初始化地图
       mMapView = (MapView) findViewById(R.id.bmapview);
       mBaidumap = mMapView.getMap();
//       mBtnPre = (Button) findViewById(R.id.pre);
//       mBtnNext = (Button) findViewById(R.id.next);
//       mBtnPre.setVisibility(View.INVISIBLE);
//       mBtnNext.setVisibility(View.INVISIBLE);
//       //地图点击事件处理
//       mBaidumap.setOnMapClickListener(this);
		
	}
	@Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

}
