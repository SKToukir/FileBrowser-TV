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
package com.walton.filebrowser.ui.music;

import java.io.RandomAccessFile;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.Gravity;

import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.leanback.widget.HorizontalGridView;

import com.walton.filebrowser.util.ToastFactory;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;

public class MusicPlayerListener {
    private static final String TAG = "MusicPlayerListener";
    private MusicPlayerActivity mMusicPlayerActivity;
    private MusicPlayerViewHolder mMusicPlayerViewHolder;
    public static final int OPTION_STATE_PRE = 0x1;
    public static final int OPTION_STATE_PLAY = 0x2;
    public static final int OPTION_STATE_NEXT = 0x3;
    public static final int OPTION_STATE_CIR = 0x4;
    public static final int OPTION_STATE_LIST = 0x5;
    public static final int OPTION_STATE_LRC = 0x6;
    public static final int OPTION_STATE_INFO = 0x7;
    public static final int OPTION_STATE_SELECT_MUSIC_SUBLIST = 0x9;
    // playMode
    public static final int SINGE = 0;
    public static final int REPEAT = 1;
    public static final int ORDER = 2;
    public static int currentPlayMode = 2;
    private static final String LOCALMM = "localMM";
    private static final String PLAYMODE = "playMode";
    private MusicDetailInfoDialog mMusicDetailInfoDialog;
    private MusicListDialog mMusicListDialog;
    // Music is in the play
    protected static boolean isPlaying = true;

    public MusicPlayerListener(MusicPlayerActivity musicPlayerActivity,
                               MusicPlayerViewHolder musicPlayerViewHolder) {
        this.mMusicPlayerActivity = musicPlayerActivity;
        this.mMusicPlayerViewHolder = musicPlayerViewHolder;
    }

