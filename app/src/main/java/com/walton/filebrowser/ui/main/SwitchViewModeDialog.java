//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

import android.widget.Toast;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.business.data.ViewMode;
import com.walton.filebrowser.business.adapter.ViewModeAdapter;
import com.walton.filebrowser.util.Tools;


//by andrew.wang


public class SwitchViewModeDialog extends Dialog {

    public static final String TAG = "SwitchViewModeDialog";
    private FileBrowserActivity mContext = null;
    private ListView mViewModeListView =null;
    private Resources mResources = null;
    protected ArrayList<ViewMode> mViewModeList;
    private String mPlatform;

    public SwitchViewModeDialog(FileBrowserActivity context) {
        super(context, R.style.dialog);
        mContext = context;
        mResources = mContext.getResources();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmode_dialog);
        setWindowsAttribute();
        initView();
        setListeners();
    }
    private void initView() {
        Log.i(TAG,"createPopWindow");
        mViewModeList = new ArrayList<ViewMode>();
        Drawable drawable =null;

        drawable = mResources.getDrawable(R.drawable.listview_mode);
        boolean presentChoise = isPresentChoise("listview");
        Log.i(TAG,"presentChoise:"+String.valueOf(presentChoise));
        ViewMode viewMode =new ViewMode(drawable, "listview",presentChoise);
        mViewModeList.add(viewMode);

        drawable = mResources.getDrawable(R.drawable.gridview_mode);
        viewMode =new ViewMode(drawable, "gridview",!presentChoise);

        mViewModeList.add(viewMode);
        mViewModeListView = (ListView) this.findViewById(R.id.lv_pop);
        mViewModeListView.setDivider(null);
        mViewModeListView.setAdapter(new ViewModeAdapter(mContext, mViewModeList));


    }
    private boolean isPresentChoise(String view){
        if ("listview".equals(view)) {
            if (FileBrowserActivity.mListOrGridFlag ==Constants.LISTVIEW_MODE)
                return true;
            return false;
        } else if ("gridview".equals(view)) {
            if (FileBrowserActivity.mListOrGridFlag ==Constants.GRIDVIEW_MODE)
                return true;
            return false;
        }
        Log.e(TAG,"the view is nither listview nor gridview!");
        return false;
    }
    private void setWindowsAttribute() {
        // instantiation new window
        /*
        Window w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        w.setTitle(null);
        //w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        */
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = 400;  //(int) (display.getWidth() * 0.204);
        int height = 200; //(int) (display.getHeight() * 0.220);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
    }

    private void setListeners() {
        mViewModeListView.setOnItemClickListener(ViewModeListener);
    }
    private OnItemClickListener ViewModeListener = new OnItemClickListener(){
        @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Log.i(TAG,"popwindow onclick position:"+String.valueOf(position));
                mPlatform = Tools.getHardwareName();
                boolean memTotalPermission = true;
                Log.i(TAG,"TotalMem: "+String.valueOf(Tools.getTotalMem()));
                if (Tools.getTotalMem() < 512*1024) {
                    memTotalPermission = false;
                }
                if (memTotalPermission) {
                    if (position != FileBrowserActivity.mListOrGridFlag) {
//                        mContext.switchViewMode();
                        mContext.getFileBrowserViewHolder().printPresentViewMode();
                    }
                } else {
                    showToast(mPlatform+" platform's memory is low and cannot support gridview mode!");
                }
                mContext.saveSharedPreferences(0);
                SwitchViewModeDialog.this.cancel();
            }
    };

    private void showToast(String content) {
        Toast toast = Toast.makeText(mContext, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.show();
    }

}

