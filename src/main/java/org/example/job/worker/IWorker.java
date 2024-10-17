package org.example.job.worker;

import org.example.job.ConcurrentStack;
import org.example.job.task.IBaseTask;

public interface IWorker extends Runnable {
    void addNextTask(IBaseTask iBaseTask,  ConcurrentStack<IBaseTask> currentBatchTasks, ConcurrentStack<IBaseTask> nextBatchTasks);
}
