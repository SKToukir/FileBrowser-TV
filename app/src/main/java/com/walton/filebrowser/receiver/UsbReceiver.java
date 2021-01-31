package com.walton.filebrowser.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.walton.filebrowser.ui.main.RootActivity;
import com.walton.filebrowser.util.L;

import java.io.File;

/**
 * Created by demo on 2017/4/1 0001
 */
public class UsbReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
//                Toast.makeText(context, "USB设备移除", Toast.LENGTH_SHORT).show();
//                System.exit(0);
            } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                //USB device mount, update UI
                // String usbPath = intent.getDataString();（usb在手机上的路径）
                File storage = new File("/storage");
                File[] files = storage.listFiles();
                for (final File file : files) {
                    if (file.canRead()&&!file.getName().equals(Environment.getExternalStorageDirectory().getName())) {
                        //The folder that meets this condition is the directory of the u disk on the phone.
                        //Start all file interface
                        Intent fileIntent = new Intent(context, RootActivity.class);
                        fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(fileIntent);
                    }
                }


            }
        }
    }


}
