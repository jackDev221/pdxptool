package org.example.cmd;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.example.apiinfo.PDXPServerInfo;
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

    @CommandLine.Option(names = {"-s", "--serverInfo"}, description = "request server info(url, jwt...).")
    private String serverInfoStr;


    @Override
    public void run() {
        log.info("taskNums:{}, threadNum:{},  serverInfoStr:{}", taskNums, threadNums, serverInfoStr);
        PDXPServerInfo serverInfo = PDXPServerInfo.decodeFromStr(serverInfoStr);
        if (taskNums > 0 && threadNums > 0 && ObjectUtil.isNotNull(serverInfo)) {
            Job job = new Job(threadNums, taskNums);
            job.doJobs(preferFieldsJson, serverInfo);
        }
    }
}
