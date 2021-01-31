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
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.music.MusicPlayerActivity;
import com.walton.filebrowser.ui.photo.ImagePlayerActivity;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;


/* *
 * SAMBA data browsing.
 * <li > receiving from Activity transmission key events and according to the key value distribution, consumption.
 * <li > through the Handler send refresh UI news or error messages.
 * <li > according to users to choose media files start the specified media player.
 * <li > receive and complete the turn of events.
 * <li > exit SAMBA data read complete data recovery.
 */
public class SambaDataBrowser {

    /** Are landing */
    protected static final int LOGIN_SAMBA = 0xa;

    /** Successful landing samba */
    protected static final int LOGIN_SUCCESS = 0xb;

    /** Are logout samba */
    protected static final int LOGOUT_SAMBA = 0xc;

    /** Is loading samba equipment */
    protected static final int LOAD_SAMBA_DEVICE = 0xd;

    /** Is loading samba resources */
    protected static final int LOAD_SAMBA_SOURCE = 0xe;

    /** Has published samba */
    protected static final int LOGOUT_DONE = 0xf;

    /** Mount samba failure */
    protected static final int MOUNT_FAILED = 0x10;

    /** Cancel the landing */
    protected static final int LOGIN_CANCEL = 0x11;

    private static final String TAG = "SambaDataBrowser";

    private SambaDataManager sambaDataManager;

    private Activity activity;

    private Handler handler;

    // File list data source
    private List<BaseData> dataList;

    // The current focus position
    private int focusPosition;

    // Currently browsing media types
    private int browserType = Constants.OPTION_STATE_ALL;

    // Click confirm focus position media types
    private int mediaType;

    public static int hasCancelVideoTaskStateInSamba=Constants.GRID_TASK_NOT_CANCELED;

