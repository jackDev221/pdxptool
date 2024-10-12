package org.example.job;

import lombok.Data;
import org.example.data.PDXPData;

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
        System.out.println(String.format("Start to generate num:%d tasks", taskNums));
        genTaskInfos(preferField);
        System.out.println(String.format("Finish generating num:%d tasks, start to do tasks", taskNums));
        doTasks();
        System.out.println("Wait to finish");
        waitFinish();
        System.out.println("All tasks finished");
    }

    private void genTaskInfos(String preferField) {
        for (int i = 0; i < taskNums; i++) {
            PDXPData pdxpData = PDXPData.genPDXPData(preferField);
            executorJob.addTask(new TaskInfo(pdxpData.toBase64Str()));
        }
    }

    private void doTasks() {
        executorJob.submit();
    }

    private void waitFinish() {
        executorJob.waitFinish();
    }
}
