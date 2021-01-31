package com.walton.filebrowser.ui.media.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MountInfo {
    private static final String TAG = "LocalPlayer--MountInfo";
    private ArrayList<String> mAllVolumes;
    private Method mMethod_getVolumePaths;
    public String[] path = new String[64];
    public int[] type = new int[64];
    public String[] label = new String[64];
    public String[] partition = new String[64];
    public int index = 0;
    private StorageManager mStorageManager = null;
    Context mContext;

    // SocketClient socketClient = null;
    public MountInfo(Context context) {
        HashMap<String, SDCardInfo> allmap = getSDCardInfo(context);
        Iterator iter = allmap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            SDCardInfo val = (SDCardInfo) entry.getValue();
           /* String key = (String) entry.getKey();
            String mountlabel = val.getLabel();
            String mountpath = val.getMountPoint();
            Log.i(TAG, "key =" + key);
            Log.i(TAG, "label =" + mountlabel);
            Log.i(TAG, "path =" + mountpath);*/
            path[index] = val.getMountPoint();
            label[index] = "C";
            partition[index] = label[index];
            index++;
        }
        //Log.i(TAG, "index =" + index);
        for (int i = 0; i < index; i++) {
            if (path[i].contains("sdcard") || path[i].contains("emulated")) {
                type[i] = 2;
            } else {
                type[i] = 0;
            }
        }
    }

    public String getMountDevices(String path) {
        int start = 0;
        start = path.lastIndexOf("/");
        String mountPath = path.substring(start + 1);
        return mountPath;
    }

    private String[] getDevicePath(StorageVolume[] storageVolumes) {
        String[] tmpPath = new String[storageVolumes.length];
        for (int i = 0; i < storageVolumes.length; i++) {
            // psqiu
            //tmpPath[i] = getMountDevices(storageVolumes[i].getPath());
            tmpPath[i] = getMountDevices(getPath(storageVolumes[i]));
        }
        int count = storageVolumes.length;
        // delete repeat
        for (int i = 0; i < storageVolumes.length; i++) {
            for (int j = i + 1; j < storageVolumes.length; j++) {
                try {
                    if (tmpPath[i] != null) {
                        if (tmpPath[j].equals(tmpPath[i]) && tmpPath[j] != null) {
                            tmpPath[j] = null;
                            count--;
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        String[] path = new String[count];
        int j = 0;
        for (int i = 0; i < storageVolumes.length; i++) {
            if (tmpPath[i] != null) {
                path[j] = tmpPath[i];
                j++;
            }
        }
        // sort
        for (int i = 0; i < count; i++) {
            for (int k = i + 1; k < count; k++) {
                if (path[i].compareTo(path[k]) > 0) {
                    String tmp = path[k];
                    path[k] = path[i];
                    path[i] = tmp;
                }
            }
        }
        return path;
    }


    public String readFile(String fileName) {
        String output = "";
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {
                try {
                    BufferedReader input = new BufferedReader(new FileReader(file));
                    output = input.readLine();
                    input.close();
                } catch (IOException ioException) {
                    System.err.println("File Error!");
                }
            }
        } else {
            System.err.println("File Does Not Exit!");
        }
        return output;
    }

    public ArrayList<String> getAllVolumes() {
        if (this.mAllVolumes == null)
            this.mAllVolumes = new ArrayList<String>(Arrays.asList(getVolumePaths()));
        return (ArrayList<String>) this.mAllVolumes.clone();
    }

    private String[] getVolumePaths() {
        try {
            String[] arrayOfString = (String[]) (String[]) this.mMethod_getVolumePaths.invoke(this.mStorageManager, new Object[0]);
            return arrayOfString;
        } catch (IllegalArgumentException localIllegalArgumentException) {
            localIllegalArgumentException.printStackTrace();
            return null;
        } catch (IllegalAccessException localIllegalAccessException) {
            localIllegalAccessException.printStackTrace();
            return null;
        } catch (InvocationTargetException localInvocationTargetException) {
            localInvocationTargetException.printStackTrace();
        }
        return null;
    }

    public HashMap<String, SDCardInfo> getSDCardInfo(Context context) {
        HashMap<String, SDCardInfo> sdCardInfos = new HashMap<String, SDCardInfo>();
        String[] storagePathList = null;
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Method getVolumePaths = storageManager.getClass().getMethod("getVolumePaths");
            storagePathList = (String[]) getVolumePaths.invoke(storageManager);
            /*for (int i = 0; i < storagePathList.length; i++) {
                Log.i(TAG, "getSDCardInfo storagePathList " + i + "=" + storagePathList[i]);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (storagePathList != null && storagePathList.length > 0) {
            String mSDCardPath = storagePathList[0];
            SDCardInfo internalDevInfo = new SDCardInfo();
            internalDevInfo.setMountPoint(mSDCardPath);
            internalDevInfo.setMounted(checkSDCardMounted(context, mSDCardPath));
            if (internalDevInfo.mounted) {
                sdCardInfos.put(SDCARD_INTERNAL, internalDevInfo);
            }
            //Log.i(TAG, "internalDevInfo.mounted=" + internalDevInfo.mounted + ",length==" + storagePathList.length);
            if (storagePathList.length >= 2) {
                for (int i = 1; i < storagePathList.length; i++) {
                    String externalDevPath = storagePathList[i];
                    SDCardInfo externalDevInfo = new SDCardInfo();
                    externalDevInfo.setMountPoint(storagePathList[i]);
                    externalDevInfo.setMounted(checkSDCardMounted(context, externalDevPath));
                    if (externalDevInfo.mounted) {
                        sdCardInfos.put(SDCARD_EXTERNAL + i, externalDevInfo);
                    }
                }
            }
        }
        return sdCardInfos;
    }

    /**
     * @param context    上下文
     * @param mountPoint 挂载点
     * @Description:判断SDCard是否挂载上,返回值为true证明挂载上了，否则未挂载
     */
    protected static boolean checkSDCardMounted(Context context,
                                                String mountPoint) {
        if (mountPoint == null) {
            return false;
        }
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getVolumeState = storageManager.getClass().getMethod("getVolumeState", String.class);
            String state = (String) getVolumeState.invoke(storageManager, mountPoint);
            //Log.i(TAG, "checkSDCardMounted state=" + state);
            return Environment.MEDIA_MOUNTED.equals(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * SDCardInfo类
     *
     * @author Administrator
     */
    public class SDCardInfo {
        /**
         * 名称
         */
        private String label;

        /**
         * 挂载点
         */
        private String mountPoint;

        /**
         * 是否已挂载
         */
        private boolean mounted;

        /**
         * @return SD卡名称
         * @Description:获取SD卡名称
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label SD卡名称
         * @Description:设置SD卡名称
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * @return 挂载路径
         * @Description:获取挂载路径
         */
        public String getMountPoint() {
            return mountPoint;
        }

        /**
         * @param mountPoint 挂载路径
         * @Description:设置挂载路径
         */
        public void setMountPoint(String mountPoint) {
            this.mountPoint = mountPoint;
        }

        /**
         * @return true:已经挂载，false:未挂载
         * @Description:是否已经挂载
         */
        public boolean isMounted() {
            return mounted;
        }

        /**
         * @param mounted true:已经挂载，false:未挂载
         * @Description:设置是否已经挂载
         */
        public void setMounted(boolean mounted) {
            this.mounted = mounted;
        }

        @Override
        public String toString() {
            return "SDCardInfo [label=" + label + ", mountPoint=" + mountPoint + ", mounted=" + mounted + "]";
        }
    }

    /**
     * 内置
     */
    static String SDCARD_INTERNAL = "internal";

    /**
     * 外置
     */
    static String SDCARD_EXTERNAL = "external";

    /**
     * 获得sd卡路径
     *
     * @return
     */
    public static String getSDPath() {
        File[] files = new File("/mnt").listFiles();
        String strExternalDir = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();
        Log.i(TAG, "getSDPath getExternalStorageDirectory=" + strExternalDir);
        for (int i = 0; i < files.length; i++) {
            String strFilePath = files[i].getAbsolutePath().toLowerCase();
            Log.i(TAG, "getSDPath strFilePath=" + strFilePath);
            if (!strFilePath.equals(strExternalDir) && (strFilePath.contains("ext") || strFilePath.contains("sdcard"))) {
                return strFilePath;
            }
        }
        return null;
    }

    public static String getPath(StorageVolume storageVolume) {
        try {
            Method getPath = StorageVolume.class.getMethod("getPath");
            String path = (String) getPath.invoke(storageVolume);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
