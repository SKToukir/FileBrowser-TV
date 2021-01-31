package com.walton.filebrowser.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static final String PERMISSION_PREFERENCES = "PermissionPref";
    public static final String KEY_PERMISSION = "key_permission";
    private static SharedPreferences sharedpreferences;
    private static Context mContext;

    private static PermissionUtils mPermissionUtils;


    public static PermissionUtils getInstance(Context context){
        mContext = context;
        sharedpreferences = mContext.getSharedPreferences(PERMISSION_PREFERENCES, Context.MODE_PRIVATE);
        if (mPermissionUtils == null){
            mPermissionUtils = new PermissionUtils();
        }
        return mPermissionUtils;
    }

    public void permissionGranted(boolean isGranted){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(KEY_PERMISSION, isGranted);
        editor.commit();
    }

    public boolean isPermissionGranted(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isGranted = preferences.getBoolean(KEY_PERMISSION,false);

        return isGranted;
    }



}
