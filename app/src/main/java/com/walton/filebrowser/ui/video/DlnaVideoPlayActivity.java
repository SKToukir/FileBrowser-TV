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
package com.walton.filebrowser.ui.video;

import java.util.ArrayList;

import android.app.Activity;
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

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.video.MediaError;
import com.walton.filebrowser.business.video.VideoPlayView;
import com.walton.filebrowser.ui.view.ACProgressConstant;
import com.walton.filebrowser.ui.view.ACProgressFlower;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.tv.TvCommonManager;
import android.os.SystemClock;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

//import android.media.MediaPlayer.EnumVideoAspectRatio;
public class DlnaVideoPlayActivity extends Activity {
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

    public static final int OPTION_STATE_REWIND = 0x01;
    public static final int OPTION_STATE_PLAY = 0x02;
    public static final int OPTION_STATE_WIND = 0x03;
    public static final int OPTION_STATE_TIME = 0x04;
    // To determine which control for focus
    protected static int state = OPTION_STATE_PLAY;
    private static final String TAG = "DlnaVideoPlayActivity";
    public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
    public static final String ACTION_CHANGE_NET = "com.mstar.localmm.network.disconnect";
    public static final String VIDEO_PLAY_MODE = "PLAY_MODE";
    private DlnaVideoPlayHolder videoPlayHolder;
    // video is in play
    private boolean isPlaying = true;
    public static final int SEEK_POS = 14;
    // video article control whether display
    private boolean isControllerShow = true;
    // video control bar to be automatic hidden time
    private static final int DEFAULT_TIMEOUT = 15 * 1000;
    // the current video broadcast to the position
    private int currentPlayerPosition = 0;
    private TvCommonManager appSkin;
    // video buffer progress bar
    private ACProgressFlower progressDialog;
    // from the VideoActivity selected to play video index, through the index
    // can obtain video
    protected int video_position = 0;
    // The current folder data sources
    protected ArrayList<BaseData> videoPlayList = new ArrayList<BaseData>();
    // Optional time control Dialog
    private ChooseTimePlayDialog chooseTimePlayDialog;
    private static int seekTimes = 0;
    private int seekPosition = 0;
    private int duration = 0;
    private int playSpeed;
    private boolean isAudioSupport = true;
    private boolean isVideoSupport = true;
    private boolean isPrepared = false;
    private WatchDog mPosUpdateDog = new WatchDog();
    private int inputSource;

