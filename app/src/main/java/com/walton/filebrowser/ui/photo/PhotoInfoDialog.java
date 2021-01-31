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

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.Tools;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Custom picture detailed information Dialog.
 *
 * @author ducj
 */
public class PhotoInfoDialog extends Dialog {
    private static final String TAG = "PhotoInfoDialog";
    private BaseData mPhotoFile;
    // Dialog box in of each component
    private TextView mFileName;
    private TextView mFileSize;
    private TextView mFileFormat;
    private TextView mDuration;
    private SimpleDateFormat mFormat = new SimpleDateFormat();

    /**
     *
     * Structure function.
     */
    public PhotoInfoDialog(Activity activity, BaseData photoFile) {
        super(activity);
        this.mPhotoFile = photoFile;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.photo_info);
        // Settings dialog related attributes, such as: the background color,
        // the width and height, the location of display, etc
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        Point point = new Point();
        display.getSize(point);
        int width = (int) (display.getWidth() * 0.25);
        int height = (int) (display.getHeight() * 0.6);
        w.setLayout(width, height);
        w.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 80;
        //wl.screenBrightness = 1.0f;
        w.setAttributes(wl);
        w.setBackgroundDrawableResource(color.transparent);
        // init date format
        mFormat.applyPattern("yyyy-MM-dd  HH:mm:ss");
        // init control
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updatePhotoInfo();
    }

    /**
     *
     * Initialization Dialog of components.
     */
    private void findViews() {
        mFileName = (TextView) findViewById(R.id.file_name);
        mFileSize = (TextView) findViewById(R.id.file_zise);
        mFileFormat = (TextView) findViewById(R.id.file_format);
        mDuration = (TextView) findViewById(R.id.duration);
    }

    /**
     *
     * Update is currently playing picture of the detailed information.
     */
    private void updatePhotoInfo() {
        if (mPhotoFile != null) {
            File f = new File(mPhotoFile.getPath());
            String formatSize = Tools.formatSize(BigInteger.valueOf(f
                    .length()));
            String ext = mPhotoFile.getName().toLowerCase().substring(mPhotoFile.getName().lastIndexOf(".") + 1);
            // name of photo
            mFileName.setText(mPhotoFile.getName());
            // size of photo
            mFileSize.setText(formatSize);
            // format of photo, such as .png
            mFileFormat.setText(ext);
            long modifiedTime = f.lastModified();
            Date date = new Date(modifiedTime);
            if (modifiedTime != 0) {
                mDuration.setText(mFormat.format(date));
            }
        }
    }
}
