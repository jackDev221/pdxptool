package org.example.job;


import lombok.extern.slf4j.Slf4j;
import org.example.job.task.IBaseTask;
import org.example.job.worker.PDXPTaskWork;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class PressJobExecutor {
    private ExecutorService fixedThreadPool;
    private ExecuteInfo executeInfo;
    private int workersNum;
    private int batchNum;

    private ConcurrentStack<IBaseTask> currentBatchTasks = new ConcurrentStack<>();

    private ConcurrentStack<IBaseTask> nextBatchTasks;


    private CountDownLatch latch;

    public PressJobExecutor(int num) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        fixedThreadPool = Executors.newFixedThreadPool(coreSize);
        workersNum = num > coreSize ? coreSize : num;
        executeInfo = new ExecuteInfo();
    }

    public void addTask(IBaseTask taskInfo) {
        currentBatchTasks.push(taskInfo);
    }

    public void submit() {
        executeInfo.setTaskNum(currentBatchTasks.size());
        executeInfo.start();
        nextBatchTasks = new ConcurrentStack<>();
        latch = new CountDownLatch(workersNum);
        for (int i = 0; i < workersNum; i++) {
            PDXPTaskWork work = new PDXPTaskWork(String.format("index:%d", i), currentBatchTasks,
                    nextBatchTasks, latch, executeInfo);
            fixedThreadPool.submit(work);
        }
    }

    public void waitFinish() {
        try {
            while (true) {
                latch.await();
                executeInfo.finish();
                log.info("taskBatch result: batch: {}, info:{}", batchNum, executeInfo.getResult());
                if (nextBatchTasks.size() == 0) {
                    break;
                }
                batchNum++;
                executeInfo.reset();
                currentBatchTasks = nextBatchTasks;
                submit();
            }
            log.info("finish all batch tasks.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getExecuteResult() {
        return executeInfo.getResult();
    }

}