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

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.walton.filebrowser.ILocalMediaService;
import com.walton.filebrowser.ILocalMediaStatusListener;

import java.util.Map;

/**
 * Created by nate.luo on 14-7-16.
 */

public class LocalMediaService extends Service {

    private static final String TAG = "LocalMediaService";
    private static final String ACTION_START_LOCAL_MEDIA = "com.mstar.android.intent.action.START_LOCAL_MEDIA";
    private static final String URL_FROM = "URL_FROM";
    private static final String LOCAL_MEDIA_SERVICE = "LOCAL_MEDIA_SERVICE";
    private static final String MEDIA_PLAYER_EXIT = "MEDIA_PLAYER_EXIT";
    private static final String MEDIA_PLAYER_SET_DURATION = "MEDIA_PLAYER_SET_DURATION";
    private static final String MEDIA_PLAYER_SET_DURATION_VALUE = "MEDIA_PLAYER_SET_DURATION_VALUE";
    private static final String MEDIA_PLAYER_SEEK = "MEDIA_PLAYER_SEEK";
    private static final String MEDIA_PLAYER_SEEK_TO_POSITION = "MEDIA_PLAYER_SEEK_TO_POSITION";
    private static final String MEDIA_PLAYER_ON_INFO = "MEDIA_PLAYER_ON_INFO";
    private static final String MEDIA_PLAYER_ON_ERROR = "MEDIA_PLAYER_ON_ERROR";
    private static final String MEDIA_PLAYER_ON_WHAT = "MEDIA_PLAYER_ON_WHAT";
    private static final String MEDIA_PLAYER_ON_EXTRA = "MEDIA_PLAYER_ON_EXTRA";
    private static final String MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED = "MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED";
    private static final String MEDIA_PLAYER_VIDEO_WIDTH = "MEDIA_PLAYER_VIDEO_WIDTH";
    private static final String MEDIA_PLAYER_VIDEO_HEIGHT = "MEDIA_PLAYER_VIDEO_HEIGHT";
    private static final String MEDIA_PLAYER_ON_PREPARED = "MEDIA_PLAYER_ON_PREPARED";
    private static final String MEDIA_PLAYER_ON_COMPLETION = "MEDIA_PLAYER_ON_COMPLETION";
    private ILocalMediaStatusListener mLocalMediaStatusListener;
    private int mCurrentPlayerPosition = 0;
    private int mDuration = 0;
    private int mVideoWidth = 1920;
    private int mVideoHeight = 1080;
//    private final IMediaService.Stub mBinder = new MediaServiceStub(this);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "LocalMediaService onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.i(TAG, "onStartCommand " + "action:" + action + " command:" + cmd);

