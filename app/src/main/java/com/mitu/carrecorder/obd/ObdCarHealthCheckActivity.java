package com.mitu.carrecorder.obd;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;
import com.mitu.carrecorder.test.TestData;
import com.mitu.carrecorder.utils.SPUtils;

import java.util.ArrayList;

import butterknife.Bind;


public class ObdCarHealthCheckActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_obd_result);

	}

	@Bind(R.id.content)
	TextView tv ;

	@Override
	public void initView() {
		setTitle(getString(R.string.obdSystemCheck));
		initTitleBack();
		requestHealthCheck();
		parseCheckResult();
	}


	//构造请求参数
	private StringBuilder request = new StringBuilder();

	private String requestHealthCheck() {

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
		request.append("fff1"); //载荷类型== 配置
		request.append("0001"); //数据长度
		request.append("18");//SID

		//
		return TestData.getOBDCarState();
	}


	private String resultString;
	private ArrayList<String> resultList = new ArrayList<>();

	private void parseCheckResult() {

		resultString = TestData.getHealthCheckResult();

		if (resultString.length() != 0) {
			String requestStr = request.toString();

			//对比时间戳
			String requestTimeStamp = requestStr.substring(4, 12);
			String resultTimeStamp = resultString.substring(4, 12);
			if (!requestTimeStamp.equals(resultTimeStamp)) {
//				return;
			}
			//对比计数器
			String reqframeCount = requestStr.substring(12, 16);
			String resultFrameCount = resultString.substring(12, 16);
			if (!reqframeCount.equals(resultFrameCount)) {
//				return;
			}
			//验证SID
			int reqSID = Integer.parseInt(requestStr.substring(24, 26), 16);
			int resultSID = Integer.parseInt(resultString.substring(24, 26), 16);

			if (resultSID == Integer.parseInt("7f", 16)) {
				Toast.makeText(this, "请求操作失败！", Toast.LENGTH_SHORT).show();
			} else if (resultSID != reqSID + Integer.parseInt("40", 16)) {
//				return;
			}

			//验证数据长度
			int length = Integer.parseInt(resultString.substring(20, 24), 16);
			int dateStrLength = resultString.substring(24).length() / 2;
			if (length != dateStrLength) {
//				return;
			}

			int totalState = Integer.parseInt(resultString.substring(26,28),16);
			for (int i = 0; i < totalState; i++) {
				int start = 28 + 6 * i;
				String item = resultString.substring(start,start+6 );
				String typeNum = item.substring(0,2);
				String type = "";
				if (typeNum.equals("00")){
					type = "P";
				}else if (typeNum.equals("01")){
					type = "C";
				}else if (typeNum.equals("02")){
					type = "B";
				}else if (typeNum.equals("03")){
					type = "U";
				}
				String num = item.substring(2);
				resultList.add(type+num);
			}

		}


		//
		refreshContent();

	}

	private void refreshContent() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < resultList.size(); i++) {
			sb.append(resultList.get(i) + "\n");
		}
		tv.setText(sb.toString());
	}


}
