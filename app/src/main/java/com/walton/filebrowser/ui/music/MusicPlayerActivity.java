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
package com.walton.filebrowser.ui.music;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.walton.filebrowser.FocusTest;
import com.walton.filebrowser.IMediaService;
import com.walton.filebrowser.IMusicStatusListener;
import com.walton.filebrowser.R;
import com.walton.filebrowser.adapter.AdapterMusicSubList;
import com.walton.filebrowser.adapter.UsbItemAdapter;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.business.video.LoginInfoDBAdapter;
import com.walton.filebrowser.dlna.server.DLNAConstants;
import com.walton.filebrowser.ui.MediaContainerApplication;
import com.walton.filebrowser.ui.visualizer.WaveLoadingView;
import com.walton.filebrowser.ui.visualizer.WaveLoadingView3;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.GridViewTV;
import com.walton.filebrowser.util.MusicUtils;
import com.walton.filebrowser.util.ToastFactory;
import com.walton.filebrowser.util.MusicUtils.ServiceToken;
import com.walton.filebrowser.util.ToastHelper;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

import com.walton.filebrowser.util.TvApiController;
import com.walton.filebrowser.util.DolbyLogoView;
import com.walton.filebrowser.view.GridLayoutManagerModified;

import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicPlayerActivity extends Activity {
    private static final String TAG = "MusicPlayerActivity";
    public static final String ACTION_STOP_MUSIC = "com.jrm.dtv.stop.music";
    public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
    public static final int HANDLE_MESSAGE_ERROR = 0;
    public static final int HANDLE_MESSAGE_COMPLETION = 1;
    public static final int HANDLE_MESSAGE_SEEK_COMPLETE = 2;
    public static final int HANDLE_MESSAGE_INFOR = 3;
    public static final int HANDLE_MESSAGE_PREPARE = 4;
    public static final int HANDLE_MESSAGE_LRC = 5;
    // Content item list
    public AdapterMusicSubList adapter = null;
    // Songs spectrum
    public static final int HANDLE_MESSAGE_SONGS_SPECTRUM = 6;
    public static final int HANDLE_MESSAGE_SEEKBAR_UPDATE = 7;
    public static final int HANDLE_MESSAGE_SERVICE_START = 8;
    // private static final int HANDLE_MESSAGE_DLNAPLAY_FINISH = 9;
    public static final int HANDLE_MESSAGE_MUSIC_PREPARE = 10;
    public static final int HANDLE_MESSAGE_ONCLICK_LISTENER = 11;
    public static final int HANDLE_MESSAGE_PLAYER_EXIT = 12;
    public static final int HANDLE_MESSAGE_UPDATE_ARTIST = 13;
    public static final int HANDLE_MESSAGE_IMAGE_ALBUM_UPDATE = 14;
    public static final int HANDLE_MESSAGE_SHOW_DOLBY_LOGO = 15;
    public static final int HANDLE_MESSAGE_HIDE_DOLBY_LOGO = 16;
    private static final int KEYCODE_PICTURE_MODE = 252;
    private static final int KEYCODE_ASPECT_RATIO = 253;
    private MusicPlayerListener mMusicPlayerListener;
    private MusicPlayerViewHolder mMusicPlayerViewHolder;
    // To determine which control for focus
    public static int state = 2;
    public static int state_last = 2;
    public static final String ONPLAYCOMPLETED = "onplaycompleted";
    public static final String LRCPATH = "/sdcard/music/";
    public static final int PLAY_PAUSE = 1;
    public static final int PLAY_PLAY = 2;
    public static int currentPlayStatus = 0;
    private DiskChangeReceiver receiver;
    private String path = null;
    // data
    public static List<BaseData> musicList;// = new ArrayList<BaseData>();
    // PhotoActivity from the chosen to play music index, through the index can
    // obtain music detailed information
    public static int currentPosition = 0;
    private ServiceToken stToken;
    public int scrollstate = 0;
    public boolean isLrcShow = false;
    // Music is prepared
    public boolean isPrepared = false;
    public static Thread thread = new Thread();
    private static LrcRead mLrcRead;
    private static int index = 0;
    private static int currentTime = 0;
    public static int countTime = 0;
    private int sourceFrom;
    private List<LyricContent> mLyricList = new ArrayList<LyricContent>();
    protected static IMediaService mService = null;
    protected boolean isNextMusic = true;
    protected boolean clickable = false;
    private TvCommonManager appSkin;
    private boolean isAudioSupport = true;
    private boolean isVideoSupport = true;
    private TvCecManager mCecManager;
    MediaScannerConnection mScanConnection;
    protected boolean isErrorDialogShow = false;
    private String mMusicListDevicePath = null;

    public DolbyLogoView mDolbyLogoView = null;
    public InvocationHandler mInvocationHandler = null;

    private int currentSelectedPosition;

    // play model to deal with
    protected void processPlayCompletion() {
        // 0. Single cycle 1. Random play 2. List cycle
        int playmode = MusicPlayerListener.currentPlayMode;
        switch (playmode) {
            case MusicPlayerListener.SINGE:
                break;
            case MusicPlayerListener.REPEAT:
                if (musicList.size() - 1 <= 0) {
                    currentPosition = 0;
                } else {
                    currentPosition = new Random().nextInt(musicList.size());
                }
                break;
            case MusicPlayerListener.ORDER:
                if (isNextMusic) {
                    if (currentPosition >= musicList.size() - 1) {
                        currentPosition = 0;
                    } else {
                        currentPosition++;
                    }
                } else {
                    if (musicList.size() > 0) {
                        if (currentPosition - 1 >= 0) {
                            currentPosition--;
                        } else {
                            currentPosition = musicList.size() - 1;
                        }
                    }
                }
                isNextMusic = true;
                break;
        }
        mDolbyLogoView.hideDolbyLogo();
        new Thread(new Runnable() {
            public void run() {
                if (clickable) {
                    clickable = false;
                    musicPlayHandle
                            .sendEmptyMessage(HANDLE_MESSAGE_SERVICE_START);
                } else {
                    musicPlayHandle
                            .removeMessages(HANDLE_MESSAGE_SERVICE_START);
                    musicPlayHandle
                            .sendEmptyMessage(HANDLE_MESSAGE_SERVICE_START);
                }
            }
        }).start();
    }

    private void processPlayPrepare() {
        // Get countTime and audioSessionId
        try {
            if (mService != null) {
                countTime = (int) mService.getDuration();
                // MusicPlayListener.isPlaying = mService.isPlaying();
                // toukir
//                mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(true);
//                mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
//                mMusicPlayerViewHolder.bt_MusicPlay
//                        .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                state = MusicPlayerListener.OPTION_STATE_PLAY;
                //getPicOfAlbum(musicList, currentPosition);
                mMusicPlayerListener.setSongsContent(musicList, currentPosition);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentTime = 0;
        mMusicPlayerViewHolder.playTime.setText(Tools
                .formatDuration(currentTime));
        mMusicPlayerViewHolder.seekBar.setProgress(currentTime);
        try {
            if (mService != null) {
                countTime = (int) mService.getDuration();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (countTime > 0) {
            mMusicPlayerViewHolder.seekBar.setMax(countTime);
            mMusicPlayerViewHolder.durationTime.setText(Tools
                    .formatDuration(countTime));
        }
        musicPlayHandle.sendEmptyMessageDelayed(
                MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE, 100);
        Log.i(TAG, "MusicPlayActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE");
        if (isSubListItemClicked) {
            isPrepared = false;
        } else {
            isPrepared = true;
        }
    }

    private IMusicStatusListener musicStatusListener = new IMusicStatusListener.Stub() {
        @Override
        public void musicSeekCompleted() throws RemoteException {
            Log.d(TAG, "IMusicStatusListener.Stub, musicSeekCompleted");
        }

        @Override
        public void musicPrepared() {
            // Ready to play, get countTime and audioSessionId
            // processPlayPrepare();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "IMusicStatusListener.Stub, musicPrepared");
                    musicPlayHandle.sendEmptyMessage(HANDLE_MESSAGE_MUSIC_PREPARE);
                }
            }).start();
        }

        @Override
        public boolean musicPlayErrorWithMsg(String errMessage)
                throws RemoteException {
            Log.d(TAG, "IMusicStatusListener.Stub, musicPlayErrorWithMsg");
            exceptionProcess(errMessage);
            musicPlayHandle.removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
            return true;
        }

        @Override
        public void musicPlayException(String errMessage)
                throws RemoteException {
            Log.d(TAG, "IMusicStatusListener.Stub, musicPlayException");
            exceptionProcess(errMessage);
            musicPlayHandle.removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
        }

        @Override
        public void musicCompleted() throws RemoteException {
            Log.d(TAG, "IMusicStatusListener.Stub, musicCompleted");
            musicPlayHandle.removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
            if (!isErrorDialogShow) {
                processPlayCompletion();
                notifySubList();
            }
        }

        @Override
        public void handleSongSpectrum() throws RemoteException {
            // Spectrum processing
            Log.d(TAG, "IMusicStatusListener.Stub, handleSongSpectrum");
        }

        @Override
        public void handleMessageInfo(String strMessage) throws RemoteException {
            if (strMessage == getResources().getString(R.string.video_media_error_video_unsupport)) {
                isVideoSupport = false;
            }
            if (strMessage == getResources().getString(R.string.video_media_error_audio_unsupport)) {
                isAudioSupport = false;
                Toast toast = ToastFactory.getToast(MusicPlayerActivity.this, strMessage,
                        Gravity.CENTER);
                toast.show();
            }
            if (!isVideoSupport && !isAudioSupport) {
                if (musicList.size() <= 1) {
                    exitMusicPaly();
                } else {
                    musicPlayHandle
                            .removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
                    processPlayCompletion();
                }
            }
            Log.d(TAG, "IMusicStatusListener.Stub, handleMessageInfo");
        }
    };

    public Handler musicPlayHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_MESSAGE_SEEKBAR_UPDATE:
                    try {
                        if (mService != null && mService.isPlaying()) {
                            currentTime = (int) mService.getCurrentPosition();
                            mMusicPlayerViewHolder.playTime.setText(Tools
                                    .formatDuration(currentTime));
                            // 0357597:Need to always get a total duration when play dlna aac file.
                            // but it will cause the MarqueeText flash in mantis:0868875, so remove it.
                        /*if (countTime != (int) mService.getDuration()) {
                            countTime = (int) mService.getDuration();
                            mMusicPlayerViewHolder.durationTime.setText(Tools
                                    .formatDuration(countTime));
                            mMusicPlayerViewHolder.seekBar.setMax(countTime);
                        }*/
                            mMusicPlayerViewHolder.seekBar.setProgress(currentTime);
                            if (isLrcShow) {
                                setLRCIndex();
                                // mMusicPlayViewHolder.lycrUtil.RefreshLRC(currentTime);
                            }
                            musicPlayHandle
                                    .sendEmptyMessageDelayed(
                                            MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE,
                                            500);

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLE_MESSAGE_LRC:
                    // Lyrics treatment
                    mMusicPlayerViewHolder.mLyricView.SetIndex(index);
                    mMusicPlayerViewHolder.mLyricView.invalidate();
                    break;
                case HANDLE_MESSAGE_SERVICE_START:
                    initPlay();
                    break;
                case HANDLE_MESSAGE_MUSIC_PREPARE:
                    processPlayPrepare();
                    break;
                case HANDLE_MESSAGE_ONCLICK_LISTENER:
                    mMusicPlayerListener.registerListeners();
                    break;
                case HANDLE_MESSAGE_PLAYER_EXIT:
                    MusicPlayerActivity.this.finish();
                    break;
                case HANDLE_MESSAGE_UPDATE_ARTIST:
//                    getPicOfAlbum(musicList, currentPosition);
                    mMusicPlayerListener.setSongsContent(musicList, currentPosition);
                    pauseOrPlayPresentMusicNameMarquee();
                    break;
                case HANDLE_MESSAGE_IMAGE_ALBUM_UPDATE:
                    Log.i(TAG, "(Bitmap)msg.obj:" + (Bitmap) msg.obj);
                    if ((Bitmap) msg.obj != null) {
//                        mMusicPlayerViewHolder.bt_MusicImageAlbum.setImageBitmap((Bitmap) msg.obj);
                    }
                    break;
                case HANDLE_MESSAGE_SHOW_DOLBY_LOGO:
                    Log.v(TAG, "show dolby logo");
                    mDolbyLogoView.showDolbyLogo(msg.arg1);
                    musicPlayHandle.removeMessages(HANDLE_MESSAGE_HIDE_DOLBY_LOGO);
                    musicPlayHandle.sendEmptyMessageDelayed(HANDLE_MESSAGE_HIDE_DOLBY_LOGO, Constants.DOLBY_LOGO_SHOW_TIME);
                    break;
                case HANDLE_MESSAGE_HIDE_DOLBY_LOGO:
                    Log.v(TAG, "hide dolby logo");
                    mDolbyLogoView.hideDolbyLogo();
                    break;
                default:
                    break;
            }
        }
    };


    boolean isSubListItemClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--------- onCreate ---------");
        setContentView(R.layout.music_player_init);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        // mMusicPlayActivity = this;
        musicList = new ArrayList<BaseData>();
        MusicPlayerListener.currentPlayMode = 2;
        currentPosition = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        String IntentPath = Tools.parseUri(getIntent().getData());
        sourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
//        if (sourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
//            musicList = getIntent().getParcelableArrayListExtra(Constants.BUNDLE_LIST_KEY);
//        } else if (sourceFrom == Constants.SOURCE_FROM_SAMBA) {
//            musicList = MediaContainerApplication.getInstance().getMediaData(Constants.FILE_TYPE_SONG);
//        } else {
        musicList.clear();
        if (IntentPath != null) {
            BaseData bd = new BaseData();
            bd.setPath(IntentPath);
            bd.setName(Tools.getFileName(IntentPath));
            currentPosition = 0;
            musicList.add(bd);
            Log.d(TAG, "onCreate: 1.MusicList_Size:" + musicList.size());
        } else {
            musicList.addAll(MediaContainerApplication.getInstance()
                    .getMediaData(Constants.FILE_TYPE_SONG));

            Log.d(TAG, "onCreate: 2.MusicList_Size:" + musicList.size());
        }
        if (musicList != null && musicList.size() > 0) {
            mMusicListDevicePath = musicList.get(0).getPath();
        }
//        }
        mMusicPlayerViewHolder = new MusicPlayerViewHolder(MusicPlayerActivity.this);
        mMusicPlayerViewHolder.findView();

        // load songList
        adapter = new AdapterMusicSubList(getApplicationContext(), musicList);
        mMusicPlayerViewHolder.horizontalgridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mMusicPlayerViewHolder.horizontalgridview.post(new Runnable() {
            @Override
            public void run() {
                mMusicPlayerViewHolder.horizontalgridview.smoothScrollToPosition(currentPosition);
            }
        });

        adapter.setOnItemCallBack(new AdapterMusicSubList.OnItemCallBack() {
            @Override
            public void onFocusChange(View v, boolean hasFocus, int posiotion) {
                Log.d(TAG, "onFocusChange: " + String.valueOf(hasFocus));
//               if (hasFocus){
//                   mMusicPlayerViewHolder.mainUpViewSubList.setFocusView(v,1.0f);
//               }else {
//                   mMusicPlayerViewHolder.mainUpViewSubList.setVisibility(View.GONE);
//               }
            }

            @Override
            public void onItemClick(View v, int position) {
                isSubListItemClicked = true;
                currentPosition = position;
                musicPlayHandle.sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                notifySubList();
            }
        });

        mMusicPlayerViewHolder.horizontalgridview.setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                currentSelectedPosition = position;
                mMusicPlayerViewHolder.txtSongItemCount.setText(String.valueOf(position + 1) + " of " + String.valueOf(musicList.size()));
            }
        });

        setThumbnail(musicList.get(currentPosition).getPath());

        mMusicPlayerListener = new MusicPlayerListener(this, mMusicPlayerViewHolder);
        mMusicPlayerListener.addMusicListener();
        mMusicPlayerListener.initPlayMode();


