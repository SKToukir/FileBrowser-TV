//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Pair;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.walton.filebrowser.R;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import com.mstar.android.samba.HttpBean;
import com.walton.filebrowser.ui.MediaContainerApplication;


/**
 * Project name: LocalMM class name: Tools class description: tool class
 *
 * @author luoyong
 * @date 2011-07-27
 */
public class Tools {
    private static final String FILE_SIZE_B = "B";
    private static final String FILE_SIZE_KB = "KB";
    private static final String FILE_SIZE_MB = "MB";
    private static final String FILE_SIZE_GB = "GB";
    private static final String FILE_SIZE_TB = "TB";
    private static final String FILE_SIZE_NA = "N/A";
    private static final String TAG = "Tools";
    public static int CURPOS = 0;
    private static int mHdrAttributes[] = new int[2];
    private static final int ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED = -1;
    private static final HashMap<Integer, String> mModuleMapTable = new HashMap<Integer, String>();
    private static List<Pair<String, Integer>> mAndroidConfigList = null;
    private static String FILE_FEATURE_XML = "/etc/feature.xml";
    private static String FILE_SYS_CONFIG = "/config/sys.ini";
    private static String FILE_SYS_TVCONFIG = "/tvconfig/config/sys.ini";

    private static String FILE_FEATURE_DEFAULT_XML = "/etc/feature_default.xml";
    private static final int mVideoPlaySettingOptionSum = 10;
    /**
     * judgment file exists.
     *
     * @param path
     *            file path.
     * @return when the parameters specified file exists return true, otherwise
     *         returns false.
     */
    public static boolean isFileExist(String path) {
        return isFileExist(new File(path));
    }

