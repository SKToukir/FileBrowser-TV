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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 * Home page data browsing class.
 */
public class TopDataBrowser {

    private static final String TAG = "TopDataBrowser";

    private static final int POSITION_0 = 0;

    private static final int POSITION_1 = 1;

    private static final int POSITION_2 = 2;

    private Activity activity;

    private Handler handler;

    private List<BaseData> data;

    private String source[] = null;

    public TopDataBrowser(Activity activity, Handler handler,
            List<BaseData> data) {
        this.activity = activity;
        this.handler = handler;
        this.data = data;
    }

    /**
     * Refresh the home page display data.
     */
    protected void refresh() {
        // clear data
        if (data != null) {
            data.clear();
        }

        // Structure home page need to display data(in string.xml)
        if (source == null) {
            source = activity.getResources()
                    .getStringArray(R.array.data_source);
        }

        for (String cell : source) {
            BaseData item = new BaseData(Constants.FILE_TYPE_DIR);
            item.setName(cell);
            data.add(item);
        }

        // Refresh the main interface, arg1: the current page number, arg2:
        // total page
        Message msg = handler.obtainMessage();
        msg.what = Constants.UPDATE_TOP_DATA;
        msg.arg1 = 1;
        msg.arg2 = 1;
        handler.sendMessage(msg);

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
        Log.d(TAG, "position : " + position + " keyCode : " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            browser(position);

            return true;
        }

        return false;
    }

    /**
     * According to the parameter value browse different data sources.
     *
     * @param position
     *            </br> <li>0 : local data <li>1 : dlna data <li>2 : samba data
     */
    private void browser(int position) {
        // Local data entry
        if (position == POSITION_0) {
            handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
            // samba data entry
        } else if (position == POSITION_1) {
            // Network available
            if (Tools.isNetWorkConnected(activity)) {
                handler.sendEmptyMessage(Constants.BROWSER_SAMBA_DATA);

                // Network not only hint user network status anomaly
            } else {
                Message message = handler.obtainMessage();
                message.what = Constants.UPDATE_EXCEPTION_INFO;
                message.arg1 = Constants.NETWORK_EXCEPTION;
                handler.sendMessage(message);
            }
        }
        /* remove dlna temporary , should not run in here.
        else if (position == POSITION_2) {
            // Network available at the start of loading dlna data
            if (Tools.isNetWorkConnected(activity)) {
                handler.sendEmptyMessage(Constants.BROWSER_DLNA_DATA);

                // Network not only hint user network status anomaly
            } else {
                Message message = handler.obtainMessage();
                message.what = Constants.UPDATE_EXCEPTION_INFO;
                message.arg1 = Constants.NETWORK_EXCEPTION;
                handler.sendMessage(message);
            }
        }
        */
    }

}
