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
import android.util.Log;

/**
 * Created by nate.luo on 15-4-17.
 */

public class LocalMediaController {
    private static final String TAG = "LocalMediaController";
    public static final String ACTION_START_LOCAL_MEDIA_SERVICE = "com.mstar.android.intent.action.START_LOCAL_MEDIA_SERVICE";
    private static final String MEDIA_PLAYER_EXIT = "MEDIA_PLAYER_EXIT";
    private static final String MEDIA_PLAYER_SET_DURATION = "MEDIA_PLAYER_SET_DURATION";
    private static final String MEDIA_PLAYER_SET_DURATION_VALUE = "MEDIA_PLAYER_SET_DURATION_VALUE";
    private static final String MEDIA_PLAYER_SEEK = "MEDIA_PLAYER_SEEK";
    private static final String MEDIA_PLAYER_SEEK_TO_POSITION = "MEDIA_PLAYER_SEEK_TO_POSITION";
    private static final String MEDIA_PLAYER_ON_INFO = "MEDIA_PLAYER_ON_INFO";
    private static final String MEDIA_PLAYER_ON_ERROR = "MEDIA_PLAYER_ON_ERROR";
    private static final String MEDIA_PLAYER_ON_WHAT = "MEDIA_PLAYER_ON_WHAT";
    private static final String MEDIA_PLAYER_ON_EXTRA = "MEDIA_PLAYER_ON_EXTRA";
    private static final String MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED = "MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED";
    private static final String MEDIA_PLAYER_VIDEO_WIDTH = "MEDIA_PLAYER_VIDEO_WIDTH";
    private static final String MEDIA_PLAYER_VIDEO_HEIGHT = "MEDIA_PLAYER_VIDEO_HEIGHT";
    private static final String MEDIA_PLAYER_ON_PREPARED = "MEDIA_PLAYER_ON_PREPARED";
    private static final String MEDIA_PLAYER_ON_COMPLETION = "MEDIA_PLAYER_ON_COMPLETION";

    private Context mContext = null;

    public LocalMediaController(Context context) {
        mContext = context;
    }

    public void exitMediaPlayer() {
        Log.i(TAG, "exitMediaPlayer");
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_EXIT);
        mContext.startService(intent);
    }

    public void setDuration(int duration) {
        Log.i(TAG, "setDuration duration:" + duration);
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_SET_DURATION);
        intent.putExtra(MEDIA_PLAYER_SET_DURATION_VALUE, duration);
        mContext.startService(intent);
    }

    public void seekToPosition(int seekToPosition) {
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_SEEK);
        intent.putExtra(MEDIA_PLAYER_SEEK_TO_POSITION, seekToPosition);
        mContext.startService(intent);
    }

    public boolean onInfo(int what, int extra) {
        Log.i(TAG, "onInfo what:" + what + " extra:" + extra);
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_ON_INFO);
        intent.putExtra(MEDIA_PLAYER_ON_WHAT, what);
        intent.putExtra(MEDIA_PLAYER_ON_EXTRA, extra);
        mContext.startService(intent);
        return true;
    }

    public void onVideoSizeChanged(int width, int height) {
        Log.i(TAG, "onVideoSizeChanged width:" + width + " height:" + height);
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED);
        intent.putExtra(MEDIA_PLAYER_VIDEO_WIDTH, width);
        intent.putExtra(MEDIA_PLAYER_VIDEO_HEIGHT, height);
        mContext.startService(intent);
    }

    public void onPrepared() {
        Log.i(TAG, "onPrepared");
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_ON_PREPARED);
        mContext.startService(intent);
    }

    public boolean onError(int what, int extra) {
        Log.i(TAG, "onError what:" + what + " extra:" + extra);
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_ON_ERROR);
        intent.putExtra(MEDIA_PLAYER_ON_WHAT, what);
        intent.putExtra(MEDIA_PLAYER_ON_EXTRA, extra);
        mContext.startService(intent);
        return true;
    }

    public void onCompletion() {
        Log.i(TAG, "onCompletion");
        Intent intent = new Intent(ACTION_START_LOCAL_MEDIA_SERVICE);
        intent.putExtra("command", MEDIA_PLAYER_ON_COMPLETION);
        mContext.startService(intent);
    }
}
