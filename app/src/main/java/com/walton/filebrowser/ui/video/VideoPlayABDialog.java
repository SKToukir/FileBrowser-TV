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

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;

/**
 *
 * @author 钂嬪崕鍏�(jianghq@biaoqi.com.cn)
 *
 * @since 1.0
 *
 * @date 2012-2-16
 */
public class VideoPlayABDialog extends Dialog {
    public static final String A_POSITION = "APOSITION";
    public static final String B_POSITION = "bPOSITION";
    private VideoPlayerActivity context;
    private CheckBox checkBox;
    private Button aButton;
    private Button bButton;
    private VideoPlayerViewHolder videoPlayHolder;
    private float total;
    public int postionA;
    public int postionB;
    public SharedPreferences sharedata;
    // private SharedPreferences sharedataFlag;
    public boolean bFlag = false;
    public boolean aFlag = false;
    public static final int POSITION = 0x01;
    private static final String LOCALMM_POSITION = "LOCALMM_POSITION";

    // private static final String LOCALMM_FUNCTION = "LOCALMM_FUNCTION";
    public VideoPlayABDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public VideoPlayABDialog(VideoPlayerActivity context, int theme,
            VideoPlayerViewHolder videoPlayHolder) {
        super(context, theme);
        this.context = context;
        this.videoPlayHolder = videoPlayHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_ab);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.40);
        int height = (int) (display.getHeight() * 0.60);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        sharedata = context.getSharedPreferences(LOCALMM_POSITION,
                Context.MODE_PRIVATE);
        // sharedataFlag = context.getSharedPreferences(LOCALMM_FUNCTION,
        // Context.MODE_PRIVATE);
        findViews();
        setListeners();
        if (Constants.abFlag) {
            checkBox.setFocusable(true);
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
        context.hideControlDelay();
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
            case 1:
                bFlag = false;
                aFlag = false;
                videoPlayHolder.bt_playA.setVisibility(View.GONE);
                videoPlayHolder.bt_playB.setVisibility(View.GONE);
                dismiss();
                context.hideControlDelay();
                break;
            case 2:
                setABVisible(msg.arg1);
                break;
                default:
                    break;
            }

        }
    };

    private void setABVisible(int isVisible){
        if(isVisible == 1){
            setPosition(A_POSITION,
                    (int) postionA);
            float postion = (postionA / total)
                    * videoPlayHolder.videoSeekBar.getWidth();
            videoPlayHolder.bt_playA
                    .setPadding((int) postion, 0, 0, 15);
            videoPlayHolder.bt_playA.setVisibility(View.VISIBLE);

            setPosition(B_POSITION, (int)postionB);
            postion = (postionB / total)
                    * videoPlayHolder.videoSeekBar.getWidth();
            videoPlayHolder.bt_playB.setPadding((int) postion, 0,
                    0, 12);
            videoPlayHolder.bt_playB.setVisibility(View.VISIBLE);
        }else{
            Constants.aFlag = false;
            aFlag = false;
            videoPlayHolder.bt_playA.setVisibility(View.INVISIBLE);
            videoPlayHolder.bt_playB.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners() {
        total = (float) videoPlayHolder.getPlayerView().getDuration();
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.abFlag) {
                    // When set positionA, media player don't need pause,seekTo,start,
                    // so comment out these useless code.
                    // context.localPause(false);
                    // videoPlayHolder.getPlayerView().seekTo((int) videoPlayHolder.getPlayerView().getCurrentPosition());
                    // context.localResume(false);
                    /*postionA = videoPlayHolder.getPlayerView()
                            .getCurrentPosition();
                    setPosition(A_POSITION,(int) postionA);
                    aFlag = true;
                    VideoPlayerActivity.state = VideoPlayerActivity.OPTION_STATE_PLAYAB;
                    videoPlayHolder.bt_playA
                            .setPadding((int) (postionA / total)
                                    * videoPlayHolder.videoSeekBar.getWidth(), 0, 0, 15);
                    videoPlayHolder.bt_playA.setVisibility(View.VISIBLE);*/
                    postionA = videoPlayHolder.getPlayerView().getCurrentPosition();
                    setPosition(A_POSITION, (int) postionA);
                    float postion = (postionA / total) * videoPlayHolder.videoSeekBar.getWidth();
                    aFlag = true;
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAYAB;
                    videoPlayHolder.bt_playA.setPadding((int) postion, 0, 0, 15);
                    videoPlayHolder.bt_playA.setVisibility(View.VISIBLE);
                    Constants.aFlag = true;
                } else {
                    // First open A - B complex sowing function
                    String text = context.getString(R.string.open_ab_repeat);
                    setToastMessage(text);
                }
            }
        });
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.aFlag && Constants.abFlag) {
                    int pos = videoPlayHolder.getPlayerView().getCurrentPosition();
                    // When set positionB, media player don't need seekTo,
                    // so comment out these useless code.
                    // videoPlayHolder.getPlayerView().seekTo(pos);
                    postionB = (int) pos;
                    float bpostion = (pos / total)* videoPlayHolder.videoSeekBar.getWidth();
                    if (sharedata.getInt(A_POSITION, 0) + 1000 < postionB) {
                        setPosition(B_POSITION, (int)postionB);
                        videoPlayHolder.bt_playB.setPadding((int) bpostion, 0, 0, 12);
                        videoPlayHolder.bt_playB.setVisibility(View.VISIBLE);
                        bFlag = true;
                        videoPlayHolder.setAllUnSelect(videoPlayHolder.getPlayerView().isPlaying());
                        videoPlayHolder.videoSeekBar.setProgress(sharedata.getInt(A_POSITION, 0));
//                        videoPlayHolder.getPlayerView().seekTo(sharedata.getInt(A_POSITION, 0));
//                      context.localPause(false);
//                      context.localResume(false);
                        videoPlayHolder.setVideoPlayABSelect(true);
                    } else {
                        // Set point B before first need to set up A point
                        // position
                        String text = context
                                .getString(R.string.set_b_point_failed);
                        setToastMessage(text);
                    }
                } else {
                    // B point position and A point position cannot be repeated
                    String text = context.getString(R.string.set_point_a_first);
                    setToastMessage(text);
                }
            }
        });
        checkBox.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.line_layout);
                if (hasFocus) {
                    layout.setBackgroundResource(R.drawable.button_selected);
                } else {
                    layout.setBackgroundResource(R.drawable.button_normal);
                }
            }
        });
        checkBox.setOnKeyListener(onkeyListenter);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    Constants.abFlag = true;
                } else {
                    Constants.aFlag = false;
                    bFlag = false;
                    aFlag = false;
                    Constants.abFlag = false;
                    videoPlayHolder.bt_playA.setVisibility(View.GONE);
                    videoPlayHolder.bt_playB.setVisibility(View.GONE);
                }
            }
        });
        // videoPlayHolder.videoSeekBar.setOnSeekBarChangeListener(seekBarChageListener);
    }

    public void setToastMessage(String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     *
     * Save the current AB point coordinate position
     */
    private void setPosition(String name, int postion) {
        SharedPreferences preference = context.getSharedPreferences(
                LOCALMM_POSITION, Context.MODE_PRIVATE);
        Editor editor = preference.edit();
        editor.putInt(name, postion);
        editor.commit();
    }

    private void findViews() {
        checkBox = (CheckBox) findViewById(R.id.play_on_or_off_checkbox);
        aButton = (Button) findViewById(R.id.play_ab_a_btn);
        bButton = (Button) findViewById(R.id.play_ab_b_btn);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                    break;
                }
                }
            }
            }
            return false;
        }
    };

    public boolean isABplaying() {
        return checkBox.isChecked();
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
