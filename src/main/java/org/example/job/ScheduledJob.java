package org.example.job;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.job.task.IRequestTask;
import org.example.job.worker.BaseWork;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledJob<T> extends BaseJob {
    private ScheduledExecutorService scheduledExecutorService;
    private ITaskProvider iTaskProvider;
    private BaseWork<T> baseWork;
    private int successNum;
    private int failNum;

    public ScheduledJob(JobInfo jobInfo, BaseWork<T> baseWork, ITaskProvider iTaskProvider) {
        super(jobInfo);
        this.successNum = 0;
        this.failNum = 0;
        this.baseWork = baseWork;
        this.iTaskProvider = iTaskProvider;

    }

    @Override
    public void doJobs() {
        try {
            super.doJobs();
            if (ObjectUtil.isNull(scheduledExecutorService)) {
                scheduledExecutorService = Executors.newScheduledThreadPool(1);
            }
            if (ObjectUtil.isNull(baseWork)) {
                log.error("baseWork is Null");
                return;
            }
            if (ObjectUtil.isNull(iTaskProvider)) {
                log.error("taskProvider is Null");
                return;
            }
            scheduledExecutorService.scheduleAtFixedRate(
                    () -> {
                        List<IRequestTask> requestTasks = iTaskProvider.genTaskList(jobInfo.getTaskNumPeriod());
                        for (IRequestTask requestTask : requestTasks) {
                            try {
                                T result = baseWork.handleTask(requestTask);
                                boolean isExpect = baseWork.isResultExpect(result);
                                if (isExpect) {
                                    baseWork.getExecuteInfo().getSuccess().incrementAndGet();
                                } else {
                                    baseWork.getExecuteInfo().getFail().incrementAndGet();
                                }
                                baseWork.handleTaskResult(result);
                                log.info("scheduled job execute detail:{}.", baseWork.getExecuteInfo());
                            } catch (IOException e) {
                                baseWork.getExecuteInfo().getFail().incrementAndGet();
                                e.printStackTrace();
                            }
                            if (baseWork.getExecuteInfo().isFinished()) {
                                baseWork.getLatch().countDown();
                            }
                        }
                    },
                    jobInfo.getInitialDelay(), jobInfo.getPeriod(), TimeUnit.SECONDS);
            baseWork.getLatch().await();
            log.info("finish scheduled job ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return Constants.JOB_TYPE_SCHEDULED;
    }

    interface ITaskProvider {
        List<IRequestTask> genTaskList(int num);
    }
}
