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

package com.walton.filebrowser.ui.photo;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.android.tv.TvS3DManager;
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Tools;

import java.util.ArrayList;

public class ImagePlayerThreeDSetting extends Dialog {
    private static final String TAG = "ImagePlayerThreeDSetting";
    private static final int KEYCODE_ZOOM = 253;
    private static final int KEYCODE_ZOOM_TWO = 255;
    private static final int REFRESH_THREED_SETTING_ITEM_VIEW = 1;
    private ImagePlayerActivity mContext;

    public ViewHolder holder;

    private ArrayList<String> mItemName = new ArrayList<String>();

    private MyAdapter mAdapter;

    private ListView video3DSettingList;

    private int mSelectedPosition = 0;

    private ArrayList<Integer> threeDFormat = new ArrayList<Integer>();

    private boolean mIsSTB = false;

    public ImagePlayerThreeDSetting(ImagePlayerActivity context) {
        super(context);
        this.mContext = context;
        refreshThreeDSettingDialogItemView();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_THREED_SETTING_ITEM_VIEW:
                    Log.i(TAG, "the selected item is:" + mSelectedPosition);
                    if (video3DSettingList != null) {
                        video3DSettingList.setSelection(mSelectedPosition);
                    }
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                default :
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.video_three_d_setting_dialog);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        mIsSTB = Tools.isMstarSTB();
        int width = (int) (display.getWidth() * 0.2);
        int height = (int) (display.getHeight() * 0.6);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        w.setBackgroundDrawableResource(color.transparent);
        initView();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onStart() {
        Log.i(TAG, "-------- onStart --------");
        refreshThreeDSettingDialogItemView();
    }

