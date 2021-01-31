package com.walton.filebrowser.util;

import android.content.Context;

import java.io.File;

public class StorageProgress {

    private Context mContext;
    private static String mPath;

    public StorageProgress(Context context, String path){
        mContext = context;
        mPath = path;
    }

    public String[] getProgress(){
        String free = StorageSpaceCount.bytesToHuman(StorageSpaceCount.getAvailableSpaceInBytes(new File(mPath)));
        String total = StorageSpaceCount.bytesToHuman(StorageSpaceCount.getTotalSpaceInBytes(new File(mPath)));

        int progressTotal = StorageSpaceCount.getSpaceInMegabyte(StorageSpaceCount.getTotalSpaceInBytes(new File(mPath)));
        int progressFree = progressTotal - StorageSpaceCount.getSpaceInMegabyte(StorageSpaceCount.getAvailableSpaceInBytes(new File(mPath)));

        int uses = (int) (((double) progressFree / (double) progressTotal) * 100);

        String[] storageData = {total,free,String.valueOf(uses)};

        return storageData;
    }

}
