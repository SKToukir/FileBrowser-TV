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
package com.walton.filebrowser.ui.music;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.visualizer.WaveLoadingView;
import com.walton.filebrowser.ui.visualizer.WaveLoadingView2;
import com.walton.filebrowser.ui.visualizer.WaveLoadingView3;
import com.walton.filebrowser.util.GridViewTV;

public class MusicPlayerViewHolder {

    protected WaveLoadingView mWaveLoadingView;
    protected WaveLoadingView2 mWaveLoadingView2;
    protected WaveLoadingView3 mWaveLoadingView3;

    private MusicPlayerActivity mMusicPlayerActivity;
    private RelativeLayout rootLayout;
    protected ScrollableViewGroup scrollView;
    protected LinearLayout listLayout;
    protected ListView musicListView;
    protected TextView playTime;
    protected SeekBar seekBar;
    protected RepeatingImageButton bt_MusicPre;
    protected ImageView bt_MusicPlay;
    protected ImageView imgThumbnail;
    protected ImageView imgBelowMore;
    protected RepeatingImageButton bt_MusicNext;
    protected ImageView bt_MusicCir;
    protected ImageView bt_MusicList;
    protected ImageView bt_MusicLrc;
    protected ImageView bt_MusicInfo;
    protected TextView currentMusicNum;
    protected TextView current_time_tv;
    protected TextView currentMusicTitle;
    protected ImageView bt_MusicImageAlbum;
    protected TextView currentMusicArtist;
    protected TextView durationTime;
    protected LyricView mLyricView;
    protected ViewGroupHook playLayout;
    protected HorizontalGridView horizontalgridview;
    protected TextView txtSongItemCount;

    public MusicPlayerViewHolder(MusicPlayerActivity musicPlayerActivity) {
        this.mMusicPlayerActivity = musicPlayerActivity;
    }

    /**
     * Components display
     */
    protected void findView() {


        txtSongItemCount = mMusicPlayerActivity.findViewById(R.id.txtSongItemCount);

        horizontalgridview = mMusicPlayerActivity.findViewById(R.id.horizontalgridviewSubMusicItem);
        horizontalgridview.setFocusableInTouchMode(false);
        horizontalgridview.setFocusable(false);

        imgBelowMore = mMusicPlayerActivity.findViewById(R.id.imgBelowMore);
        imgThumbnail = mMusicPlayerActivity.findViewById(R.id.imgThumbnail);

        mWaveLoadingView = mMusicPlayerActivity.findViewById(R.id.waveLoadingView);
        mWaveLoadingView2 = mMusicPlayerActivity.findViewById(R.id.waveLoadingView2);
        mWaveLoadingView3 = mMusicPlayerActivity.findViewById(R.id.waveLoadingView3);

        rootLayout = (RelativeLayout) mMusicPlayerActivity
                .findViewById(R.id.music_suspension_layout2);
        scrollView = (ScrollableViewGroup) rootLayout
                .findViewById(R.id.ViewFlipper);
        scrollView.setVisibility(View.GONE);
        listLayout = (LinearLayout) scrollView.findViewById(R.id.frmList);
        musicListView = (ListView) listLayout.findViewById(R.id.PlayList);
        playLayout = (ViewGroupHook) scrollView.findViewById(R.id.frmMain);
        bt_MusicPre = (RepeatingImageButton) rootLayout
                .findViewById(R.id.music_previous);
        bt_MusicPlay = (ImageView) rootLayout.findViewById(R.id.music_play);
        bt_MusicNext = (RepeatingImageButton) rootLayout
                .findViewById(R.id.music_next);
        bt_MusicCir = (ImageView) rootLayout.findViewById(R.id.music_cir);
        bt_MusicList = (ImageView) rootLayout.findViewById(R.id.music_list);
        bt_MusicInfo = (ImageView) rootLayout.findViewById(R.id.music_info);
        bt_MusicImageAlbum = (ImageView) rootLayout
                .findViewById(R.id.music_image_album);
        currentMusicArtist = (TextView) rootLayout
                .findViewById(R.id.music_current_artist);
        currentMusicTitle = (TextView) rootLayout
                .findViewById(R.id.music_current_name);
        currentMusicNum = (TextView) rootLayout
                .findViewById(R.id.music_current_num);
        playTime = (TextView) rootLayout.findViewById(R.id.txtLapse);
        durationTime = (TextView) rootLayout.findViewById(R.id.txtDuration);
        playTime = (TextView) rootLayout.findViewById(R.id.txtLapse);
        // mVisualizerView =
        // (VisualizerView)rootLayout.findViewById(R.id.mainbackground);
        mLyricView = (LyricView) mMusicPlayerActivity
                .findViewById(R.id.LyricShow);
        seekBar = (SeekBar) rootLayout.findViewById(R.id.skbGuage);

    }
}
