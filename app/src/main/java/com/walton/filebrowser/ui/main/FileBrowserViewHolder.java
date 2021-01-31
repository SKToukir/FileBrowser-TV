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

package com.walton.filebrowser.ui.main;

import java.util.ArrayList;

import android.util.Log;

import android.content.res.Resources;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.PopupWindow;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.TextView;
import android.widget.Button;
import android.widget.GridView;

import android.view.Window;
import android.view.WindowManager;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;
import com.walton.filebrowser.util.GridViewTV;
import com.walton.filebrowser.util.Tools;
import com.walton.filebrowser.business.data.ViewMode;
import com.walton.filebrowser.business.adapter.FileBrowserSettintAdapter;



/**
 * Data browsing interface control management class.
 */
public class FileBrowserViewHolder {

    private static final String TAG = "FileBrowserViewHolder";

    // false means icon browser is off ,true means icon browser is on
    public static boolean btnSwitchIsOnOrOff = true;

    private FileBrowserActivity activity;

    private Handler handler;

    /************************************************************************
     * Control and layout definition area
     ************************************************************************/

    // Progress bar
    protected ProgressBar progressScanner;

    // Data display with ListView
    protected ListView listView = null;

    // the current page number
    protected TextView currentPageNumText = null;

    // total page
    protected TextView totalPageNumText = null;

    // loading progress tooltip
    protected TextView scannerStateText = null;

    // hint information, such as file path, etc
    protected TextView displayTip = null;

    // picture title
    protected TextView pictureTitleText = null;

    // lyrics title
    protected TextView songTitleText = null;

    // video title
    protected TextView videoTitleText = null;

    // other title
    protected TextView otherTitleText = null;

    // picture icon
    protected ImageView pictureTitleImage = null;

    // lyrics icon
    protected ImageView songTitleImage = null;

    // video icon
    protected ImageView videoTitleImage = null;

    // other icon
    protected ImageView otherTitleImage = null;

    // left turn the page of the arrow icon
    protected ImageView pageUp = null;

    // right turn the page of the arrow icon
    protected ImageView pageDown = null;

    protected ImageView divxReg = null;
    // selected item
    protected int selectedItemPosition = 0;
    protected LinearLayout.LayoutParams focusParams = null;
    protected LinearLayout.LayoutParams normalParams = null;

    //video thumbnail
    protected Button btnSwitch =null;
    protected GridView gridView = null;
    protected ImageView viewModeImg = null;

    //add current selected item cache view when in touch mode
    protected View currentSelectedItemView = null ;
    protected View oldSelectedItemView = null ;
    protected boolean isEnterListviewLocation = false ;

    private SwitchViewModeDialog mSwitchViewModeDialog;
    private SwitchMultipleVideoViewDialog mSwitchMultipleVideoViewDialog;
    private SwitchDivxModeDialog mSwitchDivxModeDialog;
    protected ArrayList<ViewMode> mViewModeList;
    public Resources mResources;
    protected PopupWindow mPopupWindow;

    public FileBrowserViewHolder(FileBrowserActivity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        mResources = activity.getResources();
    }

