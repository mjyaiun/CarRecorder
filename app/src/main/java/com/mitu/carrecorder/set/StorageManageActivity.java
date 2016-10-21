package com.mitu.carrecorder.set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.activity.BaseActivity;

import java.io.File;

public class StorageManageActivity extends BaseActivity {
    private String TAG = "com.mh.carcorder.set";
    private TextView tvz, tvy, tvs, tvphoto, tvvideo, tvpsize, tvvsize, tvsavephoto, tvsavevideo;
    private EditText etyl;
    private long sDTotalSize, TotalBlocks, BloSize, AvaBlock, Freeblock,
            sDFreeSize, sDuse, yl, totalsize1;
    private int photoNum = 0;
    private int videoNum = 0;
    private File filePath;
    private long z, s, y;
    //private SpHelper sp;
    private ProgressBar useBar = null;
    private long photoSize, videoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //sp = new SpHelper(StorageManageActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_manage);
        filePath = Environment.getExternalStorageDirectory();
        //getExternalStorage();
    }

    private void getExternalStorageInfo(String path) {

        File file = new File(path);
        totalsize1 = file.length() + totalsize1;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                getExternalStorageInfo(files[i].getAbsolutePath());
            }
        } else {
            String pathTem = file.getAbsolutePath();

            if (pathTem.endsWith(".jpg") || pathTem.endsWith(".png")
                    || pathTem.endsWith(".gif") || pathTem.endsWith(".jepg")) {
                photoNum++;
                photoSize += file.length();
                Log.i(TAG, "pho" + photoNum);
                Log.i("。。。。。。。。。。。。。。。。", "size" + photoSize);
            }


            if (pathTem.endsWith(".mp4") || pathTem.endsWith(".mpe")
                    || pathTem.endsWith(".avi") || pathTem.endsWith(".rmvb") || pathTem.endsWith(".rm")
                    || pathTem.endsWith(".asf") || pathTem.endsWith(".divx") || pathTem.endsWith(".mpg") || pathTem.endsWith(".mpeg") || pathTem.endsWith(".wmv")
                    || pathTem.endsWith(".mkv") || pathTem.endsWith(".vob")) {
                videoNum++;
                Log.i(TAG, "v" + videoNum);
                videoSize += file.length();
                Log.i("。。。。。。。。。。。。。。。。", "size" + videoSize);
            }
        }
        Log.i(TAG, "pho" + photoNum + "v" + videoNum);
    }

    private void getExternalStorage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            new GetExternalStorageAsyncTask().execute();
        } else {
            toa();
        }
    }

    protected void toa() {
        Toast.makeText(this, "无SD卡", Toast.LENGTH_SHORT).show();
    }

    public class GetExternalStorageAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            useBar = (ProgressBar) findViewById(R.id.my_progress);
            getExternalStorageInfo(filePath.getAbsolutePath());
            z = filePath.getTotalSpace();
            s = filePath.getUsableSpace();
            y = filePath.getTotalSpace()
                    - filePath.getUsableSpace();
            long x = s / photoSize;
            long ys = s / videoSize;
            int a = (int) x;
            int c = (int) ys;
            int x1 = a * photoNum;
            int y1 = c * videoNum;
            long m = y / (z / 150);
            int n = (int) m;
            videoSize = videoSize;
            videoNum = videoNum;
            photoNum = photoNum;
            photoSize = photoSize;
//				  Log.i("4.....", z+".."+zo+".."+y+".."+yo+".."+s+".."+ sh+""+m+".."+n);
//				etyl = (EditText) findViewById(R.id.cc_setting_yl);

