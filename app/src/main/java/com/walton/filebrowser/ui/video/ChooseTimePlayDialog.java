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

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;

/**
 *
 * Optional time broadcast custom_text interface
 *
 * @author
 *
 * @since 1.0
 *
 * @date 2012-3-9
 */
public class ChooseTimePlayDialog extends Dialog {
    private static final String TAG = "ChooseTimePlayDialog";
    private static final int CHOOSE_TIME_ONE_TEXT = 0;
    private static final int CHOOSE_TIME_TWO_TEXT = 1;
    private static final int CHOOSE_TIME_THREE_TEXT = 2;
    private static final int CHOOSE_TIME_FOUR_TEXT = 3;
    private static final int CHOOSE_TIME_FIVE_TEXT = 4;
    private static final int CHOOSE_TIME_SIX_TEXT = 5;
    private Context context;
    private boolean mbNotSeek = false;
    private Handler mHandler;
    private int postion;// The current focus position
    private int size;
    // The remote control digital key key value and numeric value difference
    // value, temporary such under control
    public final static int DIFFERENCE = 7;
    EditText editText = null;
    // Time information on the current time
    private TextView videoTimeCurrentPositionTextView;
    private TextView videoTimeDurationTextView;

    public TextView getVideoTimeCurrentPositionTextView() {
        return videoTimeCurrentPositionTextView;
    }

    public TextView getVideoTimeDurationTextView() {
        return videoTimeDurationTextView;
    }

    // Storage setup time component list
    private List<EditText> videoTimeChooseList;

    public ChooseTimePlayDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public void setVariable(boolean mbNotSeek, Handler handler) {
        this.mbNotSeek = mbNotSeek;
        this.mHandler = handler;
    }

