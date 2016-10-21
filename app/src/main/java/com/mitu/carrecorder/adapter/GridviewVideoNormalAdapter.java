package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.FileEntity;

import java.util.ArrayList;


public class GridviewVideoNormalAdapter extends BaseAdapter {


    private Context mContext;
    ArrayList<FileEntity> videoList;
    private GridView mGridView;

    public GridviewVideoNormalAdapter(Context mContext, ArrayList<FileEntity> videoList, GridView mGridView) {
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

        final FileEntity content = videoList.get(position);
        final String replace = content.getFPath().substring(2).replace("\\", "/");

        String url = BEConstants.ADDRESS_IP_DEVICE + replace + "?custom=1&cmd=4001";
//        Log.i("photophth",url);

        item.img.setImageURI(Uri.parse(url.trim()));

        ViewGroup.LayoutParams params = item.img.getLayoutParams();
        params.width = BEApplication.width / 4;
        params.height = BEApplication.width / 4;
        item.img.setLayoutParams(params);

        item.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToPreview(replace, content.getName(), content.isHasThumbNail);
            }
        });

        return convertView;
    }

    private void changeToPreview(String replace, String name, boolean isHasThumbNail) {

        if (isHasThumbNail) {
            //vlc播放器
//            Intent intent = new Intent(mContext, VideoPreviewActivity.class);
//            intent.putExtra("url",BEConstants.ADDRESS_IP_DEVICE +
//                    replace.replace(".MOV","V.MOV").replace("MOVIE","MOVIE_VIEW"));
//            intent.putExtra("fileName",name);
//            mContext.startActivity(intent);

            //系统播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(BEConstants.ADDRESS_IP_DEVICE +
                    replace.replace(".MOV", "V.MOV").replace("MOVIE", "MOVIE_VIEW")), "video/mov");
            mContext.startActivity(intent);

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.fileNoThumbNailTryDownload), Toast.LENGTH_SHORT).show();
        }


    }


    class ViewHolder {
        SimpleDraweeView img;
        //        ImageView img;
        ImageView check;
    }


}
