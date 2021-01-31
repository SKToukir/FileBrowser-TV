package com.walton.filebrowser.focus;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.walton.filebrowser.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFlyingFrameView extends View implements IFlying {

    private final static String TAG = "BaseFlowView";

    private static final int DEFAULT_ANIM_DURATION_TIME = 180;
    protected int mAnimDuration = DEFAULT_ANIM_DURATION_TIME;

    protected RectF mPaddingOfsetRectF = new RectF();
    protected RectF mPaddingRectF = new RectF();
    protected RectF mFrameRectF = new RectF();
    protected RectF mTempRectF = new RectF();

    private WeakReference<View> mOldViewReference;
    private AnimatorSet mAnimatorSet;

    private ObjectAnimator mTranslationXAnimator;
    private ObjectAnimator mTranslationYAnimator;
    private ObjectAnimator mWidthAnimator;
    private ObjectAnimator mHeightAnimator;

    protected boolean isFullScreen = false;

    public BaseFlyingFrameView(Context context) {
        this(context, null);
    }

    public BaseFlyingFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFlyingFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseFlyingFrameView);
        mAnimDuration = a.getInt(R.styleable.BaseFlyingFrameView_flyingDuration, DEFAULT_ANIM_DURATION_TIME);
        final int padding = a.getDimensionPixelOffset(R.styleable.BaseFlyingFrameView_flyingSpace, 0);
        mPaddingOfsetRectF.left = padding;
        mPaddingOfsetRectF.top = padding;
        mPaddingOfsetRectF.right = padding;
        mPaddingOfsetRectF.bottom = padding;
        a.recycle();
        init();
    }

    /**************************私有方法*******************************/
    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); //关闭硬件加速
        setVisibility(INVISIBLE);

    }

    /**
     * view缩放
     *
     * @param tagetView
     * @param scaleX
     * @param scaleY
     */
    private void scale(@Nullable View tagetView, final float scaleX, final float scaleY) {
        if (null == tagetView) {
            //ELog.d(TAG, "tagetView is null");
            return;
        }
        tagetView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mAnimDuration).start();

    }

    View child;
    View parentView;
    ViewParent parent;

    private void buildMoveAnimation(@NonNull View tagetView, final float scaleX, final float scaleY) {
        if (tagetView == null)
            return;
        final float paddingWidth = mPaddingRectF.left + mPaddingRectF.right + mPaddingOfsetRectF.left + mPaddingOfsetRectF.right;
        final float paddingHeight = mPaddingRectF.top + mPaddingRectF.bottom + mPaddingOfsetRectF.top + mPaddingOfsetRectF.bottom;
//        ELog.i(String.format("paddingWidth:%f,paddingHeight:%f", paddingWidth, paddingHeight));
        final int ofsetWidth = (int) (tagetView.getMeasuredWidth() * (scaleX - 1f) + paddingWidth);
        final int ofsetHeight = (int) (tagetView.getMeasuredHeight() * (scaleY - 1f) + paddingHeight);
        final Rect fromRect = findLocationWithView2(this);
        final Rect toRect = findLocationWithView2(tagetView);
//        ELog.v("toRect:" + toRect.toString());
        child = tagetView;
        while (child != null) {
            parent = child.getParent();
            if (parent == null || (!(parent instanceof View))) {
                break;
            }
            if (parent instanceof RecyclerView) {
//                ELog.i("parent:"+parent.getClass().getName());
                final RecyclerView rv = (RecyclerView) parent;
                final Object tag = rv.getTag();
                if (null != tag && tag instanceof Point) {
                    Point point = (Point) tag;
                    if (point.x != 0) {
                        toRect.offset(-point.x, 0);
                    } else if (point.y != 0) {
                        toRect.offset(0, -point.y);
                    }
                }
                //ELog.d(rv.getClass().getName() + "left:" + rv.getLeft() + ",top" + rv.getTop());
                toRect.offset(rv.getLeft(), rv.getTop());
            } else {
                parentView = (View) parent;
                if (parentView.getId() == Window.ID_ANDROID_CONTENT) {
                    //ELog.e("找到了状态栏");
                    toRect.top -= parentView.getTop();
                }
                int dx = parentView.getLeft() + parentView.getScrollX();
                int dy = parentView.getTop() + parentView.getScrollY();
                //ELog.d(parentView.getClass().getName() + "View left:" + parentView.getLeft() +
                //",top" + parentView.getTop());
                toRect.offset(dx, dy);
            }
            child = (View) child.getParent();
        }
        if (!isFullScreen) {
            //ELog.d("非全屏显示");
        }
        toRect.right = toRect.left + tagetView.getMeasuredWidth();
        toRect.bottom = toRect.top + tagetView.getMeasuredHeight();
        toRect.inset(-ofsetWidth / 2, -ofsetHeight / 2);


        final int newWidth = toRect.width();
        final int newHeight = toRect.height();
        final int newX = toRect.left - fromRect.left;
        final int newY = toRect.top - fromRect.top;

//        ELog.v(TAG, String.format("newWidth:%d,newHeight:%d", newWidth, newHeight));
//        ELog.v(TAG, String.format("newX:%d,newY:%d", newX, newY));

        final List<Animator> together = new ArrayList<>();
        //final List<Animator> appendTogether = getTogetherAnimators(newX, newY, newWidth,
//                newHeight, scaleX, scaleY);
//        if (null != appendTogether && !appendTogether.isEmpty()) {
//            together.addAll(appendTogether);
//        }

        together.add(getTranslationXAnimator(newX));
        together.add(getTranslationYAnimator(newY));
        together.add(getWidthAnimator(newWidth));
        together.add(getHeightAnimator(newHeight));

//        final List<Animator> sequentially = new ArrayList<>();
        //final List<Animator> appendSequentially = getSequentiallyAnimators(newX, newY, newWidth,
        //        newHeight, scaleX, scaleY);
//        if (null != appendSequentially && !appendSequentially.isEmpty()) {
//            sequentially.addAll(appendSequentially);
//        }

        mAnimatorSet = new AnimatorSet();
        //mAnimatorSet.setInterpolator(interpolator);
        mAnimatorSet.playTogether(together);
//        mAnimatorSet.playSequentially(sequentially);
    }


    private ObjectAnimator getTranslationXAnimator(float x) {

        if (null == mTranslationXAnimator) {
            mTranslationXAnimator = ObjectAnimator.ofFloat(this, "translationX", x)
                    .setDuration(mAnimDuration);
        } else {
            mTranslationXAnimator.setFloatValues(x);
        }
        return mTranslationXAnimator;
    }

    private ObjectAnimator getTranslationYAnimator(float y) {
        if (null == mTranslationYAnimator) {
            mTranslationYAnimator = ObjectAnimator.ofFloat(this, "translationY", y)
                    .setDuration(mAnimDuration);
        } else {
            mTranslationYAnimator.setFloatValues(y);
        }
        return mTranslationYAnimator;
    }

    private ObjectAnimator getHeightAnimator(int height) {
        if (null == mHeightAnimator) {
            mHeightAnimator = ObjectAnimator.ofInt(this, "height", getMeasuredHeight(), height)
                    .setDuration(mAnimDuration);
        } else {
            mHeightAnimator.setIntValues(getMeasuredHeight(), height);
        }
        return mHeightAnimator;
    }

    private ObjectAnimator getWidthAnimator(int width) {
        if (null == mWidthAnimator) {
            mWidthAnimator = ObjectAnimator.ofInt(this, "width", getMeasuredWidth(), width)
                    .setDuration(mAnimDuration);
        } else {
            mWidthAnimator.setIntValues(getMeasuredWidth(), width);
        }
        return mWidthAnimator;
    }

