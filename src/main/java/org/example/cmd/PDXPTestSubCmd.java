package org.example.cmd;

import lombok.extern.slf4j.Slf4j;
import org.example.job.Job;
import picocli.CommandLine;

@Slf4j
@picocli.CommandLine.Command(name = "test", mixinStandardHelpOptions = true, description = "PDXP test tool.")
public class PDXPTestSubCmd implements Runnable {
    @CommandLine.Option(names = {"-n", "--number"}, description = "Define the number of tasks to be executed.")
    private int taskNums;

    @CommandLine.Option(names = {"-t", "--thread"}, description = "Define the number of threads executing tasks.")
    private int threadNums;

    @CommandLine.Option(names = {"-p", "--preference"}, description = "preference token field json string.")
    private String preferFieldsJson;


    @Override
    public void run() {
        log.info("taskNums:{}, threadNum:{}", taskNums, threadNums);
        if (taskNums > 0 && threadNums > 0) {
            Job job = new Job(threadNums, taskNums);
            job.doJobs(preferFieldsJson);
        }
    }
}
