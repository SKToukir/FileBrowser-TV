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

import android.graphics.Point;

import com.walton.filebrowser.R;


public class Constants {
    public static final String IS_DATA_AVAILABLE = "is_data_available";

    public static final int UPDATE_USB_DATA = 0x20;
    public static final int BROWSE_USB_DATA = 0x21;

    public static final String APPS = "Apps";
    public static final String AUDIO = "Audio";
    public static final String ALL = "All";
    public static final String VIDEOS = "Videos";
    public static final String ZIP = "Zip";
    public static final String PHOTOS = "Photos";
    public static final String DOCS = "Docs";

    public static final int POSITION_BACK = -2;
    /** file list focus position */
    public static final int POSITION_0 = 0;
    /** file list focus position */
    public static final int POSITION_9 = 10;
    /** list mode one page most display nine data **/
    public static final int LIST_MODE_DISPLAY_NUM = 9;

    public static final int GRID_MODE_DISPLAY_NUM = 10;

    public static final int GRID_MODE_ONE_ROW_DISPLAY_NUM = 5;

    /**
     * file browsing interface display the current file types including
     * pictures, video, music
     */
    public static final int OPTION_STATE_ALL = 0x01;
    /**
     * file browsing interface display the current file types only pictures and
     * folder
     */
    public static final int OPTION_STATE_PICTURE = 0x02;
    /**
     * file browsing interface display the current file types only music and
     * folder
     */
    public static final int OPTION_STATE_SONG = 0x03;
    /**
     * file browsing interface display the current file types only video and
     * folder
     */
    public static final int OPTION_STATE_VIDEO = 0x04;
    /** flip over instruction: through the remote page up */
    public static final int KEYCODE_PAGE_UP = 0x5;
    /** flip over instruction: through the remote downward turn the page */
    public static final int KEYCODE_PAGE_DOWN = 0x6;
    /** flip over instruction: through the Touch events page up */
    public static final int TOUCH_PAGE_UP = 0x7;
    /** flip over instruction: through the Touch events downward turn the page */
    public static final int TOUCH_PAGE_DOWN = 0x8;
    /** file browsing main interface */
    public static final int BROWSER_TOP_DATA = 0x01;
    /** file browsing interface data from local usb disk equipment */
    public static final int BROWSER_LOCAL_DATA = 0x02;
    /** file browsing interface data from network samba equipment */
    public static final int BROWSER_SAMBA_DATA = 0x03;
    /** file browsing interface data from network dlna equipment */
    public static final int BROWSER_DLNA_DATA = 0x04;
    /** refresh local data */
    public static final int UPDATE_TOP_DATA = 0x05;
    /** refresh local data */
    public static final int UPDATE_LOCAL_DATA = 0x06;
    /** refresh dlna data */
    public static final int UPDATE_DLNA_DATA = 0x07;
    /** refresh samba data */
    public static final int UPDATE_ALL_SAMBA_DATA = 0x08;
    /** refresh samba data */
    public static final int UPDATE_SAMBA_DATA = 0x58;
    public static final int SAMBA_SCAN_COMPLETED = 0x59;
    /** switching media type refresh UI */
    public static final int UPDATE_MEDIA_TYPE = 0x09;
    /** anomaly hints */
    public static final int UPDATE_EXCEPTION_INFO = 0xa;
    /** refresh loading progress information */
    public static final int UPDATE_PROGRESS_INFO = 0xb;
    /** disk loading/remove **/
    public static final int UPDATE_DISK_DEVICE = 0xc;
    /** network cannot use */
    public static final int NETWORK_EXCEPTION = 0xd;
    /** network status changes for not use */
    public static final int NETWORK_UNCONNECTED = 0xf;
    /** without the support of media formats */
    public static final int UNSUPPORT_FORMAT = 0x10;
    /** display loading hints */
    public static final int PROGRESS_TEXT_SHOW = 0x11;
    /** hidden loading hints */
    public static final int PROGRESS_TEXT_HIDE = 0x12;
    /** the mouse moves listview focus update */
    public static final int UPDATE_LISTVIEW_FOCUS = 0x13;
    /** the mouse operate page up */
    public static final int MOUSE_PAGE_UP = 0x14;
    /** the mouse operate downward turn the page */
    public static final int MOUSE_PAGE_DOWN = 0x15;

