package com.mitu.carrecorder.trackingtu;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.entiy.Mileage;
import com.mitu.carrecorder.entiy.MileageRecord;
import com.mitu.carrecorder.utils.DrivingRouteOverlay;
import com.mitu.carrecorder.utils.OverlayManager;
import com.mitu.carrecorder.utils.SpHelper;

import java.util.ArrayList;
import java.util.List;

/***
 * 我的轨迹地图显示页
 */
public class TrackGjActivity extends BaseActivity implements
        OnMapClickListener, OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
    // 定位相关


//	private static final int accuracyCircleFillColor = 0xAAFFFF88;
//	private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null; // 地图View
    BaiduMap mBaidumap = null;
    private GeoCoder gcSearch = null; //
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    private TextView popupText = null;// 泡泡view
    boolean isFirstLoc = true;
    private static final int DATA_COMPLETED = 0x123;
    private TextView tvTime, tvDistance, tvSpeed, tvOrigin, tvTerminal, tvDate;
    private ImageView ivMap;
    private LinearLayout llMapview;
    private Mileage mileage;
    private List<MileageRecord> mrList;
    private String filePath;
    private SpHelper sp;
    // 搜索相关
    RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_gj);
        //sp = new SpHelper(this);
        mileage = (Mileage) getIntent().getSerializableExtra("mileage");
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        // 定位初始化
        // 初始化搜索模块，注册事件监听f
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true); //打开GPRS  
        option.setAddrType("all");//返回的定位结果包含地址信息  
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02  
        // option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
        option.disableCache(false);//禁止启用缓存定位  
        mLocationClient.setLocOption(option);

        mLocationClient.start();
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());

            // 反Geo搜索
//			gcSearch.reverseGeoCode(new ReverseGeoCodeOption()
//					.location(ptCenter));

            //重置浏览节点的路线数据
//            route = null;
//           mBtnPre.setVisibility(View.INVISIBLE);
//           mBtnNext.setVisibility(View.INVISIBLE);
//            mBaidumap.clear();
            //设置起终点信息，对于tranist search 来说，城市名无意义

//           PlanNode stNode = PlanNode.withCityNameAndPlaceName( "商丘",result.getAddress() );
//           PlanNode enNode = PlanNode.withCityNameAndPlaceName("商丘",getIntent().getStringExtra("address"));
//           PlanNode stNode=PlanNode.withLocation(ptCenter);
//           LatLng ptCenter2 = new LatLng(32.2,117.6);
//           PlanNode enNode=PlanNode.withLocation(ptCenter2);
//           mSearch.drivingSearch((new DrivingRoutePlanOption())
//                   .from(stNode)
//                   .to(enNode));
        }
    }

    private void getRouteLine() {
        // TODO Auto-generated method stub

        initProgressDialog(getString(R.string.onLoading));
        /*new Thread() {
            public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("flag", "2");
				params.addBodyParameter("username", sp.getUserName());
				params.addBodyParameter("day", mileage.getDate());;
				HttpUtils utils = new HttpUtils();
				utils.configSoTimeout(30000);
				utils.send(HttpRequest.HttpMethod.POST,
						NetField.EACH_MILLEAGE_IP, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								cancelDialog();
								Toast.makeText(TrackGjActivity.this,
										"连接超时，请检查网络设置或稍后再试！",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								// TODO Auto-generated method stub
								cancelDialog();
								String errorString = JsonUtils
										.getMessage(arg0.result);
								String resultCode = JsonUtils
										.getResultCode(arg0.result);
								Log.i("com.mh.fjkj", "arg0=" + arg0.result);
								if (resultCode.equals("1")) {
									mrList = JsonUtils
											.getMileageRecord(arg0.result);
									doProcess();// 绘制路线
								} else {
									Toast.makeText(TrackGjActivity.this,
											errorString, Toast.LENGTH_SHORT)
											.show();
								}
							}

						});
			}
		}.start();*/
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.orbit));
        initTitleBack();
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

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaidumap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mSearch.destroy();
        super.onDestroy();
    }

    private void init() {
        // TODO Auto-generated method stub
        mrList = new ArrayList<MileageRecord>();
        for (int i = 0; i < 10; i++) {
            MileageRecord mileageRecord = new MileageRecord();
            mileageRecord.setLat(30 + i + "");
            mileageRecord.setLng(110 + i + "");
            mrList.add(mileageRecord);
        }
        doProcess();
    }

    // 绘制路线
    private void doProcess() {
        // TODO Auto-generated method stub
        // 重置浏览节点的路线数据
        mBaidumap.clear();
        Log.i("com.mh.fjkj.menu", "mrList=" + mrList.toString());
        for (int i = 0; i < mrList.size(); i++) {
            if (i == mrList.size() - 1) {
                return;
            }
            Log.i("com.mh.fjkj.menu", "mrList22=" + mrList.get(i));
            MileageRecord stMileage = mrList.get(i);
            LatLng stLatLng, endLatLng;
            PlanNode stNode = null;
            PlanNode endNode = null;
            if (stMileage.getLat() != null && !stMileage.getLat().equals("")
                    && stMileage.getLng() != null
                    && !stMileage.getLng().equals("")) {
                double lat = Double.parseDouble(stMileage.getLat());
                double lng = Double.parseDouble(stMileage.getLng());
                stLatLng = new LatLng(lat, lng);
                stNode = PlanNode.withLocation(stLatLng);
            }
            Log.i("com.mh.fjkj.menu", "mrList33=" + mrList.get(i + 1));
            MileageRecord endMileage = mrList.get(i + 1);
            if (endMileage.getLat() != null && !endMileage.getLat().equals("")
                    && endMileage.getLng() != null
                    && !endMileage.getLng().equals("")) {
                double lat = Double.parseDouble(endMileage.getLat());
                double lng = Double.parseDouble(endMileage.getLng());
                endLatLng = new LatLng(lat, lng);
                endNode = PlanNode.withLocation(endLatLng);
            }
            if (stNode != null && endNode != null) {
                Log.i("com.mh.fjkj.menu", "开始绘制路线");
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
                        stNode).to(endNode));
            }
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        mBaidumap.hideInfoWindow();

    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrackGjActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Toast.makeText(TrackGjActivity.this, result.getAddressDetail().city, Toast.LENGTH_LONG)
                .show();

        //重置浏览节点的路线数据
        route = null;
//        mBtnPre.setVisibility(View.INVISIBLE);
//        mBtnNext.setVisibility(View.INVISIBLE);
        mBaidumap.clear();
        //设置起终点信息，对于tranist search 来说，城市名无意义

        PlanNode stNode = PlanNode.withCityNameAndPlaceName("商丘", result.getAddress());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("商丘", getIntent().getStringExtra("address"));

        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));

    }

}
