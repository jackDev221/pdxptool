package org.example.job.worker;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.job.ConcurrentStack;
import org.example.job.ExecuteInfo;
import org.example.job.task.IBaseTask;
import org.example.job.task.IRequestTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@AllArgsConstructor
public class PDXPTaskWork implements IWorker {
    private String id;
    private ConcurrentStack<IBaseTask> currentBatchTasks;
    private ConcurrentStack<IBaseTask> nextBatchTasks;
    private CountDownLatch latch;
    private ExecuteInfo executeInfo;
    private OkHttpClient okHttpClient;

    @Override
    public void run() {
        if (ObjectUtil.isNull(okHttpClient)) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
        }


        while (!currentBatchTasks.isEmpty()) {
            try {
                IRequestTask iRequestTask = (IRequestTask) currentBatchTasks.pop();
                if (ObjectUtil.isNull(iRequestTask)) {
                    return;
                }
                Request request = iRequestTask.getRequest();
                Response response = okHttpClient.newCall(request).execute();
                if (iRequestTask.isResponseExpect(response)) {
                    executeInfo.getSuccess().incrementAndGet();
                } else {
                    executeInfo.getFail().incrementAndGet();
                }
                IRequestTask nextTask = iRequestTask.getNextTask(response);
                addNextTask(nextTask, currentBatchTasks, nextBatchTasks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        latch.countDown();
    }

    @Override
    public void addNextTask(IBaseTask iBaseTask, ConcurrentStack<IBaseTask> currentBatchTasks, ConcurrentStack<IBaseTask> nextBatchTasks) {
        if (ObjectUtil.isNull(iBaseTask)) {
            return;
        }
        nextBatchTasks.push(iBaseTask);
    }
}