    public static final int GRID_CANCEL_TASK = 0x16;

    public static final int GRID_CANCEL_TASK_NEED_PLAY = 0x17;

    public static final int GRID_CANCEL_TASK_NO_NEED_PLAY = 0x18;

    public static final int SHOW_DIVX_DIALOG = 0x19;

    /** file categories: general file */
    public static final int FILE_TYPE_FILE = 0x1;
    /** file categories: picture */
    public static final int FILE_TYPE_PICTURE = 0x2;
    /** file categories: music */
    public static final int FILE_TYPE_SONG = 0x3;
    /** file categories: video */
    public static final int FILE_TYPE_VIDEO = 0x4;
    /** file categories: dir */
    public static final int FILE_TYPE_DIR = 0x5;
    /** file categories: return */
    public static final int FILE_TYPE_RETURN = 0x6;
    /** file categories: mstarplaylist */
    public static final int FILE_TYPE_MPLAYLIST = 0x7;
    /** to return to dlna root page */
    public static final String RETURN_TOP = "top";
    /** returns to the local root page */
    public static final String RETURN_LOCAL = "local";
    /** to return to dlna root page */
    public static final String RETURN_DLNA = "dlna";
    /** returns to the samba root page */
    public static final String RETURN_SAMBA = "samba";
    /** not returns to the samba root page */
    public static final String RETURN_NOT_SAMBA = "notsamba";
    /** currently browsing the content page */
    public static final String BUNDLE_PAGE = "current_page";
    /** currently browsing files list of total page */
    public static final String BUNDLE_TPAGE = "total_page";
    /** currently browsing files list focus position */
    public static final String BUNDLE_INDEX = "current_index";
    public static final String BUNDLE_INDEX_KEY = "com.jrm.index";
    public static final String BUNDLE_BASEDATA_KEY = "player_basedata";
    public static final String BUNDLE_FILETYPE_KEY = "player_filetype";
    public static final String BUNDLE_FILEURL_KEY = "player_fileurl";
    public static final String BUNDLE_LIST_KEY = "com.jrm.arraylist";
    public static final String SOURCE_FROM = "sourceFrom";
    public static final String ADAPTER_POSITION = "adapter.position";
    public static final String DB_NAME = "videoplay.db";
    public static final String VIDEO_PLAY_LIST_TABLE_NAME = "videoplaylist";
    // resources from the samba
    public static final int SOURCE_FROM_SAMBA = 0x011;
    /** resources from local disk */
    public static final int SOURCE_FROM_LOCAL = 0x12;
    /** media scanning abnormal state */
    public static final int FAILED_BASE = 0x00;
    /** Ping far end equipment failure: */
    public static final int FAILED_TIME_OUT = FAILED_BASE + 1;
    /** samba landing when password mistake */
    public static final int FAILED_WRONG_PASSWD = FAILED_TIME_OUT + 1;
    /** landing samba failure */
    public static final int FAILED_LOGIN_FAILED = FAILED_WRONG_PASSWD + 1;
    /** landing samba failure */
    public static final int MOUNT_FAILED = FAILED_LOGIN_FAILED + 1;
    /** landing samba other abnormal */
    public static final int FAILED_LOGIN_OTHER_FAILED = MOUNT_FAILED + 1;
    // AB function of the switch
    public static boolean abFlag = false;
    // A function of the switch
    public static boolean aFlag = false;
    public static boolean bSupportDivx = false;
    public static boolean bSupportPhotoScale = true;

