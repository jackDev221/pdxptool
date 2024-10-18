package org.example.job.worker;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.example.job.ConcurrentStack;
import org.example.job.ExecuteInfo;
import org.example.job.task.IBaseTask;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Data
@AllArgsConstructor
public abstract class BaseWork<T> implements IWorker {
    private String id;
    private ConcurrentStack<IBaseTask> currentBatchTasks;
    private ConcurrentStack<IBaseTask> nextBatchTasks;
    private CountDownLatch latch;
    private ExecuteInfo executeInfo;


    public abstract T handleTask(IBaseTask iBaseTask) throws IOException;

    public abstract boolean isResultExpect(T result);

    public abstract void handleTaskResult(T result);

    @Override
    public void addNextTask(IBaseTask iBaseTask, ConcurrentStack<IBaseTask> currentBatchTasks, ConcurrentStack<IBaseTask> nextBatchTasks) {
        return;
    }

    @Override
    public void run() {
        while (!currentBatchTasks.isEmpty()) {
            try {
                IBaseTask iBaseTask = currentBatchTasks.pop();
                if (ObjectUtil.isNull(iBaseTask)) {
                    return;
                }
                T result = handleTask(iBaseTask);
                boolean isExpect = isResultExpect(result);
                IBaseTask nextTask = iBaseTask.getNextTask(isExpect);
                if (isExpect) {
                    executeInfo.getSuccess().incrementAndGet();
                } else {
                    executeInfo.getFail().incrementAndGet();
                }
                addNextTask(nextTask, currentBatchTasks, nextBatchTasks);
                handleTaskResult(result);
            } catch (Exception e) {
                executeInfo.getFail().incrementAndGet();
                e.printStackTrace();
            }
        }
        if (ObjectUtil.isNotNull(latch)) {
            latch.countDown();
        }
    }
}
