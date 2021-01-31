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


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnFocusChangeListener;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.walton.filebrowser.business.adapter.DataAdapter;
import com.walton.filebrowser.business.adapter.GridAdapter;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.data.ViewMode;
import com.walton.filebrowser.ui.video.FloatVideoController;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.ToastFactory;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.video.DatabaseHelper;
import com.walton.filebrowser.R;

import com.mstar.android.tv.TvCommonManager;

public class FileBrowserActivity extends Activity {

    private static final String TAG = "FileBrowserActivity";

    // control container class
    private FileBrowserViewHolder holder = null;

    // homepage surface data browsing class
    private TopDataBrowser topDataBrowser = null;

    // local usb disk data browsing class
    private LocalDataBrowser localDataBrowser = null;

    // samba data browsing class
    private SambaDataBrowser sambaDataBrowser = null;

    // dlna data browsing class
    private DlnaDataBrowser dlnaDataBrowser = null;

    // listView adapter
    private DataAdapter adapter = null;

    // the flag means what the view mode now is
    public static int mListOrGridFlag = Constants.GRIDVIEW_MODE;
    // make gridview open or not while enter LocalMM first time
    public static int isOrNotGridViewFirst = Constants.GRIDVIEW_MODE_FIRST;

    public static int clickBtnswitch = 0;

    public static int backAndNextViewFormat = 0;

    public static ArrayList<Drawable> mFileTypeDrawable = new ArrayList<Drawable>();

    private OnClickListener switchPage;

    private ArrayList<BaseData> tmpArray = new ArrayList<BaseData>();

    public static GridAdapter gridAdapter;

    private Resources resourceGrid;

    private static int positionFocusNow = 0;

    private int mItemSelectedPosition = 0;

    private Drawable listviewModeDrawable;

    private Drawable gridviewModeDrawable;

    // listView data source
    private List<BaseData> sourceData = new ArrayList<BaseData>();

    private List<BaseData> desDataList = new ArrayList<BaseData>();

    // currently browsing data sources
    private int m_dataSource = Constants.BROWSER_TOP_DATA;

    private int lastDataSource = Constants.BROWSER_TOP_DATA;

    // temporary save data type
    protected int tmpType = Constants.OPTION_STATE_ALL;

    // currently browsing data types
    protected int dataType = Constants.OPTION_STATE_ALL;

    // Touch events when DOWN the y coordinate
    private int motionY = 0;

    // key shielding identification
    private boolean m_canResponse = true;

    private TvCommonManager appSkin = null;

    //video thumbnail
    private MediaThumbnail mediaThumbnail;

    protected PopupWindow popupWindow;

    private static Toast toast;

    protected ArrayList<ViewMode> viewModeList;
    protected boolean viewModeChange = false;

    private String mPlatform;

    private IntentFilter filter = new IntentFilter("com.walton.datainfo");

    public String usb_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_list);
        showVersion();

        usb_path = getIntent().getStringExtra("usb_path");

        registerReceiver(broadcastReceiver, filter);

        // include file browsing interface all control class
        holder = new FileBrowserViewHolder(this, handler);
        // initialize all file browsing interface control
        holder.findViews();

        //topDataBrowser = new TopDataBrowser(this, handler, sourceData);
        localDataBrowser = new LocalDataBrowser(this, handler, sourceData);
        adapter = new DataAdapter(this, handler, sourceData);
        holder.listView.setAdapter(adapter);
        holder.listView.setOnItemClickListener(onItemClickListener);
        holder.listView.setOnTouchListener(onTouchListener);
        holder.listView.setOnItemSelectedListener(onItemSelectedListener);
        holder.listView.setOnKeyListener(onKeyListener);

//        dlnaDataBrowser = new DlnaDataBrowser(this, handler, sourceData);
//        sambaDataBrowser = new SambaDataBrowser(this, handler, sourceData);

        handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
