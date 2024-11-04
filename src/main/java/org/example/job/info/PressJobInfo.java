package org.example.job.info;

import lombok.Data;

@Data
public abstract class PressJobInfo implements IJobInfo {
    protected String type;
    protected int taskNum;
    protected int workerNum;
    protected long batchInterval;
}
