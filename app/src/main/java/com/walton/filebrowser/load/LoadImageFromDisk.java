package com.walton.filebrowser.load;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by penghuilou on 2016/3/8.
 * 硬盘中读取图片
 */
public class LoadImageFromDisk {
    private LoadImageFromMemory mLoadFromMemory;
    public LoadImageFromDisk(LoadImageFromMemory loadFromMemory) {
        this.mLoadFromMemory = loadFromMemory;
    }
    /**
     * 加载图片
     * @param imageView
     * @return
     */
    public void LoadImage(ImageView imageView){
//        异步加载图片
        new LoadImageTask().execute(imageView,imageView.getTag());
    }

    /**
     *图片异步加载任务
     */
    class LoadImageTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView mIvpic;
        @Override
        protected Bitmap doInBackground(Object... params) {
            mIvpic = (ImageView) params[0];
            String path = (String) params[1];
            return scaleImage(path);
        }

        @Override
        protected void onPostExecute(Bitmap res) {
            if(res!=null){
                mIvpic.setImageBitmap(res);
                //一旦获取图片，保存图片到内存中
                String path = (String) mIvpic.getTag();
                mLoadFromMemory.saveImage2Memory(path,res);
            }
        }

    }

    /**
     * 压缩图片
     * @param url
     * @return
     */
    public Bitmap scaleImage(String url){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(url, options);
        int be = (int) ((options.outHeight > options.outWidth ? options.outHeight / 95
                : options.outWidth / 75));
        if (be <= 0) // 判断200是否超过原始图片高度
            be = 1; // 如果超过，则不进行缩放
        options.inSampleSize = be;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(url, options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        }
    }
}
