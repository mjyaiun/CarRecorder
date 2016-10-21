package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.BEMenuItem;

import java.util.ArrayList;

/**
 * Created by gt on 2016/3/25 0025.
 */
public class LanguageSettingListviewAdapter extends BaseAdapter{

    private Context context;

    private ArrayList<BEMenuItem>  menuitems;

    public LanguageSettingListviewAdapter(Context context, ArrayList<BEMenuItem> items){
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder item = null;
        if(convertView == null){
            item = new ViewHolder();
            convertView  = LayoutInflater.from(context).inflate(R.layout.listview_setting_switch_item, null);
            item.name = (TextView)convertView.findViewById(R.id.list_item_text);
            item.btn = (CheckBox)convertView.findViewById(R.id.togglebutton) ;
//            item.icon = (ImageView)convertView.findViewById(R.id.list_item_image);
            convertView.setTag(item);//
        }else{
            item = (ViewHolder)convertView.getTag();
        }

        //
        final BEMenuItem content = menuitems.get(position);
        item.name.setText(content.name);
        //
        item.btn.setChecked(content.isCheck);
        item.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemToggleButtonClickListener.onButtonClick(context,position);

            }
        });

        return convertView;
    }

    //点击加号监听器
    private OnItemToggleButtonClickListener mOnItemToggleButtonClickListener;

    public static interface OnItemToggleButtonClickListener
    {
        void onButtonClick( Context context, int position);
    }

    public void setOnItemToggleButtonClickListener(OnItemToggleButtonClickListener listener)
    {
        mOnItemToggleButtonClickListener = listener;
    }

    private class ViewHolder{

        CheckBox btn;
        /**文本*/
        TextView name;
    }

}
