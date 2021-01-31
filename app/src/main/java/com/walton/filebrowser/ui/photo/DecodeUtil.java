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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


import com.walton.filebrowser.util.Constants;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utils for decode bitmap.
 *
 * @author felix.hu
 */
public class DecodeUtil {

    private static final String TAG = "DecodeUtil";

    // the largest pix of photo can be decode successful
    private static final long UPPER_BOUND_PIX = 1920 * 8 * 1080 * 8;

    private static final double UPPER_BOUND_WIDTH_PIX = 1920.0f;

    private static final double UPPER_BOUND_HEIGHT_PIX = 1080.0f;

    private final Context mContext;

    /**
     * Constructor.
     *
     * @param context
     *            {@link Context}.
     */
    public DecodeUtil(final Context context) {
        this.mContext = context;
    }

    /**
     * Create a bitmap from resource.
     *
     * @param id
     *            the id of resource.
     * @return {@link Bitmap} create from resouce.
     */
    public final Bitmap createBitmapFromResource(final int id) {
        // Obtain resources pictures
        InputStream inputStream = mContext.getResources().openRawResource(id);
        if (inputStream == null) {
            return BitmapFactory.decodeResource(mContext.getResources(), id);
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opt);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * Decode bitmap with a specified {@link URL} of image.
     *
     * @param url
     *            the url of bitmap.
     * @return {@link Bitmap} of the specified url.
     */
    public final Bitmap decodeBitmap(final URL url) {
        InputStream inputStream = null;
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpUrlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            closeStream(inputStream);
        }

        // options for decode bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        // resampling rate is 1/4
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        // options.forceNoHWDoecode = false;

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeStream(inputStream, null, options);

        // disconnect network and close stream
        httpUrlConnection.disconnect();
        closeStream(inputStream);

        return bitmap;
    }

    /**
     * Decode bitmap with the specified image path.
     *
     * @param path
     *            the absolute path of image.
     * @param windowWidth
     *            the width of window.
     * @return {@link Bitmap} of the specified path.
     */
    public final Bitmap decodeBitmap(final String path, final int windowWidth) {
        Bitmap bitmap = null;

        FileInputStream fileInputStream = null;
        FileDescriptor fileDescriptor = null;
        try {
            fileInputStream = new FileInputStream(path);
            fileDescriptor = fileInputStream.getFD();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "get FileDescriptor throws Exception");

            closeStream(fileInputStream);
            try {
                fileInputStream = new FileInputStream(path);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                Log.e(TAG, "new FileInputStream throws Exception");
            } finally {
                closeStream(fileInputStream);
            }
            bitmap = BitmapFactory.decodeStream(fileInputStream);

            return bitmap;
        } finally {
            closeStream(fileInputStream);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        // Plug disk, the following must be set to false
        options.inPurgeable = false;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        // According to the 1920 * 1080 high-definition format picture as the
        // restriction condition
        options.inSampleSize = computeSampleSizeLarger(options.outWidth,
                options.outHeight);
        Log.d(TAG, "options.inSampleSize : " + options.inSampleSize);

        bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
                options);
        bitmap = resizeDownIfTooBig(bitmap, windowWidth, true);

