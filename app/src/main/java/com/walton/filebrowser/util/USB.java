package com.walton.filebrowser.util;

public class USB {

    private String key;
    private String path;
    private String usbName;
    private String storageTotal;
    private String storageFree;
    private String storageUses;

    public String getStorageTotal() {
        return storageTotal;
    }

    public void setStorageTotal(String storageTotal) {
        this.storageTotal = storageTotal;
    }

    public String getStorageFree() {
        return storageFree;
    }

    public void setStorageFree(String storageFree) {
        this.storageFree = storageFree;
    }

    public String getStorageUses() {
        return storageUses;
    }

    public void setStorageUses(String storageUses) {
        this.storageUses = storageUses;
    }

    public String getUsbName() {
        return usbName;
    }

    public void setUsbName(String usbName) {

        this.usbName = usbName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
//        setUsbName(this.path.substring(this.path.lastIndexOf("/") + 1));
    }
}
