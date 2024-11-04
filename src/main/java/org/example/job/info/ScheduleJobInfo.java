package org.example.job.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class ScheduleJobInfo extends PressJobInfo {
    protected int initialDelay;
    protected int period;
    protected int taskNumPeriod;
}
