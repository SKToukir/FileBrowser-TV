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

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.ListDataAdapter;
import com.walton.filebrowser.business.data.BaseData;

/**
 *
 * Custom video playlist Dialog
 *
 * @author
 */
public class VideoListDialog extends Dialog {
    private static final String TAG = "VideoListDialog";
    // video list
    private ListView videoList;
    private VideoActivityBase mVideoPlayActivity;
    private Handler onClickHandler;
    // The selected video
    private int selected = 0;
    // ArrayList<HashMap<String, Object>> listData;
    ListDataAdapter simpleAdapter;
    List<BaseData> list;
    private int [] videoPosition = new int[2];
    private int mPosition;
    private static final int LIST_ITEM_NUM = 10;
    private int mCurrentPage = -1;
    private int mTotalPage = 0;
    private ArrayList<String> mPageList;

    public VideoListDialog(VideoActivityBase activity, int pos) {
        super(activity, R.style.dialog);
        mVideoPlayActivity = activity;
        this.mPosition = pos;
        mPageList = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_list);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        // Resources resources = mVideoPlayActivity.getResources();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.25);
        int height = (int) (display.getHeight() * 0.6);


        if (mVideoPlayActivity != null) {
            DisplayMetrics metrics = mVideoPlayActivity.getResources().getDisplayMetrics();
            mVideoPlayActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i(TAG, "metrics.density:" + metrics.density);
            if (metrics.density == 1.5) {
                // w.setBackgroundDrawableResource(color.transparent);
            } else if (metrics.density == 2.0) {
            }
        }

        w.setLayout(width, height);
        w.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 80;
        w.setAttributes(wl);
//        mPosition = 0;
        findView();
        videoList.setClickable(true);
        addListener();
    }

    /**
     *
     * Initialization module
     */
    private void findView() {
        Log.i(TAG, "findView: ");
        videoList = (ListView) findViewById(R.id.VidoFilename);
        getVideoName();
        videoList.setDividerHeight(5);
        videoList.requestFocus();
        videoList.setEnabled(true);
        videoList.setFocusable(true);
        videoList.setFocusableInTouchMode(true);
         videoList.setSelection(mPosition);
         simpleAdapter.notifyDataSetChanged();
        setSelection(mPosition);
    }

    /**
     *
     * Set the current broadcast video name highlighted
     *
     * @param postion
     */
    public void setSelection(int postion) {
        Log.i(TAG, "setSelection: ");
        videoList.setSelection(postion);
        simpleAdapter.notifyDataSetChanged();
    }

    /**
     *
     * For video data
     */
    private void getVideoName() {
        Log.i(TAG, "getVideoName: ");
        list = mVideoPlayActivity.getVideoPlayList();
        mTotalPage = (list.size() - 1) / LIST_ITEM_NUM + 1;
        refreshPage(0);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    mVideoPlayActivity);
            builder.setTitle(R.string.exit_title);
            builder.setMessage(R.string.exit_confirm);
            builder.setPositiveButton(
                    mVideoPlayActivity.getString(R.string.exit_ok),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            if (list.size() > 0 && selected < list.size()) {
                                int viewId = mVideoPlayActivity.getVideoPlayHolder().getViewId()-1;
                                videoPosition = mVideoPlayActivity.getVideoPosition();
                                if (videoPosition[viewId]>selected) {
                                    videoPosition[viewId] -= 1;
                                    list.remove(selected);
                                    getVideoName();
                                } else if (videoPosition[viewId] == selected ) {
                                    showToast("cannot delete the one that is playing.");
                                } else {
                                    list.remove(selected);
                                    getVideoName();
                                }
                            }
                            mDialog.dismiss();
                        }
                    });
            builder.setNeutralButton(
                    mVideoPlayActivity.getString(R.string.exit_cancel),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            mDialog.dismiss();
                        }
                    });
            builder.show();
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
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mPosition <= 0) {
                if (mCurrentPage > 0) {
                    refreshPage(mCurrentPage -1);
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mPosition >= mPageList.size() - 1 || mPosition >= LIST_ITEM_NUM - 1) {
                if (mCurrentPage < mTotalPage - 1) {
                    refreshPage(mCurrentPage + 1);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setHandler(Handler mHandler) {
        this.onClickHandler = mHandler;
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
                    selected = position + mCurrentPage * LIST_ITEM_NUM;;
                    Message msg = new Message();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("index", selected);
                    msg.setData(mBundle);
                    onClickHandler.sendMessage(msg);
                    dismiss();
                }
            }
        });
        videoList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                selected = position;
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        videoList.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_SCROLL:
                            if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){
                                refreshPage(mCurrentPage + 1);
                            }
                            else{
                                refreshPage(mCurrentPage - 1);
                            }
                            return true;
                    }
                }
                return false;
            }
        });

    }
    private void showToast(String content) {
        Toast toast = Toast.makeText(mVideoPlayActivity, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.show();
    }

    private void refreshPage(int index) {
        if (index >= mTotalPage || index < 0) {
            return;
        }
        mCurrentPage = index;
        int begin = index * LIST_ITEM_NUM;
        int end = (index + 1) * LIST_ITEM_NUM;

        mPageList.clear();
        for (int i = begin; i < list.size() && i < end; i++) {
            mPageList.add(list.get(i).getName());
        }
        if (simpleAdapter == null) {
            simpleAdapter = new ListDataAdapter(mVideoPlayActivity, mPageList);
            videoList.setAdapter(simpleAdapter);
        }
        setSelection(0);
    }
}
