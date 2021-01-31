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
package com.walton.filebrowser.ui.video;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.video.VideoPlayView;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.VerticalSeekBar;

import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.walton.filebrowser.util.Constants;

import android.widget.Toast;
import com.walton.filebrowser.util.ToastFactory;


public class VideoPlayerViewHolder {
    private static final String TAG = "VideoPlayerViewHolder";
    private Activity videoPlayActivity;
    // control
    // pre a video
    protected ImageView bt_videoPre;
    // FB
    protected ImageView bt_videoRewind;
    // play/pause
    protected ImageView bt_videoPlay;
    // FF
    protected ImageView bt_videoWind;
    // next video
    protected ImageView bt_videoNext;
    protected ImageView bt_videoTime; //
    // video play list
    protected ImageView bt_videoList;
    // video info
    protected ImageView bt_videoInfo;
    // AB repeated
    protected ImageView bt_playAB;
    // voice
    protected ImageView bt_playVoice;
    // Dual video related button
    protected ImageView bt_dualSwitch;
    protected ImageView bt_dualFocusSwitch;
    protected ImageView bt_dualMode;
    // dual mode image view
    protected ImageView dual_leftRight;
    protected ImageView dual_fullScreen;
    protected ImageView dual_leftTop;
    protected ImageView dual_leftBottom;
    protected ImageView dual_rightTop;
    protected ImageView dual_rightBottom;
    // video setting
    protected ImageView bt_videoSetting;
    // Video current broadcast time
    protected TextView current_time_video;
    // Video total time
    protected TextView total_time_video;
    // Video progress bar
    protected SeekBar videoSeekBar;
    // voice bar
    protected VerticalSeekBar voiceBar;
    // AB is aired A point
    protected ImageView bt_playA;
    // AB is aired B point
    protected ImageView bt_playB;
    protected TextView video_name; //
    protected TextView video_list; //
    // play speed
    protected TextView video_playSpeed;
    protected LinearLayout palySettingLayout;
    // Video article contr
    protected LinearLayout playControlLayout;

    // Video image caption View
    private SurfaceView mImageSubtitleSurfaceViewOne,
            mImageSubtitleSurfaceViewTwo;

    private FrameLayout activityLayout = null;
    private FrameLayout firstBorderTextViewLayout = null;
    private FrameLayout secondBorderTextViewLayout = null;
    private FrameLayout imageSurfaceViewOneLayout = null;
    private FrameLayout imageSurfaceViewTwoLayout = null;
    private FrameLayout firstLayout = null;
    private FrameLayout secondLayout = null;
    private LinearLayout noneVideoLayout = null;
    private FrameLayout dualmodeLayoutParent = null;
    private LinearLayout dualmodeLayout = null;
    private LinearLayout voiceLayout = null;
    private FrameLayout voiceLayoutParent = null;
    public LinearLayout controlBarLayout = null;
    public FrameLayout controlBarLayoutParent = null;

    private FrameLayout.LayoutParams params = null;
    private TextView videoFocus;
    private FrameLayout videoFocusLayoutParent = null;
    private FrameLayout videoFocusLayout = null;
    private int screenWidth;
    private int screenHeight;
    private int viewId = 1;
    private boolean isDualVideo = false;
    private boolean isPIPMode = false;
    private boolean isDualLayoutLoaded = false;
    public boolean isDualLayoutShow = false;
    private boolean isVoiceLayoutLoaded = false;
    public boolean isVoiceLayoutShow = false;
    // Video broadcast View
    protected VideoPlayView mVideoPlayViewOne, mVideoPlayViewTwo;
    protected BorderTextViews videoPlayerTextViewOne, videoPlayerTextViewTwo;
    protected boolean mbOneSeek = false;
    protected boolean mbTwoSeek = false;
    // private Resources resources;
    // private List<VideoMenuListItem> playSettingList;
    // Play Settings icon
    public static int[] playSettingIcon = { R.drawable.video_play_mode_icon,
            R.drawable.screen_size_icon, R.drawable.three_d_setting_icon,
            R.drawable.subtitle_icon, R.drawable.track_icon,
            R.drawable.channel_icon };
    // Play set name
    public static int[] playSettingName = {
//            R.string.video_three_d_setting,
            R.string.video_subtitle,
//            R.string.video_breakpoint,
            R.string.video_track,
//            R.string.video_thumbnail_mode,
            R.string.video_rotate_mode,
            R.string.video_goldenlefteye_mode,
            R.string.video_HDR_mode,
//            R.string.video_DolbyHDR_mode,
            R.string.continuous_play,
            R.string.video_swdr
//            R.string.video_audio_AC4
    };
    // playMode
    public static final int SINGE = 0;
    public static final int REPEAT = 1;
    public static final int ORDER = 2;
    public static int currentPlayMode = 2;
    private static final String LOCALMM = "localMM";
    private static final String PLAYMODE = "playMode";
    public static final int OPTION_STATE_PRE = 0x00;
    public static final int OPTION_STATE_REWIND = 0x01;
    public static final int OPTION_STATE_PLAY = 0x02;
    public static final int OPTION_STATE_WIND = 0x03;
    public static final int OPTION_STATE_NEXT = 0x04;
    public static final int OPTION_STATE_INCVOL = 0x05;
    public static final int OPTION_STATE_DECVOL = 0x06;
    public static final int OPTION_STATE_TIME = 0x07;
    public static final int OPTION_STATE_LIST = 0x08;
    public static final int OPTION_STATE_INFO = 0x09;
    public static final int OPTION_STATE_SETTING = 0x10;
    public static final int OPTION_STATE_PLAYAB = 0x11;
    public static final int OPTION_STATE_VOICE = 0x12;
    public static final int OPTION_STATE_SEEKBAR = 0x13;
    public static final int OPTION_STATE_DUAL_SWITCH = 0x14;
    public static final int OPTION_STATE_DUAL_FOCUS = 0x15;
    public static final int OPTION_STATE_DUAL_MODE = 0x16;
    // To determine which control for focus
    protected static int state = OPTION_STATE_PLAY;
    public static final int PIP_DUAL_ClOSED_STATE = 0;
    public static final int PIP_POSITION_LEFT_TOP = 1;
    public static final int PIP_POSITION_LEFT_BOTTOM = 2;
    public static final int PIP_POSITION_RIGHT_TOP = 3;
    public static final int PIP_POSITION_RIGHT_BOTTOM = 4;
    private int currentPIPPosition = PIP_POSITION_RIGHT_BOTTOM;
    public static final int DUAL_MODE_LEFT_RIGHT = 5;
    public static final int DUAL_MODE_FULL_SCREEN = 6;
    public static final int DUAL_MODE_PIP = 7;
    public int currentDualMode = DUAL_MODE_LEFT_RIGHT;
    private int mCurrentDualModeFocus = DUAL_MODE_LEFT_RIGHT;
    private int mCurrentDualModeSelected = DUAL_MODE_LEFT_RIGHT;
    private int mPreviousDualModeSelected = DUAL_MODE_LEFT_RIGHT;
    private int mSubPositionOfPIPMode = 0;
    private boolean mIsSwitchPipMode = false;
    private LayoutInflater inflater = null;
    public LinearLayout mDivxChapterLayout = null;
    public TextView mDivxChapterTTV = null;

    public int getCurrentDualModeSelected() {
        return mCurrentDualModeSelected;
    }

    public void setCurrentDualModeSelected(int mCurrentDualModeSelected) {
        this.mCurrentDualModeSelected = mCurrentDualModeSelected;
    }

    private int mLastTimeSeekPositionOnHoverX;
    public static final int MSG_VOICEBAR_GONE = 0;

    private VideoWindowType videoWindowType = new VideoWindowType();
    private Handler mHandler;

    ThumbnailViewHolder mThumbnailViewHolder = null;

    public VideoPlayerViewHolder(Context context) {
        //findViews();
        addViews();
    }

    public VideoPlayerViewHolder(Context context, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setActivity(Activity act, Handler handler){
        videoPlayActivity = act;
        mHandler = handler;
        //findViews();
        inflater = LayoutInflater.from(videoPlayActivity);
        addViews();
    }


    protected FrameLayout mFullscreenContainer;
    protected FrameLayout mVideoPlayViewOneContainer;
    protected FrameLayout mVideoPlayViewTwoContainer;
    protected FrameLayout mControlBarContainer;
    private FrameLayout decor;

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
        new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

    protected static final FrameLayout.LayoutParams WRAP_CONTENT_PARAMS =
        new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);

    protected static final FrameLayout.LayoutParams MATCH_PARENT_WIDTH_PARAMS =
            new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

    static class FullscreenHolder extends FrameLayout {

            public FullscreenHolder(Context ctx) {
                super(ctx);
                setBackgroundColor(0x00000000);
            }

    }

