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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.math.BigInteger;

import android.content.res.Resources;
import android.annotation.SuppressLint;
import android.app.Activity;
import com.mstar.android.samba.OnRecvMsg;
import com.mstar.android.samba.OnRecvMsgListener;
import com.mstar.android.samba.SmbAuthentication;
import com.mstar.android.samba.SmbClient;
import com.mstar.android.samba.SmbDevice;
import com.mstar.android.samba.SmbShareFolder;
import com.mstar.android.samba.NanoHTTPD;
import com.mstar.android.samba.HttpBean;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.data.ReturnData;
import com.walton.filebrowser.business.data.ReturnStack;
import com.walton.filebrowser.util.Constants;
import com.mstar.android.storage.MStorageManager;
import com.walton.filebrowser.business.video.LoginInfoDBAdapter;
import com.walton.filebrowser.util.Tools;
import java.lang.reflect.Method;

/**
 * SAMBA Data management class. <li>init SAMBA api. <li>Search within the local
 * area network (LAN) SAMBA equipment. <li>Search SAMBA equipment within the
 * media files.
 */
public class SambaDataManager extends BaseDataManager {

    private static final String TAG = "SambaDataManager";

    private static final int NET_STATUS_NOT_SUPPORT = -1073741637;

    // Is scanning the SAMBA device list
    protected static final int SCAN_HOST = 0x01;

    // Are viewing a SAMBA equipment resources
    protected static final int SCAN_FILE = 0x02;

    private static final int CNT_PER_PAGE = 9;

    private static final int CNT_GRID_PER_PAGE = 9;

    private Activity activity;

    // return to SambaDataBrowser data list
    private List<BaseData> uiData = new ArrayList<BaseData>();

    // The current equipment pages, total pages and the focus position
    private int currentPage = 1, totalPage = 1, position = 0;

    // The current state of browsing
    private int state = SCAN_HOST;

    public int switchViewMode =0;

    // current browse media type
    private int browseType = Constants.OPTION_STATE_ALL;

    // username and password
    private String usr, pwd;

    // Is currently scanning path
    private String currentPath = "";

    // used on newSamba in enterDirectory function
    private String currentDirectoryName = "";

    private SmbClient smbClient;

    private SmbDevice smbDevice;

    private NanoHTTPD nanohttp;

    // Scanning SAMBA device list thread
    private Thread findHostThread = null;

    // all SAMBA device list
    private List<SmbDevice> deviceList = new ArrayList<SmbDevice>();

    private List<SmbShareFolder> mSmbShareFolderLists = new ArrayList<SmbShareFolder>();

    private PingDeviceListener pingDeviceListener;

    private UnMountListener unmountListener;

    private LoginSambaListener loginSambaListener;

    private RefreshUIListener refreshUIListener;

    private MStorageManager mStorageManager;

    // Memory into the directory location to return to its original position
    private ReturnStack returnStack;

    LoginSambaDialog mLoginDialog;

    private Resources resource;

    private boolean mIsConnectingHttpServer = false;

    private String mSambaName = "";

    private String mSambaPassword = "";

    public SambaDataManager(Activity activity,
            LoginSambaListener loginSambaListener,
            RefreshUIListener refreshUIListener) {
        super(activity.getResources());
        this.resource = activity.getResources();
        this.activity = activity;
        this.loginSambaListener = loginSambaListener;
        this.refreshUIListener = refreshUIListener;

        this.returnStack = new ReturnStack();

        // Initialization samba search must parameters
        SmbClient.initSamba();
        this.mStorageManager = MStorageManager.getInstance(activity);
    }

    /**
     * Scanning specify the location of the corresponding data directory.
     *
     * @param index
     *            Focus Position.
     * @param type
     *            media type.
     */
    protected void browser(int index, int type) {
        this.browseType = type;
        this.position = index;

        // First browse samba data
        if (index == -1) {
            findSambaDevice();

            // return to pre
        } else if (index == 0) {
            enterParentDirectory();

            // Into the next level directory
        } else {
            enterDirectory(index);
        }

    }

    protected void stopBrowser() {
        if (smbClient != null && smbClient.isUpdating()) {
            Log.i(TAG, "stop update...");
            smbClient.StopUpdate();
        }
    }

    public boolean isUpdating() {
         if (smbClient != null) {
            return smbClient.isUpdating();
        }
         return false;
    }


