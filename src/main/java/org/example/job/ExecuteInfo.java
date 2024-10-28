package org.example.job;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ExecuteInfo {
    private int taskNum;
    private long startTimeStamp;
    private long endTimeStamp;
    private AtomicInteger success = new AtomicInteger(0);
    private AtomicInteger fail = new AtomicInteger(0);
    private long cast;

    public ExecuteInfo() {
        init();
    }

    public void init() {
        startTimeStamp = 0;
        endTimeStamp = 0;
        success = new AtomicInteger(0);
        fail = new AtomicInteger(0);
    }

    public void reset() {
        init();
    }

    public void start() {
        startTimeStamp = System.currentTimeMillis();
    }

    public void finish() {
        endTimeStamp = System.currentTimeMillis();
    }

    public String getResult() {
        return String.format("TaskNum:%d, success:%d, fail:%d, real time cast:%d ms, detail: taskStart %d: taskFinish %d",
                taskNum, success.get(), fail.get(), getRealCast(), startTimeStamp, endTimeStamp);
    }

    public long getRealCast() {
        if (cast == 0) {
            cast = endTimeStamp - startTimeStamp;
        }
        return cast;
    }

    public boolean isFinished() {
        return (success.get() + fail.get()) >= taskNum;
    }

    public void updateByExecuteInfo(ExecuteInfo updateInfo) {
        if (startTimeStamp == 0) {
            startTimeStamp = updateInfo.getStartTimeStamp();
        }
        endTimeStamp = updateInfo.getEndTimeStamp();
        taskNum += updateInfo.getTaskNum();
        cast += updateInfo.getCast();
    }

}
