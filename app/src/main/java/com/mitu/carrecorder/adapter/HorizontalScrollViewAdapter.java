package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.FileEntity;

import java.util.ArrayList;

public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FileEntity> mDatas;


    public HorizontalScrollViewAdapter(Context context, ArrayList<FileEntity> mDatas) {
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

        FileEntity content = mDatas.get(position);
        String replace = content.getFPath().substring(2).replace("\\","/");
        String url = BEConstants.ADDRESS_IP_DEVICE + replace+"?custom=1&cmd=4001";
//        Log.i("photophth",url);

        viewHolder.mImg.setImageURI(Uri.parse(url.trim()));

        ViewGroup.LayoutParams params = viewHolder.mImg.getLayoutParams();
        params.width = BEApplication.width/5;
        viewHolder.mImg.setLayoutParams(params);

//        viewHolder.mImg.setImageResource(R.drawable.a);

        return convertView;
    }

    private class ViewHolder {
//        ImageView mImg;
        SimpleDraweeView mImg;

    }


}
