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

import android.app.Dialog;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.SubtitleSettingListAdapter;
import com.walton.filebrowser.business.video.SubtitleTool;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.media.SubtitleTrackInfo;

import android.media.MediaPlayer.TrackInfo;

/**
 * Subtitle Settings interface
 *
 * @author
 * @since 1.0
 * @date 2012-2-16
 */
public class PlaySettingSubtitleDialog extends Dialog {
    private final static String TAG = "PlaySettingSubtitleDialog";
    private VideoPlayerActivity context;
    private ListView mPlaySettingSubtitleListView;
    private SubtitleSettingListAdapter adapter;
    // private String videoPath;
    //private VideoPlayView mVideoPlayView;
    // Default is external subtitles
    private static boolean subtitleFlag = true;
    // The film's inscribed title number
    private int mInternalSubtitleCount = 0;
    // External image contains the number of subtitles
    private int mImageSubtitleCount = 0;
    // Store the current film chosen subtitles position
    public  static int subtitlePosition = 0;
    // Store the current film chosen subtitles language serial number
    private int subtitleLanguageIndex = 0;
    // Subtitle time axis identification
    private int subtitleTime;
    // Subtitles view
    private BorderTextViews subtitleTextView;

    private SubtitleTrackInfo subtitleTrackInfo;
    private VideoPlaySettingDialog videoPlaySettingDialog;
    private boolean isInnerSubtitle = true;
    private int getInfoNum;
    private int viewId = 1;
    private int subCodeType[];
    private boolean isImageSub = false;

    public SubtitleSettingListAdapter getAdapter() {
        return adapter;
    }

