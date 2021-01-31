package com.walton.filebrowser.model;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class FileModel {

    private String fileName;
    private String fileType;
    private String filePath;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
