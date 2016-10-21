package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guotiny.okhttpserver.download.DownloadInfo;
import com.guotiny.okhttpserver.download.DownloadManager;
import com.guotiny.okhttpserver.download.DownloadService;
import com.guotiny.okhttpserver.listener.DownloadListener;
import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.entiy.FileEntity;

import java.util.ArrayList;


public class GridviewPhotoDownloadAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<FileEntity> photoList;
    private DownloadManager manager;

    public GridviewPhotoDownloadAdapter(Context mContext, ArrayList<FileEntity> photoList) {
        super();
        this.mContext = mContext;
        this.photoList = photoList;

        manager = DownloadService.getDownloadManager(mContext);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (photoList.size() == 0) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_download_item, null);
            item = new ViewHolder(convertView);
            convertView.setTag(item);//
        } else {
            item = (ViewHolder) convertView.getTag();
        }


        if (isContains(position)) {
            item.check.setVisibility(View.VISIBLE);
        } else {
            item.check.setVisibility(View.GONE);
        }

        FileEntity content = photoList.get(position);
        String replace = content.getFPath().substring(2).replace("\\", "/");

        String downLoadUrl = BEConstants.ADDRESS_IP_DEVICE + replace;
        final DownloadInfo downloadInfo = manager.getTaskByUrl(downLoadUrl);

        if (manager.getTaskByUrl(downLoadUrl) != null){
            Log.i("MyDownloadListener"," getview:" + downLoadUrl);
            //item.progressBar.setVisibility(View.VISIBLE);
            item.refresh(downloadInfo);
            DownloadListener downloadListener = new MyDownloadListener();
            downloadListener.setUserTag(item);
            downloadInfo.setListener(downloadListener);

        }else {
            //item.progressBar.setVisibility(View.GONE);
        }


        String url = BEConstants.ADDRESS_IP_DEVICE + replace + "?custom=1&cmd=4001";
//        Log.i("photophth",url);

        item.img.setImageURI(Uri.parse(url.trim()));

        item.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadInfo != null && downloadInfo.getState() == DownloadManager.FINISH){
                    Toast.makeText(mContext,"该文件已下载过",Toast.LENGTH_SHORT).show();
                }else {
                    if (selectIndexs.contains(position)) {
                        selectIndexs.remove((Object) position);
                        mOnItemClickListener.onItemClick(mContext, selectIndexs);
                    } else {
                        selectIndexs.add(position);
                        mOnItemClickListener.onItemClick(mContext, selectIndexs);
                    }
                }
                notifyDataSetChanged();
            }
        });

        ViewGroup.LayoutParams params = item.img.getLayoutParams();
        params.width = BEApplication.width / 4;
        params.height = BEApplication.width / 4;
        item.img.setLayoutParams(params);

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

    public boolean isContains(int selectIndex) {
        boolean result = false;
        for (int i = 0; i < selectIndexs.size(); i++) {
            if (selectIndexs.get(i) == selectIndex)
                result = true;
        }
        return result;
    }


    class ViewHolder {

        private DownloadInfo downloadInfo;

        SimpleDraweeView img;
        //        ImageView img;
        ImageView check;
        ProgressBar progressBar;


        public ViewHolder(View convertView) {
            img = (SimpleDraweeView) convertView.findViewById(R.id.iv_gridview_item);
            check = (ImageView) convertView.findViewById(R.id.iv_gridview_gouxuan);
            progressBar = (ProgressBar)convertView.findViewById(R.id.gridview_download_progressbar);
        }


        public void refresh(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            refresh();
        }
//
//
        private void refresh() {
            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress((int)(Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) );
        }

    }


    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            Log.i("MyDownloadListener",getUserTag().toString());
            if (getUserTag() == null) return;
            ViewHolder holder = (ViewHolder) getUserTag();
            holder.refresh();  //这里不能使用传递进来的 DownloadInfo，否者会出现条目错乱的问题
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
//            Toast.makeText(DownloadManagerActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
//            if (errorMsg != null)
//                Toast.makeText(DownloadManagerActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }



}