    public static final int THREE_D_MODE_2D_TO_3D = 2;
    public static final int THREE_D_MODE_AUTO = 1;
    public static final int THREE_D_MODE_CHECK_BOARD = 7;
    public static final int THREE_D_MODE_FRAME_SEQUENTIAL = 8;
    public static final int THREE_D_MODE_LEFT_RIGHT = 0;
    public static final int THREE_D_MODE_LINE_ALTERNATIVE = 5;
    public static final int THREE_D_MODE_NONE = 0;
    public static final int THREE_D_MODE_PIXEL_ALTERNATIVE = 6;
    public static final int THREE_D_MODE_RIGHT_LEFT = 1;
    public static final int THREE_D_MODE_SIDE_BY_SIDE = 3;
    public static final int THREE_D_MODE_TOP_BOTTOM = 4;
    // Play subtitles
    // public static int[] subTitleSettingOptText = {
    // R.string.subtitle_type_value, R.string.subtitle_out_path_value,
    // R.string.subtitle_in_no_value, R.string.subtitle_language_value,
    // R.string.subtitle_font_value, R.string.subtitle_font_size_value,
    // R.string.subtitle_color_value, R.string.subtitle_frame_value,
    // R.string.subtitle_area_value, R.string.subtitle_adjust_value
    // };
    // Caption title
    public static int[] subTitleSettingName = { R.string.subtitle_type,
            R.string.subtitle_out_path, R.string.subtitle_in_no,
            R.string.subtitle_language, R.string.subtitle_font,
            R.string.subtitle_font_size, R.string.subtitle_color,
            R.string.subtitle_frame, R.string.subtitle_area,
            R.string.subtitle_adjust,
            R.string.subtitle_trackname};
    // Choose played
    public static final int CHOOSE_TIME = 18;
    public static final int SEEK_TIME = 19;
    public static final int OPEN_DUAL_SEEK_TIME = 20;
    public static final int SET_LEFT_VIDEO_SIZE = 21;
    public static final int HANDLE_MESSAGE_PLAYER_EXIT = 22;
    public static final int HANDLE_MESSAGE_CHECK_AB = 23;
    public static final int HANDLE_MESSAGE_SKIP_BREAKPOINT = 24;
    public static final int OSD_3D_UI = 25;
    public static final int ShowController = 40;
    public static final int DualAudioOn = 41;
    public static final int SETHPBT = 42;
    public static final int SHOW_SUBTITLE_DIALOG = 124;
    public static final int SHOW_SUBTITLE_LIST_DIALOG = 125;
    public static final int CHECK_IS_SUPPORTED = 126;
    public static final int CHECK_PIP_SUB_POSITION_LEFT_RIGHT = 127;
    public static final int CHECK_PIP_SUB_POSITION_LEFT_TOP = 128;
    public static final int CHECK_PIP_SUB_POSITION_LEFT_BOTTOM = 129;
    public static final int CHECK_PIP_SUB_POSITION_RIGHT_TOP = 130;
    public static final int CHECK_PIP_SUB_POSITION_RIGHT_BOTTOM = 131;
    public static final int CHOOSE_DIVX_CHAPTER_BY_PRESS_0_TO_9_KEYCODE = 132;
    public static final int DIVX_STOP_ACTION = 133;
    public static final int DIVX_PREVIOUS_ACTION = 134;
    public static final int DIVX_NEXT_ACTION = 135;
    public static final int DIVX_SHOW_CHAPTER_ACTION = 136;

    // Multi Thumbnail
    public static final int SetThumbnailBorderViewOnFocus = 201;
    public static final int ShowThumbnailBorderView = SetThumbnailBorderViewOnFocus + 1;
    public static final int SeekWithHideThumbnailBorderView = SetThumbnailBorderViewOnFocus + 2;
    public static final int seekThumbnailPos = SetThumbnailBorderViewOnFocus + 3;
    public static final int OpenThumbnailPlayer = SetThumbnailBorderViewOnFocus + 4;
    public static final int HideThumbnailBorderView = SetThumbnailBorderViewOnFocus + 5;
    public static final int EnargeThumbnail = SetThumbnailBorderViewOnFocus + 6;
    public static final int FowrardThumbnail = SetThumbnailBorderViewOnFocus + 7;
    public static final int PrepareMediaPlayer = SetThumbnailBorderViewOnFocus + 8;
    public static final int RefreshThumbnailSeekViewPosition= SetThumbnailBorderViewOnFocus + 9;
    public static final int REQUEST_RENDER_THUMBNAIL = SetThumbnailBorderViewOnFocus + 10;
    public static final int INIT_THUMBNAIL_PLAYER = SetThumbnailBorderViewOnFocus + 11;


