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

package com.walton.filebrowser.ui.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.dlna.server.DLNAConstants;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.ui.photo.ImagePlayerActivity;
import com.walton.filebrowser.ui.video.DlnaVideoPlayActivity;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 * Dlna Data browsing class.
 * <li>Receiving from Activity transmission key events and
 * according to the key value distribution of consumption.
 * <li>Through the Handler send refresh UI news or error messages.
 * <li>According to the users to choose media files start the specified media player.
 * <li>Receive and complete the turn of events.
 * <li>Exit Dlna data read complete data recovery.
 */
public class DlnaDataBrowser {

    private static final String TAG = "DlnaDataBrowser";

    private Activity activity;

    private DlnaDataManager dlnaDataManager;

    private Handler handler;

    private List<BaseData> data;

    // Click confirm focus position media types
    private int mediaType;

    // The current focus position, is mainly used to turn the page
    private int focusPosition;

    // Whether in dlna device list interface
    private boolean isOnDevice = true;

    // DlnaDataManager finish after a scan data, regardless of success or not
    // all need to callback the interface completed UI refresh
    private RefreshUIListener refreshUIListener = new RefreshUIListener() {
        @Override
        public void onFinish(List<BaseData> list, int currentPage,
                int totalPage, int position) {
            Log.d(TAG, "onFinish, currentPage : " + currentPage
                    + " totalPage : " + totalPage + " focus position : "
                    + position);
            // The current focus position
            focusPosition = position;
            // Empty before the data
            data.clear();
            data.addAll(list);
            // Send refresh UI msg
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_DLNA_DATA;
            Bundle bundle = new Bundle();
            // current page
            bundle.putInt(Constants.BUNDLE_PAGE, currentPage);
            // total page
            bundle.putInt(Constants.BUNDLE_TPAGE, totalPage);
            // current focus position
            bundle.putInt(Constants.BUNDLE_INDEX, position);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void onOneItemAdd(List<BaseData> list, int currentPage,
                int totalPage, int position) {
        }

        @Override
        public void onScanCompleted() {
        }

        @Override
        public void onFailed(final int code) {
            Log.d(TAG, "onFailed, code : " + code);
            // Release dlna related resources
            release();
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_EXCEPTION_INFO;
            // ping device timeout
            if (code == Constants.FAILED_TIME_OUT) {
                msg.arg1 = Constants.FAILED_TIME_OUT;
                handler.sendMessage(msg);
            }
        }

    };

    /**
     * @param activity
     *            {@link Activity}Initialization upnp service needs to be used.
     * @param handler
     *            Refresh file list interface using.
     * @param data
     *            File list data source.
     */
    public DlnaDataBrowser(final Activity activity, final Handler handler,
            final List<BaseData> data) {
        this.activity = activity;
        this.handler = handler;
        this.data = data;
    }

    /**
     * Enter the folder for data.
     *
     * @param index
     *            Focus position index.
     * @param type
     *            medium type.
     */
    protected void browser(int index, int type) {
        // First browse dlna data, need to complete some necessary
        // initialization
        if (dlnaDataManager == null) {
            dlnaDataManager = new DlnaDataManager(activity, refreshUIListener);
        }
        Log.d(TAG, "index : " + index + " type : " + type);
        dlnaDataManager.browser(index, type);
    }

    /**
     * Complete file list turn the operation.
     *
     * @param label
     *            Distinguish in turn the page or next turn the page. <li>
     *            Constants.KEYCODE_PAGE_UP, Constants.TOUCH_PAGE_UP page up.
     *            <li>Constants.KEYCODE_PAGE_DOWN, Constants.TOUCH_PAGE_DOWN
     *            page down.
     */
    protected void refresh(int label) {
        // page up，Including the mouse turn the page, remote control flip over
        // and touch turn the page
        if (label == Constants.KEYCODE_PAGE_UP
                || label == Constants.TOUCH_PAGE_UP) {
            dlnaDataManager.getCurrentPage(-1);
            // page down
        } else if (label == Constants.KEYCODE_PAGE_DOWN
                || label == Constants.TOUCH_PAGE_DOWN) {
            dlnaDataManager.getCurrentPage(1);
        }
    }

    /**
     * Release dlna related resources.
     */
    protected void release() {
        if (dlnaDataManager != null) {
            dlnaDataManager.release();
        }
    }

    /**
     * Processing key event.
     *
     * @param keyCode
     *            key.
     * @param position
     *            Response button ListView focus position.
     * @return Key event handling complete returning true, otherwise returns
     *         false.
     */
    protected boolean processKeyDown(int keyCode, int position) {
        Log.d(TAG, "processKeyDown, keyCode : " + keyCode + " position : "
                + position);
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            return processEnterKeyEvent(position);

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            return processUpKeyEvent(position);

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            return processDownKeyEvent(position);
        }
        return false;
    }

