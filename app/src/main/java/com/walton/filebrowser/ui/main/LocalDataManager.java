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
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore.Audio;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.data.ReturnData;
import com.walton.filebrowser.business.data.ReturnStack;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.StorageProgress;
import com.walton.filebrowser.util.Tools;

public class LocalDataManager extends BaseDataManager {

    private static final String TAG = "LocalDataManager";

    public static String ROOT_PATH = "";

    // Local equipment displays a status
    protected static final int STATUS_DEVICE_DISPLAY = 0x01;

    // Local resources display state
    protected static final int STATUS_RESOURCE_DISPLAY = 0x02;

    private StorageManager storageManager;

    private Activity activity;

    // Stack, memory into the file location to return to its original position
    private ReturnStack returnStack;

    private RefreshUIListener refreshUIListener;

    private List<BaseData> deviceList = new ArrayList<BaseData>();

    // Back to display UI with data list
    private List<BaseData> uiData = new ArrayList<BaseData>();

    // The current page number and total number of pages
    private int currentPage = 1, totalPage = 1;

    // Status flag
    private int state = STATUS_RESOURCE_DISPLAY;

    // File type
    private int activityType;

    // Focus position
    private int position = 0;

    public int switchViewMode =0;

    // File type
    private int type;

    // The current directory browsing the absolute path
    private String currentDirectory;

    private MediaThumbnail mediaThumbnail =null;
    // The database music file field
    private static final String[] AUDIO_PROJECTION = new String[] {
            Audio.Media._ID, Audio.Media.DATA, Audio.Media.SIZE,
            Audio.Media.DISPLAY_NAME, Audio.Media.MIME_TYPE, Audio.Media.TITLE,
            Audio.Media.DURATION, Audio.Media.ARTIST, Audio.Media.ALBUM,
            Audio.Media.ALBUM_ID };

    public static String p;

    public LocalDataManager(Activity activity, RefreshUIListener listener) {
        super(activity.getResources());

        this.activity = activity;
        this.returnStack = new ReturnStack();
        this.refreshUIListener = listener;
        // The root directory/mnt/usb/
        this.currentDirectory = p;//Tools.getUSBMountedPath();
        this.ROOT_PATH = this.currentDirectory;
        Log.i(TAG,"currentDirectory getUSBMountedPath:"+this.currentDirectory);
        // Used to obtain disk list
        this.storageManager = (StorageManager) activity.getApplicationContext()
                .getSystemService(Context.STORAGE_SERVICE);
    }

    @Override
    public void dataListEmpty(boolean isData) {
        Log.d(TAG, "dataListEmpty: lolk");

        Intent intent = new Intent("com.walton.datainfo");
        intent.putExtra("info",isData);
        activity.sendBroadcast(intent);
    }