    // VideoPlayerActivity
    public static final int RefreshCurrentPositionStatusUI = 300;

    // Hidden video article control
    public static final int HIDE_PLAYER_CONTROL = 13;
    public static final String MPO = "mpo";
    public static final String GIF = "gif";
    public static boolean isExit = true;
    public static final int START_MEDIA_PLAYER = 100;
    public static Point sceenResolution= new Point(0,0);
    public static boolean bPhotoSeamlessEnable = false;
    public static boolean bReleasingPlayer = false;
    public static final boolean bStreamLessEnable = false;
    public static final boolean bUseAndroidStandardMediaPlayerTrackAPI = false;

    //listview or gridview mode value,ps:do not use enum for efficiency
    public static final int LISTVIEW_MODE = 0 ;
    public static final int GRIDVIEW_MODE = 1 ;

    //first enter the localMM's view Mode
    public static final int LISTVIEW_MODE_FIRST = 0 ;
    public static final int GRIDVIEW_MODE_FIRST = 1 ;

    //Dolby Logo
    public static final int DOLBY_VISION = 1;
    public static final int DOLBY_ATMOS = 2;
    public static final int DOLBY_LOGO_SHOW_TIME = 3000;//ms

    // used on GridAdapter
    public static final int UNFINISHED = -1;
    public static final int FINISHED = 1;
    public static final int GRID_POSITION_IS_VIDEO = 0;
    public static final int GRID_POSITION_IS_MUSIC = 1;
    public static final int GRID_POSITION_IS_PICTURE = 2;
    public static final int GRID_POSITION_IS_OTHERS = -1;

    // used on localDataBrowser to TAG if any task has been canceled
    public static final int GRID_TASK_NOT_CANCELED = 0;
    public static final int GRID_TASK_CANCELED = 1;

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
    /** HDR open level REF_MODE */
    public static final int HDR_OPEN_LEVEL_REF_MODE = 5;

    /* This value is mapping to EN_DOLBY_HDR_SETTING */
    /** HDR dolby level off */
    public static final int HDR_DOLBY_LEVEL_OFF = 0;
    /** HDR dolby level vivid */
    public static final int HDR_DOLBY_LEVEL_VIVID = 1;

    /* This value is mapping to EN_MAPI_HDR_ERROR_CODE */
    /** The request has not any errors */
    public static final int HDR_ERROR_CODE_SUCCESS = 0;
    /** The request is not defined */
    public static final int HDR_ERROR_CODE_UNDEFINED_FAIL = 1;
    /** The device or current input source is not support HDR */
    public static final int HDR_ERROR_CODE_NOT_SUPPORT = 2;
    /** The HDR is currently enabled */
    public static final int HDR_ERROR_CODE_STILL_WORK = 3;


    public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
    public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
    public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    public static final int MEDIA_TRACK_TYPE_TIMEDBITMAP = 5;


    public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";



    /*Mfc Level*/
    public static final int MFC_LEVEL_OFF = 0;
    public static final int MFC_LEVEL_LOW = 1;
    public static final int MFC_LEVEL_MID = 2;
    public static final int MFC_LEVEL_HIGH = 3;
    public static final int MFC_LEVEL_BYPASS = 4;


    public static final int ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED = -1;
    public static final int HDR_NOT_IS_RUNNING = 0;
    public static final int HDR_IS_RUNNING = 1;


