package com.walton.filebrowser.ui.multiplayback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import android.util.AttributeSet;
import android.view.SurfaceHolder;


/**
 * Mutil video play main activity
 * @author jason
 */
public class MultiPlayerActivityTest extends Activity{
    private static final String TAG0 = "MultiPlayerActivityTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        Log.i(TAG0,"SubtitleSurfaceView 1111");
        SubtitleSurfaceView mSubtitleSurfaceView = new SubtitleSurfaceView(this);
        decor.addView(mSubtitleSurfaceView,COVER_SCREEN_PARAMS);
        Log.i(TAG0,"SubtitleSurfaceView 2222");
    }

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
        new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

    public class SubtitleSurfaceView extends SurfaceView {

        private static final String TAG = "SubtitleSurfaceView";
        private SurfaceHolder mSurfaceHolder = null;
        private Context mContext = null;
        public SubtitleSurfaceView(Context context) {
            super(context);
            mContext = context;
            initVideoView();
        }

        public SubtitleSurfaceView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
            mContext = context;
            initVideoView();
        }

        public SubtitleSurfaceView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mContext = context;
            initVideoView();
        }

        private void initVideoView() {
            getHolder().addCallback(mSHCallback);
            //getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int w,int h) {
                mSurfaceHolder = holder;
                Log.i(TAG, "SubtitleSurfaceView surfaceChanged:" + w + "," + h);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "SubtitleSurfaceView surfaceCreated");
                mSurfaceHolder = holder;
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceHolder = null;
                Log.i(TAG, "SubtitleSurfaceView surfaceDestroyed");
            }
        };

    }

}
