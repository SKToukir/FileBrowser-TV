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

import android.app.AlertDialog;
import com.walton.filebrowser.R;
import android.os.Handler;
import android.view.KeyEvent;
import android.content.DialogInterface;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import android.util.Log;

public class DivxController {
    private VideoPlayerActivity content;
    private VideoPlayerViewHolder holder;
    private final int TITLE = 0;
    private final int EDITION = 1;
    public DivxController(VideoPlayerActivity vpa, VideoPlayerViewHolder vpvh) {
        content = vpa;
        holder = vpvh;
    }

    public void keyStopProcess(KeyEvent event, Handler handler) {
        long lastEventTime = 0;
        long margin = event.getDownTime() - lastEventTime;
        lastEventTime = event.getDownTime();
        if (margin < 300) {
            Tools.setResumePlayState(0);
        } else {
            Tools.setResumePlayState(1);
        }
        holder.mVideoPlayViewOne.stopPlayer();
        holder.mVideoPlayViewOne.setPlayerCallbackListener(null);
        handler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
    }

    public void errorMsgProcess(int msg) {
        if (msg == -5007) {
            int Y = holder.getPlayerView().getMediaParam(
                    Constants.KEY_PARAMETER_GET_DIVX_DRM_IS_RENTAL_FILE + 2);
            AlertDialog.Builder builder = new AlertDialog.Builder(content);
            builder.setMessage(content.getResources().getString(R.string.divx_vod_drm_rental)
                    + "\n" + Y + "/" + Y
                    + content.getResources().getString(R.string.divx_vod_drm_views)
                    + "\n" + "\n"
                    + content.getResources().getString(R.string.divx_vod_drm_rental_expired) + "\n");
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    holder.getPlayerView().stopPlayback();
                    content.dismissProgressDialog();
                    content.finish();
                }});
            builder.show();
        } else if (msg == -5008) {
            AlertDialog.Builder builder = new AlertDialog.Builder(content);
            builder.setMessage(content.getResources().getString(R.string.divx_vod_drm_authorization_error));
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    holder.getPlayerView().stopPlayback();
                    content.dismissProgressDialog();
                    content.finish();
                }});
            builder.show();
        }
    }

    public void preparedProcess(int id) {
        if (holder.getPlayerView(id).bResumePlay) {
            Log.i("preparedProcess","bResumePlay:"+holder.getPlayerView(id).bResumePlay);
            Log.i("preparedProcess","content.breakPoint:"+content.breakPoint);
            Log.i("preparedProcess","content.isBreakPointPlay:"+content.isBreakPointPlay);
            if (content.breakPoint>0 && content.isBreakPointPlay) {
                Log.i("preparedProcess","bResumePlay-->setCurTitleEdition");
                int titleEdition[] = new int[]{0,0};
                titleEdition[TITLE] = content.getTitleByFileName();
                titleEdition[EDITION] = content.getEditionByFileName();
                content.setCurTitleEdition(titleEdition);
                content.setCurSubtitleTrack(content.getSubtitleTrackByFileName());
                content.setCurAudioTrack(content.getAudioTrackByFileName());
                holder.getPlayerView(id).seekTo(content.breakPoint);
            } else {
                content.clearTtEtAdStProperty();
            }
            holder.getPlayerView(id).checkPreviousVideoIfFullStop();
            holder.getPlayerView(id).start();
            content.initPlayer(id);
        } else {
            int bRent = content.isRentalFile();
            if (bRent > 0) {
                Log.i("preparedProcess","This is a rental file!");
                int Y = holder.getPlayerView(id).getMediaParam(
                        Constants.KEY_PARAMETER_GET_DIVX_DRM_IS_RENTAL_FILE + 1);
                int X = holder.getPlayerView(id).getMediaParam(
                        Constants.KEY_PARAMETER_GET_DIVX_DRM_IS_RENTAL_FILE + 2);
                final int VID = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(content);
                builder.setMessage(content.getResources().getString(R.string.divx_vod_drm_rental)
                        + "\n" + X + "/" + Y
                        + content.getResources().getString(R.string.divx_vod_drm_views)
                        + "\n" + "\n"
                        + content.getResources().getString(R.string.divx_vod_drm_rental_confirm)
                        + "\n");
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        holder.getPlayerView(VID).checkPreviousVideoIfFullStop();
                        holder.getPlayerView(VID).setResumePlayState();
                        content.clearTtEtAdStProperty();
                        holder.getPlayerView(VID).start();
                        content.initPlayer(VID);
                    }});
                builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        holder.getPlayerView(VID).stopPlayback();
                        content.dismissProgressDialog();
                        content.finish();
                    }});
                builder.show();
            } else {
                Log.i("preparedProcess","content.breakPoint:"+content.breakPoint);
                Log.i("preparedProcess","content.isBreakPointPlay:"+content.isBreakPointPlay);
                if (content.breakPoint>0 && content.isBreakPointPlay) {
                    Log.i("preparedProcess","bResumePlay-->setCurTitleEdition");
                    int titleEdition[] = new int[]{0,0};
                    titleEdition[TITLE] = content.getTitleByFileName();
                    titleEdition[EDITION] = content.getEditionByFileName();
                    content.setCurTitleEdition(titleEdition);
                    content.setCurSubtitleTrack(content.getSubtitleTrackByFileName());
                    content.setCurAudioTrack(content.getAudioTrackByFileName());
                    holder.getPlayerView(id).seekTo(content.breakPoint);
                } else {
                    content.clearTtEtAdStProperty();
                }
                holder.getPlayerView(id).setResumePlayState();
                holder.getPlayerView(id).checkPreviousVideoIfFullStop();
                holder.getPlayerView(id).start();
                content.initPlayer(id);
            }
        }
    }
}
