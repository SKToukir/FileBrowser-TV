package com.walton.filebrowser.ui.gridvideo;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Focused view boeder class
 * @author jason
 *
 */
public class FocusedBorderView extends ImageView {

    public FocusedBorderView(Context context) {
        super(context,null,0);
        init(context);
    }

    public FocusedBorderView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init(context);
    }

    public FocusedBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
    }

    /**
     * focused view aniamtion
     * @param width the focused view width
     * @param height the focused view height
     * @param x the focused view left
     * @param y the focused view top
     */
    private void borderAniamtion(int width, int height, float x, float y) {
        int mWidth = this.getWidth();
        int mHeight = this.getHeight();

        float scaleX = (float) width / (float) mWidth;
        float scaleY = (float) height / (float) mHeight;

        animate().translationX(x).translationY(y).setDuration(200)
                .scaleX(scaleX).scaleY(scaleY)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    /**
     * on view focused
     * @param view the focused view
     */
    public void onViewHasFocused(View view){
        if(null == view){
            return;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        int left = view.getLeft();
        int top = view.getTop();
        borderAniamtion(width, height, left, top);
        this.bringToFront();
        invalidate();
    }
}
