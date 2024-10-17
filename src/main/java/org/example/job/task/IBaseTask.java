package org.example.job.task;

import java.io.Serializable;

public interface IBaseTask extends Serializable {

    IBaseTask getNextTask(boolean currentTaskResult);

    String getTaskType();

}
