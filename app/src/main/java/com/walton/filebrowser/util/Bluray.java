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

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import android.content.Context;
import android.util.Log;

public class Bluray {

    public static final String TAG = "LibblurayReflect" ;
    public static final String sLibbluray = "org.videolan.Libbluray" ;
    public static final String sLibraryPath = "/system/framework/com.mstar.bluray.jar";
    private static Class sClassLibbluray = null;

    private static boolean sIsSupport = false;
    private static boolean sTryLoadClass = false;

    private static boolean loadClass(String libPath,String tmpPath,String className) {
        File file = new File(libPath);
        if (file.exists()) {
           sTryLoadClass = true;
           ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
           DexClassLoader localDexClassLoader = new DexClassLoader(libPath,tmpPath, null,
                                                                   localClassLoader);
           try {
                sClassLibbluray = localDexClassLoader.loadClass(className);
           } catch (Exception ex) {
                Log.w(TAG, "load class "+sLibbluray+" failed !");
                ex.printStackTrace();
                sClassLibbluray = null;
           }
        }else{
            sClassLibbluray = null;
            Log.e(TAG, "java library not exist");
        }
        file = null;
        return sClassLibbluray != null;
    }
    public static boolean isSupport(Context c) {

        if (sTryLoadClass) {
            return sIsSupport;
        }

        if (!loadClass(sLibraryPath,c.getCacheDir().getAbsolutePath(),sLibbluray)) {
            sIsSupport = false;
        } else {
           try {
                Class cls = sClassLibbluray;// Class.forName(sLibbluray);
                Method staticMethod = cls.getDeclaredMethod("isNativeLibSupport");
                Object rlt = staticMethod.invoke(cls);
                sIsSupport = ((Boolean)rlt).booleanValue();
            } catch (Exception e) {
                Log.e(TAG,"invoke isSupport failed");
                e.printStackTrace();
                sIsSupport = false;
            }
        }

        Log.d(TAG, "isSupport "+sIsSupport);
        return sIsSupport;
    }

