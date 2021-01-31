package com.walton.filebrowser.ui.gridvideo;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.walton.filebrowser.ui.video.VideoPlayerActivity;

import com.walton.filebrowser.business.data.BaseData;

import com.walton.filebrowser.util.PlaylistTool;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

/**
 * MWPlayer controller
 * @author jason
 *
 */
public class MWPlayerController {
    protected static final String TAG = "MWPlayerController";
    private MWPlayerActivity mMWPlayerActivity;
    private VideoListSelectorView mVideoListSelectorView;
    private int mCurrentVideoPosition = 0;
    private int mTotallVideoNum = 0;
    protected ArrayList<BaseData> mVideoPlayList = new ArrayList<BaseData>();
    private Handler mHandler;
    private boolean mIsConstantPlay = false;
    private int  mIsConstantStartPosition = 0;

    public MWPlayerController(MWPlayerActivity mWPlayerActivity,Handler handler) {
        this.mMWPlayerActivity = mWPlayerActivity;
        this.mHandler = handler;
    }

    /**
     * deal with focus changed business
     * @param position the item position of gridview
     */
    public void onItemFocusChanged(int position,boolean isVideoPlaying){
        mCurrentVideoPosition = position;
        Log.i(TAG,"onItemFocusChanged mCurrentVideoPosition:"+mCurrentVideoPosition);
        //changed video voice output
        // which will cause one gridview item flush and at last cause surfaceview destroyed.
        //mMWPlayerActivity.mGridView.setSelection(position);
        if(isVideoPlaying){
            MWPlayerManager.getInstance().switchVideoVoiceOnPosition(position);
        }
    }

