package com.susiha.library;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskThreadPools {

    private static volatile TaskThreadPools taskExceutorManager;
    //CPU 密集型任务的线程池
    private ThreadPoolExecutor sCPUThreadPoolExecutor;
    // IO 密集型任务的线程池
    private ExecutorService sIOThreadPoolExecutor;
    //CPU 核数
    private  final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池线程数
    private  final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));
    //线程池线程数的最大值
    private  final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    //线程空置回收时间
    private  final int KEEP_ALIVE_SECONDS = 5;
    //线程池队列
    private  final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>();
    // 这个是为了保障任务超出BlockingQueue的最大值，且线程池中的线程数已经达到MAXIMUM_POOL_SIZE时候，还有任务到来会采取任务拒绝策略，这里定义的策略就是
    //再开一个缓存线程池去执行。当然BlockingQueue默认的最大值是int_max，所以理论上这里是用不到的
    private  final RejectedExecutionHandler sHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Executors.newCachedThreadPool().execute(r);
        }
    };
    public static TaskThreadPools getInstance() {
        if (taskExceutorManager==null){
            synchronized (TaskThreadPools.class){
                if (taskExceutorManager==null){
                    taskExceutorManager=new TaskThreadPools();
                }
            }
        }
        return taskExceutorManager;
    }
    //初始化线程池
    private TaskThreadPools(){
        sCPUThreadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, Executors.defaultThreadFactory(), sHandler);
        sCPUThreadPoolExecutor.allowCoreThreadTimeOut(true);
        sIOThreadPoolExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
    }
    //获得cpu密集型线程池,因为占据CPU的时间片过多的话会影响性能，所以这里控制了最大并发，防止主线程的时间片减少
    public ThreadPoolExecutor getCPUThreadPoolExecutor() {
        return sCPUThreadPoolExecutor;
    }
    //获得io密集型线程池，有好多任务其实占用的CPU time非常少，所以使用缓存线程池,基本上来着不拒
    public ExecutorService getIOThreadPoolExecutor() {
        return sIOThreadPoolExecutor;
    }
}
