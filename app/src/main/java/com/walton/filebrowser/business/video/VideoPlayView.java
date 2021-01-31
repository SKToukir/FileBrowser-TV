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
import java.lang.reflect.Method;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.TimedText;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.media.VideoCodecInfo;
import android.media.SubtitleTrack;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.Bluray;

import android.widget.Toast;

/**
 * VideoPlayView.
 *
 * @author
 * @since 1.0
 */

@SuppressLint("NewApi")
public class VideoPlayView extends SurfaceView {

    private String TAG = "VideoPlayView";
    // settable by the client

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

    private static final int STATE_PAUSED = 4;

    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private static final int STATE_STREAMLESS_TO_NEXT = 6;

    private static final String MVC = "MVC";

    private static final int KEY_PARAMETER_SET_DUAL_DECODE_PIP = 2024;

    private static final int KEY_PARAMETER_SET_DOLBY_HDR = 2053;


    // mCurrentState is a VideoPlayView object's current state.

    // mTargetState is the state that a method caller intends to reach.

    // For instance, regardless the VideoPlayView object's current state,

    // calling pause() intends to bring the object to a target state

    // of STATE_PAUSED.

    private int mCurrentState = STATE_IDLE;

    private int mTargetState = STATE_IDLE;

    // All the stuff we need for playing and showing a video

    private SurfaceHolder mSurfaceHolder = null;
    // private MMediaPlayer mMMMediaPlayer = null;

    // use MMMediaPlayer class for sta
    private MMediaPlayer mMMediaPlayer = null;

    private MMediaPlayer mNextMMediaPlayer = null;

    private int mVideoWidth;

    private int mVideoHeight;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    private boolean bVideoDisplayByHardware = false;

    private playerCallback myPlayerCallback = null;

    private int mSeekWhenPrepared; // recording the seek position while

    private AudioManager mAudioManager = null;

    private boolean isVoiceOpen = true;

    private float currentVoice = 1.0f;

    private int viewId = 0;

    private long startTime;

    private long startSeekTime;

    private long endSeekTime;

    private boolean bBDISO = false;
    private ISOReadAdapter mDatasource = null;
    private static final int IO_ERROR = 9000;
    private Context mContext;

