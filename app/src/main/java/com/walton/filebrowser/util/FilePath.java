package com.walton.filebrowser.util;

import java.io.File;

public class FilePath {
    private File srcPath;
    private File destPath;

    public File getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(File srcPath) {
        this.srcPath = srcPath;
    }

    public File getDestPath() {
        return destPath;
    }

    public void setDestPath(File destPath) {
        this.destPath = destPath;
    }
}