    /**
     * Modules belong to compiler flag catagory
     */
    /** Module: PIP */
    public static final int MODULE_PIP = 0;
    /** Module: TRAVELING */
    public static final int MODULE_TRAVELING = 1;
    /** Module: OFFLINE_DETECT */
    public static final int MODULE_OFFLINE_DETECT = 2;
    /** Module: PREVIEW_MODE */
    public static final int MODULE_PREVIEW_MODE = 3;
    /** Module: FREEVIEW_AU */
    public static final int MODULE_FREEVIEW_AU = 4;
    // TODO: deprecated MODULE_CC/MODULE_BRAZIL_CC after SN remove it
    /** Module: CC */
    public static final int MODULE_CC = 5;
    /** Module: BRAZIL_CC */
    public static final int MODULE_BRAZIL_CC = 6;
    /** Module: KOREAN_CC */
    public static final int MODULE_KOREAN_CC = 7;
    /** For ATSC system, enable [DTV]ATSC_CC_ENABLE + [ATV]NTSC_CC_ENABLE. */
    /** For ISDB system, enable [DTV]ISDB_CC_ENABLE + [ATV]NTSC_CC_ENABLE. */
    /** For ASIA_NTSC system, only enable [ATV]NTSC_CC_ENABLE. */
    /** Module: ATSC_CC_ENABLE */
    public static final int MODULE_ATSC_CC_ENABLE = 8;
    /** Module: ISDB_CC_ENABLE */
    public static final int MODULE_ISDB_CC_ENABLE = 9;
    /** Module: NTSC_CC_ENABLE */
    public static final int MODULE_NTSC_CC_ENABLE = 10;
    /** Module: ATV_NTSC_ENABLE */
    public static final int MODULE_ATV_NTSC_ENABLE = 11;
    /** Module: ATV_PAL_ENABLE */
    public static final int MODULE_ATV_PAL_ENABLE = 12;
    /** Module: ATV_CHINA_ENABLE */
    public static final int MODULE_ATV_CHINA_ENABLE = 13;
    /** Module: ATV_PAL_M_ENABLE */
    public static final int MODULE_ATV_PAL_M_ENABLE = 14;
    /** Module: HDMITX */
    public static final int MODULE_HDMITX = 15;
    /** Module: HBBTV */
    public static final int MODULE_HBBTV = 16;
    /** Module: INPUT_SOURCE_LOCK */
    public static final int MODULE_INPUT_SOURCE_LOCK = 17;
    /** Module: EPG */
    public static final int MODULE_EPG = 18;
    /** Module: AD_SWITCH */
    public static final int MODULE_AD_SWITCH = 19;
    /** Module: VCHIP */
    public static final int MODULE_VCHIP = 20;
    /** Module: OAD */
    public static final int MODULE_OAD = 21;
    /** Module: EDID_AUTO_SWITCH */
    public static final int MODULE_EDID_AUTO_SWITCH = 22;
    /** Module: Open HDR */
    public static final int MODULE_OPEN_HDR = 23;
    /** Module: Dolby HDR */
    public static final int MODULE_DOLBY_HDR = 24;

