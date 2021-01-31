//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package com.walton.filebrowser.ui.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Tools;

public class GoldenLeftEyeActivity extends Activity {
    private static final String TAG = "GoldenLeftEyeActivity";
    LinearLayout linear;
    WindowManager wm;
    Drawl line1;
    Drawl line2;
    LayoutParams params1;
    LayoutParams params2;
    private TvS3DManager tvS3DManager = null;
    public xPositionChangeReceiver xpositionreceiver;
    private static int totalWidth = 0;
    private static int totalHeight = 0;
    private static int devidedWidth = 0;
    private static final int DRAW_LINE = 122;
    private static final int START_OFFSET = 0;
    private static final int MOVING_OFFSET = 0;
    private int mViewId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        linear = new LinearLayout(getBaseContext());
        linear.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linear.setLayoutParams(params);
        setContentView(linear);
        mViewId = getIntent().getIntExtra("viewId", 0);
        tvS3DManager = TvS3DManager.getInstance();

        xpositionreceiver = new xPositionChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mstar.tv.service.GOLDEN_LEFT_EYE");
        filter.addAction("com.walton.filebrowser.ui.common.GoldenLeftEyeActivity.exit");
        this.registerReceiver(xpositionreceiver, filter);

        params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mHandler.sendEmptyMessageDelayed(DRAW_LINE,500);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DRAW_LINE:
                    totalWidth = linear.getWidth();
                    totalHeight = linear.getHeight();
                    devidedWidth = totalWidth/2;
                    Log.i(TAG, "#####-----totalWidth is:"+totalWidth+"--------");
                    Log.i(TAG, "#####-----totalHeight is:"+totalHeight+"--------");
                    line1 = new Drawl(GoldenLeftEyeActivity.this,(0+START_OFFSET));
                    line2 = new Drawl(GoldenLeftEyeActivity.this,devidedWidth);
                    addContentView(line1, params1);
                    addContentView(line2, params2);
                    if (tvS3DManager.get3dDisplayFormat() != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                        tvS3DManager.set3dDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                    }
                    TvPictureManager.getInstance().setVideoArcType(TvPictureManager.VIDEO_ARC_16x9);
                    TvPictureManager.getInstance().setMWEDemoMode(TvPictureManager.MWE_DEMO_MODE_CUSTOMER1);
                    break;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown finish golden left eye !");
        if (keyCode== KeyEvent.KEYCODE_BACK) {
            Tools.setPlaySettingOpt(6, this.getString(R.string.play_setting_0_value_2), mViewId);
            this.finish();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
    protected void onPause() {
        super.onPause();
        if (tvS3DManager.get3dDisplayFormat() != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
            tvS3DManager.set3dDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
        }
        TvPictureManager.getInstance().setMWEDemoMode(TvPictureManager.MWE_DEMO_MODE_OFF);
        if (line1!=null) {
            line1.setVisibility(View.GONE);
        }
        if (line2!=null) {
            line2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(xpositionreceiver);
        super.onDestroy();
    }

    private class xPositionChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int xRatio = 0;
            if (action.equals("com.mstar.tv.service.GOLDEN_LEFT_EYE")) {
                xRatio = intent.getIntExtra("position_x",-1);
                if ((xRatio >= 0) && (xRatio <= (500-MOVING_OFFSET))) {
                    if (line1 != null) {
                        line1.setVisibility(View.GONE);
                    }
//                    line1 = new Drawl(context,(xRatio+10)*totalWidth/1000);
                    line1 = new Drawl(context,(xRatio+MOVING_OFFSET)*totalWidth/1000);
                    addContentView(line1, params1);
                    line1.setVisibility(View.VISIBLE);
                    return;
                }
                else if (xRatio == 500) {
                    line1.setVisibility(View.GONE);
                    return;
                }
                else if ((xRatio > 500) && (xRatio < (1000-MOVING_OFFSET))) {
                    line2.setVisibility(View.GONE);
                    line2 = new Drawl(context,(xRatio+MOVING_OFFSET)*totalWidth/1000);
                    addContentView(line2, params1);
                    line2.setVisibility(View.VISIBLE);
                    return;
                }
                else if (xRatio >= 1000) {
                    line2.setVisibility(View.GONE);
                    TvPictureManager.getInstance().setMWEDemoMode(TvPictureManager.MWE_DEMO_MODE_OFF);
                    return;
                }
            }else if("com.walton.filebrowser.ui.common.GoldenLeftEyeActivity.exit".equals(action)){
                Tools.setPlaySettingOpt(6, GoldenLeftEyeActivity.this.getString(R.string.play_setting_0_value_2), mViewId);
                GoldenLeftEyeActivity.this.finish();
            }
        }
    }

    public class Drawl extends View {
        private Paint paint;
        private Canvas canvas;
        private float positon_x;
        public Drawl(Context context, float x) {
            super(context);
            positon_x = x;
            paint=new Paint(Paint.DITHER_FLAG);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
       }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawLine(positon_x,0,positon_x,1080,paint);
        }
    }
}