    private void updateCurrentPage(int increase, int type) {
        Log.d(TAG, "updateCurrentPage, increase : " + increase
                + " current state : " + state + " type : " + type);
        // Flip over operation
        if (type == 0) {
            // page down
            if (increase == 1) {
                if (currentPage < totalPage) {
                    currentPage++;
                } else {
                    return;
                }
                position = Constants.POSITION_0;
            } else if (increase == 0) {
                // page up
            } else if (increase == -1) {
                if (currentPage > 1) {
                    currentPage--;
                } else {
                    return;
                }
                if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
                    position=Constants.GRID_MODE_DISPLAY_NUM;
                else
                    position = 9;
            }

        } else {
            // Switching media type operation
            if (browseType != type) {
                browseType = type;
                position = Constants.POSITION_0;
                currentPage = 1;
            }
        }
    }
    /**
     * @param increase
     *            Turn the page index. </br> <li>1 : Said back flip over. <li>0
     *            : Said the first page. <li>-1 : Said forward turn the page.
     * @param type
     *            Media types, 0 indicates no practical significance.
     */
    protected void getCurrentPage(int increase, int type) {
        Log.d(TAG, "getCurrentPage, increase : " + increase
                + " current state : " + state + " type : " + type);

        updateCurrentPage(increase, type);
        // Structure UI display data
        //packageUIData();
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            packageUIDataInGrid();
        } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            packageUIData();
        }
        // Callback interface notice UI refresh
        refreshUIListener.onFinish(uiData, currentPage, totalPage, position);
    }
    protected int getBrowserSambaDataState(){
        return state;
    }
    private void notifyScanCompleted() {
        //getCurrentPage(0,0);
        refreshUIListener.onScanCompleted();
    }
    private void updataDeviceListItem() {
        Log.d(TAG, "updataDeviceListItem"+ " current state : " + state );
        state = SCAN_HOST;

        if (smbClient == null) {
            return;
        }
        if(!smbClient.isUpdating()) {
            return;
        }
        deviceList.clear();
        List<SmbDevice> local = smbClient.getSmbDeviceList();
        if (local != null) {
            deviceList.addAll(local);
            int size = deviceList.size();
            // recalculate Page Breaks
            int numPerPage=-100;
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
                numPerPage =CNT_GRID_PER_PAGE;
            else
                numPerPage =CNT_PER_PAGE;
            if (size != 0) {
                totalPage = size / numPerPage;
                totalPage += size % numPerPage == 0 ? 0 : 1;
            } else {
                Log.d(TAG, "load samba device failed");
            }
            Log.d(TAG, "updataDeviceListItem, totalPage : " + totalPage);

            updateCurrentPage(0, 0);
            // Structure UI display data
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                packageUIDataInGrid();
            } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                packageUIData();
            }
            // Callback interface notice UI refresh
            position =0;
            Log.d(TAG, "update UI data  currentPage:"+currentPage+"  totalPage:"+totalPage+" position:"+position);
            refreshUIListener.onOneItemAdd(uiData, currentPage, totalPage, position);
        } else {
            Log.d(TAG, "smbDevice list is null");
        }
    }

    /**
     * Release the SAMBA related resources and clean up the local cache data.
     */
    protected void release() {
        if (smbClient != null) {
            state = SCAN_HOST;

            // Samba device list page number replacement.
            currentPage = 1;
            totalPage = 1;
            position = 0;

            // Interrupt search samba equipment operation
            if (smbClient.isUpdating()) {
                smbClient.StopUpdate();
            }

            // Thread resources release
            if (findHostThread != null && findHostThread.isAlive()) {
                findHostThread.interrupt();
                findHostThread = null;
            }

            // Release stack data
            if (returnStack != null && returnStack.getTankage() > 0) {
                returnStack.clear();
            }

            // unmountSamba();
        }
    }

    /**
     * Get the current directory all designated type of media files.
     *
     * @param type
     *            ,media type.
     * @param position
     *            focus position.
     * @return The current players play index.
     */
    protected int getMediaFile(int type, int position) {
        // get all meida file
        ArrayList<BaseData> mediaFiles = new ArrayList<BaseData>();
        // get all data
        List<BaseData> allFiles = new ArrayList<BaseData>();

        // File type switch in all types mode
        if (type < 0) {
            allFiles.addAll(getUIDataList(Constants.OPTION_STATE_ALL));
            mediaFiles.addAll(getMediaFileList(-type));

            // File type switch in pictures, music, or video mode
        } else {
            allFiles.addAll(getUIDataList(type));
            mediaFiles.addAll(getMediaFileList(type));
        }

        // index switch
        int index = -100;
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
            index = (currentPage - 1) * CNT_GRID_PER_PAGE+ position - 1;
        else
            index = (currentPage - 1) * CNT_PER_PAGE+ position - 1;
        int size = allFiles.size();
        Log.d(TAG, "all media file size : " + mediaFiles.size()
                + " currentPage : " + currentPage + " position : " + position);
        if (index >= 0 && index < size) {
            BaseData bd = allFiles.get(index);
            String path = bd.getPath();

            index = 0;
            for (BaseData item : mediaFiles) {
                if (path.equals(item.getPath())) {
                    return index;
                } else {
                    index++;
                }
            }
        }

        return 0;
    }

    /**
     * Open thread finish ping network equipment operation.
     */
    protected void pingDevice(final PingDeviceListener listener) {
        // Ensure examples for null further follow-up operations
        if (smbDevice != null) {

            Runnable localRunnable = new Runnable() {

                @Override
                public void run() {
                    boolean result = false;
                    try {
                        String ip = smbDevice.getAddress();
                        InetAddress localnetAddress = InetAddress.getByName(ip);
                        result = localnetAddress.isReachable(1000);
                        HttpBean.setmIpAddress(ip);
                        //callSambaSetmIpAddress(ip);
                        Log.d(TAG, "host ip : " + ip + " result : " + result);
                    } catch (Exception e) {
                        result = false;
                    }
                    // Not a network segment or domain name, it is always false
                    // Application layer executive ping command word, many are
                    // no authority problem
                    listener.onFinish(result);
                }
            };
            // The network operation must not in the main thread
            Thread localThread = new Thread(localRunnable);
            localThread.start();

            //
        } else {
            listener.onFinish(false);
        }

    }

    public  void callSambaSetmIpAddress(String ip) {
        Log.i(TAG, "callSambaSetmIpAddress");
        try {
             Class clz = Class.forName("com.mstar.android.samba.HttpBean");
             Method setmIpAddress = clz.getDeclaredMethod("setmIpAddress",String.class);
             setmIpAddress.invoke(null,ip);
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    /**
     * unmount the data source which had mount to /mnt/samba/.
     */
    protected void unmount() {
        if (Tools.isUseHttpSambaModeOn()){
            return;
        }
        // monitor
        unmountListener = new UnMountListener() {

            @Override
            public void onFinish(int code) {
                smbDevice = null;
                if (code == OnRecvMsg.NT_STATUS_UMOUNT_SUCCESSFUL) {
                    Log.d(TAG, "unmount success");
                } else if (code == OnRecvMsg.NT_STATUS_UMOUNT_FAILURE) {
                    Log.d(TAG, "unmount failed");
                }
            }
        };

        // Unloading operation more time-consuming, so start a thread to
        // complete the operation
        if (smbDevice != null && smbDevice.isMounted()) {
            Runnable localRunnable = new Runnable() {

                @Override
                public void run() {
                    int code = smbDevice.unmount();
                    Log.d(TAG, "unmount code : " + code);
                    unmountListener.onFinish(code);
                }
            };
            Thread localThread = new Thread(localRunnable);
            localThread.start();
        }
    }

    protected void closeDialogIfneeded() {
        if (mLoginDialog != null && mLoginDialog.isShowing()) {
            mLoginDialog.dismiss();
        }
    }

    /************************************************************************
     * Rewrite interface method area
     ************************************************************************/
    @Override
    public void onFinish() {

        // Get the current page samba resources data
        if (1==switchViewMode){
            switchViewMode=0;
            position=0;
        }
        getCurrentPage(0, browseType);

    }

    /************************************************************************
     * Private method area
     ************************************************************************/

    /*
     * Loading samba equipment data and calculation paging.
     */
    private void loadDeviceData() {
        state = SCAN_HOST;

        if (smbClient == null) {
            return;
        }
        deviceList.clear();
        List<SmbDevice> local = smbClient.getSmbDeviceList();
        if (local != null) {
            deviceList.addAll(local);

            int size = deviceList.size();
            // recalculate Page Breaks
            int numPerPage = -100;
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
                numPerPage = CNT_GRID_PER_PAGE;
            else
                numPerPage = CNT_PER_PAGE;
            if (size != 0) {
                totalPage = size / numPerPage;
                totalPage += size % numPerPage == 0 ? 0 : 1;
            } else {
                Log.d(TAG, "load samba device failed");
            }
            Log.d(TAG, "loadDeviceData, totalPage : " + totalPage);

            // Get the current page samba equipment
            getCurrentPage(0, 0);
        } else {
            Log.d(TAG, "smbDevice list is null");
        }

    }

    /*
     * Packaging UI display use SAMBA data list.
     */
    private void packageUIDataInGrid() {
        int tail = 0;
        int size = 0;
        // loading SAMBA device list
        if (state == SCAN_HOST) {
            List<SmbDevice> deviceList_local = new ArrayList<SmbDevice>();
            deviceList_local.addAll(deviceList);
            size = deviceList_local.size();


            if (size > currentPage * CNT_GRID_PER_PAGE) {
                tail = currentPage * CNT_GRID_PER_PAGE;
            } else {
                tail = size;
            }
            Log.d(TAG, "device size : " + size + " tail : " + tail);
            // Empty before the data
            uiData.clear();
            // return to pre dir
            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_TOP);
            uiData.add(di);

            // Loading the current page need to display equipment data
            for (int i = (currentPage - 1) * CNT_GRID_PER_PAGE; i < tail; i++) {
                SmbDevice sd = deviceList_local.get(i);
                BaseData item = new BaseData(Constants.FILE_TYPE_DIR);
                if (sd != null) {
                    item.setName(sd.getAddress());
                    uiData.add(item);
                }
            }
            deviceList_local.clear();
            // Structure resources data list
        } else if (state == SCAN_FILE) {
            // Empty before the data
            uiData.clear();
            // Add to return to a
            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_SAMBA);
            uiData.add(di);

            List<BaseData> localUiData = new ArrayList<BaseData>();
            localUiData.addAll(getUIDataList(browseType));
            size = localUiData.size();
            if (size > currentPage * CNT_GRID_PER_PAGE) {
                tail = currentPage * CNT_GRID_PER_PAGE;
            } else {
                tail = size;
            }
            Log.d(TAG, "size : " + size + " tail : " + tail);

            // Loading the current page need to display resources data
            for (int i = (currentPage - 1) * CNT_GRID_PER_PAGE; i < tail; i++) {
                BaseData item = localUiData.get(i);
                uiData.add(item);
            }
        }

        // Computing distribution number
        if (size != 0) {
            totalPage = size / CNT_GRID_PER_PAGE;
            totalPage += size % CNT_GRID_PER_PAGE == 0 ? 0 : 1;
        } else {
            totalPage = 1;
        }

    }
    /*
         * Packaging UI display use SAMBA data list while Icon's mode
         */
    private void packageUIData() {
        int tail = 0;
        int size = 0;
        // loading SAMBA device list
        if (state == SCAN_HOST) {
            List<SmbDevice> deviceList_local = new ArrayList<SmbDevice>();
            deviceList_local.addAll(deviceList);
            size = deviceList_local.size();


            if (size > currentPage * CNT_PER_PAGE) {
                tail = currentPage * CNT_PER_PAGE;
            } else {
                tail = size;
            }
            Log.d(TAG, "device size : " + size + " tail : " + tail);
            // Empty before the data
            uiData.clear();
            // return to pre dir
            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_TOP);
            uiData.add(di);

            // Loading the current page need to display equipment data
            for (int i = (currentPage - 1) * CNT_PER_PAGE; i < tail; i++) {
                 SmbDevice sd = deviceList_local.get(i);
                 BaseData item = new BaseData(Constants.FILE_TYPE_DIR);
                 if (sd != null) {
                     item.setName(sd.getAddress());
                     uiData.add(item);
                 }
            }
            deviceList_local.clear();
            // Structure resources data list
        } else if (state == SCAN_FILE) {
            // Empty before the data
            uiData.clear();
            // Add to return to a
            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_SAMBA);
            uiData.add(di);

            List<BaseData> localUiData = new ArrayList<BaseData>();
            localUiData.addAll(getUIDataList(browseType));
            size = localUiData.size();
            if (size > currentPage * CNT_PER_PAGE) {
                tail = currentPage * CNT_PER_PAGE;
            } else {
                tail = size;
            }
            Log.d(TAG, "size : " + size + " tail : " + tail);

            // Loading the current page need to display resources data
            for (int i = (currentPage - 1) * CNT_PER_PAGE; i < tail; i++) {
                BaseData item = localUiData.get(i);
                uiData.add(item);
            }
        }

        // Computing distribution number
        if (size != 0) {
            totalPage = size / CNT_PER_PAGE;
            totalPage += size % CNT_PER_PAGE == 0 ? 0 : 1;
        } else {
            totalPage = 1;
        }

    }

    /*
     * In the file list first position press ok button finished previous catalog
     * operation.
     */
    private void enterParentDirectory() {
        // There are also Stack data, return the first level directory
        if (returnStack.getTankage() > 0) {
            ReturnData rd = returnStack.pop();
            String id = rd.getId();
            currentPage = rd.getPage();
            position = rd.getPosition();
            currentPath = rd.getId();
            currentDirectoryName = rd.getDiractoryName();
            int returnViewMode = rd.getViewMode();
            Log.d(TAG, "pop stack, page : " + currentPage + " position : "
                    + position + " path : " + currentPath);
            int tmp=0;
            if (1==returnViewMode) {
                tmp = (currentPage - 1) * Constants.GRID_MODE_DISPLAY_NUM + position- 1;
            } else {
                tmp = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM + position- 1;
            }
            int tmpPos = position;
            if (0==returnViewMode && Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                // changeReturnDataList2Grid
                // tmp+1 is the true digit without counting "back" per page
                int mod =(tmp+1)%(Constants.GRID_MODE_DISPLAY_NUM);
                currentPage =(tmp+1)/(Constants.GRID_MODE_DISPLAY_NUM);
                if (mod>0)
                    currentPage++;
                if (mod>0)
                    position=mod;
                else if (0==mod&&tmpPos!=0)
                    position=Constants.GRID_MODE_DISPLAY_NUM;
            } else if (1==returnViewMode&& Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                // changeReturnDataGrid2List
                int mod =(tmp+1)%(Constants.LIST_MODE_DISPLAY_NUM);
                currentPage =(tmp+1)/(Constants.LIST_MODE_DISPLAY_NUM);
                if (mod>0)
                    currentPage++;
                if (mod>0)
                    position=mod;
                else if (0==mod)
                    position=Constants.LIST_MODE_DISPLAY_NUM;
            }
            // Returns to the equipment list
            if (Constants.RETURN_SAMBA.equals(id)) {
                state = SCAN_HOST;
                // Loading all equipment data
                loadDeviceData();

                // Returns the SAMBA equipment at the next higher level
                // directory
            } else {
                state = SCAN_FILE;

                pingDeviceListener = new PingDeviceListener() {

                    @Override
                    public void onFinish(boolean flag) {
                        if (flag) {
                            // start scanning data of the specified directory
                            if (Tools.isUseHttpSambaModeOn()) {
                                Log.i("andrew", "startScanSmbShareFolder enterParentDirectory:"+currentDirectoryName);
                                //startScanSmbShareFolder(smbDevice.enterDirectory(currentDirectoryName));
                                startScanSmbShareFolder(smbDevice.enterParent());
                            } else {
                                startScan(currentPath);
                            }

                            // can't SAMBA equipment Ping to record
                        } else {
                            refreshUIListener
                                    .onFailed(Constants.FAILED_TIME_OUT);
                        }
                    }
                };

                pingDevice(pingDeviceListener);
            }

        }
    }

    /*
     * complete landing or loading a catalogue of data operation.
     */
    private void enterDirectory(int index) {
        // are display samba device list data
        if (state == SCAN_HOST) {
            int tmp = -100;
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                tmp = (currentPage - 1) * CNT_GRID_PER_PAGE+ index- 1;
            } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                tmp = (currentPage - 1) * CNT_PER_PAGE+ index- 1;
            }
            if (tmp >= 0 && tmp < deviceList.size()) {
                Log.d(TAG, "enterDirectory index : " + index + " tmp index : "
                        + tmp);
                final SmbDevice localSmbDevice = deviceList.get(tmp);

                // if have loaded samba equipment is prior to discharge
                if (!Tools.isUseHttpSambaModeOn() && smbDevice != null && smbDevice.isMounted()) {
                    // unloading after the completion of display landing box
                    unmountListener = new UnMountListener() {

                        @Override
                        public void onFinish(int code) {
                            Log.d(TAG, "unmount code : " + code);
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    loginSambaListener
                                            .onEnd(SambaDataBrowser.LOGOUT_DONE);

                                    smbDevice = localSmbDevice;
                                    // No matter whether the logout success
                                    // relog
                                    // showLogin();
                                    mLoginDialog = new LoginSambaDialog(
                                            SambaDataManager.this.activity,
                                            handler, smbDevice);
                                    mLoginDialog.show();
                                }
                            });
                        }
                    };

                    // unloading operation more time-consuming, so open a thread
                    Runnable localRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // because exit landing there may be a need for a
                            // long time, so given exit landing tips let users
                            // wait
                            loginSambaListener
                                    .onEnd(SambaDataBrowser.LOGOUT_SAMBA);

                            int code = smbDevice.unmount();
                            unmountListener.onFinish(code);
                        }
                    };
                    Thread localThread = new Thread(localRunnable);
                    localThread.start();

                    //
                } else {
                    smbDevice = localSmbDevice;
                    // Popup landing box
                    // showLogin();
                    mLoginDialog = new LoginSambaDialog(
                            this.activity, handler, smbDevice);
                    mLoginDialog.show();
                }

            } else {
                Log.e(TAG, "invalid index when show login dialog");
            }

            // Browse Samba resource file
        } else if (state == SCAN_FILE) {

            pingDeviceListener = new PingDeviceListener() {

                @Override
                public void onFinish(boolean flag) {
                    if (flag) {
                        String parentPath = "";
                        int tmp = -100;
                        int viewMode = -100;
                        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                            tmp = (currentPage - 1) * CNT_GRID_PER_PAGE+ position- 1;
                            viewMode = 1;
                        } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                            tmp = (currentPage - 1) * CNT_PER_PAGE+ position- 1;
                            viewMode = 0;
                        }
                        // Get the current directory all need to display data
                        // browsing Samba resource file
                        List<BaseData> list = new ArrayList<BaseData>();
                        list.addAll(getUIDataList(browseType));
                        if (tmp >= 0 && tmp < list.size()) {
                            BaseData bd = list.get(tmp);
                            currentPath = bd.getPath();
                            if (Tools.isUseHttpSambaModeOn()) {
                                parentPath = Constants.RETURN_NOT_SAMBA;
                            } else {
                                parentPath = bd.getParentPath();
                            }
                            currentDirectoryName = bd.getName();
                        } else {
                            Log.e(TAG, "invalied index on browser");
                        }
                        Log.i(TAG,"parentPath:"+parentPath);
                        ReturnData rd = new ReturnData(viewMode,parentPath, currentPage,
                                position,currentDirectoryName);
                        returnStack.push(rd);

                        Log.d(TAG, "scan file path : " + currentPath);
                        position = 0;
                        currentPage = 1;
                        totalPage = 1;

                        loginSambaListener
                                .onEnd(SambaDataBrowser.LOAD_SAMBA_SOURCE);
                        // start scanning data of the specified directory
                        if (Tools.isUseHttpSambaModeOn() && currentDirectoryName.length()>0) {
                            Log.i("andrew", "startScanSmbShareFolder enterDirectory:"+currentDirectoryName);
                            startScanSmbShareFolder(smbDevice.enterDirectory(currentDirectoryName));
                        } else {
                            startScan(currentPath);
                        }
                        // can't Ping to SAMBA equipment
                    } else {
                        refreshUIListener.onFailed(Constants.FAILED_TIME_OUT);
                    }
                }
            };

            pingDevice(pingDeviceListener);
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case 0:
                // Cancel the landing
                loginSambaListener.onEnd(SambaDataBrowser.LOGIN_CANCEL);
                break;
            case 1:
                Bundle data = msg.getData();
                // Retrieve the user input user name
                usr = data.getString("USERNAME");
                // Get the password of user input
                pwd = data.getString("PASSWORD");
                Log.i(TAG, "user: " + usr + " pass: " + pwd);
                pingDeviceListener = new PingDeviceListener() {

                    @Override
                    public void onFinish(boolean flag) {
                        if (flag) {
                            LoginStatus localLoginStatus = new LoginStatus();
                            // Landing samba equipment
                            login(usr, pwd, localLoginStatus);

                        } else {
                            // Equipment, not with
                            refreshUIListener
                                    .onFailed(Constants.FAILED_TIME_OUT);
                        }
                    }
                };

                // Before landing samba equipment ping the equipment
                pingDevice(pingDeviceListener);
                break;
            default:
                break;
            }

        }
    };

    /*
     * Call samba interface complete landing operation.
     */
    private void login(final String usr, final String pwd, LoginStatus onRecvMsg) {
        if (usr.length()==0 && pwd.length()==0) {
            // Anonymous Logon
            smbDevice.setAuth(null);
        } else {
            // Must input the user name if it has username(Can no password)
            SmbAuthentication auth = new SmbAuthentication(usr, pwd);
            smbDevice.setAuth(auth);
        }
        smbDevice.setStorageManager(mStorageManager);
        smbDevice.setOnRecvMsg(onRecvMsg);
        // Tips are landing
        loginSambaListener.onEnd(SambaDataBrowser.LOGIN_SAMBA);
        mSmbShareFolderLists.clear();
        try {
            mSmbShareFolderLists = smbDevice.getSharefolderList();
            if (mSmbShareFolderLists!=null && Tools.isUseHttpSambaModeOn()) {
                startScanAfterLoginDone();
            }
        }catch(Exception e){
            onRecvMsg.onRecvMsg(OnRecvMsg.NT_STATUS_LOGON_FAILURE);
        }

    }

    private void startScanAfterLoginDone(){
        Log.i(TAG,"startScanAfterLoginDone");
        // Reset focus and the current page numbe
        if (nanohttp!=null) {
           Log.i(TAG, "setmName:" + mSambaName + ",setmPassword:" + mSambaPassword);
           HttpBean.setmName(mSambaName);
           HttpBean.setmPassword(mSambaPassword);
        }
        position = 0;
        currentPage = 1;
        totalPage = 1;

        loginSambaListener.onEnd(SambaDataBrowser.LOAD_SAMBA_SOURCE);
        startScanSmbShareFolder(mSmbShareFolderLists);
    }

    /*
     * Initialization scanning samba equipment thread.
     */
    private void findSambaDevice() {
        // Search samba equipment thread
        findHostThread = new Thread(new FindHostRunnable());
        findHostThread.start();
    }

    /************************************************************************
     * Search SAMBA equipment thread realize area
     ************************************************************************/
    private class FindHostRunnable implements Runnable {

        @Override
        public void run() {
            smbClient = new SmbClient();
            smbClient.SetPingTimeout(500);
            smbClient.setOnRecvMsgListener(new OnRecvMsgListener() {
                public void onRecvMsgListener(int msg) {
                    switch (msg) {
                    case OnRecvMsgListener.MSG_UPDATE_DEVLIST_CANCEL:
                        System.out.println("scan cancel!");
                        break;

                    case OnRecvMsgListener.MSG_UPDATE_DEVLIST_ADD:
                        System.out.println("scan add!");
                        //position = 0;
                        //currentPage = 1;
                        updataDeviceListItem();
                        break;

                    case OnRecvMsgListener.MSG_UPDATE_DEVLIST_DONE:
                        System.out.println("scan completed ");
                        notifyScanCompleted();
                        if (!mIsConnectingHttpServer) {

                            Log.i(TAG, "scan completed,http server start");
                            File wwwroot = new File(".").getAbsoluteFile();
                            int port = 8088;
                            try {
                                mIsConnectingHttpServer = true;
                                nanohttp = new NanoHTTPD(port, wwwroot);//start http server
                            } catch ( IOException ioe ) {
                                Log.i(TAG, "Couldn't start server:"+ioe);
                            }
                        }
                        break;
                    }
                }
            });
            loginSambaListener.onEnd(SambaDataBrowser.LOAD_SAMBA_DEVICE);

            // Call interface start searching samba equipment data
            smbClient.updateSmbDeviceList();
        }
    }
    public void stopHttpServer(){
        if (nanohttp!=null && mIsConnectingHttpServer) {
            mIsConnectingHttpServer = false;
            nanohttp.stop();
            nanohttp = null;
        }
    }
    /************************************************************************
     * Landing samba equipment state interface implementation area
     ************************************************************************/
    private class LoginStatus implements OnRecvMsg {

        @Override
        public void onRecvMsg(int arg0) {
            switch (arg0) {
            case OnRecvMsg.NT_STATUS_WRONG_PASSWORD:
                Log.d(TAG, "wrong password");
                refreshUIListener.onFailed(Constants.FAILED_WRONG_PASSWD);

                break;

            case OnRecvMsg.NT_STATUS_LOGON_FAILURE:
                Log.d(TAG, "login failed ");
                refreshUIListener.onFailed(Constants.FAILED_LOGIN_FAILED);

                break;
            case OnRecvMsg.NT_STATUS_OK:
                Log.d(TAG, "login ok");

                state = SCAN_FILE;
                // After the success of the land will be the current page number
                // and focus information press in the stack
                int viewMode =-100;
                if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
                    viewMode =1;
                else
                    viewMode =0;
                ReturnData rd = new ReturnData(viewMode,Constants.RETURN_SAMBA,
                        currentPage, position, "");
                returnStack.push(rd);

                SmbAuthentication localSmbAuth = smbDevice.getAuth();
                if (localSmbAuth != null) {
                    //save  username and password to database
                    LoginInfoDBAdapter     dbAdapter = new LoginInfoDBAdapter(SambaDataManager.this.activity);
                    dbAdapter.open();
                    String saved_ip = smbDevice.getAddress();
                    String saved_usr = localSmbAuth.getName();
                    mSambaName = saved_usr;
                    String saved_pwd = localSmbAuth.getPassword();
                    mSambaPassword = saved_pwd;
                    String savedstr[] = {"", ""};
                    dbAdapter.query_loginInfo(saved_ip, savedstr);
                    if(!savedstr[1].trim().equals(saved_pwd.trim())  || !savedstr[0].trim().equals(saved_usr.trim())){
                        Log.d(TAG, "login info is different, to save new info.");
                        try{
                           dbAdapter.delete_loginInfo(saved_ip, saved_usr);
                        }
                        catch(Exception ex) {
                            Log.d(TAG, "maybe no login info.");
                        }
                        dbAdapter.insert_loginInfo(saved_ip, saved_usr, saved_pwd);
                    }
                    dbAdapter.close();
                    Log.d(TAG, "save  user login info  done.");
                }
                if (!Tools.isUseHttpSambaModeOn()){
                    smbDevice.mount(localSmbAuth);
                }
                // Specify the root directories of the scanning
                currentPath = activity.getString(R.string.sambarootDirectory);
                Log.i(TAG, "scan device path : " + currentPath);
                if (Tools.isUseHttpSambaModeOn()) {
                    return;
                }
                if (smbDevice.isMounted()) {
                    // Reset focus and the current page numbe
                    position = 0;
                    currentPage = 1;
                    totalPage = 1;

                    loginSambaListener
                            .onEnd(SambaDataBrowser.LOAD_SAMBA_SOURCE);
                    // Landing successful,Start scanning data
                    startScan(currentPath);
                } else {
                    loginSambaListener.onEnd(SambaDataBrowser.MOUNT_FAILED);
                    // in order to avoid Mount failed  be caused by Device or resource busy.
                    // Therefore , the unmount should be execute.
                    smbDevice.unmount();
                    state = SCAN_HOST;
                    smbDevice = null;
                }

                break;
            case NET_STATUS_NOT_SUPPORT:
            case OnRecvMsg.NT_STATUS_UNSUCCESSFUL:
            default:
                refreshUIListener.onFailed(Constants.FAILED_LOGIN_OTHER_FAILED);
                break;
            }
        }
    }



    public final synchronized void startScanSmbShareFolder(final List<SmbShareFolder> tmpLists) {
        // Startup thread scanning
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "startScanSmbShareFolder");
                    getFileFromSmbShareFolder(tmpLists);
                } catch (Exception e) {
                    Log.i("SCAN", "********scan**fail**********");
                }

                // Data scan complete
                onFinish();
            }
        }).start();

    }

    /*
     * Scanning the specified directory of all files or folders.
     */
    private void getFileFromSmbShareFolder(List<SmbShareFolder> tmpLists) {
        Log.i(TAG, "getFileFromSmbShareFolder");

        List<BaseData> localFile = new ArrayList<BaseData>();
        List<BaseData> localFolder = new ArrayList<BaseData>();
        List<BaseData> localPicture = new ArrayList<BaseData>();
        List<BaseData> localSong = new ArrayList<BaseData>();
        List<BaseData> localVideo = new ArrayList<BaseData>();

        try {
            for (SmbShareFolder f : tmpLists) {
                String name = f.getFileName();
                BaseData file = new BaseData();
                Log.i(TAG,"getFileFromSmbShareFolder samba name:"+name);
                file.setName(name);
                file.setPath(f.getPath());
                //file.setParentPath(Constants.RETURN_SAMBA);
                if (f.isDirectory()) {
                    file.setType(Constants.FILE_TYPE_DIR);
                    localFolder.add(file);
                } else {
                    int pos = name.lastIndexOf(".");
                    String extension = "";
                    if (pos > 0) {
                        extension = name.toLowerCase().substring(pos + 1);
                        file.setFormat(extension);
                    }

                    String formatSize = Tools.formatSize(BigInteger.valueOf(f.getLength()));
                    file.setSize(formatSize);
                    file.setDescription(formatSize);


                    if (check(name, resource.getStringArray(R.array.photo_filter))) {
                        file.setType(Constants.FILE_TYPE_PICTURE);

                        localPicture.add(file);

                    } else if (check(name,
                            resource.getStringArray(R.array.audio_filter))) {
                        file.setType(Constants.FILE_TYPE_SONG);

                        localSong.add(file);

                    } else if (check(name,
                            resource.getStringArray(R.array.video_filter))) {
                        file.setType(Constants.FILE_TYPE_VIDEO);

                        localVideo.add(file);

                    } else if (check(name,
                            resource.getStringArray(R.array.playlist_filter))) {
                        file.setType(Constants.FILE_TYPE_MPLAYLIST);
                    } else {
                        file.setType(Constants.FILE_TYPE_FILE);
                    }
                    localFile.add(file);
                }
            }
        } catch (Exception e) {

        }
        mediaContainer.clearAll();
        if (localFile.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_FILE, localFile);
        }
        if (localFolder.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_DIR, localFolder);
        }
        if (localPicture.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_PICTURE, localPicture);
        }
        if (localSong.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_SONG, localSong);
        }
        if (localVideo.size() > 0) {
            putAllToCache(Constants.FILE_TYPE_VIDEO, localVideo);
        }

    }
}
