package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.FileEntity;

import java.util.ArrayList;


public class GridviewPhotoNormalAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<FileEntity> photoList;

    public GridviewPhotoNormalAdapter(Context mContext, ArrayList<FileEntity> photoList) {
        super();
        this.mContext = mContext;
        this.photoList = photoList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (photoList.size()  == 0) {
            return 0;
        } else {
            return this.photoList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (photoList == null) {
            return null;
        } else {
            return this.photoList.get(position);
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
        // final ViewHolder holder;

        ViewHolder item = null;
        if (convertView == null) {
            item = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
            item.img = (SimpleDraweeView) convertView.findViewById(R.id.iv_gridview_item);
            item.check = (ImageView) convertView.findViewById(R.id.iv_gridview_gouxuan);
            item.check.setVisibility(View.GONE);

            convertView.setTag(item);//
        } else {
            item = (ViewHolder) convertView.getTag();
        }

        //final int path = photoList.get(position);
        // if (convertView == null) {
        // holder = new ViewHolder();


        FileEntity content = photoList.get(position);
        String replace = content.getFPath().substring(2).replace("\\","/");
        String url = BEConstants.ADDRESS_IP_DEVICE + replace+"?custom=1&cmd=4001";
//        Log.i("photophth",url);

        item.img.setImageURI(Uri.parse(url.trim()));

        ViewGroup.LayoutParams params = item.img.getLayoutParams();
        params.width = BEApplication.width / 4;
        params.height = BEApplication.width / 4;
        item.img.setLayoutParams(params);


        return convertView;
    }


    class ViewHolder {

        SimpleDraweeView img;
//        ImageView img;
        ImageView check;
    }


}
