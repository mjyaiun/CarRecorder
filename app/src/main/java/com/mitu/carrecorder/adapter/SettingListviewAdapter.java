package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

/**
 * Created by gt on 2016/3/25 0025.
 */
public class SettingListviewAdapter extends BaseAdapter{

    private Context context;

    private ArrayList<BEMenuItem>  menuitems;

    public SettingListviewAdapter(Context context, ArrayList<BEMenuItem> items){
        this.context = context;
        this.menuitems = items;
    }

    public int getCount() {
        return menuitems.size();
    }

    public Object getItem(int position) {
        return menuitems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder item = null;
        if(convertView == null){
            item = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.listview_setting_normal_item, null);
            item.name = (TextView)convertView.findViewById(R.id.list_item_text);
//            item.icon = (ImageView)convertView.findViewById(R.id.list_item_image);
            convertView.setTag(item);//
        }else{
            item = (ViewHolder)convertView.getTag();
        }

        BEMenuItem content = menuitems.get(position);
        if(content.iconId > 0){
            item.icon.setImageResource(content.iconId);
        }
        item.name.setText(content.name);

        return convertView;
    }

    private class ViewHolder{

        /**图标*/
        public ImageView icon;
        /**文本*/
        TextView name;
    }

}
