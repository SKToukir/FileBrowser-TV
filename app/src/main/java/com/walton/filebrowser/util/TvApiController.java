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

package com.walton.filebrowser.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.CecSetting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

//import com.mstar.android.tvapi.common.vo.SwdrLevel;

public class TvApiController {

    private static final String TAG = TvApiController.class.getSimpleName();

    // 3D display format
    public static final int E_ThreeD_Video_DISPLAYFORMAT_NONE = 0;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE = E_ThreeD_Video_DISPLAYFORMAT_NONE + 1;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_TOP_BOTTOM = E_ThreeD_Video_DISPLAYFORMAT_NONE + 2;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING = E_ThreeD_Video_DISPLAYFORMAT_NONE + 3;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_LINE_ALTERNATIVE = E_ThreeD_Video_DISPLAYFORMAT_NONE + 4;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_2DTO3D = E_ThreeD_Video_DISPLAYFORMAT_NONE + 5;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_AUTO = E_ThreeD_Video_DISPLAYFORMAT_NONE + 6;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_CHECK_BOARD = E_ThreeD_Video_DISPLAYFORMAT_NONE + 7;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_PIXEL_ALTERNATIVE = E_ThreeD_Video_DISPLAYFORMAT_NONE + 8;
    public static final int E_ThreeD_Video_DISPLAYFORMAT_FRAME_ALTERNATIVE = E_ThreeD_Video_DISPLAYFORMAT_NONE + 9;

    // 3dto2d display mode
    public static final int E_ThreeD_Video_3DTO2D_NONE = 0;
    public static final int E_ThreeD_Video_3DTO2D_SIDE_BY_SIDE = E_ThreeD_Video_3DTO2D_NONE + 1;
    public static final int E_ThreeD_Video_3DTO2D_TOP_BOTTOM = E_ThreeD_Video_3DTO2D_NONE + 2;
    public static final int E_ThreeD_Video_3DTO2D_FRAME_PACKING = E_ThreeD_Video_3DTO2D_NONE + 3;
    public static final int E_ThreeD_Video_3DTO2D_LINE_ALTERNATIVE = E_ThreeD_Video_3DTO2D_NONE + 4;
    public static final int E_ThreeD_Video_3DTO2D_FRAME_ALTERNATIVE = E_ThreeD_Video_3DTO2D_NONE + 5;
    public static final int E_ThreeD_Video_3DTO2D_AUTO = E_ThreeD_Video_3DTO2D_NONE + 6;
    public static final int E_ThreeD_Video_3DTO2D_CHECK_BOARD = E_ThreeD_Video_3DTO2D_NONE + 7;
    public static final int E_ThreeD_Video_3DTO2D_PIXEL_ALTERNATIVE = E_ThreeD_Video_3DTO2D_NONE + 8;

    /* This value is mapping to EnumScalerWindow */
    /** main window if with PIP or without PIP */
    public static final int VIDEO_MAIN_WINDOW = 0;
    /** sub window if PIP */
    public static final int VIDEO_SUB_WINDOW = 1;

    /* This value is mapping to EN_HDR_ATTRIBUTES */
    /** HDR open attributes */
    public static final int HDR_OPEN_ATTRIBUTES = 0;
    /** HDR dolby attributes */
    public static final int HDR_DOLBY_ATTRIBUTES = 1;

    /* This value is mapping to EN_OPEN_HDR_SETTING */
    /** HDR open level off */
    public static final int HDR_OPEN_LEVEL_OFF = 0;
    /** HDR open level auto */
    public static final int HDR_OPEN_LEVEL_AUTO = 1;
    /** HDR open level low */
    public static final int HDR_OPEN_LEVEL_LOW = 2;
    /** HDR open level middle */
    public static final int HDR_OPEN_LEVEL_MIDDLE = 3;
    /** HDR open level high */
    public static final int HDR_OPEN_LEVEL_HIGH = 4;

    /* This value is mapping to EN_MAPI_HDR_ERROR_CODE */
    /** The request has not any errors */
    public static final int HDR_ERROR_CODE_SUCCESS = 0;
    /** The request is not defined */
    public static final int HDR_ERROR_CODE_UNDEFINED_FAIL = 1;
    /** The device or current input source is not support HDR */
    public static final int HDR_ERROR_CODE_NOT_SUPPORT = 2;
    /** The HDR is currently enabled */
    public static final int HDR_ERROR_CODE_STILL_WORK = 3;

