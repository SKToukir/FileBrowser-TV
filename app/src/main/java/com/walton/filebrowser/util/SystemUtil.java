package com.walton.filebrowser.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SystemUtil {
    //获取当前年月日
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getDateWithWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 E", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    //获取当前时间
    public static String getTime() {
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatime.format(curDate);
    }

    public static String getWeek() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Date date = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static long getTotalMemory() {
        //系统的内存信息文件
        String filePath = "/proc/meminfo";
        String lineString;
        String[] stringArray;
        long totalMemory = 0;
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024 * 8);
            //读取meminfo第一行,获取系统总内存大小
            lineString = bufferedReader.readLine();
            //按照空格拆分
            stringArray = lineString.split("\\s+");
            //获得系统总内存,单位KB
            totalMemory = Integer.valueOf(stringArray[1]);
            bufferedReader.close();
            System.out.println("------> lineString=" + lineString + ",stringArray[0]=" + stringArray[0] +
                    ",stringArray[1]=" + stringArray[1] + ",stringArray[2]=" + stringArray[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMemory / 1024;
    }

    public static long getAvailMemory(ActivityManager mActivityManager) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / (1024 * 1024);
    }

    public static void cleanMemory(ActivityManager mActivityManager) {
        long availMemory = getAvailMemory(mActivityManager);
        RunningAppProcessInfo runningAppProcessInfo;
        List<RunningAppProcessInfo> mRunningAppProcessInfoList = mActivityManager.getRunningAppProcesses();
        StringBuilder mCleanInfoStringBuffer = new StringBuilder();
        if (mRunningAppProcessInfoList != null) {
            for (int i = 0; i < mRunningAppProcessInfoList.size(); ++i) {
                runningAppProcessInfo = mRunningAppProcessInfoList.get(i);
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE
                // 的进程即为长时间未使用进程或者空进程
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE
                // 的进程都是非可见进程,即在后台运行
                if (runningAppProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = runningAppProcessInfo.pkgList;
                    for (String aPkgList : pkgList) {
                        mActivityManager.killBackgroundProcesses(aPkgList);
                        mCleanInfoStringBuffer.append(aPkgList).append(" is killed...\n");
                    }
                }
            }
        }
        //再次获得剩余内存以计算清理值
        mCleanInfoStringBuffer.append("共清理：").append(getAvailMemory(mActivityManager) - availMemory).append("MB");
    }


    private static long time = 0;
    public static boolean isAccessTooQuick(int delay){
        if(System.currentTimeMillis() - time < delay){
            time = System.currentTimeMillis();
            L.d("Access too quick!!!");
            return true;
        }
        return false;
    }



}