    /**
     * deal with item has click business
     * 1. add video to play
     * 2.play video in fullscreen
     * @param position the item position of gridview
     * @param isVideoPlaying
     */
    public void onItemClicked(int position, boolean isVideoPlaying){
        mCurrentVideoPosition = position;
        Log.i(TAG, "onItemClicked  position:"+position);
        mMWPlayerActivity.setCachedCurrentVideoPosition();
        mMWPlayerActivity.mGridView.setSelection(position);
        if(isVideoPlaying){
            //play video in fullscreen
            Log.i(TAG, "item clecked to play in fullscreen,the position = "+position);

            String videoPath = MWPlayerManager.getInstance().getVideoPathInPosition(position);
            Intent intent = new Intent(mMWPlayerActivity, VideoPlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.setData(Uri.parse(videoPath));
            mMWPlayerActivity.startActivity(intent);
            MWPlayerManager.getInstance().releaseAllResource();
            mMWPlayerActivity.backToInitState();
        }else{
            //select video play in gridview
            addVideoPlayInGridView();
        }


    }
    /**
     * open a dialog to select a video for play in gridview
     */
    private void addVideoPlayInGridView() {

        showVideoListDialogForSelectVideo();
        VideoFileFilter.startFilterVideoFile(mMWPlayerActivity, VideoFileFilter.allVideoList, new IVideoFileAddedObserevr() {

            @Override
            public void watchVideoFileAdded(final List<VideoInfo> videoInfos) {
                Log.i(TAG, "onVideoFileAdded  total videoInfos size = "+videoInfos.size());
                if(null != videoInfos){
                    if(mVideoListSelectorView != null){
                        mMWPlayerActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                mVideoListSelectorView.updateVideoListData(videoInfos);
                            }
                        });

                    }
                }
            }
        });

    }

    /**
     * show video playlist dialog for select video to play
     */
    public void showVideoListDialogForSelectVideo() {
        if (mVideoListSelectorView !=null && mVideoListSelectorView.isShowing()) {
            if (mVideoListSelectorView != null) {
                mVideoListSelectorView.dismiss();
                mVideoListSelectorView = null;
            }
            return;
        }
        mVideoListSelectorView = new VideoListSelectorView(mMWPlayerActivity,new IVideoFileSelectedObserver() {

            @Override
            public void watchUserSelectedVideoPath(String path) {
                File file = new File(path);
                String fileName = file.getName();
                mIsConstantPlay = false;
                if (fileName.endsWith("mplt")) {
                    if (!mIsConstantPlay) {
                        mIsConstantPlay = true;
                        mIsConstantStartPosition = mCurrentVideoPosition;
                    }
                    Log.i(TAG,"is_mplt_file");
                    PlaylistTool ptool =new PlaylistTool();
                    mVideoPlayList = ptool.parsePlaylist(path);
                    int size = mVideoPlayList.size();
                    for(int i =0; i < size; i++) {
                        Log.i(TAG, "mplt_file_path: "+mVideoPlayList.get(i).getPath());
                        //usePathDoMWPlayBack(mVideoPlayList.get(i).getPath(),i);
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("path", mVideoPlayList.get(i).getPath());
                        bundle.putInt("pos", i);
                        msg.setData(bundle);
                        mHandler.sendMessageDelayed(msg, 0);
                    }
                } else {
                    mIsConstantPlay = false;
                    Log.i(TAG,"is_not_mplt_file");
                    usePathDoMWPlayBack(path,0);
                }
            }
            @Override
            public void watchUserSelectedMutilVideoPaths(List<String> videoPaths) {
                // TODO Auto-generated method stub
                // current don't implement
            }
        });
        mVideoListSelectorView.show();
    }
    public void usePathDoMWPlayBack(String path,int pos){
            //add this video to play
            Log.i(TAG, "usePathDoMWPlayBack select video path="+path +"  currentPosition="+mCurrentVideoPosition);
            SurfaceView surfaceView = null;
            int tmpPosition = 0;
            if (mIsConstantPlay) {
                tmpPosition = mIsConstantStartPosition + pos;
            } else {
                Log.i(TAG,"usePathDoMWPlayBack mCurrentVideoPosition:"+mCurrentVideoPosition);
                tmpPosition = mCurrentVideoPosition + pos;
            }
            final int toBePlayPathPosition = tmpPosition;
            if (toBePlayPathPosition >= mTotallVideoNum) {
                return;
            }
            if ( MWPlayerManager.getInstance().isVideoPlayingOnPostion(toBePlayPathPosition)) {
                return;
            }
            surfaceView = mMWPlayerActivity.getCachedSurfaceViewInPosition(toBePlayPathPosition);
            if (null == surfaceView) {
                return ;
            }
            MWPlayerManager.getInstance().addOneVideoToPlayInGridView(mMWPlayerActivity,path,toBePlayPathPosition,surfaceView
                  ,new IMWPlayerListener() {

                  @Override
                  public void onVideoPlayFaild(final String errorMsg) {
                        Log.i(TAG, "At position:"+toBePlayPathPosition +
                                "   onVideoPlayFaild  errorMsg:"+errorMsg);
                        if(null != errorMsg && !errorMsg.isEmpty()){
                            mMWPlayerActivity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(mMWPlayerActivity, errorMsg, Toast.LENGTH_SHORT).show();
                                    MWPlayerManager.getInstance().removeVideoPlayingInfoAtPosition(toBePlayPathPosition);
                                    mMWPlayerActivity.invalidateView(toBePlayPathPosition);
                                }
                           });
                        }
                  }
                  @Override
                  public void onAttachedVideoFrame() {
                        Log.i(TAG, "onAttachedVideoFrame toBePlayPathPosition:"+toBePlayPathPosition);
                        mMWPlayerActivity.runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                            // changed default imageview with surfaceview to play video
                                 //mMWPlayerActivity.invalidateView(toBePlayPathPosition);
                                 MWPlayerManager.getInstance().switchVideoVoiceOnPosition(mCurrentVideoPosition);
                             }
                       });

                 }
         });
         mMWPlayerActivity.invalidateView(toBePlayPathPosition);

    }

    public void stopMultiPlayBack(){
        MWPlayerManager.getInstance().removeVideoPlayingInfoAtPosition(mCurrentVideoPosition);
        mMWPlayerActivity.invalidateView(mCurrentVideoPosition);
    }

    public int getCurrentVideoPosition() {
        return mCurrentVideoPosition;
    }

    public void setCurrentVideoPosition(int mCurrentVideoPosition) {
        this.mCurrentVideoPosition = mCurrentVideoPosition;
    }

    public int getTotallVideoPosition() {
        return mTotallVideoNum;
    }

    public void setTotallVideoNum(int mTotallVideoPosition) {
        this.mTotallVideoNum = mTotallVideoPosition;
    }
}
