package com.walton.filebrowser.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.walton.filebrowser.receiver.UsbReceiver;
import com.walton.filebrowser.receiver.UsbSatesReceiver;

public class UsbService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        iFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        iFilter.addDataScheme("file");
        UsbSatesReceiver usbReceiver = new UsbSatesReceiver();
        registerReceiver(usbReceiver,iFilter);
    }
}
