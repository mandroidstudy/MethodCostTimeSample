package com.maove.libutil;

import android.util.Log;

import java.util.HashMap;

/**
 * @author maoweiyi
 * @time 2022/3/7
 * @describe
 */
public class TimeMonitor {

    private static final HashMap<String,Long> timeMap = new HashMap<>();

    public static void i(String className,String methodName,String descriptor){
        if (className.contains(TimeMonitor.class.getSimpleName())) return;
        String key= className+"_"+methodName+"_"+descriptor;
        timeMap.put(key,System.currentTimeMillis());
    }

    public static void o(final String className,String methodName,String descriptor){
        if (className.contains(TimeMonitor.class.getSimpleName())) return;
        String key= className+"_"+methodName+"_"+descriptor;
        long endTime = System.currentTimeMillis();
        Long startTime = timeMap.get(key);
        if (startTime!=null){
            timeMap.remove(key);
            long cost = endTime - startTime;
            //只答应耗时超过OVER_TIME ms的方法
            long OVER_TIME = 2;
            if (cost > OVER_TIME){
                String tag = className.substring(className.lastIndexOf("/")+1);
                Log.d(TimeMonitor.class.getSimpleName(),tag+"#"+methodName+":"+cost+"ms" + " thread:"+Thread.currentThread().getName());
            }
        }
    }
}
