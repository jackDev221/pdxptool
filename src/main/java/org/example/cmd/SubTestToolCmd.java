package org.example.cmd;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.job.BaseJob;
import org.example.job.JobFactory;
import org.example.job.info.IJobInfo;
import org.example.serialize.InterfaceAdapter;
import org.example.utils.GsonUtils;
import picocli.CommandLine;

@Slf4j
@picocli.CommandLine.Command(name = "test", mixinStandardHelpOptions = true, description = "PDXP test tool.")
public class SubTestToolCmd implements Runnable {
    @CommandLine.Option(names = {"-j", "--jobInfo"}, description = "job information in json format.")
    private String jobInfoJson;

    @Override
    public void run() {
        IJobInfo jobInfo = GsonUtils.gsonToOject(jobInfoJson, IJobInfo.class);
        if (ObjectUtil.isNotNull(jobInfo)) {
            BaseJob job = JobFactory.getJob(jobInfo);
            job.doJobs();
        }
    }
}