//        handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);

        IntentFilter filter = new IntentFilter();
        filter.addAction(VideoPlayerActivity.ACTION_CHANGE_SOURCE);
        this.registerReceiver(sourceChangeReceiver, filter);

        DatabaseHelper dbHelper = new DatabaseHelper(MediaContainerApplication.getInstance(), Constants.DB_NAME);
        dbHelper.getReadableDatabase();

        if (!Tools.unSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onKeyInfo");
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch arg0:" + arg0 + " arg1:" + " arg2:" + arg2);
                    TvCommonManager.getInstance().setInputSource(arg1);
                    // ComponentName componentName = new ComponentName("mstar.tvsetting.ui",
                    //      "com.mstar.android.intent.action.START_TV_PLAYER");
                    Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    //intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.putExtra("task_tag", "input_source_changed");
                    FileBrowserActivity.this.startActivity(intent);
                    return false;
                }
            });
        }
        //GridView Adapter
        gridAdapter = new GridAdapter(this, handler, tmpArray);
        holder.gridView.setAdapter(gridAdapter);
        resourceGrid = getResources();
        creatFileIconDrawable();

        //GridView relative code Start---
        switchPage = new OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.createPopWindow();
            }

        };
        holder.btnSwitch.setOnClickListener(switchPage);
        holder.gridView.setOnItemClickListener(onItemClickListener);
        holder.gridView.setOnTouchListener(onTouchListener);
        holder.gridView.setOnItemSelectedListener(onItemSelectedListener);
        holder.gridView.setOnKeyListener(onKeyListener);

        focusLeftFirst();

        //holder.btnSwitch.setOnHoverListener(btnSwitchOnHoverListener);
        //holder.viewModeImg.setOnHoverListener(viewModeImgOnHoverListener);
        //holder.btnSwitch.setOnFocusChangeListener(btnSwitchOnFocusChangeListener);
        //the first enter time is default logic of mListOrGridFlag,else enter lasted time user's choose
        mPlatform = Tools.getHardwareName();
        Log.i(TAG, "mPlatform:" + mPlatform);
        isOrNotGridViewFirst = getSharedPreferences("localmm_sharedPreferences", Context.MODE_PRIVATE).getInt("listOrGridFlag", isOrNotGridViewFirst);
        //GridView relative code End---
    }

    private void focusLeftFirst() {
        tmpType = dataType;
        holder.setLeftFocus(dataType, true);
        holder.setGridViewFocus(false, 0);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "---------- onPause ---------");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---------- onStop ---------");
        gridAdapter.cancelTask(true);
        super.onStop();
    }

    private void creatFileIconDrawable() {
        Drawable drawable = null;
        drawable = resourceGrid.getDrawable(R.drawable.icon_file_file2);
        mFileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_pic2);
        mFileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_song2);
        mFileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_video2);
        mFileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.file_icon_folder_h);
        mFileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_return2);
        mFileTypeDrawable.add(drawable);
    }


    private void changeFocusWhenL2G() {
        holder.gridView.setVisibility(View.VISIBLE);
        holder.listView.setVisibility(View.GONE);
        holder.setBtnSwitchFocus(false);
        holder.setGridViewFocus(true, 0);
        holder.setListViewFocus(false, 0);
        saveSharedPreferences(0);
    }

    private void changeFocusWhenG2L() {
        holder.gridView.setVisibility(View.GONE);
        holder.listView.setVisibility(View.VISIBLE);
        holder.setBtnSwitchFocus(false);
        holder.setGridViewFocus(false, 0);
        holder.setListViewFocus(true, 0);
        saveSharedPreferences(0);
    }

    @Override
    protected void onResume() {

        Log.d(TAG, "onResume");
//        if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
//            holder.viewModeImg.setBackgroundResource(R.drawable.listview_mode);
//        } else {
//            holder.viewModeImg.setBackgroundResource(R.drawable.gridview_mode);
//        }

        holder.viewModeImg.setBackgroundResource(R.drawable.gridview_mode);

        if (Tools.isFloatVideoPlayModeOn()) {
            FloatVideoController.getInstance().hideFloatVideoWindow();
        }
        findViewById(R.id.rootLinearLayout).setVisibility(View.VISIBLE);
        setCanResponse(true);
        // switching source to storage
        Tools.changeSource(true);

        // registration network monitoring radio receiver
        IntentFilter networkFilter = new IntentFilter(
                "com.mstar.localmm.network.disconnect");
        registerReceiver(networkReceiver, networkFilter);

        // registered disk pull plug radio receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        //filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        registerReceiver(diskChangeReceiver, filter);
        registerReceiver(homeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        IntentFilter finishFileBrowserActivityFilter = new IntentFilter("action_finish_filebrowseractivity");
        registerReceiver(finishFileBrowserActivityReceiver, finishFileBrowserActivityFilter);
        IntentFilter wallpaperChangeFilter = new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED);
        registerReceiver(mWallpaperChangeReceiver, wallpaperChangeFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.initToolsModuleConfig();
                // In order to save time , check whether these supported in thread and note it in their property.
                // Mantis:1306123
                Tools.isSupportDualDecode();
                Tools.isSupportNativeAudio();
                Tools.isSupportUseHttpSamba();
            }
        }).start();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "----------onRestart------------");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
//        sambaDataBrowser.unmount();
        super.onDestroy();

        // When LMM Destroyed, Launcher can auto-change source,
        // So LMM do not need changeSource again.
        // Tools.changeSource(false);

        // release resources
//        release();
//
//        // reverse registering listeners
//        unregisterReceiver(broadcastReceiver);
//        unregisterReceiver(diskChangeReceiver);
//        unregisterReceiver(networkReceiver);
//        unregisterReceiver(sourceChangeReceiver);
//        unregisterReceiver(homeKeyEventBroadCastReceiver);
//        unregisterReceiver(finishFileBrowserActivityReceiver);
//        unregisterReceiver(mWallpaperChangeReceiver);
//        System.gc();
//
//        finish();
//
//        if (Tools.isFloatVideoPlayModeOn()) {
//            FloatVideoController.getInstance().showFloatVideoWindow();
//            FloatVideoController.getInstance().getVideoListItem(Constants.DB_NAME, Constants.VIDEO_PLAY_LIST_TABLE_NAME);
//        }
//
//        // kill process and recycle resource
//        Process.killProcess(Process.myPid());   // psqiu 测试
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp keyCode:" + keyCode + " getCurrentDataSource():" + getCurrentDataSource());
        // Operation time consuming operation, will not escape key shielding
        // Key shielding
        if (!getCanResponse()) {
            return true;
        }

        if (keyCode == 19) {
            return processUpKey();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            return processLeftKeyDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return processRightKeyDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            return true;
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean processUpKey() {
        Log.d(TAG, "processUpKey: Hello");
        return false;
    }

    // TODO
    // for today, start working from here wednesday
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onkeydown KeyEvent:" + event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getCurrentDataSource() != Constants.BROWSER_TOP_DATA) {
                if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA && (sambaDataBrowser != null) && sambaDataBrowser.isUpdating()) {
                    Log.i(TAG, "to stop Samba Updating....");
                    sambaDataBrowser.stopBrowser();
                    setCanResponse(true);
                    /*
                    Message msg = handler.obtainMessage();
                    msg.what = Constants.UPDATE_TOP_DATA;
                    msg.arg1 = 1;
                    msg.arg2 = 1;
                    handler.sendMessage(msg);
                    setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                    return false;
                    */
                }
