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

package com.walton.filebrowser.ui.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mstar.android.MDisplay;
import com.mstar.android.MDisplay.PanelMode;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.photo.GifDecoder;
import com.walton.filebrowser.business.photo.MyInputStream;
import com.walton.filebrowser.dlna.server.DLNAConstants;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.view.ACProgressConstant;
import com.walton.filebrowser.ui.view.ACProgressFlower;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.ToastFactory;
import com.walton.filebrowser.util.Tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImagePlayerActivity extends Activity {
    private static final String TAG = "ImagePlayerActivity";

    private static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
    private static final String ACTION_MCAST_STATE_CHANGED = "com.mstar.android.mcast.MCAST_STATE_CHANGED";
    private static final int KEYCODE_ZOOM = 253;
    private static final int KEYCODE_ZOOM_TWO = 255;
    private static final int KEYCODE_TV_SETTING = 264;
    // the id of article control button
    private static final int OPTION_STATE_PRE = 0x01;

    private static final int OPTION_STATE_PLAY = 0x02;

    private static final int OPTION_STATE_NEXT = 0x03;

    private static final int OPTION_STATE_ENL = 0x04;

    private static final int OPTION_STATE_NARROW = 0x05;

    private static final int OPTION_STATE_TURNLEFT = 0x06;

    private static final int OPTION_STATE_TURNRIGHT = 0x07;

    private static final int OPTION_STATE_INFO = 0x08;

    private static final int OPTION_STATE_WALLPAPER = 0x09;

    private static final int OPTION_STATE_3D = 0xa;

    private static final int OPTION_STATE_SETTING = 0xb;

    // Set transfer news value，Pictures are playing
    private static final int PPT_PLAYER = 0xb;

    private static final int PHOTO_3D = 0xd;

    private static final int PHOTO_DECODE_PROPRESS = 0xe;

    private static final int PHOTO_DECODE_FINISH = 0xf;

    // private static final int PLAY_FIRST_PHOTO = 0xa;

    // private static final int PLAY_4K2K_PHOTO = 0xab;

    // private static final int RESTORE_CURRENT_VIEW = 0xba;
    private static final int INIT_THREED = 0xbe;

    protected static final int PHOTO_PLAY_SETTING = 0x9;

    // protected static final int SWITCH_VIEW = 0x25;

    // Hidden article control
    private static final int HIDE_PLAYER_CONTROL = 0x10;
    private static final int PHOTO_NAME_UPDATE = 0x11;
    private static final int SHOW_TOAST = 0x12;
    private static final int S3D_UPDATE_3D_INFO = 0x13;
    // Article control default disappear time 3s
    private static final int DEFAULT_TIMEOUT = 3000;

    private static final int IMAGE_VIEW = 0;

    private static final int SURFACE_VIEW = 1;

    private static final int SURFACE4K2K_VIEW = 2;

    private static final int GIF_VIEW = 3;

    // the largest size of file can be decode
    // private static final long LARGEST_FILE_SIZE = 30 * 1024 * 1024;
    // the largest pix of photo can be decode successful
    private static final long UPPER_BOUND_PIX = 1920 * 8 * 1080 * 8;

    private static final double UPPER_BOUND_WIDTH_PIX = 1920.0f;

    private static final double UPPER_BOUND_HEIGHT_PIX = 1080.0f;

    private static final int mStep = 60;

    // Picture player all control container
    private ImagePlayerViewHolder mPhotoPlayerHolder;

    // control view
    private LinearLayout mPlayControllerLayout;

    // all photo file
    private List<BaseData> mPhotoFileList = new ArrayList<BaseData>();

    // Picture detailed information Dialog
    private PhotoInfoDialog mPhotoInfoDialog;

    private boolean b3DMode = false;

    // Video buffer progress bar
    private ACProgressFlower mProgressDialog;

    // Disk pull plug monitor
    private DiskChangeReceiver mDiskChangeReceiver;

    private FileInputStream mFileInputStream = null;

    private TvCommonManager appSkin;

    private InputStream is = null;
    /**
     * use TV2 api replace EnumThreeDVideoDisplayFormat displayFormat
     */
    public int mDisplayFormat ;
    // control show or not
    private boolean mIsControllerShow = true;

    private boolean mCanSetWallpaper = true;

    // Key shielding switch
    private boolean mCanResponse = true;

    // Whether in the playing mode
    private boolean mPPTPlayer = false;

    private boolean mIsPlaying = false;

    // To determine which control for focus
    // focus by default in the play button
    private int mState = OPTION_STATE_PLAY;

    int mEVType = TvPictureManager.VIDEO_ARC_DEFAULT;
    /**
     * support new TV2 api
     */
    public int mThreeDVideo3DTo2D;
    // index in list
    private int mCurrentPosition = 0;

    // Picture enlarge or reduce The Times
    private float mZoomTimes = 1.0f;
    public float mMaxZoomInTimes = 2.4f;
    public float mMinZoomOutTimes = 0.4f;

    private float mRotateAngle = 0f;

    // screen resolution
    private int mWindowResolutionWidth = 0;
    private int mWindowResolutionHeight = 0;

    private int mCurrentView = IMAGE_VIEW;

    private int mPreCurrentView = IMAGE_VIEW;

    // The current broadcast document source, local or network
    private int mSourceFrom;

    // Image analytical thread
    private static Thread mThread = new Thread();

    private Thread mZoomThread = new Thread();

    private Thread mRotateThread = new Thread();

    private static BitmapFactory.Options options;

    // private boolean isExit = false;
    private int detectNum = 0;

    private boolean isShowMsg = false;

    private boolean isOnPause = false;

    // Only MPO format images can switch 3d
    private boolean isMPO = false;

    private boolean isAnimationOpened = false;

    private int slideTime = 3000;

    // 4K2K photo decode so slow, so define slide4K2KTime for 4K2K photo play
    private int slide4K2KTime = 5000;

    private boolean fristShowPicture = true;

    protected boolean isDefaultPhoto = false;

    private boolean bHasOpen3D = false;

    private boolean isSettingWallPaper = false;

    private boolean bHasSetDefaultMode = false;

    private String str4K2KPhotoPath = "";

    private boolean mWillExit = false;

    private boolean isErrorDialogShow = false;

    public boolean mIsSourceChange = false;

    // processing images play and pause
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PPT_PLAYER: // Slide mode play pictures
                    if (mPPTPlayer) {
                        moveNextOrPrevious(1);
                    }
                    break;
                case PHOTO_3D:
                    if (hasScaleOrRotate()) {
                        showToastAtBottom(getString(R.string.photo_scale_or_rotate_toast));
                        break;
                    }
                    // Currently Comment out setJustScanMode, because image player do not need it.
                    // setJustScanMode();
                    break;
                case INIT_THREED:
                    open3Dphoto(true);
                    break;
                case PHOTO_DECODE_PROPRESS:
                    dismissProgressDialog();
                    if (isSettingWallPaper) {
                        showProgressDialog(R.string.picture_wallpaper);
                    } else {
                        mPhotoPlayerHolder.aviPhoto.smoothToShow();
//                        showProgressDialog(R.string.picture_decoding);
                    }
                    break;
                case PHOTO_DECODE_FINISH:
                    mPhotoPlayerHolder.aviPhoto.smoothToHide();
//                    dismissProgressDialog();
                    break;
                case HIDE_PLAYER_CONTROL:
                    hideController();
                    break;
                case PHOTO_PLAY_SETTING:
                    Bundle mBundle = msg.getData();
                    isAnimationOpened = mBundle.getBoolean("isOpen");
                    slideTime = mBundle.getInt("time");
                    SharedPreferences mShared = getSharedPreferences("photoPlayerInfo",
                            Context.MODE_PRIVATE);
                    Editor editor = mShared.edit();
                    editor.putBoolean("isAnimationOpened", isAnimationOpened);
                    editor.commit();
                    break;
                case Constants.HANDLE_MESSAGE_PLAYER_EXIT:
                    ImagePlayerActivity.this.finish();
                    break;
                case PHOTO_NAME_UPDATE:
                    mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                    + mPhotoFileList.get(mCurrentPosition).getName());
                    break;
                case SHOW_TOAST:
                    showToast(getString(msg.arg1), Gravity.CENTER, Toast.LENGTH_SHORT);
                    break;
                case S3D_UPDATE_3D_INFO:
                    Log.i(TAG,"mHandler S3D_UPDATE_3D_INFO");
                    if (ipt != null) {
                        ipt.refreshThreeDSettingDialogItemView();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // for 4K2K
    protected static boolean is4K2KMode = false;

    // protected static boolean panel4k2kmode = false;
    // protected static boolean b4k2kenable =
    // SystemProperties.getBoolean("mstar.4k2k.photo", false);
    private boolean bDecodeRet = false;

    private ImagePlayerThreeDSetting ipt = null;
    private boolean mHasPrevious3DOperationFinished = true;
    private boolean mIs3DUpdateEventExist = true;
    private Object mclzOnS3DCommonEventListener = null;
    public void setJustScanMode() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        if(TvPictureManager.VIDEO_ARC_JUSTSCAN != TvPictureManager.getInstance().getVideoArcType()){
            TvPictureManager.getInstance().setVideoArcType(TvPictureManager.VIDEO_ARC_JUSTSCAN);
        }
    }

    public void setDefaultMode() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        Log.i(TAG, "setDefaultMode ---------------  mEVType:" + mEVType);
        if (bHasSetDefaultMode) {
            return;
        }
        int formatType = TvS3DManager.getInstance().getCurrent3dType();
        Log.i(TAG,"the current format type is:"+formatType);
        if (formatType != TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
            TvS3DManager.getInstance().set3dDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
        }
        TvPictureManager.getInstance().setVideoArcType(mEVType);
        bHasSetDefaultMode = true;
    }

    private int getCurZoomMode() {
        int EVA = TvPictureManager.VIDEO_ARC_DEFAULT;
        EVA = TvPictureManager.getInstance().getVideoArcType();
        return EVA;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "------onCreate ------");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
        setContentView(R.layout.image_show);
        setDefaultMode();
        mCurrentPosition = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        mSourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
        String IntentPath = Tools.parseUri(getIntent().getData());
