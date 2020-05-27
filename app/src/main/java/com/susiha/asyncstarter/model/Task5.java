package com.susiha.asyncstarter.model;

import com.susiha.annotation.TaskAnnotation;
import com.susiha.library.Task;
import com.susiha.library.TaskThreadPools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
@TaskAnnotation("Task5")
public class Task5 extends Task {

    public Task5(String name) {
        super(name);
    }

    @Override
    public List<Class<? extends Task>> denpendsList() {
        List<Class<? extends Task>> list = new ArrayList<>();
        list.add(Task2.class);
        list.add(Task3.class);
        list.add(Task4.class);
        return list;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Executor executor() {
        return TaskThreadPools.getInstance().getIOThreadPoolExecutor();
    }

    @Override
    public boolean runOnMainThread() {
        return true;
    }
}