    /************************************************************************
     * protection method area
     ************************************************************************/
    /**
     * file browsing interface all control initialization.
     */
    protected void findViews() {

        listView = (ListView) activity.findViewById(R.id.videoFileList);
        listView.setOnHoverListener(onHoverListener);

        // loading the progress of the progress bar and the tooltip
        progressScanner = (ProgressBar) activity
                .findViewById(R.id.progress_scan);
        scannerStateText = (TextView) activity.findViewById(R.id.scanner_id);
        // page text control
        currentPageNumText = (TextView) activity
                .findViewById(R.id.videoCurrentPageNumText);
        totalPageNumText = (TextView) activity
                .findViewById(R.id.videoTotalPageNumText);
        // left, right turn the page's control
        pageUp = (ImageView) activity.findViewById(R.id.leftArrowImg);
        pageUp.setOnClickListener(onClickListener);
        pageUp.setOnHoverListener(onHoverListener);
        pageDown = (ImageView) activity.findViewById(R.id.rightArrowImg);
        pageDown.setOnClickListener(onClickListener);
        pageDown.setOnHoverListener(onHoverListener);

                divxReg = (ImageView) activity.findViewById(R.id.divxset);
        divxReg.setOnClickListener(onClickListener);
        divxReg.setOnHoverListener(onHoverListener);
        if(!Constants.bSupportDivx)
                  divxReg.setVisibility(View.INVISIBLE);
        displayTip = (TextView) activity.findViewById(R.id.displayInfor);
        // Picture icon and title
        pictureTitleImage = (ImageView) activity
                .findViewById(R.id.pictureTitleImage);
        pictureTitleText = (TextView) activity
                .findViewById(R.id.pictureTitleText);
        pictureTitleImage.setOnClickListener(onClickListener);
        pictureTitleImage.setOnHoverListener(onHoverListener);

        // Lyrics icon and title
        songTitleImage = (ImageView) activity.findViewById(R.id.songTitleImage);
        songTitleText = (TextView) activity.findViewById(R.id.songTitleText);
        songTitleImage.setOnClickListener(onClickListener);
        songTitleImage.setOnHoverListener(onHoverListener);

        // Video icon and title
        videoTitleImage = (ImageView) activity
                .findViewById(R.id.videoTitleImage);
        videoTitleText = (TextView) activity.findViewById(R.id.videoTitleText);
        videoTitleImage.setOnClickListener(onClickListener);
        videoTitleImage.setOnHoverListener(onHoverListener);

        // Other icon and title
        otherTitleImage = (ImageView) activity
                .findViewById(R.id.otherTitleImage);
        otherTitleText = (TextView) activity.findViewById(R.id.otherTitleText);
        otherTitleImage.setOnClickListener(onClickListener);
        otherTitleImage.setOnHoverListener(onHoverListener);

        focusParams = (LinearLayout.LayoutParams) otherTitleImage.getLayoutParams();
        normalParams = (LinearLayout.LayoutParams) videoTitleImage.getLayoutParams();

        // gridview
        btnSwitch = (Button)activity.findViewById(R.id.btnSwitch);
        gridView = (GridView) activity.findViewById(R.id.gridView);
        viewModeImg = (ImageView)activity.findViewById(R.id.viewmode_img);
        if (Constants.GRIDVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
            viewModeImg.setBackgroundResource(R.drawable.gridview_mode);
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            setBtnSwitchFocus(false);
            setGridViewFocus(true,0);
            setListViewFocus(false,0);
        } else if (Constants.LISTVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
            viewModeImg.setBackgroundResource(R.drawable.listview_mode);
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            setBtnSwitchFocus(false);
            setGridViewFocus(false,0);
            setListViewFocus(true,0);

        }
    }

    /**
     * pictures, music, video, and other icon for focus or lose focus state
     * Settings.
     *
     * @param type
     *            data type.
     * @param focusable
     *            whether get focus.
     */
    protected void setLeftFocus(int type, boolean focusable) {
        if (type == Constants.OPTION_STATE_PICTURE) {
            if (focusable) {
                changePictureImageStyle(true, true, R.drawable.icon_video_close);
                changePictureTextStyle(16, Color.WHITE);

            } else {
                changePictureImageStyle(false, true, R.drawable.icon_video_close);
                changePictureTextStyle(16, Color.LTGRAY);
            }
            setDisplayTip(R.string.picture);

        } else if (type == Constants.OPTION_STATE_SONG) {
            if (focusable) {
                changeSongImageStyle(true, true, R.drawable.icon_video_close);
                changeSongTextStyle(16, Color.WHITE);

            } else {
                changeSongImageStyle(false, true, R.drawable.icon_video_close);
                changeSongTextStyle(16, Color.LTGRAY);
            }
            setDisplayTip(R.string.song);

        } else if (type == Constants.OPTION_STATE_VIDEO) {
            if (focusable) {
                changeVideoImageStyle(true, true, R.drawable.icon_video_foucs);
                changeVideoTextStyle(16, Color.WHITE);

            } else {
                changeVideoImageStyle(false, true, R.drawable.icon_video_close);
                changeVideoTextStyle(16, Color.LTGRAY);
            }
            setDisplayTip(R.string.video);

        } else if (type == Constants.OPTION_STATE_ALL) {
            if (focusable) {
                changeOtherImageStyle(true, true, R.drawable.icon_video_close);
                changeOtherTextStyle(16, Color.WHITE);

            } else {
                changeOtherImageStyle(false, true, R.drawable.icon_video_close);
                changeOtherTextStyle(16, Color.LTGRAY);
            }
            setDisplayTip(R.string.all);

        }

    }

