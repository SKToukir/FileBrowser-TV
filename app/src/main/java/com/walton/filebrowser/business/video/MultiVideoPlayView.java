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

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import android.os.Handler;
import android.os.SystemProperties;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

import com.mstar.android.media.MMediaPlayer;
import com.walton.filebrowser.util.Tools;

/**
 * MultiVideoPlayView.
 *
 * @author
 * @since 1.0
 */

@SuppressLint("NewApi")
public class MultiVideoPlayView extends SurfaceView {

    private String TAG = "MultiVideoPlayView";
    // settable by the client
    private String mVideoPath;
    private Uri mUri;
    private Uri mNetVideoUri;
    private Map<String, String> mHeaders = null;

    private int mDuration;

    // all possible internal states

    private static final int STATE_ERROR = -1;

    private static final int STATE_IDLE = 0;

    private static final int STATE_PREPARING = 1;

    private static final int STATE_PREPARED = 2;

    private static final int STATE_PLAYING = 3;

    private static final int KEY_PARAMETER_SET_MUTIL_VIDEO_PLAY = 2054;

    private int mCurrentState = STATE_IDLE;

    // All the stuff we need for playing and showing a video

    private SurfaceHolder mSurfaceHolder = null;

    private MMediaPlayer mMMediaPlayer = null;

    private int mVideoWidth;

    private int mVideoHeight;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    private playerCallback myPlayerCallback = null;

    private AudioManager mAudioManager = null;

    private int viewId = 0;

    private Context mContext;

    private Handler mHandler;

    private static final int IO_ERROR = 9000;

    private static final String netParameter = "set_multiple_player";

    public MultiVideoPlayView(Context context) {
        super(context);
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVideoView();
    }

