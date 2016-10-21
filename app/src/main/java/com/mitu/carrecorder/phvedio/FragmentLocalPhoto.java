package com.mitu.carrecorder.phvedio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.mitu.carrecorder.adapter.PhotoListviewLocalAdapter;
import com.mitu.carrecorder.adapter.PhotoListviewLocalEditAdapter;
import com.mitu.carrecorder.utils.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 照片
 *
 * @author Administrator
 */
public class FragmentLocalPhoto extends BaseFragment implements Handler.Callback{

    private ListView mListView;
    //test
    private PhotoListviewLocalAdapter mAdapter;
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

        initUI();
        mHandler = new Handler(this);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            int mode = ( (LocalPhotoVideoActivity)getActivity()).getPageMode();
            Log.i("fragment",":photo"+isVisibleToUser+"  mode: " +mode);

            if (mode == PhotoVideoActivity.MODE_NORMAL){
                listViewMode = MODE_NORMAL;
                if (mAdapter != null && mListView != null){
                    mListView.setAdapter(mAdapter);
                }

                //
            }else if (mode == PhotoVideoActivity.MODE_DELETE){
                listViewMode = MODE_DELETE;
                if (editAdapter == null) {
                    editAdapter = new PhotoListviewLocalEditAdapter(getContext(), mPhotoList, dateList);
                    mListView.setAdapter(editAdapter);
                } else {
                    mListView.setAdapter(editAdapter);
                    editAdapter.notifyDataSetChanged();
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

    private Map<String, ArrayList<String>> mPhotoList = new HashMap<>();
    private ArrayList<String> dateList;


    private PhotoListviewLocalEditAdapter editAdapter;

    private void getPhotoList() {
        initProgressDialog(getString(R.string.onLoading));
        if (dateList == null){
            dateList = new ArrayList<>();
        }else
            dateList.clear();

        ArrayList<String> tempList = new ArrayList<>();

        String path = SystemUtils.getLocalFilePath();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null){
            for (int i = 0; i < files.length; i++) {//遍历出所有图片文件
                File child = files[i];
                String name = child.getName();
                if (name.endsWith("JPG")){
                    tempList.add(name);
                }
            }

            for (int m = 0; m < tempList.size(); m++) {//初始化日期列表
                String name = tempList.get(m);
                String[] subItem = name.split("_");
                if (subItem.length > 2){
                    String date = subItem[0]+"-"+subItem[1];
                    if (!dateList.contains(date)){
                        dateList.add(date);
                    }
                }
            }


            for (int n = 0; n < dateList.size(); n++) {
                String date = dateList.get(n);
                ArrayList<String> fileList = new ArrayList<>();
                for (int f = 0; f < tempList.size(); f++) {
                    String name = tempList.get(f);
                    String datef = name.split("_")[0] + "-"+name.split("_")[1];
                    if (date.equals(datef)){
                        fileList.add(name);
                    }
                }

                mPhotoList.put(date,fileList);

            }
        }

        initListviewData();

    }

    private Map<Integer,ArrayList<Integer>> deleteSelected;

    private void initListview() {
        //mHandler = new Handler(new InnerCallback());
        mListView = (ListView) mView.findViewById(R.id.pull_refresh_list);
        mListView.setSelector(R.color.color_bank);

//        mAdapter = new PhotoListviewAdapter(getActivity(), mPhotoList, dataList);
//        mListView.setAdapter(mAdapter);

        final LocalPhotoVideoActivity parent = (LocalPhotoVideoActivity) this.getActivity();
        parent.setOnMyBtnClickListener(new LocalPhotoVideoActivity.OnMyBtnClickListener() {
            @Override
            public void onButtonClick(Context context, int type) {
                switch (type) {
                    case LocalPhotoVideoActivity.DELETE_BUTTON:
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

                    case LocalPhotoVideoActivity.EDIT_TOOLBAR:
                        listViewMode = MODE_DELETE;
                        if (editAdapter == null) {
                            editAdapter = new PhotoListviewLocalEditAdapter(getContext(), mPhotoList, dateList);
                            mListView.setAdapter(editAdapter);
                        } else {
                            mListView.setAdapter(editAdapter);
                            editAdapter.notifyDataSetChanged();
                        }
                        break;
                    case LocalPhotoVideoActivity.CANCEL_TOOLBAR:
                        listViewMode = MODE_NORMAL;
                        if (mAdapter == null) {
                            mAdapter = new PhotoListviewLocalAdapter(getActivity(), mPhotoList, dateList);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }

                Toast.makeText(parent, "photogragment", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private List<DownloadInfo> allTask;
    private DownloadManager downloadManager;


    private void doDeletePhotos() {
        initProgressDialog(getString(R.string.onDelete));
        //
        downloadManager = DownloadService.getDownloadManager(this.getContext());
        allTask = downloadManager.getAllTask();
        //
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
                String fileName = mPhotoList.get(dateList.get(dateIndex)).get(fileIndex);
               ///
                //优先要删除下载队列的
                ///要删除下载队列数据库表的任务，任务会同时删除下载的文件
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
        getPhotoList();
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

        dismissDialog();

        if (listViewMode == MODE_NORMAL){
            if (mAdapter == null) {
                mAdapter = new PhotoListviewLocalAdapter(getActivity(), mPhotoList, dateList);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }else if (listViewMode == MODE_DELETE){
            editAdapter.notifyDataSetChanged();
            totalDelete = 0;
            successDelete = 0;
            failedDelete = 0;
        }

        mAdapter.setOnGridViewItemClickListener(new PhotoListviewLocalAdapter.OnGridviewItemClickListener() {
            @Override
            public void OnItemClick(Context context, int gridviewIndex, int listviewIndex) {
                Intent intent = new Intent();
                intent.setClass(getContext(),LocalBigPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photoList", mPhotoList.get(dateList.get(listviewIndex)));
                bundle.putString("dateStr", dateList.get(listviewIndex));
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
