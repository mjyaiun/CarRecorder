package com.mitu.carrecorder.phvedio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.mitu.carrecorder.adapter.PhotoListviewAdapter;
import com.mitu.carrecorder.adapter.PhotoListviewDownloadAdapter;
import com.mitu.carrecorder.adapter.PhotoListviewEditAdapter;
import com.mitu.carrecorder.entiy.FileEntity;
import com.mitu.carrecorder.net.CommandCallBack;
import com.mitu.carrecorder.net.FileListCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 照片
 *
 * @author Administrator
 */
public class FragmentPhoto extends BaseFragment implements Handler.Callback{

    private ListView mListView;
    //test
    private PhotoListviewAdapter mAdapter;
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

        initUI();
        mHandler = new Handler(this);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            int mode = ( (PhotoVideoActivity)getActivity()).getPageMode();
            Log.i("fragment",":photo"+isVisibleToUser+"  mode: " +mode);

            if (mode == PhotoVideoActivity.MODE_NORMAL){
                listViewMode = MODE_NORMAL;
                if (mAdapter != null && mListView != null){
                    mListView.setAdapter(mAdapter);
                }

            }else if (mode == PhotoVideoActivity.MODE_DELETE){
                listViewMode = MODE_DELETE;
                if (editAdapter == null) {
                    editAdapter = new PhotoListviewEditAdapter(getContext(), mPhotoList, dataList);
                    mListView.setAdapter(editAdapter);
                } else {
                    mListView.setAdapter(editAdapter);
                    editAdapter.notifyDataSetChanged();
                }
            }else if (mode == PhotoVideoActivity.MODE_DOWNLOAD){
                listViewMode = MODE_DOWNLOAD;
                if (downloadAdapter == null){
                    downloadAdapter = new PhotoListviewDownloadAdapter(getContext(),mPhotoList,dataList);
                    mListView.setAdapter(downloadAdapter);
                }else {
                    mListView.setAdapter(downloadAdapter);
                    downloadAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initUI() {
        initListview();
        initContent();
        //getPhotoList();
        //initTestData();
        getPhotoList();

    }

    private void initContent() {

    }

    private ArrayList<FileEntity> responseList = new ArrayList<>();
    private Map<String, ArrayList<FileEntity>> mPhotoList = new HashMap<>();
    private ArrayList<String> dataList;


    private PhotoListviewEditAdapter editAdapter;
    private PhotoListviewDownloadAdapter downloadAdapter;

    private void getPhotoList() {
        initProgressDialog(getString(R.string.onLoading));
        dataList = new ArrayList<>();

        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom", "1")
                .addParams("cmd", "3015")
                .build()
                .execute(new FileListCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(ArrayList<FileEntity> fileEntities, int i) {
                        dismissDialog();
                        if (fileEntities.size() != 0) {
//                            Toast.makeText(getContext(), "3015返回的状态值是：" + fileEntities.size() , Toast.LENGTH_SHORT).show();
                            responseList.clear();
                            responseList.addAll(fileEntities);
                            doRefreshListData();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.loadFailed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void doRefreshListData() {
        ArrayList<FileEntity> tempList = new ArrayList<FileEntity>();
        for (int i = 0; i < responseList.size(); i++) {//将图片筛选出来
            String name = responseList.get(i).getName();
            String time = responseList.get(i).getTime();
            if (name.endsWith("JPG")) {
//                String[] sub = name.split("_");
//                String dataString  = sub[0] + "-"+ sub[1].substring(0,2)+"-"+sub[1].substring(2);
                String dataString = time.substring(0, 10);
                responseList.get(i).dateString = dataString;
                tempList.add(responseList.get(i));//将图片筛选出来

                if (!dataList.contains(dataString)) {
                    dataList.add(dataString);
                    //mPhotoList.put(dataString,responseList.get(i));
                }
            }
        }
        for (int j = 0; j < dataList.size(); j++) {//将图片进行分组
            String date = dataList.get(j);
            ArrayList<FileEntity> fileEntities = new ArrayList<FileEntity>();
            for (int m = 0; m < tempList.size(); m++) {
                if (date.equals(tempList.get(m).dateString)) {
                    fileEntities.add(tempList.get(m));
                }
            }
            mPhotoList.put(date, fileEntities);
        }
//        doChangeMode();
        initListviewData();
    }

    private void doChangeMode() {
        OkHttpUtils.get()
                .url(BEConstants.ADDRESS_IP_DEVICE)
                .addParams("custom", "1")
                .addParams("cmd", "3001")
                .addParams("par", "2")
                .build()
                .execute(new CommandCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
//                        hideOnNetProgress();
                    }

                    @Override
                    public void onResponse(String s, int i) {
//                        Toast.makeText(VideoPlayActivity.this, "3001返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                        if (s.equals("0")) {

                        }
                    }
                });
    }

    
    private Map<Integer,ArrayList<Integer>> downSelected ;
    private Map<Integer,ArrayList<Integer>> deleteSelected;


    private void initListview() {
        //mHandler = new Handler(new InnerCallback());
        mListView = (ListView) mView.findViewById(R.id.pull_refresh_list);
        mListView.setSelector(R.color.color_bank);

//        mAdapter = new PhotoListviewAdapter(getActivity(), mPhotoList, dataList);
//        mListView.setAdapter(mAdapter);

        final PhotoVideoActivity parent = (PhotoVideoActivity) this.getActivity();
        parent.setOnMyBtnClickListener(new PhotoVideoActivity.OnMyBtnClickListener() {
            @Override
            public void onButtonClick(Context context, int type) {
                switch (type) {
                    case PhotoVideoActivity.DOWNLOAD_BUTTON:
                        listViewMode = MODE_DOWNLOAD;
//                        StringBuilder builder = new StringBuilder();
                        downSelected = downloadAdapter.getSelectIndex();
//                        for (int dayIndex : downSelected.keySet()) {
//                            builder.append("\n"+" 位置  "+ dayIndex + "item:");
//                              ArrayList<Integer> gridIndex = downSelected.get(dayIndex);
//                            for (int j = 0; j < gridIndex.size(); j++) {
//                                builder.append(gridIndex.get(j)+"  ");
//                            }
//                        }
                        doPhotoDownLoad();
//                        Log.i("下载位置",builder.toString());
                        break;
                    case PhotoVideoActivity.DELETE_BUTTON:
                        listViewMode = MODE_DELETE;
//                        StringBuilder builderDelete = new StringBuilder();
                        deleteSelected = editAdapter.getSelectIndex();
//                        for (int dayIndex : deleteSelected.keySet()) {
//                            builderDelete.append("\n"+" 位置  "+ dayIndex + "item:");
//                            ArrayList<Integer> gridIndex = deleteSelected.get(dayIndex);
//                            for (int j = 0; j < gridIndex.size(); j++) {
//                                builderDelete.append(gridIndex.get(j)+"  ");
//                            }
//                        }
                        //
                        doDeletePhotos();
//                        Log.i("删除位置",builderDelete.toString());

                        break;
                    case PhotoVideoActivity.DOWNLOAD_TOOLBAR:

                        if (downloadAdapter == null) {
                            downloadAdapter = new PhotoListviewDownloadAdapter(getContext(), mPhotoList, dataList);
                            mListView.setAdapter(downloadAdapter);
                        } else {
                            mListView.setAdapter(downloadAdapter);
                            downloadAdapter.notifyDataSetChanged();
                        }
                        break;
                    case PhotoVideoActivity.EDIT_TOOLBAR:
                        if (editAdapter == null) {
                            editAdapter = new PhotoListviewEditAdapter(getContext(), mPhotoList, dataList);
                            mListView.setAdapter(editAdapter);
                        } else {
                            mListView.setAdapter(editAdapter);
                            editAdapter.notifyDataSetChanged();
                        }
                        break;
                    case PhotoVideoActivity.CANCEL_TOOLBAR:
                        listViewMode = MODE_NORMAL;
                        if (mAdapter == null) {
                            mAdapter = new PhotoListviewAdapter(getActivity(), mPhotoList, dataList);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }

//                Toast.makeText(parent, "photogragment", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private DownloadManager downloadManager;
    /**
     * 网络请求，执行删除任务
     */
    private void doPhotoDownLoad() {
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
                String filePath = mPhotoList.get(dataList.get(dateIndex)).get(fileIndex).getFPath();
//                String fileName = mPhotoList.get(dataList.get(dateIndex)).get(fileIndex).getName();
                String url = BEConstants.ADDRESS_IP_DEVICE + filePath.substring(2).replace("\\", "/");
//                downloadManager.setTargetFolder(SystemUtils.getFileCachePath()+"photo/");
                downloadManager.addTask(url, null);
            }
        }

        downloadAdapter.notifyDataSetChanged();

    }

    private void doDeletePhotos() {
        initProgressDialog(getString(R.string.onDelete));
        deleteSelected = editAdapter.getSelectIndex();

        int dateIndex = 0;
        int fileIndex = 0;

        for (int dayIndex : deleteSelected.keySet()) {
//            builderDelete.append("\n"+" 位置  "+ dayIndex + "item:");
            dateIndex = dayIndex;
            ArrayList<Integer> gridIndex = deleteSelected.get(dateIndex);
            totalDelete += gridIndex.size();
        }

        for ( int dayIndex : deleteSelected.keySet()) {
            dateIndex = dayIndex;
            ArrayList<Integer> gridIndex = deleteSelected.get(dateIndex);

            for (int m = 0; m < gridIndex.size(); m++) {
                fileIndex = gridIndex.get(m);
                String filePath = mPhotoList.get(dataList.get(dateIndex)).get(fileIndex).getFPath();
                OkHttpUtils.get()//////删除photo
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
                                Toast.makeText(getContext(), "4003返回的状态值是：" + s , Toast.LENGTH_SHORT).show();
                                if (s.equals("0")){
//                                    success += 1;
                                    mHandler.sendEmptyMessage(DELETE_SUCCESS_ONE);
                                }else if (s.equals("-4")  || s.equals("-5") || s.equals(-6)){
                                    mHandler.sendEmptyMessage(DELETE_FAILED_ONE);
                                }

                            }
                        });


            }
        }
    }


    private Handler mHandler;
    //删除
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
                    getPhotoList();
                }
                break;
            case DELETE_FAILED_ONE:
                failedDelete += 1;
                Log.i("filepath","total:"+totalDelete + "  success:"+ successDelete + "  failed："+ failedDelete);
                if (totalDelete == successDelete + failedDelete){
                    dismissDialog();
                    getPhotoList();
                }
                break;

        }

        return false;
    }


