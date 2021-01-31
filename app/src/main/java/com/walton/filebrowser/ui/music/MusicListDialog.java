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
package com.walton.filebrowser.ui.music;

import java.util.ArrayList;
import java.util.List;
import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent;
import android.view.InputDevice;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;
import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.ListDataAdapter;
import com.walton.filebrowser.business.data.BaseData;
import android.util.DisplayMetrics;

/**
 * Custom audio playlist Dialog
 * @author Ebric
 */
public class MusicListDialog extends Dialog {
    private static final String TAG = "MusicListDialog";
    private Activity mActivity;
    private Handler mHandler;
    private ListDataAdapter mAdapter;
    // All music list from MusicActivity
    private List<BaseData> mDataList;
    // Music List View
    private ListView mMusicListView;
    // The selected video index in all data list
    private int mSelected = 0;
    // Position of current focus item in one page
    private int mPosition;
    private Toast mToast = null;

    // The current page index
    private int mCurrentPage = -1;
    // The total page count,mTotalPage = (mDataList.size() - 1) / LIST_ITEM_NUM + 1
    private int mTotalPage = 0;
    // The data list of current page,it is different of mDataList.
    private ArrayList<String> mPageList;

    private static final int LIST_ITEM_NUM = 7;

    public MusicListDialog(Activity activity, Handler handler, int style) {

        super(activity, style);
        this.mActivity = activity;
        this.mHandler = handler;
        mPageList = new ArrayList<String>();

    }

    public MusicListDialog(Activity activity, Handler handler) {
        super(activity);
        this.mActivity = activity;
        this.mHandler = handler;
        mPageList = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_list);
        Window w = getWindow();
        w.setTitle(null);

        if (mActivity != null) {
            DisplayMetrics metrics = mActivity.getResources()
                    .getDisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i(TAG, "metrics.density:" + metrics.density);
            if (metrics.density == 1.5) {
                w.setBackgroundDrawableResource(color.transparent);
            }
        }

        w.setGravity(Gravity.CENTER);

        // w.setBackgroundDrawableResource(color.transparent);
        mPosition = MusicPlayerActivity.currentPosition;
        // control initialization
        findView();
        mMusicListView.setClickable(true);
        Log.d(TAG, "listview is focused : " + mMusicListView.isFocused());
        // ListView focus monitoring
        addListener();
    }

    /**
     * update ListView current focus position.
     * @param position the current focus index.
     */
    protected void setSelection(int position) {
        mMusicListView.setSelection(position);
    }

    /*
     * Initialization module
     */
    private void findView() {
        mMusicListView = (ListView) findViewById(R.id.MusicFilename);
        getMusicName();
        mMusicListView.setDividerHeight(0);
        mMusicListView.requestFocus();
        mMusicListView.setEnabled(true);
        mMusicListView.setFocusable(true);
        mMusicListView.setFocusableInTouchMode(true);
        setSelection(mPosition);
    }

    /*
     * registering listeners
     */
    private void addListener() {
        // add click monitor
        mMusicListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position >= 0) {
                    mSelected = position + mCurrentPage * LIST_ITEM_NUM;;
                    MusicPlayerActivity.currentPosition = mSelected;
                    new Thread(new Runnable() {
                        public void run() {
                            mHandler.sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                        }
                    }).start();
                    dismiss();
                }
            }
        });
        // Add mSelected monitoring
        mMusicListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mSelected = mCurrentPage * LIST_ITEM_NUM + position;
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setOnDismissListener(listener);

        mMusicListView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_SCROLL:
                            if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){
                                refreshPage(mCurrentPage + 1);
                            } else {
                                refreshPage(mCurrentPage - 1);
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    /**
     * For video data.
     */
    private void getMusicName() {
        mDataList = MusicPlayerActivity.musicList;
        mTotalPage = (mDataList.size() - 1) / LIST_ITEM_NUM + 1;
        refreshPage(0);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode
                || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode
                || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }

        // Selected songs began to play
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            MusicPlayerActivity.currentPosition = mSelected;
            new Thread(new Runnable() {
                public void run() {
                    mHandler.sendEmptyMessage(MusicPlayerActivity.HANDLE_MESSAGE_SERVICE_START);
                }
            }).start();
            dismiss();
            // Custom playlists, the user can delete play in the mDataList of songs
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.exit_title);
            builder.setMessage(R.string.exit_confirm);
            builder.setPositiveButton(mActivity.getString(R.string.exit_ok),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            if (mDataList.size() > 0 && mSelected < mDataList.size()) {
                                mDataList.remove(mSelected);
                                getMusicName();
                                setSelection(mSelected);
                                mAdapter.notifyDataSetChanged();
                                mDialog.dismiss();
                            }
                        }
                    });
            builder.setNeutralButton(mActivity.getString(R.string.exit_cancel),
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            mDialog.dismiss();
                        }
                    });
            builder.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int arg1,
                        KeyEvent arg2) {
                    if (KeyEvent.KEYCODE_MEDIA_PLAY == arg1
                            || KeyEvent.KEYCODE_MEDIA_PAUSE == arg1
                            || KeyEvent.KEYCODE_MEDIA_NEXT == arg1
                            || KeyEvent.KEYCODE_MEDIA_PREVIOUS == arg1) {
                        return true;
                    }
                    return false;
                }
            });
            builder.show();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
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
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * Tip is currently play mDataList of the first or the last term.
     */
    private void showToast(int id) {
        if (mToast != null) {
            mToast.setText(id);
            mToast.show();
            return;
        }
        mToast = Toast.makeText(mActivity,
                mActivity.getResources().getString(id), Toast.LENGTH_SHORT);
        mToast.show();

    }

    public OnDismissListener listener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MusicPlayerActivity) mActivity).pauseOrPlayPresentMusicNameMarquee();
        }
    };

    /**
     * Refresh the page with the page number
     * @param index page index
     */
    private void refreshPage(int index) {
        if (index >= mTotalPage || index < 0) {
            return;
        }
        mCurrentPage = index;
        int begin = index * LIST_ITEM_NUM;
        int end = (index + 1) * LIST_ITEM_NUM;

        mPageList.clear();
        for (int i = begin; i < mDataList.size() && i < end; i++) {
            mPageList.add(mDataList.get(i).getName());
        }
        if (mAdapter == null) {
            mAdapter = new ListDataAdapter(mActivity, mPageList);
            mMusicListView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        setSelection(0);
    }
}
