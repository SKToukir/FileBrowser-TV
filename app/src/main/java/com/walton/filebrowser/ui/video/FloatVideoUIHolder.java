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
package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Tools;

/**
 * Created by nate.luo on 15-4-14.
 */

public class FloatVideoUIHolder {

    private static final String TAG = "FloatVideoUIHolder";
    public static final String ACTION_START_FLOAT_VIDEO_SERVICE = "com.mstar.android.intent.action.START_FLOAT_VIDEO_SERVICE";
    private static final String MEDIA_PLAYER_PLAY = "MEDIA_PLAYER_PLAY";
    private static final String MEDIA_PLAYER_PREVIOUS = "MEDIA_PLAYER_PREVIOUS";
    private static final String MEDIA_PLAYER_NEXT = "MEDIA_PLAYER_NEXT";
    private boolean mNeedDebug = true;
    protected SurfaceView mFloatVideoPlayView;
    // pre a video
    protected ImageView bt_videoPre;
    // play/pause
    protected ImageView bt_videoPlay;
    // next video
    protected ImageView bt_videoNext;
    // video play list
    protected ImageView bt_videoList;
    // video info
    protected ImageView bt_videoFullScreen;
    protected ImageView bt_videoInfo;
    private ImageView bt_video_icon;
    private int statusBarHeight;// 状态栏高度
    private View view1;// 透明窗体
    private View view2;
    private boolean viewAdded = false;// 透明窗体是否已经显示
    private LayoutInflater mLayoutInflater;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private Context mContext = null;

    public FloatVideoUIHolder(Context context) {
        mContext = context;
        initView();
        initButtonListener();
    }

    public void initView() {
        Log.i(TAG, "initView");
        mLayoutInflater = LayoutInflater.from(mContext);

        view2 = mLayoutInflater.inflate(R.layout.float_video_player_frame, null);
        mFloatVideoPlayView = (SurfaceView)view2.findViewById(R.id.floatVideoPlayView);
        bt_videoPre = (ImageView) view2.findViewById(R.id.video_previous);
        bt_videoPlay = (ImageView) view2.findViewById(R.id.video_play);
        bt_videoNext = (ImageView) view2.findViewById(R.id.video_next);
        bt_videoList = (ImageView) view2.findViewById(R.id.video_list);
        bt_videoFullScreen = (ImageView) view2.findViewById(R.id.video_full);
        bt_videoInfo = (ImageView) view2.findViewById(R.id.video_info);
        view1 = mLayoutInflater.inflate(R.layout.float_video_player_icon, null);
        bt_video_icon = (ImageView)view1.findViewById(R.id.video_icon);
        windowManager = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);

