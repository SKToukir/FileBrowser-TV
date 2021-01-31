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
package com.walton.filebrowser.ui.video;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.os.Message;
import android.content.DialogInterface.OnCancelListener;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.video.MediaError;
import com.walton.filebrowser.business.video.VideoPlayView;

import com.walton.filebrowser.ui.view.ACProgressConstant;
import com.walton.filebrowser.ui.view.ACProgressFlower;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.PlaylistTool;
import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.tv.TvCommonManager;

//import android.media.MediaPlayer.EnumVideoAspectRatio;
public class NetVideoPlayActivity extends VideoActivityBase {
    public class errorStruct {
        protected String strMessage = "";
        // The default mode with error display.
        protected boolean showStateWithError = true;

        public errorStruct() {
            super();
            showStateWithError = true;
            strMessage = "";
        }
    }

    public static final int OPTION_STATE_PRE = 0x00;
    public static final int OPTION_STATE_REWIND = 0x01;
    public static final int OPTION_STATE_PLAY = 0x02;
    public static final int OPTION_STATE_WIND = 0x03;
    public static final int OPTION_STATE_NEXT = 0x04;
    public static final int OPTION_STATE_TIME = 0x05;
    public static final int OPTION_STATE_LIST = 0x06;
    // To determine which control for focus
    protected static int state = OPTION_STATE_PLAY;
    private static final String TAG = "NetVideoPlayActivity";
    public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
    public static final String ACTION_CHANGE_NET = "com.mstar.localmm.network.disconnect";
    public static final String VIDEO_PLAY_MODE = "PLAY_MODE";
    private NetVideoPlayHolder mvideoPlayHolder;
    // video is in play
    private boolean isPlaying = true;
    public static final int SEEK_POS = 14;
    // video article control whether display
    private boolean isControllerShow = true;
    // video control bar to be automatic hidden time
    private static final int DEFAULT_TIMEOUT = 15 * 1000;
    // the current video broadcast to the position
    private int currentPlayerPosition = 0;
    private int mCurrentIndex = 0;
    private TvCommonManager appSkin;
    private int inputSource;
    // video buffer progress bar
    private ACProgressFlower progressDialog;
    // from the VideoActivity selected to play video index, through the index
    // can obtain video
    protected int video_position = 0;
    // The current folder data sources
    protected ArrayList<BaseData> mVideoPlayList = new ArrayList<BaseData>();
    // Optional time control Dialog
    private ChooseTimePlayDialog chooseTimePlayDialog;
    private static int seekTimes = 0;
    private int seekPosition = 0;
    private int duration = 0;
    private int playSpeed;
    private MMediaPlayer musicplayer = null;
    private boolean isAudioSupport = true;
    private boolean isVideoSupport = true;
    private boolean isPrepared = false;
    private boolean isMusicPlayer = false;
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private int mCurrentState = STATE_IDLE;
    private VideoListDialog mVideoListDialog;

