package com.walton.filebrowser.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class StorageSpaceCount {

    private static final long MEGABYTE = 1024L * 1024L;

    public static int getSpaceInMegabyte(long bytes) {
        return (int) (bytes / MEGABYTE);
    }

    /**
     * @return Number of bytes available on External storage
     */
    public static long getAvailableSpaceInBytes(File file) {
        long availableSpace = -1L;
        StatFs stat = new StatFs(file.getPath());
        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

        return availableSpace;
    }

    public static long getTotalSpaceInBytes(File file) {
        long totalSpace = -1L;
        StatFs stat = new StatFs(file.getPath());
        totalSpace = (long) stat.getBlockCount() * (long) stat.getBlockSize();

        return totalSpace;
    }

//    public static long getBusyMemorySpaceInBytes(File file) {
//
//
//        return getTotalSpaceInBytes(file) - getAvailableSpaceInBytes(file);
//    }
//
//    public long totalMemory() {
//        StatFs statFs = new StatFs((Environment.getExternalStorageDirectory().getAbsolutePath()));
//        long total = (statFs.getBlockCount() * statFs.getBlockSize());
//        return total;
//    }
//
//    public long freeMemory() {
//        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
//        long free = (statFs.getAvailableBlocks() * statFs.getBlockSize());
//        return free;
//    }
//
//    public long busyMemory() {
//        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
//        long total = (statFs.getBlockCount() * statFs.getBlockSize());
//        long free = (statFs.getAvailableBlocks() * statFs.getBlockSize());
//        long busy = total - free;
//        return busy;
//    }
//
//    public long usbSize(Context mContext){
//        usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
//    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }


    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " KB";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " MB";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " GB";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " TB";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " PB";
        if (size >= Eb) return floatForm((double) size / Eb) + " EB";

        return "???";
    }

    private List<String> deviceNameList;

    private String deviceName;

//    @SuppressLint("NewApi")
//    public String getUsbDeviceName(int position, Context mContext) {
//
//        deviceNameList = new ArrayList<>();
//
//        UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
//
//        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
//        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//
//        while (deviceIterator.hasNext()) {
//            UsbDevice usbDevice = deviceIterator.next();
//            deviceNameList.add(usbDevice.getSerialNumber());
//        }
//
//        try {
//            deviceName = deviceNameList.get(position);
//        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
//            deviceName = "USB Disk "+String.valueOf(position);
//        }
//        Log.d("DeviceNum", "getUsbDeviceName: "+deviceName+" Position:"+position);
//
//
//        return deviceName;
//    }

}