            try {
                if (MEDIA_PLAYER_EXIT.equals(cmd)) {
                    handlePlayExitByUser();
                } else if (MEDIA_PLAYER_SET_DURATION.equals(cmd)) {
                    int duration = intent.getIntExtra(MEDIA_PLAYER_SET_DURATION_VALUE, 0);
                    setVideoDuration(duration);
                } else if (MEDIA_PLAYER_SEEK.equals(cmd)) {
                    int seekToPosition = intent.getIntExtra(MEDIA_PLAYER_SEEK_TO_POSITION, 0);
                    setCurrentVideoPosition(seekToPosition);
                } else if (MEDIA_PLAYER_ON_INFO.equals(cmd)) {
                    int what = intent.getIntExtra(MEDIA_PLAYER_ON_WHAT, 0);
                    int extra = intent.getIntExtra(MEDIA_PLAYER_ON_EXTRA, 0);
                    handleMediaPlayInfo(what, extra);
                } else if (MEDIA_PLAYER_ON_VIDEO_SIZE_CHANGED.equals(cmd)) {
                    int videoWidth = intent.getIntExtra(MEDIA_PLAYER_VIDEO_WIDTH, mVideoWidth);
                    int videoHeight = intent.getIntExtra(MEDIA_PLAYER_VIDEO_HEIGHT, mVideoHeight);
                    setVideoWidth(videoWidth);
                    setVideoHeight(videoHeight);
                } else if (MEDIA_PLAYER_ON_PREPARED.equals(cmd)) {
                    handlerMediaPrepared();
                }  else if (MEDIA_PLAYER_ON_ERROR.equals(cmd)) {
                    int what = intent.getIntExtra(MEDIA_PLAYER_ON_WHAT, 0);
                    int extra = intent.getIntExtra(MEDIA_PLAYER_ON_EXTRA, 0);
                    handleMediaPlayError(what, extra);
                } else if (MEDIA_PLAYER_ON_COMPLETION.equals(cmd)) {
                    handlerMediaCompleted();
                }
            } catch (RemoteException e) {
                Log.i(TAG, "RemoteException:" + e);
            }

        }
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "LocalMediaService onBind");
        Uri uri=intent.getData();
        Log.i(TAG, "LocalMediaService onBind uri:" + uri);
        Map<String, String[]> headers = null;
        Bundle bundle = intent.getBundleExtra("headers");
        // Intent i = new Intent(LocalMediaService.this, VideoPlayerActivity.class);
        Intent i = new Intent(ACTION_START_LOCAL_MEDIA);
        i.setData(uri);
        i.putExtra("headers", bundle);
        i.putExtra(URL_FROM, LOCAL_MEDIA_SERVICE);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        return mLocalMediaServiceStub;
    }

    ILocalMediaService.Stub mLocalMediaServiceStub = new ILocalMediaService.Stub() {
//        WeakReference<LocalMediaService> mService;
//
//        MediaServiceStub(LocalMediaService service) {
//            Log.i(TAG, "MediaServiceStub constructure");
//            mService = new WeakReference<LocalMediaService>(service);
//        }
        @Override
        public int getCurrentPosition() {
            return getCurrentVideoPosition();
        }

        @Override
        public int getDuration() {
            return getVideoDuration();
        }

        @Override
        public int getWidth() {
            return getVideoWidth();
        }

        @Override
        public int getHeight() {
            return getVideoHeight();
        }

        @Override
        public boolean isPlaying() {
            return isVideoPlaying();
        }

        @Override
        public void setLocalMediaStatusListener(ILocalMediaStatusListener localMediaStatusListener)
            throws RemoteException {
            mLocalMediaStatusListener = localMediaStatusListener;
        }

    };

    // Below is some function for local media service calling.
    public int getCurrentVideoPosition() {
        return mCurrentPlayerPosition;
    }

    public void setCurrentVideoPosition(int currentPlayerPosition) {
        mCurrentPlayerPosition = currentPlayerPosition;
    }

    public void setVideoDuration(int duration) {
        mDuration = duration;
    }

    public int getVideoDuration() {
        return mDuration;
    }

    public void setVideoWidth(int videoWidth) {
        mVideoWidth = videoWidth;
    }

    public void setVideoHeight(int videoHeight) {
        mVideoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public boolean isVideoPlaying() {
        return true;
    }

    // Below is some callback function. which called by Localmm.
    public void handlerMediaPrepared() throws RemoteException {
        Log.i(TAG, "------------- handlerMediaPrepared---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaPrepared();
        }
    }

    public void handlerMediaCompleted() throws RemoteException {
        Log.i(TAG, "------------- handlerMediaCompleted---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaCompleted();
        }
    }

    public void handlerMediaSeekCompleted() throws RemoteException {
        Log.i(TAG, "------------- handlerMediaSeekCompleted---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaSeekCompleted();
        }
    }

    public void handleMediaPlayInfo(int what, int extra) throws RemoteException {
        Log.i(TAG, "------------- handleMediaPlayInfo---------what:" + what + " extra:" + extra);
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaPlayInfo(what, extra);
        }
    }

    public void handleMediaPlayError(int what, int extra) throws RemoteException {
        Log.i(TAG, "------------- handleMediaPlayError---------what:" + what + " extra:" + extra);
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaPlayError(what, extra);
        }
    }

    public void handleVideoSizeChanged(int width, int height) throws RemoteException {
        Log.i(TAG, "------------- handleVideoSizeChanged---------width:" + width + " height:" + height);
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.videoSizeChanged(width, height);
        }
    }

    public void handleMediaStop() throws RemoteException {
        Log.i(TAG, "------------- handleMediaStop---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaStop();
        }
    }

    public void handleMediaRelease() throws RemoteException {
        Log.i(TAG, "------------- handleMediaRelease---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.mediaRelease();
        }
    }

    public void handlePlayExitByUser() throws RemoteException {
        Log.i(TAG, "------------- handlePlayExitByUser---------");
        if (mLocalMediaStatusListener != null) {
            mLocalMediaStatusListener.playExitByUser();
        }
    }
}
