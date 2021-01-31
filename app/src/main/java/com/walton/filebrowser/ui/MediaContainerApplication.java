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

package com.walton.filebrowser.ui;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Container of media data.
 *
 * @author felix.hu
 */
public class MediaContainerApplication extends Application {
    private final static String TAG ="MediaContainerApplication";

    private static int mainTid;
    private static Handler handler;

    // singleton
    private static MediaContainerApplication instance;

    // all file in current path
    private List<BaseData> allFileList = null;

    // all folder in current path
    private List<BaseData> allFolderList = null;

    // all picture in current path
    private List<BaseData> allPictureFileList = null;

    // all song in current path
    private List<BaseData> allSongFileList = null;

    // all video in current path
    private List<BaseData> allVideoFileList = null;

    private String hardwareName = null;

    private int[] panelSize = {0,0};

    private int[] osdSize = {0,0};

    private long totalMem = -1;

    private boolean openInstallPackage;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // init all arrayList
        allFileList = new ArrayList<BaseData>();
        allFolderList = new ArrayList<BaseData>();
        allPictureFileList = new ArrayList<BaseData>();
        allSongFileList = new ArrayList<BaseData>();
        allVideoFileList = new ArrayList<BaseData>();


        mainTid = android.os.Process.myTid();
        handler=new Handler();
    }

    /**
     * @return singleton instance.
     */
    public static MediaContainerApplication getInstance() {
        return instance;
    }

    /**
     * Cache the media data.
     *
     * @param type media type.
     * @param data media data that will be cached in memory.
     */
    public final void putMediaData(final int type, final List<BaseData> data) {
        switch (type) {
            case Constants.FILE_TYPE_FILE:
                // cache all file
                allFileList.addAll(data);

                break;
            case Constants.FILE_TYPE_PICTURE:
                // cache picture file
                allPictureFileList.addAll(data);

                break;
            case Constants.FILE_TYPE_SONG:
                // cache song file
                allSongFileList.addAll(data);

                break;
            case Constants.FILE_TYPE_VIDEO:
                // cache video file
                allVideoFileList.addAll(data);

                break;
            case Constants.FILE_TYPE_DIR:
                // cache folder
                allFolderList.addAll(data);

                break;
            default:
                break;
        }
    }

    /**
     * Get all the media data with specified type.
     *
     * @param type media type.
     * @return media data in current folder or empty List.
     */
    public final List<BaseData> getMediaData(final int type) {
        List<BaseData> local = new ArrayList<BaseData>();
        // switch media type
        switch (type) {
            case Constants.FILE_TYPE_FILE:
                // return all file
                local.addAll(allFileList);

                break;
            case Constants.FILE_TYPE_PICTURE:
                // return all picture file
                local.addAll(allPictureFileList);

                break;
            case Constants.FILE_TYPE_SONG:
                // return all song file
                local.addAll(allSongFileList);

                break;
            case Constants.FILE_TYPE_VIDEO:
                // return all video file
                local.addAll(allVideoFileList);

                break;
            case Constants.FILE_TYPE_DIR:
                local.addAll(allFolderList);

                break;
            default:
                break;
        }

        return local;
    }

    /**
     * Clear all media data in memory.
     */
    public final void clearAll() {
        allFileList.clear();
        allFolderList.clear();
        allPictureFileList.clear();
        allSongFileList.clear();
        allVideoFileList.clear();
    }

    /**
     * Check whether has specified media data.
     *
     * @param type the type of media, such as image.
     * @return true if has specified media data in container.
     */
    public final boolean hasMedia(final int type) {
        boolean flag = false;

        switch (type) {
            case Constants.FILE_TYPE_PICTURE:
                // whether has picture or not
                if (allPictureFileList == null || allPictureFileList.size() == 0) {
                    flag = false;
                } else {
                    flag = true;
                }

                break;
            case Constants.FILE_TYPE_SONG:
                // whether has song or not
                if (allSongFileList == null || allSongFileList.size() == 0) {
                    flag = false;
                } else {
                    flag = true;
                }

                break;
            case Constants.FILE_TYPE_VIDEO:
                // whether has video or not
                if (allVideoFileList == null || allVideoFileList.size() == 0) {
                    flag = false;
                } else {
                    flag = true;
                }

                break;
            default:
                break;
        }

        return flag;
    }

    public final void setHardwareName(final String name) {
        hardwareName = name;
    }

    public final String getHardwareName() {
        return hardwareName;
    }

    public final void setPanelSize(final int[] config) {
        panelSize = config;
    }

    public final int[] getPanelSize() {
        return panelSize;
    }

    public final void setOsdSize(final int[] config) {
        osdSize = config;
    }

    public final int[] getOsdSize() {
        return osdSize;
    }

    public final void setTotalMem(final long total) {
        totalMem = total;
    }

    public final long getTotalMem() {
        return totalMem;
    }

    public final void clearPhotoList() {
        allPictureFileList.clear();
    }

    public final void clearMediaSongList() {
        allSongFileList.clear();
    }


    public final void setAllPhotoList(ArrayList<BaseData> photoList) {
        allPictureFileList = photoList;
    }

    public void setOpenInstallPackage(boolean openInstallPackage2) {
        this.openInstallPackage = openInstallPackage2;
    }

    public boolean isOpenInstallPackage() {
        return this.openInstallPackage;
    }



    public static Context getApplication() {
        return instance;
    }
    public static int getMainTid() {
        return mainTid;
    }
    public static Handler getHandler() {
        return handler;
    }
}
