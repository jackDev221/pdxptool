package org.example.job.task;

import okhttp3.Request;
import okhttp3.Response;

public interface IRequestTask extends IBaseTask {
    Request getRequest();

    boolean isResponseExpect(Response response);

    IRequestTask getNextTask(Response currenResponse);

    void closeResponse(Response response);

    int getStep();
}