//        num_of_column = 4;//mMusicPlayerViewHolder.row_childreen.getNumColumns();
//
//        if (musicList.size() > num_of_column) {
//            mMusicPlayerViewHolder.imgBelowMore.setVisibility(View.VISIBLE);
//        } else {
//            mMusicPlayerViewHolder.imgBelowMore.setVisibility(View.INVISIBLE);
//        }

//        mMusicPlayerViewHolder.grid_view_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int n = musicList.size() - num_of_column;
//                Log.d(TAG, "onItemSelected: A" + String.valueOf(n) + " " + String.valueOf(position));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // when selected last row then not showing down below
//                        if (musicList.size() - num_of_column < position) {
//                            mMusicPlayerViewHolder.imgBelowMore.setVisibility(View.INVISIBLE);
//                        } else {
//                            mMusicPlayerViewHolder.imgBelowMore.setVisibility(View.VISIBLE);
//                        }
//                        // when selected first row then not showing up image
//                        if (position < num_of_column) {
//                            mMusicPlayerViewHolder.imgUpMore.setVisibility(View.INVISIBLE);
//                        } else if (position > num_of_column) {
//                            mMusicPlayerViewHolder.imgUpMore.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // add wave loading view
        mMusicPlayerViewHolder.mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mMusicPlayerViewHolder.mWaveLoadingView.setProgressValue(50);
        mMusicPlayerViewHolder.mWaveLoadingView.setBorderWidth(0);
        mMusicPlayerViewHolder.mWaveLoadingView.setAmplitudeRatio(80);
        mMusicPlayerViewHolder.mWaveLoadingView.setWaterLevelRatio(0.2f);
        mMusicPlayerViewHolder.mWaveLoadingView.setThirdWaveColor(getResources().getColor(R.color.colorPrimaryDark));
        mMusicPlayerViewHolder.mWaveLoadingView.setWaveColor(getResources().getColor(R.color.colorAccent));
        mMusicPlayerViewHolder.mWaveLoadingView.setBorderColor(Color.TRANSPARENT);
        mMusicPlayerViewHolder.mWaveLoadingView.setAnimDuration(1700);
        mMusicPlayerViewHolder.mWaveLoadingView.pauseAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView.resumeAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView.cancelAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView.startAnimation();


        mMusicPlayerViewHolder.mWaveLoadingView2.setProgressValue(50);
        mMusicPlayerViewHolder.mWaveLoadingView2.setBorderWidth(0);
        mMusicPlayerViewHolder.mWaveLoadingView2.setAmplitudeRatio(80);
        mMusicPlayerViewHolder.mWaveLoadingView2.setWaterLevelRatio(0.2f);
        mMusicPlayerViewHolder.mWaveLoadingView2.setThirdWaveColor(getResources().getColor(R.color.colorPrimaryDark));
        mMusicPlayerViewHolder.mWaveLoadingView2.setWaveColor(getResources().getColor(R.color.colorAccent));
        mMusicPlayerViewHolder.mWaveLoadingView2.setBorderColor(Color.TRANSPARENT);
        mMusicPlayerViewHolder.mWaveLoadingView2.setAnimDuration(1600);
        mMusicPlayerViewHolder.mWaveLoadingView2.pauseAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView2.resumeAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView2.cancelAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView2.startAnimation();


        mMusicPlayerViewHolder.mWaveLoadingView3.setShapeType(WaveLoadingView3.ShapeType.CIRCLE);
        mMusicPlayerViewHolder.mWaveLoadingView3.setProgressValue(50);
        mMusicPlayerViewHolder.mWaveLoadingView3.setBorderWidth(0);
        mMusicPlayerViewHolder.mWaveLoadingView3.setAmplitudeRatio(80);
        mMusicPlayerViewHolder.mWaveLoadingView3.setWaterLevelRatio(0.2f);
        mMusicPlayerViewHolder.mWaveLoadingView3.setThirdWaveColor(getResources().getColor(R.color.colorPrimaryDark));
        mMusicPlayerViewHolder.mWaveLoadingView3.setWaveColor(getResources().getColor(R.color.colorAccent));
        mMusicPlayerViewHolder.mWaveLoadingView3.setBorderColor(Color.TRANSPARENT);
        mMusicPlayerViewHolder.mWaveLoadingView3.setAnimDuration(1500);
        mMusicPlayerViewHolder.mWaveLoadingView3.pauseAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView3.resumeAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView3.cancelAnimation();
        mMusicPlayerViewHolder.mWaveLoadingView3.startAnimation();


        // Registered monitor shutdown broadcast
        IntentFilter ittfile = new IntentFilter(Intent.ACTION_SHUTDOWN);
        this.registerReceiver(shutdownReceiver, ittfile);
        IntentFilter ittfileStop = new IntentFilter(ACTION_STOP_MUSIC);
        this.registerReceiver(stopMusicReceiver, ittfileStop);
        IntentFilter sourceChange = new IntentFilter(ACTION_CHANGE_SOURCE);
        this.registerReceiver(sourceChangeReceiver, sourceChange);
        stToken = MusicUtils.bindToService(MusicPlayerActivity.this,
                musicServiceConnection);

        View parentView = View.inflate(this, R.layout.music_player_init, null);
        mDolbyLogoView = new DolbyLogoView(this, parentView);
        mInvocationHandler = new onDolbyEventListenerImpl();
        TvApiController.getInstance(this).registerDolbyEventListner(mInvocationHandler);
        if (!Tools.unSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onKeyInfo");
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch arg0:" + arg0 + " arg1:" + " arg2:" + arg2);
                    TvCommonManager.getInstance().setInputSource(arg1);

                    // ComponentName componentName = new ComponentName("mstar.tvsetting.ui",
                    //        "mstar.tvsetting.ui.RootActivity");
                    Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                    // Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    // intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.putExtra("task_tag", "input_source_changed");
                    MusicPlayerActivity.this.startActivity(intent);

                    finish();
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "--------- onDestroy ----------");
        musicList.clear();
        state = 2;
        if (stToken != null) {
            MusicUtils.unbindFromService(stToken);
        }
        // Quit music broadcast service
        //stopMusicService();
