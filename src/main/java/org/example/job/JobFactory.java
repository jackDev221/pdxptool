package org.example.job;

public class JobFactory {
    public static BaseJob getJob(JobInfo jobInfo) {
        BaseJob res = null;
        switch (jobInfo.getType()) {
            case Constants.JOB_TYPE_PRESS:
                res = new PressJob(jobInfo);
                break;
            case Constants.JOB_TYPE_SCHEDULED:
                res = new ScheduledJob(jobInfo);
                break;
            default:
                break;
        }
        return res;

    }
}
