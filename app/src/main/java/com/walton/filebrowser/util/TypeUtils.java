package com.walton.filebrowser.util;

/**
 * Created by penghuilou on 2016/3/10.
 * 获取文件的类型
 */
public class TypeUtils {
    public static boolean isTxt(String name) {
        if (name.endsWith(".txt") || name.endsWith(".log") || name.endsWith(".rtf") || name.endsWith(".conf") || name.endsWith(".xml")) {
            return true;
        }
        return false;
    }

    public static boolean isImage(String name) {
        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith("jpeg")) {
            return true;
        }
        return false;
    }

    public static boolean isVideo(String name) {
        /*if (name.endsWith(".mp4")
                || name.endsWith(".avi")
                || name.endsWith(".flv")
                || name.endsWith(".rmvb")
                || name.endsWith("wmv")
                || name.endsWith(".mkv")
                || name.equals(".mov")
                || name.endsWith(".mpeg")
                || name.endsWith("mpg")) {
            return true;
        }
        return false;*/

        /**
         * Modify by anakin
         */
        int i = name.indexOf('.');
        if (i != -1) {
            name = name.substring(i);
            if (name.equalsIgnoreCase(".mp4")
                    || name.equalsIgnoreCase(".3gp")
                    || name.equalsIgnoreCase(".dat")
                    || name.equalsIgnoreCase(".wmv")
                    || name.equalsIgnoreCase(".ts")
                    || name.equalsIgnoreCase(".rmvb")
                    || name.equalsIgnoreCase(".mov")
                    || name.equalsIgnoreCase(".m4v")
                    || name.equalsIgnoreCase(".avi")
                    || name.equalsIgnoreCase(".m3u8")
                    || name.equalsIgnoreCase(".3gpp")
                    || name.equalsIgnoreCase(".3gpp2")
                    || name.equalsIgnoreCase(".mkv")
                    || name.equalsIgnoreCase(".flv")
                    || name.equalsIgnoreCase(".divx")
                    || name.equalsIgnoreCase(".f4v")
                    || name.equalsIgnoreCase(".rm")
                    || name.equalsIgnoreCase(".asf")
                    || name.equalsIgnoreCase(".ram")
                    || name.equalsIgnoreCase(".mpg")
                    || name.equalsIgnoreCase(".v8")
                    || name.equalsIgnoreCase(".swf")
                    || name.equalsIgnoreCase(".m2v")
                    || name.equalsIgnoreCase(".asx")
                    || name.equalsIgnoreCase(".ra")
                    || name.equalsIgnoreCase(".tp")
                    || name.equalsIgnoreCase(".ndivx")
                    || name.equalsIgnoreCase(".xvid")) {
                return true;
            } else {
                return false;
            }
        }
        return false;

    }


    public static boolean isAudio(String name) {
        if (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".wma") || name.endsWith(".midi") || name.endsWith(".aac")) {
            return true;
        }
        return false;
    }
}
