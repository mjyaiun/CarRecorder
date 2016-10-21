package com.mitu.carrecorder.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import com.mitu.carrecorder.BEApplication;
import com.mitu.carrecorder.BEConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 图片处理工具类
 *
 * @author
 */
public class ImageUtils {
	/**
	 * 缩放图片
	 *
	 * @param bitmap
	 *            原始图片
	 * @param w
	 *            缩放以后的宽度
	 * @return 缩放以后的图片
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int w) {
		if (bitmap == null) {
			return null;
		}
		int bW = bitmap.getWidth();
		int bH = bitmap.getHeight();
		// 计算缩放比例
		float scaleWidth = (float) w / bW; // 水平缩放比例
		Matrix m = new Matrix();
		m.postScale(scaleWidth, scaleWidth);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bW, bH, m, true);
		return newBitmap;
	}



	public static Bitmap scaleBitmap(Bitmap bitmap, int w,int h){
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * 缩放图片
	 *
	 * @param bitmap
	 *            原始图片
	 * @param h
	 *            缩放以后的宽度
	 * @return 缩放以后的图片
	 */
	public static Bitmap scaleBitmapByHeight(Bitmap bitmap, int h) {
		if (bitmap == null) {
			return null;
		}
		int bW = bitmap.getWidth();
		int bH = bitmap.getHeight();
		// 计算缩放比例
		float scaleHeight = (float) h / bH; // 垂直缩放比例
		Matrix m = new Matrix();
		m.postScale(scaleHeight, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bW, bH, m, true);
		return newBitmap;
	}

	/**
	 * 存储图片对象到本地存储卡
	 *
	 * @param url
	 *            图片地址，使用地址中最后的内容作为文件名
	 * @param bitmap
	 *            图片对象
	 */
	public static void saveImage(String url, Bitmap bitmap) {
		if (url != null && bitmap != null) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {// 有存储卡
				String filePath = Environment.getExternalStorageDirectory()
						+ BEConstants.FOLDER;
				File f = new File(filePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				f = new File(f, "images");
				if (!f.exists()) {
					f.mkdirs();
				}
				try {
					// 获得文件名
					int index = url.lastIndexOf('/');
					if (index != -1) {
						String fileName = url.substring(index + 1);
						f = new File(f, fileName);
						if (fileName.length() > 0) {
							// 转换图片数据
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							if (bitmap.getWidth() > BEApplication.width) {// 如果图片宽度超过屏幕尺寸，则缩放成屏幕尺寸大小
								bitmap = scaleBitmap(bitmap,
										BEApplication.width);
							}
							if (fileName.endsWith(".png")
									|| fileName.equals(".PNG")) {
								bitmap.compress(Bitmap.CompressFormat.PNG, 100,
										baos);
							} else {
								bitmap.compress(Bitmap.CompressFormat.JPEG,
										100, baos);
							}
							byte[] b = baos.toByteArray();
							FileOutputStream fos = new FileOutputStream(f);
							fos.write(b);
							fos.close();
							baos.close();
						}
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 导入图片，如果本地已存储则使用本地图片
	 *
	 * @param url
	 *            url地址
	 * @return 图片对象，如果没有则返回null
	 */
	public static Bitmap loadBitmap(String url) {
		if (url != null && url.length() > 0) {
			try {
				// 获得文件名
				int index = url.lastIndexOf('/');
				if (index != -1) {
					String fileName = url.substring(index + 1);
					String filePath = Environment.getExternalStorageDirectory()
							+ BEConstants.FOLDER + "images/";
					File f = new File(filePath, fileName);
					if (f.exists()) {
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
						BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
						// int[] dimen = {opts.outWidth, opts.outHeight};
						Bitmap b = null;
						if (opts.outWidth > BEApplication.width) {
							int scale = opts.outWidth / BEApplication.width;
							opts.inJustDecodeBounds = false;
							opts.inSampleSize = 1 / scale;
							b = BitmapFactory.decodeFile(f.getAbsolutePath(),
									opts);
						} else {// 导入原图
							b = BitmapFactory.decodeFile(f.getAbsolutePath());
						}
						return b;
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 删除所有本地缓存的图片
	 */
	public static void delAllImage() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {// 有存储卡
				String filePath = Environment.getExternalStorageDirectory()
						+ BEConstants.FOLDER + "images/";
				File f = new File(filePath);
				File[] list = f.listFiles();
				if (list != null && list.length > 0) {
					for (int i = 0; i < list.length; i++) {
						if (list[i].isFile()) {
							list[i].delete();
						}
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * 将Bitmap对象转换为byte数组
	 *
	 * @param bm
	 *            bitmap对象
	 * @return byte数组
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		return baos.toByteArray();
	}

	/**
	 * 获得图片高度
	 *
	 * @param res
	 *            资源对象
	 * @param resId
	 *            资源id
	 * @return 包含2个元素的数组，下标0是宽度，下标1是高度
	 */
	public static int[] getBitmapDimention(Resources res, int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		int[] dimen = { options.outWidth, options.outHeight };
		return dimen;
	}

	/**
	 * 将图片转换为base64字符串
	 *
	 * @param bm
	 *            图片对象
	 * @return base64字符串
	 */
	public static String getBase64Str(Bitmap bm) {
		byte[] data = bitmap2Bytes(bm);
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	/**
	 * 将图片设置为圆角
	 *
	 * @param bitmap
	 *            图片对象
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap) {
		return toRoundCorner(bitmap, 8);
	}

	/**
	 * 将图片设置为圆角
	 *
	 * @param bitmap
	 *            图片对象
	 * @param r
	 *            描边半径
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int r) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// 转换为圆形图片
		int bWidth = bitmap.getWidth();
		Bitmap buffer = Bitmap.createBitmap(bWidth, bWidth, Config.ARGB_4444);
		Canvas canvas = new Canvas(buffer);
		canvas.drawCircle(bWidth / 2, bWidth / 2, bWidth / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		// 绘制新图片
		paint = new Paint();
		paint.setAntiAlias(true);
		int width = bWidth + 2 * r;
		Bitmap output = Bitmap.createBitmap(width, width, Config.ARGB_4444);
		canvas = new Canvas(output);
		int color = 0xffffffff;
		paint.setColor(color);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		canvas.drawBitmap(buffer, r, r, paint);
		return output;
	}


	public static Bitmap toRoundRectCorner(Bitmap bitmap) {
		return toRoundRectCorner(bitmap,15);
	}


	/**
	 * 获取圆角位图的方法
	 * @param bitmap 需要转化成圆角的位图
	 * @param pixels 圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundRectCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}



	/**
	 * 解析相册图片为Bitmap图片
	 * @param resolver resolver对象
	 * @param data 数据对象
	 * @return 解析后的图片对象
	 */
	public static Bitmap getBitmapFromUri(ContentResolver resolver, Intent data) {
		Bitmap bitmap = null;
		try {
			byte[] mContent=readStream(resolver.openInputStream(Uri.parse(data.getData().toString())));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;//缩放图片为原来的1/4
			bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
}