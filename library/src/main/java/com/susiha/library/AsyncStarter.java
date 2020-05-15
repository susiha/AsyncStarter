package com.susiha.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class AsyncStarter {

    /**
     * 保存Task 在map中
     */
    public  HashMap<Class<? extends Task>,Task> taskMap ;
    private List<Task> taskList;
    private boolean isRunning = false;
    private AsyncStarter(){
        taskMap = new HashMap<>();
        taskList = new ArrayList<>();
    }

    /**
     * 内部 枚举类用于支持单例模式
     */
     enum SingletonEnum{
        INSTANCE;

        private AsyncStarter starter;

        SingletonEnum(){
            starter = new AsyncStarter();
        }

        public AsyncStarter getInstance(){
            return starter;
        }

    }


    public static AsyncStarter getInstance(){

        return SingletonEnum.INSTANCE.getInstance();
    }

    public AsyncStarter addTask(Task task){
         //如果已经运行了就不可以再添加任务了
         if(isRunning){
             return this;
         }
         taskList.add(task);
         return this;
    }

    public AsyncStarter start(){
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
                new TaskRunnable(task).run();
            }else{

                task.executor().execute(new TaskRunnable(task));
            }

        }
    }










}
