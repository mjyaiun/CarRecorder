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
 * 说明：
 * 2016/6/18 0018
 */
public class MainMenuListviewAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<BEMenuItem> menuitems;

    public MainMenuListviewAdapter(Context context, ArrayList<BEMenuItem> items){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder item = null;
        if(convertView == null){
            item = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.listview_main_left_menu_item, null);
            item.name = (TextView)convertView.findViewById(R.id.menu_name_tv);
            item.icon = (ImageView)convertView.findViewById(R.id.menu_img);
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
