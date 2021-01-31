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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.video.VideoGLSurfaceView;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 * Created by nate.luo on 14-12-10.
 */

public class ThumbnailViewHolder {
    private static final String TAG = "ThumbnailViewHolder";

    private Activity mActivity = null;
    private VideoPlayerViewHolder mVideoPlayerViewHolder = null;
    private Handler mHandler = null;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;
    private ViewGroup.LayoutParams mCurrentParams = null;
    private float mLastSeekBaronHoverX;
    private int mLastThumbnailonHoverIndex;
    private boolean isThumbnailBorderLineShow = false;

    // Multi Thumbnail View for HW
    private LinearLayout mMultiThumbnailLayout = null;
    private LinearLayout mVideoGLSurfaceViewLayout = null;
    private VideoGLSurfaceView mVideoGLSurfaceView = null;
    private ImageView mThumbnailTimePositionView= null;
    private ImageView []mThumbnailBorderLine = null;

    // for sw
    private LinearLayout mMultiThumbnailSWLayout = null;
    private LinearLayout mMultiThumbnailSWView = null;
    private ImageView []mThumbnailBorderLineSW = null;
    private ImageView []mThumbnailSWView = null;

    public ThumbnailViewHolder(Activity activity, VideoPlayerViewHolder videoPlayerViewHolder, Handler handler) {
        mActivity = activity;
        mVideoPlayerViewHolder = videoPlayerViewHolder;
        mHandler = handler;
        initViews();
        initConfig();
        setListener();
    }

    private void initViews() {
        mMultiThumbnailLayout = (LinearLayout)mVideoPlayerViewHolder.controlBarLayout.findViewById(R.id.main_thumbnail_layout);
        mVideoGLSurfaceViewLayout = (LinearLayout)mMultiThumbnailLayout.findViewById(R.id.video_glsurfaceview_layout);
        mVideoGLSurfaceView = (VideoGLSurfaceView)mMultiThumbnailLayout.findViewById(R.id.video_glsurfaceview);
        mThumbnailTimePositionView = (ImageView) mVideoPlayerViewHolder.controlBarLayout.findViewById(R.id.thumbnail_time_position_view);

        mThumbnailBorderLine = new ImageView[2];
        mThumbnailBorderLine[0] = (ImageView) mVideoPlayerViewHolder.controlBarLayout.findViewById(R.id.thumbnail_topline);
        mThumbnailBorderLine[1] = (ImageView) mVideoPlayerViewHolder.controlBarLayout.findViewById(R.id.thumbnail_bottomline);
    }

    private void initConfig() {
        mSharedPreferences = mActivity.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mCurrentParams = mVideoGLSurfaceView.getLayoutParams();
        mEditor.putInt("GLSurfaceViewLayoutWidth", mCurrentParams.width);
        mEditor.putInt("GLSurfaceViewLayoutHeight", mCurrentParams.height);
        int thumbnailWidth = (mCurrentParams.width-12*Tools.getThumbnailNumber())/Tools.getThumbnailNumber();
        int thumbnailHeight = mCurrentParams.height-20;
        Log.i(TAG, "thumbnailWidth:" + thumbnailWidth + " thumbnailHeight:" + thumbnailHeight);
        mEditor.putInt("thumbnailWidth", thumbnailWidth);
        mEditor.putInt("thumbnailHeight", thumbnailHeight);
        mEditor.commit();
        Log.i(TAG, "[initConfig] GLSurfaceViewLayout width:" + mCurrentParams.width + " height:" + mCurrentParams.height + " mThumbnailNumber:" + Tools.getThumbnailNumber());

    }

    private void setListener() {
        mVideoGLSurfaceView.setOnHoverListener(onHoverListener);
        mVideoPlayerViewHolder.getProgressBar().setOnHoverListener(seekBaronHoverListener);
    }

