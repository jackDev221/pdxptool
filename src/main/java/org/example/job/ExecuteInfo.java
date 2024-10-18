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
        return String.format("TaskNum:%d, success:%d, fail:%d, time cast:%d ms, detail: %d:%d",
                taskNum, success.get(), fail.get(), endTimeStamp - startTimeStamp, startTimeStamp, endTimeStamp);
    }

    public boolean isFinished() {
        return (success.get() + fail.get()) >= taskNum;
    }

}