    /**
     * @param focusable
     *            Whether the ListView set for focus.
     */
    protected void setListViewFocus(boolean focusable, int position) {
        Log.i(TAG, "setListViewFocus: focusable " + focusable);
        if (focusable) {
            listView.setFocusable(true);
            listView.requestFocus();
            listView.setSelection(position);
        } else {
            listView.setFocusable(false);
        }
    }

    protected void setGridViewFocus(boolean focusable, int position) {
        if (focusable) {
            gridView.setFocusable(true);
            gridView.requestFocus();
            gridView.setSelection(position);
        } else {
            gridView.setFocusable(false);
        }

    }
    protected void setBtnSwitchFocus(boolean focusable) {
        if (focusable) {
            btnSwitch.setFocusable(true);
            btnSwitch.requestFocus();
        } else {
            btnSwitch.setFocusable(false);
            btnSwitch.setFocusableInTouchMode(false);
        }
    }
    public void printPresentViewMode(){
         if (FileBrowserActivity.mListOrGridFlag == Constants.LISTVIEW_MODE)
             Log.i(TAG,"prensentViewMode : listview");
         else if (FileBrowserActivity.mListOrGridFlag == Constants.GRIDVIEW_MODE)
             Log.i(TAG,"prensentViewMode : gridview");
   }
    private void initSettingDialog(){
         Log.i(TAG,"initSettingDialog");
         if (mSwitchViewModeDialog!=null) mSwitchViewModeDialog =null;
         if (mSwitchMultipleVideoViewDialog!=null) mSwitchMultipleVideoViewDialog = null;
         if (mSwitchDivxModeDialog!=null) mSwitchDivxModeDialog = null;
    }
    public void createPopWindow(){
         initSettingDialog();
         Log.i(TAG,"createPopWindow");
         int mWidth = 440;
         int mHight = 200;
         mViewModeList = new ArrayList<ViewMode>();
         Drawable drawable =null;
         drawable = mResources.getDrawable(R.drawable.icon_video_close);
         ViewMode viewMode =new ViewMode(drawable, "viewMode");
         mViewModeList.add(viewMode);

         drawable = mResources.getDrawable(R.drawable.icon_video_close);
         viewMode = new ViewMode(drawable, "divxMode");
         mViewModeList.add(viewMode);

         // to do in RR by jason
         if (Tools.isMWPlayBackOn()) {
             drawable = mResources.getDrawable(R.drawable.icon_video_close);
             viewMode =new ViewMode(drawable, "multipleVideoView");
             mViewModeList.add(viewMode);
             mHight = 300;
         }

         View myView = activity.getLayoutInflater().inflate(R.layout.filebrowser_setting_popwin, null);
         mPopupWindow = new PopupWindow(myView, mWidth, mHight, true);
         mPopupWindow.setBackgroundDrawable(mResources.getDrawable(R.drawable.popwindow_diaolog_bg));
         mPopupWindow.setFocusable(true);
         mPopupWindow.showAsDropDown(btnSwitch, -5, 0);
         ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
         lv.setAdapter(new FileBrowserSettintAdapter(activity, mViewModeList));
         lv.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                  Log.i(TAG,"popwindow onclick position:"+String.valueOf(position));
                  switch (position){
                  case 0:
                      showSwitchViewModeDialog();
                      break;
                  case 1:
                      handler.sendEmptyMessageDelayed(Constants.SHOW_DIVX_DIALOG, 500);
                      break;
                  case 2:
                      showSwitchMultipleVideoViewDialog();
                      break;
                  case 3:
                      break;
                  case 4:
                      break;
                  default:
                      break;
                  }
                  mPopupWindow.dismiss();
                }
         });

     }

    public void showSwitchViewModeDialog() {
        if (mSwitchViewModeDialog == null) {
            mSwitchViewModeDialog = new SwitchViewModeDialog(activity);
            mSwitchViewModeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = mSwitchViewModeDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            Log.i(TAG, "showSwitchViewModeDialog lp.x:" + lp.x + " lp.y:" + lp.y);
            mSwitchViewModeDialog.getWindow().setAttributes(lp);
        }
        mSwitchViewModeDialog.show();
    }
    public void showSwitchMultipleVideoViewDialog() {
        if (mSwitchMultipleVideoViewDialog == null) {
            mSwitchMultipleVideoViewDialog = new SwitchMultipleVideoViewDialog(activity);
            mSwitchMultipleVideoViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = mSwitchMultipleVideoViewDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            Log.i(TAG, "showSwitchMultipleVideoViewDialog lp.x:" + lp.x + " lp.y:" + lp.y);
            mSwitchMultipleVideoViewDialog.getWindow().setAttributes(lp);
        }
        mSwitchMultipleVideoViewDialog.show();
    }

    public void showSwitchDivxModeDialog() {
        if (mSwitchDivxModeDialog == null) {
            mSwitchDivxModeDialog = new SwitchDivxModeDialog(activity,handler);
            mSwitchDivxModeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = mSwitchDivxModeDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            Log.i(TAG, "showSwitchDivxModeDialog lp.x:" + lp.x + " lp.y:" + lp.y);
            mSwitchDivxModeDialog.getWindow().setAttributes(lp);
        }
        mSwitchDivxModeDialog.show();
    }

    /**
     * switching pictures, music, video, and other icon of focus.
     *
     * @param oldType
     *            Previous type
     * @param newType
     *            next type
     */
    protected void changeLeftFocus(int oldType, int newType) {
        // Dlna data do not switch media types
        if (activity.getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
            return;
        }

        if (oldType == Constants.OPTION_STATE_PICTURE) {
            changePictureImageStyle(false, false, R.drawable.icon_video_close);
            changePictureTextStyle(14, Color.LTGRAY);

        } else if (oldType == Constants.OPTION_STATE_SONG) {
            changeSongImageStyle(false, false, R.drawable.icon_video_close);
            changeSongTextStyle(14, Color.LTGRAY);

        } else if (oldType == Constants.OPTION_STATE_VIDEO) {
            changeVideoImageStyle(false, false, R.drawable.icon_video_close);
            changeVideoTextStyle(14, Color.LTGRAY);

        } else if (oldType == Constants.OPTION_STATE_ALL) {
            changeOtherImageStyle(false, false, R.drawable.icon_video_close);
            changeOtherTextStyle(14, Color.LTGRAY);
        }

        // The current data type
        if (newType == Constants.OPTION_STATE_PICTURE) {
            changePictureImageStyle(true, true, R.drawable.icon_video_close);
            changePictureTextStyle(16, Color.WHITE);

            setDisplayTip(R.string.picture);
        } else if (newType == Constants.OPTION_STATE_SONG) {
            changeSongImageStyle(true, true, R.drawable.icon_video_close);
            changeSongTextStyle(16, Color.WHITE);

            setDisplayTip(R.string.song);
        } else if (newType == Constants.OPTION_STATE_VIDEO) {
            changeVideoImageStyle(true, true, R.drawable.icon_video_foucs);
            changeVideoTextStyle(16, Color.WHITE);

            setDisplayTip(R.string.video);
        } else if (newType == Constants.OPTION_STATE_ALL) {
            changeOtherImageStyle(true, true, R.drawable.icon_video_close);
            changeOtherTextStyle(16, Color.WHITE);

            setDisplayTip(R.string.all);
        }

    }

    /**
     * that left the type switch can be used.
     */
    protected void openTypeChange() {
        // picture type optional
        pictureTitleImage.setClickable(true);
        changePictureImageStyle(false, true, R.drawable.icon_video_close);
        changePictureTextStyle(14, Color.LTGRAY);

        // music type optional
        songTitleImage.setClickable(true);
        changeSongImageStyle(false, true, R.drawable.icon_video_close);
        changeSongTextStyle(14, Color.LTGRAY);

        // video type optional
        videoTitleImage.setClickable(true);
        changeVideoImageStyle(false, true, R.drawable.icon_video_close);
        changeVideoTextStyle(14, Color.LTGRAY);
    }

    /**
     * that left the type switch, not with.
     */
    protected void closeTypeChange() {
        // remove the focus of the state before
        changeLeftFocus(activity.dataType, 0);

        activity.dataType = Constants.OPTION_STATE_ALL;
        // picture type do not choose
        pictureTitleImage.setClickable(false);
        changePictureImageStyle(false, false, R.drawable.icon_video_close);
        changePictureTextStyle(14, Color.LTGRAY);

        // music type do not choose
        songTitleImage.setClickable(false);
        changeSongImageStyle(false, false, R.drawable.icon_video_close);
        changeSongTextStyle(14, Color.LTGRAY);

        // video type do not choose
        videoTitleImage.setClickable(false);
        changeVideoImageStyle(false, false, R.drawable.icon_video_close);
        changeVideoTextStyle(14, Color.LTGRAY);

    }

    /**
     * to set the current page number.
     *
     * @param num
     *            page value.
     */
    protected void setCurrentPageNum(int num) {
        this.currentPageNumText.setText("" + num);
    }

    /**
     * set total page.
     *
     * @param num
     *            page value.
     */
    protected void setTotalPageNum(int num) {
        this.totalPageNumText.setText("" + num);
    }

    /**
     * show or hide loading progress and loading tooltip.
     *
     * @param flag
     *            if need to display loading progress as a true, or be false.
     */
    protected void showScanStatus(boolean flag) {
        if (flag) {
            progressScanner.setVisibility(View.VISIBLE);
        } else {
            progressScanner.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update loading tooltip.
     *
     * @param id
     *            resource id.
     */
    protected void updateScanStatusText(boolean flag, int id) {
        if (flag) {
            scannerStateText.setVisibility(View.VISIBLE);
            scannerStateText.setText(activity.getString(id));
        } else {
            scannerStateText.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * set tooltip.
     *
     * @param text
     *            tooltip text information.
     */
    protected void setDisplayTip(final String text) {
        if (text != null) {
            displayTip.setText(text);
        } else {
            displayTip.setText("");
        }
    }

    protected String getDisplayTipPath() {
        return displayTip.getText().toString().substring(0, displayTip.getText().toString().lastIndexOf("/"));
    }

    /**
     * set tooltip.
     *
     * @param id
     *            prompt file information.
     */
    protected void setDisplayTip(int id) {
        if (id > 0) {
            displayTip.setText(activity.getString(id));
        } else {
            displayTip.setText("");
        }
    }

    /************************************************************************
     * Private method area
     ************************************************************************/
    private void changeVideoImageStyle(boolean focusable,boolean selected, int id) {
        if (focusable) {
            videoTitleImage.setFocusable(true);
            videoTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            videoTitleImage.requestFocus();
            videoTitleImage.setLayoutParams(focusParams);
        } else {
            videoTitleImage.setFocusable(false);
            videoTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            if(selected){
                videoTitleImage.setLayoutParams(focusParams);
            }else{
                videoTitleImage.setLayoutParams(normalParams);
            }
        }
    }

    private void changeVideoTextStyle(int size, int color) {
        videoTitleText.setTextSize(size);
        videoTitleText.setTextColor(color);
    }

    private void changePictureImageStyle(boolean focusable,boolean selected, int id) {
        if (focusable) {
            pictureTitleImage.setFocusable(true);
            pictureTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            pictureTitleImage.requestFocus();
            pictureTitleImage.setLayoutParams(focusParams);
        } else {
            pictureTitleImage.setFocusable(false);
            pictureTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            if(selected){
                pictureTitleImage.setLayoutParams(focusParams);
            }else{
                pictureTitleImage.setLayoutParams(normalParams);
            }
        }
    }

    private void changePictureTextStyle(int size, int color) {
        pictureTitleText.setTextSize(size);
        pictureTitleText.setTextColor(color);
    }

    private void changeSongImageStyle(boolean focusable,boolean selected, int id) {
        if (focusable) {
            songTitleImage.setFocusable(true);
            songTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            songTitleImage.requestFocus();
            songTitleImage.setLayoutParams(focusParams);
        } else {
            songTitleImage.setFocusable(false);
            songTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            if(selected){
                songTitleImage.setLayoutParams(focusParams);
            }else{
                songTitleImage.setLayoutParams(normalParams);
            }
        }
    }

    private void changeSongTextStyle(int size, int color) {
        songTitleText.setTextSize(size);
        songTitleText.setTextColor(color);
    }

    private void changeOtherImageStyle(boolean focusable,boolean selected, int id) {
        if (focusable) {
            otherTitleImage.setFocusable(true);
            otherTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            otherTitleImage.requestFocus();
            otherTitleImage.setLayoutParams(focusParams);
        } else {
            otherTitleImage.setFocusable(false);
            otherTitleImage.setBackgroundDrawable(activity.getResources()
                    .getDrawable(id));
            if(selected){
                otherTitleImage.setLayoutParams(focusParams);
            }else{
                otherTitleImage.setLayoutParams(normalParams);
            }
        }
    }

    private void changeOtherTextStyle(int size, int color) {
        otherTitleText.setTextSize(size);
        otherTitleText.setTextColor(color);
    }

    private void changeFocus(int id) {
        listView.setFocusable(false);

        switch (id) {
        case R.id.otherTitleImage: {
            changeLeftFocus(activity.tmpType, Constants.OPTION_STATE_ALL);
            activity.tmpType = Constants.OPTION_STATE_ALL;

            break;
        }
        case R.id.pictureTitleImage: {
            changeLeftFocus(activity.tmpType, Constants.OPTION_STATE_PICTURE);
            activity.tmpType = Constants.OPTION_STATE_PICTURE;

            break;
        }
        case R.id.songTitleImage: {
            changeLeftFocus(activity.tmpType, Constants.OPTION_STATE_SONG);
            activity.tmpType = Constants.OPTION_STATE_SONG;

            break;
        }
        case R.id.videoTitleImage: {
            changeLeftFocus(activity.tmpType, Constants.OPTION_STATE_VIDEO);
            activity.tmpType = Constants.OPTION_STATE_VIDEO;

            break;
        }
        case R.id.videoFileList: {
            setLeftFocusable(false);
            listView.requestFocus();
            listView.setFocusable(true);
            activity.sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);

            break;
        }
        case R.id.leftArrowImg: {
            displayTip.setText(R.string.page_up);

            break;
        }
        case R.id.rightArrowImg: {
            displayTip.setText(R.string.page_down);

            break;
        }
        case R.id.divxset:{
            break;
        }
        }
    }

    private void setLeftFocusable(boolean flag) {
        otherTitleImage.setFocusable(flag);
        pictureTitleImage.setFocusable(flag);
        songTitleImage.setFocusable(flag);
        videoTitleImage.setFocusable(flag);
    }

    public void showDivx() {
         Log.i(TAG,"showDivx");
         DivxReg dr=new DivxReg(activity,btnSwitch );
         dr.showPopupMenu();
    }

    /************************************************************************
     * Pictures, music, video, and other icon click event monitoring processing
     * area
     ************************************************************************/
    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!activity.getCanResponse()) {
                return;
            }
            switch (v.getId()) {
            case R.id.pictureTitleImage: {
                if (activity.dataType == Constants.OPTION_STATE_PICTURE) {
                    // do nothing
                } else {
                    activity.dataType = Constants.OPTION_STATE_PICTURE;
                    handler.sendEmptyMessage(Constants.UPDATE_MEDIA_TYPE);
                }

                break;
            }
            case R.id.songTitleImage: {
                if (activity.dataType == Constants.OPTION_STATE_SONG) {
                    // do nothing
                } else {
                    activity.dataType = Constants.OPTION_STATE_SONG;
                    handler.sendEmptyMessage(Constants.UPDATE_MEDIA_TYPE);
                }

                break;
            }
            case R.id.videoTitleImage: {
                if (activity.dataType == Constants.OPTION_STATE_VIDEO) {
                    // do nothing
                } else {
                    activity.dataType = Constants.OPTION_STATE_VIDEO;
                    handler.sendEmptyMessage(Constants.UPDATE_MEDIA_TYPE);
                }

                break;
            }
            case R.id.otherTitleImage: {
                if (activity.dataType == Constants.OPTION_STATE_ALL) {
                    // do nothing
                } else {
                    activity.dataType = Constants.OPTION_STATE_ALL;
                    handler.sendEmptyMessage(Constants.UPDATE_MEDIA_TYPE);
                }

                break;
            }
            case R.id.leftArrowImg: {
                handler.sendEmptyMessage(Constants.MOUSE_PAGE_UP);
                break;
            }
            case R.id.rightArrowImg: {
                handler.sendEmptyMessage(Constants.MOUSE_PAGE_DOWN);
                break;
            }
            case R.id.divxset:
                               DivxReg dr=new DivxReg(activity,divxReg );
                dr.showPopupMenu();
                break;

            default:
                break;
            }

        }
    };

    private OnHoverListener onHoverListener = new OnHoverListener() {

        @Override
        public boolean onHover(View v, MotionEvent event) {
            if (activity.getCanResponse()) {
                int what = event.getAction();

                switch (what) {
                // The mouse to click the event
                case MotionEvent.ACTION_HOVER_ENTER: {
                    changeFocus(v.getId());
                    if(v.getId() == R.id.videoFileList){
                        isEnterListviewLocation = true ;
                    }else{
                        isEnterListviewLocation = false ;
                        clearItemViewFocus() ;
                    }
                    break;
                }
                // The right mouse button click
                case MotionEvent.ACTION_HOVER_EXIT: {
                    if (v.getId() != R.id.videoFileList) {
                        changeLeftFocus(activity.tmpType, 0);
                        setLeftFocus(activity.dataType, false);
                        activity.tmpType = activity.dataType;
                    }
                    break;
                }
                default:
                    break;
                }

                return false;

            } else {
                return true;
            }

        }
    };


    //refresh the focus state of listview item
    public void refreshItemFocusState(int index) {
        // TODO Auto-generated method stub
        Log.d(TAG, "refreshItemFocusState index = "+index) ;
        currentSelectedItemView =  listView.getChildAt(index);
        if(isEnterListviewLocation){
            if(oldSelectedItemView != null)
                oldSelectedItemView.setBackgroundResource(R.drawable.button_normal) ;
            if(currentSelectedItemView != null)
                currentSelectedItemView.setBackgroundResource(R.drawable.list_foucs);
            oldSelectedItemView = currentSelectedItemView ;
        }
    }

    public void clearFocusFromKey() {
        // TODO Auto-generated method stub
        clearItemViewFocus() ;
    }

    private void clearItemViewFocus() {
        // TODO Auto-generated method stub
        if(oldSelectedItemView != null){
            oldSelectedItemView.setBackgroundResource(R.drawable.button_normal) ;
            oldSelectedItemView = null ;
        }
        if(currentSelectedItemView != null){
            currentSelectedItemView.setBackgroundResource(R.drawable.button_normal);
            currentSelectedItemView = null ;
        }
    }

}