    private void addViews(){
        Log.i(TAG,"addViews");
        decor = (FrameLayout) videoPlayActivity.getWindow().getDecorView();
        mFullscreenContainer = new FullscreenHolder(videoPlayActivity);
        decor.addView(mFullscreenContainer,COVER_SCREEN_PARAMS);

        addVideoPlayViewOne();
        mFullscreenContainer.addView(firstLayout, COVER_SCREEN_PARAMS);

        addVideoPlayViewTwo();
        mFullscreenContainer.addView(secondLayout, COVER_SCREEN_PARAMS);
        secondLayout.setVisibility(View.GONE);

        addFocusTextView();

        addDualMode();

        addNoneVideo();
        mFullscreenContainer.addView(noneVideoLayout, COVER_SCREEN_PARAMS);
        noneVideoLayout.setVisibility(View.GONE);

        //addVideoControlBar();
        addVoiceView();

        showVideoFocus(true);
    }


    private void addVideoPlayViewOne(){
        Log.i(TAG,"addVideoPlayViewOne");
        firstLayout = new FullscreenHolder(videoPlayActivity);

        imageSurfaceViewOneLayout = (FrameLayout) inflater.inflate(R.layout.image_surfaceview_one_layout,
            null).findViewById(R.id.imageSurfaceViewOneLayout);
        mImageSubtitleSurfaceViewOne = (SurfaceView) imageSurfaceViewOneLayout
                .findViewById(R.id.videoPlayerImageSurfaceViewOne);
        mImageSubtitleSurfaceViewOne.getHolder().setFormat(
                PixelFormat.RGBA_8888);
        mImageSubtitleSurfaceViewOne.setBackgroundColor(Color.TRANSPARENT);

        mVideoPlayViewOne = new VideoPlayView(videoPlayActivity);
        COVER_SCREEN_PARAMS.gravity = Gravity.CENTER;

        firstLayout.addView(mVideoPlayViewOne, COVER_SCREEN_PARAMS);
        mImageSubtitleSurfaceViewOne.setZOrderMediaOverlay(true);
        firstLayout.addView(imageSurfaceViewOneLayout);

        firstBorderTextViewLayout = (FrameLayout) inflater.inflate(R.layout.first_border_textview_layout,
            null).findViewById(R.id.firstBorderTextViewLayout);
        videoPlayerTextViewOne = (BorderTextViews) firstBorderTextViewLayout
                .findViewById(R.id.firstBorderTextView);
        firstLayout.addView(firstBorderTextViewLayout);
        videoPlayerTextViewOne.setVisibility(View.INVISIBLE);
    }

    private void addVideoPlayViewTwo(){

        secondLayout = new FullscreenHolder(videoPlayActivity);
    }
    private void addFocusTextView(){
        videoFocusLayoutParent = new FullscreenHolder(videoPlayActivity);
        mFullscreenContainer.addView(videoFocusLayoutParent,COVER_SCREEN_PARAMS);
        videoFocusLayout = (FrameLayout) inflater.inflate(R.layout.video_focus_layout,
            null).findViewById(R.id.video_focus_layout);

        videoFocus = (TextView) videoFocusLayout
                .findViewById(R.id.video_focus);
        videoFocusLayout.setVisibility(View.INVISIBLE);
        videoFocusLayoutParent.setVisibility(View.INVISIBLE);
    }
    private void addNoneVideo(){
        noneVideoLayout = (LinearLayout) inflater.inflate(R.layout.nonevideolayout,
            null).findViewById(R.id.nonevideolayout);
    }


