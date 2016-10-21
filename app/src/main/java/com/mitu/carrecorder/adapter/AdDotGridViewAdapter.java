package com.mitu.carrecorder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mitu.carrecorder.R;


/**
 * 循环圆点Adapter
 * @author
 */
public class AdDotGridViewAdapter extends BaseAdapter {
	/**上下文对象*/
	private Context context;

	public int numColumns;

	public AdDotGridViewAdapter(Context context,int numColumns){
		this.context = context;
		this.numColumns = numColumns;
	}

	public int getCount() {
		return numColumns;
	}

	public Object getItem(int position) {
		return "";
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		DotItem item = null;
		if(convertView == null){
			item = new DotItem();
			convertView  = LayoutInflater.from(context).inflate(R.layout.item_dot, null);
			item.icon = (ImageView)convertView.findViewById(R.id.imageViewImg);
			convertView.setTag(item);//设置数据源
		}else{
			item = (DotItem)convertView.getTag();
		}
		if(selectIndex == position){
			item.icon.setImageResource(R.drawable.yindaoyuanlv);
		}else{
			item.icon.setImageResource(R.drawable.yindaoyuanhuise);
		}
		//计算图片大小
		//convertView.setLayoutParams(new GridView.LayoutParams((int)(20 * BEApplication.dx), GridView.LayoutParams.WRAP_CONTENT));
		return convertView;
	}


	public int selectIndex;

	public void setSelectIndex(int selectIndex){
		this.selectIndex = selectIndex;
	}

	private class DotItem{
		ImageView icon;
	}
}