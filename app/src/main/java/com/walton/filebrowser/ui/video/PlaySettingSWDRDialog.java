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
package com.walton.filebrowser.ui.video;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.SWDRListAdapter;
import com.walton.filebrowser.util.TvApiController;
import com.mstar.android.tv.TvPictureManager;

public class PlaySettingSWDRDialog extends Dialog {
    private static final String TAG = "PlaySettingSWDRDialog";
    public static final float DIALOG_DISPLAY_WIDTH_SCALING = 0.40f;
    public static final float DIALOG_DISPLAY_HEIGHT_SCALING = 0.63f;
    private static final String SWDR_STATUS_DISABLED = "DISABLED";
    private static final String SWDR_STATUS_ENABLED = "ENABLED";
    private static final String SWDR_STATUS_UNKNOWN = "UNKNOWN";
    private VideoPlayerActivity context;
    private ListView mListView = null;
    private TextView mSwdrStatusValueTextView = null;
    private SWDRListAdapter adapter;
    private VideoPlaySettingDialog mVideoPlaySettingDialog;

    public static int[] mSWDRSettingName = { R.string.video_swdr_level};

    public static int mSwdrLevel = 0;
    public static String mSwdrStatus = SWDR_STATUS_DISABLED;

    public PlaySettingSWDRDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public PlaySettingSWDRDialog(VideoPlayerActivity context, int theme,
            VideoPlaySettingDialog videoPlaySettingDialog) {
        super(context, theme);
        this.context = context;
        mVideoPlaySettingDialog = videoPlaySettingDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mVideoPlaySettingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_setting_swdr_dialog);
        // instantiation new window
        Window w = getWindow();
        // for the default display data
        Display display = w.getWindowManager().getDefaultDisplay();
        int getSwdrLevelIndex = TvApiController.getInstance(context).getSwdrLevelIndex();
        mSwdrLevel = getSwdrLevelIndex;
        mListView = (ListView) this.findViewById(R.id.playsetting_swdr_list);
        adapter = new SWDRListAdapter(context, mSWDRSettingName, mSwdrLevel,
                PlaySettingSWDRDialog.this);
        mListView.setDivider(null);
        mListView.setAdapter(adapter);
        mSwdrStatusValueTextView = (TextView) this.findViewById(R.id.swdr_status_value);
        mSwdrStatusValueTextView.setText(mSwdrStatus);
        setListeners();
        // window's title is empty
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.25);
        int height = (int) (display.getHeight() * 0.50);
        // Settings window size
        w.setLayout(width, height);
        // Settings window display position
        w.setGravity(Gravity.RIGHT);
        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 80;
        w.setAttributes(wl);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "****onStart*******");
        if (TvApiController.getInstance(context).isSwdrEnabled()) {
            int getSwdrLevelIndex = TvApiController.getInstance(context).getSwdrLevelIndex();
            mSwdrLevel = getSwdrLevelIndex;
            if (getSwdrLevelIndex == 0) {
                mSwdrStatusValueTextView.setText(SWDR_STATUS_DISABLED);
            } else {
                mSwdrStatusValueTextView.setText(SWDR_STATUS_ENABLED);
            }
        } else {
            mSwdrStatusValueTextView.setText(SWDR_STATUS_DISABLED);
        }
    }

    private void setListeners() {
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.i(TAG, "------onItemClick ---------- position:" + position);
                handleMidClick(position);
            }
        });
        mListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i(TAG, "------onkeyListenter ---------- keyCode:" + keyCode + " getAction:" + event.getAction());

            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT: {
                            int position = mListView.getSelectedItemPosition();
                            handleImageClick(position, true);
                            break;
                        }
                        case KeyEvent.KEYCODE_DPAD_RIGHT: {
                            int position = mListView.getSelectedItemPosition();
                            handleImageClick(position, false);
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };
    public void handleImageClick(int position, boolean bLeftImageClick) {
        Log.i(TAG, "------ handleImageClick -------- position:" + position + " bLeftImageClick:" + bLeftImageClick);
        if (bLeftImageClick) {
            changeSwdrLevel(-1);
        } else {
           changeSwdrLevel(1);
        }
        adapter.notifyDataSetChanged();
    }

    public void handleMidClick(int position) {
        Log.i(TAG, "----- handleMidClick ----- position:" + position);
    }

    public void changeSwdrLevel(int delta) {
        if (TvApiController.getInstance(context).isSwdrEnabled()) {
            int getSwdrLevelIndex = TvApiController.getInstance(context).getSwdrLevelIndex();
            int getSwdrSupportCount = TvApiController.getInstance(context).getSwdrSupportCount();
            getSwdrLevelIndex += delta;
            if (getSwdrLevelIndex > getSwdrSupportCount) {
                getSwdrLevelIndex = 0;
            }
            if (getSwdrLevelIndex < 0) {
                getSwdrLevelIndex = getSwdrSupportCount - 1;
            }

            mSwdrLevel = getSwdrLevelIndex;
            Log.i(TAG, "----- changeSwdrLevel ----- mSwdrLevel:" + mSwdrLevel);
            adapter.setSWDRLevel(mSwdrLevel);
            TvApiController.getInstance(context).setSwdrLevel(mSwdrLevel,TvPictureManager.VIDEO_MAIN_WINDOW);
            if (getSwdrLevelIndex == 0) {
                mSwdrStatusValueTextView.setText(SWDR_STATUS_DISABLED);
            } else {
                mSwdrStatusValueTextView.setText(SWDR_STATUS_ENABLED);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}

