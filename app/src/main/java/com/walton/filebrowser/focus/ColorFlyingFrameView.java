package com.walton.filebrowser.focus;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.walton.filebrowser.R;

public class ColorFlyingFrameView extends BaseFlyingFrameView {

    public final static int SHAPE_MODE_RECTANGE = 1;
    public final static int SHAPE_MODE_ROUND_RECTANGE = 1 << 2;
    public final static int SHAPE_MODE_CICRLE = 1 << 3;

    //阴影
    private Paint mShadowPaint;
    private int mShadowColor;
    private float mShadowWidth;
    //边框
    private float mBorderWidth;
    private float mRoundRadius;
    private int mBorderColor;
    private Paint mBorderPaint;

    private int shapeMode = SHAPE_MODE_CICRLE;

    public void setShapeMode(int shapeMode) {
        this.shapeMode = shapeMode;
    }

    public ColorFlyingFrameView(Context context) {
        this(context, null);
    }

    public ColorFlyingFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFlyingFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorFlyingFrameView);
        mBorderWidth = a.getDimensionPixelOffset(R.styleable.ColorFlyingFrameView_flyingBorderWidth,
                getContext().getResources().getDimensionPixelOffset(R.dimen.px4));
        mBorderColor = a.getColor(R.styleable.ColorFlyingFrameView_flyingBorderColor, 0xFF97C7FE);
        mShadowColor = a.getColor(R.styleable.ColorFlyingFrameView_flyingShadowColor, 0xFF888888);
        mShadowWidth = a.getDimensionPixelOffset(R.styleable.ColorFlyingFrameView_flyingShadWidth,
                getContext().getResources().getDimensionPixelOffset(R.dimen.px16));
        mRoundRadius = a.getDimensionPixelOffset(R.styleable.ColorFlyingFrameView_flyingRadius, getContext().getResources().getDimensionPixelOffset(R.dimen.px8));
        a.recycle();
        init();
    }

    private void init() {
        final float padding = mShadowWidth + mBorderWidth;
        mPaddingRectF.set(padding, padding, padding, padding);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setDither(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        //mBorderPaint.setMaskFilter(new BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL));

        if (mShadowWidth > 0) {
            mShadowPaint = new Paint();
            mShadowPaint.setColor(mShadowColor);
            mShadowPaint.setAntiAlias(true); //抗锯齿功能，会消耗较大资源，绘制图形速度会变慢
            mShadowPaint.setDither(true);    //抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
            mShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowWidth, BlurMaskFilter.Blur.OUTER));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShadowPaint != null) {
            drawShadow(canvas);
        }
        drawBroader(canvas);
        super.onDraw(canvas);

    }

    /**
     * 绘制外发光阴影
     *
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        if (mShadowWidth > 0) {
            canvas.save();

            //裁剪处理(使阴影矩形框内变为透明)
            if (shapeMode == SHAPE_MODE_CICRLE) {
                canvas.drawCircle(mFrameRectF.centerX(), mFrameRectF.centerY(), mFrameRectF.width()
                        / 2, mShadowPaint);
            } else if (shapeMode == SHAPE_MODE_RECTANGE) {
                canvas.clipRect(0, 0, getWidth(), getHeight());
                mTempRectF.set(mFrameRectF);
                mTempRectF.inset(mRoundRadius / 2f, mRoundRadius / 2f);
                canvas.clipRect(mTempRectF, Region.Op.DIFFERENCE);
                canvas.drawRect(mFrameRectF, mShadowPaint);
            } else if (shapeMode == SHAPE_MODE_ROUND_RECTANGE) {
                canvas.clipRect(0, 0, getWidth(), getHeight());
                mTempRectF.set(mFrameRectF);
                mTempRectF.inset(mRoundRadius / 2f, mRoundRadius / 2f);
                canvas.clipRect(mTempRectF, Region.Op.DIFFERENCE);
                canvas.drawRoundRect(mFrameRectF, mRoundRadius, mRoundRadius, mShadowPaint);
            }
            canvas.restore();
        }
    }

    private void drawBroader(Canvas canvas) {
        if (mBorderWidth > 0) {
            canvas.save();
            mTempRectF.set(mFrameRectF);
            if (shapeMode == SHAPE_MODE_CICRLE) {
                canvas.drawCircle(mFrameRectF.centerX(), mFrameRectF.centerY(), mFrameRectF.width()
                        / 2, mBorderPaint);
            } else if (shapeMode == SHAPE_MODE_RECTANGE) {
                canvas.drawRect(mFrameRectF, mBorderPaint);
            } else if (shapeMode == SHAPE_MODE_ROUND_RECTANGE) {
                canvas.drawRoundRect(mFrameRectF, mRoundRadius, mRoundRadius, mBorderPaint);
            }
            canvas.restore();
        }
    }

    public static ColorFlyingFrameView build(Activity activity) {
        if (null == activity) {
            throw new NullPointerException("The activity cannot be null");
        }
        final ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        return build(parent);
    }

    public static ColorFlyingFrameView build(ViewGroup parent) {
        if (null == parent) {
            throw new NullPointerException("The FlowView parent cannot be null");
        }
        ColorFlyingFrameView flowView = new ColorFlyingFrameView(parent.getContext());
        final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parent.addView(flowView, lp);
        return flowView;
    }


    public static void remove(Activity activity, View view) {
        if (null == activity) {
            throw new NullPointerException("The activity cannot be null");
        }
        final ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        parent.removeView(view);
    }

    public static void remove(ViewGroup parent, View view) {
        if (null == parent) {
            throw new NullPointerException("The FlowView parent cannot be null");
        }
        parent.removeView(view);
    }


    public void setShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
    }

    public void setShadowWidth(float mShadowWidth) {
        this.mShadowWidth = mShadowWidth;
        mShadowPaint.setMaskFilter(new BlurMaskFilter(mShadowWidth, BlurMaskFilter.Blur.OUTER));
    }

    public void setBorderWidth(float mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        mBorderPaint.setColor(mBorderColor);
    }

    public void setRoundRadius(float roundRadius) {
        if (mRoundRadius != roundRadius) {
            mRoundRadius = roundRadius;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
