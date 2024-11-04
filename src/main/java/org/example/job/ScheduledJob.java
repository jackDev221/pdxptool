package org.example.job;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.example.job.info.ScheduleJobInfo;
import org.example.job.info.SchedulePDXPJobInfo;
import org.example.job.task.IBaseTask;
import org.example.job.worker.BaseWork;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class ScheduledJob<T> extends BaseJob {
    private ScheduleJobInfo scheduleJobInfo;
    private ScheduledExecutorService scheduledExecutorService;
    private ITaskProvider iTaskProvider;
    private BaseWork<T> baseWork;
    private int successNum;
    private int failNum;

    public ScheduledJob(SchedulePDXPJobInfo scheduleJobInfo, BaseWork<T> baseWork, ITaskProvider iTaskProvider) {
        this.scheduleJobInfo = scheduleJobInfo;
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
                        List<IBaseTask> requestTasks = iTaskProvider.genTaskList(scheduleJobInfo.getTaskNumPeriod());
                        for (IBaseTask requestTask : requestTasks) {
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
                                return;
                            }
                        }
                    },
                    scheduleJobInfo.getInitialDelay(), scheduleJobInfo.getPeriod(), TimeUnit.SECONDS);
            baseWork.getLatch().await();
            log.info("finish scheduled job ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return Constants.JOB_TYPE_SCHEDULED_PDXP;
    }

    interface ITaskProvider {
        List<IBaseTask> genTaskList(int num);
    }
}
