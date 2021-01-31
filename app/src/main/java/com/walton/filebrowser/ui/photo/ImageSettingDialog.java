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

package com.walton.filebrowser.ui.photo;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.VideoPlaySettingListAdapter;
import com.walton.filebrowser.util.Tools;

public class ImageSettingDialog extends Dialog {
    private ImagePlayerActivity context;

    private ListView playSettingListView;

    private VideoPlaySettingListAdapter adapter;

    private int time;

    private boolean isOpen;

    private Handler mHandler;

    private String open;

    private String close;

    public ImageSettingDialog(ImagePlayerActivity context) {
        super(context);
        this.context = context;
    }

    public ImageSettingDialog(ImagePlayerActivity context, int time,
            boolean isOpen, Handler mHandler) {
        super(context);
        this.context = context;
        this.time = time / 1000;
        this.isOpen = isOpen;
        this.mHandler = mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_setting_dialog);
        // instantiation new window
        Window w = getWindow();
        // for the default display data
        Display display = w.getWindowManager().getDefaultDisplay();
        playSettingListView = (ListView) this
                .findViewById(R.id.videoPlaySettingListView);
        adapter = new VideoPlaySettingListAdapter(context,
                PhotoPlayerViewHolder.playSettingName,
                Tools.initPlaySettingOpt(context, 1));
        open = context.getString(R.string.play_setting_0_value_1);
        close = context.getString(R.string.play_setting_0_value_2);
        if (isOpen) {
            Tools.setPlaySettingOpt(0, open, 1);
        } else {
            Tools.setPlaySettingOpt(0, close, 1);
        }
        Tools.setPlaySettingOpt(1, "" + time, 1);
        playSettingListView.setDivider(null);

        playSettingListView.setAdapter(adapter);
        // window's title is empty
        w.setTitle(null);
        // definition window width and height
        int width = (int) (display.getWidth() * 0.43);
        int height = (int) (display.getHeight() * 0.55);
        // Settings window size
        w.setLayout(width, height);
        // Settings window display position
        w.setGravity(Gravity.CENTER);
        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        playSettingListView.setOnKeyListener(onkeyListenter);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                int position = playSettingListView.getSelectedItemPosition();
                Bundle mBundle = new Bundle();
                Message msg = new Message();
                msg.what = PhotoPlayerActivity.PHOTO_PLAY_SETTING;
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    switch (position) {
                    case 0:
                        if (isOpen) {
                            isOpen = false;
                            Tools.setPlaySettingOpt(0, close, 1);
                        } else {
                            isOpen = true;
                            Tools.setPlaySettingOpt(0, open, 1);
                        }
                        break;
                    case 1:
                        if (time > 1) {
                            time--;
                            Tools.setPlaySettingOpt(1, "" + time, 1);
                        }
                        break;
                     default:
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    mBundle.putInt("time", time * 1000);
                    mBundle.putBoolean("isOpen", isOpen);
                    msg.setData(mBundle);
                    mHandler.sendMessage(msg);
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    Log.e("***********", "*********position********" + position);
                    switch (position) {
                    case 0:
                        if (isOpen) {
                            isOpen = false;
                            Tools.setPlaySettingOpt(0, close, 1);
                        } else {
                            isOpen = true;
                            Tools.setPlaySettingOpt(0, open, 1);
                        }
                        break;
                    case 1:
                        if (time < 10) {
                            time++;
                            Tools.setPlaySettingOpt(1, "" + time, 1);
                        }
                        break;
                     default:
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    mBundle.putInt("time", time * 1000);
                    mBundle.putBoolean("isOpen", isOpen);
                    msg.setData(mBundle);
                    mHandler.sendMessage(msg);
                    break;
                }
                }
            }
            default:
                break;
            }
            return false;
        }
    };
}
