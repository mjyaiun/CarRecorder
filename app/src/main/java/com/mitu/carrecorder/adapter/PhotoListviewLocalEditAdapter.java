package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mitu.carrecorder.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 说明：照片列表显示
 * 2016/6/20 0020
 */
public class PhotoListviewLocalEditAdapter extends BaseAdapter {

    private Context mContext;
    private Map<String,ArrayList<String>> mPhotoList;
    private ArrayList<String> dateList;


    public PhotoListviewLocalEditAdapter(Context cx, Map<String,ArrayList<String>> mPhotoList, ArrayList<String> dateList) {
        this.mContext = cx;
//		this.ImageArrs = arr;
        this.mPhotoList = mPhotoList;
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (dateList == null) {
            return 0;
        } else {
            return this.dateList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (dateList == null) {
            return null;
        } else {
            return this.dateList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
       // final int[] resourceId = dateList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.fragment_listview_photo_item, null, false);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_fragment_time);
            holder.gridView = (GridView) convertView.findViewById(R.id.listview_item_gridview);
            holder.more = (RelativeLayout) convertView.findViewById(R.id.more_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.dateList != null) {
            if (holder.tvTime != null) {
                holder.tvTime.setText(dateList.get(position));
            }
            if (holder.gridView != null) {
//
                GridviewPhotoLocalEditAdapter gridViewAdapter = new GridviewPhotoLocalEditAdapter(mContext,
                        mPhotoList.get(dateList.get(position)));
                holder.gridView.setAdapter(gridViewAdapter);


                final int listviewIndex = position;

                gridViewAdapter.setOnItemClickListener(new GridviewPhotoLocalEditAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Context context, ArrayList<Integer> positions) {
                        if (selectIndex.containsKey(listviewIndex)){
                            selectIndex.remove(listviewIndex);
                            selectIndex.put(listviewIndex,positions);
                        }else {
                            selectIndex.put(listviewIndex,positions);
                        }
                    }
                });




            }
        }
        return convertView;
    }


    private Map<Integer,ArrayList<Integer>> selectIndex = new HashMap<>();

    public Map<Integer, ArrayList<Integer>> getSelectIndex() {
        return selectIndex;
    }


    private class ViewHolder {
        private TextView tvTime;
        private RelativeLayout more;
        private GridView gridView;
    }

}
