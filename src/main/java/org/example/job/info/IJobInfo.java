package org.example.job.info;

import org.example.job.task.IBaseTask;

public interface IJobInfo {
    String getType();
    boolean isValidate();

    IBaseTask getIBaseTask();
}