    /**
     * judgment file exists.
     *
     * @param file
     *            {@link File}.
     * @return when the parameters specified file exists return true, otherwise
     *         returns false.
     */
    public static boolean isFileExist(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    /**
     * will millisecond number formatting 00:00:00.
     *
     * @param duration
     *            time value to seconds for the unit.
     * @return format for 00:00:00 forms of string or '-- : -- : -- ".
     */
    public static String formatDuration(long duration) {
        long time = duration / 1000;
        if (time <= 0) {
            return "--:--:--";
        }
        long min = time / 60 % 60;
        long hour = time / 60 / 60;
        long second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    public static String formatISODuration(long duration) {
        long time = duration / 90000;
        if (time <= 0) {
            return "--:--:--";
        }
        long min = time / 60 % 60;
        long hour = time / 3600;
        long second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    /**
     * will millisecond number formatting 00:00.
     *
     * @ param duration time value to seconds for the unit. @ return format for
     * 00:00 forms of string.
     */
    public static String formatDuration(int duration) {
        int time = duration / 1000;
        if (time == 0 && duration > 0) {
            time = 1;
        }

        int second = time % 60;
        int min = time / 60;
        long hour = 0;
        if (min > 60) {
            hour =  time / 3600;
            min = time / 60 % 60;
        }
        if (hour > 0)
            return String.format("%02d:%02d:%02d", hour, min, second);
        else
            return String.format("%02d:%02d", min, second);
    }

    /**
     * will file size into human form, the biggest said 1023 TB.
     *
     * @ param size file size. @ return file size minimum unit for B string.
     */
    public static String formatSize(BigInteger size) {
        // Less than
        if (size.compareTo(BigInteger.valueOf(1024)) == -1) {
            return (size.toString() + FILE_SIZE_B);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024)).toString() + FILE_SIZE_KB);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024)).toString() + FILE_SIZE_MB);
        } else if (size.compareTo(BigInteger
                .valueOf(1024 * 1024 * 1024 * 1024L)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024))
                    .toString() + FILE_SIZE_GB);
        } else if (size.compareTo(BigInteger
                .valueOf(1024 * 1024 * 1024 * 1024L).multiply(
                        BigInteger.valueOf(1024))) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L))
                    .toString() + FILE_SIZE_TB);
        }
        return FILE_SIZE_NA;
    }

    /**
     * judgment parameter the specified file type is media files.
     *
     * @ param type said file types of string. @ return if it is media file
     * returns true on, otherwise returns false.
     */
    public static boolean isMediaFile(final int type) {
        if (Constants.FILE_TYPE_PICTURE == type
                || Constants.FILE_TYPE_SONG == type
                || Constants.FILE_TYPE_VIDEO == type) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return If the network can be used to return to true, otherwise returns
     *         false.
     */
    public static boolean isNetWorkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                // Get the network connection management object
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null) {
                    return false;
                }
                boolean isConnected = ni.isConnected();
                boolean isStateConnected = (ni.getState() == NetworkInfo.State.CONNECTED);
                Log.i(TAG, "isNetWorkConnected isConnected:" + isConnected + " isStateConnected:" + isStateConnected);
                if (isConnected && isStateConnected) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "network exception." + e.getMessage());
        }
        return false;
    }

    /**
     * @return The current usb disk equipment loading position.
     */
    public static String getUSBMountedPath() {
        return Environment.getExternalStorageDirectory().getParent();
    }

    // Play Settings option value
    private static String[] playSettingOptTextOne = null;
    private static String[] playSettingOptTextTwo = null;

    /**
     * @return Initialization play Settings related option value
     */
    public static String[] initPlaySettingOpt(Context context, int id) {
        if(playSettingOptTextOne == null) {
            playSettingOptTextOne = context.getResources().getStringArray(
                    R.array.play_setting_opt);
            if (isThumbnailModeOn()) {
                playSettingOptTextOne[4] = context.getString(R.string.play_setting_0_value_1);
            } else {
                playSettingOptTextOne[4] = context.getString(R.string.play_setting_0_value_2);
            }
        }
        if (playSettingOptTextTwo == null) {
            playSettingOptTextTwo = context.getResources().getStringArray(
                    R.array.play_setting_opt);
        }
        if (id == 1) {
            playSettingOptTextOne[6] = context.getString(R.string.play_setting_0_value_2);
            return playSettingOptTextOne;
        } else {
            playSettingOptTextTwo[6] = context.getString(R.string.play_setting_0_value_2);
            return playSettingOptTextTwo;
        }
    }

    /**
     * @param index
     *            initPlaySettingOpt
     */
    public static String getPlaySettingOpt(int index, int id) {
        if (id == 1) {
            if (index >= 0 && index < mVideoPlaySettingOptionSum) {
                if (playSettingOptTextOne != null) {
                    return playSettingOptTextOne[index];
                }
                return null;
            }
        } else {
            if (index >= 0 && index < mVideoPlaySettingOptionSum) {
                if (playSettingOptTextTwo != null) {
                    return playSettingOptTextTwo[index];
                }
                return null;
            }
        }
        return null;
    }

    /**
     * @ param index subscript index value. @ param value to set to value.
     */
    public static void setPlaySettingOpt(int index, final String value, int id) {
        if (id == 1) {
            if (index >= 0 && index < mVideoPlaySettingOptionSum) {
                if (playSettingOptTextOne != null) {
                    playSettingOptTextOne[index] = value;
                }
            }
        } else {
            if (index >= 0 && index < mVideoPlaySettingOptionSum) {
                if (playSettingOptTextTwo != null) {
                    playSettingOptTextTwo[index] = value;
                }
            }
        }
        if (index == 2) {
            if (playSettingOptTextOne != null) {
                playSettingOptTextOne[index] = value;
            }
            if (playSettingOptTextTwo != null) {
                playSettingOptTextTwo[index] = value;
            }
        }
    }

    /**
     * Exit video player when eliminate all Settings option value.
     */
    public static void destroyAllSettingOpt() {
        playSettingOptTextOne = null;
        playSettingOptTextTwo = null;
    }

    /**
     * Stop media scanning.
     */
    public static void stopMediascanner(Context context) {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_stop");
        context.sendBroadcast(intent);
        Log.d("Tools", "stopMediascanner");
    }

    /**
     * Start media scanning.
     */
    public static void startMediascanner(Context context) {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_start");
        context.sendBroadcast(intent);
        Log.d("Tools", "startMediascanner");
    }

    /**
     * The size of file is whether larger than the specified size.
     *
     * @param path
     *            absolute path of file.
     * @param size
     *            the specified size of file.
     * @return true if the file is larger than size, otherwise false.
     */
    public static boolean isLargeFile(final String path, final long size) {
        File file = new File(path);
        // file does not exist
        if (!isFileExist(file)) {
            Log.d("Tools", "file does not exist");
            return true;
        }
        long length = file.length();
        Log.d("Tools", "size of file : " + length);
        // file bigger than size
        if (length > size) {
            return true;
        }
        return false;
    }

    /**
     * Screen open and cover screen
     *
     * @param isMute
     *            cover/open
     * @param time
     */
    public static void setVideoMute(boolean isMute, int time) {
        if (unSupportTVApi()) {
            return;
        }
        Log.i("Tools", "*********setVideoMute********isMute:" + isMute + " time:" + time);
        if (TvCommonManager.getInstance() != null) {
            TvCommonManager.getInstance().setVideoMute(isMute,
                    TvCommonManager.SCREEN_MUTE_BLACK, time,
                    TvCommonManager.INPUT_SOURCE_STORAGE);
        }
    }

    // Static class will capture large memory
    //private static TvCommonManager appSkin = null;
    //private static EnumInputSource inputSource = null;

    /*
     * Complete TV and storage of between source switching
     */
    public static void changeSource(final boolean isEnter) {
        if (unSupportTVApi()) {
            return;
        }
        Runnable localRunnable = new Runnable() {
            @Override
            public void run() {
                TvCommonManager appSkin = TvCommonManager.getInstance();
                int inputSource = 0;

                // boot will input from TV switch to AP
                if (appSkin != null) {
                    // switching source to storage
                    if (isEnter) {
                        inputSource = appSkin.getCurrentTvInputSource();
                        if (inputSource != TvCommonManager.INPUT_SOURCE_STORAGE) {
                            appSkin.setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                        }
                    } else {// switching source to the TV
                        if (inputSource != TvCommonManager.INPUT_SOURCE_STORAGE) {
                            appSkin.setInputSource(inputSource);
                        }
                    }
                }
            }
        };

        // Switching source more time-consuming, so start thread operation
        new Thread(localRunnable).start();

    }

    public static boolean isCurrentInputSourceStorage() {
        if (unSupportTVApi()) {
            return false;
        } else {
            TvCommonManager appSkin = TvCommonManager.getInstance();
            int inputSource = 0;

            if (appSkin != null) {
                inputSource = appSkin.getCurrentTvInputSource();
                Log.i(TAG, "isCurrentInputSourceStorage inputSource:" + inputSource);
                if (inputSource == TvCommonManager.INPUT_SOURCE_STORAGE) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean is3DTVPlugedIn() {
        boolean is3D = TvPictureManager.getInstance().is3DTVPlugedIn();
        Log.i(TAG, "is3DTVPlugedIn = "+is3D);
        return is3D;
    }

    /**
     * Select MfcLevel
     * <p> The supported MfcLevel are:
     * <ul>
     * <li> {@link #MFC_LEVEL_OFF}
     * <li> {@link #MFC_LEVEL_LOW}
     * <li> {@link #MFC_LEVEL_MID}
     * <li> {@link #MFC_LEVEL_HIGH}
     * <li> {@link #MFC_LEVEL_BYPASS}
     * </ul>
     *
     */
    /*
    public static void setMfcLevel(int mfcLevel){
        Log.i(TAG,"setMfcLevel: "+String.valueOf(mfcLevel));
        if (Tools.unSupportTVApi()) {
            return ;
        }
        TvPictureManager.getInstance().setMfcLevel(mfcLevel);
    }*/

    public static void setMfcLevel(int mfcLevel) {
        Log.i(TAG,"setMfcLevel: "+ String.valueOf(mfcLevel));
        if (unSupportTVApi()) {
            return ;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
             Method setMfcLevel = clz.getDeclaredMethod("setMfcLevel",new Class[]{int.class});
             setMfcLevel.invoke(mTvPictureManager,mfcLevel);
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    /**
     * Get Selected MfcLevel
     *
     * @return Selected MfcLevel
     * @see #MFC_LEVEL_OFF
     * @see #MFC_LEVEL_LOW
     * @see #MFC_LEVEL_MID
     * @see #MFC_LEVEL_HIGH
     * @see #MFC_LEVEL_BYPASS
     */
    /*
    public static int getMfcLevel() {
        if (Tools.unSupportTVApi()) {
            return 0;
        }
        int level = TvPictureManager.getInstance().getMfcLevel();
        Log.i(TAG,"getMfcLevel: "+String.valueOf(level));
        return level;
    }*/
    public static int getMfcLevel() {
        Log.i(TAG, "getMfcLevel");
        if (unSupportTVApi()) {
            return Constants.MFC_LEVEL_OFF;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
            Method getMfcLevel = clz.getDeclaredMethod("getMfcLevel");
            Object mfcLevel = getMfcLevel.invoke(mTvPictureManager);
            int level = Integer.parseInt(mfcLevel.toString());
            Log.i(TAG,"getMfcLevel: "+ String.valueOf(level));
            return level;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.MFC_LEVEL_OFF;
    }

    /**
     * TV2 new api
     *  Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
     * @param threeDVideoDisPlayFormat
     */
    public static void setDisplayFormat(int threeDVideoDisPlayFormat){
        TvS3DManager.getInstance().set3dDisplayFormat(threeDVideoDisPlayFormat);
    }

    //Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    public static int getDisplayFormat() {
        return TvS3DManager.getInstance().get3dDisplayFormat();
    }

    public static void set3DTo2D(int displayMode) {
        Log.d(TAG, "set3DTo2D(), paras displayMode = " + displayMode);
        TvS3DManager.getInstance().set3DTo2DDisplayMode(displayMode);
    }

    public static boolean hasSet3dFormat() {
         if (Tools.unSupportTVApi()) {
             return false;
         }
         if (getCurrent3dFormat() == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
             return false;
         } else {
             return true;
         }
    }

    /**
     * support new TV2 api  replace public static EnumThreeDVideoDisplayFormat getCurrent3dFormat()
     * @return
     */
    public static int getCurrent3dFormat() {
        int getCurrent3dType = TvS3DManager.getInstance().getCurrent3dType();
        switch (getCurrent3dType) {
            case TvS3DManager.THREE_DIMENSIONS_TYPE_NONE:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_SIDE_BY_SIDE_HALF:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_TOP_BOTTOM:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_1080P:
            case TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_LINE_ALTERNATIVE:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_2DTO3D:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_CHECK_BORAD:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_CHECK_BOARD;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_PIXEL_ALTERNATIVE:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_PIXEL_ALTERNATIVE;
            case TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_ALTERNATIVE:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_ALTERNATIVE;
            default:
                return TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE;

        }
    }

    /**
     * TV2 api
     * @return
     */
    public static int getCurrent3dFormatOnSTB2DTV() {
        return TvS3DManager.getInstance().get3DTo2DDisplayMode();
    }

    private static final String MSTAR_PRODUCT_CHARACTERISTICS = "mstar.product.characteristics";
    private static final String MSTAR_PRODUCT_STB = "stb";
    private static String mProduct = null;

    public static boolean isBox() {
        if (mProduct == null) {
            Class<?> systemProperties = null;
            Method method = null;
            try {
                systemProperties = Class.forName("android.os.SystemProperties");
                method = systemProperties.getMethod("get", String.class,
                        String.class);
                mProduct = (String) method.invoke(null,
                        MSTAR_PRODUCT_CHARACTERISTICS, "");
            } catch (Exception e) {
                return false;
            }
        }
        // Log.d("Tools", "mstar.product.characteristics is " + mProduct);
        if (MSTAR_PRODUCT_STB.equals(mProduct)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean showRootDir() {
        String value = SystemProperties.get("persist.mstar.localmm.rootdir", null);
        if (value != null && value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }
    public static String getHardwareName() {
        String name = MediaContainerApplication.getInstance().getHardwareName();
        if(name == null) {
            name = parseHardwareName();
            MediaContainerApplication.getInstance().setHardwareName(name);
        }
        return name;
    }

    private static String parseHardwareName() {
        String str = null;
        try {
             FileReader reader = new FileReader("/proc/cpuinfo");
             BufferedReader br = new BufferedReader(reader);
             while ((str = br.readLine()) != null) {
                 if (str.indexOf("Hardware") >= 0) {
                     break;
                 }
             }
             if (str != null) {
                 String s[] = str.split(":" , 2);
                 str = s[1].trim().toLowerCase();
             }
             br.close();
             reader.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return str;
    }

    private static String getConfigName(String file, String index) {
        String str = null;
        String gConfigName = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(index) >= 0) {
                    break;
                }
            }
            //Log.i(TAG,"str:"+str);
            if (str != null) {
                int begin = str.indexOf("\"") + 1;
                int end = str.lastIndexOf("\"");
                if ((begin > 0) && (end > 0)) {
                    gConfigName = str.substring(begin,end);
                    str = null;
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
            return null;
        }
        Log.i(TAG, "gConfigName:" + gConfigName + "; file: " + file + "; index: " + index);
        return gConfigName;
    }
    private static String getbEnabled(String file, String index) {
        String str = null;
        String bEnabled = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(index) >= 0 && str.indexOf(index) <9) {
                    break;
                }
            }
            Log.i(TAG,"str:"+str);
            if (str != null) {
                int begin = str.indexOf("=") + 1;
                int end = str.lastIndexOf(";");
                if ((begin > 0) && (end > 0)) {
                    bEnabled = str.substring(begin,end);
                    str = null;
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
            return null;
        }
        Log.i(TAG, "bEnabled:" + bEnabled + "; file: " + file + "; index: " + index);
        return bEnabled;
    }

    private static int[] parsePanelOsdSize(String index) {
        String str = null;
        String wPanelWidth = null;
        String wPanelHeight = null;
        int[] config = {0,0};
        String platform = getHardwareName();
        if ("muji".equals(platform) || "Manhattan".equals(platform)) {
            String strbEnabled = null;
            strbEnabled = getbEnabled("/config/sys.ini", "bEnabled");
            strbEnabled = strbEnabled.trim();
            if (strbEnabled!=null && strbEnabled.equals("TRUE")) {
                return  config;
            }
        }
        try {
            FileReader reader;
            String modelName = getConfigName(FILE_SYS_CONFIG, "gModelName");
            if(modelName == null || modelName.equals("")) {
                modelName = getConfigName(FILE_SYS_TVCONFIG, "gModelName");
            }
            reader = new FileReader(getConfigName(modelName, "m_pPanelName"));
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(index + "Width") >= 0) {
                    Log.i(TAG, index + "Width:" + str);
                    int begin = str.indexOf("=") + 1;
                    int end = str.lastIndexOf(";");
                    if ((begin > 0) && (end > 0)) {
                        wPanelWidth = str.substring(begin, end).trim();
                        Log.i(TAG, index +"Width:" + wPanelWidth);
                        config[0] = Integer.parseInt(wPanelWidth);
                    }
                } else if (str.indexOf(index + "Height") >= 0) {
                    Log.i(TAG, index + "Height:" + str);
                    int begin = str.indexOf("=") + 1;
                    int end = str.lastIndexOf(";");
                    if ((begin > 0) && (end > 0)) {
                        wPanelHeight = str.substring(begin, end).trim();
                        Log.i(TAG, index + "Height:" + wPanelHeight);
                        config[1] = Integer.parseInt(wPanelHeight);
                    }
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return config;
        }
        return config;
    }

    public static int[] getPanelSize() {
        int[] config = MediaContainerApplication.getInstance().getPanelSize();
        if(config[0] == 0 || config[1] == 0) {
            config = parsePanelOsdSize("m_wPanel");
            MediaContainerApplication.getInstance().setPanelSize(config);
        }
        else
            Log.i(TAG,"config!=0");
        return config;
    }

    public static int[] getOsdSize() {
        int[] config = MediaContainerApplication.getInstance().getOsdSize();
        if(config[0] == 0 || config[1] == 0) {
            config = parsePanelOsdSize("osd");
            MediaContainerApplication.getInstance().setOsdSize(config);
        }
        return config;
    }

    private static long parseMem(String index) {
        String str = null;
        long mTotal = 0;
        try {
             FileReader reader = new FileReader("/proc/meminfo");
             BufferedReader br = new BufferedReader(reader);
             while ((str = br.readLine()) != null) {
                 if (str.indexOf(index) >= 0) {
                     break;
                 }
             }
             if (str != null) {
                 int begin = str.indexOf(':');
                 int end = str.indexOf('k');
                 str = str.substring(begin + 1, end).trim();
                 mTotal = Integer.parseInt(str);
             }
             br.close();
             reader.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
        Log.i(TAG, "parse " + index + " : " + mTotal + " kB");
        return mTotal;
    }

    public static long getTotalMem() {
        long total = MediaContainerApplication.getInstance().getTotalMem();
        if(total == -1) {
            total = parseMem("MemTotal");
            MediaContainerApplication.getInstance().setTotalMem(total);
        }
        return total;
    }

    public static long getFreeMem() {
        return parseMem("MemFree");
    }
    public static String getFileName(String sPath) {
        int pos = sPath.lastIndexOf("/");
        String name = sPath.substring(pos+1,sPath.length());
        return name;
    }

    public static String parseUri(Uri intent) {
        if (intent != null) {
            return intent.getPath();
        }
        return null;
    }

    public static boolean isFloatVideoPlayModeOn() {
        int status = SystemProperties.getInt("mstar.floatvideoplaymode", 0);
        if (1 == status) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isThumbnailModeOn() {
        if (!isSupportDualDecode()) {
            return false;
        }
        int status = SystemProperties.getInt("mstar.thumbnailmode", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isThumbnailMode_SW_On() {
        int status = SystemProperties.getInt("mstar.thumbnail.swmode", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static int getThumbnailNumber() {
        return SystemProperties.getInt("mstar.thumbnail.number", 5);
    }

    public static void setThumbnailMode(String value) {
        if (!isSupportDualDecode()) {
            return;
        }
        SystemProperties.set("mstar.thumbnailmode", value);
    }

    public static boolean showThumbnailTimeStamp() {
        int status = SystemProperties.getInt("mstar.thumbnail.showtime", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean thumbnailSeekBarEnable() {
        int status = SystemProperties.getInt("mstar.thumbnail.seekbar.enable", 1);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void setRotateMode(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.video.rotate", "true");
        } else {
            SystemProperties.set("mstar.video.rotate", "false");
        }

    }
    public static boolean isRotateModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.video.rotate", false);
        Log.i(TAG,"isRotateModeOn:"+status);
        return  status;
    }

    public static boolean isRotateDisplayAspectRatioModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.video.rotate.aspectratio", false);
        return status;
    }

    public static void setRotateDegrees(String value) {
        SystemProperties.set("mstar.video.rotate.degrees", value);
    }

    public static int getRotateDegrees() {
        return SystemProperties.getInt("mstar.video.rotate.degrees", 0);
    }

    public static boolean isVideoSWDisplayModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.video.sw.display", true);
        Log.i(TAG,"isVideoSWDisplayModeOn:"+status);
        return  status;
    }

    public static boolean isRotate90OR270Degree() {
        if (!isRotateModeOn()) {
            return false;
        }
        if ("90".equalsIgnoreCase(String.valueOf(getRotateDegrees()))
            || "270".equalsIgnoreCase(String.valueOf(getRotateDegrees()))) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isSupportDualDecode() {
        boolean ret = isSupportModule(Constants.MODULE_PIP);
        Log.i(TAG,"isSupportDualDecode : "+ ret);

        return ret;
   }

   public static boolean isSupportNativeAudio() {
        boolean ret = false;
        if (isNativeAudioModeOn()) {
            return true;
        }
        if (getHasCheckNativeAudioModeSupportOrNot()) {
            return false;
        }
        ret = isSupportModule(Constants.MODULE_TV_CONFIG_PIP_SINGLE_AUDIO_OUPUT_ENABLE);
        Log.i(TAG,"isSupportNativeAudio : "+ ret);
        if (ret) {
            setNativeAudioModeOn(true);
        }
        setHasCheckNativeAudioModeSupportOrNot(true);
        return ret;
   }

   public static boolean isSupportUseHttpSamba() {
        boolean ret = false;
        if (isUseHttpSambaModeOn()) {
            return true;
        }
        if (getHasCheckUseHttpSambaModeSupportOrNot()) {
            return false;
        }
        ret = isSupportModule(Constants.MODULE_TV_CONFIG_REFINE_SAMBA_SCAN_BROWSE_PLAY_ENABLE);
        Log.i(TAG,"isSupportUseHttpSamba : "+ ret);
        if (ret) {
            setUseHttpSambaModeOn(true);
        }
        setHasCheckUseHttpSambaModeSupportOrNot(true);
        return ret;
   }

   public static void setSWDetileModeOn(boolean flag) {
       Log.i(TAG,"setSWDetileModeOn flag:"+flag);
       if (flag) {
           SystemProperties.set("ms.vsync_bridge.swdetile", "1");
       } else {
           SystemProperties.set("ms.vsync_bridge.swdetile", "0");
       }
   }

   public static boolean isSWDetileModeOn() {
       int ret = SystemProperties.getInt("ms.vsync_bridge.swdetile", 0);
       if (ret == 1) {
           return true;
       } else {
           return false;
       }
   }

   public static void setMWPlayBackOn(boolean flag) {
       Log.i(TAG,"setMWPlayBackOn flag:"+flag);
       if (flag) {
           SystemProperties.set("mstar.mwplaybackon", "1");
       } else {
           SystemProperties.set("mstar.mwplaybackon", "0");
       }
   }

   public static boolean isMWPlayBackOn() {
       int ret = SystemProperties.getInt("mstar.mwplaybackon", 0);
       if (ret == 1) {
           return true;
       } else {
           return false;
       }
   }

   public static boolean isSupportMWPlayBack() {
        boolean ret = false;
        ret = isSupportModule(Constants.MODULE_TV_CONFIG_MM_MULTIPLAYBACK_ENABLE);
        Log.i(TAG,"isSupportMWPlayBack : "+ ret);
        return ret;
   }

    /**
     * Check if module is supported
     * If module catagory is compiler flag, check both SN config and AN config
     * (return true if SN and AN config both support);
     * otherwise, check AN config
     * <p> The supported modules are:
     * <ul>
     * <li> {@link #MODULE_PIP}
     * <li> {@link #MODULE_TRAVELING}
     * <li> {@link #MODULE_OFFLINE_DETECT}
     * <li> {@link #MODULE_PREVIEW_MODE}
     * <li> {@link #MODULE_FREEVIEW_AU}
     * <li> {@link #MODULE_CC}
     * <li> {@link #MODULE_BRAZIL_CC}
     * <li> {@link #MODULE_KOREAN_CC}
     * <li> {@link #MODULE_HDMITX}
     * <li> {@link #MODULE_HBBTV}
     * <li> {@link #MODULE_INPUT_SOURCE_LOCK}
     * <li> {@link #MODULE_EPG}
     * <li> {@link #MODULE_AD_SWITCH}
     * <li> {@link #MODULE_VCHIP}
     * <li> {@link #MODULE_OAD}
     * <li> {@link #MODULE_EDID_AUTO_SWITCH}
     * <li> {@link #MODULE_OPEN_HDR}
     * <li> {@link #MODULE_DOLBY_HDR}
     * <li> {@link #MODULE_ATV_NTSC_ENABLE}
     * <li> {@link #MODULE_ATV_PAL_ENABLE}
     * <li> {@link #MODULE_ATV_CHINA_ENABLE}
     * <li> {@link #MODULE_ATV_PAL_M_ENABLE}
     * <li> {@link #MODULE_TV_CONFIG_ATV_MANUAL_TUNING}
     * <li> {@link #MODULE_TV_CONFIG_AUTO_HOH}
     * <li> {@link #MODULE_TV_CONFIG_AUDIO_DESCRIPTION}
     * <li> {@link #MODULE_TV_CONFIG_THREED_DEPTH}
     * <li> {@link #MODULE_TV_CONFIG_SELF_DETECT}
     * <li> {@link #MODULE_TV_CONFIG_THREED_CONVERSION_TWODTOTHREED}
     * <li> {@link #MODULE_TV_CONFIG_THREED_CONVERSION_AUTO}
     * <li> {@link #MODULE_TV_CONFIG_THREED_CONVERSION_PIXEL_ALTERNATIVE}
     * <li> {@link #MODULE_TV_CONFIG_THREED_CONVERSION_FRAME_ALTERNATIVE}
     * <li> {@link #MODULE_TV_CONFIG_THREED_CONVERSION_CHECK_BOARD}
     * <li> {@link #MODULE_TV_CONFIG_THREED_TWOD_AUTO}
     * <li> {@link #MODULE_TV_CONFIG_THREED_TWOD_PIXEL_ALTERNATIVE}
     * <li> {@link #MODULE_TV_CONFIG_THREED_TWOD_FRAME_ALTERNATIVE}
     * <li> {@link #MODULE_TV_CONFIG_THREED_TWOD_CHECK_BOARD}
     * <li> {@link #MODULE_TV_CONFIG_MM_MULTIPLAYBACK_ENABLE}
     * </ul>
     *
     * @param module queried module
     * @see #MODULE_PIP
     * @see #MODULE_TRAVELING
     * @see #MODULE_OFFLINE_DETECT
     * @see #MODULE_PREVIEW_MODE
     * @see #MODULE_FREEVIEW_AU
     * @see #MODULE_CC
     * @see #MODULE_BRAZIL_CC
     * @see #MODULE_KOREAN_CC
     * @see #MODULE_HDMITX
     * @see #MODULE_HBBTV
     * @see #MODULE_INPUT_SOURCE_LOCK
     * @see #MODULE_EPG
     * @see #MODULE_AD_SWITCH
     * @see #MODULE_VCHIP
     * @see #MODULE_OAD
     * @see #MODULE_EDID_AUTO_SWITCH
     * @see #MODULE_OPEN_HDR
     * @see #MODULE_DOLBY_HDR
     * @see #MODULE_ATV_NTSC_ENABLE
     * @see #MODULE_ATV_PAL_ENABLE
     * @see #MODULE_ATV_CHINA_ENABLE
     * @see #MODULE_ATV_PAL_M_ENABLE
     * @see #MODULE_TV_CONFIG_ATV_MANUAL_TUNING
     * @see #MODULE_TV_CONFIG_AUTO_HOH
     * @see #MODULE_TV_CONFIG_AUDIO_DESCRIPTION
     * @see #MODULE_TV_CONFIG_THREED_DEPTH
     * @see #MODULE_TV_CONFIG_SELF_DETECT
     * @see #MODULE_TV_CONFIG_THREED_CONVERSION_TWODTOTHREED
     * @see #MODULE_TV_CONFIG_THREED_CONVERSION_AUTO
     * @see #MODULE_TV_CONFIG_THREED_CONVERSION_PIXEL_ALTERNATIVE
     * @see #MODULE_TV_CONFIG_THREED_CONVERSION_FRAME_ALTERNATIVE
     * @see #MODULE_TV_CONFIG_THREED_CONVERSION_CHECK_BOARD
     * @see #MODULE_TV_CONFIG_THREED_TWOD_AUTO
     * @see #MODULE_TV_CONFIG_THREED_TWOD_PIXEL_ALTERNATIVE
     * @see #MODULE_TV_CONFIG_THREED_TWOD_FRAME_ALTERNATIVE
     * @see #MODULE_TV_CONFIG_THREED_TWOD_CHECK_BOARD
     * @see #MODULE_TV_CONFIG_MM_MULTIPLAYBACK_ENABLE
     * @return boolean true: supported, false: unsupported
     */
    public static boolean isSupportModule(int module) {
        String name = "";
        boolean ret = false;
        try {
            if (mModuleMapTable.containsKey(module)) {
                name = mModuleMapTable.get(module);
                Log.i(TAG,"check name: "+name+" isSupportModule");
                if (module < Constants.MODULE_NOT_COMPILE_FLAG_START) {
                    boolean configSetting = (getAndroidConfigSetting(name) <= 0 ? false : true);
                    if (getCompilerFlag(name) && configSetting) {
                        ret = true;
                    } else {
                        ret = false;
                    }
                    Log.i(TAG, "isSupportModule: " + name + " = " + ret);
                    return ret;
                } else {
                    ret = (getAndroidConfigSetting(name) <= 0 ? false : true);
                    Log.i(TAG, "isSupportModule: " + name + " = " + ret);
                    return ret;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean parseCompilerFlag(String file, String configName) {
        String str = null;
        boolean compilerFlagEnable = false;
        int compilerFlag = 0;
        File testFile = new File(file);
        if (!testFile.exists()) {
            return false;
        }
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(configName) >= 0) {
                    break;
                }
            }
            //Log.i(TAG,"str:"+str);
            if (str != null) {
                // e.g. PIP_ENABLE=1
                int begin = str.indexOf(configName) + configName.length()+1;
                int end = str.indexOf(configName) + configName.length()+2;
                if ((begin > 0) && (end > 0)) {
                    compilerFlag = Integer.parseInt(str.substring(begin,end));
                    str = null;
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
            return false;
        }
        if (compilerFlag>0) {
            compilerFlagEnable = true;
        }
        Log.i(TAG, "compilerFlagEnable:" + compilerFlagEnable);
        return compilerFlagEnable;
    }
    public static boolean getCompilerFlag(String configName) {
        return parseCompilerFlag("/config/Flags",configName);
    }

    public static int getAndroidConfigSetting(String configName) {
        if (mAndroidConfigList != null) {
            for (Pair<String, Integer> p : mAndroidConfigList) {
                if (p.first.equals(configName)) {
                    Log.d(TAG, "getAndroidConfigSetting return = " + p.second);
                    return p.second;
                }
            }
        }
        return -1;
    }

    public static void initToolsModuleConfig() {
        // Fixme:Android O SDK_INT = 26, the directory of feature file is changed in An O
        if (Build.VERSION.SDK_INT >= 26) {
            FILE_FEATURE_XML = "/vendor/etc/feature.xml";
            FILE_FEATURE_DEFAULT_XML = "/vendor/etc/feature_default.xml";
        }
        mAndroidConfigList = getAndroidConfigList();
        BuildModuleMapTable();
    }

    // in order to add a flag for a feature on new platform. u should do three points.
    // first of all,  add value(string type) in feature.xml or feature_default.xml
    // secondly,   add key (constant integers type)in Constant.java
    // at last,      new one hashTable object ((key,value)in one,two))in mModuleMapTable
    public static void BuildModuleMapTable() {
        if (mModuleMapTable == null) return;
        /* SN compiler flag catagory */
        mModuleMapTable.put(Constants.MODULE_PIP, "PIP_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TRAVELING, "TRAVELING_ENABLE");
        mModuleMapTable.put(Constants.MODULE_OFFLINE_DETECT, "OFL_DET");
        mModuleMapTable.put(Constants.MODULE_PREVIEW_MODE, "PREVIEW_MODE_ENABLE");
        mModuleMapTable.put(Constants.MODULE_FREEVIEW_AU, "FREEVIEW_AU_ENABLE");
        mModuleMapTable.put(Constants.MODULE_CC, "CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_BRAZIL_CC, "BRAZIL_CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_KOREAN_CC, "KOREAN_CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ATSC_CC_ENABLE, "ATSC_CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ISDB_CC_ENABLE, "ISDB_CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_NTSC_CC_ENABLE, "NTSC_CC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ATV_NTSC_ENABLE, "ATV_NTSC_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ATV_PAL_ENABLE, "ATV_PAL_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ATV_CHINA_ENABLE, "ATV_CHINA_ENABLE");
        mModuleMapTable.put(Constants.MODULE_ATV_PAL_M_ENABLE, "ATV_PAL_M_ENABLE");
        mModuleMapTable.put(Constants.MODULE_HDMITX, "HDMITX_ENABLE");
        mModuleMapTable.put(Constants.MODULE_HBBTV, "HBBTV_ENABLE");
        mModuleMapTable.put(Constants.MODULE_INPUT_SOURCE_LOCK, "INPUT_SOURCE_LOCK_ENABLE");
        mModuleMapTable.put(Constants.MODULE_EPG, "EPG_ENABLE");
        mModuleMapTable.put(Constants.MODULE_AD_SWITCH, "AD_SWITCH_ENABLE");
        mModuleMapTable.put(Constants.MODULE_VCHIP, "VCHIP_ENABLE");
        mModuleMapTable.put(Constants.MODULE_OAD, "OAD_ENABLE");
        mModuleMapTable.put(Constants.MODULE_EDID_AUTO_SWITCH, "EDID_AUTO_SWITCH_ENABLE");
        mModuleMapTable.put(Constants.MODULE_OPEN_HDR, "OPEN_HDR_SUPPORT");
        mModuleMapTable.put(Constants.MODULE_DOLBY_HDR, "DOLBY_HDR_SUPPORT");

        /* AN capability catagory */
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_ATV_MANUAL_TUNING, "ATV_MANUAL_TUNING_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_AUTO_HOH, "AUTO_HOH_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_AUDIO_DESCRIPTION, "AUDIO_DESCRIPTION_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_DEPTH, "THREED_DEPTH_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_SELF_DETECT, "SELF_DETECT_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_CONVERSION_TWODTOTHREED, "THREED_CONVERSION_TWODTOTHREED");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_CONVERSION_AUTO, "THREED_CONVERSION_AUTO");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_CONVERSION_PIXEL_ALTERNATIVE,
                "THREED_CONVERSION_PIXEL_ALTERNATIVE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_CONVERSION_FRAME_ALTERNATIVE,
                "THREED_CONVERSION_FRAME_ALTERNATIVE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_CONVERSION_CHECK_BOARD, "THREED_CONVERSION_CHECK_BOARD");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_TWOD_AUTO, "THREED_TWOD_AUTO");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_TWOD_PIXEL_ALTERNATIVE, "THREED_TWOD_PIXEL_ALTERNATIVE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_TWOD_FRAME_ALTERNATIVE, "THREED_TWOD_FRAME_ALTERNATIVE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_THREED_TWOD_CHECK_BOARD, "THREED_TWOD_CHECK_BOARD");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_MM_MULTIPLAYBACK_ENABLE, "MM_MULTIPLAYBACK_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_PIP_SINGLE_AUDIO_OUPUT_ENABLE, "PIP_SINGLE_AUDIO_OUPUT_ENABLE");
        mModuleMapTable.put(Constants.MODULE_TV_CONFIG_REFINE_SAMBA_SCAN_BROWSE_PLAY_ENABLE, "REFINE_SAMBA_SCAN_BROWSE_PLAY_ENABLE");
    }

    public static boolean isMstarSTB() {
        String value = SystemProperties.get("mstar.build.for.stb", null);
        if (value != null && value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public static boolean unSupportTVApi() {
        String property = SystemProperties.get("mstar.build.mstartv", null);
        if (property != null) {
            if (property.equalsIgnoreCase("ddi")) {
                return true;
            } else if (property.equalsIgnoreCase("mi")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMstarTV_MI() {
        String property = SystemProperties.get("mstar.build.mstartv", null);
        if (property == null || !property.equalsIgnoreCase("mi")) {
            Log.i(TAG, "-------- unSupportTVApi false");
            return false;
        }
        Log.i(TAG, "-------- unSupportTVApi true");
        return true;
    }

   private static int curLangID = 1;
   public static void setCurrLangID(int nID) {
        curLangID = nID;
   }

   public static int getCurrLangID() {
        return curLangID;
   }

    public static void setSystemProperty(String key , String value) {
        try {
            Class clz = Class.forName("android.os.SystemProperties");
            Method set = clz.getDeclaredMethod("set", String.class, String.class);
            set.invoke(clz, key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSystemProperty(String key , String defValue) {
        try {
            Class clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getDeclaredMethod("get", String.class, String.class);
            return String.valueOf(get.invoke(clz,key,defValue));
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }
    public static void setDolbyHDR(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.dolbyhdr", "1");
        } else {
            SystemProperties.set("mstar.dolbyhdr", "0");
        }
    }
    public static boolean getDolbyHDR() {
        int dolbyhdrtatus = SystemProperties.getInt("mstar.dolbyhdr", 0);
        if (dolbyhdrtatus == 1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Get HDR attributes
     *
     * @param attribute
     * <p> The supported value are:
     * <ul>
     * <li> {@link #HDR_OPEN_ATTRIBUTES}
     * <li> {@link #HDR_DOLBY_ATTRIBUTES}
     * </ul>
     * @param window
     * <p> The supported value are:
     * <ul>
     * <li> {@link #VIDEO_MAIN_WINDOW}
     * <li> {@link #VIDEO_SUB_WINDOW}
     * </ul>
     * @return HdrAttribute
     * @throws TvCommonException
     */

    public static int[] getHdrAttributes(int attribute, int window) {
        Log.i(TAG, "getHdrAttributes");
        mHdrAttributes[0] = Constants.ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED;
        mHdrAttributes[1] = Constants.HDR_NOT_IS_RUNNING;
        if (unSupportTVApi()) {
            return mHdrAttributes;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
            Method getHdrAttributes = clz.getDeclaredMethod("getHdrAttributes",
                                           new Class[]{int.class,int.class});
            Object hdr = getHdrAttributes.invoke(mTvPictureManager,attribute,window);
            Class hdrClass = hdr.getClass();
            Field fieldResult = hdrClass.getDeclaredField("result");
            Field fieldLevel = hdrClass.getDeclaredField("level");
            Field fieldIsRunning = hdrClass.getDeclaredField("isRunning");
            int result = Integer.parseInt(fieldResult.get(hdr).toString());
            int level = Integer.parseInt(fieldLevel.get(hdr).toString());
            boolean isRunning = Boolean.parseBoolean(fieldIsRunning.get(hdr).toString());
            Log.i(TAG,"result level:"+ String.valueOf(result)+" "+ String.valueOf(level));
            Log.i(TAG,"isRunning:"+isRunning);
            if (result == Constants.HDR_ERROR_CODE_NOT_SUPPORT ||
                result == Constants.HDR_ERROR_CODE_UNDEFINED_FAIL) {
                return mHdrAttributes;
            } else {
                mHdrAttributes[0] = level;
                if (isRunning) {
                    mHdrAttributes[1] = Constants.HDR_IS_RUNNING;
                }
                return mHdrAttributes;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHdrAttributes;

    }

    /**
     * Set HDR attributes
     *
     * @param attribute
     * <p> The supported value are:
     * <ul>
     * <li> {@link #HDR_OPEN_ATTRIBUTES}
     * <li> {@link #HDR_DOLBY_ATTRIBUTES}
     * </ul>
     * @param window
     * <p> The supported value are:
     * <ul>
     * <li> {@link #VIDEO_MAIN_WINDOW}
     * <li> {@link #VIDEO_SUB_WINDOW}
     * </ul>
     * @param level
     * <p> When attribute is set as HDR_OPEN_ATTRIBUTES, the supported value are:
     * <ul>
     * <li> {@link #HDR_OPEN_LEVEL_OFF}
     * <li> {@link #HDR_OPEN_LEVEL_AUTO}
     * <li> {@link #HDR_OPEN_LEVEL_LOW}
     * <li> {@link #HDR_OPEN_LEVEL_MIDDLE}
     * <li> {@link #HDR_OPEN_LEVEL_HIGH}
     * <li> {@link #HDR_OPEN_LEVEL_REF_MODE}
     * </ul>
     * <p> When attribute is set as HDR_DOLBY_ATTRIBUTES, the supported value are:
     * <ul>
     * <li> {@link #HDR_DOLBY_LEVEL_OFF}
     * <li> {@link #HDR_DOLBY_LEVEL_VIVID}
     * </ul>
     * @return boolean TRUE:Success, or FALSE:failed.
     * @throws TvCommonException
     */

    public static boolean setHdrAttributes(int attribute, int window, int level) {
        Log.i(TAG, "setHdrAttributes level:"+level);
        boolean result = false;
        if (unSupportTVApi()) {
            return result;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
             Method setHdrAttributes = clz.getDeclaredMethod("setHdrAttributes",
                                           new Class[]{int.class,int.class,int.class});
             Object object = setHdrAttributes.invoke(mTvPictureManager,attribute,window,level);
             result = Boolean.parseBoolean(object.toString());
             return result;
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
             return  result;
        }
    }

    public static boolean isCurrPlatformSupportHdrOrDolbyHdr() {
        String platform = getHardwareName();
        if ("maserati".equals(platform)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isNetPlayback(String path){
        String ret = null;
        if (path != null && path.length()!=0) {
            ret = path.substring(0,4);
        }
        if (ret != null && ret.length()!=0) {
            if (ret.equals("http") || ret.equals("rtsp")) {
                return true;
            }
        }
        return false;
    }
    public static void setSubtitleAPI(boolean flag) {
        if (flag) {
            // mstar subtitle api
            SystemProperties.set("mstar.subtitleapi", "1");
        } else {
            // android native api
            SystemProperties.set("mstar.subtitleapi", "0");
        }
    }

    public static boolean getSubtitleAPI() {
        int subtitleAPI = SystemProperties.getInt("mstar.subtitleapi", 1);
        if (subtitleAPI == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void setMainPlay4K2KModeOn(boolean flag) {
         if (flag) {
             SystemProperties.set("mstar.mainplay4k2k", "1");
         } else {
             SystemProperties.set("mstar.mainplay4k2k", "0");
         }
    }
    public static boolean isMainPlay4K2KModeOn() {
         int ret = SystemProperties.getInt("mstar.mainplay4k2k", 0);
         if (ret == 1) {
             return true;
         } else {
             return false;
         }
    }

    public static void setVideoStreamlessModeOn(boolean streamlessOn) {
        if (streamlessOn) {
            SystemProperties.set("mstar.video.seamless.playback", "1");
        } else {
            SystemProperties.set("mstar.video.seamless.playback", "0");
        }
    }

    public static boolean isVideoStreamlessModeOn() {
        int seamstatus = SystemProperties.getInt("mstar.video.seamless.playback", 0);
        // maxim is asked to close video seamless playback by weiping.liu and gemi.Tsai
        // mooney is asked to close video seamless playback by ocean.zhao
        String platform = getHardwareName();
        if (seamstatus == 1
            && (platform!=null && !platform.equals("maxim"))
            && (platform!=null && !platform.equals("mooney")) ) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPhotoStreamlessModeOn() {
        int seamstatus = SystemProperties.getInt("mstar.photo.seamless.playback", 1);
        if (seamstatus == 1) {
            return true;
        } else {
            return false;
        }
    }

    // Set this function when has checked(call isSupportModule(Constants.MODULE_PIP))
    // this platform is support dual decode or not
    public static void setHasCheckDualDecodeModeSupportOrNot(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.dualdecodechecked", "1");
        } else {
            SystemProperties.set("mstar.dualdecodechecked", "0");
        }
    }

    public static boolean getHasCheckDualDecodeModeSupportOrNot() {
        int ret = SystemProperties.getInt("mstar.dualdecodechecked", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void setDualDecodeModeOn(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.dualdecode", "1");
        } else {
            SystemProperties.set("mstar.dualdecode", "0");
        }
    }

    public static boolean isDualDecodeModeOn() {
        int ret = SystemProperties.getInt("mstar.dualdecode", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    // See "isSupportDualDecode()"
    public static void setHasCheckNativeAudioModeSupportOrNot(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.nativeaudiochecked", "1");
        } else {
            SystemProperties.set("mstar.nativeaudiochecked", "0");
        }
    }

    public static boolean getHasCheckNativeAudioModeSupportOrNot() {
        int ret = SystemProperties.getInt("mstar.nativeaudiochecked", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void setNativeAudioModeOn(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.nativeaudiomodeon", "1");
        } else {
            SystemProperties.set("mstar.nativeaudiomodeon", "0");
        }
    }

    public static boolean isNativeAudioModeOn() {
        int ret = SystemProperties.getInt("mstar.nativeaudiomodeon", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElderPlatformForStreamLessMode(){
        boolean ret = false;
        String hardwareName = getHardwareName();
        if (hardwareName.equals("edison") || hardwareName.equals("kaiser")) {
            ret = true;
        }
        return ret;
    }

    // See "isSupportDualDecode()"
    public static void setHasCheckUseHttpSambaModeSupportOrNot(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.httpsambachecked", "1");
        } else {
            SystemProperties.set("mstar.httpsambachecked", "0");
        }
    }

    public static boolean getHasCheckUseHttpSambaModeSupportOrNot() {
        int ret = SystemProperties.getInt("mstar.httpsambachecked", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void setUseHttpSambaModeOn(boolean flag) {
        if (flag) {
            SystemProperties.set("mstar.usehttpsamba", "1");
        } else {
            SystemProperties.set("mstar.usehttpsamba", "0");
        }
    }

    // there are two ways to playback samba's resource
    // one way uses mount/unmount (in sdk which less than 23)
    // another way uses http server to send data to player who request data.(in sdk which is 23 or more than 23)
    public static boolean isUseHttpSambaModeOn() {
        int ret = SystemProperties.getInt("mstar.usehttpsamba", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSambaPlaybackUrl(String path){
        String ret = null;
        if (path != null && path.length()!=0) {
            ret = path.substring(0,3);
        }
        if (ret != null && ret.length()!=0) {
            if (ret.equals("smb")) {
                return true;
            }
        }
        return false;
    }

    public static String convertToHttpUrl(String path) {
        Log.i(TAG,"HttpBean.setmSmbUrl path:"+path);
        HttpBean.setmSmbUrl(path);
        Log.i(TAG,"before convertSambaToHttpUrl's path:"+path);
        String sambaPath = HttpBean.convertSambaToHttpUrl(path);
        Log.i(TAG,"convertSambaToHttpUrl's result sambaPath:"+sambaPath);
        sambaPath = sambaPath.substring(22);
        Log.i(TAG,"sambaPath.substring(22)'s result:"+sambaPath);
        sambaPath = Uri.encode(sambaPath);
        Log.i(TAG,"Uri.encode(sambaPath)'s result:"+sambaPath);
        sambaPath = "http://127.0.0.1:8088/"+sambaPath;
        Log.i(TAG,"passed to mediaplayer's sambaPath:"+sambaPath);
        return sambaPath;
    }

    public static boolean isSambaVideoPlayBack(){
        int ret = SystemProperties.getInt("mstar.samba.video.playback", 0);
        Log.i(TAG,"isSambaVideoPlayBack:"+(ret == 1));
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void setSambaVideoPlayBack(boolean flag){
        Log.i(TAG,"setSambaVideoPlayBack:"+flag);
        if (flag) {
            SystemProperties.set("mstar.samba.video.playback", "1");
        } else {
            SystemProperties.set("mstar.samba.video.playback", "0");
        }
    }

    public static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            if (!f1.exists()) {
                return;
            }
            File f2 = new File(dtFile);
            if (!f2.exists()){
                f2.createNewFile();
            }
            FileInputStream in = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch(FileNotFoundException ex) {
            Log.d(TAG,"the file cannot be found");
        } catch(IOException e){
            Log.i(TAG,"---IOException---");
        }
    }

    public static boolean getResumePlayState(Uri uri) {
        if (SystemProperties.getInt("mstar.bootinfo", 1) == 0) {
            if (SystemProperties.getInt("mstar.backstat", 0) == 1) {
                String lPath = SystemProperties.get("mstar.path", "");
                String FN = getFileName(uri.getPath());
                Log.i("andrew", "the file name is:" + FN);
                if (lPath.equals(FN)) {
                    return true;
                } else {
                    SystemProperties.set("mstar.path", FN);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            SystemProperties.set("mstar.bootinfo", "0");
            return false;
        }
    }

    public static void setResumePlayState(int state) {
        String sState = state + "";
        SystemProperties.set("mstar.backstat", sState);
    }

    public static void setVideoWindowVisable(boolean isMainWindow, boolean visable) {
        Log.d(TAG, "setVideoWindowVisable  isMainWindow: "+isMainWindow +"  visible :" +visable);
        if (unSupportTVApi()) {
            return;
        }
        if (isMainWindow) {
            TvPictureManager.getInstance().selectWindow(TvPictureManager.VIDEO_MAIN_WINDOW);
        } else {
            TvPictureManager.getInstance().selectWindow(TvPictureManager.VIDEO_SUB_WINDOW);
        }
        if (visable) {
            TvPictureManager.getInstance().setWindowVisible();
        } else {
           TvPictureManager.getInstance().setWindowInvisible();
        }
    }

    public static void setHpBtMainSource(boolean isMainSource) {
        // change earphone and bluetooth to main source or sub source
        if (!unSupportTVApi()) {
            Log.v(TAG, "setHpBtMainSource isMainSource:"+isMainSource);
            if(isMainSource) {
                TvAudioManager.getInstance().setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1
                        , TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                //TODO ,only after merge can open,
                TvAudioManager.getInstance().setOutputSourceInfo(
                        TvAudioManager.AUDIO_VOL_SOURCE_HP_OUT,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
            } else {
                TvAudioManager.getInstance().setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1
                        , TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                //TODO ,only after merge can open,
                TvAudioManager.getInstance().setOutputSourceInfo(
                        TvAudioManager.AUDIO_VOL_SOURCE_HP_OUT,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
            }
        }
    }

    public static String fixPath(String path) {
        // lollipop5.1 corrsponding to 22, don't need to handle the sign of '#' and '%' after lollipop5.1
        // mantis:0893065,0870320
        if(Build.VERSION.SDK_INT < 22) {
            if (path.indexOf("%") != -1) {
                path = path.replaceAll("%", "%25");
            }
            if (path.indexOf("#") != -1) {
                path = path.replaceAll("#", "%23");
            }
        }
        return path;
    }

    public static int getSdkVersion(){
        int ret = Build.VERSION.SDK_INT;
        return ret;
    }

    public static List<Pair<String, Integer>> getAndroidConfigList() {
        XmlParser parser = XmlParser.getInstance();
        List<Pair<String, Integer>> feature_default;
        List<Pair<String, Integer>> feature;
        if (parser.parseXml(FILE_FEATURE_XML)) {
            Log.d(TAG, "getAndroidConfigList: parse feature xml successfully");
        } else {
            Log.d(TAG, "getAndroidConfigList: parse feature xml failed");
        }
        feature = parser.getList();
        File file = new File(FILE_FEATURE_DEFAULT_XML);
        if (file.exists()) {
            if (parser.parseXml(FILE_FEATURE_DEFAULT_XML)) {
                Log.d(TAG, "getAndroidConfigList: parse feature_default xml successfully");
            } else {
                Log.d(TAG, "getAndroidConfigList: parse feature_default xml failed");
            }
            feature_default = parser.getList();
            for (Pair<String, Integer> d : feature_default) {
                boolean found = false;
                for (Pair<String, Integer> f : feature) {
                    if (d.first.equals(f.first)) {
                        found = true;
                        break;
                    }
                }
                if (false == found) {
                    feature.add(d);
                }
            }
        }

        //return parser.getList();
        return feature;
    }

    // print the utf-8 encoding hex string
    public static String str2HexStr(String str)
    {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    // print the byte array(every encoding) to hex string
    public  static String byte2hex(byte [] buffer){
        String h = "";
        for(int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }
        return h;
    }

    // divx subtitle request
    public static byte[] cutForThreeRowSubtitle(byte [] buffer){
        int cnt = 0;
        Log.i(TAG,"cutForThreeRowSubtitle");
        byte result[] = new byte[buffer.length];
        for(int i = 0; i < buffer.length; i++) {
            Log.i(TAG,"buffer[i]:"+i+","+buffer[i]);
            result[i] = buffer[i];
            if (buffer[i] == 0x0A) {
                cnt++;
                if (cnt == 3) break;
            }
        }
        return result;
    }

    public static boolean isSubSupportFullScreenIn4K2K(){
        int ret = SystemProperties.getInt("mstar.sub.sptfsn.4k2k", 0);
        if (ret == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean is4K2KPlatForm(){
        int[] config = getPanelSize();
        int mPanelWidth = config[0];
        int mPanelHeight = config[1];
        Log.i(TAG,"is4K2KPlatForm:"+ String.valueOf(mPanelWidth)+" "+ String.valueOf(mPanelHeight));
        if (mPanelWidth == 3840 && mPanelHeight == 2160) {
            return true;
        }
        return false;
    }

    public static int getAndroidSDKVersion(){
        Log.i(TAG,"Build.VERSION.SDK_INT:"+ Build.VERSION.SDK_INT);
        return Build.VERSION.SDK_INT;
    }

    /**
     * judge file chaset,use opensource solution from UniversalDetector
     * @param file the file need to judge chaset
     * @return file chaset
     */
    public static String getFileEncoding(String filePath) {
        String encoding = "utf-8";
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        InputStream fs = null;
        BufferedInputStream bis = null;
        try {
            if (isNetPlayback(filePath)) {
                try {
                    fs = new URL(filePath).openStream();
                } catch (MalformedURLException e) {
                     e.printStackTrace();
                } catch (IOException e) {
                     e.printStackTrace();
                }
                if (fs == null) {
                    Log.i(TAG,"fs is null");
                    return null;
                }
                bis = new BufferedInputStream(fs);
            } else {
                fis = new FileInputStream(filePath);
                bis = new BufferedInputStream(fis);
            }
            // (1)
            UniversalDetector detector = new UniversalDetector(null);
            // (2)
            int nread;
            while ((nread = bis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();
            // (4)
            encoding = detector.getDetectedCharset();
            if (encoding != null) {
                 Log.i(TAG,"Detected encoding = " + encoding);
            } else {
                 Log.i(TAG,"No encoding detected.");
                //defualt UTF-8
                encoding = "utf-8";
            }
            // (5)
            detector.reset();
            return encoding;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fs != null) {
                    fs.close();
                }
                if (bis != null) {
                    bis.close();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encoding;
    }

}
