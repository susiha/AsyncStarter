package com.susiha.asyncstarter;

import com.susiha.library.Task;
import com.susiha.library.TaskThreadPools;

import java.util.List;
import java.util.concurrent.Executor;

public class Task3 extends Task {

    public Task3(String name) {
        super(name);
    }

    @Override
    public List<Class<? extends Task>> denpendsList() {
        return null;
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
