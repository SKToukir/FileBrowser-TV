package com.walton.filebrowser.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.os.StrictMode;
import android.util.Log;
import android.util.Slog;

import androidx.core.content.FileProvider;

import com.mstar.android.MIntent;
import com.walton.filebrowser.ui.media.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApkUtil {

    /**
     * 获取apk的图标
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static Drawable loadApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo apkInfo = info.applicationInfo;
            apkInfo.publicSourceDir = apkPath;
            Drawable icon = apkInfo.loadIcon(pm);
            return icon;
        }
        return null;
    }

    /**
     * 获取应用程序的名字
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String loadApkName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo apkInfo = info.applicationInfo;
            apkInfo.publicSourceDir = apkPath;
            return apkInfo.loadLabel(pm).toString();
        }
        return null;
    }

    /**
     * 安装APK文件
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath, File fileEntity) throws FileNotFoundException {
        Log.d("ApkPath", apkPath);

        Intent intent5 = new Intent("android.intent.action.VIEW");
        intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent5.setDataAndType(Uri.fromFile(fileEntity), "application/vnd.android.package-archive");
        context.startActivity(intent5);

    }

    public static void revokeFileReadPermission(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            String dirpath = context.getFilesDir() + File.separator + "directory";
            File file = new File(dirpath + File.separator + "file.apk");
            Uri uri = FileProvider.getUriForFile(context, "com.walton.filebrowser.fileprovider", file);
            context.revokeUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            https://stackoverflow.com/questions/18249007/how-to-use-support-fileprovider-for-sharing-content-to-other-apps?rq=1

        }
    }


    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