    /************************************************************************
     * Private method area
     ************************************************************************/
    /**
     * According to the parameters start designated type of player.
     *
     * @param type
     *            medium type.
     * @param position
     *            focus position.
     */
    private void startPlayer(int type, int position) {
        // cache data
        ArrayList<BaseData> local = new ArrayList<BaseData>();
        local.addAll(data);
        if (position > 0 && position < local.size()) {
            // get media files
            BaseData bd = local.get(position);
            if (bd != null) {
                // Transfer to player data list
                ArrayList<BaseData> mediaFile = new ArrayList<BaseData>();
                //mediaFile.add(bd);
                Intent intent = null;
                // Start picture player
                if (Constants.FILE_TYPE_PICTURE == type) {
                    mediaFile.add(bd);
                    intent = new Intent(activity, ImagePlayerActivity.class);
                    // Start music player
                } else if (Constants.FILE_TYPE_SONG == type) {
                    mediaFile.addAll(data);
                    mediaFile.remove(0);
                    intent = new Intent(activity, MusicPlayerActivity.class);
                    // Start video player
                } else if (Constants.FILE_TYPE_VIDEO == type) {
                    mediaFile.add(bd);
                    intent = new Intent(activity, DlnaVideoPlayActivity.class);
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.BUNDLE_LIST_KEY,
                        mediaFile);
                // The current broadcast media index
                if(Constants.FILE_TYPE_SONG == type){
                    bundle.putInt(Constants.BUNDLE_INDEX_KEY, focusPosition - 1);
                }else{
                    bundle.putInt(Constants.BUNDLE_INDEX_KEY, 0);
                }
                bundle.putInt(Constants.SOURCE_FROM,
                        DLNAConstants.SOURCE_FROM_DLNA);
                intent.putExtras(bundle);

                Log.d(TAG, "start player from dlna, position : " + position
                        + " media list size : " + mediaFile.size());
                // Start media player
                if(Constants.FILE_TYPE_PICTURE == type){
                    if(Constants.isExit){
                        activity.startActivity(intent);
                    }else{
                        handler.sendEmptyMessageDelayed(Constants.START_MEDIA_PLAYER, 500);
                    }
                    return;
                }
                activity.startActivity(intent);
            }
        }
    }

    public void startPlayer(){
        startPlayer(mediaType, focusPosition);
    }

    /*
     * Confirm key the event handling.
     */
    private boolean processEnterKeyEvent(int position) {
        focusPosition = position;
        // File list first click the ok button to return to the highest level 1
        // page
        if (position == Constants.POSITION_0) {
            Message message = handler.obtainMessage();
            // Get back to mark
            String description = data.get(0).getDescription();
            // Return to the home page surface
            if (Constants.RETURN_TOP.equals(description)) {
                release();
                isOnDevice = true;
                message.what = Constants.BROWSER_TOP_DATA;
                handler.sendMessage(message);
                // Return to the directory DLNA level
            } else if (Constants.RETURN_DLNA.equals(description)) {
                // Loading tooltip
                message.what = Constants.UPDATE_PROGRESS_INFO;
                message.arg1 = R.string.loading_dlna_resource;
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                handler.sendMessage(message);
                // To get at the next higher level data directory
                browser(0, 0);
            }
        } else {
            if (position < data.size()) {
                BaseData di = data.get(position);
                mediaType = di.getType();
            } else {
                Log.e(TAG, "processEnterKeyEvent, positon : " + position);
                return false;
            }
            // Catalog click confirm button to enter directory
            if (Constants.FILE_TYPE_DIR == mediaType) {
                Message message = handler.obtainMessage();
                message.what = Constants.UPDATE_PROGRESS_INFO;
                // Prompt loading dlna equipment information
                if (isOnDevice) {
                    message.arg1 = R.string.loading_dlna_device;
                    isOnDevice = false;
                    // Prompt loading dlna source information
                } else {
                    message.arg1 = R.string.loading_dlna_resource;
                }
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                handler.sendMessage(message);
                // Loading dlna resources data
                browser(position, 0);
                // Document click confirm key
            } else {
                // Media files on click confirm key activate a player
                if (Tools.isMediaFile(mediaType)) {
                    PingDeviceListener pingDeviceListener = new PingDeviceListener() {
                        @Override
                        public void onFinish(boolean flag) {
                            // ping device success
                            if (flag) {
                                startPlayer(mediaType, focusPosition);

                                // ping device fail
                            } else {
                                Message msg = handler.obtainMessage();
                                msg.what = Constants.UPDATE_EXCEPTION_INFO;
                                msg.arg1 = Constants.FAILED_TIME_OUT;
                                handler.sendMessage(msg);
                            }
                        }
                    };
                    // Start player before you judge whether the equipment can
                    // reach (ping success)
                    dlnaDataManager.pingDevice(pingDeviceListener);
                    // Does not support file format
                } else {
                    Message message = handler.obtainMessage();
                    message.what = Constants.UPDATE_EXCEPTION_INFO;
                    message.arg1 = Constants.UNSUPPORT_FORMAT;
                    handler.sendMessage(message);
                }
            }
        }
        return true;
    }

    /*
     * up key direction on the event handling.
     */
    private boolean processUpKeyEvent(int position) {
        // Focus is located in the file list first press upward direction key
        // triggered when the flip over operation
        if (focusPosition == Constants.POSITION_0) {
            // Flip over processing
            refresh(Constants.KEYCODE_PAGE_UP);

        } else {
            focusPosition = position;
        }
        return true;
    }

    /*
     * down key direction under the event handling.
     */
    private boolean processDownKeyEvent(int position) {
        // Focus is located in the file list finally a press down the direction
        // key triggered when the flip over operation
        if (focusPosition == Constants.POSITION_9) {
            // Flip over processing
            refresh(Constants.KEYCODE_PAGE_DOWN);

        } else {
            focusPosition = position;
        }
        return true;
    }
}
