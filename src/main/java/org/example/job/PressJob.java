package org.example.job;

import lombok.extern.slf4j.Slf4j;
import org.example.data.PDXPData;
import org.example.job.task.PDXPDataCredibleTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
//@Data
public class PressJob extends BaseJob {
    private PressJobExecutor pressJobExecutor;

    public PressJob(JobInfo jobInfo) {
        super(jobInfo);
        pressJobExecutor = new PressJobExecutor(jobInfo.getWorkerNum(), jobInfo.getBatchInterval());
    }

    private void genTaskInfos() {
        log.info(String.format("Start to generate num:%d tasks", jobInfo.getTaskNum()));
        for (int i = 0; i < jobInfo.getTaskNum(); i++) {
            PDXPData pdxpData = PDXPData.genPDXPData(jobInfo.getPreferField());
            List<String> nextUrls = new ArrayList<>(Arrays.asList(jobInfo.getPdxpServerInfo().getValidateUrl()));
            PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), jobInfo.getPdxpServerInfo().getEvidenceUrl(),
                    nextUrls, jobInfo.getPdxpServerInfo().getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
            pressJobExecutor.addTask(task);
        }
    }

    private void beforeTasks() {
        log.info("PressJob beforeTasks");
        PDXPData pdxpData = PDXPData.genPDXPData(jobInfo.getPreferField());
        List<String> nextUrls = new ArrayList<>(Arrays.asList(jobInfo.getPdxpServerInfo().getValidateUrl()));
        PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), jobInfo.getPdxpServerInfo().getEvidenceUrl(),
                nextUrls, jobInfo.getPdxpServerInfo().getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
        pressJobExecutor.beforeExecutor(task);
    }

    private void doTasks() {
        log.info(String.format("Finish generating num:%d tasks, start to do tasks", jobInfo.getTaskNum()));
        pressJobExecutor.submit();
    }

    private void endTasks() {
        log.info("PressJob end Task");
        pressJobExecutor.endExecutor();
    }

    private void waitFinish() {
        log.info("wait to finish");
        pressJobExecutor.waitFinish();
        log.info("all tasks finish result :{}",pressJobExecutor.getExecuteResult());

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
        return Constants.JOB_TYPE_PRESS;
    }
}
