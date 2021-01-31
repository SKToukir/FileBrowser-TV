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
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;

/**
 *
 * Created by nate.luo on 13-8-29.
 *
 */
public class FloatVideoPlayService extends Service implements KeyEvent.Callback {

    private static final String TAG = "FloatVideoPlayService";
    private static final String EXTRA_SELECTED_ITEM = "extra_selected_item";
    private static final String SHOW_WINDOW = "SHOW_WINDOW";
    private static final String HIDE_WINDOW = "HIDE_WINDOW";
    private static final String GET_VIDEO_LIST_ITEM = "GET_VIDEO_LIST_ITEM";
    private static final String EXTRA_VIDEO_LIST_DATABASE = "EXTRA_VIDEO_LIST_DATABASE";
    private static final String EXTRA_VIDEO_LIST_TABLE = "EXTRA_VIDEO_LIST_TABLE";
    private static final String VIDEO_LIST_SELECT_ITEM = "VIDEO_LIST_SELECT_ITEM";
    private static final String MEDIA_PLAYER_PLAY = "MEDIA_PLAYER_PLAY";
    private static final String MEDIA_PLAYER_START = "MEDIA_PLAYER_START";
    private static final String MEDIA_PLAYER_PREVIOUS = "MEDIA_PLAYER_PREVIOUS";
    private static final String MEDIA_PLAYER_NEXT = "MEDIA_PLAYER_NEXT";
    private static final String MEDIA_PLAYER_STOP_PLAYBACK = "MEDIA_PLAYER_STOP_PLAYBACK";

    private int mVideoPlayListSize = 0;
    private int mCurrentVideoPlayerIndex = 0;

    private String []mVideoPlayPathList;
    private String []mVideoPlayNameList;

    private FloatVideoUIHolder mFloatVideoUIHolder = null;
    private MediaPlayerClient mMediaPlayerClient = null;
    private SurfaceHolder mSurfaceHolder = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        mFloatVideoUIHolder = new FloatVideoUIHolder(this);
        mMediaPlayerClient = new MediaPlayerClient(this);
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

