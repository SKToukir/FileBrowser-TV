package com.walton.filebrowser.ui.gridvideo;
/**
 * MMediaPlayer listener
 * @author jason
 *
 */
public interface IMWPlayerListener {

    /**
     * When　play arise error，include UNsupport format、player error...
     * it like to add a new video to play
     * @param errorMsg the error info
     */
    public abstract void onVideoPlayFaild(final String errorMsg);

    /**
     * when Play is ready ,has prepared &　surface has created
     */
    public abstract void onAttachedVideoFrame();


}
