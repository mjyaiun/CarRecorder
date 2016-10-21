package com.mitu.carrecorder.phvedio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guotiny.okhttpserver.download.DownloadInfo;
import com.guotiny.okhttpserver.download.DownloadManager;
import com.guotiny.okhttpserver.download.DownloadService;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.adapter.VideoListviewLocalAdapter;
import com.mitu.carrecorder.adapter.VideoListviewLocalEditAdapter;
import com.mitu.carrecorder.utils.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频
 *
 * @author Administrator
 */
public class FragmentLocalVideo extends BaseFragment implements Callback {


    private ListView mListView;

    private VideoListviewLocalAdapter mAdapter;

    private View mView;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_DELETE = 1;
    private int listViewMode = MODE_NORMAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_local_photo, null);

        this.mView = view;
        initListview();

        mHandler = new Handler(this);
        getVideoList();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            int mode = ( (LocalPhotoVideoActivity)getActivity()).getPageMode();
            Log.i("fragment",":localvideo"+isVisibleToUser+"  mode: " +mode);

            if (mode == PhotoVideoActivity.MODE_NORMAL){
                listViewMode = MODE_NORMAL;
                if (mAdapter != null && mListView != null){
                    mListView.setAdapter(mAdapter);
                }

                //
            }else if (mode == PhotoVideoActivity.MODE_DELETE){
                listViewMode = MODE_DELETE;
                if (editAdapter == null) {
                    editAdapter = new VideoListviewLocalEditAdapter(getContext(), videoList, dataList);
                    mListView.setAdapter(editAdapter);
                } else {
                    mListView.setAdapter(editAdapter);
                    editAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    private VideoListviewLocalEditAdapter editAdapter;

    private void initListview() {

        mListView = (ListView) mView.findViewById(R.id.pull_refresh_list);
        mListView.setSelector(R.color.color_bank);

        final LocalPhotoVideoActivity parent = (LocalPhotoVideoActivity) this.getActivity();
        parent.setOnMyBtnClickListener(new LocalPhotoVideoActivity.OnMyBtnClickListener() {
            @Override
            public void onButtonClick(Context context, int type) {
                switch (type) {
                    case LocalPhotoVideoActivity.DELETE_BUTTON:
                        listViewMode = MODE_DELETE;
//                        StringBuilder builderDelete = new StringBuilder();
//                        deleteSelected = editAdapter.getSelectIndex();
//                        for (int dayIndex : deleteSelected.keySet()) {
//                            builderDelete.append("\n"+" 位置  "+ dayIndex + "item:");
//                            ArrayList<Integer> gridIndex = deleteSelected.get(dayIndex);
//                            for (int j = 0; j < gridIndex.size(); j++) {
//                                builderDelete.append(gridIndex.get(j)+"  ");
//                            }
//                        }

                        doVideoDelete();
//                        Log.i("视频删除位置",builderDelete.toString());

                        break;

                    case LocalPhotoVideoActivity.EDIT_TOOLBAR:
                        if (editAdapter == null) {
                            editAdapter = new VideoListviewLocalEditAdapter(getContext(), videoList, dataList);
                            mListView.setAdapter(editAdapter);
                        } else {
                            mListView.setAdapter(editAdapter);
                            editAdapter.notifyDataSetChanged();
                        }
                        break;
                    case LocalPhotoVideoActivity.CANCEL_TOOLBAR:
                        listViewMode = MODE_NORMAL;
                        if (mAdapter == null) {
                            mAdapter = new VideoListviewLocalAdapter(getActivity(), videoList, dataList);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }

                Toast.makeText(parent, "videogragment", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private DownloadManager downloadManager;
    private List<DownloadInfo> allTask;

    private void doVideoDelete() {

        initProgressDialog(getString(R.string.onDelete));
        //
        downloadManager = DownloadService.getDownloadManager(this.getContext());
        allTask = downloadManager.getAllTask();

        //
        deleteSelected = editAdapter.getSelectIndex();

        int dateIndex = 0;
        int fileIndex = 0;

        //统计删除的总数
        for (int dayIndex : deleteSelected.keySet()) {
//            builderDelete.append("\n"+" 位置  "+ dayIndex + "item:");
            dateIndex = dayIndex;
            ArrayList<Integer> gridIndex = deleteSelected.get(dateIndex);
            totalDelete += gridIndex.size();
        }


        //循环删除
        for (int dayIndex : deleteSelected.keySet()) {
            dateIndex = dayIndex;
            ArrayList<Integer> gridIndex = deleteSelected.get(dateIndex);
            for (int m = 0; m < gridIndex.size(); m++) {
                fileIndex = gridIndex.get(m);
                String fileName = videoList.get(dataList.get(dateIndex)).get(fileIndex);

                ///要删除下载队列数据库表的任务，任务会同时删除下载的文件
                //不允许进行删除文件操作，否则再次进行下载任务会异常
                for (int n = 0; n < allTask.size(); n++) {
                    DownloadInfo info = allTask.get(n);
                    String name = info.getFileName();
                    if (fileName.equals(name)){
                        String url = info.getUrl();
                        downloadManager.removeTaskByUrl(url);
                    }
                }

                //然后在执行单独的文件删除，防止下载列表为空时候无法删除文件
                String path = SystemUtils.getLocalFilePath()+ fileName;
                deleteFile(path);

            }
        }

        dismissDialog();
        getVideoList();

    }


    /** 根据路径删除文件 */
    private boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return true;
        File file = new File(path);
        if (!file.exists()) return true;
        if (file.isFile()) return file.delete();
        return false;
    }


    private Handler mHandler;
    //mov视频删除
    private static final int DELETE_SUCCESS_ONE = 101;
    private static final int DELETE_FAILED_ONE = 102;


    private int totalDelete = 0;
    private int successDelete = 0;
    private int failedDelete = 0;


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DELETE_SUCCESS_ONE:
                successDelete += 1;
                Log.i("filepath","total:"+totalDelete + "  success:"+ successDelete + "  failed："+ failedDelete);
                if (totalDelete == successDelete + failedDelete){
                    dismissDialog();
                    getVideoList();
                }
                break;
            case DELETE_FAILED_ONE:
                Log.i("filepath","total:"+totalDelete + "  success:"+ successDelete + "  failed："+ failedDelete);
                failedDelete += 1;
                if (totalDelete == successDelete + failedDelete){
                    dismissDialog();
                    getVideoList();
                }
                break;

        }

        return false;
    }


    private Map<Integer,ArrayList<Integer>> deleteSelected;

    private void initListviewData(){
        dismissDialog();
        if (listViewMode == MODE_NORMAL){
            if (mAdapter == null){
                mAdapter = new VideoListviewLocalAdapter(getActivity(), videoList,dataList);
                mListView.setAdapter(mAdapter);
            }else {
                mAdapter.notifyDataSetChanged();
            }
        } else if (listViewMode == MODE_DELETE) {
            editAdapter.notifyDataSetChanged();
            //界面刷新后删除相关的数量要重置
            totalDelete = 0;
            successDelete = 0;
            failedDelete = 0;
        }
    }


    private Map<String ,ArrayList<String>> videoList = new HashMap<>();

    private ArrayList<String> dataList;

    private void getVideoList(){
        initProgressDialog(getString(R.string.onLoading));
        if (dataList == null){
            dataList = new ArrayList<>();
        }else
            dataList.clear();


        ArrayList<String> tempList = new ArrayList<>();

        String path = SystemUtils.getLocalFilePath();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null){
            for (int i = 0; i < files.length; i++) {//遍历出所有视频文件
                File child = files[i];
                String name = child.getName();
                if (name.endsWith("MOV")){
                    tempList.add(name);
                }
            }

            for (int m = 0; m < tempList.size(); m++) {//初始化日期列表
                String name = tempList.get(m);
                String[] subItem = name.split("_");
                if (subItem.length > 2){
                    String date = subItem[0]+"-"+subItem[1];
                    if (!dataList.contains(date)){
                        dataList.add(date);
                    }
                }
            }

            for (int n = 0; n < dataList.size(); n++) {
                String date = dataList.get(n);
                ArrayList<String> fileList = new ArrayList<>();
                for (int f = 0; f < tempList.size(); f++) {
                    String name = tempList.get(f);
                    String datef = name.split("_")[0] + "-"+name.split("_")[1];
                    if (date.equals(datef)){
                        fileList.add(name);
                    }
                }

                videoList.put(date,fileList);

            }
        }
        initListviewData();

    }


    /**联网对话框*/
    public Dialog pd;
    /**
     * 初始化联网进度条对象
     */
    public void initProgressDialog(String info){
        try{
            if(pd == null){
                pd = new Dialog(this.getContext(),R.style.customDialog);
                LayoutInflater inflater = LayoutInflater.from(this.getContext());
                View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
                TextView content = (TextView)v.findViewById(R.id.textViewContent);
                content.setText(info);
                pd.setContentView(v);
                pd.show();
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
//						isResponseData = true;
                    }
                });
            }else{
                pd.show();
            }
        }catch(Exception e){}
    }

    /**
     * dismiss联网进度条
     */
    public void dismissDialog(){
        if(pd != null){
            pd.dismiss();
            pd = null;
        }
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



}
