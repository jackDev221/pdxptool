package org.example.job;

import lombok.extern.slf4j.Slf4j;
import org.example.apiinfo.PDXPServerInfo;
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

    private void genTaskInfos(int taskNums, String preferField, PDXPServerInfo pdxpServerInfo) {
        for (int i = 0; i < taskNums; i++) {
            PDXPData pdxpData = PDXPData.genPDXPData(preferField);
            List<String> nextUrls = new ArrayList<>(Arrays.asList(pdxpServerInfo.getValidateUrl()));
            PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), pdxpServerInfo.getEvidenceUrl(),
                    nextUrls, pdxpServerInfo.getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
            pressJobExecutor.addTask(task);
        }
    }

    private void beforeTasks(){

    }
    private void doTasks() {
        pressJobExecutor.submit();
    }

    private void endTasks(){

    }

    private void waitFinish() {
        pressJobExecutor.waitFinish();
    }

    @Override
    public void doJobs() {
        super.doJobs();
        log.info(String.format("Start to generate num:%d tasks", jobInfo.getTaskNum()));
        genTaskInfos(jobInfo.getTaskNum(), jobInfo.getPreferField(), jobInfo.getPdxpServerInfo());
        log.info(String.format("Finish generating num:%d tasks, start to do tasks", jobInfo.getTaskNum()));

        doTasks();
        log.info("Wait to finish");
        waitFinish();
        log.info("All tasks finished, result: " + pressJobExecutor.getExecuteResult());
    }

    @Override
    public String getType() {
        return Constants.JOB_TYPE_PRESS;
    }
}
