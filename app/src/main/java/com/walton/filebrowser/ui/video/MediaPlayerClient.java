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
package com.walton.filebrowser.ui.video;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import com.mstar.android.media.MMediaPlayer;
import android.os.SystemProperties;

/**
 * Created by nate.luo on 15-4-14.
 */

public class MediaPlayerClient {
    private static final String TAG = "MediaPlayerClient";
    private static final int KEY_PARAMETER_SET_DUAL_DECODE_PIP = 2024;
    public static final String ACTION_START_FLOAT_VIDEO_SERVICE = "com.mstar.android.intent.action.START_FLOAT_VIDEO_SERVICE";
    private static final String MEDIA_PLAYER_NEXT = "MEDIA_PLAYER_NEXT";
    private Context mContext = null;
    private MMediaPlayer mMMediaPlayer = null;
    private Uri mUri = null;
    private SurfaceHolder mSurfaceHolder = null;

    public MediaPlayerClient(Context context) {
        mContext = context;
    }

    public void setVideoPath(String path, SurfaceHolder surfaceHolder) {
        Log.i(TAG, "setVideoPath path:" + path);
        mUri = Uri.parse(path);
        mSurfaceHolder = surfaceHolder;
        openPlayer();
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    /**
     * Start player.
     */
    public void openPlayer() {
        Log.i(TAG, "openPlayer mUri:" + mUri + " mSurfaceHolder:" + mSurfaceHolder);
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }

        // close the built-in music service of android
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);


        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        try {
            mMMediaPlayer = new MMediaPlayer();
            mMMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMMediaPlayer.setOnErrorListener(mErrorListener);
            mMMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMMediaPlayer.setOnInfoListener(mInfoListener);
            mMMediaPlayer.setOnTimedTextListener(mTimedTextListener);
            mMMediaPlayer.setOnSeekCompleteListener(mMMediaPlayerSeekCompleteListener);
            mMMediaPlayer.setDataSource(mContext, mUri);
            setParameter();
            mMMediaPlayer.setDisplay(mSurfaceHolder);
            mMMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMMediaPlayer.setScreenOnWhilePlaying(true);
            mMMediaPlayer.prepareAsync();

        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } catch (IllegalStateException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        } catch (SecurityException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            return;
        }
    }

    private void setParameter() {
        if (SystemProperties.getBoolean("mstar.floatvideo.dualdecodepip", false)) {
            Log.i(TAG, "float video set KEY_PARAMETER_SET_DUAL_DECODE_PIP");
            mMMediaPlayer.setParameter(KEY_PARAMETER_SET_DUAL_DECODE_PIP, 1);
        } else {
            Log.i(TAG, "float video use main decode");
        }
    }

    public void start() {
        Log.i(TAG, "start");
        if (mMMediaPlayer != null) {
            mMMediaPlayer.start();
        }
    }

    public void pause() {
        Log.i(TAG, "pause");
        if (mMMediaPlayer != null) {
            mMMediaPlayer.pause();
        }

    }

    public boolean isPlaying() {
        if (mMMediaPlayer != null) {
            return mMMediaPlayer.isPlaying();
        }
        return false;
    }

    public boolean isMediaPlayerCreated() {
        if (mMMediaPlayer != null) {
            return true;
        }
        return false;
    }
    /**
     * call before play next.
     */
    public void stopPlayback() {
        Log.i(TAG, "stopPlayback");
        if (mMMediaPlayer != null) {
            mMMediaPlayer.stop();
            mMMediaPlayer.release();
            mMMediaPlayer = null;
        }
    }

    public void release(boolean bAsyncRelease) {
        Log.i(TAG, "release mMMediaPlayer:" + mMMediaPlayer + " bAsyncRelease:" + bAsyncRelease);
        if (bAsyncRelease) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "thread mMMediaPlayer:" + mMMediaPlayer);
                    if (mMMediaPlayer != null && mMMediaPlayer.isPlaying()) {
                        try {
                            Log.i(TAG, "mMMediaPlayer.stop ---------------- begin");
                            mMMediaPlayer.stop();
                        } catch (IllegalStateException e) {
                            Log.i(TAG, "stop fail! please try again!");
                            try {
                                this.wait(2000);
                                Log.i(TAG, "IllegalStateException mMMediaPlayer.stop ---------------- begin");
                                mMMediaPlayer.stop();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    if (mMMediaPlayer != null) {
                        Log.i(TAG, "*****release start*****  ");
                        mMMediaPlayer.release();// release will done reset
                        Log.i(TAG, "*****release end*****   ");
                    }
                    mMMediaPlayer = null;
                }
            }).start();
        } else {
            if (mMMediaPlayer != null) {
                Log.i(TAG, "***********release Player ");
                mMMediaPlayer.release();
                mMMediaPlayer = null;
            }
        }

    }

    // The following is a series of the player listener in callback
    MMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.d(TAG, "onVideoSizeChanged width: " + width + ", height " + height);
        }
    };

    MMediaPlayer.OnPreparedListener mPreparedListener = new MMediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, "onPrepared getVideoWidth: " + mp.getVideoWidth() + ", getVideoHeight " + mp.getVideoHeight());
            start();
        }
    };

    private MMediaPlayer.OnCompletionListener mCompletionListener = new MMediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "OnCompletion");
            Intent intent = new Intent(ACTION_START_FLOAT_VIDEO_SERVICE);
            intent.putExtra("command", MEDIA_PLAYER_NEXT);
            mContext.startService(intent);
        }
    };

    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.e(TAG, "onError: " + framework_err + "," + impl_err);
            return true;
        }
    };

    private MMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MMediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.i(TAG, "OnBufferingUpdateListener");
        }
    };

    private MMediaPlayer.OnInfoListener mInfoListener = new MMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "onInfo extra:" + extra);
            switch (what) {
                case MMediaPlayer.MEDIA_INFO_SUBTITLE_UPDATA:
                    Log.i(TAG, "MEDIA_INFO_SUBTITLE_UPDATA");
                    break;
                case MMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.i(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                    return true;
                case MMediaPlayer.MEDIA_INFO_AUDIO_UNSUPPORT:
                    Log.i(TAG, "MEDIA_INFO_AUDIO_UNSUPPORT");
                    break;
                case MMediaPlayer.MEDIA_INFO_VIDEO_UNSUPPORT:
                    Log.i(TAG, "MEDIA_INFO_VIDEO_UNSUPPORT");
                    break;
                default:
                    Log.i(TAG, "Play onInfo::: default onInfo!");
                    break;
            }
            return true;
        }
    };

    private MMediaPlayer.OnTimedTextListener mTimedTextListener = new MediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(MediaPlayer arg0, TimedText arg1) {
            Log.i(TAG, "OnTimedTextListener");
        }
    };

    private MMediaPlayer.OnSeekCompleteListener mMMediaPlayerSeekCompleteListener = new MMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.i(TAG, "OnSeekCompleteListener");
        }
    };
}
