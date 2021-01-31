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
package com.walton.filebrowser.ui.music;

import java.io.IOException;
import java.lang.ref.WeakReference;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.walton.filebrowser.IMediaService;
import com.walton.filebrowser.IMusicStatusListener;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.video.MediaError;
import com.mstar.android.media.MMediaPlayer;
import com.walton.filebrowser.util.Tools;

/**
 *
 * Music broadcast service
 *
 * @author
 */
public class MediaService extends Service {
    private static final String TAG = "MediaService";
    private final IBinder mBinder = (IBinder) new ServiceStub(this);
    // mediaplayer
    private MMediaPlayer player = null;
    private IMusicStatusListener mMusicStatusListener;
    private int mDuration;
    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState = STATE_IDLE;
    @SuppressWarnings("unused")
    private int mTargetState = STATE_IDLE;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    // recording the seek position while preparing
    @SuppressWarnings("unused")
    private int mSeekWhenPrepared;

    // ///////////////////////////////////////////////////////////////////////////////////////////////
    // Service Class rewrite area
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null) {
            player = new MMediaPlayer();
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand player");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy player");
        stop();
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////
    // Internal function area
    public void openPlayer(String path) {
        // close the built-in music service of android
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        this.sendBroadcast(i);

        Log.d(TAG, "openPlayer, current thread id : "
                + Thread.currentThread().getId());
        String errorMessage = getResources().getString(
                R.string.video_media_other_error_unknown);
        release(false);
        try {
            player = new MMediaPlayer();
            player.reset();
            mSeekWhenPrepared = 0;
            player.setOnErrorListener(mErrorListener);
            player.setOnSeekCompleteListener(mSeekCompleteListener);
            player.setOnCompletionListener(mCompletionListener);
            player.setOnInfoListener(mInfoListener);
            player.setOnPreparedListener(mPreparedListener);
            player.setDataSource(path);
            player.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (IllegalStateException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (IOException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (IllegalArgumentException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (SecurityException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            e.printStackTrace();
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void next(String path) {
        stop();
        openPlayer(path);
    }

    public void pause() {
        String errorMessage = getResources().getString(
                R.string.video_media_other_error_unknown);
        if (isInPlaybackState() && player.isPlaying()) {
            try {
                player.pause();
                mCurrentState = STATE_PAUSED;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public void stop() {
        String errorMessage = getResources().getString(
                R.string.video_media_other_error_unknown);
        if (player != null) {
            try {
                mCurrentState = STATE_IDLE;
                mTargetState = STATE_IDLE;
                Log.d(TAG, "stop player start");
                player.stop();
                player.release();
                player = null;
                Log.d(TAG, "stop player done");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public boolean isPlaying() {
        return (isInPlaybackState() && player.isPlaying());
    }

    public void seekTo(int posiztion) {
        String errorMessage = getResources().getString(
                R.string.video_media_other_error_unknown);
        // Direct drag-and-drop transactions state wrong cause
        if (isInPlaybackState() && posiztion >= 0 && posiztion <= getDuration()) {
            try {
                mSeekWhenPrepared = 0;
                player.seekTo(posiztion);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMusicStatusListener.musicPlayException(errorMessage);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            mSeekWhenPrepared = posiztion;
        }
    }

    public long getCurrentPosition() {
        if (isInPlaybackState()) {
            return player.getCurrentPosition();
        }
        return -1;
    }

    public void start() {
        String errorMessage = getResources().getString(
                R.string.video_media_other_error_unknown);
        if (!isInPlaybackState())
            return;
        try {
            player.start();
            mCurrentState = STATE_PLAYING;
            mTargetState = STATE_PLAYING;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mMusicStatusListener.musicPlayException(errorMessage);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
        try {
            mMusicStatusListener.handleSongSpectrum();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public long getDuration() {
        if (isInPlaybackState()) {
            mDuration = player.getDuration();
            return mDuration;
        }
        mDuration = -1;
        return mDuration;
    }

    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }

    private boolean isInPlaybackState() {
        return (player != null && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    public boolean canPause() {
        return mCanPause;
    }

    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    // Unknown error handling
    private String processErrorUnknown(MediaPlayer mp, int what, int extra) {
        int strID = R.string.video_media_error_unknown;
        switch (extra) {
        case MediaError.ERROR_MALFORMED:
            strID = R.string.video_media_error_malformed;
            break;
        case MediaError.ERROR_IO:
            strID = R.string.video_media_error_io;
            break;
        case MediaError.ERROR_UNSUPPORTED:
            strID = R.string.video_media_error_unsupported;
            break;
        case MediaError.ERROR_FILE_FORMAT_UNSUPPORT:
            strID = R.string.video_media_error_format_unsupport;
            break;
        case MediaError.ERROR_NOT_CONNECTED:
            strID = R.string.video_media_error_not_connected;
            break;
        case MediaError.ERROR_AUDIO_UNSUPPORT:
            strID = R.string.video_media_error_audio_unsupport;
            break;
        case MediaError.ERROR_VIDEO_UNSUPPORT:
            strID = R.string.video_media_error_video_unsupport;
            break;
        case MediaError.ERROR_DRM_NO_LICENSE:
            strID = R.string.video_media_error_no_license;
            break;
        case MediaError.ERROR_DRM_LICENSE_EXPIRED:
            strID = R.string.video_media_error_license_expired;
            break;
        }
        String strMessage = getResources().getString(strID);
        return strMessage;
    }

    /*
     *
     * release the media player in any state
     */
    private void release(boolean cleartargetstate) {
        if (player != null) {
            player.release();
            player = null;
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////
    // Callback function real
    private MMediaPlayer.OnInfoListener mInfoListener = new MMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "MMediaPlayer onInfo what:" + what + " extra:" + extra);
            //ignore info
            /*String strMessage = "";
            switch (what) {
            case MMediaPlayer.MEDIA_INFO_AUDIO_UNSUPPORT:
                strMessage = getResources().getString(R.string.video_media_error_audio_unsupport);
                break;
            case MMediaPlayer.MEDIA_INFO_VIDEO_UNSUPPORT:
                strMessage = getResources().getString(R.string.video_media_error_video_unsupport);
                break;
            default:
                break;
            }
            Log.i(TAG, "****onInfo****" + what + " " + extra + " " + strMessage);
            try {
                mMusicStatusListener.handleMessageInfo(strMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
            return false;
        }
    };
    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(TAG, "onError, what : " + what + " extra : " + extra);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            // To resolve the error string, the string passed directly
            String strMessage = "";
            switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "Play Error::: MEDIA_ERROR_SERVER_DIED");
                player.release();
                player = null;
                player = new MMediaPlayer();
                strMessage = getResources().getString(R.string.video_media_error_server_died);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "Play Error::: MEDIA_ERROR_UNKNOWN");
                strMessage = processErrorUnknown(mp, what, extra);
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "Play Error::: MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                strMessage = getResources().getString(R.string.video_media_error_not_valid);
                break;
            default:
                Log.d(TAG, "Play Error::: default error!");
                strMessage = getResources().getString(R.string.video_media_other_error_unknown);
                break;
            }
            try {
                return mMusicStatusListener.musicPlayErrorWithMsg(strMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return false;
        }
    };
    private MMediaPlayer.OnPreparedListener mPreparedListener = new MMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, "onPrepared");
            mCurrentState = STATE_PREPARED;
            mCanPause = mCanSeekBack = mCanSeekForward = true;
            try {
                mMusicStatusListener.musicPrepared();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            start();

            //player.setVolume(1.0f, 1.0f);
        }
    };
    private MMediaPlayer.OnCompletionListener mCompletionListener = new MMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion");
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;
            try {
                mMusicStatusListener.musicCompleted();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private MMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            if (player.isPlaying()) {
                Log.d(TAG, "onSeekComplete");
                try {
                    start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    try {
                        mMusicStatusListener.musicSeekCompleted();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    };

    private class ServiceStub extends IMediaService.Stub {
        WeakReference<MediaService> mService;

        ServiceStub(MediaService service) {
            mService = new WeakReference<MediaService>(service);
        }

        public void stop() {
            mService.get().stop();
        }

        public void pause() {
            mService.get().pause();
        }

        public void initPlayer(String path) {
            Log.d(TAG, "initPlayer, current thread id : "
                    + Thread.currentThread().getId());
            String musicPath = path;
            if (Tools.isSambaPlaybackUrl(path)) {
                musicPath = Tools.convertToHttpUrl(path);
            }
            Log.i(TAG,"musicPath:"+musicPath);
            mService.get().openPlayer(musicPath);
        }

        public void next(String path) {
            mService.get().next(path);
        }

        public void continuePlay() {
            mService.get().start();
        }

        public void playerToPosiztion(int position) {
            mService.get().seekTo(position);
        }

        public boolean isPlaying() {
            if (mService.get() != null) {
                return mService.get().isPlaying();
            } else {
                return false;
            }
        }

        public String getAudioCodecType() {
            if (player != null) {
                return player.getAudioCodecType();
            }
            return null;
        }

        public long getCurrentPosition() {
            return mService.get().getCurrentPosition();
        }

        public long getDuration() {
            return mService.get().getDuration();
        }

        public int getAudioSessionId() {
            return mService.get().getAudioSessionId();
        }

        public void setMusicStatusListener(
                IMusicStatusListener musicStatusListener)
                throws RemoteException {
            mMusicStatusListener = musicStatusListener;
        }
    }
}
