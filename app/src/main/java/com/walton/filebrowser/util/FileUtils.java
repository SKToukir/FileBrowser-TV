package com.walton.filebrowser.util;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;


import com.walton.filebrowser.FocusTest;
import com.walton.filebrowser.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by penghuilou on 2016/3/7.
 * 文件操作工具类
 */
public class FileUtils {

    private FileUtils() {

    }


    public static ArrayList<String> getUsbPath(Context context) {

        ArrayList<String> usbPaths = new ArrayList<>();
        final StorageManager sm = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);
        String[] volumePaths = new String[0];
        try {
            final Method method = sm.getClass().getMethod("getVolumePaths");
            if (null != method) {
                method.setAccessible(true);
                volumePaths = (String[]) method.invoke(sm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((volumePaths != null) && (volumePaths.length > 0)) {
            for (String sdcardPath : volumePaths) {
                //创建一个文件夹判断文件夹是否可用
               /* File file = new File(sdcardPath + "/testdir");
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                file = new File(sdcardPath + "/testdir");*/
                File file = new File(sdcardPath);
                if (file.isDirectory()) {
                    String path = file.getAbsolutePath();
                    if (file.toString().toLowerCase().contains("usb") ) {
                        // usb 路径
                        if(!usbPaths.contains(path)){
                            usbPaths.add(path);
                            Log.d("FileUtils","usb 路径 : "+path);
                        }
                    }
                }
            }
        }
        return usbPaths;
    }
    static List<File> fileSort;
    public static File [] sort(File [] fileArray,String selectedPosition){
        List<File> files = Arrays.asList(fileArray);
        fileSort = new ArrayList<>();
        fileSort.clear();

       for (int i = 0; i < files.size(); i++){

           if (selectedPosition.equalsIgnoreCase("Audio")){
               if (files.get(i).isDirectory() || TypeUtils.isAudio(files.get(i).getName().toLowerCase())){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("Videos")){
               if (files.get(i).isDirectory() || TypeUtils.isVideo(files.get(i).getName().toLowerCase())){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("Photos")){
               if (files.get(i).isDirectory() || TypeUtils.isImage(files.get(i).getName().toLowerCase())){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("Docs")){
               if (files.get(i).isDirectory() || TypeUtils.isTxt(files.get(i).getName().toLowerCase())){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("Apps")){
               if (files.get(i).isDirectory() || files.get(i).getName().endsWith(".apk")){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("Zip")){
               if (files.get(i).isDirectory() || files.get(i).getName().endsWith(".zip") || files.get(i).getName().endsWith(".rar")){
                   fileSort.add(files.get(i));
               }
           } else if (selectedPosition.equalsIgnoreCase("All")){
               fileSort.add(files.get(i));
//               break;
           }

       }

        Collections.sort(fileSort, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() == f2.isDirectory())
                    return 0;
                else if (f1.isDirectory() && !f2.isDirectory())
                    return -1;
                else
                    return 1;
            }
        });



        return fileSort.toArray(new File[fileSort.size()]);
    }

    /**
     * 重命名文件
     * @param file
     * @param newName
     */
    public static boolean renameFile(File file,String newName){
        if(isLegal(newName)){
            try {
                String oldName = file.getName();
                newName = file.getAbsolutePath().replace(oldName, newName);
                File temp = new File(newName);
                if(temp.exists()){
                    ToastHelper.newInstance(UiUtils.getContext()).show(R.string.file_existed);
                    return false;
                }
                boolean res = file.renameTo(temp);
                L.e("temp:"+temp.getAbsolutePath());
                L.e("newFiel:"+file.getAbsolutePath());
                if(res){
                    //在这里更新一下数据库,让媒体库去实现更新
                    updateDataBase(file.getAbsolutePath(),temp.getAbsolutePath(),false);
                    return true;
                }else {
                    ToastHelper.newInstance(UiUtils.getContext()).show(R.string.rename_file_fail);

                    return false;
                }
            }catch (RuntimeException ex){
                ToastHelper.newInstance(UiUtils.getContext()).show(R.string.rename_file_fail);
                ex.printStackTrace();
                return false;
            }

        }else{
            ToastHelper.newInstance(UiUtils.getContext()).show(R.string.rename_file_Illegal);
            return false;
        }
    }

    /**
     * 判断文件名是否合法
     * @param newName
     * @return
     */
    public static boolean isLegal(String newName){
        Pattern pattern = Pattern.compile("(?!((^(con)$)|^(con)//..*|(^(prn)$)|^(prn)//..*|(^(aux)$)|^(aux)//..*|(^(nul)$)|^(nul)//..*|(^(com)[1-9]$)|^(com)[1-9]//..*|(^(lpt)[1-9]$)|^(lpt)[1-9]//..*)|^//s+|.*//s$)(^[^/////////://*//?///\"//<//>//|]{1,255}$)");
        Matcher matcher = pattern.matcher(newName);
        return  matcher.matches();
    }


    /**
     * 更新媒体库
     * @param oldPath 原路径
     * @param newPath 新路径
     */
    public static void updateDataBase(String oldPath,String newPath,boolean isDel) {
        Uri uri = getMediaUrl(oldPath);
        // 说明不是媒体文件
        if(uri==null){return;}
        if(isDel){
            int delete = UiUtils.getContext().getContentResolver().delete(uri, "_data=?", new String[]{oldPath});
            L.e("delete:"+delete);
        }else {
            ContentValues values = new ContentValues();
            values.put("_data",newPath);
            String where = "_data=?";
            int update = UiUtils.getContext().getContentResolver().update(uri, values, where, new String[]{oldPath});
            L.e("update:"+update+",MediaUrl:"+getMediaUrl(oldPath));
        }
    }


    /**
     * 更新媒体库
     * @param newPath 新路径
     */
    public static void updateDataBase(String newPath) {
        Uri uri = getMediaUrl(newPath);
        if(uri==null){return;}
        ContentValues values = new ContentValues();
        values.put("_data",newPath);
        Uri insert = UiUtils.getContext().getContentResolver().insert(uri, values);
        L.e("insert:"+insert);
    }


    /**
     * 获取当前的查询URI
     * @return Uri
     * @param oldPath
     */
    public static Uri getMediaUrl(String oldPath) {
        if(TypeUtils.isAudio(oldPath)){
            return MediaStore.Audio.Media.getContentUri("external");
        }else if(TypeUtils.isImage(oldPath)){
            return MediaStore.Images.Media.getContentUri("external");
        }else if(TypeUtils.isVideo(oldPath)){
            return MediaStore.Video.Media.getContentUri("external");
        }else {
            return null;
        }
    }


    /**
     * 创建文件夹
     * @param path
     * @param name
     */
    public static void createFolder(String path,String name){
        if(isLegal(name)){
            try {
                File file = new File(path+"/"+name);
                if(!file.exists()){
                    boolean res = file.mkdir();
                    if(res){
                        ToastHelper.newInstance(UiUtils.getContext()).show("Created successfully");
                    }else {
                        ToastHelper.newInstance(UiUtils.getContext()).show("Creation failed");                    }
                }
            }catch (RuntimeException ex){
                ex.printStackTrace();
                ToastHelper.newInstance(UiUtils.getContext()).show("Creation failed");
            }
        }else{
            ToastHelper.newInstance(UiUtils.getContext()).show("Illegal file name");
        }
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getEx(String filePath){
        Log.d("FocusTest", "getEx: "+filePath);
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }


    /**
     * 递归删除文件
     * @param srcFolder
     */
    public static void deleteFolder(File srcFolder) {
        // 获取该目录下的所有文件或者文件夹的File数组
        File[] fileArray = srcFolder.listFiles();
        if (fileArray != null) {
            // 遍历该File数组，得到每一个File对象
            for (File file : fileArray) {
                // 判断该File对象是否是文件夹
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                    updateDataBase(file.getAbsolutePath(),null,true);
                }
            }
            srcFolder.delete();
        }
    }
}
