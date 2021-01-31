package com.walton.filebrowser.ui.gridvideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.walton.filebrowser.R;

import android.content.Context;
import android.util.Log;

/**
 * Video file filter class,to filter sdcard & u disk all video file by sync
 * @author jason
 *
 */
public class VideoFileFilter {
    private static final String TAG = "VideoFileFilter";
    public static List<VideoInfo> allVideoList =  new ArrayList<VideoInfo>();
    private static String[] forMatchs;
    public static boolean hasFiterVideoCompletedBefore = false;
    /**
     * filter video file sync
     * @param context
     * @param mList
     * @param fileAddedListener
     */
    public static synchronized void startFilterVideoFile(final Context context,final List<VideoInfo> mList,final IVideoFileAddedObserevr fileAddedListener) {

        if(hasFiterVideoCompletedBefore && mList!= null && mList.size()>0){
            Log.i(TAG, "isFilterCompletedBefor and mList.size() ="+mList.size());
           fileAddedListener.watchVideoFileAdded(mList);
           return;
        }
        Log.i(TAG, "startFilterVideoFile ---------->");
        forMatchs = context.getResources().getStringArray(R.array.mwvideo_filter);
        //forMatchs = context.getResources().getStringArray(R.array.audio_filter);
        //if has sdcard ,can't scan u disk
        //final File file = new File(Environment.getExternalStorageDirectory().getParent());
        final File file = new File("/mnt");
        Log.i(TAG, "first file.isDirectory() ="+file.isDirectory() +"   path = "+file.getPath());
        if(file.isDirectory()){
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        hasFiterVideoCompletedBefore = false;
                        FilterFile(file, forMatchs, mList, fileAddedListener);
                        hasFiterVideoCompletedBefore = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    /**
     * Filter video file
     * @param f root file
     * @param forMatchs the all need file suffix array
     * @param videoInfos videoinfo list
     * @param fileAddedListener filter video file add to list listener
     */
    private static synchronized void FilterFile(File f,String[] forMatchs, List<VideoInfo> videoInfos, IVideoFileAddedObserevr fileAddedListener){
        Log.i(TAG, "FilterFile ----------> f:"+f.getPath());
        if(f.list() != null && f.list().length > 0){
            Log.i(TAG, "f.listFiles() size="+f.listFiles().length);
            for (File file : f.listFiles()) {
                //Log.i(TAG, "listFiles file:"+file.getName());
                if(!file.isDirectory()){
                    int i = file.getName().indexOf('.');
                    if (i != -1) {
                        String suffix = file.getName().substring(i);
                        for (String s : forMatchs) {
                            if(suffix.equalsIgnoreCase(s)){
                                VideoInfo vi = new VideoInfo();
                                vi.setName(file.getName());
                                vi.setPath(file.getAbsolutePath());
                                videoInfos.add(vi);
                                Log.i(TAG, "find video ----------> " +vi);
                                fileAddedListener.watchVideoFileAdded(videoInfos);
                            }
                        }
                    }
                }else{
                    Log.i(TAG, "directory ,name = "+file.getName());
                    if(file.list() != null && file.list().length>0){
                        FilterFile(file,forMatchs, videoInfos,fileAddedListener);
                    }
                }
            }
        }
    }
}