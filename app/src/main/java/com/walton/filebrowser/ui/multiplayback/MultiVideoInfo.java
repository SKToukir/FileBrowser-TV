package com.walton.filebrowser.ui.multiplayback;

public class MultiVideoInfo {
    private String mPath;
    private String mName;
    public String getPath() {
        return mPath;
    }
    public void setPath(String mPath) {
        this.mPath = mPath;
    }
    public String getName() {
        return mName;
    }
    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString() {
        return "VideoInfo [mPath=" + mPath + ", mName=" + mName + "]";
    }
}
