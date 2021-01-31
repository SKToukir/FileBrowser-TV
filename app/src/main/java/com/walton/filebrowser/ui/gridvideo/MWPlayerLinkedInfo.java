package com.walton.filebrowser.ui.gridvideo;

import android.view.SurfaceView;
import com.mstar.android.media.MMediaPlayer;

/**
 * MWPlayer linked info class
 * @author jason
 *
 */
public class MWPlayerLinkedInfo {

    private MMediaPlayer player;
    private int index;
    private SurfaceView surfaceView;
    private String videoPath;
    public String getVideoPath() {
        return videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public MMediaPlayer getPlayer() {
        return player;
    }
    public void setPlayer(MMediaPlayer player) {
        this.player = player;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public SurfaceView getSurfaceView() {
        return surfaceView;
    }
    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

}