//        if (IntentPath == null) {   // psqiu    测试使用
//            IntentPath = "/storage/emulated/0/4.jpg";
//        }
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
            mPhotoFileList = getIntent().getParcelableArrayListExtra(Constants.BUNDLE_LIST_KEY);
        } else {
            mPhotoFileList = MediaContainerApplication.getInstance().getMediaData(
                    Constants.FILE_TYPE_PICTURE);
        }
        findView();
        mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
        mState = OPTION_STATE_PLAY;
        mCurrentPosition = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        mSourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
            mPhotoFileList = getIntent().getParcelableArrayListExtra(Constants.BUNDLE_LIST_KEY);
        } else {
            // get all cache photo files
            if (IntentPath != null) {
                BaseData bd = new BaseData();
                bd.setPath(IntentPath);
                bd.setName(Tools.getFileName(IntentPath));
                mCurrentPosition = 0;
                mPhotoFileList.add(bd);
           } else {
                mPhotoFileList = MediaContainerApplication.getInstance().getMediaData(
                    Constants.FILE_TYPE_PICTURE);
           }
        }
        if (mPhotoFileList == null || mPhotoFileList.get(mCurrentPosition) == null) {
            return;
        }
        mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                + mPhotoFileList.get(mCurrentPosition).getName());
        WindowManager windowManager = getWindowManager();
        if (!Tools.unSupportTVApi()) {
            mEVType = getCurZoomMode();
            Log.i(TAG, "mEVType:" + mEVType);
        }
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mWindowResolutionWidth = point.x; // display.getWidth();
        mWindowResolutionHeight = point.y;
        Log.i(TAG, "mWindowResolutionWidth:" + mWindowResolutionWidth + " mWindowResolutionHeight:" + mWindowResolutionHeight);
        PhotoImageViewClickListener listener = new PhotoImageViewClickListener();
        mPhotoPlayerHolder.setOnClickListener(listener);
        // switch source monitor
        IntentFilter sourceChange = new IntentFilter(ACTION_CHANGE_SOURCE);
        this.registerReceiver(mSourceChangeReceiver, sourceChange);
        IntentFilter castStateChangeIntentFilter = new IntentFilter(ACTION_MCAST_STATE_CHANGED);
        this.registerReceiver(mCastStateChangeReceiver, castStateChangeIntentFilter);
        Constants.bPhotoSeamlessEnable = Tools.isPhotoStreamlessModeOn();
        String platform = Tools.getHardwareName();
        if(Tools.getTotalMem() < 512*1024) {
            Constants.bSupportPhotoScale = false;
        }
        SharedPreferences mShared = getSharedPreferences("photoPlayerInfo", Context.MODE_PRIVATE);
        isAnimationOpened = mShared.getBoolean("isAnimationOpened", false);
        // mHandler.sendEmptyMessageDelayed(INIT_THREED, 500);
        startToShowPhoto();
        if (!Tools.unSupportTVApi()) {
            try {
                TvS3DManager mTvS3DManager = TvS3DManager.getInstance();
                Class clz = Class.forName("com.mstar.android.tv.TvS3DManager");
                Class clzOnS3DCommonEventListener =
                    Class.forName("com.mstar.android.tv.TvS3DManager$OnS3DCommonEventListener");
                Method registerOnS3DCommonEventListener =
                    clz.getDeclaredMethod("registerOnS3DCommonEventListener",
                    clzOnS3DCommonEventListener);
                mclzOnS3DCommonEventListener =
                        Proxy.newProxyInstance(clzOnS3DCommonEventListener.getClassLoader(),
                        new Class[] { clzOnS3DCommonEventListener },
                        new OnS3DCommonEventListenerImpl());
                registerOnS3DCommonEventListener.invoke(mTvS3DManager,
                        new Object[] { mclzOnS3DCommonEventListener });

            } catch (Exception e) {
                mIs3DUpdateEventExist = false;
                e.printStackTrace();
            }

        }
        // mHandler.sendEmptyMessageDelayed(INIT_THREED, 800);
        if (!Tools.unSupportTVApi()) {
            // Currently Comment out setJustScanMode, because image player do not need it.
            // setJustScanMode();
            // mHandler.sendEmptyMessageDelayed(INIT_THREED, 1200);
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
                    exitPlayer();

                    // ComponentName componentName = new ComponentName("mstar.tvsetting.ui",
                    //        "mstar.tvsetting.ui.RootActivity");
                    Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                    // Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    // intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.putExtra("task_tag", "input_source_changed");
                    ImagePlayerActivity.this.startActivity(intent);

                    finish();
                    return false;
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "********onDestroy*******");
        dismissProgressDialog();
        stopPPTPlayer();
        unregisterReceiver(mSourceChangeReceiver);
        // unregisterReceiver(homeKeyEventBroadCastReceiver);
        unregisterReceiver(mCastStateChangeReceiver);
        Constants.isExit = true;
        if (bHasOpen3D) {
            open3Dphoto(false);
        }

        if (mIs3DUpdateEventExist && !Tools.unSupportTVApi()) {
            try {
                TvS3DManager mTvS3DManager = TvS3DManager.getInstance();
                Class clz = Class.forName("com.mstar.android.tv.TvS3DManager");
                Class clzOnS3DCommonEventListener =
                    Class.forName("com.mstar.android.tv.TvS3DManager$OnS3DCommonEventListener");
                Method unregisterOnS3DCommonEventListener =
                    clz.getDeclaredMethod("unregisterOnS3DCommonEventListener",
                    clzOnS3DCommonEventListener);
                unregisterOnS3DCommonEventListener.invoke(mTvS3DManager,
                        new Object[] { mclzOnS3DCommonEventListener });
                mclzOnS3DCommonEventListener = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---------- onStop ---------");
        mWillExit = true;
        if (mCurrentView == IMAGE_VIEW) {
            mPhotoPlayerHolder.mSurfaceView.stopPlayback(true);
            // Currently Comment out setDefaultMode, because image player do not need setJustScanMode.
            // setDefaultMode();
        }
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "********onPause*******" + Constants.isExit);
        isOnPause = true;
        if (mThread.isAlive() && options != null) {
            options.requestCancelDecode();
        }

        // Began to disk scan
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.startMediascanner(ImagePlayerActivity.this);
            }
        }).start();
        unregisterReceiver(mDiskChangeReceiver);
        unregisterReceiver(mNetDisconnectReceiver);
        // Close file resources
        closeSilently(mFileInputStream);
        closeSilently(is);

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "********onResume*******" + Constants.isExit);
        isOnPause = false;
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        mDiskChangeReceiver = new DiskChangeReceiver();
        registerReceiver(mDiskChangeReceiver, filter);
        IntentFilter networkIntentFilter = new IntentFilter("com.mstar.localmm.network.disconnect");
        registerReceiver(mNetDisconnectReceiver, networkIntentFilter);
        // registerReceiver(homeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.stopMediascanner(ImagePlayerActivity.this);
            }
        }).start();
        if (fristShowPicture) {
            fristShowPicture = false;
            int count = mPhotoFileList.size();
            if (count == 0) {
                finish();
                return;
            } else if (count <= mCurrentPosition) {
                mCurrentPosition = count - 1;
            }
            // System.gc();
            isShowMsg = true;
            Constants.isExit = false;
        } else {
            showController();
        }
        // animationNum = animationArray.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!mIsControllerShow) {
                showController();
                hideControlDelay();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        Log.i(TAG, "********onTouchEvent*******" + event.getAction());
        return super.onTouchEvent(event);
    }

    public void startShowProgress() {
        mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
    }

    public void stopShowingProgress() {
        mHandler.sendEmptyMessage(PHOTO_DECODE_FINISH);
    }

    public int getPhotoFileListSize() {
        if (mPhotoFileList != null) {
            return mPhotoFileList.size();
        }
        return 0;
    }

    private void startToShowPhoto() {
        String url = mPhotoFileList.get(mCurrentPosition).getPath();
        url = Tools.fixPath(url);
        boolean bgif = url.substring(url.lastIndexOf(".") + 1).equalsIgnoreCase(Constants.GIF);
        if (bgif) {
            decodeGif(url);
            mCurrentView = GIF_VIEW;
        } else {
            mCurrentView = IMAGE_VIEW;
            mPhotoPlayerHolder.mSurfaceView.setHandler(mHandler);
            mPhotoPlayerHolder.mSurfaceView.setImagePath(url, ImagePlayerActivity.this);
        }

    }

    public boolean getmHasPrevious3DOperationFinished(){
        if (mIs3DUpdateEventExist == false ) {
            // If mIs3DUpdateEventExist is false ,then the 3D event callback may not exist at this platform.
            // And  retrun true anyway to do next 3D operation
            // because we cannot know if 3D operation has been finished or not.
            Log.i(TAG,"getmHasPrevious3DOperationFinished:true");
            return true;
        }
        Log.i(TAG,"getmHasPrevious3DOperationFinished"+mHasPrevious3DOperationFinished);
        return mHasPrevious3DOperationFinished;
    }

    public void setmHasPrevious3DOperationFinished(boolean flag){
        Log.i(TAG,"setmHasPrevious3DOperationFinished"+flag);
        this.mHasPrevious3DOperationFinished = flag;
    }

    public class OnS3DCommonEventListenerImpl implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG,"method.getName():"+method.getName());
            if(!"onEvent".equals(method.getName())) {
                return true;
            }
            int what  = Integer.parseInt(String.valueOf(args[0]));
            int arg1  = Integer.parseInt(String.valueOf(args[1]));
            int arg2  = Integer.parseInt(String.valueOf(args[2]));
            Log.i(TAG, "onEvent,what = "+ what
                               +",arg1 = "+arg1
                               +",arg2 = "+arg2);
            if (what == Constants.THREED_COMMON_ENABLE_3D) {
                Log.i(TAG, "getMsg TvS3DManager.THREED_COMMON_ENABLE_3D");
            } else if (what == Constants.THREED_COMMON_4K2K_UNSUPPORT_DUALVIEW) {
                Log.i(TAG, "getMsg TvS3DManager.THREED_COMMON_4K2K_UNSUPPORT_DUALVIEW");
            } else if (what == Constants.THREED_COMMON_UPDATE_3D_INFO) {
                Log.i(TAG, "getMsg TvS3DManager.THREED_COMMON_UPDATE_3D_INFO");
            }
            Message m = Message.obtain();
            m.what = S3D_UPDATE_3D_INFO;
            m.arg1 = arg1;
            m.arg2 = arg2;
            mHandler.sendMessage(m);
            return true;
        }

    }

    public void onError(MediaPlayer mp, int framework_err, int impl_err) {
        if (!mWillExit) {
            showToastAtCenter(getString(R.string.picture_decode_failed));
        }

        int position = mCurrentPosition + 1;
        if (position >= mPhotoFileList.size() || mCurrentPosition < 0) {
            stopPPTPlayer();
            this.finish();
        } else {
            showTipDialog("framework_err:" + framework_err + " impl_err:" + impl_err);
            if(isErrorDialogShow){
                stopPPTPlayer() ;
            }
        }

    }

    public void showTipDialog(String msg) {
        String sName = mPhotoFileList.get(mCurrentPosition).getName();
        String showtip = sName + " "+ msg + "!\n" + getResources().getString(R.string.photo_media_play_next);
        showErrorDialog(showtip);
    }

    // Pop up display an error dialog box
    private void showErrorDialog(final String strMessage) {
        dismissProgressDialog();
        // Prevent activity died when the popup menu
        if (!isFinishing()) {
            new AlertDialog.Builder(ImagePlayerActivity.this)
                    .setTitle(getResources().getString(R.string.show_info))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(strMessage)
                    .setPositiveButton(getResources().getString(R.string.exit_ok),
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isErrorDialogShow = false ;
                                    moveNextOrPrevious(1);
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.exit_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isErrorDialogShow = false ;
                                    ImagePlayerActivity.this.finish();
                                }
                            }).setCancelable(false).show();
            isErrorDialogShow = true ;
        }
    }

    public void open3Dphoto(boolean bOpen) {
        Log.i(TAG, "open3Dphoto ---------- bOpen:" + bOpen);
        if (Tools.unSupportTVApi()) {
            return;
        }
        if (bOpen) {
            Tools.setDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO);
            bHasOpen3D = true;
        } else {
            Tools.setDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO);
            bHasOpen3D = false;
        }
    }

    /**
     * show module
     */
    private void findView() {
        mPlayControllerLayout = (LinearLayout) findViewById(R.id.photo_suspension_layout);
        mPhotoPlayerHolder = new ImagePlayerViewHolder(this);
        mPhotoPlayerHolder.findViews();
        showController();
        hideControlDelay();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown ----------- keyCode:" + keyCode);

        if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
            if (isPhotoParsing()) {
                return true;
            }
            if (mPPTPlayer) {
                stopPPTPlayer();
            }
            if (Constants.bPhotoSeamlessEnable
                && !checkIfItIsGifPhoto(0)
                && !checkIfItIsGifPhoto(-1)) {
                initParameterBeforeShowNextPhoto();
                mPhotoPlayerHolder.mSurfaceView.showNextPhoto(-1);
            } else {
                moveNextOrPrevious(-1);
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT ) {
            if (isPhotoParsing()) {
                return true;
            }
            if (mPPTPlayer) {
                stopPPTPlayer();
            }
            if (Constants.bPhotoSeamlessEnable
                && !checkIfItIsGifPhoto(0)
                && !checkIfItIsGifPhoto(1)) {
                initParameterBeforeShowNextPhoto();
                mPhotoPlayerHolder.mSurfaceView.showNextPhoto(1);
            } else {
                moveNextOrPrevious(1);
            }
            return true;
        }

        if (!mIsControllerShow) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    Log.d(TAG, "move up");
                    mPhotoPlayerHolder.mSurfaceView.moveDirection(0, 0-mStep);
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Log.d(TAG, "move down");
                    mPhotoPlayerHolder.mSurfaceView.moveDirection(0, mStep);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.d(TAG, "move left");
                    mPhotoPlayerHolder.mSurfaceView.moveDirection(0-mStep, 0);
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.d(TAG, "move right");
                    mPhotoPlayerHolder.mSurfaceView.moveDirection(mStep, 0);
                    return true;
            }

        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (isPhotoParsing()) {
                        break;
                    }
                    cancleDelayHide();
                    if (mPPTPlayer) {
                        stopPPTPlayer();
                    }
                    moveNextOrPrevious(-1);
                    // hideControlDelay();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (isPhotoParsing()) {
                        break;
                    }
                    cancleDelayHide();
                    if (mPPTPlayer) {
                        stopPPTPlayer();
                    }
                    moveNextOrPrevious(1);
                    // hideControlDelay();
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    cancleDelayHide();
                    drapLeft();
                    hideControlDelay();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    cancleDelayHide();
                    drapRight();
                        hideControlDelay();
                        break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    Log.i(TAG, "onKeyDown: KEYCODE_ENTER");
                    cancleDelayHide();
                    registerListeners();
                    hideControlDelay();
                    break;
                /*
                 * case KeyEvent.KEYCODE_BACK: mPhotoFileList.clear();
                 * if(mThread.isAlive()){
                 * mHandler.sendEmptyMessageDelayed(Constants.
                 * HANDLE_MESSAGE_PLAYER_EXIT, 1000); }else{ this.finish(); }
                 * break;
                 */
                default:
                    Log.i(TAG, "onKeyDown: default");
                    break;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isPhotoParsing()) {
                return true;
            }

            mWillExit = true;
            //mPhotoFileList.clear();
            if (is4K2KMode) {
                if (mPlayControllerLayout != null) {
                    mPlayControllerLayout.setVisibility(View.INVISIBLE);
                    mIsControllerShow = false;
                }
                is4K2KMode = false;
                // panel4k2kmode = false;
                MDisplay.setPanelMode(PanelMode.E_PANELMODE_NORMAL);
                this.finish();
            }

            // setDefaultMode();
            if (mThread.isAlive()) {
                mHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 1000);
            } else {
                // mPhotoPlayerHolder.mSurfaceView.stopPlayback(false);
                ImagePlayerActivity.this.finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "*********onKeyUp*********" + keyCode);

        if (!mCanResponse) {
            return true;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (!mIsControllerShow) {
                    showController();
                    hideControlDelay();
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                if (!mPPTPlayer) {
                    PlayProcess();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_STOP:
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (!mIsControllerShow) {
                    showController();
                    hideControlDelay();
                } else {
                    hideController();
                }
                return true;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * The remote control left-hand button, and focus switching
     */
    private void drapLeft() {
        switch (mState) {
            case OPTION_STATE_PRE:
                mState = OPTION_STATE_NARROW;
                mPhotoPlayerHolder.setPhotoPreSelect(false);
                mPhotoPlayerHolder.setPhotoNarrowSelect(true);
//                mPhotoPlayerHolder.setPhotoPlaySelect(mPPTPlayer,true);
                break;
            case OPTION_STATE_PLAY:
                mState = OPTION_STATE_PRE;
                mPhotoPlayerHolder.setPhotoPlaySelect(false, mPPTPlayer);
                mPhotoPlayerHolder.setPhotoPreSelect(true);
                break;
            case OPTION_STATE_NEXT:
                mState = OPTION_STATE_PLAY;
                mPhotoPlayerHolder.setPhotoNextSelect(false);
                mPhotoPlayerHolder.setPhotoPlaySelect(true,mPPTPlayer);
                break;
            case OPTION_STATE_ENL:
                mState = OPTION_STATE_INFO;
                mPhotoPlayerHolder.setPhotoEnlargeSelect(false);
                mPhotoPlayerHolder.setPhotoInfoSelect(true);
                break;
            case OPTION_STATE_NARROW:
                mState = OPTION_STATE_ENL;
                mPhotoPlayerHolder.setPhotoNarrowSelect(false);
                mPhotoPlayerHolder.setPhotoEnlargeSelect(true);
                break;
            case OPTION_STATE_TURNLEFT:
                mState = OPTION_STATE_NEXT;
                mPhotoPlayerHolder.setPhotoTurnLeftSelect(false);
                mPhotoPlayerHolder.setPhotoNextSelect(true);
                break;
            case OPTION_STATE_TURNRIGHT:
                mState = OPTION_STATE_TURNLEFT;
                mPhotoPlayerHolder.setPhotoTurnRightSelect(false);
                mPhotoPlayerHolder.setPhotoTurnLeftSelect(true);
                break;
            case OPTION_STATE_INFO:
                mState = OPTION_STATE_TURNRIGHT;
                mPhotoPlayerHolder.setPhotoInfoSelect(false);
                mPhotoPlayerHolder.setPhotoTurnRightSelect(true);
                break;
            case OPTION_STATE_WALLPAPER:
                mState = OPTION_STATE_INFO;
                mPhotoPlayerHolder.setPhotoWallpaperSelect(false);
                mPhotoPlayerHolder.setPhotoInfoSelect(true);
                break;
            case OPTION_STATE_3D:
                mState = OPTION_STATE_WALLPAPER;
                mPhotoPlayerHolder.setPhoto3DSelect(false);
                mPhotoPlayerHolder.setPhotoWallpaperSelect(true);
                break;
        }
    }

    /**
     * The remote control right-hand button, and focus switching
     */
    private void drapRight() {
        switch (mState) {
            case OPTION_STATE_PRE:
                mState = OPTION_STATE_PLAY;
                mPhotoPlayerHolder.setPhotoPreSelect(false);
                mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
                break;
            case OPTION_STATE_PLAY:
                mState = OPTION_STATE_NEXT;
                mPhotoPlayerHolder.setPhotoPlaySelect(false, mPPTPlayer);
                mPhotoPlayerHolder.setPhotoNextSelect(true);
                break;
            case OPTION_STATE_NEXT:
                mState = OPTION_STATE_TURNLEFT;
                mPhotoPlayerHolder.setPhotoNextSelect(false);
                mPhotoPlayerHolder.setPhotoTurnLeftSelect(true);
                break;
            case OPTION_STATE_ENL:
                mState = OPTION_STATE_NARROW;
                mPhotoPlayerHolder.setPhotoEnlargeSelect(false);
                mPhotoPlayerHolder.setPhotoNarrowSelect(true);
                break;
            case OPTION_STATE_NARROW:
                mState = OPTION_STATE_PRE;
                mPhotoPlayerHolder.setPhotoNarrowSelect(false);
                mPhotoPlayerHolder.setPhotoPreSelect(true);
                break;
            case OPTION_STATE_TURNLEFT:
                mState = OPTION_STATE_TURNRIGHT;
                mPhotoPlayerHolder.setPhotoTurnLeftSelect(false);
                mPhotoPlayerHolder.setPhotoTurnRightSelect(true);
                break;
            case OPTION_STATE_TURNRIGHT:
                mState = OPTION_STATE_INFO;
                mPhotoPlayerHolder.setPhotoTurnRightSelect(false);
                mPhotoPlayerHolder.setPhotoInfoSelect(true);
                break;
            case OPTION_STATE_INFO:
                mState = OPTION_STATE_ENL;
                mPhotoPlayerHolder.setPhotoInfoSelect(false);
                mPhotoPlayerHolder.setPhotoEnlargeSelect(true);
                break;
            case OPTION_STATE_WALLPAPER:
                mState = OPTION_STATE_3D;
                mPhotoPlayerHolder.setPhotoWallpaperSelect(false);
                mPhotoPlayerHolder.setPhoto3DSelect(true);
                break;
            case OPTION_STATE_3D:
                mState = OPTION_STATE_PRE;
                mPhotoPlayerHolder.setPhoto3DSelect(false);
                mPhotoPlayerHolder.setPhotoPreSelect(true);
                ;
                break;
        }
    }

    private void PlayProcess() {
        /*
         * if (mCurrentView != IMAGE_VIEW && mCurrentView != GIF_VIEW &&
         * mCurrentView != SURFACE4K2K_VIEW) {
         * showToastAtBottom(getString(R.string.photo_3D_toast)); return; }
         */
        if (mPPTPlayer) {
            // If is playing.
            stopPPTPlayer();
        } else {
            // Determine whether the current for the last one
            if ((mCurrentPosition + 1) < mPhotoFileList.size()) {
                mPPTPlayer = true;
                // currentPosition++;
                hideControlDelay();
                mHandler.sendEmptyMessage(PPT_PLAYER);
            } else if (mCurrentPosition == mPhotoFileList.size() - 1) {
                mPPTPlayer = true;
                //mCurrentPosition = -1;
                mHandler.sendEmptyMessage(PPT_PLAYER);
            }
        }
        mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
    }

    private void zoomIn() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mZoomTimes >= mMaxZoomInTimes) {
            showToast(getString(R.string.max_tip), Gravity.CENTER, 500);
            return;
        }
        if (mZoomThread.isAlive()) {
            showToast(getString(R.string.photo_zooming), Gravity.CENTER, 500);
            return;
        }
        mZoomTimes += 0.2;
        mZoomThread = new Thread(new Runnable() {
            public void run() {
                mPhotoPlayerHolder.mSurfaceView.scaleImage(mRotateAngle, mZoomTimes);
            }
        });
        mZoomThread.start();
    }

    private void zoomOut() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mZoomTimes < mMinZoomOutTimes) {
            showToast(getString(R.string.min_tip), Gravity.CENTER, 500);
            return;
        }
        if (mZoomThread.isAlive()) {
            showToast(getString(R.string.photo_zooming), Gravity.CENTER, 500);
            return;
        }
        mZoomTimes -= 0.2;
        mZoomThread = new Thread(new Runnable() {
            public void run() {
                mPhotoPlayerHolder.mSurfaceView.scaleImage(mRotateAngle, mZoomTimes);
            }
        });
        mZoomThread.start();
    }

    private void rotateImageLeft() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mRotateAngle == -360)
            mRotateAngle = 0;
        mRotateAngle -= 90;
        if (mRotateThread.isAlive()) {
            showToast(getString(R.string.photo_rotating), Gravity.CENTER, 500);
            return;
        }
        mRotateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mPhotoPlayerHolder.mSurfaceView.rotateImage(mRotateAngle,mZoomTimes);
            }
        });
        mRotateThread.start();
    }

    private void rotateImageRight() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mRotateAngle == 360)
            mRotateAngle = 0;
        mRotateAngle += 90;
        if (mRotateThread.isAlive()) {
            showToast(getString(R.string.photo_rotating), Gravity.CENTER, 500);
            return;
        }
        mRotateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mPhotoPlayerHolder.mSurfaceView.rotateImage(mRotateAngle,mZoomTimes);
            }
        });
        mRotateThread.start();
    }

    /**
     * Each button response events
     */
    private void registerListeners() {
        Log.i(TAG, "registerListeners: mState:" + mState);
        switch (mState) {
            case OPTION_STATE_PRE:
                if (isPhotoParsing()) {
                    break;
                }
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                if (Constants.bPhotoSeamlessEnable
                    && !checkIfItIsGifPhoto(0)
                    && !checkIfItIsGifPhoto(-1)) {
                    initParameterBeforeShowNextPhoto();
                    mPhotoPlayerHolder.mSurfaceView.showNextPhoto(-1);
                } else {
                    moveNextOrPrevious(-1);
                }
                break;
            case OPTION_STATE_PLAY:
                if (isPhotoParsing()) {
                    return;
                }
                PlayProcess();
                break;
            case OPTION_STATE_NEXT:
                if (isPhotoParsing()) {
                    break;
                }
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                if (Constants.bPhotoSeamlessEnable
                    && !checkIfItIsGifPhoto(0)
                    && !checkIfItIsGifPhoto(1)) {
                    initParameterBeforeShowNextPhoto();
                    mPhotoPlayerHolder.mSurfaceView.showNextPhoto(1);
                } else {
                    moveNextOrPrevious(1);
                }
                break;
            case OPTION_STATE_ENL:
                if(!Constants.bSupportPhotoScale) {
                    showToastAtBottom(getString(R.string.photo_not_support_scale));
                    break;
                }
                if (Tools.hasSet3dFormat()) {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                    break;
                }
                if (mCurrentView == IMAGE_VIEW) {
                    zoomIn();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                break;
            case OPTION_STATE_NARROW:
                if(!Constants.bSupportPhotoScale) {
                    showToastAtBottom(getString(R.string.photo_not_support_scale));
                    break;
                }
                if (Tools.hasSet3dFormat()) {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                    break;
                }
                if (mCurrentView == IMAGE_VIEW) {
                    zoomOut();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                break;
            case OPTION_STATE_TURNLEFT:
                if (Tools.hasSet3dFormat()) {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                    break;
                }
                if (mCurrentView == IMAGE_VIEW) {
                    rotateImageLeft();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                break;
            case OPTION_STATE_TURNRIGHT:
                if (Tools.hasSet3dFormat()) {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                    break;
                }
                if (mCurrentView == IMAGE_VIEW) {
                    rotateImageRight();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                break;
            case OPTION_STATE_WALLPAPER:
                if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else if (mCurrentView == SURFACE_VIEW) {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                } else {
                    if (mCanSetWallpaper) {
                        setPhoto2Wallpaper();
                    } else {
                        showToastAtBottom(getString(R.string.is_setting_wallpaper));
                    }
                }
                break;
            case OPTION_STATE_INFO:
                showPhotoInfo();
                break;
            case OPTION_STATE_3D:
                if (hasScaleOrRotate()) {
                    showToastAtBottom(getString(R.string.photo_scale_or_rotate_toast));
                    break;
                }
                if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.picture_can_not_set3d));
                } else {

                    if (ipt == null) {
                        ipt = new ImagePlayerThreeDSetting(ImagePlayerActivity.this);
                    }
                    ipt.show();
                }
                break;
            case OPTION_STATE_SETTING:
                sbowPhotoSetting();
                break;
        }
    }

    /**
     * pictures play relevant Settings
     */
    private void sbowPhotoSetting() {
        ImageSettingDialog mPhotoSettingDialog = new ImageSettingDialog(this, slideTime,
                isAnimationOpened, mHandler);
        mPhotoSettingDialog.show();
    }

    private void setPhoto2Wallpaper() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        new AlertDialog.Builder(this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(getString(R.string.photo_set_wallpaper))
                .setMessage(getString(R.string.photo_set_wallpaper))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCanSetWallpaper = false;
                                        String fullPath = mPhotoFileList
                                                .get(mCurrentPosition).getPath();
                                        isSettingWallPaper = true;
                                        mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
                                        final WallpaperManager wpm = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                        if (Tools.isSambaPlaybackUrl(fullPath)) {
                                            fullPath = Tools.convertToHttpUrl(fullPath);
                                        }
                                        final Bitmap bitmap = decodeBitmap(fullPath);
                                        if (bitmap == null) {
                                            Log.e(TAG, "Couldn't get bitmap for path!!");
                                        } else {
                                            try {
                                                wpm.suggestDesiredDimensions(mWindowResolutionWidth,mWindowResolutionHeight);
                                                wpm.setBitmap(bitmap);
                                                try {
                                                    Thread.sleep(500);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                isSettingWallPaper = false;
                                                mHandler.sendEmptyMessage(PHOTO_DECODE_FINISH);
                                            } catch (IOException e) {
                                                isSettingWallPaper = false;
                                                e.printStackTrace();
                                                Log.e(TAG, "Failed to set wallpaper.");
                                            }
                                        }
                                        mCanSetWallpaper = true;
                                    }
                                }).start();
                            }
                        }).show();
    }

    /**
     * mouse click button.
     */
    private class PhotoImageViewClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!mIsControllerShow) {
                showController();
                hideControlDelay();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                return;
            } else {
                hideControlDelay();
            }

            if (!mCanResponse) {
                return;
            }
            switch (v.getId()) {
                case R.id.player_previous:
                    if (isPhotoParsing()) {
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mPPTPlayer) {
                        stopPPTPlayer();
                    }
                    if (Constants.bPhotoSeamlessEnable
                        && !checkIfItIsGifPhoto(0)
                        && !checkIfItIsGifPhoto(-1)) {
                        initParameterBeforeShowNextPhoto();
                        mPhotoPlayerHolder.mSurfaceView.showNextPhoto(-1);
                    } else {
                        moveNextOrPrevious(-1);
                    }
                    // hideControlDelay();
                    mPhotoPlayerHolder.setPhotoPreSelect(true);
                    mState = OPTION_STATE_PRE;
                    break;
                case R.id.photo_play:
                    if (isPhotoParsing()) {
                        return;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    PlayProcess();
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
                    mState = OPTION_STATE_PLAY;
                    break;
                case R.id.photo_next:
                    if (isPhotoParsing()) {
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mPPTPlayer) {
                        stopPPTPlayer();
                    }
                    if (Constants.bPhotoSeamlessEnable
                        && !checkIfItIsGifPhoto(0)
                        && !checkIfItIsGifPhoto(1)) {
                        initParameterBeforeShowNextPhoto();
                        mPhotoPlayerHolder.mSurfaceView.showNextPhoto(1);
                    } else {
                        moveNextOrPrevious(1);
                    }
                    // hideControlDelay();
                    mPhotoPlayerHolder.setPhotoNextSelect(true);
                    mState = OPTION_STATE_NEXT;
                    break;
                case R.id.photo_enlarge:
                    if(!Constants.bSupportPhotoScale) {
                        showToastAtBottom(getString(R.string.photo_not_support_scale));
                        break;
                    }
                    if (Tools.hasSet3dFormat()) {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == IMAGE_VIEW) {
                        zoomIn();
                    } else {
                        showToastAtBottom(getString(R.string.photo_GIF_toast));
                    }
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoEnlargeSelect(true);
                    mState = OPTION_STATE_ENL;
                    break;
                case R.id.photo_narrow:
                    if(!Constants.bSupportPhotoScale) {
                        showToastAtBottom(getString(R.string.photo_not_support_scale));
                        break;
                    }
                    if (Tools.hasSet3dFormat()) {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == IMAGE_VIEW) {
                        zoomOut();
                    } else {
                        showToastAtBottom(getString(R.string.photo_GIF_toast));
                    }
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoNarrowSelect(true);
                    mState = OPTION_STATE_NARROW;
                    break;
                case R.id.photo_turn_left:
                    if (Tools.hasSet3dFormat()) {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == IMAGE_VIEW) {
                        rotateImageLeft();
                    } else {
                        showToastAtBottom(getString(R.string.photo_GIF_toast));
                    }
                    // rotateImageLeft();
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoTurnLeftSelect(true);
                    mState = OPTION_STATE_TURNLEFT;
                    break;
                case R.id.photo_turn_right:
                    if (Tools.hasSet3dFormat()) {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == IMAGE_VIEW) {
                        rotateImageRight();
                    } else if (mCurrentView == GIF_VIEW) {
                        showToastAtBottom(getString(R.string.photo_GIF_toast));
                    } else {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                    }
                    // rotateImageRight();
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoTurnRightSelect(true);
                    mState = OPTION_STATE_TURNRIGHT;
                    break;
                case R.id.photo_info:
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    showPhotoInfo();
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoInfoSelect(true);
                    mState = OPTION_STATE_INFO;
                    break;
                case R.id.photo_wallpaper:
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == GIF_VIEW) {
                        showToastAtBottom(getString(R.string.photo_GIF_toast));
                    } else if (mCurrentView == SURFACE_VIEW) {
                        showToastAtBottom(getString(R.string.photo_3D_toast));
                    } else {
                        if (mCanSetWallpaper) {
                            setPhoto2Wallpaper();
                        } else {
                            showToastAtBottom(getString(R.string.is_setting_wallpaper));
                        }
                    }
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoWallpaperSelect(true);
                    mState = OPTION_STATE_WALLPAPER;
                    break;
                case R.id.photo_3d:
                    if (hasScaleOrRotate()) {
                        showToastAtBottom(getString(R.string.photo_scale_or_rotate_toast));
                        break;
                    }
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    if (mCurrentView == GIF_VIEW) {
                        showToastAtBottom(getString(R.string.picture_can_not_set3d));
                    } else {
                        // open3Dphoto(true);
                        if (ipt == null) {
                            ipt = new ImagePlayerThreeDSetting(ImagePlayerActivity.this);
                        }
                        ipt.show();
                    }
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhoto3DSelect(true);
                    mState = OPTION_STATE_3D;
                    break;
                case R.id.photo_setting:
                    mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                    cancleDelayHide();
                    sbowPhotoSetting();
                    hideControlDelay();
                    mPhotoPlayerHolder.setPhotoSettingSelect(true);
                    mState = OPTION_STATE_SETTING;
                    break;
                default:
                    break;
            }
        }
    }

    private GifDecoder.IGifCallBack mGifCallBack = new GifDecoder.IGifCallBack() {
        public void onFrameIndexChanged(int index) {
            //Log.d(TAG, "GifDecoder.IGifCallBack: onFrameIndexChanged index = " + index);
        }

        public void onFinalFrame() {
            Log.d(TAG, "GifDecoder.IGifCallBack: onFinalFrame!!!");
            if (mPPTPlayer) {
                mHandler.sendEmptyMessageDelayed(PPT_PLAYER, 100);
            }
        }
    };

    private boolean isLargerThanLimit(BitmapFactory.Options options) {
        long pixSize = options.outWidth * options.outHeight;
        // largest pix is 1920 * 8 * 1080 * 8
        if (pixSize <= UPPER_BOUND_PIX) {
            return false;
        }
        return true;
    }

    private boolean isErrorPix(BitmapFactory.Options options) {
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            return true;
        }
        return false;
    }

    private void decodeGif(final String url) {

        String tmpPath = null;
        if (Tools.isSambaPlaybackUrl(url)) {
            tmpPath = Tools.convertToHttpUrl(url);
        } else {
            tmpPath = url;
        }
        final String realPath = tmpPath;
        Log.i(TAG,"Gif decode real path:"+realPath);

        File f = new File(realPath);
        if((!f.exists()) && (!Tools.isNetPlayback(realPath))) {
            Log.e(TAG,"file not exists or not http url");
            return;
        }
        ImagePlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCurrentPosition < 0 ) {
                    mCurrentPosition = 0;
                }
                mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                        + mPhotoFileList.get(mCurrentPosition).getName());
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isDecodeSuccess = true;
                if ((mSourceFrom != DLNAConstants.SOURCE_FROM_DLNA) && (!Tools.isNetPlayback(realPath))) {
                    isDecodeSuccess = mPhotoPlayerHolder.mSurfaceView.setSrc(realPath,
                            ImagePlayerActivity.this);
                } else {
                    isDecodeSuccess = mPhotoPlayerHolder.mSurfaceView.decodeBitmapFromNet(realPath,
                            ImagePlayerActivity.this);
                }

                if (isDecodeSuccess) {
                    mHandler.sendEmptyMessage(PHOTO_DECODE_FINISH);

                    if (mPhotoPlayerHolder.mSurfaceView.getFrameCount() > 1) {
                        mPhotoPlayerHolder.mSurfaceView.setStart(mGifCallBack);
                    } else {
                        if (mPPTPlayer) {
                            mHandler.sendEmptyMessageDelayed(PPT_PLAYER, slideTime);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * decode local picture.
     */
    private Bitmap decodeBitmapFromLocal(final String imagePath) {
        // file no found
        if (!Tools.isFileExist(imagePath)) {
            mCanResponse = true;
            return null;
        }
        Bitmap bitmap = null;
        /* BitmapFactory.Options */options = new BitmapFactory.Options();
        try {
            closeSilently(mFileInputStream);
            mFileInputStream = new FileInputStream(imagePath);
            FileDescriptor fd = mFileInputStream.getFD();
            if (fd == null) {
                closeSilently(mFileInputStream);
                decodeBitmapFailed(R.string.picture_decode_failed);
                return null;
            }
            // Plug disk, the following must be set to false.
            options.inPurgeable = false;
            options.inInputShareable = true;
            options.inJustDecodeBounds = true;
            if (!imagePath.substring(imagePath.lastIndexOf(".") + 1)
                    .equalsIgnoreCase(Constants.MPO)) {
                // options.forceNoHWDoecode = true;
            } else {
                isMPO = true;
            }
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            Log.d(TAG, "options " + options.outHeight + " " + options.outWidth);

            is4K2KMode = false;

            if (isLargerThanLimit(options)) {
                closeSilently(mFileInputStream);
                mCanResponse = true;
                Log.d(TAG, "**show default photo**");
                return setDefaultPhoto();
            }
            if (isErrorPix(options)) {
                closeSilently(mFileInputStream);
                mCanResponse = true;
                if (!isOnPause)
                    decodeBitmapFailed(R.string.picture_decode_failed);
                return null;
            }
            // options.forceNoHWDoecode = false;
            // According to the 1920 * 1080 high-definition format picture as
            // the restriction condition
            options.inSampleSize = computeSampleSizeLarger(options.outWidth, options.outHeight);

            Log.d(TAG, "options.inSampleSize : " + options.inSampleSize);
            options.inJustDecodeBounds = false;
            if (fd != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                // jpeg png gif use the open source third-party library，bmp is
                // decoded by skia
                // Open source third-party library have default exception
                // handling methods（In the exit will interrupt analytic，return
                // null
                if (bitmap != null) {
                    bitmap = resizeDownIfTooBig(bitmap, true);
                } else {
                    if (!isOnPause)
                        decodeBitmapFailed(R.string.picture_can_not_decode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (Constants.isExit) {
                return null;
            }
            try {
                closeSilently(mFileInputStream);
                mFileInputStream = new FileInputStream(imagePath);
                bitmap = BitmapFactory.decodeStream(mFileInputStream, null, options);
                if (bitmap == null) {
                    decodeBitmapFailed(R.string.picture_can_not_decode);
                    return setDefaultPhoto();
                }
            } catch (Exception error) {
                error.printStackTrace();
                decodeBitmapFailed(R.string.picture_can_not_decode);
                return setDefaultPhoto();
            } finally {
                closeSilently(mFileInputStream);
            }
        } finally {
            closeSilently(mFileInputStream);
        }
        mCanResponse = true;
        // ARGB_8888 is flexible and offers the best quality
        if (options != null && options.inPreferredConfig != Config.ARGB_8888) {
            return ensureGLCompatibleBitmap(bitmap);
        }
        return bitmap;
    }

    /**
     * decode net picture.
     */
    private Bitmap decodeBitmapFromNet(final String imagePath) {
        Bitmap bitmap = null;
        InputStream is = null;
        MyInputStream mIs = null;
        try {

            closeSilently(is);
            closeSilently(mIs);
            is = new URL(imagePath).openStream();
            if (is == null) {
                decodeBitmapFailed(R.string.picture_decode_failed);
                return null;
            }
            mIs = new MyInputStream(is, imagePath);
            /* BitmapFactory.Options */options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = true;
            if (!imagePath.substring(imagePath.lastIndexOf(".") + 1)
                    .equalsIgnoreCase(Constants.MPO)) {
                // options.forceNoHWDoecode = true; // Get the original
                // resolution
            } else {
                isMPO = true;
            }
            BitmapFactory.decodeStream(mIs, null, options);
            Log.d(TAG, "from dlna, options " + options.outHeight + " " + options.outWidth);
            if (Constants.isExit) {
                return null;
            }
            // Test the image's resolution.
            if (isLargerThanLimit(options)) {
                closeSilently(is);
                closeSilently(mIs);
                mCanResponse = true;
                return setDefaultPhoto();
            }
            if (isErrorPix(options)) {
                closeSilently(is);
                closeSilently(mIs);
                mCanResponse = true;
                if (!isOnPause)
                    decodeBitmapFailed(R.string.picture_decode_failed);
                return null;
            }
            // options.inSampleSize = 4;
            options.inSampleSize = computeSampleSizeLarger(options.outWidth, options.outHeight);
            options.inJustDecodeBounds = false;
            // options.forceNoHWDoecode = false;
            closeSilently(mIs);
            is = new URL(imagePath).openStream();
            if (is == null) {
                decodeBitmapFailed(R.string.picture_decode_failed);
                return null;
            }
            mIs = new MyInputStream(is, imagePath);
            Log.d(TAG, "mIs : " + mIs);
            Log.i(TAG, "*****mIs*******" + mIs.markSupported());
            bitmap = BitmapFactory.decodeStream(mIs, null, options);
            if (bitmap == null) {
                Log.d(TAG, "BitmapFactory.decodeStream return null");
                if (isOnPause) {
                    return null;
                }
                decodeBitmapFailed(R.string.picture_decode_failed);
            }
            closeSilently(is);
            closeSilently(mIs);
            mCanResponse = true;
            return bitmap;
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in decodeBitmap");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException in decodeBitmap");
            e.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(mIs);
        }
        if (Constants.isExit) {
            return null;
        }
        decodeBitmapFailed(R.string.picture_decode_failed);
        return null;
    }

    /**
     * Decode bitmap from local path or HTTP URL.
     *
     * @param url path of image.
     * @return bitmap with the specified URL or null.
     */
    public Bitmap decodeBitmap(final String url) {
        Log.d(TAG, "decodeBitmap, url : " + url);
        mCanResponse = false;
        isDefaultPhoto = false;
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA || Tools.isNetPlayback(url)) {
            return decodeBitmapFromNet(url);
        } else {
            return decodeBitmapFromLocal(url);
        }
    }

    public Bitmap setDefaultPhoto() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // Obtain resources pictures
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.default_sound_back);
        /*
         * InputStream in = null; try { in = getAssets().open("default_bg.jpg");
         * } catch (IOException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         */
        if (is == null) {
            return null;
        }
        /*
         * return ensureGLCompatibleBitmap(BitmapFactory.decodeStream(is, null,
         * opt));
         */
        isDefaultPhoto = true;
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public void showToastAtCenter(String text) {
        showToast(text, Gravity.CENTER, Toast.LENGTH_SHORT);
    }

    private void showToastAtBottom(String text) {
        showToast(text, Gravity.BOTTOM, Toast.LENGTH_SHORT);
    }

    private void showToast(final String text, int gravity, int duration) {
        Toast toast = ToastFactory.getToast(ImagePlayerActivity.this, text, gravity);
        toast.show();
    }

    private void closeSilently(final Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
        }
    }

    // Resize the bitmap if each side is >= targetSize * 2
    private Bitmap resizeDownIfTooBig(Bitmap bitmap, boolean recycle) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float widthScale = (float) mWindowResolutionWidth / srcWidth;
        float heightScale = (float) mWindowResolutionHeight / srcHeight;
        Log.d(TAG, "srcWidth : " + srcWidth + " srcHeight : " + srcHeight + " widthScale : " + widthScale + " heightScale:" + heightScale);

        return resizeBitmapByScale(bitmap, widthScale, heightScale, recycle);
    }

    private Bitmap resizeBitmapByScale(Bitmap bitmap, float widthScale, float heightScale, boolean recycle) {
        int width = Math.round(bitmap.getWidth() * widthScale);
        int height = Math.round(bitmap.getHeight() * heightScale);
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(widthScale, heightScale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) {
            bitmap.recycle();
        }
        return target;
    }

    private Config getConfig(Bitmap bitmap) {
        Config config = bitmap.getConfig();
        if (config == null) {
            config = Config.ARGB_8888;
        }
        return config;
    }

    // This function should not be called directly from
    // DecodeUtils.requestDecode(...), since we don't have the knowledge
    // if the bitmap will be uploaded to GL.
    private Bitmap ensureGLCompatibleBitmap(Bitmap bitmap) {
        Log.i(TAG, "***is**" + (bitmap == null) + (bitmap.getConfig() != null));
        if (bitmap == null || bitmap.getConfig() != null) {
            return bitmap;
        }
        Bitmap newBitmap = bitmap.copy(Config.ARGB_8888, false);
        bitmap.recycle();
        System.gc();
        Log.i(TAG, "***bitmap**" + (bitmap == null) + " " + (newBitmap == null));
        return newBitmap;
    }

    // This computes a sample size which makes the longer side at least
    // minSideLength long. If that's not possible, return 1.
    private int computeSampleSizeLarger(double w, double h) {
        double initialSize = Math.max(w / UPPER_BOUND_WIDTH_PIX, h / UPPER_BOUND_HEIGHT_PIX);
        if (initialSize <= 2.0f) {
            return 1;
        } else if (initialSize < 4.0f) {
            return 2;
        } else if (initialSize < 8.0f) {
            return 4;
        } else {
            return 8;
        }
    }

    // Returns the previous power of two.
    // Returns the input if it is already power of 2.
    // Throws IllegalArgumentException if the input is <= 0
    @SuppressWarnings("unused")
    private int prevPowerOf2(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        return Integer.highestOneBit(n);
    }

    /**
     * Show the pictures there is detailed information
     */
    private void showPhotoInfo() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        Log.d(TAG, "mCurrentPosition : " + mCurrentPosition + " size : " + mPhotoFileList.size());
        if (mCurrentPosition >= 0 && mCurrentPosition < mPhotoFileList.size()) {
            mPhotoInfoDialog = new PhotoInfoDialog(this, mPhotoFileList.get(mCurrentPosition));
            mPhotoInfoDialog.show();
        }
    }

    public void setCurrentPos(int delta) {
        int position = mCurrentPosition + delta;
        if (position <= -1) {
            position = mPhotoFileList.size() - 1;
        } else if (position >= mPhotoFileList.size()) {
            position = 0;
        }
        mCurrentPosition = position;
        mHandler.sendEmptyMessage(PHOTO_NAME_UPDATE);
    }

    public String getNextPhotoPath(int delta) {
        int position = mCurrentPosition + delta;
        if (mPhotoFileList.size() == 0)
            return null;
        if (position <= -1) {
            position = mPhotoFileList.size() - 1;
        } else if (position >= mPhotoFileList.size()) {
            position = 0;
        }
        String url = mPhotoFileList.get(position).getPath();
        return url;
    }

    public boolean checkIfItIsGifPhoto(final int delta){
        int position = mCurrentPosition + delta;
        if (position <= -1) {
            position = mPhotoFileList.size() - 1;
        } else if (position >= mPhotoFileList.size()) {
            position = 0;
        }
        String url = mPhotoFileList.get(position).getPath();
        url = Tools.fixPath(url);
        if (url.substring(url.lastIndexOf(".") + 1).equalsIgnoreCase(Constants.GIF)) {
            return true;
        }
        return false;
    }

    // Init relative parameter before call "showNextPhoto" function in photo seamless playback .
    public void initParameterBeforeShowNextPhoto(){
        mZoomTimes = 1.0f;
        mRotateAngle = 0f;
    }
    /**
     * play next or previous picture.
     *
     * @param delta
     */
    public void moveNextOrPrevious(final int delta) {
        mPhotoPlayerHolder.mSurfaceView.setStop();
        int position = mCurrentPosition + delta;
        if (position <= -1) {
            position = mPhotoFileList.size() - 1;
        } else if (position >= mPhotoFileList.size()) {
            position = 0;
        }
        // mZoomTimes should be initialized as 1.0f,
        // mZoomTimes>1.0f for zoomIn, mZoomTimes<1.0f for zoomOut.
        mZoomTimes = 1.0f;
        mRotateAngle = 0f;
        String url = mPhotoFileList.get(position).getPath();
        url = Tools.fixPath(url);
        if (url.substring(url.lastIndexOf(".") + 1).equalsIgnoreCase(Constants.GIF)) {
            mPhotoPlayerHolder.mSurfaceView.stopPlayback(false);
            decodeGif(url);
            mCurrentPosition = position;
            mCurrentView = GIF_VIEW;
        } else {
            mCurrentView = IMAGE_VIEW;
            // Currently Comment out setJustScanMode, because image player do not need it.
            // setJustScanMode();
            mCurrentPosition = position;
            if (mPhotoPlayerHolder.mSurfaceView.startNextVideo(url, ImagePlayerActivity.this)) {

            } else {
                showToastAtCenter(getString(R.string.busy_tip));
            }
        }
        /* if (mPPTPlayer) {
            mHandler.sendEmptyMessageDelayed(PPT_PLAYER, slideTime);
        } */
        mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                + mPhotoFileList.get(mCurrentPosition).getName());
    }

    /**
     * start play slide.
     */
    public void startPPT_Player() {
        if (mPPTPlayer) {
            mHandler.sendEmptyMessageDelayed(PPT_PLAYER, slideTime);
        }
    }

    /**
     * stop play slide.
     */
    private void stopPPTPlayer() {
        if (!mPPTPlayer) {
            return;
        }
        mPPTPlayer = false;// stop cycling.
        mHandler.removeMessages(PPT_PLAYER);
        if (/* mPhotoPlayerHolder.bt_photoPlay.isFocused() */mState == OPTION_STATE_PLAY) {
            mPhotoPlayerHolder.setPhotoPlaySelect(true, false);
        } else {
            mPhotoPlayerHolder.setPhotoPlaySelect(false, false);
        }
    }

    /**
     * exit player after Mhl event callback.
     */
    private void exitPlayer() {
        mPhotoPlayerHolder.mSurfaceView.stopPlayback(false);
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
    }

    /**
     * Display buffer progress.
     *
     * @param id
     */
    private void showProgressDialog(int id) {
        if (!isFinishing()) {
            mProgressDialog = new ACProgressFlower.Builder(this, R.style.NonDimACProgressDialog)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text(getString(id))
                    .textColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    /**
     * hide buffer progress.
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Log.i(TAG, "dismissProgressDialog");
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * show control
     */
    private void showController() {
        if (mPlayControllerLayout != null) {
            mPlayControllerLayout.setVisibility(View.VISIBLE);
            mPlayControllerLayout.setFocusable(true);
            mPlayControllerLayout.requestFocus();
            mIsControllerShow = true;
        } else {
            Log.d(TAG, "playControlLayout is null ptr===");
        }
    }

    /**
     * After how long hidden article control
     */
    public void hideControlDelay() {
        do {
            mHandler.removeMessages(ImagePlayerActivity.HIDE_PLAYER_CONTROL);
        } while (mHandler.hasMessages(ImagePlayerActivity.HIDE_PLAYER_CONTROL));
        mHandler.sendEmptyMessageDelayed(ImagePlayerActivity.HIDE_PLAYER_CONTROL, DEFAULT_TIMEOUT);
    }

    /**
     * Cancel time delay hidden.
     */
    private void cancleDelayHide() {
        mHandler.removeMessages(ImagePlayerActivity.HIDE_PLAYER_CONTROL);
        mPlayControllerLayout.setFocusable(true);
        mPlayControllerLayout.requestFocus();
    }

    /**
     * Hidden article control.
     */
    private void hideController() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        if (mPlayControllerLayout != null) {
            mPlayControllerLayout.setVisibility(View.INVISIBLE);
            mIsControllerShow = false;
        } else {
            Log.d(TAG, "playControlLayout is null ptr!!");
        }
    }

    // Network disconnection radio treatment
    BroadcastReceiver mNetDisconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "receive net disconnect msg...");
            if (mSourceFrom == Constants.SOURCE_FROM_SAMBA
                    || mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
                showToastAtCenter(getString(R.string.net_disconnect));
                stopPPTPlayer();
                closeSilently(mFileInputStream);
                ImagePlayerActivity.this.finish();
            }
        }
    };
    private BroadcastReceiver mSourceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"*******BroadcastReceiver**********" + intent.getAction());
            mIsSourceChange = true;
            mPhotoPlayerHolder.mSurfaceView.stopPlayback(false);
            ImagePlayerActivity.this.finish();
        }
    };

    private BroadcastReceiver mCastStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "------mcastStateChangeReceiver ---intent.getExtras(extraArg):" + intent.getStringExtra("extraArg"));
            if ("mairplay_playphoto".equalsIgnoreCase(intent.getStringExtra("extraArg"))) {
                mPhotoPlayerHolder.mSurfaceView.stopPlayback(true);
                ImagePlayerActivity.this.finish();
                Constants.isExit = true;
            }
        }
    };

    private class DiskChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String devicePath = intent.getDataString().substring(7);
            // Disk remove
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                if (mPhotoFileList.get(0).getPath().contains(devicePath)) {
                    showToastAtCenter(getString(R.string.disk_eject));
                    // Close file resources
                    closeSilently(mFileInputStream);
                    ImagePlayerActivity.this.finish();
                }
            }
        }
    }