    /*
     * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
     * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
     * PixelFormat.TRANSPARENT：悬浮窗透明
     */
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        // layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //悬浮窗开始在右下角显示
        layoutParams.gravity = Gravity.CENTER | Gravity.TOP;
    }

    public void initButtonListener() {
        Log.i(TAG, "initButtonListener");
        bt_videoFullScreen.setVisibility(View.INVISIBLE);
        bt_video_icon.setOnTouchListener(new View.OnTouchListener() {
            float[] temp = new float[]{0f, 0f};
            int[] iPointDown = new int[2];
            int[] iPointUp = new int[2];

            public boolean onTouch(View v, MotionEvent event) {
                printLog("bt_video_icon --------------- onTouch");
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
                        printLog("ACTION_DOWN");
                        temp[0] = event.getX();
                        temp[1] = event.getY();
                        iPointDown[0] = (int) temp[0];
                        iPointDown[1] = (int) temp[1];
                        break;
                    case MotionEvent.ACTION_MOVE:
                        printLog("ACTION_MOVE");
                        refreshView(view1, (int) (event.getRawX() - temp[0]), (int) (event
                                .getRawY() - temp[1]));
                        break;
                    case MotionEvent.ACTION_UP:
                        printLog("ACTION_UP");
                        iPointUp[0] = (int) event.getX();
                        iPointUp[1] = (int) event.getY();
                        if ((iPointUp[0] == iPointDown[0]) && (iPointUp[1] == iPointDown[1])) {
                            printLog("bt_video_icon onClick ----------- begin");
                            removeView(view1);
                            refresh(view2);
                            /// view = mLayoutInflater.inflate(R.layout.float_video_player_frame, null);
                            printLog("bt_video_icon onClick ----------- end");
                        }
                        break;
                }
                return true;
            }
        });

        view1.setOnTouchListener(new View.OnTouchListener() {
            float[] temp = new float[] { 0f, 0f };

            public boolean onTouch(View v, MotionEvent event) {
                printLog("view1 --------------- onTouch");
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
                        temp[0] = event.getX();
                        temp[1] = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        refreshView(view1, (int) (event.getRawX() - temp[0]), (int) (event
                                .getRawY() - temp[1]));
                        break;
                }
                return true;
            }
        });

        view2.setOnTouchListener(new View.OnTouchListener() {
            float[] temp = new float[]{0f, 0f};

            public boolean onTouch(View v, MotionEvent event) {
                printLog("view2 --------------- onTouch");
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN: // 按下事件，记录按下时手指在悬浮窗的XY坐标值
                        temp[0] = event.getX();
                        temp[1] = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        refreshView(view2, (int) (event.getRawX() - temp[0]), (int) (event
                                .getRawY() - temp[1]));
                        break;
                }
                return true;
            }
        });
        refresh(view1);

        setOnClickListener(mControlButtonOnClickListener);
        setOnKeyListener(mControlButtonOnKeyListener);
    }

    private void setFloatVideoPlayViewVisibility(boolean flag) {
        if (flag) {
            mFloatVideoPlayView.setVisibility(View.VISIBLE);
        } else {
            mFloatVideoPlayView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新悬浮窗 Refresh float window
     * @param x 拖动后的X轴坐标
     * @param y 拖动后的Y轴坐标
     */
    public void refreshView(View view, int x, int y) {
        //状态栏高度不能立即取，不然得到的值是0
        if(statusBarHeight == 0){
            View rootView  = view.getRootView();
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            statusBarHeight = r.top;
        }

        layoutParams.x = x;
        // y轴减去状态栏的高度，因为状态栏不是用户可以绘制的区域，不然拖动的时候会有跳动
        layoutParams.y = y - statusBarHeight;//STATUS_HEIGHT;
        refresh(view);
    }

    /**
     * 添加悬浮窗或者更新悬浮窗 如果悬浮窗还没添加则添加 如果已经添加则更新其位置
     * Add of Refresh Float window, if float window still not be added, then add it. else refresh it's position.
     */
    private void refresh(View view) {
        printLog("refresh view:" + view);
        if (viewAdded) {
            windowManager.updateViewLayout(view, layoutParams);
        } else {
            windowManager.addView(view, layoutParams);
            viewAdded = true;
        }
    }

    /**
     * removeView
     */
    public void removeView(View view) {
        printLog("removeView view:" + view);
        if (viewAdded) {
            windowManager.removeView(view);
            viewAdded = false;
        }
    }

    public View getViewId(int id) {
        switch (id) {
            case 1:
                return view1;
            case 2:
                return view2;
            default:
                break;
        }
        return view1;
    }

    public SurfaceView getFloatVideoPlayView() {
        return mFloatVideoPlayView;
    }

    // ControlButtonClickListener code
    private ControlButtOnClickListener mControlButtonOnClickListener = new ControlButtOnClickListener();
    private void setOnClickListener(ControlButtOnClickListener controlButtOnClickListener) {
        bt_videoPre.setOnClickListener(controlButtOnClickListener);
        bt_videoPlay.setOnClickListener(controlButtOnClickListener);
        bt_videoNext.setOnClickListener(controlButtOnClickListener);
        bt_videoList.setOnClickListener(controlButtOnClickListener);
        bt_videoInfo.setOnClickListener(controlButtOnClickListener);
    }

    class ControlButtOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(TAG, " ControlButtonClickListener view.getId:" + view.getId());
            Intent intent = null;
            switch (view.getId()) {
                case R.id.video_icon:
                    removeView(view1);
                    refresh(view2);
                    /// view = mLayoutInflater.inflate(R.layout.float_video_player_frame, null);
                    break;
                case R.id.video_previous:
                    intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
                    intent.putExtra("command", MEDIA_PLAYER_PREVIOUS);
                    mContext.startService(intent);
                    break;
                case R.id.video_play:
                    intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
                    intent.putExtra("command", MEDIA_PLAYER_PLAY);
                    mContext.startService(intent);
                    break;
                case R.id.video_next:
                    intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
                    intent.putExtra("command", MEDIA_PLAYER_NEXT);
                    mContext.startService(intent);
                    break;
                case R.id.video_list:
                    intent = new Intent(MediaContainerApplication.getInstance(), FloatVideoListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                    break;
                case R.id.video_info:
                    printLog("bt_videoInfo --------------- onclick");
                    Tools.changeSource(true);
                    bt_videoPlay.setBackgroundResource(R.drawable.float_video_play);
                    removeView(view2);
                    refresh(view1);
                    break;
                default:
                    break;
            }
        }
    }

    // ControlButtonOnKeyListener code
    private ControlButtonOnKeyListener mControlButtonOnKeyListener = new ControlButtonOnKeyListener();
    private void setOnKeyListener(ControlButtonOnKeyListener controlButtonOnKeyListener) {
        bt_videoPre.setOnKeyListener(controlButtonOnKeyListener);
        bt_videoPlay.setOnKeyListener(controlButtonOnKeyListener);
        bt_videoNext.setOnKeyListener(controlButtonOnKeyListener);
        bt_videoList.setOnKeyListener(controlButtonOnKeyListener);
        bt_videoInfo.setOnKeyListener(controlButtonOnKeyListener);
    }

    class ControlButtonOnKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            Log.i(TAG, "------------ ControlButtonKeyListener ---------");
            switch (view.getId()) {
                case R.id.video_icon:
                    break;
                case R.id.video_previous:
                    break;
                case R.id.video_play:
                    break;
                case R.id.video_next:
                    break;
                case R.id.video_list:
                    break;
                case R.id.video_info:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public void mediaPlayerStartedCallback() {
        setFloatVideoPlayViewVisibility(true);
        bt_videoPlay.setBackgroundResource(R.drawable.float_video_pause);
    }

    public void mediaPlayerPausedCallback() {
        bt_videoPlay.setBackgroundResource(R.drawable.float_video_play);
    }

    private void printLog(String msg) {
        if (mNeedDebug) {
            Log.i(TAG, msg);
        }
    }

}