//    abstract List<Animator> getTogetherAnimators(int newX, int newY, int newWidth, int newHeight, float scaleX, float scaleY);
//
//
//    abstract List<Animator> getSequentiallyAnimators(int newX, int newY, int newWidth, int newHeight, float scaleX, float scaleY);


    private void runFocusViewAnimation(@NonNull View tagetView, final float scaleX, final float scaleY) {
        setVisibility(View.VISIBLE);
        scale(tagetView, scaleX, scaleY);
        runBorderAnimation(tagetView, scaleX, scaleY);
    }

    protected void runBorderAnimation(@NonNull View tagetView, final float scaleX, final float scaleY) {
        if (null != mAnimatorSet && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        buildMoveAnimation(tagetView, scaleX, scaleY);
        mAnimatorSet.start();
    }


    protected Rect findLocationWithView2(@NonNull View view) {
        ViewGroup root = (ViewGroup) view.getParent();
        Rect rect = new Rect();
        root.offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    /**
     * 属性动画用到的方法
     **/
    protected void setWidth(int width) {
        if (getLayoutParams().width != width) {
            getLayoutParams().width = width;
            requestLayout();
        }
    }

    protected void setHeight(int height) {
        if (getLayoutParams().height != height) {
            getLayoutParams().height = height;
            requestLayout();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            //ELog.v(TAG, String.format("w:%d,h:%d;oldw:%d,oldh:%d", w, h, oldw, oldh));
            mFrameRectF.set(mPaddingRectF.left, mPaddingRectF.top, w - mPaddingRectF.right, h - mPaddingRectF.bottom);
        }
    }

    @Override
    public void onMoveTo(@NonNull View focusView, float scaleX, float scaleY, float raduis) {
        if (null != mOldViewReference && null != mOldViewReference.get()) {
            if (focusView != null && focusView.equals(mOldViewReference.get())) {

            } else {

            }
            scale(mOldViewReference.get(), 1f, 1f);
            mOldViewReference.clear();
        }
        runFocusViewAnimation(focusView, scaleX, scaleY);
        mOldViewReference = new WeakReference<>(focusView);
    }

    public void setAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    public void setPadding(int padding) {
        mPaddingOfsetRectF.left = padding;
        mPaddingOfsetRectF.top = padding;
        mPaddingOfsetRectF.right = padding;
        mPaddingOfsetRectF.bottom = padding;
    }
}