        return bitmap;
    }

    /**
     * Compute the width and height of bitmap that from network.
     *
     * @param url
     *            {@link URL}.
     * @return the width and height of bitmap as a {@link Rect}.
     */
    public final Rect getBitmapBounds(final URL url) {
        Rect bounds = new Rect();

        InputStream inputStream = null;
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpUrlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,
                    "URL.openConnection() or HttpURLConnection.getInputStream throws Exception");

            return bounds;
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            closeStream(inputStream);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;

        String path = url.getPath();
        Log.d(TAG, "path : " + path);
        if (!path.substring(path.lastIndexOf(".") + 1).equalsIgnoreCase(
                Constants.MPO)) {
            // options.forceNoHWDoecode = true;
        }

        BitmapFactory.decodeStream(inputStream, null, options);

        bounds.right = options.outWidth;
        bounds.bottom = options.outHeight;

        // disconnect network and close stream
        httpUrlConnection.disconnect();
        closeStream(inputStream);

        return bounds;
    }

    /**
     * Compute the width and height of bitmap that from USB disk.
     *
     * @param path
     *            the absolute path of image.
     * @return the width and height of bitmap as a {@link Rect}.
     */
    public final Rect getBitmapBounds(final String path) {
        Rect bounds = new Rect();

        FileInputStream fileInputStream = null;
        FileDescriptor fileDescriptor = null;
        try {
            fileInputStream = new FileInputStream(path);
            fileDescriptor = fileInputStream.getFD();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "new FileInputStream throws FileNotFoundException");

            return bounds;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "FileInputStream.getFD() throws IOException");

            return bounds;
        } finally {
            closeStream(fileInputStream);
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        // Plug disk, the following must be set to false.
        options.inPurgeable = false;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        if (!path.substring(path.lastIndexOf(".") + 1).equalsIgnoreCase(
                Constants.MPO)) {
            // options.forceNoHWDoecode = true;
        }
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        bounds.right = options.outWidth;
        bounds.bottom = options.outHeight;

        // close stream
        closeStream(fileInputStream);
        fileDescriptor = null;

        return bounds;
    }

    /**
     * Check the resolution of width multiply by height.
     *
     * @param width
     *            the width of bitmap.
     * @param height
     *            the height of bitmap.
     * @return true if the resolution is larger than 1920 * 8 * 1080 * 8,
     *         otherwise false.
     */
    public final static boolean isLargerThanLimit(final int width,
            final int height) {
        long pixSize = width * height;
        // largest pix is 1920 * 8 * 1080 * 8
        if (pixSize <= UPPER_BOUND_PIX) {
            return false;
        }

        return true;
    }

    /**
     * Close the stream.
     *
     * @param stream
     *            the stream to be closed.
     */
    private void closeStream(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Compute the sampling rate of the bitmap.
     *
     * @param w
     *            the width of bitmap.
     * @param h
     *            the height of bitmap.
     * @return the sampling rate(1, 2, 4 or 8).
     */
    private int computeSampleSizeLarger(final double w, final double h) {
        double initialSize = Math.max(w / UPPER_BOUND_WIDTH_PIX, h
                / UPPER_BOUND_HEIGHT_PIX);
        if (initialSize <= 2.0f) {
            return 1;
        } else if (initialSize < 4.0f) {
            return 2;
        } else if (initialSize < 8.0f) {
            return 4;
        } else {
            return 8;
        }
    }

    /**
     * Scale the bitmap if it's too bigger than window.
     *
     * @param bitmap
     *            the bitmap to be resize.
     * @param windowWidth
     *            the width of window.
     * @param recycle
     *            the flag for recycle bitmap.
     * @return the scaled bitmap of original one.
     */
    private Bitmap resizeDownIfTooBig(final Bitmap bitmap,
                                      final int windowWidth, final boolean recycle) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float scale = Math.max((float) windowWidth / srcWidth,
                (float) windowWidth / srcHeight);
        Log.d(TAG, "srcWidth : " + srcWidth + " srcHeight : " + srcHeight
                + " scale : " + scale);
        if (scale > 0.5f) {
            return bitmap;
        }

        return resizeBitmapByScale(bitmap, scale, recycle);
    }

    /**
     * Scale the bitmap.
     *
     * @param bitmap
     *            the bitmap to be resize.
     * @param scale
     *            the scale rate.
     * @param recycle
     *            the flag for recycle bitmap.
     * @return the scaled bitmap of original one.
     */
    private Bitmap resizeBitmapByScale(final Bitmap bitmap, final float scale,
                                       final boolean recycle) {
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

    /**
     * Get the {@link Bitmap.Config} of bitmap or Bitmap.Config.ARGB_8888.
     *
     * @param bitmap
     *            {@link Bitmap}.
     * @return {@link Bitmap.Config}.
     */
    private Bitmap.Config getConfig(final Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        return config;
    }

}
