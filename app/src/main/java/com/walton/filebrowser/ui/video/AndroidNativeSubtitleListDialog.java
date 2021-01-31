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

import java.util.List;


import android.R.color;
import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;


import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.ListDataListAdapter;
import com.walton.filebrowser.business.adapter.SubtitleSettingListAdapter;
import com.walton.filebrowser.business.video.SubtitleTool;
import com.walton.filebrowser.util.Constants;

import android.media.MediaPlayer.TrackInfo;
/**
 * Subtitle playlist Dialog
 *
 * @author
 */
public class AndroidNativeSubtitleListDialog extends Dialog {
    private static final String TAG = "AndroidNativeSubtitleListDialog";
    // subtitle list
    private ListView subtitleSettingList;
    private Context context;
    // The selected subtitle
    private int mSubtitleSelectedItem = 0;
    private String videoPath;
    private PlaySettingSubtitleDialog playSettingSubtitleDialog;
    private List<String> listSubtitle = null;

    private ListDataListAdapter adapter;
    // Subtitles view
    private BorderTextViews subtitleTextView;
    private SubtitleTool subtitleTool;
    private String path = "";
    private boolean getInfoSuccess = false;
    private int getInfoNum;
    private int viewId = 1;

    public AndroidNativeSubtitleListDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AndroidNativeSubtitleListDialog(Context context, int theme, String videoPath,
            PlaySettingSubtitleDialog playSettingSubtitleDialog,
            SubtitleSettingListAdapter subAdapter) {
        super(context, theme);
        this.context = context;
        this.videoPath = videoPath;
        this.playSettingSubtitleDialog = playSettingSubtitleDialog;
        // this.subAdapter = subAdapter;
        subtitleTextView = ((VideoPlayerActivity) context).getVideoPlayHolder().getSubtitleTextView();
        viewId = ((VideoPlayerActivity) context).getVideoPlayHolder().getViewId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.subtitle_video_play_list);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.4);
        int height = (int) (display.getHeight() * 0.7);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        w.setBackgroundDrawableResource(color.transparent);
        findView();
        subtitleSettingList.setOnKeyListener(onkeyListenter);
        subtitleSettingList.setClickable(true);
        addListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshSubtitleDialog();
    }

    /**
     * Initialization module
     */
    private void findView() {
        subtitleSettingList = (ListView) findViewById(R.id.subtitle_list);
        subtitleSettingList.requestFocus();
        subtitleSettingList.setEnabled(true);
        subtitleSettingList.setFocusable(true);
        subtitleSettingList.setFocusableInTouchMode(true);
        getVideoPathName();
        subtitleSettingList.setDividerHeight(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "The mouse to click the::::::::;");
        refreshSubtitleDialog();
        return super.onTouchEvent(event);
    }

    /**
     * Registering listeners
     */
    private void addListener() {
        subtitleSettingList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.e(TAG, "Subitem monitoring setOnItemClickListener " + position);
                if (position >= 0) {
                    mSubtitleSelectedItem = position;
                    refreshSubtitleDialog();
                }
            }
        });
        subtitleSettingList.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        Log.e(TAG, "---subtitleSettingList onItemSelected position:" + position);
                        mSubtitleSelectedItem = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER: {
                            Log.e(TAG, "KEYCODE_ENTER :mSubtitleSelectedItem:" + mSubtitleSelectedItem);
                            refreshSubtitleDialog();
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };

    private void closePreviousSubTrack () {
        Log.i(TAG,"closePreviousSubTrack");
        /*
        if (Build.VERSION.SDK_INT >= 21) {
            Log.i(TAG,"Build.VERSION.SDK_INT >= 21");
            int selectTrackText = ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                                        .getSelectedTrack(Constants.MEDIA_TRACK_TYPE_TIMEDTEXT);
            int selectTrackBit = ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                                         .getSelectedTrack(Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP);
            Log.i(TAG,"selectTrackText:"+String.valueOf(selectTrackText));
            Log.i(TAG,"selectTrackBit:"+String.valueOf(selectTrackBit));
            if (selectTrackText >0) {
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                        .deselectTrack(selectTrackText);
            } else if (selectTrackBit >0) {
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                        .deselectTrack(selectTrackBit);
            }
        } else {*/
            if ( SubtitleManager.mSelectedSubTrackLast>0 ) {
                Log.i(TAG,"mSelectedSubTrackLast:"+String.valueOf(SubtitleManager.mSelectedSubTrackLast));
                try {
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                          .deselectTrack(SubtitleManager.mSelectedSubTrackLast);
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
        //}

    }

    /**
     * Refresh subtitle Settings interface data
     */
    private void refreshSubtitleDialog() {
         closePreviousSubTrack();
        // int position = subtitleSettingList.getSelectedItemPosition();
        String subtitleLanguageType = context.getResources().getString(
                R.string.subtitle_3_value_1);
        if (listSubtitle != null && listSubtitle.size() > mSubtitleSelectedItem
                && mSubtitleSelectedItem >= 0) {
            // For a specific subtitles subtitleTrackInfo object
            path = listSubtitle.get(mSubtitleSelectedItem);
            ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().addTimedTextSource(path,"");
            if (path.endsWith("idx") || path.endsWith("sup")) {
                playSettingSubtitleDialog.setExternalSubType(true);
                subtitleTextView.setText("");
            } else {
                playSettingSubtitleDialog.setExternalSubType(false);
            }
            SubtitleManager.setSubtitleLanguageType(null, viewId);
            TrackInfo []trackInfo = ((VideoPlayerActivity) context).getVideoPlayHolder()
                     .getPlayerView().getTrackInfo();
            if (trackInfo != null) {
                getInfoSuccess = false;
                int length = trackInfo.length;

                int innerSubBase = SubtitleManager.mVideoSubtitleNo+SubtitleManager.mInnerSubtitleBase;
                Log.i(TAG,"mVideoSubtitleNo:"+String.valueOf(SubtitleManager.mVideoSubtitleNo));
                Log.i(TAG,"mInnerSubtitleBase:"+String.valueOf(SubtitleManager.mInnerSubtitleBase));
                int videoAudioInnerSubBase = innerSubBase;
                if (length > innerSubBase) {
                    String subtitleLanguageTypes[] = new String[length];
                    for (int i=innerSubBase ;i<length ; i++) {
                        int type = trackInfo[i].getTrackType();
                        Log.i(TAG,"extsub track type:"+String.valueOf(type));
                        if (type == Constants.MEDIA_TRACK_TYPE_TIMEDTEXT
                            || type == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP) {
                            if (SubtitleManager.mExtSubtitleBase == 0 )
                                SubtitleManager.mExtSubtitleBase = i;
                            subtitleLanguageTypes[i] = trackInfo[i].getLanguage();
                            if (!getInfoSuccess && i > (videoAudioInnerSubBase-1+SubtitleManager.mExtSubtitleNo)) {
                                ((VideoPlayerActivity) context).getVideoPlayHolder()
                                    .getPlayerView().selectTrack(i);
                                SubtitleManager.mSelectedSubTrackLast = i;
                                SubtitleManager.mExtSubtitleNo = length - videoAudioInnerSubBase;
                                Log.i(TAG,"selectTrackIndex:"+String.valueOf(i));
                                SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageTypes[i], viewId);
                                getInfoSuccess = true;
                            } else {
                                Log.i(TAG,"selectTrackIndex:"+String.valueOf(i));
                                Log.i(TAG,"videoAudioInnerSubBase:"+String.valueOf(videoAudioInnerSubBase));
                                Log.i(TAG,"mExtSubtitleNo:"+String.valueOf(SubtitleManager.mExtSubtitleNo));
                                Log.i(TAG,"total:"+String.valueOf((videoAudioInnerSubBase-1+SubtitleManager.mExtSubtitleNo)));
                            }
                        } else {
                            videoAudioInnerSubBase++;
                        }
                    }
                    SubtitleManager.setSubtitleLanguageType(subtitleLanguageTypes, viewId);
                }
                if (!getInfoSuccess) {
                    getInfoNum = 0;
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    dismiss();
                    return;
                }
           }
            SubtitleManager.setSubtitleSettingOpt(1, listSubtitle.get(mSubtitleSelectedItem), viewId);
            // subAdapter.notifyDataSetChanged();
        }
        playSettingSubtitleDialog.show();
        dismiss();
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            String subtitleLanguageType = context.getResources().getString(
                    R.string.subtitle_3_value_1);
            SubtitleManager.setSubtitleLanguageType(null, viewId);
            TrackInfo []trackInfo = ((VideoPlayerActivity) context).getVideoPlayHolder()
                   .getPlayerView().getTrackInfo();

            if (trackInfo != null) {
                getInfoSuccess = false;
                int length = trackInfo.length;

                int innerSubBase = SubtitleManager.mVideoSubtitleNo+SubtitleManager.mInnerSubtitleBase;
                Log.i(TAG,"mVideoSubtitleNo:"+String.valueOf(SubtitleManager.mVideoSubtitleNo));
                Log.i(TAG,"mInnerSubtitleBase:"+String.valueOf(SubtitleManager.mInnerSubtitleBase));
                int videoAudioInnerSubBase = innerSubBase;
                if (length > innerSubBase) {
                    String subtitleLanguageTypes[] = new String[length];
                    for (int i=innerSubBase ;i<length ; i++) {
                        int type = trackInfo[i].getTrackType();
                        Log.i(TAG,"ext track type:"+String.valueOf(type));
                        if (type == Constants.MEDIA_TRACK_TYPE_TIMEDTEXT
                            || type == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP) {
                            if (SubtitleManager.mExtSubtitleBase == 0 )
                                SubtitleManager.mExtSubtitleBase = i;
                            subtitleLanguageTypes[i] = trackInfo[i].getLanguage();
                            if (!getInfoSuccess && i > (videoAudioInnerSubBase-1+SubtitleManager.mExtSubtitleNo)) {
                                ((VideoPlayerActivity) context).getVideoPlayHolder()
                                    .getPlayerView().selectTrack(i);
                                SubtitleManager.mSelectedSubTrackLast = i;
                                SubtitleManager.mExtSubtitleNo = length - videoAudioInnerSubBase;
                                Log.i(TAG,"selectTrackIndex:"+String.valueOf(i));
                                SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageTypes[i], viewId);
                                getInfoSuccess = true;
                            }
                        } else {
                            videoAudioInnerSubBase++;
                        }
                    }
                    SubtitleManager.setSubtitleLanguageType(subtitleLanguageTypes, viewId);
               }
               if (!getInfoSuccess) {
                    getInfoNum++;
                    if (getInfoNum <= 2) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        return;
                    }
               }
            }
            SubtitleManager.setSubtitleSettingOpt(1, listSubtitle.get(mSubtitleSelectedItem), viewId);
            // subAdapter.notifyDataSetChanged();
            playSettingSubtitleDialog.show();
        }
    };
    /**
     * For video subtitles data
     */
    private void getVideoPathName() {
        Log.i(TAG, "getVideoPathName videoPath:" + videoPath);
        subtitleTool = new SubtitleTool(videoPath);
        listSubtitle = subtitleTool.getSubtitlePathList(SubtitleTool.SUBTITLE_FORMATE_NULL);
        Log.i(TAG, "listSubtitle:" + listSubtitle + " listSubtitle.size:" + listSubtitle.size());
        String listData[] = new String[listSubtitle.size()];
        for (int i = 0; i < listSubtitle.size(); i++) {
            listData[i] = listSubtitle.get(i);
        }
        adapter = new ListDataListAdapter(context, listData);
        subtitleSettingList.setAdapter(adapter);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