    // Video correlation processing handler
    private Handler videoHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.HIDE_PLAYER_CONTROL) {
                hideController();
            } else if (msg.what == SEEK_POS  && mvideoPlayHolder.mVideoPlayView != null) {
                if (isMusicPlayer) {
                    if (musicplayer != null && musicplayer.isPlaying()) {
                        currentPlayerPosition = (int)getCurrentPosition();
                        mvideoPlayHolder.videoSeekBar.setProgress(currentPlayerPosition);
                        String time = Tools.formatDuration(currentPlayerPosition);
                        mvideoPlayHolder.current_time_video.setText(time);
                        if (chooseTimePlayDialog != null) {
                           chooseTimePlayDialog.getVideoTimeCurrentPositionTextView().setText(time);
                        }
                        videoHandler.sendEmptyMessageDelayed(SEEK_POS, 500);
                    }
                } else {
                   if (mvideoPlayHolder.mVideoPlayView.isPlaying()) {
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                              String time = null;
                              Log.d(TAG,"before getCurrentPosition, currentPlayerPosition = " + currentPlayerPosition);
                              currentPlayerPosition = mvideoPlayHolder.getPlayerView().getCurrentPosition();
                              time = Tools.formatDuration(currentPlayerPosition);
                              Message msg = videoHandler.obtainMessage(Constants.RefreshCurrentPositionStatusUI);
                              msg.obj = time;
                              videoHandler.sendMessage(msg);
                           }
                       }).start();
                  }
                }

            } else if (msg.what == Constants.RefreshCurrentPositionStatusUI) {
                   mvideoPlayHolder.videoSeekBar.setProgress(currentPlayerPosition);
                   mvideoPlayHolder.current_time_video.setText((String)msg.obj);
                   if (chooseTimePlayDialog != null) {
                       chooseTimePlayDialog.getVideoTimeCurrentPositionTextView().setText((String)msg.obj);
                   }
                   videoHandler.sendEmptyMessageDelayed(SEEK_POS, 500);

            } else if (msg.what == Constants.CHOOSE_TIME) {
                /*
                 * localPause(true);
                 * mvideoPlayHolder.mVideoPlayView.seekTo(msg.arg1);
                 * localResume(true); state = OPTION_STATE_PLAY;
                 * mvideoPlayHolder.SetVideoTimeSelect(false);
                 */
                seekBarListener.onProgressChanged(mvideoPlayHolder.videoSeekBar, msg.arg1, true);
                mvideoPlayHolder.SetVideoTimeSelect(false);

            } else if (msg.what == Constants.SEEK_TIME) {
                // localPause(true);
                if (isMusicPlayer) {
                    seekTo(seekPosition);
                } else {
                    mvideoPlayHolder.mVideoPlayView.seekTo(seekPosition);
                }
//                localResume(true);
                seekTimes = 0;
            }
        };
    };
    private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // The progress of the progress bar is changed
            if (fromUser) {
                if (!mvideoPlayHolder.mbNotSeek) {
                    if (isMusicPlayer) {
                        mvideoPlayHolder.setAllUnSelect(musicplayer.isPlaying());
                    } else {
                    mvideoPlayHolder.setAllUnSelect(mvideoPlayHolder.mVideoPlayView.isPlaying());
                    }
                    cancleDelayHide();
                    seekPosition = progress;
                    if (duration <= progress) {
                        seekPosition = progress - 1000;
                    }
                    videoHandler.removeMessages(Constants.SEEK_TIME);
                    if (++seekTimes == 1) {
                        videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                    } else {
                        videoHandler.sendEmptyMessageDelayed(
                                Constants.SEEK_TIME, 1000);
                    }
                    hideControlDelay();
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                } else {
                    showToastTip(getResources().getString(
                            R.string.choose_time_failed));
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.buffering);
        setContentView(R.layout.net_video_player_frame);
        InitView();
        String video_path = "";
        String videoName = "none";
        int  filetype = getIntent().getIntExtra(Constants.BUNDLE_FILETYPE_KEY, Constants.FILE_TYPE_VIDEO);
        BaseData fileBaseData = (BaseData)getIntent().getExtra(Constants.BUNDLE_BASEDATA_KEY, null);
        BaseData toBePlayData = null;

        if(fileBaseData != null) {
            if(filetype == Constants.FILE_TYPE_MPLAYLIST) {

                PlaylistTool ptool =new PlaylistTool();
                mVideoPlayList = ptool.parsePlaylist(fileBaseData.getPath());
                int i=0;
                int size = mVideoPlayList.size();
                for(i =0; i < size; i++) {
                    Log.v(TAG, "--->"+mVideoPlayList.get(i).getPath());
                }
                toBePlayData = mVideoPlayList.get(0);
            } else {
                toBePlayData = fileBaseData;
            }

            Tools.setVideoStreamlessModeOn(false);//only enable streamless mode for  *.mplt file

            video_path = toBePlayData.getPath();
            if(toBePlayData.getName() != null) {
                videoName = toBePlayData.getName();
            }
            InitVideoPlayer(video_path);
            ControlButtListener listener = new ControlButtListener();
            mvideoPlayHolder.setOnClickListener(listener);
            mvideoPlayHolder.setVideoName(videoName);
            hideControlDelay();
            // Registered monitor shutdown broadcast
            IntentFilter ittfile = new IntentFilter();
            ittfile.addAction(Intent.ACTION_SHUTDOWN);
            ittfile.addAction(ACTION_CHANGE_SOURCE);
            ittfile.addAction(ACTION_CHANGE_NET);
            this.registerReceiver(receiver, ittfile);
            if (!Tools.unSupportTVApi()) {
                changeSource();
            }
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "***************onResume********");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        // Registered disk change broadcast receiver
        registerReceiver(diskChangeReceiver, filter);
        state = OPTION_STATE_PLAY;
        super.onResume();
    }
    @Override
    public void finish() {
        //send a broadcast when finish , pip/pop will use it
        //mantis-0692032
        sendBroadcast(new Intent("com.walton.filebrowser.ui.video.NetVideoPlayActivity.FINISH_SELF"));
        super.finish();
    }
    protected void onStop() {
          Log.i(TAG, "***************onStop****finish****");
          if (mvideoPlayHolder.mVideoPlayView!= null) {
              mvideoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
              new Thread(new Runnable(){
                 @Override
                 public void run(){
                    mvideoPlayHolder.mVideoPlayView.stopPlayer();
                 }
              }).start();

          }
          NetVideoPlayActivity.this.finish();
          super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "***************onDestroy********");
        // Avoid activity is lost player anomaly to upper send anomaly code lead
        // to showErrorDialog error
        if (mvideoPlayHolder.mVideoPlayView != null) {
            mvideoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
        }
        if (isMusicPlayer) {
            stop();
        }
        dismissProgressDialog();
        try {
            unregisterReceiver(diskChangeReceiver);
            unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDestroy();
        /*
         * new Thread(new Runnable() {
         *
         * @Override public void run() {
         * mvideoPlayHolder.mVideoPlayView.stopPlayer(); } }).start();
         */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isControllerShow) {
                showController();
                hideControlDelay();
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp keyCode:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                moveToNextOrPrevious(mCurrentIndex - 1);
                return true;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                moveToNextOrPrevious(mCurrentIndex + 1);
                return true;
            case  KeyEvent.KEYCODE_MEDIA_PLAY:
                if (!isPlaying) {
                    localResume(true);
                } else {
                    if (playSpeed != 0) {
                        localResumeFromSpeed(true);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                if (isPlaying) {
                    localPause(true);
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                registerListeners();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mvideoPlayHolder.processRightKey(keyCode, event);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mvideoPlayHolder.processLeftKey(keyCode, event);
                return true;
            case KeyEvent.KEYCODE_MENU:
                if (!isControllerShow) {
                    showController();
                    hideControlDelay();
                } else {
                    hideController();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                //slowForward();
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                //fastForward();
            default:
                break;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if (KeyEvent.KEYCODE_ASPECT_RATIO == keyCode) {
        // return true;
        // }
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        if (!isControllerShow) {
            if (KeyEvent.KEYCODE_ENTER != keyCode)
                return super.onKeyDown(keyCode, event);
        }
        if (KeyEvent.KEYCODE_ENTER == keyCode
                || KeyEvent.KEYCODE_BACK == keyCode
                || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
                || KeyEvent.KEYCODE_DPAD_RIGHT == keyCode
                || KeyEvent.KEYCODE_DPAD_DOWN == keyCode
                || KeyEvent.KEYCODE_DPAD_UP == keyCode
                || KeyEvent.KEYCODE_MENU == keyCode) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * Response OK button
     */
    private void registerListeners() {
        if (isControllerShow) {
            switch (state) {
                case OPTION_STATE_PRE:
                    mvideoPlayHolder.bt_videoPre
                            .setBackgroundResource(R.drawable.ic_player_icon_previous_focus);
                    cancleDelayHide();
                    moveToNextOrPrevious(mCurrentIndex - 1);
                    hideControlDelay();
                    break;
                case OPTION_STATE_REWIND:
                    cancleDelayHide();
                    if (!isMusicPlayer)
                        slowForward();
                    hideControlDelay();
                    break;
                case OPTION_STATE_PLAY:
                    if (isPrepared) {
                        cancleDelayHide();
                        if (isPlaying) {
                            if (playSpeed != 0) {
                                localResumeFromSpeed(true);
                            } else {
                                localPause(true);
                            }
                        } else {
                            localResume(true);
                        }
                        hideControlDelay();
                    }
                    break;
                case OPTION_STATE_WIND:
                    cancleDelayHide();
                    if (!isMusicPlayer)
                        fastForward();
                    hideControlDelay();

                    break;
                case OPTION_STATE_NEXT:
                    cancleDelayHide();
                    moveToNextOrPrevious(mCurrentIndex + 1);
                    hideControlDelay();
                    mvideoPlayHolder.bt_videoNext
                            .setBackgroundResource(R.drawable.ic_player_icon_next_focus);
                    break;
                case OPTION_STATE_TIME:
                    cancleDelayHide();
                    showVideoTimeSet();
                    hideControlDelay();
                    break;
                case OPTION_STATE_LIST:
                    cancleDelayHide();
                    showVideoListDialog();
                    hideControlDelay();
                    break;
            }
        } else {
            showController();
            hideControlDelay();
        }
    }

    private void InitVideoPlayer(String videoPlayPath) {
        isAudioSupport = true;
        isVideoSupport = true;
        isPrepared = false;
        mvideoPlayHolder.mbNotSeek = false;
        // Setting error of the monitor
        if (videoPlayPath != null && !videoPlayPath.endsWith(".mp3")) {
            // mvideoPlayHolder.mVideoPlayView.stopPlayback();
            if (isMusicPlayer) {
                mvideoPlayHolder.setBgPhotoVisible(false);
                //if (musicplayer != null) {
                stop();
                //}
                isMusicPlayer = false;

            }
            mvideoPlayHolder.setVideoBtnVisible(false);
            mvideoPlayHolder.mVideoPlayView
                .setPlayerCallbackListener(myPlayerCallback);
            mvideoPlayHolder.mVideoPlayView.setVideoPath(videoPlayPath, 1);
        } else {
          Log.i(TAG,"mp3 file");
          if (!isMusicPlayer) {
              mvideoPlayHolder.setBgPhotoVisible(true);
              if (mvideoPlayHolder.mVideoPlayView != null) {
                  mvideoPlayHolder.mVideoPlayView.stopPlayback();
              }
              isMusicPlayer = true;
          }
            mvideoPlayHolder.setVideoBtnVisible(false);
            initMusicPlayer(videoPlayPath);
        }
        if (chooseTimePlayDialog != null) {
            chooseTimePlayDialog.clearChooseList();
            chooseTimePlayDialog.dismiss();
        }
    }

    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
           Log.d(TAG,"---onError---");
           return false;
        }
    };

    private MMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.d(TAG, TAG + " onCompletion");
                mCurrentState = STATE_PLAYBACK_COMPLETED;
        }
    };

    private MMediaPlayer.OnPreparedListener mPreparedListener = new MMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, " onPrepared");
            dismissProgressDialog();
            isPrepared = true;
            mCurrentState = STATE_PREPARED;
            duration = (int) getDuration();
            Log.d(TAG, "getDuration()" + duration);
            String time = Tools.formatDuration(duration);
            mvideoPlayHolder.setAllUnSelect(true);
            mvideoPlayHolder.SetVideoPlaySelect(true,true);
            mvideoPlayHolder.total_time_video.setText(time);
            mvideoPlayHolder.videoSeekBar.setMax(duration);
            start();
            videoHandler.sendEmptyMessage(SEEK_POS);
        }
    };

    private MMediaPlayer.OnCompletionListener mCompletionListener = new MMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, TAG + " onCompletion");
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            moveToNextOrPrevious(mCurrentIndex + 1);
            }
    };

    private void initMusicPlayer(String musicPlayPath) {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        this.sendBroadcast(i);
        stop();
         try {
            musicplayer = new MMediaPlayer();
            musicplayer.reset();
            //mSeekWhenPrepared = 0;
            musicplayer.setOnErrorListener(mErrorListener);
            musicplayer.setOnSeekCompleteListener(mSeekCompleteListener);
            musicplayer.setOnCompletionListener(mCompletionListener);
            musicplayer.setOnPreparedListener(mPreparedListener);
            musicplayer.setDataSource(musicPlayPath);
            musicplayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (IllegalStateException e) {
            mCurrentState = STATE_ERROR;
            return;
        } catch (IOException e) {
            mCurrentState = STATE_ERROR;
            return;
        } catch (IllegalArgumentException e) {
            mCurrentState = STATE_ERROR;
            Log.d(TAG,"IllegalArgumentException");
            return;
        } catch (SecurityException e) {
            mCurrentState = STATE_ERROR;
            Log.d(TAG,"SecurityException");
            return;
        } catch (Exception e) {
            mCurrentState = STATE_ERROR;
            Log.d(TAG,"Exception");
        }
    }

    private boolean isInPlaybackState() {
        return (musicplayer != null && mCurrentState != STATE_ERROR
                    && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    private void pause() {
         if (isInPlaybackState() && musicplayer.isPlaying()) {
            try {
                musicplayer.pause();
                //isPlaying = false;
                mCurrentState = STATE_PAUSED;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
         }
    }

    private long getDuration() {
        int Duration = -1;
        if (isInPlaybackState()) {
            Duration = musicplayer.getDuration();
            return Duration;
        }
        return Duration;
    }

    private void seekTo(int position) {
        if (isInPlaybackState() && position >= 0 && position <= getDuration()) {
             try {
                musicplayer.seekTo(position);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
        if (!isInPlaybackState()) return;
            try {
            musicplayer.start();
            //isPlaying = true;
            mCurrentState = STATE_PLAYING;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

   private long getCurrentPosition() {
       if (isInPlaybackState()) {
            return musicplayer.getCurrentPosition();
       }
       return -1;
    }

   private void stop() {
         if (musicplayer != null) {
            try {
                 musicplayer.stop();
                 Log.i(TAG,"musicplayer is released");
            } catch (IllegalStateException e) {
                 Log.i(TAG, "stop fail! please try again!");
            }
            //isPlaying = false;
            musicplayer.release();
            musicplayer = null;
            mCurrentState = STATE_IDLE;
        }
    }

    @Override
    public List<BaseData> getVideoPlayList() {
        return mVideoPlayList;
    }

    @Override
    public int getCurrentViewPosition() {
        return 0;
    }
    public boolean getIsMusicPlayer(){
        return isMusicPlayer;
    }
    public boolean musicPlayerIsPlaying(){
        if (isInPlaybackState())
            return musicplayer.isPlaying();
        return false;
    }
    // Pop up display an error dialog box
    private void showErrorDialog(String strMessage) {
        if (!isFinishing()) {
            new AlertDialog.Builder(NetVideoPlayActivity.this)
                    .setTitle(getResources().getString(R.string.show_info))
                    .setMessage(strMessage)
                    .setPositiveButton(
                            getResources().getString(R.string.exit_ok),
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // mvideoPlayHolder.mVideoPlayView.stopPlayback();
                                    dismissProgressDialog();
                                    NetVideoPlayActivity.this.finish();
                                }
                            }).setCancelable(false).show();
        }
    }

    // Unknown error handling
    private errorStruct processErrorUnknown(MediaPlayer mp, int what, int extra) {
        errorStruct retStruct = new errorStruct();
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
                retStruct.showStateWithError = false;
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
        retStruct.strMessage = getResources().getString(strID);
        return retStruct;
    }

    public VideoPlayView.playerCallback myPlayerCallback = new VideoPlayView.playerCallback() {
        @Override
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err, int viewId) {
            String strMessage = "";
            switch (framework_err) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED: {
                    mvideoPlayHolder.mVideoPlayView.stopPlayback();
                    strMessage = getResources().getString(
                            R.string.video_media_error_server_died);
                    break;
                }
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    errorStruct retStruct = processErrorUnknown(mp, framework_err,
                            impl_err);
                    strMessage = retStruct.strMessage;
                    if (!retStruct.showStateWithError) {
                        showToastTip(retStruct.strMessage);
                        return false;
                    }
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    strMessage = getResources().getString(
                            R.string.video_media_error_not_valid);
                    break;
                default:
                    strMessage = getResources().getString(
                            R.string.video_media_other_error_unknown);
                    break;
            }
            showErrorDialog(strMessage);
            return true;
        }

        @Override
        public void onCompletion(MediaPlayer mp, int viewId) {
            Log.i(TAG, "onCompletion()");
            Message msg = new Message();
            Bundle mBundle = new Bundle();
            mBundle.putInt("index", mCurrentIndex + 1);
            msg.setData(mBundle);
            toBePlayedHandler.sendMessage(msg);
           // finish();
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height,int viewId) {
        }

        @Override
        public void onPrepared(MediaPlayer mp, int viewId) {
            seekPosition=0;
            dismissProgressDialog();
            duration = (int) mvideoPlayHolder.mVideoPlayView.getDuration();
            Log.i(TAG, "getDuration()" + duration);
            String time = Tools.formatDuration(duration);
            mvideoPlayHolder.total_time_video.setText(time);
            mvideoPlayHolder.videoSeekBar.setMax(duration);
            mvideoPlayHolder.mVideoPlayView.setPlayingState();
            isPrepared = true;
            mvideoPlayHolder.SetVideoPlaySelect(true, true); //mantis:0834147,mvideoPlayHolder.mVideoPlayView.isPlaying()
            hideControlDelay();
            videoHandler.sendEmptyMessage(SEEK_POS);
        }

        @Override
        public void onSeekComplete(MediaPlayer mp, int viewId) {
            Log.i(TAG,"onSeekComplete");
            mvideoPlayHolder.setPlaySpeed(1);
            if (mvideoPlayHolder !=null && mvideoPlayHolder.mVideoPlayView !=null ) {
                mvideoPlayHolder.mVideoPlayView.setVoice(true);
            }
        }

        @Override
        public void onCloseMusic() {
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra, int viewId) {
            switch (what) {
                case MMediaPlayer.MEDIA_INFO_SUBTITLE_UPDATA:
                    if (extra == 1) {
                        String str = SubtitleManager.getInstance()
                                .getSubtitleData(mvideoPlayHolder.getPlayerView().getMMediaPlayer());
                        Log.i("*************", "*******setSubTitleText******" + str);
                        if (str.length() >= 1)
                            mvideoPlayHolder.setSubTitleText(str);
                        return true;
                    }
                    if (extra == 0) {
                        // Subtitles hidden
                        mvideoPlayHolder.setSubTitleText("");
                        return true;
                    }
                    break;
                case MMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    mvideoPlayHolder.mbNotSeek = true;
                    String strMessage = getResources().getString(
                            R.string.video_media_infor_no_index);
                    mvideoPlayHolder.setPlaySpeed(1);
                    showToastTip(strMessage);
                    return true;
                case MMediaPlayer.MEDIA_INFO_AUDIO_UNSUPPORT:
                    isAudioSupport = false;
                    showToastTip(getResources().getString(
                            R.string.video_media_error_audio_unsupport));
                    break;
                case MMediaPlayer.MEDIA_INFO_VIDEO_UNSUPPORT:
                    isVideoSupport = false;
                    showToastTip(getResources().getString(
                            R.string.video_media_error_video_unsupport));
                    break;
                default:
                    Log.i(TAG, "Play onInfo::: default onInfo!");
                    break;
            }
            return false;
        }

        @Override
        public void onUpdateSubtitle(String sub) {
        }
    };

    /**
     * Initialization view
     */
    private void InitView() {
        mvideoPlayHolder = new NetVideoPlayHolder(NetVideoPlayActivity.this);
        mvideoPlayHolder.videoSeekBar
                .setOnSeekBarChangeListener(seekBarListener);
        // Video control bar
        mvideoPlayHolder.playControlLayout = (LinearLayout) findViewById(R.id.net_video_suspension_layout);
    }

    private void moveToNextOrPrevious(int moveToIndex) {
        Message msg = new Message();
        Bundle mBundle = new Bundle();
        mBundle.putInt("index", moveToIndex);
        msg.setData(mBundle);
        toBePlayedHandler.sendMessage(msg);
    }

    /**
     * Click the mouse button response
     */
    class ControlButtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isControllerShow)
                return;
            boolean isPlaystatus = false;
            if (isMusicPlayer) {
               isPlaystatus = musicplayer.isPlaying();
            } else {
               isPlaystatus = mvideoPlayHolder.getPlayerView().isPlaying();
            }
            switch (v.getId()) {
                case R.id.video_previous:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    cancleDelayHide();
                    moveToNextOrPrevious(mCurrentIndex - 1);
                    hideControlDelay();
                    mvideoPlayHolder.setVideoPreSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PRE;
                    mvideoPlayHolder.bt_videoPre.setBackgroundResource(R.drawable.ic_player_icon_previous_focus);
                    break;
                case R.id.net_video_rewind:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    cancleDelayHide();
                    if (!isMusicPlayer)
                        slowForward();
                    hideControlDelay();
                    mvideoPlayHolder.SetVideoRewindSelect(true);
                    state = OPTION_STATE_REWIND;
                    break;
                case R.id.net_video_play:
                    if (isPrepared) {
                        mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                        cancleDelayHide();
                        if (isPlaying) {
                            if (playSpeed != 0) {
                                localResumeFromSpeed(true);
                            } else {
                                localPause(true);
                            }
                        } else {
                            localResume(true);
                        }
                        hideControlDelay();
                        //mvideoPlayHolder.SetVideoPlaySelect(true,isPlaystatus);
                        state = OPTION_STATE_PLAY;
                    }
                    break;
                case R.id.net_video_wind:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    cancleDelayHide();
                    if (!isMusicPlayer)
                        fastForward();
                    hideControlDelay();
                    mvideoPlayHolder.SetVideoWindSelect(true);
                    state = OPTION_STATE_WIND;
                    break;
                case R.id.video_next:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    moveToNextOrPrevious(mCurrentIndex + 1);
                    cancleDelayHide();
                    hideControlDelay();
                    mvideoPlayHolder.setVideoNextSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_NEXT;
                    mvideoPlayHolder.bt_videoNext.setBackgroundResource(R.drawable.ic_player_icon_next_focus);
                    break;
                case R.id.net_video_time:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    state = OPTION_STATE_TIME;
                    cancleDelayHide();
                    //if (!isMusicPlayer)
                    showVideoTimeSet();
                    hideControlDelay();
                    break;

                case R.id.net_video_list:
                    mvideoPlayHolder.setAllUnSelect(isPlaystatus);
                    cancleDelayHide();
                    showVideoListDialog();
                    hideControlDelay();
                    //mvideoPlayHolder.setVideoListSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_LIST;
                    break;
                default:
                    break;
            }
        }
    }
    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String devicePath = intent.getDataString().substring(7);
            Log.d(TAG, "diskChangeReceiver" + action);
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                if (mvideoPlayHolder.mVideoPlayView != null) {
                    mvideoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
                }
                if (isMusicPlayer) {
                    stop();
                }
                if (devicePath!=null) {
                    Toast toast = Toast.makeText(NetVideoPlayActivity.this,
                            R.string.disk_eject, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    NetVideoPlayActivity.this.finish();
                }
            }
       }
    };
    // To receive shutdown broadcast (or source switching/network interruption)
    // exit play interface
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,
                    "*******BroadcastReceiver**********" + intent.getAction());
            // Avoid activity is lost player anomaly to upper send anomaly code
            // lead to showErrorDialog error
            if (mvideoPlayHolder.mVideoPlayView != null) {
                mvideoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
            }
            dismissProgressDialog();
            finish();
        }
    };


    // play control Handler
    private Handler toBePlayedHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.v(TAG, "toBePlayedHandler  be called.");
            mvideoPlayHolder.reset();

            Bundle data = msg.getData();
            int  currentIndex = data.getInt("index", 0);
            if (currentIndex < 0) {
                currentIndex = mVideoPlayList.size() - 1;
            } else if (currentIndex >= mVideoPlayList.size()) {
                currentIndex = 0;
            }
			mCurrentIndex = currentIndex;
            String videoName = mVideoPlayList.get(currentIndex).getName();
            mvideoPlayHolder.setVideoName(videoName);
            Log.v(TAG, "to play:"+mVideoPlayList.get(currentIndex).getPath());
            InitVideoPlayer(mVideoPlayList.get(currentIndex).getPath());

            showController();
            hideControlDelay();
        };
    };


    /**
     * Display video playlist dialog box
     */
    private void showVideoListDialog() {
        mVideoListDialog = new VideoListDialog(this, currentPlayerPosition);
        mVideoListDialog.show();
        mVideoListDialog.setHandler(toBePlayedHandler);
        mVideoListDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {

            }
        });
    }

    /**
     * Local broadcast suspended
     */
    protected void localPause(boolean bSelect) {
        cancleDelayHide();
        if (!isMusicPlayer) {
            mvideoPlayHolder.mVideoPlayView.setVoice(false);
            mvideoPlayHolder.mVideoPlayView.setPlayMode(0);
            mvideoPlayHolder.mVideoPlayView.pause();
            mvideoPlayHolder.mVideoPlayView.setVoice(true);
            mvideoPlayHolder.setPlaySpeed(1);
        } else {
            pause();
        }
        isPlaying = false;
        mvideoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        hideControlDelay();
    }

    /**
     * Local broadcast recovery
     */
    protected void localResume(boolean bSelect) {
        cancleDelayHide();
        isPlaying = true;
        if (!isMusicPlayer) {
            mvideoPlayHolder.mVideoPlayView.start();
        } else {
            start();
        }
        mvideoPlayHolder.setPlaySpeed(1);
        mvideoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        videoHandler.sendEmptyMessage(SEEK_POS);
        hideControlDelay();
    }

    /**
     * Local broadcast recovery
     */
    protected void localResumeFromSpeed(boolean bSelect) {
        // cancleDelayHide();
        mvideoPlayHolder.mVideoPlayView.setVoice(true);
        mvideoPlayHolder.mVideoPlayView.setPlayMode(0);
        isPlaying = true;
        playSpeed = 0;
        mvideoPlayHolder.setPlaySpeed(1);
        mvideoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        videoHandler.removeMessages(SEEK_POS);
        videoHandler.sendEmptyMessage(SEEK_POS);
        // hideControlDelay();
    }

    /**
     * Backward times quick play
     */
    private void slowForward() {
        if (!isPlaying) {
            mvideoPlayHolder.mVideoPlayView.setVoice(false);
            localResume(false);
        }
        playSpeed = mvideoPlayHolder.mVideoPlayView.getPlayMode();
        if (playSpeed < 0) {
            return;
        }
        int currentSpeed = 1 * (-2);
        /*
         * if (playSpeed < 64 && playSpeed < 0) { currentSpeed = playSpeed * 2;
         * }
         */
        playSpeed = currentSpeed;
        mvideoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
        // Set the current approaching speed display string
        mvideoPlayHolder.setPlaySpeed(currentSpeed);
    }

    /**
     * Forward times quick play
     */
    private void fastForward() {
        if (!isPlaying) {
            mvideoPlayHolder.mVideoPlayView.setVoice(false);
            localResume(false);
        }
        playSpeed = mvideoPlayHolder.mVideoPlayView.getPlayMode();
        if (playSpeed > 1) {
            return;
        }
        int currentSpeed = 1 * 2;
        /*
         * if (playSpeed < 64 && playSpeed > 0) { currentSpeed = playSpeed * 2;
         * }
         */
        playSpeed = currentSpeed;
        mvideoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
        // Set the current approaching speed display string
        mvideoPlayHolder.setPlaySpeed(currentSpeed);
    }

    /**
     * To display the play time Settings dialog box
     */
    private void showVideoTimeSet() {
        mvideoPlayHolder.SetVideoTimeSelect(true);
        // Choose the operation of Dialog
        if (chooseTimePlayDialog == null) {
            chooseTimePlayDialog = new ChooseTimePlayDialog(
                    NetVideoPlayActivity.this, R.style.choose_time_dialog);
            chooseTimePlayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = chooseTimePlayDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.x = 150;
            lp.y = 180;
            chooseTimePlayDialog.getWindow().setAttributes(lp);
            chooseTimePlayDialog.setVariable(mvideoPlayHolder.mbNotSeek,
                    videoHandler);
        }
        chooseTimePlayDialog.show();
        // video
        if(isInPlaybackState()==false)
           duration = (int) mvideoPlayHolder.mVideoPlayView.getDuration();
        // music
        else
           duration=(int)getDuration();
        Log.i(TAG, "getDuration()" + duration);
        String time = Tools.formatDuration(duration);
        chooseTimePlayDialog.getVideoTimeDurationTextView().setText(time);
        chooseTimePlayDialog.getVideoTimeCurrentPositionTextView()
                .setText(time);
        if (time.equals(getResources().getString(R.string.default_time))) {
            chooseTimePlayDialog.setVariable(true);
        }
    }

    /**
     *
     */
    public void hideControlDelay() {
        videoHandler.sendEmptyMessageDelayed(Constants.HIDE_PLAYER_CONTROL,
                DEFAULT_TIMEOUT);
    }

    /**
     * Article display control
     */
    private void showController() {
        if (mvideoPlayHolder.playControlLayout != null) {
            mvideoPlayHolder.playControlLayout.setVisibility(View.VISIBLE);
            isControllerShow = true;
        } else
            Log.i(TAG, "playControlLayout is null ptr===");
    }

    /**
     *
     */
    private void cancleDelayHide() {
        videoHandler.removeMessages(Constants.HIDE_PLAYER_CONTROL);
    }

    /**
     *
     */
    private void hideController() {
        if (mvideoPlayHolder.playControlLayout != null) {
            mvideoPlayHolder.playControlLayout.setVisibility(View.INVISIBLE);
            isControllerShow = false;
        } else
            System.err.println("playControlLayout is null ptr!!");
    }

    // Pop up display Tip
    private void showToastTip(String strMessage) {
        Toast toast = Toast.makeText(NetVideoPlayActivity.this, strMessage,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if(!isVideoSupport && !isAudioSupport){
            NetVideoPlayActivity.this.finish();
        }
    }

    /**
     * Display buffer progress
     *
     * @param id
     */
    private void showProgressDialog(int id) {
        if (!isFinishing()) {
            progressDialog = new ACProgressFlower.Builder(this, R.style.NonDimACProgressDialog)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text(getString(id))
                    .textColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            progressDialog.show();
        }
    }

    /**
     *
     */
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // switch source
    public void changeSource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Boot the input from TV switch to AP
                if (appSkin == null) {
                    // appSkin = TvApiManager
                    // .getTvCommonManager(NetVideoPlayActivity.this);
                    appSkin = TvCommonManager.getInstance();
                }
                if (appSkin != null) {
                    inputSource = appSkin.getCurrentTvInputSource();
                    if (inputSource != TvCommonManager.INPUT_SOURCE_STORAGE) {
                        appSkin.setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                        Log.i("main", "already SetInputSource.......... ");
                    }
                }
            }
        }).start();
    }
}
