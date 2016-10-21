package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.Mileage;

import java.util.List;

public class GetMileageAdapter extends BaseAdapter {

	private Context context;
	private List<Mileage> mileageList;
	private LayoutInflater inflater;

	public GetMileageAdapter(Context context, List<Mileage> mileageList) {
		super();
		this.context = context;
		this.mileageList = mileageList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mileageList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mileageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (holder == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.activity_mileage_item, null);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.tv_mileage_date);
			holder.tvDistance = (TextView) convertView
					.findViewById(R.id.tv_mileage_distance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDate.setText(mileageList.get(position).getDate());
		holder.tvDistance.setText(mileageList.get(position).getMileage());
		return convertView;
	}

	public class ViewHolder {
		TextView tvDate;
		TextView tvDistance;
	}

}
