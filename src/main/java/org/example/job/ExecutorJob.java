package org.example.job;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorJob {
    private ExecutorService fixedThreadPool;
    private int workers;

    private ConcurrentStack<TaskInfo> taskStack = new ConcurrentStack<>();

    CountDownLatch latch;

    public ExecutorJob(int num) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        fixedThreadPool = Executors.newFixedThreadPool(coreSize);
        workers = num > coreSize ? coreSize : num;
        latch = new CountDownLatch(workers);
    }

    public void addTask(TaskInfo taskInfo) {
        taskStack.push(taskInfo);
    }

    public void submit() {
        for (int i = 0; i < workers; i++) {
            final int num = i;
            fixedThreadPool.submit(() -> {
                while (!taskStack.isEmpty()) {
                    TaskInfo taskInfo = taskStack.pop();
                    if (taskInfo != null) {
                        System.out.println(num + "  :  " + taskInfo.tokenStr);
                    }
                }
                latch.countDown();
            });
        }
    }

    public void waitFinish() {
        try {
            latch.await();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }

}
