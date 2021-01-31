package com.walton.filebrowser.util;

import android.os.Build;
import android.os.StatFs;

import java.util.Locale;

public final class StatUtils {

    public static long totalMemory(String path) {
        StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            return (statFs.getBlockCount() * statFs.getBlockSize());
        } else {
            return (statFs.getBlockCountLong() * statFs.getBlockSizeLong());
        }
    }

    public static long freeMemory(String path) {
        StatFs statFs = new StatFs(path);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //noinspection deprecation
            return (statFs.getAvailableBlocks() * statFs.getBlockSize());
        } else {
            return (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
        }
    }

    public static long usedMemory(String path) {
        long total = totalMemory(path);
        long free = freeMemory(path);
        return total - free;
    }

    public static String humanize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}