    protected void addMusicListener() {
        mMusicPlayerViewHolder.seekBar.setClickable(true);
        mMusicPlayerViewHolder.seekBar
                .setOnSeekBarChangeListener(seekBarChageListener);
        mMusicPlayerActivity.musicPlayHandle
                .sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
        mMusicPlayerViewHolder.bt_MusicPre
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_PLAY;
//                        setViewDefault();
                        mMusicPlayerViewHolder.bt_MusicPlay
                                .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                        mMusicPlayerViewHolder.bt_MusicPre
                                .setBackgroundResource(R.drawable.player_icon_previous);
                        mMusicPlayerActivity.isNextMusic = false;
                        mMusicPlayerActivity.clickable = true;
                        mMusicPlayerActivity.musicPlayHandle
                                .removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                        mMusicPlayerActivity.processPlayCompletion();
                        isPlaying = true;

                        // toukir
                        mMusicPlayerActivity.notifySubList();

                        mMusicPlayerViewHolder.horizontalgridview.setFocusable(false);
                        mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                        mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    }
                });
        mMusicPlayerViewHolder.bt_MusicPlay
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_PLAY;
                        registerListeners();
                    }
                });
        mMusicPlayerViewHolder.bt_MusicNext
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_PLAY;
//                        setViewDefault();
                        mMusicPlayerViewHolder.bt_MusicPlay
                                .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                        mMusicPlayerViewHolder.bt_MusicNext
                                .setBackgroundResource(R.drawable.player_icon_next);
                        mMusicPlayerActivity.isNextMusic = true;
                        mMusicPlayerActivity.clickable = true;
                        mMusicPlayerActivity.musicPlayHandle
                                .removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                        mMusicPlayerActivity.processPlayCompletion();
                        isPlaying = true;

                        // toukir
                        mMusicPlayerActivity.notifySubList();


                        mMusicPlayerViewHolder.horizontalgridview.setFocusable(false);
                        mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                        mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    }
                });
        mMusicPlayerViewHolder.bt_MusicCir
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_CIR;
                        registerListeners();
                    }
                });
        mMusicPlayerViewHolder.bt_MusicList
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_LIST;
                        registerListeners();
                    }
                });
        mMusicPlayerViewHolder.bt_MusicInfo
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicPlayerActivity.state = OPTION_STATE_INFO;
                        registerListeners();
                    }
                });

    }

    private OnSeekBarChangeListener seekBarChageListener = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int position = seekBar.getProgress();
            int duration = MusicPlayerActivity.countTime;
            if (duration > 0) {
                mMusicPlayerActivity.seekTo(position);
            } else {
                String strMessage = "This operation is not supported !";
                ToastFactory.getToast(mMusicPlayerActivity, strMessage, Gravity.CENTER).show();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    protected void drapTop() {
        switch (MusicPlayerActivity.state) {
            case OPTION_STATE_SELECT_MUSIC_SUBLIST:
                int playModeL;
                playModeL = currentPlayMode;
                mMusicPlayerViewHolder.horizontalgridview.setFocusable(true);

                mMusicPlayerViewHolder.bt_MusicPre.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicNext.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicList.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(false);

                mMusicPlayerViewHolder.bt_MusicPre
                        .setBackgroundResource(R.drawable.player_icon_previous);

                mMusicPlayerViewHolder.bt_MusicNext
                        .setBackgroundResource(R.drawable.player_icon_next);
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.player_icon_infor);

                if (playModeL == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.ic_player_icon_singles);
                } else if (playModeL == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random);
                } else if (playModeL == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop);
                }

                if (MusicPlayerActivity.currentPlayStatus == MusicPlayerActivity.PLAY_PLAY) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_pause);

                } else if (MusicPlayerActivity.currentPlayStatus == MusicPlayerActivity.PLAY_PAUSE) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }
                break;
        }
    }

    public void drapDown() {

        mMusicPlayerViewHolder.horizontalgridview.setFocusable(false);
        mMusicPlayerViewHolder.horizontalgridview.setFocusableInTouchMode(false);

        mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
        mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
        if (MusicPlayerActivity.currentPlayStatus == MusicPlayerActivity.PLAY_PLAY) {
            mMusicPlayerViewHolder.bt_MusicPlay
                    .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);

        } else if (MusicPlayerActivity.currentPlayStatus == MusicPlayerActivity.PLAY_PAUSE) {
            mMusicPlayerViewHolder.bt_MusicPlay
                    .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
        }
    }

    /**
     * The remote control shift to the left, the focus switched
     */
    protected void drapLeft() {
        int playModeL;
        switch (MusicPlayerActivity.state) {

            case OPTION_STATE_PRE:
                MusicPlayerActivity.state = OPTION_STATE_INFO;//OPTION_STATE_INFO;
                mMusicPlayerViewHolder.bt_MusicPre.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicPre
                        .setBackgroundResource(R.drawable.player_icon_previous);
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicInfo.requestFocus();
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.ic_player_icon_infor_focus);

                break;
            case OPTION_STATE_PLAY:

                MusicPlayerActivity.state = OPTION_STATE_PRE;
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_pause);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }
                mMusicPlayerViewHolder.bt_MusicPre.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicPre.requestFocus();
                mMusicPlayerViewHolder.bt_MusicPre
                        .setBackgroundResource(R.drawable.ic_player_icon_previous_focus);

                break;
            case OPTION_STATE_NEXT:

                MusicPlayerActivity.state = OPTION_STATE_PLAY;
                mMusicPlayerViewHolder.bt_MusicNext.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicNext
                        .setBackgroundResource(R.drawable.player_icon_next);
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                    mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                    mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                }

                break;
            case OPTION_STATE_CIR:

                MusicPlayerActivity.state = OPTION_STATE_NEXT;
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(false);
                playModeL = currentPlayMode;
                if (playModeL == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_singles);
                } else if (playModeL == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random);
                } else if (playModeL == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop);
                }
                mMusicPlayerViewHolder.bt_MusicNext.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicNext.requestFocus();
                mMusicPlayerViewHolder.bt_MusicNext
                        .setBackgroundResource(R.drawable.ic_player_icon_next_focus);

                break;
            case OPTION_STATE_LIST:

                MusicPlayerActivity.state = OPTION_STATE_CIR;
                mMusicPlayerViewHolder.bt_MusicList.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.player_icon_list);
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicCir.requestFocus();
                playModeL = currentPlayMode;
                if (playModeL == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
                } else if (playModeL == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random_focus);
                } else if (playModeL == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop_focus);
                }

                break;
            case OPTION_STATE_INFO:

                MusicPlayerActivity.state = OPTION_STATE_CIR;
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.player_icon_infor);
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicCir.requestFocus();
                playModeL = currentPlayMode;
                if (playModeL == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
                } else if (playModeL == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random_focus);
                } else if (playModeL == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop_focus);
                }
                break;

        }
    }


    /**
     * The remote control right shift, the focus switched
     */
    protected void drapRight() {
        int playModeR;

        switch (MusicPlayerActivity.state) {
            case OPTION_STATE_PRE:

                MusicPlayerActivity.state = OPTION_STATE_PLAY;
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                    mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                    mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                }
                mMusicPlayerViewHolder.bt_MusicPre.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicPre
                        .setBackgroundResource(R.drawable.player_icon_previous);

                break;
            case OPTION_STATE_PLAY:

                MusicPlayerActivity.state = OPTION_STATE_NEXT;
                mMusicPlayerViewHolder.bt_MusicNext.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicNext.requestFocus();
                mMusicPlayerViewHolder.bt_MusicNext
                        .setBackgroundResource(R.drawable.ic_player_icon_next_focus);
                Log.d(TAG, "OPTION_STATE_PLAY : " + isPlaying);
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_pause);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }

                break;
            case OPTION_STATE_NEXT:

                MusicPlayerActivity.state = OPTION_STATE_CIR;
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicCir.requestFocus();
                playModeR = currentPlayMode;
                if (playModeR == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
                } else if (playModeR == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random_focus);
                } else if (playModeR == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop_focus);
                }
                mMusicPlayerViewHolder.bt_MusicNext.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicNext
                        .setBackgroundResource(R.drawable.player_icon_next);

                break;
            case OPTION_STATE_CIR:

                MusicPlayerActivity.state = OPTION_STATE_INFO;
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicInfo.requestFocus();
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.ic_player_icon_infor_focus);
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(false);
                playModeR = currentPlayMode;
                if (playModeR == SINGE) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_singles);
                } else if (playModeR == REPEAT) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_random);
                } else if (playModeR == ORDER) {
                    mMusicPlayerViewHolder.bt_MusicCir
                            .setBackgroundResource(R.drawable.player_icon_loop);
                }


                break;
            case OPTION_STATE_LIST:

                MusicPlayerActivity.state = OPTION_STATE_INFO;
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicInfo.requestFocus();
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.ic_player_icon_infor_focus);
                mMusicPlayerViewHolder.bt_MusicList.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.player_icon_list);

                break;
            case OPTION_STATE_INFO:

                MusicPlayerActivity.state = OPTION_STATE_PRE;
                mMusicPlayerViewHolder.bt_MusicPre.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicPre.requestFocus();
                mMusicPlayerViewHolder.bt_MusicPre
                        .setBackgroundResource(R.drawable.ic_player_icon_previous_focus);
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.player_icon_infor);


                break;
        }
    }

    protected void drapRightDlna() {
        switch (MusicPlayerActivity.state) {
            case OPTION_STATE_PLAY:
                MusicPlayerActivity.state = OPTION_STATE_CIR;
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicCir.requestFocus();
                mMusicPlayerViewHolder.bt_MusicCir
                        .setBackgroundResource(R.drawable.player_icon_loop_focus);
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_pause);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }
                break;
            case OPTION_STATE_CIR:
                MusicPlayerActivity.state = OPTION_STATE_LIST;
                mMusicPlayerViewHolder.bt_MusicList.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicList.requestFocus();
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.ic_player_icon_list_focus);
                mMusicPlayerViewHolder.bt_MusicCir.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicCir
                        .setBackgroundResource(R.drawable.player_icon_loop);
                break;
            case OPTION_STATE_LIST:
                MusicPlayerActivity.state = OPTION_STATE_INFO;
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicInfo.requestFocus();
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.ic_player_icon_infor_focus);
                mMusicPlayerViewHolder.bt_MusicList.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.player_icon_list);
                break;
            case OPTION_STATE_INFO:
                MusicPlayerActivity.state = OPTION_STATE_PLAY;
                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
                mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
                if (isPlaying) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                }
                mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.player_icon_infor);
                break;
        }
    }

    /**
     * Initialization playback modes
     */
    public void initPlayMode() {
        int playmode = currentPlayMode;
        if (playmode == SINGE) {
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.player_icon_singles);
        } else if (playmode == REPEAT) {
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.player_icon_random);
        } else if (playmode == ORDER) {
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.player_icon_loop);
        }
    }

    /**
     * Each button response events
     */
    public void registerListeners() {
        //setViewDefault();
        switch (MusicPlayerActivity.state) {
            case OPTION_STATE_PRE:

                if (mMusicPlayerActivity.isPrepared) {
                    mMusicPlayerActivity.isPrepared = false;
                    mMusicPlayerViewHolder.bt_MusicPre
                            .setBackgroundResource(R.drawable.ic_player_icon_previous_focus);
                    mMusicPlayerActivity.preMusic();
                    isPlaying = true;
                }

                break;
            case OPTION_STATE_PLAY:
                Log.d(TAG, "OPTION_STATE_PLAY:::::pauseMusic::::::::");
                mMusicPlayerActivity.pauseMusic();
                break;
            case OPTION_STATE_NEXT:

                if (mMusicPlayerActivity.isPrepared) {
                    mMusicPlayerActivity.isPrepared = false;
                    mMusicPlayerViewHolder.bt_MusicNext
                            .setBackgroundResource(R.drawable.ic_player_icon_next_focus);
                    mMusicPlayerActivity.nextMusic();
                    isPlaying = true;
                }

                break;
            case OPTION_STATE_CIR:
            /*if (isPlaying) {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_pause);
            } else {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_play);
            }*/
//                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);

                changePlayMode();
                break;
            case OPTION_STATE_LIST:
            /*if (isPlaying) {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_pause);
            } else {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_play);
            }*/

                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                if (mMusicPlayerActivity != null) {
                    DisplayMetrics metrics = mMusicPlayerActivity.getResources().getDisplayMetrics();
                    mMusicPlayerActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    Log.i(TAG, "metrics.density:" + metrics.density);
                    if (metrics.density == 1.5) {
                        mMusicListDialog = new MusicListDialog(mMusicPlayerActivity, mMusicPlayerActivity.musicPlayHandle);
                    } else {
                        mMusicListDialog = new MusicListDialog(mMusicPlayerActivity, mMusicPlayerActivity.musicPlayHandle, R.style.dialog);
                    }

                    if (mMusicListDialog != null) {
                        mMusicPlayerActivity.pauseOrPlayPresentMusicNameMarquee();
                        mMusicListDialog.show();
                    }
                }
                mMusicPlayerViewHolder.bt_MusicList
                        .setBackgroundResource(R.drawable.ic_player_icon_list_focus);

                break;
            case OPTION_STATE_LRC:

                mMusicPlayerActivity.scrollstate = 0;
                mMusicPlayerViewHolder.scrollView
                        .setCurrentView(mMusicPlayerActivity.scrollstate);

                break;
            case OPTION_STATE_INFO:
            /*if (isPlaying) {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_pause);
            } else {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.player_icon_play);
            }*/

                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);
                showMusicInfo(MusicPlayerActivity.currentPosition);
                mMusicPlayerViewHolder.bt_MusicInfo
                        .setBackgroundResource(R.drawable.ic_player_icon_infor_focus);

                break;
        }
    }

    protected void setViewDefault() {
        mMusicPlayerViewHolder.bt_MusicNext
                .setBackgroundResource(R.drawable.player_icon_next);
        mMusicPlayerViewHolder.bt_MusicNext.setFocusable(false);
        mMusicPlayerViewHolder.bt_MusicPre
                .setBackgroundResource(R.drawable.player_icon_previous);
        mMusicPlayerViewHolder.bt_MusicPre.setFocusable(false);
        initPlayMode();
        mMusicPlayerViewHolder.bt_MusicCir.setFocusable(false);
        mMusicPlayerViewHolder.bt_MusicList
                .setBackgroundResource(R.drawable.player_icon_list);
        mMusicPlayerViewHolder.bt_MusicList.setFocusable(false);
        mMusicPlayerViewHolder.bt_MusicInfo
                .setBackgroundResource(R.drawable.player_icon_infor);
        mMusicPlayerViewHolder.bt_MusicInfo.setFocusable(false);
        // Dismiss file information dialog box
        if (mMusicDetailInfoDialog != null
                && mMusicDetailInfoDialog.isShowing()) {
            mMusicDetailInfoDialog.dismiss();
        }
    }

    /**
     * Playback modes change
     */
    protected void changePlayMode() {
        if (currentPlayMode == SINGE) {
            currentPlayMode = REPEAT;
            setPlayMode(REPEAT);
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.player_icon_random_focus);
        } else if (currentPlayMode == REPEAT) {
            currentPlayMode = ORDER;
            setPlayMode(ORDER);
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.player_icon_loop_focus);
        } else if (currentPlayMode == ORDER) {
            currentPlayMode = SINGE;
            setPlayMode(SINGE);
            mMusicPlayerViewHolder.bt_MusicCir
                    .setBackgroundResource(R.drawable.ic_player_icon_singles_focus);
        }
    }

    /**
     * Set the current playback modes
     *
     * @param mode
     */
    private void setPlayMode(int mode) {
        SharedPreferences preference = mMusicPlayerActivity
                .getSharedPreferences(LOCALMM, MusicPlayerActivity.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(PLAYMODE, mode);
        editor.commit();
    }

    /**
     * Show the current information songs
     */
    protected void setSongsContent(List<BaseData> musicList, int currentPosition) {

        Log.i(TAG, "setSongsContent currentPosition:" + currentPosition);
        if (musicList == null || musicList.size() < 1) {
            Log.i(TAG, "setSongsContent musicList:" + musicList + " musicList.size():" + musicList.size());
            return;
        }
        if (currentPosition < 0 || currentPosition > (musicList.size() - 1)) {
            Log.i(TAG, "setSongsContent currentPosition:" + currentPosition + " musicList.size():" + musicList.size());
            return;
        }
        mMusicPlayerViewHolder.currentMusicArtist.setText("   ");
        if (musicList.get(currentPosition).getArtist() != null) {
            mMusicPlayerViewHolder.currentMusicArtist
                    .setText(mMusicPlayerActivity.getResources().getString(
                            R.string.singer)
                            + dealWithMp3Messy(musicList.get(currentPosition)).getArtist());
            //+ musicList.get(currentPosition).getArtist());
        }
        if (musicList.get(currentPosition).getName() != null) {
//            mMusicPlayerViewHolder.currentMusicTitle
//                    .setText(mMusicPlayerActivity.getResources().getString(
//                            R.string.current_song)
//                            + musicList.get(currentPosition).getName());
            mMusicPlayerViewHolder.currentMusicTitle
                    .setText(musicList.get(currentPosition).getName());
        } else {
            String name = musicList.get(currentPosition).getPath();
            mMusicPlayerViewHolder.currentMusicTitle
                    .setText(mMusicPlayerActivity.getResources().getString(
                            R.string.current_song)
                            + name.substring(name.lastIndexOf("/") + 1,
                            name.length()));
        }
        mMusicPlayerViewHolder.currentMusicNum.setText((currentPosition + 1)
                + "/" + musicList.size());

    }

    /**
     * There is music show detailed information
     */
    private void showMusicInfo(int currentPosition) {
        if (currentPosition < MusicPlayerActivity.musicList.size()
                && currentPosition >= 0) {
            BaseData music = MusicPlayerActivity.musicList.get(currentPosition);
            //music = dealWithMp3Messy(music) ;
            mMusicPlayerActivity.pauseOrPlayPresentMusicNameMarquee();
            mMusicDetailInfoDialog = new MusicDetailInfoDialog(mMusicPlayerActivity, R.style.dialog, music);
            mMusicDetailInfoDialog.show();
        }
    }

    /**
     * Update playlist dialog focus position.
     */
    protected void updateListViewSelection() {

        mMusicPlayerViewHolder.horizontalgridview.smoothScrollToPosition(MusicPlayerActivity.currentPosition);

//        if (mMusicListDialog != null) {
//            mMusicListDialog.setSelection(MusicPlayerActivity.currentPosition);
//        }
    }

    private synchronized BaseData dealWithMp3Messy(BaseData currentMusic) {
        String contry = Locale.getDefault().getCountry();
        if ((!contry.equalsIgnoreCase("CN"))
                && (!contry.equalsIgnoreCase("HK"))
                && (!contry.equalsIgnoreCase("TW"))) {
            return currentMusic;
        }
        String currentMp3Path = currentMusic.getPath();
        if (!currentMp3Path.toLowerCase().trim().endsWith("mp3")) {
            return currentMusic;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(currentMp3Path, "r");
            raf.seek(raf.length() - 128);
            byte[] buff = new byte[128];
            raf.read(buff);
            String Tag = new String(buff, 0, 3, "GBK");
            if (buff.length == 128 && "TAG".equals(Tag)) {
                String artist = new String(buff, 33, 30, "GBK").trim();
                Log.i(TAG, "deal with mp3 messy,mp3 artist=******" + artist + "****** and currentMp3Path=" + currentMp3Path);
                if (!TextUtils.isEmpty(artist)) {
                    currentMusic.setArtist(artist);
                    return currentMusic;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e) {
                } finally {
                    raf = null;
                }
            }
        }
        return currentMusic;
    }
}
