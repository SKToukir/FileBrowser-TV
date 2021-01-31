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
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mstar.android.MDisplay;

/**
 *
 * @author 罗勇 (luoyong@biaoqi.com.cn)
 *
 * @since 1.0
 *
 * @date 2012-4-1 Support 3 d and normal mode display picture control
 */

public class ImageSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = "ImageSurfaceView";

    private Bitmap bitmap;

    public ImageSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        getContext();
    }

    public ImageSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        getHolder().addCallback(this);
    }

    public ImageSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    protected void set3D() {
        Surface xx = getHolder().getSurface();
        int firstpos = xx.toString().indexOf("identity=") + 9;
        int lastpos = xx.toString().lastIndexOf(")");
        String identitystring = xx.toString().substring(firstpos, lastpos);
        try {
            int identityint = Integer.parseInt(identitystring);
            Log.d(TAG, "apk xx.toString()=" + xx.toString() + ";firstpos="
                    + firstpos + ";lastpos=" + lastpos + ";identitystring="
                    + identitystring + ";identityint=" + identityint);

            MDisplay.setAutoStereoMode(identityint, 0);

            SystemProperties.set("mstar.desk-display-mode", "3");
        } catch (NumberFormatException e) {
            Log.i(TAG, "e:" + e);
        }
    }

    protected void setNormal() {
        Surface xx = getHolder().getSurface();
        int firstpos = xx.toString().indexOf("identity=") + 9;
        int lastpos = xx.toString().lastIndexOf(")");
        String identitystring = xx.toString().substring(firstpos, lastpos);
        try {
            int identityint = Integer.parseInt(identitystring);

            //MDisplay.setBypassTransformMode(identityint, 1);
            MDisplay.setAutoStereoMode(identityint, 1);
            SystemProperties.set("mstar.desk-display-mode", "0");
        } catch (NumberFormatException e) {
            Log.i(TAG, "e:" + e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // set3D();

        drawImage();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "********surfaceDestroyed******");
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    // ////////////////////////////////////////////////////////////////////////

    // ////////////////////////////////////////////////////////////////////////

    /**
     * Picture reading and loading start.
     */
    protected void drawImage() {
        Canvas canvas = getHolder().lockCanvas();
        if (bitmap != null && canvas != null) {
            Paint paint = new Paint();
            android.graphics.Rect src = new android.graphics.Rect();
            android.graphics.Rect dst = new android.graphics.Rect();
            src.left = 0;
            src.top = 0;
            src.bottom = bitmap.getHeight();
            src.right = bitmap.getWidth();
            dst.left = 0;
            dst.top = 0;
            dst.bottom = this.getHeight();
            dst.right = this.getWidth();
            canvas.drawBitmap(bitmap, src, dst, paint);
        }

        if (canvas != null) {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected void cleanView(int width, int height) {
        Canvas canvas = getHolder().lockCanvas();
        if (bitmap != null && canvas != null) {
            Paint paint = new Paint();
            android.graphics.Rect src = new android.graphics.Rect();
            paint.setColor(Color.BLACK);
            src.left = 0;
            src.top = 0;
            src.bottom = height;
            src.right = width;
            canvas.drawRect(src, paint);
            canvas.save(1);
        }
        if (canvas != null) {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    protected boolean updateView() {
        if (this.bitmap != null) {
            invalidate();
            return true;
        } else {
            return false;
        }
    }

}