    public static boolean init(String devicePath, String keyfilePath) {

        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("init",String.class,String.class);
            Object rlt = staticMethod.invoke(cls,devicePath,keyfilePath);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke init failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "init "+result);
        return result;
    }

    public static void deinit() {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("deinit");
            Object rlt = staticMethod.invoke(cls);
        } catch (Exception e) {
            Log.e(TAG,"invoke deinit failed");
            e.printStackTrace();
        }
    }

    public static int read(byte[] buff, int offset, int len) {

        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("read",byte[].class,int.class,int.class);
            Object rlt = staticMethod.invoke(cls,buff,offset,len);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke read failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "read "+result);
        return result;
    }

    public static int  read(int len) {

        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("read",int.class);
            Object rlt = staticMethod.invoke(cls,len);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke read failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "read "+result);
        return result;
    }

    public static byte[] getVolumeID() {
        byte result[] = null;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getVolumeID");
            Object rlt = staticMethod.invoke(cls);
            result = (byte[])rlt;
        } catch (Exception e) {
            Log.e(TAG,"invoke getVolumeID failed");
            e.printStackTrace();
            result = null;
        }

        Log.d(TAG, "getVolumeID "+result);
        return result;
    }

    public static byte[] getPMSN() {
        byte result[] = null;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getPMSN");
            Object rlt = staticMethod.invoke(cls);
            result = (byte[])rlt;
        } catch (Exception e) {
            Log.e(TAG,"invoke getPMSN failed");
            e.printStackTrace();
            result = null;
        }

        Log.d(TAG, "getPMSN "+result);
        return result;
    }

    public static byte[] getDeviceBindingID() {
        byte result[] = null;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getDeviceBindingID");
            Object rlt = staticMethod.invoke(cls);
            result = (byte[])rlt;
        } catch (Exception e) {
            Log.e(TAG,"invoke getDeviceBindingID failed");
            e.printStackTrace();
            result = null;
        }

        Log.d(TAG, "getDeviceBindingID "+result);
        return result;
    }

    public static int getTitles() {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getTitles");
            Object rlt = staticMethod.invoke(cls);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getTitles failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getTitles "+result);
        return result;
    }

    /*
    public static TitleInfo getTitleInfo(int titleNum) {

    }
    */

    public static long getTitleDuration(int titleIndex) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getTitleDuration",int.class);
            Object rlt = staticMethod.invoke(cls,titleIndex);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getTitleDuration failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getTitleDuration "+result);
        return result;
    }

   /*
   public static PlaylistInfo getPlaylistInfo(int playlist) {

    }
    */

    public static long seek(long pos) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seek",long.class);
            Object rlt = staticMethod.invoke(cls,pos);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke seek failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "seek "+result);
        return result;
    }

    public static long seekTime(long tick) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seekTime",long.class);
            Object rlt = staticMethod.invoke(cls,tick);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke seekTime failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "seekTime "+result);
        return result;
    }

    public static long seekChapter(int chapter) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seekChapter",int.class);
            Object rlt = staticMethod.invoke(cls,chapter);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke seekChapter failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "seekChapter "+result);
        return result;
    }

    public static long chapterPos(int chapter) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("chapterPos",int.class);
            Object rlt = staticMethod.invoke(cls,chapter);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke chapterPos failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "chapterPos "+result);
        return result;
    }

    public static int getCurrentChapter(){
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getCurrentChapter");
            Object rlt = staticMethod.invoke(cls);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getCurrentChapter failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getCurrentChapter "+result);
        return result;
    }

    public static long seekMark(int mark) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seekMark",int.class);
            Object rlt = staticMethod.invoke(cls,mark);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke seekMark failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "seekMark "+result);
        return result;
    }

    public static long seekPlayItem(int clip) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seekPlayItem",int.class);
            Object rlt = staticMethod.invoke(cls,clip);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke seekPlayItem failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "seekPlayItem "+result);
        return result;
    }

    public static boolean selectPlaylist(int playlist) {
        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("selectPlaylist",int.class);
            Object rlt = staticMethod.invoke(cls,playlist);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke selectPlaylist failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "selectPlaylist "+result);
        return result;
    }

    /*
    public static boolean selectTitle(TitleImpl title) {

    }
    */

    public static boolean selectTitle(int title) {
        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("selectTitle",int.class);
            Object rlt = staticMethod.invoke(cls,title);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke selectTitle failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "selectTitle "+result);
        return result;
    }

    public static boolean selectAngle(int angle) {
        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("selectAngle",int.class);
            Object rlt = staticMethod.invoke(cls,angle);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke selectAngle failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "selectAngle "+result);
        return result;
    }

    public static void seamlessAngleChange(int angle) {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("seamlessAngleChange",int.class);
            staticMethod.invoke(cls,angle);
        } catch (Exception e) {
            Log.e(TAG,"invoke seamlessAngleChange failed");
            e.printStackTrace();
        }
    }

    public static long getTitleSize() {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getTitleSize");
            Object rlt = staticMethod.invoke(cls);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getTitleSize failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getTitleSize "+result);
        return result;
    }

    public static int getCurrentTitle() {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getCurrentTitle");
            Object rlt = staticMethod.invoke(cls);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getCurrentTitle failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getCurrentTitle "+result);
        return result;
    }

    public static int getCurrentAngle() {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getCurrentAngle");
            Object rlt = staticMethod.invoke(cls);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getCurrentAngle failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getCurrentAngle "+result);
        return result;
    }

    public static long tell() {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("tell");
            Object rlt = staticMethod.invoke(cls);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke tell failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "tell "+result);
        return result;
    }

    public static long tellTime() {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("tellTime");
            Object rlt = staticMethod.invoke(cls);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke tellTime failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "tellTime "+result);
        return result;
    }

    public static boolean selectRate(float rate) {
        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("selectRate",float.class);
            Object rlt = staticMethod.invoke(cls,rate);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke selectRate failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "selectRate "+result);
        return result;
    }

    public static void writeGPR(int num, int value) {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("writeGPR",int.class,int.class);
            staticMethod.invoke(cls,num,value);
        } catch (Exception e) {
            Log.e(TAG,"invoke writeGPR failed");
            e.printStackTrace();
        }
    }

    public static int readGPR(int num) {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("readGPR",int.class);
            Object rlt = staticMethod.invoke(cls,num);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke readGPR failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "readGPR "+result);
        return result;
    }

    public static int readPSR(int num) {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("readPSR",int.class);
            Object rlt = staticMethod.invoke(cls,num);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke readPSR failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "readPSR "+result);
        return result;
    }

    public static void updateGraphic(int width, int height, int[] rgbArray) {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("updateGraphic",int.class,int.class,int[].class);
            staticMethod.invoke(cls,width,height,rgbArray);
        } catch (Exception e) {
            Log.e(TAG,"invoke updateGraphic failed");
            e.printStackTrace();
        }
    }

    public static void updateGraphic(int width, int height, int[] rgbArray,
            int x0, int y0, int x1, int y1) {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("updateGraphic",
                                                                int.class,
                                                                int.class,
                                                                int[].class,
                                                                int.class,
                                                                int.class,
                                                                int.class,
                                                                int.class);
            staticMethod.invoke(cls,width,height,rgbArray,x0,y0,x1,y1);
        } catch (Exception e) {
            Log.e(TAG,"invoke updateGraphic failed");
            e.printStackTrace();
        }
    }

    public static int getClipNum(int nTitle) {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getClipNum",int.class);
            Object rlt = staticMethod.invoke(cls,nTitle);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getClipNum failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getClipNum "+result);
        return result;
    }

    public static int getClipDuration(int nTitle, int nClipIndex) {
        int result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getClipDuration",int.class,int.class);
            Object rlt = staticMethod.invoke(cls,nTitle,nClipIndex);
            result = ((Integer)rlt).intValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getClipDuration failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getClipDuration "+result);
        return result;
    }

    public static long getClipLength(int nTitle, int nClipIndex) {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getClipLength",int.class,int.class);
            Object rlt = staticMethod.invoke(cls,nTitle,nClipIndex);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getClipLength failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getClipLength "+result);
        return result;
    }

    public static boolean getIs3DStream(int nTitle) {
        boolean result = false;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getIs3DStream",int.class);
            Object rlt = staticMethod.invoke(cls,nTitle);
            result = ((Boolean)rlt).booleanValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getIs3DStream failed");
            e.printStackTrace();
            result = false;
        }

        Log.d(TAG, "getIs3DStream "+result);
        return result;
    }

    public static void setEnable3D(boolean benable) {
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("setEnable3D",boolean.class);
            staticMethod.invoke(cls,benable);
        } catch (Exception e) {
            Log.e(TAG,"invoke setEnable3D failed");
            e.printStackTrace();
        }
    }

    public static long getNativePointer() {
        long result = -1;
        try {
            Class cls = sClassLibbluray;// Class.forName(sLibbluray);
            Method staticMethod = cls.getDeclaredMethod("getNativePointer");
            Object rlt = staticMethod.invoke(cls);
            result = ((Long)rlt).longValue();
        } catch (Exception e) {
            Log.e(TAG,"invoke getNativePointer failed");
            e.printStackTrace();
            result = -1;
        }

        Log.d(TAG, "getNativePointer "+result);
        return result;
    }

}
