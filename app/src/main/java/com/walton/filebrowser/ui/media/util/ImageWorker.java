package com.walton.filebrowser.ui.media.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public class ImageWorker {
	private static final String TAG = "ImageWorker";

	Bitmap mLoadingBitmap;

	protected Resources mResources;
	private Context mContext = null;
	private ExecutorService mExecutorService = null;
	private boolean FLAG = true;

	public enum FILETYPE {
		IMAGE, VIDEO, APK
	}

	private void Log(String msg) {
		if (FLAG) {
			Log.d(TAG, msg);
		}
	}

	public ImageWorker(Context context) {
		mContext = context;
		mResources = mContext.getResources();
		mExecutorService = (ExecutorService) Executors.newFixedThreadPool(8);
	}

	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param data
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(Object data, FILETYPE type, ImageView imageView) {
		if (data == null) {
			return;
		}
        Log("try to load image:" + data);
		if (cancelPotentialWork(data, imageView)) {
            Log("try to new worker task");
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView, type);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources,
					mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.executeOnExecutor(mExecutorService, data, type);
		}
        else {
            Log("cancelPotentialWork");
        }
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorker#loadImage(Object, ImageView)}
	 * @return The processed bitmap
	 */
	private Bitmap processBitmap(Object data) {
		String path = (String) data;
        
        Log("process bitmap,path=" + path);
        
		File file = new File(path);
		Bitmap bm = null;
        
		bm = decodeFile(file);
		return bm;
	}

	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			// The new size we want to scale to
			// final int REQUIRED_HEIGHT = 300;
			// final int REQUIRED_WIDTH = 200;
			final int REQUIRED_HEIGHT = 260;
			final int REQUIRED_WIDTH = 170;
			// Find the correct scale value. It should be the power of 2.
			// int width_tmp = o.outWidth, height_tmp = o.outHeight;
			if (o.outWidth <= 0 || o.outHeight <= 0) {
				if (fis != null) {
					fis.close();
				}
				return null;
			}
			o.inSampleSize = getInSampleSize(o, REQUIRED_WIDTH, REQUIRED_HEIGHT);
			// o.inSampleSize = calculateInSampleSize(o, REQUIRED_WIDTH,
			// REQUIRED_HEIGHT);
			o.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o);
		} catch (Exception e) {
		    try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                
                Log.e(TAG,"decode image:" + f + ",fail,Stack:" + sw.toString());
            } catch (Exception e2) {
                Log("bad getErrorInfoFromException");
            }		    
		}
		return null;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	private int getInSampleSize(BitmapFactory.Options options, int reqWidth,
                                int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;

		int wInSampleSize = 1;
		if (width > reqWidth) {
			wInSampleSize = Math.round((float) width / (float) reqWidth);
		}

		int hInSampleSize = 1;
		if (height > reqHeight) {
			hInSampleSize = Math.round((float) height / (float) reqHeight);
		}
		return roundToPower2((wInSampleSize > hInSampleSize) ? wInSampleSize
				: hInSampleSize); //
	}

	private int roundToPower2(int value) {
		int power = 1;
		while (power * 2 <= value) {
			power *= 2;
		}
		return power;
	}

	private Bitmap processVideo(Object data) {
		String path = (String) data;
		File file = new File(path);
		Bitmap bm = null;
		bm = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),
				MediaStore.Images.Thumbnails.MINI_KIND);
		return bm;
	}

	public Bitmap getApkLogo(Object data) {
		String apkPath = (String) data;
		PackageManager pm = mContext.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				BitmapDrawable draw = (BitmapDrawable) appInfo.loadIcon(pm);
				return draw.getBitmap();
			} catch (Exception e) {
				Log.e("ApkIconLoader", e.toString());
				return null;
			}
		}
		return null;
	}

	/**
	 * Cancels any pending work attached to the provided ImageView.
	 * 
	 * @param imageView
	 */
	public static void cancelWork(ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel(true);
			final Object bitmapData = bitmapWorkerTask.data;
			Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
		}
	}

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
				//Log.d(TAG, "cancelPotentialWork - cancelled work for " + data);
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with
	 *         this imageView. null if there is no such task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView, FILETYPE type) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		/**
		 * Background processing.
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			Log("doInBackground - starting work,params[0]=" + params[0] + ",param[1](FILETYPE)=" + params[1]);

			data = params[0];
			Bitmap bitmap = null;

			// If the bitmap was not found in the cache and this task has not
			// been cancelled by
			// another thread and the ImageView that was originally bound to
			// this task is still
			// bound back to this task and our "exit early" flag is not set,
			// then call the main
			// process method (as implemented by a subclass)
			if (bitmap == null && !isCancelled()
					&& getAttachedImageView() != null) {
				FILETYPE type = (FILETYPE) params[1];
				switch (type) {
				case IMAGE:
					bitmap = processBitmap(params[0]);
					break;
				case VIDEO:
					bitmap = processVideo(params[0]);
					break;
				case APK:
					bitmap = getApkLogo(params[0]);
					break;
				}
			}

			Log( "doInBackground - finished work");

			return bitmap;
		}

		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			final ImageView imageView = getAttachedImageView();
			if (imageView != null) {
				if (bitmap != null) {
					Log("onPostExecute - setting bitmap");
					setImageBitmap(imageView, bitmap);
				}
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * A custom_text Drawable that will be attached to the imageView while the work
	 * is in progress. Contains a reference to the actual worker task, so that
	 * it can be stopped if a new binding is required, and makes sure that only
	 * the last started worker process can bind its result, independently of the
	 * finish order.
	 */
	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * Called when the processing is complete and the final bitmap should be set
	 * on the ImageView.
	 * 
	 * @param imageView
	 * @param bitmap
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}
}