//				if (condition) {
//					
//				}
//				double r = (double) a / (double) b;
            Message msg = new Message();
            msg.what = 0x1234;
            Bundle b = new Bundle();// 存放数据
            b.putString("z", Formatter.formatFileSize(StorageManageActivity.this, z));
            b.putString("s", Formatter.formatFileSize(StorageManageActivity.this, s));
            b.putString("y", Formatter.formatFileSize(StorageManageActivity.this, y));
            b.putString("psize", Formatter.formatFileSize(StorageManageActivity.this, photoSize));
            b.putString("vsize", Formatter.formatFileSize(StorageManageActivity.this, videoSize));
            b.putString("p", photoNum + "");
            b.putString("v", videoNum + "");
            b.putInt("n", n);
            b.putInt("x1", x1);
            b.putInt("y1", y1);
            msg.setData(b);
            handler.sendMessage(msg);
            Log.i("2.....", msg.toString());
            return null;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {
                String photoNum = msg.getData().getString("p");
                String videoNum = msg.getData().getString("v");
                String z = msg.getData().getString("z");
                String y = msg.getData().getString("y");
                String s = msg.getData().getString("s");
                String vsize = msg.getData().getString("vsize");
                String psize = msg.getData().getString("psize");
                int n = msg.getData().getInt("n");
                int xnum = msg.getData().getInt("x1");
                int ynum = msg.getData().getInt("y1");
                for (int k = 0; k < n; k++) {
                    useBar.setProgress(k);
                }
                sp.setPHTONUM(photoNum + "张");
                sp.setSNC(s);
                sp.setVIDEONUM(videoNum + "个");
                sp.setYYC(y);
                sp.setZNC(z);
                sp.setPhotosize(psize);
                sp.setPsave(xnum + "张");
                sp.setVideosive(vsize);
                sp.setVsave(ynum + "个");
                tvphoto.setText(photoNum + "张");
                tvvideo.setText(videoNum + "个");
                tvz.setText(z);
                tvs.setText(s);
                tvy.setText(y);
                tvvsize.setText(vsize);
                tvpsize.setText(psize);
                tvsavephoto.setText(xnum + "张");
                tvsavevideo.setText(ynum + "个");
                pd.cancel();
            }
        }
    };

    @Override
    public void initView() {
        setTitle(getString(R.string.storageManage));
        initTitleBack();
        tvz = (TextView) findViewById(R.id.cc_setting_zyl);
        tvsavephoto = (TextView) findViewById(R.id.tv_photosave);
        tvsavevideo = (TextView) findViewById(R.id.tv_videosave);
        tvpsize = (TextView) findViewById(R.id.tv_photosize);
        tvvsize = (TextView) findViewById(R.id.tv_videosize);
        tvsavephoto.setText("正在计算...");
        tvsavevideo.setText("正在计算...");
        tvz.setText("正在计算...");
        tvpsize.setText("正在计算...");
        tvvsize.setText("正在计算...");
        tvphoto = (TextView) findViewById(R.id.tv_photo);
        tvphoto.setText("正在计算...");
        tvvideo = (TextView) findViewById(R.id.tv_video);
        tvvideo.setText("正在计算...");
        tvy = (TextView) findViewById(R.id.cc_setting_yy);
        tvy.setText("正在计算...");
        tvs = (TextView) findViewById(R.id.cc_setting_hs);
        tvs.setText("正在计算...");
        if (!sp.getSNC().equals(null)) {
            tvs.setText(sp.getSNC());
        }
        if (!sp.getPHTONUM().equals(null)) {
            tvphoto.setText(sp.getPHTONUM());
        }
        if (!sp.getVIDEONUM().equals(null)) {
            tvvideo.setText(sp.getVIDEONUM());
        }
        if (!sp.getZNC().equals(null)) {
            tvz.setText(sp.getZNC());
        }
        if (!sp.getYYC().equals(null)) {
            tvy.setText(sp.getYYC());
        }
        if (!sp.getVsave().equals(null)) {
            tvz.setText(sp.getVsave());
        }
        if (!sp.getPsave().equals(null)) {
            tvsavephoto.setText(sp.getPsave());
        }
        if (!sp.getVideosize().equals(null)) {
            tvvsize.setText(sp.getVideosize());
        }
        if (!sp.getPhotosize().equals(null)) {
            tvpsize.setText(sp.getPhotosize());
        }
//        pd.show();
    }

}
