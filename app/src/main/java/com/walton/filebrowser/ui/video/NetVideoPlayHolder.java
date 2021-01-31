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

import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.video.VideoPlayView;

/**
 * NetVideoPlayActivity broadcast control bar.
 *
 * @author
 * @category: .
 */
public class NetVideoPlayHolder {
    public static final String TAG = "NetVideoPlayHolder";
    private NetVideoPlayActivity videoPlayActivity;
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
    // choose time
    protected ImageView bt_videoTime;
    // choose list
    protected ImageView bt_videoList;
    // Video current broadcast time
    protected TextView current_time_video;
    // Video in time
    protected TextView total_time_video;
    // Video progress bar
    protected SeekBar videoSeekBar;
    protected TextView video_name; //
    protected TextView video_list; //
    // play speed
    protected TextView video_playSpeed;
    protected LinearLayout palySettingLayout;
	protected LinearLayout musicLayout;
    // Video article control
    protected LinearLayout playControlLayout;
    // Video play View
    protected VideoPlayView mVideoPlayView;
    protected boolean mbNotSeek = false;
    protected BorderTextViews videoPlayerTextView;

    public NetVideoPlayHolder(NetVideoPlayActivity act) {
        videoPlayActivity = act;
        findViews();
    }

    /**
     * @param @rootLayout
     */
    void findViews() {
        // Video article control
        playControlLayout = (LinearLayout) videoPlayActivity
                .findViewById(R.id.net_video_suspension_layout);
        musicLayout = (LinearLayout) videoPlayActivity
                .findViewById(R.id.musiclayout);
        // Button control
        bt_videoPre = (ImageView) videoPlayActivity
                .findViewById(R.id.video_previous);
        bt_videoPlay = (ImageView) videoPlayActivity
                .findViewById(R.id.net_video_play);
        bt_videoRewind = (ImageView) videoPlayActivity
                .findViewById(R.id.net_video_rewind);
        bt_videoWind = (ImageView) videoPlayActivity
                .findViewById(R.id.net_video_wind);
        bt_videoNext = (ImageView) videoPlayActivity
                .findViewById(R.id.video_next);
        bt_videoTime = (ImageView) videoPlayActivity
                .findViewById(R.id.net_video_time);
        bt_videoList = (ImageView) videoPlayActivity
                .findViewById(R.id.net_video_list);
        current_time_video = (TextView) videoPlayActivity
                .findViewById(R.id.net_control_timer_current);
        total_time_video = (TextView) videoPlayActivity
                .findViewById(R.id.net_control_timer_total);
        videoSeekBar = (SeekBar) videoPlayActivity
                .findViewById(R.id.net_progress);
        video_name = (TextView) videoPlayActivity
                .findViewById(R.id.net_video_name_display);
        video_playSpeed = (TextView) videoPlayActivity
                .findViewById(R.id.net_video_play_speed_display);
        mVideoPlayView = (VideoPlayView) videoPlayActivity
                .findViewById(R.id.netVideoPlayerSurfaceView);
        videoPlayerTextView = (BorderTextViews) videoPlayActivity
                .findViewById(R.id.net_video_player_text);
    }

    public void setOnClickListener(OnClickListener listener) {
        if (listener != null) {
            bt_videoPre.setOnClickListener(listener);
            bt_videoPlay.setOnClickListener(listener);
            bt_videoRewind.setOnClickListener(listener);
            bt_videoWind.setOnClickListener(listener);
            bt_videoTime.setOnClickListener(listener);
            bt_videoList.setOnClickListener(listener);
            bt_videoNext.setOnClickListener(listener);
        }
    }

