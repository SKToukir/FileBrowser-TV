//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.util.Log;


/**
 *
 * Create thumabnail from video or music.
 *
 * @date 2015-07-23
 *
 * @version 1.0.0 *
 *
 * @author andrew.wang
 *
 */

public class MediaThumbnail {
    final static String TAG = "MediaThumbnail";
    MediaMetadataRetriever mRetriever;

    private Bitmap getVideoThumabnailByThumbnailUtils(String filePath){
           Log.i(TAG,"getVideoThumabnailByThumbnailUtils");
           Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
           return bitmap;
    }
    private synchronized Bitmap getVideoThumbnailMediaStore(ContentResolver cr, String path) {
        Log.i(TAG,"getVideoThumbnailMediaStore");
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //select condition.
        String whereClause = Video.Media.DATA + " = '"
        + path + "'";
        //colection of results.
        Cursor cursor = cr.query(Video.Media.EXTERNAL_CONTENT_URI,
                new String[] { Video.Media._ID }, whereClause,
                null, null);
        Log.i(TAG, "cursor = " + cursor);
        if (cursor == null || cursor.getCount() == 0) {
            return ThumbnailUtils.createVideoThumbnail(path, Video.Thumbnails.MINI_KIND);
        }
        cursor.moveToFirst();
        //image id in image table.
        String videoId = cursor.getString(cursor
                .getColumnIndex(Video.Media._ID));
        Log.i(TAG, "videoId = " + videoId);
        if (videoId == null) {
            return null;
        }
        cursor.close();
        long videoIdLong = Long.parseLong(videoId);
        //via imageid get the bimap type thumbnail in thumbnail table.
        bitmap = Video.Thumbnails.getThumbnail(cr, videoIdLong,
                            Thumbnails.MICRO_KIND, options);
        return bitmap;
    }
    private Bitmap getVideoThumbnailByRetriever(String filePath){
        Log.i(TAG,"getVideoThumbnailByRetriever");
        Bitmap bitmap = null;
        mRetriever = new MediaMetadataRetriever();
        try {
            mRetriever.setDataSource(filePath);
            try {
                bitmap= mRetriever.getFrameAtTime();
                }
            catch (Exception e) {
                Log.i(TAG,"failed to capture a video frame");
            }
        } catch (IllegalArgumentException ex) {
            Log.i(TAG,"IllegalArgumentException:failed to capture a video frame ");
        } catch (RuntimeException ex) {
            Log.i(TAG,"RuntimeException:failed to capture a video frame ");
        } finally {
            try {
                mRetriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bitmap;
    }
    public Bitmap createVideoThumbnail(ContentResolver cr, String filePath) {
        Log.i(TAG,"createVideoThumbnail");
        //Bitmap bitmap = getVideoThumabnailByThumbnailUtils(filePath);
        //Bitmap bitmap = getVideoThumbnailMediaStore(cr,filePath);
        Bitmap bitmap = getVideoThumbnailByRetriever(filePath);
        return bitmap;
    }
    public Bitmap createAlbumThumbnail(String filePath) {
        Log.i(TAG,"createAlbumThumbnail");
        Bitmap bitmap = null;
        mRetriever = new MediaMetadataRetriever();
        try {
            mRetriever.setDataSource(filePath);
            byte[] art = mRetriever.getEmbeddedPicture();
            Log.i(TAG, "art:" + art);
            if (art != null) {
                Log.i(TAG, "art.length:" + art.length);
            } else {
                Log.e(TAG,"AlbumThumbnail is NULL");
            }
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, "IllegalArgumentException");
        } catch (RuntimeException ex) {
            Log.i(TAG, "RuntimeException");
        } finally {
            try {
                mRetriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bitmap;
    }

    public void release() {
        if (mRetriever != null) {
            mRetriever.release();
            mRetriever = null;
        }
    }

}