    // Video correlation processing handler
    private Handler videoHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case Constants.HIDE_PLAYER_CONTROL:
                    hideController();
                    break;
                case Constants.HANDLE_MESSAGE_PLAYER_EXIT:
                    DlnaVideoPlayActivity.this.finish();
                    break;
                case SEEK_POS:
                    if(videoPlayHolder.mVideoPlayView != null && mCurrentIsPlaying){
                        currentPlayerPosition = mCurrentPosition;
                        // videoPlayHolder.mVideoPlayView.getCurrentPosition();
                    videoPlayHolder.videoSeekBar
                            .setProgress(currentPlayerPosition);
                    String time = Tools.formatDuration(currentPlayerPosition);
                    videoPlayHolder.current_time_video.setText(time);
                    if (chooseTimePlayDialog != null) {
                        chooseTimePlayDialog
                                .getVideoTimeCurrentPositionTextView().setText(
                                        time);
                    }
                    videoHandler.sendEmptyMessageDelayed(SEEK_POS, 500);
                }
                    break;
                case Constants.CHOOSE_TIME:
                    seekBarListener.onProgressChanged(videoPlayHolder.videoSeekBar,
                        msg.arg1, true);
                    videoPlayHolder.SetVideoTimeSelect(false);
                    break;
                case Constants.SEEK_TIME:
                    // videoPlayHolder.mVideoPlayView.seekTo(seekPosition);
                    // seekTimes = 0;
                    mModelHandler.sendEmptyMessage(ModelHandler.MSG_SEEK_TO);
                    break;
                case ModelHandler.MSG_HIDE_CTL :
                    hideControlDelay();
                    break;
                case ModelHandler.MSG_SET_PLAY_SPEED :
                    videoPlayHolder.setPlaySpeed(msg.arg1);
                    break;
                case ModelHandler.MSG_SET_REW_SLT :
                    videoPlayHolder.SetVideoRewindSelect(msg.arg1 > 0 ? true:false);
                    break;
                case ModelHandler.MSG_SET_WIN_SLT :
                    videoPlayHolder.SetVideoWindSelect(msg.arg1 > 0 ? true:false);
                    break;
                case ModelHandler.MSG_SET_PLAY_SLT :
                    videoPlayHolder.SetVideoPlaySelect((msg.arg1 > 0 ? true:false),
                                                       (msg.arg2 > 0 ? true:false));
                    break;
                case ModelHandler.MSG_CANCEL_HIDE :
                    cancleDelayHide();
                    break;
                default:
                    break;

            }
        };
    };
    private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            // The progress of the progress bar is changed
            if (fromUser) {
                if (!videoPlayHolder.mbNotSeek) {
                    videoPlayHolder
                        .setAllUnSelect(getCurrIsPlaying());
                    cancleDelayHide();
                    seekPosition = progress;
                    if (duration <= progress) {
                        seekPosition = progress - 1000;
                    }
                    videoHandler.removeMessages(Constants.SEEK_TIME);
//                  localPause(true);
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
        setContentView(R.layout.dlna_video_player_frame);
        InitView();
        String video_path = "";
        String videoName = "";
        video_position = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        videoPlayList = getIntent().getParcelableArrayListExtra(
                Constants.BUNDLE_LIST_KEY);
        video_path = videoPlayList.get(video_position).getPath();
        videoName = videoPlayList.get(video_position).getName();
        mModelThread.start();
        mModelHandler = new ModelHandler(mModelThread.getLooper());
        InitVideoPlayer(video_path);
        ControlButtListener listener = new ControlButtListener();
        videoPlayHolder.setOnClickListener(listener);
        videoPlayHolder.setVideoName(videoName);
        hideControlDelay();
        // Registered monitor shutdown broadcast
        IntentFilter ittfile = new IntentFilter();
        ittfile.addAction(Intent.ACTION_SHUTDOWN);
        ittfile.addAction(ACTION_CHANGE_SOURCE);
        ittfile.addAction(ACTION_CHANGE_NET);
        this.registerReceiver(receiver, ittfile);
        changeSource();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "***************onResume********");
        state = OPTION_STATE_PLAY;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "***************onDestroy********");
        mPosUpdateDog.disable();
        mModelThread.getLooper().quit();
        // Avoid activity is lost player anomaly to upper send anomaly code lead
        // to showErrorDialog error
        if (videoPlayHolder.mVideoPlayView != null) {
            videoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
        }
        dismissProgressDialog();
        unregisterReceiver(receiver);
        super.onDestroy();
        /*
         * new Thread(new Runnable() {
         *
         * @Override public void run() {
         * videoPlayHolder.mVideoPlayView.stopPlayer(); } }).start();
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
        if(KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        boolean bRet = false;
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (!isPlaying) {
                localResumeAsyn(true);
            } else {
                if (playSpeed != 0) {
                    localResumeFromSpeedAsyn(true);
                }
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (isPlaying) {
                localPauseAsyn(true);
            }
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_MEDIA_STOP){
            if (videoPlayHolder.mVideoPlayView != null) {
                videoPlayHolder.mVideoPlayView.stopPlayer();
                videoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
            slowForwardAsyn();
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            fastForwardAsyn();
        }
        if (!isControllerShow && keyCode != KeyEvent.KEYCODE_BACK) {
            showController();
            hideControlDelay();
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE);
            return super.onKeyUp(keyCode, event);
        } else {
            switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                bRet = true;
                break;
            case KeyEvent.KEYCODE_ENTER:
                registerListeners();
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                videoPlayHolder.processRightKey(keyCode, event);
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                videoPlayHolder.processLeftKey(keyCode, event);
                bRet = true;
                break;
            case KeyEvent.KEYCODE_MENU:
                bRet = true;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return super.onKeyUp(keyCode, event);
                // case KeyEvent.KEYCODE_ASPECT_RATIO:
                // currentVideoScale = sharedata.getInt(VIDEO_PLAY_MODE,
                // _AUTO);
                //
                // switch (currentVideoScale) {
                // case _AUTO:
                // currentVideoScale = _4X3;
                // showToastTip(getString(R.string.aspect_ratio_4X3));
                // videoPlayHolder.mVideoPlayView.getMediaPlayer()
                // .setVideoDisplayAspectRatio(
                // EnumVideoAspectRatio.E_VIDEO_ASPECT_RATIO_4X3);
                // setVideoMode(_4X3);
                // break;
                // case _4X3:
                // currentVideoScale = _16X9;
                // videoPlayHolder.mVideoPlayView.getMediaPlayer()
                // .setVideoDisplayAspectRatio(
                // EnumVideoAspectRatio.E_VIDEO_ASPECT_RATIO_16X9);
                // showToastTip(getString(R.string.aspect_ratio_16X9));
                // setVideoMode(_16X9);
                // break;
                //
                // case _16X9:
                // currentVideoScale = _AUTO;
                // videoPlayHolder.mVideoPlayView.getMediaPlayer()
                // .setVideoDisplayAspectRatio(
                // EnumVideoAspectRatio.E_VIDEO_ASPECT_RATIO_AUTO);
                // showToastTip(getString(R.string.aspect_ratio_default));
                // setVideoMode(_AUTO);
                // break;
                // }
                // bRet = true;
                //
                // break;
            default:
                bRet = false;
                break;
            }
        }
        if (bRet)
            return true;
        else
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
            case OPTION_STATE_REWIND:
                slowForwardAsyn();
                break;
            case OPTION_STATE_PLAY:
                if (isPrepared) {
                    if (isPlaying) {
                        if (playSpeed != 0) {
                            localResumeFromSpeedAsyn(true);
                        } else {
                            localPauseAsyn(true);
                        }
                    } else {
                        localResumeAsyn(true);
                    }
                }
                break;
            case OPTION_STATE_WIND:
                fastForwardAsyn();
                break;
            case OPTION_STATE_TIME:
                cancleDelayHide();
                showVideoTimeSet();
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
        videoPlayHolder.mbNotSeek = false;
        // Setting error of the monitor
        videoPlayHolder.mVideoPlayView
                .setPlayerCallbackListener(myPlayerCallback);
        if (videoPlayPath != null) {
            // videoPlayHolder.mVideoPlayView.stopPlayback();
            videoPlayHolder.mVideoPlayView.setVideoPath(videoPlayPath, 1);
        }
    }

    // Pop up display an error dialog box
    private void showErrorDialog(String strMessage) {
        if (!isFinishing()) {
            new AlertDialog.Builder(DlnaVideoPlayActivity.this)
                    .setTitle(getResources().getString(R.string.show_info))
                    .setMessage(strMessage)
                    .setPositiveButton(
                            getResources().getString(R.string.exit_ok),
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    // videoPlayHolder.mVideoPlayView.stopPlayback();
                                    dismissProgressDialog();
                                    DlnaVideoPlayActivity.this.finish();
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
                videoPlayHolder.mVideoPlayView.stopPlayback();
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
            finish();
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }

        @Override
        public void onPrepared(MediaPlayer mp, int viewId) {
            dismissProgressDialog();
            duration = (int) videoPlayHolder.mVideoPlayView.getDuration();
            Log.i(TAG, "getDuration()" + duration);
            String time = Tools.formatDuration(duration);
            videoPlayHolder.total_time_video.setText(time);
            videoPlayHolder.videoSeekBar.setMax(duration);
            videoPlayHolder.mVideoPlayView.start();
            isPrepared = true;
            videoPlayHolder.SetVideoPlaySelect(true,getCurrIsPlaying());
            hideControlDelay();
            mPosUpdateDog.enable();
            videoHandler.sendEmptyMessage(SEEK_POS);
        }

        @Override
        public void onSeekComplete(MediaPlayer mp, int viewId) {
            videoPlayHolder.setPlaySpeed(1);
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
                            .getSubtitleData(videoPlayHolder.mVideoPlayView.getMMediaPlayer());
                    Log.i("*************", "*******setSubTitleText******" + str);
                    if (str.length() >= 1)
                        videoPlayHolder.setSubTitleText(str);
                    return true;
                }
                if (extra == 0) {
                    // Subtitles hidden
                    videoPlayHolder.setSubTitleText("");
                    return true;
                }
                break;
            case MMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                videoPlayHolder.mbNotSeek = true;
                String strMessage = getResources().getString(
                        R.string.video_media_infor_no_index);
                videoPlayHolder.setPlaySpeed(1);
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

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height, int viewId) {
        }
    };

    /**
     * Initialization view
     */
    private void InitView() {
        videoPlayHolder = new DlnaVideoPlayHolder(DlnaVideoPlayActivity.this);
        videoPlayHolder.videoSeekBar
                .setOnSeekBarChangeListener(seekBarListener);
        // Video control bar
        videoPlayHolder.playControlLayout = (LinearLayout) findViewById(R.id.dlna_video_suspension_layout);
    }

    /**
     * Click the mouse button response
     */
    class ControlButtListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isControllerShow)
                return;
            switch (v.getId()) {
            case R.id.dlna_video_rewind:
                if (mRsponseAble) {
                    videoPlayHolder.setAllUnSelect(getCurrIsPlaying());
                    slowForwardAsyn();
                }
                break;
            case R.id.dlna_video_play:
                if (isPrepared && mRsponseAble) {
                    videoPlayHolder.setAllUnSelect(getCurrIsPlaying());
                    if (isPlaying) {
                        if (playSpeed != 0) {
                            localResumeFromSpeedAsyn(true);
                        } else {
                            localPauseAsyn(true);
                        }
                    } else {
                        localResumeAsyn(true);
                    }
                }
                break;
            case R.id.dlna_video_wind:
                if (mRsponseAble) {
                    videoPlayHolder.setAllUnSelect(getCurrIsPlaying());
                    fastForwardAsyn();
                }
                break;
            case R.id.dlna_video_time:
                videoPlayHolder.setAllUnSelect(getCurrIsPlaying());
                state = OPTION_STATE_TIME;
                cancleDelayHide();
                showVideoTimeSet();
                hideControlDelay();
                break;
            default:
                break;
            }
        }
    }

    // To receive shutdown broadcast (or source switching/network interruption)
    // exit play interface
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,
                    "*******BroadcastReceiver**********" + intent.getAction());
            // Avoid activity is lost player anomaly to upper send anomaly code
            // lead to showErrorDialog error
            if (videoPlayHolder.mVideoPlayView != null) {
                videoPlayHolder.mVideoPlayView.setPlayerCallbackListener(null);
            }
            dismissProgressDialog();
            finish();
        }
    };

    /**
     * Local broadcast suspended
     */
    protected void localPause(boolean bSelect) {
        cancleDelayHide();
        videoPlayHolder.mVideoPlayView.setVoice(false);
        videoPlayHolder.mVideoPlayView.setPlayMode(0);
        videoPlayHolder.mVideoPlayView.pause();
        videoPlayHolder.setPlaySpeed(1);
        videoPlayHolder.mVideoPlayView.setVoice(true);
        isPlaying = false;
        videoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        hideControlDelay();
    }
    protected void localPauseAsyn(boolean bSelect) {
        if (mRsponseAble) {
            Message msg2 = new Message();
            msg2.arg1 = bSelect ? 1 : 0;
            msg2.what = ModelHandler.MSG_DLNA_VIDEO_PAUSE;
            mModelHandler.sendMessage(msg2);
        }
    }

    /**
     * Local broadcast recovery
     */
    protected void localResume(boolean bSelect) {
        cancleDelayHide();
        videoPlayHolder.mVideoPlayView.start();
        isPlaying = true;
        videoPlayHolder.setPlaySpeed(1);
        videoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        mPosUpdateDog.enable();
        videoHandler.sendEmptyMessage(SEEK_POS);
        hideControlDelay();
    }
    protected void localResumeAsyn(boolean bSelect) {
        if (mRsponseAble) {
            Message msg3 = new Message();
            msg3.arg1 = bSelect ? 1 : 0;
            msg3.what = ModelHandler.MSG_DLNA_VIDEO_RESUME;
            mModelHandler.sendMessage(msg3);
        }
    }


    /**
     * Local broadcast recovery
     */
    protected void localResumeFromSpeed(boolean bSelect) {
        // cancleDelayHide();
        videoPlayHolder.mVideoPlayView.setVoice(true);
        videoPlayHolder.mVideoPlayView.setPlayMode(0);
        isPlaying = true;
        playSpeed = 0;
        videoPlayHolder.setPlaySpeed(1);
        videoPlayHolder.SetVideoPlaySelect(bSelect, isPlaying);
        videoHandler.removeMessages(SEEK_POS);
        videoHandler.sendEmptyMessage(SEEK_POS);
        // hideControlDelay();
    }
    protected void localResumeFromSpeedAsyn(boolean bSelect) {
        if (mRsponseAble) {
            Message msg1 = new Message();
            msg1.arg1 = bSelect ? 1 : 0;
            msg1.what = ModelHandler.MSG_DLNA_VIDEO_RESUME_SPEED;
            mModelHandler.sendMessage(msg1);
        }
    }

    /**
     * Backward times quick play
     */
    private void slowForward() {
        if (!isPlaying) {
            videoPlayHolder.mVideoPlayView.setVoice(false);
            localResume(false);
        }
        playSpeed = videoPlayHolder.mVideoPlayView.getPlayMode();
        if (playSpeed < 0) {
            return;
        }
        int currentSpeed = 1 * (-2);
        /*
         * if (playSpeed < 64 && playSpeed < 0) { currentSpeed = playSpeed * 2;
         * }
         */
        playSpeed = currentSpeed;
        videoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
        // Set the current approaching speed display string
        videoPlayHolder.setPlaySpeed(currentSpeed);
    }

    private void slowForwardAsyn() {
        if (mRsponseAble) {
            mModelHandler.sendEmptyMessage(ModelHandler.MSG_DLNA_VIDEO_REWIND);
        }
    }

    /**
     * Forward times quick play
     */
    private void fastForward() {
        if (!isPlaying) {
            videoPlayHolder.mVideoPlayView.setVoice(false);
            localResume(false);
        }
        playSpeed = videoPlayHolder.mVideoPlayView.getPlayMode();
        if (playSpeed > 1) {
            return;
        }
        int currentSpeed = 1 * 2;
        /*
         * if (playSpeed < 64 && playSpeed > 0) { currentSpeed = playSpeed * 2;
         * }
         */
        playSpeed = currentSpeed;
        videoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
        // Set the current approaching speed display string
        videoPlayHolder.setPlaySpeed(currentSpeed);
    }
    private void fastForwardAsyn() {
        if (mRsponseAble) {
            mModelHandler.sendEmptyMessage(ModelHandler.MSG_DLNA_VIDEO_WIND);
        }
    }

    /**
     * To display the play time Settings dialog box
     */
    private void showVideoTimeSet() {
        videoPlayHolder.SetVideoTimeSelect(true);
        // Choose the operation of Dialog
        if (chooseTimePlayDialog == null) {
            chooseTimePlayDialog = new ChooseTimePlayDialog(
                    DlnaVideoPlayActivity.this, R.style.choose_time_dialog);
            chooseTimePlayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = chooseTimePlayDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.x = 150;
            lp.y = 180;
            chooseTimePlayDialog.getWindow().setAttributes(lp);
            chooseTimePlayDialog.setVariable(videoPlayHolder.mbNotSeek,
                    videoHandler);
        }
        chooseTimePlayDialog.show();
        duration = (int) videoPlayHolder.mVideoPlayView.getDuration();
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
        if (videoPlayHolder.playControlLayout != null) {
            videoPlayHolder.playControlLayout.setVisibility(View.VISIBLE);
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
        if (videoPlayHolder.playControlLayout != null) {
            videoPlayHolder.playControlLayout.setVisibility(View.INVISIBLE);
            isControllerShow = false;
        } else
            System.err.println("playControlLayout is null ptr!!");
    }

    // Pop up display Tip
    private void showToastTip(String strMessage) {
        Toast toast = Toast.makeText(DlnaVideoPlayActivity.this, strMessage,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if(!isVideoSupport && !isAudioSupport){
            DlnaVideoPlayActivity.this.finish();
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
                    // .getTvCommonManager(DlnaVideoPlayActivity.this);
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

    private boolean mRsponseAble = true;

    private HandlerThread mModelThread = new HandlerThread(this.getClass().getName());
    private ModelHandler mModelHandler = null;//new ModelHandler(mModelThread.getLooper());
    class ModelHandler extends Handler {
        public static final int MSG_BASE = 1999;
        public static final int MSG_DLNA_VIDEO_REWIND = MSG_BASE+0;
        public static final int MSG_DLNA_VIDEO_RESUME = MSG_BASE+1;
        public static final int MSG_DLNA_VIDEO_WIND = MSG_BASE+2;
        public static final int MSG_DLNA_VIDEO_RESUME_SPEED = MSG_BASE+3;
        public static final int MSG_DLNA_VIDEO_PAUSE = MSG_BASE+4;
        public static final int MSG_HIDE_CTL = MSG_BASE+5;
        public static final int MSG_SET_PLAY_SPEED = MSG_BASE+6;
        public static final int MSG_SET_REW_SLT = MSG_BASE+7;
        public static final int MSG_SET_WIN_SLT = MSG_BASE+8;
        public static final int MSG_SET_PLAY_SLT = MSG_BASE+9;
        public static final int MSG_CANCEL_HIDE = MSG_BASE+10;
        public static final int MSG_GET_PLAYING = MSG_BASE+11;
        public static final int MSG_SEEK_TO = MSG_BASE+12;

        ModelHandler(Looper l) {
            super(l);
        }
        private void setSpeed(int speed) {
            Message msg1 = new Message();
            msg1.what = MSG_SET_PLAY_SPEED;
            msg1.arg1 = speed;
            videoHandler.sendMessage(msg1);
        }
        private void setPlaySlt(int a , int b) {
            // mCurrentIsPlaying = videoPlayHolder.mVideoPlayView.isPlaying();
            Message msg2 = new Message();
            msg2.what = MSG_SET_PLAY_SLT;
            msg2.arg1 = a;
            msg2.arg2 = b;
            videoHandler.sendMessage(msg2);
        }
        private void setRewSlt(int b) {
            Message msg2 = new Message();
            msg2.what = MSG_SET_REW_SLT;
            msg2.arg1 = b;
            videoHandler.sendMessage(msg2);
        }
        private void setWinSlt(int b) {
            Message msg2 = new Message();
            msg2.what = MSG_SET_WIN_SLT;
            msg2.arg1 = b;
            videoHandler.sendMessage(msg2);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            mRsponseAble = false;
            switch(msg.what) {
            case MSG_DLNA_VIDEO_REWIND :
            {
                videoHandler.sendEmptyMessage(MSG_CANCEL_HIDE);
                if (!isPlaying) {
                    videoPlayHolder.mVideoPlayView.setVoice(false);
                    videoPlayHolder.mVideoPlayView.start();
                    isPlaying = true;
                    setSpeed(1);
                    setPlaySlt(0,1);
                    mPosUpdateDog.enable();
                    videoHandler.sendEmptyMessage(SEEK_POS);
                }
                playSpeed = videoPlayHolder.mVideoPlayView.getPlayMode();
                if (playSpeed < 0) {
                    break;
                }
                int currentSpeed = 1 * (-2);
                playSpeed = currentSpeed;
                videoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
                setSpeed(-2);
                videoHandler.sendEmptyMessage(MSG_HIDE_CTL);
                setRewSlt(1);
                state = OPTION_STATE_REWIND;
            }
            break;
            case MSG_DLNA_VIDEO_RESUME_SPEED :
            {
                videoHandler.sendEmptyMessage(MSG_CANCEL_HIDE);
                videoPlayHolder.mVideoPlayView.setVoice(true);
                videoPlayHolder.mVideoPlayView.setPlayMode(0);
                isPlaying = true;
                playSpeed = 0;
                setSpeed(1);
                setPlaySlt(msg.arg1,1);
                videoHandler.removeMessages(SEEK_POS);
                videoHandler.sendEmptyMessage(SEEK_POS);
                videoHandler.sendEmptyMessage(MSG_HIDE_CTL);
                state = OPTION_STATE_PLAY;
            }
            break;
            case MSG_DLNA_VIDEO_RESUME :
            {
                videoHandler.sendEmptyMessage(MSG_CANCEL_HIDE);
                videoPlayHolder.mVideoPlayView.start();
                isPlaying = true;
                setSpeed(1);
                setPlaySlt(msg.arg1,1);
                videoHandler.sendEmptyMessage(MSG_HIDE_CTL);
                state = OPTION_STATE_PLAY;
            }
            break;
            case MSG_DLNA_VIDEO_PAUSE :
            {
                videoHandler.sendEmptyMessage(MSG_CANCEL_HIDE);
                videoPlayHolder.mVideoPlayView.setVoice(false);
                videoPlayHolder.mVideoPlayView.setPlayMode(0);
                videoPlayHolder.mVideoPlayView.pause();
                setSpeed(1);
                videoPlayHolder.mVideoPlayView.setVoice(true);
                isPlaying = false;
                setPlaySlt(msg.arg1,0);
                videoHandler.sendEmptyMessage(MSG_HIDE_CTL);
                state = OPTION_STATE_PLAY;
            }
            break;
            case MSG_DLNA_VIDEO_WIND :
            {
                videoHandler.sendEmptyMessage(MSG_CANCEL_HIDE);
                if (!isPlaying) {
                    videoPlayHolder.mVideoPlayView.setVoice(false);
                    videoPlayHolder.mVideoPlayView.start();
                    isPlaying = true;
                    setSpeed(1);
                    setPlaySlt(0,1);
                    mPosUpdateDog.enable();
                    videoHandler.sendEmptyMessage(SEEK_POS);
                }
                playSpeed = videoPlayHolder.mVideoPlayView.getPlayMode();
                if (playSpeed > 1) {
                    break;
                }
                int currentSpeed = 1 * 2;
                playSpeed = currentSpeed;
                videoPlayHolder.mVideoPlayView.setPlayMode(currentSpeed);
                setSpeed(currentSpeed);
                setWinSlt(1);
                videoHandler.sendEmptyMessage(MSG_HIDE_CTL);
                state = OPTION_STATE_WIND;
            }
            break;
            case MSG_GET_PLAYING :
                mCurrentIsPlaying = videoPlayHolder.mVideoPlayView.isPlaying();
                break;
            case MSG_SEEK_TO :
                videoPlayHolder.mVideoPlayView.seekTo(seekPosition);
                seekTimes = 0;
                break;
            }
            mRsponseAble = true;
        }

    }

    private int mCurrentPosition = 0;
    private boolean mCurrentIsPlaying = false;
    class UpdatePositionThread extends Thread {
        public boolean mGoOn = false;
        private long mStartTime = 0; //ms
        private long mDoneTime = 0; //ms
        private long mSleepTime = 0; //ms
        @Override
        public void run() {
            mGoOn = true;
            while(mGoOn) {
                mCurrentIsPlaying = videoPlayHolder.mVideoPlayView.isPlaying();
                mStartTime = SystemClock.uptimeMillis();
                mCurrentPosition = videoPlayHolder.mVideoPlayView
                    .getCurrentPosition();
                mDoneTime = SystemClock.uptimeMillis();
                mSleepTime = 380 - (mDoneTime - mStartTime);
                // Log.d(TAG , "start="+mStartTime+" end="+mDoneTime+" sleep="+mSleepTime);
                try {
                    if (mSleepTime > 6) {
                        Thread.sleep(mSleepTime);
                    }
                } catch (InterruptedException e) {}
            }
        }

    }
    private boolean getCurrIsPlaying() {
        mModelHandler.sendEmptyMessage(ModelHandler.MSG_GET_PLAYING);
        SystemClock.sleep(120);
        return mCurrentIsPlaying;
    }
    class WatchDog extends Thread {
        private UpdatePositionThread mThread = new UpdatePositionThread();
        private boolean mGoOn = false;
        private int mSleepTime = 500; //ms

        public void enable() {
            if (!mGoOn) {
                mGoOn = true;
                this.start();
            }
        }

        public void disable() {
            mGoOn = false;
        }

        public void run() {
            mThread.start();
            while(true) {
                if (!mGoOn) {
                    if (mThread.isAlive()) {
                        mThread.interrupt();
                        mThread.mGoOn = false;
                    }
                    break;
                }

                if (!mThread.isAlive()) {
                    mThread.mGoOn = false;
                    mThread = new UpdatePositionThread();
                    mThread.start();
                }else{
                    try {
                        Thread.sleep(mSleepTime);
                    } catch (InterruptedException e) {}
                }
            }
        }
    }
}
