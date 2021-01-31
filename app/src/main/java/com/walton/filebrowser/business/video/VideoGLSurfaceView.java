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
package com.walton.filebrowser.business.video;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;

import java.io.IOException;
import android.graphics.SurfaceTexture;
import android.widget.Toast;

import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.media.MMediaPlayer;

public class VideoGLSurfaceView extends MyGLSurfaceView {
    private static final String TAG = "VideoGLSurfaceView";
    private static final int PIXEL_FORMAT = GLES20.GL_RGB;
    private static final boolean SET_CHOOSER = PIXEL_FORMAT == GLES20.GL_RGBA ? true : false;
    private static final int MEDIA_PLAYER_IDEL = 0;
    private static final int MEDIA_PLAYER_PREPARED = 1;
    private static final int MEDIA_PLAYER_STARTED = 2;
    private static final int MEDIA_PLAYER_PAUSED = 3;
    private static final int MEDIA_PLAYER_STOPPED = 4;
    private static final int MEDIA_PLAYER_PLAYBACK_COMPLETE = 5;
    private static final int MEDIA_PLAYER_ERROR = 6;
    private static final int KEY_PARAMETER_SET_DUAL_DECODE_PIP = 2024;
    private static final int KEY_PARAMETER_SET_MULTI_THUMBS = 2040;

    private Context mContext = null;
    private MultiThumbnailRenderer mRenderer = null;
    private Handler mHandler = null;
    private SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mEditor = null;
    private SurfaceTexture mSurfaceTexture = null;
    private MMediaPlayer mThumbnailMMediaPlayer = null;
    private int mCurrentState = MEDIA_PLAYER_IDEL;
    private int mDuration;
    private int mSeekPosition;
    private int mThumbnailInterval;
    private int mThumbnailNumber;
    private Thread mSurfaceTextureThread = null;
    private Thread mThumbnailPlayerThread = null;
    public static boolean bNeedClear = false;

    public VideoGLSurfaceView(Context context) {
        super(context);
        Log.i(TAG, "VideoGLSurfaceView context:" + context);
        initRenderer(context);
    }

