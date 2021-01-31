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

import android.content.Intent;

import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Constants;

import java.util.List;


/**
 * Created by nate.luo on 15-4-13.
 */

public class FloatVideoController {
    private static final String TAG = "FloatVideoController";
    public static final String ACTION_START_FLOAT_VIDEO_SERVICE = "com.mstar.android.intent.action.START_FLOAT_VIDEO_SERVICE";
    private static final String SHOW_WINDOW = "SHOW_WINDOW";
    private static final String HIDE_WINDOW = "HIDE_WINDOW";
    private static final String GET_VIDEO_LIST_ITEM = "GET_VIDEO_LIST_ITEM";
    private static final String EXTRA_VIDEO_LIST_DATABASE = "EXTRA_VIDEO_LIST_DATABASE";
    private static final String EXTRA_VIDEO_LIST_TABLE = "EXTRA_VIDEO_LIST_TABLE";
    private static FloatVideoController mFloatVideoController = null;

    public static FloatVideoController getInstance() {
        if (mFloatVideoController == null) {
            mFloatVideoController = new FloatVideoController();
        }
        return mFloatVideoController;
    }

    public void showFloatVideoWindow() {
        Intent intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
        intent.putExtra("command", SHOW_WINDOW);
        MediaContainerApplication.getInstance().startService(intent);
    }

    public void hideFloatVideoWindow() {
        Intent intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
        intent.putExtra("command", HIDE_WINDOW);
        MediaContainerApplication.getInstance().startService(intent);
    }

    public void getVideoListItem(String dbName, String tableName) {
        Intent intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
        intent.putExtra("command", GET_VIDEO_LIST_ITEM);
        intent.putExtra(EXTRA_VIDEO_LIST_DATABASE, dbName);
        intent.putExtra(EXTRA_VIDEO_LIST_TABLE, tableName);
        MediaContainerApplication.getInstance().startService(intent);
    }

    public void setVideoPlayList(String dbName, String tableName, List<BaseData> videoPlayList, int playItem) {
        DatabaseHelper dbHelper = new DatabaseHelper(MediaContainerApplication.getInstance(), dbName);
        dbHelper.deleteTableAllItems(tableName);
        int index = 0;
        int videoPlayListSize = videoPlayList.size();
        for (index = 0; index < videoPlayListSize; index++) {
            if (playItem == index) {
                dbHelper.insert(Constants.VIDEO_PLAY_LIST_TABLE_NAME, videoPlayList.get(index).getName(), videoPlayList.get(index).getPath(), 1);
            } else {
                dbHelper.insert(Constants.VIDEO_PLAY_LIST_TABLE_NAME, videoPlayList.get(index).getName(), videoPlayList.get(index).getPath(), 0);
            }
        }
        dbHelper.close();
    }

}
