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

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.tv.TvS3DManager;
import android.util.Log;


public class ThreeDSettingDialog extends Dialog {
    private static final String TAG = "ThreeDSettingDialog";
    private VideoPlayerActivity context;
    private VideoPlaySettingDialog videoPlaySettingDialog;
    public ViewHolder holder;
    private ArrayList<String> mItemName = new ArrayList<String>();
    private MyAdapter mAdapter;
    private ListView video3DSettingList;
    private int selectPosition;
    private int mViewID = 1;
    private boolean mIsSTB = false;

    private ArrayList<Integer> threeDFormat = new ArrayList<Integer>();

    public ThreeDSettingDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public ThreeDSettingDialog(VideoPlayerActivity context,
            VideoPlaySettingDialog videoPlaySettingDialog) {
        super(context, R.style.dialog);
        this.context = context;
        selectPosition = 0;
        mViewID = context.getVideoPlayHolder().getViewId();
        this.videoPlaySettingDialog = videoPlaySettingDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlaySettingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "-----------onCreate--------");
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
        // w.setBackgroundDrawableResource(color.transparent);
        initView();
    }

    private void initView() {
        Log.i(TAG, "--------- initView ---------");
        if (!Tools.unSupportTVApi()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    context.mDisplayFormat = Tools.getCurrent3dFormat();
                }
            }).start();
        }

        int size = threeDFormat.size();

        if (mIsSTB) {
            mItemName.add(this.context.getResources().getString(R.string.video_3D_side_by_side));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_top_bottom));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_frame_packing));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_line_alternative));
            // G_HQ_Monaco_AN_Raptors Project need these
            if ("monaco".equalsIgnoreCase(Tools.getHardwareName())) {
                mItemName.add(this.context.getResources().getString(R.string.video_3D_2dto3d));
                mItemName.add(this.context.getResources().getString(R.string.video_3D_auto));
            }
            boolean is3DTVPlugedIn = Tools.is3DTVPlugedIn();
            String opt = Tools.getPlaySettingOpt(0, mViewID);
            if (!Tools.unSupportTVApi()) {
                if (is3DTVPlugedIn) {
                    if (context.getString(R.string.video_3D_3dto2d).equals(opt)) {
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_TOP_BOTTOM);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_FRAME_PACKING);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_LINE_ALTERNATIVE);
                    } else {
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING);
                        threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE);
                        // G_HQ_Monaco_AN_Raptors Project need these
                        if ("monaco".equalsIgnoreCase(Tools.getHardwareName())) {
                            threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D);
                            threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO);
                        }
                    }
                } else {
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SIDE_BY_SIDE);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_TOP_BOTTOM);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_FRAME_PACKING);
                    threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_LINE_ALTERNATIVE);
                }
            }
            video3DSettingList = (ListView) findViewById(R.id.video3DList);
            video3DSettingList.setMinimumHeight(300);
            mAdapter = new MyAdapter(context);
            video3DSettingList.setAdapter(mAdapter);
            if (!Tools.unSupportTVApi()) {
                if (context.getString(R.string.video_3D_3dto2d).equals(opt)){
                    for (int i = 0; i < size; i++) {
                         if (Tools.getCurrent3dFormatOnSTB2DTV() == threeDFormat.get(i)) {
                             selectPosition = i;
                             video3DSettingList.setSelection(i);
                             break;
                         }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                         if (context.mDisplayFormat == threeDFormat.get(i)) {
                             selectPosition = i;
                             video3DSettingList.setSelection(i);
                             break;
                         }
                    }
                }
            }
        } else {
            mItemName.add(this.context.getResources().getString(R.string.video_3D_side_by_side));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_top_bottom));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_frame_packing));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_line_alternative));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_2dto3d));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_auto));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_check_board));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_pixel_alternative));
            mItemName.add(this.context.getResources().getString(R.string.video_3D_frame_alternative));
            if (!Tools.unSupportTVApi()) {
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_SIDE_BY_SIDE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_TOP_BOTTOM);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_PACKING);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_LINE_ALTERNATIVE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_CHECK_BOARD);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_PIXEL_ALTERNATIVE);
                threeDFormat.add(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_FRAME_ALTERNATIVE);
            }
            video3DSettingList = (ListView) findViewById(R.id.video3DList);
            video3DSettingList.setMinimumHeight(300);
            mAdapter = new MyAdapter(context);
            video3DSettingList.setAdapter(mAdapter);
            if (!Tools.unSupportTVApi()) {
                for (int i = 0; i < size; i++) {
                    if (this.context.mDisplayFormat == threeDFormat.get(i)) {
                        selectPosition = i;
                        video3DSettingList.setSelection(i);
                         break;
                     }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        video3DSettingList.setOnItemClickListener(new ListItemOnClickListener());
        // Don't need set3DMode again, because already set3DMode when VideoPlaySettingDialog turn 3D Status Open,
        // Further question please ask 3D Owner.
        // set3DMode(selectPosition);
    }

    private void set3DMode(int position) {
        if (Tools.unSupportTVApi()) {
            return;
        }
        int twoDformat = 0;
        if (mIsSTB) {
            if (Tools.is3DTVPlugedIn()) {
                String opt = Tools.getPlaySettingOpt(0, mViewID);
                if (context.getString(R.string.video_3D_3dto2d).equals(opt)) {
                    twoDformat = position+1;
                    Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                    TvS3DManager.getInstance().set3DTo2DDisplayMode(twoDformat);
                } else {
                    context.mDisplayFormat = threeDFormat.get(position);
                    Log.i(TAG, "setDisplayFormat context.displayFormat:" + context.mDisplayFormat);
                    TvS3DManager.getInstance().set3dDisplayFormat(context.mDisplayFormat);
                }
            } else {
                twoDformat = position+1;
                Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                TvS3DManager.getInstance().set3DTo2DDisplayMode(twoDformat);
            }
        } else {
            context.mDisplayFormat = threeDFormat.get(position);
            Log.i(TAG, "setDisplayFormat context.displayFormat:" + context.mDisplayFormat);
            TvS3DManager.getInstance().set3dDisplayFormat(context.mDisplayFormat);
        }
    }

    class ListItemOnClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                long arg3) {
            if (context.isHRDMode() || context.isDolbyHDRMode()) {
                if (threeDFormat.get(position)!=TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D) {
                    context.showToastTip(context.getResources()
                        .getString(R.string.can_not_open_3D_in_HDR_or_dolbyHDR_mode_except_2DTo3D));
                    return;
                }
            }
            selectPosition = position;
            mAdapter.notifyDataSetChanged();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    set3DMode(selectPosition);
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
            if (selectPosition == position) {
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
