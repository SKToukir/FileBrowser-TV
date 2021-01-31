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
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.AudioAC4ListAdapter;
import com.walton.filebrowser.util.TvApiController;

public class AudioAC4SettingDialog extends Dialog {
    private static final String TAG = "AudioAC4SettingDialog";
    public static final float DIALOG_DISPLAY_WIDTH_SCALING = 0.40f;
    public static final float DIALOG_DISPLAY_HEIGHT_SCALING = 0.63f;
    private static final int PROGRESS_DELTA = 12;
    private static final int DE_MAX_VALUE = 24;
    private ListView mAudioAC4ListView = null;
    private TextView mAudioAC4EnhanceTextView = null;
    private SeekBar mAudioADTypeSeekBar= null;
    private AudioAC4ListAdapter adapter = null;
    private VideoPlaySettingDialog mSettingDialog = null;
    private Context context = null;

    private int mADPresentationSelected = 0;
    private String[] audioAC4SettingName = {"Presentation"};
    private String []audioAC4SettingADPresentationOpt;

    public AudioAC4SettingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public AudioAC4SettingDialog(Context context, int theme,
            VideoPlaySettingDialog videoPlaySettingDialog) {
        super(context, theme);
        this.context = context;
        this.mSettingDialog = videoPlaySettingDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSettingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_ac4_setting_dialog);
        // instantiation new window
        Window w = getWindow();
        // for the default display data
        Display display = w.getWindowManager().getDefaultDisplay();

        mAudioAC4ListView = (ListView) this.findViewById(R.id.audio_ac4_list);
        mAudioAC4EnhanceTextView = (TextView) this.findViewById(R.id.audio_ac4_enhance);
        mAudioADTypeSeekBar = (SeekBar) this.findViewById(R.id.ad_type_seekbar);

        if (mAudioADTypeSeekBar != null) {
            mAudioAC4EnhanceTextView.setText("DE: " + (mAudioADTypeSeekBar.getProgress()-PROGRESS_DELTA) + "db ");
            mAudioADTypeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
            mAudioADTypeSeekBar.setEnabled(true);
            mAudioADTypeSeekBar.setMax(DE_MAX_VALUE);
            mAudioADTypeSeekBar.setProgress(getAC4Enhance() + PROGRESS_DELTA);
            mAudioADTypeSeekBar.setKeyProgressIncrement(1);
        }

        initAC4PresentationItem();

        adapter = new AudioAC4ListAdapter(context, audioAC4SettingName, audioAC4SettingADPresentationOpt, AudioAC4SettingDialog.this);
        mAudioAC4ListView.setDivider(null);
        mAudioAC4ListView.setAdapter(adapter);
        setListeners();
        // window's title is empty
        w.setTitle(null);
        int width = (int) (display.getWidth() * DIALOG_DISPLAY_WIDTH_SCALING);
        int height = (int) (display.getHeight() * DIALOG_DISPLAY_HEIGHT_SCALING);
        // Settings window size
        w.setLayout(width, height);
        // Settings window display position
        w.setGravity(Gravity.CENTER);
        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        initAC4PresentationItem();
    }

    private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int value = mAudioADTypeSeekBar.getProgress()-PROGRESS_DELTA;
            Log.i(TAG, "onProgressChanged progress:" + progress);
            mAudioAC4EnhanceTextView.setText("DE " + value + "db ");
            setAC4Enhance(value);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    }

    private void initAC4PresentationItem() {
        int ac4PresentationNum = getAC4PresentationNum();
        if (ac4PresentationNum > 0) {
            audioAC4SettingADPresentationOpt = new String[ac4PresentationNum];
            // initialize
            for (int i = 0; i < ac4PresentationNum; i++) {
                audioAC4SettingADPresentationOpt[i] = getTvLanguage(i);
            }

            int []presentationLang = new int[ac4PresentationNum];
            presentationLang = getAC4PresentationLanguage();
            // set value
            if (presentationLang != null) {
                int length = ac4PresentationNum > presentationLang.length ? presentationLang.length : ac4PresentationNum;
                for (int i = 0; i < length; i++) {
                    audioAC4SettingADPresentationOpt[i] = getTvLanguage(presentationLang[i]);
                }
            }

        } else {
            audioAC4SettingADPresentationOpt = new String[1];
            audioAC4SettingADPresentationOpt[0] = "-1";
        }
    }

    public String getTvLanguage(int iLang) {
        return TvApiController.getInstance(context).getTvLanguage(iLang);
    }

    private int getAC4PresentationNum() {
        return TvApiController.getInstance(context).getAC4PresentationNum();
    }

    private int[] getAC4PresentationLanguage() {
        return TvApiController.getInstance(context).getAC4PresentationLanguage();
    }

    private int getAC4Enhance() {
        return TvApiController.getInstance(context).getAC4DialogEnhance();
    }

    private void setAC4Enhance(int progress) {
        TvApiController.getInstance(context).setAC4DialogEnhance(progress);
    }

    private void setAC4Presentation(int index) {
        TvApiController.getInstance(context).setAC4Presentation(index);
    }

    private void setListeners() {
        mAudioAC4ListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "------onItemClick ---------- position:" + position);
                handleMidClick(position);
            }
        });
        mAudioAC4ListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.i(TAG, "------onkeyListenter ---------- keyCode:" + keyCode + " getAction:" + event.getAction());

            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT: {
                            int position = mAudioAC4ListView.getSelectedItemPosition();
                            handleImageClick(position, true);
                            break;
                        }
                        case KeyEvent.KEYCODE_DPAD_RIGHT: {
                            int position = mAudioAC4ListView.getSelectedItemPosition();
                            handleImageClick(position, false);
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };

    public void handleImageClick(int position, boolean bLeftImageClick) {
        Log.i(TAG, "------ handleImageClick -------- position:" + position + " bLeftImageClick:" + bLeftImageClick);

        switch (position) {
            case 0:
                if (bLeftImageClick) {
                    if (mADPresentationSelected<= 0) {
                        mADPresentationSelected = audioAC4SettingADPresentationOpt.length - 1;
                    } else {
                        mADPresentationSelected--;
                    }
                } else {
                    if (mADPresentationSelected >= audioAC4SettingADPresentationOpt.length - 1) {
                        mADPresentationSelected = 0;
                    } else {
                        mADPresentationSelected++;
                    }
                }
                adapter.setADTypeItemSelected(mADPresentationSelected);
                break;
        }
        setAC4Presentation(mADPresentationSelected+1);

        adapter.notifyDataSetChanged();
    }

    public void handleMidClick(int position) {
        Log.i(TAG, "----- handleMidClick ----- position:" + position);
        switch (position) {
        case 0:
            break;
        default:
            break;
        }
        adapter.notifyDataSetChanged();
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

