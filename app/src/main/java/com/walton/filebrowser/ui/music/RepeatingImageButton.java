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
package com.walton.filebrowser.ui.music;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 *
 * This custom_text major is to prevent crazy click to make the corresponding
 * treatment
 */
public class RepeatingImageButton extends ImageButton {
    private long jrmtTime;
    private int mRepeatCount;
    private RepeatListener mListener;
    private long mInterval = 500;

    public RepeatingImageButton(Context context) {
        this(context, null);
    }

    public RepeatingImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.imageButtonStyle);
    }

    public RepeatingImageButton(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        setLongClickable(true);
    }

    /**
     *
     * Sets the listener to be called while the button is pressed and the
     *
     * interval in milliseconds with which it will be called.
     *
     * @param l
     *            The listener that will be called
     *
     * @param interval
     *            The interval in milliseconds for calls
     */
    public void setRepeatListener(RepeatListener l, long interval) {
        mListener = l;
        mInterval = interval;
    }

    @Override
    public boolean performLongClick() {
        jrmtTime = SystemClock.elapsedRealtime();
        mRepeatCount = 0;
        post(mRepeater);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // remove the repeater, but call the hook one more time
            removeCallbacks(mRepeater);
            if (jrmtTime != 0) {
                doRepeat(true);
                jrmtTime = 0;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:
            // need to call super to make long press work, but return
            // true so that the application doesn't get the down event.
            super.onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_CENTER:
        case KeyEvent.KEYCODE_ENTER:
            // remove the repeater, but call the hook one more time
            removeCallbacks(mRepeater);
            if (jrmtTime != 0) {
                doRepeat(true);
                jrmtTime = 0;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private Runnable mRepeater = new Runnable() {
        public void run() {
            doRepeat(false);
            if (isPressed()) {
                postDelayed(this, mInterval);
            }
        }
    };

    private void doRepeat(boolean last) {
        long now = SystemClock.elapsedRealtime();
        if (mListener != null) {
            mListener
                    .onRepeat(this, now - jrmtTime, last ? -1 : mRepeatCount++);
        }
    }

    public interface RepeatListener {
        /**
         * This method will be called repeatedly at roughly the interval
         * specified in setRepeatListener(), for as long as the button is
         * pressed.
         * @param v
         *            The button as a View.
         *
         * @param duration
         *            The number of milliseconds the button has been
         *            pressed so far.
         *
         * @param repeatcount
         *            The number of previous calls in this sequence. If
         *            this is going to be the last call in this sequence (i.e.
         *            the user just stopped pressing the button), the value will
         *            be -1.
         */
        void onRepeat(View v, long duration, int repeatcount);
    }
}