    public static final int ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED = -1;
    public static final int HDR_NOT_IS_RUNNING = 0;
    public static final int HDR_IS_RUNNING = 1;

    public static final int mAutoDetect3DFromatTimes = 3;
    private static TvApiController mTvApiController;
    private Context mContext = null;
    // MVC sources flag
    private boolean mVideoSourceIsMvc = false;
    private boolean isThreeDMode = false;
    // 3D mode
    private int mThreeDMode = E_ThreeD_Video_DISPLAYFORMAT_AUTO;
   // private EnumThreeDVideo3DTo2D mThreeD3DTo2D;
    private TvCecManager mTvCecManager;
    private final static int THREED_3DTO2D_NO = 0;
    private final static int THREED_3DTO2D_YES = 1;
    // 3D to 2D
    private int mThreeD3DTo2DIndex = THREED_3DTO2D_NO;
    // Sources of 3d type
    private int mVideoSource3DType = E_ThreeD_Video_DISPLAYFORMAT_NONE;
    // Automatic identification number 3d
    private int detectNum = 0;
//    private int detectTopBottomNum = 0;
//    private int detectSideBySideNum = 0;
    private boolean mIsOpen3DTo2D = false;
    private static int mHdrAttributes[] = new int[2];

    private Object mDolbyEventListener = null;

    public TvApiController(Context context) {
        mContext = context;
    }

    public TvApiController() {
    }

    public static TvApiController getInstance(Context context) {
        if (mTvApiController == null) {
            mTvApiController = new TvApiController(context);
        }
        return mTvApiController;
    }

