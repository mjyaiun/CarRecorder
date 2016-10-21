package com.mitu.carrecorder.trackingtu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.GetMileageAdapter;
import com.mitu.carrecorder.entiy.Mileage;
import com.mitu.carrecorder.utils.SpHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 里程记录
 *
 * @author Administrator
 */
public class MileageActivity extends BaseActivity implements
        OnItemClickListener {
    private TextView tvLevel, tvTotalTime, tvTotalMileage, tvSpeed;
    private List<Mileage> mileageList;
    private GetMileageAdapter adapter;
    private ListView mListView;
    private SpHelper sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage);
        mListView = (ListView) findViewById(R.id.pull_refresh_list);
        mileageList = new ArrayList<>();
        adapter = new GetMileageAdapter(MileageActivity.this, mileageList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        getMileageList();
        getAllMileage();
    }

    // 获取所有里程
    private void getAllMileage() {
        // TODO Auto-generated method stub
//        showDialog(MileageActivity.this, "正在加载...");
        /*new Thread() {
            public void run() {
				RequestParams params = new RequestParams();
				params.addBodyParameter("flag", "2");
				params.addBodyParameter("username", sp.getUserName());
				HttpUtils utils = new HttpUtils();
				utils.configSoTimeout(30000);
				utils.send(HttpRequest.HttpMethod.POST, NetField.MILLEAGE_IP,
						params, new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								cancelDialog();
								Toast.makeText(MileageActivity.this,
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
									List<Mileage> list = JsonUtils
											.getAllMileage(arg0.result);
									mileageList.clear();
									mileageList.addAll(list);
									adapter.notifyDataSetChanged();
									Toast.makeText(MileageActivity.this, "cg",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(MileageActivity.this, "无数据",
											Toast.LENGTH_SHORT).show();
								}
							}

						});
			}
		}.start();*/
    }

    public void initView() {
        // TODO Auto-generated method stub
        sp = new SpHelper(this);
        setTitle(getString(R.string.myMileage));
        initTitleBack();
        // tvLevel=(TextView) findViewById(R.id.tv_mileage_level);
        // tvLevel.setText(sp.getLevelName());
        tvTotalTime = (TextView) findViewById(R.id.tv_mileage_totaltime);
        tvTotalTime.setText(sp.getTotalTime());// sp.getTotalTime()
        tvTotalMileage = (TextView) findViewById(R.id.tv_mileage_totalmileage);
        tvTotalMileage.setText(sp.getTotalMileage());// sp.getTotalMileage()
        tvSpeed = (TextView) findViewById(R.id.tv_mileage_speed);
        tvSpeed.setText(sp.getSpeed());// sp.getSpeed()
        // shareVisibility();
        // ivShare.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.pull_refresh_list);
        mileageList = new ArrayList<>();
        adapter = new GetMileageAdapter(MileageActivity.this, mileageList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    private void getMileageList() {
        // TODO Auto-generated method stub
        List<Mileage> list = new ArrayList<Mileage>();
        for (int i = 1; i < 10; i++) {
            Mileage mileage = new Mileage();
            mileage.setId(i);
            mileage.setDate("2015-11-" + i);
            mileage.setMileage(40 + 2 * i + "");
            mileage.setTime(5 + "");
            mileage.setLn("39.234");
            mileage.setLg("113.345");
            mileage.setEnd("武汉汽车总站");

            list.add(mileage);
        }
        mileageList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MileageActivity.this, TrackGjActivity.class);
        intent.putExtra("mileage", mileageList.get(position));
        startActivity(intent);
    }

}
