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

package com.walton.filebrowser.business.video;

import android.util.Log;
import java.io.InputStream;
import java.io.IOException;
import com.walton.filebrowser.util.Bluray;

public class ISOReadAdapter extends InputStream {
     private String TAG = "ISOReadAdapter";
     long mOffset = 0;
     long fileLength = 0;
     long position = 0;
     int titleNum = 0;
     long titltSize;
     int currentTitle = 0;
     int duration = 0; // in ms
     int clipNum;
     int []clipStartTime;
     long []clipLength;
     boolean b3dStream;
     public ISOReadAdapter(int titleIndex) {
         currentTitle = titleIndex;
         Bluray.selectTitle(currentTitle);
         fileLength = Bluray.getTitleSize();
         Log.i(TAG,"the file length is:"+fileLength);
         duration = (int)(Bluray.getTitleDuration(currentTitle) / 45);
         clipNum = Bluray.getClipNum(currentTitle);
         b3dStream = Bluray.getIs3DStream(currentTitle);
         Log.v(TAG, "title index " + currentTitle + " length " + fileLength + " duration in ms " + duration + " clipNum " + clipNum + " b3dStream " + b3dStream);

         if (duration > 1000 && fileLength <= 0) {
             fileLength = -1;
         }
         clipStartTime = new int[clipNum];
         clipLength = new long[clipNum];
         for(int i = 0; i< clipNum; i++) {
             clipStartTime[i] = Bluray.getClipDuration(currentTitle, i);
             clipLength[i] = Bluray.getClipLength(currentTitle, i);
         }
     }

     public long available64() throws IOException {
         Log.v(TAG, "available64 " + fileLength);
         return fileLength;
     }

     public int available() throws IOException {
         // Log.v(TAG, "available " + (int)fileLength);
         int fl = (int)fileLength;
         if (fl < 0) {
             fl = -1;
         }
         // return (int)fileLength;
         return fl;
     }

     public void mark(int readlimit) {
         Log.v(TAG, "Sets a mark position in this InputStream. readlimit " + readlimit);
     }

     public boolean markSupported() {
         Log.v(TAG, "Get markSupported");
         return false;
     }

     public int read() throws IOException {
         return Bluray.read(1);
     }

     public int read(byte[] buffer) throws IOException {
         //Log.v(TAG, "read " + buffer.length + " bytes");
         return read(buffer, 0, buffer.length);
     }

     public int read(byte[] buffer, int offset, int length) throws IOException {
         //Log.i(TAG, "read offset " + offset + " length " + length + " current pos " + Bluray.tell());
         int requsetLen;
         int realLen = 0;

         if (fileLength > 0) {
             requsetLen = (int)(fileLength - Bluray.tell() >= length ?
                     length : fileLength - Bluray.tell());
         } else {
             requsetLen = length;
         }

         realLen = Bluray.read(buffer, offset, requsetLen);
         if (realLen != requsetLen)
         {
             Log.e(TAG, "Read error request len " + requsetLen + " return len " + realLen);
         }

         if (realLen <= 0) {
             return -2;
         }

         mOffset += realLen;
         return realLen;
     }
     @Override
     public long skip(long byteCount) throws IOException {
         long nCurrent = Bluray.tell();
         long nTargetpPos = nCurrent + byteCount;
         long result = Bluray.seek(nTargetpPos);
         Log.i(TAG, "skip " + byteCount + " current " + nCurrent + " seek position  " + nTargetpPos + " result " + result);

         if (Bluray.tell() < nTargetpPos)
         {
             byte[] skipBytes = new byte[(int)(nTargetpPos - Bluray.tell())];
             read(skipBytes);
             skipBytes = null;
         }

         Log.v(TAG, "skip result " + Bluray.tell());
         return Bluray.tell();
     }

     public int getRefDuration() {
         return duration;
     }

     public int getClipNum(){
         int clipNum = Bluray.getClipNum(currentTitle);
         Log.v(TAG, "getClipNum " + clipNum);
         return clipNum;
     }

     public int getClipRefTime(int n) {
         int Clipduration = Bluray.getClipDuration(currentTitle, n);
         // Log.v(TAG, "getClipRefTime index " + n + " Clipduration " + Clipduration);

         return Clipduration;
     }

     public int getTimeBaseByPosition(long nPos) {
         long totalLen = 0;
         int timeBase = 0;
         for(int i = 0; i< clipNum; i++) {
             totalLen += clipLength[i];
             if (nPos <= totalLen) {
                 return clipStartTime[i];
             }
             // timeBase += clipDuration[i];
         }

         return clipStartTime[clipNum-1];
     }

     public int getTotalTime() {
         return duration;
     }

     public long getClipLength(int clip) {
         return clipLength[clip];
     }

     public long seekByTime(int timeMs) {
         Log.v(TAG, "seekByTime timeMs " + timeMs);
         return Bluray.seekTime(timeMs * 90);
     }
 }
