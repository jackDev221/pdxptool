package org.example.job.worker;

import org.example.job.ConcurrentStack;
import org.example.job.TaskInfo;
import org.example.job.task.IBaseTask;
import org.example.job.task.IRequestTask;

public interface IWorker extends Runnable {
    void addNextTask(IBaseTask iBaseTask, ConcurrentStack<IBaseTask> currentBatchTasks, ConcurrentStack<IBaseTask> nextBatchTasks);
}
