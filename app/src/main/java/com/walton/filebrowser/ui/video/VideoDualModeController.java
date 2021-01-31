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
package com.walton.filebrowser.ui.video;
import android.content.res.Resources;
import android.os.Message;
import android.view.View;
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 *
 * Provide controller to dualMode.
 *
 * @date 2015-04
 *
 * @author andrew.wang
 *
 */
    public class VideoDualModeController{

        private final static String TAG= "VideoDualModeController";

        private VideoPlayerActivity context;

        private VideoPlayerViewHolder videoPlayerHolder;

        private Resources resources;

        public VideoDualModeController(VideoPlayerActivity context ,VideoPlayerViewHolder videoPlayerHolder,
                Resources resources) {
            this.context = context;
            this.videoPlayerHolder =videoPlayerHolder;
            this.resources=resources;
        }
    public void switchDualMode() {
        // When switch Dual Mode, should know current video's 3D mode,
        // so add getDisplayFormat to get video's display format.
        context.getDisplayFormat(context, 1);
        String hwName = Tools.getHardwareName();
        if (!videoPlayerHolder.getDualVideoMode() && context.isThreeDMode()) {
            // show toast
            context.showToastTip(resources.getString(R.string.can_not_open_dual_in_3d_mode));
        } else if (!videoPlayerHolder.getDualVideoMode() && context.isHRDMode()) {
            // show toast
            context.showToastTip(resources.getString(R.string.can_not_open_dual_in_hdr_mode));
        } else if (!videoPlayerHolder.getDualVideoMode() && context.isDolbyHDRMode()) {
            // show toast
            context.showToastTip(resources.getString(R.string.can_not_open_dual_in_dolbyhdr_mode));
        } else if (ThreeDimentionControl.getInstance().isPIPMode()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_pip));
        } else if (!videoPlayerHolder.getDualVideoMode()
                    && context.isVideoSize_4K(1)
                    && !"kano".equals(hwName)) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_4kVideo));
        } else if (Tools.isThumbnailModeOn()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_Thumbnail_Video));
        } else if (!Tools.isSupportDualDecode()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_this_platform));
        } else if (Tools.isRotateModeOn()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_rotate_mode));
        } else{
            if (!videoPlayerHolder.getDualVideoMode()) {
                videoPlayerHolder.showVideoFocus(false);
                videoPlayerHolder.currentDualMode = videoPlayerHolder.DUAL_MODE_LEFT_RIGHT;
                videoPlayerHolder.switchFocusedView();
                context.showVideoListDialog();
            } else {
                int time = 0;
                if (videoPlayerHolder.getPlayerView(1) != null) {
                    time = videoPlayerHolder.getPlayerView(1).getCurrentPosition();
                }
                //videoPlayerHolder.isDualVideoClosed = true;
                if (!videoPlayerHolder.isSeekable(1)
                        || context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1] != null) {
                    context.localPauseFromDualSwitch(1, false);
                    if (videoPlayerHolder.getViewId() == 2) {
                        switchDualFocus();
                    }
                    videoPlayerHolder.openOrCloseDualDecode(false);
                    context.localResumeFromDualSwitch(false);
                    if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1] != null ) {
                        if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1].isABplaying()) {
                            Constants.abFlag = true;
                            if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1].aFlag) {
                                Constants.aFlag = true;
                            }
                        }
                    }
                } else {
                    videoPlayerHolder.setSeekVar(2, true);
                    if (videoPlayerHolder.getViewId() == 2) {
                        switchDualFocus();
                    }
                    videoPlayerHolder.openOrCloseDualDecode(false);
                    //seekBarListener.onProgressChanged(videoPlayerHolder.videoSeekBar, time, true);
                    context.isPlaying = videoPlayerHolder.getPlayerView().isPlaying();
                }

                if (Tools.isVideoSWDisplayModeOn()) {
                    // After close dual decode, if video display not by hardware, should set video display aspect ratio
                    // to adapt to the video(S/W) size.
                    if (!context.getVideoPlayHolder().getPlayerView(1).isVideoDisplayByHardware()) {
                        context.setVideoDisplayRotate90(1);
                    }
                }
                if (!Tools.isNativeAudioModeOn()) {
                    Tools.setHpBtMainSource(true);
                    videoPlayerHolder.getPlayerView().setDualAudioOn(false);
                }
                videoPlayerHolder.setSubtitleSize(videoPlayerHolder.getViewId(),
                       VideoPlayerViewHolder.PIP_DUAL_ClOSED_STATE);
            }
        }
    }
    public void switchDualFocus() {

        if (Tools.isNativeAudioModeOn()) {
            if (videoPlayerHolder.getViewId() == 1) {
                if (videoPlayerHolder.getPlayerView(1)!=null) {
                    videoPlayerHolder.getPlayerView(1).setVoice(false);
                }
                if (videoPlayerHolder.getPlayerView(2)!=null) {
                    videoPlayerHolder.getPlayerView(2).setVoice(true);
                }

            } else {
                if (videoPlayerHolder.getPlayerView(1)!=null) {
                    videoPlayerHolder.getPlayerView(1).setVoice(true);
                }
                if (videoPlayerHolder.getPlayerView(2)!=null) {
                    videoPlayerHolder.getPlayerView(2).setVoice(false);
                }
            }
        }
        videoPlayerHolder.switchFocusedView();
        //getVideoPlayHolder().setAudioProcessor(getVideoPlayHolder().getViewId());
        String videoName = null;
        int duration = 0;
        int pos = 0;
        int speed = 1;
        int id = videoPlayerHolder.getViewId() - 1;
        if (context.isPrepared()) {
            videoName = context.mVideoPlayList.get(context.video_position[id]).getName();
            duration = (int) videoPlayerHolder.getPlayerView().getDuration();
            pos = context.video_position[id] + 1;
            speed = videoPlayerHolder.getPlayerView().getPlayMode();
            if (speed == 0)
                speed = 1;
        }
        videoPlayerHolder.refreshControlInfo(videoName, speed, pos, context.mVideoPlayList.size(), duration);

        if (context.videoPlayAbDialog[id] != null) {
            Message msg = new Message();
            msg.what = 2;
            msg.arg1 = 0;
            if (context.videoPlayAbDialog[id].bFlag && context.videoPlayAbDialog[id].sharedata != null) {
                msg.arg1 = 1;
            }
            context.videoPlayAbDialog[id].mHandler.sendMessage(msg);
        } else {
            videoPlayerHolder.bt_playA.setVisibility(View.INVISIBLE);
            videoPlayerHolder.bt_playB.setVisibility(View.INVISIBLE);
        }

        context.checkABCycle(videoPlayerHolder.getViewId());
        if (context.mChooseTimePlayDialog != null) {
            context.mChooseTimePlayDialog.setVariable(!videoPlayerHolder.isSeekable(videoPlayerHolder.getViewId()));
        }
        context.videoHandler.removeMessages(context.SEEK_POS);
        context.videoHandler.sendEmptyMessageDelayed(context.SEEK_POS, 1000);
    }
}


