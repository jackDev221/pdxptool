package org.example.job;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorJob {
    private ExecutorService fixedThreadPool;
    private ExecuteInfo executeInfo;
    private int workers;

    private ConcurrentStack<TaskInfo> taskStack = new ConcurrentStack<>();


    CountDownLatch latch;

    public ExecutorJob(int num) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        fixedThreadPool = Executors.newFixedThreadPool(coreSize);
        workers = num > coreSize ? coreSize : num;
        latch = new CountDownLatch(workers);
        executeInfo = new ExecuteInfo();
    }

    public void addTask(TaskInfo taskInfo) {
        taskStack.push(taskInfo);
    }

    public void submit() {
        executeInfo.setTaskNum(taskStack.size());
        executeInfo.start();
        for (int i = 0; i < workers; i++) {
            final int num = i;
            fixedThreadPool.submit(() -> {
                while (!taskStack.isEmpty()) {
                    TaskInfo taskInfo = taskStack.pop();
                    if (taskInfo != null) {
                        System.out.println(num + "  :  " + taskInfo.tokenStr);
                        executeInfo.getSuccess().incrementAndGet();
                    }
                }
                latch.countDown();
            });
        }
    }

    public void waitFinish() {
        try {
            latch.await();
            executeInfo.finish();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }

    public String getExecuteResult() {
        return executeInfo.getResult();
    }

}
