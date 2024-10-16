package org.example.job;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    public void doJobs(String preferField) {
        log.info(String.format("Start to generate num:%d tasks", taskNums));
        genTaskInfos(preferField);
        log.info(String.format("Finish generating num:%d tasks, start to do tasks", taskNums));
        doTasks();
        log.info("Wait to finish");
        waitFinish();
        log.info("All tasks finished, result: " + executorJob.getExecuteResult());
    }

    private void genTaskInfos(String preferField) {
        for (int i = 0; i < taskNums; i++) {
            PDXPData pdxpData = PDXPData.genPDXPData(preferField);
            List<String> nextUrls = new ArrayList<>(Arrays.asList("http://127.0.0.1:8080/api/app/orderValidate"));
            PDXPDataCredibleTask task = new PDXPDataCredibleTask(pdxpData.toBase64Str(), "http://127.0.0.1:8080/api/app/orderEvidence",
                    nextUrls, "jwt:tesstttt", PDXPDataCredibleTask.STEP_EVIDENCE);
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