    private RefreshUIListener refreshUIListener = new RefreshUIListener() {

        @Override
        public void onFinish(List<BaseData> data, int currentPage,
                int totalPage, int position) {
            Log.d(TAG, "onFinish, currentPage : " + currentPage
                    + " totalPage : " + totalPage + " position : " + position);

            // Focus position
            focusPosition = position;

            // Empty before the data
            dataList.clear();
            dataList.addAll(data);

            // Send refresh UI event
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_ALL_SAMBA_DATA;

            Bundle bundle = new Bundle();
            // The current page number
            bundle.putInt(Constants.BUNDLE_PAGE, currentPage);
            // Total page
            bundle.putInt(Constants.BUNDLE_TPAGE, totalPage);
            // The current focus position
            bundle.putInt(Constants.BUNDLE_INDEX, position);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }

        @Override
        public void onOneItemAdd(List<BaseData> data, int currentPage,
                int totalPage, int position) {
                Log.d(TAG, "onOneItemAdd, currentPage : " + currentPage
                    + " totalPage : " + totalPage + " position : " + position);

            // Focus position
            focusPosition = position;

            // Empty before the data
            dataList.clear();
            dataList.addAll(data);

            // Send refresh UI event
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_SAMBA_DATA;

            Bundle bundle = new Bundle();
            // The current page number
            bundle.putInt(Constants.BUNDLE_PAGE, currentPage);
            // Total page
            bundle.putInt(Constants.BUNDLE_TPAGE, totalPage);
            // The current focus position
            bundle.putInt(Constants.BUNDLE_INDEX, position);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void onScanCompleted() {
            Message msg = handler.obtainMessage();
            msg.what = Constants.SAMBA_SCAN_COMPLETED;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailed(int code) {
            Log.d(TAG, "onFailed, code : " + code);
            //
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_EXCEPTION_INFO;
            //
            if (code == Constants.FAILED_TIME_OUT) {
                // Release the samba related resources
                release();
                msg.arg1 = Constants.FAILED_TIME_OUT;
            } else if (code == Constants.FAILED_WRONG_PASSWD) {
                msg.arg1 = Constants.FAILED_WRONG_PASSWD;
            } else if (code == Constants.FAILED_LOGIN_FAILED) {
                msg.arg1 = Constants.FAILED_LOGIN_FAILED;
            } else if (code == Constants.FAILED_LOGIN_OTHER_FAILED) {
                msg.arg1 = Constants.FAILED_LOGIN_OTHER_FAILED;
            }
            handler.sendMessage(msg);

        }

    };

    private LoginSambaListener loginSambaListener = new LoginSambaListener() {

        @Override
        public void onEnd(int code) {
            Message message = handler.obtainMessage();
            message.what = Constants.UPDATE_PROGRESS_INFO;
            message.arg2 = Constants.PROGRESS_TEXT_SHOW;

            switch (code) {
            // Are landing samba
            case LOGIN_SAMBA: {
                message.arg1 = R.string.login_samba;
                break;
            }
            // Landing successful
            case LOGIN_SUCCESS: {
                // message.arg1 = R.string.login_success;
                break;
            }
            // Are logout samba equipment
            case LOGOUT_SAMBA: {
                message.arg1 = R.string.logging_out_samba;
                break;
            }
            // Is loading samba equipment
            case LOAD_SAMBA_DEVICE: {
                message.arg1 = R.string.loading_samba_device;
                break;
            }
            // Is loading samba resources
            case LOAD_SAMBA_SOURCE: {
                message.arg1 = R.string.loading_samba_resource;
                break;
            }
            // Logout complete
            case LOGOUT_DONE: {
                message.arg2 = Constants.PROGRESS_TEXT_HIDE;
                break;
            }
            // Mount failure
            case MOUNT_FAILED: {
                message.what = Constants.UPDATE_EXCEPTION_INFO;
                message.arg1 = Constants.MOUNT_FAILED;
                break;
            }
            // Cancel the landing
            case LOGIN_CANCEL: {
                message.arg2 = Constants.PROGRESS_TEXT_HIDE;
                break;
            }
            default:
                break;
            }
            // Send a message to interface update
            handler.sendMessage(message);
        }

    };

    /**
     * @param activity
     *            {@link Activity}.
     * @param handler
     *            Send refresh interface news{@link Handler}.
     * @param data
     *            File list data source.
     */
    public SambaDataBrowser(Activity activity, Handler handler,
            List<BaseData> data) {
        this.activity = activity;
        this.handler = handler;
        this.dataList = data;
    }

    /**
     * Enter the folder for data.
     *
     * @param index
     *            .
     * @param type
     *            Media types.
     */
    protected void browser(int index, int type) {
        this.browserType = type;
        Log.d(TAG, "index : " + index + " type : " + type);

        if (sambaDataManager == null) {
            sambaDataManager = new SambaDataManager(activity,
                    loginSambaListener, refreshUIListener);
        }

        sambaDataManager.browser(index, type);
    }


    protected void stopBrowser() {
         if (sambaDataManager != null) {
             Log.i(TAG, "stop samba browser...");
             sambaDataManager.stopBrowser();
         }
    }

    protected boolean isUpdating() {
         if (sambaDataManager != null) {
             return sambaDataManager.isUpdating();
         }
         return false;
    }

    /**
     * Complete turn the page and media switch operation.
     *
     * @param label
     *            Distinguish in turn the page or next turn the page. <li>page
     *            up Constants.KEYCODE_PAGE_UP, Constants.TOUCH_PAGE_UP. <li>
     *            page down Constants.KEYCODE_PAGE_DOWN,
     *            Constants.TOUCH_PAGE_DOWN. <li>Media type switch
     *            Constants.OPTION_STATE_ALL
     *            ,Constants.OPTION_STATE_PICTURE,Constants
     *            .OPTION_STATE_SONG,Constants.OPTION_STATE_VIDEO
     */
    protected void refresh(int label) {
        // Switching media types
        if (label == Constants.OPTION_STATE_ALL
                || label == Constants.OPTION_STATE_PICTURE
                || label == Constants.OPTION_STATE_SONG
                || label == Constants.OPTION_STATE_VIDEO) {
            this.browserType = label;
            sambaDataManager.getCurrentPage(0, label);

            // Page up
        } else if (label == Constants.KEYCODE_PAGE_UP
                || label == Constants.TOUCH_PAGE_UP) {
            if (FileBrowserActivity.mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                Message msg = handler.obtainMessage();
                msg.what = Constants.GRID_CANCEL_TASK;
                msg.arg1 = Constants.GRID_CANCEL_TASK_NO_NEED_PLAY;
                handler.sendMessage(msg);
            }
            sambaDataManager.getCurrentPage(-1, 0);

            // Page down
        } else if (label == Constants.KEYCODE_PAGE_DOWN
                || label == Constants.TOUCH_PAGE_DOWN) {
            if (FileBrowserActivity.mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                Message msg = handler.obtainMessage();
                msg.what = Constants.GRID_CANCEL_TASK;
                msg.arg1 = Constants.GRID_CANCEL_TASK_NO_NEED_PLAY;
                handler.sendMessage(msg);
            }
            sambaDataManager.getCurrentPage(1, 0);
        }
    }

    /**
     * Release the samba related resources.
     */
    protected void release() {
        if (sambaDataManager != null) {
            sambaDataManager.release();
        }
    }

    /**
     * Unmount off all of the mount SAMBA file.
     */
    protected void unmount() {
        if (Tools.isUseHttpSambaModeOn()){
            return;
        }
        if (sambaDataManager != null) {
            sambaDataManager.unmount();
        }
    }

    public void stopHttpServer(){
        if (!Tools.isUseHttpSambaModeOn()) {
            return;
        }
        if (sambaDataManager != null) {
            sambaDataManager.stopHttpServer();
        }
    }
    protected void closeDialogIfneeded() {
        if (sambaDataManager != null) {
            sambaDataManager.closeDialogIfneeded();
        }
    }

    /**
     * Processing key event.
     *
     * @param keyCode
     *            Key value.
     * @param position
     *            Response button ListView focus position.
     * @return Key event handling complete returning true, otherwise returns
     *         false.
     */
    protected boolean processKeyDown(int keyCode, int position) {
        Log.d(TAG, "keyCode : " + keyCode + " position : " + position);

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
     * According to the parameters start designated player.
     *
     * @param type
     *            Media types.
     * @param position
     *            Focus position.
     */
    private void startPlayer(int type, int position) {
        boolean hasMedia = false;
        // check whether has the specified type of media
        hasMedia = MediaContainerApplication.getInstance().hasMedia(type);
        if (hasMedia) {
            // Focus on the current position of the position
            int index = 0;
            if (browserType != type
                    && browserType == Constants.OPTION_STATE_ALL) {
                index = sambaDataManager.getMediaFile(-type, position);
            } else {
                index = sambaDataManager.getMediaFile(type, position);
            }
            Log.d(TAG, "startPlayer, index : " + index);

            Intent intent = null;
            // Start picture player
            if (Constants.FILE_TYPE_PICTURE == type) {
                intent = new Intent(activity, ImagePlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                // Start music player
            } else if (Constants.FILE_TYPE_SONG == type) {
                intent = new Intent(activity, MusicPlayerActivity.class);

                // Start video player
            } else if (Constants.FILE_TYPE_VIDEO == type) {
                intent = new Intent(activity, VideoPlayerActivity.class);
            }
            // The current broadcast media index
            intent.putExtra(Constants.BUNDLE_INDEX_KEY, index);
            intent.putExtra(Constants.SOURCE_FROM, Constants.SOURCE_FROM_SAMBA);

            // Start media player
            if(Constants.FILE_TYPE_PICTURE == type){
                if(Constants.isExit){
                    activity.startActivity(intent);
                }else{
                    focusPosition = position;
                    handler.sendEmptyMessageDelayed(Constants.START_MEDIA_PLAYER, 500);
                }
                return;
            }
            activity.startActivity(intent);
        } else {
            Log.d(TAG, "Does not has specified type : " + type + " of media.");
        }
    }

    public void startPlayer(){
        startPlayer(mediaType, focusPosition);
    }


    /*
     * Confirm key the event handling..
     */
    private boolean processEnterKeyEvent(int position) {
        focusPosition = position;

        // List the first click the ok button to return to the highest level 1
        // page
        if (position == Constants.POSITION_0) {
            Message message = handler.obtainMessage();

            String description = dataList.get(0).getDescription();
            Log.i(TAG, "processEnterKeyEvent: description: " + description);
            // Return to the home page surface
            if (description == null || Constants.RETURN_TOP.equals(description)) {
                release();

                message.what = Constants.BROWSER_TOP_DATA;
                handler.sendMessage(message);

                // Return to the Samba level directory
            } else if (Constants.RETURN_SAMBA.equals(description)) {
                // Loading tooltip
                message.what = Constants.UPDATE_PROGRESS_INFO;
                message.arg1 = R.string.loading_samba_resource;
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                handler.sendMessage(message);

                // Reload on level 1 catalogue data
                browser(0, browserType);
            }

        } else {
            if (position < dataList.size()) {
                BaseData bd = dataList.get(position);
                mediaType = bd.getType();
            } else {
                Log.e(TAG, "processEnterKeyEvent, positon : " + position);

                return false;
            }

            // Catalog click confirm button to enter directory
            if (Constants.FILE_TYPE_DIR == mediaType) {
                stopBrowser();
                // Loading samba data
                browser(position, browserType);

                // Document click confirm key
            } else {
                // Media files on click confirm key activate a player
                if (Tools.isMediaFile(mediaType)) {
                    PingDeviceListener pingDeviceListener = new PingDeviceListener() {

                        @Override
                        public void onFinish(boolean flag) {
                            // Equipment can ping pass
                            if (flag) {
                                if (Constants.GRIDVIEW_MODE == FileBrowserActivity.mListOrGridFlag) {
                                     Message msg = handler.obtainMessage();
                                     msg.what = Constants.GRID_CANCEL_TASK;
                                     msg.arg1 = Constants.GRID_CANCEL_TASK_NEED_PLAY;
                                     handler.sendMessage(msg);
                                 } else
                                     startPlayer(mediaType, focusPosition);

                                // Equipment not ping pass
                            } else {
                                Message msg = handler.obtainMessage();
                                msg.what = Constants.UPDATE_EXCEPTION_INFO;
                                msg.arg1 = Constants.FAILED_TIME_OUT;
                                handler.sendMessage(msg);
                            }
                        }
                    };
                    // Before starting player ping the equipment, if you can
                    // start player of the general principles of the ping
                    sambaDataManager.pingDevice(pingDeviceListener);

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
     * Key direction on the event handling.
     */
    private boolean processUpKeyEvent(int position) {
        if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            if (focusPosition == Constants.POSITION_0) {
                // page up
                refresh(Constants.KEYCODE_PAGE_UP);
            } else {
                focusPosition = position;
            }
            return true;

        } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            if (focusPosition<Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM && focusPosition>=0) {
                // page up
                refresh(Constants.KEYCODE_PAGE_UP);
            } else {
                focusPosition = position;
            }
            return true;
        }
        return true;
    }

    /*
     * Key direction under the event handling.
     */
    private boolean processDownKeyEvent(int position) {
        if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            if (focusPosition == Constants.POSITION_9) {
                // page down
                refresh(Constants.KEYCODE_PAGE_DOWN);
            } else {
                focusPosition = position;
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            int lastRowStartPosition =Constants.GRID_MODE_DISPLAY_NUM-Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM+1;
            if (focusPosition<=Constants.GRID_MODE_DISPLAY_NUM && focusPosition>=lastRowStartPosition) {
                // page down
                refresh(Constants.KEYCODE_PAGE_DOWN);
            } else {
                focusPosition = position;
            }
            return true;
        }
        return true;
    }
    protected int getBrowserSambaDataState(){
        if (null == sambaDataManager)
            return -1;
        int tmpState = sambaDataManager.getBrowserSambaDataState();
        return tmpState;
    }
    protected void switchViewMode(){
          sambaDataManager.switchViewMode=1;
          sambaDataManager.onFinish();
    }
}
