package com.susiha.library;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class AsyncStarter {

    /**
     * 保存Task 在map中
     */
    public  HashMap<Class<? extends Task>,Task> taskMap ;
    private List<Task> taskList;
    private List<Task> finishedTasks;
    private boolean isRunning = false;
    private StartCallBack mCallBack;
    private AsyncStarter(StartCallBack callBack){

        this.mCallBack = callBack;
        taskMap = new HashMap<>();
        taskList = new ArrayList<>();
        finishedTasks = new ArrayList<>();
    }


    private volatile static AsyncStarter INSTANCE = null;


    private static AsyncStarter getInstance(StartCallBack callBack){

        if(INSTANCE == null){
            synchronized (AsyncStarter.class){
                if(INSTANCE == null){
                    INSTANCE = new AsyncStarter(callBack);
                }
            }
        }
        return INSTANCE;
    }


    public static void init(){
         init(null);

    }

    public static void init(StartCallBack callBack){

        AsyncStarter.getInstance(callBack).addAllTask().start();
    }

    private AsyncStarter addAllTask(){

        if(isRunning){
            return this;
        }
        TaskLogger.logStart("反射");
        long startTime = System.currentTimeMillis();
        try {
            Class taskUtils = Class.forName("com.susiha.TaskUtils");
            Method method =taskUtils.getMethod("getTaskList");
            List<Task> tasks = (List<Task>) method.invoke(null);
            taskList.addAll(tasks);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        TaskLogger.logCostTime("反射",System.currentTimeMillis()-startTime);

        return this;
    }

    private AsyncStarter addTask(Task task){
         //如果已经运行了就不可以再添加任务了
         if(isRunning){
             return this;
         }
         taskList.add(task);
         return this;
    }

    private AsyncStarter start(){
        isRunning = true;
        TaskLogger.logStart("AsyncStarter");
        long startTime = System.currentTimeMillis();
        executor(SortUtils.dagTopology(taskList,taskMap));
        TaskLogger.logCostTime("AsyncStarter",System.currentTimeMillis()-startTime);
        return this;
    }
    /**
     * 执行
     * @param tasks
     */
    private void executor(List<Task> tasks){
        for (Task task : tasks) {
            if(task.runOnMainThread()){
                new TaskRunnable(task,this).run();
            }else{

                task.executor().execute(new TaskRunnable(task,this));
            }
        }
    }


    void finishCallBack(Task task){
        finishedTasks.add(task);
        if(finishedTasks.size() ==taskList.size()||mCallBack!=null){
            mCallBack.finish();
        }
    }











}
