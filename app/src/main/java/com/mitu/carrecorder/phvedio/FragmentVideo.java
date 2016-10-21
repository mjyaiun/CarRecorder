package com.mitu.carrecorder.phvedio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guotiny.httputils.OkHttpUtils;
import com.guotiny.okhttpserver.download.DownloadManager;
import com.guotiny.okhttpserver.download.DownloadService;
import com.mitu.carrecorder.BEConstants;
import com.mitu.carrecorder.R;
import com.mitu.carrecorder.adapter.VideoListviewAdapter;
import com.mitu.carrecorder.adapter.VideoListviewDownloadAdapter;
import com.mitu.carrecorder.adapter.VideoListviewEditAdapter;
import com.mitu.carrecorder.entiy.FileEntity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.net.FileListCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 视频
 *
 * @author Administrator
 */
public class FragmentVideo extends BaseFragment implements Callback {


    private ListView mListView;

    private VideoListviewAdapter mAdapter;

    private View mView;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_DELETE = 1;
    private static final int MODE_DOWNLOAD = 2;
    private int listViewMode = MODE_NORMAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_photo, null);

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
            int mode = ( (PhotoVideoActivity)getActivity()).getPageMode();
            Log.i("fragment",":photo"+isVisibleToUser+"  mode: " +mode);

            if (mode == PhotoVideoActivity.MODE_NORMAL){
                if (mAdapter != null && mListView != null){
                    mListView.setAdapter(mAdapter);
                }
//                mListView.setAdapter(mAdapter);
            }else if (mode == MODE_DELETE){
                listViewMode = MODE_DELETE;
                if (editAdapter == null) {
                    editAdapter = new VideoListviewEditAdapter(getContext(), videoList, dataList);
                    mListView.setAdapter(editAdapter);
                } else {
                    mListView.setAdapter(editAdapter);
                    editAdapter.notifyDataSetChanged();
                }
            }else if (mode == PhotoVideoActivity.MODE_DOWNLOAD){
                listViewMode = MODE_DOWNLOAD;
                if (downloadAdapter == null){
                    downloadAdapter = new VideoListviewDownloadAdapter(getContext(),videoList,dataList);
                    mListView.setAdapter(downloadAdapter);
                }else {
                    mListView.setAdapter(downloadAdapter);
                    downloadAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    private VideoListviewDownloadAdapter downloadAdapter;
    private VideoListviewEditAdapter editAdapter;

    private void initListview() {

        mListView = (ListView) mView.findViewById(R.id.pull_refresh_list);
        mListView.setSelector(R.color.color_bank);

        final PhotoVideoActivity parent = (PhotoVideoActivity) this.getActivity();
        //父activity 某些控件的点击事件监听
        parent.setOnMyBtnClickListener(new PhotoVideoActivity.OnMyBtnClickListener() {
            @Override
            public void onButtonClick(Context context, int type) {
                switch (type) {
                    case PhotoVideoActivity.DOWNLOAD_BUTTON:
                        listViewMode = MODE_DOWNLOAD;
                        StringBuilder builder = new StringBuilder();
                        downSelected = downloadAdapter.getSelectIndex();
//                        for (int dayIndex : downSelected.keySet()) {
//                            builder.append("\n"+" 位置  "+ dayIndex + "item:");
//                            ArrayList<Integer> gridIndex =downSelected.get(dayIndex);
//                            for (int j = 0; j < gridIndex.size(); j++) {
//                                builder.append(gridIndex.get(j)+"  ");
//                            }
//                        }

                        doVideoDownLoad();

//                        Log.i("视频下载位置",builder.toString());
                        break;
                    case PhotoVideoActivity.DELETE_BUTTON:
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
                    case PhotoVideoActivity.DOWNLOAD_TOOLBAR:
                        if (downloadAdapter == null) {
                            downloadAdapter = new VideoListviewDownloadAdapter(getContext(), videoList, dataList);
                            mListView.setAdapter(downloadAdapter);
                        } else {
                            mListView.setAdapter(downloadAdapter);
                            downloadAdapter.notifyDataSetChanged();
                        }
                        break;
                    case PhotoVideoActivity.EDIT_TOOLBAR:
                        if (editAdapter == null) {
                            editAdapter = new VideoListviewEditAdapter(getContext(), videoList, dataList);
                            mListView.setAdapter(editAdapter);
                        } else {
                            mListView.setAdapter(editAdapter);
                            editAdapter.notifyDataSetChanged();
                        }
                        break;
                    case PhotoVideoActivity.CANCEL_TOOLBAR:
                        listViewMode = MODE_NORMAL;
                        if (mAdapter == null) {
                            mAdapter = new VideoListviewAdapter(getActivity(), videoList, dataList);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }

//                Toast.makeText(parent, "videogragment", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private DownloadManager downloadManager;

    private void doVideoDownLoad() {

        downloadManager = DownloadService.getDownloadManager(getContext());
//        downloadManager.removeAllTask();
        //设置下载线程数量
        downloadManager.getThreadPool().setCorePoolSize(3);

        int dateIndex = 0;
        int fileIndex = 0;

        for (int dayIndex : downSelected.keySet()) {
//            builderDelete.append("\n"+" 位置  "+ dayIndex + "item:");
            ArrayList<Integer> gridIndex = downSelected.get(dayIndex);
            dateIndex = dayIndex;
            for (int j = 0; j < gridIndex.size(); j++) {
//                builderDelete.append(gridIndex.get(j)+"  ");
                fileIndex = gridIndex.get(j);
                String filePath = videoList.get(dataList.get(dateIndex)).get(fileIndex).getFPath();
//                String fileName = mPhotoList.get(dataList.get(dateIndex)).get(fileIndex).getName();
                String url = BEConstants.ADDRESS_IP_DEVICE + filePath.substring(2).replace("\\", "/");
//                downloadManager.setTargetFolder(SystemUtils.getFileCachePath()+"photo/");
                downloadManager.addTask(url, null);
            }
        }

        downloadAdapter.notifyDataSetChanged();
    }


    private void doVideoDelete() {

        initProgressDialog(getString(R.string.onDelete));
        deleteSelected = editAdapter.getSelectIndex();

        int dateIndex = 0;
        int fileIndex = 0;

//        int success = 0;
//        int failed = 0;

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
                String filePath = videoList.get(dataList.get(dateIndex)).get(fileIndex).getFPath();
                OkHttpUtils.get()//////删除mov
                        .url(BEConstants.ADDRESS_IP_DEVICE)
                        .addParams("custom","1")
                        .addParams("cmd","4003")
                        .addParams("str",filePath)
                        .build()
                        .execute(new CommandCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissDialog();
                            }

                            @Override
                            public void onResponse(String s, int i) {
//                                Toast.makeText(getContext(), "4003返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                                if (s.equals("0")){
//                                    success += 1;
                                    mHandler.sendEmptyMessage(DELETE_SUCCESS_ONE);
                                }else if (s.equals("-4")  || s.equals("-5") || s.equals(-6)){
                                    mHandler.sendEmptyMessage(DELETE_FAILED_ONE);
                                }

                            }
                        });

                Log.i("filePath",filePath.replace(".MOV","V.MOV").replace("MOVIE","MOVIE_VIEW"));
                OkHttpUtils.get()//////删除v.mov
                        .url(BEConstants.ADDRESS_IP_DEVICE)
                        .addParams("custom","1")
                        .addParams("cmd","4003")
                        .addParams("str",filePath.replace(".MOV","V.MOV").replace("MOVIE","MOVIE_VIEW"))
                        .build()
                        .execute(new CommandCallBack() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissDialog();
                            }

                            @Override
                            public void onResponse(String s, int i) {
//                                Toast.makeText(getContext(), "4003返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                                if (s.equals("0")){
//                                    success += 1;
                                    mHandler.sendEmptyMessage(DELETE_SUCCESS_V_ONE);
                                }else if (s.equals("-4")  || s.equals("-5") || s.equals(-6)){
                                    mHandler.sendEmptyMessage(DELETE_FAILED_V_ONE);
                                }

                            }
                        });

            }
        }

    }





    private Handler mHandler;
    //mov视频删除
    private static final int DELETE_SUCCESS_ONE = 101;
    private static final int DELETE_FAILED_ONE = 102;
    //v.mov视频删除
    private static final int DELETE_SUCCESS_V_ONE = 201;
    private static final int DELETE_FAILED_V_ONE = 202;


    private int totalDelete = 0;
    private int successDelete = 0;
    private int failedDelete = 0;

    private int success_v = 0;
    private int failed_v = 0;

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
            case DELETE_SUCCESS_V_ONE:
                success_v += 1;

                break;
            case DELETE_FAILED_V_ONE:
                failed_v += 1;
                break;
        }

        return false;
    }



    private Map<Integer,ArrayList<Integer>> downSelected ;
    private Map<Integer,ArrayList<Integer>> deleteSelected;

    private void initListviewData(){

        if (listViewMode == MODE_NORMAL){
            if (mAdapter == null){
                mAdapter = new VideoListviewAdapter(getActivity(), videoList,dataList);
                mListView.setAdapter(mAdapter);
            }else {
                mAdapter.notifyDataSetChanged();
            }
        } else if (listViewMode == MODE_DELETE) {

            editAdapter = null;
            editAdapter = new VideoListviewEditAdapter(getContext(),videoList,dataList);
            mListView.setAdapter(editAdapter);
            //不能进行刷新
//            editAdapter.notifyDataSetChanged();
            //界面刷新后删除相关的数量要重置
            totalDelete = 0;
            successDelete = 0;
            failedDelete = 0;
        }else if (listViewMode == MODE_DOWNLOAD){
            downloadAdapter.notifyDataSetChanged();
        }


    }

    private ArrayList<FileEntity> responseList = new ArrayList<>();
    private Map<String ,ArrayList<FileEntity>> videoList = new HashMap<>();
    private ArrayList<FileEntity> thumbNailList = new ArrayList<>();
    private ArrayList<String> dataList;

    private void getVideoList(){
        initProgressDialog(getString(R.string.onLoading));
        dataList = new ArrayList<>();

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3015")
                .build()
                .execute(new FileListCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(ArrayList<FileEntity> fileEntities, int i) {
                        dismissDialog();
                        if (fileEntities.size() != 0){
//                            Toast.makeText(getContext(), "3015返回的状态值是：" + fileEntities.size() , Toast.LENGTH_SHORT).show();
                            responseList.clear();
                            responseList.addAll(fileEntities);
                            doRefreshListData();
                        }else {
                            Toast.makeText(getContext(),getString(R.string.loadFailed),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void doRefreshListData() {
        dataList.clear();
        videoList.clear();
        ArrayList<FileEntity> tempList = new ArrayList<FileEntity>();
        for (int i = 0; i < responseList.size(); i++) {//将视频筛选出来
            String name = responseList.get(i).getName();
            String time = responseList.get(i).getTime();
            if (name.endsWith("MOV") && !name.endsWith("V.MOV")){
//                String[] sub = name.split("_");
//                String dataString  = sub[0] + "-"+ sub[1].substring(0,2)+"-"+sub[1].substring(2);
                String dataString  = time.substring(0,10);
                responseList.get(i).dateString = dataString;
                tempList.add(responseList.get(i));//将视频筛选出来

                if (!dataList.contains(dataString)){
                    dataList.add(dataString);
                    //mPhotoList.put(dataString,responseList.get(i));
                }
            }else if (name.endsWith("V.MOV")){
                //筛选出缩略图文件
                thumbNailList.add(responseList.get(i));
            }
        }
        for (int j = 0; j < dataList.size(); j++) {//将图片进行分组
            String date = dataList.get(j);
            ArrayList<FileEntity> fileEntities = new ArrayList<FileEntity>();
            for (int m = 0; m < tempList.size(); m++) {
                FileEntity file = tempList.get(m);
                if (date.equals(file.dateString)){
                    if (isHasThumbNailFile(file.getName())){
                        file.isHasThumbNail = true;
                    }else
                        file.isHasThumbNail = false;
                    fileEntities.add(tempList.get(m));
                }
            }
            videoList.put(date,fileEntities);
        }
//        doChangeMode();
        initListviewData();
    }


    /**
     * 检查视频文件是否有缩略视频
     * @param fileName
     * @return
     */
    private boolean isHasThumbNailFile(String fileName){
        boolean result = false;
        for (int i = 0; i < thumbNailList.size(); i++) {
            String name = thumbNailList.get(i).getName().replace("V.MOV","");
//            Log.i("fragmentVideo", name +"  " + fileName.replace(".MOV",""));
            if (name.equals(fileName.replace(".MOV",""))){
                result = true;
                break;
            }
        }
        return result;
    }

    private void doChangeMode(){
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom","1")
                .addParams("cmd","3001")
                .addParams("par","2")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
//                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")){

                        }
                    }
                });
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
                pd.setCancelable(false);
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
