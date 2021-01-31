package com.walton.filebrowser.ui.gridvideo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.walton.filebrowser.R;
import com.walton.filebrowser.util.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import com.walton.filebrowser.util.Tools;
import android.os.Handler;
import android.view.KeyEvent;


/**
 * Mutil video play main activity
 * @author jason
 */
public class MWPlayerActivity extends Activity{
    private static final int DEFAULT_VIDEOVIEW_MAGIN = 10;
    private static final int DEFAULT_VIDEO_NUM = 9;
    private static final String TAG = "MWPlayerActivity";
    private MWPlayerController mMWPlayerController;
    private int mScreenWidth;
    private int mScreenHeight;
    private MGridAdapter mGridViewAdapter;
    private FocusedBorderView mFocusedBorderView;
    private int mViewCount = DEFAULT_VIDEO_NUM;
    private int mFocusedPosition = -1;
    public GridView mGridView;
    private int mColumNum = (int) Math.sqrt(DEFAULT_VIDEO_NUM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        mMWPlayerController = new MWPlayerController(this,toBePlayedHandler);
        mViewCount = getIntent().getIntExtra("videoNum", DEFAULT_VIDEO_NUM);
        mColumNum = (int) Math.sqrt(mViewCount);
        mMWPlayerController.setTotallVideoNum(mViewCount);
        setContentView(initView(mViewCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        backToInitState();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        // Registered disk change broadcast receiver
        registerReceiver(diskChangeReceiver, filter);
        IntentFilter sourceChangeFilter = new IntentFilter();
        sourceChangeFilter.addAction(Constants.ACTION_CHANGE_SOURCE);
        registerReceiver(sourceChangeReceiver,sourceChangeFilter);
        registerReceiver(homeKeyEventBroadCastReceiver,
            new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        int ret = getCachedCurrentVideoPosition();
        mMWPlayerController.setCurrentVideoPosition(ret);
        mGridView.setSelection(ret);
    }
    public int getCachedCurrentVideoPosition() {
        int tmpCurrentVideoPosition = 0;
        tmpCurrentVideoPosition = getSharedPreferences("localmm_sharedPreferences",
            Context.MODE_PRIVATE).getInt("CurrentVideoPosition", mMWPlayerController.getCurrentVideoPosition());
        Log.i(TAG,"tmpCurrentVideoPosition:"+tmpCurrentVideoPosition);
        return tmpCurrentVideoPosition;
    }
    public void setCachedCurrentVideoPosition(){
        getSharedPreferences("localmm_sharedPreferences", Context.MODE_PRIVATE)
                     .edit().putInt("CurrentVideoPosition", mMWPlayerController.getCurrentVideoPosition()).commit();
    }
    /**
     * init view with video num
     * @param viewNum video num
     */
    private View initView(int viewNum) {
        Log.i(TAG, "init view video num = "+viewNum);
        FrameLayout layout = new FrameLayout(getBaseContext());
        layout.setBackgroundColor(Color.DKGRAY);
        mGridView = new GridView(getBaseContext());
        Point p = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(p);
        mScreenHeight = p.y;
        mScreenWidth = p.x;
        Log.i(TAG, "screenWidth = "+mScreenWidth+" screenHeight = "+mScreenHeight);
        LayoutParams params = new LayoutParams(mScreenWidth, mScreenHeight);
        mGridView.setLayoutParams(params);

        mGridView.setCacheColorHint(Color.TRANSPARENT);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridView.setBackgroundColor(Color.DKGRAY);
        mGridView.setHorizontalSpacing(DEFAULT_VIDEOVIEW_MAGIN);
        mGridView.setVerticalSpacing(DEFAULT_VIDEOVIEW_MAGIN);
        mGridView.setLayoutAnimation(createLayoutAnimation());
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(0, 0);
        mGridView.setNumColumns(mColumNum);
        mGridView.setColumnWidth((mScreenWidth-DEFAULT_VIDEOVIEW_MAGIN*(mColumNum+1))/mColumNum);
        params2.height = (mScreenHeight-DEFAULT_VIDEOVIEW_MAGIN*(mColumNum+1))/mColumNum;
        params2.width = (mScreenWidth-DEFAULT_VIDEOVIEW_MAGIN*(mColumNum+1))/mColumNum;
        registeViewEventListener(mGridView,viewNum);
        layout.addView(mGridView);
        mFocusedBorderView = new FocusedBorderView(getBaseContext());
        mFocusedBorderView.setImageResource(R.drawable.focused_border);
        mFocusedBorderView.setScaleType(ScaleType.FIT_XY);
        mFocusedBorderView.setLayoutParams(params2);
        layout.addView(mFocusedBorderView);
        return layout;
    }

    private void registeViewEventListener(GridView view, int videoNum) {
        view.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                //Log.i(TAG, "select item :"+position);
                mFocusedPosition = position;
                //Toast.makeText(getBaseContext(), "select item "+position, Toast.LENGTH_SHORT).show();
                if(view instanceof ImageView){
                    mMWPlayerController.onItemFocusChanged(position,false);
                }else if(view instanceof SurfaceView){
                    mMWPlayerController.onItemFocusChanged(position,true);
                }
                mFocusedBorderView.onViewHasFocused(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                //Log.i(TAG, "click item :"+position);
                //Toast.makeText(getBaseContext(), "click item "+position, Toast.LENGTH_SHORT).show();
                if(view instanceof ImageView){
                    mMWPlayerController.onItemClicked(position,false);
                }else if(view instanceof SurfaceView){
                    mMWPlayerController.onItemClicked(position,true);
                }
            }
        });

        /**
         * for first item view focusd on start
         */
        view.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(mFocusedPosition == -1){
                    Log.i(TAG, "onFocusChange  (mFocusedPosition == -1)");
                try {
                    @SuppressWarnings("unchecked")
                    Class<GridView> c = (Class<GridView>) Class
                      .forName("android.widget.GridView");
                    Method[] flds = c.getDeclaredMethods();
                    for (Method f : flds) {
                     if ("setSelectionInt".equals(f.getName())) {
                      f.setAccessible(true);
                      f.invoke(v,
                        new Object[] { Integer.valueOf(mFocusedPosition) });
                     }
                    }
                   } catch (Exception e) {
                    e.printStackTrace();
                   }
                }
            }
        });
    }

    /**
     * band gridview with init data
     * @param view
     */
    private void bindViewWithData(GridView view,int childNum) {
        mGridViewAdapter = new MGridAdapter(childNum);
        view.setAdapter(mGridViewAdapter);
    }

    /**
     *
     * @param position
     * @return
     */
    private boolean isVideoPlaying(int position) {
        return MWPlayerManager.getInstance().isVideoPlayingOnPostion(position);
    }

    /**
     * get cached surfaceview in position
     * @param pos the girdview's item position
     * @return a cached surfaceview
     */
    public SurfaceView getCachedSurfaceViewInPosition(int pos) {
        if(null == mGridViewAdapter) {
            return null;
        }
        return mGridViewAdapter.getCachedSurfaceViewInPosition(pos);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown");
        if (KeyEvent.KEYCODE_MENU == keyCode) {
            return true;
        } else if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode){
            //mMWPlayerController.stopMultiPlayBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void invalidateView(int mCurrentVideoPosition) {
        mGridViewAdapter.notifyDataSetChanged();
        mGridViewAdapter.notifyDataSetInvalidated();
        int ret = getCachedCurrentVideoPosition();
        mMWPlayerController.setCurrentVideoPosition(ret);
        mGridView.setSelection(ret);
    }
    private Handler toBePlayedHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG,"toBePlayedHandler");
            Bundle bundle = msg.getData();
            String path = bundle.getString("path");
            int pos = bundle.getInt("pos");
            mMWPlayerController.usePathDoMWPlayBack(path,pos);

        };
    };
    private class MGridAdapter extends BaseAdapter{
        private int childNum;
        private List<SurfaceView> videoSufeaceViews;
        private View defaultImageView;
        private int countPosition = 0;
        public MGridAdapter(int childNum){
            this.childNum = childNum;
            if(null == videoSufeaceViews){
                videoSufeaceViews = new ArrayList<SurfaceView>(childNum);
            }else{
                videoSufeaceViews.clear();
            }
            for (int i=0;i<childNum;i++) {
                SurfaceView surfaceView = new SurfaceView(getBaseContext());
                surfaceView.setOnHoverListener(new MyOnHoverListener());
                videoSufeaceViews.add(surfaceView);
            }
            if(null == defaultImageView){
                defaultImageView = new ImageView(getBaseContext());
            }
        }

        @Override
        public int getCount() {
            return childNum;
        }

        @Override
        public Object getItem(int position) {
            return videoSufeaceViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public SurfaceView getCachedSurfaceViewInPosition(int position){
            return videoSufeaceViews.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutParams layoutParams = new LayoutParams((mScreenWidth-DEFAULT_VIDEOVIEW_MAGIN*(mColumNum+1))/mColumNum, (mScreenHeight-DEFAULT_VIDEOVIEW_MAGIN*(mColumNum+1))/mColumNum);
            Log.i(TAG,"position :"+String.valueOf(position));
            if(isVideoPlaying(position)){
                SurfaceView surfaceView = videoSufeaceViews.get(position);
                surfaceView.setLayoutParams(layoutParams);
                surfaceView.setOnHoverListener(new MyOnHoverListener());
                return surfaceView;
            }else{
                ImageView v = new ImageView(getBaseContext());
                v.setLayoutParams(layoutParams);
                v.setImageResource(R.drawable.add_video);
                v.setOnHoverListener(new MyOnHoverListener());
                return v;
            }
        }

    }

    /**
     * inner class for gridview Hover listener
     * @author jason
     *
     */
    class MyOnHoverListener implements OnHoverListener {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            int itemHeight = v.getHeight();
            int itemWidth = v.getWidth();
            switch (what) {
            // Mouse Enter view
            case MotionEvent.ACTION_HOVER_ENTER:
                int position = ((int) v.getY() / itemHeight)* mScreenWidth/itemWidth  +((int) v.getX() / itemWidth);
                Log.i(TAG, "onHover position :"+position);
                //Toast.makeText(getBaseContext(), "select item "+position, Toast.LENGTH_SHORT).show();
                if(v instanceof ImageView){
                    mMWPlayerController.onItemFocusChanged(position,false);
                }else if(v instanceof SurfaceView){
                    mMWPlayerController.onItemFocusChanged(position,true);
                }
                mFocusedBorderView.onViewHasFocused(v);
                break;
            }
            return false;
        }
    }

    /**
     * create layout animation
     * @return layout animation
     */
    private LayoutAnimationController createLayoutAnimation() {
         AnimationSet set = new AnimationSet(true);
         Animation animation = new AlphaAnimation(0.0f, 1.0f);
         animation.setDuration(300);
         set.addAnimation(animation);
         animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
         Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
         -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
         animation.setDuration(400);
         set.addAnimation(animation);
         LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
         return controller;
    }

    /**
     * resume to init state
     */
    public void backToInitState() {
        if(null != mGridView){
            bindViewWithData(mGridView, mViewCount);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseResource();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseResource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(diskChangeReceiver);
            unregisterReceiver(sourceChangeReceiver);
            unregisterReceiver(homeKeyEventBroadCastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        releaseResource();
        mMWPlayerController.setCurrentVideoPosition(0);
        setCachedCurrentVideoPosition();
    }

    /**
     * release the resource
     */
    private void releaseResource(){
        MWPlayerManager.getInstance().releaseAllResource();
    }
    private BroadcastReceiver sourceChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context,Intent intent) {
            MWPlayerActivity.this.finish();
        }
    };
    // Disk change monitoring
    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Disk remove
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i(TAG, "DiskChangeReceiver: " + action);
                String devicePath = intent.getData().getPath();
                Log.i(TAG, "DiskChangeReceiver: " + devicePath);
                VideoFileFilter.hasFiterVideoCompletedBefore = false;
                if (MWPlayerManager.getInstance().isPlayingPathInEjectedUSB(devicePath)) {
                    Log.i(TAG,"ejected usb contains playing video");
                    releaseResource();
                    Toast toast = Toast.makeText(MWPlayerActivity.this, R.string.disk_eject,
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    MWPlayerActivity.this.finish();
                } else  {
                    Log.i(TAG,"ejected usb not contains playing video");
                }
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                Log.i(TAG, "DiskChangeReceiver: " + action);
                VideoFileFilter.hasFiterVideoCompletedBefore = false;
            }
        }
    };
    private BroadcastReceiver homeKeyEventBroadCastReceiver = new BroadcastReceiver() {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    String hwName = Tools.getHardwareName();
                    if (!hwName.equals("messi") && reason.equals(SYSTEM_HOME_KEY)) {
                        MWPlayerActivity.this.finish();
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    }
                }
            }
        }
    };
}
