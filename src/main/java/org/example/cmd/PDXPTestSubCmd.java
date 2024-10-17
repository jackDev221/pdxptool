package org.example.cmd;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.apiinfo.PDXPServerInfo;
import org.example.job.BaseJob;
import org.example.job.JobFactory;
import org.example.job.JobInfo;
import picocli.CommandLine;

@Slf4j
@picocli.CommandLine.Command(name = "test", mixinStandardHelpOptions = true, description = "PDXP test tool.")
public class PDXPTestSubCmd implements Runnable {
    @CommandLine.Option(names = {"-j", "--jobInfo"}, description = "job information in json format.")
    private String jobInfoJson;

    @Override
    public void run() {
        JobInfo jobInfo = JobInfo.decodeFromJsonStr(jobInfoJson);
        if (ObjectUtil.isNotNull(jobInfo)) {
            BaseJob job = JobFactory.getJob(jobInfo);
            job.doJobs();
        }
    }
}
