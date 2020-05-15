package com.susiha.library;


public class TaskRunnable implements Runnable {
    private Task task;
    public TaskRunnable(Task task){
        this.task = task;
    }
    @Override
    public void run() {
        task.await();
        TaskLogger.logStart(task.getName());
        long startTime = System.currentTimeMillis();
        task.run();
        TaskLogger.logCostTime(task.getName(),System.currentTimeMillis()-startTime);
        //执行完通知执行后继
        notifySucceed(task);
    }

    private void notifySucceed(Task task){

        if(task.getSucceedTasks()!=null){
            for (Task succeedTask : task.getSucceedTasks()) {
                succeedTask.notifyTask();
            }
        }
    }


}
