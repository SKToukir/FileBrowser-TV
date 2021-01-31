package com.walton.filebrowser.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

import com.walton.filebrowser.interfaces.OnUsbSateChanged;

public class USBStateReceiver extends BroadcastReceiver  {

    private static final String TAG = USBStateReceiver.class.getSimpleName();

    private OnUsbSateChanged mOnUsbSateChange;
    public USBStateReceiver(OnUsbSateChanged onUsbSateChanged) {
        mOnUsbSateChange = onUsbSateChanged;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String path;
        String action = intent.getAction();
        switch (action) {
            case Intent.ACTION_MEDIA_REMOVED:
            case Intent.ACTION_MEDIA_BAD_REMOVAL:
                path = intent.getDataString().substring(8);
                Log.d(TAG,path + "---disconnect");
                mOnUsbSateChange.onUsbSateChange(false,path);
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                path = intent.getDataString().substring(8);
                Log.d(TAG,path + "---connect");
                mOnUsbSateChange.onUsbSateChange(true,path);
                break;
        }
    }

    public IntentFilter getIntentFilter(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        return filter;
    }
}
