package com.walton.filebrowser.ui.media.util;

import java.lang.reflect.Method;

public class SystemProperties {

    /**
     * Get the value for the given key.
     * @return an empty string if the key isn't found
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static String get(String key,String value) {
        String result = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            result = (String) get.invoke(c, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean getBoolean(String key,boolean flag) {
        boolean isFlag = false;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            isFlag = (Boolean) get.invoke(c, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isFlag;
    }

    /**
     * Set the value for the given key.
     * @throws IllegalArgumentException if the key exceeds 32 characters
     * @throws IllegalArgumentException if the value exceeds 92 characters
     */
    public static void set(String key, String val) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
