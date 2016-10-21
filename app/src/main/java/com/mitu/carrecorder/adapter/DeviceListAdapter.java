package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.Device;

import java.util.ArrayList;

/**
 * 说明：
 * 2016/6/16 0016
 */
public class DeviceListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Device> menuitems;

    public DeviceListAdapter(Context context, ArrayList<Device> items){
        this.context = context;
        this.menuitems = items;
    }


    @Override
    public int getCount() {
        return menuitems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder item = null;
        if(convertView == null){
            item = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.listview_device_item, null);
            item.name = (TextView)convertView.findViewById(R.id.device_name_tv);
            item.link = (ImageView)convertView.findViewById(R.id.link_img);
            item.delete = (ImageView)convertView.findViewById(R.id.delete_img);
            item.download = (ImageView)convertView.findViewById(R.id.download_img);
            item.setting = (ImageView)convertView.findViewById(R.id.device_setting_img);

            convertView.setTag(item);//
        }else{
            item = (ViewHolder)convertView.getTag();
        }


        Device content = menuitems.get(position);
        item.name.setText(content.deviceName);
        if (content.isConnect){
            item.link.setImageResource(R.drawable.haslink);
        }else{
            item.link.setImageResource(R.drawable.notlink);
        }
        //
        item.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemImageClickListener.onButtonClick(context,position,0);
            }
        });
        item.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemImageClickListener.onButtonClick(context,position,1);
            }
        });
        item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemImageClickListener.onButtonClick(context,position,2);
            }
        });

        return convertView;
    }

    private OnItemImageClickListener mOnItemImageClickListener;

    public static interface OnItemImageClickListener
    {
        void onButtonClick(Context context, int position,int index);
    }

    public void setOnItemImageClickListener(OnItemImageClickListener listener)
    {
        mOnItemImageClickListener = listener;
    }

    private class ViewHolder{

        ImageView link;
        ImageView download;
        ImageView setting;
        ImageView delete;
        /**文本*/
        TextView name;
    }
}
