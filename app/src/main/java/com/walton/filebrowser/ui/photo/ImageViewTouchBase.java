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
package com.walton.filebrowser.ui.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.walton.filebrowser.R;


public class ImageViewTouchBase extends ImageView {
    private static final String TAG = "ImageViewTouchBase";
    // This is the base transformation which is used to show the image
    // initially. The current computation for this shows the image in
    // it's entirety, letterboxing as needed. One could choose to
    // show the image as cropped instead.
    //
    // This matrix is recomputed when we go from the thumbnail image to
    // the full size image.
    protected Matrix mBaseMatrix = new Matrix();
    // This is the supplementary transformation which reflects what
    // the user has done in terms of zooming and panning.
    //
    // This matrix remains the same when we go from the thumbnail image
    // to the full size image.
    protected Matrix mSuppMatrix = new Matrix();
    // This is the final matrix which is computed as the concatentation
    // of the base matrix and the supplementary matrix.
    private final Matrix mDisplayMatrix = new Matrix();
    // Temporary buffer used for getting the values out of a matrix.
    private final float[] mMatrixValues = new float[9];
    // The current bitmap being displayed.
    protected final RotateBitmap mBitmapDisplayed = new RotateBitmap(null);
    int mThisWidth = -1, mThisHeight = -1;
    private float mViewWidth = 0, mViewHeight = 0;
    public int mRotateCounter = 0;
    private boolean mCanMove = false;

    // float mMaxZoom;
    // float mMinZoom;
    // ImageViewTouchBase will pass a Bitmap to the Recycler if it has finished
    // its use of that Bitmap.
    public interface Recycler {
        public void recycle(Bitmap b);
    }

    public void setRecycler(Recycler r) {
        mRecycler = r;
    }

