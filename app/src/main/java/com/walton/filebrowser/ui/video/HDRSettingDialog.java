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
import android.widget.Toast;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.util.Constants;

import android.util.Log;


public class HDRSettingDialog extends Dialog {
    private static final String TAG = "HDRSettingDialog";
    private VideoPlayerActivity context;
    private VideoPlaySettingDialog videoPlaySettingDialog;
    public ViewHolder holder;
    private ArrayList<String> mItemName = new ArrayList<String>();
    private MyAdapter mAdapter;
    private ListView mDolbyHDRSettingList;
    private int selectPosition;
    private int mViewID = 1;
    private int mItemNum = 0;

    public HDRSettingDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public HDRSettingDialog(VideoPlayerActivity context,
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
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.video_three_d_setting_dialog);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.25);
        int height = (int) (display.getHeight() * 0.50);
        w.setLayout(width, height);
        w.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 80;
        w.setAttributes(wl);
        // w.setBackgroundDrawableResource(color.transparent);
        initView();
    }

    private void initView() {
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_none));
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_auto));
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_low));
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_middle));
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_high));
        mItemName.add(this.context.getResources().getString(R.string.video_HDR_ref_mode));
        mItemNum = mItemName.size();
        mDolbyHDRSettingList = (ListView) findViewById(R.id.video3DList);
        mDolbyHDRSettingList.setMinimumHeight(300);
        mAdapter = new MyAdapter(context);
        mDolbyHDRSettingList.setAdapter(mAdapter);
        getPresentHDRChoice();
        mAdapter.notifyDataSetChanged();
        mDolbyHDRSettingList.setOnItemClickListener(new ListItemOnClickListener());
    }
    private void getPresentHDRChoice(){
        Log.i(TAG,"getPresentHDRChoice");
        int result[] = new int[2];
        result[0] = Constants.ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED;
        result[1] = Constants.HDR_NOT_IS_RUNNING;
        result = Tools.getHdrAttributes(Constants.HDR_OPEN_ATTRIBUTES,Constants.VIDEO_MAIN_WINDOW);
        if (result[0] == Constants.ERROR_CODE_NOT_SUPPORT_OR_UNDEFINED) {
            selectPosition = 0;
            mDolbyHDRSettingList.setSelection(0);
            showToast("HDR is not supportted or not defined");
        } else {
            if (result[0] >= mItemNum) {
                result[0] = 0;
                setPresentHDRChoice(result[0]);
            }
            selectPosition = result[0];
            mDolbyHDRSettingList.setSelection(result[0]);
        }
    }
    private void setPresentHDRChoice(int position){
        Log.i(TAG,"setPresentHDRChoice");
        selectPosition = position;
        boolean setHdrAttributesResult = false;
        setHdrAttributesResult = Tools.setHdrAttributes(Constants.HDR_OPEN_ATTRIBUTES,Constants.VIDEO_MAIN_WINDOW,position);
        if (setHdrAttributesResult)
            showToast("Succeeded");
        else
            showToast("Failed");
    }

    private void showToast(String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.show();
    }
    class ListItemOnClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                long arg3) {
            setPresentHDRChoice(position);
            mAdapter.notifyDataSetChanged();
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
                mDolbyHDRSettingList.setSelection(selectPosition);
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