//                Log.d(TAG, "onKeyDown: paths :"+holder.getDisplayTipPath()+"\n"+FileBrowserActivity.CLICKED_USB_PATH);
//                if (holder.getDisplayTipPath().equalsIgnoreCase(FileBrowserActivity.CLICKED_USB_PATH)) {
//                    return super.onKeyDown(keyCode, event);
//                }

//                if (LocalDataManager.p.equalsIgnoreCase())
                FileBrowserActivity.this.finish();
//                if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
//                    onItemClickListener.onItemClick(holder.gridView, holder.gridView, -2, -2);
//                } else if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
//                    onItemClickListener.onItemClick(holder.gridView, holder.gridView, -2, -2);
//                }
                return true;
            }
            // FileBrowserActivity.this.finish();
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            return processUpKeyDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            return processDownKeyDown();
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * send virtual buttons.
     */
    protected void sendKeyEvent(int keycode) {
        /*
         * long now = SystemClock.uptimeMillis(); try { KeyEvent downEvent = new
         * KeyEvent(now, now, KeyEvent.ACTION_DOWN, keycode, 0); KeyEvent
         * upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP, keycode, 0);
         * (IWindowManager
         * .Stub.asInterface(ServiceManager.getService("window")))
         * .injectInputEventNoWait(downEvent);
         * (IWindowManager.Stub.asInterface(ServiceManager
         * .getService("window"))) .injectInputEventNoWait(upEvent); } catch
         * (RemoteException e) { e.printStackTrace(); }
         */
    }

    public FileBrowserViewHolder getFileBrowserViewHolder() {
        return holder;
    }

    public void setCanResponse(boolean isCan) {
        Log.i(TAG, "setCanResponse:" + isCan);
        /*
        Exception e = new Exception("this is a log");
        e.printStackTrace();
        */
        m_canResponse = isCan;
    }

    public boolean getCanResponse() {
        return m_canResponse;
    }

    public void setCurrentDataSource(int datasource_) {
        Log.i(TAG, "set current dataSource:" + datasource_);
        m_dataSource = datasource_;
        /*
        Exception e = new Exception("this is a log");
        e.printStackTrace();
        */
    }

    public int getCurrentDataSource() {
        return m_dataSource;
    }

    /************************************************************************
     * ListView event listeners regard highly write area
     ************************************************************************/
    private OnItemClickListener onItemClickListener = (parent, view, position, id) -> {
        Log.d(TAG, "list view onItemClick ... dataSource : " + getCurrentDataSource() + " position:" + position);
        // Key shielding
        if (!getCanResponse()) {
            return;
        }
        Log.d(TAG, "list view onItemClick ... dataSource : " + getCurrentDataSource() + " position:" + position);
        // Distribution key event
        dispatchKeyEvent(KeyEvent.KEYCODE_ENTER, position);

    };

    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Log.d(TAG, "onItemSelected, position : " + position);
            mItemSelectedPosition = position;
            showHits(position);
            if (0 == positionFocusNow && 0 == position)
                positionFocusNow = position;
            else if (0 == positionFocusNow && Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM == position)
                positionFocusNow = position;
            else if (Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM == positionFocusNow && 0 == position)
                positionFocusNow = position;
            else
                positionFocusNow = position + 1;
            localDataBrowser.focusPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int y = (int) event.getY();
            if (getCanResponse()) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        motionY = y;
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        // File list page up // 50 difference says at least 50
                        // sliding distance just turn the page, is probably the
                        // height of the item
                        if (y > motionY && y - 50 > motionY) {
                            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                                localDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                            } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                                dlnaDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                            } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                                sambaDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                            }

                            // File list down turn the page
                        } else if (y < motionY && y + 50 < motionY) {
                            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                                localDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                            } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                                dlnaDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                            } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                                sambaDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                            }
                        }
                        break;
                    }
                }

                return false;

            } else {
                return true;
            }
        }

    };

    private OnKeyListener onKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // operation time consuming operation, will not escape key shielding
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }

            // if the current key in shielding state, is directly to return to
            if (!getCanResponse()) {
                return true;
            }

            // below is turn the page processing
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                // shortcuts turn the page, the page up
                if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
                    if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                        localDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                        dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                        sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    }

                    return true;

                    // shortcuts turn the page down, turn the page
                } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
                    if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                        localDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                        dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                        sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    }

                    return true;
                }

            }

            return false;
        }
    };
    private OnHoverListener btnSwitchOnHoverListener = new OnHoverListener() {

        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (toast == null)
                toast = Toast.makeText(FileBrowserActivity.this, "More browser mode...", Toast.LENGTH_SHORT);
            else
                toast.setText("More browser mode...");
            toast.setGravity(Gravity.TOP, 0, 8);
            switch (what) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    toast.show();
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    break;
            }
            return false;
        }
    };
    private OnHoverListener viewModeImgOnHoverListener = new OnHoverListener() {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            String tmpText = "";
            if (Constants.LISTVIEW_MODE == mListOrGridFlag)
                tmpText = "You are at listview browser mode";
            else if (Constants.GRIDVIEW_MODE == mListOrGridFlag)
                tmpText = "You are at gridview browser mode";
            if (toast == null)
                toast = Toast.makeText(FileBrowserActivity.this, tmpText, Toast.LENGTH_SHORT);
            else
                toast.setText(tmpText);
            toast.setGravity(Gravity.TOP, 0, 8);
            switch (what) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    toast.show();
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    break;
            }
            return false;
        }

    };
    private OnFocusChangeListener btnSwitchOnFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (toast == null)
                    toast = Toast.makeText(FileBrowserActivity.this, "More browser mode...", Toast.LENGTH_SHORT);
                else
                    toast.setText("More browser mode...");
                toast.setGravity(Gravity.TOP, 0, 8);
                toast.show();
            }
        }

    };

    /************************************************************************
     * Handler event handling area
     ************************************************************************/
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("NewApi")
        public void handleMessage(Message msg) {
            // home page surface
//            if (msg.what == Constants.BROWSER_TOP_DATA) {
//                lastDataSource = getCurrentDataSource();
//                if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
//                    // reset type switch for available state
//                    holder.openTypeChange();
//                }
//                if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
//                    sambaDataBrowser.stopHttpServer();
//                }
//                // data
//                setCurrentDataSource(Constants.BROWSER_TOP_DATA);
//                // loading progress tip
//                holder.showScanStatus(true);
//                showScanMessage(R.string.loading_top);
//
//                // refresh homepage data
//                topDataBrowser.refresh();
//
//                // browsing local usb disk data: level directory
//            } else

            /*
             * Modified by Toukir. For only showing Gridview
             *
             * */

            if (msg.what == Constants.BROWSER_LOCAL_DATA) {
                Log.d(TAG, "handleMessage: Called");
                setCurrentDataSource(Constants.BROWSER_LOCAL_DATA);
                // loading progress tip.
                showScanMessage(R.string.loading_usb_device);

                // browsing USB devices.
                localDataBrowser.browser(1, Constants.OPTION_STATE_ALL);

                // browsing samba data: level directory
            } else if (msg.what == Constants.UPDATE_LOCAL_DATA) {
                // hide loading progress tooltip
                dismissScanMessage();
                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                // refresh UI.
                if (mListOrGridFlag == Constants.GRIDVIEW_MODE) {
                    tmpArray.clear();

                    tmpArray.addAll(sourceData);

                    gridAdapter.notifyDataSetChanged();

                    positionFocusNow = 0;
                } else {
                    desDataList.clear();
                    desDataList.addAll(sourceData);
                    adapter.notifyDataSetChanged();
                }
                if (mListOrGridFlag == Constants.GRIDVIEW_MODE)
                    holder.gridView.setSelection(index);
                else
                    holder.listView.setSelection(index);
                // refresh the page number.
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
                // display the tooltip
                showHits(index);
                // refresh dlna data
            } else if (msg.what == Constants.UPDATE_MEDIA_TYPE) {
                switchMediaType(dataType);

            } else if (msg.what == Constants.UPDATE_EXCEPTION_INFO) {
                processExceptions(msg.arg1);
                // Loading tooltip invisible
                holder.showScanStatus(false);
                holder.updateScanStatusText(false, 0);

            } else if (msg.what == Constants.UPDATE_PROGRESS_INFO) {
                if (msg.arg2 == Constants.PROGRESS_TEXT_SHOW) {
                    showScanMessage(msg.arg1);
                } else if (msg.arg2 == Constants.PROGRESS_TEXT_HIDE) {
                    dismissScanMessage();
                }

                // not be used when the network
            } else if (msg.what == Constants.NETWORK_UNCONNECTED) {
                // let go to key shielding
                setCanResponse(true);

                // release resources
                release();

                // if the current are to browse the network data and network,
                // not with is directly turn to Top page
                if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    holder.openTypeChange();
                }

                lastDataSource = getCurrentDataSource();
                // data
                setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                // loading progress tip
                holder.showScanStatus(true);

                // refresh homepage data
                localDataBrowser.refresh(dataType);

                // mouse page up operation
            } else if (msg.what == Constants.MOUSE_PAGE_UP) {

                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                }

                // The mouse down turn the page operation
            } else if (msg.what == Constants.MOUSE_PAGE_DOWN) {
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                }

                // The mouse focus with use
            } else if (msg.what == Constants.UPDATE_LISTVIEW_FOCUS) {
                if (getCanResponse()) {
                    int position = msg.getData().getInt(
                            Constants.ADAPTER_POSITION);
                    holder.setListViewFocus(true, position);
                    showHits(position);
                }
            } else if (msg.what == Constants.START_MEDIA_PLAYER) {
//                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
//                    localDataBrowser.startPlayer();
//                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
//                    dlnaDataBrowser.startPlayer();
//                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
//                    sambaDataBrowser.startPlayer();
//                }

                localDataBrowser.startPlayer();


            } else if (msg.what == Constants.GRID_CANCEL_TASK) {
                if (msg.arg1 == Constants.GRID_CANCEL_TASK_NEED_PLAY) {
                    gridAdapter.cancelTask(true);
                    handler.sendEmptyMessage(Constants.START_MEDIA_PLAYER);
                } else if (msg.arg1 == Constants.GRID_CANCEL_TASK_NO_NEED_PLAY) {
                    gridAdapter.cancelTask(false);
                }
            } else if (msg.what == Constants.SHOW_DIVX_DIALOG) {
                holder.showDivx();
            } else if (msg.what == 222) {
                Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            }
            // End of Toukir modified


