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

import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.data.BaseData;
import com.walton.filebrowser.ui.main.FileBrowserActivity;

/**
 *
 * @author
 */
public class DetailInfoDialog extends Dialog {
    private VideoPlayerActivity mVideoPlayActivity;
    // private VideoLastPlayTimePre mVideoLastPlayTimePre;
    private int current_position;
    private String mDuration = "00:00:00";
    private String mAudioCodec = null;
    private String mVideoCodec = null;
    // Corresponding control
    private TextView file_name_tv;
    // private TextView file_path_tv;
    private TextView file_zise_tv;
    private TextView file_format_tv;
    // private TextView resolution_tv;
    private TextView duration_tv;
    private TextView audio_codec_tv;
    private TextView video_codec_tv;

    // private TextView last_playertime_tv;
    /**
     *
     * Structure function
     */
    public DetailInfoDialog(VideoPlayerActivity activity, int theme, int position,
            String duration, String aCodec, String vCodec) {
        super(activity, theme);
        this.mVideoPlayActivity = activity;
        this.current_position = position;
        if (duration.length() > 0)
            mDuration = duration;
        mAudioCodec = aCodec;
        mVideoCodec = vCodec;
    }

    public DetailInfoDialog(FileBrowserActivity fba, int position) {
        super(fba);
        // this.mFileBrowserActivity = fba;
        this.current_position = position;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_info);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        // Resources resources = mVideoPlayActivity.getResources();
        // Drawable drawable = resources.getDrawable(R.drawable.bg_tips);
        // w.setBackgroundDrawable(drawable);
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.20);
        int height = (int) (display.getHeight() * 0.50);
        w.setLayout(width, height);
        w.setGravity(Gravity.RIGHT);

        WindowManager.LayoutParams wl = w.getAttributes();

        //wl.screenBrightness = 1.0f;

        wl.x = 80;

        w.setAttributes(wl);
        // w.setBackgroundDrawableResource(color.transparent);
        findViews();
        getVideoInfor();
    }

    /**
     *
     */
    private void findViews() {
        file_name_tv = (TextView) findViewById(R.id.file_name);
        file_zise_tv = (TextView) findViewById(R.id.file_zise);
        file_format_tv = (TextView) findViewById(R.id.file_format);
        duration_tv = (TextView) findViewById(R.id.duration);
        audio_codec_tv = (TextView) findViewById(R.id.audio_codec);
        video_codec_tv = (TextView) findViewById(R.id.video_codec);
        // last_playertime_tv = (TextView) findViewById(R.id.last_playertime);
    }

    /**
     *
     * For video information
     */
    private void getVideoInfor() {
        List<BaseData> list = mVideoPlayActivity.getVideoPlayList();
        BaseData data = list.get(current_position);
        String name = data.getName();
        file_name_tv.setText(name);
        String path = data.getPath();
        System.out.println("path:" + path);
        if (data.getSize() == null || "".equals(data.getSize())) {
            file_zise_tv.setText(mVideoPlayActivity
                    .getString(R.string.video_size_unknown));
        } else {
            file_zise_tv.setText(data.getSize());
        }
        file_format_tv.setText(path.substring(path.lastIndexOf(".") + 1,
                path.length()));
        duration_tv.setText(mDuration);
        audio_codec_tv.setText(mAudioCodec);
        video_codec_tv.setText(mVideoCodec);
    }
}
