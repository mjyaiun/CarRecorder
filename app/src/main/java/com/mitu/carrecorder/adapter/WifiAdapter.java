package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mitu.carrecorder.R;

import java.util.List;

public class WifiAdapter extends BaseAdapter {

	private List<ScanResult > wifiList;
	private Context context;
	private LayoutInflater inflater;
	
	
	public WifiAdapter(List<ScanResult> wifiList, Context context) {
		super();
		this.wifiList = wifiList;
		this.context = context;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wifiList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wifiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=inflater.inflate(R.layout.wifi_item, null);
		TextView tvWifi=(TextView) convertView.findViewById(R.id.tv_wifi_item);
		tvWifi.setText(wifiList.get(position).SSID);
		return convertView;
	}
	

}
