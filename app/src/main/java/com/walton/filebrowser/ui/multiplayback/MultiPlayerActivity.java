package com.walton.filebrowser.ui.multiplayback;

import com.walton.filebrowser.R;
import com.walton.filebrowser.ui.video.VideoPlayerActivity;
import com.walton.filebrowser.util.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnHoverListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.walton.filebrowser.util.Tools;

import android.view.KeyEvent;

import com.walton.filebrowser.business.video.MultiVideoPlayView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.net.Uri;


/**
 * Mutil video play main activity
 * @author jason
 */
public class MultiPlayerActivity extends Activity{
    private static final String TAG = "MultiPlayerActivity";
    private static final int MAX_MULTI_PLAYBACK_NUM = 9;
    private int mCurrentFocusPosition = 1;
    private int mColumNum = 0;
    private MultiPlayerController mMultiPlayerController;

    public MultiVideoPlayView mMultiVideoPlayView[] = new MultiVideoPlayView[MAX_MULTI_PLAYBACK_NUM+1];
    public int mMultiVideoPlayViewId[] = new int[MAX_MULTI_PLAYBACK_NUM+1];
    public ImageView mMultiImageView[] = new ImageView[MAX_MULTI_PLAYBACK_NUM+1];
    public int mMultiImageViewId[] = new int[MAX_MULTI_PLAYBACK_NUM+1];
    public TextView mMultiVideoFocus[] = new TextView[MAX_MULTI_PLAYBACK_NUM+1];
    public int mGridVideoNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridVideoNum = getIntent().getIntExtra("videoNum", MAX_MULTI_PLAYBACK_NUM);
        mColumNum = (int) Math.sqrt(mGridVideoNum);
        if (mGridVideoNum == 4) {
            setContentView(R.layout.multifourplayback);
        } else if (mGridVideoNum == 9) {
            setContentView(R.layout.multinineplayback);
        }
        findviews();
        initParameters();
        mMultiPlayerController = new MultiPlayerController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mMultiPlayerController.releaseAllResource();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMultiPlayerController.releaseAllResource();
        try {
            unregisterReceiver(diskChangeReceiver);
            unregisterReceiver(sourceChangeReceiver);
            unregisterReceiver(homeKeyEventBroadCastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mMultiPlayerController.releaseAllResource();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown");
        if (KeyEvent.KEYCODE_MENU == keyCode) {
        } else if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode){
            Log.i(TAG,"andrew stop");
            mMultiPlayerController.stopPlayBack(mCurrentFocusPosition);
        } else if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode){

        } else if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode){

        } else if (KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode){

        } if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            processLeftKeyDown();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            processRightKeyDown();
            return true;
        }  if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            processUpKeyDown();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            processDownKeyDown();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                   || keyCode == KeyEvent.KEYCODE_ENTER) {
            processOkKeyDown();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void processLeftKeyDown(){
        if (mCurrentFocusPosition-1>=1) {
            showVideoFocus(false,mCurrentFocusPosition--);
            showVideoFocus(true,mCurrentFocusPosition);
        }
    }

    private void processRightKeyDown(){
        if (mCurrentFocusPosition+1<=mGridVideoNum) {
            showVideoFocus(false,mCurrentFocusPosition++);
            showVideoFocus(true,mCurrentFocusPosition);
        }
    }

    private void processUpKeyDown(){
        if (mCurrentFocusPosition-mColumNum>=1) {
            showVideoFocus(false,mCurrentFocusPosition);
            mCurrentFocusPosition -= mColumNum;
            showVideoFocus(true,mCurrentFocusPosition);
        }
    }

    private void processDownKeyDown(){
        if (mCurrentFocusPosition+mColumNum<=mGridVideoNum) {
            showVideoFocus(false,mCurrentFocusPosition);
            mCurrentFocusPosition += mColumNum;
            showVideoFocus(true,mCurrentFocusPosition);
        }
    }

    private void processOkKeyDown(){
        Log.i(TAG,"processOkKeyDown");
        if (mCurrentFocusPosition>=1 && mCurrentFocusPosition <= mGridVideoNum) {
            if (mMultiVideoPlayView[mCurrentFocusPosition-1] == null) {
                return;
            }
            if (mMultiVideoPlayView[mCurrentFocusPosition-1].getVisibility()!=View.VISIBLE) {
                Log.i(TAG,"processOkKeyDown show video list to play");
                mMultiPlayerController.addVideoPlayInGridView();
            } else {
                Log.i(TAG,"processOkKeyDown play a video on fullscreen");
                String videoPath = mMultiVideoPlayView[mCurrentFocusPosition-1].getVideoPath();
                Intent intent = new Intent(MultiPlayerActivity.this, VideoPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setData(Uri.parse(videoPath));
                MultiPlayerActivity.this.startActivity(intent);
                mMultiPlayerController.releaseAllResource();
            }

        }

    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            processOkKeyDown();
        }
    };

    private OnHoverListener onHoverListener = new OnHoverListener() {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (v instanceof ImageView) {
                if (what == MotionEvent.ACTION_HOVER_ENTER) {
                    for (int i=0;i<mGridVideoNum;i++) {
                        if (v.getId()== mMultiImageViewId[i]) {
                            showVideoFocus(false,mCurrentFocusPosition);
                            mCurrentFocusPosition = i+1;
                            showVideoFocus(true,mCurrentFocusPosition);
                            break;
                        }
                    }

                } else if (what == MotionEvent.ACTION_HOVER_EXIT){
                    for (int i=0;i<mGridVideoNum;i++) {
                        if (v.getId()== mMultiImageViewId[i]) {
                            showVideoFocus(false,i+1);
                            break;
                        }
                    }
                }

            } else if(v instanceof SurfaceView) {
                if (what == MotionEvent.ACTION_HOVER_ENTER) {
                    v.requestFocus();
                    v.setFocusable(true);
                    for (int i=0;i<mGridVideoNum;i++) {
                        if (v.getId()== mMultiVideoPlayViewId[i]) {
                            showVideoFocus(false,mCurrentFocusPosition);
                            mCurrentFocusPosition = i+1;
                            showVideoFocus(true,mCurrentFocusPosition);
                            break;
                        }
                    }
                } else if (what == MotionEvent.ACTION_HOVER_EXIT){
                    for (int i=0;i<mGridVideoNum;i++) {
                        if (v.getId()== mMultiVideoPlayViewId[i]) {
                            showVideoFocus(false,i+1);
                            break;
                        }
                    }
                }
            }
            return false;
        }
    };

    private void findviews() {
        findVideoFocusView();
        findSurfaceView();
        findImageView();
    }

    public void findVideoFocusView(){
        mMultiVideoFocus[0] = (TextView)findViewById(R.id.videofocus1);
        mMultiVideoFocus[1] = (TextView)findViewById(R.id.videofocus2);
        mMultiVideoFocus[2] = (TextView)findViewById(R.id.videofocus3);
        mMultiVideoFocus[3] = (TextView)findViewById(R.id.videofocus4);
        if (mGridVideoNum == 4 ) return;
        mMultiVideoFocus[4] = (TextView)findViewById(R.id.videofocus5);
        mMultiVideoFocus[5] = (TextView)findViewById(R.id.videofocus6);
        mMultiVideoFocus[6] = (TextView)findViewById(R.id.videofocus7);
        mMultiVideoFocus[7] = (TextView)findViewById(R.id.videofocus8);
        mMultiVideoFocus[8] = (TextView)findViewById(R.id.videofocus9);
    }
    public void findSurfaceviewId(){

        mMultiVideoPlayViewId[0] = R.id.videoview1;
        mMultiVideoPlayViewId[1] = R.id.videoview2;
        mMultiVideoPlayViewId[2] = R.id.videoview3;
        mMultiVideoPlayViewId[3] = R.id.videoview4;
        if (mGridVideoNum == 4 ) return;
        mMultiVideoPlayViewId[4] = R.id.videoview5;
        mMultiVideoPlayViewId[5] = R.id.videoview6;
        mMultiVideoPlayViewId[6] = R.id.videoview7;
        mMultiVideoPlayViewId[7] = R.id.videoview8;
        mMultiVideoPlayViewId[8] = R.id.videoview9;

    }
    public void findSurfaceView(){
        findSurfaceviewId();
        for (int i=0;i<mGridVideoNum;i++) {
            mMultiVideoPlayView[i] = (MultiVideoPlayView)findViewById(mMultiVideoPlayViewId[i]);
        }
        for (int i=0 ; i<mGridVideoNum ; i++) {
             mMultiVideoPlayView[i].setOnClickListener(onClickListener);
             mMultiVideoPlayView[i].setOnHoverListener(onHoverListener);
        }
    }

    public void findImageViewId(){

        mMultiImageViewId[0] = R.id.imageview1;
        mMultiImageViewId[1] = R.id.imageview2;
        mMultiImageViewId[2] = R.id.imageview3;
        mMultiImageViewId[3] = R.id.imageview4;
        if (mGridVideoNum == 4 ) return;
        mMultiImageViewId[4] = R.id.imageview5;
        mMultiImageViewId[5] = R.id.imageview6;
        mMultiImageViewId[6] = R.id.imageview7;
        mMultiImageViewId[7] = R.id.imageview8;
        mMultiImageViewId[8] = R.id.imageview9;


    }

    public void findImageView(){
        findImageViewId();
        for (int i=0;i<mGridVideoNum;i++) {
            mMultiImageView[i] = (ImageView)findViewById(mMultiImageViewId[i]);
        }
        for (int i=0 ; i<mGridVideoNum ; i++) {
             mMultiImageView[i].setOnClickListener(onClickListener);
             mMultiImageView[i].setOnHoverListener(onHoverListener);
        }
    }
    private void initParameters(){
        mCurrentFocusPosition = 1;
        for (int i=1; i<=mGridVideoNum; i++) {
            showOrHideSurfaceViews(false,i);
        }
        showVideoFocus(true,1);
    }
    public void hideOtherVideoFocus(int pos) {
        for (int i=1; i<=mGridVideoNum; i++) {
            if(pos == i) {
               showVideoFocus(true,pos);
            } else {
               showVideoFocus(false,i);
            }
        }
    }

    public int getCurrentFocusPosition(){
        return mCurrentFocusPosition;
    }
    public void setCurrentFocusPosition(int cnt){
        mCurrentFocusPosition = cnt;
    }
    public void showVideoFocus(boolean isShow,int pos) {
        Log.i(TAG,"position:"+pos+"*******showVideoFocus***********" + isShow);
        if (isShow) {
            if (mMultiVideoPlayView[pos-1]!=null) {
                mMultiVideoPlayView[pos-1].setVoice(true);
            }
            mMultiVideoFocus[pos-1].setBackgroundResource(R.drawable.border_focused);
        } else {
            if (mMultiVideoPlayView[pos-1]!=null) {
                mMultiVideoPlayView[pos-1].setVoice(false);
            }
            mMultiVideoFocus[pos-1].setBackgroundResource(R.drawable.border_unfocus);
        }
    }

    public void showOrHideSurfaceViews(boolean flag, int pos){
        if (pos>mGridVideoNum) return;
        if (flag) {
            mMultiVideoPlayView[pos-1].setVisibility(View.VISIBLE);
            mMultiImageView[pos-1].setVisibility(View.GONE);

        } else {
            mMultiVideoPlayView[pos-1].setVisibility(View.GONE);
            mMultiImageView[pos-1].setVisibility(View.VISIBLE);
        }
    }

    public boolean isPlayingPathInEjectedUSB(String ejectedUsbMountPath) {
        for (int i = 0; i< mGridVideoNum; i++) {
            String videoPath = mMultiVideoPlayView[i].getVideoPath();
            if (videoPath.contains(ejectedUsbMountPath)) {
                return true;
            }
        }
        return false;

    }
    private BroadcastReceiver sourceChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context,Intent intent) {
            mMultiPlayerController.releaseAllResource();
            MultiPlayerActivity.this.finish();
        }
    };

    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i(TAG, "DiskChangeReceiver: " + action);
                String devicePath = intent.getData().getPath();
                Log.i(TAG, "DiskChangeReceiver: " + devicePath);
                MultiVideoFileFilter.hasFiterVideoCompletedBefore = false;
                if (isPlayingPathInEjectedUSB(devicePath)) {
                    Log.i(TAG,"ejected usb contains playing video");
                    mMultiPlayerController.releaseAllResource();
                    Toast toast = Toast.makeText(MultiPlayerActivity.this, R.string.disk_eject,
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    MultiPlayerActivity.this.finish();
                } else  {
                    Log.i(TAG,"ejected usb not contains playing video");
                }
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                Log.i(TAG, "DiskChangeReceiver: " + action);
                MultiVideoFileFilter.hasFiterVideoCompletedBefore = false;
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
                        mMultiPlayerController.releaseAllResource();
                        MultiPlayerActivity.this.finish();
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    }
                }
            }
        }
    };
}