    public void setOnMhlEventListener(final Activity activity) {
        if (isSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onKeyInfo");
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch arg0:" + arg0 + " arg1:" + " arg2:" + arg2);
                    TvCommonManager.getInstance().setInputSource(arg1);
                    Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.putExtra("task_tag", "input_source_changed");
                    activity.startActivity(intent);
                    activity.finish();
                    return false;
                }
            });
        }
    }

    // Check 3D TV PlugedIn
    public boolean is3DTVPlugedIn() {
        if (isSupportTVApi()) {
            TvPictureManager PM = TvPictureManager.getInstance();
            if (PM.is3DTVPlugedIn()) {
                Log.i(TAG, "is3DTVPlugedIn");
                return true;
            }
            Log.i(TAG, "!is3DTVPlugedIn");
        }
        return false;
    }

    /**
     * 3D automatic identification
     */
    public void threeDInit() {
        if (isSupportTVApi()) {
            if(isBox()){
                setOSD3DMode();
                return;
            }
            Log.i(TAG, "ThreeDInit -> mThreeDMode =" + mThreeDMode);
            if (!isVideoSourceMvc()) {

                if (isThreeDVideoSelfAdaptiveDetectOn()) {
                    setThreeDMode(E_ThreeD_Video_DISPLAYFORMAT_AUTO);
                }
                Log.i(TAG, "ThreeDInit -> mThreeDMode=" + mThreeDMode);
            }
        }
    }

    // isThreeDVideoSelfAdaptiveDetectOn is a function to get MiscSetting 3D AutoDetect Switch Status.
    // MiscSetting 3D AutoDetect Switch Value: 0: detect off, 1: right now, 2: when source changed
    public boolean isThreeDVideoSelfAdaptiveDetectOn() {
        if (isSupportTVApi()) {
            int selfDetectMode = TvS3DManager.getInstance().getSelfAdaptiveDetectMode();
            Log.i(TAG, "isThreeDVideoSelfAdaptiveDetectOn");
            if ((selfDetectMode == TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_RIGHT_NOW)
                    || (selfDetectMode == TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_WHEN_SOURCE_CHANGE)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVideoSourceMvc() {
        return mVideoSourceIsMvc;
    }
    public boolean isThreeDAuto() {
        if (mThreeDMode == E_ThreeD_Video_DISPLAYFORMAT_AUTO) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isThreeDMode() {
        if (getCurrent3dType() != E_ThreeD_Video_DISPLAYFORMAT_NONE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is2DTo3DMode() {
        if (!isSupportTVApi()) {
            return false;
        }
        return (getCurrent3dFormat() == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D);
    }

    public boolean isMIHDRMode(){
        return isMIHDRModeOn();
    }

    public boolean isMIHDRModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.mi.hdr", false);
        return  status;
    }

    public boolean isHRDMode() {
        if (!isSupportTVApi()) {
            return false;
        }
        int result[] = new int[2];
        result[0] = ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED;
        result[1] = HDR_NOT_IS_RUNNING;
        result = getHdrAttributes(HDR_OPEN_ATTRIBUTES,VIDEO_MAIN_WINDOW);
        if (result[0]>0 && result[1]>0) {
            return true;
        } else {
            return false;
        }

    }
    public boolean isDolbyHDRMode() {
        if (!isSupportTVApi()) {
            return false;
        }
        int result[] = new int[2];
        result[0] = ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED;
        result[1] = HDR_NOT_IS_RUNNING;
        result = getHdrAttributes(HDR_DOLBY_ATTRIBUTES,VIDEO_MAIN_WINDOW);
        if (result[0]>0 && result[1]>0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * set up 3D model
     *
     * @param threeDModeIndex
     *            3D model index
     */
    public void setThreeDMode(int threeDMode) {
        if (!isSupportTVApi()) {
            return;
        }
        Log.i(TAG, "settingThreeDMode->mThreeDMode=" + threeDMode);
        mThreeDMode = threeDMode;
        if (mThreeDMode == E_ThreeD_Video_DISPLAYFORMAT_AUTO) {
            // Log.i(TAG, "TvS3DManager autoDetect3DFormat " + mAutoDetect3DFromatTimes + " times");
            // TvS3DManager.getInstance().autoDetect3DFormat(mAutoDetect3DFromatTimes);
            autoDetect3DFormat(mAutoDetect3DFromatTimes);
            // Tools.setDisplayFormat(mThreeDMode);
            if (!isVideoSourceMvc()) {
                int videoSource3DTypetemp = getCurrent3dType();
                mVideoSource3DType = videoSource3DTypetemp;
                Log.i(TAG, "settingThreeDMode -> mThreeDMode=" + mThreeDMode);
                Log.i(TAG, "settingThreeDMode -> mVideoSource3DType=" + mVideoSource3DType);
            }
        } else {
            if (mThreeDMode != E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                set3dDisplayFormat(mThreeDMode);
            }
        }
        Log.i(TAG, "settingThreeDMode -> mThreeDMode=" + mThreeDMode);
    }

    public boolean autoDetect3DFormat(int autoDetect3DFormatTimes) {
        Log.i(TAG, "autoDetect3DFormat autoDetect3DFormatTimes:" + autoDetect3DFormatTimes);
        //return TvS3DManager.getInstance().autoDetect3DFormat(autoDetect3DFormatTimes);
        boolean result = false;
        for (int i = 0; i < autoDetect3DFormatTimes; i++) {
            if (mContext == null) {
                return false;
            }
            if (!isCurrentInputSourceStorage()) {
                return false;
            }
            if (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE != TvS3DManager.getInstance().getCurrent3dType()) {
                Log.i(TAG, "Don't need autoDetect3dFormat, because current 3D mode is " + TvS3DManager.getInstance().getCurrent3dType());
                return true;
            }
            Log.i(TAG, "detect3dFormat times: " + i +" / "+autoDetect3DFormatTimes);
            result = TvS3DManager.getInstance().autoDetect3DFormat(1);
            Log.i(TAG, "detect3dFormat type:" + TvS3DManager.getInstance().getCurrent3dType());
            Log.i(TAG, "detect3dFormat result: " + result);
            if (true == result) {
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * * 3D sources turn 2D to watch
     *
     * @param threeD3DTo2DIndex
     *            3D turn 2D parameter index
     */
    public void set3DTo2D(int threeD3DTo2DIndex) {
        if (isSupportTVApi()) {
            mThreeD3DTo2DIndex = threeD3DTo2DIndex;
            Log.i(TAG, "setting3DTo2D -> mThreeD3DTo2DIndex=" + mThreeD3DTo2DIndex);
            switch (mThreeD3DTo2DIndex) {
                case THREED_3DTO2D_NO:
                    Log.i(TAG, "setting3DTo2D 0000000 -> mThreeDMode=" + mThreeDMode);
                    setThreeDMode(mThreeDMode);
                    break;
                case THREED_3DTO2D_YES:
                    Log.i(TAG, "setting3DTo2D 1111111 -> mThreeDMode=" + mThreeDMode);
                    // 3D to 2D
                    set3DTo2DDisplayMode(mThreeDMode);
                    break;
                default:
                    break;
            }
        }



    }

    public void setOSD3DMode() {
        if (Tools.unSupportTVApi()) {
            return;
        }
    }

    public void init3DMode() {
        if (isSupportTVApi()) {
            TvS3DManager mTvS3DManager = TvS3DManager.getInstance();
            try {
                if (mTvS3DManager != null) {
                    TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
                    int mode = mTvPipPopManager.getCurrentPipMode();
                    Log.i(TAG, "******mode:****" + mode);
                    if (mode == TvPipPopManager.E_PIP_MODE_PIP) {
                        swithVol2Main();
                        mTvPipPopManager.disablePip();
                        mTvPipPopManager.setPipOnFlag(false);
                    } else if (mode == TvPipPopManager.E_PIP_MODE_POP) {
                        swithVol2Main();
                        int formatType = mTvS3DManager.getCurrent3dType();
                        if (formatType == mTvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
                            mTvPipPopManager.disable3dDualView();
                        } else {
                            mTvPipPopManager.disablePop();
                        }
                    } else {
                        int formatType = mTvS3DManager.getCurrent3dType();
                        Log.i(TAG, "******formatType:****" + formatType);
                        if (formatType == mTvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
                            mTvPipPopManager.disable3dDualView();
                        }
                    }
                }
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        }
    }

    public void disable3dDualView() {
        if (isSupportTVApi()) {
            TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
            TvS3DManager mTvS3DManager = TvS3DManager.getInstance();
            if (mTvS3DManager != null) {
                try {
                    int formatType = mTvS3DManager.getCurrent3dType();
                    if (formatType == mTvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW
                        && mTvPipPopManager!= null) {
                        Log.i(TAG, "disable3dDualView EN_3D_DUALVIEW");
                        mTvPipPopManager.disable3dDualView();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void swithVol2Main() {
        if (isSupportTVApi()) {
            try {
                TvAudioManager.getInstance().setAudioCaptureSource(
                        TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                        TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public boolean sendCecKey(int keyCode) {
        Log.d(TAG, "send Cec key,keyCode is " + keyCode);
        if (isSupportTVApi()) {
            try {
                if (mTvCecManager == null) {
                    mTvCecManager = TvCecManager.getInstance();
                }
                CecSetting setting = mTvCecManager.getCecConfiguration();
                if (setting.cecStatus == 1) {
                    if (mTvCecManager.sendCecKey(keyCode)) {
                        Log.d(TAG, "send Cec key,keyCode is " + keyCode + ", localmm don't handl the key");
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void setVideoMute(boolean isMute, int time) {
        Log.i("Tools", "*********setVideoMute********isMute:" + isMute + " time:" + time);
        if (isSupportTVApi()) {
            try {
                if (TvCommonManager.getInstance() != null) {
                    TvCommonManager.getInstance().setVideoMute(isMute,
                            TvCommonManager.SCREEN_MUTE_BLACK, time,
                            TvCommonManager.INPUT_SOURCE_STORAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Static class will capture large memory
    //private static TvCommonManager appSkin = null;

    /*
     * Complete TV and storage of between source switching
     */
    public void changeSource(final boolean isEnter) {
        if (!isSupportTVApi()) {
            return;
        }
        Runnable localRunnable = new Runnable() {
            @Override
            public void run() {
                TvCommonManager appSkin = TvCommonManager.getInstance();
                int inputSource;

                // boot will input from TV switch to AP
                if (appSkin != null) {
                    // switching source to storage
                    if (isEnter) {
                        inputSource = appSkin.getCurrentTvInputSource();
                        if (inputSource != TvCommonManager.INPUT_SOURCE_STORAGE) {
                            appSkin.setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                        }
                    } else {// switching source to the TV
                        inputSource = appSkin.getCurrentTvInputSource();
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

    /**
     * set 3D display format
     * @param displayFormat display format
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_NONE
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_CHECK_BOARD
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_PIXEL_ALTERNATIVE
     * @see #THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_ALTERNATIVE
     * @return boolean true if operation success or false if fail.
     */
    public boolean set3dDisplayFormat(int displayFormat) {
        Log.d(TAG, "set3dDisplayFormat(), paras displayFormat = " + displayFormat);
        if (isSupportTVApi()) {
            return TvS3DManager.getInstance().set3dDisplayFormat(displayFormat);
        }
        return false;
    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    @Deprecated
    public void setDisplayFormat(int threeDVideoDisplayFormat) {
        TvS3DManager.getInstance().set3dDisplayFormat(threeDVideoDisplayFormat);
    }

    /**
     * set 3dto2d display mode
     * @param displayMode display mode
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_NONE
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_TOP_BOTTOM
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_FRAME_PACKING
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_LINE_ALTERNATIVE
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_FRAME_ALTERNATIVE
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_CHECK_BOARD
     * @see #THREE_DIMENSIONS_VIDEO_3DTO2D_PIXEL_ALTERNATIVE
     * @return boolean true if operation success or false if fail.
     */
    public boolean set3DTo2DDisplayMode(int displayMode) {
        Log.d(TAG, "set3DTo2DDisplayMode(), paras displayMode = " + displayMode);
        if (isSupportTVApi()) {
            if (displayMode != 0) {
                mIsOpen3DTo2D = true;
            } else {
                mIsOpen3DTo2D = false;
            }
            return TvS3DManager.getInstance().set3DTo2DDisplayMode(displayMode);
        } else {
            return false;
        }
    }

    public  boolean isOpen3DTo2D(){
        return mIsOpen3DTo2D;
    }
    /**
     * get current 3D type
     * @return int 3D type
     * @see #THREE_DIMENSIONS_TYPE_NONE
     * @see #THREE_DIMENSIONS_TYPE_SIDE_BY_SIDE_HALF
     * @see #THREE_DIMENSIONS_TYPE_TOP_BOTTOM
     * @see #THREE_DIMENSIONS_TYPE_FRAME_PACKING_1080P
     * @see #THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P
     * @see #THREE_DIMENSIONS_TYPE_LINE_ALTERNATIVE
     * @see #THREE_DIMENSIONS_TYPE_FRAME_ALTERNATIVE
     * @see #THREE_DIMENSIONS_TYPE_FIELD_ALTERNATIVE
     * @see #THREE_DIMENSIONS_TYPE_CHECK_BORAD
     * @see #THREE_DIMENSIONS_TYPE_2DTO3D
     * @see #THREE_DIMENSIONS_TYPE_DUALVIEW
     * @see #THREE_DIMENSIONS_TYPE_PIXEL_ALTERNATIVE
     */
    public int getCurrent3dType() {
        if (isSupportTVApi()) {
            return TvS3DManager.getInstance().getCurrent3dType();
        } else {
            return E_ThreeD_Video_DISPLAYFORMAT_NONE;
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

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    @Deprecated
    public int getCurrent3dFormatOnSTB2DTV() {
        int result = TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE;
        result = TvS3DManager.getInstance().get3DTo2DDisplayMode();
        return result;

    }

    public void setHpBtMainSource(boolean isMainSource) {
        // change earphone and bluetooth to main source or sub source
        if (isSupportTVApi()) {
            try{
                Log.v(TAG, "setHpBtMainSource isMainSource:"+isMainSource);
                TvAudioManager mTvAudioManager = TvAudioManager.getInstance();
                if(isMainSource) {
                    mTvAudioManager.setAudioCaptureSource(TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                    mTvAudioManager.setOutputSourceInfo(TvAudioManager.AUDIO_VOL_SOURCE_HP_OUT,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                } else {
                    mTvAudioManager.setAudioCaptureSource(TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                    mTvAudioManager.setOutputSourceInfo(TvAudioManager.AUDIO_VOL_SOURCE_HP_OUT,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSpeakerMainSource(boolean isMainSource) {
        // change speaker to main source or sub source, not use now
        if (isSupportTVApi()) {
            try{
                Log.v(TAG, "setSpeakerMainSource isMainSource:"+isMainSource);
                TvAudioManager mTvAudioManager = TvAudioManager.getInstance();
                if(isMainSource) {
                    mTvAudioManager.setOutputSourceInfo(TvAudioManager.AUDIO_VOL_SOURCE_SPEAKER_OUT,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
                } else {
                    mTvAudioManager.setOutputSourceInfo(TvAudioManager.AUDIO_VOL_SOURCE_SPEAKER_OUT,
                            TvAudioManager.AUDIO_CAPTURE_SOURCE_SUB_SOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPipModeEnabled() {
        if (isSupportTVApi()) {
            TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
            if (mTvPipPopManager.isPipModeEnabled()) {
                return true;
            }
        }

        return false;
    }

    public boolean isPIPMode() {
        if (isSupportTVApi()) {
            try {
                TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
                if (mTvPipPopManager != null) {

                    int mode = mTvPipPopManager.getCurrentPipMode();
                    Log.i(TAG, "******mode:****" + mode);
                    if (mode == TvPipPopManager.E_PIP_MODE_PIP || mode == TvPipPopManager.E_PIP_MODE_POP) {
                        return true;
                    } else {
                        TvS3DManager mTvS3DManager = TvS3DManager.getInstance();
                        if (mTvS3DManager!=null
                            && mTvS3DManager.getCurrent3dType()
                            == mTvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean isCurrentInputSourceStorage() {
        if (isSupportTVApi()) {
            TvCommonManager appSkin = TvCommonManager.getInstance();
            int inputSource;
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

    public boolean isSupportTVApi() {
        return !Tools.unSupportTVApi();
    }

    public int getAC4PresentationNum() {
        if (!isSupportTVApi()) {
            return 0;
        }
        TvAudioManager tvAudioManager = TvAudioManager.getInstance();
        TvCommonManager appSkin = TvCommonManager.getInstance();
	int presentationNum = 0;
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
             Method getAC4Presentation = clz.getDeclaredMethod("getAC4Presentation",
                                           new Class[]{int.class});
             Object presentation = getAC4Presentation.invoke(tvAudioManager, appSkin.getCurrentTvInputSource());
            Class presentationClass = presentation.getClass();
            Field presentation_num = presentationClass.getDeclaredField("presentation_num");
            presentationNum = Integer.parseInt(presentation_num.get(presentation).toString());
            Log.i(TAG,"presentationNum: "+presentationNum);
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
            Log.i(TAG, "getAC4PresentationNum presentationNum:" + presentationNum);
            return presentationNum;
        }
        // TvAudioManager.getInstance().setAC4Presentation(index);
    }

    public int[] getAC4PresentationLanguage() {
        if (!isSupportTVApi()) {
            return null;
        }
        TvAudioManager tvAudioManager = TvAudioManager.getInstance();
        TvCommonManager appSkin = TvCommonManager.getInstance();
	 int []presentationLang = null;
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
            Method getAC4Presentation = clz.getDeclaredMethod("getAC4Presentation", new Class[]{int.class});
            Object presentation = getAC4Presentation.invoke(tvAudioManager, appSkin.getCurrentTvInputSource());
            Class presentationClass = presentation.getClass();
            Field presentation_lang = presentationClass.getDeclaredField("presentation_lang");
            Object value = presentation_lang.get(presentation);
            if(value != null) {
                if (value.getClass().isArray()) {
                    int[] array = (int[]) value;
                    Log.v(TAG,"array .length: " + array.length);
                    presentationLang = new int[array.length];
                    presentationLang = array;
                }
            }
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
            Log.i(TAG, "getAC4PresentationLanguage presentationLang:" + presentationLang);
            return presentationLang;
        }
        // TvAudioManager.getInstance().setAC4Presentation(index);
    }

    public void setAC4Presentation(int index) {
        if (!isSupportTVApi()) {
            return;
        }

        Log.i(TAG, "setAC4Presentation index:" + index);
        TvAudioManager tvAudioManager = TvAudioManager.getInstance();
        TvCommonManager appSkin = TvCommonManager.getInstance();
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
             Method setAC4Presentation = clz.getDeclaredMethod("setAC4Presentation",
                                           new Class[]{int.class, int.class});
             Object object = setAC4Presentation.invoke(tvAudioManager, index, appSkin.getCurrentTvInputSource());
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
        }
        // TvAudioManager.getInstance().setAC4Presentation(index);
    }
/*
    public AudioAc4Presentation getAC4Presentation(int index) {
        if (!isSupportTVApi()) {
            return null;
        }
        TvAudioManager.getInstance().getAC4Presentation();
    }
*/
    public int getAC4DialogEnhance() {
        if (!isSupportTVApi()) {
            return 0;
        }
        int enhance = 0;
        TvAudioManager tvAudioManager = TvAudioManager.getInstance();
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
             Method getAC4DialogEnhance = clz.getDeclaredMethod("getAC4DialogEnhance",
                                           new Class[]{});
             Object object = getAC4DialogEnhance.invoke(tvAudioManager);
             enhance = Integer.parseInt((String)object);
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
        }
        Log.i(TAG, "getAC4DialogEnhance enhance:" + enhance);
        return enhance;
        // TvAudioManager.getInstance().getAC4DialogEnhance();
    }

    public void setAC4DialogEnhance(int progress) {
        if (!isSupportTVApi()) {
            return;
        }
        Log.i(TAG, "setAC4DialogEnhance progress:" + progress);
        TvAudioManager tvAudioManager = TvAudioManager.getInstance();
        TvCommonManager appSkin = TvCommonManager.getInstance();

        try {
             Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
             Method setAC4DialogEnhance = clz.getDeclaredMethod("setAC4DialogEnhance",
                                           new Class[]{int.class, int.class});
             Object object = setAC4DialogEnhance.invoke(tvAudioManager, progress, appSkin.getCurrentTvInputSource());
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
        }
        // TvAudioManager.getInstance().setAC4DialogEnhance(progress);
    }

    public boolean isSwdrEnabled() {
        if (!isSupportTVApi()) {
            return false;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
            Method getSwdrInfo = clz.getDeclaredMethod("getSwdrInfo",
                                           new Class[]{});
            Object swdrInfo = getSwdrInfo.invoke(mTvPictureManager);
            Log.i(TAG, "isSwdrEnabled: " + (swdrInfo != null ? "true" : "false"));
            if (swdrInfo != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public int getSwdrSupportCount() {
        Log.i(TAG, "getHdrAttributes");
        if (!isSupportTVApi()) {
            return 0;
        }
        int swdrSupportCount = 0;
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
            Method getSwdrInfo = clz.getDeclaredMethod("getSwdrInfo",
                                           new Class[]{});
            Object swdrInfo = getSwdrInfo.invoke(mTvPictureManager);
            Class swdrInfoClass = swdrInfo.getClass();
            Field supportedcount = swdrInfoClass.getDeclaredField("supportedcount");
            swdrSupportCount = Integer.parseInt(supportedcount.get(swdrInfo).toString());
            Log.i(TAG,"getSwdrSupportCount: "+swdrSupportCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return swdrSupportCount;

    }

    public boolean setSwdrLevel(int swdrIndex, int enWin) {
        Log.i(TAG, "setSwdrLevel swdrIndex:"+swdrIndex+",enWin:"+enWin);
        boolean result = false;
        if (!isSupportTVApi()) {
            return result;
        }
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
             Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
             Method setSwdrLevel = clz.getDeclaredMethod("setSwdrLevel",
                                           new Class[]{int.class,int.class});
             Object object = setSwdrLevel.invoke(mTvPictureManager,swdrIndex,enWin);
             result = Boolean.parseBoolean(object.toString());
             return result;
        } catch (Exception e) {
              e.printStackTrace();
        } finally {
             return  result;
        }
    }

    public int getSwdrLevelIndex() {
        Log.i(TAG, "getSwdrLevelIndex");
        if (!isSupportTVApi()) {
            return 0;
        }
        int swdrIndex = 0;
        TvPictureManager mTvPictureManager = TvPictureManager.getInstance();
        try {
            Class clz = Class.forName("com.mstar.android.tv.TvPictureManager");
            Method getSwdrInfo = clz.getDeclaredMethod("getSwdrInfo",
                                           new Class[]{});
            Object swdrInfo = getSwdrInfo.invoke(mTvPictureManager);
            Class swdrInfoClass = swdrInfo.getClass();
            Field swdrindex = swdrInfoClass.getDeclaredField("swdrindex");
            swdrIndex = Integer.parseInt(swdrindex.get(swdrInfo).toString());
            Log.i(TAG,"swdrIndex: "+swdrIndex);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return swdrIndex;

    }

    public String getHardwareName() {
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

    public boolean isBox() {
        String MSTAR_PRODUCT_CHARACTERISTICS = "mstar.product.characteristics";
        String MSTAR_PRODUCT_STB = "stb";
        String mProduct = null;
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

    public int[] getHdrAttributes(int attribute, int window) {
        Log.i(TAG, "getHdrAttributes");
        mHdrAttributes[0] = ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED;
        mHdrAttributes[1] = HDR_NOT_IS_RUNNING;
        if (!isSupportTVApi()) {
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
            Log.i(TAG,"result level:"+String.valueOf(result)+" "+String.valueOf(level));
            Log.i(TAG,"isRunning:"+isRunning);
            if (result == HDR_ERROR_CODE_NOT_SUPPORT ||
                result == HDR_ERROR_CODE_UNDEFINED_FAIL) {
                return mHdrAttributes;
            } else {
                mHdrAttributes[0] = level;
                if (isRunning) {
                    mHdrAttributes[1] = HDR_IS_RUNNING;
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
     * <li> {@link #HDR_DOLBY_LEVEL_OFF}
     * <li> {@link #HDR_DOLBY_LEVEL_VIVID}
     * </ul>
     * <p> When attribute is set as HDR_DOLBY_ATTRIBUTES, the supported value are:
     * <ul>
     * <li> {@link #HDR_DOLBY_LEVEL_OFF}
     * <li> {@link #HDR_DOLBY_LEVEL_VIVID}
     * </ul>
     * @return boolean TRUE:Success, or FALSE:failed.
     * @throws TvCommonException
     */
    public boolean setHdrAttributes(int attribute, int window, int level) {
        Log.i(TAG, "setHdrAttributes");
        boolean result = false;
        if (!isSupportTVApi()) {
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

    public String getTvLanguage(int iLang) {
        Log.i(TAG, "getTvLanguage iLang:" + iLang);
        if (!isSupportTVApi()) {
            return " " + iLang;
        }
        return TvLanguage.getName(iLang);
    }


    public void registerDolbyEventListner(InvocationHandler handler){
        if(!Tools.unSupportTVApi()) {
            try{

                TvAudioManager tvAudioManager = TvAudioManager.getInstance();

                Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");

                Field dolDapVer = clz.getDeclaredField("AUDIO_CAPABILITY_DOLBY_DAP_VERSION");
                Field dolbyVer1 = clz.getDeclaredField("AUDIO_DOLBY_DAP_VERSION_V1");

                Method getAudioCapability = clz.getDeclaredMethod("getAudioCapability",
                                                                    new Class[]{int.class});
                Object object = getAudioCapability.invoke(tvAudioManager, dolDapVer.getInt(null));
                int audioCap = Integer.parseInt(object.toString());

                if (audioCap < dolbyVer1.getInt(null)){
                    return;
                }

                Class clzOnDolbyEventListener =
                        Class.forName("com.mstar.android.tv.TvAudioManager$OnDolbyEventListener");
                Method mRegDolbyEventListener =
                        clz.getDeclaredMethod("registerOnDolbyEventListener"
                                                    ,clzOnDolbyEventListener);
                mDolbyEventListener =
                            Proxy.newProxyInstance(clzOnDolbyEventListener.getClassLoader(),
                            new Class[] { clzOnDolbyEventListener },
                            handler);

                mRegDolbyEventListener.invoke(tvAudioManager, mDolbyEventListener );
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }


    public void unRegisterDolbyEventListner(){
        if(!Tools.unSupportTVApi() && mDolbyEventListener != null) {

            try{

                TvAudioManager tvAudioManager = TvAudioManager.getInstance();
                Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
                Class clzOnDolbyEventListener =
                        Class.forName("com.mstar.android.tv.TvPictureManager$OnDolbyEventListener");
                Method unRegDolbyEventListener =
                        clz.getDeclaredMethod("unregisterOnDolbyEventListener"
                                                    ,clzOnDolbyEventListener);
                unRegDolbyEventListener.invoke(tvAudioManager, mDolbyEventListener );
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                mDolbyEventListener = null;
            }
        }
    }


    public int getDolbyAtomParam(){

        try{
            TvAudioManager tvAudioManager = TvAudioManager.getInstance();
            Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");

            Field dapMode = clz.getDeclaredField("DAP_MODE_PARAM_ATMOS");

            Method getDolbyDapUserParam = clz.getDeclaredMethod("getDolbyDapUserParam",
                                                                new Class[]{int.class});
            Object object = getDolbyDapUserParam.invoke(tvAudioManager, dapMode.getInt(null));
            int ret = Integer.parseInt(object.toString());
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

