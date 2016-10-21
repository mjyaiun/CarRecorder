package com.mitu.carrecorder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 拍照和从相册选取照片的工具类
 * @author
 */
public class PhotoUtils {
	/**拍照常数*/
	public static final int TAKE_PHOTO = 0;
	/**从相册选择常数*/
	public static final int GET_IMAGE_LIST = 1;
	/**缩放图片常数*/
	public static final int ZOOM_PIC = 2;
	/**拍照文件的路径*/
	public static String TAKE_PHOTO_FILE_PATH;

	/**
	 * 拍照工具方法
	 * @param activity activity对象
	 */
	public static void takePhoto(Activity activity){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//文件路径
		String filePath = Environment.getExternalStorageDirectory() + "/ShenGu/";
		File f = new File(filePath);
		if(!f.exists()){
			f.mkdirs(); //创建文件夹
		}
		f = new File(f,System.currentTimeMillis() + ".jpg"); //图片路径
		if(f.exists()){
			f.delete();
		}
		TAKE_PHOTO_FILE_PATH = f.getAbsolutePath();
		Uri uri = Uri.fromFile(f);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, TAKE_PHOTO);
	}

	/**
	 * 获得相册文件列表
	 * @param activity activity对象
	 */
	public static void requestGalleryPhoto (Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		activity.startActivityForResult(intent, GET_IMAGE_LIST);
	}

	/**
	 * 缩放图片
	 * @param uri 图片vuri
	 * @param activity activity对象
	 */
	public static void startPhotoZoom(Uri uri,Activity activity) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 2);
		intent.putExtra("outputX",640 );
		intent.putExtra("outputY", 640);
		if(Build.VERSION.SDK_INT > 8){//2.2及以上
			intent.putExtra("return-data", false);
			intent.putExtra("scale", true);
			intent.putExtra("noFaceDetection", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		}else{
			intent.putExtra("return-data", true);
		}
		activity.startActivityForResult(intent, ZOOM_PIC);
	}

	/**剪裁以后的图片对象*/
	private static File CROP_FILE;

	private static Uri getTempUri() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory() + "/ShenGu/";
			File f = new File(filePath);
			if(!f.exists()){
				f.mkdirs(); //创建文件夹
			}
			CROP_FILE = new File(f,"temp.jpg"); //图片路径
			if(CROP_FILE.exists()){
				CROP_FILE.delete();
			}
			try {
				CROP_FILE.createNewFile();
			} catch (IOException e) {
			}
			return Uri.fromFile(CROP_FILE);
		}
		return null;
	}

	/**
	 * 读取剪裁以后的图片
	 * @param context 上下文对象
	 * @param data 数据对象，用于2.2版本的读取
	 * @return 图片对象
	 */
	public static Bitmap getCroppedBitmap(Context context,Intent data){
		Bitmap bitmap = null;
		if(Build.VERSION.SDK_INT > 8){//2.2及以上
			try {
				bitmap = BitmapFactory.decodeFile(CROP_FILE.getAbsolutePath());
				//删除图片
				CROP_FILE.delete();
			} catch (Exception e) {
//			    e.printStackTrace();
			}
		}else{
			try{
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					bitmap = bundle.getParcelable("data");
				}
			}catch(Exception e){
//				e.printStackTrace();
			}
		}
		return bitmap;
	}

	/**
	 * 从文件中生成图片
	 * @param filePath 文件路径
	 * @return bitmap,if not exist return null
	 */
	public static Bitmap getBitMapByFilePath(String filePath){
		return getBitMapByFilePath(filePath,1);
	}

	/**
	 * 从文件中生成图片
	 * @param filePath 文件路径
	 * @return bitmap,if not exist return null
	 */
	public static Bitmap getBitMapByFilePath(String filePath,int scale){
		Log.d("getBitMapByFilePath:", filePath);
		try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeFile(filePath,opts);
			return bitmap;
		}catch(Exception e){
//			e.printStackTrace();
		}
		return null;
	}
}
