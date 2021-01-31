package com.walton.filebrowser.receiver;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import com.walton.filebrowser.R;
import com.walton.filebrowser.dialog.DialogFileBrowserActivity;
import com.walton.filebrowser.ui.main.FileBrowserActivity;
import com.walton.filebrowser.ui.main.RootActivity;
import com.walton.filebrowser.util.UiUtils;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by llh on 2016/3/16.
 * USB广播接收者
 */
public class UsbSatesReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbSatesReceiver";
    private LinkedList<String> mDevices = new LinkedList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        String path;
        String action = intent.getAction();

        /**
         * Add by anakin
         * When usb is removed, will the flag initialized for the first time isFirstInit = true ,
         *          * Delete the database and rescan the file
         *
         *  /storage/8278-6532
         */


        switch (action) {
            case Intent.ACTION_MEDIA_REMOVED:
            case Intent.ACTION_MEDIA_BAD_REMOVAL:
                path = intent.getDataString().substring(8);
                Log.d(TAG, "Without Substring " + intent.getDataString());
                Log.d(TAG, "Substring " + path);
                if (mDevices.contains(path)) {
                    mDevices.remove(path);
                }
                saveDevices();
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                Log.d(TAG, "remove Substring " + path);
                sp.edit().putString("forScan", path).apply();
                notifyScanFile(path);

                break;
            case Intent.ACTION_MEDIA_MOUNTED:
//                sp.edit().putString(Constant.USB.STATUS,Constant.USB.INSERT);
                path = intent.getDataString().substring(8);

                /**
                 * s905x file:///storage/8278-6532
                 */
                if (!path.toLowerCase().contains("usb") && !path.toLowerCase().contains("mnt/")) {
                    return;
                }
                if (!mDevices.contains(path)) {
                    mDevices.add(path);
                    saveDevices();
                }
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                sp.edit().putString("forScan", path).apply();

                Log.d(TAG, "U disk detected, get usb path ::: " + mDevices.toString());
                notifyScanFile(path);
                //Start all file interface
//                Intent fileIntent = new Intent(UiUtils.getContext(), RootActivity.class);
//                fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                UiUtils.getContext().startActivity(fileIntent);

                break;
        }
    }


    private void saveDevices() {
        @SuppressLint("WrongConstant") SharedPreferences sp = UiUtils.getContext().getSharedPreferences("config", Context.MODE_APPEND);
        sp.edit().putString("usb1", (mDevices.size() < 1 || mDevices.get(0) == null) ? "" : mDevices.get(0)).apply();
        sp.edit().putString("usb2", (mDevices.size() < 2 || mDevices.get(1) == null) ? "" : mDevices.get(1)).apply();
    }

    /**
     * There is a device to insert the latter is to pull out, notify the system to update the media library
     *
     * @param path Insert or unplug device path
     */
    private void notifyScanFile(String path) {
        final Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(path));
        mediaScanIntent.setData(contentUri);
        UiUtils.getContext().sendBroadcast(mediaScanIntent);
    }
}
