package com.walton.filebrowser.ui.gridvideo;

import java.util.List;

/**
 * Find video file add to video list listener
 * @author jason
 *
 */
public interface IVideoFileAddedObserevr {
    /**
     * Find video file when scan
     * @param videoInfos all find video info data collection
     */
    public abstract void watchVideoFileAdded(List<VideoInfo> videoInfos);

}
