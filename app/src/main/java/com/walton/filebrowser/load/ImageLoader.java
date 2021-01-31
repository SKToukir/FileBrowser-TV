package com.walton.filebrowser.load;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by penghuilou on 2016/3/8.
 * 图片加载工具类
 */
public class ImageLoader {
    private LoadImageFromDisk mLoadFromDisk;
    private LoadImageFromMemory mLoadFromMemory;


    public ImageLoader() {
        mLoadFromMemory = new LoadImageFromMemory();
        mLoadFromDisk = new LoadImageFromDisk(mLoadFromMemory);
    }

    public void loadImage(ImageView ivPic){
        String path = (String)ivPic.getTag();
        // 1、先从内存中读取图片，如果可以读取则结束加载图片
        Bitmap bitmap = mLoadFromMemory.loadImage(ivPic);
        if(bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            return;
        }
        //内存没有加载到，则从磁盘读取
        mLoadFromDisk.LoadImage(ivPic);

    }
}