//        stopScanFile();
        unregisterReceiver(netDisconnectReceiver);
        unregisterReceiver(shutdownReceiver);
        unregisterReceiver(stopMusicReceiver);
        unregisterReceiver(sourceChangeReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "--------- onStop ----------");
        unregisterReceiver(receiver);
        mDolbyLogoView.hideDolbyLogo();
        TvApiController.getInstance(this).unRegisterDolbyEventListner();
        // Should actual need, first temporarily the background music turn off.
        new Thread(new Runnable() {
            @Override
            public void run() {
                stopMusicService();
                Tools.startMediascanner(MusicPlayerActivity.this);
            }
        }).start();
        musicPlayHandle
                .removeMessages(MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE);
        MusicPlayerActivity.this.finish();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "--------- onResume ---------");
        super.onResume();
        if (!Tools.unSupportTVApi()) {
            Tools.changeSource(true);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        receiver = new DiskChangeReceiver();
        registerReceiver(receiver, filter);
        IntentFilter networkIntentFilter = new IntentFilter(
                "com.mstar.localmm.network.disconnect");

        registerReceiver(netDisconnectReceiver, networkIntentFilter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Tools.stopMediascanner(MusicPlayerActivity.this);
            }
        }).start();
    }

    /*************** Music played processing area ****************************/
    /***
     *
     * Music player Service connection
     */
    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName classname, IBinder obj) {
            mService = IMediaService.Stub.asInterface(obj);
            Log.i(TAG, "ServiceConnection:::onServiceConnected mService:" + mService);
            if (mService != null) {

                try {
                    mService.setMusicStatusListener(musicStatusListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                musicPlayHandle.sendEmptyMessage(HANDLE_MESSAGE_SERVICE_START);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "ServiceConnection:::onServiceDisconnected mService:" + mService);
            mService = null;
        }
    };

    /**
     * pause and play
     */
    protected void pauseMusic() {
        if (musicList.size() > 0) {
            if (currentPlayStatus == PLAY_PLAY) {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                pause();
                // pause animations equalizer/wave
                playPauseAnim(true);
            } else if (currentPlayStatus == PLAY_PAUSE) {
                mMusicPlayerViewHolder.bt_MusicPlay
                        .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                continuing();
                playPauseAnim(false);
            }
        }
    }

    /**
     * previous
     */
    protected void preMusic() {
        state = 2;
        isNextMusic = false;
        processPlayCompletion();
        mMusicPlayerViewHolder.bt_MusicPlay
                .setBackgroundResource(R.drawable.player_icon_pause);

        notifySubList();
    }

    /**
     * next
     */
    protected void nextMusic() {
        state = 2;
        isNextMusic = true;
        processPlayCompletion();
        mMusicPlayerViewHolder.bt_MusicPlay
                .setBackgroundResource(R.drawable.player_icon_pause);

        notifySubList();
    }

    /**
     * Jump to the appointed position play.
     *
     * @param position The current broadcast position.
     */
    protected void seekTo(int position) {
        if (mService != null) {
            try {
                Log.i(TAG, "*******seekTo*********" + position + " " + mService.getDuration());
                if (position < mService.getDuration()) {
                    mService.playerToPosiztion(position);
                } else {
                    isPrepared = false;
                    nextMusic();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    protected void notifySubList() {
        setThumbnail(musicList.get(currentPosition).getPath());

        adapter.notifyDataSetChanged();
    }

    private void setThumbnail(String path) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = createAlbumThumbnail(path);
                if (bm == null) {
                    bm = MusicUtils.getArtwork(getApplicationContext(), musicList.get(currentPosition).getId(), musicList.get(currentPosition).getAlbum(), false);
                    if (bm == null) {
                        bm = MusicUtils.getDefaultArtwork(getApplicationContext());
                    }
                }
                mMusicPlayerViewHolder.imgThumbnail.setImageBitmap(bm);
            }
        });
    }


//    static class ClientProxy implements MediaScannerConnection.MediaScannerConnectionClient {
//        final String[] mPaths;
//        final String[] mMimeTypes;
//        final OnScanCompletedListener mClient;
//        MediaScannerConnection mConnection;
//        int mNextPath;
//
//        ClientProxy(String[] paths, String[] mimeTypes, OnScanCompletedListener client) {
//            mPaths = paths;
//            mMimeTypes = mimeTypes;
//            mClient = client;
//        }
//
//        public void onMediaScannerConnected() {
//            scanNextPath();
//        }
//
//        public void onScanCompleted(String path, Uri uri) {
//            if (mClient != null) {
//                mClient.onScanCompleted(path, uri);
//            }
//            scanNextPath();
//        }
//
//        void scanNextPath() {
//            if (mNextPath >= mPaths.length) {
//                mConnection.disconnect();
//                return;
//            }
//            String mimeType = mMimeTypes != null ? mMimeTypes[mNextPath] : null;
//            mConnection.scanFile(mPaths[mNextPath], mimeType);
//            mNextPath++;
//        }
//    }
//
//
//    public void startScanFile(Context context, String[] paths, String[] mimeTypes,
//                              OnScanCompletedListener callback) {
//        ClientProxy client = new ClientProxy(paths, mimeTypes, callback);
//        stopScanFile();
//        mScanConnection = new MediaScannerConnection(context, client);
//        client.mConnection = mScanConnection;
//        mScanConnection.connect();
//    }
//
//    public void stopScanFile() {
//        if (mScanConnection != null && mScanConnection.isConnected()) {
//            Log.i(TAG, "stop Scan File!");
//            mScanConnection.disconnect();
//        }
//    }

    /**
     * Play music
     */
    private void initPlay() {
        if (musicList.size() == 0) {
            this.finish();
            return;
        }
        if (currentPosition >= 0 && currentPosition < musicList.size()) {
            path = musicList.get(currentPosition).getPath();
            if (path == null) {
                Log.d(TAG, "initPlay: Path null");
                return;
            }
            isVideoSupport = true;
            isAudioSupport = true;
            isPrepared = false;
            try {
                if (mService != null) {
                    mService.initPlayer(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] pathArray = new String[1];
            pathArray[0] = path;
            if (sourceFrom != DLNAConstants.SOURCE_FROM_DLNA) {
//                startScanFile(this, pathArray, null, scanListener);
            }
//            getPicOfAlbum(musicList, currentPosition);
            // Because setSongsContent will be called several times and cause music title be reseted several times
            // And cause the ui flashing several times(Mantis:1308442)
            // So pause here and resume at last time in msg of "HANDLE_MESSAGE_UPDATE_ARTIST"
            pauseOrPlayPresentMusicNameMarquee();
            mMusicPlayerListener.setSongsContent(musicList, currentPosition);
            new Thread(new Runnable() {
                public void run() {
//                    initLrc();
                }
            }).start();
            //initLrc();
            initCurrentPosition();
            currentPlayStatus = PLAY_PLAY;
//            mMusicPlayerListener.setViewDefault();
            // Update in the list of songs focus position
//            mMusicPlayerListener.updateListViewSelection();
        }
    }

    private OnScanCompletedListener scanListener = new OnScanCompletedListener() {

        @Override
        public void onScanCompleted(String arg0, Uri arg1) {
            Log.i(TAG, "***************onScanCompleted**************");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Because queryCurrentMusicExtraInfo takes some time, and will cause ANR,
                    // so run it in Not-UI thread instead of UI thread.
                    queryCurrentMusicExtraInfo();
                    musicPlayHandle.sendEmptyMessage(HANDLE_MESSAGE_UPDATE_ARTIST);
                }
            }).start();
        }
    };

    public String getAudioCodecType() {
        String sRet = "";
        try {
            if (mService != null) {
                sRet = mService.getAudioCodecType();
            }
        } catch (Exception e) {
        }
        return sRet;
    }

    /**
     * pause
     */
    private void pause() {
        MusicPlayerListener.isPlaying = false;
        try {
            if (mService != null)
                mService.pause();
        } catch (Exception e) {
        }
        currentPlayStatus = PLAY_PAUSE;
    }

    /**
     * Continue to play
     */
    private void continuing() {
        MusicPlayerListener.isPlaying = true;
        currentPlayStatus = PLAY_PLAY;
        musicPlayHandle.sendEmptyMessageDelayed(
                MusicPlayerActivity.HANDLE_MESSAGE_SEEKBAR_UPDATE, 100);
        try {
            if (mService != null) {
                mService.continuePlay();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean sendCecKey(int keyCode) {
        if (Tools.unSupportTVApi()) {
            return false;
        }
        try {
            if (mCecManager == null) {
                mCecManager = TvCecManager.getInstance();
            }
            CecSetting setting = mCecManager.getCecConfiguration();
            if (setting.cecStatus == 1) {
                if (mCecManager.sendCecKey(keyCode)) {
                    Log.d(TAG, "send Cec key,keyCode is " + keyCode + ", localmm don't handl the key");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * <p>
     * <p>
     * ******************Key processing area*****************************
     *
     * </P>
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "-----onKeyDown ----- keyCode:" + keyCode);
        if (keyCode == KEYCODE_ASPECT_RATIO ||
                keyCode == KEYCODE_PICTURE_MODE) {
            return true;
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }

        /*
         * Key code constant: Volume Up key. Adjusts the speaker volume up.
         * Key code constant: Volume Down key. Adjusts the speaker volume down.
         * Key code constant: Volume Mute key. Mute the speaker volume.
         */
        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode ||
                KeyEvent.KEYCODE_VOLUME_MUTE == keyCode) {
            if (sendCecKey(keyCode) == true) {
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {

            if (mMusicPlayerViewHolder.horizontalgridview.isSelected()) {
                mMusicPlayerViewHolder.horizontalgridview.setFocusable(false);
                mMusicPlayerViewHolder.bt_MusicPlay.requestFocus();
            } else {
                // Should actual need, first temporarily the background music turn
                // off.
                stopMusicService();
                super.finish();
                System.gc();
            }

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            Log.d(TAG, "onKeyDown: currentPlayStatus : " + currentPlayStatus);
            if (currentPlayStatus == PLAY_PAUSE) {
                if (mMusicPlayerViewHolder.bt_MusicPlay.isFocused()) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_pause_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_pause);
                }
                continuing();
            } else if (currentPlayStatus == PLAY_PLAY) {
                Log.i(TAG, "keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE2");
                if (mMusicPlayerViewHolder.bt_MusicPlay.isFocused()) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }
                pause();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            Log.i(TAG, "keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE");
            if (currentPlayStatus == PLAY_PLAY) {
                Log.i(TAG, "keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE2");
                if (mMusicPlayerViewHolder.bt_MusicPlay.isFocused()) {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.ic_player_icon_play_focus);
                } else {
                    mMusicPlayerViewHolder.bt_MusicPlay
                            .setBackgroundResource(R.drawable.player_icon_play);
                }
                pause();

            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_NEXT) {
            if (isPrepared && sourceFrom != DLNAConstants.SOURCE_FROM_DLNA) {
                isPrepared = false;
                nextMusic();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
            if (isPrepared && sourceFrom != DLNAConstants.SOURCE_FROM_DLNA) {
                isPrepared = false;
                preMusic();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
            if (isPrepared/* && sourceFrom != DLNAConstants.SOURCE_FROM_DLNA*/) {
                rewind();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            if (isPrepared/* && sourceFrom != DLNAConstants.SOURCE_FROM_DLNA*/) {
                wind();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // mMusicPlayerListener.showDialog();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

            if (isPrepared) {
                mMusicPlayerListener.drapLeft();
            } else {
                if (currentSelectedPosition > 0) {
                    mMusicPlayerViewHolder.horizontalgridview.smoothScrollToPosition(--currentSelectedPosition);
                }
            }


            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (!mMusicPlayerViewHolder.horizontalgridview.isFocused() || !mMusicPlayerViewHolder.horizontalgridview.isSelected()) {
                if (isPrepared) {
                    mMusicPlayerListener.drapRight();
                } else {
                    mMusicPlayerViewHolder.horizontalgridview.smoothScrollToPosition(++currentSelectedPosition);
                }
            }

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

            isPrepared = false;
            state_last = state;
            MusicPlayerActivity.state = MusicPlayerListener.OPTION_STATE_SELECT_MUSIC_SUBLIST;
            mMusicPlayerListener.drapTop();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMusicPlayerViewHolder.bt_MusicPlay.setFocusable(false);

                    mMusicPlayerViewHolder.horizontalgridview.requestFocus();
                    mMusicPlayerViewHolder.horizontalgridview.setFocusable(true);
                    mMusicPlayerViewHolder.horizontalgridview.setSelectedPosition(currentPosition);
                }
            });

            return true;
        }


        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

            isPrepared = true;
            isSubListItemClicked = false;

            MusicPlayerActivity.state = MusicPlayerListener.OPTION_STATE_PLAY;
            mMusicPlayerListener.drapDown();

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    isPrepared = true;
//
//                    mMusicPlayerViewHolder.horizontalgridview.setFocusable(false);
//                    mMusicPlayerViewHolder.horizontalgridview.setFocusableInTouchMode(false);
//
//                    mMusicPlayerViewHolder.horizontalgridview.getChildAt(4).animate().scaleX(1f).scaleY(1f).start();
//                }
//            });

            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            mMusicPlayerListener.registerListeners();
            return true;
        }
        return super.

                onKeyDown(keyCode, event);
    }


    private void playPauseAnim(boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView.ViewHolder view = mMusicPlayerViewHolder.horizontalgridview.findViewHolderForAdapterPosition(currentPosition);

                EqualizerView equalizerView = view.itemView.findViewById(R.id.eq);//getViewByPosition(currentPosition).findViewById(R.id.eq);
                if (b) {
                    if (equalizerView.isAnimating()) {
                        equalizerView.stopBars();
                    }
                    mMusicPlayerViewHolder.mWaveLoadingView.cancelAnimation();
                    mMusicPlayerViewHolder.mWaveLoadingView2.cancelAnimation();
                    mMusicPlayerViewHolder.mWaveLoadingView3.cancelAnimation();
                } else {
                    if (!equalizerView.isAnimating()) {
                        equalizerView.animateBars();
                    }
                    mMusicPlayerViewHolder.mWaveLoadingView.startAnimation();
                    mMusicPlayerViewHolder.mWaveLoadingView2.startAnimation();
                    mMusicPlayerViewHolder.mWaveLoadingView3.startAnimation();
                }
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "-----onKeyUp ----- keyCode:" + keyCode);
        if (keyCode == KEYCODE_ASPECT_RATIO ||
                keyCode == KEYCODE_PICTURE_MODE) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_UNKNOWN || keyCode == 0) {
            return true;
        }
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return true;
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /*************** Private method processing area *************/
    /**
     * When playing after abnormal end the current broadcast page
     */
    private void exitMusicPaly() {
        if (mService != null) {
            try {
                mService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        musicPlayHandle.sendEmptyMessageDelayed(HANDLE_MESSAGE_PLAYER_EXIT, 1000);
    }

    private void exceptionProcess(String strMessage) {
        String sName = musicList.get(currentPosition).getPath();
        String showtip = sName + " " + strMessage + ",\n" + getResources().getString(R.string.music_media_play_next);
        if (musicList.size() <= 1) {
            Toast toast = Toast.makeText(MusicPlayerActivity.this,
                    sName + " " + strMessage, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            exitMusicPaly();
        } else {
            showErrorDialog(showtip);
            //processPlayCompletion();
        }
    }

    // Pop up display an error dialog box
    private void showErrorDialog(final String strMessage) {
        // Prevent activity died when the popup menu
        if (!isFinishing()) {
            new AlertDialog.Builder(MusicPlayerActivity.this)
                    .setTitle(getResources().getString(R.string.show_info))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(strMessage)
                    .setPositiveButton(getResources().getString(R.string.exit_ok),
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isErrorDialogShow = false;
                                    if (strMessage.equals(getResources().getString(
                                            R.string.video_media_error_server_died))) {
                                        MusicPlayerActivity.this.finish();
                                    } else {
                                        processPlayCompletion();
                                    }
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.exit_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isErrorDialogShow = false;
                                    MusicPlayerActivity.this.finish();
                                }
                            }).setCancelable(false).show();
            isErrorDialogShow = true;
        }
    }

    /******************* Lyrics handling area **************************/
    /**
     * Initialization lyrics
     */
    private void initLrc() {
        if (mLrcRead != null) {
            mLrcRead.close();
        }
        isLrcShow = false;
        mLrcRead = new LrcRead();
        try {
            String tempPath = musicList.get(currentPosition).getPath();
            int index = musicList.get(currentPosition).getPath()
                    .lastIndexOf(".");
            String lrcPath = tempPath.substring(0, index) + ".lrc";
            // LrcPath judge whether there is
            File file = new File(lrcPath);
            if (file.exists() || Tools.isSambaPlaybackUrl(lrcPath)) {
                mLrcRead.Read(lrcPath);
                isLrcShow = true;
            } else {
                String txtPath = tempPath.substring(0, index) + ".txt";
                file = new File(txtPath);
                if (file.exists()) {
                    mLrcRead.Read(txtPath);
                    isLrcShow = true;
                } else {
                    isLrcShow = false;
                }
            }
        } catch (FileNotFoundException e) {
            isLrcShow = false;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLyricList = mLrcRead.GetLyricContent();
        // Set lyrics resources
        mMusicPlayerViewHolder.mLyricView
                .setSentenceEntities(mLyricList);
        mMusicPlayerViewHolder.mLyricView.invalidate();
    }

    /**
     * Set lyrics position index
     */
    private void setLRCIndex() {
        if (currentTime < countTime) {
            int size = mLyricList.size();
            for (int i = 0; i < size; i++) {
                if (i < size - 1) {
                    if (currentTime < mLyricList.get(i).getLyricTime()
                            && i == 0) {
                        index = i;
                        musicPlayHandle
                                .sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_LRC);
                    } else if (currentTime > mLyricList.get(i).getLyricTime()
                            && currentTime < mLyricList.get(i + 1)
                            .getLyricTime()) {
                        index = i;
                        musicPlayHandle
                                .sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_LRC);
                    }
                } else if (i == size - 1
                        && currentTime > mLyricList.get(i).getLyricTime()) {
                    index = i;
                    musicPlayHandle
                            .sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_LRC);
                }
            }
        }
    }

    /**
     * Initialize the current music serial number
     */
    private void initCurrentPosition() {
        Log.i(TAG, "initCurrentPosition musicList.size():" + musicList.size());
        if (musicList.size() > 0) {
            int duration = musicList.get(currentPosition).getDuration();
            mMusicPlayerViewHolder.durationTime.setText(MusicUtils.durationToString(duration));
            mMusicPlayerViewHolder.seekBar.setMax(duration);
        }
    }

    /**
     * Fast back 10 s
     */
    private void rewind() {
        int currentTime = mMusicPlayerViewHolder.seekBar.getProgress();
        if (currentTime >= 0) {
            currentTime = currentTime - 10000;
            if (currentTime < 0) {
                currentTime = 0;
            }
            if (mService != null) {
                try {
                    mService.playerToPosiztion(currentTime);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Fast forward 10 s
     */
    private void wind() {
        int currentTime = mMusicPlayerViewHolder.seekBar.getProgress();
        if (mService != null) {
            try {
                currentTime = currentTime + 10000;
                if (currentTime < mService.getDuration()) {
                    mService.playerToPosiztion(currentTime);
                } else {
                    isPrepared = false;
                    nextMusic();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /******************* Radio receiving area *******************************/
    private class DiskChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String devicePath = intent.getDataString().substring(7);
            Log.d(TAG, "DiskChangeReceiver: " + action);
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                // Disk remove
                Log.i(TAG, "mMusicListDevicePath:" + mMusicListDevicePath);
                if (mMusicListDevicePath != null && mMusicListDevicePath.contains(devicePath)) {
                    Toast toast = Toast.makeText(MusicPlayerActivity.this,
                            R.string.disk_eject, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    MusicPlayerActivity.this.finish();
                }
            }
        }
    }

    // Network disconnection radio treatment
    private BroadcastReceiver netDisconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sourceFrom == Constants.SOURCE_FROM_SAMBA
                    || sourceFrom == DLNAConstants.SOURCE_FROM_DLNA) {
                Toast toast = Toast.makeText(MusicPlayerActivity.this,
                        R.string.net_disconnect, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                MusicPlayerActivity.this.finish();
            }
        }
    };
    // To receive off the radio exit play interface
    private BroadcastReceiver shutdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // standby button.
            if (action.equals(Intent.ACTION_SHUTDOWN)) {
                // music off
                stopMusicService();
                return;
            }
        }
    };
    private BroadcastReceiver stopMusicReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "stop music:   ");
            // music off
            stopMusicService();
        }
    };
    private BroadcastReceiver sourceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: sourceChangeReceiver");
            stopMusicService();
        }
    };

    /*
     *
     * Stop music broadcast service
     */
    private void stopMusicService() {
        Log.i(TAG, "stopMusicService: ");
        Intent i = new Intent(MusicPlayerActivity.this, MediaService.class);
        stopService(i);
    }

    /**
     * Query extras information of music from ContentProvider.
     */
    /* Currently this class has not been used, so comment out it.
    private class InitDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // query extras information from DB, such as artist
            queryMusicExtraInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    } */

    /**
     * Query extras information of music from DB.
     *
     * @return all music with extra information, such as artist.
     */
    /* Currently this method has not been used, so comment out it.
    private void queryMusicExtraInfo() {
        Cursor cursor = null;
        String where = MediaColumns.DATA + "=?";
        for (BaseData item : musicList) {
            String path = item.getPath();
            try {
                // According to the path query the database, access file
                // information
                cursor = getContentResolver().query(
                        Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[] { Audio.Media._ID, Audio.Media.TITLE,
                                Audio.Media.ARTIST, Audio.Media.ALBUM_ID },
                        where, new String[] { path }, null);
                if (cursor != null && cursor.moveToFirst()) {
                    item.setId(cursor.getInt(cursor
                            .getColumnIndex(Audio.Media._ID)));
                    item.setTitle(cursor.getString(cursor
                            .getColumnIndex(Audio.Media.TITLE)));
                    item.setArtist(cursor.getString(cursor
                            .getColumnIndex(Audio.Media.ARTIST)));
                    item.setAlbum(cursor.getLong(cursor
                            .getColumnIndex(Audio.Media.ALBUM_ID)));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    } */
    public void queryCurrentMusicExtraInfo() {
        if (musicList == null || musicList.size() < 1) {
            Log.i(TAG, "queryCurrentMusicExtraInfo musicList:" + musicList + " musicList.size():" + musicList.size());
            return;
        }
        if (currentPosition < 0 || currentPosition > (musicList.size() - 1)) {
            Log.i(TAG, "queryCurrentMusicExtraInfo currentPosition:" + currentPosition + " musicList.size():" + musicList.size());
            return;
        }
        Cursor cursor = null;
        String where = MediaColumns.DATA + "=?";
        BaseData item = musicList.get(currentPosition);
        String path = item.getPath();
        if (Tools.isSambaPlaybackUrl(path)) {
            path = Tools.convertToHttpUrl(item.getPath());
        }
        Log.i(TAG, "*****queryCurrentMusicExtraInfo*****" + path);
        try {
            // According to the path query the database, access file
            // information
            cursor = getContentResolver().query(
                    Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{Audio.Media._ID, Audio.Media.TITLE,
                            Audio.Media.ARTIST, Audio.Media.ALBUM_ID, Audio.Media.YEAR,
                            Audio.Media.TRACK},
                    where, new String[]{path}, null);
            if (cursor != null && cursor.moveToFirst()) {
                item.setId(cursor.getInt(cursor.getColumnIndex(Audio.Media._ID)));
                item.setTitle(cursor.getString(cursor.getColumnIndex(Audio.Media.TITLE)));
                item.setArtist(cursor.getString(cursor.getColumnIndex(Audio.Media.ARTIST)));
                item.setAlbum(cursor.getLong(cursor.getColumnIndex(Audio.Media.ALBUM_ID)));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Obtain album pictures
     */
    private void getPicOfAlbum(final List<BaseData> musicList, final int currentPosition) {
        if (musicList == null || musicList.size() < 1) {
            Log.i(TAG, "getPicOfAlbum musicList:" + musicList + " musicList.size():" + musicList.size());
            return;
        }
        final long songid = (long) musicList.get(currentPosition).getId();
        final long albumid = musicList.get(currentPosition).getAlbum();
        Log.i(TAG, "getPicOfAlbum songid:" + songid + " albumid:" + albumid);

        // Because getPicOfAlbum takes some time, and will cause ANR,
        // so run it in Not-UI thread instead of UI thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Don't allow default artwork here, because we want to fall back to song-specific
                // album art if we can't find anything for the album.
//                Bitmap bm = MusicUtils.getArtwork(MusicPlayerActivity.this, songid, albumid, false);
//                Log.i(TAG, "getPicOfAlbum getArtwork bm:" + bm);
                Bitmap bm = createAlbumThumbnail(musicList.get(currentPosition).getPath());
                Log.i(TAG, "createAlbumThumbnail bm:" + bm);
                if (bm == null) {
                    bm = MusicUtils.getArtwork(MusicPlayerActivity.this, songid, albumid, false);
                    Log.i(TAG, "getPicOfAlbum getArtwork bm:" + bm);
                    if (bm == null) {
                        // Use parameter -1 will get local folder's picture as album.
                        // bm = MusicUtils.getArtwork(MusicPlayerActivity.this, songid, -1);
                        bm = MusicUtils.getDefaultArtwork(MusicPlayerActivity.this);
                    }
                }

                if (bm != null) {
                    Message msg = musicPlayHandle.obtainMessage(HANDLE_MESSAGE_IMAGE_ALBUM_UPDATE, bm);
                    musicPlayHandle.removeMessages(HANDLE_MESSAGE_IMAGE_ALBUM_UPDATE);
                    musicPlayHandle.sendMessage(msg);
                }
            }
        }).start();
    }

    private Bitmap createAlbumThumbnail(String filePath) {
        Log.i(TAG, "-------- createAlbumThumbnail ---------   filePath:" + filePath);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            byte[] art = retriever.getEmbeddedPicture();
            Log.i(TAG, "art:" + art);
            if (art != null) {
                Log.i(TAG, "art.length:" + art.length);
            }
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, "IllegalArgumentException");
        } catch (RuntimeException ex) {
            Log.i(TAG, "RuntimeException");
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }

    public void pauseOrPlayPresentMusicNameMarquee() {
        if (mMusicPlayerViewHolder == null || mMusicPlayerViewHolder.currentMusicTitle == null) {
            return;
        }
        if (mMusicPlayerViewHolder.currentMusicTitle.getEllipsize() != null) {
            mMusicPlayerViewHolder.currentMusicTitle.setEllipsize(null);
        } else {
            mMusicPlayerViewHolder.currentMusicTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
    }


    public class onDolbyEventListenerImpl implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            int what = Integer.parseInt(String.valueOf(args[0]));
            Log.i(TAG, "onEvent,what = " + what);

            try {

                Class clz = Class.forName("com.mstar.android.tv.TvAudioManager");
                Field dolby = clz.getDeclaredField("TVAUDIO_DOLBY_ATMOS_INFO");
                if (dolby != null) {
                    if (what == dolby.getInt(null)) {
                        Log.d(TAG, "send show atom dolby logo");
                        Message message = musicPlayHandle.obtainMessage(HANDLE_MESSAGE_SHOW_DOLBY_LOGO);
                        message.arg1 = Constants.DOLBY_ATMOS;
                        musicPlayHandle.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
