package org.example.job.worker;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.job.ConcurrentStack;
import org.example.job.ExecuteInfo;
import org.example.job.task.Constants;
import org.example.job.task.IBaseTask;
import org.example.job.task.IRequestTask;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PDXPTaskWork extends BaseWork<Response> {
    private OkHttpClient okHttpClient;

    public PDXPTaskWork(String id, ConcurrentStack currentBatchTasks, ConcurrentStack nextBatchTasks,
                        CountDownLatch latch, ExecuteInfo executeInfo) {
        super(id, currentBatchTasks, nextBatchTasks, latch, executeInfo);
    }


    @Override
    public Response handleTask(IBaseTask iBaseTask) throws IOException {
        if (ObjectUtil.isNull(okHttpClient)) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
        }
        if (!iBaseTask.getTaskType().equals(Constants.TASK_TYPE_HTTP_REQUEST)) {
            log.warn("{} handle a task with wrong type, expect:{}, act:{}", getId(), iBaseTask.getTaskType(), Constants.TASK_TYPE_HTTP_REQUEST);
            return null;
        }
        IRequestTask iRequestTask = (IRequestTask) iBaseTask;
        Request request = iRequestTask.getRequest();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    @Override
    public boolean isResultExpect(Response result) {
        return result.code() == 200;
    }

    @Override
    public void handleTaskResult(Response result) {
        result.close();
    }

    @Override
    public void addNextTask(IBaseTask iBaseTask, ConcurrentStack<IBaseTask> currentBatchTasks, ConcurrentStack<IBaseTask> nextBatchTasks) {
        if (ObjectUtil.isNull(iBaseTask)) {
            return;
        }
        nextBatchTasks.push(iBaseTask);
    }
}
