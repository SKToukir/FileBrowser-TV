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
package com.walton.filebrowser.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Field;

/**
 *
 * when the tv device is disconnected with network,samba or dlna shuld be over.
 *
 * this receiver will receive wifi,pppoe and ethernet broadcast.
 *
 *
 *
 * @author Jacky Zhu
 */
public class NetworkStatusChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetStatusChangeReceiver";
    // pppoe network status change action
    private final String PPPOE_STATE_ACTION = "com.mstar.android.pppoe.PPPOE_STATE_ACTION";
    // pppoe network status
    private final String PPPOE_STATE_STATUE = "PppoeStatus";
    // pppoe network connect status
    private final String PPPOE_STATE_CONNECT = "connect";
    // pppoe network disconnect status
    private final String PPPOE_STATE_DISCONNECT = "disconnect";
    // ethernet status change action
    private final String ETHERNET_STATE_CHANGED_ACTION = "com.mstar.android.ethernet.ETHERNET_STATE_CHANGED";


    //see device/mstar/common/libraries/ethernet/java/com/mstar/android/ethernet/EthernetStateTracker.java
    public static final int EVENT_DHCP_START                        = 0;
    public static final int EVENT_INTERFACE_CONFIGURATION_SUCCEEDED = 1;
    public static final int EVENT_INTERFACE_CONFIGURATION_FAILED    = 2;
    public static final int EVENT_HW_CONNECTED                      = 3;
    public static final int EVENT_HW_DISCONNECTED                   = 4;
    public static final int EVENT_HW_PHYCONNECTED                   = 5;
    public static final int EVENT_STOP_INTERFACE                    = 6;
    public static final int EVENT_RESET_INTERFACE                   = 7;
    public static final int EVENT_ADDR_REMOVE                       = 8;
    public static final int EVENT_PROXY_CHANGE                      = 9;
    private static final int NOTIFY_ID                              = 10;
    //see  /frameworks/base/core/java/android/net/ConnectivityManager.java
    public static final String CONNECTIVITY_ACTION_IMMEDIATE =
             "android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE";  //android L
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"; //android 6.0

    // flag for pppoe connect status
    private boolean mPppoeFlag = false;
    // flag for ethernet connect status
    private boolean mEthernetFlag = false;
    // flag for wifi connect status
    private boolean mWifiFlag = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG,"onReceive intent:"+intent);
        if (action.equals(PPPOE_STATE_ACTION)) {
            String pppoeState = intent.getStringExtra(PPPOE_STATE_STATUE);
            if (pppoeState.equals(PPPOE_STATE_CONNECT)) { // pppoe connect
                mPppoeFlag = true;
            } else if (pppoeState.equals(PPPOE_STATE_DISCONNECT)) { // disconnect
                mPppoeFlag = false;
            }
        } else if (action.equals(ETHERNET_STATE_CHANGED_ACTION)) { // for android 4.4 or 4.3
            final int event = intent.getIntExtra("ETHERNET_state", 0);
            switch (event) {
                case EVENT_HW_CONNECTED:
                case EVENT_HW_PHYCONNECTED:
                case EVENT_INTERFACE_CONFIGURATION_SUCCEEDED: // connect
                    mEthernetFlag = true;
                    break;
                case EVENT_HW_DISCONNECTED:
                case EVENT_INTERFACE_CONFIGURATION_FAILED: // disconnect
                    mEthernetFlag = false;
                    break;
            }
        } else if (action.equals(CONNECTIVITY_ACTION_IMMEDIATE)|| // for android L and android 6.0
                   action.equals(CONNECTIVITY_ACTION)) {
            final NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(
                    ConnectivityManager.EXTRA_NETWORK_INFO);
            final boolean Connected = networkInfo != null && networkInfo.isConnected();
            if (Connected && (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)) { //ethernet connected
                mEthernetFlag = true;
            } else { // ethernet disconnected
                mEthernetFlag = false;
            }
        } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            final NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            boolean mWifiConnected = networkInfo != null
                    && networkInfo.isConnected();
            // wifi connect
            if (mWifiConnected) {
                mWifiFlag = true;
                // wifi disconnect
            } else {
                mWifiFlag = false;
            }
        }

        if (Tools.isNetWorkConnected(context)) {
            setNetWorkFlag(context);
        }
        Log.i(TAG, "mWifiFlag:" + mWifiFlag + " mEthernetFlag:" + mEthernetFlag + " mPppoeFlag:" + mPppoeFlag);
        // no network
        if (!mWifiFlag && !mEthernetFlag && !mPppoeFlag) {
            Intent networkIntent = new Intent(
                    "com.mstar.localmm.network.disconnect");
            Log.i(TAG, "send com.mstar.localmm.network.disconnect");
            context.sendBroadcast(networkIntent);
        }
    }

    public void setNetWorkFlag(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo wifiNetworkInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetworkInfo != null) {
                    mWifiFlag = wifiNetworkInfo.isAvailable();
                }

                NetworkInfo ethernetNetworkInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                if (ethernetNetworkInfo != null) {
                    mEthernetFlag = ethernetNetworkInfo.isAvailable();
                }

                try {
                    Class clz = Class.forName("android.net.ConnectivityManager");
                    Field field = clz.getDeclaredField("TYPE_PPPOE");
                    Log.i(TAG, "field:" + field);
                    if (field != null) {
                        NetworkInfo pppoeNetworkInfo = connectivityManager
                                .getNetworkInfo(field.getInt(field.getName()));
                        if (pppoeNetworkInfo != null) {
                            mPppoeFlag = pppoeNetworkInfo.isAvailable();
                        }
                    }

                } catch (Exception e) {
                    Log.i(TAG, "Can't find android.net.ConnectivityManager TYPE_PPPOE !");
                    e.printStackTrace();
                }


            }

        }
    }

}
