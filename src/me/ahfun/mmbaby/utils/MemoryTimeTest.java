package me.ahfun.mmbaby.utils;

import android.util.Log;

/**
 * Created by sudongsheng on 2014/4/22 0022.
 */
public class MemoryTimeTest {

    private static long startMemory;
    private static long endMemory;

    private static long startTime ;
    private static long endTime ;

    private static long memoryUsed() {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        return (total - free);
    }

    public static void start() {
        startTime = System.currentTimeMillis();
        startMemory = memoryUsed();
    }

    public static void end() {
        endMemory = memoryUsed();
        endTime = System.currentTimeMillis();
        long memo = endMemory - startMemory;
        long time = endTime - startTime;
        Log.i("TAG", "calculateUsedMemory is " + memo);
        Log.i("TAG", "calculateProcessTime is "+time);
    }
}