//            if (msg.what == Constants.BROWSER_LOCAL_DATA) {
//                setCurrentDataSource(Constants.BROWSER_LOCAL_DATA);
//                // loading progress tip.
//                showScanMessage(R.string.loading_usb_device);
//
//                // browsing USB devices.
//                localDataBrowser.browser(-1, dataType);
//
//                // browsing samba data: level directory
//            } else if (msg.what == Constants.BROWSER_SAMBA_DATA) {
//                lastDataSource = getCurrentDataSource();
//                setCurrentDataSource(Constants.BROWSER_SAMBA_DATA);
//                showScanMessage(R.string.loading_samba_device);
//
//                // loading samba need longer time, need to screen buttons
//                setCanResponse(false);
//                // browsing samba equipment
//                sambaDataBrowser.browser(-1, dataType);
//
//                // browsing dlna data: level directory
//            } else if (msg.what == Constants.BROWSER_DLNA_DATA) {
//                lastDataSource = getCurrentDataSource();
//                setCurrentDataSource(Constants.BROWSER_DLNA_DATA);
//                showScanMessage(R.string.loading_dlna_device);
//
//                // browsing dlna equipment
//                dlnaDataBrowser.browser(-1, dataType);
//
//                // refresh homepage data
//            } else if (msg.what == Constants.UPDATE_TOP_DATA) {
//                // Hide loading progress tooltip
//                dismissScanMessage();
//                int index = -100;
//                String text = "";
//                if (lastDataSource == Constants.BROWSER_TOP_DATA || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
//                    index = 0;
//                } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
//                    index = 1;
//                } else {
//                    index = 2;
//                }
//                if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
//                    tmpArray.clear();
//                    tmpArray.addAll(sourceData);
//                    gridAdapter.notifyDataSetChanged();
//                    positionFocusNow = 0;
//                } else if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
//                    desDataList.clear();
//                    desDataList.addAll(sourceData);
//                    adapter.notifyDataSetChanged();
//                }
//                if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
//                    if (lastDataSource == Constants.BROWSER_TOP_DATA
//                            || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
//                        holder.gridView.setSelection(0);
//                        text = getResources().getStringArray(R.array.data_source)[0];
//                    } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
//                        holder.gridView.setSelection(1);
//                        text = getResources().getStringArray(R.array.data_source)[1];
//                    } else if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
//                        holder.gridView.setSelection(2);
//                        text = getResources().getStringArray(R.array.data_source)[2];
//                    }
//                } else if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
//                    if (lastDataSource == Constants.BROWSER_TOP_DATA
//                            || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
//                        holder.listView.setSelection(0);
//                        text = getResources().getStringArray(R.array.data_source)[0];
//                    } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
//                        holder.listView.setSelection(1);
//                        text = getResources().getStringArray(R.array.data_source)[1];
//                    } else if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
//                        holder.listView.setSelection(2);
//                        text = getResources().getStringArray(R.array.data_source)[2];
//                    }
//                }
//                // ui refresh
//                // Refresh hint
//                holder.setDisplayTip(text);
//
//                // Update page
//                holder.setCurrentPageNum(msg.arg1);
//                holder.setTotalPageNum(msg.arg2);
//
//                // refresh local data
//            } else if (msg.what == Constants.UPDATE_LOCAL_DATA) {
//                // hide loading progress tooltip
//                dismissScanMessage();
//                Bundle bundle = msg.getData();
//                int index = bundle.getInt(Constants.BUNDLE_INDEX);
//                // refresh UI.
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE) {
//                    tmpArray.clear();
//                    tmpArray.addAll(sourceData);
//                    gridAdapter.notifyDataSetChanged();
//                    positionFocusNow = 0;
//                } else {
//                    desDataList.clear();
//                    desDataList.addAll(sourceData);
//                    adapter.notifyDataSetChanged();
//                }
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE)
//                    holder.gridView.setSelection(index);
//                else
//                    holder.listView.setSelection(index);
//                // refresh the page number.
//                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
//                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
//                // display the tooltip
//                showHits(index);
//                // refresh dlna data
//            } else if (msg.what == Constants.UPDATE_DLNA_DATA) {
//                // hide loading progress tooltip
//                dismissScanMessage();
//
//                // dlna module doesn't allow for types of switching
//                holder.setLeftFocus(Constants.OPTION_STATE_ALL, false);
//                holder.closeTypeChange();
//                tmpType = Constants.OPTION_STATE_ALL;
//
//                // update UI
//                desDataList.clear();
//                desDataList.addAll(sourceData);
//                adapter.notifyDataSetChanged();
//
//                Bundle bundle = msg.getData();
//                int index = bundle.getInt(Constants.BUNDLE_INDEX);
//                holder.listView.setSelection(index);
//                // update the page number
//                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
//                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
//
//                // display the tooltip
//                showHits(index);
//
//                // refresh samba data
//            } else if (msg.what == Constants.UPDATE_ALL_SAMBA_DATA) {
//                // let go to key shielding
//                setCanResponse(true);
//                Bundle bundle = msg.getData();
//                int index = bundle.getInt(Constants.BUNDLE_INDEX);
//                // refresh UI.
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE) {
//                    tmpArray.clear();
//                    tmpArray.addAll(sourceData);
//                    gridAdapter.notifyDataSetChanged();
//                    positionFocusNow = 0;
//                } else {
//                    desDataList.clear();
//                    desDataList.addAll(sourceData);
//                    adapter.notifyDataSetChanged();
//                }
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE)
//                    holder.gridView.setSelection(index);
//                else
//                    holder.listView.setSelection(index);
//                // refresh the page number.
//                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
//                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
//                // display the tooltip
//                showHits(index);
//                dismissScanMessage();
//                // view picture data
//            } else if (msg.what == Constants.SAMBA_SCAN_COMPLETED) {
//                // hide loading progress tooltip
//                if (!m_canResponse && (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA)) {
//                    setCanResponse(true);
//                    Message msg1 = handler.obtainMessage();
//                    msg1.what = Constants.UPDATE_TOP_DATA;
//                    msg1.arg1 = 1;
//                    msg1.arg2 = 1;
//                    handler.sendMessage(msg1);
//                    setCurrentDataSource(Constants.BROWSER_TOP_DATA);
//                    showToast(R.string.network_connect_failed);
//                }
//                dismissScanMessage();
//            } else if (msg.what == Constants.UPDATE_SAMBA_DATA) {
//                // let go to key shielding
//                setCanResponse(true);
//                Bundle bundle = msg.getData();
//                int index = bundle.getInt(Constants.BUNDLE_INDEX);
//                // refresh UI.
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE) {
//                    tmpArray.clear();
//                    tmpArray.addAll(sourceData);
//                    gridAdapter.notifyDataSetChanged();
//                    positionFocusNow = 0;
//                    changeFocusWhenL2G();
//                } else {
//                    desDataList.clear();
//                    desDataList.addAll(sourceData);
//                    adapter.notifyDataSetChanged();
//                    changeFocusWhenG2L();
//                }
//                if (mListOrGridFlag == Constants.GRIDVIEW_MODE)
//                    holder.gridView.setSelection(index);
//                else
//                    holder.listView.setSelection(index);
//                // refresh the page number.
//                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
//                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
//                // display the tooltip
//                showHits(index);
//                // view picture data
//            } else if (msg.what == Constants.UPDATE_MEDIA_TYPE) {
//                switchMediaType(dataType);
//
//            } else if (msg.what == Constants.UPDATE_EXCEPTION_INFO) {
//                processExceptions(msg.arg1);
//                // Loading tooltip invisible
//                holder.showScanStatus(false);
//                holder.updateScanStatusText(false, 0);
//
//            } else if (msg.what == Constants.UPDATE_PROGRESS_INFO) {
//                if (msg.arg2 == Constants.PROGRESS_TEXT_SHOW) {
//                    showScanMessage(msg.arg1);
//                } else if (msg.arg2 == Constants.PROGRESS_TEXT_HIDE) {
//                    dismissScanMessage();
//                }
//
//                // not be used when the network
//            } else if (msg.what == Constants.NETWORK_UNCONNECTED) {
//                // let go to key shielding
//                setCanResponse(true);
//
//                // release resources
//                release();
//
//                // if the current are to browse the network data and network,
//                // not with is directly turn to Top page
//                if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
//                    holder.openTypeChange();
//                }
//
//                lastDataSource = getCurrentDataSource();
//                // data
//                setCurrentDataSource(Constants.BROWSER_TOP_DATA);
//                // loading progress tip
//                holder.showScanStatus(true);
//
//                // refresh homepage data
//                topDataBrowser.refresh();
//
//                // mouse page up operation
//            } else if (msg.what == Constants.MOUSE_PAGE_UP) {
//                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
//                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
//                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
//                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
//                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
//                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
//                }
//
//                // The mouse down turn the page operation
//            } else if (msg.what == Constants.MOUSE_PAGE_DOWN) {
//                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
//                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
//                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
//                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
//                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
//                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
//                }
//
//                // The mouse focus with use
//            } else if (msg.what == Constants.UPDATE_LISTVIEW_FOCUS) {
//                if (getCanResponse()) {
//                    int position = msg.getData().getInt(
//                            Constants.ADAPTER_POSITION);
//                    holder.setListViewFocus(true, position);
//                    showHits(position);
//                }
//            } else if (msg.what == Constants.START_MEDIA_PLAYER) {
//                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
//                    localDataBrowser.startPlayer();
//                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
//                    dlnaDataBrowser.startPlayer();
//                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
//                    sambaDataBrowser.startPlayer();
//                }
//
//
//            } else if (msg.what == Constants.GRID_CANCEL_TASK) {
//                if (msg.arg1 == Constants.GRID_CANCEL_TASK_NEED_PLAY) {
//                    gridAdapter.cancelTask(true);
//                    handler.sendEmptyMessage(Constants.START_MEDIA_PLAYER);
//                } else if (msg.arg1 == Constants.GRID_CANCEL_TASK_NO_NEED_PLAY) {
//                    gridAdapter.cancelTask(false);
//                }
//            } else if (msg.what == Constants.SHOW_DIVX_DIALOG) {
//                holder.showDivx();
//            }
        }

        ;
    };

    /************************************************************************
     * Broadcast radio event handling area
     ************************************************************************/

    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // disk loading
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                // current for Local data browsing disk device list page
                Log.i(TAG, "action.equals(Intent.ACTION_MEDIA_MOUNTED)");
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    // update device list
                    localDataBrowser.updateUSBDevice(null);
                }

                // disk unloading
            } else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i(TAG, "action.equals(Intent.ACTION_MEDIA_EJECT)");
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    Uri uri = intent.getData();
                    String devicePath = uri.getPath();
                    localDataBrowser.updateUSBDevice(devicePath);
                }
            }
        }
    };

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "network disconnect");
            // network state changes through the radio to handle processing, if
            // the current in to browse the network data is returned to the Top
            // page
            if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA
                    || getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                handler.sendEmptyMessage(Constants.NETWORK_UNCONNECTED);
                if (sambaDataBrowser != null) {
                    sambaDataBrowser.closeDialogIfneeded();
                }
            }
        }

    };

    BroadcastReceiver sourceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "*******BroadcastReceiver**********" + intent.getAction());
            findViewById(R.id.rootLinearLayout).setVisibility(View.INVISIBLE);
        }
    };

    private BroadcastReceiver homeKeyEventBroadCastReceiver = new BroadcastReceiver() {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        Log.i(TAG, "SYSTEM_HOME_KEY");
                        // Before, call this.finish() is for 4k2k to release resource faster when exit lmm.
                        // But Currently Comment out this, because users want lmm remember the current list menu position after exit lmm.
                        // FileBrowserActivity.this.finish();
                        // android.os.Process.killProcess(Process.myPid());
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        // long home key
                    }
                }
            }
        }
    };

    private BroadcastReceiver finishFileBrowserActivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "finishFileBrowserActivityReceiver onReceive");
            String action = intent.getAction();

            if (action.equals("action_finish_filebrowseractivity")) {
                Log.i(TAG, "action_finish_filebrowseractivity");
                FileBrowserActivity.this.finish();
            }
        }

    };
    private BroadcastReceiver mWallpaperChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "mWallpaperChangeReceiver onReceive");
            showToastAtBottom("Set wallpaper Success!");
        }
    };

    /************************************************************************
     * Key event handling area
     ************************************************************************/
    private boolean processLeftKeyDown() {
        // do nothing when focus on btnSwitch;
        if (holder.btnSwitch.isFocusable()) {
            return true;
        }
        // ListView current gains focus
        if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
            if (holder.listView.isFocusable()) {
                tmpType = dataType;
                holder.setLeftFocus(dataType, true);
                holder.setListViewFocus(false, 0);
                holder.clearFocusFromKey();
                return true;
            }
        } else if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
                if (0 == positionFocusNow ||
                        0 == (positionFocusNow % Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM)) {
                    tmpType = dataType;
                    holder.setLeftFocus(dataType, true);
                    holder.setGridViewFocus(false, 0);
                }
            } else {
                // do nothing
            }
            // because it add 1 in the onItemSelected
            positionFocusNow--;
        }
        return true;


    }

    private boolean processRightKeyDown() {
        Log.i(TAG, "processRightKeyDown: ");
        // do nothing when focus on btnSwitch
        if (holder.btnSwitch.isFocusable()) {
            holder.btnSwitch.requestFocus();
            return true;
        }
        if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
            // ListView current gains focus
            Log.i(TAG, "processRightKeyDown: holder.listView.isFocusable() " + holder.listView.isFocusable());
            if (holder.listView.isFocusable()) {
                Log.i(TAG, "processRightKeyDown: ");
                holder.listView.requestFocus();
            } else {
                holder.setLeftFocus(dataType, false);
                if (dataType != tmpType) {
                    holder.changeLeftFocus(tmpType, 0);
                }
                holder.setListViewFocus(true, 0);
                showHits(0);
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
                holder.gridView.requestFocus();
            } else {
                holder.setLeftFocus(dataType, false);
                if (dataType != tmpType) {
                    holder.changeLeftFocus(tmpType, 0);
                }
                holder.setGridViewFocus(true, 0);
                positionFocusNow = 0;
                showHits(0);
            }
            return true;
        }
        return true;
    }

    private boolean processUpKeyDown() {
        // do nothing
//        if (holder.gridView.isFocusable()) {
//            return true;
//        }
        String tmp = (String) holder.currentPageNumText.getText();
        // from GridView to btnSwitch
        if (holder.gridView.isFocusable() && Integer.parseInt(tmp) == 1) {
//            holder.setGridViewFocus(true, 0);
//            holder.setBtnSwitchFocus(true);
            return true;
        }
        // from  listView to btnSwitch
        if (holder.btnSwitchIsOnOrOff && !holder.btnSwitch.isFocusable()
                && holder.listView.isFocusable()
                && holder.listView.getSelectedItemPosition() == 0 && Integer.parseInt(tmp) == 1) {
            holder.setListViewFocus(false, 0);
            holder.setBtnSwitchFocus(true);
            return true;
        }
        if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
            if (holder.listView.isFocusable()) {
                // Turn the page
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_UP, holder.listView.getSelectedItemPosition());
            } else {
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_ALL) {
                    type = Constants.OPTION_STATE_VIDEO;
                } else {
                    type = tmpType - 1;
                }
                Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
                // Turn the page
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_UP, holder.gridView.getSelectedItemPosition());
            } else {
                // According to the up button, switch and the focus of the two icon
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_ALL) {
                    type = Constants.OPTION_STATE_VIDEO;
                } else {
                    type = tmpType - 1;
                }
                Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;


        }
        return true;
    }

    private boolean processDownKeyDown() {
        // from btnSwitch to Gridview
        if (holder.btnSwitch.isFocusable() && Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            holder.setGridViewFocus(true, 0);
            holder.setBtnSwitchFocus(false);
            positionFocusNow = 0;
            return true;
        }
        // from btnSwitch to listView
        if (holder.btnSwitch.isFocusable() && Constants.LISTVIEW_MODE == mListOrGridFlag) {
            holder.setListViewFocus(true, 0);
            holder.setBtnSwitchFocus(false);
            return true;
        }
        if (Constants.LISTVIEW_MODE == mListOrGridFlag) {
            // ListView current gains focus
            if (holder.listView.isFocusable()) {
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN, holder.listView.getSelectedItemPosition());
            } else {
                // According to the down button, switch and the focus of the two icon
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_VIDEO) {
                    type = Constants.OPTION_STATE_ALL;
                } else {
                    type = tmpType + 1;
                }
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            // gridView current gains focus
            if (holder.gridView.isFocusable()) {
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN, holder.gridView.getSelectedItemPosition());
            } else {
                // According to the down button, switch and the focus of the two icon
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_VIDEO) {
                    type = Constants.OPTION_STATE_ALL;
                } else {
                    type = tmpType + 1;
                }
                Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;

        }

        return true;
    }

    /************************************************************************
     * Private method area
     ************************************************************************/
    /*
     * Exit system call to free resources or reverse registering listeners.
     */
    private void release() {
        try {
            dlnaDataBrowser.release();
            sambaDataBrowser.release();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                Log.i(TAG, "holder.listView.isFocusable() :" + holder.listView.isFocusable());
                Log.i(TAG, "holder.gridView.isFocusable() :" + holder.gridView.isFocusable());
                Log.i(TAG, "holder.btnSwitch.isFocusable() :" + holder.btnSwitch.isFocusable());
//                if (holder.btnSwitch.isFocusable() == true) {
//                    return super.dispatchKeyEvent(event);
//                }
                if (holder.listView.isFocusable() || holder.gridView.isFocusable()) {
                    dispatchKeyEvent(KeyEvent.KEYCODE_ENTER, mItemSelectedPosition);
                    return true;
                }

            }
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (holder.listView.hasFocus()) {
                return true;
            } else if (holder.gridView.hasFocus()) {
                if ((holder.gridView.getSelectedItemPosition() + 1) % Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM == 0
                        || holder.gridView.getSelectedItemPosition() + 1 == holder.gridView.getCount()) {
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /*
     * key distributed to each module for processing.
     */
    private void dispatchKeyEvent(int keyCode, int position) {

        if (getCurrentDataSource() == Constants.BROWSER_TOP_DATA) {
            topDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
            Log.d(TAG, "dispatchKeyEvent: Browser_local");
            localDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
            dlnaDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
            sambaDataBrowser.processKeyDown(keyCode, position);
        }
    }

    /*
     * when an exception occurs subsequent processing.
     */
    private void processExceptions(int code) {
        // ping network equipment over time
        if (code == Constants.FAILED_TIME_OUT) {
            // shielding key
            setCanResponse(false);

            if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                showToast(R.string.miss_dlna_device);
                // loading progress tip
                showScanMessage(R.string.loading_dlna_device);

                // browsing dlna equipment
                dlnaDataBrowser.browser(-1, 0);
            } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                showToast(R.string.miss_samba_device);
                // loading progress tip
                showScanMessage(R.string.loading_samba_device);

                // browsing dlna equipment
                sambaDataBrowser.browser(-1, dataType);
            }

            // need to continue shielding keypress, so direct return
            return;

            // landing samba equipment password mistake
        } else if (code == Constants.FAILED_WRONG_PASSWD) {
            showToast(R.string.login_wrong_password);

            // landing samba equipment failure
        } else if (code == Constants.FAILED_LOGIN_FAILED) {
            showToast(R.string.login_failed);

            // other landing samba equipment error
        } else if (code == Constants.FAILED_LOGIN_OTHER_FAILED) {
            showToast(R.string.login_other_failed);

            // do not support media format
        } else if (code == Constants.UNSUPPORT_FORMAT) {
            showToast(R.string.unsupport_format);

            // mount failure
        } else if (code == Constants.MOUNT_FAILED) {
            showToast(R.string.mount_failed);

            // network do not use
        } else if (code == Constants.NETWORK_EXCEPTION) {
            showToast(R.string.network_unconnected);
        }

        // let go to key shielding
        setCanResponse(true);

    }

    /*
     * switching currently browsing media types.
     */
    private void switchMediaType(int type) {

        // local resource switch file type
        if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
            localDataBrowser.refresh(dataType);

            // dlna data default always reveal all types of media files
        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {

            // switching currently browsing the samba resource file type
        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
            sambaDataBrowser.refresh(type);
        }

    }

    /*
     * hidden loading progress tooltip.
     */
    private void dismissScanMessage() {
        // loading tooltip invisible
        holder.showScanStatus(false);
        holder.updateScanStatusText(false, 0);
        // let go to key shielding
        setCanResponse(true);
    }

    /*
     * display loading progress tooltip.
     */
    private void showScanMessage(int id) {
        // shielding key
        setCanResponse(false);

        // loading tooltip invisible
        holder.showScanStatus(true);
        if (0 != id) {
            holder.updateScanStatusText(true, id);
        }
    }

    private void showToastAtBottom(String text) {
        showToast(text, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    }

    private void showToast(final String text, int gravity, int duration) {
        Toast toast = ToastFactory.getToast(FileBrowserActivity.this, text, gravity);
        toast.show();
    }

    /*
     * display tooltip.
     */
    private void showToast(int id) {
        // Resource id cannot use
        if (id <= 0) {
            return;
        }
        // activity life cycle has ended
        if (isFinishing()) {
            return;
        } else {
            String text = getString(id);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * display file (clamp) name hints or data sources hint, such as local disk
     * data.
     */
    private void showHits(int index) {

        if (index < desDataList.size() && Constants.LISTVIEW_MODE == mListOrGridFlag) {
            if (index < 0) {
                index = 0;
                Log.i(TAG, "Monkey Test,showHits index < 0,force index 0!");
            }
            BaseData baseData = desDataList.get(index);
            holder.refreshItemFocusState(index);
            if (baseData == null) {
                return;
            }
            // local documents show full path
            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                String path = baseData.getPath();
                //
                if (path != null && !path.equals("")) {
                    holder.setDisplayTip(path);
                } else {
                    holder.setDisplayTip(baseData.getName());
                }

                // network data display file name
            } else {
                holder.setDisplayTip(baseData.getName());
            }
        } else if (index < tmpArray.size() && Constants.GRIDVIEW_MODE == mListOrGridFlag) {
            BaseData baseData = tmpArray.get(index);
            if (baseData == null) {
                return;
            }
            // local documents show full path
            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                String path = baseData.getPath();
                if (path != null && !path.equals("")) {
                    holder.setDisplayTip(path);
                } else {
                    holder.setDisplayTip(baseData.getName());
                }
                // network data display file name
            } else {
                holder.setDisplayTip(baseData.getName());
            }
        } else {
            Log.d(TAG, "showHits invalid index : " + index);
        }
    }

    /*
     * Print version.
     */
    private void showVersion() {
        System.out.println(getResources().getString(R.string.mm_version));
    }

    public void saveSharedPreferences(int settingPosition) {
        switch (settingPosition) {
            case 0:
                getSharedPreferences("localmm_sharedPreferences", Context.MODE_PRIVATE)
                        .edit().putInt("listOrGridFlag", 1).commit();
                break;
            case 1:
            case 2:
            case 3:
            default:
                break;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.walton.datainfo")){
                boolean isDataAvailable = intent.getBooleanExtra("info",false);
                if (!isDataAvailable){
                    Log.d(TAG, "onReceive: No data in list");
                    focusLeftFirst();
                    Toast.makeText(context,"No data available!",Toast.LENGTH_LONG).show();
                }else {
                    Log.d(TAG, "onReceive: Data available in list");
                }
            }
        }
    };

}
