package com.walton.filebrowser.ui.multiplayback;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import android.util.Log;
import android.widget.Toast;

import com.walton.filebrowser.R;

import com.walton.filebrowser.business.data.BaseData;

import com.walton.filebrowser.util.PlaylistTool;
import android.os.Handler;

import com.walton.filebrowser.business.video.MultiVideoPlayView;
import android.media.MediaPlayer;
import android.view.Gravity;
import com.walton.filebrowser.util.ToastFactory;


public class MultiPlayerController {
    protected static final String TAG = "MultiPlayerController";
    private MultiPlayerActivity mMultiPlayerActivity;
    private MultiVideoListSelectorView mVideoListSelectorView;
    protected ArrayList<BaseData> mVideoPlayList = new ArrayList<BaseData>();
    private Handler mHandler;
    private static final int IO_ERROR = 9000;

    public MultiPlayerController(MultiPlayerActivity mMultiPlayerActivity) {
        this.mMultiPlayerActivity = mMultiPlayerActivity;
    }

    /**
     * open a dialog to select a video for play in gridview
     */
    public void addVideoPlayInGridView() {
        showVideoListDialogForSelectVideo();
        MultiVideoFileFilter.startFilterVideoFile(mMultiPlayerActivity, MultiVideoFileFilter.allVideoList, new IMultiVideoFileAddedObserevr() {

            @Override
            public void watchVideoFileAdded(final List<MultiVideoInfo> videoInfos) {
                Log.i(TAG, "onVideoFileAdded  total videoInfos size = "+videoInfos.size());
                if(null != videoInfos){
                    if(mVideoListSelectorView != null){
                        mMultiPlayerActivity.runOnUiThread(new Runnable() {

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
        mVideoListSelectorView = new MultiVideoListSelectorView(mMultiPlayerActivity,new IMultiVideoFileSelectedObserver() {

            @Override
            public void watchUserSelectedVideoPath(String path) {
                File file = new File(path);
                String fileName = file.getName();
                if (fileName.endsWith("mplt")) {
                    Log.i(TAG,"is_mplt_file");
                    PlaylistTool ptool =new PlaylistTool();
                    mVideoPlayList = ptool.parsePlaylist(path);
                    int size = mVideoPlayList.size();
                    for(int i =0; i < size; i++) {
                        Log.i(TAG, "mplt_file_path: "+mVideoPlayList.get(i).getPath());
                        usePathDoMWPlayBack(i,mVideoPlayList.get(i).getPath());
                    }
                } else {
                    Log.i(TAG,"is_not_mplt_file");
                    usePathDoMWPlayBack(0,path);
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
    public void usePathDoMWPlayBack(int pos , String path){
        startPlayBack(mMultiPlayerActivity.getCurrentFocusPosition()+pos,path);
    }


    private void startPlayBack(int pos,String path) {
        Log.i(TAG,"startPlayBack");
        if (pos>mMultiPlayerActivity.mGridVideoNum) return;
        mMultiPlayerActivity.showOrHideSurfaceViews(true,pos);
        mMultiPlayerActivity.mMultiVideoPlayView[pos-1].setPlayerCallbackListener(myPlayerCallback);
        mMultiPlayerActivity.mMultiVideoPlayView[pos-1].setVideoPath(path, pos);

    }

    public void stopPlayBack(int pos) {
        Log.i(TAG,"stopPlayBack");
        if (pos>mMultiPlayerActivity.mGridVideoNum) return;
        mMultiPlayerActivity.showOrHideSurfaceViews(false,pos);
        mMultiPlayerActivity.mMultiVideoPlayView[pos-1].stopPlayer();
    }

    public void releaseAllResource(){
        for (int i =1; i<=mMultiPlayerActivity.mGridVideoNum; i++) {
            stopPlayBack(i);
        }
    }

    public MultiVideoPlayView.playerCallback myPlayerCallback = new MultiVideoPlayView.playerCallback() {
        @Override
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err, int viewId) {
            String strMessage = "";
            switch (framework_err) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    strMessage = mMultiPlayerActivity.getResources().getString(R.string.video_media_error_server_died);
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    strMessage = mMultiPlayerActivity.getResources().getString(R.string.video_media_error_not_valid);
                    break;
                case IO_ERROR:
                    strMessage = mMultiPlayerActivity.getResources().getString(R.string.open_file_fail);
                    break;
                default:
                    strMessage = mMultiPlayerActivity.getResources().getString(R.string.video_media_other_error_unknown);
                    break;
            }
            Log.i(TAG,"player onError---start to stop playerback.");
            stopPlayBack(viewId);
            showErrorToast(strMessage, viewId);
            mMultiPlayerActivity.showOrHideSurfaceViews(false,viewId);
            return true;
        }

        @Override
        public void onCompletion(MediaPlayer mp, int viewId) {
            Log.i(TAG, "----------- onCompletion ------------");

        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra, int viewId) {
            Log.i(TAG, "*******onInfo******" + what + " getVideoWidth:" + mp.getVideoWidth() + " getVideoHeight:" + mp.getVideoHeight());
            return false;
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height, int viewId) {
            Log.i(TAG, "onVideoSizeChanged width:" + width + " height:" + height + " viewId:" + viewId);

        }

        @Override
        public void onPrepared(MediaPlayer mp, int viewId) {
            Log.i(TAG, "onPrepared");
            mMultiPlayerActivity.hideOtherVideoFocus(viewId);
            mMultiPlayerActivity.setCurrentFocusPosition(viewId);
            //mMultiPlayerActivity.showVideoFocus(true,viewId);
        }

        @Override
        public void onSeekComplete(MediaPlayer mp, int viewId) {
        }

        @Override
        public void onCloseMusic() {
        }

        @Override
        public void onUpdateSubtitle(String sub) {
        }
    };

    private void showErrorToast(String strMessage, int viewId) {
        Toast toast = ToastFactory.getToast(mMultiPlayerActivity, strMessage, Gravity.CENTER);
        toast.show();
        //MultiPlayerActivity.this.finish();
    }
}