    private Recycler mRecycler;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        mThisWidth = right - left;
        mThisHeight = bottom - top;
        Runnable r = mOnLayoutRunnable;
        if (r != null) {
            mOnLayoutRunnable = null;
            r.run();
        }
        if (mBitmapDisplayed.getBitmap() != null) {
            getProperBaseMatrix(mBitmapDisplayed, mBaseMatrix);
            setImageMatrix(getImageViewMatrix());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown, keyCode : " + keyCode + " mCanMove : "
                + mCanMove);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp, keyCode : " + keyCode + " mCanMove : " + mCanMove);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            if (getScale() > 1.0f) {
                // If we're zoomed in, pressing Back jumps out to show the
                // entire image, otherwise Back returns the user to the gallery.
                zoomTo(1.0f);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mCanMove = !mCanMove;
        }
        // can move
        if (mCanMove) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(TAG, "move up");
                moveDirection(KeyEvent.KEYCODE_DPAD_UP);
                setImageMatrix(mDisplayMatrix);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.d(TAG, "move down");
                moveDirection(KeyEvent.KEYCODE_DPAD_DOWN);
                setImageMatrix(mDisplayMatrix);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d(TAG, "move left");
                moveDirection(KeyEvent.KEYCODE_DPAD_LEFT);
                setImageMatrix(mDisplayMatrix);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d(TAG, "move right");
                moveDirection(KeyEvent.KEYCODE_DPAD_RIGHT);
                setImageMatrix(mDisplayMatrix);
                break;
            default:
                break;
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    protected Handler mHandler = new Handler();
    public int mLastXTouchPos;
    public int mLastYTouchPos;

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, 0);
    }

    private void setImageBitmap(Bitmap bitmap, int rotation) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }
        Bitmap old = mBitmapDisplayed.getBitmap();
        mBitmapDisplayed.setBitmap(bitmap);
        mBitmapDisplayed.setRotation(rotation);
        if (old != null && old != bitmap && mRecycler != null) {
            mRecycler.recycle(old);
        }
    }

    public void clear() {
        setImageBitmapResetBase(null, true);
    }

    private Runnable mOnLayoutRunnable = null;

    // This function changes bitmap, reset base matrix according to the size
    // of the bitmap, and optionally reset the supplementary matrix.
    public void setImageBitmapResetBase(final Bitmap bitmap,
            final boolean resetSupp) {
        setImageRotateBitmapResetBase(new RotateBitmap(bitmap), resetSupp, null);
    }

    public void setImageRotateBitmapResetBase(final RotateBitmap bitmap,
            final boolean resetSupp, PhotoPlayerActivity photoPlayActivity) {
        final int viewWidth = getWidth();
        Log.i(TAG, "showBitmap, viewWidth : " + viewWidth);
        if (viewWidth <= 0) {
            mOnLayoutRunnable = new Runnable() {
                public void run() {
                    setImageRotateBitmapResetBase(bitmap, resetSupp, null);
                }
            };
            return;
        }
        Bitmap tempBitmap = bitmap.getBitmap();
        Log.i(TAG, "showBitmap, viewWidth : " + tempBitmap);
        if (tempBitmap != null) {
            getProperBaseMatrix(bitmap, mBaseMatrix);
            setImageBitmap(tempBitmap, bitmap.getRotation());
            if ((photoPlayActivity != null)
                    && (photoPlayActivity.isDefaultPhoto)) {
                photoPlayActivity.showToastAtCenter(photoPlayActivity
                        .getString(R.string.can_not_decode));
            }
            tempBitmap = null;
            System.gc();
        } else {
            mBaseMatrix.reset();
            setImageBitmap(null);
        }
        if (resetSupp) {
            mSuppMatrix.reset();
        }
        setImageMatrix(getImageViewMatrix());
        // mMaxZoom = maxZoom();
        // mMinZoom = minZoom();
    }

    // Center as much as possible in one or both axis. Centering is
    // defined as follows: if the image is scaled down below the
    // view's dimensions then center it (literally). If the image
    // is scaled larger than the view and is translated out of view
    // then translate it back into view (i.e. eliminate black bars).
    protected void center(boolean horizontal, boolean vertical) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        Matrix m = getImageViewMatrix();
        RectF rect = new RectF(0, 0, mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            int viewHeight = getHeight();
            if (height < viewHeight) {
                deltaY = (viewHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < viewHeight) {
                deltaY = getHeight() - rect.bottom;
            }
        }
        if (horizontal) {
            int viewWidth = getWidth();
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }
        postTranslate(deltaX, deltaY);
        setImageMatrix(getImageViewMatrix());
    }

    public ImageViewTouchBase(Context context) {
        super(context);
        init();
    }

    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
    }

    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    // Get the scale factor out of the matrix.
    protected float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    public float getScale() {
        return getScale(mSuppMatrix);
    }

    // Setup the base matrix so that the image is centered and scaled properly.
    private void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        matrix.reset();
        // We limit up-scaling to 3x otherwise the result may look bad if it's
        // a small icon.
        float widthScale = Math.min(viewWidth / w, 3.0f);
        float heightScale = Math.min(viewHeight / h, 3.0f);
        float scale = Math.min(widthScale, heightScale);
        matrix.postConcat(bitmap.getRotateMatrix());
        matrix.postScale(scale, scale);
        matrix.postTranslate((viewWidth - w * scale) / 2F, (viewHeight - h
                * scale) / 2F);
    }

    // Combine the base matrix and the supp matrix to make the final matrix.
    protected Matrix getImageViewMatrix() {
        // The final matrix is computed as the concatentation of the base matrix
        // and the supplementary matrix.
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        return mDisplayMatrix;
    }

    static final float SCALE_RATE = 1.25F;

    // Sets the maximum zoom, which is a scale relative to the base matrix. It
    // is calculated to show the image at 400% zoom regardless of screen or
    // image orientation. If in the future we decode the full 3 megapixel image,
    // rather than the current 1024x768, this should be changed down to 200%.
    protected float maxZoom() {
        if (mBitmapDisplayed.getBitmap() == null) {
            return 1F;
        }
        float fw = (float) mBitmapDisplayed.getWidth() / (float) mThisWidth;
        float fh = (float) mBitmapDisplayed.getHeight() / (float) mThisHeight;
        float max = Math.max(fw, fh) * 4;
        return max;
    }

    protected float minZoom() {
        if (mBitmapDisplayed.getBitmap() == null) {
            return 1F;
        }
        float fw = (float) mBitmapDisplayed.getWidth() / (float) mThisWidth;
        float fh = (float) mBitmapDisplayed.getHeight() / (float) mThisHeight;
        float min = Math.max(fw, fh) / 4;
        return min;
    }

    protected void zoomTo(float scale, float centerX, float centerY) {
        // if (scale > mMaxZoom) {
        // scale = mMaxZoom;
        // }
        float oldScale = getScale();
        float deltaScale = scale / oldScale;
        mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    protected void zoomTo(final float scale, final float centerX,
            final float centerY, final float durationMs) {
        final float incrementPerMs = (scale - getScale()) / durationMs;
        final float oldScale = getScale();
        final long startTime = System.currentTimeMillis();
        mHandler.post(new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();
                float currentMs = Math.min(durationMs, now - startTime);
                float target = oldScale + (incrementPerMs * currentMs);
                zoomTo(target, centerX, centerY);
                if (currentMs < durationMs) {
                    mHandler.post(this);
                }
            }
        });
    }

    public void zoomTo(float scale) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        zoomTo(scale, cx, cy);
    }

    protected void zoomToPoint(float scale, float pointX, float pointY) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        panBy(cx - pointX, cy - pointY);
        zoomTo(scale, cx, cy);
    }

    public void zoomIn() {
        zoomIn(SCALE_RATE);
    }

    public void zoomOut() {
        zoomOut(SCALE_RATE);
    }

    public void rotateImage(float f) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        mSuppMatrix.postRotate(f);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    /**
     *
     * narrow
     *
     * @param rate
     */
    protected void zoomIn(float rate) {
        // Log.i("ff", "enlarge getScale(tmp):" + getScale());
        // if (getScale() >= 1.6F) {
        // return; // Don't let the user zoom into the molecular level.
        // }
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        mSuppMatrix.postScale(rate, rate, cx, cy);
        prepareDisplayMatrix();
        setImageMatrix(mDisplayMatrix);
        // setImageMatrix(getImageViewMatrix());
    }

    /**
     *
     * bigger
     *
     * @param rate
     */
    protected void zoomOut(float rate) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
        prepareDisplayMatrix();
        setImageMatrix(mDisplayMatrix);
    }

    protected void postTranslate(float dx, float dy) {
        mSuppMatrix.postTranslate(dx, dy);
    }

    protected void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }

    public void rotateImageLeft(int zoomTimes) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        set2Original(zoomTimes);
        --mRotateCounter;
        mSuppMatrix.postRotate(-90.0f);
        prepareDisplayMatrix();
        setImageMatrix(mDisplayMatrix);
    }

    public void rotateImageRight(int zoomTimes) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }
        set2Original(zoomTimes);
        ++mRotateCounter;
        mSuppMatrix.postRotate(90.0f);
        prepareDisplayMatrix();
        setImageMatrix(mDisplayMatrix);
    }

    private void prepareDisplayMatrix() {
        float imgW = mBitmapDisplayed.getBitmap().getWidth();
        float imgH = mBitmapDisplayed.getBitmap().getHeight();
        float widthScale, heightScale;
        if (Math.abs(mRotateCounter) % 2 == 1) {
            widthScale = Math.min(mViewWidth / imgH, 3.0f);
            heightScale = Math.min(mViewHeight / imgW, 3.0f);
        } else {
            widthScale = Math.min(mViewWidth / imgW, 3.0f);
            heightScale = Math.min(mViewHeight / imgH, 3.0f);
        }
        float scale = Math.min(widthScale, heightScale);
        // Log.d("ImageViewBase","counter:"+mRotateCounter+",widthScale:"+widthScale+",heightScale:"+heightScale);
        mBaseMatrix.reset();
        mBaseMatrix.postConcat(new Matrix());
        mBaseMatrix.postScale(scale, scale);
        mBaseMatrix.postTranslate((mViewWidth - imgW * scale) / 2F,
                (mViewHeight - imgH * scale) / 2F);
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        RectF rect = new RectF(0, 0, imgW, imgH);
        // Log.d("ImageViewBase","lf:"+rect.left+",tp:"+rect.top+",rg:"+rect.right+",bm:"+rect.bottom);
        mDisplayMatrix.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        // Log.d("ImageViewBase","rglf:"+rect.left+",tp:"+rect.top+",rg:"+rect.right+",bm:"+rect.bottom);
        if (height < mViewHeight) {
            deltaY = (mViewHeight - height) / 2 - rect.top;
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < mViewHeight) {
            deltaY = mViewHeight - rect.bottom;
        }
        if (width < mViewWidth) {
            deltaX = (mViewWidth - width) / 2 - rect.left;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
        } else if (rect.right < mViewWidth) {
            deltaX = mViewWidth - rect.right;
        }
        mDisplayMatrix.postTranslate(deltaX, deltaY);
    }

    private void set2Original(int zoomTimes) {
        float cx = mViewWidth / 2F;
        float cy = mViewHeight / 2F;
        float oldScaleX = getValue(mSuppMatrix, Matrix.MSCALE_X);
        // Log.d("ImageViewBase","set2Original oldScaleX:"+oldScaleX+",zoomTimes:"+zoomTimes);
        if (oldScaleX > 0) {
            mSuppMatrix.postScale(1 / oldScaleX, 1 / oldScaleX, cx, cy);
        } else if (zoomTimes > 0) {
            for (int i = 0; i < zoomTimes; i++)
                mSuppMatrix.postScale(1 / SCALE_RATE, 1 / SCALE_RATE, cx, cy);
        } else if (zoomTimes < 0) {
            for (int i = 0; i < -zoomTimes; i++)
                mSuppMatrix.postScale(SCALE_RATE, SCALE_RATE, cx, cy);
        } else {
            return;
        }
        prepareDisplayMatrix();
        setImageMatrix(mDisplayMatrix);
    }

    private void moveDirection(int keyCode) {
        float deltaX = 0, deltaY = 0;
        if (mBitmapDisplayed.getBitmap() == null)
            return;
        RectF rect = new RectF(0, 0, mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        // Log.d("ImageViewBase","moveDirection lf:"+rect.left+",tp:"+rect.top+",rg:"+rect.right+",bm:"+rect.bottom);
        mDisplayMatrix.mapRect(rect);
        // Log.d("ImageViewBase","moveDirection rglf:"+rect.left+",tp:"+rect.top+",rg:"+rect.right+",bm:"+rect.bottom);
        /*
         *
         * 1920 0 1080 0 r1.25 2040 120 1215 135 2310 390 1384 304 2648 728 1595
         *
         * 515
         */
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            if (rect.bottom > 100 + mViewHeight) {
                deltaY = -100;
            } else if (rect.bottom > mViewHeight) {
                deltaY = -rect.bottom + mViewHeight;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if (0 > 100 + rect.top) {
                deltaY = 100;
            } else if (0 > rect.top) {
                deltaY = -rect.top;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (rect.right > 100 + mViewWidth) {
                deltaX = -100;
            } else if (rect.right > mViewWidth) {
                deltaX = -rect.right + mViewWidth;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (0 > 100 + rect.left) {
                deltaX = 100;
            } else if (0 > rect.left) {
                deltaX = -rect.left;
            }
            break;
        default:
            System.err.println("default is click!!");
            break;
        }
        // Log.d("ImageViewBase","moveDirection deltaX:"+deltaX+",deltaY:"+deltaY);
        mDisplayMatrix.postTranslate(deltaX, deltaY);
    }

    public void setMoveFlag(boolean value) {
        mCanMove = value;
    }

    public boolean getMoveFlag() {
        return mCanMove;
    }

    public void resetRotateCounter() {
        mRotateCounter = 0;
    }
}
