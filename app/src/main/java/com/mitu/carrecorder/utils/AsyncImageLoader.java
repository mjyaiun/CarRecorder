package com.mitu.carrecorder.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.mitu.carrecorder.BEApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图片缓存器类
 * @author
 */
public class AsyncImageLoader {
	/**图片索引缓冲*/
	private HashMap<String, SoftReference<Bitmap>> imageCache;
	/**正在下载的图片地址list*/
	private static ArrayList<String> urlList;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		urlList = new ArrayList<String>();
	}

	private Bitmap bitmap;

	public Bitmap loadBitmap(final String imageUrl,
							 final ImageCallback imageCallback) {
		return loadBitmap(imageUrl,imageCallback,false);
	}

	public Bitmap loadBitmap(final String imageUrl,
							 final ImageCallback imageCallback,boolean isWait) {
		bitmap = null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (message.obj != null) {
					imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
				}
			}
		};
		// 从存储读取图片
		bitmap = ImageUtils.loadBitmap(imageUrl);
		if(bitmap != null){
			imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		}
		//判断是否已在下载地址列表中
		boolean isInUrlList = false;
		for(int i = 0;i < urlList.size();i++){
			String url = urlList.get(i);
			if(url != null && url.equals(imageUrl)){
				isInUrlList = true;
				break;
			}
		}
		if(!isInUrlList){//不在下载地址列表中
			urlList.add(imageUrl);
			new Thread() {
				public void run() {
					bitmap = loadImageFromUrl(imageUrl);
					if (bitmap != null) {
						imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
						Message message = handler.obtainMessage(0, bitmap);
						handler.sendMessage(message);
					}
				}
			}.start();
		}
		return null;
	}

	public static Bitmap loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		if (url != null && url.length() > 0) {
			try {
				m = new URL(url);
				i = (InputStream) m.getContent();
				//下载数据
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int num = i.read(buffer);
				while(num > 0){
					baos.write(buffer, 0, num);
					num = i.read(buffer);
				}
				byte[] data = baos.toByteArray();
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
				BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				BitmapFactory.decodeStream(i, null, opts);
				Bitmap b = null;
				if(opts.outWidth > BEApplication.width){
					int scale = opts.outWidth / BEApplication.width;
					opts.inJustDecodeBounds = false;
					opts.inSampleSize = 1 / scale;
					b = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				}else{//导入原图
					b = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
				// 存储网络下载的图片
				if (b != null) {
					ImageUtils.saveImage(url, b);
				}
				i.close();
				urlList.remove(url); //删除下载地址
				return b;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		urlList.remove(url); //删除下载地址
		return null;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap, String imageUrl);
	}
}