    public PlaySettingSubtitleDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
        viewId = ((VideoPlayerActivity) context).getVideoPlayHolder().getViewId();
    }

    public PlaySettingSubtitleDialog(VideoPlayerActivity context, int theme,
            String videoPath, VideoPlaySettingDialog videoPlaySettingDialog,
            boolean isInner) {
        super(context, theme);
        this.isInnerSubtitle = isInner;
        this.context = context;
        // this.videoPath = videoPath;
        this.videoPlaySettingDialog = videoPlaySettingDialog;
        subtitleFlag = isInner;
        subtitleTextView = ((VideoPlayerActivity) context).getVideoPlayHolder().getSubtitleTextView();
        viewId = ((VideoPlayerActivity) context).getVideoPlayHolder().getViewId();

        if (isInnerSubtitle) {
            mHandler.sendEmptyMessage(0);
            SubtitleManager.setSubtitleSettingOpt(0, this.context.getString(R.string.subtitle_0_value_2), viewId);
        } else {
            SubtitleManager.setSubtitleSettingOpt(0, this.context.getString(R.string.subtitle_0_value_1), viewId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playsetting_subtitle);
        mInternalSubtitleCount = SubtitleManager.mVideoSubtitleNo;
        // instantiation new window
        Window w = getWindow();
        // for the default display data
        Display display = w.getWindowManager().getDefaultDisplay();
        mPlaySettingSubtitleListView = (ListView) this.findViewById(R.id.playsetting_subtitle_list);
        adapter = new SubtitleSettingListAdapter(context,
                Constants.subTitleSettingName,
                SubtitleManager.initSubtitleSettingOpt(context, viewId), isInnerSubtitle, PlaySettingSubtitleDialog.this);
        mPlaySettingSubtitleListView.setDivider(null);
        mPlaySettingSubtitleListView.setAdapter(adapter);
        setListeners();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.43);
        int height = (int) (display.getHeight() * 0.75);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        // android native  api
        if (Tools.getSubtitleAPI()) {
            initAnNativeSubCodeType();
        // mstar api
        } else {
            initSubCodeType();
        }
    }
    public void initAnNativeSubCodeType() {
        TrackInfo []trackInfo = null;
        if (((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()) {
            trackInfo = ((VideoPlayerActivity) context).getVideoPlayHolder()
                            .getPlayerView().getTrackInfo();
        }
        if (trackInfo != null) {
             subCodeType = new int[trackInfo.length];
             for (int i=0 ;i<trackInfo.length ; i++) {
                  int type = trackInfo[i].getTrackType();
                  subCodeType[i] = type;
            }

            if (isInnerSubtitle && SubtitleManager.mVideoSubtitleNo > 0) {
                int base = SubtitleManager.mInnerSubtitleBase;
                if (subCodeType[base] == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP)
                    isImageSub = true;
            }
        } else {
             subCodeType = new int[1];
             subCodeType[0] = -1;
             if (isInnerSubtitle) {
                    //isImageSub = true;no inner subtitle
             }
        }
    }
    public void initSubCodeType() {
         SubtitleTrackInfo info = null ;
         if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
             info = SubtitleManager.getInstance().getAllSubtitleTrackInfo(
                     ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer());
         }
        if(null != info){
            int totalInfoNum = info.getAllSubtitleCount();
            if (totalInfoNum>0) {
                subCodeType = new int[totalInfoNum];
                info.getSubtitleCodeType(subCodeType);
                if (subCodeType[subtitlePosition] == 5) {
                    isImageSub = true;
                }
            } else {
                subCodeType = new int[1];
                subCodeType[0] = -1;
                if (isInnerSubtitle) {
                    //isImageSub = true;no inner subtitle
                }
            }
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoPlaySettingDialog != null) {
            videoPlaySettingDialog.show();
        }
    }

    public void handleImageClick(int position, boolean bLeftImageClick) {
        Log.i(TAG, "handleImageClick ---position:" + position + " bLeftImageClick:" + bLeftImageClick +" isInnerSubtitle:" + isInnerSubtitle + " isImageSub:" + isImageSub);
        switch (position) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                if (isExternalSubtitleSettingPage(viewId)) {
                    if (!hasSetExternalSubtitlePath(viewId)) {
                        showToast(context.getString(R.string.external_subtitle_setting_operation_invalid));
                        return;
                    }
                }
                if(isInnerSubtitle && mInternalSubtitleCount<=0){
                    showToast(context.getString(R.string.inner_subtitle_setting_opration_invalid));
                    return;
                }
                break;
        }
        switch (position) {
            case 0:
                // changeSubtitleType();
                break;
            case 1:
                if (isInnerSubtitle) {
                    // android native  api
                    if (Tools.getSubtitleAPI()) {
                        changeAnNativeInnerSubtitleLanguage(subtitlePosition, bLeftImageClick);
                    // mstar api
                    } else {
                        changeInnerSubtitleLanguage(subtitlePosition, bLeftImageClick);
                    }
                }
                break;
            case 2:// Subtitles language
                if (!isInnerSubtitle) {
                    // android native  api
                    if (Tools.getSubtitleAPI()) {
                        changeAnNativeExtSubtitleLanguage(bLeftImageClick);
                    // mstar api
                    } else {
                        changeExtSubtitleLanguage(bLeftImageClick);
                    }

                }
                break;
            case 3:// Subtitles size
                if (!isImageSub) {
                    changeSubtitleFont(bLeftImageClick);
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            case 4:// Subtitles font size
                if (!isImageSub) {
                    changeSubtitleFontSize(bLeftImageClick);
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            case 5:// Subtitles color
                if (!isImageSub) {
                    changeSubtitleColor(bLeftImageClick);
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            case 6:// Subtitles clip
                if (!isImageSub) {
                    changeSubtitleClip();
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            case 7:// Subtitles position
                if (!isImageSub) {
                    changeSubtitlePostion();
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            case 8:// Subtitle time axis adjustment
                if (!isImageSub) {
                    changeTimeAdjust(subtitleTime, bLeftImageClick);
                } else {
                    showToast(context.getString(R.string.subtitle_change_invalid));
                }
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void handleMidClick(int position) {
        Log.i(TAG, "---- handleMidClick --- position:" + position);
        switch (position) {
            case 0:
                // changeSubtitleType();
                break;
            case 1:
                if (isInnerSubtitle) {
                    // changeSubtitleOrder(subtitlePosition);
                    //changeSubtitleLanguage(subtitlePosition, false);
                    onBackPressed();
                } else {
                    PlaySettingSubtitleDialog.this.dismiss();
                    context.getVideoHandler().sendEmptyMessage(Constants.SHOW_SUBTITLE_LIST_DIALOG);
                }
                break;
            /*
            case 2:
                if (!isInnerSubtitle) {
                    changeSubtitleLanguage(false);
                }
                break;
            case 3:
                if (!isImageSub) {
                    changeSubtitleFont(false);
                }
                break;
            case 4:// Subtitles font size
                if (!isImageSub) {
                    changeSubtitleFontSize(false);
                }
                break;
            case 5:
                if (!isImageSub) {
                    changeSubtitleColor(false);
                }
                break;
            case 6:
                if (!isImageSub) {
                    changeSubtitleClip();
                }
                break;
            case 7:
                if (!isImageSub) {
                    changeSubtitlePostion();
                }
                break;
            case 8:
                if (!isImageSub) {
                    changeTimeOrder(subtitleTime);
                }
                // changeTimeAdjust(subtitleTime, false);
                break;
            */
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void setExternalSubType(boolean bImage) {
        isImageSub = bImage;
    }

    public void setInternalSubCount(int cnt) {
        mInternalSubtitleCount = cnt;
    }

    private void setListeners() {
        mPlaySettingSubtitleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                handleMidClick(position);
            }
        });
        mPlaySettingSubtitleListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i(TAG, "onKey event.getAction():" + event.getAction() + " keyCode:" + keyCode);
            if (KeyEvent.ACTION_DOWN  == event.getAction()) {
                if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode || KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                    int position = mPlaySettingSubtitleListView.getSelectedItemPosition();
                    switch (position) {
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if (isExternalSubtitleSettingPage(viewId)) {
                                if (!hasSetExternalSubtitlePath(viewId)) {
                                    showToast(context.getString(R.string.external_subtitle_setting_operation_invalid));
                                    return false;
                                }
                            }
                            if(isInnerSubtitle && mInternalSubtitleCount<=0){
                                showToast(context.getString(R.string.inner_subtitle_setting_opration_invalid));
                                return false;
                            }
                            break;
                    }
                }
            }

            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    int position = mPlaySettingSubtitleListView
                            .getSelectedItemPosition();
                    switch (position) {
                    case 0:
                        // changeSubtitleType();
                        break;
                    case 1:
                        if (isInnerSubtitle) {
                             // android native  api
                            if (Tools.getSubtitleAPI()) {
                                changeAnNativeInnerSubtitleLanguage(subtitlePosition, true);
                            // mstar api
                            } else {
                                changeInnerSubtitleLanguage(subtitlePosition, true);
                            }
                        }
                        break;
                    case 2:// Subtitles language
                        if (!isInnerSubtitle) {
                            // android native  api
                            if (Tools.getSubtitleAPI()) {
                                changeAnNativeExtSubtitleLanguage(true);
                            // mstar api
                            } else {
                                changeExtSubtitleLanguage(true);
                            }
                        }
                        break;
                    case 3:// Subtitles size
                        if (!isImageSub) {
                            changeSubtitleFont(true);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 4:// Subtitles font size
                        if (!isImageSub) {
                            changeSubtitleFontSize(true);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 5:// Subtitles color
                        if (!isImageSub) {
                            changeSubtitleColor(true);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 6:// Subtitles clip
                        if (!isImageSub) {
                            changeSubtitleClip();
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 7:// Subtitles position
                        if (!isImageSub) {
                            changeSubtitlePostion();
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 8:// Subtitle time axis adjustment
                        if (!isImageSub) {
                            changeTimeAdjust(subtitleTime, true);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    default:
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    int position = mPlaySettingSubtitleListView
                            .getSelectedItemPosition();
                    switch (position) {
                    case 0:
                        // changeSubtitleType();
                        break;
                    case 1:
                        if (isInnerSubtitle) {
                             // android native  api
                            if (Tools.getSubtitleAPI()) {
                                changeAnNativeInnerSubtitleLanguage(subtitlePosition, false);
                            // mstar api
                            } else {
                                changeInnerSubtitleLanguage(subtitlePosition, false);
                            }
                        }
                        break;
                    case 2:
                        if (!isInnerSubtitle) {
                            // android native  api
                            if (Tools.getSubtitleAPI()) {
                                changeAnNativeExtSubtitleLanguage(false);
                            // mstar api
                            } else {
                                changeExtSubtitleLanguage(false);
                            }
                        }
                        break;
                    case 3:
                        if (!isImageSub) {
                            changeSubtitleFont(false);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 4:
                        if (!isImageSub) {
                            changeSubtitleFontSize(false);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 5:
                        if (!isImageSub) {
                            changeSubtitleColor(false);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 6:
                        if (!isImageSub) {
                            changeSubtitleClip();
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 7:
                        if (!isImageSub) {
                            changeSubtitlePostion();
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    case 8:
                        if (!isImageSub) {
                            changeTimeAdjust(subtitleTime, false);
                        } else {
                            showToast(context.getString(R.string.subtitle_change_invalid));
                        }
                        break;
                    default:
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
                }
            }
            }
            return false;
        }
    };

    private void showToast(String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.show();
    }

    private void changeSubtitlePostion() {
        String opt = SubtitleManager.getSubtitleSettingOptValue(8, viewId);
        if (context.getString(R.string.subtitle_8_value_2).equals(opt)) {
            SubtitleManager.setSubtitleSettingOpt(8,
                    context.getString(R.string.subtitle_8_value_1), viewId);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL);
            params.setMargins(0,80,0,80); // left , top ,right, bottom
            subtitleTextView.setLayoutParams(params);
        } else {
            SubtitleManager.setSubtitleSettingOpt(8,
                    context.getString(R.string.subtitle_8_value_2), viewId);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP
                            | Gravity.CENTER_HORIZONTAL);
            params.setMargins(0,80,0,80); // left , top ,right, bottom
            subtitleTextView.setLayoutParams(params);
        }
    }

    /**
     * Font border change
     */
    private void changeSubtitleClip() {
        String opt = SubtitleManager.getSubtitleSettingOptValue(7, viewId);
        String yes = context.getString(R.string.subtitle_7_value_2);
        if (yes.equals(opt)) {
            SubtitleManager.setSubtitleSettingOpt(7,
                    context.getString(R.string.subtitle_7_value_1), viewId);
            subtitleTextView.setPaintColor(Color.TRANSPARENT);
        } else {
            SubtitleManager.setSubtitleSettingOpt(7,
                    context.getString(R.string.subtitle_7_value_2), viewId);
            subtitleTextView.setPaintColor(Color.RED);
        }
        subtitleTextView.invalidate();
    }

    /**
     * Subtitle font changes
     */
    private void changeSubtitleFont(boolean leftOrRight) {
        // Left: song, coarse, oblique, song
        Typeface font = Typeface.DEFAULT;
        String opt = SubtitleManager.getSubtitleSettingOptValue(4, viewId);
        if (leftOrRight) {
            if (context.getString(R.string.subtitle_4_value_1).equals(opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_2), viewId);
                font = Typeface.SANS_SERIF;
            } else if (context.getString(R.string.subtitle_4_value_2).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_3), viewId);
                font = Typeface.MONOSPACE;
            } else if (context.getString(R.string.subtitle_4_value_3).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_1), viewId);
                font = Typeface.DEFAULT;
            }
        } else {
            if (context.getString(R.string.subtitle_4_value_1).equals(opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_3), viewId);
                font = Typeface.MONOSPACE;
            } else if (context.getString(R.string.subtitle_4_value_2).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_1), viewId);
                font = Typeface.DEFAULT;
            } else if (context.getString(R.string.subtitle_4_value_3).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(4,
                        context.getString(R.string.subtitle_4_value_2), viewId);
                font = Typeface.SANS_SERIF;
            }
        }
        subtitleTextView.setTypeface(font);
    }

    /**
     * Subtitle font changes
     */
    private void changeSubtitleFontSize(boolean leftOrRight) {
        // left: large, medium (35 sp), small, big
        float size = 0;
        String opt = SubtitleManager.getSubtitleSettingOptValue(5, viewId);

        if (leftOrRight) {
            if (context.getString(R.string.subtitle_5_value_1).equals(opt)) {
                size = 35.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_2), viewId);
            } else if (context.getString(R.string.subtitle_5_value_2).equals(
                    opt)) {
                size = 25.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_3), viewId);
            } else if (context.getString(R.string.subtitle_5_value_3).equals(
                    opt)) {
                size = 45.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_1), viewId);
            }
        } else {
            if (context.getString(R.string.subtitle_5_value_1).equals(opt)) {
                size = 25.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_3), viewId);
            } else if (context.getString(R.string.subtitle_5_value_3).equals(
                    opt)) {
                size = 35.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_2), viewId);
            } else if (context.getString(R.string.subtitle_5_value_2).equals(
                    opt)) {
                size = 45.0f;
                SubtitleManager.setSubtitleSettingOpt(5,
                        context.getString(R.string.subtitle_5_value_1), viewId);
            }
        }
        if(((VideoPlayerActivity) context).getVideoPlayHolder().isPIPMode(
                 viewId)){
            if(size == 25.0f){
                size = SubtitleTool.PIP_SUBTITLE_SIZE_SMALL;
            }else if(size == 35.0f){
                size = SubtitleTool.PIP_SUBTITLE_SIZE_MEDIUM;
            }else{
                size = SubtitleTool.PIP_SUBTITLE_SIZE_MAJOR;
            }
            SubtitleTool.setCurrentPIPSubSize(size);
        }

        boolean getDualVideoMode = ((VideoPlayerActivity) context).getVideoPlayHolder().getDualVideoMode();
        if (getDualVideoMode) {
            zoomSubtitleFontSize(size);
        } else {
            subtitleTextView.setTextSize(size);
        }
    }

    /**
     * Subtitle color change
     */
    private void changeSubtitleColor(boolean leftOrRight) {
        String opt = SubtitleManager.getSubtitleSettingOptValue(6, viewId);
        // White, black, blue, white
        if (leftOrRight) {
            if (context.getString(R.string.subtitle_6_value_1).equals(opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_2), viewId);
                subtitleTextView.setTextColor(Color.BLACK);
            } else if (context.getString(R.string.subtitle_6_value_2).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_3), viewId);
                subtitleTextView.setTextColor(Color.BLUE);
            } else if (context.getString(R.string.subtitle_6_value_3).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_1), viewId);
                subtitleTextView.setTextColor(Color.WHITE);
            }
        } else {
            if (context.getString(R.string.subtitle_6_value_1).equals(opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_3), viewId);
                subtitleTextView.setTextColor(Color.BLUE);
            } else if (context.getString(R.string.subtitle_6_value_3).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_2), viewId);
                subtitleTextView.setTextColor(Color.BLACK);
            } else if (context.getString(R.string.subtitle_6_value_2).equals(
                    opt)) {
                SubtitleManager.setSubtitleSettingOpt(6,
                        context.getString(R.string.subtitle_6_value_1), viewId);
                subtitleTextView.setTextColor(Color.WHITE);
            }
        }
    }

    // Change the subtitles time axis
    private void changeTimeAdjust(int postion, boolean flag) {
        if (flag) {// Default is left
            if (postion <= -9900) {
                postion = -9900;
            }
            SubtitleManager.setSubtitleSettingOpt(9, "" + (postion - 100), viewId);
            subtitleTime = postion - 100;
            if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
                SubtitleManager.getInstance().setSubtitleSync(
                        ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(), subtitleTime);
            }
        } else {
            if (postion >= 9900) {
                postion = 9900;
            }
            SubtitleManager.setSubtitleSettingOpt(9, "" + (postion + 100), viewId);
            subtitleTime = postion + 100;
            SubtitleManager.getInstance().setSubtitleSync(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(), subtitleTime);
        }
    }

    // display subtitleTrackName
    public void upDateSubtitleTrackName() {
         String subtitleTrackName = ((VideoPlayerActivity) context).getSubtitleTrackName();
         SubtitleManager.setSubtitleSettingOpt(10, subtitleTrackName,viewId);
    }

    /**
     * Subtitles Numbers
     *
     * @param postion
     * @param leftOrRight
     */
    private void changeAnNativeInnerSubtitleLanguage(int postion, boolean leftOrRight) {
        int base = SubtitleManager.mInnerSubtitleBase;
        if (leftOrRight) {// Default is left
            if (postion > base) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2)
                                + (postion - base - 1), viewId);
                subtitlePosition = postion - 1;
            } else if (postion == base) {
                if (mInternalSubtitleCount > 0) {
                    SubtitleManager.setSubtitleSettingOpt(2,
                            context.getString(R.string.subtitle_2_value_2)
                                    + (mInternalSubtitleCount - 1), viewId);
                    subtitlePosition = base + mInternalSubtitleCount - 1;
                } else {
                    subtitlePosition = base;
                }
            }
        } else {
            if (postion == (base + mInternalSubtitleCount - 1)
                    && (postion < base + mInternalSubtitleCount)) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2) + "0", viewId);
                subtitlePosition = base;
            } else if (postion < base + mInternalSubtitleCount && postion >= base) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2)
                                + (postion - base + 1), viewId);
                subtitlePosition = postion + 1;
            }
        }
        if (mInternalSubtitleCount > 0) {
            ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                .selectTrack(subtitlePosition);
            SubtitleManager.mSelectedSubTrackLast = subtitlePosition;
            SubtitleManager.setSubtitleSettingOpt(3,
                    SubtitleManager.getSubtitleLanguage(subtitlePosition, viewId), viewId);
        }

        if (subCodeType[subtitlePosition] == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP) {
            isImageSub = true;
        } else {
            isImageSub = false;
        }
    }

    /**
     * Subtitles Numbers
     *
     * @param postion
     * @param leftOrRight
     */
    public void changeInnerSubtitleLanguage(int postion, boolean leftOrRight) {
        Log.i(TAG,"changeInnerSubtitleLanguage:"+postion);
        if (leftOrRight) {// Default is left
            if (postion > 0) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2)
                                + (postion - 1), viewId);
                subtitlePosition = postion - 1;
            } else if (postion == 0) {
                if (mInternalSubtitleCount > 0) {
                    SubtitleManager.setSubtitleSettingOpt(2,
                            context.getString(R.string.subtitle_2_value_2)
                                    + (mInternalSubtitleCount - 1), viewId);
                    subtitlePosition = mInternalSubtitleCount - 1;
                } else {
                    subtitlePosition = 0;
                }
            }
        } else {
            if (postion == (mInternalSubtitleCount - 1)
                    && (postion < mInternalSubtitleCount)) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2) + "0", viewId);
                subtitlePosition = 0;
            } else if (postion < mInternalSubtitleCount && postion >= 0) {
                SubtitleManager.setSubtitleSettingOpt(2,
                        context.getString(R.string.subtitle_2_value_2)
                                + (postion + 1), viewId);
                subtitlePosition = postion + 1;
            }
        }
        Log.i(TAG,"mInternalSubtitleCount:"+mInternalSubtitleCount);
        Log.i(TAG,"subtitlePosition:"+subtitlePosition);
        if (mInternalSubtitleCount > 0) {
            SubtitleManager.getInstance().setSubtitleTrack(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(),
                    subtitlePosition) ;
            ((VideoPlayerActivity) context).SaveSubtitleTrackByFileName(subtitlePosition);
            SubtitleManager.setSubtitleSettingOpt(3,
                    SubtitleManager.getSubtitleLanguage(subtitlePosition, viewId), viewId);
        }
        if (subCodeType!=null && subCodeType[subtitlePosition] == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP) {
            isImageSub = true;
        } else {
            isImageSub = false;
        }
        upDateSubtitleTrackName();
    }

    private void changeTimeOrder(int postion) {
        if (postion > 9900) {
            postion = -10100;
        }
        SubtitleManager.setSubtitleSettingOpt(9, "" + (postion + 100), viewId);
        subtitleTime = postion + 100;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            SubtitleManager.getInstance().setSubtitleSync(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(), subtitleTime);
        }
    }

    private void changeAnNativeExtSubtitleLanguage(boolean leftOrRight) {
        String[] languageTypes = SubtitleManager.getSubtitleLanguageTypes(viewId);
        if (languageTypes != null) {
            if (SubtitleManager.mExtSubtitleNo<= 1) {
                return;
            }
            if (subtitleLanguageIndex == 0){
                subtitleLanguageIndex = SubtitleManager.mExtSubtitleBase;
            }
            Log.i(TAG, "changeSubtitleLanguage subtitleLanguageIndex : "+ subtitleLanguageIndex);
            if (leftOrRight) {
                if (subtitleLanguageIndex == SubtitleManager.mExtSubtitleBase) {
                    subtitleLanguageIndex = SubtitleManager.mExtSubtitleBase+ SubtitleManager.mExtSubtitleNo - 1;
                } else {
                    subtitleLanguageIndex = subtitleLanguageIndex - 1;
                }
            } else {
                if (subtitleLanguageIndex == SubtitleManager.mExtSubtitleBase + SubtitleManager.mExtSubtitleNo - 1) {
                    subtitleLanguageIndex = SubtitleManager.mExtSubtitleBase;
                } else {
                    subtitleLanguageIndex = subtitleLanguageIndex + 1;
                }
            }
            Log.i(TAG, "changeSubtitleLanguage subtitleLanguageIndex : "+ subtitleLanguageIndex);
            SubtitleManager.setSubtitleSettingOpt(3,
                    SubtitleManager.getSubtitleLanguage(subtitleLanguageIndex, viewId), viewId);
            ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                .selectTrack(subtitleLanguageIndex);
            SubtitleManager.mSelectedSubTrackLast = subtitleLanguageIndex;
        }
    }

    private void changeExtSubtitleLanguage(boolean leftOrRight) {
        String[] languageTypes = SubtitleManager.getSubtitleLanguageTypes(viewId);
        if (languageTypes != null) {
            mImageSubtitleCount = languageTypes.length;
            Log.i(TAG, "changeSubtitleLanguage mImageSubtitleCount : "
                    + mImageSubtitleCount+" inner sub count:"+mInternalSubtitleCount);
            int externalSubtitleCount = mImageSubtitleCount-mInternalSubtitleCount;
            if (mImageSubtitleCount <= 1) {
                return;
            }
            Log.i(TAG, "changeSubtitleLanguage subtitleLanguageIndex : "+ subtitleLanguageIndex);
            if (leftOrRight) {
                if (subtitleLanguageIndex == 0) {
                    subtitleLanguageIndex = externalSubtitleCount - 1;
                } else {
                    subtitleLanguageIndex = subtitleLanguageIndex - 1;
                }
            } else {
                if (subtitleLanguageIndex == externalSubtitleCount - 1) {
                    subtitleLanguageIndex = 0;
                } else {
                    subtitleLanguageIndex = subtitleLanguageIndex + 1;
                }
            }
            Log.i(TAG, "changeSubtitleLanguage subtitleLanguageIndex : "+ subtitleLanguageIndex);
            Log.i(TAG, "subtitleLanguageIndex+mInternalSubtitleCount : "
                + subtitleLanguageIndex+mInternalSubtitleCount);
            SubtitleManager.setSubtitleSettingOpt(3,
                    SubtitleManager.getSubtitleLanguage(subtitleLanguageIndex+mInternalSubtitleCount, viewId), viewId);
            SubtitleManager.getInstance().setSubtitleTrack(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(),
                    subtitleLanguageIndex | 256) ;
        }
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            // android native  api
            if (Tools.getSubtitleAPI()) {
                refreshAnNativeInnerSubtitleSettingDialog();
            // mstar api
            } else {
                refreshInnerSubtitleSettingDialog();
                upDateSubtitleTrackName();
            }
        }
    };

    private void refreshAnNativeInnerSubtitleSettingDialog(){
        TrackInfo []trackInfo = null;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            trackInfo = ((VideoPlayerActivity) context).getVideoPlayHolder()
                            .getPlayerView().getTrackInfo();
        }
        if (null != trackInfo && SubtitleManager.mVideoSubtitleNo > 0) {
            mInternalSubtitleCount = SubtitleManager.mVideoSubtitleNo;
            int base = SubtitleManager.mInnerSubtitleBase;
            String subtitleLanguageTypes[] = new String[base+mInternalSubtitleCount];
            for (int i=base;i<base+mInternalSubtitleCount;i++) {
                 subtitleLanguageTypes[i] = trackInfo[i].getLanguage();
            }
            subtitlePosition = base;
            SubtitleManager.setSubtitleSettingOpt(3,subtitleLanguageTypes[subtitlePosition], viewId);
            SubtitleManager.setSubtitleLanguageType(subtitleLanguageTypes, viewId);
            adapter.notifyDataSetChanged();
        }


    }
    private void refreshInnerSubtitleSettingDialog(){
        SubtitleTrackInfo info = null ;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            info = SubtitleManager.getInstance().getAllSubtitleTrackInfo(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer());
        }
        if (null != info) {
            int totalInfoNum = info.getAllSubtitleCount();
            Log.i(TAG,
                    "***info**" + info + " " + totalInfoNum + " "
                            + info.getAllInternalSubtitleCount() + " "
                            + info.getAllSubtitleCount());
            mInternalSubtitleCount = info.getAllInternalSubtitleCount();
            Log.i(TAG,"subtitlePosition:"+subtitlePosition);
            String subtitleLanguageTypes[] = new String[totalInfoNum];
            info.getSubtitleLanguageType(subtitleLanguageTypes, false);
            if (totalInfoNum != 0) {
                for (int i = 0; i < totalInfoNum; i++) {
                    Log.i(TAG, "*****" + totalInfoNum + " "
                            + subtitleLanguageTypes[i]);
                }
                SubtitleManager.setSubtitleSettingOpt(3,
                        subtitleLanguageTypes[subtitlePosition], viewId);
                SubtitleManager.setSubtitleLanguageType(
                        subtitleLanguageTypes, viewId);
            } else {
                SubtitleManager.setSubtitleLanguageType(null, viewId);
                getInfoNum++;
                if (getInfoNum <= 2) {
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    return;
                } else {
                    getInfoNum = 0;
                }
            }
            if (adapter!=null) {
                adapter.notifyDataSetChanged();
            }
        }


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

    public void zoomSubtitleFontSize(float size) {
        int viewId = ((VideoPlayerActivity) context).getVideoPlayHolder().getViewId();
        int currentDualModeFocus = ((VideoPlayerActivity) context).getVideoPlayHolder().getCurrentDualModeSelected();
        // left: large, medium (35 sp), small, big
        float zoomRate = 1.0f;
        switch (currentDualModeFocus) {
            case VideoPlayerViewHolder.PIP_POSITION_LEFT_TOP:
            case VideoPlayerViewHolder.PIP_POSITION_LEFT_BOTTOM:
            case VideoPlayerViewHolder.PIP_POSITION_RIGHT_TOP:
            case VideoPlayerViewHolder.PIP_POSITION_RIGHT_BOTTOM:
                if (1 == viewId) {
                    zoomRate = 1.0f;
                } else {
                    zoomRate = 0.25f;
                }
                break;
            case VideoPlayerViewHolder.DUAL_MODE_LEFT_RIGHT:
                zoomRate = 0.5f;
                break;
            case VideoPlayerViewHolder.DUAL_MODE_FULL_SCREEN:
                zoomRate = 1.0f;
                break;
            default :
                break;
        }
        Log.i(TAG, "size:" + size + "zoomRate:" + zoomRate + " viewId:" + viewId);

        subtitleTextView.setTextSize(size * zoomRate);
    }

    private boolean isExternalSubtitleSettingPage(int viewId) {
        String opt0 = SubtitleManager.getSubtitleSettingOptValue(0, viewId);
        Log.i(TAG, "isExternalSubtitleSettingPage opt0:" + opt0 + " viewId:" + viewId);
        if (context.getString(R.string.subtitle_0_value_1).equals(opt0)) {
            return true;
        }
        return false;
    }

    private boolean hasSetExternalSubtitlePath(int viewId) {
        String opt1 = SubtitleManager.getSubtitleSettingOptValue(1, viewId);
        Log.i(TAG, "hasSetExternalSubtitle opt1:" + opt1 + " viewId:" + viewId);
        // Judge if External Subtitle Path has benn set, if has set path, then user can change External Subtitle
        // Options(ex. font size, color......), if not set path, show tips to users when users change options.
        if (context.getString(R.string.subtitle_1_value_1).equals(opt1)) {
            return false;
        }
        return true;
    }

}
