package org.example.job;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.apiinfo.PDXPServerInfo;
import org.example.data.PDXPData;
import org.example.job.task.PDXPDataCredibleTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
public class Job {
    private int workerNums;
    private int taskNums;
    private ExecutorJob executorJob;

    public Job(int workerNums, int taskNums) {
        this.workerNums = workerNums;
        this.taskNums = taskNums;
        executorJob = new ExecutorJob(workerNums);
    }

    public void doJobs(String preferField, PDXPServerInfo pdxpServerInfo) {
        log.info(String.format("Start to generate num:%d tasks", taskNums));
        genTaskInfos(preferField, pdxpServerInfo);
        log.info(String.format("Finish generating num:%d tasks, start to do tasks", taskNums));
        doTasks();
        log.info("Wait to finish");
        waitFinish();
        log.info("All tasks finished, result: " + executorJob.getExecuteResult());
    }

    private void genTaskInfos(String preferField, PDXPServerInfo pdxpServerInfo) {
        for (int i = 0; i < taskNums; i++) {
            PDXPData pdxpData = PDXPData.genPDXPData(preferField);
            List<String> nextUrls = new ArrayList<>(Arrays.asList(pdxpServerInfo.getValidateUrl()));
            PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), pdxpServerInfo.getEvidenceUrl(),
                    nextUrls, pdxpServerInfo.getJwt(), PDXPDataCredibleTask.STEP_EVIDENCE);
            executorJob.addTask(task);
        }
    }

    private void doTasks() {
        executorJob.submit();
    }

    private void waitFinish() {
        executorJob.waitFinish();
    }
}