    /**
     * Module not belong to compiler flag catagory
     */
    /** Start number of modules that are not compiler flags */
    public static final int MODULE_NOT_COMPILE_FLAG_START = 0x00001000;
    /** Module: ATV_MANUAL_TUNING */
    public static final int MODULE_TV_CONFIG_ATV_MANUAL_TUNING = MODULE_NOT_COMPILE_FLAG_START;
    /** Module: AUTO_HOH */
    public static final int MODULE_TV_CONFIG_AUTO_HOH = MODULE_NOT_COMPILE_FLAG_START + 1;
    /** Module: AUDIO_DESCRIPTION */
    public static final int MODULE_TV_CONFIG_AUDIO_DESCRIPTION = MODULE_NOT_COMPILE_FLAG_START + 2;
    /** Module: THREED_DEPTH */
    public static final int MODULE_TV_CONFIG_THREED_DEPTH = MODULE_NOT_COMPILE_FLAG_START + 3;
    /** Module: SELF_DETECT */
    public static final int MODULE_TV_CONFIG_SELF_DETECT = MODULE_NOT_COMPILE_FLAG_START + 4;
    /** Module: THREED_CONVERSION_TWODTOTHREED */
    public static final int MODULE_TV_CONFIG_THREED_CONVERSION_TWODTOTHREED = MODULE_NOT_COMPILE_FLAG_START + 5;
    /** Module: THREED_CONVERSION_AUTO */
    public static final int MODULE_TV_CONFIG_THREED_CONVERSION_AUTO = MODULE_NOT_COMPILE_FLAG_START + 6;
    /** Module: THREED_CONVERSION_PIXEL_ALTERNATIVE */
    public static final int MODULE_TV_CONFIG_THREED_CONVERSION_PIXEL_ALTERNATIVE = MODULE_NOT_COMPILE_FLAG_START + 7;
    /** Module: THREED_CONVERSION_FRAME_ALTERNATIVE */
    public static final int MODULE_TV_CONFIG_THREED_CONVERSION_FRAME_ALTERNATIVE = MODULE_NOT_COMPILE_FLAG_START + 8;
    /** Module: THREED_CONVERSION_CHECK_BOARD */
    public static final int MODULE_TV_CONFIG_THREED_CONVERSION_CHECK_BOARD = MODULE_NOT_COMPILE_FLAG_START + 9;
    /** Module: THREED_TWOD_AUTO */
    public static final int MODULE_TV_CONFIG_THREED_TWOD_AUTO = MODULE_NOT_COMPILE_FLAG_START + 10;
    /** Module: THREED_TWOD_PIXEL_ALTERNATIVE */
    public static final int MODULE_TV_CONFIG_THREED_TWOD_PIXEL_ALTERNATIVE = MODULE_NOT_COMPILE_FLAG_START + 11;
    /** Module: THREED_TWOD_FRAME_ALTERNATIVE */
    public static final int MODULE_TV_CONFIG_THREED_TWOD_FRAME_ALTERNATIVE = MODULE_NOT_COMPILE_FLAG_START + 12;
    /** Module: THREED_TWOD_CHECK_BOARD */
    public static final int MODULE_TV_CONFIG_THREED_TWOD_CHECK_BOARD = MODULE_NOT_COMPILE_FLAG_START + 13;
    /** Module: MM_MULTIPLAYBACK_ENABLE */
    public static final int MODULE_TV_CONFIG_MM_MULTIPLAYBACK_ENABLE = MODULE_NOT_COMPILE_FLAG_START + 14;
    /** Module: PIP_SINGLE_AUDIO_OUPUT_ENABLE */
    public static final int MODULE_TV_CONFIG_PIP_SINGLE_AUDIO_OUPUT_ENABLE = MODULE_NOT_COMPILE_FLAG_START + 15;
    /** Module: REFINE_SAMBA_SCAN_BROWSE_PLAY_ENABLE */
    public static final int MODULE_TV_CONFIG_REFINE_SAMBA_SCAN_BROWSE_PLAY_ENABLE = MODULE_NOT_COMPILE_FLAG_START + 16;

    //PackageManager
    public static final String FEATURE_HDMI_CEC = "android.hardware.hdmi.cec";
    public static final String FEATURE_LIVE_TV = "android.software.live_tv";

    /* Do not change these values without updating their counterparts
     * in device/mstar/common/libraries/tv2/java/com/mstar/android/tv2/TvS3DManager.java
     */
    public static final int THREED_COMMON_EVENT_START = 0;
    public static final int THREED_COMMON_ENABLE_3D = 1;
    public static final int THREED_COMMON_4K2K_UNSUPPORT_DUALVIEW = 2;
    public static final int THREED_COMMON_UPDATE_3D_INFO = 3;
    // Divx
    public static final int KEY_PARAMETER_SET_RESUME_PLAY = 2014;
    public static final int KEY_PARAMETER_GET_DIVX_DRM_IS_RENTAL_FILE = 2015;
    public static final int KEY_PARAMETER_GET_DIVX_DRM_RENTAL_LIMIT = 2016;
    public static final int KEY_PARAMETER_GET_DIVX_DRM_RENTAL_USE_COUNT = 2017;
    public static final int DIVX_FIRST_PLAYBACK = -1;
    public static final int DIVX_FULLSTOP_PLAYBACK = 0;
    public static final int DIVX_RESUMEPLAY_PLAYBACK = 1;

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
}
