package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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


public class GridviewPhotoLocalNormalAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<String> photoList;

    public GridviewPhotoLocalNormalAdapter(Context mContext, ArrayList<String> photoList) {
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

        String content = photoList.get(position);
        String url = SystemUtils.getLocalFilePath()+content;
//        Log.i("photophth",url);
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

        item.img.setImageURI(Uri.parse("file://"+url.trim()));

        ViewGroup.LayoutParams params = item.img.getLayoutParams();
        params.width = BEApplication.width / 4;
        params.height = BEApplication.width / 4;
        item.img.setLayoutParams(params);


        return convertView;
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


    class ViewHolder {

        SimpleDraweeView img;
//        ImageView img;
        ImageView check;
    }


}
