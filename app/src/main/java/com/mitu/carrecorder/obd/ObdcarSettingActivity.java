package com.mitu.carrecorder.obd;


import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.adapter.SwitchSettingListviewAdapter;
import com.mitu.carrecorder.entiy.BEMenuItem;
import com.mitu.carrecorder.test.TestData;
import com.mitu.carrecorder.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ObdcarSettingActivity extends BaseActivity {


	@Bind(R.id.obd_setting_lv)
	ListView obdSettingListview;
	private String[] obdSettingArray;
	private ArrayList<BEMenuItem> itemArrayList;
	private SwitchSettingListviewAdapter adapter;


	private String resultString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_obdcar_set);
		//
		ButterKnife.bind(this);


	}



	private StringBuilder request = new StringBuilder();

	private String getObdCarState() {

		request.append("f00f");//tou

		long time = System.currentTimeMillis()/1000;
		request.append(Long.toHexString(time));//时间戳、
		int frameCounter = SPUtils.readInt(this,SPUtils.OBD_FRAME_COUNTER);//计数器 4位
		String counter = Integer.toHexString(frameCounter);
		int countLen = counter.length();
		if (countLen < 4){
			for (int i = 0; i < 4 - countLen; i++) {
				counter = "0"+ counter;
			}
		}

		request.append(counter);//frame_counter
		request.append("fff0"); //载荷类型== 配置
		request.append("0002"); //数据长度
		request.append("21");//SID
		request.append("ff");

		//
		return TestData.getOBDCarState();

	}

	private Map<Integer,Integer> stateMap = new HashMap<>();

	private void parseListState() {
		if (resultString.length() != 0){
			String requestStr = request.toString();

			//对比时间戳
			String requestTimeStamp = requestStr.substring(4,12);
			String resultTimeStamp = resultString.substring(4,12);
			if (!requestTimeStamp.equals(resultTimeStamp)){
//				return;
			}
			//对比计数器
			String reqframeCount = requestStr.substring(12,16);
			String resultFrameCount = resultString.substring(12,16);
			if (!reqframeCount.equals(resultFrameCount)){
//				return;
			}
			//验证SID
			int reqSID = Integer.parseInt(requestStr.substring(24,26),16);
			int resultSID = Integer.parseInt(resultString.substring(24,26),16);

			if (resultSID == Integer.parseInt("7f",16)){
				Toast.makeText(this,"请求操作失败！",Toast.LENGTH_SHORT).show();
			}else if (resultSID != reqSID + Integer.parseInt("40",16)){
//				return;
			}

			//验证数据长度
			int length = Integer.parseInt(resultString.substring(20,24),16);
			int dateStrLength = resultString.substring(24).length()/2;
			if (length != dateStrLength){
//				return;
			}

			stateMap.clear();

			//解析数据结果
			int totalState = Integer.parseInt(resultString.substring(26,28),16);
			for (int i = 0; i < totalState; i++) {
				int start = 28 + 4 * i;
				String item = resultString.substring(start,start+4 );
				int type = Integer.parseInt(item.substring(0,2),16);
				int state = Integer.parseInt(item.substring(2),16); //0：关闭  1 开启
				stateMap.put(type,state);
			}

		}


	}

	/**
	 * type :1.自动折叠后视镜
	 *       2.一键自动升窗
	 *       4.雾灯随动转向证明
	 *       5.自动转向灯
	 *       7.发动机未熄火提醒
	 * obd其他暂不支持
	 *
	 */

	private void initListState() {

		obdSettingArray = getResources().getStringArray(R.array.obdCarSettingArray);
		itemArrayList = new ArrayList<>();
		for (int i = 0; i < obdSettingArray.length; i++) {
			BEMenuItem item = new BEMenuItem(obdSettingArray[i]);
			switch (i){
				case 0:
					item.isCheck = stateMap.get(1) == 0 ? false:true;
					break;
				case 1:
					item.isCheck = stateMap.get(2) == 0 ? false:true;
					break;
				case 2:
					item.isCheck = stateMap.get(4) == 0 ? false:true;
					break;
				case 3:
					item.isCheck = stateMap.get(5) == 0 ? false:true;
					break;
				case 4:
					item.isCheck = stateMap.get(7) == 0 ? false:true;
					break;
			}
			itemArrayList.add(item);
		}



	}



	@Override
	public void initView() {
		setTitle(getString(R.string.obdSetting));
		initTitleBack();
		//
		//
		resultString = getObdCarState();
		parseListState();
		initListState();
		//
		//
		adapter = new SwitchSettingListviewAdapter(this,itemArrayList);
		obdSettingListview.setAdapter(adapter);
		adapter.setOnItemCheckChangeListener(new SwitchSettingListviewAdapter.OnItemCheckChangeListener() {
			@Override
			public void onButtonClick(Context context, int position, boolean ischeck) {
				int commandId = 0;
				switch (position){
					case 0:
						commandId = 1;
						break;
					case 1:
						commandId = 2;
						break;
					case 2:
						commandId = 4;
						break;
					case 3:
						commandId = 5;
						break;
					case 4:
						commandId = 7;
						break;
				}

				doChangeCommandMode(commandId);


			}
		});

	}

	private void doChangeCommandMode(int command) {
		StringBuilder sb = new StringBuilder();
		sb.append("f00f");//头

		long time = System.currentTimeMillis()/1000;
		request.append(Long.toHexString(time));//时间戳、
		int frameCounter = SPUtils.readInt(this,SPUtils.OBD_FRAME_COUNTER);//计数器 4位
		String counter = Integer.toHexString(frameCounter);
		int countLen = counter.length();
		if (countLen < 4){
			for (int i = 0; i < 4 - countLen; i++) {
				counter = "0"+ counter;
			}
		}

		request.append(counter);//frame_counter
		request.append("fff0"); //载荷类型== 配置
		request.append("0002"); //数据长度
		request.append("2e");//SID
		request.append("01");

		String result = TestData.getModifyResult();
		parseModifyResult();

	}

	private void parseModifyResult() {
		//

	}


}
