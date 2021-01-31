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
import android.util.DisplayMetrics;
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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.os.SystemProperties;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Tools;

import java.util.ArrayList;


/**
 * Created by nate.luo on 14-7-24.
 */

public class VideoRotateSettingDialog extends Dialog {
    private static final String TAG = "VideoRotateSettingDialog";
    private VideoPlayerActivity mContext;
    private VideoPlaySettingDialog videoPlaySettingDialog;
    public ViewHolder holder;
    private ArrayList<String> mVideoRotateDegresArray = new ArrayList<String>();
    private MyAdapter mAdapter;
    private ListView mVideoRotateDegressSettingList;
    private int mSelectPosition;

    public VideoRotateSettingDialog(VideoPlayerActivity context, VideoPlaySettingDialog videoPlaySettingDialog) {
        super(context,R.style.dialog);
        this.mContext = context;
        this.videoPlaySettingDialog = videoPlaySettingDialog;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlaySettingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.video_rotate_setting_dialog);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i(TAG, "metrics.density:" + metrics.density);
        int width = display.getWidth();
        int height = display.getHeight();
        if (metrics.density == 1.5) {
            width = (int) (display.getWidth() * 0.20);
            height = (int) (display.getHeight() * 0.30);
        } else if (metrics.density == 2.0) {
            width = (int) (display.getWidth() * 0.3);
            height = (int) (display.getHeight() * 0.54);
        }

        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        w.setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    private void initView() {
        final  String[] arrayDegrees = mContext.getResources().getStringArray(R.array.video_rotate_degrees);
        int length = arrayDegrees.length;
        for (int i = 0; i < length; i++) {
            mVideoRotateDegresArray.add(arrayDegrees[i]);
        }

        mVideoRotateDegressSettingList = (ListView) findViewById(R.id.videoRotateDegressList);
        mVideoRotateDegressSettingList.setMinimumHeight(300);
        mAdapter = new MyAdapter(mContext);
        mVideoRotateDegressSettingList.setAdapter(mAdapter);
        String rotateDegrees = SystemProperties.get("mstar.video.rotate.degrees", "0");
        Log.i(TAG, "rotateDegrees:" + rotateDegrees);
        int size = mVideoRotateDegresArray.size();
        for (int i = 0; i < size; i++) {
            if (mVideoRotateDegresArray.get(i).equalsIgnoreCase(rotateDegrees)) {
                mSelectPosition = i;
                mVideoRotateDegressSettingList.setSelection(i);
            }
        }

        mAdapter.notifyDataSetChanged();
        mVideoRotateDegressSettingList.setOnItemClickListener(new ListItemOnClickListener());
    }

    private void setVideoRotateDegrees(int position) {
        Log.i(TAG, "setVideoRotateDegrees position:" + position + " value:" + mVideoRotateDegresArray.get(position));
        String rotateDegrees = SystemProperties.get("mstar.video.rotate.degrees", "0");
        if (!mVideoRotateDegresArray.get(position).equals(rotateDegrees)) {
            SystemProperties.set("mstar.video.rotate.degrees", mVideoRotateDegresArray.get(position));
            /*
            if ("90".equalsIgnoreCase(mVideoRotateDegresArray.get(position)) || "270".equalsIgnoreCase(mVideoRotateDegresArray.get(position))) {
                mContext.setVideoDisplayRotate90(mContext.getVideoPlayHolder().getViewId());
            } else {
                mContext.setVideoDisplayFullScreen(mContext.getVideoPlayHolder().getViewId());
            }*/

            mContext.imageRotate(mContext.getVideoPlayHolder().getViewId(), Tools.getRotateDegrees());
        }
    }

    class ListItemOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            mSelectPosition = position;
            mAdapter.notifyDataSetChanged();
            setVideoRotateDegrees(mSelectPosition);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mVideoRotateDegresArray.size();
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
                convertView = mInflater.inflate(R.layout.video_rotate_degrees_item, null);
                holder.fileNameText = (TextView) convertView.findViewById(R.id.rotateDegreesItemName);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.rotateDegreesCheckBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fileNameText.setText(mVideoRotateDegresArray.get(position));
            if (mSelectPosition == position) {
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
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        if (KeyEvent.KEYCODE_TV_INPUT == keyCode) {
            this.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