    public MultiVideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVideoView();
    }

    public MultiVideoPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVideoView();
    }

    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        // getHolder().setFormat(PixelFormat.RGBA_8888);
        getHolder().addCallback(mSHCallback);
        // getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void isNetVideo(String path) {
        if (!Tools.isNetPlayback(path)) {
            return;
        }
        mHeaders = new HashMap<String, String>();
        mHeaders.put(netParameter, "1");
    }

    public void setVideoPath(String path, int id) {
        isNetVideo(path);
        viewId = id;
        mVideoPath = path;
        mUri = Uri.parse(path);
        int md5 = SystemProperties.getInt("mstar.md5", 0);
        if (md5 == 1) {
            int ind = path.lastIndexOf(".");
            if (ind>0) {
                String sOrgMD5 = path.substring(0,ind) + ".md5";
                int lastInd = path.lastIndexOf("/");
                String sDstMD5 = path.substring(0,lastInd+1) + "golden.md5";
                Tools.copyfile(sOrgMD5, sDstMD5);
            }
        }
        Log.i(TAG, "***********setVideoURI:" + mUri + "viewId:" + viewId);
        Log.i(TAG, "***********setVideoPath is calling openPlayer***************");
        openPlayer();
        requestLayout();
        invalidate();
    }

    public String getVideoPath(){
        return  mVideoPath;
    }

    /**
     * call before play next.
     */
    public void stopPlayback() {
        if (mMMediaPlayer != null && mCurrentState != STATE_IDLE) {
            mCurrentState = STATE_IDLE;
            mMMediaPlayer.stop();
            Log.i(TAG, "stopPlayback: *****release start*****  viewId:"+viewId);
            mMMediaPlayer.release();
            mMMediaPlayer = null;
        }
    }

    /**
     * When abnormal stop play.
     */
    public void stopPlayer() {
        synchronized(this) {
            if (mMMediaPlayer != null && mCurrentState != STATE_IDLE) {
                mCurrentState = STATE_IDLE;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mMMediaPlayer.isPlaying()) {
                                Log.i(TAG, "*****stop start*****");
                                mMMediaPlayer.stop();
                                Log.i(TAG, "*****stop end*****");
                            }
                            Log.i(TAG, "***stopPlayer()**release start*****");
                            mMMediaPlayer.release();
                            Log.i(TAG, "***stopPlayer()**release end*****");
                            mMMediaPlayer = null;
                        } catch (Exception e) {
                            Log.i(TAG, "Exception:" + e);
                        }

                    }
                }).start();
            }
        }
    }

    /**
     * Start player.
     */
    private void openPlayer() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        Log.i(TAG, "***openPlayer()***mUri:"+",viewId:"+viewId);
        // close the built-in music service of android
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        this.getContext().sendBroadcast(i);
        // Close the user's music callback interface
        if (myPlayerCallback != null)
            myPlayerCallback.onCloseMusic();

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        try {
            mMMediaPlayer = new MMediaPlayer();
            mDuration = -1;
            mMMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMMediaPlayer.setOnErrorListener(mErrorListener);
            mMMediaPlayer.setOnInfoListener(mInfoListener);
            String path = mUri.toString();
            if (mSurfaceHolder != null) {
                mMMediaPlayer.setDisplay(mSurfaceHolder);
            }

            if (Tools.isNetPlayback(path) && mHeaders != null) {
                Log.i(TAG,"netvideo setDataSource with headers");
                mMMediaPlayer.setDataSource(mContext, mUri ,mHeaders);
            } else {
                mMMediaPlayer.setDataSource(mContext, mUri);
                mMMediaPlayer.setParameter(KEY_PARAMETER_SET_MUTIL_VIDEO_PLAY, 1);
            }
            mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMMediaPlayer.setScreenOnWhilePlaying(true);
            Log.i(TAG, "***********prepareAsync: " + mSurfaceHolder);
            mMMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            Log.i(TAG, "*******prepareAsync  end***** viewId:"+viewId);
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            if (myPlayerCallback != null) {
                myPlayerCallback.onError(mMMediaPlayer,   IO_ERROR, 0, viewId);
            }
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            errorCallback(0);
            return;
        } catch (IllegalStateException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            errorCallback(0);
            return;
        } catch (SecurityException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            errorCallback(0);
            return;
        }
    }

    private void errorCallback(int errId) {
        mCurrentState = STATE_ERROR;
        if (myPlayerCallback != null)
            myPlayerCallback.onError(mMMediaPlayer,MMediaPlayer.MEDIA_ERROR_UNKNOWN, errId, viewId);
    }

    // The following is a series of the player listener in callback
    MMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            Log.e(TAG, "MediaPlayer: "+mp+"    Video Size Changed: (" + mVideoWidth + "," + mVideoHeight+")");
            //setVideoScale(0,0,getLayoutParams().MATCH_PARENT,getLayoutParams().MATCH_PARENT);
        }
    };

    MMediaPlayer.OnPreparedListener mPreparedListener = new MMediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            Log.i(TAG, "******onPrepared*myPlayerCallback*****" + myPlayerCallback);
            requestLayout();
            invalidate();
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            start();
            setVoice(true);
            if (myPlayerCallback != null) {
                myPlayerCallback.onPrepared(mMMediaPlayer, viewId);
            }
        }
    };

    private MMediaPlayer.OnCompletionListener mCompletionListener = new MMediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "MediaPlayer  call  onCompletion ..");
            if (mMMediaPlayer.getDuration()>100) {
                mMMediaPlayer.seekTo(100);
                mMMediaPlayer.start();
            } else {
                String errorMsg = "This video's duration is not beyond 100ms";
                errorCallback(0);
            }
            if (myPlayerCallback != null) {
                myPlayerCallback.onCompletion(mMMediaPlayer, viewId);
            }
        }
    };

    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.e(TAG, "viewId:"+viewId+";Error: " + framework_err + "," + impl_err);
            mCurrentState = STATE_ERROR;

            /* If an error handler has been supplied, use it and finish. */
            if (myPlayerCallback != null) {
                if (myPlayerCallback.onError(mMMediaPlayer, framework_err, impl_err, viewId)) {
                    return true;
                }
            }
            return true;
        }
    };

    private MMediaPlayer.OnInfoListener mInfoListener = new MMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "onInfo what:" + what  + " extra:" + extra);
            if (myPlayerCallback != null) {
                myPlayerCallback.onInfo(mp, what, extra, viewId);
                return true;
            }
            return false;
        }
    };

    /**
     * Surface relevant callback interface.
     */
    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w,
                int h) {
            mSurfaceHolder = holder;
            Log.i(TAG, "*************surfaceChanged************" + w + " " + h+"  viewId:"+viewId);
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            //mSurfaceHolder.setFixedSize(mSurfaceWidth, mSurfaceWidth);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "*************surfaceCreated************"+viewId);
            mSurfaceHolder = holder;
            // mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
            Log.i(TAG, "***********surfaceCreated is calling openPlayer***************");
            openPlayer();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            Log.i(TAG, "*************surfaceDestroyed************viewId:"+viewId);
            Log.w(TAG,"why surfaceDestroyed:"+Log.getStackTraceString(new Throwable()));
            // Setting url null is so important  that "openPlayer()" of "surfaceCreated" and "setVideoPath" can do work
            // only one of them,or it would do two "openPlayer()" and would reset the player that is playing.
            mUri = null;
            //if (viewId != 0)
                //release(true);
        }
    };

    public void setVideoScale(int leftMargin, int topMargin, int width, int height) {
        Log.i(TAG,"setVideoScale width height :"+String.valueOf(width)+" "+String.valueOf(height));
        LayoutParams lp = getLayoutParams();
        lp.height = height;
        lp.width = width;

        setLayoutParams(lp);
    }

    /*
     * release the media player in any state.
     */
    private void release(boolean cleartargetstate) {
        Log.i(TAG, "***********release*******");
        if (mCurrentState == STATE_IDLE) {
            return;
        }
        mCurrentState = STATE_IDLE;
        if (cleartargetstate) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mMMediaPlayer != null && mMMediaPlayer.isPlaying()) {
                        try {
                            mMMediaPlayer.stop();
                        } catch (IllegalStateException e) {
                            Log.i(TAG, "stop fail! please try again!");
                            try {
                                this.wait(2000);
                                mMMediaPlayer.stop();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    if (mMMediaPlayer != null) {
                        Log.i(TAG, "*****release start*****  viewId:"+viewId);
                        mMMediaPlayer.release();// release will done reset
                        Log.i(TAG, "*****release end*****   viewId:"+viewId);
                    }
                    mMMediaPlayer = null;
                }
            }).start();
        } else {
            if (mMMediaPlayer != null) {
                Log.i(TAG, "***********release Player  viewId:"+viewId);
                mMMediaPlayer.release();
            }
            mMMediaPlayer = null;
        }

    }

    public void setPlayingState() {
        mCurrentState = STATE_PREPARED;
    }

    public void setVoice(boolean isSetOpen) {
        if (isInPlaybackState()) {
            Log.i(TAG,"isInPlaybackState setVoice isSetOpen:"+isSetOpen+",viewId:"+viewId);
            if (isSetOpen) {
                mMMediaPlayer.setVolume(1.0f, 1.0f);
            } else {
                mMMediaPlayer.setVolume(0, 0);
            }
        }
    }
    public void start() {
        if (isInPlaybackState()) {
            mMMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
    }
    public boolean isPlaying() {
        if (mMMediaPlayer == null ){
            return false;
        }
        try{
            return isInPlaybackState() && mMMediaPlayer.isPlaying();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Determine whether normal play.
     *
     * @return
     */
    public boolean isInPlaybackState() {
        return (mMMediaPlayer != null && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    public MMediaPlayer getMMediaPlayer() {
        return mMMediaPlayer;
    }
    /**
     * Register a callback to be invoked
     *
     * @param l The callback that will be run
     */
    public void setPlayerCallbackListener(playerCallback l) {
        myPlayerCallback = l;
    }


    /**
     * User callback interface.
     */
    public interface playerCallback {
        // error tip
        boolean onError(MediaPlayer mp, int framework_err, int impl_err, int viewId);

        // play complete
        void onCompletion(MediaPlayer mp, int viewId);

        boolean onInfo(MediaPlayer mp, int what, int extra, int viewId);

        void onBufferingUpdate(MediaPlayer mp, int percent);

        void onPrepared(MediaPlayer mp, int viewId);

        // Finish back
        void onSeekComplete(MediaPlayer mp, int viewId);

        // Video began to play before, closed music.
        void onCloseMusic();

        void onUpdateSubtitle(String sub);

        void onVideoSizeChanged(MediaPlayer mp, int width, int height, int viewId);
    }
}
