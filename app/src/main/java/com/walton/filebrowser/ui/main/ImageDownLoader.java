//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>
package com.walton.filebrowser.ui.main;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap.Config;

import android.graphics.drawable.BitmapDrawable;
import android.content.ContentResolver;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.media.ThumbnailUtils;

import com.walton.filebrowser.util.Constants;

// by andrew.wang


public class ImageDownLoader {
    private final static String TAG = "ImageDownLoader";
    private LruCache<String, Drawable> mMemoryCache;
    private ThumbnailThreadPool mVideoThumbnailThreadPool = null;
    private ThumbnailThreadPool mImageThumbnailThreadPool = null;
    private ContentResolver cr =null;
    private MediaThumbnail mtb;


    public ImageDownLoader(Context context){
        cr = context.getContentResolver();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 16;
        mMemoryCache = new LruCache<String,Drawable>(mCacheSize){
           @Override
           protected int sizeOf(String key,Drawable drawable){
              BitmapDrawable bd = (BitmapDrawable)drawable;
              Bitmap bitmap = bd.getBitmap();
              return bitmap.getByteCount();
           }
        };
    }
    public ThumbnailThreadPool getThreadPool(int fileType) {
        if (fileType == Constants.GRID_POSITION_IS_VIDEO || fileType == Constants.GRID_POSITION_IS_MUSIC) {
            if (mVideoThumbnailThreadPool == null){
                synchronized(ThumbnailThreadPool.class){
                    if (mVideoThumbnailThreadPool == null) {
                        mVideoThumbnailThreadPool = new ThumbnailThreadPool(10,
                                new ArrayBlockingQueue<Runnable>(16));
                    }
                }
            }
            return mVideoThumbnailThreadPool;
        }
        if (mImageThumbnailThreadPool == null){
            synchronized(ThumbnailThreadPool.class){
                if (mImageThumbnailThreadPool == null) {
                    mImageThumbnailThreadPool = new ThumbnailThreadPool(10, 2,
                            new ArrayBlockingQueue<Runnable>(16));
                }
            }
        }
        return mImageThumbnailThreadPool;
    }

    public void addDrawableToMemoryCache(String key, Drawable drawable) {
        if (getDrawableFromMemCache(key) == null && drawable != null) {
            mMemoryCache.put(key, drawable);
        }
    }
    public Drawable getDrawableFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    public void downloadImage(final int position,final String url,
                      final int fileType,final onImageLoaderListener listener){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listener.onImageLoader((Drawable)msg.obj, position, url);
            }
        };
        getThreadPool(fileType).execute(new Runnable() {
             @Override
             public void run() {
                 Log.i(TAG,"run fileType:"+String.valueOf(fileType));
                 Drawable drawable = getDrawableFormUrl(url,fileType);
                 Message msg = handler.obtainMessage();
                 msg.obj = drawable;
                 handler.sendMessage(msg);
                 addDrawableToMemoryCache(url, drawable);
             }
        });
    }
    public Drawable showCacheBitmap(String url){
        if(getDrawableFromMemCache(url) != null)
            return getDrawableFromMemCache(url);
        return null;
    }
    private Drawable getDrawableFormUrl(String url,int fileType) {
        Log.i(TAG,"getDrawableFormUrl:"+url);
        Bitmap bitmap = null;
        Bitmap bitmapResult = null;
        mtb= new MediaThumbnail();
        File file = new File(url);
        if (Constants.GRID_POSITION_IS_VIDEO == fileType) {
            bitmap = mtb.createVideoThumbnail(cr,file.getAbsolutePath());
        } else if (Constants.GRID_POSITION_IS_MUSIC == fileType) {
            bitmap = mtb.createAlbumThumbnail(file.getAbsolutePath());
        } else if (Constants.GRID_POSITION_IS_PICTURE == fileType) {
            //bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            bitmap = decodeSampledBitmapFromFilePath(file.getAbsolutePath(), 380, 380) ;
        }
        bitmapResult=ThumbnailUtils.extractThumbnail(bitmap, 380, 380);
        if (bitmap != bitmapResult) {
            bitmap.recycle();
        }
        if (bitmapResult == null) return null;
        Drawable drawable=new BitmapDrawable(bitmapResult);
        return drawable;
    }
    private static int calculateRatioSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSampleSize)>reqHeight && (halfWidth/inSampleSize)>reqWidth) {
                   inSampleSize *= 2;
            }

        }
        return inSampleSize;
    }
    private static Bitmap decodeSampledBitmapFromFilePath(String filePath,
            int reqWidth, int reqHeight) {
        FileInputStream fis =null;
        try {
            fis= new FileInputStream(filePath);
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        }
        FileDescriptor fd =null;
        if (fis == null) return null;
        try {
            fd= fis.getFD();
        } catch(IOException e){
            e.printStackTrace();
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bMap = null;
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd,null,options);
            options.inSampleSize = calculateRatioSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bMap = BitmapFactory.decodeFileDescriptor(fd,null,options);
        } catch (OutOfMemoryError e) {
            Log.i(TAG,"OutOfMemoryError");
            options.inSampleSize+=1;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            bMap = BitmapFactory.decodeFileDescriptor(fd,null,options);
        }
        return bMap;
        //return createScaleBitmap(bMap, reqWidth, reqHeight);
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) {
            src.recycle();
        }
        return dst;
    }

    public synchronized void cancelTask(boolean isShutDown) {
        if(mVideoThumbnailThreadPool != null) {
            mVideoThumbnailThreadPool.removeAllTask();
            if (isShutDown) {
                mVideoThumbnailThreadPool.shutdownNow();
                mVideoThumbnailThreadPool = null;
                mtb.release();
            }
        }

        if (mImageThumbnailThreadPool != null) {
            mImageThumbnailThreadPool.removeAllTask();
            if (isShutDown) {
                mImageThumbnailThreadPool.shutdownNow();
                mImageThumbnailThreadPool = null;
            }
        }
        System.gc();
    }
    public interface onImageLoaderListener{
        void onImageLoader(Drawable drawable, int position, String url);
    }

    private class ThumbnailThreadPool extends ThreadPoolExecutor {
        int maximumTaskSize;
        public ThumbnailThreadPool(int maximumTaskSize,
                                   BlockingQueue<Runnable> workQueue) {
            super(1, 1, 5, TimeUnit.SECONDS, workQueue);
            this.maximumTaskSize = maximumTaskSize;
        }

        public ThumbnailThreadPool(int maximumTaskSize, int poolSize,
                                   BlockingQueue<Runnable> workQueue) {
            super(1, poolSize, 5, TimeUnit.SECONDS, workQueue);
            this.maximumTaskSize = maximumTaskSize;
        }

        @Override
        public void execute(Runnable command) {
            Log.i(TAG, "execute: Queue().size() = " + getQueue().size());
            if (getQueue().size() > maximumTaskSize) {
                try {
                    getQueue().take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            super.execute(command);
        }

        public void removeAllTask() {
            Log.i(TAG, "removeAllTask: ");
            BlockingQueue<Runnable> queue = getQueue();
            if (queue != null) {
                queue.clear();
            }
        }
    }
}
