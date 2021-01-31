package com.walton.filebrowser.ui.gridvideo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.mstar.android.media.MMediaPlayer;
/**
 * mutil play manager class
 * @author jason
 *
 */
public class MWPlayerManager {
    private static final String TAG = "MWPlayerManager";
    private static MWPlayerManager mMWPlayerManager = null;
    private List<MWPlayerLinkedInfo> mPlayerLinkedInfos = new ArrayList<MWPlayerLinkedInfo>();
    public  Hashtable mSurfaceCreatedTAG = new Hashtable<Integer,Integer>();
    public  Hashtable mSurfaceSetRenderTAG = new Hashtable<Integer,Integer>();
    private MWPlayerManager(){}

    /**
     * single instance
     * @return MWPlayerManager object
     */
    public static MWPlayerManager getInstance(){
        if(null == mMWPlayerManager){
            mMWPlayerManager = new MWPlayerManager();
        }
        return mMWPlayerManager;
    }

    /**
     * new a player to play video in gridview
     * @param context Context object
     * @param path the video path
     * @param position the video surfaceview position in gridview
     * @param surfaceView video play in this surfaceview
     */
    public void addOneVideoToPlayInGridView(Context context,String path,int position, SurfaceView surfaceView,IMWPlayerListener mwPlayerListener) {
        boolean isFirstVideo = false;
        if(null == mPlayerLinkedInfos){
            mPlayerLinkedInfos = new ArrayList<MWPlayerLinkedInfo>();
        }
        if(null == mSurfaceCreatedTAG){
            mSurfaceCreatedTAG = new Hashtable<Integer,Integer>();
        }
        if(null == mSurfaceSetRenderTAG){
            mSurfaceSetRenderTAG = new Hashtable<Integer,Integer>();
        }
        if(mPlayerLinkedInfos.size() == 0){
            isFirstVideo = true;
        }
        MMediaPlayer player = MWPlayerFactory.getInstance().creatMWPlayerWithSurfaceView(context, isFirstVideo, path, surfaceView,mwPlayerListener,position);
       // mvideoPaths.add(position, path);
        if (player == null) {
            return;
        }
        Log.i(TAG,"record the position is playing video or not");
        MWPlayerLinkedInfo playerLinkedInfo = new MWPlayerLinkedInfo();
        playerLinkedInfo.setIndex(position);
        playerLinkedInfo.setPlayer(player);
        playerLinkedInfo.setSurfaceView(surfaceView);
        playerLinkedInfo.setVideoPath(path);
        mPlayerLinkedInfos.add(playerLinkedInfo);
    }

    /**
     * get the position video's path
     * @param position the video surfaceview position
     * @return video's path
     */
    public String getVideoPathInPosition(int position) {
        if(null != mPlayerLinkedInfos){
            for (MWPlayerLinkedInfo info : mPlayerLinkedInfos) {
                if(info.getIndex() == position){
                    return info.getVideoPath();
                }
            }
        }
        return "";

    }
    public boolean isPlayingPathInEjectedUSB(String ejectedUsbMountPath) {
        if(null != mPlayerLinkedInfos){
            for (MWPlayerLinkedInfo info : mPlayerLinkedInfos) {
                Log.i(TAG,"info.getVideoPath():"+info.getVideoPath());
                if(info.getVideoPath().contains(ejectedUsbMountPath)){
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * query is position in gridview is surfaceview
     * @param position
     * @return
     */
    public boolean isVideoPlayingOnPostion(int position){
        if(null != mPlayerLinkedInfos){
            for (MWPlayerLinkedInfo info : mPlayerLinkedInfos) {
                if(info.getIndex() == position){
                    Log.d(TAG, "isVideoPlayingOnPostion  :"+position);
                    return true;
                }
            }
        }
        Log.d(TAG, "Not!!!   isVideoPlayingOnPostion  :"+position);
        return false;
    }

    /**
     * remove the cached videoplaylinkedinfo at position
     * @param position
     */
    public void removeVideoPlayingInfoAtPosition(int position){
        Log.d(TAG, "removeVideoPlayingInfoAtPosition : "+position);
        if (null != mPlayerLinkedInfos) {
            for (MWPlayerLinkedInfo info : mPlayerLinkedInfos) {
                if(info.getIndex() == position){
                    if (info.getPlayer()!= null) {
                        info.getPlayer().stop();
                        info.getPlayer().release();
                    }
                    mPlayerLinkedInfos.remove(info);
                    break;
                }
            }
        }
        if (null!=mSurfaceCreatedTAG && mSurfaceCreatedTAG.containsKey(position)) {
            mSurfaceCreatedTAG.remove(position);
        }
    }

    /**
     * switch voice output on position video
     * @param position focused video position
     */
    public void switchVideoVoiceOnPosition(int position) {
        if(null != mPlayerLinkedInfos){
            for (MWPlayerLinkedInfo videoInfo : mPlayerLinkedInfos) {
                if(videoInfo.getIndex() == position){
                    try {
                        //set voice value 1.0
                        if(null != videoInfo.getPlayer()){
                            videoInfo.getPlayer().setVolume(1.0f, 1.0f);
                        }
                    } catch (Exception e) {
                    }
                }else{
                    //set voice value 0
                    try {
                        if(null != videoInfo.getPlayer()){
                            videoInfo.getPlayer().setVolume(0.0f, 0.0f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * clear all data and release player
     */
    public void releaseAllResource(){
        Log.d(TAG, "releaseAllResource");
        if(null != mPlayerLinkedInfos){
            for (MWPlayerLinkedInfo info : mPlayerLinkedInfos) {
                try {
                    if (info.getPlayer()!=null) {
                        info.getPlayer().stop();
                        info.getPlayer().release();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mPlayerLinkedInfos.clear();
            mPlayerLinkedInfos = null;
        }
        if (mSurfaceCreatedTAG!=null) {
            mSurfaceCreatedTAG.clear();
            mSurfaceCreatedTAG = null;
        }
        if (mSurfaceSetRenderTAG!=null) {
            mSurfaceSetRenderTAG.clear();
            mSurfaceSetRenderTAG = null;
        }
    }
}