    @Override
    public void onFinish() {
        // Structure UI display data

        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            constructGridCurrentPage(activityType);
        } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            constructCurrentPage(activityType);
        }
        // set position to 0 while switch viewMode cause it's as the same as enterDir
        if (1==switchViewMode) {
            position=0;
            switchViewMode=0;
        }
        // Inform interface refresh
        refreshUIListener.onFinish(uiData, currentPage, totalPage, position);

    }

    /**
     * Scanning specify the location of the corresponding data directory.
     *
     * @param index
     *            Focus position.
     * @param type
     *            Media types.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void browser(int index, int type) {
        this.activityType = type;
        if (index == -1) {
            position = 0;
            // Display the current mount usb equipment
            showUSBDevice();

            // Previous directory
            // here index 0 means press back button
        } else if (index == -2) {
            Log.d(TAG, "browser: Enter parent directory == 0 ");
//            enterDirectory(index);
            enterParentDirectory();

            // Enter the next level directory
        } else {
            enterDirectory(index);
        }

    }

    /**
     * @param page
     *            Turn the page index. </br> <li>1 : Said downward turn the
     *            page. <li>0 : Said the first page. <li>-1 : Said page up.
     * @param type
     *            Media types, 0 no practical significance.
     */
    protected void getCurrentPage(int page, int type) {

        if (type == 0) {
            // page up
            if (page == -1) {

                if (currentPage > 1) {
                    currentPage--;
                    if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
                        position=Constants.GRID_MODE_DISPLAY_NUM;
                    else
                        position = Constants.GRID_MODE_DISPLAY_NUM;
                } else {
                    return;
                }

                // page down
            } else if (page == 1) {
                if (currentPage < totalPage) {
                    currentPage++;
                    position = 0;
                } else {
                    return;
                }
            }

            // File filtering
        } else {
            this.activityType = type;
            position = Constants.POSITION_0;
            currentPage = 1;
        }

        // Structure the current page data
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
            constructGridCurrentPage(activityType);
        else
            constructCurrentPage(activityType);

        // Inform interface refresh UI
        refreshUIListener.onFinish(uiData, currentPage, totalPage, position);

    }

    /**
     * Get the current directory all designated type of media files.
     *
     * @param type
     *            Media types.
     * @param position
     *            Focus position.
     * @return The current players play index.
     */
    protected int getMediaFile(int type, int position) {
        // Obtain all media files
        ArrayList<BaseData> mediaFiles = new ArrayList<BaseData>();
        // get all the data
        List<BaseData> allFiles = new ArrayList<BaseData>();
        // file type switch in all types mode
        if (type < 0) {
            Log.d("jdk", "type===" + type);
            allFiles.addAll(getUIDataList(Constants.OPTION_STATE_ALL));
            mediaFiles.addAll(getMediaFileList(-type));

            // file type switch in pictures, music, or video mode
        } else {
            allFiles.addAll(getUIDataList(type));
            mediaFiles.addAll(getMediaFileList(type));
        }

        // Index conversion
        int index=-100;
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            index= (currentPage - 1) * Constants.GRID_MODE_DISPLAY_NUM+ position;
        } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            index = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM+ position;
        }
        int size = allFiles.size();
        Log.d(TAG, "all media file size : " + mediaFiles.size()
                + " currentPage : " + currentPage + " position : " + position);
        Log.d("jdk", "index===" + index + "size==" + size);
        if (index >= 0 && index < size) {
            BaseData bd = allFiles.get(index);
            String path = bd.getPath();

            index = 0;
            // Calculating the current click the media file subscript
            for (BaseData item : mediaFiles) {
                if (path.equals(item.getPath())) {
                    Log.d("jdk", "path===" + path + ",item.getPath()=" + item.getPath());
                    return index;
                } else {
                    index++;
                }
            }
        }

        return 0;
    }

    /**
     * Get the current directory all designated type
     *
     * @param position
     *            Focus position.
     * @return The current players play index.
     */
    protected BaseData getCommonFile(int position) {
        // get all the data
        List<BaseData> allFiles = new ArrayList<BaseData>();
        allFiles.addAll(getUIDataList(Constants.OPTION_STATE_ALL));
        // Index conversion
        int index = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM
                + position - 1;
        int size = allFiles.size();
        Log.d(TAG, " currentPage : " + currentPage + " position : " + position+"  allFiles.size:"+size);

        if (index >= 0 && index < size) {
            BaseData bd = allFiles.get(index);
            String path = bd.getPath();
            Log.v(TAG, "getCommonFile:"+path);
            return bd;
        }
        return null;
    }
    public int getLocalDataBrowserState(){
        return state;
    }

    /**
     * Receiving to disk pull plug event to scan and display disk data.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void showUSBDevice() {
        Log.i(TAG, "showUSBDevice: ");
        // Data browsing in a disk list
        state = STATUS_DEVICE_DISPLAY;
        currentPage = 1;
        totalPage = 1;

        // to empty the current cache data
        uiData.clear();

        // loading usb devices
        loadUSBDevice();

        // to add a return to term
//        BaseData baseData = new BaseData(Constants.FILE_TYPE_RETURN);
//        baseData.setName(activity.getString(R.string.back));
//        baseData.setDescription(Constants.RETURN_TOP);
//        uiData.add(baseData);

        if (Tools.showRootDir()) {
            BaseData bd1 = new BaseData(Constants.FILE_TYPE_DIR);
            bd1.setName("/");
            bd1.setPath("/");
            uiData.add(bd1);
        }

        // disk total number
        int size = deviceList.size();
        Log.i(TAG,"deviceList.size():"+String.valueOf(deviceList.size()));
        int perPageDisplayNum=-100;
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
            perPageDisplayNum=Constants.GRID_MODE_DISPLAY_NUM;
        } else {
            perPageDisplayNum=Constants.LIST_MODE_DISPLAY_NUM;
        }
        if (size > 0) {
            // calculation disk total number of pages
            totalPage = size / perPageDisplayNum;
            totalPage += size % perPageDisplayNum == 0 ? 0 : 1;

            int tail = 0;
            if (size > currentPage * perPageDisplayNum) {
                tail = currentPage * perPageDisplayNum;
            } else {
                tail = size;
            }

            // Calculation for the data starting subscript
            int i = (currentPage - 1) * perPageDisplayNum;
            //just add one page's data into the uidata
            for (; i < tail; i++) {
                BaseData bd = deviceList.get(i);
                uiData.add(bd);
            }

        }

        refreshUIListener.onFinish(uiData, currentPage, totalPage, position);

    }

    /**
     * @param unmount
     *            Disk plug for when the broadcast
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void showUSBDevice(final String unmount) {
        // page in the equipment list page or remove the disk data browse pages,
        // update data
        if (state == STATUS_DEVICE_DISPLAY
                || (unmount != null && currentDirectory.contains(unmount))) {
            Log.d(TAG, "state" + state + " unmount : " + unmount
                    + " currentDirectory : " + currentDirectory);

            // display loading tooltip, here is not wrong, just use onFailed to
            // complete
            refreshUIListener.onFailed(Constants.UPDATE_DISK_DEVICE);

            // update device list
            showUSBDevice();
        }

    }

    /***********************************************************
     * private methods area
     ***********************************************************/

    /*
     * construct current page data list.
     */
    private void constructCurrentPage(int type) {
        // for display data
        List<BaseData> list = new ArrayList<BaseData>();
        if (state == STATUS_DEVICE_DISPLAY) {
            list.addAll(deviceList);
        } else {
            // access to current type of data
            list.addAll(getUIDataList(type));
        }

        // empty before the data
        uiData.clear();
        // add return items, each page for a return to the first paragraph
        addReturn();

        int size = list.size();
        if (size > 0) {
            totalPage = size / Constants.LIST_MODE_DISPLAY_NUM;
            totalPage += size % Constants.LIST_MODE_DISPLAY_NUM == 0 ? 0 : 1;

            int tail = 0;
            if (size > currentPage * Constants.LIST_MODE_DISPLAY_NUM) {
                tail = currentPage * Constants.LIST_MODE_DISPLAY_NUM;
            } else {
                tail = size;
            }
            Log.d(TAG, "size : " + size + " tail : " + tail + "totalPage:"
                    + totalPage);

            int i = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM;
            for (; i < tail; i++) {
                BaseData bd = list.get(i);
                uiData.add(bd);
            }

        } else {
            totalPage = 1;
        }

    }
    private void constructGridCurrentPage(int type) {
        // for display data
        List<BaseData> list = new ArrayList<BaseData>();
        if (state == STATUS_DEVICE_DISPLAY) {
            list.addAll(deviceList);
            Log.d(TAG, "constructGridCurrentPage: Device_Display");
        } else {
            Log.d(TAG, "constructGridCurrentPage: Non Device_Display");
            // access to current type of data
            list.addAll(getUIDataList(type));
        }

        // empty before the data
        uiData.clear();
        // add return items, each page for a return to the first paragraph
//        addReturn();
        if (list.size() > 0){

        }else {
            Log.d(TAG, "constructCurrentPage: no data");

        }
        int size = list.size();
        if (size > 0) {
            totalPage = size / Constants.GRID_MODE_DISPLAY_NUM;
            totalPage += size % Constants.GRID_MODE_DISPLAY_NUM == 0 ? 0 : 1;

            int tail = 0;
            if (size > currentPage * Constants.GRID_MODE_DISPLAY_NUM) {
                tail = currentPage * Constants.GRID_MODE_DISPLAY_NUM;
            } else {
                tail = size;
            }
            Log.d(TAG, "size : " + size + " tail : " + tail + "totalPage:"
                    + totalPage);

            int i = (currentPage - 1) * Constants.GRID_MODE_DISPLAY_NUM;
            Log.d(TAG, "constructGridCurrentPage: Tail"+tail);
            for (; i < tail; i++) {
                BaseData bd = list.get(i);
                uiData.add(bd);
            }

        } else {
            totalPage = 1;
        }

    }

    /*
     * returns to the directory at the next higher level.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void enterParentDirectory() {
        // Stack there are data is previous directory
        if (returnStack.getTankage() > 0) {

            ReturnData rd = returnStack.pop();
            currentDirectory = rd.getId();
            position = rd.getPosition();
            currentPage = rd.getPage();
            int viewMode = rd.getViewMode();
            ROOT_PATH = currentDirectory;
            Log.i(TAG, "returns path:" + currentDirectory);

            int tmp=0;
            if (1==viewMode) {
                tmp = (currentPage - 1) * Constants.GRID_MODE_DISPLAY_NUM + position;
            } else {
                tmp = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM + position;
            }
            int tmpPos = position;
            if (0==viewMode && Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                // changeReturnDataList2Grid
                // tmp+1 is the true digit without counting "back" per page
                int mod =(tmp)%(Constants.GRID_MODE_DISPLAY_NUM);
                currentPage =(tmp)/(Constants.GRID_MODE_DISPLAY_NUM);
                if (mod>0)
                    currentPage++;
                if (mod>0)
                    position=mod;
                else if (0==mod&&tmpPos!=0)
                    position=Constants.GRID_MODE_DISPLAY_NUM;



            } else if (1==viewMode&& Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
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
            // usb root directory
            if (currentDirectory != null
                    && (currentDirectory.equals("/mnt")||currentDirectory.equals("/storage")
                    || currentDirectory.equals("/storage/emulated"))) {
                showUSBDevice();

                // not usb root directory
            } else {
                // scanning current path of the resources
                startScan(currentDirectory);
            }
        }

    }

    /*
     * record into the file when the position.
     */
    private void enterDirectory(int index) {
        // reset focus position
        position = 0;

        // in the folder path
        String id = null;
        int viewMode = -100;
        if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag)
            viewMode = 1;
        else
            viewMode = 0;

        // USB device page
        if (state == STATUS_DEVICE_DISPLAY) {
            if (index >= 0 && index < uiData.size()) {
                state = STATUS_RESOURCE_DISPLAY;

                BaseData bd = uiData.get(index);
                // mnt/usb/
                id = Tools.getUSBMountedPath();
                Log.i(TAG, "stack file path " + id);
                currentDirectory = bd.getPath();
                ROOT_PATH = currentDirectory;
                // Stack, return operation returns to the specified page and
                // position
                ReturnData rd = new ReturnData(viewMode,id, currentPage, index);
                returnStack.push(rd);

                Log.i(TAG, "push stack page:" + currentPage + " index : "
                        + index);
            }

            // resources page
        } else if (state == STATUS_RESOURCE_DISPLAY) {
            Log.d(TAG, "enterDirectory: STATUS_RESOURCE_DISPLAY");
            List<BaseData> list = new ArrayList<BaseData>();
            // according to type for different data
            list.addAll(getUIDataList(activityType));

            // subscript index
            int tmp = 0;
            // subscript index conversion
            // parentDir:grid(use to back and enter)
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                tmp = (currentPage - 1) * Constants.GRID_MODE_DISPLAY_NUM + index;
            } else if (Constants.LISTVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                tmp = (currentPage - 1) * Constants.LIST_MODE_DISPLAY_NUM + index;
            }
            if (tmp >= 0 && tmp < list.size()) {
                BaseData bd;
                if (tmp < list.size()) {
                    bd = list.get(tmp);
                    currentDirectory = bd.getPath();
                    id = bd.getParentPath();
                } else {
                    Log.e(TAG, "invalid index in browser, index : " + index);
                    return;
                }
                Log.i(TAG, "activityType:" + activityType);
                Log.i(TAG, "stack file path:" + id);

                type = bd.getType();
                // stack, return operation returns to the specified page and position
//                if (type == Constants.FILE_TYPE_DIR) {
                ReturnData rd = new ReturnData(viewMode,id, currentPage, index);
                returnStack.push(rd);
//                }

                Log.i(TAG, "push stack page:" + currentPage + "index" + index);
            }
        }

        Log.d(TAG, "enterDirectory: currentDirectory "+currentDirectory);

        currentPage = 1;
        totalPage = 1;

        // scanning current path of the resources
        startScan(currentDirectory);

    }

    /*
     * add to return to a
     */
    private void addReturn() {
        String usbMountedPath = Tools.getUSBMountedPath();
        Log.d(TAG, "usbMountedPath : " + usbMountedPath
                + " currentDirectory : " + currentDirectory);
        // the root directory, the equipment list page
        if (currentDirectory.equals(usbMountedPath)) {
            Log.i(TAG, "STATUS_LOCAL_DEVICE_DISPLAY");
            state = STATUS_DEVICE_DISPLAY;

            // comment below code because we hide back button from gridview

            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_TOP);
            uiData.add(di);
            // Resources page
        } else {
            Log.i(TAG, "STATUS_LOCAL_RESOURCE_DISPLAY");
            state = STATUS_RESOURCE_DISPLAY;

            // comment below code because we hide back button from gridview

            BaseData di = new BaseData(Constants.FILE_TYPE_RETURN);
            di.setName(activity.getString(R.string.back));
            di.setDescription(Constants.RETURN_LOCAL);
            uiData.add(di);
        }

    }

    /*
     * Loading usb disk equipment.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadUSBDevice() {
        Log.i(TAG, "loadUSBDevice: ");
        // Empty disk list before
        deviceList.clear();

        // Get the current all hang load disk information
        StorageVolume[] volumes = storageManager.getVolumeList();
        // At present there is no any disk mount
        if (volumes == null || volumes.length == 0) {
            return;
        }
        Log.i(TAG,"volumes.length:"+String.valueOf(volumes.length));
        String path = "";
        // List all your mount disk
        for (StorageVolume item : volumes) {
            path = item.getPath();
            String state = storageManager.getVolumeState(path);
            // Mount is not successful
            Log.i(TAG,"StorageVolume path state:"+path+"  "+state);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                continue;

                // Successful mount
            } else {
                BaseData bd = new BaseData(Constants.FILE_TYPE_DIR);
                // bd.setName(storageManager.getVolumeLabel(path));
                String name = path.substring(path.lastIndexOf("/") + 1,
                        path.length());
                bd.setName(item.getDescription(activity));
                bd.setPath(path);
                bd.setStorageTotal(new StorageProgress(activity, path).getProgress()[0]);
                bd.setStorageFree(new StorageProgress(activity, path).getProgress()[1]);
                bd.setStorageUses(new StorageProgress(activity, path).getProgress()[2]);
                deviceList.add(bd);
            }
        }

    }

}