    private Handler mHandler;
    public boolean bResumePlay = true;
    public VideoPlayView(Context context) {
        super(context);
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVideoView();
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initVideoView();
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyle) {
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
        mTargetState = STATE_IDLE;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void setNetVideoUri(Uri uri, Map<String, String> headers) {
        mNetVideoUri = uri;
        mHeaders = headers;
    }

    public void setVideoPath(String path, int id) {
        viewId = id;
        if (viewId == 2) {
            String hardwareName = Tools.getHardwareName();
            if (Tools.isSupportDualDecode()) {
                Log.d(TAG,"setDualDecodePip :false");
                setDualDecodePip(viewId, false);
            }
        }
        if (Tools.isSambaPlaybackUrl(path)) {
            mUri = Uri.parse(Tools.convertToHttpUrl(path));
            Tools.setSambaVideoPlayBack(true);
        } else {
            mUri = Uri.parse(path);
            Tools.setSambaVideoPlayBack(false);
        }
        mSeekWhenPrepared = 0;
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
        bVideoDisplayByHardware = false;
        if (Tools.isVideoStreamlessModeOn() && (mMMediaPlayer != null) && mMMediaPlayer.isPlaying()) {
            Log.i(TAG, "openPlayer2 for streamless mode.");
            openPlayer2();
        } else {
            Log.i(TAG, "openPlayer for Nonstreamless mode.");
            openPlayer();
        }
        requestLayout();
        invalidate();
    }

    public boolean is4kVideo() {
        Log.i(TAG, "VideoPlayview is4kVideo mVideoWidth:" + mVideoWidth + " mVideoHeight:" + mVideoHeight);
        if (mVideoWidth >= 3840 && mVideoHeight >= 1080) {
            return true;
        }
        return false;
    }

    public boolean isVideoDisplayByHardware() {
        return bVideoDisplayByHardware;
    }

    /**
     * call before play next.
     */
    public void stopPlayback() {
        if (mMMediaPlayer != null && mTargetState != STATE_IDLE) {
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            mMMediaPlayer.stop();
            Log.i(TAG, "stopPlayback: *****release start*****  viewId:"+viewId);
            mMMediaPlayer.release();
            Log.i(TAG, "stopPlayback: *****release end*****  viewId:"+viewId);
            Log.i(TAG, "stopPlayback: *****mMMediaPlayer*****"+mMMediaPlayer);
            mMMediaPlayer = null;
        }
    }

    /**
     * When abnormal stop play.
     */
    public void stopPlayer() {
        synchronized(this) {
            if (mMMediaPlayer != null && mTargetState != STATE_IDLE) {
                mCurrentState = STATE_IDLE;
                mTargetState = STATE_IDLE;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            releaseSeamlessPlay();
                            if (mMMediaPlayer.isPlaying()) {
                                Log.i(TAG, "*****stop start*****");
                                mMMediaPlayer.stop();
                                Log.i(TAG, "*****stop end*****");
                            }
                            Log.i(TAG, "***stopPlayer()**release start*****");
                            mMMediaPlayer.release();
                            Log.i(TAG, "***stopPlayer()**release end*****");
                            Log.i(TAG, "***stopPlayer()**mMMediaPlayer*****"+mMMediaPlayer);
                            mMMediaPlayer = null;
                        } catch (Exception e) {
                            Log.i(TAG, "Exception:" + e);
                        }

                    }
                }).start();
            }
        }
    }

    public void releaseSeamlessPlay(){
        Log.i(TAG,"Tools.isVideoStreamlessModeOn():"+Tools.isVideoStreamlessModeOn());
        Log.i(TAG,"mMMediaPlayer:"+mMMediaPlayer);
        if (Tools.isVideoStreamlessModeOn() && mMMediaPlayer!=null) {
            Log.i(TAG,"SetSeamlessMode E_PLAYER_SEAMLESS_NONE");
            mMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_NONE);
        }
    }

    public long getStartTime() {
        return startTime;
    }

    /**
     * When player_a(viewId=1) playes with KEY_PARAMETER_SET_DUAL_DECODE_PIP, set KEY_PARAMETER_SET_DUAL_DECODE_PIP_a true
     * When player_b(viewId=2) playes with KEY_PARAMETER_SET_DUAL_DECODE_PIP, set KEY_PARAMETER_SET_DUAL_DECODE_PIP_b true
     * If KEY_PARAMETER_SET_DUAL_DECODE_PIP_a or KEY_PARAMETER_SET_DUAL_DECODE_PIP_b is true, means KEY_PARAMETER_SET_DUAL_DECODE_PIP
     * already been set for player_a or player_b, so will not set it again.
     */
    public void setDualDecodePip(int viewId, boolean flag) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("VideoPlayView", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (viewId == 2) {
            editor.putBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_b", flag);
            editor.putBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_a", false);
        } else {
            editor.putBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_a", flag);
            editor.putBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_b", false);
        }
        editor.commit();
    }

    public boolean getDualDecodePip() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("VideoPlayView", Context.MODE_PRIVATE);
        boolean flag_a = sharedPreferences.getBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_a", false);
        boolean flag_b = sharedPreferences.getBoolean("KEY_PARAMETER_SET_DUAL_DECODE_PIP_b", false);
        return (flag_a && flag_b);
    }

    /**
     * Start player.
     */
    private void openPlayer() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }

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
            mMMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMMediaPlayer.setOnInfoListener(mInfoListener);
            mMMediaPlayer.setOnTimedTextListener(mTimedTextListener);
            mMMediaPlayer.setOnSeekCompleteListener(mMMediaPlayerSeekCompleteListener);
            String path = mUri.toString();
            // Log.i("andrew","the path is:"+path);
            int IdPos = 0;
            if (path.startsWith("BlurayISO:") && (IdPos = path.lastIndexOf("index")) > 0) {
                String sTitle = path.substring(IdPos+5,path.length());
                Log.i(TAG,"the title is"+sTitle);
                int titID = Integer.parseInt(sTitle);
                bBDISO = true;
                mDatasource = new ISOReadAdapter(titID);
                mMMediaPlayer.setDataSource(mDatasource);
            } else {
                bBDISO = false;
                if (mNetVideoUri != null) {
                    if (mHeaders != null) {
                        mMMediaPlayer.setDataSource(this.getContext(), mNetVideoUri, mHeaders);
                    } else {
                        mMMediaPlayer.setDataSource(this.getContext(), mNetVideoUri);
                    }
                } else {
                    mMMediaPlayer.setDataSource(this.getContext(), mUri);
                }
            }
            String hardwareName = Tools.getHardwareName();
            if (viewId == 2) {
                if (Tools.isSupportDualDecode()) {
                    if (!getDualDecodePip()) {
                        Log.i(TAG,"set param KEY_PARAMETER_SET_DUAL_DECODE_PIP");
                        Log.i(this.TAG, "viewId=" + viewId + " setParameter KEY_PARAMETER_SET_DUAL_DECODE_PIP");
                        mMMediaPlayer.setParameter(KEY_PARAMETER_SET_DUAL_DECODE_PIP, 1);
                        setDualDecodePip(viewId, true);
                    }
                }
            }

            if (Tools.isVideoStreamlessModeOn()) {
                if (!Tools.isElderPlatformForStreamLessMode()) {
                    Log.v(TAG,"einstein/napoli flow set seamless mode E_PLAYER_SEAMLESS_DS");
                    mMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_DS);
                }
            }

            boolean isRotateModeOn = Tools.isRotateModeOn();
            Log.i(TAG, "isRotateModeOn:" + isRotateModeOn);
            if (true == isRotateModeOn) {
                int rotateDegrees = Tools.getRotateDegrees();
                Log.i(TAG, "rotateDegrees:" + rotateDegrees);
                imageRotate(rotateDegrees);
            }

            if (mSurfaceHolder != null) {
                mMMediaPlayer.setDisplay(mSurfaceHolder);
            }
            if (viewId == 1) {
                mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } else {
                if (Tools.isNativeAudioModeOn()) {
                    mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                } else {
                    mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                }
            }
            mMMediaPlayer.setScreenOnWhilePlaying(true);
            Log.i(TAG, "***********prepareAsync: " + mSurfaceHolder);
            if (Constants.bSupportDivx) {
                String fileName = Tools.getFileName(mUri.getPath());
                if (((VideoPlayerActivity)mContext).checkFullStopByFileName(fileName)
                        != Constants.DIVX_RESUMEPLAY_PLAYBACK){

                    ((VideoPlayerActivity)mContext).mIsNeedShowRentalCount = true;
                    bResumePlay = false;
                } else {
                    // resume-stop state
                    bResumePlay = true;
                    mMMediaPlayer.setParameter(Constants.KEY_PARAMETER_SET_RESUME_PLAY, 1);
                }
            }
            if (Tools.getDolbyHDR()) {
                mMMediaPlayer.setParameter(KEY_PARAMETER_SET_DOLBY_HDR, 1);
            }
            startTime = System.currentTimeMillis();
            mMMediaPlayer.prepareAsync();
            // we don't set the target state here either, but preserve the
            // target state that was there before.
            Log.i(TAG, "*******prepareAsync  end***** viewId:"+viewId);
            if (Tools.isVideoStreamlessModeOn()) {
                if (Tools.isElderPlatformForStreamLessMode()) {
                    mMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_FREEZ);
                    Log.i(TAG, "SetSeamlessMode E_PLAYER_SEAMLESS_FREEZ");
                }
            }
            if (Tools.isVideoStreamlessModeOn() && hardwareName.equals("nike")) {
                Log.i(TAG, "MediaPlayer1 SetSeamlessMode.....(nike flow)");
                mMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_FREEZ);
            }
            mCurrentState = STATE_PREPARING;
            mTargetState = STATE_PREPARED;
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

    /**
     * Start player2.
     */
    private void openPlayer2() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }

        // Close the user's music callback interface
        if (myPlayerCallback != null)
            myPlayerCallback.onCloseMusic();

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        // release(false);
        mCurrentState = STATE_STREAMLESS_TO_NEXT;
        try {
            Log.i(TAG, "MediaPlayer2 Created.....");
            mNextMMediaPlayer = new MMediaPlayer();
            mDuration = -1;
            mNextMMediaPlayer.setOnPreparedListener(mPreparedListener2);
            mNextMMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mNextMMediaPlayer.setOnCompletionListener(mCompletionListener);
            mNextMMediaPlayer.setOnErrorListener(mErrorListener);
            mNextMMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mNextMMediaPlayer.setOnInfoListener(mInfoListener);
            mNextMMediaPlayer.setOnTimedTextListener(mTimedTextListener);
            mNextMMediaPlayer.setOnSeekCompleteListener(mMMediaPlayerSeekCompleteListener);
            startTime = System.currentTimeMillis();
            Log.i(TAG, "MediaPlayer2 setDataSource(" + mUri + ")");
            mNextMMediaPlayer.setDataSource(this.getContext(), mUri);

            boolean isRotateModeOn = Tools.isRotateModeOn();
            Log.i(TAG, "isRotateModeOn:" + isRotateModeOn);
            if (true == isRotateModeOn) {
                int rotateDegrees = Tools.getRotateDegrees();
                Log.i(TAG, "rotateDegrees:" + rotateDegrees);
                imageRotate(rotateDegrees,mNextMMediaPlayer);
            }

            if (viewId == 2) {
                if (Tools.isSupportDualDecode()) {
                    if (!getDualDecodePip()) {
                        Log.i(TAG,"set param KEY_PARAMETER_SET_DUAL_DECODE_PIP");
                        Log.i(this.TAG, "viewId=" + viewId + " setParameter KEY_PARAMETER_SET_DUAL_DECODE_PIP");
                        mNextMMediaPlayer.setParameter(KEY_PARAMETER_SET_DUAL_DECODE_PIP, 1);
                        setDualDecodePip(viewId, true);
                    }
                }
            }

            if (viewId == 1) {
                mNextMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } else {
                if (Tools.isNativeAudioModeOn()){
                    mNextMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                } else {
                    mNextMMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                    setDualAudioOn(true);
                }
            }
            mNextMMediaPlayer.setScreenOnWhilePlaying(true);

            if (Constants.bSupportDivx) {
                String fileName = Tools.getFileName(mUri.getPath());
                if (((VideoPlayerActivity)mContext).checkFullStopByFileName(fileName)
                        != Constants.DIVX_RESUMEPLAY_PLAYBACK){

                    ((VideoPlayerActivity)mContext).mIsNeedShowRentalCount = true;
                    bResumePlay = false;
                } else {
                    // resume-stop state
                    bResumePlay = true;
                    mMMediaPlayer.setParameter(Constants.KEY_PARAMETER_SET_RESUME_PLAY, 1);
                }
            }

            if (Tools.getDolbyHDR()) {
                mMMediaPlayer.setParameter(KEY_PARAMETER_SET_DOLBY_HDR, 1);
            }
            String hardwareName = Tools.getHardwareName();
            if (!Tools.isElderPlatformForStreamLessMode()) {
                Log.v(TAG,"einstein/napoli flow set seamless mode E_PLAYER_SEAMLESS_DS");
                mNextMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_DS);
            }
            //Log.i(TAG, "MediaPlayer2 prepareAsync.....");
            //mNextMMediaPlayer.prepareAsync();
            Log.v(TAG, "the hardware's name is:" + hardwareName);
            if (Tools.isElderPlatformForStreamLessMode()) {
                Log.v(TAG, "MediaPlayer2 SetSeamlessMode.....(edison/kaiser flow)");
                mNextMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_FREEZ);
            }

            Log.v(TAG, "MediaPlayer1 stop.....");
            mMMediaPlayer.stop();
            if (!Tools.isElderPlatformForStreamLessMode()){
                mMMediaPlayer.release();
                mMMediaPlayer = null;
            } else if (Tools.isElderPlatformForStreamLessMode()) {
                Log.v(TAG, "MediaPlayer1 setDisplay(null).....(edison/kaiser flow)");
                mMMediaPlayer.setDisplay(null);
            }

            if (mSurfaceHolder != null) {
                Log.v(TAG, "MediaPlayer2 setDisplay.....");
                mNextMMediaPlayer.setDisplay(mSurfaceHolder);
            }

            if (hardwareName.equals("nike")) {
                Log.v(TAG, "MediaPlayer2 SetSeamlessMode.....(nike flow)");
                mNextMMediaPlayer.SetSeamlessMode(MMediaPlayer.EnumPlayerSeamlessMode.E_PLAYER_SEAMLESS_FREEZ);
            }
            Log.i(TAG, "MediaPlayer2 prepareAsync.....");
            mNextMMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mTargetState = STATE_PREPARED;
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            if (myPlayerCallback != null) {
                myPlayerCallback.onError(null, IO_ERROR, 0, viewId);
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
        mTargetState = STATE_ERROR;
        if (myPlayerCallback != null)
            myPlayerCallback.onError(mMMediaPlayer,
                    MMediaPlayer.MEDIA_ERROR_UNKNOWN, errId, viewId);
    }

    /*@Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 1;
        int height = 1;
        if (bDisplayedByHard) {
            width = Constants.sceenResolution.x;
            height = Constants.sceenResolution.y;
        } else {
            if (mVideoWidth > 0 && mVideoHeight > 0) {
                width = getDefaultSize(mVideoWidth, widthMeasureSpec);
                height = getDefaultSize(mVideoHeight, heightMeasureSpec);
                if (mVideoWidth * height  > width * mVideoHeight) {
                    height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height  < width * mVideoHeight) {
                    width = height * mVideoWidth / mVideoHeight;
                }
            }
        }
        Log.v(TAG,"onMeasure[" + width + ", " + height + "]");
        setMeasuredDimension(width, height);
    }*/

    // The following is a series of the player listener in callback
    MMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            Log.e(TAG, "MediaPlayer: "+mp+"    Video Size Changed: (" + mVideoWidth + "," + mVideoHeight+")");
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                // Note: can't literally change the size of the SurfaceView, can
                // affect the effect of the PIP
                // getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            }
            if (mContext != null) {
                String classname = mContext.getClass().toString();
                if (!classname.contains("Net")) {
                    ((VideoPlayerActivity)mContext).checkVideoSizeIsSupported();
                }
            }
            if (!bVideoDisplayByHardware) {
                if (mContext != null) {
                    String classname = mContext.getClass().toString();
                    if (!classname.contains("Net")) {
                        if (Tools.isVideoSWDisplayModeOn()) {
                            if (!((VideoPlayerActivity)mContext).getVideoPlayHolder().getDualVideoMode()) {
                                ((VideoPlayerActivity)mContext).setVideoDisplayRotate90(1);
                            }
                        }
                    }
                }
            }
            if (myPlayerCallback != null) {
                myPlayerCallback.onVideoSizeChanged(mMMediaPlayer, mVideoWidth, mVideoHeight, viewId);
            }
        }
    };

    MMediaPlayer.OnPreparedListener mPreparedListener = new MMediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            Log.i(TAG, "******onPrepared*myPlayerCallback*****" + myPlayerCallback);
            if (bVideoDisplayByHardware && mContext != null && !Tools.isRotate90OR270Degree()) {
                String classname = mContext.getClass().toString();
                if (!classname.contains("Net")) {
                    // add for mantis:0824070
                    // remove from monet, mantis:0849243
                    if (Tools.isVideoSWDisplayModeOn() && !Tools.getHardwareName().equals("monet")) {
                        if (!((VideoPlayerActivity)mContext).getVideoPlayHolder().getDualVideoMode()) {
                            ((VideoPlayerActivity)mContext).setVideoDisplayFullScreen(1);
                        }
                    }
                }
            }
            requestLayout();
            invalidate();
            if(Tools.getHardwareName().equals("monet")) {
                Log.i(TAG, "mPreparedListener : is monet platform!");
                if(is4kVideo()) {
                    VideoCodecInfo vcInfo = getVideoInfo();
                    if (vcInfo != null) {
                        String vcType = vcInfo.getCodecType();
                        Log.i(TAG, "mPreparedListener vcType:" + vcType);
                        if(!vcType.equalsIgnoreCase("H265") && !vcType.equalsIgnoreCase("HEVC")) {
                            errorCallback(MediaError.ERROR_UNSUPPORTED);
                            Log.i(TAG, "mPreparedListener : not support,exit!");
                            return;
                        }
                    }
                }
            }
            if (myPlayerCallback != null) {
                myPlayerCallback.onPrepared(mMMediaPlayer, viewId);
                if (mAudioManager != null && viewId == 2) {
                    if (mHandler != null) {
                        if (Tools.isNativeAudioModeOn()) {
                            // SetHpBtMainSource in main source  in native audio flow
                            // and delay 100ms which is suggested by matt.zhan.
                            // mantis: 1132655
                            Message msg = new Message();
                            msg.what = Constants.SETHPBT;
                            Log.i(TAG,"delay SetHpBtMainSource 400ms");
                            mHandler.sendMessageDelayed(msg, 400);

                        } else {
                            // Note:subsource need to set before dual audio switchs on,
                            // so delay 100ms which is suggested by matt.zhan.
                            // mantis: 1096982
                            Message msg = new Message();
                            msg.what = Constants.DualAudioOn;
                            Log.i(TAG,"delay setDualAudioOn 100ms");
                            mHandler.sendMessageDelayed(msg, 100);
                        }
                    }
                }
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            // mSeekWhenPrepared may be changed after seekTo() call
            int seekToPosition = mSeekWhenPrepared;
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            } else {
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            }
            if (is4kVideo()) {
                Tools.setMainPlay4K2KModeOn(true);
            } else {
                Tools.setMainPlay4K2KModeOn(false);
            }
            if (Tools.isThumbnailModeOn()) {
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = Constants.OpenThumbnailPlayer;
                    //mHandler.sendMessageDelayed(msg, 2000);
                    mHandler.sendMessage(msg);
                }
            }
            if (mContext != null) {
                String classname = mContext.getClass().toString();
                if (!classname.contains("Net")) {
                    ((VideoPlayerActivity)mContext).checkVideoCodecIsSupported();
                }
            }
            if (mHandler != null) {
                Message msg = new Message();
                msg.what = Constants.CHECK_IS_SUPPORTED;
                mHandler.sendMessage(msg);
            }
        }
    };

    MMediaPlayer.OnPreparedListener mPreparedListener2 = new MMediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.i(TAG, "******onPrepared2*myPlayerCallback*****" + myPlayerCallback);
            mCurrentState = STATE_PREPARING;
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if (bVideoDisplayByHardware && mContext != null && !Tools.isRotate90OR270Degree()) {
                String classname = mContext.getClass().toString();
                if (!classname.contains("Net")) {
                    if (Tools.isVideoSWDisplayModeOn()) {
                        if (!((VideoPlayerActivity)mContext).getVideoPlayHolder().getDualVideoMode()) {
                            ((VideoPlayerActivity)mContext).setVideoDisplayFullScreen(1);
                        }
                    }
                }
            }
            requestLayout();
            invalidate();
            Log.i(TAG, "MediaPlayer2 start.....");
            mNextMMediaPlayer.start();
            String hardwareName = Tools.getHardwareName();
            if (hardwareName.equals("edison") || hardwareName.equals("kaiser")) {
                Log.i(TAG, "MediaPlayer1 release.....(edison/kaiser flow)");
                mMMediaPlayer.release();
            }
            mMMediaPlayer = null;
            mMMediaPlayer = mNextMMediaPlayer;
            mNextMMediaPlayer = null;
            mCurrentState = STATE_PLAYING;
            String classname = mContext.getClass().toString();
            if ((mVideoWidth == 0 || mVideoHeight == 0) && (!classname.contains("Net"))) {
                Toast toast=Toast.makeText(mContext,
                ((VideoPlayerActivity)mContext).getResources()
                    .getString(R.string.video_display_previous_video_last_frame),
                Toast.LENGTH_LONG);
               ((VideoPlayerActivity)mContext).showMyToast(toast, 4000);
            }
            if (is4kVideo()) {
                Tools.setMainPlay4K2KModeOn(true);
            } else {
                Tools.setMainPlay4K2KModeOn(false);
            }
            if (myPlayerCallback != null) {
                myPlayerCallback.onPrepared(mMMediaPlayer, viewId);
            }
            if (mContext != null) {
                if (!classname.contains("Net")) {
                    ((VideoPlayerActivity)mContext).checkVideoCodecIsSupported();
                }
            }
            if (mHandler != null) {
                Message msg = new Message();
                msg.what = Constants.CHECK_IS_SUPPORTED;
                mHandler.sendMessage(msg);
            }
        }
    };

    private MMediaPlayer.OnCompletionListener mCompletionListener = new MMediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "MediaPlayer  call  onCompletion ..");
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;
            if (Constants.bSupportDivx) {
                String fileName = Tools.getFileName(mUri.getPath());
                ((VideoPlayerActivity)mContext).setFullStopByFileName(Constants.DIVX_FULLSTOP_PLAYBACK,fileName);
            }
            if (myPlayerCallback != null) {
                myPlayerCallback.onCompletion(mMMediaPlayer, viewId);
            }
        }
    };

    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.e(TAG, "Error: " + framework_err + "," + impl_err);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            if(mMMediaPlayer == null) {
                mMMediaPlayer = mNextMMediaPlayer;
                mNextMMediaPlayer = null;
            }
            if (Constants.bSupportDivx) {
                Log.i(TAG, "onError to call checkPreviousVideoIfFullStop");
                checkPreviousVideoIfFullStop();
            }
            /* If an error handler has been supplied, use it and finish. */
            if (myPlayerCallback != null) {
                if (myPlayerCallback.onError(mMMediaPlayer, framework_err, impl_err, viewId)) {
                    return true;
                }
            }
            return true;
        }
    };


    private MMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (myPlayerCallback != null)
                myPlayerCallback.onBufferingUpdate(mp, percent);
        }
    };

    private MMediaPlayer.OnInfoListener mInfoListener = new MMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "onInfo what:" + what  + " extra:" + extra);
            if (MMediaPlayer.MEDIA_INFO_VIDEO_DISPLAY_BY_HARDWARE == what) {
                Log.i(TAG,"******MEDIA_INFO_VIDEO_DISPLAY_BY_HARDWARE******");
                bVideoDisplayByHardware = true;
            }

            if (myPlayerCallback != null) {
                myPlayerCallback.onInfo(mp, what, extra, viewId);
                return true;
            }
            return false;
        }
    };

    private MMediaPlayer.OnTimedTextListener mTimedTextListener = new OnTimedTextListener() {
        @Override
        public void onTimedText(MediaPlayer arg0, TimedText arg1) {
            if (arg1 != null && arg1.getText() != null) {
                Log.i(TAG, "********mTimedTextListener********" + arg1.getText());
                if (myPlayerCallback != null) {
                    myPlayerCallback.onUpdateSubtitle(arg1.getText());
                }
            } else {
                Log.i(TAG, "********mTimedTextListener********  null");
                if (myPlayerCallback != null) {
                    myPlayerCallback.onUpdateSubtitle(" ");
                }
            }
        }
    };

    private MMediaPlayer.OnSeekCompleteListener mMMediaPlayerSeekCompleteListener = new MMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            endSeekTime = System.currentTimeMillis();
            Log.i(TAG, ">>>SeekComplete>>>>>>seek time : "
                    + (endSeekTime - startSeekTime) + " ms   viewId:"+viewId);
            //setVoice(true);
            if (myPlayerCallback != null) {
                myPlayerCallback.onSeekComplete(mp, viewId);
            }
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
            /*
             * if (mSurfaceHolder != null && mMMediaPlayer != null ) {
             * mMMediaPlayer.setDisplay(mSurfaceHolder); }
             */
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
            if (mMMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
                start();
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            // mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);
            Log.i(TAG, "*************surfaceCreated************");
            openPlayer();

        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            Log.i(TAG, "*************surfaceDestroyed************viewId:"+viewId);
            if (viewId == 2) {
                if (!Tools.isNativeAudioModeOn()) {
                    Tools.setHpBtMainSource(true);
                    setDualAudioOn(false);
                }
            }
            if (viewId != 0) {
                if ((mContext instanceof VideoPlayerActivity) &&
                    ((VideoPlayerActivity)mContext).getVideoPlayHolder().getIsSwitchPipMode()) {
                    // pip position change cause sub replayback,so it shoud release mstplayer synchronous
                    // mantis:1105243
                    Log.i(TAG,"IsSwitchPipMode cause surfaceDestroyed");
                    stopPlayback();
                } else {
                    release(true);
                }
            }

        }
    };

    /*
     * release the media player in any state.
     */
    private void release(boolean cleartargetstate) {
        Log.i(TAG, "***********release*******" + (mTargetState == STATE_IDLE));
        if (mTargetState == STATE_IDLE) {
            return;
        }
        mCurrentState = STATE_IDLE;
        if (cleartargetstate) {
            mTargetState = STATE_IDLE;
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

    // There is three case of full-stop
    // (1)double click stop
    // (2)complete playback
    // (3)play A and play B then A is full-stop
    public void setResumePlayState(){
        Log.i(TAG,"setResumePlayState");
        String fileName = Tools.getFileName(mUri.getPath());
        // had got a full-stop state
        mMMediaPlayer.setParameter(Constants.KEY_PARAMETER_SET_RESUME_PLAY, 0);
        ((VideoPlayerActivity)mContext).setFullStopByFileName(Constants.DIVX_RESUMEPLAY_PLAYBACK,fileName);

    }

    public void checkPreviousVideoIfFullStop(){
        String presentFileName = Tools.getFileName(mUri.getPath());
        String previousVideoFileName = ((VideoPlayerActivity)mContext).getmDivxPresentFileName();
        Log.i(TAG,"checkPreviousVideoIfFullStop:"+presentFileName);
        Log.i(TAG,"checkPreviousVideoIfFullStop:"+previousVideoFileName);
        if (previousVideoFileName != null
            && (!(previousVideoFileName.equals(presentFileName)))){

            ((VideoPlayerActivity)mContext).setFullStopByFileName(Constants.DIVX_FULLSTOP_PLAYBACK,previousVideoFileName);

        } else {
            Log.i(TAG,"previousVideoFileName is equals to presentFileName");;
        }
        ((VideoPlayerActivity)mContext).setmDivxPresentFileName(presentFileName);
    }

    public void setPlayingState() {
        mCurrentState = STATE_PREPARED;
        mTargetState = STATE_PLAYING;
    }

    public void start() {
        if (isInPlaybackState()) {
            mMMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    public void pause() {
        Log.i(TAG, "pause: mMMediaPlayer.isPlaying() " + mMMediaPlayer.isPlaying());
        if (isInPlaybackState()) {
            if (mMMediaPlayer.isPlaying()) {
                Log.i(TAG, "pause: ");
                mMMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public boolean isVideoWidthHeightEqualZero(){
        if (isInPlaybackState()) {
            if (mVideoWidth == 0 && mVideoHeight == 0) {
                Log.i(TAG,"isVideoWidthHeightEqualZero yes");
                return true;
            }
        }
        return false;
    }

    /**
     * cache duration as mDuration for faster access.
     *
     * @return
     */
    public int getDuration() {
        if (isInPlaybackState()) {
            if (mDuration > 0) {
                return mDuration;
            }
            mDuration = mMMediaPlayer.getDuration();
            return mDuration;
        }
        mDuration = -1;
        return mDuration;
    }

    /**
     * Get the current play time.
     *
     * @return
     */
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mMMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * Jump to a certain time.
     *
     * @param msec
     */
    public void seekTo(int msec) {
        Log.i(TAG,"seekTo start");
        if (isInPlaybackState()) {
            if (msec>getDuration()) {
                Log.i(TAG,"seekTo is bigger than Duration");
                return;
            }
            startSeekTime = System.currentTimeMillis();
            if (bBDISO && mDatasource != null) {
                mMMediaPlayer.DemuxReset();
                mDatasource.seekByTime(msec);
                Tools.CURPOS = (int)Bluray.tellTime()/90;
                // Tools.CURPOS = msec;
                Log.i(TAG,"Current seeked time is:"+Tools.CURPOS+" the msec:"+msec);
            } else {
                setVoice(false);
                mMMediaPlayer.seekTo(msec);
            }
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
        Log.i(TAG,"seekTo end");
    }

    public boolean isPlaying() {
        Log.i(TAG, "isPlaying: ");
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
        Log.i(TAG, "isInPlaybackState: ");
        return (mMMediaPlayer != null
                && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE
                && mCurrentState != STATE_PREPARING
                && mCurrentState != STATE_STREAMLESS_TO_NEXT);
    }

    public int getMediaParam(int param) {
        Log.i("andrew", "the mMMediaPlayer:" + mMMediaPlayer);
        if (mMMediaPlayer != null) {
            return mMMediaPlayer.getIntParameter(param);
        } else
            return 0;
    }

    public void setVideoScale(int leftMargin, int topMargin, int width, int height) {
        Log.i(TAG,"setVideoScale width height :"+String.valueOf(width)+" "+String.valueOf(height));
        LayoutParams lp = getLayoutParams();
        lp.height = height;
        lp.width = width;

        setLayoutParams(lp);
    }

    public void setVideoScaleFrameLayout(int leftMargin, int topMargin, int width, int height) {

        LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            // The following the forced outfit in the decision must be based on
            // the XML type.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
            params.leftMargin = leftMargin;
            params.rightMargin = leftMargin;
            params.topMargin = topMargin;
            params.bottomMargin = topMargin;
            params.width = width;
            params.height = height;
            setLayoutParams(params);
        }
    }

    public void setVideoScaleLinearLayout(int leftMargin, int topMargin, int width, int height) {

        LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            // The following the forced outfit in the decision must be based on
            // the XML type.
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutParams;
            params.leftMargin = leftMargin;
            params.rightMargin = leftMargin;
            params.topMargin = topMargin;
            params.bottomMargin = topMargin;
            params.width = width;
            params.height = height;

            setLayoutParams(params);
        }
    }

    public double calculateZoom(double ScrennWidth, double ScrennHeight) {
        double dRet = 1.0;
        double VideoWidth = (double) mVideoWidth;
        double VideoHeight = (double) mVideoHeight;
        double dw = ScrennWidth / VideoWidth;
        double dh = ScrennHeight / VideoHeight;
        if (dw > dh)
            dRet = dh;
        else
            dRet = dw;

        return dRet;
    }

    public MMediaPlayer getMMediaPlayer() {
        return mMMediaPlayer;
    }

    public void setVoice(boolean isSetOpen) {
        Log.i(TAG,"booelan setVoice:"+isSetOpen);
        if (isInPlaybackState()) {
            if (isSetOpen) {
                // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                // currentVoice, AudioManager.FLAG_SHOW_UI);
                Log.i(TAG,"mMMediaPlayer.setVolume:"+isSetOpen);
                mMMediaPlayer.setVolume(currentVoice, currentVoice);
                isVoiceOpen = true;
            } else {
                // currentVoice =
                // mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                // 0,
                // AudioManager.FLAG_SHOW_UI);
                Log.i(TAG,"mMMediaPlayer.setVolume:"+isSetOpen);
                mMMediaPlayer.setVolume(0, 0);
                isVoiceOpen = false;
            }
        }
    }

    public void addVoice(boolean flag) {
        if (mMMediaPlayer != null) {
            int voice = getVoice();
            if (flag) {
                if (voice < 10) {
                    voice = voice + 1;
                }
            } else {
                if (voice > 0) {
                    voice = voice - 1;
                }
            }
            setVoice(voice);
            // mMMediaPlayer.setVolume(currentVoice, currentVoice);
        }
    }

    public void setVoice(int voice) {
        Log.i(TAG,"int setVoice:"+voice);
        if (isInPlaybackState()) {
            if (voice >= 0 && voice <= 10) {
                currentVoice = voice * 0.1f;
            }
            Log.i(TAG, "******currentVoice*******" + currentVoice);
            mMMediaPlayer.setVolume(currentVoice, currentVoice);
        }
    }

    public int getVoice() {
        return (int) (currentVoice * 10);
    }

    public boolean isVoiceOpen() {
        return isVoiceOpen;
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

    /****************************************/
    // mstar Extension APIs start
    /**
     * Set the speed of the video broadcast.
     *
     * @param speed
     * @return
     */
    public boolean setPlayMode(int speed) {
        Log.i(TAG, "setPlayMode: ");
        if (speed < -32 || speed > 32)
            return false;

        if (isInPlaybackState()) {
            Log.i(TAG, "****setPlayMode***" + speed);
            mMMediaPlayer.start();
            return mMMediaPlayer.setPlayMode(speed);
        }
        return false;
    }

    /**
     * For video broadcast speed.
     *
     * @return
     */
    public int getPlayMode() {
        if (isInPlaybackState()) {
            return mMMediaPlayer.getPlayMode();
        }
        return 64;
    }

    /**
     * get audio codec type.
     *
     * @return
     */
    public String getAudioCodecType() {
        if (isInPlaybackState()) {
            return mMMediaPlayer.getAudioCodecType();
        }
        return null;
    }

    /**
     * get video Info.
     *
     * @return
     */
    public VideoCodecInfo getVideoInfo() {
        if (isInPlaybackState()) {
            return mMMediaPlayer.getVideoInfo();
        }
        return null;
    }

    /**
     * Adds an external timed text source file.
     *
     * Currently supported format is SubRip with the file extension .srt, case insensitive.
     * Note that a single external timed text source may contain multiple tracks in it.
     * One can find the total number of available tracks using {@link #getTrackInfo()} to see what
     * additional tracks become available after this method call.
     *
     * @param path The file path of external timed text source file.
     * @param mimeType The mime type of the file. Must be one of the mime types listed above.
     * @throws IOException if the file cannot be accessed or is corrupted.
     * @throws IllegalArgumentException if the mimeType is not supported.
     * @throws IllegalStateException if called in an invalid state.
     */
    public void addTimedTextSource(String path, String mimeType) {
        if (isInPlaybackState()) {
            Log.i(TAG,"addTimedTextSource path:" + path + " mimeType:" + mimeType);
            try {
                mMMediaPlayer.addTimedTextSource(path, mimeType);
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
    }

    /**
     * Returns an array of track information.
     *
     * @return Array of track info. The total number of tracks is the array length.
     * Must be called again if an external timed text source has been added after any of the
     * addTimedTextSource methods are called.
     * @throws IllegalStateException if it is called in an invalid state.
     */
    public MediaPlayer.TrackInfo[] getTrackInfo() {
        if (isInPlaybackState()) {
            Log.i(TAG, "getTrackInfo");
            try {
                return mMMediaPlayer.getTrackInfo();
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
        return null;
    }

    /**
     * Returns an array of track information.
     *
     * @return Array of track info. The total number of tracks is the array length.
     * Must be called again if an external timed text source has been added after any of the
     * addTimedTextSource methods are called.
     * @throws IllegalStateException if it is called in an invalid state.
     */
    public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
    public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
    public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    public static final int MEDIA_TRACK_TYPE_TIMEDBITMAP = 5;

    public MMediaPlayer.MsTrackInfo[] getMsTrackInfo() {
        if (isInPlaybackState()) {
            Log.i(TAG, "getMsTrackInfo");
            try {
                return mMMediaPlayer.getMsTrackInfo();
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
        return null;
    }

    public MMediaPlayer.MsTrackInfo getMsTrackInfo(int index) {
        if (isInPlaybackState()) {
            Log.i(TAG, "getMsTrackInfo index:" + index);
            try {
                MMediaPlayer.MsTrackInfo[] trackInfo = mMMediaPlayer.getMsTrackInfo();
                if (trackInfo != null && trackInfo.length > index) {
                    return trackInfo[index];
                }
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
        return null;
    }

    /*
    public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
    public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
    public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
    public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
    public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
    public static final int MEDIA_TRACK_TYPE_TIMEDBITMAP = 5;
    */

    public int getMsAudioTrackCount() {
        return getMsTrackInfoCount(MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_AUDIO);
    }

    public int getMsTimedTextTrackCount() {
        return getMsTrackInfoCount(MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT);
    }

    public int getMsTimedBitmapTrackCount() {
        return getMsTrackInfoCount(MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDBITMAP);
    }

    private int[] mMsAudioTrackIndex = null;
    private int[] mMsTimedTextTrackIndex = null;
    private int[] mMsTimedBitmapTrackIndex = null;

    public int getMsTrackSelectedIndex(int type, int index) {
        switch (type) {
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_AUDIO:
                if (mMsAudioTrackIndex != null) {

                }
                break;
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT:
                if (mMsTimedTextTrackIndex != null) {

                }
                break;
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDBITMAP:
                if (mMsTimedBitmapTrackIndex != null) {

                }
                break;
            default:
                break;
        }
        return -1;
    }

    public void setMsTrackIndex(int type, int index) {
        switch (type) {
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_AUDIO:
                if (mMsAudioTrackIndex != null) {
                    selectTrack(mMsAudioTrackIndex[index]);
                }
                break;
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT:
                if (mMsTimedTextTrackIndex != null) {
                    selectTrack(mMsTimedTextTrackIndex[index]);
                }
                break;
            case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDBITMAP:
                if (mMsTimedBitmapTrackIndex != null) {
                    selectTrack(mMsTimedBitmapTrackIndex[index]);
                }
                break;
            default:
                break;
        }
    }

    public int getMsTrackInfoCount(int type) {
        Log.i(TAG, "getMsTrackInfoCount type:" + type);
        int getMsTrackInfoCount = 0;
        if (isInPlaybackState()) {
            MMediaPlayer.MsTrackInfo []getMsTrackInfo = getMsTrackInfo();
            if (getMsTrackInfo != null) {
                Log.i(TAG, "getMsTrackInfo.length:" + getMsTrackInfo.length);

                // Get TrackType Count
                int length = getMsTrackInfo.length;
                for (int i = 0; i < length; i++) {
                    Log.i(TAG, "getMsTrackInfo[" + i + "].getTrackType:" + getMsTrackInfo[i].getTrackType());
                    if (type == getMsTrackInfo[i].getTrackType()) {
                        getMsTrackInfoCount++;
                    }
                }

                // Product Model: MStar Android TV,19,4.4.4
                Log.i(TAG, "Product Model: " + Build.MODEL + ","  + Build.VERSION.SDK_INT + "," + Build.VERSION.RELEASE);
                // Check if we're running on Android 5.0 or higher
                // public static final int LOLLIPOP = 21;
                if (Build.VERSION.SDK_INT < 21) {
                    int[] trackIndex = null;
                    if (getMsTrackInfoCount > 0) {
                        trackIndex = new int[getMsTrackInfoCount];
                    }

                    // Store TrackType Index
                    int j = 0;
                    for (int i = 0; i < length; i++) {
                        Log.i(TAG, "getMsTrackInfo[" + i + "].getTrackType:" + getMsTrackInfo[i].getTrackType());
                        if (type == getMsTrackInfo[i].getTrackType()) {
                            trackIndex[j++] = i;
                        }
                    }

                    switch (type) {
                        case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_AUDIO:
                            mMsAudioTrackIndex = new int[getMsTrackInfoCount];
                            mMsAudioTrackIndex = trackIndex;
                            break;
                        case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT:
                            mMsTimedTextTrackIndex= new int[getMsTrackInfoCount];
                            mMsTimedTextTrackIndex = trackIndex;
                            break;
                        case MMediaPlayer.MsTrackInfo.MEDIA_TRACK_TYPE_TIMEDBITMAP:
                            mMsTimedBitmapTrackIndex = new int[getMsTrackInfoCount];
                            mMsTimedBitmapTrackIndex = trackIndex;
                            break;
                        default:
                            break;
                    }
                } else {
                    // ro.build.version.release
                }
                Log.i(TAG, "getMsTrackInfoCount:" + getMsTrackInfoCount);
            }
        }
        return getMsTrackInfoCount;
    }


    /**
     * Selects a track.
     * <p>
     * If a MediaPlayer is in invalid state, it throws an IllegalStateException exception.
     * If a MediaPlayer is in <em>Started</em> state, the selected track is presented immediately.
     * If a MediaPlayer is not in Started state, it just marks the track to be played.
     * </p>
     * <p>
     * In any valid state, if it is called multiple times on the same type of track (ie. Video,
     * Audio, Timed Text), the most recent one will be chosen.
     * </p>
     * <p>
     * The first audio and video tracks are selected by default if available, even though
     * this method is not called. However, no timed text track will be selected until
     * this function is called.
     * </p>
     * <p>
     * Currently, only timed text tracks or audio tracks can be selected via this method.
     * In addition, the support for selecting an audio track at runtime is pretty limited
     * in that an audio track can only be selected in the <em>Prepared</em> state.
     * </p>
     * @param index the index of the track to be selected. The valid range of the index
     * is 0..total number of track - 1. The total number of tracks as well as the type of
     * each individual track can be found by calling {@link #getTrackInfo()} method.
     * @throws IllegalStateException if called in an invalid state.
     *
     * @see MediaPlayer#getTrackInfo
     */
    public void selectTrack(int index) {
        if (isInPlaybackState()) {
            Log.i(TAG, "selectTrack");
            try {
                mMMediaPlayer.selectTrack(index);
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
    }

    /**
     * Deselect a track.
     * <p>
     * Currently, the track must be a timed text track and no audio or video tracks can be
     * deselected. If the timed text track identified by index has not been
     * selected before, it throws an exception.
     * </p>
     * @param index the index of the track to be deselected. The valid range of the index
     * is 0..total number of tracks - 1. The total number of tracks as well as the type of
     * each individual track can be found by calling {@link #getTrackInfo()} method.
     * @throws IllegalStateException if called in an invalid state.
     *
     * @see MediaPlayer#getTrackInfo
     */
    public void deselectTrack(int index) {
        if (isInPlaybackState()) {
            Log.i(TAG, "deselectTrack");
            try {
                mMMediaPlayer.deselectTrack(index);
            } catch (Exception e) {
                Log.i(TAG, "Exception:" + e);
            }
        }
    }

    // Android 5.0 or higher API
    public SubtitleTrack getSelectedTrack() {
        // Check if we're running on Android 5.0 or higher
        // public static final int LOLLIPOP = 21;
        if (Build.VERSION.SDK_INT >= 21) {
            if (isInPlaybackState()) {
                Log.i(TAG, "getSelectedTrack");
                try {
                    // mMMediaPlayer.getSelectedTrack();
                    try {
                        Class clz = Class.forName("com.mstar.android.media.MMediaPlayer");
                        Method getSelectedTrack = clz.getDeclaredMethod("getSelectedTrack");
                        return (SubtitleTrack)getSelectedTrack.invoke(clz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Exception:" + e);
                }
            }
        }
        return null;

    }


    /**
     * check mvc.
     */
    public boolean isMVCSource() {
        if (isInPlaybackState()) {
            VideoCodecInfo vcInfo = mMMediaPlayer.getVideoInfo();
            if (vcInfo != null) {
                String vcType = vcInfo.getCodecType();
                Log.i(TAG, "getCodecType:" + vcType);
                if (MVC.equals(vcType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean imageRotate(int degrees) {
        return mMMediaPlayer.ImageRotate(degrees, false);
    }

    public boolean imageRotate(int degrees , MMediaPlayer mp) {
        return mp.ImageRotate(degrees, false);
    }

    public void setDualAudioOn(boolean onOff) {
        if (Tools.isNativeAudioModeOn()) {
            return;
        }
        if (mAudioManager != null) {
            if(onOff) {
                if(!((VideoPlayerActivity)mContext).bDualAudioOn) {
                    Log.i(TAG, "*********DualAudioOn******");
                    ((VideoPlayerActivity)mContext).bDualAudioOn = true;
                    mAudioManager.setParameters("DualAudioOn");
                }
            } else {
                if(((VideoPlayerActivity)mContext).bDualAudioOn) {
                    Log.i(TAG, "*********DualAudioOff*****");
                    ((VideoPlayerActivity)mContext).bDualAudioOn = false;
                    mAudioManager.setParameters("DualAudioOff");
                }
            }
        }
    }
    // mstar Extension APIs end
    /********************************************************/
}
