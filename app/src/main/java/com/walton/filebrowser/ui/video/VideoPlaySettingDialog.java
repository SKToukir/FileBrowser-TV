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

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer.TrackInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.media.SubtitleTrackInfo;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvS3DManager;
import com.walton.filebrowser.R;
import com.walton.filebrowser.adapter.VideoPlaySettingListAdapter;
import com.walton.filebrowser.ui.common.GoldenLeftEyeActivity;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.Tools;


/**
 * @author
 * @since 1.0
 * @date 2012-2-16
 */
public class VideoPlaySettingDialog extends Dialog {
    private final static String TAG = "VideoPlaySettingDialog";
    private final static int mAutoDetect3DFromatTimes = 3;
    private final static int SHOW_TOAST = 0;
    private VideoPlayerActivity context;
    private ListView playSettingListView;
    private VideoPlaySettingListAdapter adapter;
    // private String videoPath;
    private PlaySettingAudioTrackDialog playSettingAudioTrackDialog;
    private HDRSettingDialog mHDRSettingDialog;
    private DolbyHDRSettingDialog mDolbyHDRSettingDialog;
    private ThreeDSettingDialog threeDSettingDialog;
    private VideoRotateSettingDialog mRotateDegreesSettingDialog;
    private PlaySettingSWDRDialog mPlaySettingSWDRDialog;
    private AudioAC4SettingDialog mAudioAC4SettingDialog;
    private int viewId = 1;
    private boolean mIsSTB = false;
    private boolean dectectSucess = false;
    private SubtitleManager mSubtitleManager ;
    private MMediaPlayer mMMediaPlayer ;
    /** Module: Open HDR */
    public static final int MODULE_OPEN_HDR = 23;
    /** Module: Dolby HDR */
    public static final int MODULE_DOLBY_HDR = 24;

    public final static int ITEM_3D = -1;//0;
    public final static int ITEM_SUBTITLE = 0;//1;
    public final static int ITEM_BREAK_POINT = -2;//2;
    public final static int ITEM_AUDIO_TRACK = 1;//3;
    public final static int ITEM_THUMBNAIL_MODE = -3;//4;
    public final static int ITEM_ROTATE_MODE = 2;//5;
    public final static int ITEM_GOLDEN_EYE = 3;//6;
    public final static int ITEM_OPEN_HDR = 4;//7;
    public final static int ITEM_DOLBY_HDR = -4;//8;
    public final static int ITEM_CONTINUOUS_PLAY = 5;//9;
    public final static int ITEM_SWDR = 6;//10;
    public final static int ITEM_AC4 = -5;//11;
    public final static int ITEM_TOTAL_COUNT = 7;//12;