    public VideoGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "VideoGLSurfaceView context:" + context + " attrs:" + attrs);
        initRenderer(context);
    }

    private void initRenderer(Context context) {
        mContext = context;
        setEGLContextClientVersion(2);
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
        mRenderer = new MultiThumbnailRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void init(Handler handler, String videoPath) {
        this.mHandler = handler;
        mSharedPreferences = mContext.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        bNeedClear = false;

        setRenderMode(RENDERMODE_CONTINUOUSLY);
        requestRender();
        openThumbnailPlayer(videoPath);
    }

    public void doClear() {
        Log.i(TAG, "doClear");
        bNeedClear = true;
        SharedPreferences  sharedPreferences = mContext.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SeekBarOnHover", false);
        editor.commit();
        queueEvent(new Runnable(){
            public void run() {
                mRenderer.doClear();
            }
        });
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause ---------------- begin");
        releaseThumbnailPlayer(true);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume --------------------- begin");
        super.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!Tools.isThumbnailModeOn()) {
            return true;
        }
        Log.i(TAG, "onTouchEvent ------------------ begin");

        if (mHandler != null) {
            mHandler.sendEmptyMessage(Constants.ShowController);
        }
        SharedPreferences  sharedPreferences = mContext.getSharedPreferences("VideoGLSurfaceView", Context.MODE_PRIVATE);
        int surfaceWidth = sharedPreferences.getInt("SurfaceWidth", 225); // defalut SurfaceWidth is  225
        boolean seekBarOnHover = sharedPreferences.getBoolean("SeekBarOnHover", false);
        boolean thumbnailBorderViewFocusFlag = sharedPreferences.getBoolean("ThumbnailBorderViewFocus", false);

        // if no thumbnail are shown, will return.
        if (!thumbnailBorderViewFocusFlag) {
            return true;
        }

        int index = 0;
        int getThumbnailNumber = Tools.getThumbnailNumber();
        int []imageLeftX = new int[getThumbnailNumber];
        int []imageRightX = new int[getThumbnailNumber];
        for (int i = 0; i < getThumbnailNumber; i++) {
            imageLeftX[i] = 15 * i + surfaceWidth * i; // two images's interval is 15
            imageRightX[i] = 15 * i + surfaceWidth * (i + 1);
            Log.i(TAG, "i:" + i + "=(" + imageLeftX[i] + " ," + imageRightX[i] + ")");
        }
        int getX = (int)motionEvent.getX();
        int getY = (int)motionEvent.getY();
        Log.i(TAG, "onTouchEvent -- X:" + getX + " Y:" + getY);

        for (int which = 0; which < getThumbnailNumber; which++) {
            if ((getX > imageLeftX[which]) && (getX < imageRightX[which])) {
                index = which;
            }
        }
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(Constants.SeekWithHideThumbnailBorderView);
            msg.arg1 = sharedPreferences.getInt("TextureTimeStamp" + index, mSeekPosition);
            Log.i(TAG, "Thumbnail index:" + index + " TextureTimeStamp:" + msg.arg1);
            mHandler.sendMessage(msg);
        }
        mEditor.putBoolean("ThumbnailBorderViewFocus", false);
        // doClear();
        return true;
    }

    private void openThumbnailPlayer(String videoPath) {
        //releaseThumbnailPlayer(false);
        Log.i(TAG, "openThumbnailPlayer ---------------- begin");
        mEditor.putBoolean("SeekBarOnHover", false);
        mEditor.putBoolean("ThumbnailOnHover", false);
        mEditor.putBoolean("DrawFrameFinished", true);
        mEditor.putBoolean("ThumbnailBorderViewFocus", false);
        mEditor.commit();

        mCurrentState = MEDIA_PLAYER_IDEL;
        mThumbnailPlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                releaseThumbnailPlayer(false);
                mThumbnailMMediaPlayer = new MMediaPlayer();
                mHandler.sendEmptyMessage(Constants.INIT_THUMBNAIL_PLAYER);
                Looper.loop();
            }
        });
        mThumbnailPlayerThread.start();
        Log.i(TAG, "openThumbnailPlayer ---------------- end");
    }

    public void initThumbnailPlayer(String videoPath) {
        Log.i(TAG, "-----initThumbnailPlayer ------- videoPath:" + videoPath);
        Uri uri = Uri.parse(videoPath);
        if (mThumbnailMMediaPlayer != null) {
            try {
                mThumbnailMMediaPlayer.setOnPreparedListener(mThumbnailPreparedListener);
                mThumbnailMMediaPlayer.setOnErrorListener(mErrorListener);
                mThumbnailMMediaPlayer.setDataSource(mContext, uri);
                mThumbnailMMediaPlayer.setParameter(KEY_PARAMETER_SET_DUAL_DECODE_PIP, 1);

                getThumbnailFrame(-1, Tools.getThumbnailNumber(), 2000); // initialization
                createSurfaceTextureThread();
            } catch (IOException ex) {
                Log.w(TAG, "Unable to open content: " + uri, ex);
                return;
            } catch (IllegalArgumentException ex) {
                Log.w(TAG, "Unable to open content: " + uri, ex);
                return;
            } catch (IllegalStateException ex) {
                Log.w(TAG, "Unable to open content: " + uri, ex);
                return;
            } catch (SecurityException ex) {
                Log.w(TAG, "Unable to open content: " + uri, ex);
                return;
            }
        }

    }

    private void createSurfaceTextureThread() {
        Log.i(TAG, "createSurfaceTextureThread ----------");
        mSurfaceTextureThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                int [] textureID = new int[1];
                GLES20.glGenTextures(1, textureID, 0);
                checkGlError("glGenTextures");
                mSurfaceTexture = new SurfaceTexture(textureID[0], true);
                Log.i(TAG, "new SurfaceTexture ----------");
                mHandler.sendEmptyMessage(Constants.PrepareMediaPlayer);
                Looper.loop();
            }
        });
        mSurfaceTextureThread.start();
    }

    public void prepareMediaPlayer() {
        Log.i(TAG, "prepareMediaPlayer ----------");
        if (mThumbnailMMediaPlayer == null) {
            return;
        }
        try {
            Surface surface = new Surface(mSurfaceTexture);
            mThumbnailMMediaPlayer.setSurface(surface);
            surface.release();

            queueEvent(new Runnable() {
                public void run() {
                    mRenderer.setupFramebuffer();
                    mRenderer.setSurfaceTexture(mSurfaceTexture);
                    mRenderer.setHandler(mHandler);
                }});
            mThumbnailMMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "IllegalArgumentException", ex);
            return;
        } catch (IllegalStateException ex) {
            Log.w(TAG, "IllegalStateException", ex);
            return;
        } catch (SecurityException ex) {
            Log.w(TAG, "SecurityException", ex);
            return;
        }
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    MMediaPlayer.OnPreparedListener mThumbnailPreparedListener = new MMediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.i(TAG, "onPrepared ---------------------- begin");
            mCurrentState = MEDIA_PLAYER_PREPARED;
            mThumbnailMMediaPlayer.start();
            mCurrentState = MEDIA_PLAYER_STARTED;
            Log.i(TAG, "mThumbnailMMediaPlayer start");
        }
    };

    MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.e(TAG, "MM ErrorType: " + what + " ExtraCode:" + extra);
            mCurrentState = MEDIA_PLAYER_ERROR;
            /* If an error handler has been supplied, use it and finish. */
            releaseThumbnailPlayer(false);
            Toast toast = Toast.makeText(mContext, "MM Error! ErrorType:" + what + " ExtraCode:" + extra, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Tools.setThumbnailMode("0");
            return true;
        }
    };

    public MMediaPlayer getThumbnailMMediaPlayer() {
        return mThumbnailMMediaPlayer;
    }

    /**
     * Determine whether normal play.
     *
     * @return
     */
    public boolean isInPlaybackState() {
        return ((mThumbnailMMediaPlayer != null) && (mCurrentState != MEDIA_PLAYER_IDEL) && (mCurrentState != MEDIA_PLAYER_ERROR));
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            if (mDuration > 0) {
                Log.i(TAG, "getDuration mDuration1:" + mDuration);
                return mDuration;
            }
            mDuration = mThumbnailMMediaPlayer.getDuration();
            Log.i(TAG, "getDuration mDuration2:" + mDuration);
            return mDuration;
        }
        Log.i(TAG, "getDuration mDuration3:" + mDuration);
        mDuration = -1;
        return mDuration;
    }

    public void getThumbnailFrame(int position, int number, int interval) {
//        if (!mSharedPreferences.getBoolean("DrawFrameFinished", true)) {
//            Log.i(TAG, "----getThumbnailFrame DrawFrameFinished false ----");
//            return;
//        }
        Log.i(TAG, "[MM APK] [multi-thumb] getThumbnailFrame position:" + position + " number:" + number + " interval:" + interval);
        mSeekPosition = position;
        if (mSeekPosition != -1 && mThumbnailMMediaPlayer != null) {
            if (mSeekPosition > mThumbnailMMediaPlayer.getDuration()) {
                mSeekPosition = mThumbnailMMediaPlayer.getDuration();
            } else if (mSeekPosition < 0) {
                mSeekPosition = 0;
            }
        }
        // mThumbnailNumber = number;
        mThumbnailNumber = Tools.getThumbnailNumber();
        mThumbnailInterval = interval;
        Parcel parcel = Parcel.obtain() ;
        parcel.writeInt(position); // position
        parcel.writeInt(number); // number
        parcel.writeInt(interval); // interval
        if (mThumbnailMMediaPlayer != null) {
            mThumbnailMMediaPlayer.setParameter(KEY_PARAMETER_SET_MULTI_THUMBS, parcel);
        }
        parcel.recycle();
    }

    public void releaseThumbnailPlayer(boolean flag) {

        synchronized(this) {
            Log.i(TAG, "releaseThumbnailPlayer -------------- start flag:" + flag);
            doClear();
            queueEvent(new Runnable(){
                public void run() {
                    mRenderer.doBreak();
                }
            });

            if (flag) {
                if (mThumbnailMMediaPlayer != null) {
  //                  new Thread(new Runnable() {
 //                       @Override
 //                       public void run() {
                            if (mThumbnailMMediaPlayer.isPlaying()) {
                                Log.i(TAG, "mThumbnailMMediaPlayer.isPlaying");
                                mCurrentState = MEDIA_PLAYER_IDEL;
                                mThumbnailMMediaPlayer.stop();
                                mThumbnailMMediaPlayer.release();
                                mThumbnailMMediaPlayer = null;
                            } else {
                                Log.i(TAG, "!mThumbnailMMediaPlayer.isPlaying");
                                mCurrentState = MEDIA_PLAYER_IDEL;
                                mThumbnailMMediaPlayer.stop();
                                mThumbnailMMediaPlayer.release();
                                mThumbnailMMediaPlayer = null;
                            }
                        }
//                    }).start();
//                }
            } else {
                if (mThumbnailMMediaPlayer != null) {
                    mThumbnailMMediaPlayer.stop();
                    mCurrentState = MEDIA_PLAYER_IDEL;
                    mThumbnailMMediaPlayer.release();
                    mThumbnailMMediaPlayer = null;
                }
            }
            doClear();
            releaseSurfaceTexture();
            Log.i(TAG, "releaseThumbnailPlayer -------------- end");
            }
    }

    private void releaseSurfaceTexture() {
        Log.i(TAG, "releaseSurfaceTexture mSurfaceTexture:" + mSurfaceTexture);
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mSurfaceTextureThread != null && mSurfaceTextureThread.isAlive()) {
            Log.i(TAG, "mSurfaceTextureThread = null");
            mSurfaceTextureThread = null;
        }
    }

}  // End of class VideoGLSurfaceView.
