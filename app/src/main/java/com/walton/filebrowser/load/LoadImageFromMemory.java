package com.walton.filebrowser.load;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by penghuilou on 2016/3/8.
 * 从内存中获取文件
 */
public class LoadImageFromMemory {


    private LruCache<String, Bitmap> memoryCaches;
    private int maxValue = (int) (Runtime.getRuntime() .maxMemory() / 1024)/8;

    public LoadImageFromMemory() {
        memoryCaches = new LruCache<String, Bitmap>(maxValue){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    /**
     * 从内存加载图片
     * @param ivPic
     */
    public Bitmap loadImage(ImageView ivPic){
        String path = (String) ivPic.getTag();
        return memoryCaches.get(path);
    }

    /**
     * 保存图片到内存
     * @param bitmap
     */
    public void saveImage2Memory(String path, Bitmap bitmap){
        memoryCaches.put(path,bitmap);
    }
}
