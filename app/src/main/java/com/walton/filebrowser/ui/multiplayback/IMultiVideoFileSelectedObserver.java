package com.walton.filebrowser.ui.multiplayback;

import java.util.List;

/**
 * User select video file to play in gridview's item view
 * @author jason
 *
 */
public interface IMultiVideoFileSelectedObserver {
    /**
     * select one video back
     * @param path the select video path
     */
    public abstract void watchUserSelectedVideoPath(String path);

    /**
     * select mutil video back
     * @param videoPaths mutil video path collection
     */
    public abstract void watchUserSelectedMutilVideoPaths(List<String> videoPaths);
}
