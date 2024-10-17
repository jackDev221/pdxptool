package org.example.job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduledJob extends BaseJob {

    public ScheduledJob(JobInfo jobInfo) {
        super(jobInfo);
    }

    @Override
    public void doJobs() {
        super.doJobs();
        log.info("finish scheduled job ");
    }

    @Override
    public String getType() {
        return Constants.JOB_TYPE_SCHEDULED;
    }
}
