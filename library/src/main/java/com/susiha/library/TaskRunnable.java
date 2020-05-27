package com.susiha.library;


public class TaskRunnable implements Runnable {
    private Task task;
    private AsyncStarter starter;
    public TaskRunnable(Task task,AsyncStarter starter){
        this.task = task;
        this.starter = starter;
    }
    @Override
    public void run() {
        task.await();
        TaskLogger.logStart(task.getName());
        long startTime = System.currentTimeMillis();
        task.run();
        TaskLogger.logCostTime(task.getName(),System.currentTimeMillis()-startTime);
        //执行完通知执行后继
        if(task.getSucceedTasks()!=null){
            notifySucceed(task);
        }else{
            //通知Task已经完成
            starter.finishCallBack(task);
        }
    }

    private void notifySucceed(Task task){

        if(task.getSucceedTasks()!=null){
            for (Task succeedTask : task.getSucceedTasks()) {
                succeedTask.notifyTask();
            }
        }

    }


}
