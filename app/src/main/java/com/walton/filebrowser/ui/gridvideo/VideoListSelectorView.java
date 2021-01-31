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
package com.walton.filebrowser.ui.gridvideo;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.ListDataListAdapter;

/**
 *
 * Custom video playlist Dialog
 *
 * @author
 */
public class VideoListSelectorView extends Dialog {
    private static final String TAG = "VideoListSelectorView";
    // video list
    private ListView videoList;
    private Activity mMWPlayerActivity;
    // ArrayList<HashMap<String, Object>> listData;
    ListDataListAdapter simpleAdapter;
    List<VideoInfo> mVideoInfoList;
    private int mPosition;
    private IVideoFileSelectedObserver mVideoFileSelectedListener;

    public VideoListSelectorView(MWPlayerActivity activity, IVideoFileSelectedObserver iVideoFileSelectedListener) {
        super(activity, R.style.dialog);
        mMWPlayerActivity = activity;
        this.mVideoFileSelectedListener= iVideoFileSelectedListener;
        Log.i(TAG, "new VideoListSelectorView ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_list);
        Log.i(TAG, " onCreate ");
        Window w = getWindow();
        // Resources resources = mVideoPlayActivity.getResources();
        w.setTitle(null);
//        int width = (int) (display.getWidth() * 0.4);
//        int height = (int) (display.getHeight() * 0.6);

        if (mMWPlayerActivity != null) {
            DisplayMetrics metrics = mMWPlayerActivity.getResources().getDisplayMetrics();
            mMWPlayerActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i(TAG, "metrics.density:" + metrics.density);
            if (metrics.density == 1.5) {
                // w.setBackgroundDrawableResource(color.transparent);
            } else if (metrics.density == 2.0) {
            }
        }

//        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        //mPosition = mVideoPlayActivity.getCurrentViewPosition();;
        findView();
        addListener();
    }

    /**
     *
     * Initialization module
     */
    private void findView() {
        videoList = (ListView) findViewById(R.id.VidoFilename);
        updateVideoList();
        videoList.setDividerHeight(0);
        videoList.requestFocus();
        videoList.setEnabled(true);
        videoList.setFocusable(true);
        videoList.setFocusableInTouchMode(true);
        // videoList.setSelection(mPosition);
        // simpleAdapter.notifyDataSetChanged();
        setSelection(mPosition);
    }

    /**
     *
     * Set the current broadcast video name highlighted
     *
     * @param postion
     */
    public void setSelection(int postion) {
        videoList.setSelection(postion);
        if(null != simpleAdapter){
            simpleAdapter.notifyDataSetChanged();
        }

    }

    /**
     *
     * For video data
     */
    private void updateVideoList() {
        //list = mVideoPlayActivity.getVideoPlayList();
        if(mVideoInfoList != null){
            int size = mVideoInfoList.size();
            String listData[] = new String[size];
            for (int i = 0; i < size; i++) {
                listData[i] = mVideoInfoList.get(i).getName();
            }
            simpleAdapter = new ListDataListAdapter(mMWPlayerActivity, listData);
            videoList.setAdapter(simpleAdapter);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_UP) {
        } else if (keyCode == KeyEvent.KEYCODE_MENU
                && event.getAction() == KeyEvent.ACTION_UP) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
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

    /**
     *
     * Registering list
     */
    private void addListener() {
        videoList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position >= 0) {
                    Log.e("JHQ", "Video mouse click effective：：：：：");
                    String choosedFilePath = mVideoInfoList.get(position).getPath();
                    Log.i(TAG, "choosedFilePath = "+choosedFilePath);
                    mVideoFileSelectedListener.watchUserSelectedVideoPath(choosedFilePath);
                    dismiss();
                }
            }
        });
        videoList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * update video list data
     * @param videoInfos video info data
     */
    public void updateVideoListData(List<VideoInfo> videoInfos) {
        Log.i(TAG, "updateVideoListData ");
        mVideoInfoList = videoInfos;
        updateVideoList() ;
    }
}
