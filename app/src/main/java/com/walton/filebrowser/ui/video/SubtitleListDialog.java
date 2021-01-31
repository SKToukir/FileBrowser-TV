//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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


import android.R.color;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.walton.filebrowser.R;
import com.walton.filebrowser.business.adapter.ListDataListAdapter;
import com.walton.filebrowser.business.adapter.SubtitleSettingListAdapter;
import com.walton.filebrowser.business.video.SubtitleTool;
import com.walton.filebrowser.util.Tools;
import com.mstar.android.media.SubtitleTrackInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Subtitle playlist Dialog
 *
 * @author
 */
public class SubtitleListDialog extends Dialog {
    private static final String TAG = "SubtitleListDialog";
    // subtitle list
    private ListView subtitleSettingList;
    private Context context;
    // The selected subtitle
    private int mSubtitleSelectedItem = 0;
    private String videoPath;
    private PlaySettingSubtitleDialog playSettingSubtitleDialog;
    private List<String> listSubtitle = null;

    private ListDataListAdapter adapter;
    // Subtitles view
    private BorderTextViews subtitleTextView;
    private SubtitleTool subtitleTool;
    private String path = "";
    private int getInfoNum;
    private int viewId = 1;

    private BufferedReader mBufferedReader;
    private BufferedWriter mBufferedWriter;
    private InputStreamReader mInputStreamReader;
    private OutputStreamWriter mOutputStreamWriter;
    private BufferedInputStream bis;
    private InputStream isForGetCharset;
    private InputStream is = null;
    public SubtitleListDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SubtitleListDialog(Context context, int theme, String videoPath,
            PlaySettingSubtitleDialog playSettingSubtitleDialog,
            SubtitleSettingListAdapter subAdapter) {
        super(context, theme);
        this.context = context;
        this.videoPath = videoPath;
        this.playSettingSubtitleDialog = playSettingSubtitleDialog;
        // this.subAdapter = subAdapter;
        subtitleTextView = ((VideoPlayerActivity) context).getVideoPlayHolder().getSubtitleTextView();
        viewId = ((VideoPlayerActivity) context).getVideoPlayHolder().getViewId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.subtitle_video_play_list);
        Window w = getWindow();
        Display display = w.getWindowManager().getDefaultDisplay();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.4);
        int height = (int) (display.getHeight() * 0.7);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
        w.setBackgroundDrawableResource(color.transparent);
        findView();
        subtitleSettingList.setOnKeyListener(onkeyListenter);
        subtitleSettingList.setClickable(true);
        addListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        refreshSubtitleDialog();
    }

    /**
     * Initialization module
     */
    private void findView() {
        subtitleSettingList = (ListView) findViewById(R.id.subtitle_list);
        subtitleSettingList.requestFocus();
        subtitleSettingList.setEnabled(true);
        subtitleSettingList.setFocusable(true);
        subtitleSettingList.setFocusableInTouchMode(true);
        getVideoPathName();
        subtitleSettingList.setDividerHeight(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "The mouse to click the::::::::;");
        refreshSubtitleDialog();
        return super.onTouchEvent(event);
    }

    /**
     * Registering listeners
     */
    private void addListener() {
        subtitleSettingList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.e(TAG, "Subitem monitoring setOnItemClickListener " + position);
                if (position >= 0) {
                    mSubtitleSelectedItem = position;
                    //refreshSubtitleDialog();
                    getLocalSubtitlePath(mSubtitleSelectedItem);
                }
            }
        });
        subtitleSettingList.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        Log.e(TAG, "---subtitleSettingList onItemSelected position:" + position);
                        mSubtitleSelectedItem = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER: {
                            Log.e(TAG, "KEYCODE_ENTER :mSubtitleSelectedItem:" + mSubtitleSelectedItem);
                            //refreshSubtitleDialog();
                            getLocalSubtitlePath(mSubtitleSelectedItem);
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };
    private void getLocalSubtitlePath(int pos){
        Log.i(TAG,"getLocalSubtitlePath");
        path = listSubtitle.get(pos);
        if (Tools.isSambaPlaybackUrl(path)) {
            new Thread(new Runnable(){
                public void run(){
                    path = writeToLocalPath(path);
                    mHandler.sendEmptyMessageDelayed(1, 0);
                }
            }).start();
        } else {
            refreshSubtitleDialog();
        }
    }
    public String GetCharset(final String filePath) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            if (bis != null) {
                bis.close();
            }
            if (Tools.isNetPlayback(filePath)) {
                try {
                    isForGetCharset = new URL(filePath).openStream();
                } catch (MalformedURLException e) {
                     e.printStackTrace();
                } catch (IOException e) {
                     e.printStackTrace();
                }
                if (isForGetCharset == null) {
                    Log.i(TAG,"isForGetCharset is null");
                    return null;
                }
                bis = new BufferedInputStream(isForGetCharset);
            } else {
                File file = new File(filePath);
                bis = new BufferedInputStream(new FileInputStream(file));
            }
            // bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "unicode";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0x5B
                    && first3Bytes[1] == (byte) 0x30) {
                charset = "ISO8859-1";
                checked = true;
            } else {
                charset = "GBK";
                checked = true;
            }
            // bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return charset;
    }

    private String writeToLocalPath(String url){
        String tmpPath = null;
        String subtitleLocalPath = null;
        if (Tools.isSambaPlaybackUrl(url)) {
            tmpPath = Tools.convertToHttpUrl(url);
        } else {
            tmpPath = url;
        }
        final String realPath  = tmpPath;
        if (Tools.isNetPlayback(realPath)) {
            try {
                is = new URL(realPath).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File mFile = new File(realPath);
                is = new FileInputStream(mFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (is == null) {
            Log.i(TAG,"is is null");
            return null;
        }
        String charset = GetCharset(realPath);
        try {
        mInputStreamReader = new InputStreamReader(is,charset);
            }catch (Exception e) {
                e.printStackTrace();
            }
        // One line read

        try {
            mBufferedReader = new BufferedReader(mInputStreamReader);
            char byteArray [] = new char[1024*8+1];
            int readNum = 0;
            int offset = 0;

            int index = realPath.lastIndexOf(".");
            String postFix = realPath.substring(index,realPath.length());
            subtitleLocalPath = "/data/subtitle"+postFix;
            File outPutLocalSubtitleFile = new File(subtitleLocalPath);
            mOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(outPutLocalSubtitleFile),charset);
            mBufferedWriter = new BufferedWriter(mOutputStreamWriter);
            while ((readNum = mBufferedReader.read(byteArray,0,1024*8))>0){
                mBufferedWriter.write(byteArray,0,readNum);
                offset += readNum;
                Log.i(TAG,"andrew_read_write:"+readNum+","+offset);
            }

            mBufferedReader.close();
            mBufferedWriter.close();
            mInputStreamReader.close();
            mOutputStreamWriter.close();
            if (is!=null) {
                is.close();
            }
            if (isForGetCharset!=null) {
                isForGetCharset.close();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();
        }
        try {
              String command = "chmod 777 " + subtitleLocalPath;
              Log.i(TAG, "command = " + command);
              Runtime runtime = Runtime.getRuntime();
              Process proc = runtime.exec(command);
        } catch (IOException e) {
              Log.i(TAG,"chmod fail!!!!");
              e.printStackTrace();
        }
        return subtitleLocalPath;
    }
    /**
     * Refresh subtitle Settings interface data
     */
    private void refreshSubtitleDialog() {
        // int position = subtitleSettingList.getSelectedItemPosition();
        String subtitleLanguageType = context.getResources().getString(
                R.string.subtitle_3_value_1);
        if (listSubtitle != null && listSubtitle.size() > mSubtitleSelectedItem
                && mSubtitleSelectedItem >= 0) {
            // For a specific subtitles subtitleTrackInfo object
            SubtitleManager.getInstance().offSubtitleTrack(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer()) ;
            SubtitleManager.getInstance().onSubtitleTrack(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer()) ;
            if (path == null || path.length()==0) {
                return ;
            }
            SubtitleManager.getInstance().setSubtitleDataSource(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer(), path) ;
            if (path.endsWith("idx") || path.endsWith("sup")) {
                playSettingSubtitleDialog.setExternalSubType(true);
            } else {
                playSettingSubtitleDialog.setExternalSubType(false);
            }
            if (path.endsWith("idx")) {
                // As the picture captions show to empty the last remaining
                // subtitles data
                subtitleTextView.setText("");
                SubtitleTrackInfo info = SubtitleManager.getInstance().getAllSubtitleTrackInfo(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer()) ;
                if (info != null) {
                    int totalInfoNum = info.getAllSubtitleCount();
                    int innerSubNum =  info.getAllInternalSubtitleCount();
                    Log.i(TAG, "***info**" + info + " " + totalInfoNum + " "
                                    + innerSubNum);
                    if (innerSubNum < 0) {
                        innerSubNum = 0;
                    }
                    playSettingSubtitleDialog.setInternalSubCount(innerSubNum);
                    int subtitleType[] = new int[totalInfoNum];
                    info.getSubtitleType(subtitleType);
                    int subtitleCodeType[] = new int[totalInfoNum];
                    info.getSubtitleCodeType(subtitleCodeType);
                    String subtitleLanguageTypes[] = new String[totalInfoNum];
                    info.getSubtitleLanguageType(subtitleLanguageTypes, false);
                    if (totalInfoNum > innerSubNum) {
                        Log.i(TAG, "***totalInfoNum**" + totalInfoNum + " " + subtitleLanguageTypes[innerSubNum]);
                        SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageTypes[innerSubNum], viewId);
                        SubtitleManager.setSubtitleLanguageType(subtitleLanguageTypes, viewId);
                    } else {
                        getInfoNum = 0;
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        dismiss();
                        return;
                    }
                }
            } else {
                SubtitleManager.setSubtitleLanguageType(null, viewId);
                SubtitleTrackInfo subtitleTrackInfo = null ;
                if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
                    subtitleTrackInfo = SubtitleManager.getInstance().getSubtitleTrackInfo(
                            ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer()
                            ,mSubtitleSelectedItem) ;
                }
                Log.i(TAG, "*****SubtitleTrackInfo******" );
                if (subtitleTrackInfo != null) {
                    try {
                        subtitleLanguageType = subtitleTrackInfo
                                .getSubtitleLanguageType(false);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG,"*****subtitleTrackInfo******" + subtitleLanguageType + " "
                                    + subtitleTrackInfo.getSubtitleType() + " "
                                    + subtitleTrackInfo.getAllSubtitleCount());
                    if (subtitleLanguageType == null) {
                        getInfoNum = 0;
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        dismiss();
                        return;
                    }
                }
                SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageType, viewId);
            }
            SubtitleManager.setSubtitleSettingOpt(1, listSubtitle.get(mSubtitleSelectedItem), viewId);
            // subAdapter.notifyDataSetChanged();
        }
        playSettingSubtitleDialog.show();
        dismiss();
    }

    /**
     * For video subtitles data
     */
    private void getVideoPathName() {
        Log.i(TAG, "getVideoPathName videoPath:" + videoPath);
        subtitleTool = new SubtitleTool(videoPath);
        listSubtitle = subtitleTool.getSubtitlePathList(SubtitleTool.SUBTITLE_FORMATE_NULL);
        Log.i(TAG, "listSubtitle:" + listSubtitle + " listSubtitle.size:" + listSubtitle.size());
        String listData[] = new String[listSubtitle.size()];
        for (int i = 0; i < listSubtitle.size(); i++) {
            listData[i] = listSubtitle.get(i);
        }
        adapter = new ListDataListAdapter(context, listData);
        subtitleSettingList.setAdapter(adapter);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            String subtitleLanguageType = context.getResources().getString(
                    R.string.subtitle_3_value_1);
            if (msg.what == 0) {

                if (path.endsWith("idx")) {
                    // As the picture captions show to empty the last remaining
                    // subtitles data
                    subtitleTextView.setText("");
                    SubtitleTrackInfo info = SubtitleManager.getInstance().getAllSubtitleTrackInfo(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer());
                    if (null == info) {
                        return;
                    }
                    int totalInfoNum = info.getAllSubtitleCount();
                    int innerSubNum = info.getAllInternalSubtitleCount();
                    Log.i(TAG, "***info**" + info + " " + totalInfoNum + " "+ innerSubNum + " ");
                    playSettingSubtitleDialog.setInternalSubCount(info.getAllInternalSubtitleCount());
                    String subtitleLanguageTypes[] = new String[totalInfoNum];
                    info.getSubtitleLanguageType(subtitleLanguageTypes, false);
                    if (totalInfoNum != 0) {
                        Log.i(TAG, "*****" + totalInfoNum + " " + subtitleLanguageTypes[innerSubNum]);
                        SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageTypes[innerSubNum], viewId);
                        SubtitleManager.setSubtitleLanguageType(subtitleLanguageTypes, viewId);
                    } else {
                        SubtitleManager.setSubtitleLanguageType(null, viewId);
                        getInfoNum++;
                        if (getInfoNum <= 2) {
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                            return;
                        } else {
                            getInfoNum = 0;
                        }
                    }
                } else {
                    SubtitleManager.setSubtitleLanguageType(null, viewId);
                    SubtitleTrackInfo subtitleTrackInfo = null;
                    if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
                        subtitleTrackInfo = SubtitleManager.getInstance().getSubtitleTrackInfo(
                                ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer()
                                ,mSubtitleSelectedItem);
                    }
                    Log.i(TAG, "*****subtitleTrackInfo******" );
                    if (subtitleTrackInfo != null) {
                        subtitleLanguageType = subtitleTrackInfo
                                .getSubtitleLanguageType(false);
                        Log.i(TAG, "******subtitleTrackInfo*****" + subtitleLanguageType + " "
                                        + subtitleTrackInfo.getSubtitleType() + " "
                                        + subtitleTrackInfo.getAllSubtitleCount());
                        if (subtitleLanguageType == null) {
                            getInfoNum++;
                            if (getInfoNum <= 2) {
                                mHandler.sendEmptyMessageDelayed(0, 1000);
                                return;
                            }
                            subtitleLanguageType = context.getResources()
                                    .getString(R.string.subtitle_3_value_1);
                        }
                    }
                    SubtitleManager.setSubtitleSettingOpt(3, subtitleLanguageType, viewId);
                }
                SubtitleManager.setSubtitleSettingOpt(1, listSubtitle.get(mSubtitleSelectedItem), viewId);
                // subAdapter.notifyDataSetChanged();
                playSettingSubtitleDialog.show();

            } else if (msg.what == 1) {
                refreshSubtitleDialog();
            }
        }
    };

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
