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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 * Created by nate.luo on 14-12-10.
 */

public class ThumbnailController {

    private static final String TAG = "ThumbnailController";
    private static final int mThumbnailForwardInterval = 3000;
    private static final int GetFrameDone = 1;
    private VideoPlayerActivity mVideoPlayerActivity = null;
    private ThumbnailViewHolder mThumbnailViewHolder = null;
    private Handler mVideoPlayerHanlder = null;

    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;

    private int mThumbnailPlaySpeed = 1;
    private boolean mThumbnailOnFocus = false;
    private int mCurrentFocusThumbnailIndex = 2;
    private int mCurrentThumbnailFocusPosition = 0;
    private int mThumbnailPageIncrement = 10;
    private long mLastThumbnailPageRefreshTimeMillis = 0;

    public ThumbnailController(VideoPlayerActivity context, ThumbnailViewHolder thumbnailViewHolder) {
        mVideoPlayerActivity = context;
        mThumbnailViewHolder = thumbnailViewHolder;
        mVideoPlayerHanlder = mVideoPlayerActivity.getVideoHandler();
        initConfig();
    }

    private Handler mThumbnailHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GetFrameDone:
                    Bitmap bitmap = (Bitmap)msg.obj;
                    int thumbnailViewId = msg.arg1;
                    Log.i(TAG, "GetFrameDone bitmap:" + bitmap + " thumbnailViewId:" + thumbnailViewId);
                    if (bitmap != null) {
                        if (getThumbnailViewHolder().getThumbnailView_SW(thumbnailViewId) != null) {
                            getThumbnailViewHolder().getThumbnailView_SW(thumbnailViewId).setImageBitmap(bitmap);
                        }
                    }
                    break;
                default :
                    break;
            }
        };
    };

    private void initConfig() {
        mSharedPreferences = mVideoPlayerActivity.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void thumbnailFastForward() {
        Log.d(TAG, "get play mode ---" + getThumbnailPlaySpeed());
        int currentSpeed = 1 * 2;
        showThumbnailBorderView();
        if (getThumbnailPlaySpeed() < 64 && getThumbnailPlaySpeed() > 0) {
            currentSpeed = getThumbnailPlaySpeed() * 2;
        }
        setThumbnailPlaySpeed(currentSpeed);

        sendFowardMessage(1);
    }

    public void thumbnailSlowForward() {
        int currentSpeed = 1 * (-2);
        showThumbnailBorderView();
        if (getThumbnailPlaySpeed() < 64 && getThumbnailPlaySpeed() < 0) {
            currentSpeed = getThumbnailPlaySpeed() * 2;
        }
        setThumbnailPlaySpeed(currentSpeed);

        sendFowardMessage(1);
    }

    private void sendFowardMessage(int flag) {
        Message msg = Message.obtain(mVideoPlayerHanlder, Constants.seekThumbnailPos);
        msg.arg1 = getCurrentThumbnailFocusPosition();
        msg.arg2 = flag;
        mVideoPlayerHanlder.sendMessageDelayed(msg, mThumbnailForwardInterval);
    }

    public void setCurrentThumbnailFocusPosition(int value) {
        mCurrentThumbnailFocusPosition = value;
    }

    public int getCurrentThumbnailFocusPosition() {
        return mCurrentThumbnailFocusPosition;
    }

    public void setThumbnailPlaySpeed(int value) {
        mThumbnailPlaySpeed = value;
    }

    public int getThumbnailPlaySpeed() {
        return mThumbnailPlaySpeed;
    }

    public void setThumbnailOnFocus(boolean value) {
        mThumbnailOnFocus = value;
    }

    public boolean getThumbnailOnFocus() {
        return mThumbnailOnFocus;
    }

    public ThumbnailViewHolder getThumbnailViewHolder() {
        return mThumbnailViewHolder;
    }

    public void dispatchKeyEvent(int keyCode) {
        Message msg = Message.obtain(mVideoPlayerHanlder, Constants.SeekWithHideThumbnailBorderView);
        msg.arg1 = mSharedPreferences.getInt("TextureTimeStamp" + keyCode, mCurrentThumbnailFocusPosition);
        Log.i(TAG, "dispatchKeyCode keyCode:" + keyCode + " TextureTimeStamp:" + msg.arg1);
        mVideoPlayerHanlder.sendMessage(msg);
        mThumbnailPlaySpeed = 1;
        setThumbnailPlaySpeed(mThumbnailPlaySpeed);
    }

    public void storeThumbnailTimeStamp(int i, int timeStamp) {
        mEditor.putInt("TextureTimeStamp" + i, timeStamp);
        mEditor.commit();
    }

    public void respondThumbnailOnKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "ThumbnailModeOn respondThumbnailOnKeyDown ");

        if (Tools.isThumbnailMode_SW_On()) {
            return;
        }
        // When KEYCODE_DPAD_UP, remote control will focus on Thumbnail.
        if (KeyEvent.KEYCODE_DPAD_UP == keyCode) {
            if (!getThumbnailViewHolder().getVideoGLSurfaceViewLayout().isFocused()) {
                getThumbnailViewHolder().getVideoGLSurfaceViewLayout().requestFocus();
                getThumbnailViewHolder().getVideoGLSurfaceViewLayout().setFocusable(true);
                mCurrentFocusThumbnailIndex = 2;
                mThumbnailViewHolder.setThumbnailOnFocus(mCurrentFocusThumbnailIndex);
            }
        }

        // When KEYCODE_DPAD_DOWN, remote control will focus on Controller, and reset mThumbnailOnFocus to false.
        if (KeyEvent.KEYCODE_DPAD_DOWN == keyCode) {
            if (getThumbnailViewHolder().getVideoGLSurfaceViewLayout().isFocused()) {
                getThumbnailViewHolder().getVideoGLSurfaceViewLayout().setFocusable(false);
            } else {
                showThumbnailBorderView();
                mThumbnailViewHolder.setThumbnailOnFocus(-1);
                mVideoPlayerHanlder.sendEmptyMessage(Constants.ShowController);
                mThumbnailViewHolder.getVideoGLSurfaceView().getThumbnailFrame(mVideoPlayerActivity.getVideoPlayHolder().getPlayerView().getCurrentPosition(), Tools.getThumbnailNumber(), 2000);
                showThumbnailSeekImageView(mVideoPlayerActivity.getVideoPlayHolder().getPlayerView().getCurrentPosition());
            }
        }

        if (getThumbnailViewHolder().getVideoGLSurfaceViewLayout().isFocused()) {
            if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                // When Remote Control has already focused on the fifth thumbnail, and KEYCODE_DPAD_RIGHT will re-get 5 thumnails(later) to show.
                if (mCurrentFocusThumbnailIndex >= 4) {
                    getThumbnailPageIncrement();
                    Log.i(TAG, "+mCurrentThumbnailFocusPosition:" + mCurrentThumbnailFocusPosition + " mThumbnailPageIncrement:" + mThumbnailPageIncrement);
                    mCurrentThumbnailFocusPosition += mThumbnailPageIncrement;

                    Message msg = Message.obtain(mVideoPlayerHanlder, Constants.seekThumbnailPos);
                    msg.arg1 = mCurrentThumbnailFocusPosition;
                    mVideoPlayerHanlder.removeMessages(Constants.seekThumbnailPos);
                    mVideoPlayerHanlder.sendMessage(msg);
                } else {
                    mCurrentFocusThumbnailIndex++;
                    mThumbnailViewHolder.setThumbnailOnFocus(mCurrentFocusThumbnailIndex);
                    showThumbnailSeekImageView(mCurrentThumbnailFocusPosition + (mCurrentFocusThumbnailIndex - 2) * 10000);
                }
                mVideoPlayerHanlder.sendEmptyMessage(Constants.ShowController);
            } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                // When Remote Control has already focused on the first thumbnail, and KEYCODE_DPAD_LEFT will re-get 5 thumnails(earlier) to show.
                if (mCurrentFocusThumbnailIndex <= 0) {
                    getThumbnailPageIncrement();
                    Log.i(TAG, "-mCurrentThumbnailFocusPosition:" + mCurrentThumbnailFocusPosition + " mThumbnailPageIncrement:" + mThumbnailPageIncrement);
                    mCurrentThumbnailFocusPosition -= mThumbnailPageIncrement;
                    Message msg = Message.obtain(mVideoPlayerHanlder, Constants.seekThumbnailPos);
                    msg.arg1 = mCurrentThumbnailFocusPosition;
                    mVideoPlayerHanlder.removeMessages(Constants.seekThumbnailPos);
                    mVideoPlayerHanlder.sendMessage(msg);
                } else {
                    mCurrentFocusThumbnailIndex--;
                    mThumbnailViewHolder.setThumbnailOnFocus(mCurrentFocusThumbnailIndex);
                    showThumbnailSeekImageView(mCurrentThumbnailFocusPosition + (mCurrentFocusThumbnailIndex - 2) * 10000);
                }
                mVideoPlayerHanlder.sendEmptyMessage(Constants.ShowController);
            } else if (KeyEvent.KEYCODE_ENTER == keyCode) {
                dispatchKeyEvent(mCurrentFocusThumbnailIndex);
                mThumbnailOnFocus = false;
                mCurrentFocusThumbnailIndex = 2;
                mThumbnailViewHolder.setThumbnailOnFocus(-1);
                hideThumbnailSeekImageView();
                mVideoPlayerHanlder.sendEmptyMessage(Constants.ShowController);
            }
        }
    }

    public void getThumbnailPageIncrement() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mLastThumbnailPageRefreshTimeMillis >= 1000) {
            mThumbnailPageIncrement = 10000;
        } else {
            double timeMillisIncrementFrequency = 1000.0000 / (currentTimeMillis - mLastThumbnailPageRefreshTimeMillis);
            Log.i(TAG, "timeMillisIncrementFrequency:" + timeMillisIncrementFrequency);
            mThumbnailPageIncrement = (int) (timeMillisIncrementFrequency * 10000);
            // mThumbnailPageIncrement = (int)(timeMillisIncrementFrequency * videoPlayerHolder.getPlayerView().getDuration() / 30);
        }
        mLastThumbnailPageRefreshTimeMillis = currentTimeMillis;
    }

    public void getThumbnailFrame(int position, int number, int interval) {
        if (Tools.isThumbnailMode_SW_On()) {
            showMultiThumbnailView_SW(position);
        } else {
            showMultiThumbnailView_HW(position, number, interval);
            showThumbnailSeekImageView(position);
        }
    }

    public void showThumbnailSeekImageView(int currentPosition) {
        mThumbnailViewHolder.showThumbnailSeekImageView(currentPosition);
    }

    public void hideThumbnailSeekImageView() {
        mThumbnailViewHolder.hideThumbnailSeekImageView();
    }

    public void showThumbnailBorderView() {
        getThumbnailViewHolder().showThumbnailBorderView();
    }

    public void hideThumbnailBorderView() {
        getThumbnailViewHolder().hideThumbnailBorderView();
    }

    public void initThumbnailView() {
        if (Tools.isThumbnailMode_SW_On()) {
            initMultiThumbnailView_SW();
        } else {
            initMultiThumbnailView_HW();
        }
    }

    public void releaseThumbnailView(boolean bAsyncRelease) {
        if (Tools.isThumbnailMode_SW_On()) {
            hideMultiThumbnailView_SW();
        } else {
            hideThumbnailBorderView();
            releaseMultiThumbnailView_HW(bAsyncRelease);
        }
    }

    public void initMultiThumbnailView_HW() {
        getThumbnailViewHolder().initGLSurfaceView(mVideoPlayerActivity.getCurrentVideoPlayPath());
    }

    public void releaseMultiThumbnailView_HW(boolean bAsyncRelease) {
        getThumbnailViewHolder().releaseGLSurfaceView(bAsyncRelease);
    }

    public void showMultiThumbnailView_HW(int position, int number, int interval) {
        getThumbnailViewHolder().getVideoGLSurfaceView().getThumbnailFrame(position, number, interval);
    }

    public void initMultiThumbnailView_SW() {
        getThumbnailViewHolder().initMultiThumbnailView_SW();
    }

    public void hideMultiThumbnailView_SW() {
        getThumbnailViewHolder().hideMultiThumbnailView_SW();
    }

    public void showMultiThumbnailView_SW(int position) {
        getThumbnailViewHolder().showMultiThumbnailView_SW(position);
    }

    public void getFrameCapture(final int position, final int thumbnailViewId) {
        Log.i(TAG, "------getFrameCapture ------ ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                bitmap = getFrameAtTime(position);
                if (bitmap != null) {
                    Message msg = mThumbnailHandler.obtainMessage(GetFrameDone);
                    msg.arg1 = thumbnailViewId;
                    msg.obj = bitmap;
                    mThumbnailHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public Bitmap getFrameAtTime(int position) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap bitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(((VideoPlayerActivity)mVideoPlayerActivity).getCurrentVideoPlayPath());
            long startTimeMillis = System.currentTimeMillis();
            bitmap = mediaMetadataRetriever.getFrameAtTime((int)(position * 1000)); // ms
            long endTimeMillis = System.currentTimeMillis();
            Log.i(TAG, "getFrame costs time:" + (endTimeMillis - startTimeMillis) + "ms, currentTimeMillisStart:" +
                    startTimeMillis + " endTimeMillis:" + endTimeMillis);
            return bitmap;
        } catch(Exception ex) {
            Log.i(TAG, "Exception:" + ex);
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
            return bitmap;
        }
    }
}
