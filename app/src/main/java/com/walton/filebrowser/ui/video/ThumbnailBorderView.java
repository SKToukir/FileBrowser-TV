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
package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.walton.filebrowser.util.Tools;

/**
 * Created by nate.luo on 13-12-12.
 */

public class ThumbnailBorderView extends TextView {
    private static final String TAG = "ThumbnailBorderView";
    private SharedPreferences mSharedPreferences = null;
    private Paint paint = null;
    private float mStartX;
    private float mStartY;
    private float mStopX;
    private float mStopY;
    private int mFocusIndex = -1;
    private String []mThumbnailTimeStamp = new String[5];

    private int color;

    public ThumbnailBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSharedPreferences = context.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
    }

    // Set border color
    public void setPaintColor(int color) {
        this.color = color;
    }

    // Set border coordinate
    public void setPaintCoordinate(float startX, float startY, float stopX, float stopY) {
        mStartX = startX - 2;
        mStartY = startY - 2;
        mStopX = stopX;
        mStopY = stopY - 2;
        if (mStartX < 0) {
            mStartX = 0;
        }

        if (mStartY < 0) {
            mStartY = 0;
        }
        // Log.i(TAG, "mStartX:" + mStartX + " mStartY:" + mStartY + " mStopX:" + mStopX + " mStopY:" + mStopY);
    }

    public void setFocusIndex(int index) {
        mFocusIndex = index;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawClip(canvas);
    }

    private void drawClip(Canvas canvas) {
        paint = new Paint();
        // setPaintColor
        paint.setColor(color);
        paint.setStrokeWidth(3.0f);
        paint.setTextSize(30f);
        // top
        canvas.drawLine(mStartX, mStartY, mStopX, mStartY, paint);
        // left
        // canvas.drawLine(mStartX, mStartY, mStartX, mStopY, paint);
        // bottom
        canvas.drawLine(mStartX, mStopY, mStopX, mStopY, paint);
        // right
        // canvas.drawLine(mStopX, mStartY, mStopX, mStopY, paint);

        if (Tools.showThumbnailTimeStamp()) {
            int surfaceWidth = mSharedPreferences.getInt("SurfaceWidth", 273);
            for (int i = 0; i < mThumbnailTimeStamp.length; i++) {
                mThumbnailTimeStamp[i] = Tools.formatDuration(mSharedPreferences.getInt("TextureTimeStamp" + i, 0));
                if (mThumbnailTimeStamp[i] != null) {
                    if (paint.getColor() != Color.TRANSPARENT) {
                        if ((mFocusIndex != -1) && (mFocusIndex == i)) {
                            paint.setColor(Color.YELLOW);
                        } else {
                            paint.setColor(Color.RED);
                        }
                    }

                }
                canvas.drawText(mThumbnailTimeStamp[i], (0.5f + i) * surfaceWidth, mStartY-10, paint);
            }
        }
    }

}
