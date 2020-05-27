package com.susiha.library;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 *
 */
public abstract  class Task {
    private String name;
    private Boolean isFinish = false; // 是否执行完成
    private List<Task> succeedTasks;
    private CountDownLatch denpendsCount = new CountDownLatch(denpendsList() ==null?0:denpendsList().size());
    public Task(String name){
        this.name = name;
    }
    //依赖的列表
    public abstract List<Class<? extends Task>> denpendsList();
    //任务的执行
    public abstract void run();
    // 线程池
    public abstract Executor executor();
    // 是否运行在主线程
    public abstract boolean runOnMainThread();
    //是否需要等待,这个取决于在执行动态过程中依赖的Task 是否全部执行完成 默认是false
    public boolean isNeedwait(){
       return denpendsCount.getCount() ==0? false:true;
    }

    public List<Task> getSucceedTasks() {
        return succeedTasks;
    }

    public void setSucceedTasks(List<Task> succeedTasks) {
        this.succeedTasks = succeedTasks;
    }

    /**
     * 等待依赖的任务先执行完
     */
    public void await(){
        try {
            denpendsCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }



    public String getName(){
        return name;
    }


    /**
     * 通知执行
     */
    public void notifyTask(){

        denpendsCount.countDown();
    }
}