    private View.OnHoverListener seekBaronHoverListener = new View.OnHoverListener() {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            if (!Tools.isThumbnailModeOn()) {
                return true;
            }
            if (!Tools.thumbnailSeekBarEnable()) {
                return true;
            }
            int seekBarWidth = mVideoPlayerViewHolder.getProgressBar().getWidth();
            int []seekBarLocation = new int[2];
            mVideoPlayerViewHolder.getProgressBar().getLocationOnScreen(seekBarLocation);
            int seekBarLocationX = seekBarLocation[0];
            int seekBarLocationY = seekBarLocation[1];
            float getX;
            float getY;
            // Log.i(TAG, "seekBarLocationX:" + seekBarLocationX + " seekBarWidth:" + seekBarWidth);
            int what = event.getAction();
            Message msg = null;
            if (mHandler != null) {
                msg = mHandler.obtainMessage();
            } else {
                msg = new Message();
            }
            switch (what) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    resetThumbnailHeight();
                    getX = event.getX();
                    getY = event.getY();
                    // Log.i(TAG, "seekBaronHoverListener ACTION_HOVER_ENTER getX:" + getX);
                    msg.what = Constants.seekThumbnailPos;
                    showThumbnailBorderView();
                    if (mVideoGLSurfaceView != null) {
                        if (getX > 0) {
                            Log.i(TAG, "mLastSeekBaronHoverX:" + mLastSeekBaronHoverX + " getX:" + getX);
                            if ((((getX - 2.5) < mLastSeekBaronHoverX)) && (getX + 2.5 > mLastSeekBaronHoverX)) {
                                return true;
                            }

                            mEditor.putBoolean("SeekBarOnHover", true);
                            mEditor.commit();
                            mLastSeekBaronHoverX = getX;
                            msg.arg1 = (int)(mVideoGLSurfaceView.getDuration()*getX/seekBarWidth);
                            Log.i(TAG, "msg.arg1:" + msg.arg1);
                            if (Tools.isThumbnailMode_SW_On()) {
                                int msPositin = (int)(mVideoPlayerViewHolder.getPlayerView(mVideoPlayerViewHolder.getViewId()).getDuration()*getX/seekBarWidth);
                                showMultiThumbnailView_SW(msPositin);
                                break;
                            }

                            if (msg.arg1 > 0) {
                                mHandler.removeMessages(Constants.seekThumbnailPos);
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    resetThumbnailHeight();
                    getX = event.getX();
                    getY = event.getY();
                    // Log.i(TAG, "seekBaronHoverListener ACTION_HOVER_MOVE getX:" + getX);
                    msg.what = Constants.seekThumbnailPos;
                    if (mVideoGLSurfaceView != null) {
                        if (getX > 0) {
                            Log.i(TAG, "mLastSeekBaronHoverX:" + mLastSeekBaronHoverX + " getX:" + getX);
                            if ((((getX - 2.5) < mLastSeekBaronHoverX)) && (getX + 2.5 > mLastSeekBaronHoverX)) {
                                return true;
                            }
                            mEditor.putBoolean("SeekBarOnHover", true);
                            mEditor.commit();
                            mLastSeekBaronHoverX = getX;
                            msg.arg1 = (int)(mVideoGLSurfaceView.getDuration()*getX/seekBarWidth);
                            // Log.i(TAG, "msg.arg1:" + msg.arg1);
                            if (Tools.isThumbnailMode_SW_On()) {
                                int msPositin = (int)(mVideoPlayerViewHolder.getPlayerView(mVideoPlayerViewHolder.getViewId()).getDuration()*getX/seekBarWidth);
                                showMultiThumbnailView_SW(msPositin);
                                break;
                            }
                            if (msg.arg1 > 0) {
                                mHandler.removeMessages(Constants.seekThumbnailPos);
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    Log.i(TAG, "seekBaronHoverListener ACTION_HOVER_EXIT");
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private View.OnHoverListener onHoverListener = new View.OnHoverListener() {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            if (!Tools.isThumbnailModeOn()) {
                return true;
            }

            boolean thumbnailBorderViewFocusFlag = mSharedPreferences.getBoolean("ThumbnailBorderViewFocus", false);
            if (!thumbnailBorderViewFocusFlag) {
                return true;
            }

            int []thumbnailLayoutLocation = new int[2];
            mVideoGLSurfaceView.getLocationOnScreen(thumbnailLayoutLocation);
            int thumbnailLayoutLocationX = thumbnailLayoutLocation[0];
            int thumbnailLayoutLocationY = thumbnailLayoutLocation[1];
            // Log.i(TAG, "thumbnailLayoutLocationX:" + thumbnailLayoutLocationX + " thumbnailLayoutLocationY:" + thumbnailLayoutLocationY);
            // Log.i(TAG, "mVideoGLSurfaceView width:" + mVideoGLSurfaceView.getWidth() + " height:" + mVideoGLSurfaceView.getHeight());

            int thumbnailWidth = (mVideoGLSurfaceView.getWidth()-12*Tools.getThumbnailNumber())/Tools.getThumbnailNumber();
            int thumbnailHeight = mVideoGLSurfaceView.getHeight()-20;
            Log.i(TAG, "thumbnailWidth:" + thumbnailWidth + " thumbnailHeight:" + thumbnailHeight);
            float getX;
            float getY;
            int index = -1;
            int []imageLeftX = new int[Tools.getThumbnailNumber()];
            int []imageRightX = new int[Tools.getThumbnailNumber()];
            int thumbNum = Tools.getThumbnailNumber();
            for (int i = 0; i < thumbNum; i++) {
                imageLeftX[i] = thumbnailLayoutLocationX + 15*i+thumbnailWidth*i;
                imageRightX[i] = thumbnailLayoutLocationX + 15*i+thumbnailWidth*(i+1);
                // Log.i(TAG, "i:" + i + "=(" + imageLeftX[i] + " ," + imageRightX[i] + ")");
            }
            int what = event.getAction();
            switch (what) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    getX = event.getX();
                    getY = event.getY();
                    //  Log.i(TAG, "ACTION_HOVER_ENTER -- X:" + getX + " Y:" + getY);
                    getX = getX + thumbnailLayoutLocationX;
                    index = -1;
                    for (int which = 0; which < thumbNum; which++) {
                        if ((getX > imageLeftX[which]) && (getX < imageRightX[which])) {
                            index = which;
                        }
                    }

                    Log.i(TAG, "mLastThumbnailonHoverIndex:" + mLastThumbnailonHoverIndex + " index:" + index);
                    if (mLastThumbnailonHoverIndex == index) {
                        return true;
                    }
                    mLastThumbnailonHoverIndex = index;
                    mEditor.putInt("thumbnailWidth", thumbnailWidth);
                    mEditor.putInt("thumbnailHeight", thumbnailHeight);
                    mEditor.putInt("Index", index);
                    if (index != -1) {
                        mEditor.putBoolean("ThumbnailOnHover", true);
                    } else {
                        mEditor.putBoolean("ThumbnailOnHover", false);
                    }
                    mEditor.commit();
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    getX = (int)event.getX();
                    getY = (int)event.getY();
                    //  Log.i(TAG, "ACTION_HOVER_MOVE -- X:" + getX + " Y:" + getY);
                    getX = getX + thumbnailLayoutLocationX;
                    index = -1;
                    for (int which = 0; which < thumbNum; which++) {
                        if ((getX > imageLeftX[which]) && (getX < imageRightX[which])) {
                            index = which;
                        }
                    }

                    Log.i(TAG, "mLastThumbnailonHoverIndex:" + mLastThumbnailonHoverIndex + " index:" + index);
                    if (mLastThumbnailonHoverIndex == index) {
                        return true;
                    }
                    mEditor.putInt("thumbnailWidth", thumbnailWidth);
                    mEditor.putInt("thumbnailHeight", thumbnailHeight);
                    mEditor.putInt("Index", index);
                    if (index != -1) {
                        mEditor.putBoolean("ThumbnailOnHover", true);
                    } else {
                        mEditor.putBoolean("ThumbnailOnHover", false);
                    }
                    mEditor.commit();
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    mEditor.putInt("Index", -1);
                    mEditor.putBoolean("ThumbnailOnHover", false);
                    mEditor.commit();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    // Fix:at the first time thumbnail show, the border view is not at the right position.
    private void getThumbnailLayoutConfig() {
        int []thumbnailLayoutLocation = new int[2];
        mVideoGLSurfaceView.getLocationOnScreen(thumbnailLayoutLocation);
        int thumbnailWidth = (mVideoGLSurfaceView.getWidth()-12*Tools.getThumbnailNumber())/Tools.getThumbnailNumber();
        int thumbnailHeight = mVideoGLSurfaceView.getHeight()-20;
        mEditor.putInt("thumbnailWidth", thumbnailWidth);
        mEditor.putInt("thumbnailHeight", thumbnailHeight);
        mEditor.commit();
    }

    private void resetThumbnailHeight() {
        mEditor.putBoolean("ThumbnailOnHover", false);
        mEditor.putInt("Index", -1);
        mEditor.commit();
    }

    private void setThumbnailBorderViewFocusFlag(boolean flag) {
        mEditor.putBoolean("ThumbnailBorderViewFocus", flag);
        mEditor.commit();
    }


    public ImageView getThumbnailTimePositionView() {
        return mThumbnailTimePositionView;
    }

    public LinearLayout getVideoGLSurfaceViewLayout() {
        return mVideoGLSurfaceViewLayout;
    }

    public VideoGLSurfaceView getVideoGLSurfaceView() {
        return mVideoGLSurfaceView;
    }

    public void initGLSurfaceView(String videoPath) {
        mVideoGLSurfaceViewLayout.setVisibility(View.VISIBLE);
        getVideoGLSurfaceView().setVisibility(View.VISIBLE);
        getVideoGLSurfaceView().init(mHandler, videoPath);
        //showThumbnailBorderView();
    }

    public void releaseGLSurfaceView(boolean bAsyncRelease) {
        Log.i(TAG, "-------------- releaseGLSurfaceView ----------");
        hideThumbnailSeekImageView();
        hideThumbnailBorderView();
        getVideoGLSurfaceView().releaseThumbnailPlayer(bAsyncRelease);
        getVideoGLSurfaceView().setRenderMode(0);
        getVideoGLSurfaceView().setVisibility(View.GONE);
        mVideoGLSurfaceViewLayout.setVisibility(View.GONE);
    }

    public void showThumbnailBorderView() {
        if (!isThumbnailBorderLineShow && mThumbnailBorderLine != null) {
            isThumbnailBorderLineShow = true;
            for (int i = 0; i < mThumbnailBorderLine.length; i++) {
                mThumbnailBorderLine[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideThumbnailBorderView() {
        if (isThumbnailBorderLineShow && mThumbnailBorderLine != null) {
            isThumbnailBorderLineShow = false;
            for (int i = 0; i < mThumbnailBorderLine.length; i++) {
                mThumbnailBorderLine[i].setVisibility(View.GONE);
            }
        }
    }

    public void setThumbnailOnFocus(int index) {

    }

    public void showThumbnailSeekImageView(int currentPosition) {
        int videoSeekBarWidth = mVideoPlayerViewHolder.getProgressBar().getWidth();
        float position = (currentPosition / (float)mVideoPlayerViewHolder.getPlayerView().getDuration()) * videoSeekBarWidth;
        Log.i(TAG, "showThumbnailSeekImageView currentPosition:" + currentPosition + " position:" + position);
        if (position < 0) {
            position = 0;
        } else if (position > videoSeekBarWidth) {
            position = videoSeekBarWidth;
        }
        getThumbnailTimePositionView().setPadding((int) position, 0, 0, 12);
        getThumbnailTimePositionView().setVisibility(View.VISIBLE);
    }

    public void hideThumbnailSeekImageView() {
        Log.i(TAG, "hideThumbnailSeekImageView");
        getThumbnailTimePositionView().setVisibility(View.GONE);
    }

    public void initMultiThumbnailView_SW() {
        mMultiThumbnailSWLayout = (LinearLayout)mMultiThumbnailLayout.findViewById(R.id.multi_thumbnail_sw_layout);
        mMultiThumbnailSWLayout.setVisibility(View.VISIBLE);
        mMultiThumbnailSWView = (LinearLayout)mMultiThumbnailLayout.findViewById(R.id.multi_thumbnail_sw_view);
        mThumbnailSWView = new ImageView[mMultiThumbnailSWView.getChildCount()];
        Log.i(TAG, "mMultiThumbnailSWView.getChildCount():" + mMultiThumbnailSWView.getChildCount());
        int count = mMultiThumbnailSWView.getChildCount();
        for (int i = 0; i < count; i++) {
            mThumbnailSWView[i] = (ImageView)mMultiThumbnailSWView.getChildAt(i);
            mThumbnailSWView[i].setVisibility(View.VISIBLE);
        }

        mThumbnailBorderLineSW = new ImageView[mMultiThumbnailSWLayout.getChildCount() - 1];
        mThumbnailBorderLineSW[0] = (ImageView)mMultiThumbnailSWLayout.getChildAt(0);
        mThumbnailBorderLineSW[1] = (ImageView)mMultiThumbnailSWLayout.getChildAt(2);
        mThumbnailBorderLineSW[0].setVisibility(View.VISIBLE);
        mThumbnailBorderLineSW[1].setVisibility(View.VISIBLE);

        int getCurrentPosition = mVideoPlayerViewHolder.getPlayerView(mVideoPlayerViewHolder.getViewId()).getCurrentPosition();
        int thumbNum = Tools.getThumbnailNumber();
        for (int num = 0; num < thumbNum; num++) {
            int framePosition = getCurrentPosition + (int)(num-Tools.getThumbnailNumber()/2)*2000;
            ((VideoPlayerActivity)mActivity).getThumbnailController().getFrameCapture(framePosition, num);
            ((VideoPlayerActivity)mActivity).getThumbnailController().storeThumbnailTimeStamp(num, framePosition);
        }
        setMultiThumbnailOnClickListener_SW();
    }

    public ImageView getThumbnailView_SW(int i) {
        if (mThumbnailSWView != null) {
            return mThumbnailSWView[i];
        }
        return null;
    }

    public void showMultiThumbnailView_SW(int msPositin) {
        Log.i(TAG, "-----showMultiThumbnail_SW_View ---- msPositin:" + msPositin);
        if (mThumbnailSWView != null) {
            int thumbNum = Tools.getThumbnailNumber();
            for (int num = 0; num < thumbNum; num++) {
                int framePosition = msPositin + (int)(num-Tools.getThumbnailNumber()/2)*2000;
                ((VideoPlayerActivity)mActivity).getThumbnailController().getFrameCapture(framePosition, num);
                ((VideoPlayerActivity)mActivity).getThumbnailController().storeThumbnailTimeStamp(num, framePosition);
            }
        }
    }

    public void hideMultiThumbnailView_SW() {
        if (mThumbnailBorderLineSW != null) {
            for (int i = 0; i < mThumbnailBorderLineSW.length; i++) {
                mThumbnailBorderLineSW[i].setVisibility(View.GONE);
            }
        }
        if (mThumbnailSWView != null) {
            for (int num = 0; num < mThumbnailSWView.length; num++) {
                mThumbnailSWView[num].setVisibility(View.GONE);
            }
        }
        if (mMultiThumbnailSWLayout != null) {
            mMultiThumbnailSWLayout.setVisibility(View.GONE);
        }
    }

    private void setMultiThumbnailOnClickListener_SW() {
        if (mThumbnailSWView != null) {
            for (int num = 0; num < mThumbnailSWView.length; num++) {
                mThumbnailSWView[num].setOnClickListener(multiThumbnailOnClickListener_SW);
            }
        }
    }

    private View.OnClickListener multiThumbnailOnClickListener_SW = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.thumbnail_sw_first:
                    ((VideoPlayerActivity)mActivity).getThumbnailController().dispatchKeyEvent(0);
                    break;
                case R.id.thumbnail_sw_second:
                    ((VideoPlayerActivity)mActivity).getThumbnailController().dispatchKeyEvent(1);
                    break;
                case R.id.thumbnail_sw_third:
                    ((VideoPlayerActivity)mActivity).getThumbnailController().dispatchKeyEvent(2);
                    break;
                case R.id.thumbnail_sw_forth:
                    ((VideoPlayerActivity)mActivity).getThumbnailController().dispatchKeyEvent(3);
                    break;
                case R.id.thumbnail_sw_fifth:
                    ((VideoPlayerActivity)mActivity).getThumbnailController().dispatchKeyEvent(4);
                    break;
                default:
                    break;
            }
        }
    };

}