    /*
     * Treatment right
     */
    public boolean processRightKey(int keyCode, KeyEvent event) {
        switch (NetVideoPlayActivity.state) {
            case NetVideoPlayActivity.OPTION_STATE_PRE:
                NetVideoPlayActivity.state =NetVideoPlayActivity.OPTION_STATE_PLAY;
                setVideoPreSelect(false);
                if (videoPlayActivity.getIsMusicPlayer()) {
                    SetVideoPlaySelect(true, videoPlayActivity.musicPlayerIsPlaying());
                } else {
                    SetVideoPlaySelect(true, mVideoPlayView.isPlaying());
                }
                break;
//            case NetVideoPlayActivity.OPTION_STATE_REWIND:
//                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PLAY;
//                SetVideoRewindSelect(false);
//                SetVideoPlaySelect(true, mVideoPlayView.isPlaying());
//                break;
            case NetVideoPlayActivity.OPTION_STATE_PLAY:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_NEXT;
                if (videoPlayActivity.getIsMusicPlayer()) {
                    SetVideoPlaySelect(false, videoPlayActivity.musicPlayerIsPlaying());
                } else {
                    SetVideoPlaySelect(false, mVideoPlayView.isPlaying());
                }
                setVideoNextSelect(true);
                break;
//            case NetVideoPlayActivity.OPTION_STATE_WIND:
//                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_NEXT;
//                SetVideoWindSelect(false);
//                setVideoNextSelect(true);
//                break;
            case NetVideoPlayActivity.OPTION_STATE_NEXT:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_TIME;
                setVideoNextSelect(false);
                SetVideoTimeSelect(true);
                break;
            case NetVideoPlayActivity.OPTION_STATE_TIME:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_LIST;
                SetVideoTimeSelect(false);
                setVideoListSelect(true);
                break;
            case NetVideoPlayActivity.OPTION_STATE_LIST:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PRE;
                setVideoListSelect(false);
                setVideoPreSelect(true);
                break;
        }
        return true;
    }

    /*
     * Treatment left
     */
    public boolean processLeftKey(int keyCode, KeyEvent event) {
        switch (NetVideoPlayActivity.state) {
            case NetVideoPlayActivity.OPTION_STATE_PRE:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_LIST;
                setVideoPreSelect(false);
                setVideoListSelect(true);
                break;
//            case NetVideoPlayActivity.OPTION_STATE_REWIND:
//                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PRE;
//                SetVideoRewindSelect(false);
//                setVideoPreSelect(true);
//                break;
            case NetVideoPlayActivity.OPTION_STATE_PLAY:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PRE;
                if (videoPlayActivity.getIsMusicPlayer()) {
                    SetVideoPlaySelect(false, videoPlayActivity.musicPlayerIsPlaying());
                } else {
                    SetVideoPlaySelect(false, mVideoPlayView.isPlaying());
                }
                setVideoPreSelect(true);
                break;
//            case NetVideoPlayActivity.OPTION_STATE_WIND:
//                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PLAY;
//                SetVideoWindSelect(false);
//                SetVideoPlaySelect(true, mVideoPlayView.isPlaying());
//                break;
            case NetVideoPlayActivity.OPTION_STATE_NEXT:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PLAY;
                setVideoNextSelect(false);
                if (videoPlayActivity.getIsMusicPlayer()) {
                    SetVideoPlaySelect(true, videoPlayActivity.musicPlayerIsPlaying());
                } else {
                    SetVideoPlaySelect(true, mVideoPlayView.isPlaying());
                }
                break;
            case NetVideoPlayActivity.OPTION_STATE_TIME:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_NEXT;
                SetVideoTimeSelect(false);
                setVideoNextSelect(true);
                break;
            case NetVideoPlayActivity.OPTION_STATE_LIST:
                NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_TIME;
                setVideoListSelect(false);
                SetVideoTimeSelect(true);
                break;
        }
        return true;
    }

    // Set broadcast the speed of the display
    public void setPlaySpeed(int speed) {
        if (speed < 64 && speed > -64) {
            String str;
            str = String.format(
                    "*%d "
                            + videoPlayActivity
                            .getString(R.string.x_times_speed), speed);
            video_playSpeed.setText(str);
        }
    }

    /**
     * Set video name
     */
    public void setVideoName(String videoName) {
        video_name.setText(videoPlayActivity.getString(R.string.playing) + ":"
                + videoName);
    }