    private void initListviewData() {

        if (listViewMode == MODE_NORMAL){
            if (mAdapter == null) {
                mAdapter = new PhotoListviewAdapter(getActivity(), mPhotoList, dataList);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }else if (listViewMode == MODE_DELETE){
            editAdapter = null;
            editAdapter = new PhotoListviewEditAdapter(getContext(),mPhotoList,dataList);
            mListView.setAdapter(editAdapter);
//            editAdapter.notifyDataSetChanged();
            totalDelete = 0;
            successDelete = 0;
            failedDelete = 0;
        }else if (listViewMode == MODE_DOWNLOAD){
            downloadAdapter.notifyDataSetChanged();

        }


        mAdapter.setOnGridViewItemClickListener(new PhotoListviewAdapter.OnGridviewItemClickListener() {
            @Override
            public void OnItemClick(Context context, int gridviewIndex, int listviewIndex) {
                Intent intent = new Intent();
                intent.setClass(getContext(),BigPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photoList", mPhotoList.get(dataList.get(listviewIndex)));
                bundle.putString("dateStr", dataList.get(listviewIndex));
                bundle.putInt("gv_posi", gridviewIndex);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    /**
     * 联网对话框
     */
    public Dialog pd;

    /**
     * 初始化联网进度条对象
     */
    public void initProgressDialog(String info) {
        try {
            if (pd == null) {
                pd = new Dialog(this.getContext(), R.style.customDialog);
                LayoutInflater inflater = LayoutInflater.from(this.getContext());
                View v = inflater.inflate(R.layout.item_progress_dialog, null);// 得到加载view
                TextView content = (TextView) v.findViewById(R.id.textViewContent);
                content.setText(info);
                pd.setContentView(v);
                pd.setCancelable(false);
                pd.show();
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
//						isResponseData = true;
                    }
                });
            } else {
                pd.show();
            }
        } catch (Exception e) {
        }
    }

    /**
     * dismiss联网进度条
     */
    public void dismissDialog() {
        if (pd != null) {
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
