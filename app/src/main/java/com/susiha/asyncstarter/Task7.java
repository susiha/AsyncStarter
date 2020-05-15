package com.susiha.asyncstarter;

import com.susiha.library.Task;
import com.susiha.library.TaskThreadPools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class Task7 extends Task {

    public Task7(String name) {
        super(name);
    }

    @Override
    public List<Class<? extends Task>> denpendsList() {
        List<Class<? extends Task>> list = new ArrayList<>();
        list.add(Task1.class);
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
        return false;
    }
}