    public void setVariable(boolean mbNotSeek) {
        this.mbNotSeek = mbNotSeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_choose_time_play);
        setWindowsAttribute();
        initView();
        setListeners();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp, keyCode : " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            cancel();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown, keyCode : " + keyCode);
        return super.onKeyUp(keyCode, event);
    }

    private void initView() {
        // Optional time information time optional time information time
        videoTimeCurrentPositionTextView = (TextView) this
                .findViewById(R.id.videoCurrentTimeTextView);
        videoTimeDurationTextView = (TextView) this
                .findViewById(R.id.videoTotalDurationTextView);
        // Optional time play setup time component
        videoTimeChooseList = new ArrayList<EditText>();
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumOne));
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumTwo));
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumThree));
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumFour));
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumFive));
        videoTimeChooseList.add((EditText) this
                .findViewById(R.id.videoTimeChooseNumSix));
        size = videoTimeChooseList.size();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        int size = videoTimeChooseList.size();
        for (int i = 0; i < size; i++) {
            imm.hideSoftInputFromWindow(videoTimeChooseList.get(i)
                    .getWindowToken(), 0);
        }
    }

    private void setWindowsAttribute() {
        // instantiation new window
        Window w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                           WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // for the default display data
        // Display display = w.getWindowManager().getDefaultDisplay();
        // window's title is empty
        w.setTitle(null);
        // definition window width and height (no matter what resolution are so
        // big)
        // int width = 235;
        // int height = 140;
        // Settings window size
        // w.setLayout(width, height);
        // Settings window display position
        w.setGravity(Gravity.BOTTOM);
        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
    }

    private void setListeners() {
        // Optional time play interface monitoring events
        setTimeChooseKeyListener(chooseTimelistener);
        setTimeChooseTouchListener(chooseTimeTouchListener);
    }

    /**
     *
     * Optional time play for each EditText for monitoring
     *
     * @param listener
     */
    public void setTimeChooseKeyListener(View.OnKeyListener listener) {
        for (int i = 0; i < size; i++) {
            videoTimeChooseList.get(i).setOnKeyListener(listener);
        }
    }

    public void setTimeChooseFocusChangeListener(
            View.OnFocusChangeListener listener) {
        for (int i = 0; i < size; i++) {
            videoTimeChooseList.get(i).setOnFocusChangeListener(listener);
        }
    }

    public void setTimeChooseTouchListener(View.OnTouchListener listener) {
        for (int i = 0; i < size; i++) {
            videoTimeChooseList.get(i).setOnTouchListener(listener);
        }
    }

    private View.OnTouchListener chooseTimeTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //EditText editText = null;
            videoTimeChooseList.get(postion).setBackgroundColor(
                    Color.TRANSPARENT);
            switch (arg0.getId()) {
            case R.id.videoTimeChooseNumOne:
                postion = CHOOSE_TIME_ONE_TEXT;
                break;
            case R.id.videoTimeChooseNumTwo:
                postion = CHOOSE_TIME_TWO_TEXT;
                break;
            case R.id.videoTimeChooseNumThree:
                postion = CHOOSE_TIME_THREE_TEXT;
                break;
            case R.id.videoTimeChooseNumFour:
                postion = CHOOSE_TIME_FOUR_TEXT;
                break;
            case R.id.videoTimeChooseNumFive:
                postion = CHOOSE_TIME_FIVE_TEXT;
                break;
            case R.id.videoTimeChooseNumSix:
                postion = CHOOSE_TIME_SIX_TEXT;
                break;
            }
            editText = videoTimeChooseList.get(postion);
            editText.requestFocus();
            editText.setBackgroundResource(R.drawable.accurate_seek_focus);
            editText.addTextChangedListener(new TextWatcher(){

                int l=0;
                int location=0;
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    editText = videoTimeChooseList.get(postion);
                    if(l<s.toString().length()){
                        if(editText.getSelectionStart()!=2){
                            String temp = s.toString().substring(0,1);
                            Log.i("after text change","temp quence is "+temp+"location is "+editText.getSelectionStart());

                            editText.setText(temp);
                        }
                        else{
                            String temp = s.toString().substring(1,2);
                            Log.i("after text change","temp quence is "+temp+"location is "+editText.getSelectionStart());

                            editText.setText(temp);

                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                        int count, int after) {
                    // TODO Auto-generated method stub
                    l=s.length();
                    editText = videoTimeChooseList.get(postion);
                    location=editText.getSelectionStart();
                    Log.i("before text change","charsequence is "+s+"location is "+location);
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                        int before, int count) {

                 }

            });
            editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        dismiss();
                        return chooseTime();
                    }
                    return false;
                }

            });
            return false;
        }
    };

    private boolean chooseTime(){
        // Hours of ten
        String strHourTen = "0"
                + videoTimeChooseList.get(0).getText().toString();
        // Hours of bytes
        String strHourBit = "0"
                + videoTimeChooseList.get(1).getText().toString();
        // minutes of ten
        String strMinuteTen = "0"
                + videoTimeChooseList.get(2).getText().toString();
        // minutes of bytes
        String strMinuteBit = "0"
                + videoTimeChooseList.get(3).getText().toString();
        // seconds of ten
        String strSecondTen = "0"
                + videoTimeChooseList.get(4).getText().toString();
        // seconds of bytes
        String strSecondBit = "0"
                + videoTimeChooseList.get(5).getText().toString();
        int iHourTen = Integer.parseInt(strHourTen);
        int iHourBit = Integer.parseInt(strHourBit);
        int iMinuteTen = Integer.parseInt(strMinuteTen);
        int iMinuteBit = Integer.parseInt(strMinuteBit);
        int iSecondTen = Integer.parseInt(strSecondTen);
        int iSecondBit = Integer.parseInt(strSecondBit);
        // set hours
        int iHour = iHourTen * 10 + iHourBit;
        // set minutes
        int iMinute = iMinuteTen * 10 + iMinuteBit;
        // set seconds
        int iSecond = iSecondTen * 10 + iSecondBit;
        // for video of the total duration

        String time = videoTimeDurationTextView.getText()
                .toString();
        if (!mbNotSeek) {
            String [] values = time.split(":");
            int totalHour=0, totalMinute=0, totalSecond=0;
            if (values.length > 2) {
                totalHour = Integer.parseInt(values[0]);
                totalMinute = Integer.parseInt(values[1]);
                totalSecond = Integer.parseInt(values[2]);
            } else {
                totalMinute = Integer.parseInt(values[0]);
                totalSecond = Integer.parseInt(values[1]);
            }
            int videoTotalTime = totalHour * 3600 + totalMinute
                    * 60 + totalSecond;
            // Set the time (unit seconds)
            int iTotalTime = iHour * 3600 + iMinute * 60
                    + iSecond;
            if ((iHour <= totalHour) && (iMinute < 60)
                    && (iSecond < 60)
                    && (iTotalTime < videoTotalTime)) {
                Message message = new Message();
                message.what = Constants.CHOOSE_TIME;
                message.arg1 = 1000 * iTotalTime;
                mHandler.sendMessage(message);
                dismiss();
                return true;
            } else {
                showToast(context
                        .getString(R.string.choose_time_invalid));
                return true;
            }
        } else {
            showToast(context
                    .getString(R.string.choose_time_failed));
            return true;
        }
    }
    /**
     *
     * Optional time broadcast digital key monitoring
     */
    private View.OnKeyListener chooseTimelistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN: {
                    EditText editText = null;
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        videoTimeChooseList.get(postion).setBackgroundColor(Color.TRANSPARENT);
                        switch (postion) {
                            case CHOOSE_TIME_ONE_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumTwo);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_TWO_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_TWO_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumThree);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_THREE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_THREE_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumFour);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_FOUR_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_FOUR_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumFive);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_FIVE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_FIVE_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumSix);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_SIX_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_SIX_TEXT:
                                v.setNextFocusRightId(R.id.videoTimeChooseNumOne);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_ONE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                        }
                        postion++;
                        if (postion > 5) {
                            postion = 0;
                        }
                        // Focus on the left for the correct
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        videoTimeChooseList.get(postion).setBackgroundColor(Color.TRANSPARENT);
                        switch (postion) {
                            case CHOOSE_TIME_ONE_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumSix);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_SIX_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_TWO_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumOne);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_ONE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_THREE_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumTwo);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_TWO_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_FOUR_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumThree);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_THREE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_FIVE_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumFour);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_FOUR_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                            case CHOOSE_TIME_SIX_TEXT:
                                v.setNextFocusLeftId(R.id.videoTimeChooseNumFive);
                                editText = videoTimeChooseList
                                        .get(CHOOSE_TIME_FIVE_TEXT);
                                editText.setBackgroundResource(R.drawable.accurate_seek_focus);
                                break;
                        }
                        postion--;
                        if (postion < 0) {
                            postion = 5;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        String strOriValue = "0" + videoTimeChooseList.get(postion).getText().toString();
                        int oriValue = Integer.parseInt(strOriValue);
                        switch (postion) {
                            case CHOOSE_TIME_THREE_TEXT:
                            case CHOOSE_TIME_FIVE_TEXT:
                                if (oriValue == 5) {
                                    oriValue = 0;
                                } else {
                                    oriValue++;
                                }
                                break;
                            default:
                                if (oriValue == 9) {
                                    oriValue = 0;
                                } else {
                                    oriValue++;
                                }
                                break;
                        }
                        String strValue = Integer.toString(oriValue);
                        videoTimeChooseList.get(postion).setText(null);
                        videoTimeChooseList.get(postion).setText(strValue);

                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                         String strOriValue = "0" + videoTimeChooseList.get(postion).getText().toString();
                         int oriValue = Integer.parseInt(strOriValue);
                        switch (postion) {
                            case CHOOSE_TIME_THREE_TEXT:
                            case CHOOSE_TIME_FIVE_TEXT:
                                if (oriValue == 0) {
                                    oriValue = 5;
                                } else {
                                    oriValue--;
                                }
                                break;
                            default:
                                if (oriValue == 0) {
                                    oriValue = 9;
                                } else {
                                    oriValue--;
                                }
                                break;
                        }
                         String strValue = Integer.toString(oriValue);
                         videoTimeChooseList.get(postion).setText(null);
                         videoTimeChooseList.get(postion).setText(strValue);

                    } else if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER ) {
                        return chooseTime();
                    } else {
                        int value = keyCode - DIFFERENCE;
                        String strValue = Integer.toString(value);
                        // The remote control button set number
                        if ((keyCode < 17) && (keyCode > 6)) {
                            videoTimeChooseList.get(postion).setText(null);
                            videoTimeChooseList.get(postion).setText(strValue);
                        }
                    }
                    break;
                }
                case KeyEvent.ACTION_UP: {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        dismiss();
                        return true;
                    }
                }
                default:
                    break;
            }
            return true;
        }
    };

    public void clearChooseList() {
        int size = videoTimeChooseList.size();
        for (int i = 0; i < size; i++) {
            videoTimeChooseList.get(i).setText(null);
            videoTimeChooseList.get(i).setText("0");
        }
    }

    private void showToast(String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.show();
    }
}
