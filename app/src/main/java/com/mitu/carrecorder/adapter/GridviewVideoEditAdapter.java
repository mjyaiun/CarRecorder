package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.FileEntity;

import java.util.ArrayList;


public class GridviewVideoEditAdapter extends BaseAdapter {


    private Context mContext;
    ArrayList<FileEntity> videoList;
    private GridView mGridView;

    public GridviewVideoEditAdapter(Context mContext, ArrayList<FileEntity> videoList, GridView mGridView) {
        super();
        this.mContext = mContext;
        this.videoList = videoList;
        this.mGridView = mGridView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
       return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (videoList == null) {
            return null;
        } else {
            return this.videoList.get(position);
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
        if(convertView == null){
            item = new ViewHolder();
            convertView  = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
            item.img  = (SimpleDraweeView) convertView.findViewById(R.id.iv_gridview_item);
            item.check = (ImageView) convertView.findViewById(R.id.iv_gridview_gouxuan);

            convertView.setTag(item);//
        }else{
            item = (ViewHolder)convertView.getTag();
        }


        if (isContains(position)){
            item.check.setVisibility(View.VISIBLE);
        }else {
            item.check.setVisibility(View.GONE);
        }


        FileEntity content = videoList.get(position);
        String replace = content.getFPath().substring(2).replace("\\","/");

        String url = BEConstants.ADDRESS_IP_DEVICE + replace+"?custom=1&cmd=4001";
//        Log.i("photophth",url);

        item.img.setImageURI(Uri.parse(url.trim()));

        ViewGroup.LayoutParams params = item.img.getLayoutParams();
        params.width = BEApplication.width /4;
        params.height = BEApplication.width /4;
        item.img.setLayoutParams(params);

        item.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectIndexs.contains(position)) {
                    selectIndexs.remove((Object) position);
                    mOnItemClickListener.onItemClick(mContext, selectIndexs);
                } else {
                    selectIndexs.add(position);
                    mOnItemClickListener.onItemClick(mContext, selectIndexs);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    //点击监听器
    private OnItemClickListener mOnItemClickListener;

    public static interface OnItemClickListener {
        void onItemClick(Context context, ArrayList<Integer> positions);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    private ArrayList<Integer> selectIndexs = new ArrayList<>();

    public ArrayList<Integer> getSelectIndex() {
        return selectIndexs;
    }

    public void clearSelected(){
        selectIndexs.clear();
    }

    public boolean isContains(int selectIndex) {
        boolean result = false;
        for (int i = 0; i < selectIndexs.size(); i++) {
            if (selectIndexs.get(i) == selectIndex)
                result = true;
        }
        return result;
    }


//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//
//    }

    class ViewHolder{
         SimpleDraweeView img;
//        ImageView img;
        ImageView check;
    }


}
