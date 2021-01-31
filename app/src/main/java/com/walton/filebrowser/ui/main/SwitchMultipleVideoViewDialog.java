//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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
package com.walton.filebrowser.ui.main;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.walton.filebrowser.R;

import com.walton.filebrowser.ui.multiplayback.MultiPlayerActivity;
import com.walton.filebrowser.util.Tools;


//by andrew.wang


public class SwitchMultipleVideoViewDialog extends Dialog {

    public static final String TAG = "SwitchMultipleVideoViewDialog";
    private FileBrowserActivity mContext = null;
    private ListView mListView =null;
    private String mPlatform;
    private Resources mResources;

    public SwitchMultipleVideoViewDialog(FileBrowserActivity context) {
        super(context, R.style.dialog);
        mContext = context;
        mResources = mContext.getResources();
        mPlatform = Tools.getHardwareName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmode_dialog);
        setWindowsAttribute();
        initView();
    }
    private void initView() {
        Log.i(TAG,"createPopWindow");
        mListView = (ListView) this.findViewById(R.id.lv_pop);
        //mViewModeListView.setDivider(null);
        mListView.setSelector(R.drawable.button_selected);
        mListView.setVerticalScrollBarEnabled(true);
        mListView.setPadding(3, 3, 3, 3);
        List<String> contents = new ArrayList<String>(2);
        contents.add(mResources.getString(R.string.four_video_play_in_gridview));
        contents.add(mResources.getString(R.string.nine_video_play_in_gridview));
        //for test
        /*contents.add("for test: 16 video paly");
        contents.add("for test: 25 video paly");
        contents.add("for test: 36 video paly");
        contents.add("for test: 100 video paly");*/
        mListView.setAdapter(new MySimpleAdapter(contents));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.d(TAG, "listview onItemClick :"+position);
                //only open in Maserati platform
                if(!Tools.isSupportMWPlayBack()){
                    Toast.makeText(mContext, mResources.getString(R.string.not_support_multiplayback), 0).show();
                    return;
                }else{
                    Intent intent = new Intent(mContext, MultiPlayerActivity.class);
                    switch (position) {
                    case 0:
                        intent.putExtra("videoNum", 4);
                        break;
                    case 1:
                        intent.putExtra("videoNum", 9);
                        break;
                    case 2:
                        intent.putExtra("videoNum", 16);
                        break;
                    case 3:
                        intent.putExtra("videoNum", 25);
                        break;
                    case 4:
                        intent.putExtra("videoNum", 36);
                        break;
                    case 5:
                        intent.putExtra("videoNum", 100);
                        break;
                    default:
                        break;
                    }
                    mContext.startActivity(intent);
                    SwitchMultipleVideoViewDialog.this.cancel();
                }

            }
        });
    }

    private void setWindowsAttribute() {
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = 400;  //(int) (display.getWidth() * 0.204);
        int height = 200; //(int) (display.getHeight() * 0.220);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
    }

    class MySimpleAdapter extends BaseAdapter{
        private List<View> mCachedViews;
        public MySimpleAdapter(List<String> contents){
            mCachedViews = new ArrayList<View>(contents.size());
            for (String content : contents) {
                TextView textView = new TextView(mContext);
                textView.setText(content);
                textView.setWidth(192);
                textView.setHeight(96);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                textView.setTextColor(Color.WHITE);
                textView.setFocusable(false);
                textView.setBackgroundResource(R.drawable.text_shape);
                mCachedViews.add(textView);
            }
        }

        @Override
        public int getCount() {
            return mCachedViews.size();
        }

        @Override
        public Object getItem(int position) {
            return mCachedViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCachedViews.get(position);
        }
    }
}

