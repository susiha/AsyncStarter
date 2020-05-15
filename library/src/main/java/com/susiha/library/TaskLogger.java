package com.susiha.library;

import android.util.Log;

public class TaskLogger {

    public static final String TAG = "TaskLogger";

    public static void logStart(String name){

        Log.i(TAG,name+" started by thread "+Thread.currentThread().getName());
    }


    public static void logCostTime(String name,long time){
        Log.i(TAG,name+" costTime =  "+time);
    }
}
