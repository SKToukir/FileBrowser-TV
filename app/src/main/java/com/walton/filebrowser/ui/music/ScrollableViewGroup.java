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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollableViewGroup extends ViewGroup {
    private static final int INVALID_SCREEN = -1;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int mDefaultScreen;
    private int mCurrentScreen;
    private int mNextScreen = INVALID_SCREEN;
    private Scroller mScroller;
    private int mTouchState;
    private boolean mFirstLayout = true;
    private int mPaintFlag = 0;
    private OnCurrentViewChangedListener mOnCurrentViewChangedListener;

    public interface OnCurrentViewChangedListener {
        public void onCurrentViewChanged(View view, int currentview);
    }

    public ScrollableViewGroup(Context context) {
        super(context);
        initViewGroup();
    }

    public ScrollableViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewGroup();
    }

    public ScrollableViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViewGroup();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                child.layout(childLeft, 0, childLeft + childWidth,
                        child.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    /**
     *
     * Initializes various states for this viewgroup.
     */
    private void initViewGroup() {
        mScroller = new Scroller(getContext());
        mCurrentScreen = mDefaultScreen;
    }

    public boolean isDefaultViewShowing() {
        return mCurrentScreen == mDefaultScreen;
    }

    public int getCurrentView() {
        return mCurrentScreen;
    }

    public void setCurrentView(int currentView) {
        Log.d("qqqq", "here:view " + currentView);
        mCurrentScreen = Math
                .max(0, Math.min(currentView, getChildCount() - 1));
        scrollTo(mCurrentScreen * getWidth(), 0);
        if (mOnCurrentViewChangedListener != null)
            mOnCurrentViewChangedListener.onCurrentViewChanged(this,
                    mCurrentScreen);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int currx = mScroller.getCurrX(), curry = mScroller
                    .getCurrY(), scrx = getScrollX(), scry = getScrollY();
            if (currx != scrx || curry != scry)
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            else
                invalidate();
        } else if (mNextScreen != INVALID_SCREEN) {
            mCurrentScreen = Math.max(0,
                    Math.min(mNextScreen, getChildCount() - 1));
            mNextScreen = INVALID_SCREEN;
            mPaintFlag = 0;
            clearChildrenCache();
            final int scrx = getScrollX(), scry = getScrollY(), mCurrentScrollX = mCurrentScreen
                    * getWidth();
            if (scrx != mCurrentScrollX)
                scrollTo(mCurrentScrollX, scry);
        }
        if (mOnCurrentViewChangedListener != null)
            mOnCurrentViewChangedListener.onCurrentViewChanged(this,
                    mCurrentScreen);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING
                && mNextScreen == INVALID_SCREEN;
        // If we are not scrolling or flinging, draw only the current screen
        if (fastDraw) {
            drawChild(canvas, getChildAt(mCurrentScreen), getDrawingTime());
        } else {
            final long drawingTime = getDrawingTime();
            // If we are flinging, draw only the current screen and the target
            // screen
            if (mNextScreen >= 0
                    && mNextScreen < getChildCount()
                    && (Math.abs(mCurrentScreen - mNextScreen) == 1 || mPaintFlag != 0)) {
                final View viewCurrent = getChildAt(mCurrentScreen), viewNext = getChildAt(mNextScreen);
                drawChild(canvas, viewCurrent, drawingTime);
                if (mPaintFlag == 0) {
                    drawChild(canvas, viewNext, drawingTime);
                } else {
                    Paint paint = new Paint();
                    if (mPaintFlag < 0) {
                        canvas.drawBitmap(viewNext.getDrawingCache(),
                                -viewNext.getWidth(), viewNext.getTop(), paint);
                    } else {
                        canvas.drawBitmap(viewNext.getDrawingCache(),
                                getWidth() * getChildCount(),
                                viewNext.getTop(), paint);
                    }
                }
            } else {
                // If we are scrolling, draw all of our children
                final int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    drawChild(canvas, getChildAt(i), drawingTime);
                }
                if (mPaintFlag != 0) {
                    final View viewNext;
                    Paint paint = new Paint();
                    if (mPaintFlag < 0) {
                        viewNext = getChildAt(getChildCount() - 1);
                        canvas.drawBitmap(viewNext.getDrawingCache(),
                                -viewNext.getWidth(), viewNext.getTop(), paint);
                    } else {
                        viewNext = getChildAt(0);
                        canvas.drawBitmap(viewNext.getDrawingCache(),
                                getWidth() * getChildCount(),
                                viewNext.getTop(), paint);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        if (mFirstLayout) {
            scrollTo(mCurrentScreen * width, 0);
            mFirstLayout = false;
        }
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
            boolean immediate) {
        int screen = indexOfChild(child);
        if (screen != mCurrentScreen || !mScroller.isFinished()) {
            snapToScreen(screen);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction,
            Rect previouslyFocusedRect) {
        int focusableScreen;
        if (mNextScreen != INVALID_SCREEN) {
            focusableScreen = mNextScreen;
        } else {
            focusableScreen = mCurrentScreen;
        }
        getChildAt(focusableScreen).requestFocus(direction,
                previouslyFocusedRect);
        return false;
    }

    private void enableChildrenCache() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View layout = getChildAt(i);
            layout.setDrawingCacheEnabled(true);
            if (layout instanceof ViewGroup) {
                ((ViewGroup) layout).setAlwaysDrawnWithCacheEnabled(true);
            }
        }
    }

    private void clearChildrenCache() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View layout = getChildAt(i);
            if (layout instanceof ViewGroup) {
                ((ViewGroup) layout).setAlwaysDrawnWithCacheEnabled(false);
            }
        }
    }

    private void snapToScreen(int whichScreen) {
        if (!mScroller.isFinished())
            return;
        enableChildrenCache();
        final int viewCount = getChildCount() - 1;
        final int oldWhichScreen = whichScreen;
        if (whichScreen < 0) {
            whichScreen = viewCount;
            mPaintFlag = -1;
            // next screen should be painted before current
        } else if (whichScreen > viewCount) {
            whichScreen = 0;
            mPaintFlag = 1;
        } else {
            mPaintFlag = 0;
        }
        boolean changingScreens = whichScreen != mCurrentScreen;
        mNextScreen = whichScreen;
        View focusedChild = getFocusedChild();
        if (focusedChild != null && changingScreens
                && focusedChild == getChildAt(mCurrentScreen)) {
            focusedChild.clearFocus();
        }
        final int newX = oldWhichScreen * getWidth();
        final int delta = newX - getScrollX();
        mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
        invalidate();
    }

    public void moveToDefaultScreen() {
        snapToScreen(mDefaultScreen);
        getChildAt(mDefaultScreen).requestFocus();
    }

    public boolean isScrollFinish() {
        return mTouchState != TOUCH_STATE_SCROLLING
                && mNextScreen == INVALID_SCREEN;
    }

    public OnCurrentViewChangedListener getOnCurrentViewChangedListener() {
        return mOnCurrentViewChangedListener;
    }

    public void setOnCurrentViewChangedListener(
            OnCurrentViewChangedListener mOnCurrentViewChangedListener) {
        this.mOnCurrentViewChangedListener = mOnCurrentViewChangedListener;
    }
}
