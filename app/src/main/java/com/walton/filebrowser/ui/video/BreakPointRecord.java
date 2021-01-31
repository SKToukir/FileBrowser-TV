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
package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

public class BreakPointRecord {

    private static BreakPointDBAdapter dbAdapter;
    static SharedPreferences sharedPreferences;

    /**
     * Inquires the record.
     * @param path
     * @param size
     * @param context
     * @return
     */
    public static synchronized int getData(String path, String size, Context context) {
        if (dbAdapter == null) {
            dbAdapter = new BreakPointDBAdapter(context);
            dbAdapter.open();
        }
        path = Base64.encodeToString(path.getBytes(), Base64.DEFAULT);
        Log.i("BreakPointRecord", "****************Base64******getData******" + path);
        BreakPointInfo[] breakPoint = dbAdapter.queryOneData(path, size);
        if(breakPoint != null){
            return breakPoint[0].Time;
        }
        return 0;
    }

    public static void AddDataThread(final String path, final int time, final String size, final Context context){
        Thread AddDataThread = new Thread(new Runnable(){
            public void run(){
                Log.i("BreakPointRecord","AddDataThread start to addData");
                addData(path, time, size, context);
            }
        });
        AddDataThread.setName("AddDataThread");
        AddDataThread.start();
    }

    /**
     * Add the record.
     * @param path
     * @param time
     * @param size
     * @param context
     */
    public static synchronized void addData(final String path, final int time, final String size, final Context context){
        Log.i("BreakPointRecord","addData time = "+time);
        if(time < 0){
            return;
        }
        //new Thread(new Runnable(){
          //  public void run(){
                String encodePath = path;
                if (dbAdapter == null) {
                    dbAdapter = new BreakPointDBAdapter(context);
                    dbAdapter.open();
                }
                encodePath = Base64.encodeToString(encodePath.getBytes(), Base64.DEFAULT);
                Log.i("BreakPointRecord", "****************Base64*****addData*******" + encodePath);
                BreakPointInfo breakPoint = new BreakPointInfo();
                breakPoint.Path = encodePath;
                breakPoint.Time = time;
                breakPoint.Size = size;
                dbAdapter.deleteOneData(encodePath, size);
                long colunm = dbAdapter.insert(breakPoint);
                if (colunm == -1) {
                    Log.i("BreakPointRecord", "********add breakPoint fail!********");
                } else {
                    Log.i("BreakPointRecord",
                            "********add breakPoint success!******** ID: "+ String.valueOf(colunm));
                }
                dbAdapter.queryAllData();
            //}
        //}).start();
    }

    /**
     * close db.
     */
    public static synchronized void closeDB(){
        Log.i("BreakPointRecord","closeDB");
        if(dbAdapter != null){
            dbAdapter.close();
            dbAdapter = null;
        }
    }

    public static synchronized boolean getBreakPointFlag(Context context)
    {
        return context.getSharedPreferences("breakpoint", Context.MODE_PRIVATE).getBoolean("needBreakpoint", false);
    }

    public static synchronized void setBreakPointFlag(Context context, boolean flag)
    {
        context.getSharedPreferences("breakpoint", Context.MODE_PRIVATE).edit().putBoolean("needBreakpoint", flag).commit() ;
    }
}