    public void reset() {
        /*
         * current_time_video.setText(Tools.formatDuration(0));
         * total_time_video.setText(Tools.formatDuration(0));
         */
        current_time_video.setText("--:--:--");
        total_time_video.setText("--:--:--");
        videoSeekBar.setProgress(0);
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

    public void setBgPhotoVisible(boolean bVisible) {
       if (bVisible) {
           musicLayout.setVisibility(View.VISIBLE);
       } else {
           musicLayout.setVisibility(View.GONE);
       }
    }

    public void setVideoBtnVisible(boolean bVisible) {
        if (bVisible) {
            bt_videoRewind.setVisibility(View.VISIBLE);
            //bt_videoTime.setVisibility(View.VISIBLE);
            bt_videoWind.setVisibility(View.VISIBLE);
        } else {
            bt_videoRewind.setVisibility(View.GONE);
            //bt_videoTime.setVisibility(View.GONE);
            bt_videoWind.setVisibility(View.GONE);
        }
    }

    public void SetVideoPlaySelect(boolean bSelect, boolean isPlaying) {
        bt_videoPlay.setFocusable(bSelect);
        if (bSelect) {
            setAllUnSelect(isPlaying);
            NetVideoPlayActivity.state = NetVideoPlayActivity.OPTION_STATE_PLAY;
            if (isPlaying)
                bt_videoPlay
                        .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
            else
                bt_videoPlay
                        .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
        } else {
            if (isPlaying)
                bt_videoPlay
                        .setBackgroundResource(R.drawable.player_icon_pause);
            else
                bt_videoPlay.setBackgroundResource(R.drawable.player_icon_play);
        }
    }

    public void SetVideoTimeSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoTime.setFocusable(true);
            bt_videoTime
                    .setBackgroundResource(R.drawable.ic_player_icon_time_focus);
        } else {
            bt_videoTime.setFocusable(false);
            bt_videoTime.setBackgroundResource(R.drawable.player_icon_time);
        }
    }

    public void setVideoListSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoList.setFocusable(true);
            bt_videoList
                    .setBackgroundResource(R.drawable.ic_player_icon_list_focus);
        } else {
            bt_videoList.setFocusable(false);
            bt_videoList.setBackgroundResource(R.drawable.player_icon_list);
        }
    }

    public void SetVideoWindSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoWind.setFocusable(true);
            bt_videoWind
                    .setBackgroundResource(R.drawable.ic_player_icon_wind_focus);
        } else {
            bt_videoWind.setFocusable(false);
            bt_videoWind.setBackgroundResource(R.drawable.player_icon_wind);
        }
    }

    public void setVideoNextSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoNext.setFocusable(true);
            bt_videoNext
                    .setBackgroundResource(R.drawable.ic_player_icon_next_focus);
        } else {
            bt_videoNext.setFocusable(false);
            bt_videoNext.setBackgroundResource(R.drawable.player_icon_next);
        }
    }

    public void SetVideoRewindSelect(boolean bSelect) {
        if (bSelect) {
            bt_videoRewind.setFocusable(true);
            bt_videoRewind
                    .setBackgroundResource(R.drawable.ic_player_icon_rewind_focus);
        } else {
            bt_videoRewind.setFocusable(false);
            bt_videoRewind.setBackgroundResource(R.drawable.player_icon_rewind);
        }
    }

    // All buttons don't choose
    public void setAllUnSelect(boolean isPlaying) {
        setVideoPreSelect(false);
        SetVideoRewindSelect(false);
        SetVideoPlaySelect(false, isPlaying);
        SetVideoWindSelect(false);
        setVideoNextSelect(false);
        SetVideoTimeSelect(false);
        setVideoListSelect(false);
    }

    public VideoPlayView getPlayerView() {
        return mVideoPlayView;
    }

    public SeekBar getProgressBar() {
        return videoSeekBar;
    }

    public void setSubTitleText(String str) {
        if (videoPlayerTextView != null) {
            videoPlayerTextView.setText(str);
        }
    }
}
