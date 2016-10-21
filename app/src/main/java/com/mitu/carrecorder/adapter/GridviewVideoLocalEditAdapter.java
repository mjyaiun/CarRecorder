package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.utils.SystemUtils;

import java.util.ArrayList;


public class GridviewVideoLocalEditAdapter extends BaseAdapter {


    private Context mContext;
    ArrayList<String> videoList;
    private GridView mGridView;

    public GridviewVideoLocalEditAdapter(Context mContext, ArrayList<String> videoList, GridView mGridView) {
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


        String content = videoList.get(position);
        String url = SystemUtils.getLocalFilePath()+content;
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse("file://"+url.trim()))
                .setResizeOptions(new ResizeOptions(dip2px(mContext, 80), dip2px(mContext, 80)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(item.img.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        item.img.setController(controller);

//        item.img.setImageURI(Uri.parse("file://"+url.trim()));

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


    /**
     * dp 转像素
     * @param context
     * @param dpValue
     * @return
     */
    private  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int value=(int) (dpValue * scale + 0.5f);
        return value;
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