    public VideoPlaySettingDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public VideoPlaySettingDialog(VideoPlayerActivity context, int theme,
                                  String videoPath, MMediaPlayer mMMediaPlayer) {
        super(context, theme);
        this.context = context;
        // this.videoPath = videoPath;
        this.mMMediaPlayer = mMMediaPlayer ;
        mSubtitleManager = SubtitleManager.getInstance() ;
        viewId = context.getVideoPlayHolder().getViewId();
        adapter = new VideoPlaySettingListAdapter(context, VideoPlayerViewHolder.playSettingName,
                Tools.initPlaySettingOpt(context, viewId),VideoPlaySettingDialog.this);
        Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.play_setting_0_value_2), viewId);
        if(context.getBreakPointFlag()) {
            Tools.setPlaySettingOpt(ITEM_BREAK_POINT, context.getString(R.string.play_setting_0_value_1), viewId);
        }else{
            Tools.setPlaySettingOpt(ITEM_BREAK_POINT, context.getString(R.string.play_setting_0_value_2), viewId);
        }
        if (context.getmIsNeedContinuousPlay()) {
            Tools.setPlaySettingOpt(ITEM_CONTINUOUS_PLAY, context.getString(R.string.play_setting_0_value_1), viewId);
        } else {
            Tools.setPlaySettingOpt(ITEM_CONTINUOUS_PLAY, context.getString(R.string.play_setting_0_value_2), viewId);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_TOAST:
                    showToastTip((String)msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        };
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "****onCreate*******");
        setContentView(R.layout.video_play_setting_dialog);
        // Instantiation new window
        Window w = getWindow();

        // Get the default display data
         Resources resources = context.getResources();
         Drawable drawable = resources.getDrawable(R.drawable.dialog_background);
         w.setBackgroundDrawable(drawable);
        Display display = w.getWindowManager().getDefaultDisplay();
        playSettingListView = (ListView) this.findViewById(R.id.videoPlaySettingListView);
        playSettingListView.setDivider(null);
        mIsSTB = Tools.isMstarSTB();

        setListeners();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.25);
        int height = (int) (display.getHeight() * 0.50);
        w.setLayout(width, height);
        w.setGravity(Gravity.RIGHT);

        WindowManager.LayoutParams wl = w.getAttributes();

        wl.x = 80;

        w.setAttributes(wl);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "****onStart*******");
        if (Tools.isThumbnailModeOn()) {
            Tools.setPlaySettingOpt(ITEM_THUMBNAIL_MODE,context.getString(R.string.play_setting_0_value_1), viewId);
        } else {
            Tools.setPlaySettingOpt(ITEM_THUMBNAIL_MODE,context.getString(R.string.play_setting_0_value_2), viewId);
        }
        if (Tools.isRotateModeOn()) {
            Tools.setPlaySettingOpt(ITEM_ROTATE_MODE,context.getString(R.string.play_setting_0_value_1), viewId);
        } else {
            Tools.setPlaySettingOpt(ITEM_ROTATE_MODE,context.getString(R.string.play_setting_0_value_2), viewId);
        }
        refresh3DSwitchStatus();
    }

    @Override
    public void dismiss() {
        Log.d(TAG, "****dismiss*******");
        if (threeDSettingDialog != null) {
            threeDSettingDialog.dismiss();
        }
        if (playSettingAudioTrackDialog != null) {
            playSettingAudioTrackDialog.dismiss();
        }
        if (mHDRSettingDialog != null) {
            mHDRSettingDialog.dismiss();
        }

        if (mDolbyHDRSettingDialog!= null) {
            mDolbyHDRSettingDialog.dismiss();
        }
        if (mRotateDegreesSettingDialog != null) {
            mRotateDegreesSettingDialog.dismiss();
        }
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "****show*******");
        playSettingListView.setAdapter(adapter);
    }


    /* These code is Extraneous, so comment out it.
     * When user change 3D switch(3D ON or OFF) in VideoPlaySettingDialog,
     * lmm don't necessary to reflesh3DState.
     *
    private boolean mReflesh3DStateFlag = false;
    private Runnable mReflesh3DStateRun = new  Runnable (){
        public void run() {
            context.displayFormat = Tools.getCurrent3dFormat();
            if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                Tools.setPlaySettingOpt(0, context.getString(R.string.play_setting_0_value_2), viewId);
            }
            mReflesh3DStateFlag = true;
        }
        };

    private void reflesh3DState() {
        mReflesh3DStateFlag = false;
        new Thread(mReflesh3DStateRun).start();
        int count = 100;
        while(count-- > 0 && !mReflesh3DStateFlag) {
            android.os.SystemClock.sleep(1);
        }
    }
    */

    private void  refresh3DSwitchStatus() {
        if (!Tools.unSupportTVApi()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        context.mDisplayFormat = Tools.getCurrent3dFormat();
                        Log.i(TAG, "refresh3DSwitchStatus current3DFormat = "+ context.mDisplayFormat);
                        if (context.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                            Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_2), viewId);
                        }  else {
                            String opt = Tools.getPlaySettingOpt(ITEM_3D, viewId);
                            if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                                Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_1), viewId);
                            }
                        }
                    }
                }).start();
        } else {
            Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_2), viewId);
        }
    }

    public void handleLeftClick(int pos){
        if (pos == ITEM_3D) {
            change3DState();
        } else if (pos == ITEM_SUBTITLE){
            changeSubtitleStateLeft();
        } else if (pos == ITEM_BREAK_POINT) {
            changeBreakPointFlag();
        } else if (pos == ITEM_THUMBNAIL_MODE) {
            changeThumbnailMode();
        } else if (pos == ITEM_ROTATE_MODE) {
            changeRotateMode();
        } else if (pos == ITEM_GOLDEN_EYE) {
            changdeGoldenLeftEyeMode();
        } else if (pos == ITEM_CONTINUOUS_PLAY) {
            changeContinuousPlayFlag();
        }
        adapter.notifyDataSetChanged();
    }

    private void changdeGoldenLeftEyeMode() {
        String opt = Tools.getPlaySettingOpt(ITEM_GOLDEN_EYE, viewId);
        if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            if(context.getVideoPlayHolder().getDualVideoMode()){
                Toast.makeText(context, context.getString(R.string.dual_mode_not_support_golden_left_eye), Toast.LENGTH_SHORT).show();
                return;
            }
            if(Tools.isMstarSTB() || Tools.unSupportTVApi() || Tools.isMstarTV_MI()){
                Toast.makeText(context, context.getString(R.string.stb_not_support_golden_left_eye), Toast.LENGTH_SHORT).show();
                return;
            }
            if(Tools.isRotateModeOn()){
                Toast.makeText(context, context.getString(R.string.dual_mode_not_support_golden_left_eye), Toast.LENGTH_SHORT).show();
                return;
            }
            Tools.setPlaySettingOpt(ITEM_GOLDEN_EYE, context.getString(R.string.play_setting_0_value_1), viewId);
            //open golden left eye
            Intent intent = new Intent(context, GoldenLeftEyeActivity.class);
            intent.putExtra("viewId", viewId);
            context.startActivity(intent);
            this.dismiss();
        } else if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_GOLDEN_EYE, context.getString(R.string.play_setting_0_value_2), viewId);
            //close golden left eye
            Intent intent = new Intent("com.walton.filebrowser.ui.common.GoldenLeftEyeActivity.exit");
            context.sendBroadcast(intent);
        }
    }

    public void handleMidClick(int position) {
        String opt = Tools.getPlaySettingOpt(ITEM_3D, viewId);
        String opt2 = Tools.getPlaySettingOpt(ITEM_SUBTITLE, viewId);
        String opt6 = Tools.getPlaySettingOpt(ITEM_ROTATE_MODE, viewId);
        if (position == ITEM_3D && context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            dismiss();
            if (threeDSettingDialog == null) {
                threeDSettingDialog = new ThreeDSettingDialog(context,VideoPlaySettingDialog.this);
            }
            threeDSettingDialog.show();
        } else if (position == ITEM_SUBTITLE && !context.getString(R.string.play_setting_0_value_2).equals(opt2)) {
            dismiss();
            Message msg = new Message();
            Bundle mBundle = new Bundle();
            mBundle.putString("index", opt2);
            msg.setData(mBundle);
            msg.what = Constants.SHOW_SUBTITLE_DIALOG;
            context.getVideoHandler().sendMessage(msg);
        } else if (position == ITEM_3D && context.getString(R.string.video_3D_3dto2d).equals(opt)) {
            dismiss();
            if (threeDSettingDialog == null) {
                threeDSettingDialog = new ThreeDSettingDialog(context,VideoPlaySettingDialog.this);
            }
            threeDSettingDialog.show();
        }  else if (position == ITEM_ROTATE_MODE && context.getString(R.string.play_setting_0_value_1).equals(opt6)) {
            dismiss();
            mRotateDegreesSettingDialog = new VideoRotateSettingDialog(context, VideoPlaySettingDialog.this);
            mRotateDegreesSettingDialog.show();
        } else if (position == ITEM_SWDR) {
            dismiss();
            mPlaySettingSWDRDialog = new PlaySettingSWDRDialog(context, R.style.dialog, VideoPlaySettingDialog.this);
            mPlaySettingSWDRDialog.show();
        } else if (position == ITEM_AC4) {
            dismiss();
            mAudioAC4SettingDialog = new AudioAC4SettingDialog(context, R.style.dialog, VideoPlaySettingDialog.this);
            mAudioAC4SettingDialog.show();
        }
    }

    public void handleRightClick(int pos) {
        if (pos == ITEM_3D){
            change3DState();
        } else if (pos == ITEM_SUBTITLE) {
            changeSubtitleStateRight();
        } else if (pos == ITEM_BREAK_POINT) {
            changeBreakPointFlag();
        }  else if (pos == ITEM_THUMBNAIL_MODE) {
            changeThumbnailMode();
        } else if (pos == ITEM_ROTATE_MODE) {
            changeRotateMode();
        } else if (pos == ITEM_GOLDEN_EYE) {
            changdeGoldenLeftEyeMode();
        } else if (pos == ITEM_CONTINUOUS_PLAY) {
            changeContinuousPlayFlag();
        }
        adapter.notifyDataSetChanged();
    }

    private void setListeners() {
        playSettingListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String opt6 = Tools.getPlaySettingOpt(5, viewId);
                if (position == ITEM_BREAK_POINT){
                    changeBreakPointFlag();
                    adapter.notifyDataSetChanged();
                } else if (position == ITEM_AUDIO_TRACK) {
                    dismiss();
                    playSettingAudioTrackDialog = new PlaySettingAudioTrackDialog(
                            context, R.style.dialog, VideoPlaySettingDialog.this);
                    playSettingAudioTrackDialog.show();
                } else if (position == ITEM_THUMBNAIL_MODE) {
                    changeThumbnailMode();
                    adapter.notifyDataSetChanged();
                }  else if (position == ITEM_ROTATE_MODE && context.getString(R.string.play_setting_0_value_1).equals(opt6)) {
                    dismiss();
                    mRotateDegreesSettingDialog = new VideoRotateSettingDialog(context, VideoPlaySettingDialog.this);
                    mRotateDegreesSettingDialog.show();
                } else if (position == ITEM_GOLDEN_EYE){
                    changdeGoldenLeftEyeMode();
                    adapter.notifyDataSetChanged();
                } else if (position == ITEM_OPEN_HDR) {
                    if (Tools.unSupportTVApi()
                        || false == TvCommonManager.getInstance().isSupportModule(MODULE_OPEN_HDR)) {
                        showToastTip(context.getResources().getString(R.string.can_not_open_HDR_this_platform));
                        return;
                    }
                    if (context.getVideoPlayHolder().getDualVideoMode() ) {
                        showToastTip(context.getResources()
                                .getString(R.string.can_not_open_HDR_in_dualdecode_mode));
                        return;
                    }
                    if (context.isThreeDMode()&& !context.is2DTo3DMode() ) {
                        showToastTip(context.getResources()
                                .getString(R.string.can_not_open_HDR_in_3d_mode_except_2DTo3D));
                        return;
                    }
                    dismiss();
                    mHDRSettingDialog = new HDRSettingDialog(context,VideoPlaySettingDialog.this);
                    mHDRSettingDialog.show();

                } else if (position == ITEM_DOLBY_HDR) {
                    if (Tools.unSupportTVApi()
                        || false == TvCommonManager.getInstance().isSupportModule(MODULE_DOLBY_HDR)) {
                        showToastTip(context.getResources().getString(R.string.can_not_open_Dolby_HDR_this_platform));
                        return;
                    }
                    if (context.getVideoPlayHolder().getDualVideoMode() ) {
                        showToastTip(context.getResources()
                                .getString(R.string.can_not_open_DolbyHDR_in_dualdecode_mode));
                        return;
                    }
                    if (context.isThreeDMode()&& !context.is2DTo3DMode() ) {
                        showToastTip(context.getResources()
                                .getString(R.string.can_not_open_DolbyHDR_in_3d_mode_except_2DTo3D));
                        return;
                    }
                    dismiss();
                    mDolbyHDRSettingDialog = new DolbyHDRSettingDialog(context,VideoPlaySettingDialog.this);
                    mDolbyHDRSettingDialog.show();

                }  else if (position == ITEM_CONTINUOUS_PLAY) {
                    changeContinuousPlayFlag();
                }
            }
        });
        playSettingListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN: {
                int position = playSettingListView.getSelectedItemPosition();
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.e(TAG, "****KEYCODE_DPAD_LEFT*******position:" + position);
                    switch (position) {
                    case ITEM_3D:
                        change3DState();
                        break;
                    case ITEM_SUBTITLE:
                        changeSubtitleStateLeft();
                        break;
                    case ITEM_BREAK_POINT:
                        changeBreakPointFlag();
                        break;
                    case ITEM_THUMBNAIL_MODE:
                        changeThumbnailMode();
                        break;
                    case ITEM_ROTATE_MODE:
                        changeRotateMode();
                        break;
                    case ITEM_GOLDEN_EYE:
                        changdeGoldenLeftEyeMode();
                        break;
                    case ITEM_CONTINUOUS_PLAY:
                        changeContinuousPlayFlag();
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.e(TAG, "****KEYCODE_DPAD_RIGHT*******position:" + position);
                    switch (position) {
                    case ITEM_3D:
                        change3DState();
                        break;
                    case ITEM_SUBTITLE:
                        changeSubtitleStateRight();
                        break;
                    case ITEM_BREAK_POINT:
                        changeBreakPointFlag();
                        break;
                    case ITEM_THUMBNAIL_MODE:
                        changeThumbnailMode();
                        break;
                    case ITEM_ROTATE_MODE:
                        changeRotateMode();
                        break;
                    case ITEM_GOLDEN_EYE:
                        changdeGoldenLeftEyeMode();
                        break;
                    case ITEM_CONTINUOUS_PLAY:
                        changeContinuousPlayFlag();
                        break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                 case KeyEvent.KEYCODE_DPAD_CENTER:
                 case KeyEvent.KEYCODE_ENTER:
                    handleMidClick(position);
                    break;
                }
            }
            case KeyEvent.ACTION_UP: {
                break;
            }
            }
            return false;
        }
    };

    private void change3DState() {
        if (Tools.unSupportTVApi()) {
            return;
        }

        final TvS3DManager s3dSkin = TvS3DManager.getInstance();
        if (s3dSkin != null) {
            String opt = Tools.getPlaySettingOpt(ITEM_3D, viewId);
            if (mIsSTB) {
                TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
                if (mTvPipPopManager.isPipModeEnabled()) {
                    showToastTip(context.getResources().getString(R.string.can_not_open_3D_pip));
                    return;
                }
                if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
                    Tools.setPlaySettingOpt(ITEM_3D,context.getString(R.string.video_3D_3dto2d), viewId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int twoDformat = TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE;
                            Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                            s3dSkin.set3DTo2DDisplayMode(twoDformat);
                        }
                    }).start();
                } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                    if (!context.getVideoPlayHolder().getDualVideoMode()) {
                        // once open hdr , only can open 2DTo3D type
                        if (Tools.is3DTVPlugedIn() || context.isHRDMode() || context.isDolbyHDRMode()) {
                            Tools.setPlaySettingOpt(ITEM_3D,context.getString(R.string.play_setting_0_value_1),viewId);
                        } else {
                            Tools.setPlaySettingOpt(ITEM_3D,context.getString(R.string.video_3D_3dto2d), viewId);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!Tools.is3DTVPlugedIn()) {
                                    if (s3dSkin != null) {
                                        int twoDformat = TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE;
                                        Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                                        s3dSkin.set3DTo2DDisplayMode(twoDformat);
                                    }
                                } else {
                                    context.mDisplayFormat = Tools.getCurrent3dFormat();
                                    if (context.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                                        context.mDisplayFormat = TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE;
                                    }
                                    Log.i(TAG, "setDisplayFormat displayFormat:" + context.mDisplayFormat);
                                    if (context.isHRDMode() || context.isDolbyHDRMode()) {
                                        if (context.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D) {
                                            s3dSkin.set3dDisplayFormat(context.mDisplayFormat);
                                        }

                                    } else {
                                        s3dSkin.set3dDisplayFormat(context.mDisplayFormat);
                                    }

                                }
                            }
                        }).start();
                    } else {
                        showToastTip(context.getResources().getString(R.string.can_not_open_3D));
                    }
                } else if (context.getString(R.string.video_3D_3dto2d).equals(opt)) {
                    Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_2), viewId);
                   new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "setDisplayFormat E_ThreeD_Video_DISPLAYFORMAT_NONE");
                            s3dSkin.set3DTo2DDisplayMode(0);
                        }
                   }).start();
                }
            } else {
                if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
                    Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_2), viewId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        s3dSkin.set3dDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                        }
                    }).start();
                } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                    if (!context.getVideoPlayHolder().getDualVideoMode()) {
                        Tools.setPlaySettingOpt(ITEM_3D, context.getString(R.string.play_setting_0_value_1),viewId);
                        context.mDisplayFormat = Tools.getCurrent3dFormat();
                        Log.i(TAG, "context.displayFormat:" + context.mDisplayFormat);
                        if (context.isHRDMode()||context.isDolbyHDRMode()) {
                            return;
                        }
                        if (this.context.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                            this.context.mDisplayFormat = TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (context.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO) {
                                    Log.i(TAG, "TvS3DManager autoDetect3DFormat " + mAutoDetect3DFromatTimes + " times");
                                    // dectectSucess = s3dSkin.autoDetect3DFormat(mAutoDetect3DFromatTimes);
                                    dectectSucess = ThreeDimentionControl.getInstance().autoDetect3DFormat(mAutoDetect3DFromatTimes);
                                    Log.i(TAG, "TvS3DManager autoDetect3D result: " + dectectSucess);
                                    if(!dectectSucess) {
                                        Message msg = handler.obtainMessage(SHOW_TOAST);
                                        msg.obj = context.getResources().getString(R.string.video_3D_auto_detect_info);
                                        handler.sendMessage(msg);
                                    }
                                } else {
                                    s3dSkin.set3dDisplayFormat(context.mDisplayFormat);
                                }
                            }
                        }).start();
                    }else{
                        showToastTip(context.getResources().getString(R.string.can_not_open_3D));
                    }
                }
            }
        }
    }

    public void changeSubtitleStateRight() {
        String opt = Tools.getPlaySettingOpt(ITEM_SUBTITLE, viewId);
        if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            context.showSubtitleView();
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.subtitle_setting_0_value_1), viewId);
            openInnerSubtitle(true);
        } else if (context.getString(R.string.subtitle_setting_0_value_1).equals(opt)) {
            context.showSubtitleView();
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.subtitle_setting_0_value_2), viewId);
            closeInnerSubtitle();
        } else if (context.getString(R.string.subtitle_setting_0_value_2).equals(opt)) {
            context.hideSubtitleView();
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.play_setting_0_value_2), viewId);
            closeExternalSubtitle();
            SubtitleManager.mSelectedSubTrackLast = 0;
        }
    }
    private void openInnerSubtitle(boolean right) {
            // android native api
            if (Tools.getSubtitleAPI()) {
                openAnNativeInnerSubtitle(right);
            // mstar api
            } else {
                mSubtitleManager.setSubtitleDataSource(mMMediaPlayer, null);
                onInnerSubtitleTrack(right);
            }
    }

    private void closePreviousSubTrack () {
        Log.i(TAG,"closePreviousSubTrack");
        /*
        if (Build.VERSION.SDK_INT >= 21) {
            int selectTrackText = ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                               .getMMediaPlayer().getSelectedTrack(Constants.MEDIA_TRACK_TYPE_TIMEDTEXT);
            int selectTrackBit = ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                               .getMMediaPlayer().getSelectedTrack(Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP);
            if (selectTrackText >0) {
                ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                        .deselectTrack(selectTrackText);
            } else if (selectTrackBit >0) {
                ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                        .deselectTrack(selectTrackBit);
            }
        } else {*/
            if ( SubtitleManager.mSelectedSubTrackLast>0 ) {
                try {
                    Log.i(TAG,"mSelectedSubTrackLast:"+ String.valueOf(SubtitleManager.mSelectedSubTrackLast));
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView()
                          .deselectTrack(SubtitleManager.mSelectedSubTrackLast);
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
        //}

    }
    private void closeInnerSubtitle() {

        // android native api
        if (Tools.getSubtitleAPI()) {
            closePreviousSubTrack();
        // mstar api
        } else {
            ((VideoPlayerActivity) context).clearSTProperty();
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer);
        }

    }
    private void closeExternalSubtitle() {

        // android native api
        if (Tools.getSubtitleAPI()) {
            closePreviousSubTrack();
        // mstar api
        } else {
            mSubtitleManager.setSubtitleDataSource(mMMediaPlayer, null);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer);
        }

    }
    private void changeSubtitleStateLeft() {
        String opt = Tools.getPlaySettingOpt(ITEM_SUBTITLE, viewId);
        if (context.getString(R.string.subtitle_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.play_setting_0_value_2), viewId);
            context.hideSubtitleView();
            closeInnerSubtitle();
            SubtitleManager.mSelectedSubTrackLast = 0;
        } else if (context.getString(R.string.subtitle_setting_0_value_2).equals(opt)) {
            context.showSubtitleView();
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.subtitle_setting_0_value_1), viewId);
            openInnerSubtitle(false);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            context.showSubtitleView();
            Tools.setPlaySettingOpt(ITEM_SUBTITLE, context.getString(R.string.subtitle_setting_0_value_2), viewId);
            closeInnerSubtitle();
        }
    }
    private void openAnNativeInnerSubtitle(boolean right) {
        TrackInfo[]trackInfo = null;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            trackInfo = ((VideoPlayerActivity) context).getVideoPlayHolder()
                            .getPlayerView().getTrackInfo();
        }
        if (trackInfo != null) {
            // Display the film contains all the subtitles number
            if (trackInfo != null) {
                for (int i=0;i<trackInfo.length;i++) {
                     int type = trackInfo[i].getTrackType();
                     Log.i(TAG,"InnerSub track type:"+ String.valueOf(type));
                     if(type == Constants.MEDIA_TRACK_TYPE_TIMEDTEXT
                        || type == Constants.MEDIA_TRACK_TYPE_TIMEDBITMAP) {
                        if(SubtitleManager.mExtSubtitleBase > 0 && i>=SubtitleManager.mExtSubtitleBase) {
                            Log.i(TAG,"no innerSubtitle but has externalSbtitle");
                            break;
                        }
                        if (SubtitleManager.mVideoSubtitleNo == 0) {
                            SubtitleManager.mVideoSubtitleNo = trackInfo.length - i;
                            SubtitleManager.mInnerSubtitleBase = i;
                            Log.i(TAG,"mInnerSubtitleBase:"+ String.valueOf(SubtitleManager.mInnerSubtitleBase));
                            Log.i(TAG,"mVideoSubtitleNo:"+ String.valueOf(SubtitleManager.mVideoSubtitleNo));

                        }
                        ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().selectTrack(i);
                        SubtitleManager.mSelectedSubTrackLast = i;
                        if (right) {
                            SubtitleManager.setSubtitleSettingOpt(ITEM_BREAK_POINT, context.getString(R.string.subtitle_2_value_2) + "0", viewId);
                        }
                        break;
                    }
                }
           }
        }
    }

    private void onInnerSubtitleTrack(boolean right) {
        Log.i(TAG,"onInnerSubtitleTrack");
        SubtitleTrackInfo subtitleAllTrackInfo = null ;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            subtitleAllTrackInfo = SubtitleManager.getInstance().getAllSubtitleTrackInfo(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer());
        }
        if (subtitleAllTrackInfo != null) {
            // Display the film contains all the subtitles number

            SubtitleManager.mVideoSubtitleNo = subtitleAllTrackInfo.getAllInternalSubtitleCount();
            Log.i(TAG,"mVideoSubtitleNo:"+ String.valueOf(SubtitleManager.mVideoSubtitleNo));
            Log.i(TAG,"subtitlePosition:"+ String.valueOf(((VideoPlayerActivity) context).playSettingSubtitleDialogOne.subtitlePosition));
            if (SubtitleManager.mVideoSubtitleNo > 0) {
                mSubtitleManager.onSubtitleTrack(mMMediaPlayer);
                mSubtitleManager.setSubtitleTrack(mMMediaPlayer, 0);
                if (right) {
                    String tmpPos = "0";
                    if (Constants.bSupportDivx) {
                        SubtitleManager.initSubtitleSettingOpt(context,viewId);
                        int tmpSubtitleTrack = ((VideoPlayerActivity) context).playSettingSubtitleDialogOne.subtitlePosition;
                        tmpPos = String.valueOf(tmpSubtitleTrack);
                        ((VideoPlayerActivity) context).SaveSubtitleTrackByFileName(tmpSubtitleTrack);
                    }
                    Log.i(TAG,"tmpPos:"+tmpPos);
                    SubtitleManager.initSubtitleSettingOpt(context,viewId);
                    SubtitleManager.setSubtitleSettingOpt(2, context.getString(R.string.subtitle_2_value_2) + tmpPos, viewId);
                }
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

    private void changeBreakPointFlag(){
        String opt = Tools.getPlaySettingOpt(ITEM_BREAK_POINT, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_BREAK_POINT, context.getString(R.string.play_setting_0_value_2), viewId);
            BreakPointRecord.setBreakPointFlag(context, false);
            context.setBreakPointFlag(false);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_BREAK_POINT, context.getString(R.string.play_setting_0_value_1), viewId);
            BreakPointRecord.setBreakPointFlag(context, true);
            context.setBreakPointFlag(true);
        }
    }

    private void changeContinuousPlayFlag(){
        String opt = Tools.getPlaySettingOpt(ITEM_CONTINUOUS_PLAY, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_CONTINUOUS_PLAY, context.getString(R.string.play_setting_0_value_2), viewId);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_CONTINUOUS_PLAY, context.getString(R.string.play_setting_0_value_1), viewId);
        }
        context.setmIsNeedContinuousPlay();
    }

    private void changeThumbnailMode() {
        Log.i(TAG, "---------changeThumbnailMode----------- ");
        if (!Tools.isThumbnailModeOn()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_this_platform));
            return;
        }
        if (!Tools.isSupportDualDecode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_this_platform));
            return;
        }
        if (context.isVideoSize_4K(1)) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_4kVideo));
            return;
        }
        if (context.isThreeDMode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_3DMode));
            return;
        }

        String opt = Tools.getPlaySettingOpt(ITEM_THUMBNAIL_MODE, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_THUMBNAIL_MODE, context.getString(R.string.play_setting_0_value_2), viewId);
            Tools.setThumbnailMode("0");
            context.getThumbnailController().releaseThumbnailView(true);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            if (context.getVideoPlayHolder().getDualVideoMode()) {
                showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_dual_Video));
            } else {
                Tools.setPlaySettingOpt(ITEM_THUMBNAIL_MODE,context.getString(R.string.play_setting_0_value_1), viewId);
                Tools.setThumbnailMode("1");
                context.getThumbnailController().initThumbnailView();
            }
        }
    }

    private void changeRotateMode() {
        Log.i(TAG, "-----changeRotateMode------");
        if (context.getVideoPlayHolder().getDualVideoMode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_rotate_dual_mode));
            return;
        }
        String opt = Tools.getPlaySettingOpt(ITEM_ROTATE_MODE, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(ITEM_ROTATE_MODE, context.getString(R.string.play_setting_0_value_2), viewId);
            Tools.setRotateMode(false);
            context.setVideoDisplayFullScreen(context.getVideoPlayHolder().getViewId());
            if (Tools.getRotateDegrees()!=0) {
                Tools.setRotateDegrees("0");
                context.imageRotate(context.getVideoPlayHolder().getViewId(), 0);
            }
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setRotateMode(true);
            Tools.setPlaySettingOpt(ITEM_ROTATE_MODE, context.getString(R.string.play_setting_0_value_1), viewId);
        }
    }

    private void showToastTip(String strMessage) {
        Toast toast = Toast.makeText(context, strMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
