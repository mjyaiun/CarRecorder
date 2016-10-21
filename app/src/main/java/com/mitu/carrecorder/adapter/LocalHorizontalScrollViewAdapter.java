package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.SystemUtils;

import java.util.ArrayList;

public class LocalHorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDatas;


    public LocalHorizontalScrollViewAdapter(Context context, ArrayList<String> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_index_gallery_item, parent, false);
            viewHolder.mImg = (SimpleDraweeView) convertView.findViewById(R.id.id_index_gallery_item_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String content = mDatas.get(position);
        String url = SystemUtils.getLocalFilePath() + content;

        ViewGroup.LayoutParams params = viewHolder.mImg.getLayoutParams();
        params.width = BEApplication.width/5;
        viewHolder.mImg.setLayoutParams(params);

        viewHolder.mImg.setImageURI(Uri.parse("file://"+url));
//        viewHolder.mImg.setImageResource(R.drawable.a);

        return convertView;
    }

    private class ViewHolder {
//        ImageView mImg;
        SimpleDraweeView mImg;

    }


}
