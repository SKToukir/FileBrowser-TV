package com.walton.filebrowser.ui.multiplayback;

import java.util.List;

/**
 * Find video file add to video list listener
 * @author jason
 *
 */
public interface IMultiVideoFileAddedObserevr {
    /**
     * Find video file when scan
     * @param videoInfos all find video info data collection
     */
    public abstract void watchVideoFileAdded(List<MultiVideoInfo> videoInfos);

}
