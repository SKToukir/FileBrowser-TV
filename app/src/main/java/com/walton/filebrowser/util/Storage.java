package com.walton.filebrowser.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class Storage extends File {

    public static final int INTERNAL_STORAGE = 1;
    public static final int SD_CARD = 2;
    public static final int USB_DRIVE = 3;

    public String name;
    public int type;

    public Storage(String path, String name, int type) {
        super(path);
        this.name = name;
        this.type = type;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Storage> getStorages(Context context) {
        ArrayList<Storage> storages = new ArrayList<>();

        // Internal storage
        storages.add(new Storage(Environment.getExternalStorageDirectory().getPath(),
                "Internal Storage", Storage.INTERNAL_STORAGE));

        // SD Cards
        ArrayList<File> extStorages = new ArrayList<>();
        extStorages.addAll(Arrays.asList(context.getExternalFilesDirs(null)));
        extStorages.remove(0); // Remove internal storage
        String secondaryStoragePath = System.getenv("SECONDARY_STORAGE");
        for (int i = 0; i < extStorages.size(); i++) {
            String path = extStorages.get(i).getPath().split("/Android")[0];
            if (Environment.isExternalStorageRemovable(extStorages.get(i)) || secondaryStoragePath != null && secondaryStoragePath.contains(path)) {
                String name = "SD Card" + (i == 0 ? "" : " " + String.valueOf(i + 1));
                storages.add(new Storage(path, name, Storage.SD_CARD));
            }
        }

        // USB Drives
        ArrayList<String> drives = new ArrayList<>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s += new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec") && line.matches(reg)) {
                String[] parts = line.split(" ");
                for (String path : parts) {
                    if (path.startsWith(File.separator) && !path.toLowerCase(Locale.US).contains("vold")) {
                        drives.add(path);
                    }
                }
            }
        }

        // Remove SD Cards from found drives (already found)
        ArrayList<String> ids = new ArrayList<>();
        for (Storage st : storages) {
            String[] parts = st.getPath().split(File.separator);
            ids.add(parts[parts.length - 1]);
        }
        for (int i = drives.size() - 1; i >= 0; i--) {
            String[] parts = drives.get(i).split(File.separator);
            String id = parts[parts.length - 1];
            if (ids.contains(id)) drives.remove(i);
        }

        // Get USB Drive name
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Collection<UsbDevice> dList = usbManager.getDeviceList().values();
        ArrayList<UsbDevice> deviceList = new ArrayList<>();
        deviceList.addAll(dList);
        for (int i = 0; i < deviceList.size(); i++) {
            storages.add(new Storage(drives.get(i), deviceList.get(i).getProductName(), Storage.USB_DRIVE));
        }

        return storages;
    }
}