    public void addVideoControlBar(){
        controlBarLayout = (LinearLayout) inflater.inflate(R.layout.video_control_bar,
            null).findViewById(R.id.video_control_bar);

        // Button control
        bt_videoPre = (ImageView) controlBarLayout
                .findViewById(R.id.video_previous);
        bt_videoRewind = (ImageView) controlBarLayout
                .findViewById(R.id.video_rewind);
        bt_videoPlay = (ImageView) controlBarLayout
                .findViewById(R.id.video_play);
        bt_videoWind = (ImageView) controlBarLayout
                .findViewById(R.id.video_wind);
        bt_videoNext = (ImageView) controlBarLayout
                .findViewById(R.id.video_next);
        bt_videoTime = (ImageView) controlBarLayout
                .findViewById(R.id.video_time);
        bt_videoList = (ImageView) controlBarLayout
                .findViewById(R.id.video_list);
        bt_videoSetting = (ImageView) controlBarLayout
                .findViewById(R.id.video_setting);
        bt_videoInfo = (ImageView) controlBarLayout
                .findViewById(R.id.video_info);
        bt_playAB = (ImageView) controlBarLayout
                .findViewById(R.id.play_icon_ab);
        bt_playVoice = (ImageView) controlBarLayout
                .findViewById(R.id.play_icon_voice);
        bt_dualSwitch = (ImageView) controlBarLayout
                .findViewById(R.id.play_icon_dual_switch);
        bt_dualFocusSwitch = (ImageView) controlBarLayout
                .findViewById(R.id.play_icon_dual_focus_switch);
        bt_dualMode = (ImageView) controlBarLayout
                .findViewById(R.id.play_icon_dual_mode_switch);
        if(!Tools.isSupportDualDecode()){
            bt_dualSwitch.setVisibility(View.GONE);
            bt_dualFocusSwitch.setVisibility(View.GONE);
            bt_dualMode.setVisibility(View.GONE);
        }
        if (Tools.isSambaVideoPlayBack()) {
            bt_videoRewind.setVisibility(View.GONE);
            bt_videoWind.setVisibility(View.GONE);
        }
        bt_playA = (ImageView) controlBarLayout.findViewById(R.id.seek_a);
        bt_playB = (ImageView) controlBarLayout.findViewById(R.id.seek_b);
        current_time_video = (TextView) controlBarLayout
                .findViewById(R.id.control_timer_current);
        total_time_video = (TextView) controlBarLayout
                .findViewById(R.id.control_timer_total);
        videoSeekBar = (SeekBar) controlBarLayout.findViewById(R.id.progress);

        video_name = (TextView) controlBarLayout
                .findViewById(R.id.video_name_display);
        video_list = (TextView) controlBarLayout
                .findViewById(R.id.video_list_display);
        video_playSpeed = (TextView) controlBarLayout
                .findViewById(R.id.video_play_speed_display);

        controlBarLayoutParent = new FullscreenHolder(videoPlayActivity);
        MATCH_PARENT_WIDTH_PARAMS.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mFullscreenContainer.addView(controlBarLayoutParent, MATCH_PARENT_WIDTH_PARAMS);
        controlBarLayoutParent.addView(controlBarLayout);


        setDualButtonNotChoose();
        // Multi Thumbnail
        mThumbnailViewHolder = new ThumbnailViewHolder(videoPlayActivity, this, mHandler);

    }
    private void addDualMode(){
        dualmodeLayout = (LinearLayout) inflater.inflate(R.layout.dual_mode,
                null).findViewById(R.id.dualmodelayout);
        dual_leftRight = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_left_right);
        dual_fullScreen = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_fullscreen);
        dual_leftTop = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_left_top);
        dual_leftBottom = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_left_bottom);
        dual_rightTop = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_right_top);
        dual_rightBottom = (ImageView) dualmodeLayout
                .findViewById(R.id.dual_mode_right_bottom);
        DualModeListener listener = new DualModeListener();
        setDualModeOnClickListener(listener);
    }

    private void addVoiceView(){
        voiceLayoutParent = new FullscreenHolder(videoPlayActivity);
        mFullscreenContainer.addView(voiceLayoutParent,MATCH_PARENT_WIDTH_PARAMS);
        voiceLayoutParent.bringToFront();
        voiceLayout = (LinearLayout) inflater.inflate(R.layout.voicebar, null)
                .findViewById(R.id.voicebarlayout);
        voiceBar = (VerticalSeekBar) voiceLayout.findViewById(R.id.voicebar);
        voiceLayout.setVisibility(View.INVISIBLE);
        voiceLayoutParent.setVisibility(View.INVISIBLE);
    }
    public void setOnClickListener(OnClickListener listener) {
        if (listener != null) {
            bt_videoPre.setOnClickListener(listener);
            bt_videoRewind.setOnClickListener(listener);
            bt_videoPlay.setOnClickListener(listener);
            bt_videoWind.setOnClickListener(listener);
            bt_videoNext.setOnClickListener(listener);
            bt_videoTime.setOnClickListener(listener);
            bt_videoList.setOnClickListener(listener);
            bt_videoInfo.setOnClickListener(listener);
            bt_videoSetting.setOnClickListener(listener);
            bt_playAB.setOnClickListener(listener);
            bt_playVoice.setOnClickListener(listener);
            bt_dualSwitch.setOnClickListener(listener);
            bt_dualMode.setOnClickListener(listener);
            bt_dualFocusSwitch.setOnClickListener(listener);
        }
    }

    public void setDualModeOnClickListener(OnClickListener listener) {
        dual_fullScreen.setOnClickListener(listener);
        dual_leftBottom.setOnClickListener(listener);
        dual_leftRight.setOnClickListener(listener);
        dual_leftTop.setOnClickListener(listener);
        dual_rightBottom.setOnClickListener(listener);
        dual_rightTop.setOnClickListener(listener);
    }

    // All buttons don't choose
    public void setAllUnSelect(boolean isPlaying) {
        setVideoPreSelect(false);
        setVideoRewindSelect(false);
        setVideoPlaySelect(false, isPlaying);
        setVideoWindSelect(false);
        setVideoNextSelect(false);
        setVideoTimeSelect(false);
        setVideoListSelect(false);
        setVideoInforSelect(false);
        setVideoSettingSelect(false);
        setVideoPlayABSelect(false);
        setVideoPlayVoiceSelect(false);
        setVideoDualFocusSwitchSelect(false);
        setVideoDualModeSelect(false);
        setVideoDualSwitchSelect(false);
        if (isDualLayoutShow) {
            //dualmodeLayout.setVisibility(View.INVISIBLE);
            //isDualLayoutShow = false;
            setDualModeLayoutVisibility(false);
        }
    }

    public void setSeekVar(int viewId, boolean kSeek) {
        if (viewId == 1) {
            mbOneSeek = kSeek;
        } else {
            mbTwoSeek = kSeek;
        }
    }

    public boolean isSeekable(int viewId) {
        if (viewId == 1) {
            return mbOneSeek;
        } else {
            return mbTwoSeek;
        }
    }

    // Set video list play state
    public void setVideoListText(int currentPos, int TotalCount) {
        String str = currentPos + "/" + TotalCount;
        if (video_list != null) {
            video_list.setText(str);
        }
    }

    // Set broadcast the speed of
    public void setPlaySpeed(int speed) {
        if (speed < 64 && speed > -64 && video_playSpeed != null) {
            String str;
//            str = String.format("*%d " + videoPlayActivity.getString(R.string.x_times_speed), speed);
            str = String.format("%dX", speed);
            Log.i(TAG, "setPlaySpeed str:" + str);
            if (speed == 1){
                video_playSpeed.setVisibility(View.GONE);
            }else {
                video_playSpeed.setVisibility(View.VISIBLE);
                video_playSpeed.setText(str);
            }
        }
    }

    /**
     *
     * Set video name
     */
    public void setVideoName(String videoName) {
        if (videoName != null && video_name != null) {
            video_name.setText(videoPlayActivity.getString(R.string.playing) + ":"
                + videoName);
        }
    }

    public void reset() {
        /*
         *
         * current_time_video.setText(Tools.formatDuration(0));
         *
         * total_time_video.setText(Tools.formatDuration(0));
         */
        if (current_time_video != null) {
            current_time_video.setText("--:--:--");
        }
        if (total_time_video != null){
            total_time_video.setText("--:--:--");
        }
        if (videoSeekBar != null) {
            videoSeekBar.setProgress(0);
        }
        setPlaySpeed(1);
    }

    /*
     *
     * Processing left key
     */
    public boolean processLeftKey(int keyCode, KeyEvent event) {
        switch (state) {
        case OPTION_STATE_PRE:
            setVideoPreSelect(false);
            if (Tools.isSupportDualDecode()) {
                if (isDualVideo) {
                    state = OPTION_STATE_DUAL_MODE;
                    setVideoDualModeSelect(true);
                } else {
                    state = OPTION_STATE_DUAL_SWITCH;
                    setVideoDualSwitchSelect(true);
                }
            } else {
                state = OPTION_STATE_INFO;
//                setVideoPlayVoiceSelect(true);
                setVideoInforSelect(true);
            }
            break;
        case OPTION_STATE_REWIND:
            state = OPTION_STATE_PRE;
            setVideoRewindSelect(false);
            setVideoPreSelect(true);
            break;
        case OPTION_STATE_PLAY:
            if (Tools.isSambaVideoPlayBack()) {
                state = OPTION_STATE_PRE;
                setVideoPlaySelect(false, getPlayerView().isPlaying());
                //setVideoDualSwitchSelect(false);
                //setVideoListSelect(false);
                //setVideoNextSelect(false);
                setVideoPreSelect(true);
            } else {
                state = OPTION_STATE_REWIND;
                setVideoPlaySelect(false, getPlayerView().isPlaying());
                setVideoRewindSelect(true);
                setVideoDualSwitchSelect(false);
                setVideoListSelect(false);
                setVideoNextSelect(false);
                setVideoPreSelect(false);
            }
            break;
        case OPTION_STATE_WIND:
            state = OPTION_STATE_PLAY;
            setVideoWindSelect(false);
            setVideoPlaySelect(true, getPlayerView().isPlaying());
            break;
        case OPTION_STATE_NEXT:
            if (Tools.isSambaVideoPlayBack()) {
                state = OPTION_STATE_PLAY;
                setVideoNextSelect(false);
                setVideoPlaySelect(true, getPlayerView().isPlaying());
            } else {
                state = OPTION_STATE_WIND;
                setVideoNextSelect(false);
                setVideoWindSelect(true);
            }
            break;
        case OPTION_STATE_TIME:
            state = OPTION_STATE_INFO;
            setVideoTimeSelect(false);
//            setVideoNextSelect(true);
            setVideoInforSelect(true);
            break;
        case OPTION_STATE_LIST:
            state = OPTION_STATE_PLAYAB;
            setVideoListSelect(false);
//            setVideoTimeSelect(true);
            setVideoPlayABSelect(true);
            break;
        case OPTION_STATE_INFO:
            state = OPTION_STATE_LIST;
            setVideoInforSelect(false);
            setVideoListSelect(true);
            break;
        case OPTION_STATE_SETTING:
            state = OPTION_STATE_NEXT;
            setVideoSettingSelect(false);
//            setVideoInforSelect(true);
            setVideoNextSelect(true);
            break;
        case OPTION_STATE_PLAYAB:
            state = OPTION_STATE_SETTING;
            setVideoPlayABSelect(false);
//            setVideoSettingSelect(true);
            setVideoSettingSelect(true);
            break;
        case OPTION_STATE_VOICE:
//            state = OPTION_STATE_PLAYAB;
            state = OPTION_STATE_SETTING;
            setVideoPlayVoiceSelect(false);
//            setVideoPlayABSelect(true);
            setVideoSettingSelect(true);
            break;
        case OPTION_STATE_DUAL_SWITCH:
            state = OPTION_STATE_VOICE;
            setVideoDualSwitchSelect(false);
            setVideoPlayVoiceSelect(true);
            break;
        case OPTION_STATE_DUAL_FOCUS:
            state = OPTION_STATE_DUAL_SWITCH;
            setVideoDualFocusSwitchSelect(false);
            setVideoDualSwitchSelect(true);
            break;
        case OPTION_STATE_DUAL_MODE:
            setDualModeLayoutVisibility(false);
            state = OPTION_STATE_DUAL_FOCUS;
            setVideoDualModeSelect(false);
            setVideoDualFocusSwitchSelect(true);
            break;
        }
        return true;
    }

    /*
     *
     * Processing right key
     */
    public boolean processRightKey(int keyCode, KeyEvent event) {
        switch (state) {
        case OPTION_STATE_PRE:
            if (Tools.isSambaVideoPlayBack()) {
                state = OPTION_STATE_PLAY;
                setVideoPreSelect(false);
                setVideoPlaySelect(true, getPlayerView().isPlaying());

            } else {
                state = OPTION_STATE_REWIND;
                setVideoPreSelect(false);
                setVideoRewindSelect(true);

            }
            break;
        case OPTION_STATE_REWIND:
            state = OPTION_STATE_PLAY;
            setVideoRewindSelect(false);
            setVideoPlaySelect(true, getPlayerView().isPlaying());
            break;
        case OPTION_STATE_PLAY:
            if (Tools.isSambaVideoPlayBack()) {
                state = OPTION_STATE_NEXT;
                setVideoPlaySelect(false, getPlayerView().isPlaying());
                setVideoNextSelect(true);

            } else {
                state = OPTION_STATE_WIND;
                setVideoPlaySelect(false, getPlayerView().isPlaying());
                setVideoWindSelect(true);
                setVideoDualSwitchSelect(false);
                setVideoListSelect(false);
                setVideoNextSelect(false);
                setVideoPreSelect(false);

            }
            break;
        case OPTION_STATE_WIND:
            state = OPTION_STATE_NEXT;
            setVideoWindSelect(false);
            setVideoNextSelect(true);
            break;
        case OPTION_STATE_NEXT:
//            state = OPTION_STATE_TIME;
            state = OPTION_STATE_SETTING;
            setVideoNextSelect(false);
//            setVideoTimeSelect(true);
            setVideoSettingSelect(true);
            break;
        case OPTION_STATE_TIME:
//            state = OPTION_STATE_LIST;
            state = OPTION_STATE_PRE;
            setVideoTimeSelect(false);
//            setVideoListSelect(true);
            setVideoPreSelect(true);
            break;
        case OPTION_STATE_LIST:
            state = OPTION_STATE_INFO;
            setVideoListSelect(false);
            setVideoInforSelect(true);
            break;
        case OPTION_STATE_INFO:
//            state = OPTION_STATE_SETTING;
            state = OPTION_STATE_PRE;
            setVideoInforSelect(false);
//            setVideoSettingSelect(true);
            setVideoPreSelect(true);
            break;
        case OPTION_STATE_SETTING:
//            state = OPTION_STATE_PLAYAB;
            state = OPTION_STATE_PLAYAB;
            setVideoSettingSelect(false);
//            setVideoPlayABSelect(true);
            setVideoPlayABSelect(true);
            break;
        case OPTION_STATE_PLAYAB:
            state = OPTION_STATE_LIST;
            setVideoPlayABSelect(false);
//            setVideoPlayVoiceSelect(true);
            setVideoListSelect(true);
            break;
        case OPTION_STATE_VOICE:
            //Mstar Android Patch Begin
            if(Tools.isSupportDualDecode()){
            //Mstar Android Patch End
                state = OPTION_STATE_DUAL_SWITCH;
                setVideoPlayVoiceSelect(false);
                setVideoDualSwitchSelect(true);
            //Mstar Android Patch Begin
            }else{
//                state = OPTION_STATE_PRE;
                state = OPTION_STATE_LIST;
                setVideoPlayVoiceSelect(false);
//                setVideoPreSelect(true);
                setVideoListSelect(true);
            }
            //Mstar Android Patch End
            break;
        case OPTION_STATE_DUAL_SWITCH:
            setVideoDualSwitchSelect(false);
            if (isDualVideo) {
                state = OPTION_STATE_DUAL_FOCUS;
                setVideoDualFocusSwitchSelect(true);
            } else {
                state = OPTION_STATE_PRE;
                setVideoPreSelect(true);
            }
            break;
        case OPTION_STATE_DUAL_FOCUS:
            state = OPTION_STATE_DUAL_MODE;
            setVideoDualFocusSwitchSelect(false);
            setVideoDualModeSelect(true);
            break;
        case OPTION_STATE_DUAL_MODE:
            setDualModeLayoutVisibility(false);
            state = OPTION_STATE_PRE;
            setVideoDualModeSelect(false);
            setVideoPreSelect(true);
            break;
        }
        return true;
    }

    public boolean processUpKey(int keyCode, KeyEvent event) {
        if (isDualLayoutShow) {
            setDualModeUnSelect();
            switch (mCurrentDualModeFocus) {
            case DUAL_MODE_FULL_SCREEN:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_BOTTOM;
                dual_leftBottom
                        .setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom_focus);
                break;
            case DUAL_MODE_LEFT_RIGHT:
                mCurrentDualModeFocus = DUAL_MODE_FULL_SCREEN;
                dual_fullScreen
                        .setBackgroundResource(R.drawable.player_icon_dual_fullscreen_focus);
                break;
            case PIP_POSITION_RIGHT_TOP:
                mCurrentDualModeFocus = DUAL_MODE_LEFT_RIGHT;
                dual_leftRight
                        .setBackgroundResource(R.drawable.player_icon_dual_left_right_focus);
                break;
            case PIP_POSITION_RIGHT_BOTTOM:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_TOP;
                dual_rightTop
                        .setBackgroundResource(R.drawable.player_icon_dual_pip_right_top_focus);
                break;
            case PIP_POSITION_LEFT_TOP:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_BOTTOM;
                dual_rightBottom
                        .setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom_focus);
                break;
            case PIP_POSITION_LEFT_BOTTOM:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_TOP;
                dual_leftTop
                        .setBackgroundResource(R.drawable.player_icon_dual_pip_left_top_focus);
                break;
            default:
                break;
            }

            if (getDualVideoMode()) {
                setSubtitleSize(1, mCurrentDualModeSelected);
                setSubtitleSize(2, mCurrentDualModeSelected);
            }
        }
        return true;
    }

    public boolean processDownKey(int keyCode, KeyEvent event) {
        if (isDualLayoutShow) {
            setDualModeUnSelect();
            switch (mCurrentDualModeFocus) {
            case DUAL_MODE_FULL_SCREEN:
                mCurrentDualModeFocus = DUAL_MODE_LEFT_RIGHT;
                dual_leftRight.setBackgroundResource(R.drawable.player_icon_dual_left_right_focus);
                break;
            case DUAL_MODE_LEFT_RIGHT:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_TOP;
                dual_rightTop.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top_focus);
                break;
            case PIP_POSITION_RIGHT_TOP:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_BOTTOM;
                dual_rightBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom_focus);
                break;
            case PIP_POSITION_RIGHT_BOTTOM:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_TOP;
                dual_leftTop.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top_focus);
                break;
            case PIP_POSITION_LEFT_TOP:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_BOTTOM;
                dual_leftBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom_focus);
                break;
            case PIP_POSITION_LEFT_BOTTOM:
                mCurrentDualModeFocus = DUAL_MODE_FULL_SCREEN;
                dual_fullScreen.setBackgroundResource(R.drawable.player_icon_dual_fullscreen_focus);
                break;
            default:
                break;
            }

            if (getDualVideoMode()) {
                setSubtitleSize(1, mCurrentDualModeSelected);
                setSubtitleSize(2, mCurrentDualModeSelected);
            }
        }
        return true;
    }

    public boolean processOkKey(int keyCode, KeyEvent event) {
        mCurrentDualModeSelected = mCurrentDualModeFocus;
        mPreviousDualModeSelected = currentDualMode;
        setDualMode(mCurrentDualModeFocus);
        return true;
    }

    public void setDualModeUnSelect() {
        dual_leftBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom);
        dual_fullScreen.setBackgroundResource(R.drawable.player_icon_dual_fullscreen);
        dual_leftRight.setBackgroundResource(R.drawable.player_icon_dual_left_right);
        dual_rightTop.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top);
        dual_rightBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom);
        dual_leftTop.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top);
    }

    public void setVideoPreSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoPre.setFocusable(true);
            bt_videoPre.setBackgroundResource(R.drawable.ic_player_icon_previous_focus);
        } else {
            bt_videoPre.setFocusable(false);
            bt_videoPre.setBackgroundResource(R.drawable.player_icon_previous);
        }
    }

    public void setVideoRewindSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoRewind.setFocusable(true);
            bt_videoRewind.setBackgroundResource(R.drawable.ic_player_icon_rewind_focus);
        } else {
            bt_videoRewind.setFocusable(false);
            bt_videoRewind.setBackgroundResource(R.drawable.player_icon_rewind);
        }
    }

    public void setRewindorForwardSelect(boolean bForward) {
        setAllUnSelect(true);
        if (bForward) {
            state = OPTION_STATE_WIND;
            setVideoWindSelect(true);
        } else {
           state = OPTION_STATE_REWIND;
           setVideoRewindSelect(true);
        }
    }

    public void setVideoPlaySelect(boolean bSelect, boolean isPlaying) {
        bt_videoPlay.setFocusable(bSelect);
        if (bSelect) {
            setAllUnSelect(isPlaying);
            state = OPTION_STATE_PLAY;
            if (isPlaying)
                bt_videoPlay.setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
            else
                bt_videoPlay.setBackgroundResource(R.drawable.ic_player_icon_play_focus);
        } else {
            if (isPlaying)
                bt_videoPlay.setBackgroundResource(R.drawable.player_icon_pause);
            else
                bt_videoPlay.setBackgroundResource(R.drawable.player_icon_play);
        }
    }

    public void setVideoWindSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoWind.setFocusable(true);
            bt_videoWind.setBackgroundResource(R.drawable.ic_player_icon_wind_focus);
        } else {
            bt_videoWind.setFocusable(false);
            bt_videoWind.setBackgroundResource(R.drawable.player_icon_wind);
        }
    }

    public void setVideoNextSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoNext.setFocusable(true);
            bt_videoNext.setBackgroundResource(R.drawable.ic_player_icon_next_focus);
        } else {
            bt_videoNext.setFocusable(false);
            bt_videoNext.setBackgroundResource(R.drawable.player_icon_next);
        }
    }

    public void setVideoTimeSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoTime.setFocusable(true);
            bt_videoTime.setBackgroundResource(R.drawable.ic_player_icon_time_focus);
        } else {
            bt_videoTime.setFocusable(false);
            bt_videoTime.setBackgroundResource(R.drawable.player_icon_time);
        }
    }

    public void setVideoListSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoList.setFocusable(true);
            bt_videoList.setBackgroundResource(R.drawable.ic_player_icon_list_focus);
        } else {
            bt_videoList.setFocusable(false);
            bt_videoList.setBackgroundResource(R.drawable.player_icon_list);
        }
    }

    public void setVideoInforSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoInfo.setFocusable(true);
            bt_videoInfo.setBackgroundResource(R.drawable.ic_player_icon_infor_focus);
        } else {
            bt_videoInfo.setFocusable(false);
            bt_videoInfo.setBackgroundResource(R.drawable.player_icon_infor);
        }
    }

    public void setVideoSettingSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoSetting.setFocusable(true);
            bt_videoSetting.setBackgroundResource(R.drawable.player_icon_setting_foucs);
        } else {
            bt_videoSetting.setFocusable(false);
            bt_videoSetting.setBackgroundResource(R.drawable.player_icon_setting);
        }
    }

    /**
     * Initialization playback modes
     */
    public void initPlayMode() {
        int playmode = currentPlayMode;
        if (playmode == SINGE) {
            bt_playAB
                    .setBackgroundResource(R.drawable.player_icon_singles);
        } else if (playmode == REPEAT) {
            bt_playAB
                    .setBackgroundResource(R.drawable.player_icon_loop);
        } else if (playmode == ORDER) {
            bt_playAB
                    .setBackgroundResource(R.drawable.player_icon_random);
        }
    }


    public void setVideoPlayABSelect(boolean bSelect) {
        if (bSelect) {
            bt_playAB.setFocusable(true);
            if (currentPlayMode == SINGE){
                bt_playAB.setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
            }else if (currentPlayMode == REPEAT){
                bt_playAB.setBackgroundResource(R.drawable.player_icon_loop_focus);
            }else if (currentPlayMode == ORDER){
                bt_playAB.setBackgroundResource(R.drawable.ic_player_icon_ab_focus);
            }
        } else {
            if (currentPlayMode == SINGE){
                bt_playAB.setBackgroundResource(R.drawable.ic_player_icon_singles);
            }else if (currentPlayMode == REPEAT){
                bt_playAB.setBackgroundResource(R.drawable.player_icon_loop);
            }else if (currentPlayMode == ORDER){
                bt_playAB.setBackgroundResource(R.drawable.player_icon_ab);
            }
        }
    }

    /**
     * Playback modes change
     */
    protected void changePlayMode() {
        if (currentPlayMode == SINGE) {
            currentPlayMode = REPEAT;
            setPlayMode(REPEAT);
            bt_playAB
                    .setBackgroundResource(R.drawable.player_icon_loop_focus);
        } else if (currentPlayMode == REPEAT) {
            currentPlayMode = ORDER;
            setPlayMode(ORDER);
            bt_playAB
                    .setBackgroundResource(R.drawable.ic_player_icon_ab_focus);
        } else if (currentPlayMode == ORDER) {
            currentPlayMode = SINGE;
            setPlayMode(SINGE);
            bt_playAB
                    .setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
        }
    }

    /**
     * Set the current playback modes
     *
     * @param mode
     */
    private void setPlayMode(int mode) {
        SharedPreferences preference = videoPlayActivity
                .getSharedPreferences(LOCALMM, MusicPlayerActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(PLAYMODE, mode);
        editor.commit();
    }

    public void setVideoPlayVoiceSelect(boolean bSelect) {
        if (bSelect) {
            bt_playVoice.setFocusable(true);
            bt_playVoice.setBackgroundResource(R.drawable.ic_player_icon_voice_focus);
        } else {
            bt_playVoice.setFocusable(false);
            bt_playVoice.setBackgroundResource(R.drawable.player_icon_voice);
        }
    }

    public void setVideoDualSwitchSelect(boolean bSelect) {
        if (bSelect) {
            bt_dualSwitch.setFocusable(true);
            bt_dualSwitch.setBackgroundResource(R.drawable.player_icon_dual_switch_focus);
        } else {
            bt_dualSwitch.setFocusable(false);
            bt_dualSwitch.setBackgroundResource(R.drawable.player_icon_dual_switch);
        }
    }

    public void setVideoDualFocusSwitchSelect(boolean bSelect) {
        if (bSelect) {
            bt_dualFocusSwitch.setFocusable(true);
            bt_dualFocusSwitch.setBackgroundResource(R.drawable.player_icon_dual_focus_switch_focus);
        } else {
            bt_dualFocusSwitch.setFocusable(false);
            if (isDualVideo) {
                bt_dualFocusSwitch.setBackgroundResource(R.drawable.player_icon_dual_focus_switch);
            } else {
                bt_dualFocusSwitch.setBackgroundResource(R.drawable.player_icon_dual_focus_switch_cannot_choose);
            }
        }
    }

    public void setVideoDualModeSelect(boolean bSelect) {
        if (bSelect) {
            bt_dualMode.setFocusable(true);
            switch (currentDualMode) {
            case DUAL_MODE_FULL_SCREEN:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_fullscreen_focus);
                break;
            case DUAL_MODE_LEFT_RIGHT:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right_focus);
                break;
            case PIP_POSITION_RIGHT_BOTTOM:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom_focus);
                break;
            case PIP_POSITION_RIGHT_TOP:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top_focus);
                break;
            case PIP_POSITION_LEFT_BOTTOM:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom_focus);
                break;
            case PIP_POSITION_LEFT_TOP:
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top_focus);
                break;
            default:
                break;
            }
        } else {
            bt_dualMode.setFocusable(false);
            if (isDualVideo) {
                switch (currentDualMode) {
                case DUAL_MODE_FULL_SCREEN:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_fullscreen);
                    break;
                case DUAL_MODE_LEFT_RIGHT:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right);
                    break;
                case PIP_POSITION_RIGHT_BOTTOM:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom);
                    break;
                case PIP_POSITION_RIGHT_TOP:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top);
                    break;
                case PIP_POSITION_LEFT_BOTTOM:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom);
                    break;
                case PIP_POSITION_LEFT_TOP:
                    bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top);
                    break;
                default:
                    break;
                }
            } else {
                bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right_cannot_choose);
            }
        }
    }

    public SeekBar getProgressBar() {
        return videoSeekBar;
    }

    public SurfaceView getImageSubtitleSurfaceView(int id) {
        if (id == 1) {
            return mImageSubtitleSurfaceViewOne;
        } else {
            return mImageSubtitleSurfaceViewTwo;
        }
    }

    public void setSubTitleText(String str, int id) {
        Log.i(TAG, "----setSubTitleText---- id:" + id + " getDualVideoMode():" + getDualVideoMode() + " mCurrentDualModeFocus:" + mCurrentDualModeFocus);
        if (str.length() != 0) {
            str = "\u200E "+ str ;
        }
        // For RTL language, the BIDI algorithm determines the order of punctuation,
        // and we need to add \u200E character after the line break.
        str = str.replace("\n","\n\u200E");
        if (id == 1) {
            if (videoPlayerTextViewOne != null) {
                if (getDualVideoMode()) {
                    setSubtitleSize(id, mCurrentDualModeSelected);
                }
                videoPlayerTextViewOne.setText(str);
            }
        } else {
            if (videoPlayerTextViewTwo != null) {
                if (getDualVideoMode()) {
                    setSubtitleSize(id, mCurrentDualModeSelected);
                    videoPlayerTextViewTwo.setText(str);
                } else {
                    videoPlayerTextViewTwo.setText(str);
                }
            }
        }
    }

    public void setSubtitleSize(int viewId, int currentDualModeSelected) {
        // left: large, medium (35 sp), small, big
        float size = 0;
        float zoomRate = 1.0f;

        switch (currentDualModeSelected) {
            case PIP_DUAL_ClOSED_STATE:
                zoomRate = 1.0f;
                break;
            case PIP_POSITION_LEFT_TOP:
            case PIP_POSITION_LEFT_BOTTOM:
            case PIP_POSITION_RIGHT_TOP:
            case PIP_POSITION_RIGHT_BOTTOM:
                if (1 == viewId) {
                    zoomRate = 1.0f;
                } else {
                    zoomRate = 0.25f;
                }

                break;
            case DUAL_MODE_LEFT_RIGHT:
                zoomRate = 0.5f;
                break;
            case DUAL_MODE_FULL_SCREEN:
                zoomRate = 1.0f;
                break;
            default :
                break;
        }

        String opt = SubtitleManager.getSubtitleSettingOptValue(5, viewId);
        if (videoPlayActivity.getString(R.string.subtitle_5_value_1).equals(opt)) {
            size = 45.0f;
        } else if (videoPlayActivity.getString(R.string.subtitle_5_value_2).equals(opt)) {
            size = 35.0f;
        } else if (videoPlayActivity.getString(R.string.subtitle_5_value_3).equals(opt)) {
            size = 25.0f;
        }

        Log.i(TAG, "size:" + size + "zoomRate:" + zoomRate + " viewId:" + viewId);

        if (1 == viewId) {
            videoPlayerTextViewOne.setTextSize(size * zoomRate);
        } else {
            videoPlayerTextViewTwo.setTextSize(size * zoomRate);
        }

    }

    public BorderTextViews getSubtitleTextView() {
        if (viewId == 1) {
            return videoPlayerTextViewOne;
        } else {
            return videoPlayerTextViewTwo;
        }
    }

    public SurfaceView getSubtitleImageView() {
        if (viewId == 1) {
            return mImageSubtitleSurfaceViewOne;
        } else {
            return mImageSubtitleSurfaceViewTwo;
        }
    }

    public void setSubtitleTextViewVisible(boolean bvisible) {
        if (getSubtitleTextView()!=null) {
            getSubtitleTextView().setVisibility(bvisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setSubtitleImageViewVisible(boolean bvisible) {
        if (getSubtitleImageView()!=null) {
            getSubtitleImageView().setVisibility(bvisible ? View.VISIBLE : View.GONE);
        }
    }

    public VideoPlayView getPlayerView() {
        if (viewId == 1) {
            return mVideoPlayViewOne;
        } else {
            return mVideoPlayViewTwo;
        }
    }

    public VideoPlayView getPlayerView(int id) {
        if (id == 1) {
            return mVideoPlayViewOne;
        } else {
            return mVideoPlayViewTwo;
        }
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public int getViewId() {
        return this.viewId;
    }

    public void setSubPositionOfPIPMode(int pos) {
        Log.i(TAG,"mSubPositionOfPIPMode:"+pos);
        this.mSubPositionOfPIPMode = pos;
    }

    public int getSubPositionOfPIPMode(){
        return mSubPositionOfPIPMode;
    }

    public void setIsSwitchPipMode(boolean flag) {
        Log.i(TAG,"setIsSwitchPipMode:"+flag);
        this.mIsSwitchPipMode= flag;
    }

    public boolean getIsSwitchPipMode() {
        return mIsSwitchPipMode;
    }

    public FrameLayout getLinearLayout(int viewId) {
        switch (viewId) {
            case 1:
                return firstLayout;
            case 2:
                return  secondLayout;
            default:
                return  firstLayout;
        }
    }

    public boolean getDualVideoMode() {
        return isDualVideo;
    }

    public int getCurrentDualModeFocus() {
        return mCurrentDualModeFocus;
    }

    public boolean isPIPMode(int id) {
        if (isPIPMode && id == 2) {
            return true;
        }
        return false;
    }

    private void addSubALLView(){
        Log.i(TAG,"andrew addSubALLView");
        secondLayout.setVisibility(View.VISIBLE);

        mVideoPlayViewTwo= new VideoPlayView(videoPlayActivity);
        COVER_SCREEN_PARAMS.gravity = Gravity.CENTER;
        //mVideoPlayViewTwo.setBackgroundColor(Color.TRANSPARENT);
        mVideoPlayViewTwo.setZOrderMediaOverlay(true);
        secondLayout.addView(mVideoPlayViewTwo, COVER_SCREEN_PARAMS);

        imageSurfaceViewTwoLayout = (FrameLayout) inflater.inflate(R.layout.image_surfaceview_two_layout,
            null).findViewById(R.id.imageSurfaceViewTwoLayout);
        mImageSubtitleSurfaceViewTwo = (SurfaceView) imageSurfaceViewTwoLayout
                .findViewById(R.id.videoPlayerImageSurfaceViewTwo);
        mImageSubtitleSurfaceViewTwo.getHolder().setFormat(
                PixelFormat.RGBA_8888);
        mImageSubtitleSurfaceViewTwo.setBackgroundColor(Color.TRANSPARENT);
        mImageSubtitleSurfaceViewTwo.setZOrderOnTop(true);
        secondLayout.addView(imageSurfaceViewTwoLayout);
        //mImageSubtitleSurfaceViewTwo.setVisibility(View.GONE);

        secondBorderTextViewLayout = (FrameLayout) inflater.inflate(R.layout.second_border_textview_layout,
            null).findViewById(R.id.secondBorderTextViewLayout);
        videoPlayerTextViewTwo= (BorderTextViews) secondBorderTextViewLayout
                .findViewById(R.id.secondBorderTextView);
        secondLayout.addView(secondBorderTextViewLayout);
        videoPlayerTextViewTwo.setVisibility(View.INVISIBLE);

        videoFocusLayout.setVisibility(View.VISIBLE);
        videoFocusLayoutParent.setVisibility(View.VISIBLE);
        videoFocusLayoutParent.addView(videoFocusLayout);

    }

    private void removeSubAllView(){
        Log.i(TAG,"andrew removeSubAllView");
        secondLayout.removeView(mVideoPlayViewTwo);
        secondLayout.removeView(mImageSubtitleSurfaceViewTwo);
        secondLayout.removeView(videoPlayerTextViewTwo);
        secondLayout.setVisibility(View.GONE);

        videoFocusLayout.setVisibility(View.INVISIBLE);
        videoFocusLayoutParent.setVisibility(View.INVISIBLE);
        videoFocusLayoutParent.removeView(videoFocusLayout);
    }

    public void addDivxChapterUI(){
        removeDivxChapterUI();
        if (firstLayout == null) {
            return;
        }
        FrameLayout.LayoutParams tmpLayoutParams = null;
        mDivxChapterLayout = (LinearLayout )inflater.inflate(R.layout.divx_chapter
            ,null).findViewById(R.id.divx_chapter_layout);
        tmpLayoutParams = new FrameLayout.LayoutParams(screenWidth/8,screenWidth/16,
            Gravity.TOP | Gravity.RIGHT);
        mDivxChapterLayout.setLayoutParams(tmpLayoutParams);
        firstLayout.addView(mDivxChapterLayout);

    }

    public void removeDivxChapterUI(){
        if (mDivxChapterLayout!=null && firstLayout!=null) {
            firstLayout.removeView(mDivxChapterLayout);
        }
    }

    /**
     * open dual decode
     *
     * @param willOpen
     */
    public void openOrCloseDualDecode(boolean willOpen) {
        Log.i(TAG,"openOrCloseDualDecode:"+willOpen);
        if (willOpen) {
            addSubALLView();

            isDualVideo = true;
            // mantis-0636831:Others module(such as MtvMisc.apk),
            // need exec in different way by the property when localmm dual playing.
            Tools.setSystemProperty("mstar.localmm.dual-playing","true");
            currentDualMode = DUAL_MODE_LEFT_RIGHT;
            params = new FrameLayout.LayoutParams(screenWidth / 2,
                    screenHeight, Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(screenWidth / 2,
                    screenHeight, Gravity.RIGHT);
            secondLayout.setLayoutParams(params);
            showVideoFocus(true);
            videoFocusLayout.setLayoutParams(params);
            // remove setVideoWindow() ,which do noting.
            setDualButtonOptional();
        } else {
            isDualVideo = false;
            Tools.setSystemProperty("mstar.localmm.dual-playing","false");
            showVideoFocus(false);
            if (mVideoPlayViewTwo != null) {
                mVideoPlayViewTwo.pause();
                mVideoPlayViewTwo.stopPlayer();
                mVideoPlayViewTwo.setPlayerCallbackListener(null);
            }
            //params = new FrameLayout.LayoutParams(2, 2, Gravity.BOTTOM
                    //| Gravity.RIGHT);
            params = new FrameLayout.LayoutParams(2, screenHeight, Gravity.RIGHT);

            secondLayout.setLayoutParams(params);
            removeSubAllView();
            params = new FrameLayout.LayoutParams(screenWidth, screenHeight,
                    Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            videoFocusLayout.setLayoutParams(params);
            // remove setVideoWindow() ,which do noting.
            if (viewId != 1) {
                switchFocusedView();
            }
            setDualButtonNotChoose();
            if (currentDualMode == DUAL_MODE_FULL_SCREEN) {
                Tools.setVideoWindowVisable(true, true);
            }
        }
    }

    public void setDualButtonNotChoose() {
        bt_dualFocusSwitch.setBackgroundResource(R.drawable.player_icon_dual_focus_switch_cannot_choose);
        bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right_cannot_choose);
    }

    public void setDualButtonOptional() {
        bt_dualFocusSwitch.setBackgroundResource(R.drawable.player_icon_dual_focus_switch);
        bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right);
    }

    public void switchFocusedView() {
        if (viewId == 1) {
            viewId = 2;
            params = (FrameLayout.LayoutParams) secondLayout.getLayoutParams();
        } else {
            viewId = 1;
            params = (FrameLayout.LayoutParams) firstLayout.getLayoutParams();
        }
        if (currentDualMode == DUAL_MODE_FULL_SCREEN) {
            Tools.setVideoWindowVisable(true, true);
            params = new FrameLayout.LayoutParams(screenWidth, screenHeight,
                    Gravity.CENTER);
            if (viewId == 1) {
                firstLayout.setLayoutParams(params);
                // setVideoWindowVisable(false, false);
            } else {
                secondLayout.setLayoutParams(params);
                Tools.setVideoWindowVisable(true, false);
            }
            params = new FrameLayout.LayoutParams(2, screenHeight, Gravity.BOTTOM
                    | Gravity.RIGHT);
            if (viewId == 1) {
                secondLayout.setLayoutParams(params);
            } else {
                // firstLayout.setLayoutParams(params);
            }
        }
        videoFocusLayout.setLayoutParams(params);
    }

    public void refreshControlInfo(String videoName, int speed, int currentPos,
            int TotalCount, int duration) {
        Log.i(TAG, "refreshControlInfo videoName:" + videoName + " speed:" + speed + " currentPos:" + currentPos
                + " TotalCount:" + TotalCount + " duration:" + duration);
        reset();
        setVideoName(videoName);
        setPlaySpeed(speed);
        setVideoListText(currentPos, TotalCount);
        if (duration != 0) {
            total_time_video.setText(Tools.formatDuration(duration));
            videoSeekBar.setMax(duration);
        }
    }

    /**
     * change the position of the PIP.
     */
    public void changePIPposition() {
        switch (currentPIPPosition) {
        case PIP_POSITION_RIGHT_BOTTOM:
            currentPIPPosition = PIP_POSITION_RIGHT_TOP;
            break;
        case PIP_POSITION_RIGHT_TOP:
            currentPIPPosition = PIP_POSITION_LEFT_BOTTOM;
            break;
        case PIP_POSITION_LEFT_BOTTOM:
            currentPIPPosition = PIP_POSITION_LEFT_TOP;
            break;
        case PIP_POSITION_LEFT_TOP:
            currentPIPPosition = PIP_POSITION_RIGHT_BOTTOM;
            break;
        default:
            break;
        }
        setPIPposition(currentPIPPosition);
    }

    /**
     * set the position of the PIP.
     *
     * @param position
     *            Position index.
     */
    public void setPIPposition(int position) {
        if (isPIPMode) {
            switch (position) {
            case PIP_POSITION_RIGHT_BOTTOM:
                params = new FrameLayout.LayoutParams(screenWidth / 4,
                        screenHeight / 4, Gravity.BOTTOM | Gravity.RIGHT);
                break;
            case PIP_POSITION_RIGHT_TOP:
                params = new FrameLayout.LayoutParams(screenWidth / 4,
                        screenHeight / 4, Gravity.TOP | Gravity.RIGHT);
                // activityLayout.addView(firstLayout);
                break;
            case PIP_POSITION_LEFT_BOTTOM:
                params = new FrameLayout.LayoutParams(screenWidth / 4,
                        screenHeight / 4, Gravity.BOTTOM | Gravity.LEFT);
                break;
            case PIP_POSITION_LEFT_TOP:
                params = new FrameLayout.LayoutParams(screenWidth / 4,
                        screenHeight / 4, Gravity.TOP | Gravity.LEFT);
                break;

            default:
                break;
            }
            secondLayout.setLayoutParams(params);
            if (viewId == 2) {
                videoFocusLayout.setLayoutParams(params);
            }
        }
    }

    /**
     * change dual video display mode.
     */
    public void changeDualMode() {
        mCurrentDualModeFocus = currentDualMode;
        showDualModeLayout();
        isPIPMode = false;
        // videoPlayerTextViewTwo.setTextSize(SubtitleTool.getCurrentSubSize());
        setDualModeUnSelect();
        switch (currentDualMode) {
        case DUAL_MODE_FULL_SCREEN:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_fullscreen);
            dual_fullScreen.setBackgroundResource(R.drawable.player_icon_dual_fullscreen_focus);
            break;
        case DUAL_MODE_LEFT_RIGHT:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_left_right);
            dual_leftRight.setBackgroundResource(R.drawable.player_icon_dual_left_right_focus);
            break;
        case PIP_POSITION_RIGHT_BOTTOM:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom);
            dual_rightBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_right_bottom_focus);
            break;
        case PIP_POSITION_RIGHT_TOP:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top);
            dual_rightTop.setBackgroundResource(R.drawable.player_icon_dual_pip_right_top_focus);
            break;
        case PIP_POSITION_LEFT_BOTTOM:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom);
            dual_leftBottom.setBackgroundResource(R.drawable.player_icon_dual_pip_left_bottom_focus);
            break;
        case PIP_POSITION_LEFT_TOP:
            bt_dualMode.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top);
            dual_leftTop.setBackgroundResource(R.drawable.player_icon_dual_pip_left_top_focus);
        default:
            break;
        }
    }

    private void showSubCannotSptFsn4k2kTip() {
        String strMessage =videoPlayActivity.getResources().getString(R.string.video_sub_no_support_fullscreen_in_4K2K);
        Toast toast = ToastFactory.getToast(videoPlayActivity, strMessage, Gravity.CENTER);
        toast.show();
    }

    /**
     * set dual video display mode.
     *
     * @param state
     *            display mode.
     */
    public void setDualMode(int state) {
        // mantis:1188093.
        if (state == DUAL_MODE_FULL_SCREEN
            && viewId == 2
            && !Tools.isSubSupportFullScreenIn4K2K()
            && Tools.is4K2KPlatForm()) {
            showSubCannotSptFsn4k2kTip();
            return;
        }
        currentDualMode = state;
        setDualModeLayoutVisibility(false);
        setVideoDualModeSelect(true);
        // setVideoWindowVisable(false, true);
        Tools.setVideoWindowVisable(true, true);
        switch (state) {
        case DUAL_MODE_FULL_SCREEN:
            params = new FrameLayout.LayoutParams(screenWidth, screenHeight,
                    Gravity.CENTER);
            if (viewId == 1) {
                firstLayout.setLayoutParams(params);
                // setVideoWindowVisable(false, false);
            } else {
                secondLayout.setLayoutParams(params);
                Tools.setVideoWindowVisable(true, false);
            }
            params = new FrameLayout.LayoutParams(2, screenHeight, Gravity.RIGHT);
            if (viewId == 1) {
                secondLayout.setLayoutParams(params);
            } else {
                // firstLayout.setLayoutParams(params);
            }
            break;
        case DUAL_MODE_LEFT_RIGHT:
            showOrHideSubView(false);
            params = new FrameLayout.LayoutParams(screenWidth / 2,screenHeight, Gravity.RIGHT);
            secondLayout.setLayoutParams(params);
            mHandler.sendEmptyMessageDelayed(Constants.CHECK_PIP_SUB_POSITION_LEFT_RIGHT, 50);
            break;
        case PIP_POSITION_RIGHT_BOTTOM:
            showOrHideSubView(false);
            mHandler.sendEmptyMessageDelayed(Constants.CHECK_PIP_SUB_POSITION_RIGHT_BOTTOM, 50);
            params = new FrameLayout.LayoutParams(screenWidth , screenHeight ,
                    Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(screenWidth / 4,
                    screenHeight / 4, Gravity.BOTTOM | Gravity.RIGHT);
            secondLayout.setLayoutParams(params);
            break;
        case PIP_POSITION_RIGHT_TOP:
            showOrHideSubView(false);
            mHandler.sendEmptyMessageDelayed(Constants.CHECK_PIP_SUB_POSITION_RIGHT_TOP, 50);
            params = new FrameLayout.LayoutParams(screenWidth , screenHeight ,
                    Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(screenWidth / 4,
                    screenHeight / 4, Gravity.TOP | Gravity.RIGHT);
            secondLayout.setLayoutParams(params);
            break;
        case PIP_POSITION_LEFT_BOTTOM:
            showOrHideSubView(false);
            mHandler.sendEmptyMessageDelayed(Constants.CHECK_PIP_SUB_POSITION_LEFT_BOTTOM, 50);
            params = new FrameLayout.LayoutParams(screenWidth , screenHeight ,
                    Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(screenWidth / 4,
                    screenHeight / 4, Gravity.BOTTOM | Gravity.LEFT);
            secondLayout.setLayoutParams(params);
            break;
        case PIP_POSITION_LEFT_TOP:
            showOrHideSubView(false);
            mHandler.sendEmptyMessageDelayed(Constants.CHECK_PIP_SUB_POSITION_LEFT_TOP, 50);
            params = new FrameLayout.LayoutParams(screenWidth , screenHeight ,
                    Gravity.LEFT);
            firstLayout.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(screenWidth / 4,
                    screenHeight / 4, Gravity.TOP | Gravity.LEFT);
            secondLayout.setLayoutParams(params);
            break;
        default:
            break;
        }

        if (viewId == 1) {
            params = (FrameLayout.LayoutParams) firstLayout.getLayoutParams();
        } else {
            params = (FrameLayout.LayoutParams) secondLayout.getLayoutParams();
        }
        videoFocusLayout.setLayoutParams(params);

        setVideoWindow();

    }

    public void changeMainVideoView () {
        Log.i(TAG,"changeMainVideoView");
        params = new FrameLayout.LayoutParams(screenWidth / 2,screenHeight, Gravity.LEFT);
        firstLayout.setLayoutParams(params);
        if (viewId == 1) {
            params = (FrameLayout.LayoutParams) firstLayout.getLayoutParams();
        } else {
            params = (FrameLayout.LayoutParams) secondLayout.getLayoutParams();
        }
        videoFocusLayout.setLayoutParams(params);
        setVideoWindow();
    }

    public void showOrHideSubView(boolean flag){
        if (flag == false && getPlayerView(2).isInPlaybackState() == false) {
            return;
        }
        Log.i(TAG,"showOrHideSubView:"+flag);
        if (flag) {
            //firstLayout.setVisibility(View.VISIBLE);
            secondLayout.setVisibility(View.VISIBLE);
            videoFocusLayout.setVisibility(View.VISIBLE);
            getPlayerView(2).setVisibility(View.VISIBLE);
            getImageSubtitleSurfaceView(2).setVisibility(View.VISIBLE);
        } else {
            //firstLayout.setVisibility(View.GONE);
            setIsSwitchPipMode(true);
            setSubPositionOfPIPMode(getPlayerView(2).getCurrentPosition());
            secondLayout.setVisibility(View.INVISIBLE);
            videoFocusLayout.setVisibility(View.INVISIBLE);
            getPlayerView(2).setVisibility(View.INVISIBLE);
            getImageSubtitleSurfaceView(2).setVisibility(View.INVISIBLE);
        }

    }

    public void showDefaultPhoto(boolean isShow) {
        if (isShow) {
            noneVideoLayout.setVisibility(View.VISIBLE);
        } else {
            noneVideoLayout.setVisibility(View.GONE);
        }
    }

    /**
     * show focus which video controlled.
     */
    public void showVideoFocus(boolean isShow) {
        if (isShow && isDualVideo) {
            //videoFocus.setPaintColor(Color.RED);
            videoFocus.setBackgroundResource(R.drawable.border_focused);
            Log.i("VideoPlayerViewHolder",
                    "*******showVideoFocus***********" + true);
        } else {
            //videoFocus.setPaintColor(Color.TRANSPARENT);
            videoFocus.setBackgroundResource(R.drawable.border_unfocus);
            Log.i("VideoPlayerViewHolder",
                    "*******showVideoFocus***********" + false);
        }
        //videoFocus.invalidate();
    }

    public void showDualModeLayout() {
        if (!isDualLayoutLoaded) {
            params = new FrameLayout.LayoutParams(88, 300, Gravity.RIGHT
                    | Gravity.BOTTOM);
            params.bottomMargin = controlBarLayout.getHeight()
                    - bt_dualMode.getHeight();
            params.rightMargin = (screenWidth - controlBarLayout.getWidth())
                    / 2 + bt_dualMode.getHeight() - 26;
            Log.i(TAG, "**showDualModeLayout**" + " "
                    + params.bottomMargin + " " + params.rightMargin + " "
                    + bt_dualMode.getBottom());
            dualmodeLayout.setLayoutParams(params);
            decor.addView(dualmodeLayout, params);
            dualmodeLayout.bringToFront();
            isDualLayoutLoaded = true;
        }
        dualmodeLayout.setVisibility(View.VISIBLE);
        isDualLayoutShow = true;
    }

    class DualModeListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (!isDualLayoutShow) {
                return;
            }
            switch (arg0.getId()) {
            case R.id.dual_mode_fullscreen:
                mCurrentDualModeFocus = DUAL_MODE_FULL_SCREEN;
                break;
            case R.id.dual_mode_left_bottom:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_BOTTOM;
                break;
            case R.id.dual_mode_left_right:
                mCurrentDualModeFocus = DUAL_MODE_LEFT_RIGHT;
                break;
            case R.id.dual_mode_left_top:
                mCurrentDualModeFocus = PIP_POSITION_LEFT_TOP;
                break;
            case R.id.dual_mode_right_bottom:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_BOTTOM;
                break;
            case R.id.dual_mode_right_top:
                mCurrentDualModeFocus = PIP_POSITION_RIGHT_TOP;
                break;

            default:
                break;
            }
            mCurrentDualModeSelected = mCurrentDualModeFocus;
            mPreviousDualModeSelected = currentDualMode;
            setDualMode(mCurrentDualModeFocus);
            if (getDualVideoMode()) {
                setSubtitleSize(1, mCurrentDualModeSelected);
                setSubtitleSize(2, mCurrentDualModeSelected);
            }

            mHandler.sendEmptyMessage(MSG_VOICEBAR_GONE);
        }
    }

    public void setVideoWindow() {
        params = (FrameLayout.LayoutParams) firstLayout.getLayoutParams();
        Log.i(TAG, "****setVideoWindow****params********" + params.leftMargin
                + " " + params.topMargin + " " + params.width + " "
                + params.height);
        videoWindowType.x = params.leftMargin;
        videoWindowType.y = params.topMargin;
        videoWindowType.width = params.width;
        videoWindowType.height = params.height;
    }

    public void updateVoiceBar() {
        loadVoiceLayout();
        if(mHandler != null)
            mHandler.removeMessages(MSG_VOICEBAR_GONE);
        setVoiceLayoutVisibility(true);
        // isVoiceLayoutShow = true;
        int voice = (int) getPlayerView().getVoice();
        Log.i(TAG, "******voice*******" + voice);
        switch (voice) {
        case 0:
            voiceLayout.setBackgroundResource(R.drawable.sound_zore);
            break;
        case 1:
            voiceLayout.setBackgroundResource(R.drawable.sound_one);
            break;
        case 2:
            voiceLayout.setBackgroundResource(R.drawable.sound_two);
            break;
        case 3:
            voiceLayout.setBackgroundResource(R.drawable.sound_three);
            break;
        case 4:
            voiceLayout.setBackgroundResource(R.drawable.sound_four);
            break;
        case 5:
            voiceLayout.setBackgroundResource(R.drawable.sound_five);
            break;
        case 6:
            voiceLayout.setBackgroundResource(R.drawable.sound_six);
            break;
        case 7:
            voiceLayout.setBackgroundResource(R.drawable.sound_seven);
            break;
        case 8:
            voiceLayout.setBackgroundResource(R.drawable.sound_eight);
            break;
        case 9:
            voiceLayout.setBackgroundResource(R.drawable.sound_nine);
            break;
        case 10:
            voiceLayout.setBackgroundResource(R.drawable.sound_ten);
            break;

        default:
            break;
        }
        if(mHandler != null)
            mHandler.sendEmptyMessageDelayed(MSG_VOICEBAR_GONE, 5000);
    }

    private void loadVoiceLayout() {
        if (!isVoiceLayoutLoaded) {
            params = new FrameLayout.LayoutParams(30, 190, Gravity.RIGHT
                    | Gravity.CENTER);
//            params.bottomMargin = controlBarLayout.getHeight()
//                    -  bt_playVoice.getHeight();

            params.bottomMargin = controlBarLayout.getHeight() / 2;
            if (!Tools.isSupportDualDecode()) {
//                params.rightMargin = (screenWidth - controlBarLayout.getWidth())
//                    / 2 + controlBarLayout.getWidth() / 5 - 54;


                params.rightMargin = (int) (bt_playVoice.getHeight() * 1.8);
           } else {
//                params.rightMargin = (screenWidth - controlBarLayout.getWidth())
//                    / 2 + controlBarLayout.getWidth() / 4;

                params.rightMargin = (int) (bt_playVoice.getHeight() * 1.8);

           }
            Log.i(TAG, "******loadVoiceLayout******** " + params.bottomMargin + " " + params.rightMargin + " " + bt_dualMode.getBottom());
            voiceLayout.setLayoutParams(params);
            //decor.addView(voiceLayout, params);
            voiceLayoutParent.addView(voiceLayout);
            setVoiceLayoutVisibility(false);
            voiceBar.setHolder(this);
            isVoiceLayoutLoaded = true;
        }
    }

    public void setVoice(int voice) {
        Log.i(TAG, "******setVoice********" + voice);
        getPlayerView().setVoice(voice);
        updateVoiceBar();
    }

    public void addVoice(boolean isAdd) {
        getPlayerView().addVoice(isAdd);
        updateVoiceBar();
    }

    public void setVoiceLayoutVisibility(boolean visibility){
        if (visibility) {
            voiceLayoutParent.setVisibility(View.VISIBLE);
            voiceLayoutParent.bringToFront();
            voiceLayout.setVisibility(View.VISIBLE);
            isVoiceLayoutShow = true;
        } else {
            voiceLayoutParent.setVisibility(View.INVISIBLE);
            voiceLayout.setVisibility(View.INVISIBLE);
            isVoiceLayoutShow = false;
        }
    }

    public void setDualModeLayoutVisibility(boolean visibility) {
        if (visibility) {
            dualmodeLayout.setVisibility(View.VISIBLE);
            isDualLayoutShow = true;
        } else {
            dualmodeLayout.setVisibility(View.INVISIBLE);
            isDualLayoutShow = false;
        }
    }

    public ThumbnailViewHolder getThumbnailViewHolder() {
        return mThumbnailViewHolder;
    }

    public void resetSubtitleTextView() {
        if (viewId == 1) {
            if (firstLayout != null && firstBorderTextViewLayout != null)
                firstLayout.removeView(firstBorderTextViewLayout);
            firstBorderTextViewLayout = (FrameLayout) inflater.inflate(R.layout.first_border_textview_layout,
                    null).findViewById(R.id.firstBorderTextViewLayout);
            videoPlayerTextViewOne = (BorderTextViews) firstBorderTextViewLayout
                    .findViewById(R.id.firstBorderTextView);
            firstLayout.addView(firstBorderTextViewLayout);
            videoPlayerTextViewOne.setVisibility(View.INVISIBLE);
        } else {
            if (secondLayout != null && secondBorderTextViewLayout != null) {
                secondBorderTextViewLayout.removeView(secondBorderTextViewLayout);
                secondBorderTextViewLayout = (FrameLayout) inflater.inflate(R.layout.second_border_textview_layout,
                        null).findViewById(R.id.secondBorderTextViewLayout);
                videoPlayerTextViewTwo= (BorderTextViews) secondBorderTextViewLayout
                        .findViewById(R.id.secondBorderTextView);
                secondLayout.addView(secondBorderTextViewLayout);
                videoPlayerTextViewTwo.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void removeAllViews() {
        decor.removeAllViews();
    }

}