    @SuppressLint("LongLogTag")
    private void initView() {
        Log.i(TAG, "------initView ------ isMstarSTB:" + Tools.isMstarSTB());
        if (!Tools.isMstarSTB()) {
            // Not STB
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_none));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_side_by_side));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_top_bottom));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_frame_packing));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_line_alternative));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_2dto3d));
            // Currently some photo do 3D auto detect will be failed, so Comment out it.Mantis:640550
            // mItemName.add(this.mContext.getResources().getString(R.string.video_3D_auto));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_check_board));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_pixel_alternative));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_frame_alternative));
            if (!Tools.unSupportTVApi()) {
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D);
                // Currently some photo do 3D auto detect will be failed, so Comment out it.Mantis:640550
                // threeDFormat.add(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_CHECK_BOARD);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_PIXEL_ALTERNATIVE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_ALTERNATIVE);
            }
        } else {
            // STB
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_none));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_side_by_side));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_top_bottom));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_frame_packing));
            mItemName.add(this.mContext.getResources().getString(R.string.video_3D_line_alternative));
            if (!Tools.unSupportTVApi()) {
                boolean is3DTVPlugedIn = Tools.is3DTVPlugedIn();
                if (is3DTVPlugedIn) {
                    // 3D TV
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE);
                } else {
                    // 2D TV
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_TOP_BOTTOM);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_FRAME_PACKING);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_LINE_ALTERNATIVE);
                }

            }
        }

        video3DSettingList = (ListView) findViewById(R.id.video3DList);
        video3DSettingList.setMinimumHeight(300);
        mAdapter = new MyAdapter(mContext);
        video3DSettingList.setAdapter(mAdapter);
        if (!Tools.unSupportTVApi()) {
            int size = threeDFormat.size();
            boolean is3DTVPlugedIn;
            for (int i = 0; i < size; i++) {
                if (Tools.isMstarSTB()) {
                    // is3DTVPlugedIn is only for STB platform
                    is3DTVPlugedIn = Tools.is3DTVPlugedIn();
                    if (is3DTVPlugedIn) {
                        // 3D TV
                        if (this.mContext.mDisplayFormat == threeDFormat.get(i)) {
                            mSelectedPosition = i;
                        }
                    } else {
                        // 2D TV
                        if (this.mContext.mThreeDVideo3DTo2D == threeDFormat.get(i)) {
                            mSelectedPosition = i;
                        }
                    }
                } else {
                    // not STB
                    if (this.mContext.mDisplayFormat == threeDFormat.get(i)) {
                        mSelectedPosition = i;
                    }
                }

            }
            Log.i(TAG, "the selected item is:" + mSelectedPosition);
            video3DSettingList.setSelection(mSelectedPosition);
        }
        mAdapter.notifyDataSetChanged();
        video3DSettingList.setOnItemClickListener(new ListItemOnClickListener());
    }

    public void  refreshThreeDSettingDialogItemView() {
        Log.i(TAG,"refreshThreeDSettingDialogItemView");
        if (!Tools.unSupportTVApi()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Tools.isMstarSTB()) {
                        // STB
                        boolean is3DTVPlugedIn = Tools.is3DTVPlugedIn();
                        if (is3DTVPlugedIn) {
                            // 3D TV
                            mContext.mDisplayFormat = Tools.getCurrent3dFormat();
                            // Currently some photo do 3D auto detect will be failed, so Comment out it.Mantis:640550
                            if (mContext.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO) {
                                mContext.mDisplayFormat = TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE;
                            }
                        } else {
                            // 2D TV
                            mContext.mThreeDVideo3DTo2D = Tools.getCurrent3dFormatOnSTB2DTV();
                        }
                    } else {
                        mContext.mDisplayFormat = Tools.getCurrent3dFormat();
                        // Currently some photo do 3D auto detect will be failed, so Comment out it.Mantis:640550
                        if (mContext.mDisplayFormat == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO) {
                            mContext.mDisplayFormat = TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE;
                        }
                    }
                    int size = threeDFormat.size();
                    for (int i = 0; i < size; i++) {
                        if (Tools.isMstarSTB()) {
                            // is3DTVPlugedIn is only for STB platform
                            boolean is3DTVPlugedIn = Tools.is3DTVPlugedIn();
                            if (is3DTVPlugedIn) {
                                // 3D TV
                                if (mContext.mDisplayFormat == threeDFormat.get(i)) {
                                    mSelectedPosition = i;
                                }
                            } else {
                                // 2D TV
                                if (mContext.mThreeDVideo3DTo2D == threeDFormat.get(i)) {
                                    mSelectedPosition = i;
                                }
                            }
                        } else {
                            // not STB
                            if (mContext.mDisplayFormat == threeDFormat.get(i)) {
                                mSelectedPosition = i;
                            }
                        }
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(REFRESH_THREED_SETTING_ITEM_VIEW);
                    }
                }
            }).start();
        }
    }

    private void set3DMode(int position) {
        if (Tools.unSupportTVApi()) {
            return;
        }
        if (Tools.isMstarSTB()) {
            // STB
            boolean is3DTVPlugedIn = Tools.is3DTVPlugedIn();
            if (is3DTVPlugedIn) {
                // 3D TV
                this.mContext.mDisplayFormat = threeDFormat.get(position);
            } else {
                // 2D TV
                this.mContext.mThreeDVideo3DTo2D = threeDFormat.get(position);
            }
            Tools.setVideoMute(true, 50);
            if (is3DTVPlugedIn) {
                // 3D TV
                Tools.setDisplayFormat(this.mContext.mDisplayFormat);
            } else {
                // 2D TV
                Tools.set3DTo2D(this.mContext.mThreeDVideo3DTo2D);
            }
            Tools.setVideoMute(false, 0);
        } else {
            this.mContext.mDisplayFormat = threeDFormat.get(position);
            Tools.setVideoMute(true, 50);
            Tools.setDisplayFormat(this.mContext.mDisplayFormat);
            Tools.setVideoMute(false, 0);
        }
    }

    class ListItemOnClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            // If in MI platform we should notify user and return.
            if (Tools.unSupportTVApi()) {
                mContext.showToastAtCenter(mContext.getResources().getString(R.string.unsupport_3D_in_this_platform));
                return;
            }

            // If current 3D format equals change format we should return.
            if (Tools.getCurrent3dFormat() == threeDFormat.get(position)) {
                return;
            }

            if (mContext.getmHasPrevious3DOperationFinished()) {
                mContext.setmHasPrevious3DOperationFinished(false);
            } else {
                // previous 3D opreration has not finished.
                mContext.showToastAtCenter(mContext.getResources().getString(R.string.busy_tip));
                return;
            }
            mSelectedPosition = position;
            mAdapter.notifyDataSetChanged();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"mSelectedPosition:"+mSelectedPosition);
                    set3DMode(mSelectedPosition);
                    mContext.setmHasPrevious3DOperationFinished(true);
                }
            }).start();
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemName.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.video_three_d_item, null);
                holder.fileNameText = (TextView) convertView.findViewById(R.id.ThreeDItemName);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.threeDCheckBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fileNameText.setText(mItemName.get(position));
            if (mSelectedPosition == position) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView fileNameText;

        CheckBox checkBox;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "------onKeyDown ---- keyCode:" + keyCode);
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        if (KeyEvent.KEYCODE_TV_INPUT == keyCode) {
            this.dismiss();
        }

        // an4.4's zoom key
        if (keyCode == KEYCODE_ZOOM && Tools.getSdkVersion()<21) {
            return true;
        }
        // an5.1 and 6.0's zoom key
        if (keyCode == KEYCODE_ZOOM_TWO && Tools.getSdkVersion()>=21) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "------onKeyUp ---- keyCode:" + keyCode);
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }

        // an4.4's zoom key
        if (keyCode == KEYCODE_ZOOM && Tools.getSdkVersion()<21) {
            return true;
        }
        // an5.1 and 6.0's zoom key
        if (keyCode == KEYCODE_ZOOM_TWO && Tools.getSdkVersion()>=21) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