/*
    private BroadcastReceiver homeKeyEventBroadCastReceiver = new BroadcastReceiver() {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    String hwName = Tools.getHardwareName();
                    if (!hwName.equals("messi") && reason.equals(SYSTEM_HOME_KEY)) {
                        Log.i(TAG, "SYSTEM_HOME_KEY");
                        isSettingWallPaper = false;
                        mPhotoPlayerHolder.mSurfaceView.stopPlayback(true);
                        // Currently Comment out setDefaultMode, because image player do not need setJustScanMode.
                        // setDefaultMode();
                        Constants.isExit = true;
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        // long home key
                    }
                }
            }
        }
    };
*/

    private void decodeBitmapFailed(final int id) {
        mCanResponse = true;
        ImagePlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastAtCenter(getString(id));
            }
        });
    }

    private boolean hasScaleOrRotate() {
        Log.i(TAG, "---- hasScaleOrRotate ---- mRotateAngle:" + mRotateAngle);
        if ((360 == mRotateAngle) || (-360 == mRotateAngle)) {
            mRotateAngle = 0;
        }
        if ((mRotateAngle == 0) && (mZoomTimes == 1.0f)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isPhotoParsing() {
        if (mPhotoPlayerHolder.mSurfaceView.getImagePlayerThread() != null) {
            if (mPhotoPlayerHolder.mSurfaceView.getImagePlayerThread().isAlive()) {
                if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                } else if (mProgressDialog == null) {
                    showProgressDialog(R.string.picture_decoding);
                }
                return true;
            }
        }
        return false;
    }
}