            if (GET_VIDEO_LIST_ITEM.equals(cmd)) {
                String dbName = intent.getStringExtra(EXTRA_VIDEO_LIST_DATABASE);
                String tableName = intent.getStringExtra(EXTRA_VIDEO_LIST_TABLE);
                getVideoListItem(dbName, tableName);
            } else if (SHOW_WINDOW.equals(cmd)) {
                mFloatVideoUIHolder.getViewId(1).setVisibility(View.VISIBLE);
                mFloatVideoUIHolder.getViewId(2).setVisibility(View.VISIBLE);
            } else if (HIDE_WINDOW.equals(cmd)) {
                mFloatVideoUIHolder.getViewId(1).setVisibility(View.INVISIBLE);
                mFloatVideoUIHolder.getViewId(2).setVisibility(View.INVISIBLE);
            } else if (VIDEO_LIST_SELECT_ITEM.equals(cmd)) {
                int toBePlaySelectedItem = intent.getIntExtra(EXTRA_SELECTED_ITEM, 0);
                mMediaPlayerClient.stopPlayback();
                goToPlayVideoItem(toBePlaySelectedItem);
            } else if (MEDIA_PLAYER_PLAY.equals(cmd)) {
                mediaPlayerPlay();
            } else if (MEDIA_PLAYER_START.equals(cmd)) {

            }  else if (MEDIA_PLAYER_PREVIOUS.equals(cmd)) {
                mediaPlayerPrevious();
            } else if (MEDIA_PLAYER_NEXT.equals(cmd)) {
                mediaPlayerNext();
            } else if (MEDIA_PLAYER_STOP_PLAYBACK.equals(cmd)) {
                mediaPlayerStopPlayback();
            }
        }
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private void goToPlayVideoItem(int index) {
        mSurfaceHolder = mFloatVideoUIHolder.getFloatVideoPlayView().getHolder();
        Log.i(TAG, "goToPlayVideoItem index:" + index + " mSurfaceHolder = " + mSurfaceHolder);
        mSurfaceHolder.addCallback(mSHCallback);

        mCurrentVideoPlayerIndex = index;
        if (mCurrentVideoPlayerIndex <= -1) {
            mCurrentVideoPlayerIndex = mVideoPlayListSize - 1;
        } else if (mCurrentVideoPlayerIndex >= mVideoPlayListSize) {
            mCurrentVideoPlayerIndex = 0;
        }
        Log.i(TAG, "mCurrentVideoPlayerIndex:" + mCurrentVideoPlayerIndex);
        Log.i(TAG, "mVideoPlayPathList[mCurrentVideoPlayerIndex]:" + mVideoPlayPathList[mCurrentVideoPlayerIndex]);
        if (mMediaPlayerClient.isMediaPlayerCreated()) {
            if (mMediaPlayerClient.isPlaying()) {
                mMediaPlayerClient.stopPlayback();
                mMediaPlayerClient.setVideoPath(mVideoPlayPathList[mCurrentVideoPlayerIndex], mFloatVideoUIHolder.getFloatVideoPlayView().getHolder());
            } else {
                Log.i(TAG, "mFloatVideoPlayView.isPlaying");
            }
        } else {
            mMediaPlayerClient.setVideoPath(mVideoPlayPathList[mCurrentVideoPlayerIndex], mFloatVideoUIHolder.getFloatVideoPlayView().getHolder());
        }
    }

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.i(TAG, "surfaceChanged mSurfaceHolder:" + mSurfaceHolder + " format:" + format + " width:" + w + " height:" + h);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated mSurfaceHolder = " + mSurfaceHolder);
            mMediaPlayerClient.setSurfaceHolder(holder);
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "surfaceDestroyed mSurfaceHolder = " + mSurfaceHolder);
            mMediaPlayerClient.release(true);
        }
    };

    public void start() {
        Log.i(TAG, "mMMediaPlayer start-------------------- begin");
        mMediaPlayerClient.start();
        mFloatVideoUIHolder.mediaPlayerStartedCallback();
    }

    private void getVideoListItem(String dbName, String tableName) {
        DatabaseHelper dbHelper = new DatabaseHelper(MediaContainerApplication.getInstance(), Constants.DB_NAME);
        Cursor cursor = dbHelper.selectAll(Constants.VIDEO_PLAY_LIST_TABLE_NAME);

        mVideoPlayListSize = cursor.getCount();
        Log.i(TAG, "mVideoPlayListSize = " + mVideoPlayListSize);
        if (mVideoPlayListSize == 0 || !cursor.moveToFirst()) {
            Log.i(TAG, "Error ---------- Error");
        }
        mVideoPlayPathList = new String[mVideoPlayListSize];
        mVideoPlayNameList = new String[mVideoPlayListSize];
        int i = 0;
        for (i = 0; i < mVideoPlayListSize; i++) {
            if (cursor.getInt(cursor.getColumnIndex("checked")) == 1) {
                mCurrentVideoPlayerIndex = i;
            }
            mVideoPlayNameList[i] = cursor.getString(cursor.getColumnIndex("name"));
            mVideoPlayPathList[i] = cursor.getString(cursor.getColumnIndex("path"));
            Log.i(TAG, "mVideoPlayNameList[" + i + "]:" + mVideoPlayNameList[i]);
            Log.i(TAG, "mVideoPlayPathList[" + i + "]:" + mVideoPlayPathList[i]);

            cursor.moveToNext();
        }

        if (cursor != null) {
            cursor.close();
        }

        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Log.i(TAG, "onKeyDown i:" + i + " keyEvent.getAction::" + keyEvent.getAction());
        return false;
    }

    @Override
    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        Log.i(TAG, "onKeyUp i:" + i + " keyEvent.getAction::" + keyEvent.getAction());
        return false;
    }

    @Override
    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return false;
    }

    public void mediaPlayerPlay() {
        Tools.changeSource(true);
        mFloatVideoUIHolder.mediaPlayerStartedCallback();
        mSurfaceHolder = mFloatVideoUIHolder.getFloatVideoPlayView().getHolder();
        Log.i(TAG, "mSurfaceHolder = " + mSurfaceHolder);
        mSurfaceHolder.addCallback(mSHCallback);
        Log.i(TAG, "mCurrentVideoPlayerIndex:" + mCurrentVideoPlayerIndex);
        Log.i(TAG, "mVideoPlayPathList[mCurrentVideoPlayerIndex]:" + mVideoPlayPathList[mCurrentVideoPlayerIndex]);
        if (mMediaPlayerClient.isMediaPlayerCreated()) {
            if (mMediaPlayerClient.isPlaying()) {
                Log.i(TAG, "mMMediaPlayer.pause");
                mMediaPlayerClient.pause();
                mFloatVideoUIHolder.mediaPlayerPausedCallback();
            } else {
                Log.i(TAG, "mMMediaPlayer.pause");
                mMediaPlayerClient.start();
                mFloatVideoUIHolder.mediaPlayerStartedCallback();
            }
        } else {
            if (mVideoPlayPathList[mCurrentVideoPlayerIndex] != null) {
                mMediaPlayerClient.setVideoPath(mVideoPlayPathList[mCurrentVideoPlayerIndex], mFloatVideoUIHolder.getFloatVideoPlayView().getHolder());
            } else {
//                        Intent intent;
//                        intent = new Intent(FloatVideoPlayService.this, FileBrowserActivity.class);
//                        startActivity(intent);
            }

        }
    }

    public void mediaPlayerPrevious() {
        mMediaPlayerClient.stopPlayback();
        mCurrentVideoPlayerIndex--;
        goToPlayVideoItem(mCurrentVideoPlayerIndex);
    }

    public void mediaPlayerNext() {
        mMediaPlayerClient.stopPlayback();
        mCurrentVideoPlayerIndex++;
        goToPlayVideoItem(mCurrentVideoPlayerIndex);
    }

    public void mediaPlayerStopPlayback() {
    }

}