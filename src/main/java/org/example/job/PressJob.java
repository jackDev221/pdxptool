package org.example.job;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.example.job.info.PressJobInfo;


@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class PressJob extends BaseJob {
    private PressJobInfo pressJobInfo;
    private PressJobExecutor pressJobExecutor;

    public PressJob(PressJobInfo pressJobInfo) {
        this.pressJobInfo = pressJobInfo;
        pressJobExecutor = new PressJobExecutor(pressJobInfo.getWorkerNum(), pressJobInfo.getBatchInterval());
    }

    private void genTaskInfos() {
        log.info(String.format("Start to generate num:%d tasks", pressJobInfo.getTaskNum()));
        for (int i = 0; i < pressJobInfo.getTaskNum(); i++) {
            pressJobExecutor.addTask(pressJobInfo.getIBaseTask());
        }
    }

    private void beforeTasks() {
        log.info("PressJob beforeTasks");
        pressJobExecutor.beforeExecutor(pressJobInfo.getIBaseTask());
    }

    private void doTasks() {
        log.info(String.format("Finish generating num:%d tasks, start to do tasks", pressJobInfo.getTaskNum()));
        pressJobExecutor.submit();
    }

    private void endTasks() {
        log.info("PressJob end Task");
        pressJobExecutor.endExecutor();
    }

    private void waitFinish() {
        log.info("wait to finish");
        pressJobExecutor.waitFinish();
        log.info("all tasks finish result :{}", pressJobExecutor.getExecuteResult());

    }

    @Override
    public void doJobs() {
        super.doJobs();
        genTaskInfos();
        beforeTasks();
        doTasks();
        waitFinish();
        endTasks();
    }

    @Override
    public String getType() {
        return Constants.JOB_TYPE_PRESS_PDXP;
    }
}
