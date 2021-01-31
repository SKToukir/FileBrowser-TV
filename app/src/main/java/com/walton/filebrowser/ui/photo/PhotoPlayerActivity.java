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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mstar.android.MDisplay;
import com.mstar.android.MDisplay.PanelMode;
import com.mstar.android.tv.TvCommonManager;
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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PhotoPlayerActivity extends Activity {
    private static final String TAG = "PhotoPlayerActivity";

    private static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";

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

    private static final int SHOW_TOAST = 0xc;

    private static final int PHOTO_3D = 0xd;

    private static final int PHOTO_DECODE_PROPRESS = 0xe;

    private static final int PHOTO_DECODE_FINISH = 0xf;

    private static final int PLAY_FIRST_PHOTO = 0xa;

    private static final int PLAY_4K2K_PHOTO = 0xab;

    private static final int RESTORE_CURRENT_VIEW = 0xba;

    protected static final int PHOTO_PLAY_SETTING = 0x9;

    protected static final int SWITCH_VIEW = 0x25;

    // Hidden article control
    private static final int HIDE_PLAYER_CONTROL = 0x10;

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

    // Picture player all control container
    private PhotoPlayerViewHolder mPhotoPlayerHolder;

    // control view
    private LinearLayout mPlayControllerLayout;

    // Picture rotates buffer
    private RotateBitmap mBitmapCache = new RotateBitmap(null);

    // all photo file
    private List<BaseData> mPhotoFileList = new ArrayList<BaseData>();
    BaseData baseData;
    // Picture detailed information Dialog
    private PhotoInfoDialog mPhotoInfoDialog;

    // Video buffer progress bar
    private ACProgressFlower mProgressDialog;

    // Disk pull plug monitor
    private DiskChangeReceiver mDiskChangeReceiver;

    private FileInputStream mFileInputStream = null;

    private InputStream is = null;

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

    // index in list
    private int mCurrentPosition = 0;

    // Picture enlarge or reduce The Times
    private int mZoomTimes = 0;

    // screen resolution
    private int mWindowWidth = 0;

    private int mWindowHeight = 0;

    private int mCurrentView = IMAGE_VIEW;

    private int mPreCurrentView = IMAGE_VIEW;

    // The current broadcast document source, local or network
    private int mSourceFrom;

    // Image analytical thread
    private static Thread mThread = new Thread();

    private static BitmapFactory.Options options;

    // private boolean isExit = false;

    private boolean isShowMsg = false;

    private boolean isOnPause = false;

    // Only MPO format images can switch 3d
    private boolean isMPO = false;

    private boolean isAnimationOpened = false;

    private int slideTime = 3000;

    // 4K2K photo decode so slow, so define slide4K2KTime for 4K2K photo play
    private int slide4K2KTime = 5000;

    private boolean fristShowPicture = true;

    private int animationArray[] = { R.anim.photo_scale_translate_left_bottom,
            R.anim.photo_scale_translate_left_top,
            R.anim.photo_scale_translate_right_top,
            R.anim.photo_scale_translate_right_bottom,
            R.anim.photo_push_down_in, R.anim.photo_push_left_in,
            R.anim.photo_push_right_in, R.anim.photo_push_up_in,
            R.anim.photo_zoom_enter, R.anim.photo_hyperspace_out,
            R.anim.photo_zoom_exit, R.anim.photo_wave_scale };

    private int animationNum;

    protected boolean isDefaultPhoto = false;

    private String str4K2KPhotoPath = "";

    // processing images play and pause
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case PPT_PLAYER: // Slide mode play pictures
                if (mPPTPlayer) {
                    moveNextOrPrevious(1);
                }
                break;
            case SHOW_TOAST:
                showToastAtBottom("Set wallpaper Success!");
                break;
            case PHOTO_3D:
                /*
                 * mPhotoPlayerHolder.mSurfaceView.setImagePath(mPhotoFileList
                 * .get( mCurrentPosition).getPath());
                 * mPhotoPlayerHolder.mSurfaceView.drawImage(); if (mPPTPlayer)
                 * { mHandler.sendEmptyMessageDelayed(PPT_PLAYER, 4000); }
                 */
                mPhotoPlayerHolder.mSurfaceView.updateView();
                break;
            case PHOTO_DECODE_PROPRESS:
                mPhotoPlayerHolder.aviPhoto.smoothToShow();
//                dismissProgressDialog();
//                showProgressDialog(R.string.picture_decoding);
                break;
            case PHOTO_DECODE_FINISH:
                mPhotoPlayerHolder.aviPhoto.smoothToHide();
//                dismissProgressDialog();
                break;
            case HIDE_PLAYER_CONTROL:
                hideController();
                break;
            case PLAY_FIRST_PHOTO:
                playFirstPhoto();
                break;
            case PHOTO_PLAY_SETTING:
                Bundle mBundle = msg.getData();
                isAnimationOpened = mBundle.getBoolean("isOpen");
                slideTime = mBundle.getInt("time");
                SharedPreferences mShared = getSharedPreferences("photoPlayerInfo", Context.MODE_PRIVATE);
                Editor editor = mShared.edit();
                editor.putBoolean("isAnimationOpened", isAnimationOpened);
                editor.commit();
                break;
            case PLAY_4K2K_PHOTO:
                Play4K2KPhoto();
                break;
            case RESTORE_CURRENT_VIEW:
                RestoreCurrentView();
                break;
            case Constants.HANDLE_MESSAGE_PLAYER_EXIT:
                PhotoPlayerActivity.this.finish();
                break;
            case SWITCH_VIEW:
                if (mCurrentView != IMAGE_VIEW) {
                    mPhotoPlayerHolder.mScrollView.setCurrentView(IMAGE_VIEW);
                    mCurrentView = IMAGE_VIEW;
                }
                break;
            default:
                break;
            }
        }
    };

    // for 4K2K
    protected static boolean is4K2KMode = false;
    protected static boolean panel4k2kmode = false;
    protected static boolean b4k2kenable = SystemProperties.getBoolean("mstar.4k2k.photo", false);
    private boolean bDecodeRet = false;

    private boolean Play4K2KPhoto() {
        Log.i(TAG, "******Play4K2KPhoto******");
        // move long-running opration to thread
        // mPhotoPlayerHolder.mSurfaceView4K2K.destroyDrawingCache();
        // if (mPhotoPlayerHolder.mSurfaceView4K2K.setImagePath(str4K2KPhotoPath) == true)
        if (bDecodeRet) {
            Log.i(TAG, "******Draw 4k2k photo img******");
            str4K2KPhotoPath = "";
            mPreCurrentView = mCurrentView;
            mCurrentView = SURFACE4K2K_VIEW;
            mPhotoPlayerHolder.mScrollView.setCurrentView(SURFACE4K2K_VIEW);
            mPhotoPlayerHolder.mSurfaceView4K2K.drawImage();
            mPhotoPlayerHolder.mSurfaceView4K2K
                .setVisibility(View.VISIBLE);
            dismissProgressDialog();
            Log.i(TAG, "******play 4k2k photo finished******");
            return true;
        }
        else
            return false;
    }

    private boolean RestoreCurrentView()
    {
        if(mCurrentView == SURFACE4K2K_VIEW)
        {
            mCurrentView = mPreCurrentView;
            mPhotoPlayerHolder.mScrollView.setCurrentView(mCurrentView);
            return true;
        }
        else
            return false;
    }

    ///////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(R.layout.photo_show);

        mCurrentPosition = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        mSourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
             mPhotoFileList = getIntent().getParcelableArrayListExtra(Constants.BUNDLE_LIST_KEY);
        } else {
             mPhotoFileList = MediaContainerApplication.getInstance().getMediaData(Constants.FILE_TYPE_PICTURE);
        }
        // Log.i(TAG, "============panel Mode is :" + MDisplay.getPanelMode());

        findView();
        mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
        mState = OPTION_STATE_PLAY;
        mPhotoPlayerHolder.mImageView.setEnableTrackballScroll(true);
        mCurrentPosition = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY,
                0);
        mSourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
            mPhotoFileList = getIntent().getParcelableArrayListExtra(
                    Constants.BUNDLE_LIST_KEY);
        } else {
            // get all cache photo files
            mPhotoFileList = MediaContainerApplication.getInstance()
                    .getMediaData(Constants.FILE_TYPE_PICTURE);
        }
        mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                + mPhotoFileList.get(mCurrentPosition).getName());
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mWindowWidth = point.x; // display.getWidth();
        mWindowHeight = point.y;
        PhotoImageViewClickListener listener = new PhotoImageViewClickListener();
        mPhotoPlayerHolder.setOnClickListener(listener);
        // switch source monitor
        IntentFilter sourceChange = new IntentFilter(ACTION_CHANGE_SOURCE);
        this.registerReceiver(mSourceChangeReceiver, sourceChange);
        SharedPreferences mShared = getSharedPreferences("photoPlayerInfo", Context.MODE_PRIVATE);
        isAnimationOpened = mShared.getBoolean("isAnimationOpened", false);
        if (!Tools.unSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(
                    new OnMhlEventListener() {
                        @Override
                        public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                            Log.d(TAG, "onKeyInfo");
                            return false;
                        }
                        @Override
                        public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                            Log.d(TAG, "onAutoSwitch");
                            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI3);
                            exitPlayer();
                            ComponentName componentName = new ComponentName("mstar.tvsetting.ui", "mstar.tvsetting.ui.RootActivity");
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.setComponent(componentName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            intent.putExtra("task_tag", "input_source_changed");
                            PhotoPlayerActivity.this.startActivity(intent);
                            finish();
                            return false;
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        stopPPTPlayer();
        // photoPlayerHolder.mSurfaceView.setNormal();
        mPhotoPlayerHolder.mImageView.setImageDrawable(null);
        cleanBitmapArray();
        unregisterReceiver(mSourceChangeReceiver);
        Log.d(TAG, "********onDestroy*******");
        Constants.isExit = true;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mCurrentView == SURFACE_VIEW) {
            mPhotoPlayerHolder.mSurfaceView.setNormal();
        }
        mPhotoPlayerHolder.mImageView.setImageDrawable(null);
        mPhotoPlayerHolder.mImageView.clear();
        mPhotoPlayerHolder.mImageView.resetRotateCounter();
        mPhotoPlayerHolder.mGifView.setStop();
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (is4K2KMode) {
            is4K2KMode = false;
            panel4k2kmode = false;
            mCurrentView = IMAGE_VIEW;
            mPhotoPlayerHolder.mScrollView.setCurrentView(IMAGE_VIEW);
            MDisplay.setPanelMode(PanelMode.E_PANELMODE_NORMAL);
            this.finish();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "********onPause*******" + Constants.isExit);
        isOnPause = true;
        if (mThread.isAlive() && options != null) {
            options.requestCancelDecode();
        }
        //this will cause play 3D photo lose proportion and 3D effect when 'Resouce' key pressed, so delete it
        //if (mCurrentView == SURFACE_VIEW) {
            //mPhotoPlayerHolder.mSurfaceView.setNormal();
        //}

        // Began to disk scan
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.startMediascanner(PhotoPlayerActivity.this);
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
    protected void onStart() {
        super.onStart();

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
        IntentFilter networkIntentFilter = new IntentFilter(
                "com.mstar.localmm.network.disconnect");
        registerReceiver(mNetDisconnectReceiver, networkIntentFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.stopMediascanner(PhotoPlayerActivity.this);
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
            playFirstPhoto();
        } else {
            showController();
        }
        animationNum = animationArray.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!mIsControllerShow) {
                showController();
                hideControlDelay();
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        Log.i(TAG, "********onTouchEvent*******" + event.getAction());
        return super.onTouchEvent(event);
    }

    /**
     * play the first picture
     */
    private void playFirstPhoto() {
        if (Constants.isExit || mCurrentPosition < 0
                || mCurrentPosition >= mPhotoFileList.size()) {
            Log.i(TAG, "******playFirstPhoto*****" + Constants.isExit);
            return;
        }

        if (mThread.isAlive()) {
            Log.i(TAG, "****playFirstPhoto****mThread.isAlive()");

            if (mSourceFrom != DLNAConstants.SOURCE_FROM_DLNA) {
                options.requestCancelDecode();
                if (isShowMsg) {
                    mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
                    isShowMsg = false;
                }
                mHandler.sendEmptyMessageDelayed(PLAY_FIRST_PHOTO, 500);
                return;
            }
        }

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = mPhotoFileList.get(mCurrentPosition).getPath();
                if (url.substring(url.lastIndexOf(".") + 1).equalsIgnoreCase(
                        Constants.GIF)
                /* && mSourceFrom != DLNAConstants.SOURCE_FROM_DLNA */) {
                    decodeGif(url);
                    return;
                } else {
                    if (!initBitmap()) {
                        return;
                    }
                    PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCurrentView != IMAGE_VIEW) {
                                mPhotoPlayerHolder.mScrollView
                                        .setCurrentView(IMAGE_VIEW);
                                mCurrentView = IMAGE_VIEW;
                            }
                            mPhotoPlayerHolder.mImageView
                                    .setImageRotateBitmapResetBase(
                                            mBitmapCache, false,
                                            PhotoPlayerActivity.this);
                        }
                    });
                }
            }
        });
        mThread.start();
        // startPhotoAnimation();
    }

    /**
     * initialize bitmap for show
     */
    private boolean initBitmap() {
        Bitmap tempBitmap = null;
        if (mBitmapCache != null) {
            mBitmapCache.recycle();
            System.gc();
        }
        // Analysis the first show picture
        int size = mPhotoFileList.size();
        if (mCurrentPosition >= 0 && mCurrentPosition < size) {
            if (isShowMsg) {
                mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
            }
            isMPO = false;
            tempBitmap = decodeBitmap(mPhotoFileList.get(mCurrentPosition)
                    .getPath());
            /*Show 4k2k photo*/
            if (is4K2KMode == true && tempBitmap == null) {
                Log.i(TAG, "******Draw 4k2k photo******");
                if (isShowMsg == false) {
                    isShowMsg = true;
                    mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
                }
                if (panel4k2kmode == false) {
                    panel4k2kmode = true;
                    MDisplay.setPanelMode(PanelMode.E_PANELMODE_4K2K_15HZ);
                }
                mPhotoPlayerHolder.mSurfaceView4K2K.destroyDrawingCache();
                bDecodeRet = mPhotoPlayerHolder.mSurfaceView4K2K.setImagePath(str4K2KPhotoPath);
                mHandler.sendEmptyMessageDelayed(PLAY_4K2K_PHOTO, 100);
                return true;
            } else if (panel4k2kmode == true) {
                panel4k2kmode = false;
                MDisplay.setPanelMode(PanelMode.E_PANELMODE_NORMAL);
                mHandler.sendEmptyMessage(RESTORE_CURRENT_VIEW);
            }
            isShowMsg = true;
            Log.i(TAG, "******tempBitmap******" + tempBitmap);
            if (Constants.isExit
                    || (PhotoPlayerActivity.this != null && !PhotoPlayerActivity.this
                            .isFinishing())) {
                if (mCurrentView == IMAGE_VIEW) {
                    Log.i(TAG, "******mBitmapCache.setBitmap******"
                            + tempBitmap);
                    mBitmapCache.setBitmap(tempBitmap);
                } else if (mCurrentView == SURFACE_VIEW) {
                    mPhotoPlayerHolder.mSurfaceView.setBitmap(tempBitmap);
                    mHandler.sendEmptyMessage(PHOTO_3D);
                }
                mHandler.sendEmptyMessage(PHOTO_DECODE_FINISH);
                tempBitmap = null;
                System.gc();
                return true;
            } else {
                Log.i(TAG, "********isFinishing*********");
                tempBitmap = null;
                System.gc();
                return false;
            }
        }
        return false;
    }

    /**
     * clean all bitmapArray.
     */
    protected void cleanBitmapArray() {
        if (mBitmapCache != null)
            mBitmapCache.recycle();
    }

    /**
     * show module
     */
    private void findView() {
        mPlayControllerLayout = (LinearLayout) findViewById(R.id.photo_suspension_layout);
        mPhotoPlayerHolder = new PhotoPlayerViewHolder(this);
        mPhotoPlayerHolder.findViews();
        hideControlDelay();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // dispatch source key event
        if (keyCode == KeyEvent.KEYCODE_TV_INPUT) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "*********onKeyUp*********" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPhotoFileList.clear();
            if (is4K2KMode) {
                if (mPlayControllerLayout != null) {
                    mPlayControllerLayout.setVisibility(View.INVISIBLE);
                    mIsControllerShow = false;
                }
                mPhotoPlayerHolder.mSurfaceView4K2K
                        .setVisibility(View.INVISIBLE);

                is4K2KMode = false;
                panel4k2kmode = false;
                MDisplay.setPanelMode(PanelMode.E_PANELMODE_NORMAL);
                this.finish();
            }

            if (mThread.isAlive()) {
                mHandler.sendEmptyMessageDelayed(
                        Constants.HANDLE_MESSAGE_PLAYER_EXIT, 1000);
            } else {
                this.finish();
            }
            return true;
        }
        if (!mCanResponse) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
            return true;
        }
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return true;
        }
        if (mIsControllerShow) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                cancleDelayHide();
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                moveNextOrPrevious(-1);
                hideControlDelay();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                cancleDelayHide();
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                moveNextOrPrevious(1);
                hideControlDelay();
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
                cancleDelayHide();
                registerListeners();
                hideControlDelay();
                break;
            /*
             * case KeyEvent.KEYCODE_BACK: mPhotoFileList.clear();
             * if(mThread.isAlive()){
             * mHandler.sendEmptyMessageDelayed(Constants.
             * HANDLE_MESSAGE_PLAYER_EXIT, 1000); }else{ this.finish(); } break;
             */
            case KeyEvent.KEYCODE_MENU:
                mPhotoPlayerHolder.mImageView
                        .setMoveFlag(!mPhotoPlayerHolder.mImageView
                                .getMoveFlag());
                break;
            default:
                System.err.println("default is click!!");
                break;
            }
        } else {
            switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                showController();
                hideControlDelay();
                break;
            /*
             * case KeyEvent.KEYCODE_BACK: mPhotoFileList.clear();
             * if(mThread.isAlive()){
             * mHandler.sendEmptyMessageDelayed(Constants.
             * HANDLE_MESSAGE_PLAYER_EXIT, 1000); }else{ this.finish(); } break;
             */
            }
        }
        return true;
    }

    /**
     * The remote control left-hand button, and focus switching
     */
    private void drapLeft() {
        switch (mState) {
        case OPTION_STATE_PRE:
            mState = OPTION_STATE_SETTING;
            mPhotoPlayerHolder.setPhotoPreSelect(false);
            mPhotoPlayerHolder.setPhotoSettingSelect(true);
            break;
        case OPTION_STATE_PLAY:
            mState = OPTION_STATE_PRE;
            mPhotoPlayerHolder.setPhotoPlaySelect(false, mPPTPlayer);
            mPhotoPlayerHolder.setPhotoPreSelect(true);
            break;
        case OPTION_STATE_NEXT:
            mState = OPTION_STATE_PLAY;
            mPhotoPlayerHolder.setPhotoNextSelect(false);
            mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
            break;
        case OPTION_STATE_ENL:
            mState = OPTION_STATE_NEXT;
            mPhotoPlayerHolder.setPhotoEnlargeSelect(false);
            mPhotoPlayerHolder.setPhotoNextSelect(true);
            break;
        case OPTION_STATE_NARROW:
            mState = OPTION_STATE_ENL;
            mPhotoPlayerHolder.setPhotoNarrowSelect(false);
            mPhotoPlayerHolder.setPhotoEnlargeSelect(true);
            break;
        case OPTION_STATE_TURNLEFT:
            mState = OPTION_STATE_NARROW;
            mPhotoPlayerHolder.setPhotoTurnLeftSelect(false);
            mPhotoPlayerHolder.setPhotoNarrowSelect(true);
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
        case OPTION_STATE_SETTING:
            mState = OPTION_STATE_3D;
            mPhotoPlayerHolder.setPhotoSettingSelect(false);
            mPhotoPlayerHolder.setPhoto3DSelect(true);
            break;
        }
    }

    /**
     * The remote control right-hand button, and focus switching
     */
    private void drapRight() {
        Log.d(TAG, "drapRight: Right");
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
//        case OPTION_STATE_WALLPAPER:
//            mState = OPTION_STATE_3D;
//            mPhotoPlayerHolder.setPhotoWallpaperSelect(false);
//            mPhotoPlayerHolder.setPhoto3DSelect(true);
//            break;
//        case OPTION_STATE_3D:
//            mState = OPTION_STATE_SETTING;
//            mPhotoPlayerHolder.setPhoto3DSelect(false);
//            mPhotoPlayerHolder.setPhotoSettingSelect(true);
//            break;
//        case OPTION_STATE_SETTING:
//            mState = OPTION_STATE_PRE;
//            mPhotoPlayerHolder.setPhotoSettingSelect(false);
//            mPhotoPlayerHolder.setPhotoPreSelect(true);
//            break;
        }
    }

    private void PlayProcess() {
        if (mCurrentView != IMAGE_VIEW && mCurrentView != GIF_VIEW && mCurrentView != SURFACE4K2K_VIEW) {
            showToastAtBottom(getString(R.string.photo_3D_toast));
            return;
        }
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
                mCurrentPosition = -1;
                mHandler.sendEmptyMessage(PPT_PLAYER);
            }
        }
        mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
    }

    private void zoomIn() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mZoomTimes >= 3) {
            return;
        }
        mZoomTimes++;
        mPhotoPlayerHolder.mImageView.zoomIn();
    }

    private void zoomOut() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mZoomTimes <= -3) {
            return;
        }
        mZoomTimes--;
        mPhotoPlayerHolder.mImageView.zoomOut();
    }

    private void rotateImageLeft() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        mPhotoPlayerHolder.mImageView.rotateImageLeft(mZoomTimes);
        mZoomTimes = 0;
    }

    private void rotateImageRight() {
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        mPhotoPlayerHolder.mImageView.rotateImageRight(mZoomTimes);
        mZoomTimes = 0;
    }

    /**
     * Each button response events
     */
    private void registerListeners() {
        switch (mState) {
        case OPTION_STATE_PRE:
            if (mPPTPlayer) {
                stopPPTPlayer();
            }
            moveNextOrPrevious(-1);
            break;
        case OPTION_STATE_PLAY:
            PlayProcess();
            break;
        case OPTION_STATE_NEXT:
            if (mPPTPlayer) {
                stopPPTPlayer();
            }
            moveNextOrPrevious(1);
            break;
        case OPTION_STATE_ENL:
            if (mCurrentView == IMAGE_VIEW) {
                zoomIn();
            } else if (mCurrentView == GIF_VIEW) {
                showToastAtBottom(getString(R.string.photo_GIF_toast));
            } else {
                showToastAtBottom(getString(R.string.photo_3D_toast));
            }
            break;
        case OPTION_STATE_NARROW:
            if (mCurrentView == IMAGE_VIEW) {
                zoomOut();
            } else if (mCurrentView == GIF_VIEW) {
                showToastAtBottom(getString(R.string.photo_GIF_toast));
            } else {
                showToastAtBottom(getString(R.string.photo_3D_toast));
            }
            break;
        case OPTION_STATE_TURNLEFT:
            if (mCurrentView == IMAGE_VIEW) {
                rotateImageLeft();
            } else if (mCurrentView == GIF_VIEW) {
                showToastAtBottom(getString(R.string.photo_GIF_toast));
            } else {
                showToastAtBottom(getString(R.string.photo_3D_toast));
            }
            break;
        case OPTION_STATE_TURNRIGHT:
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
            } else if (mCurrentView == SURFACE_VIEW){
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
            setPhotoThreeD();
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
        PhotoSettingDialog mPhotoSettingDialog = new PhotoSettingDialog(this,
                slideTime, isAnimationOpened, mHandler);
        mPhotoSettingDialog.show();
    }

    /**
     * MPO switch between 2D and 3D
     */
    private void setPhotoThreeD() {
        if (mThread.isAlive()) {
            return;
        }
        if (!isMPO) {
            showToastAtBottom(getString(R.string.picture_can_not_set3d));
            return;
        }
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (mCurrentView == IMAGE_VIEW) {
            hideController();
            mCurrentView = SURFACE_VIEW;
            mPhotoPlayerHolder.mImageView.setImageDrawable(null);
            cleanBitmapArray();
            mPhotoPlayerHolder.mImageView.clear();
            mPhotoPlayerHolder.mImageView.resetRotateCounter();
            mZoomTimes = 0;
            /*
             * new Thread(new Runnable() {
             *
             * @Override public void run() {
             * mPhotoPlayerHolder.mSurfaceView.setImagePath(mPhotoFileList
             * .get(mCurrentPosition).getPath());
             * PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
             *
             * @Override public void run() {
             * mPhotoPlayerHolder.mSurfaceView.set3D();
             * mPhotoPlayerHolder.mSurfaceView.drawImage();
             * mPhotoPlayerHolder.mScrollView .setCurrentView(mCurrentView); }
             * }); } });
             */
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    initBitmap();
                    PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPhotoPlayerHolder.mSurfaceView.set3D();
                            mPhotoPlayerHolder.mSurfaceView.drawImage();
                        }
                    });
                }
            });
            mThread.start();
            // mPhotoPlayerHolder.mSurfaceView.setImagePath(mPhotoFileList.get(
            // mCurrentPosition).getPath());
            mPhotoPlayerHolder.mScrollView.setCurrentView(mCurrentView);
        } else if (mCurrentView == SURFACE_VIEW) {
            mCurrentView = IMAGE_VIEW;
            mPhotoPlayerHolder.mSurfaceView.setNormal();
            mPhotoPlayerHolder.mSurfaceView.destroyDrawingCache();
            mPhotoPlayerHolder.mSurfaceView.cleanView(mWindowWidth,
                    mWindowHeight);
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    initBitmap();
                    PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPhotoPlayerHolder.mImageView
                                    .setImageRotateBitmapResetBase(
                                            mBitmapCache, false,
                                            PhotoPlayerActivity.this);
                        }
                    });
                }
            });
            mThread.start();
            // initBitmap();
            mPhotoPlayerHolder.mScrollView.setCurrentView(mCurrentView);
        }
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
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCanSetWallpaper = false;
                                        final String fullPath = mPhotoFileList
                                                .get(mCurrentPosition)
                                                .getPath();
                                        final WallpaperManager wpm = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
                                        final Bitmap bitmap = decodeBitmap(fullPath);
                                        if (bitmap == null) {
                                            Log.e(TAG,
                                                    "Couldn't get bitmap for path!!");
                                        } else {
                                            try {
                                                // wpm.suggestDesiredDimensions(1920,
                                                // 1280);
                                                wpm.setBitmap(bitmap);
                                                mHandler.sendEmptyMessage(SHOW_TOAST);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Log.e(TAG,
                                                        "Failed to set wallpaper.");
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
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_VISIBLE);
                return;
            }
            if (!mCanResponse) {
                return;
            }
            switch (v.getId()) {
            case R.id.player_previous:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                moveNextOrPrevious(-1);
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoPreSelect(true);
                mState = OPTION_STATE_PRE;
                break;
            case R.id.photo_play:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                PlayProcess();
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoPlaySelect(true, mPPTPlayer);
                mState = OPTION_STATE_PLAY;
                break;
            case R.id.photo_next:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mPPTPlayer) {
                    stopPPTPlayer();
                }
                moveNextOrPrevious(1);
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoNextSelect(true);
                mState = OPTION_STATE_NEXT;
                break;
            case R.id.photo_enlarge:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mCurrentView == IMAGE_VIEW) {
                    zoomIn();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoEnlargeSelect(true);
                mState = OPTION_STATE_ENL;
                break;
            case R.id.photo_narrow:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mCurrentView == IMAGE_VIEW) {
                    zoomOut();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoNarrowSelect(true);
                mState = OPTION_STATE_NARROW;
                break;
            case R.id.photo_turn_left:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mCurrentView == IMAGE_VIEW) {
                    rotateImageLeft();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
                hideControlDelay();
                mPhotoPlayerHolder.setPhotoTurnLeftSelect(true);
                mState = OPTION_STATE_TURNLEFT;
                break;
            case R.id.photo_turn_right:
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                if (mCurrentView == IMAGE_VIEW) {
                    rotateImageRight();
                } else if (mCurrentView == GIF_VIEW) {
                    showToastAtBottom(getString(R.string.photo_GIF_toast));
                } else {
                    showToastAtBottom(getString(R.string.photo_3D_toast));
                }
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
                } else if (mCurrentView == SURFACE_VIEW){
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
                mPhotoPlayerHolder.setAllUnSelect(mPPTPlayer, mIsPlaying);
                cancleDelayHide();
                setPhotoThreeD();
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
        mHandler.sendEmptyMessage(PHOTO_DECODE_PROPRESS);
        PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCurrentView != GIF_VIEW) {
                    mPhotoPlayerHolder.mScrollView.setCurrentView(GIF_VIEW);
                    mCurrentView = GIF_VIEW;
                }
                mPhotoPlayerHolder.photo_name
                        .setText(getString(R.string.current_pic)
                                + mPhotoFileList.get(mCurrentPosition)
                                        .getName());
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isDecodeSuccess = true;
                if (mSourceFrom != DLNAConstants.SOURCE_FROM_DLNA) {
                    isDecodeSuccess = mPhotoPlayerHolder.mGifView.setSrc(url,
                            PhotoPlayerActivity.this);
                } else {
                    isDecodeSuccess = mPhotoPlayerHolder.mGifView
                            .decodeBitmapFromNet(url, PhotoPlayerActivity.this);
                }

                if (isDecodeSuccess) {
                    mHandler.sendEmptyMessage(PHOTO_DECODE_FINISH);

                    if (mPhotoPlayerHolder.mGifView.getFrameCount() > 1) {
                        mPhotoPlayerHolder.mGifView.setStart(mGifCallBack);
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
            if (b4k2kenable && (options.outHeight >= 2160 && options.outWidth >= 3840)
                && mPhotoPlayerHolder.mSurfaceView4K2K.checkImageSize(options)) {
                str4K2KPhotoPath = imagePath;
                is4K2KMode = true;
                closeSilently(mFileInputStream);
                mCanResponse = true;
                return null;
            }

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
            options.inSampleSize = computeSampleSizeLarger(options.outWidth,
                    options.outHeight);

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
                    bitmap = resizeDownIfTooBig(bitmap, mWindowWidth, true);
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
                bitmap = BitmapFactory.decodeStream(mFileInputStream, null,
                        options);
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
            /*
             * HttpURLConnection conn = (HttpURLConnection) (new
             * URL(imagePath)).openConnection(); conn.setDoInput(true);
             * conn.connect(); is = conn.getInputStream();
             */
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
            Log.d(TAG, "from dlna, options " + options.outHeight + " "
                    + options.outWidth);
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
            // Log.i(TAG, "*****is*******" + is.markSupported());
            /*
             * is = new URL(imagePath).openStream(); // is =
             * conn.getInputStream(); if (is == null) {
             * decodeBitmapFailed(R.string.picture_decode_failed); return null;
             * }
             */
            // options.inSampleSize = 4;
            options.inSampleSize = computeSampleSizeLarger(options.outWidth,
                    options.outHeight);
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
     * @param url
     *            path of image.
     * @return bitmap with the specified URL or null.
     */
    public Bitmap decodeBitmap(final String url) {
        Log.d(TAG, "decodeBitmap, url : " + url);
        mCanResponse = false;
        isDefaultPhoto = false;
        if (mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
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
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.file_icon_picture);
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
        Toast toast = ToastFactory.getToast(PhotoPlayerActivity.this, text, gravity);
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
    private Bitmap resizeDownIfTooBig(Bitmap bitmap, int targetSize,
                                      boolean recycle) {
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float scale = Math.max((float) targetSize / srcWidth,
                (float) targetSize / srcHeight);
        Log.d(TAG, "srcWidth : " + srcWidth + " srcHeight : " + srcHeight
                + " scale : " + scale);
        if (scale > 0.5f) {
            return bitmap;
        }
        return resizeBitmapByScale(bitmap, scale, recycle);
    }

    private Bitmap resizeBitmapByScale(Bitmap bitmap, float scale,
                                       boolean recycle) {
        int width = Math.round(bitmap.getWidth() * scale);
        int height = Math.round(bitmap.getHeight() * scale);
        if (width == bitmap.getWidth() && height == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap target = Bitmap.createBitmap(width, height, getConfig(bitmap));
        Canvas canvas = new Canvas(target);
        canvas.scale(scale, scale);
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
        double initialSize = Math.max(w / UPPER_BOUND_WIDTH_PIX, h
                / UPPER_BOUND_HEIGHT_PIX);
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
        Log.d(TAG, "mCurrentPosition : " + mCurrentPosition + " size : "
                + mPhotoFileList.size());
        if (mCurrentPosition >= 0 && mCurrentPosition < mPhotoFileList.size()) {
            mPhotoInfoDialog = new PhotoInfoDialog(this,
                    mPhotoFileList.get(mCurrentPosition));
            mPhotoInfoDialog.show();
        }
    }

    /**
     * play next or previous picture.
     *
     * @param delta
     */
    private void moveNextOrPrevious(final int delta) {
        mPhotoPlayerHolder.mGifView.setStop();
        if (mThread.isAlive()) {
            return;
        }
        if (mCurrentView != IMAGE_VIEW && mCurrentView != GIF_VIEW && mCurrentView != SURFACE4K2K_VIEW) {
            showToastAtBottom(getString(R.string.photo_3D_toast));
            return;
        }
        if (!mCanResponse) {
            return;
        }
        // check
        int position = mCurrentPosition + delta;
        if (position <= -1) {
            position = 0;
            // Tip the current is the first picture
            showToastAtCenter(getString(R.string.first_photo));
            return;
        } else if (position >= mPhotoFileList.size()) {
            position = mPhotoFileList.size() - 1;
            // Tip the current is the last picture
            showToastAtCenter(getString(R.string.last_photo));
            stopPPTPlayer();
            return;
        }
        mPhotoPlayerHolder.mImageView.setImageDrawable(null);
        mPhotoPlayerHolder.mImageView.clear();
        // reset imageview control
        if (mPhotoPlayerHolder.mImageView.mRotateCounter != 0
                || mZoomTimes != 0) {
            mPhotoPlayerHolder.mImageView.resetRotateCounter();
        }
        mZoomTimes = 0;
        mCurrentPosition = position;

            final String url = mPhotoFileList.get(mCurrentPosition).getPath();
            if (!url.substring(url.lastIndexOf(".") + 1)
                    .equalsIgnoreCase(Constants.GIF)) {
                if (mCurrentView != IMAGE_VIEW) {
                    mPhotoPlayerHolder.mScrollView.setCurrentView(IMAGE_VIEW);
                    mCurrentView = IMAGE_VIEW;
                }
            }
            // in 2D mode
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //String url = mPhotoFileList.get(mCurrentPosition).getPath();
                    if (url.substring(url.lastIndexOf(".") + 1)
                            .equalsIgnoreCase(Constants.GIF)) {
                        decodeGif(url);
                        return;
                    } else {
                        if(!initBitmap())
                        {
                            return;
                        }
                        PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mCurrentView == IMAGE_VIEW) {
                                    mPhotoPlayerHolder.mImageView
                                            .setImageRotateBitmapResetBase(
                                                    mBitmapCache, false,
                                                    PhotoPlayerActivity.this);
                                    if (isAnimationOpened)
                                        startPhotoAnimation();
                                } else {
                                    mPhotoPlayerHolder.mSurfaceView.drawImage();
                                }
                                if (mPPTPlayer) {
                                    if (is4K2KMode) {
                                        mHandler.sendEmptyMessageDelayed(PPT_PLAYER, slide4K2KTime);
                                    } else {
                                        mHandler.sendEmptyMessageDelayed(PPT_PLAYER, slideTime);
                                    }
                                }
                            }
                        });
                    }
                }
            });
            mThread.start();
        // in 3D mode
        // update name of photo
        mPhotoPlayerHolder.photo_name.setText(getString(R.string.current_pic)
                + mPhotoFileList.get(mCurrentPosition).getName());
    }

    /**
     * Play Photo started animation effects(random)
     */
    private void startPhotoAnimation() {
        int random = (int) Math.round(Math.random() * (animationNum - 1) + 0);
        Animation animation = AnimationUtils.loadAnimation(
                getApplicationContext(), animationArray[random]);
        // Components play back the animation
        mPhotoPlayerHolder.mImageView.startAnimation(animation);
    }

    /**
     * stop play slide.
     */
    private void stopPPTPlayer() {
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
        if (mCurrentView == SURFACE_VIEW) {
            mPhotoPlayerHolder.mSurfaceView.setNormal();
        }
        mPhotoPlayerHolder.mImageView.setImageDrawable(null);
        mPhotoPlayerHolder.mImageView.clear();
        mPhotoPlayerHolder.mImageView.resetRotateCounter();
        mPhotoPlayerHolder.mGifView.setStop();
        if (mPPTPlayer) {
            stopPPTPlayer();
        }
        if (is4K2KMode) {
            is4K2KMode = false;
            panel4k2kmode = false;
            mCurrentView = IMAGE_VIEW;
            mPhotoPlayerHolder.mScrollView.setCurrentView(IMAGE_VIEW);
            MDisplay.setPanelMode(PanelMode.E_PANELMODE_NORMAL);
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
            Log.d(TAG, "dismissProgressDialog");
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
            mPlayControllerLayout.requestFocus();
            mPlayControllerLayout.setFocusable(true);
            mIsControllerShow = true;
        } else {
            Log.d(TAG, "playControlLayout is null ptr===");
        }
    }

    /**
     * After how long hidden article control
     */
    private void hideControlDelay() {
        mHandler.sendEmptyMessageDelayed(
                PhotoPlayerActivity.HIDE_PLAYER_CONTROL, DEFAULT_TIMEOUT);
    }

    /**
     * Cancel time delay hidden.
     */
    private void cancleDelayHide() {
        mHandler.removeMessages(PhotoPlayerActivity.HIDE_PLAYER_CONTROL);
        mPlayControllerLayout.requestFocus();
        mPlayControllerLayout.setFocusable(true);
    }

    /**
     * Hidden article control.
     */
    private void hideController() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE);
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
            if (mSourceFrom == Constants.SOURCE_FROM_SAMBA
                    || mSourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
                showToastAtCenter(getString(R.string.net_disconnect));
                closeSilently(mFileInputStream);
                PhotoPlayerActivity.this.finish();
            }
        }
    };

    private BroadcastReceiver mSourceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PhotoPlayerActivity.this.finish();
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
                    PhotoPlayerActivity.this.finish();
                }
            }
        }
    }

    private void decodeBitmapFailed(final int id) {
        mCanResponse = true;
        PhotoPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastAtCenter(getString(id));
            }
        });
    }

}
