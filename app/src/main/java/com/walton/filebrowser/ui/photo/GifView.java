//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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
package com.walton.filebrowser.ui.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.walton.filebrowser.business.photo.GifDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GifView extends View {
    private GifDecoder gDecoder;
    private boolean isStop = false;
    private int delta = 1;

    private Bitmap bmp;
    private InputStream is;

    private android.graphics.Rect src = new android.graphics.Rect();
    private android.graphics.Rect dst = new android.graphics.Rect();

    private Thread updateTimer;


    /**
     *  construct - refer for java
     * @param context
     */
    public GifView(Context context) {
        this(context, null);

    }

    /**
     *  construct - refer for xml
     * @param context
     * @param attrs
     */
    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDelta(1);
    }

    /**
     * stop
     * @param stop
     */
    public void setStop() {
        isStop = true;
    }

    private void frameDelay() {
        try {
            Thread.sleep(getPlayTimeEachFrame());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * start
     */
    public void setStart(GifDecoder.IGifCallBack mGifCallBack) {
        if(updateTimer != null && updateTimer.isAlive()){
            isStop = true;
            frameDelay();
        }
        gDecoder.setOnGifListener(mGifCallBack);
        updateTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                frameDelay();
                while (true) {
                    if(isStop){
                        return;
                    }
                    GifView.this.postInvalidate();
                    frameDelay();
                }
            }
        });
        isStop = false;
        updateTimer.start();
    }

    public int getFrameCount(){
        return gDecoder.getFrameCount();
    }

    /**
     * Through the subscript the zoomed image display
     * @param id
     */
    public boolean setSrc(String path, PhotoPlayerActivity player) {
        if(bmp != null){
            bmp.recycle();
        }
        if(gDecoder == null){
        }else{
            gDecoder.reset();
        }
        gDecoder = new GifDecoder();

        try {
            is = new FileInputStream(path);
            if(gDecoder != null){
                gDecoder.read(is);
                if(gDecoder.err()){
                    bmp = player.decodeBitmap(path);
                }else{
                    bmp = gDecoder.getImage();// first
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.postInvalidate();
        return true;
    }

    public boolean decodeBitmapFromNet(String path, PhotoPlayerActivity player){
        if(bmp != null){
            bmp.recycle();
        }
        if(gDecoder == null){
        }else{
            gDecoder.reset();
            //gDecoder.resetFrame();
        }
        gDecoder = new GifDecoder();
        try {
            is = new URL(path).openStream();
            if(gDecoder != null){
                gDecoder.read(is);
                if(gDecoder.err()){
                    bmp = player.decodeBitmap(path);
                }else{
                    bmp = gDecoder.getImage();// first
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.postInvalidate();
        return true;
    }

    public void setDelta(int time) {
        delta = time;
    }

    public int getPlayTimeEachFrame(){
        if(gDecoder != null){
            return Math.max(100, gDecoder.nextDelay()) / delta;
        }
        return 0;
    }

    public int getPlayTime() {
        int allDelay = 0;
        if(gDecoder != null) {
            int n = gDecoder.getFrameCount();
            for(int i = 0; i < n; i++) {
                allDelay += Math.max(100, gDecoder.getDelay(i));
            }
        }
        Log.i("GifView","getPlayTime: allDelay = " + allDelay);
        return allDelay;
    }

    /*public int getPlayTime() {
        if(gDecoder != null){
            return gDecoder.getFrameCount() * getPlayTimeEachFrame();
        }
        return 0;
    }*/


/*  @Override
    public void layout(int arg0, int arg1, int arg2, int arg3) {
        super.layout(0, 0, 1920, 1080);
    }
*/

    protected void onDraw(Canvas canvas) {
        if (bmp != null) {
            Paint paint = new Paint();
            src.left = 0;
            src.top = 0;
            src.bottom = bmp.getHeight();
            src.right = bmp.getWidth();
            dst.left = 0;
            dst.top = 0;
            dst.bottom = this.getHeight();
            dst.right = this.getWidth();
            //Log.i("***************", "****src****" + src.top + " " + src.bottom + " "
            //      + src.left + " " + src.right);
            if(!bmp.isRecycled()){
                center();
                canvas.drawBitmap(bmp, src, dst, paint);
                bmp = gDecoder.nextBitmap();
            }
        }
    }

    protected void center() {
        bmp = resizeDownIfTooBig(bmp, true);
        float height = bmp.getHeight();
        float width = bmp.getWidth();
        float deltaX = 0, deltaY = 0;
        int viewHeight = getHeight();
        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2 - src.top;
        }  else if (src.top > 0) {
            deltaY = -src.top;
        } else if (src.bottom < viewHeight) {
            deltaY = getHeight() - src.bottom;
        }

        int viewWidth = getWidth();
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - src.left;
        } else if (src.left > 0) {
            deltaX = -src.left;
        } else if (src.right < viewWidth) {
            deltaX = viewWidth - src.right;
        }

        dst.top = src.top + (int)deltaY;
        dst.left = src.left + (int)deltaX;
        dst.bottom = bmp.getHeight() + (int)deltaY;
        dst.right = bmp.getWidth() + (int)deltaX;
/*      Log.i("***************", "****src****" + height + " " + width + " " +
                viewHeight + " " + viewWidth);
        Log.i("***************", "****src****" + src.top + " " + src.bottom + " "
                + src.left + " " + src.right);*/
    }

    // Resize the bitmap if each side is >= targetSize * 2
        private Bitmap resizeDownIfTooBig(Bitmap bitmap,
                                          boolean recycle) {
            int srcWidth = bitmap.getWidth();
            int srcHeight = bitmap.getHeight();
            float scale = Math.min((float) getWidth() / srcWidth,
                    (float) getHeight() / srcHeight);
            //Log.d("************", "srcWidth : " + srcWidth + " srcHeight : " + srcHeight
            //      + " scale : " + scale);
            if (scale > 1.0f) {
                return bitmap;
            }
            return resizeBitmapByScale(bitmap, scale, recycle);
        }

        private Bitmap resizeBitmapByScale(Bitmap bitmap, float scale,
                                           boolean recycle) {
            int width = Math.round(bitmap.getWidth() * scale);
            int height = Math.round(bitmap.getHeight() * scale);
            if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
                return bitmap;
            }
            Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
            Canvas canvas = new Canvas(target);
            canvas.scale(scale, scale);
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            if (recycle) {
                bitmap.recycle();
            }
            return target;
        }

        private Bitmap.Config getConfig(Bitmap bitmap) {
            Bitmap.Config config = bitmap.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            return config;
        }
}
